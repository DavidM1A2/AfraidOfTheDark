package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getSilverDamage
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDChargeableSwordItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.MobEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level
import kotlin.math.max

/**
 * Igneous sword lets you set fire to an area with its special ability
 *
 * @constructor sets the sword properties
 */
class IgneousSwordItem : AOTDChargeableSwordItem(
    "igneous_sword",
    ModToolMaterials.IGNEOUS,
    3,
    -2.4f,
    Properties()
) {
    init {
        percentChargePerAttack = 35.0
    }

    /**
     * Called when you left click an entity with the sword
     *
     * @param stack The itemstack that the entity was clicked with
     * @param player The player that clicked the entity
     * @param target The entity that was clicked
     * @return True to cancel the interaction, false otherwise
     */
    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        // If igneous is researched allow the sword to function
        if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            // The fire burn time is heavily upgraded by fire aspect enchantment
            target.remainingFireTicks = max(target.remainingFireTicks, 100 + EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, player) * 100)
            // Attack the entity from silver damage
            target.hurt(getSilverDamage(player), damage)
        } else {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    /**
     * Adds tooltip information to the sword
     *
     * @param stack The itemstack to get information about
     * @param world The world that the item is in
     * @param tooltip The tooltip to return
     * @param flag True if advanced tooltips are on, false otherwise
     */
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.igneous_sword.line1"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.igneous_sword.line2"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.igneous_sword.line3"))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Called to strike down fire on entities in an area
     *
     * @param itemStack    The itemstack that was charged
     * @param world        The world the charge attack happened in
     * @param entityPlayer The player who used the charge attack
     * @return True if the location being aimed at is valid, false otherwise
     */
    override fun performChargeAttack(itemStack: ItemStack, world: Level, entityPlayer: Player): Boolean {
        // Grab the player's eye posiiton and look vector
        val fromVec = entityPlayer.getEyePosition(0f)
        val lookDir = entityPlayer.lookAngle

        // The vector we want to ray trace to
        val toVec = fromVec.add(lookDir.scale(SPECIAL_FIRE_RANGE_BLOCKS.toDouble()))

        val context = RayTraceContext(fromVec, toVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, entityPlayer)
        // Perform the ray trace
        val rayTraceResult: RayTraceResult? = world.clip(context)

        // Ensure we hit something
        if (rayTraceResult?.location != null) {
            // Grab the hit block position
            val hitPos = BlockPos(rayTraceResult.location.x, rayTraceResult.location.y, rayTraceResult.location.z)
            // Grab all surrounding entities
            val surroundingEntities = world.getEntities(
                entityPlayer,
                AxisAlignedBB(hitPos).inflate(HIT_RANGE.toDouble())
            )

            // Set each entity living on fire
            surroundingEntities.filter { it is MobEntity || it is Player }.forEach { it.remainingFireTicks = max(it.remainingFireTicks, 100) }

            // True, the effect was procd
            return true
        }
        return false
    }

    companion object {
        // The max range the fire can be cast
        private const val SPECIAL_FIRE_RANGE_BLOCKS = 50

        // The AOE fire range
        private const val HIT_RANGE = 5
    }
}