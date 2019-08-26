package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Spell effect that applies potion effects
 */
public class SpellEffectPotionEffect extends AOTDSpellEffect
{
    // NBT constants spell potion stats
    private static final String NBT_POTION_TYPE = "potion_type";
    private static final String NBT_POTION_STRENGTH = "potion_strength";
    private static final String NBT_POTION_DURATION = "potion_duration";

    // Field defaults
    private static final Potion DEFAULT_POTION_TYPE = Potion.getPotionFromResourceLocation("minecraft:speed");
    private static final int DEFAULT_POTION_STRENGTH = 0;
    private static final int DEFAULT_POTION_DURATION = 20;

    // Spell potion fields
    private Potion potionType = DEFAULT_POTION_TYPE;
    private int potionStrength = DEFAULT_POTION_STRENGTH;
    private int potionDuration = DEFAULT_POTION_DURATION;

    /**
     * Constructor adds the editable props
     */
    public SpellEffectPotionEffect()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Potion Type",
                "The type of potion effect to apply. Must be using the minecraft naming convention, like 'minecraft:speed'.",
                () -> this.potionType.getRegistryName().toString(),
                newValue ->
                {
                    // Grab the potion associated with the text
                    Potion type = Potion.REGISTRY.getObject(new ResourceLocation(newValue));
                    // Since type will be a default value if newValue is invalid we need to test if this is in fact a default value or not
                    if (type != null)
                    {
                        potionType = type;
                        return null;
                    }
                    else
                    {
                        return "Invalid potion type " + newValue + ", it was not found in the registry.";
                    }
                }));
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Potion Strength",
                "The level of the potion to apply, ex. 4 means apply 'Potion Type' at level 4",
                () -> Integer.toString(this.potionStrength + 1),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the strength, subtract 1 to account for 0 based counting
                        this.potionStrength = Integer.parseInt(newValue) - 1;
                        // Ensure strength is valid
                        if (this.potionStrength >= 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.potionStrength = DEFAULT_POTION_STRENGTH;
                            return "Potion strength must be at least level 1.";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Potion Duration",
                "The number of ticks the potion effect should run for.",
                () -> Integer.toString(this.potionDuration),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the duration
                        this.potionDuration = Integer.parseInt(newValue);
                        // Ensure duration is valid
                        if (this.potionDuration > 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.potionDuration = DEFAULT_POTION_DURATION;
                            return "Potion duration must be at least level 1.";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 15 + this.potionDuration / 5.0 * this.potionStrength;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        if (state.getEntity() != null)
        {
            if (state.getEntity() instanceof EntityLivingBase)
            {
                Entity entityHit = state.getEntity();
                this.createParticlesAt(1, 3, new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ), entityHit.dimension);
                ((EntityLivingBase) entityHit).addPotionEffect(new PotionEffect(this.potionType, this.potionDuration, this.potionStrength));
            }
        }
        else
        {
            World world = state.getWorld();
            BlockPos position = new BlockPos(state.getPosition());
            // Move the hit pos up if we didn't hit an air block
            if (!(world.getBlockState(position).getBlock() instanceof BlockAir))
            {
                position = position.up();
            }
            EntityAreaEffectCloud aoePotion = new EntityAreaEffectCloud(world, position.getX(), position.getY(), position.getZ());
            aoePotion.addEffect(new PotionEffect(this.potionType, this.potionDuration, this.potionStrength));
            aoePotion.setOwner(state.getSpell().getOwner(world));
            aoePotion.setRadius(2.0f);
            aoePotion.setRadiusPerTick(0);
            aoePotion.setDuration(this.potionDuration);
            world.spawnEntity(aoePotion);
            this.createParticlesAt(4, 8, new Vec3d(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5), world.provider.getDimension());
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.POTION_EFFECT;
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

        nbt.setString(NBT_POTION_TYPE, this.potionType.getRegistryName().toString());
        nbt.setInteger(NBT_POTION_DURATION, this.potionDuration);
        nbt.setInteger(NBT_POTION_STRENGTH, this.potionStrength);

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

        this.potionType = Potion.getPotionFromResourceLocation(nbt.getString(NBT_POTION_TYPE));
        this.potionDuration = nbt.getInteger(NBT_POTION_DURATION);
        this.potionStrength = nbt.getInteger(NBT_POTION_STRENGTH);
    }
}
