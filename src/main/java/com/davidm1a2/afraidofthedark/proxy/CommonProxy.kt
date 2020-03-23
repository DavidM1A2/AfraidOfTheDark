package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import com.davidm1a2.afraidofthedark.common.packets.capabilityPackets.*
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.*
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.oredict.OreDictionary

/**
 * Common proxy that is instantiated on both sides (CLIENT and SERVER)
 */
abstract class CommonProxy : IProxy {
    /**
     * Called to initialize any mod blocks into the ore dictionary. Happens on server and client
     */
    override fun initializeOreDictionary() {
        OreDictionary.registerOre("logWood", ModBlocks.GRAVEWOOD)
        OreDictionary.registerOre("plankWood", ModBlocks.GRAVEWOOD_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.GRAVEWOOD_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.GRAVEWOOD_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.GRAVEWOOD_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.GRAVEWOOD_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.GRAVEWOOD_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.GRAVEWOOD_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.GRAVEWOOD_DOOR)

        OreDictionary.registerOre("logWood", ModBlocks.MANGROVE)
        OreDictionary.registerOre("plankWood", ModBlocks.MANGROVE_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.MANGROVE_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.MANGROVE_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.MANGROVE_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.MANGROVE_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.MANGROVE_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.MANGROVE_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.MANGROVE_DOOR)

        OreDictionary.registerOre("logWood", ModBlocks.SACRED_MANGROVE)
        OreDictionary.registerOre("plankWood", ModBlocks.SACRED_MANGROVE_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.SACRED_MANGROVE_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.SACRED_MANGROVE_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.SACRED_MANGROVE_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.SACRED_MANGROVE_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.SACRED_MANGROVE_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.SACRED_MANGROVE_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.SACRED_MANGROVE_DOOR)
    }

    /**
     * Registers any packets used by AOTD
     */
    override fun registerPackets() {
        val packetHandler = AfraidOfTheDark.INSTANCE.packetHandler
        packetHandler.registerBidiPacket(SyncStartedAOTD::class.java, SyncStartedAOTD.Handler())
        packetHandler.registerBidiPacket(SyncAOTDPlayerBasics::class.java, SyncAOTDPlayerBasics.Handler())
        packetHandler.registerBidiPacket(SyncResearch::class.java, SyncResearch.Handler())
        packetHandler.registerBidiPacket(UpdateWatchedMeteor::class.java, UpdateWatchedMeteor.Handler())
        packetHandler.registerBidiPacket(SyncSpell::class.java, SyncSpell.Handler())
        packetHandler.registerBidiPacket(SyncClearSpells::class.java, SyncClearSpells.Handler())
        packetHandler.registerPacket(SyncAnimation::class.java, SyncAnimation.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncItemWithCooldown::class.java, SyncItemWithCooldown.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncVoidChest::class.java, SyncVoidChest.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncParticle::class.java, SyncParticle.Handler(), Side.CLIENT)
        packetHandler.registerPacket(SyncFreezeData::class.java, SyncFreezeData.Handler(), Side.CLIENT)
        packetHandler.registerPacket(FireWristCrossbow::class.java, FireWristCrossbow.Handler(), Side.SERVER)
        packetHandler.registerPacket(ProcessSextantInput::class.java, ProcessSextantInput.Handler(), Side.SERVER)
        packetHandler.registerPacket(
            SyncSelectedWristCrossbowBolt::class.java,
            SyncSelectedWristCrossbowBolt.Handler(),
            Side.SERVER
        )
        packetHandler.registerPacket(SyncSpellKeyPress::class.java, SyncSpellKeyPress.Handler(), Side.SERVER)
    }
}