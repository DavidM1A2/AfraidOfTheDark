package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.client.keybindings.ModKeybindings;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItemWithSharedCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Cloak of agility item used to dash around
 */
public class ItemCloakOfAgility extends AOTDItemWithSharedCooldown
{
    /**
     * Constructor sets up item properties
     */
    public ItemCloakOfAgility()
    {
        super("cloak_of_agility");
    }

    /**
     * Called to add a tooltip to the item
     *
     * @param stack   The itemstack to add a tooltip to
     * @param worldIn The world the item is in
     * @param tooltip The tooltip to add to
     * @param flagIn  True if the advanced flag is set or false otherwise
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        // If the player has the research show them what key is used to roll, otherwise tell them they don't know how to use the cloak
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.CLOAK_OF_AGILITY))
        {
            tooltip.add("Use " + Keyboard.getKeyName(ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY.getKeyCode()) + " to perform a roll in");
            tooltip.add("the current direction of movement");
        }
        else
        {
            tooltip.add("I'm not sure how to use this.");
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    @Override
    public int getItemCooldownInMilliseconds(ItemStack itemStack)
    {
        return 4000;
    }
}
