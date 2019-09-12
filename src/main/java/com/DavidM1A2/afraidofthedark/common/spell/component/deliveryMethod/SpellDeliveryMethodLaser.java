package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.particle.AOTDParticleRegistry;
import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncParticle;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Laser delivery method delivers the spell to the target with a hitscan laser
 */
public class SpellDeliveryMethodLaser extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_RANGE = "range";
    private static final String NBT_HIT_LIQUIDS = "hit_liquids";

    // The range of the hitscan
    private double range = 50;
    // True if liquids can be hit, false otherwise
    private boolean hitLiquids = false;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodLaser()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.doubleProperty()
                .withName("Range")
                .withDescription("The range of the laser in blocks.")
                .withSetter(newValue -> this.range = newValue)
                .withGetter(() -> this.range)
                .withDefaultValue(50D)
                .withMinValue(1D)
                .withMaxValue(300D)
                .build());
        this.addEditableProperty(SpellComponentPropertyFactory.booleanProperty()
                .withName("Hit Liquids")
                .withDescription("'True' to let liquid blocks be hit, or 'false' to go through them.")
                .withSetter(newValue -> this.hitLiquids = newValue)
                .withGetter(() -> this.hitLiquids)
                .withDefaultValue(false)
                .build());
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void executeDelivery(DeliveryTransitionState state)
    {
        // Perform a ray trace to try and find a hit point
        World world = state.getWorld();
        Entity entity = state.getEntity();
        // Start at entity eye height if it's from an entity
        Vec3d startPos = entity == null ? state.getPosition() : state.getPosition().addVector(0, entity.getEyeHeight(), 0);
        Vec3d direction = state.getDirection().normalize();
        // The end position is the start position in the right direction scaled to range
        Vec3d endPos = startPos.add(direction.scale(this.range));
        // Perform a ray trace, this will not hit blocks
        RayTraceResult rayTraceResult = world.rayTraceBlocks(startPos, endPos, this.hitLiquids);
        // Compute the hit vector
        Vec3d hitPos = rayTraceResult == null ? endPos : rayTraceResult.hitVec;
        // Compute the block position we hit
        BlockPos hitBlockPos = rayTraceResult == null ? new BlockPos(hitPos) : rayTraceResult.getBlockPos();

        // Ray tracing doesn't actually hit entities, so use this "hack" to test that. Create a bounding box that is huge and
        // has the entire possible ray inside. Then grab entities inside, then intersect each of their hitboxes manually
        final Vec3d finalHitPos = hitPos;
        List<Entity> potentialHitEntities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(startPos.x, startPos.y, startPos.z, hitPos.x, hitPos.y, hitPos.z));
        Optional<Entity> hitEntity = potentialHitEntities.stream()
                // Don't hitscan ourselves
                .filter(potentialHitEntity -> potentialHitEntity != entity)
                // Ensure the entity is along the path with the ray
                .filter(potentialHitEntity -> potentialHitEntity.getEntityBoundingBox().calculateIntercept(startPos, finalHitPos) != null)
                // Find the closest entity
                .min((entity1, entity2) -> Double.compare(entity1.getDistanceSq(new BlockPos(startPos)), entity2.getDistanceSq(new BlockPos(finalHitPos))));

        hitPos = hitEntity.map(Entity::getPositionVector).orElse(hitPos);
        // Compute the distance the ray traveled
        double distanceToHit = startPos.distanceTo(hitPos);
        // Spawn at least 10 particles and at most 100. Take the distance to the hit position and spawn one particle per distance
        int numParticlesToSpawn = MathHelper.clamp(MathHelper.ceil(distanceToHit), 10, 100);
        // Compute points along the hitscan line
        List<Vec3d> laserPositions = new ArrayList<>();
        for (int i = 0; i < numParticlesToSpawn; i++)
        {
            laserPositions.add(startPos.add(direction.scale((double) i / (double) numParticlesToSpawn * distanceToHit)));
        }
        // Spawn laser particles
        AfraidOfTheDark.INSTANCE.getPacketHandler().sendToDimension(
                new SyncParticle(AOTDParticleRegistry.ParticleTypes.SPELL_LASER, laserPositions, Collections.nCopies(numParticlesToSpawn, Vec3d.ZERO)), state.getWorld().provider.getDimension());

        // Begin performing effects and transition
        DeliveryTransitionState currentState;
        if (hitEntity.isPresent())
        {
            // Apply the effect to the hit entity
            currentState = new DeliveryTransitionStateBuilder()
                    .withSpell(state.getSpell())
                    .withStageIndex(state.getStageIndex())
                    .withEntity(hitEntity.get())
                    .build();
        }
        else
        {
            // Apply the effect at the hit position
            currentState = new DeliveryTransitionStateBuilder()
                    .withSpell(state.getSpell())
                    .withStageIndex(state.getStageIndex())
                    .withWorld(state.getWorld())
                    .withPosition(hitPos)
                    .withBlockPosition(hitBlockPos)
                    .withDirection(hitPos.subtract(startPos).normalize())
                    .build();
        }
        this.procEffects(currentState);
        this.transitionFrom(currentState);
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state  The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    @Override
    public void defaultEffectProc(DeliveryTransitionState state, SpellEffect effect)
    {
        effect.procEffect(state);
    }

    /**
     * Performs the default transition from this delivery method to the next
     *
     * @param state The state of the spell to transition
     */
    @Override
    public void performDefaultTransition(DeliveryTransitionState state)
    {
        Spell spell = state.getSpell();
        int spellIndex = state.getStageIndex();
        // Perform the transition between the next delivery method and the current delivery method
        spell.getStage(spellIndex + 1).getDeliveryMethod().executeDelivery(new DeliveryTransitionStateBuilder()
                .copyOf(state)
                .withStageIndex(spellIndex + 1)
                .build());
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 15 + this.range * 0.5;
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    @Override
    public double getStageCostMultiplier()
    {
        return 1.2;
    }

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public SpellDeliveryMethodEntry getEntryRegistryType()
    {
        return ModSpellDeliveryMethods.LASER;
    }

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setDouble(NBT_RANGE, this.range);
        nbt.setBoolean(NBT_HIT_LIQUIDS, this.hitLiquids);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.range = nbt.getDouble(NBT_RANGE);
        this.hitLiquids = nbt.getBoolean(NBT_HIT_LIQUIDS);
    }

    /**
     * @return Gets the hitscan range
     */
    public double getRange()
    {
        return range;
    }
}
