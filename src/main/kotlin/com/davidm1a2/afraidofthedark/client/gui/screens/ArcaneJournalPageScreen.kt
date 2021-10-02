package com.davidm1a2.afraidofthedark.client.gui.screens

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
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RecipePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextBoxComponent
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import java.awt.Color

/**
 * Journal page UI which is shown when a player opens a page
 */
class ArcaneJournalPageScreen(text: String, titleText: String, relatedItemRecipes: List<Item>) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.arcane_journal_page")) {
    private val completeText: String = text
    private val textOnEachPage: MutableList<String> = mutableListOf()
    private val researchRecipes: List<IRecipe<*>> = entityPlayer.level.recipeManager.recipes.filter { relatedItemRecipes.contains(it.resultItem.item) }
    private val leftPage: StackPane
    private val rightPage: StackPane
    private val leftPageText: TextBoxComponent
    private val rightPageText: TextBoxComponent
    private val forwardButton: ButtonPane
    private val backwardButton: ButtonPane
    private val leftPageNumber: LabelComponent
    private val rightPageNumber: LabelComponent
    private val topLeftRecipe: RecipePane
    private val bottomLeftRecipe: RecipePane
    private val topRightRecipe: RecipePane
    private val bottomRightRecipe: RecipePane
    private var pageNumber = 0

    init {
        // Create a panel to contain everything
        val guiPane = StackPane(padding = Spacing(0.125))

        // Create a page image to be used as the background
        val journalPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/background.png"), ImagePane.DispMode.FIT_TO_PARENT)
        journalPane.gravity = Gravity.CENTER
        guiPane.add(journalPane)

        // Create red colors for text
        val textColor = Color(170, 3, 25)
        val titleColor = Color(200, 0, 0)

        // Create a title label to contain the research name
        val titleLabel = LabelComponent(ClientData.getOrCreate(50f), Dimensions(1.0, 0.1), Gravity.TOP_CENTER)
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.textAlignment = TextAlignment.ALIGN_CENTER
        contentPane.add(titleLabel)

        // Create two page numbers, one for the left page and one for the right page
        leftPageNumber = LabelComponent(ClientData.getOrCreate(32f), Dimensions(0.1, 0.1), Gravity.TOP_LEFT)
        leftPageNumber.offset = Position(0.05, 0.03)
        rightPageNumber = LabelComponent(ClientData.getOrCreate(32f), Dimensions(0.1, 0.1), Gravity.TOP_RIGHT)
        rightPageNumber.offset = Position(-0.05, 0.03)
        // Align the right page number right so that it fits into the corner
        rightPageNumber.textAlignment = TextAlignment.ALIGN_RIGHT
        // Start the page numbers at 1 and 2
        leftPageNumber.text = "1"
        rightPageNumber.text = "2"
        // Both page numbers are red
        leftPageNumber.textColor = textColor
        rightPageNumber.textColor = textColor
        // Add both page numbers
        journalPane.add(leftPageNumber)
        journalPane.add(rightPageNumber)

        // Create two pages, one for the left page text and one for the right page text
        leftPage = StackPane(prefSize = Dimensions(0.5, 1.0), padding = Spacing(0.08, 0.15, 0.2, 0.05))
        leftPage.gravity = Gravity.TOP_LEFT
        leftPageText = TextBoxComponent(font = ClientData.getOrCreate(32f))
        leftPage.add(leftPageText)
        rightPage = StackPane(prefSize = Dimensions(0.5, 1.0), padding = Spacing(0.08, 0.15, 0.05, 0.2))
        rightPage.gravity = Gravity.TOP_RIGHT
        rightPageText = TextBoxComponent(font = ClientData.getOrCreate(32f))
        rightPage.add(rightPageText)
        // Set the text on both pages to red
        leftPageText.textColor = textColor
        rightPageText.textColor = textColor
        // Add the pages to the journal
        journalPane.add(leftPage)
        journalPane.add(rightPage)

        // The bookmark button returns the user to the research screen
        // The bookmark button to go back
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
        // When we click the bookmark return to the journal research ui
        bookmarkButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    returnToResearchScreen()
                }
            }
        }
        journalPane.add(bookmarkButton)

        // Initialize 4 recipes, two for the left page and two for the right page
        topLeftRecipe = RecipePane(Dimensions(1.0, 0.5))
        topLeftRecipe.gravity = Gravity.TOP_LEFT
        leftPage.add(topLeftRecipe)
        bottomLeftRecipe = RecipePane(Dimensions(1.0, 0.5))
        bottomLeftRecipe.gravity = Gravity.BOTTOM_LEFT
        leftPage.add(bottomLeftRecipe)
        topRightRecipe = RecipePane(Dimensions(1.0, 0.5))
        topRightRecipe.gravity = Gravity.TOP_RIGHT
        rightPage.add(topRightRecipe)
        bottomRightRecipe = RecipePane(Dimensions(1.0, 0.5))
        bottomRightRecipe.gravity = Gravity.BOTTOM_RIGHT
        rightPage.add(bottomRightRecipe)

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
                    invalidate()
                }
            }
        }
        // Upon clicking backward then rewind the page, if we hover the button darken the color, if we don't hover the button brighten the color
        backwardButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    rewindPage()
                    invalidate()
                }
            }
        }
        forwardButton.gravity = Gravity.BOTTOM_RIGHT
        backwardButton.gravity = Gravity.BOTTOM_LEFT
        // Add the buttons to the pane
        journalPane.add(forwardButton)
        journalPane.add(backwardButton)

        // Add the journal to the content pane
        contentPane.add(guiPane)

        // Update the text on each page
        this.updateText()
        // Update the actual page content
        this.refreshPagesForNumber()

        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = this.hasPageForward()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = this.hasPageBackward()

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
        if (this.hasPageForward()) {
            // Advance the page number
            pageNumber += 2
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
        if (this.hasPageBackward()) {
            // Rewind the page number
            pageNumber -= 2
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
            // Update the page content
            this.refreshPagesForNumber()
        }
    }

    /**
     * @return True if we can rewind the page, or false otherwise
     */
    private fun hasPageBackward(): Boolean {
        return pageNumber != 0
    }

    /**
     * @return True if we can advance the page, or false otherwise
     */
    private fun hasPageForward(): Boolean {
        val hasMoreText = textOnEachPage.hasIndex(pageNumber + 2)
        val hasMoreRecipes = researchRecipes.hasIndex((pageNumber + 2 - textOnEachPage.size) * 2)
        return hasMoreText || hasMoreRecipes
    }

    /**
     * Updates the text or recipes on each page
     */
    private fun refreshPagesForNumber() {
        // Update the page numbers
        leftPageNumber.text = (pageNumber + 1).toString()
        rightPageNumber.text = (pageNumber + 2).toString()

        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = hasPageForward()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = hasPageBackward()

        // The adjusted index for an index into the recipe list
        var adjustedIndexForRecipe = (pageNumber - textOnEachPage.size) * 2

        // If we have another page of text then load that page of text and clear out the recipes
        if (textOnEachPage.hasIndex(pageNumber)) {
            leftPageText.setText(textOnEachPage[pageNumber])
            topLeftRecipe.setRecipe(null)
            bottomLeftRecipe.setRecipe(null)
            adjustedIndexForRecipe += 2
        } else {
            leftPageText.setText("")
            // If we have another recipe load it into the top left box, otherwise clear it
            topLeftRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
            // If we have another recipe load it into the bottom left box, otherwise clear it
            bottomLeftRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
        }

        // If we have another page of text then load that page of text and clear out the recipes
        if (textOnEachPage.hasIndex(pageNumber + 1)) {
            rightPageText.setText(textOnEachPage[pageNumber + 1])
            topRightRecipe.setRecipe(null)
            bottomRightRecipe.setRecipe(null)
        } else {
            rightPageText.setText("")
            // If we have another recipe load it into the top left box, otherwise clear it
            topRightRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
            // If we have another recipe load it into the bottom left box, otherwise clear it
            bottomRightRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe] else null)
        }
    }

    override fun invalidate() {
        super.invalidate()
        this.updateText()
        this.refreshPagesForNumber()
    }

    /**
     * Updates the text based on the size of text boxes
     */
    private fun updateText() {
        // Clear the text distribution to start out with
        textOnEachPage.clear()
        // Create a variable that will be the text to distribute
        var textToDistribute = completeText
        // An alternator variable to switch between adding text to the left and right box
        var alternator = true
        // Loop while we have text to distribute
        while (textToDistribute.isNotEmpty()) {
            // Left over text
            val leftOver = if (alternator) {
                // Set the text of the left page, then retrieve the text that doesnt fit into the box
                leftPageText.setText(textToDistribute)
                leftPageText.overflowText
            } else {
                // Set the text of the right page, then retrieve the text that doesnt fit into the box
                rightPageText.setText(textToDistribute)
                rightPageText.overflowText
            }

            // Flip our alternator
            alternator = !alternator
            // Don't get caught filling zero sized pages infinitely
            if (textToDistribute.length <= leftOver.length) break
            // Grab the text that will go on this page alone
            val pageText = textToDistribute.substring(0, textToDistribute.length - leftOver.length)
            // Update the remaining text that needs distributing
            textToDistribute = textToDistribute.substring(textToDistribute.length - leftOver.length)

            // Add the page of text
            textOnEachPage.add(pageText)
        }
    }

    /**
     * Returns true if the list has the given index or false otherwise
     *
     * @param index The index to check
     * @return True if the list has the index, false otherwise
     */
    private fun <T> List<T>.hasIndex(index: Int): Boolean {
        return index >= 0 && index < this.size
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
}