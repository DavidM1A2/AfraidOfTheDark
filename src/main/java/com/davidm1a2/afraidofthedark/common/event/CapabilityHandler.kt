package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsProvider
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.*
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.AOTDPlayerResearchImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.AOTDPlayerResearchProvider
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.AOTDPlayerResearchStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.AOTDPlayerSpellManagerImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.AOTDPlayerSpellManagerProvider
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.AOTDPlayerSpellManagerStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.*
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerEvent.Clone
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class used to register all of our mod capabilities
 */
class CapabilityHandler
{
    /**
     * Called to initialize all of our mod capabilities into the capability manager if it was not already initialized
     */
    init
    {
        // If the capability manager was not initialized initialize it
        if (!wasInitialized)
        {
            CapabilityManager.INSTANCE.register(IAOTDPlayerBasics::class.java, AOTDPlayerBasicsStorage()) { AOTDPlayerBasicsImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerResearch::class.java, AOTDPlayerResearchStorage()) { AOTDPlayerResearchImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerVoidChestData::class.java, AOTDPlayerVoidChestDataStorage()) { AOTDPlayerVoidChestDataImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerNightmareData::class.java, AOTDPlayerNightmareDataStorage()) { AOTDPlayerNightmareImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerSpellManager::class.java, AOTDPlayerSpellManagerStorage()) { AOTDPlayerSpellManagerImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerSpellFreezeData::class.java, AOTDPlayerSpellFreezeDataStorage()) { AOTDPlayerSpellFreezeDataImpl() }
            CapabilityManager.INSTANCE.register(IAOTDPlayerSpellCharmData::class.java, AOTDPlayerSpellCharmDataStorage()) { AOTDPlayerSpellCharmDataImpl() }
            wasInitialized = true
        }
    }

    /**
     * When we get an attach capabilites event we attach our player capabilities
     *
     * @param event The attach event that we will add to
     */
    @SubscribeEvent
    fun onAttachCapabilitiesEntity(event: AttachCapabilitiesEvent<Entity>)
    {
        // If the entity is a player then add any player specific capabilities
        if (event.getObject() is EntityPlayer)
        {
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_basics"), AOTDPlayerBasicsProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_research"), AOTDPlayerResearchProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_void_chest_data"), AOTDPlayerVoidChestDataProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_nightmare_data"), AOTDPlayerNightmareDataProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_manager"), AOTDPlayerSpellManagerProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_freeze_data"), AOTDPlayerSpellFreezeDataProvider())
            event.addCapability(ResourceLocation(Constants.MOD_ID, "player_spell_charm_data"), AOTDPlayerSpellCharmDataProvider())
        }
    }

    /**
     * When an entity joins the world we perform a capability sync
     *
     * @param event The join event we will check
     */
    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent)
    {
        // When the player joins the world
        if (event.entity is EntityPlayer)
        {
            val entityPlayer = event.entity as EntityPlayer
            // The server will have correct data, the client needs new data
            if (!event.world.isRemote)
            {
                entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null)!!.syncAll(entityPlayer)
                entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.sync(entityPlayer, false)
                // Dont sync PLAYER_VOID_CHEST_DATA because it's server side only storage!
                // Dont sync PLAYER_NIGHTMARE_DATA because it's server side only storage!
                entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!.syncAll(entityPlayer)
                entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)!!.sync(entityPlayer)
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
    fun onClonePlayer(event: Clone)
    {
        // The player only loses capabilities upon death
        if (event.isWasDeath)
        {
            // Grab new and original player capabilities
            val originalPlayerBasics = event.original.getCapability(ModCapabilities.PLAYER_BASICS, null)
            val newPlayerBasics = event.entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null)

            val originalPlayerResearch = event.original.getCapability(ModCapabilities.PLAYER_RESEARCH, null)
            val newPlayerResearch = event.entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null)

            val originalPlayerVoidChestData = event.original.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null)
            val newPlayerVoidChestData = event.entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null)

            val originalPlayerNightmareData = event.original.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null)
            val newPlayerNightmareData = event.entityPlayer.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null)

            val originalPlayerSpellManager = event.original.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)
            val newPlayerSpellManager = event.entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)

            // Don't copy PLAYER_SPELL_FREEZE_DATA, if the player dies they aren't frozen anymore

            // Don't copy PLAYER_SPELL_CHARM_DATA, if the player dies they aren't charmed anymore

            // Grab the NBT compound off of the original capabilities
            val originalPlayerBasicsNBT = ModCapabilities.PLAYER_BASICS.storage.writeNBT(ModCapabilities.PLAYER_BASICS, originalPlayerBasics, null) as NBTTagCompound
            val originalPlayerResearchNBT = ModCapabilities.PLAYER_RESEARCH.storage.writeNBT(ModCapabilities.PLAYER_RESEARCH, originalPlayerResearch, null) as NBTTagCompound
            val originalPlayerVoidChestDataNBT = ModCapabilities.PLAYER_VOID_CHEST_DATA.storage.writeNBT(ModCapabilities.PLAYER_VOID_CHEST_DATA, originalPlayerVoidChestData, null) as NBTTagCompound
            val originalPlayerNightmareDataNBT = ModCapabilities.PLAYER_NIGHTMARE_DATA.storage.writeNBT(ModCapabilities.PLAYER_NIGHTMARE_DATA, originalPlayerNightmareData, null) as NBTTagCompound
            val originalPlayerSpellManagerNBT = ModCapabilities.PLAYER_SPELL_MANAGER.storage.writeNBT(ModCapabilities.PLAYER_SPELL_MANAGER, originalPlayerSpellManager, null) as NBTTagCompound

            // Copy the NBT compound onto the new capabilities
            ModCapabilities.PLAYER_BASICS.storage.readNBT(ModCapabilities.PLAYER_BASICS, newPlayerBasics, null, originalPlayerBasicsNBT)
            ModCapabilities.PLAYER_RESEARCH.storage.readNBT(ModCapabilities.PLAYER_RESEARCH, newPlayerResearch, null, originalPlayerResearchNBT)
            ModCapabilities.PLAYER_VOID_CHEST_DATA.storage.readNBT(ModCapabilities.PLAYER_VOID_CHEST_DATA, newPlayerVoidChestData, null, originalPlayerVoidChestDataNBT)
            ModCapabilities.PLAYER_NIGHTMARE_DATA.storage.readNBT(ModCapabilities.PLAYER_NIGHTMARE_DATA, newPlayerNightmareData, null, originalPlayerNightmareDataNBT)
            ModCapabilities.PLAYER_SPELL_MANAGER.storage.readNBT(ModCapabilities.PLAYER_SPELL_MANAGER, newPlayerSpellManager, null, originalPlayerSpellManagerNBT)
        }
    }

    companion object
    {
        // Store a flag that ensures if we create multiple capability handlers we only initialize once
        private var wasInitialized = false
    }
}