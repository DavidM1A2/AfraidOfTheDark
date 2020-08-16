package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemArmor
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.math.sqrt

/**
 * Class representing the 4 different pieces of igneous armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 */
class ItemIgneousArmor(baseName: String, equipmentSlot: EntityEquipmentSlot) :
    AOTDItemArmor(baseName, ModArmorMaterials.IGNEOUS, equipmentSlot, Properties().defaultMaxDamage(0)) {
    private val percentOfDamageBlocked = 0.9f

    /**
     * Gets the resource location path of the texture for the armor when worn by the player
     *
     * @param stack  The armor itemstack
     * @param entity The entity that is wearing the  armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EntityEquipmentSlot, type: String?): String {
        // Igneous 1 is for helm, boots, and chest while igneous 2 is for leggings
        return if (slot == EntityEquipmentSlot.LEGS) {
            "afraidofthedark:textures/armor/igneous_2.png"
        } else {
            "afraidofthedark:textures/armor/igneous_1.png"
        }
    }

    /**
     * Adds a tooltip to the armor piece
     *
     * @param stack   The itemstack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip list to add to
     * @param flag  The flag telling us if advanced tooltips are on or not
     */
    @OnlyIn(Dist.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TextComponentTranslation(LocalizationConstants.TOOLTIP_MAGIC_ARMOR_NEVER_BREAKS))
            tooltip.add(TextComponentTranslation("tooltip.afraidofthedark.igneous_armor.effect"))
        } else {
            tooltip.add(TextComponentTranslation(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param stack The itemstack of the armor item
     * @param player    The player that is wearing the armor
     * @param world     The world the player is in
     */
    override fun onArmorTick(stack: ItemStack, world: World, player: EntityPlayer) {
        // Dead players don't have capabilities
        if (player.isAlive) {
            // If the armor wearer is burning extinguish the fire
            if (player.isBurning) {
                // Ensure the player has the right research
                if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
                    // If the player is wearing full armor then add armor set bonuses
                    if (isWearingFullArmor(player)) {
                        player.extinguish()
                    }
                }
            }
        }
    }

    override fun processDamage(entity: EntityLivingBase, armorStack: ItemStack, source: DamageSource, amount: Float, slot: EntityEquipmentSlot): Double {
        // Compute armor properties for players only
        if (entity is EntityPlayer) {
            // Ensure the player has the right research
            if (entity.getResearch().isResearched(ModResearches.IGNEOUS)) {
                // If the player is wearing full armor then add armor set bonuses
                if (isWearingFullArmor(entity)) {
                    val damageSourceEntity = source.trueSource
                    // If the damage source is non-null set them on fire
                    if (damageSourceEntity != null) {
                        damageSourceEntity.setFire(5)

                        // Also knock the damage source entity back
                        val motionX = entity.position.x - damageSourceEntity.position.x.toDouble()
                        val motionZ = entity.position.z - damageSourceEntity.position.z.toDouble()
                        val hypotenuse = sqrt(motionX * motionX + motionZ * motionZ)

                        // Move the entity away from the player
                        damageSourceEntity.addVelocity(
                            -motionX * KNOCKBACK_STRENGTH * 0.6 / hypotenuse,
                            0.1,
                            -motionZ * KNOCKBACK_STRENGTH * 0.6 / hypotenuse
                        )
                    }
                }

                // Blocks all fire damage
                if (FIRE_SOURCES.contains(source)) {
                    return getRatio(slot)
                }
                // Blocks no true damage
                else if (TRUE_DAMAGE_SOURCES.contains(source)) {
                    return 0.0
                }
            } else {
                // Armor is useless without research
                return 0.0
            }
        }

        return getRatio(slot) * percentOfDamageBlocked
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private fun getRatio(slot: EntityEquipmentSlot): Double {
        // Total protection of each piece
        val totalProtection = 3 + 6 + 8 + 3
        return when (slot) {
            EntityEquipmentSlot.HEAD, EntityEquipmentSlot.FEET -> 3.0 / totalProtection * percentOfDamageBlocked
            EntityEquipmentSlot.LEGS -> 6.0 / totalProtection * percentOfDamageBlocked
            EntityEquipmentSlot.CHEST -> 8.0 / totalProtection * percentOfDamageBlocked
            else -> 0.0
        }
    }

    companion object {
        // How much strength the armor knocks back enemies that attack you
        private const val KNOCKBACK_STRENGTH = 0.6

        // Damage sources that relate to fire damage
        private val FIRE_SOURCES = setOf(DamageSource.IN_FIRE, DamageSource.ON_FIRE)

        // Damage sources that relate to unblockable damage
        private val TRUE_DAMAGE_SOURCES = setOf(
            DamageSource.DROWN,
            DamageSource.FALL,
            DamageSource.IN_WALL,
            DamageSource.OUT_OF_WORLD,
            DamageSource.STARVE
        )
    }
}