package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.particle.ProjectileParticleData
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ProjectileSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.utility.getLookNormal
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData
import net.minecraftforge.fmllegacy.network.NetworkHooks
import java.awt.Color
import java.util.*
import java.util.function.Predicate

/**
 * Class representing the projectile delivery method
 *
 * @constructor required constructor that sets the world
 * @param world The world the entity is in
 * @property shooter The entity that fired the projectile, can be null
 */
class SpellProjectileEntity(
    entityType: EntityType<out SpellProjectileEntity>,
    world: Level
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
        world: Level,
        spell: Spell,
        spellIndex: Int,
        spellCaster: Entity?,
        position: Vec3,
        velocity: Vec3,
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
        val blocksPerSecond = deliveryMethodProjectile.getSpeed(deliveryInstance)
        val blocksPerTick = blocksPerSecond / 20.0

        // Update the acceleration vector by normalizing it and multiplying by speed
        this.deltaMovement = velocity.normalize().scale(blocksPerTick)

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

            // Sometimes show particles
            if (level.isClientSide) {
                for (i in 0..random.nextInt(3)) {
                    val color = getColor()
                    val position = position()
                    val velocity = deltaMovement.reverse()
                        .scale(0.4)
                        .multiply(random.nextDouble() * 0.4 + 0.8, random.nextDouble() * 0.4 + 0.8, random.nextDouble() * 0.4 + 0.8)
                    level.addParticle(
                        ProjectileParticleData(random.nextFloat() * 0.6f + 0.6f, color.red / 255f, color.green / 255f, color.blue / 255f),
                        position.x + bbWidth / 2 + (random.nextDouble() - 0.5) * 0.4,
                        position.y + bbHeight / 2 + (random.nextDouble() - 0.5) * 0.4,
                        position.z + bbWidth / 2 + (random.nextDouble() - 0.5) * 0.4,
                        velocity.x,
                        velocity.y,
                        velocity.z
                    )
                }
            }

            // Process hit detection server side
            if (!level.isClientSide) {
                val rayTraceResult = checkCollision()

                // If the ray trace hit something, perform the hit effect
                if (rayTraceResult.type != HitResult.Type.MISS) {
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
                    val state = DeliveryTransitionState(
                        spell = spell,
                        stageIndex = spellIndex,
                        world = level,
                        position = position(),
                        blockPosition = blockPosition(),
                        direction = deltaMovement.normalize(),
                        normal = deltaMovement.getNormal(),
                        casterEntity = casterEntityId?.let { (level as? ServerLevel)?.getEntity(it) },
                        deliveryEntity = this
                    )

                    // Proc the effects and transition
                    val currentDeliveryMethod = spell.getStage(spellIndex)!!.deliveryInstance!!.component
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)

                    remove(RemovalReason.DISCARDED)
                }
            }
        } else {
            remove(RemovalReason.DISCARDED)
        }
    }

    private fun checkCollision(): HitResult {
        val entityHitPredicate = Predicate<Entity> {
            if (ticksInAir > 25) {
                true
            } else {
                it != shooter
            }
        }

        // < 0.003 speed causes collision detection to break. Use custom logic
        if (deltaMovement.lengthSqr() < 0.00001) {
            val blockResult = level.clip(
                ClipContext(
                    position(),
                    position().add(0.0, 0.001, 0.0),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this
                )
            )
            if (blockResult.type != HitResult.Type.MISS) {
                return blockResult
            }

            val entities = level.getEntities(this, boundingBox)
            for (entity in entities) {
                if (entityHitPredicate.test(entity)) {
                    return EntityHitResult(entity)
                }
            }
            return BlockHitResult.miss(position(), Direction.NORTH, blockPosition())
        } else {
            // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
            return ProjectileUtil.getHitResult(this, entityHitPredicate)
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: HitResult) {
        // Only process server side
        if (!level.isClientSide) {
            val spell = this.entityData[SPELL]
            val spellIndex = this.entityData[SPELL_INDEX]

            // Grab the current spell stage
            val currentStage = spell.getStage(spellIndex)

            // If we hit something process the hit
            if (result.type != HitResult.Type.MISS) {
                val currentDeliveryMethod = currentStage!!.deliveryInstance!!.component
                val direction = deltaMovement.normalize()
                if (result.type == HitResult.Type.BLOCK && result is BlockHitResult) {
                    // Grab the hit position
                    var hitPos = BlockPos(result.location)

                    // If we hit an air block find the block to the side of the air, hit that instead
                    if (level.isEmptyBlock(hitPos)) {
                        hitPos = hitPos.relative(result.direction.opposite)
                    }
                    val state = DeliveryTransitionState(
                        spell = spell,
                        stageIndex = spellIndex,
                        world = level,
                        position = result.location,
                        blockPosition = hitPos,
                        direction = direction,
                        normal = deltaMovement.getNormal(),
                        casterEntity = this.casterEntityId?.let { (level as? ServerLevel)?.getEntity(it) },
                        deliveryEntity = this
                    )

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state.copy(position = result.location.subtract(direction.scale(HIT_DELIVERY_TRANSITION_OFFSET))))
                } else if (result.type == HitResult.Type.ENTITY && result is EntityHitResult) {
                    val entityHit = result.entity
                    val state = DeliveryTransitionState(
                        spell = spell,
                        stageIndex = spellIndex,
                        world = level,
                        position = result.location,
                        blockPosition = BlockPos(result.location),
                        direction = direction,
                        normal = deltaMovement.getNormal(),
                        casterEntity = this.casterEntityId?.let { (level as? ServerLevel)?.getEntity(it) },
                        entity = entityHit,
                        deliveryEntity = this
                    )

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)

                    val position = entityHit.getEyePosition(1.0f)
                    val transitionState = state.copy(
                        position = position,
                        blockPosition = BlockPos(position),
                        direction = entityHit.lookAngle,
                        normal = entityHit.getLookNormal()
                    )
                    currentDeliveryMethod.transitionFrom(transitionState)
                }
            }

            // Kill the projectile
            remove(RemovalReason.DISCARDED)
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

    override fun getAddEntityPacket(): Packet<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
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

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.put("spell", this.entityData[SPELL].serializeNBT())
        compound.putInt("spell_index", this.entityData[SPELL_INDEX])
        compound.putInt("ticks_in_air", this.ticksInAir)
        compound.putFloat("distance_remaining_blocks", this.distanceRemainingBlocks)
        this.casterEntityId?.let { compound.putUUID("caster_entity_id", it) }
    }

    /*
     * Spawn data is sent Server -> Client BEFORE the entity is spawned. We use this to ensure the client knows the spell's color before rendering
     */

    override fun readSpawnData(additionalData: FriendlyByteBuf) {
        this.entityData[SPELL] = Spell(additionalData.readNbt()!!)
        this.entityData[SPELL_INDEX] = additionalData.readInt()
    }

    override fun writeSpawnData(buffer: FriendlyByteBuf) {
        buffer.writeNbt(this.entityData[SPELL].serializeNBT())
        buffer.writeInt(this.entityData[SPELL_INDEX])
    }

    companion object {
        private const val HIT_DELIVERY_TRANSITION_OFFSET = 0.01

        private val SPELL = SynchedEntityData.defineId(SpellProjectileEntity::class.java, ModDataSerializers.SPELL)
        private val SPELL_INDEX = SynchedEntityData.defineId(SpellProjectileEntity::class.java, EntityDataSerializers.INT)
    }
}