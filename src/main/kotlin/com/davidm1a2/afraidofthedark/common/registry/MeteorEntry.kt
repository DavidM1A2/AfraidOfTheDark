package com.davidm1a2.afraidofthedark.common.registry

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import com.mojang.datafixers.util.Function7
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslatableComponent
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry
import org.apache.logging.log4j.LogManager

/**
 * Base class for all meteor entries, meteor entries are used to define new meteor properties
 *
 * @property icon The resource location containing the icon that this meteor type will use in the telescope
 * @property minRadius The minimum radius of the meteor
 * @property maxRadius The maximum radius of the meteor
 * @property richnessPercent What percent of the meteor is ore vs meteor block
 * @property interiorBlock The block that the meteor uses on the inside
 * @property prerequisiteResearch The pre-requisite research to be able to see this type of meteor
 */
class MeteorEntry(
    val icon: ResourceLocation,
    val minRadius: Int,
    val maxRadius: Int,
    val richnessPercent: Double,
    val interiorBlock: Block,
    lazyPrerequisiteResearch: Lazy<Research>,
) : ForgeRegistryEntry<MeteorEntry>() {
    // Why do we need this lazy initializer? Because forge loads registries in random order, so the RESEARCH registry might not be valid yet
    val prerequisiteResearch: Research by lazyPrerequisiteResearch

    init {
        // Ensure the min/max radii are valid values
        if (minRadius < 2) {
            LOG.error("Meteor entries should not have a min radius less than 2!")
        }
        if (minRadius > maxRadius) {
            LOG.error("Meteor entries max-radius should be larger than the min-radius!")
        }
    }

    /**
     * @return The unlocalized name of the meteor entry
     */
    fun getName(): ITextComponent {
        return TranslatableComponent("meteor_entry.${registryName!!.namespace}.${registryName!!.path}")
    }

    companion object {
        private val LOG = LogManager.getLogger()

        val CODEC: Codec<MeteorEntry> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("forge:registry_name").forGetter(MeteorEntry::getRegistryName),
                ResourceLocation.CODEC.fieldOf("icon").forGetter(MeteorEntry::icon),
                Codec.INT.fieldOf("min_radius").forGetter(MeteorEntry::minRadius),
                Codec.INT.fieldOf("max_radius").forGetter(MeteorEntry::maxRadius),
                Codec.DOUBLE.fieldOf("richness_percent").forGetter(MeteorEntry::richnessPercent),
                ForgeRegistries.BLOCKS.codec()
                    .fieldOf("interior_block")
                    .forGetter(MeteorEntry::interiorBlock),
                ModRegistries.RESEARCH.codec()
                    .lazy()
                    .fieldOf("prerequisite_research")
                    .forGetter { meteorEntry -> lazyOf(meteorEntry.prerequisiteResearch) }
            ).apply(it, it.stable(Function7 { name, icon, minRadius, maxRadius, richnessPercent, interiorBlock, prerequisiteResearch ->
                MeteorEntry(icon, minRadius, maxRadius, richnessPercent, interiorBlock, prerequisiteResearch).setRegistryName(name)
            }))
        }
    }
}