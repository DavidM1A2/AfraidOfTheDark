package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight

enum class EnariaFightEvents(
    private val factory: (fight: EnariaFight) -> EnariaFightEvent
) {
    LavaRise(::LavaRiseFightEvent),
    WaterFall(::WaterFallFightEvent),
    GrassSkeleton(::GrassSkeletonFightEvent),
    SummonSentinels(::SummonSentinelsFightEvent),
    RegenerateRoom(::RegenerateRoomFightEvent),
    Darkness(::DarknessFightEvent);

    fun build(fight: EnariaFight): EnariaFightEvent {
        return factory.invoke(fight)
    }
}