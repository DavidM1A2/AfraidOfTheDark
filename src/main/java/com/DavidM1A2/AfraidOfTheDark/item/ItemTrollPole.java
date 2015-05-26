package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.threads.TrollPoleCooldown;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrollPole extends AOTDItem
{
	private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;
	private TrollPoleCooldown cooldown;
	
	public ItemTrollPole()
	{
		super();
		this.setUnlocalizedName("trollPole");
		this.cooldown = new TrollPoleCooldown(20000);
	}
	
	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!this.cooldown.isAlive())
		{
			entityPlayer.setItemInUse(itemStack, MAX_TROLL_POLE_TIME_IN_TICKS);
		}
		else
		{
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("This item will be on cooldown for another " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")));
			}			
		}

		return itemStack;
	}
	
	/**
     * Called each tick while using an item.
     * @param stack The Item being used
     * @param player The Player using the item
     * @param count The amount of time in tick the item has been used for continuously
     */
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
    	count = MAX_TROLL_POLE_TIME_IN_TICKS - count;
    	if (count == 0)
    	{
    		player.setVelocity(0, 0.5, 0);
    	}
    	if (count >= 3)
    	{
    		LogHelper.info("InAir? " + count);
    		player.setVelocity(0, 0, 0);
    		player.setInvisible(true);
    	}
    	if (count == MAX_TROLL_POLE_TIME_IN_TICKS - 1)
    	{
    		player.stopUsingItem();
    		player.fallDistance = 0;
    	}
    }
    
    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     *  
     * @param timeLeft The amount of ticks left before the using would have been complete
     */
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) 
    {
    	playerIn.setInvisible(false);
    	
    	playerIn.fallDistance = 0.0f;

		this.cooldown = new TrollPoleCooldown(20000);
		this.cooldown.start();
    }
    
    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
    	tooltip.add(this.cooldown.isAlive() ? ("Cooldown remaining: " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")) : "Ready to Use");
    }

}
