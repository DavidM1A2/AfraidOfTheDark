package com.davidm1a2.afraidofthedark.common.entity.enaria.fight

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import java.awt.Color

class EnariaBasicAttacks(private val fight: EnariaFight) {
    private val attacks = listOf(
        this::shootExplosiveProjectile,
        this::shootFireLaser,
        this::shootSlowLaser,
        this::shootFreezeProjectile,
        this::shootCharmProjectile,
        this::shootInstantDamageLaser,
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

    private fun shootFireLaser(): Boolean {
        lookAtNearestPlayer()
        FIRE_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootSlowLaser(): Boolean {
        lookAtNearestPlayer()
        SLOW_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootFreezeProjectile(): Boolean {
        lookAtNearestPlayer()
        FREEZE_PROJECTILE_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootCharmProjectile(): Boolean {
        lookAtNearestPlayer()
        CHARM_PROJECTILE_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun shootInstantDamageLaser(): Boolean {
        lookAtNearestPlayer()
        DAMAGE_LASER_SPELL.attemptToCast(fight.enaria)
        return true
    }

    private fun useSmokeScreen(): Boolean {
        val nearestPlayerDistance = fight.playersInFight.mapNotNull {
            fight.enaria.level.getPlayerByUUID(it)
        }.map {
            fight.enaria.distanceToSqr(it)
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
        val world = enaria.level
        val nearestPlayer = fight.playersInFight.mapNotNull {
            world.getPlayerByUUID(it)
        }.minByOrNull {
            enaria.distanceToSqr(it)
        }

        return if (nearestPlayer != null) {
            world.playSound(
                null,
                enaria.x,
                enaria.y,
                enaria.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundCategory.HOSTILE,
                1.0f,
                1.0f
            )
            enaria.teleportTo(nearestPlayer.x, nearestPlayer.y, nearestPlayer.z)
            world.playSound(
                null,
                enaria.x,
                enaria.y,
                enaria.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundCategory.HOSTILE,
                1.0f,
                1.0f
            )
            true
        } else {
            false
        }
    }

    private fun lookAtNearestPlayer() {
        fight.playersInFight.mapNotNull {
            fight.enaria.level.getPlayerByUUID(it)
        }.minByOrNull {
            fight.enaria.distanceToSqr(it)
        }?.let {
            fight.enaria.lookAt(EntityAnchorArgument.Type.EYES, it.position().add(0.0, 1.8, 0.0))
        }
    }

    companion object {
        private val EXPLOSIVE_PROJECTILE_SPELL = Spell().apply {
            name = "Enaria Explosive Projectile Attack"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                    ModSpellDeliveryMethods.PROJECTILE.setSpeed(this, 28.0)
                    ModSpellDeliveryMethods.PROJECTILE.setColor(this, Color(140, 0, 0))
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.EXPLOSION).apply {
                    setDefaults()
                    ModSpellEffects.EXPLOSION.setRadius(this, 3.5f)
                }
            })
        }
        private val FIRE_LASER_SPELL = Spell().apply {
            name = "Enaria Fire Laser Attack"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
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
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.SPEED).apply {
                    setDefaults()
                    ModSpellEffects.SPEED.setMultiplier(this, -2)
                    ModSpellEffects.SPEED.setDuration(this, 15.0)
                }
                effects[1] = SpellEffectInstance(ModSpellEffects.DISINTEGRATE).apply {
                    setDefaults()
                    ModSpellEffects.DISINTEGRATE.setStrength(this, 30f)
                }
            })
        }
        private val FREEZE_PROJECTILE_SPELL = Spell().apply {
            name = "Enaria Freeze Projectile Attack"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                    ModSpellDeliveryMethods.PROJECTILE.setSpeed(this, 16.0)
                    ModSpellDeliveryMethods.PROJECTILE.setColor(this, Color(0, 140, 100))
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.FREEZE).apply {
                    setDefaults()
                    ModSpellEffects.FREEZE.setDuration(this, 5.0)
                }
                effects[1] = SpellEffectInstance(ModSpellEffects.DISINTEGRATE).apply {
                    setDefaults()
                    ModSpellEffects.DISINTEGRATE.setStrength(this, 30f)
                }
            })
        }
        private val CHARM_PROJECTILE_SPELL = Spell().apply {
            name = "Enaria Charm Projectile Attack"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                    ModSpellDeliveryMethods.PROJECTILE.setSpeed(this, 16.0)
                    ModSpellDeliveryMethods.PROJECTILE.setColor(this, Color(255, 0, 220))
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.CHARM).apply {
                    setDefaults()
                    ModSpellEffects.CHARM.setDuration(this, 5.0)
                }
                effects[1] = SpellEffectInstance(ModSpellEffects.DISINTEGRATE).apply {
                    setDefaults()
                    ModSpellEffects.DISINTEGRATE.setStrength(this, 30f)
                }
            })
        }
        private val DAMAGE_LASER_SPELL = Spell().apply {
            name = "Enaria Damage Laser Attack"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.LASER).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.LASER.setRange(this, 100.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.DISINTEGRATE).apply {
                    setDefaults()
                    ModSpellEffects.DISINTEGRATE.setStrength(this, 80f)
                }
            })
        }
        private val SMOKE_SCREEN_SELF_SPELL = Spell().apply {
            name = "Enaria Smoke Screen Self"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply { setDefaults() }
                effects[0] = SpellEffectInstance(ModSpellEffects.SMOKE_SCREEN).apply { setDefaults() }
            })
        }
        private val HEAL_SELF_SPELL = Spell().apply {
            name = "Enaria Heal Self"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply { setDefaults() }
                effects[0] = SpellEffectInstance(ModSpellEffects.HEAL).apply {
                    setDefaults()
                    ModSpellEffects.HEAL.setAmount(this, 30)
                }
            })
        }
    }
}