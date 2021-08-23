package com.davidm1a2.afraidofthedark.common.registry.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.living.LivingDamageEvent
import java.io.InputStreamReader

object AOTDResearchLoader {
    private val GSON = GsonBuilder().disableHtmlEscaping().create()

    fun load(name: String): Research {
        val data = ResourceLocation(Constants.MOD_ID, "researches/$name.json")
        val element = InputStreamReader(ResourceUtil.getInputStream(data).buffered()).use {
            GSON.fromJson(it, JsonElement::class.java)
        }
        return Research.CODEC.decode(JsonOps.INSTANCE, element)
            .get()
            .orThrow()
            .first
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