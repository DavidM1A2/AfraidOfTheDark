package com.davidm1a2.afraidofthedark.common.entity.spell.projectile

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ProjectileSpellDeliveryMethod
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData
import net.minecraftforge.fml.network.NetworkHooks
import java.awt.Color
import java.util.*

/**
 * Class representing the projectile delivery method
 *
 * @constructor required constructor that sets the world
 * @param world The world the entity is in
 * @property shooter The entity that fired the projectile, can be null
 */
class SpellProjectileEntity(
    entityType: EntityType<out SpellProjectileEntity>,
    world: World
) : Entity(entityType, world), IEntityAdditionalSpawnData {
    private var shooter: Entity? = null
    private var casterEntityId: UUID? = null
    private var distanceRemainingBlocks: Float = 0f
    private var ticksInAir: Int = 0

    /**
     * Required constructor that sets the world, pos, and velocity
     *
     * @param world       The world the entity is in
     * @param spell       The spell that this projectile is delivering
     * @param spellIndex  The index of the current spell stage that is being executed
     * @param spellCaster The entity that casted the spell
     * @param position    The position of the spell projectile
     * @param velocity    The velocity of the projectile, default will just be random velocity
     */
    constructor(
        world: World,
        spell: Spell,
        spellIndex: Int,
        spellCaster: Entity?,
        position: Vector3d,
        velocity: Vector3d,
        shooter: Entity? = null
    ) : this(ModEntities.SPELL_PROJECTILE, world) {
        this.shooter = shooter
        this.entityData[SPELL] = spell
        this.entityData[SPELL_INDEX] = spellIndex
        this.casterEntityId = spellCaster?.uuid
        val deliveryInstance = spell.getStage(spellIndex)!!.deliveryInstance!!
        val deliveryMethodProjectile = deliveryInstance.component as ProjectileSpellDeliveryMethod
        this.distanceRemainingBlocks = deliveryMethodProjectile.getRange(deliveryInstance).toFloat()

        // Grab the projectile speed from the delivery method
        val projectileSpeed = deliveryMethodProjectile.getSpeed(deliveryInstance)

        // Update the acceleration vector by normalizing it and multiplying by speed
        this.deltaMovement = velocity.normalize().scale(projectileSpeed)

        // Position the entity at the center of the shooter moved slightly in the dir of fire
        moveTo(position.x + this.deltaMovement.x, position.y + this.deltaMovement.y, position.z + this.deltaMovement.z)

        this.shooter?.let {
            setRot(it.xRot, it.yRot)
        }
    }

    /**
     * Initialize dataManager. Anything registered here is automatically synced from Server -> Client
     */
    override fun defineSynchedData() {
        this.entityData.define(SPELL, Spell())
        this.entityData.define(SPELL_INDEX, 0)
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun tick() {
        super.tick()

        // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
        if (level.isLoaded(this.blockPosition())) {
            // We are in the air, so increment our counter
            this.ticksInAir = this.ticksInAir + 1

            // Process hit detection server side
            if (!level.isClientSide) {
                // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                val rayTraceResult = ProjectileHelper.getHitResult(this) {
                    if (ticksInAir > 25) {
                        true
                    } else {
                        it != shooter
                    }
                }

                // If the ray trace hit something, perform the hit effect
                if (rayTraceResult.type != RayTraceResult.Type.MISS) {
                    onImpact(rayTraceResult)
                }
            }

            // Continue flying in the direction of motion, update the position
            setPos(x + deltaMovement.x, y + deltaMovement.y, z + deltaMovement.z)

            // Update distance flown, and kill the entity if it went
            val distanceFlown = deltaMovement.length()
            this.distanceRemainingBlocks = this.distanceRemainingBlocks - distanceFlown.toFloat()

            // If we're out of distance deliver the spell and kill the projectile
            if (this.distanceRemainingBlocks <= 0) {
                val spell = this.entityData[SPELL]
                val spellIndex = this.entityData[SPELL_INDEX]

                // Update spell logic server side
                if (!level.isClientSide) {
                    val state = DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex)
                        .withWorld(level)
                        .withPosition(this.position())
                        .withBlockPosition(this.blockPosition())
                        .withDirection(deltaMovement.normalize())
                        .withCasterEntity(this.casterEntityId?.let { (level as? ServerWorld)?.getEntity(it) })
                        .withDeliveryEntity(this)
                        .build()

                    // Proc the effects and transition
                    val currentDeliveryMethod = spell.getStage(spellIndex)!!.deliveryInstance!!.component
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)

                    remove()
                }
            }
        } else {
            remove()
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: RayTraceResult) {
        // Only process server side
        if (!level.isClientSide) {
            val spell = this.entityData[SPELL]
            val spellIndex = this.entityData[SPELL_INDEX]

            // Grab the current spell stage
            val currentStage = spell.getStage(spellIndex)

            // If we hit something process the hit
            if (result.type != RayTraceResult.Type.MISS) {
                val currentDeliveryMethod = currentStage!!.deliveryInstance!!.component
                if (result.type == RayTraceResult.Type.BLOCK && result is BlockRayTraceResult) {
                    // Grab the hit position
                    var hitPos = BlockPos(result.location)

                    // If we hit an air block find the block to the side of the air, hit that instead
                    if (level.isEmptyBlock(hitPos)) {
                        hitPos = hitPos.relative(result.direction.opposite)
                    }
                    val state = DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex)
                        .withWorld(level)
                        .withPosition(result.location)
                        .withBlockPosition(hitPos)
                        .withDirection(deltaMovement.normalize())
                        .withCasterEntity(this.casterEntityId?.let { (level as? ServerWorld)?.getEntity(it) })
                        .withDeliveryEntity(this)
                        .build()

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)
                } else if (result.type == RayTraceResult.Type.ENTITY && result is EntityRayTraceResult) {
                    val state = DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex)
                        .withEntity(result.entity)
                        .withCasterEntity(this.casterEntityId?.let { (level as? ServerWorld)?.getEntity(it) })
                        .withDeliveryEntity(this)
                        .build()

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)
                }
            }

            // Kill the projectile
            remove()
        }
    }

    fun getColor(): Color {
        return ModSpellDeliveryMethods.PROJECTILE.getColor(entityData[SPELL].spellStages[entityData[SPELL_INDEX]].deliveryInstance!!)
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun getPickRadius(): Float {
        return 0.4f
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.hurt(source, amount)
        }
        return false
    }

    /**
     * Gets how bright this entity is.
     *
     * @return 1.0, max brightness so the projectile isn't too dark
     */
    override fun getBrightness(): Float {
        return 1.0f
    }

    /**
     * The spell projectile can't ride anything
     *
     * @param entityIn The entity to test
     * @return False
     */
    override fun canRide(entityIn: Entity): Boolean {
        return false
    }

    override fun ignoreExplosion(): Boolean {
        return true
    }

    override fun doWaterSplashEffect() {
    }

    override fun updateInWaterStateAndDoFluidPushing(): Boolean {
        return false
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        this.entityData[SPELL] = Spell(compound.getCompound("spell"))
        this.entityData[SPELL_INDEX] = compound.getInt("spell_index")
        this.ticksInAir = compound.getInt("ticks_in_air")
        this.distanceRemainingBlocks = compound.getFloat("distance_remaining_blocks")
        this.casterEntityId = if (compound.contains("caster_entity_id")) {
            compound.getUUID("caster_entity_id")
        } else {
            null
        }
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        compound.put("spell", this.entityData[SPELL].serializeNBT())
        compound.putInt("spell_index", this.entityData[SPELL_INDEX])
        compound.putInt("ticks_in_air", this.ticksInAir)
        compound.putFloat("distance_remaining_blocks", this.distanceRemainingBlocks)
        this.casterEntityId?.let { compound.putUUID("caster_entity_id", it) }
    }

    /*
     * Spawn data is sent Server -> Client BEFORE the entity is spawned. We use this to ensure the client knows the spell's color before rendering
     */

    override fun readSpawnData(additionalData: PacketBuffer) {
        this.entityData[SPELL] = Spell(additionalData.readNbt()!!)
        this.entityData[SPELL_INDEX] = additionalData.readInt()
    }

    override fun writeSpawnData(buffer: PacketBuffer) {
        buffer.writeNbt(this.entityData[SPELL].serializeNBT())
        buffer.writeInt(this.entityData[SPELL_INDEX])
    }

    companion object {
        private val SPELL = EntityDataManager.defineId(SpellProjectileEntity::class.java, ModDataSerializers.SPELL)
        private val SPELL_INDEX = EntityDataManager.defineId(SpellProjectileEntity::class.java, DataSerializers.INT)
    }
}