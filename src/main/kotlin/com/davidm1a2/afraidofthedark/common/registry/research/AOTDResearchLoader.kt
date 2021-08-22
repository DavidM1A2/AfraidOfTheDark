package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.living.LivingDamageEvent
import java.io.BufferedReader
import java.io.InputStreamReader

object AOTDResearchLoader {
    private val GSON = GsonBuilder().disableHtmlEscaping().create()

    fun load(name: String): AOTDResearch {
        val data = ResourceLocation(Constants.MOD_ID, "research_notes/$name.json")
        ResourceUtil.getInputStream(data).use { inputStream ->
            InputStreamReader(inputStream).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { reader ->
                    val json = reader.readLines().joinToString(separator = "")
                    // Read the file as JSON
                    val jsonObject = JSONUtils.fromJson(GSON, json, JsonObject::class.java)!!
                    // Parse all the fields of the JSON object using the JSONUtils class
                    val xPosition = JSONUtils.getAsInt(jsonObject, "x")
                    val zPosition = JSONUtils.getAsInt(jsonObject, "y")
                    val researchedRecipes = JSONUtils.getAsJsonArray(jsonObject, "recipes").map {
                        JSONUtils.convertToItem(it, "")
                    }
                    val preResearchedRecipes = JSONUtils.getAsJsonArray(jsonObject, "preRecipes").map {
                        JSONUtils.convertToItem(it, "")
                    }
                    val icon = ResourceLocation(JSONUtils.getAsString(jsonObject, "icon"))
                    val prerequisiteResearch = if (JSONUtils.isValidNode(jsonObject, "prerequisite")) {
                        ResourceLocation(JSONUtils.getAsString(jsonObject, "prerequisite"))
                    } else {
                        null
                    }

                    val research = AOTDResearch(
                        name,
                        xPosition,
                        zPosition,
                        researchedRecipes,
                        preResearchedRecipes,
                        icon,
                        prerequisiteResearch
                    )

                    if (JSONUtils.isValidNode(jsonObject, "trigger")) {
                        procTrigger(JSONUtils.getAsJsonObject(jsonObject, "trigger"), research)
                    }

                    return research
                }
            }
        }
    }

    private fun procTrigger(trigger: JsonObject, research: Research) {
        when (JSONUtils.getAsString(trigger, "type")) {
            "takeDamage" -> {
                val fromEntity = JSONUtils.getAsString(trigger, "entity")
                AfraidOfTheDark.researchHooks.addHook(LivingDamageEvent::class) {
                    val event = it as LivingDamageEvent
                    if (event.entity is PlayerEntity) {
                        val player = event.entity as PlayerEntity
                        val playerResearch = player.getResearch()
                        if (playerResearch.canResearch(research)) {
                            if (event.source.entity != null) {
                                if (event.source.entity!!.type.registryName.toString() == fromEntity) {
                                    if (!JSONUtils.getAsBoolean(trigger, "mustSurvive") || player.health > event.amount) {
                                        playerResearch.setResearch(research, true)
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
}