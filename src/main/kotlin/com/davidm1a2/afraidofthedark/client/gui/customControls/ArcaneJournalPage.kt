package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextBoxComponent
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import java.awt.Color

class ArcaneJournalPage(
    private val rawText: String,
    private val recipes: List<IRecipe<*>>,
    private val stickers: List<ResourceLocation>
) : StackPane() {
    // Used when the page has text
    private val textBox: TextBoxComponent
    private val textPerPage = mutableListOf<String>()

    // Used when the page has recipe(ies) on it
    private val topRecipe: RecipePane
    private val bottomRecipe: RecipePane

    // Used when the page has stickers on it
    private val topSticker: StickerPane
    private val bottomSticker: StickerPane

    init {
        textBox = TextBoxComponent(font = FontCache.getOrCreate(38f))
        textBox.textColor = Color(135, 70, 44)
        add(textBox)

        topRecipe = RecipePane(Dimensions(1.0, 0.5))
        topRecipe.gravity = Gravity.TOP_CENTER
        add(topRecipe)
        bottomRecipe = RecipePane(Dimensions(1.0, 0.5))
        bottomRecipe.gravity = Gravity.BOTTOM_CENTER
        add(bottomRecipe)

        topSticker = StickerPane(Dimensions(1.0, 0.5))
        topSticker.gravity = Gravity.TOP_CENTER
        add(topSticker)
        bottomSticker = StickerPane(Dimensions(1.0, 0.5))
        bottomSticker.gravity = Gravity.BOTTOM_CENTER
        add(bottomSticker)
    }

    override fun invalidate() {
        super.invalidate()
        this.computeTextPerPage()
    }

    /**
     * Updates the text based on the size of text boxes
     */
    private fun computeTextPerPage() {
        // Clear the text distribution to start out with
        textPerPage.clear()

        // Create a variable that will be the text to distribute
        var textToDistribute = rawText
        // Loop while we have text to distribute
        while (textToDistribute.isNotEmpty()) {
            // Left over text
            textBox.setText(textToDistribute)
            val leftOver = textBox.overflowText

            // Don't get caught filling zero sized pages infinitely
            if (textToDistribute.length <= leftOver.length) break
            // Grab the text that will go on this page alone
            val pageText = textToDistribute.substring(0, textToDistribute.length - leftOver.length)
            // Update the remaining text that needs distributing
            textToDistribute = textToDistribute.substring(textToDistribute.length - leftOver.length)

            // Remove \n characters from the start of the page, so we avoid empty lines on the top of a page.
            while (textToDistribute.startsWith("\n")) {
                textToDistribute = textToDistribute.substring(1)
            }

            // Add the page of text
            textPerPage.add(pageText)
        }
    }

    fun clear() {
        textBox.setText("")
        topRecipe.setRecipe(null)
        bottomRecipe.setRecipe(null)
        topSticker.setSticker(null)
        bottomSticker.setSticker(null)
    }

    fun setIndex(index: Int) {
        clear()
        if (index < 0) {
            return
        }

        val lastTextIndex = textPerPage.size - 1
        val firstRecipeIndex = lastTextIndex + 1
        val lastRecipeIndex = lastTextIndex + MathHelper.ceil(recipes.size / 2.0)
        val firstStickerIndex = lastRecipeIndex + 1
        val lastStickerIndex = lastRecipeIndex + MathHelper.ceil(stickers.size / 2.0)
        if (index <= lastTextIndex) {
            setTextPage(index)
        } else if (index <= lastRecipeIndex) {
            setRecipePage((index - firstRecipeIndex) * 2)
        } else if (index <= lastStickerIndex) {
            setStickerPage((index - firstStickerIndex) * 2)
        }
    }

    fun hasIndex(index: Int): Boolean {
        val maxIndex = textPerPage.size + MathHelper.ceil(recipes.size / 2.0) + MathHelper.ceil(stickers.size / 2.0) - 1
        return index <= maxIndex
    }

    private fun setTextPage(index: Int) {
        textBox.setText(textPerPage[index])
    }

    private fun setRecipePage(index: Int) {
        topRecipe.setRecipe(recipes[index])
        bottomRecipe.setRecipe(recipes.getOrNull(index + 1))
    }

    private fun setStickerPage(index: Int) {
        topSticker.setSticker(stickers[index])
        bottomSticker.setSticker(stickers.getOrNull(index + 1))
    }
}