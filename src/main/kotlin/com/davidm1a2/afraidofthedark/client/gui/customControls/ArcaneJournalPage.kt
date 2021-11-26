package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RecipePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextBoxComponent
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.math.MathHelper
import java.awt.Color

class ArcaneJournalPage(
    private val rawText: String,
    private val recipes: List<IRecipe<*>>
) : StackPane() {
    // Used when the page has text
    private val textBox: TextBoxComponent

    // Used when the page has recipe(ies) on it
    private val topRecipe: RecipePane
    private val bottomRecipe: RecipePane

    private val textPerPage = mutableListOf<String>()

    init {
        textBox = TextBoxComponent(font = ClientData.getOrCreate(38f))
        textBox.textColor = Color(135, 70, 44)
        add(textBox)

        topRecipe = RecipePane(Dimensions(1.0, 0.5))
        topRecipe.gravity = Gravity.TOP_CENTER
        add(topRecipe)
        bottomRecipe = RecipePane(Dimensions(1.0, 0.5))
        bottomRecipe.gravity = Gravity.BOTTOM_CENTER
        add(bottomRecipe)
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
    }

    fun setIndex(index: Int) {
        clear()
        if (textPerPage.hasIndex(index)) {
            setTextPage(index)
        } else {
            setRecipePage((index - textPerPage.size) * 2)
        }
    }

    fun hasIndex(index: Int): Boolean {
        val maxIndex = textPerPage.size + MathHelper.ceil(recipes.size / 2.0) - 1
        return index <= maxIndex
    }

    private fun setTextPage(index: Int) {
        textBox.setText(textPerPage[index])
    }

    private fun setRecipePage(index: Int) {
        topRecipe.setRecipe(recipes[index])
        bottomRecipe.setRecipe(if (recipes.hasIndex(index + 1)) recipes[index + 1] else null)
    }

    companion object {
        private fun <T> List<T>.hasIndex(index: Int): Boolean {
            return index >= 0 && index < this.size
        }
    }
}