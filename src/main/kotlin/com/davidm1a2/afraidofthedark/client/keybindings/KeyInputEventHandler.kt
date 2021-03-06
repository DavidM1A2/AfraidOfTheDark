package com.davidm1a2.afraidofthedark.client.keybindings

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler.Companion.ROLL_VELOCITY
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.CloakOfAgilityItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.WristCrossbowItem
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.FireWristCrossbowPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.SpellKeyPressPacket
import com.davidm1a2.afraidofthedark.common.utility.BoltOrderHelper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

/**
 * Class that receives all keyboard events and processes them accordingly
 *
 * @property ROLL_VELOCITY The velocity which the player rolls with the cloak of agility
 */
class KeyInputEventHandler {
    /**
     * Called whenever a key is pressed
     *
     * @param event The key event containing press information
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun handleKeyInputEvent(event: InputEvent.KeyInputEvent) {
        // This gets fired in the main menu, or when we have an inventory open. In either case return
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().currentScreen != null) {
            return
        }

        // Process input
        if (ModKeybindings.FIRE_WRIST_CROSSBOW.isPressed) {
            fireWristCrossbow()
        }

        if (ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY.isPressed) {
            rollWithCloakOfAgility()
        }

        // If a key was pressed and it is bound to a spell fire the spell
        if (event.action == GLFW.GLFW_PRESS && KeybindingUtils.isKeyBindable(event.key)) {
            // Grab the currently held bind
            val keybindingPressed = KeybindingUtils.getCurrentlyHeldKeybind(event.key, event.scanCode)
            // If that keybind exists then tell the server to fire the spell
            if (Minecraft.getInstance().player!!.getSpellManager().keybindExists(keybindingPressed)) {
                AfraidOfTheDark.packetHandler.sendToServer(SpellKeyPressPacket(keybindingPressed))
            }
        }
    }

    /**
     * Call to attempt firing the wrist crossbow
     */
    private fun fireWristCrossbow() {
        // Grab a player reference
        val entityPlayer = Minecraft.getInstance().player!!

        // Grab the player's bolt of choice
        val playerBasics = entityPlayer.getBasics()

        // If the player is sneaking change the mode
        if (entityPlayer.isSneaking) {
            // Advance the current index
            var currentBoltIndex = playerBasics.selectedWristCrossbowBoltIndex
            // Compute the next bolt index
            currentBoltIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltIndex)
            // Set the selected index and sync the index
            playerBasics.selectedWristCrossbowBoltIndex = currentBoltIndex
            playerBasics.syncSelectedWristCrossbowBoltIndex(entityPlayer)

            // Tell the player what type of bolt will be fired now
            entityPlayer.sendMessage(
                TranslationTextComponent(
                    "message.afraidofthedark.wrist_crossbow.bolt_change",
                    TranslationTextComponent(BoltOrderHelper.getBoltAt(currentBoltIndex).getUnlocalizedName())
                )
            )
        } else {
            // Test if the player has the correct research
            if (entityPlayer.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
                // Test if the player has a wrist crossbow to shoot with
                if (entityPlayer.inventory.hasItemStack(ItemStack(ModItems.WRIST_CROSSBOW))) {
                    // Grab the currently selected bolt type
                    val boltType = BoltOrderHelper.getBoltAt(playerBasics.selectedWristCrossbowBoltIndex)

                    // Ensure the player has a bolt of the right type in his/her inventory or is in creative mode
                    if (entityPlayer.inventory.hasItemStack(ItemStack(boltType.boltItem)) || entityPlayer.isCreative) {
                        // Find the wrist crossbow item in the player's inventory
                        for (itemStack in entityPlayer.inventory.mainInventory) {
                            if (itemStack.item is WristCrossbowItem) {
                                // Grab the crossbow item reference
                                val wristCrossbow = itemStack.item as WristCrossbowItem

                                // Test if the crossbow is on CD or not. If it is fire, if it is not continue searching
                                if (!wristCrossbow.isOnCooldown(itemStack)) {
                                    // Tell the server to fire the crossbow
                                    AfraidOfTheDark.packetHandler.sendToServer(FireWristCrossbowPacket(boltType))
                                    // Set the item on CD
                                    wristCrossbow.setOnCooldown(itemStack, entityPlayer)
                                    // Return, we fired the bolt
                                    return
                                }
                            }
                        }
                        // No valid wrist crossbow found
                        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wrist_crossbow.reloading"))
                    } else {
                        entityPlayer.sendMessage(
                            TranslationTextComponent(
                                "message.afraidofthedark.wrist_crossbow.no_bolt",
                                TranslationTextComponent(boltType.getUnlocalizedName())
                            )
                        )
                    }
                } else {
                    entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wrist_crossbow.no_crossbow"))
                }
            } else {
                entityPlayer.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
    }

    /**
     * Call to attempt rolling with the cloak of agility
     */
    private fun rollWithCloakOfAgility() {
        // Grab a player reference
        val entityPlayer = Minecraft.getInstance().player!!

        // Test if the player has the correct research
        if (entityPlayer.getResearch().isResearched(ModResearches.CLOAK_OF_AGILITY)) {
            // Ensure the player is on the ground
            if (entityPlayer.onGround) {
                // Test if the player has a cloak of agility in their inventory
                for (itemStack in entityPlayer.inventory.mainInventory) {
                    // If the itemstack is a cloak set it on cooldown and dash
                    if (itemStack.item is CloakOfAgilityItem) {
                        val cloakOfAgility = itemStack.item as CloakOfAgilityItem
                        // Ensure the cloak is not on cooldown
                        if (!cloakOfAgility.isOnCooldown(itemStack)) {
                            // Set the cloak on CD
                            cloakOfAgility.setOnCooldown(itemStack, entityPlayer)

                            // If the player is not moving roll in the direction the player is looking, otherwise roll in the direction the player is moving
                            var motionDirection =
                                if (entityPlayer.motion.x <= 0.01 && entityPlayer.motion.x >= -0.01 && entityPlayer.motion.z <= 0.01 && entityPlayer.motion.z >= -0.01) {
                                    val lookDirection = entityPlayer.lookVec
                                    Vec3d(lookDirection.x, 0.0, lookDirection.z)
                                } else {
                                    Vec3d(entityPlayer.motion.x, 0.0, entityPlayer.motion.z)
                                }

                            // Normalize the motion vector
                            motionDirection = motionDirection.normalize()

                            // Update the player's motion in the new direction
                            entityPlayer.setMotion(
                                motionDirection.x * ROLL_VELOCITY,
                                0.2,
                                motionDirection.z * ROLL_VELOCITY
                            )

                            // Return
                            return
                        } else {
                            entityPlayer.sendMessage(
                                TranslationTextComponent(
                                    "message.afraidofthedark.cloak_of_agility.too_tired",
                                    cloakOfAgility.cooldownRemainingInSeconds(itemStack)
                                )
                            )
                            // If one cloak is on cooldown they all are, return
                            return
                        }
                    }
                }
            } else {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.cloak_of_agility.not_grounded"))
            }
        } else {
            entityPlayer.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }

    companion object {
        private const val ROLL_VELOCITY = 3.0
    }
}