package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.registries.ForgeRegistryEntry
import org.apache.logging.log4j.LogManager
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
 * @property xPosition The X position on the research screen that this research will be placed at
 * @property zPosition The Z position on the research screen that this research will be placed at
 * @property researchedRecipes A list of recipes that will be shown if the research is researched
 * @property preResearchedRecipes A list of recipes that will be shown if the previous research is researched
 * @property icon The icon to show for this research
 */
abstract class Research(data: ResourceLocation, val preRequisite: Research? = null) : ForgeRegistryEntry<Research>() {
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

    init {
        // Open an input stream to our data resource JSON file
        try {
            ResourceUtil.getInputStream(data).use { inputStream ->
                InputStreamReader(inputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { reader ->
                        val json = reader.readLines().joinToString(separator = "")
                        // Read the file as JSON
                        val jsonObject = JSONUtils.fromJson(DESERIALIZER, json, JsonObject::class.java)
                        if (jsonObject != null) {
                            // Parse all the fields of the JSON object using the JSONUtils class
                            xPosition = JSONUtils.getAsInt(jsonObject, "x")
                            zPosition = JSONUtils.getAsInt(jsonObject, "y")
                            researchedRecipes = JSONUtils.getAsJsonArray(jsonObject, "recipes").map {
                                JSONUtils.convertToItem(it, "")
                            }
                            preResearchedRecipes = JSONUtils.getAsJsonArray(jsonObject, "preRecipes").map {
                                JSONUtils.convertToItem(it, "")
                            }
                            icon = ResourceLocation(JSONUtils.getAsString(jsonObject, "icon"))
                            if (JSONUtils.isValidNode(jsonObject, "trigger")) {
                                procTrigger(JSONUtils.getAsJsonObject(jsonObject, "trigger"))
                            }
                        }
                    }
                }
            }
        }
        // This shouldn't happen, but if it does print out an error
        catch (e: IOException) {
            logger.error("Could not load the research defined by '$data'", e)
        }
    }

    /**
     * @return The unlocalized name of the research
     */
    fun getUnlocalizedName(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.name"
    }

    /**
     * @return The unlocalized tooltip of the research
     */
    fun getUnlocalizedTooltip(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.tooltip"
    }

    /**
     * @return The unlocalized pre text of the research
     */
    fun getUnlocalizedPreText(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.pre_text"
    }

    /**
     * @return The unlocalized text of the research
     */
    fun getUnlocalizedText(): String {
        return "research.${registryName!!.namespace}.${registryName!!.path}.text"
    }

    private fun procTrigger(trigger: JsonObject) {
        when (JSONUtils.getAsString(trigger, "type")) {
            "takeDamage" -> {
                val fromEntity = JSONUtils.getAsString(trigger, "entity")
                AfraidOfTheDark.researchHooks.addHook(LivingDamageEvent::class) {
                    val event = it as LivingDamageEvent
                    if (event.entity is PlayerEntity) {
                        val player = event.entity as PlayerEntity
                        val playerResearch = player.getResearch()
                        if (playerResearch.canResearch(this)) {
                            if (event.source.entity != null) {
                                if (event.source.entity!!.type.registryName.toString() == fromEntity) {
                                    if (!JSONUtils.getAsBoolean(trigger, "mustSurvive") || player.health > event.amount) {
                                        playerResearch.setResearch(this, true)
                                        playerResearch.sync(player, true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Gson serializer to convert from JSON to java types
        private val DESERIALIZER = GsonBuilder().disableHtmlEscaping().create()
    }
}