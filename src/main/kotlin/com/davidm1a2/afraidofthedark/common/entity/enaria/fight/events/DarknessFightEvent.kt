package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.util.Constants
import java.util.*

class DarknessFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.Darkness) {
    private var ticksExisted: Int = 0
    private val werewolfIds = mutableListOf<UUID>()

    override fun start() {
        val numWerewolvesToSpawn = fight.playersInFight.size
        for (ignored in 0 until numWerewolvesToSpawn) {
            val werewolf = WerewolfEntity(fight.enaria.world)
            werewolf.setPosition(fight.centerPos.x + 0.5, fight.centerPos.y - 3.0, fight.centerPos.z + 0.5)
            werewolf.setCanAttackAnyone(true)
            fight.enaria.world.addEntity(werewolf)
            werewolfIds.add(werewolf.uniqueID)
        }
    }

    override fun tick() {
        if (ticksExisted % 20 == 0) {
            fight.playersInFight.forEach {
                fight.enaria.world.getPlayerByUuid(it)?.addPotionEffect(EffectInstance(Effects.BLINDNESS, 20 * 5, 0))
            }
        }
        ticksExisted = ticksExisted + 1
    }

    override fun forceStop() {
        fight.playersInFight.forEach {
            fight.enaria.world.getPlayerByUuid(it)?.removePotionEffect(Effects.BLINDNESS)
        }
        werewolfIds.forEach {
            (fight.enaria.world as ServerWorld).getEntityByUuid(it)?.onKillCommand()
        }
    }

    override fun isOver(): Boolean {
        werewolfIds.removeIf {
            val werewolf = (fight.enaria.world as ServerWorld).getEntityByUuid(it)
            werewolf == null || !werewolf.isAlive
        }
        return werewolfIds.isEmpty()
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_TICKS_EXISTED, ticksExisted)

        val werewolfIdNbts = ListNBT()
        werewolfIds.forEach {
            werewolfIdNbts.add(NBTUtil.writeUniqueId(it))
        }
        nbt.put(NBT_WEREWOLF_IDS, werewolfIdNbts)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)

        ticksExisted = nbt.getInt(NBT_TICKS_EXISTED)

        val werewolfIdNbts = nbt.getList(NBT_WEREWOLF_IDS, Constants.NBT.TAG_COMPOUND)
        werewolfIdNbts.forEach {
            werewolfIds.add(NBTUtil.readUniqueId(it as CompoundNBT))
        }
    }

    companion object {
        private const val NBT_TICKS_EXISTED = "ticks_existed"
        private const val NBT_WEREWOLF_IDS = "werewolf_ids"
    }
}