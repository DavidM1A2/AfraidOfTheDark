package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.world.entity.player.Player
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ChestContainer
import net.minecraft.inventory.container.SimpleNamedContainerProvider
import net.minecraft.stats.Stats
import net.minecraft.util.text.TranslationTextComponent

/**
 * Effect that creates an ender chest
 */
class EnderPocketSpellEffect : AOTDSpellEffect("ender_pocket", ModResearches.POCKET_DIMENSION) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        // If we hit a player open the ender chest GUI
        val entity = state.entity
        if (entity is Player) {
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.ENDER)
                    .position(state.position)
                    .iterations(15)
                    .build()
            )
            val enderChest = entity.enderChestInventory
            entity.openMenu(SimpleNamedContainerProvider({ inner: Int, inventory: PlayerInventory, _: Player ->
                ChestContainer.threeRows(
                    inner,
                    inventory,
                    enderChest
                )
            }, TranslationTextComponent("container.enderchest")))
            entity.awardStat(Stats.OPEN_ENDERCHEST)
        } else {
            return ProcResult.failure()
        }
        return ProcResult.success()
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 45.0
    }
}