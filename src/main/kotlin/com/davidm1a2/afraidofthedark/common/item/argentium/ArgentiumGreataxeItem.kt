package com.davidm1a2.afraidofthedark.common.item.argentium

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDChargeableAxeItem
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import kotlin.math.sqrt

class ArgentiumGreataxeItem : AOTDChargeableAxeItem("argentium_greataxe", ModToolMaterials.ARGENTIUM, 3f, -2.4f, Properties()) {
    init {
        percentChargePerAttack = 35.0
    }
    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        if (player.getResearch().isResearched(ModResearches.METALLURGY)) {
            target.hurt(ModDamageSources.getSilverDamage(player), attackDamage)
        } else {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    override fun performChargeAttack(itemStack: ItemStack, world: Level, entityPlayer: Player): Boolean {
        val nearbyEntities = world.getEntities(
            entityPlayer,
            entityPlayer.boundingBox.inflate(HIT_RANGE.toDouble())
        )

        // Iterate over all entities within 5 blocks of the player
        for (entity in nearbyEntities) {
            // Only knock back living entities
            if (entity is Player || entity is Mob) {
                // Compute the vector from player to entity
                val motionX = entityPlayer.x - entity.x
                val motionZ = entityPlayer.z - entity.z

                // Compute the magnitude of the distance vector
                val hypotenuse = sqrt(motionX * motionX + motionZ * motionZ)

                // Compute the strength we knock back with
                val knockbackStrength =
                    EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, entityPlayer) + 2.toDouble()

                // Compute the damage we hit with based on sharpness
                val sharpnessDamage = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, entityPlayer)
                // Attack the entity with player damage and move them back
                entity.hurt(
                    DamageSource.playerAttack(entityPlayer),
                    attackDamage + 4.0f + sharpnessDamage * 1.5f
                )
                entity.push(
                    -motionX * knockbackStrength * 0.6 / hypotenuse,
                    0.1,
                    -motionZ * knockbackStrength * 0.6 / hypotenuse
                )
            }
        }
        world.playSound(null, entityPlayer.x, entityPlayer.y, entityPlayer.z, SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0f, 0.5f)
        return true
    }

    companion object {
        // The AOE knockback range
        private const val HIT_RANGE = 5
    }
}