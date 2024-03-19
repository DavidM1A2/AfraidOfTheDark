package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import java.util.*

class DarknessFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.Darkness) {
    private var ticksExisted: Int = 0
    private val werewolfIds = mutableListOf<UUID>()

    override fun start() {
        val numWerewolvesToSpawn = fight.playersInFight.size
        for (ignored in 0 until numWerewolvesToSpawn) {
            val werewolf = WerewolfEntity(fight.enaria.level)
            werewolf.setPos(fight.centerPos.x + 0.5, fight.centerPos.y + 0.5, fight.centerPos.z + 0.5)
            werewolf.canAttackAnyone = true
            fight.enaria.level.addFreshEntity(werewolf)
            werewolfIds.add(werewolf.uuid)
        }
    }

    override fun tick() {
        if (ticksExisted % 20 == 0) {
            fight.playersInFight.forEach {
                fight.enaria.level.getPlayerByUUID(it)?.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 5, 0))
            }
        }
        ticksExisted = ticksExisted + 1
    }

    override fun forceStop() {
        fight.playersInFight.forEach {
            fight.enaria.level.getPlayerByUUID(it)?.removeEffect(MobEffects.BLINDNESS)
        }
        werewolfIds.forEach {
            (fight.enaria.level as ServerLevel).getEntity(it)?.kill()
        }
    }

    override fun isOver(): Boolean {
        werewolfIds.removeIf {
            val werewolf = (fight.enaria.level as ServerLevel).getEntity(it)
            werewolf == null || !werewolf.isAlive
        }
        return werewolfIds.isEmpty()
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_TICKS_EXISTED, ticksExisted)

        val werewolfIdNbts = ListTag()
        werewolfIds.forEach {
            werewolfIdNbts.add(NbtUtils.createUUID(it))
        }
        nbt.put(NBT_WEREWOLF_IDS, werewolfIdNbts)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        super.deserializeNBT(nbt)

        ticksExisted = nbt.getInt(NBT_TICKS_EXISTED)

        val werewolfIdNbts = nbt.getList(NBT_WEREWOLF_IDS, Tag.TAG_INT_ARRAY.toInt())
        werewolfIdNbts.forEach {
            werewolfIds.add(NbtUtils.loadUUID(it))
        }
    }

    companion object {
        private const val NBT_TICKS_EXISTED = "ticks_existed"
        private const val NBT_WEREWOLF_IDS = "werewolf_ids"
    }
}