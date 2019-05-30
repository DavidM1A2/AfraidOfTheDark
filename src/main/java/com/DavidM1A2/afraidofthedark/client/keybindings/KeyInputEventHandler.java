package com.DavidM1A2.afraidofthedark.client.keybindings;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.FireWristCrossbow;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncSpellKeyPress;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.utility.BoltOrderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Class that receives all keyboard events and processes them accordingly
 */
public class KeyInputEventHandler
{
    private static final double ROLL_VELOCITY = 3;

    /**
     * Called whenever a key is pressed
     *
     * @param event The key event containing press information
     */
    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        // Process input
        if (ModKeybindings.FIRE_WRIST_CROSSBOW.isPressed())
        {
            this.fireWristCrossbow();
        }
        if (ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY.isPressed())
        {
            this.rollWithCloakOfAgility();
        }
        // If a key was pressed and it is bound to a spell fire the spell
        if (Keyboard.getEventKeyState() && KeybindingUtils.keybindableKeyDown())
        {
            // Grab the currently held bind
            String keybindingPressed = KeybindingUtils.getCurrentlyHeldKeybind();
            // If that keybind exists then tell the server to fire the spell
            if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null).keybindExists(keybindingPressed))
            {
                AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncSpellKeyPress(keybindingPressed));
            }
        }
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
            entityPlayer.sendMessage(new TextComponentTranslation("aotd.wrist_crossbow.bolt_change", new TextComponentTranslation(BoltOrderHelper.getBoltAt(currentBoltIndex).getUnLocalizedName())));
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
                        entityPlayer.sendMessage(new TextComponentTranslation("aotd.wrist_crossbow.reloading"));
                    }
                    else
                    {
                        entityPlayer.sendMessage(new TextComponentTranslation("aotd.wrist_crossbow.no_bolt", new TextComponentTranslation(boltType.getUnLocalizedName())));
                    }
                }
                else
                {
                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.wrist_crossbow.no_crossbow"));
                }
            }
            else
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.dont_understand"));
            }
        }
    }

    /**
     * Call to attempt rolling with the cloak of agility
     */
    private void rollWithCloakOfAgility()
    {
        // Grab a player reference
        EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
        // Test if the player has the correct research
        if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.CLOAK_OF_AGILITY))
        {
            // Ensure the player is on the ground
            if (entityPlayer.onGround)
            {
                // Test if the player has a cloak of agility in their inventory
                for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
                {
                    // If the itemstack is a cloak set it on cooldown and dash
                    if (itemStack.getItem() instanceof ItemCloakOfAgility)
                    {
                        ItemCloakOfAgility cloakOfAgility = (ItemCloakOfAgility) itemStack.getItem();
                        // Ensure the cloak is not on cooldown
                        if (!cloakOfAgility.isOnCooldown(itemStack))
                        {
                            // Set the cloak on CD
                            cloakOfAgility.setOnCooldown(itemStack, entityPlayer);
                            // Roll with the cloak
                            Vec3d motionDirection;
                            // If the player is not moving roll in the direction the player is looking, otherwise roll in the direction the player is moving
                            if (entityPlayer.motionX <= 0.01 && entityPlayer.motionX >= -0.01 && entityPlayer.motionZ <= 0.01 && entityPlayer.motionZ >= -0.01)
                            {
                                Vec3d lookDirection = entityPlayer.getLookVec();
                                motionDirection = new Vec3d(lookDirection.x, 0, lookDirection.z);
                            }
                            else
                            {
                                motionDirection = new Vec3d(entityPlayer.motionX, 0, entityPlayer.motionZ);
                            }
                            // Normalize the motion vector
                            motionDirection = motionDirection.normalize();
                            // Update the player's motion in the new direction
                            entityPlayer.motionX = motionDirection.x * ROLL_VELOCITY;
                            entityPlayer.motionY = 0.2;
                            entityPlayer.motionZ = motionDirection.z * ROLL_VELOCITY;
                            // Return
                            return;
                        }
                        else
                        {
                            entityPlayer.sendMessage(new TextComponentTranslation("aotd.cloak_of_agility.too_tired", cloakOfAgility.cooldownRemainingInSeconds(itemStack)));
                            // If one cloak is on cooldown they all are, return
                            return;
                        }
                    }
                }
            }
            else
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.cloak_of_agility.not_grounded"));
            }
        }
        else
        {
            entityPlayer.sendMessage(new TextComponentTranslation("aotd.dont_understand"));
        }
    }
}
