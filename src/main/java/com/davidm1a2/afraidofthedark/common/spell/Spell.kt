package com.davidm1a2.afraidofthedark.common.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncParticle
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
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint
import java.util.*

/**
 * Class representing a spell instance created by a player
 *
 * @property name The spell's name, can't be null (empty by default)
 * @property id The spell's universally unique identifier, cannot be null
 * @property ownerId The spell's owner's persistent entity id, cannot be null
 * @property powerSource The source that is powering the spell, can be null
 * @property spellStages The list of spell stages this spell can go through, can have 0 - inf elements
 */
class Spell : INBTSerializable<NBTTagCompound> {
    lateinit var name: String
    lateinit var id: UUID
        private set
    private lateinit var ownerId: UUID
    var powerSource: SpellComponentInstance<SpellPowerSource>? = null
    val spellStages = mutableListOf<SpellStage>()

    /**
     * Constructor that takes the player that created the spell in as a parameter
     *
     * @param entity The entity that owns the spell/made the spell
     */
    constructor(entity: Entity) {
        // Assign a random spell ID
        id = UUID.randomUUID()
        // Assign the owner id to the player's id
        ownerId = entity.persistentID
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
     * Called to cast the spell, notifies the player if something is wrong so the spell won't cast
     *
     * @param entityPlayer The player casting the spell
     */
    fun attemptToCast(entityPlayer: EntityPlayer) {
        // Server side processing only
        if (!entityPlayer.world.isRemote) {
            // Make sure the player isn't in the nightmare realm
            if (entityPlayer.dimension != ModDimensions.NIGHTMARE.id) {
                // If the spell is valid continue, if not print an error
                if (isValid()) {
                    // Test if the spell can be cast, if not tell the player why
                    if (powerSource!!.component.canCast(entityPlayer, this)) {
                        // Consumer the power to cast the spell
                        powerSource!!.component.consumePowerToCast(entityPlayer, this)

                        // Play a cast sound
                        entityPlayer.world.playSound(
                            null,
                            entityPlayer.position,
                            ModSounds.SPELL_CAST,
                            SoundCategory.PLAYERS,
                            1.0f,
                            (0.8f + Math.random() * 0.4).toFloat()
                        )
                        // Spawn 3-5 particles
                        val positions: MutableList<Vec3d> = ArrayList()
                        for (i in 0 until entityPlayer.rng.nextInt(4) + 2) {
                            positions.add(Vec3d(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ))
                        }
                        // Send the particle packet
                        AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                            SyncParticle(
                                AOTDParticleRegistry.ParticleTypes.SPELL_CAST_ID,
                                positions,
                                List<Vec3d>(positions.size) { Vec3d.ZERO }
                            ),
                            TargetPoint(
                                entityPlayer.dimension,
                                entityPlayer.posX,
                                entityPlayer.posY,
                                entityPlayer.posZ,
                                100.0
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
                                    .withEntity(entityPlayer)
                                    .build()
                            )
                    } else {
                        entityPlayer.sendMessage(TextComponentTranslation(powerSource!!.component.getUnlocalizedOutOfPowerMsg()))
                    }
                } else {
                    entityPlayer.sendMessage(TextComponentTranslation("message.afraidofthedark:spell.invalid"))
                }
            } else {
                entityPlayer.sendMessage(TextComponentTranslation("message.afraidofthedark:spell.wrong_dimension"))
            }
        }
    }

    /**
     * Returns true if this spell is valid, false otherwise
     *
     * @return True if the power source method is non-null and at least one spell stage is registered
     */
    fun isValid(): Boolean {
        val isValid = powerSource != null
        // Ensure the power source is valid and the spell stages are non-empty
        return if (isValid && spellStages.isNotEmpty()) {
            // Test to ensure all spell stages are valid
            spellStages.all { it.isValid() }
        } else false
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
     * Gets the owner of the spell
     *
     * @return The entity that owns the spell, or null if the entity doesn't exist
     */
    fun getOwner(): Entity? {
        return FMLCommonHandler.instance().minecraftServerInstance.getEntityFromUuid(ownerId)
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
        nbt.setTag(NBT_ID, NBTUtil.createUUIDTag(id))
        nbt.setTag(NBT_OWNER_ID, NBTUtil.createUUIDTag(ownerId))

        // The spell power source can be null, double check that it isn't before writing it and its state
        if (powerSource != null) {
            nbt.setTag(NBT_POWER_SOURCE, powerSource!!.serializeNBT())
        }

        // Write each spell stage to NBT
        val spellStagesNBT = NBTTagList()
        spellStages.forEach { spellStagesNBT.appendTag(it.serializeNBT()) }
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
        id = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_ID))
        ownerId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_OWNER_ID))

        // The spell power source can be null, double check that it exists before reading it and its state
        if (nbt.hasKey(NBT_POWER_SOURCE)) {
            // Grab the power source NBT and create a power source out of it
            powerSource = SpellPowerSourceInstance.createFromNBT(nbt.getCompoundTag(NBT_POWER_SOURCE))
        }

        // Read each spell stage from NBT
        val spellStagesNBT = nbt.getTagList(NBT_SPELL_STAGES, Constants.NBT.TAG_COMPOUND)
        spellStages.clear()
        for (i in 0 until spellStagesNBT.tagCount()) {
            // Grab the spell stage NBT, read it into the spell stage, and add it
            val spellStageNBT = spellStagesNBT.getCompoundTagAt(i)
            val spellStage = SpellStage(spellStageNBT)
            spellStages.add(spellStage)
        }
    }

    companion object {
        // Constants used for NBT serialization/deserialiation
        private const val NBT_NAME = "name"
        private const val NBT_ID = "id"
        private const val NBT_OWNER_ID = "owner_id"
        private const val NBT_POWER_SOURCE = "power_source"
        private const val NBT_SPELL_STAGES = "spell_stages"
    }
}