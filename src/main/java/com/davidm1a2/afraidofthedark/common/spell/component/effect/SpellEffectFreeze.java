package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Spell effect that causes water to freeze and creates ice
 */
public class SpellEffectFreeze extends AOTDSpellEffect
{
    // NBT constants for freeze duration
    private static final String NBT_FREEZE_DURATION = "freeze_duration";

    // The duration that the freeze lasts in ticks
    private int freezeDuration = 20;

    /**
     * Constructor initializes properties
     */
    public SpellEffectFreeze()
    {
        this.addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withName("Duration")
                .withDescription("The number of ticks the freeze will last against entities.")
                .withSetter(newValue -> this.freezeDuration = newValue)
                .withGetter(() -> this.freezeDuration)
                .withDefaultValue(20)
                .withMinValue(1)
                .withMaxValue(1200)
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
        return 26 + this.freezeDuration * this.freezeDuration / 100.0;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        World world = state.getWorld();
        BlockPos blockPos = state.getBlockPosition();
        Entity entity = state.getEntity();
        // If the entity hit is living freeze it in place
        if (entity != null)
        {
            // We can only freeze a living entity
            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase entityLiving = (EntityLivingBase) entity;
                // If we hit a player, freeze their position and direction
                if (entityLiving instanceof EntityPlayer)
                {
                    IAOTDPlayerSpellFreezeData freezeData = entityLiving.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null);
                    freezeData.setFreezeTicks(this.freezeDuration);
                    freezeData.setFreezePosition(new Vec3d(entity.posX, entity.posY, entity.posZ));
                    freezeData.setFreezeDirection(entity.rotationYaw, entity.rotationPitch);
                }
                // If we hit an entity add slowness 99 to ensure the entity can't move
                else
                {
                    entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionById(2), this.freezeDuration, 99));
                }
            }
        }
        // If we hit a block set it to ice
        else
        {
            IBlockState hitBlock = world.getBlockState(blockPos);
            if (hitBlock.getBlock() == Blocks.WATER || hitBlock.getBlock() == Blocks.FLOWING_WATER)
            {
                world.setBlockState(blockPos, Blocks.ICE.getDefaultState());
            }
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
        return ModSpellEffects.FREEZE;
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

        nbt.setInteger(NBT_FREEZE_DURATION, this.freezeDuration);

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
        this.freezeDuration = nbt.getInteger(NBT_FREEZE_DURATION);
    }
}
