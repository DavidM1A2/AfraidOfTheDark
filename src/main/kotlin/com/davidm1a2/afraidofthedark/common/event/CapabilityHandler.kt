package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.*
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerEvent.Clone
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to register all of our mod capabilities
 */
class CapabilityHandler {
    /**
     * When we get an attach capabilities event we attach our world capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesWorld(event: AttachCapabilitiesEvent<World>) {
        val world = event.getObject()
        // Some capabilities only exist server side
        if (!world.isRemote) {
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "spell_states"),
                CapabilityProvider(ModCapabilities.WORLD_SPELL_STATES)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "structure_collision_map"),
                CapabilityProvider(ModCapabilities.WORLD_STRUCTURE_COLLISION_MAP)
            )
            event.addCapability(
                ResourceLocation(Constants.MOD_ID, "structure_miss_counter"),
                CapabilityProvider(ModCapabilities.STRUCTURE_MISS_COUNTER)
            )

            if (DIMENSIONS_WITH_ISLAND_VISITORS.contains(world.dimension.type)) {
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
            if (!event.world.isRemote) {
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
     * When the player dies, he is cloned but no capabilities are copied by default, so we need to manually do that here
     *
     * @param event The clone event
     */
    @SubscribeEvent
    fun onClonePlayer(event: Clone) {
        // The player only loses capabilities upon death
        if (event.isWasDeath) {
            // The player needs to be "Alive" to read capabilities
            event.original.revive()

            // Grab new and original player capabilities
            val originalPlayerBasics = event.original.getBasics()
            val newPlayerBasics = event.player.getBasics()

            val originalPlayerResearch = event.original.getResearch()
            val newPlayerResearch = event.player.getResearch()

            val originalPlayerVoidChestData = event.original.getVoidChestData()
            val newPlayerVoidChestData = event.player.getVoidChestData()

            val originalPlayerNightmareData = event.original.getNightmareData()
            val newPlayerNightmareData = event.player.getNightmareData()

            val originalPlayerSpellManager = event.original.getSpellManager()
            val newPlayerSpellManager = event.player.getSpellManager()

            // Kill the player again
            event.original.remove()

            // Don't copy PLAYER_SPELL_FREEZE_DATA, if the player dies they aren't frozen anymore

            // Don't copy PLAYER_SPELL_CHARM_DATA, if the player dies they aren't charmed anymore

            // Grab the NBT compound off of the original capabilities
            val originalPlayerBasicsNBT = ModCapabilities.PLAYER_BASICS.storage.writeNBT(
                ModCapabilities.PLAYER_BASICS,
                originalPlayerBasics,
                null
            ) as CompoundNBT
            val originalPlayerResearchNBT = ModCapabilities.PLAYER_RESEARCH.storage.writeNBT(
                ModCapabilities.PLAYER_RESEARCH,
                originalPlayerResearch,
                null
            ) as CompoundNBT
            val originalPlayerVoidChestDataNBT = ModCapabilities.PLAYER_VOID_CHEST_DATA.storage.writeNBT(
                ModCapabilities.PLAYER_VOID_CHEST_DATA,
                originalPlayerVoidChestData,
                null
            ) as CompoundNBT
            val originalPlayerNightmareDataNBT = ModCapabilities.PLAYER_NIGHTMARE_DATA.storage.writeNBT(
                ModCapabilities.PLAYER_NIGHTMARE_DATA,
                originalPlayerNightmareData,
                null
            ) as CompoundNBT
            val originalPlayerSpellManagerNBT = ModCapabilities.PLAYER_SPELL_MANAGER.storage.writeNBT(
                ModCapabilities.PLAYER_SPELL_MANAGER,
                originalPlayerSpellManager,
                null
            ) as CompoundNBT

            // Copy the NBT compound onto the new capabilities
            ModCapabilities.PLAYER_BASICS.storage.readNBT(
                ModCapabilities.PLAYER_BASICS,
                newPlayerBasics,
                null,
                originalPlayerBasicsNBT
            )
            ModCapabilities.PLAYER_RESEARCH.storage.readNBT(
                ModCapabilities.PLAYER_RESEARCH,
                newPlayerResearch,
                null,
                originalPlayerResearchNBT
            )
            ModCapabilities.PLAYER_VOID_CHEST_DATA.storage.readNBT(
                ModCapabilities.PLAYER_VOID_CHEST_DATA,
                newPlayerVoidChestData,
                null,
                originalPlayerVoidChestDataNBT
            )
            ModCapabilities.PLAYER_NIGHTMARE_DATA.storage.readNBT(
                ModCapabilities.PLAYER_NIGHTMARE_DATA,
                newPlayerNightmareData,
                null,
                originalPlayerNightmareDataNBT
            )
            ModCapabilities.PLAYER_SPELL_MANAGER.storage.readNBT(
                ModCapabilities.PLAYER_SPELL_MANAGER,
                newPlayerSpellManager,
                null,
                originalPlayerSpellManagerNBT
            )
        }
    }

    companion object {
        private val DIMENSIONS_WITH_ISLAND_VISITORS by lazy {
            listOf(
                ModDimensions.NIGHTMARE_TYPE,
                ModDimensions.VOID_CHEST_TYPE
            )
        }
    }
}