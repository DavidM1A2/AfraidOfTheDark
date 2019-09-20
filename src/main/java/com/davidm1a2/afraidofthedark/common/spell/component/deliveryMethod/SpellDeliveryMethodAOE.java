package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AOE method delivers the spell to the target in a circle
 */
public class SpellDeliveryMethodAOE extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_RADIUS = "radius";
    private static final String NBT_TARGET_TYPE = "target_type";

    // The radius of the projectile
    private double radius = 3;
    // If the AOE should target entities (true) or blocks (false)
    private boolean targetEntities = false;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodAOE()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.doubleProperty()
                .withName("Radius")
                .withDescription("The area of effect radius in blocks.")
                .withSetter(newValue -> this.radius = newValue)
                .withGetter(() -> this.radius)
                .withDefaultValue(3D)
                .withMinValue(1D)
                .withMaxValue(150D)
                .build());
        this.addEditableProperty(new SpellComponentProperty(
                "Target Type",
                "Should be either 'entity' or 'block'. If the target type is 'block' all nearby blocks will be affected, if it is 'entity' all nearby entities will be affected.",
                newValue ->
                {
                    // Check the two valid options first
                    if (newValue.equalsIgnoreCase("entity"))
                    {
                        this.targetEntities = true;
                    }
                    else if (newValue.equalsIgnoreCase("block"))
                    {
                        this.targetEntities = false;
                    }
                    else
                    {
                        throw new InvalidValueException("Invalid value " + newValue + ", should be 'entity' or 'block'");
                    }
                },
                () -> this.targetEntities ? "entity" : "block"));
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void executeDelivery(DeliveryTransitionState state)
    {
        // AOE just procs the effects and transitions in a sphere
        this.procEffects(state);
        this.transitionFrom(state);
    }

    /**
     * Applies a given effect given the spells current state
     *
     * @param state The state of the spell at the current delivery method
     * @param effect The effect that needs to be applied
     */
    @Override
    public void defaultEffectProc(DeliveryTransitionState state, SpellEffect effect)
    {
        // This AOE should target entities hit all nearby entities
        if (this.targetEntities)
        {
            // A list of nearby entities
            List<Entity> entitiesWithinAABB = state.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(new BlockPos(state.getPosition())).grow(this.radius));
            // Go over each nearby entity
            entitiesWithinAABB.forEach(entity ->
            {
                // Apply it to the entity
                effect.procEffect(new DeliveryTransitionStateBuilder()
                        .withSpell(state.getSpell())
                        .withStageIndex(state.getStageIndex())
                        .withEntity(entity)
                        .build());
            });
        }
        else
        {
            // Grab references to
            BlockPos basePos = new BlockPos(state.getPosition());
            // Compute the radius in blocks
            int blockRadius = MathHelper.floor(this.radius);
            DeliveryTransitionStateBuilder transitionBuilder = new DeliveryTransitionStateBuilder()
                    .withSpell(state.getSpell())
                    .withStageIndex(state.getStageIndex())
                    .withWorld(state.getWorld());
            // Go over every block in the radius
            for (int x = -blockRadius; x <= blockRadius; x++)
            {
                for (int y = -blockRadius; y <= blockRadius; y++)
                {
                    for (int z = -blockRadius; z <= blockRadius; z++)
                    {
                        // Grab the blockpos
                        BlockPos aoePos = basePos.add(x, y, z);
                        // Test to see if the block is within the radius
                        if (aoePos.distanceSq(basePos) < radius * radius)
                        {
                            // Apply the effect at the position
                            effect.procEffect(transitionBuilder
                                    .withPosition(new Vec3d(aoePos.getX(), aoePos.getY(), aoePos.getZ()))
                                    .withBlockPosition(aoePos)
                                    .build());
                        }
                    }
                }
            }
        }
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
        if (this.targetEntities)
        {
            // A list of nearby entities
            List<Entity> entitiesWithinAABB = state.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(new BlockPos(state.getPosition())).grow(this.radius));
            // Go over each nearby entity
            entitiesWithinAABB.forEach(entity ->
            {
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1).getDeliveryMethod().executeDelivery(new DeliveryTransitionStateBuilder()
                        .withSpell(state.getSpell())
                        .withStageIndex(spellIndex + 1)
                        .withEntity(entity)
                        .build());
            });
        }
        else
        {
            DeliveryTransitionStateBuilder deliveryTransitionStateBuilder = new DeliveryTransitionStateBuilder()
                    .withSpell(state.getSpell())
                    .withStageIndex(spellIndex + 1)
                    .withWorld(state.getWorld())
                    .withBlockPosition(state.getBlockPosition());

            // Send out deliveries in all 6 possible directions around the hit point
            ArrayList<Vec3d> cardinalDirections = Lists.newArrayList(
                    new Vec3d(1, 0, 0),
                    new Vec3d(0, 1, 0),
                    new Vec3d(0, 0, 1),
                    new Vec3d(-1, 0, 0),
                    new Vec3d(0, -1, 0),
                    new Vec3d(0, 0, -1)
            );
            // Randomize which order the directions get applied in
            Collections.shuffle(cardinalDirections);
            cardinalDirections.forEach(direction ->
            {
                // Perform the transition between the next delivery method and the current delivery method
                spell.getStage(spellIndex + 1).getDeliveryMethod().executeDelivery(deliveryTransitionStateBuilder
                        .withPosition(state.getPosition().add(direction.scale(0.2)))
                        .withDirection(direction)
                        .build());
            });
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    @Override
    public double getCost()
    {
        return 10 + this.radius * this.radius;
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    @Override
    public double getStageCostMultiplier()
    {
        return 3;
    }

    /**
     * Should get the SpellDeliveryMethodEntry registry's type
     *
     * @return The registry entry that this delivery method was built with, used for deserialization
     */
    @Override
    public SpellDeliveryMethodEntry getEntryRegistryType()
    {
        return ModSpellDeliveryMethods.AOE;
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

        nbt.setDouble(NBT_RADIUS, this.radius);
        nbt.setBoolean(NBT_TARGET_TYPE, this.targetEntities);

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
        this.radius = nbt.getDouble(NBT_RADIUS);
        this.targetEntities = nbt.getBoolean(NBT_TARGET_TYPE);
    }

    ///
    /// Getters
    ///

    public double getRadius()
    {
        return radius;
    }

    public boolean shouldTargetEntities()
    {
        return targetEntities;
    }
}
