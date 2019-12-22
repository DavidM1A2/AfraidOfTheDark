package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.item.Item
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Base class for all researches that the player can unlock
 *
 * @constructor takes the resource location of the JSON file which contains the research data in it as the first parameter and
 * the pre-requisite for this research as the second argument
 *
 * @param data The resource location with the JSON document for this research
 * @property preRequisite The research that this research requires before it can be researched
 * @property tooltip The tooltip that is displayed when the research is hovered
 * @property preResearchedText The pre-researched text that is displayed on the page when the research is not researched yet
 * @property researchedText The researched text that is displayed on the page when the research is researched
 * @property xPosition The X position on the research screen that this research will be placed at
 * @property zPosition The Z position on the research screen that this research will be placed at
 * @property researchedRecipes A list of recipes that will be shown if the research is researched
 * @property preResearchedRecipes A list of recipes that will be shown if the previous research is researched
 * @property icon The icon to show for this research
 */
abstract class Research(data: ResourceLocation, val preRequisite: Research? = null) : IForgeRegistryEntry.Impl<Research>()
{
    lateinit var tooltip: String
        private set
    lateinit var preResearchedText: String
        private set
    lateinit var researchedText: String
        private set
    var xPosition = 0
        private set
    var zPosition = 0
        private set
    lateinit var researchedRecipes: List<Item>
        private set
    lateinit var preResearchedRecipes: List<Item>
        private set
    lateinit var icon: ResourceLocation
        private set

    init
    {
        // Open an input stream to our data resource JSON file
        try
        {
            ResourceUtil.getInputStream(data).use()
            { inputStream ->
                InputStreamReader(inputStream).use()
                { inputStreamReader ->
                    BufferedReader(inputStreamReader).use()
                    { reader ->
                        // Read the file as JSON
                        val jsonObject = JsonUtils.fromJson(DESERIALIZER, reader, JsonObject::class.java)
                        if (jsonObject != null)
                        {
                            // Parse all the fields of the JSON object using the JSONUtils class
                            xPosition = JsonUtils.getInt(jsonObject, "x")
                            zPosition = JsonUtils.getInt(jsonObject, "y")
                            tooltip = JsonUtils.getString(jsonObject, "tooltip")
                            researchedRecipes = JsonUtils.getJsonArray(jsonObject, "recipes").map()
                            {
                                JsonUtils.getItem(it, "")
                            }
                            preResearchedRecipes = JsonUtils.getJsonArray(jsonObject, "preRecipes").map()
                            {
                                JsonUtils.getItem(it, "")
                            }
                            icon = ResourceLocation(JsonUtils.getString(jsonObject, "icon"))
                            preResearchedText = JsonUtils.getString(jsonObject, "pre")
                            researchedText = JsonUtils.getString(jsonObject, "researched")
                        }
                    }
                }
            }
        }

        // This shouldn't happen, but if it does print out an error
        catch (_: IOException)
        {
            AfraidOfTheDark.INSTANCE.logger.error("Could not load the research defined by '$data'")
        }
    }

    /**
     * @return The unlocalized name of the research
     */
    fun getUnlocalizedName(): String
    {
        return "research.${registryName!!.resourcePath}"
    }

    companion object
    {
        // Gson serializer to convert from JSON to java types
        private val DESERIALIZER = GsonBuilder().disableHtmlEscaping().create()
    }
}