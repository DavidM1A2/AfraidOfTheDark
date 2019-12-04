package com.davidm1a2.afraidofthedark.common.entity.spell.projectile

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation.AnimationHandlerSpellProjectile
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodProjectile
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing the projectile delivery method
 *
 * @constructor required constructor that sets the world
 * @param world The world the entity is in
 * @property ticksInAir The number of ticks the projectile has been in the air
 * @property spell The spell that this projectile is a part of
 * @property spellIndex The current spell stage index
 * @property shooter The entity that fired the projectile, can be null
 * @property blockDistanceRemaining The amount of blocks left before this projectile expires
 * @property animHandler The animation handler that this entity uses for all animations
 */
class EntitySpellProjectile(world: World) : Entity(world), IMCAnimatedEntity
{
    private var ticksInAir = 0
    private lateinit var spell: Spell
    private var spellIndex: Int = 0
    private var shooter: Entity? = null
    private var blockDistanceRemaining: Double = 0.0
    private val animHandler = AnimationHandlerSpellProjectile(this)

    init
    {
        setSize(0.4f, 0.4f)
    }

    /**
     * Required constructor that sets the world, pos, and velocity
     *
     * @param world      The world the entity is in
     * @param spell      The spell that this projectile is delivering
     * @param spellIndex The index of the current spell stage that is being executed
     * @param position   The position of the spell projectile
     * @param velocity   The velocity of the projectile
     */
    constructor(world: World, spell: Spell, spellIndex: Int, position: Vec3d, velocity: Vec3d?) : this(world)
    {
        var velocity = velocity
        this.spell = spell
        this.spellIndex = spellIndex
        val deliveryMethodProjectile = spell.getStage(spellIndex).deliveryMethod as SpellDeliveryMethodProjectile
        blockDistanceRemaining = deliveryMethodProjectile.range
        // Grab the projectile speed from the delivery method
        val projectileSpeed = deliveryMethodProjectile.speed
        // Default velocity will just be random velocity
        if (velocity == null)
        {
            velocity = Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5)
        }
        // Update the acceleration vector by normalizing it and multiplying by speed
        val motion = velocity.normalize().scale(projectileSpeed)
        motionX = motion.x
        motionY = motion.y
        motionZ = motion.z
        // Position the entity at the center of the shooter moved slightly in the dir of fire
        setPosition(position.x + motionX, position.y + motionY, position.z + motionZ)
        // Null shooter
        shooter = null
    }

    /**
     * Constructor sets the projectile's properties based on a shooter entity
     *
     * @param world      The world the projectile is in
     * @param spell      The spell that was fired
     * @param spellIndex The index of the current spell stage that is being executed
     * @param entity     The entity that fired the projectile
     */
    constructor(world: World, spell: Spell, spellIndex: Int, entity: Entity) : this(world,
            spell,
            spellIndex,
            entity.positionVector.addVector(0.0, entity.eyeHeight.toDouble(), 0.0),
            entity.lookVec)
    {
        setRotation(entity.rotationYaw, entity.rotationPitch)
        shooter = entity
    }

    /**
     * Required init method, does nothing
     */
    override fun entityInit()
    {
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun onUpdate()
    {
        super.onUpdate()
        // Animations only update client side
        if (world.isRemote)
        {
            animHandler.animationsUpdate()
        }
        // Update logic server side
        if (!world.isRemote)
        {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            if (world.isBlockLoaded(BlockPos(this)))
            {
                // We are in the air, so increment our counter
                ticksInAir++
                // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                val rayTraceResult: RayTraceResult? = ProjectileHelper.forwardsRaycast(this, true, ticksInAir >= 25, shooter)
                // If the ray trace hit something, perform the hit effect
                // Intellij says this is always non-null, that is not the case....
                if (rayTraceResult != null)
                {
                    onImpact(rayTraceResult)
                }

                // Continue flying in the direction of motion, update the position
                setPosition(posX + motionX, posY + motionY, posZ + motionZ)
                // Update distance flown, and kill the entity if it went
                val distanceFlown = Vec3d(motionX, motionY, motionZ).lengthVector()
                blockDistanceRemaining = blockDistanceRemaining - distanceFlown
                // If we're out of distance deliver the spell and kill the projectile
                if (blockDistanceRemaining <= 0)
                {
                    val state = DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withWorld(world)
                            .withPosition(this.positionVector)
                            .withBlockPosition(this.position)
                            .withDirection(Vec3d(motionX, motionY, motionZ))
                            .withDeliveryEntity(this)
                            .build()
                    // Proc the effects and transition
                    val currentDeliveryMethod = spell.getStage(spellIndex).deliveryMethod
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)
                    setDead()
                }
            }
            else
            {
                setDead()
            }
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: RayTraceResult)
    {
        // Only process server side
        if (!world.isRemote)
        {
            // Grab the current spell stage
            val currentStage = spell.getStage(spellIndex)
            // If we hit something process the hit
            if (result.typeOfHit != RayTraceResult.Type.MISS)
            {
                val currentDeliveryMethod = currentStage.deliveryMethod
                if (result.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    // Grab the hit position
                    var hitPos = BlockPos(result.hitVec)
                    // If we hit an air block find the block to the side of the air, hit that instead
                    if (world.getBlockState(hitPos).block === Blocks.AIR)
                    {
                        hitPos = hitPos.offset(result.sideHit.opposite)
                    }
                    val state = DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withWorld(world)
                            .withPosition(result.hitVec)
                            .withBlockPosition(hitPos)
                            .withDirection(Vec3d(motionX, motionY, motionZ))
                            .withDeliveryEntity(this)
                            .build()
                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)
                }
                else if (result.typeOfHit == RayTraceResult.Type.ENTITY)
                {
                    val state = DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withEntity(result.entityHit)
                            .withDeliveryEntity(this)
                            .build()
                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state)
                    currentDeliveryMethod.transitionFrom(state)
                }
            }
            // Kill the projectile
            setDead()
        }
    }

    /**
     * Called from onUpdate to update entity specific logic
     */
    override fun onEntityUpdate()
    {
        super.onEntityUpdate()
        // If we're client side and no animation is active play the idle animation
        if (world.isRemote)
        {
            if (!animHandler.isAnimationActive("Idle"))
            {
                animHandler.activateAnimation("Idle", 0f)
            }
        }
    }

    /**
     * Writes the entity to the nbt compound
     *
     * @param compound The nbt compound to write to
     */
    override fun writeEntityToNBT(compound: NBTTagCompound)
    {
        compound.setInteger(NBT_TICKS_IN_AIR, ticksInAir)
        compound.setTag(NBT_MOTION_DIRECTION, newDoubleNBTList(motionX, motionY, motionZ))
        compound.setTag(NBT_SPELL, spell.serializeNBT())
        compound.setInteger(NBT_SPELL_INDEX, spellIndex)
        compound.setDouble(NBT_BLOCK_DISTANCE_REMAINING, blockDistanceRemaining)
    }

    /**
     * Reads the entity from the nbt compound into the entity object
     *
     * @param compound The nbt compound to read data from
     */
    override fun readEntityFromNBT(compound: NBTTagCompound)
    {
        ticksInAir = compound.getInteger(NBT_TICKS_IN_AIR)
        val motionTagList = compound.getTagList(NBT_MOTION_DIRECTION, Constants.NBT.TAG_DOUBLE)
        motionX = motionTagList.getDoubleAt(0)
        motionY = motionTagList.getDoubleAt(1)
        motionZ = motionTagList.getDoubleAt(2)
        spell = Spell(compound.getCompoundTag(NBT_SPELL))
        spellIndex = compound.getInteger(NBT_SPELL_INDEX)
        blockDistanceRemaining = compound.getDouble(NBT_BLOCK_DISTANCE_REMAINING)
    }

    /**
     * @return True, the projectile can hit other entities
     */
    override fun canBeCollidedWith(): Boolean
    {
        return true
    }

    /**
     * Gets the collision bounding box which allows the collision box to be bigger than the entity box
     *
     * @return The size of the entity, 0.4
     */
    override fun getCollisionBorderSize(): Float
    {
        return 0.4f
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean
    {
        return false
    }

    /**
     * Gets how bright this entity is.
     *
     * @return 1.0, max brightness so the projectile isn't too dark
     */
    override fun getBrightness(): Float
    {
        return 1.0f
    }

    /**
     * Not sure exactly what this does, but the fireball uses this code too so I copied the value over
     *
     * @return The same value as EntityFireball
     */
    @SideOnly(Side.CLIENT)
    override fun getBrightnessForRender(): Int
    {
        return 15728880
    }

    /**
     * Gets the animation handler which makes the projectile spin
     *
     * @return The animation handler for the projectile
     */
    override fun getAnimationHandler(): AnimationHandler
    {
        return animHandler
    }

    companion object
    {
        // NBT compound constants
        private const val NBT_TICKS_IN_AIR = "ticks_in_air"
        private const val NBT_MOTION_DIRECTION = "motion_direction"
        private const val NBT_SPELL = "spell"
        private const val NBT_SPELL_INDEX = "spell_index"
        private const val NBT_BLOCK_DISTANCE_REMAINING = "block_distance_remaining"
    }
}