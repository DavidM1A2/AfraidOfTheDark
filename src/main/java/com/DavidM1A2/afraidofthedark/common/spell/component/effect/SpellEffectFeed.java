package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

/**
 * Effect that feeds a hit player
 */
public class SpellEffectFeed extends AOTDSpellEffect
{
    // NBT constants
    private static final String NBT_HUNGER_VALUE = "hunger_value";
    private static final String NBT_SATURATION_VALUE = "saturation_value";

    // The amount of hunger bars this effect gives
    private int hungerValue = 2;
    // The amount of saturation this effect gives
    private int saturationValue = 1;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectFeed()
    {
        super();
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Hunger Amount")
                .withDescription("The amount of food half 'drumsticks' to restore.")
                .withSetter(newValue -> this.hungerValue = newValue)
                .withGetter(() -> this.hungerValue)
                .withDefaultValue(2)
                .withMinValue(1)
                .withMaxValue(300)
                .build());
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Saturation Amount")
                .withDescription("The amount of saturation restore.")
                .withSetter(newValue -> this.saturationValue = newValue)
                .withGetter(() -> this.saturationValue)
                .withDefaultValue(1)
                .withMinValue(0)
                .build());
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return this.hungerValue / 2.0 + this.saturationValue * 2.0;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        if (state.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) state.getEntity();
            createParticlesAt(1, 2, state.getPosition(), entityPlayer.dimension);
            FoodStats foodStats = entityPlayer.getFoodStats();
            foodStats.addStats(this.hungerValue, this.saturationValue);
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
        return ModSpellEffects.FEED;
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

        nbt.setInteger(NBT_HUNGER_VALUE, this.hungerValue);
        nbt.setInteger(NBT_SATURATION_VALUE, this.saturationValue);

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
        this.hungerValue = nbt.getInteger(NBT_HUNGER_VALUE);
        this.saturationValue = nbt.getInteger(NBT_SATURATION_VALUE);
    }
}
