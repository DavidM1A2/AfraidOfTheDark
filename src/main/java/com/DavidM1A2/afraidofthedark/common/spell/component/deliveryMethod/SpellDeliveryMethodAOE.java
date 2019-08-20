package com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.*;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * AOE method delivers the spell to the target in a circle
 */
public class SpellDeliveryMethodAOE extends AOTDSpellDeliveryMethod
{
    // The NBT keys
    private static final String NBT_RADIUS = "radius";
    private static final String NBT_TARGET_TYPE = "target_type";

    // Default property values
    private static final double DEFAULT_RADIUS = 3.0;

    // The radius of the projectile
    private double radius = DEFAULT_RADIUS;
    // If the AOE should target entities (true) or blocks (false)
    private boolean targetEntities = false;

    /**
     * Constructor initializes the editable properties
     */
    public SpellDeliveryMethodAOE()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Radius",
                "The radius of the AOE in blocks",
                () -> Double.toString(this.radius),
                newValue ->
                {
                    // Ensure the number is parsable
                    if (NumberUtils.isParsable(newValue))
                    {
                        // Parse the radius
                        this.radius = Double.parseDouble(newValue);
                        // Ensure radius is valid
                        if (this.radius >= 1.0)
                        {
                            return null;
                        }
                        else
                        {
                            this.radius = DEFAULT_RADIUS;
                            return "Radius must be larger than or equal to 1";
                        }
                    }
                    // If it's not valid return an error
                    else
                    {
                        return newValue + " is not a valid decimal number!";
                    }
                }));
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Target Type",
                "Should be either 'entity' or 'block'. If the target type is 'block' all nearby blocks will be affected, if it is 'entity' all nearby entities will be affected.",
                () -> this.targetEntities ? "entity" : "block",
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
                        return "Invalid value " + newValue + ", should be 'entity' or 'block'";
                    }
                    return null;
                }));
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    @Override
    public void deliver(DeliveryTransitionState state)
    {
        this.performAOEEffects(state);

        Spell spell = state.getSpell();
        int spellIndex = state.getStageIndex();

        if (spell.hasStage(spellIndex + 1))
        {
            // Grab the next delivery method
            SpellDeliveryMethod nextDeliveryMethod = spell.getStage(spellIndex + 1).getDeliveryMethod();
            // Perform the transition between the next delivery method and the current delivery method
            nextDeliveryMethod.getEntryRegistryType().getTransitioner(this.getEntryRegistryType()).transition(state);
        }
    }

    /**
     * Performs effects in an AOE at a transition state
     *
     * @param state The state of the spell to deliver
     */
    private void performAOEEffects(DeliveryTransitionState state)
    {
        // This AOE should target entities hit all nearby entities
        if (this.targetEntities)
        {
            // A list of nearby entities
            List<Entity> entitiesWithinAABB = state.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(new BlockPos(state.getPosition())).grow(this.radius));
            // Go over each nearby entity
            entitiesWithinAABB.forEach(entity ->
            {
                Spell spell = state.getSpell();
                int stageIndex = state.getStageIndex();
                // Go through each effect and apply it to the entity
                spell.getStage(stageIndex).forAllValidEffects((spellEffect, index) ->
                {
                    ISpellDeliveryEffectApplicator effectApplicator = this.getEntryRegistryType().getApplicator(spellEffect.getEntryRegistryType());
                    effectApplicator.applyEffect(spell, stageIndex, index, entity);
                });
            });
        }
        else
        {
            // Grab references to
            BlockPos basePos = new BlockPos(state.getPosition());
            Spell spell = state.getSpell();
            int stageIndex = state.getStageIndex();
            // Compute the radius in blocks
            int blockRadius = MathHelper.floor(this.radius);
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
                            // Go through each effect and apply it to the position
                            spell.getStage(stageIndex).forAllValidEffects((spellEffect, index) ->
                            {
                                ISpellDeliveryEffectApplicator effectApplicator = this.getEntryRegistryType().getApplicator(spellEffect.getEntryRegistryType());
                                effectApplicator.applyEffect(spell, stageIndex, index, state.getWorld(), aoePos);
                            });
                        }
                    }
                }
            }
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
}
