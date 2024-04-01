package com.davidm1a2.afraidofthedark.client.gui.toasts

import com.davidm1a2.afraidofthedark.client.sound.ResearchUnlockedSound
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation

class ResearchToast(private val research: Research) : Toast {
    private var playedSound = false

    override fun render(poseStack: PoseStack, component: ToastComponent, millisSinceShow: Long): Toast.Visibility {
        RenderSystem.setShaderTexture(0, BACKGROUND)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        GuiComponent.blit(poseStack, 0, 0, 0f, 0f, this.width(), this.height(), 160, 32)
        component.minecraft.font.draw(poseStack, TranslatableComponent("researchbanner.title"), 10f, 5f, -256)
        component.minecraft.font.draw(poseStack, research.getName(), 10f, 18f, -1)

        // Only try to play the sound once at the beginning of the toast's life
        if (!playedSound) {
            // Play the achievement sound (if one hasn't recently already done so to avoid spamming the sound)
            if (timeOfLastSound + TOAST_SHOW_TIME < System.currentTimeMillis()) {
                Minecraft.getInstance().soundManager.play(ResearchUnlockedSound())
                timeOfLastSound = System.currentTimeMillis()
            }
            playedSound = true
        }
        return if (millisSinceShow >= TOAST_SHOW_TIME) Toast.Visibility.HIDE else Toast.Visibility.SHOW
    }

    companion object {
        private const val TOAST_SHOW_TIME = 6000L
        private val BACKGROUND = ResourceLocation(Constants.MOD_ID, "textures/gui/research_achieved.png")
        private var timeOfLastSound = 0L
    }
}