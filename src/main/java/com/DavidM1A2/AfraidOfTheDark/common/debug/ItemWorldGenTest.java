/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.google.common.base.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWorldGenTest extends AOTDItem
{
	public ItemWorldGenTest()
	{
		super();
		this.setUnlocalizedName("worldGenTest");
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItemMainhand();
		//entityPlayer.openGui(Reference.MOD_ID, GuiHandler.SPELL_SELECTION_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());

		/*TargetPoint particleCenter = new TargetPoint(entityPlayer.dimension, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, 40);
		if (!world.isRemote)
			for (int i = 0; i < 50; i++)
			{
				AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncParticleFX(AOTDParticleFXTypes.EnariaSplash, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ), particleCenter);
			}
		LogHelper.info(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasBeatenEnaria());
		*/

		if (!entityPlayer.isSneaking())
		{
			EntityGhastlyEnaria enaria = new EntityGhastlyEnaria(world);
			enaria.setBenign(true);
			enaria.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
			if (!world.isRemote)
				world.spawnEntityInWorld(enaria);
		}

		if (entityPlayer.isSneaking())
			for (EntityGhastlyEnaria entity : world.getEntities(EntityGhastlyEnaria.class, new Predicate<EntityGhastlyEnaria>()
			{
				@Override
				public boolean apply(EntityGhastlyEnaria input)
				{
					return true;
				}
			}))
			{
				LogHelper.info("killed" + entity);
				entity.onKillCommand();
				entity.setDead();
			}

		/*
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationPreTeleport(new Point3D(0, 100, 0), 0);
		*/

		/*
		Success: Basically what return true used to be. The call has succeeded in doing what was needed and should stop here.
		Pass: The call succeeded, but more calls can be made farther down the call stack.
		Fail: Basically what return false used to be. The call has failed to do what was intended and should stop here.
		*/
		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, itemStack);
	}
}
