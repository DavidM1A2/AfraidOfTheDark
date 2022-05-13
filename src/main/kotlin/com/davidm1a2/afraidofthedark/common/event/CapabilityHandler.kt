package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.CapabilityProvider
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.common.capabilities.Capability
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
    fun onAttachCapabilitiesChunk(event: AttachCapabilitiesEvent<Chunk>) {
        event.addCapability(
            ResourceLocation(Constants.MOD_ID, "warded_block_map"),
            CapabilityProvider(ModCapabilities.WARDED_BLOCK_MAP)
        )
    }

    /**
     * When we get an attach capabilities event we attach our world capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesWorld(event: AttachCapabilitiesEvent<World>) {
        val world = event.getObject()
        // Some capabilities only exist server side
        if (!world.isClientSide) {
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "spell_states"),
                CapabilityProvider(ModCapabilities.WORLD_SPELL_STATES)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "structure_mapper"),
                CapabilityProvider(ModCapabilities.WORLD_STRUCTURE_MAPPER)
            )

            if (world.dimension() == ModDimensions.NIGHTMARE_WORLD || world.dimension() == ModDimensions.VOID_CHEST_WORLD) {
                event.addCapability(
                    ResourceLocation(Constants.MOD_ID, "island_visitors"),
                    CapabilityProvider(ModCapabilities.WORLD_ISLAND_VISITORS)
                )
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
        if (event.getObject() is PlayerEntity) {
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_basics"),
                CapabilityProvider(ModCapabilities.PLAYER_BASICS)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_research"),
                CapabilityProvider(ModCapabilities.PLAYER_RESEARCH)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_void_chest_data"),
                CapabilityProvider(ModCapabilities.PLAYER_VOID_CHEST_DATA)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_nightmare_data"),
                CapabilityProvider(ModCapabilities.PLAYER_NIGHTMARE_DATA)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_spell_manager"),
                CapabilityProvider(ModCapabilities.PLAYER_SPELL_MANAGER)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_spell_freeze_data"),
                CapabilityProvider(ModCapabilities.PLAYER_SPELL_FREEZE_DATA)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "player_spell_charm_data"),
                CapabilityProvider(ModCapabilities.PLAYER_SPELL_CHARM_DATA)
            )
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
        if (event.entity is PlayerEntity) {
            val entityPlayer = event.entity as PlayerEntity
            // The server will have correct data, the client needs new data
            if (!event.world.isClientSide) {
                entityPlayer.getBasics().syncAll(entityPlayer)
                entityPlayer.getResearch().sync(entityPlayer, false)
                // Dont sync PLAYER_VOID_CHEST_DATA because it's server side only storage!
                // Dont sync PLAYER_NIGHTMARE_DATA because it's server side only storage!
                entityPlayer.getSpellManager().syncAll(entityPlayer)
                entityPlayer.getSpellFreezeData().sync(entityPlayer)
                // Dont sync PLAYER_SPELL_CHARM_DATA because it's server side only storage!
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
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_BASICS)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_RESEARCH)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_VOID_CHEST_DATA)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_NIGHTMARE_DATA)
        copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_MANAGER)

        // Save off data that ONLY persists when entering the end portal, but not deaths
        if (!event.isWasDeath) {
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_FREEZE_DATA)
            copyCapability(event.original, event.player, ModCapabilities.PLAYER_SPELL_CHARM_DATA)
        }
    }

    private fun <T> copyCapability(oldPlayer: PlayerEntity, newPlayer: PlayerEntity, capability: Capability<T>) {
        val oldCapability = oldPlayer.getCapability(capability).resolve().get()
        val newCapability = newPlayer.getCapability(capability).resolve().get()
        if (oldCapability == null || newCapability == null) {
            LOG.warn("Player was cloned without required capabilities? Missing: ${capability.name}")
            return
        }
        val storage = capability.storage
        // Write the old capability to NBT, then read it right back into the new capability
        val oldCapabilityNbt = storage.writeNBT(capability, oldCapability, null)
        storage.readNBT(capability, newCapability, null, oldCapabilityNbt)
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}