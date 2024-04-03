package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.*
import com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward.WardedBlockMapCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerNightmareDataCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerVoidChestDataCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.PlayerResearchCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.PlayerSpellManagerCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.*
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.WorldIslandVisitorsCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.WorldSpellStatesCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.world.structure.WorldStructureMapperCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerEvent.Clone
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager

/**
 * Class used to register all of our mod capabilities
 */
class CapabilityHandler {
    /**
     * When we get an attach capabilities event we attach our chunk capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesChunk(event: AttachCapabilitiesEvent<LevelChunk>) {
        event.addCapability(ResourceLocation(Constants.MOD_ID, "warded_block_map"), WardedBlockMapCapabilitySerializer())
    }

    /**
     * When we get an attach capabilities event we attach our world capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesWorld(event: AttachCapabilitiesEvent<Level>) {
        val world = event.getObject()
        // Some capabilities only exist server side
        if (!world.isClientSide) {
            event.addCapability(ResourceLocation(Constants.MOD_ID, "spell_states"), WorldSpellStatesCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "structure_mapper"), WorldStructureMapperCapabilitySerializer())

            if (world.dimension() == ModDimensions.NIGHTMARE_WORLD || world.dimension() == ModDimensions.VOID_CHEST_WORLD) {
                event.addCapability(ResourceLocation(Constants.MOD_ID, "island_visitors"), WorldIslandVisitorsCapabilitySerializer())
            }
        }
    }

    /**
     * When we get an attach capabilities event we attach our player capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesEntity(event: AttachCapabilitiesEvent<Entity>) {
        // If the entity is a player then add any player specific capabilities
        if (event.getObject() is Player) {
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_basics"), AOTDPlayerBasicsCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_research"), PlayerResearchCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_void_chest_data"), PlayerVoidChestDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_nightmare_data"), PlayerNightmareDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_manager"), PlayerSpellManagerCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_freeze_data"), PlayerSpellFreezeDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_charm_data"), PlayerSpellCharmDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_lunar_data"), PlayerSpellLunarDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_solar_data"), PlayerSpellSolarDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_thermal_data"), PlayerSpellThermalDataCapabilitySerializer())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_innate_data"), PlayerSpellInnateDataCapabilitySerializer())
        }
    }

    /**
     * When an entity joins the world we perform a capability sync
     *
     * @param event The join event we will check
     */
    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        // When the player joins the world
        if (event.entity is Player) {
            val entityPlayer = event.entity as Player
            // The server will have correct data, the client needs new data
            if (!event.world.isClientSide) {
                entityPlayer.getBasics().syncAll(entityPlayer)
                entityPlayer.getResearch().sync(entityPlayer, false)
                // Dont sync PLAYER_VOID_CHEST_DATA because it's server side only storage!
                // Dont sync PLAYER_NIGHTMARE_DATA because it's server side only storage!
                entityPlayer.getSpellManager().syncAll(entityPlayer)
                entityPlayer.getSpellFreezeData().sync(entityPlayer)
                // Dont sync PLAYER_SPELL_CHARM_DATA because it's server side only storage!
                entityPlayer.getSpellLunarData().sync(entityPlayer)
                entityPlayer.getSpellSolarData().sync(entityPlayer)
                entityPlayer.getSpellThermalData().sync(entityPlayer)
                entityPlayer.getSpellInnateData().sync(entityPlayer)
            }
        }
    }

    /**
     * When the player is cloned no capabilities are copied by default, so we need to manually do that here
     *
     * @param event The clone event
     */
    @SubscribeEvent
    fun onClonePlayer(event: Clone) {
        // Save off data that persists when entering the end portal, and also across deaths
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_BASICS, ::AOTDPlayerBasicsCapabilitySerializer)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_RESEARCH, ::PlayerResearchCapabilitySerializer)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_VOID_CHEST_DATA, ::PlayerVoidChestDataCapabilitySerializer)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_NIGHTMARE_DATA, ::PlayerNightmareDataCapabilitySerializer)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_MANAGER, ::PlayerSpellManagerCapabilitySerializer)

        // Save off data that ONLY persists when entering the end portal, but not deaths
        if (!event.isWasDeath) {
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_FREEZE_DATA, ::PlayerSpellFreezeDataCapabilitySerializer)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_CHARM_DATA, ::PlayerSpellCharmDataCapabilitySerializer)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_LUNAR_DATA, ::PlayerSpellLunarDataCapabilitySerializer)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_SOLAR_DATA, ::PlayerSpellSolarDataCapabilitySerializer)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_THERMAL_DATA, ::PlayerSpellThermalDataCapabilitySerializer)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_INNATE_DATA, ::PlayerSpellInnateDataCapabilitySerializer)
        }
    }

    private fun <I : Any, T : Tag> copyCapability(oldPlayer: Player, newPlayer: Player, capability: Capability<I>, serializerFactory: ((I) -> INBTSerializable<T>)) {
        val oldCapability = getCapabilitySafe(oldPlayer, capability)
        val newCapability = getCapabilitySafe(newPlayer, capability)
        if (oldCapability == null || newCapability == null) {
            LOG.warn("Player was cloned without required capabilities? Missing: ${capability.name}")
            return
        }

        val oldSerializer = serializerFactory(oldCapability)
        val newSerializer = serializerFactory(newCapability)
        newSerializer.deserializeNBT(oldSerializer.serializeNBT())
    }

    private fun <I : Any> getCapabilitySafe(player: Player, capability: Capability<I>): I? {
        val instance = player.getCapability(capability)
        if (instance.isPresent) {
            return instance.resolve().get()
        }

        // If the player just died revive their capabilties for a brief second to clone onto the new player
        if (player.isRemoved) {
            player.reviveCaps()
            try {
                val revivedInstance = player.getCapability(capability)
                if (revivedInstance.isPresent) {
                    return revivedInstance.resolve().get()
                }
            } finally {
                player.invalidateCaps()
            }
        }

        return null
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}