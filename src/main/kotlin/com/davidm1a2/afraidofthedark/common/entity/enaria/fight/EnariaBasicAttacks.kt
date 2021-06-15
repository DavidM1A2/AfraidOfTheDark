package com.davidm1a2.afraidofthedark.common.entity.enaria.fight

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceInstance
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.potion.Effects
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents

class EnariaBasicAttacks(private val fight: EnariaFight) {
    private val attacks = listOf(
        this::shootExplosiveProjectile,
        this::shootFireProjectile,
        this::shootSlowLaser,
        this::shootFreezeLaser,
        this::shootCharmLaser,
        this::useSmokeScreen,
        this::useSelfHeal,
        this::useRandomTeleport
    )

    fun attack() {
        // Keep trying an attack until one goes through
        do {
            val result = attacks.random().invoke()
        } while (!result)

        // Show the auto attack animation
        val enaria = fight.enaria
        AfraidOfTheDark.packetHandler.sendToAllAround(
            AnimationPacket(enaria, "autoattack", "spell", "autoattack"),
            enaria,
            100.0
        )
    }

    private fun shootExplosiveProjectile(): Boolean {
        lookAtNearestPlayer()
        EXPLOSIVE_PROJECTILE_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootFireProjectile(): Boolean {
        lookAtNearestPlayer()
        FIRE_PROJECTILE_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootSlowLaser(): Boolean {
        lookAtNearestPlayer()
        SLOW_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootFreezeLaser(): Boolean {
        lookAtNearestPlayer()
        FREEZE_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootCharmLaser(): Boolean {
        lookAtNearestPlayer()
        CHARM_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun useSmokeScreen(): Boolean {
        val nearestPlayerDistance = fight.playersInFight.mapNotNull {
            fight.enaria.world.getPlayerByUuid(it)
        }.map {
            fight.enaria.getDistanceSq(it)
        }.minOrNull()

        // This attack requires a player within 10 blocks
        if (nearestPlayerDistance != null && nearestPlayerDistance < 10) {
            SMOKE_SCREEN_SELF_SPELL.attemptToCast(fight.enaria)
            return true
        }
        return false
    }

    private fun useSelfHeal(): Boolean {
        return if (fight.enaria.health / fight.enaria.maxHealth < 0.5) {
            HEAL_SELF_SPELL.attemptToCast(fight.enaria)
            true
        } else {
            false
        }
    }

    private fun useRandomTeleport(): Boolean {
        val enaria = fight.enaria
        val world = enaria.world
        val nearestPlayer = fight.playersInFight.mapNotNull {
            world.getPlayerByUuid(it)
        }.minByOrNull {
            enaria.getDistanceSq(it)
        }

        return if (nearestPlayer != null) {
            world.playSound(null, enaria.posX, enaria.posY, enaria.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f)
            enaria.setPositionAndUpdate(nearestPlayer.posX, nearestPlayer.posY, nearestPlayer.posZ)
            world.playSound(null, enaria.posX, enaria.posY, enaria.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f)
            true
        } else {
            false
        }
    }

    private fun lookAtNearestPlayer() {
        fight.playersInFight.mapNotNull {
            fight.enaria.world.getPlayerByUuid(it)
        }.minByOrNull {
            fight.enaria.getDistanceSq(it)
        }?.let {
            fight.enaria.lookAt(EntityAnchorArgument.Type.EYES, it.positionVec.add(0.0, 1.8, 0.0))
        }
    }

    companion object {
        private val EXPLOSIVE_PROJECTILE_SPELL = Spell().apply {
            name = "Enaria Explosive Projectile Attack"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.EXPLOSION).apply { setDefaults() }
            })
        }
        private val FIRE_PROJECTILE_SPELL = Spell().apply {
            name = "Enaria Fire Projectile Attack"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                }
            })
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.AOE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.AOE.setRadius(this, 3.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.BURN).apply { setDefaults() }
            })
        }
        private val SLOW_LASER_SPELL = Spell().apply {
            name = "Enaria Slow Laser Attack"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.POTION_EFFECT).apply {
                    setDefaults()
                    ModSpellEffects.POTION_EFFECT.setPotionType(this, Effects.SLOWNESS)
                    ModSpellEffects.POTION_EFFECT.setPotionStrength(this, 2)
                    ModSpellEffects.POTION_EFFECT.setPotionDuration(this, 20 * 15)
                }
            })
        }
        private val FREEZE_LASER_SPELL = Spell().apply {
            name = "Enaria Freeze Laser Attack"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.FREEZE).apply {
                    setDefaults()
                    ModSpellEffects.FREEZE.setFreezeDuration(this, 20 * 5)
                }
            })
        }
        private val CHARM_LASER_SPELL = Spell().apply {
            name = "Enaria Charm Laser Attack"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.CHARM).apply {
                    setDefaults()
                    ModSpellEffects.CHARM.setCharmDuration(this, 20 * 5)
                }
            })
        }
        private val SMOKE_SCREEN_SELF_SPELL = Spell().apply {
            name = "Enaria Smoke Screen Self"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply { setDefaults() }
                effects[0] = SpellEffectInstance(ModSpellEffects.SMOKE_SCREEN).apply { setDefaults() }
            })
        }
        private val HEAL_SELF_SPELL = Spell().apply {
            name = "Enaria Heal Self"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply { setDefaults() }
                effects[0] = SpellEffectInstance(ModSpellEffects.HEAL).apply {
                    setDefaults()
                    ModSpellEffects.HEAL.setHealAmount(this, 15)
                }
            })
        }
    }
}