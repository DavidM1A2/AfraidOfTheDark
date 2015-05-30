package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.threads.TrollPoleCooldown;

public class ItemTrollPole extends AOTDItem
{
	private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;
	private static final int TROLL_POLE_COOLDOWN_IN_TICKS = 20000;
	private TrollPoleCooldown cooldown;
	private int previousResistanceDuration = 0;
	private int previousResistancePower = 0;
	private boolean previousResistanceIsAmbient = false;
	private boolean previousResistanceShowParticles = false;
	private boolean thereIsPreviousResistance = false;

	public ItemTrollPole()
	{
		super();
		this.setUnlocalizedName("trollPole");
		this.cooldown = new TrollPoleCooldown(ItemTrollPole.TROLL_POLE_COOLDOWN_IN_TICKS);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!this.cooldown.isAlive())
		{
			// Begin troll-poling
			if (entityPlayer.isPotionActive(Potion.resistance))
			{
				this.previousResistanceDuration = entityPlayer.getActivePotionEffect(Potion.resistance).getDuration();
				this.previousResistancePower = entityPlayer.getActivePotionEffect(Potion.resistance).getAmplifier();
				this.previousResistanceShowParticles = entityPlayer.getActivePotionEffect(Potion.resistance).getIsShowParticles();
				this.previousResistanceIsAmbient = entityPlayer.getActivePotionEffect(Potion.resistance).getIsAmbient();
				this.thereIsPreviousResistance = true;
			}
			entityPlayer.removePotionEffect(Potion.resistance.id);
			entityPlayer.addPotionEffect(new PotionEffect(Potion.resistance.id, ItemTrollPole.MAX_TROLL_POLE_TIME_IN_TICKS, 5, false, false));
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addVelocity(0, .5,	0);
			}
			entityPlayer.setItemInUse(itemStack, ItemTrollPole.MAX_TROLL_POLE_TIME_IN_TICKS);
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
	@Override
	public void onUsingTick(final ItemStack stack, final EntityPlayer player, int count)
	{
		count = ItemTrollPole.MAX_TROLL_POLE_TIME_IN_TICKS - count;
		if (count >= 3)
		{
			player.setVelocity(0, 0, 0);
		}
		if (count == (ItemTrollPole.MAX_TROLL_POLE_TIME_IN_TICKS - 1))
		{
			player.stopUsingItem();
		}
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 *
	 * @param timeLeft The amount of ticks left before the using would have been complete
	 */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityPlayer playerIn, final int timeLeft)
	{
		playerIn.fallDistance = 0.0f;
		playerIn.removePotionEffect(Potion.resistance.id);
		if (this.thereIsPreviousResistance)
		{
			playerIn.addPotionEffect(new PotionEffect(Potion.resistance.id, this.previousResistanceDuration, this.previousResistancePower, this.previousResistanceIsAmbient, this.previousResistanceShowParticles));
			if (!worldIn.isRemote)
			{
				this.thereIsPreviousResistance = false;
			}
		}
		this.cooldown = new TrollPoleCooldown(ItemTrollPole.TROLL_POLE_COOLDOWN_IN_TICKS);
		this.cooldown.start();
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return ItemTrollPole.MAX_TROLL_POLE_TIME_IN_TICKS;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced Whether the setting "Advanced tooltips" is enabled
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add(this.cooldown.isAlive() ? ("Cooldown remaining: " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")) : "Ready to Use");
	}
}
