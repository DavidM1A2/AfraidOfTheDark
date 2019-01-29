package com.DavidM1A2.afraidofthedark.client.keybindings;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.FireWristCrossbow;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.AOTDBoltEntry;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.utility.BoltOrderHelper;
import com.google.common.collect.Iterators;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Class that receives all keyboard events and processes them accordingly
 */
public class KeyInputEventHandler
{
	/**
	 * Called whenever a key is pressed
	 *
	 * @param event The key event containing press information
	 */
	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
	{
		if (ModKeybindings.FIRE_WRIST_CROSSBOW.isPressed())
			this.fireWristCrossbow();
	}

	/**
	 * Call to attempt firing the wrist crossbow
	 */
	private void fireWristCrossbow()
	{
		// Grab a player reference
		EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
		// Grab the player's bolt of choice
		IAOTDPlayerBasics playerBasics = entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null);
		// If the player is sneaking change the mode
		if (entityPlayer.isSneaking())
		{
			// Advance the current index
			int currentBoltIndex = playerBasics.getSelectedWristCrossbowBoltIndex();
			// Compute the next bolt index
			currentBoltIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltIndex);
			// Set the selected index and sync the index
			playerBasics.setSelectedWristCrossbowBoltIndex(currentBoltIndex);
			playerBasics.syncSelectedWristCrossbowBoltIndex(entityPlayer);
			// Tell the player what type of bolt will be fired now
			entityPlayer.sendMessage(new TextComponentString("Wrist crossbow will now fire " + BoltOrderHelper.getBoltAt(currentBoltIndex).getLocalizedName().toLowerCase() + " bolts."));
		}
		// Fire a bolt
		else
		{
			// Test if the player has the correct research
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.WRIST_CROSSBOW))
			{
				// Test if the player has a wrist crossbow to shoot with
				if (entityPlayer.inventory.hasItemStack(new ItemStack(ModItems.WRIST_CROSSBOW, 1, 0)))
				{
					// Grab the currently selected bolt type
					BoltEntry boltType = BoltOrderHelper.getBoltAt(playerBasics.getSelectedWristCrossbowBoltIndex());
					// Ensure the player has a bolt of the right type in his/her inventory or is in creative mode
					if (entityPlayer.inventory.hasItemStack(new ItemStack(boltType.getBoltItem(), 1, 0)) || entityPlayer.isCreative())
					{
						// Find the wrist crossbow item in the player's inventory
						for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
						{
							if (itemStack.getItem() instanceof ItemWristCrossbow)
							{
								// Grab the crossbow item reference
								ItemWristCrossbow wristCrossbow = (ItemWristCrossbow) itemStack.getItem();
								// Test if the crossbow is on CD or not. If it is fire, if it is not continue searching
								if (!wristCrossbow.isOnCooldown(itemStack))
								{
									// Tell the server to fire the crossbow
									AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new FireWristCrossbow(boltType));
									// Set the item on CD
									wristCrossbow.setOnCooldown(itemStack, entityPlayer);

									// Return, we fired the bolt
									return;
								}
							}
						}
						// No valid wrist crossbow found
						entityPlayer.sendMessage(new TextComponentString("Wrist crossbow(s) are reloading..."));
					}
					else
					{
						entityPlayer.sendMessage(new TextComponentString("I'll need a " + boltType.getLocalizedName().toLowerCase() + " bolt in my inventory to shoot."));
					}
				}
				else
				{
					entityPlayer.sendMessage(new TextComponentString("I'll need a wrist crossbow in my inventory to shoot."));
				}
			}
			else
			{
				entityPlayer.sendMessage(new TextComponentString("I don't understand how this works."));
			}
		}
	}
}
