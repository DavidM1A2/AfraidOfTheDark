package com.davidm1a2.afraidofthedark.client.keybindings

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.screens.PowerSourceSelectionScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.WristCrossbowItem
import com.davidm1a2.afraidofthedark.common.network.packets.other.FireWristCrossbowPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.SpellKeyPressPacket
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries
import org.lwjgl.glfw.GLFW

/**
 * Class that receives all keyboard events and processes them accordingly
 */
class KeyInputEventHandler {
    private val boltItems: List<AOTDBoltItem> by lazy {
        ForgeRegistries.ITEMS
            .values
            .filterIsInstance<AOTDBoltItem>()
            .sortedBy { it.registryName }
    }

    /**
     * Called whenever a key is pressed
     *
     * @param event The key event containing press information
     */
    @SubscribeEvent
    fun handleKeyInputEvent(event: InputEvent.KeyInputEvent) {
        // This gets fired in the main menu, or when we have an inventory open. In either case return
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().screen != null) {
            return
        }

        // Process input
        if (ModKeybindings.POWER_SOURCE_SELECTOR.isDown && event.action == GLFW.GLFW_PRESS) {
            Minecraft.getInstance().setScreen(PowerSourceSelectionScreen())
            // GLFW.glfwSetInputMode(Minecraft.getInstance().window.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
        }

        if (ModKeybindings.FIRE_WRIST_CROSSBOW.isDown) {
            fireWristCrossbow()
        }

        if (ModKeybindings.ROLL_WITH_CLOAK_OF_AGILITY.isDown) {
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
        if (entityPlayer.isCrouching) {
            // Advance the current index
            var currentBoltIndex = playerBasics.selectedWristCrossbowBoltIndex
            // Compute the next bolt index
            currentBoltIndex = boltItems.getNextBoltItem(currentBoltIndex, entityPlayer.getResearch())
            // Set the selected index and sync the index
            playerBasics.selectedWristCrossbowBoltIndex = currentBoltIndex
            playerBasics.syncSelectedWristCrossbowBoltIndex(entityPlayer)

            // Tell the player what type of bolt will be fired now
            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wrist_crossbow.bolt_change", boltItems[currentBoltIndex].description))
        } else {
            // Test if the player has the correct research
            if (entityPlayer.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
                // Test if the player has a wrist crossbow to shoot with
                if (entityPlayer.inventory.contains(ItemStack(ModItems.WRIST_CROSSBOW))) {
                    // Grab the currently selected bolt type
                    val boltType = boltItems[playerBasics.selectedWristCrossbowBoltIndex]

                    // Ensure the player has a bolt of the right type in his/her inventory or is in creative mode
                    if (entityPlayer.inventory.contains(ItemStack(boltType.item)) || entityPlayer.isCreative) {
                        // Find the wrist crossbow item in the player's inventory
                        for (itemStack in entityPlayer.inventory.items) {
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
                        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wrist_crossbow.no_bolt", boltType.description))
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

        for (itemStack in entityPlayer.inventory.items) {
            if (itemStack.item == ModItems.CLOAK_OF_AGILITY) {
                ModItems.CLOAK_OF_AGILITY.roll(entityPlayer, itemStack)
                return
            }
        }
        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.cloak_of_agility.no_cloak"))
    }

    private fun List<AOTDBoltItem>.getNextBoltItem(currentIndex: Int, research: IPlayerResearch): Int {
        var nextIndex = currentIndex + 1
        if (nextIndex >= this.size) {
            nextIndex = 0
        }
        val nextBoltResearch = this[nextIndex].requiredResearch
        return if (nextBoltResearch == null || research.isResearched(nextBoltResearch)) {
            nextIndex
        } else {
            getNextBoltItem(nextIndex, research)
        }
    }
}