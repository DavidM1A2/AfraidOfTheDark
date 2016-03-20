/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.Projectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Explosion;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.Self;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!entityPlayer.isSneaking())
			entityPlayer.openGui(Refrence.MOD_ID, GuiHandler.SPELL_SELECTION_ID, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
		else
		{
			List<IEffect> effects = new LinkedList<IEffect>();
			effects.add(new Explosion());
			List<IEffect> effects2 = new LinkedList<IEffect>();
			effects2.add(new Explosion());
			List<IEffect> effects3 = new LinkedList<IEffect>();
			effects3.add(new Explosion());
			List<IEffect> effects4 = new LinkedList<IEffect>();
			effects4.add(new Explosion());
			List<IEffect> effects5 = new LinkedList<IEffect>();
			effects5.add(new Explosion());
			SpellStage[] stages = new SpellStage[]
			{ new SpellStage(new Projectile(), effects), new SpellStage(new Projectile(), effects2), new SpellStage(new Projectile(), effects3), new SpellStage(new Projectile(), effects4), new SpellStage(new Projectile(), effects5) };
			if (entityPlayer.worldObj.isRemote)
			{
				Spell temp = new Spell(entityPlayer, "GG " + Double.toString(Math.random()).substring(0, 5), new Self(), stages, UUID.randomUUID());
				LogHelper.info("Adding spell " + temp.getName());
				AOTDPlayerData.get(entityPlayer).getSpellManager().addSpell(temp);
				AOTDPlayerData.get(entityPlayer).syncSpellManager();
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}
}
