package com.davidm1a2.afraidofthedark.client.keybindings

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.ItemCloakOfAgility
import com.davidm1a2.afraidofthedark.common.item.crossbow.ItemWristCrossbow
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.FireWristCrossbow
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncSpellKeyPress
import com.davidm1a2.afraidofthedark.common.utility.BoltOrderHelper
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard

/**
 * Class that receives all keyboard events and processes them accordingly
 */
object KeyInputEventHandler
{
    private const val ROLL_VELOCITY = 3.0

    /**
     * Called whenever a key is pressed
     *
     * @param event The key event containing press information
     */
    @SubscribeEvent
    fun handleKeyInputEvent(event: KeyInputEvent)
    {
        // Process input
        if (ModKeybindings.FIRE_WRIST_CROSSBOW.isPressed)
        {
            fireWristCrossbow()
        }

        if (ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY.isPressed)
        {
            rollWithCloakOfAgility()
        }

        // If a key was pressed and it is bound to a spell fire the spell
        if (Keyboard.getEventKeyState() && KeybindingUtils.keybindableKeyDown())
        {
            // Grab the currently held bind
            val keybindingPressed = KeybindingUtils.getCurrentlyHeldKeybind()
            // If that keybind exists then tell the server to fire the spell
            if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!.keybindExists(keybindingPressed))
            {
                AfraidOfTheDark.INSTANCE.packetHandler.sendToServer(SyncSpellKeyPress(keybindingPressed))
            }
        }
    }

    /**
     * Call to attempt firing the wrist crossbow
     */
    private fun fireWristCrossbow()
    {
        // Grab a player reference
        val entityPlayer: EntityPlayer = Minecraft.getMinecraft().player
        // Grab the player's bolt of choice
        val playerBasics = entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null)!!
        // If the player is sneaking change the mode
        if (entityPlayer.isSneaking)
        {
            // Advance the current index
            var currentBoltIndex = playerBasics.selectedWristCrossbowBoltIndex
            // Compute the next bolt index
            currentBoltIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltIndex)
            // Set the selected index and sync the index
            playerBasics.selectedWristCrossbowBoltIndex = currentBoltIndex
            playerBasics.syncSelectedWristCrossbowBoltIndex(entityPlayer)
            // Tell the player what type of bolt will be fired now
            entityPlayer.sendMessage(TextComponentTranslation("aotd.wrist_crossbow.bolt_change",
                    TextComponentTranslation(BoltOrderHelper.getBoltAt(currentBoltIndex).unLocalizedName)))
        }
        else
        {
            // Test if the player has the correct research
            if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.WRIST_CROSSBOW))
            {
                // Test if the player has a wrist crossbow to shoot with
                if (entityPlayer.inventory.hasItemStack(ItemStack(ModItems.WRIST_CROSSBOW, 1, 0)))
                {
                    // Grab the currently selected bolt type
                    val boltType = BoltOrderHelper.getBoltAt(playerBasics.selectedWristCrossbowBoltIndex)
                    // Ensure the player has a bolt of the right type in his/her inventory or is in creative mode
                    if (entityPlayer.inventory.hasItemStack(ItemStack(boltType.boltItem, 1, 0)) || entityPlayer.isCreative)
                    {
                        // Find the wrist crossbow item in the player's inventory
                        for (itemStack in entityPlayer.inventory.mainInventory)
                        {
                            if (itemStack.item is ItemWristCrossbow)
                            {
                                // Grab the crossbow item reference
                                val wristCrossbow = itemStack.item as ItemWristCrossbow
                                // Test if the crossbow is on CD or not. If it is fire, if it is not continue searching
                                if (!wristCrossbow.isOnCooldown(itemStack))
                                {
                                    // Tell the server to fire the crossbow
                                    AfraidOfTheDark.INSTANCE.packetHandler.sendToServer(FireWristCrossbow(boltType))
                                    // Set the item on CD
                                    wristCrossbow.setOnCooldown(itemStack, entityPlayer)
                                    // Return, we fired the bolt
                                    return
                                }
                            }
                        }
                        // No valid wrist crossbow found
                        entityPlayer.sendMessage(TextComponentTranslation("aotd.wrist_crossbow.reloading"))
                    }
                    else
                    {
                        entityPlayer.sendMessage(TextComponentTranslation("aotd.wrist_crossbow.no_bolt", TextComponentTranslation(boltType.unLocalizedName)))
                    }
                }
                else
                {
                    entityPlayer.sendMessage(TextComponentTranslation("aotd.wrist_crossbow.no_crossbow"))
                }
            }
            else
            {
                entityPlayer.sendMessage(TextComponentTranslation("aotd.dont_understand"))
            }
        }
    }

    /**
     * Call to attempt rolling with the cloak of agility
     */
    private fun rollWithCloakOfAgility()
    {
        // Grab a player reference
        val entityPlayer: EntityPlayer = Minecraft.getMinecraft().player
        // Test if the player has the correct research
        if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.CLOAK_OF_AGILITY))
        {
            // Ensure the player is on the ground
            if (entityPlayer.onGround)
            {
                // Test if the player has a cloak of agility in their inventory
                for (itemStack in entityPlayer.inventory.mainInventory)
                {
                    // If the itemstack is a cloak set it on cooldown and dash
                    if (itemStack.item is ItemCloakOfAgility)
                    {
                        val cloakOfAgility = itemStack.item as ItemCloakOfAgility
                        // Ensure the cloak is not on cooldown
                        if (!cloakOfAgility.isOnCooldown(itemStack))
                        {
                            // Set the cloak on CD
                            cloakOfAgility.setOnCooldown(itemStack, entityPlayer)
                            // If the player is not moving roll in the direction the player is looking, otherwise roll in the direction the player is moving
                            var motionDirection = if (entityPlayer.motionX <= 0.01 && entityPlayer.motionX >= -0.01 && entityPlayer.motionZ <= 0.01 && entityPlayer.motionZ >= -0.01)
                            {
                                val lookDirection = entityPlayer.lookVec
                                Vec3d(lookDirection.x, 0.0, lookDirection.z)
                            }
                            else
                            {
                                Vec3d(entityPlayer.motionX, 0.0, entityPlayer.motionZ)
                            }
                            // Normalize the motion vector
                            motionDirection = motionDirection.normalize()
                            // Update the player's motion in the new direction
                            entityPlayer.motionX = motionDirection.x * ROLL_VELOCITY
                            entityPlayer.motionY = 0.2
                            entityPlayer.motionZ = motionDirection.z * ROLL_VELOCITY
                            // Return
                            return
                        }
                        else
                        {
                            entityPlayer.sendMessage(TextComponentTranslation("aotd.cloak_of_agility.too_tired", cloakOfAgility.cooldownRemainingInSeconds(itemStack)))
                            // If one cloak is on cooldown they all are, return
                            return
                        }
                    }
                }
            }
            else
            {
                entityPlayer.sendMessage(TextComponentTranslation("aotd.cloak_of_agility.not_grounded"))
            }
        }
        else
        {
            entityPlayer.sendMessage(TextComponentTranslation("aotd.dont_understand"))
        }
    }
}