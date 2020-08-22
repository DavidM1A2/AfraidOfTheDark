package com.davidm1a2.afraidofthedark.common.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceInstance
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.fml.network.PacketDistributor
import org.apache.logging.log4j.LogManager
import java.util.*
import kotlin.random.Random

/**
 * Class representing a spell instance created by a player
 *
 * @property name The spell's name, can't be null (empty by default)
 * @property id The spell's universally unique identifier, cannot be null
 * @property powerSource The source that is powering the spell, can be null
 * @property spellStages The list of spell stages this spell can go through, can have 0 - inf elements
 */
class Spell : INBTSerializable<NBTTagCompound> {
    lateinit var name: String
    lateinit var id: UUID
        private set
    var powerSource: SpellComponentInstance<SpellPowerSource>? = null
    val spellStages = mutableListOf<SpellStage>()

    /**
     * Constructor that takes the player that created the spell in as a parameter
     */
    constructor() {
        // Assign a random spell ID
        id = UUID.randomUUID()
        // Empty spell name is default
        name = ""
    }

    /**
     * Constructor that takes in an NBT compound and creates the spell from NBT
     *
     * @param nbt The NBT containing the spell's information
     */
    constructor(nbt: NBTTagCompound) {
        deserializeNBT(nbt)
    }

    /**
     * Called to cast the spell, notifies the a player if something is wrong so the spell won't cast
     *
     * @param entity The entity casting the spell
     * @param direction The direction the spell should be casted, defaults to the look vec
     */
    fun attemptToCast(entity: Entity, direction: Vec3d = entity.lookVec) {
        // Server side processing only
        if (!entity.world.isRemote) {
            // Make sure the player isn't in the nightmare realm
            if (entity.dimension != ModDimensions.NIGHTMARE_TYPE) {
                // If the spell is valid continue, if not print an error
                if (isValid()) {
                    // Test if the spell can be cast, if not tell the player why
                    if (powerSource!!.component.canCast(entity, this)) {
                        // Consumer the power to cast the spell
                        powerSource!!.component.consumePowerToCast(entity, this)

                        // Play a cast sound
                        entity.world.playSound(
                            null,
                            entity.position,
                            ModSounds.SPELL_CAST,
                            SoundCategory.PLAYERS,
                            1.0f,
                            (0.8f + Math.random() * 0.4).toFloat()
                        )
                        // Spawn 3-5 particles
                        val positions: MutableList<Vec3d> = ArrayList()
                        for (i in 0 until Random.nextInt(2, 6)) {
                            positions.add(Vec3d(entity.posX, entity.posY, entity.posZ))
                        }
                        // Send the particle packet
                        AfraidOfTheDark.packetHandler.sendToAllAround(
                            ParticlePacket(
                                ModParticles.SPELL_CAST,
                                positions,
                                List<Vec3d>(positions.size) { Vec3d.ZERO }
                            ),
                            PacketDistributor.TargetPoint(
                                entity.posX,
                                entity.posY,
                                entity.posZ,
                                100.0,
                                entity.dimension
                            )
                        )

                        // Tell the first delivery method to fire
                        getStage(0)!!
                            .deliveryInstance!!
                            .component
                            .executeDelivery(
                                DeliveryTransitionStateBuilder()
                                    .withSpell(this)
                                    .withStageIndex(0)
                                    .withCasterEntity(entity)
                                    .withEntity(entity)
                                    .withDirection(direction)
                                    .build()
                            )
                    } else {
                        entity.sendMessage(TextComponentTranslation(powerSource!!.component.getUnlocalizedOutOfPowerMsg()))
                        if (entity !is EntityPlayer) {
                            logger.info("Entity '${entity.name}' attempted to cast a spell without enough power?")
                        }
                    }
                } else {
                    entity.sendMessage(TextComponentTranslation("message.afraidofthedark.spell.invalid"))
                    if (entity !is EntityPlayer) {
                        logger.info("Entity '${entity.name}' attempted to cast an invalid spell?")
                    }
                }
            } else {
                entity.sendMessage(TextComponentTranslation("message.afraidofthedark.spell.wrong_dimension"))
                if (entity !is EntityPlayer) {
                    logger.info("Entity '${entity.name}' attempted to cast a spell in the nightmare?")
                }
            }
        }
    }

    /**
     * Returns true if this spell is valid, false otherwise
     *
     * @return True if the power source method is non-null and at least one spell stage is registered
     */
    private fun isValid(): Boolean {
        // Ensure the power source is valid and the spell stages are non-empty, and all spell stages are valid
        return powerSource != null && spellStages.isNotEmpty() && spellStages.all { it.isValid() }
    }

    /**
     * Gets the cost of the spell
     *
     * @return The cost of the spell including all spell stages
     */
    fun getCost(): Double {
        var cost = 0.0

        // Keep a multiplier that will make each spell stage more and more expensive
        var costMultiplier = 1.0

        // Go over each spell stage and add up costs
        for (spellStage in spellStages) {
            // Add the cost of the stage times the multiplier
            cost = cost + spellStage.getCost() * costMultiplier
            // Increase the cost of the next spell stage by 5% by default
            costMultiplier = costMultiplier + 0.05
        }

        // If cost overflowed then set it to max double
        if (cost < 0) {
            cost = Double.MAX_VALUE
        }
        return cost
    }

    /**
     * True if the spell has a given stage, false otherwise
     *
     * @param index The index of the stage to get
     * @return True if the stage exists, false otherwise
     */
    fun hasStage(index: Int): Boolean {
        return index >= 0 && index < spellStages.size
    }

    /**
     * Gets the spell stage at a given index
     *
     * @param index The spell stage index
     * @return The spell stage at a given index or null if it doesn't exist
     */
    fun getStage(index: Int): SpellStage? {
        return if (hasStage(index)) {
            spellStages[index]
        } else {
            null
        }
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell's data
     */
    override fun serializeNBT(): NBTTagCompound {
        val nbt = NBTTagCompound()

        // Write each field to NBT
        nbt.setString(NBT_NAME, name)
        nbt.setTag(NBT_ID, NBTUtil.writeUniqueId(id))

        // The spell power source can be null, double check that it isn't before writing it and its state
        powerSource?.let { nbt.setTag(NBT_POWER_SOURCE, it.serializeNBT()) }

        // Write each spell stage to NBT
        val spellStagesNBT = NBTTagList()
        spellStages.forEach { spellStagesNBT.add(it.serializeNBT()) }
        nbt.setTag(NBT_SPELL_STAGES, spellStagesNBT)
        return nbt
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    override fun deserializeNBT(nbt: NBTTagCompound) {
        // Read each field from NBT
        name = nbt.getString(NBT_NAME)
        id = NBTUtil.readUniqueId(nbt.getCompound(NBT_ID))

        // The spell power source can be null, double check that it exists before reading it and its state
        if (nbt.hasKey(NBT_POWER_SOURCE)) {
            // Grab the power source NBT and create a power source out of it
            powerSource = SpellPowerSourceInstance.createFromNBT(nbt.getCompound(NBT_POWER_SOURCE))
        }

        // Read each spell stage from NBT
        val spellStagesNBT = nbt.getList(NBT_SPELL_STAGES, Constants.NBT.TAG_COMPOUND)
        spellStages.clear()
        for (i in 0 until spellStagesNBT.size) {
            // Grab the spell stage NBT, read it into the spell stage, and add it
            val spellStageNBT = spellStagesNBT.getCompound(i)
            val spellStage = SpellStage(spellStageNBT)
            spellStages.add(spellStage)
        }
    }

    /**
     * @return a pretty printed spell
     */
    override fun toString(): String {
        return "Spell $name\n" +
                "Power Source: ${powerSource?.component?.registryName?.path}\n" +
                "Spell Stages:\n" +
                spellStages.joinToString(separator = "\n") { " -> $it" }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Constants used for NBT serialization/deserialiation
        private const val NBT_NAME = "name"
        private const val NBT_ID = "id"
        private const val NBT_POWER_SOURCE = "power_source"
        private const val NBT_SPELL_STAGES = "spell_stages"
    }
}