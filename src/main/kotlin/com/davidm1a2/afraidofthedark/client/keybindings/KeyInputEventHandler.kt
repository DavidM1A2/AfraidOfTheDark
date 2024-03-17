package com.davidm1a2.afraidofthedark.client.keybindings

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.screens.PowerSourceSelectionScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.network.packets.other.SpellKeyPressPacket
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.TranslatableComponent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

/**
 * Class that receives all keyboard events and processes them accordingly
 */
class KeyInputEventHandler {
    @SubscribeEvent
    fun onMouseInputEvent(event: InputEvent.MouseInputEvent) {
        // This gets fired in the main menu, or when we have an inventory open. In either case return
        val player = Minecraft.getInstance().player
        if (player == null || Minecraft.getInstance().screen != null) {
            return
        }

        if (event.action == GLFW.GLFW_PRESS) {
            // Grab the currently held bind
            val keybindingPressed = KeybindingUtils.getCurrentlyHeldKeybind(event.button)
            // If that keybind exists then tell the server to fire the spell
            if (player.getSpellManager().keybindExists(keybindingPressed)) {
                AfraidOfTheDark.packetHandler.sendToServer(SpellKeyPressPacket(keybindingPressed))
            }
        }
    }

    /**
     * Called whenever a key is pressed
     *
     * @param event The key event containing press information
     */
    @SubscribeEvent
    fun handleKeyInputEvent(event: InputEvent.KeyInputEvent) {
        // This gets fired in the main menu, or when we have an inventory open. In either case return
        val player = Minecraft.getInstance().player
        if (player == null || Minecraft.getInstance().screen != null) {
            return
        }

        // Process input
        if (event.action == GLFW.GLFW_PRESS && event.key == ModKeybindings.POWER_SOURCE_SELECTOR.key.value) {
            if (player.hasStartedAOTD()) {
                Minecraft.getInstance().setScreen(PowerSourceSelectionScreen())
            } else {
                player.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
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
            if (player.getSpellManager().keybindExists(keybindingPressed)) {
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

        ModItems.WRIST_CROSSBOW.shoot(entityPlayer)
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
        entityPlayer.sendMessage(TranslatableComponent("message.afraidofthedark.cloak_of_agility.no_cloak"))
    }
}