package com.davidm1a2.afraidofthedark.common.entity.spell.projectile

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation.SpellProjectileIdleChannel
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.ProjectileSpellDeliveryMethod
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.math.*
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.network.NetworkHooks
import java.util.*

/**
 * Class representing the projectile delivery method
 *
 * @constructor required constructor that sets the world
 * @param world The world the entity is in
 * @property shooter The entity that fired the projectile, can be null
 * @property animHandler The animation handler that this entity uses for all animations
 */
class SpellProjectileEntity(entityType: EntityType<out SpellProjectileEntity>, world: World) : Entity(entityType, world),
    IMCAnimatedModel {
    private var shooter: Entity? = null
    private val animHandler = AnimationHandler(SpellProjectileIdleChannel("Idle", 100.0f, 60, ChannelMode.LOOP))

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
        position: Vec3d,
        velocity: Vec3d,
        shooter: Entity? = null
    ) : this(ModEntities.SPELL_PROJECTILE, world) {
        this.shooter = shooter
        this.dataManager[SPELL] = spell
        this.dataManager[SPELL_INDEX] = spellIndex
        this.dataManager[CASTER_ENTITY_ID] = Optional.ofNullable(spellCaster?.uniqueID)
        val deliveryInstance = spell.getStage(spellIndex)!!.deliveryInstance!!
        val deliveryMethodProjectile = deliveryInstance.component as ProjectileSpellDeliveryMethod
        this.dataManager[DISTANCE_REMAINING_BLOCKS] = deliveryMethodProjectile.getRange(deliveryInstance).toFloat()

        // Grab the projectile speed from the delivery method
        val projectileSpeed = deliveryMethodProjectile.getSpeed(deliveryInstance)

        // Update the acceleration vector by normalizing it and multiplying by speed
        this.motion = velocity.normalize().scale(projectileSpeed)

        // Position the entity at the center of the shooter moved slightly in the dir of fire
        setPosition(position.x + this.motion.x, position.y + this.motion.y, position.z + this.motion.z)

        this.shooter?.let {
            setRotation(it.rotationYaw, it.rotationPitch)
        }
    }

    override fun registerData() {
        this.dataManager.register(TICKS_IN_AIR, -1)
        this.dataManager.register(SPELL, Spell())
        this.dataManager.register(SPELL_INDEX, 0)
        this.dataManager.register(DISTANCE_REMAINING_BLOCKS, -1f)
        this.dataManager.register(CASTER_ENTITY_ID, Optional.empty())
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun tick() {
        super.tick()

        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }

        // Update logic server side
        if (!world.isRemote) {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            @Suppress("DEPRECATION")
            if (world.isBlockLoaded(BlockPos(this))) {
                // We are in the air, so increment our counter
                val ticksInAir = this.dataManager[TICKS_IN_AIR] + 1
                this.dataManager[TICKS_IN_AIR] = ticksInAir

                // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                // Intellij says 'shooter' should always be non-null, that is not the case....
                val rayTraceResult = ProjectileHelper.func_221266_a(this, true, ticksInAir >= 25, shooter, RayTraceContext.BlockMode.COLLIDER)

                // If the ray trace hit something, perform the hit effect
                if (rayTraceResult.type != RayTraceResult.Type.MISS) {
                    onImpact(rayTraceResult)
                }

                // Continue flying in the direction of motion, update the position
                setPosition(posX + motion.x, posY + motion.y, posZ + motion.z)

                // Update distance flown, and kill the entity if it went
                val distanceFlown = motion.length()
                val blockDistanceRemaining = this.dataManager[DISTANCE_REMAINING_BLOCKS] - distanceFlown.toFloat()
                this.dataManager[DISTANCE_REMAINING_BLOCKS] = blockDistanceRemaining

                val spell = this.dataManager[SPELL]
                val spellIndex = this.dataManager[SPELL_INDEX]

                // If we're out of distance deliver the spell and kill the projectile
                if (blockDistanceRemaining <= 0) {
                    val state = DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex)
                        .withWorld(world)
                        .withPosition(this.positionVector)
                        .withBlockPosition(this.position)
                        .withDirection(motion)
                        .withCasterEntity(this.dataManager[CASTER_ENTITY_ID].map { (world as? ServerWorld)?.getEntityByUuid(it) }.orElse(null))
                        .withDeliveryEntity(this)
                        .build()

                    // Proc the effects and transition
                    val currentDeliveryMethod = spell.getStage(spellIndex)!!.deliveryInstance!!.component
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)

                    remove()
                }
            } else {
                remove()
            }
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: RayTraceResult) {
        // Only process server side
        if (!world.isRemote) {
            val spell = this.dataManager[SPELL]
            val spellIndex = this.dataManager[SPELL_INDEX]

            // Grab the current spell stage
            val currentStage = spell.getStage(spellIndex)

            // If we hit something process the hit
            if (result.type != RayTraceResult.Type.MISS) {
                val currentDeliveryMethod = currentStage!!.deliveryInstance!!.component
                if (result.type == RayTraceResult.Type.BLOCK && result is BlockRayTraceResult) {
                    // Grab the hit position
                    var hitPos = BlockPos(result.hitVec)
                    val hitBlock = world.getBlockState(hitPos)

                    // If we hit an air block find the block to the side of the air, hit that instead
                    if (hitBlock.isAir(world, hitPos)) {
                        hitPos = hitPos.offset(result.face.opposite)
                    }
                    val state = DeliveryTransitionStateBuilder()
                        .withSpell(spell)
                        .withStageIndex(spellIndex)
                        .withWorld(world)
                        .withPosition(result.hitVec)
                        .withBlockPosition(hitPos)
                        .withDirection(motion)
                        .withCasterEntity(this.dataManager[CASTER_ENTITY_ID].map { (world as? ServerWorld)?.getEntityByUuid(it) }.get())
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
                        .withCasterEntity(this.dataManager[CASTER_ENTITY_ID].map { (world as? ServerWorld)?.getEntityByUuid(it) }.get())
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

    /**
     * Called from onUpdate to update entity specific logic
     */
    override fun baseTick() {
        super.baseTick()

        // If we're client side and no animation is active play the idle animation
        if (world.isRemote) {
            if (!animHandler.isAnimationActive("Idle")) {
                animHandler.playAnimation("Idle")
            }
        }
    }

    /**
     * @return True, the projectile can hit other entities
     */
    override fun canBeCollidedWith(): Boolean {
        return true
    }

    /**
     * Gets the collision bounding box which allows the collision box to be bigger than the entity box
     *
     * @return The size of the entity, 0.4
     */
    override fun getCollisionBorderSize(): Float {
        return 0.4f
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
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
     * Not sure exactly what this does, but the fireball uses this code too so I copied the value over
     *
     * @return The same value as EntityFireball
     */
    @OnlyIn(Dist.CLIENT)
    override fun getBrightnessForRender(): Int {
        return 15728880
    }

    /**
     * The spell projectile can't ride anything
     *
     * @param entityIn The entity to test
     * @return False
     */
    override fun canBeRidden(entityIn: Entity): Boolean {
        return false
    }

    /**
     * Gets the animation handler which makes the projectile spin
     *
     * @return The animation handler for the projectile
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditional(compound: CompoundNBT) {
        this.dataManager[TICKS_IN_AIR] = compound.getInt("ticks_in_air")
        this.dataManager[SPELL] = Spell(compound.getCompound("spell"))
        this.dataManager[SPELL_INDEX] = compound.getInt("spell_index")
        this.dataManager[DISTANCE_REMAINING_BLOCKS] = compound.getFloat("distance_remaining_blocks")
        this.dataManager[CASTER_ENTITY_ID] = if (compound.contains("caster_entity_id")) {
            Optional.of(compound.getUniqueId("caster_entity_id"))
        } else {
            Optional.empty()
        }
    }

    override fun writeAdditional(compound: CompoundNBT) {
        compound.putInt("ticks_in_air", this.dataManager[TICKS_IN_AIR])
        compound.put("spell", this.dataManager[SPELL].serializeNBT())
        compound.putInt("spell_index", this.dataManager[SPELL_INDEX])
        compound.putFloat("distance_remaining_blocks", this.dataManager[DISTANCE_REMAINING_BLOCKS])
        this.dataManager[CASTER_ENTITY_ID].map { compound.putUniqueId("caster_entity_id", it) }
    }

    companion object {
        private val TICKS_IN_AIR = EntityDataManager.createKey(SpellProjectileEntity::class.java, DataSerializers.VARINT)
        private val SPELL = EntityDataManager.createKey(SpellProjectileEntity::class.java, ModDataSerializers.SPELL)
        private val SPELL_INDEX = EntityDataManager.createKey(SpellProjectileEntity::class.java, DataSerializers.VARINT)
        private val DISTANCE_REMAINING_BLOCKS = EntityDataManager.createKey(SpellProjectileEntity::class.java, DataSerializers.FLOAT)
        private val CASTER_ENTITY_ID = EntityDataManager.createKey(SpellProjectileEntity::class.java, DataSerializers.OPTIONAL_UNIQUE_ID)
    }
}