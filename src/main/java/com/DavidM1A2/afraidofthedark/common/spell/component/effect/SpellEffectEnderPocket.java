package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.util.math.Vec3d;

/**
 * Effect that creates an ender chest
 */
public class SpellEffectEnderPocket extends AOTDSpellEffect
{
    /**
     * Constructor just calls super
     */
    public SpellEffectEnderPocket()
    {
        super();
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 45;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        // If we hit a player open the ender chest GUI
        if (state.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) state.getEntity();
            createParticlesAt(3, 5, new Vec3d(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ), entityPlayer.dimension);
            InventoryEnderChest enderChest = entityPlayer.getInventoryEnderChest();
            entityPlayer.displayGUIChest(enderChest);
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
        return ModSpellEffects.ENDER_POCKET;
    }
}
