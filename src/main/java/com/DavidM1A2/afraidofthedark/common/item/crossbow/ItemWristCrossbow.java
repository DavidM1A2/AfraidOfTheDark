package com.DavidM1A2.afraidofthedark.common.item.crossbow;

import com.DavidM1A2.afraidofthedark.client.keybindings.ModKeybindings;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class representing a wrist-mounted crossbow
 */
public class ItemWristCrossbow extends AOTDItemWithPerItemCooldown
{
    /**
     * Constructor sets up item properties
     */
    public ItemWristCrossbow()
    {
        super("wrist_crossbow");
        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "is_loaded"), (stack, worldIn, entityIn) -> this.isOnCooldown(stack) ? 0 : 1);
    }

    /**
     * Adds tooltip text to the item
     *
     * @param stack   The itemstack to add text to
     * @param worldIn The world that this item is in
     * @param tooltip The tooltip to add to
     * @param flagIn  The flag telling us if advanced tooltips are on or off
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.WRIST_CROSSBOW))
        {
            tooltip.add("Use " + Keyboard.getKeyName(ModKeybindings.FIRE_WRIST_CROSSBOW.getKeyCode()) + " to fire a bolt in the current look direction.");
            tooltip.add("Crouch & " + Keyboard.getKeyName(ModKeybindings.FIRE_WRIST_CROSSBOW.getKeyCode()) + " to change bolt type.");
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
        return 3000;
    }
}
