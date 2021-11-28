package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.customControls.ArcaneJournalPage
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Journal page UI which is shown when a player opens a page
 */
class ArcaneJournalPageScreen(research: Research) : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.arcane_journal_page")) {
    private val forwardButton: ButtonPane
    private val backwardButton: ButtonPane
    private val leftPage: ArcaneJournalPage
    private val rightPage: ArcaneJournalPage
    private var currentPageIndex = 0

    init {
        val isResearched = entityPlayer.getResearch().isResearched(research)

        // Create a panel to contain everything
        val guiPane = StackPane(padding = Spacing(0.125))

        // Create a page image to be used as the background
        val journalPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/background.png"), ImagePane.DispMode.FIT_TO_PARENT)
        journalPane.gravity = Gravity.CENTER
        guiPane.add(journalPane)

        val titleColor = Color(165, 70, 20)

        // Create a title label to contain the research name
        val titleLabel = LabelComponent(FontCache.getOrCreate(80f), Dimensions(1.0, 0.1), Gravity.TOP_CENTER)
        titleLabel.text = if (isResearched) research.getName().string else "???"
        titleLabel.textColor = titleColor
        titleLabel.textAlignment = TextAlignment.ALIGN_CENTER
        contentPane.add(titleLabel)

        val researchTime = entityPlayer.getResearch().getResearchTime(research)?.withZoneSameInstant(ZoneId.systemDefault())
        val previousResearchTime = research.preRequisite?.let { entityPlayer.getResearch().getResearchTime(it) }?.withZoneSameInstant(ZoneId.systemDefault())
        val rawText = buildString {
            // Date and time the previous research was unlocked
            if (previousResearchTime != null) {
                append(previousResearchTime.format(DATE_TIME_FORMAT))
                appendLine()
                appendLine()
            }
            // Indent the pre text paragraph
            append("   ")
            append(research.getPreText().string)

            // Date and time the current research was unlocked
            if (researchTime != null && isResearched) {
                appendLine()
                appendLine()
                append(researchTime.format(DATE_TIME_FORMAT))
                // Indent the text paragraph
                appendLine()
                appendLine()
                append("   ")
                append(research.getText().string)
            }
        }
        val recipesToShow = if (isResearched) research.researchedRecipes else research.preResearchedRecipes
        val researchRecipes = entityPlayer.level.recipeManager.recipes.filter { recipesToShow.contains(it.resultItem.item) }
        val stickersToShow = if (isResearched) research.stickers else research.preStickers
        leftPage = ArcaneJournalPage(rawText, researchRecipes, stickersToShow)
        rightPage = ArcaneJournalPage(rawText, researchRecipes, stickersToShow)
        leftPage.gravity = Gravity.TOP_LEFT
        leftPage.prefSize = Dimensions(0.5, 1.0)
        leftPage.padding = Spacing(0.08, 0.15, 0.2, 0.05)
        rightPage.gravity = Gravity.TOP_RIGHT
        rightPage.prefSize = Dimensions(0.5, 1.0)
        rightPage.padding = Spacing(0.08, 0.15, 0.05, 0.2)

        journalPane.add(leftPage)
        journalPane.add(rightPage)

        // The bookmark button returns the user to the research screen
        val bookmarkIcon = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/slot_highlight.png"), ImagePane.DispMode.STRETCH)
        val bookmarkButton = ButtonPane(
            icon = null,
            iconHovered = bookmarkIcon,
            prefSize = Dimensions(0.05, 0.1),
            offset = Position(-0.036, 0.0)
        )
        bookmarkButton.gravity = Gravity.BOTTOM_CENTER
        // Set the color to a see-through white
        bookmarkIcon.color = Color(255, 255, 255, 50)
        bookmarkIcon.isVisible = false
        // When we click the bookmark return to the journal research ui
        bookmarkButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    returnToResearchScreen()
                }
            }
        }
        journalPane.add(bookmarkButton)

        // Create the forward and backward button to advance and rewind pages
        forwardButton = ButtonPane(
            icon = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/forward_button.png")),
            iconHovered = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/forward_button_hovered.png")),
            prefSize = Dimensions(16.0, 16.0, false)
        )
        backwardButton = ButtonPane(
            icon = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/backward_button.png")),
            iconHovered = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/backward_button_hovered.png")),
            prefSize = Dimensions(16.0, 16.0, false)
        )
        // Upon clicking forward then advance the page, if we hover the button darken the color, if we don't hover the button brighten the color
        forwardButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    advancePage()
                }
            }
        }
        // Upon clicking backward then rewind the page, if we hover the button darken the color, if we don't hover the button brighten the color
        backwardButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    rewindPage()
                }
            }
        }
        forwardButton.gravity = Gravity.BOTTOM_RIGHT
        backwardButton.gravity = Gravity.BOTTOM_LEFT
        // Add the buttons to the pane
        journalPane.add(forwardButton)
        journalPane.add(backwardButton)

        // Create two pages, one for the left page text and one for the right page text
        val leftPage = StackPane(prefSize = Dimensions(0.5, 1.0), padding = Spacing(0.08, 0.15, 0.2, 0.05))
        leftPage.gravity = Gravity.TOP_LEFT
        val rightPage = StackPane(prefSize = Dimensions(0.5, 1.0), padding = Spacing(0.08, 0.15, 0.05, 0.2))
        rightPage.gravity = Gravity.TOP_RIGHT
        // Add the pages to the journal
        journalPane.add(leftPage)
        journalPane.add(rightPage)

        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = this.hasNextPage()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = this.hasPreviousPage()

        contentPane.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Press) {
                if (isInventoryKeybind(it.key, it.scanCode)) {
                    returnToResearchScreen()
                } else if (it.key == GLFW.GLFW_KEY_A || it.key == GLFW.GLFW_KEY_LEFT) {
                    rewindPage()
                } else if (it.key == GLFW.GLFW_KEY_D || it.key == GLFW.GLFW_KEY_RIGHT) {
                    advancePage()
                }
            }
        }

        // Play a page turn sound to the player
        entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)

        // Add the journal to the content pane
        contentPane.add(guiPane)
    }

    override fun invalidate() {
        super.invalidate()
        refreshPagesForNumber()
    }

    private fun returnToResearchScreen() {
        val mainhandItem = entityPlayer.mainHandItem
        val isCheatSheet = if (mainhandItem.item == ModItems.ARCANE_JOURNAL) {
            ModItems.ARCANE_JOURNAL.isCheatSheet(mainhandItem)
        } else {
            ModItems.ARCANE_JOURNAL.isCheatSheet(entityPlayer.mainHandItem)
        }
        Minecraft.getInstance().setScreen(ArcaneJournalResearchScreen(isCheatSheet))
    }

    /**
     * Advances the page to the next page
     */
    private fun advancePage() {
        // Ensure we can advance the page
        if (this.hasNextPage()) {
            // Advance the page number
            currentPageIndex = currentPageIndex + 2
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
            // Update the page content
            this.refreshPagesForNumber()
        }
    }

    /**
     * Rewinds the page to the previous page
     */
    private fun rewindPage() {
        // Ensure we can rewind the page
        if (this.hasPreviousPage()) {
            // Rewind the page number
            currentPageIndex = currentPageIndex - 2
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
            // Update the page content
            this.refreshPagesForNumber()
        }
    }

    private fun hasPreviousPage(): Boolean {
        return currentPageIndex != 0
    }

    private fun hasNextPage(): Boolean {
        return leftPage.hasIndex(currentPageIndex + 2)
    }

    /**
     * Updates the text or recipes on each page
     */
    private fun refreshPagesForNumber() {
        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = hasNextPage()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = hasPreviousPage()

        if (leftPage.hasIndex(currentPageIndex)) {
            leftPage.setIndex(currentPageIndex)
            if (rightPage.hasIndex(currentPageIndex + 1)) {
                rightPage.setIndex(currentPageIndex + 1)
            } else {
                rightPage.clear()
            }
        } else {
            leftPage.clear()
            rightPage.clear()
        }
    }

    /**
     * @return False since the inventory key does not close the screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return false
    }

    /**
     * @return True since the screen should have a gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }

    companion object {
        private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(I18n.get("journalpage.dateformat"))
    }
}