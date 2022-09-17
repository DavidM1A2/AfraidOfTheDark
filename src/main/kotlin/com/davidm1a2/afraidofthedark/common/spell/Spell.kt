package com.davidm1a2.afraidofthedark.common.spell

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.event.custom.CastSpellEvent
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.utility.getLookNormal
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.particles.IParticleData
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.fml.network.PacketDistributor
import org.apache.logging.log4j.LogManager
import kotlin.random.Random

/**
 * Class representing a spell instance created by a player
 *
 * @property name The spell's name, can't be null (empty by default)
 * @property spellStages The list of spell stages this spell can go through, can have 0 - inf elements
 */
class Spell() : INBTSerializable<CompoundNBT> {
    // Empty spell name is default
    var name: String = ""
    val spellStages = mutableListOf<SpellStage>()

    /**
     * Constructor that takes in an NBT compound and creates the spell from NBT
     *
     * @param nbt The NBT containing the spell's information
     */
    constructor(nbt: CompoundNBT) : this() {
        deserializeNBT(nbt)
    }

    /**
     * Called to cast the spell, notifies the a player if something is wrong so the spell won't cast
     *
     * @param entity The entity casting the spell
     * @param direction The direction the spell should be casted, defaults to the look vec
     * @param isSpellScroll True if the cast is coming from a spell scroll
     */
    fun attemptToCast(entity: Entity, direction: Vector3d = entity.lookAngle, isSpellScroll: Boolean = false) {
        // Server side processing only
        if (!entity.level.isClientSide) {
            // Make sure the player isn't in the nightmare realm
            if (entity.level.dimension() != ModDimensions.NIGHTMARE_WORLD) {
                // If the spell is valid continue, if not print an error
                if (isValid()) {
                    // Determine the selected power source
                    val selectedPowerSource = if (entity is PlayerEntity && !isSpellScroll) {
                        entity.getBasics().selectedPowerSource
                    } else {
                        ModSpellPowerSources.SPELL_SCROLL
                    }
                    // Try casting the spell
                    val castResult = selectedPowerSource.cast(entity, this)

                    // If we failed for some reason, let the entity know
                    if (!castResult.wasSuccessful()) {
                        entity.sendMessage(castResult.failureMessage!!)
                        if (entity !is PlayerEntity) {
                            logger.info("Entity '${entity.name}' attempted to cast a spell without enough power?")
                        }
                        return
                    }

                    // Send an event for the spell being cast
                    MinecraftForge.EVENT_BUS.post(CastSpellEvent(entity, this, selectedPowerSource))

                    // Play a cast sound
                    entity.level.playSound(
                        null,
                        entity.blockPosition(),
                        ModSounds.SPELL_CAST,
                        SoundCategory.PLAYERS,
                        1.0f,
                        (0.8f + Math.random() * 0.4).toFloat()
                    )

                    // Determine the particles from the strength of the spell
                    val spellPower = this.getCost()
                    var spellParticle: IParticleData = ModParticles.SPELL_CAST
                    if (spellPower > SPELL_TIER2_CUTOFF) spellParticle = ModParticles.SPELL_CAST2
                    if (spellPower > SPELL_TIER3_CUTOFF) spellParticle = ModParticles.SPELL_CAST3
                    // Spawn 3-5 particles
                    val positions: MutableList<Vector3d> = ArrayList()
                    for (i in 0 until Random.nextInt(2, 6)) {
                        positions.add(Vector3d(entity.x, entity.y, entity.z))
                    }

                    // Send the particle packet
                    AfraidOfTheDark.packetHandler.sendToAllAround(
                        ParticlePacket.builder()
                            .particle(spellParticle)
                            .positions(positions)
                            .speed(Vector3d.ZERO)
                            .build(),
                        PacketDistributor.TargetPoint(
                            entity.x,
                            entity.y,
                            entity.z,
                            100.0,
                            entity.level.dimension()
                        )
                    )

                    val position = entity.getEyePosition(1.0f)
                    // Tell the first delivery method to fire
                    getStage(0)!!
                        .deliveryInstance!!
                        .component
                        .executeDelivery(
                            DeliveryTransitionState(
                                spell = this,
                                stageIndex = 0,
                                world = entity.level,
                                position = position,
                                blockPosition = BlockPos(position),
                                direction = direction,
                                normal = entity.getLookNormal(),
                                entity = entity,
                                casterEntity = entity
                            )
                        )
                } else {
                    entity.sendMessage(TranslationTextComponent("message.afraidofthedark.spell.invalid"))
                    if (entity !is PlayerEntity) {
                        logger.info("Entity '${entity.name}' attempted to cast an invalid spell?")
                    }
                }
            } else {
                entity.sendMessage(TranslationTextComponent("message.afraidofthedark.spell.wrong_dimension"))
                if (entity !is PlayerEntity) {
                    logger.info("Entity '${entity.name}' attempted to cast a spell in the nightmare?")
                }
            }
        }
    }

    /**
     * Returns true if this spell is valid, false otherwise
     *
     * @return True if the at least one spell stage is registered
     */
    fun isValid(): Boolean {
        // Ensure the spell stages are non-empty and all spell stages are valid
        return spellStages.isNotEmpty() && spellStages.all { it.isValid() }
    }

    /**
     * Gets the cost of the spell
     *
     * @return The cost of the spell including all spell stages
     */
    fun getCost(): Double {
        var cost = 0.0

        var currentDeliveryMultiplicity = 1.0
        // Go over each spell stage and add up costs. The last stage has no
        for (spellStage in spellStages) {
            // Add the cost of the stage
            cost = cost + spellStage.getCost() * currentDeliveryMultiplicity
            val deliveryInstance = spellStage.deliveryInstance!!
            // Each stage after will cost "Multiplicity" more since it gets proc'd "Multiplicity" times
            currentDeliveryMultiplicity = currentDeliveryMultiplicity * deliveryInstance.component.getMultiplicity(deliveryInstance)
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

    fun hasDeliveryMethod(deliveryMethod: SpellDeliveryMethod): Boolean {
        return spellStages.any { it.deliveryInstance?.component == deliveryMethod }
    }

    fun hasEffect(effect: SpellEffect): Boolean {
        return spellStages.any { it.effects.any { eff -> eff?.component == effect } }
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell's data
     */
    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        // Write each field to NBT
        nbt.putString(NBT_NAME, name)

        // Write each spell stage to NBT
        val spellStagesNBT = ListNBT()
        spellStages.forEach { spellStagesNBT.add(it.serializeNBT()) }
        nbt.put(NBT_SPELL_STAGES, spellStagesNBT)
        return nbt
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    override fun deserializeNBT(nbt: CompoundNBT) {
        // Read each field from NBT
        name = nbt.getString(NBT_NAME)

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
            "Spell Stages:\n" +
            spellStages.joinToString(separator = "\n") { " -> $it" }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Constants used for spell tiers
        private const val SPELL_TIER2_CUTOFF = 100
        private const val SPELL_TIER3_CUTOFF = 500

        // Constants used for NBT serialization/deserialiation
        private const val NBT_NAME = "name"
        private const val NBT_SPELL_STAGES = "spell_stages"
    }
}