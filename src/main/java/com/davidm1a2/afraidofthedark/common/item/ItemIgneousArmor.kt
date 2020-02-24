package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmor
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.common.ISpecialArmor
import net.minecraftforge.common.ISpecialArmor.ArmorProperties
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.sqrt

/**
 * Class representing the 4 different pieces of igneous armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 */
class ItemIgneousArmor(baseName: String, equipmentSlot: EntityEquipmentSlot) :
    AOTDArmor(baseName, ModArmorMaterials.IGNEOUS, 3, equipmentSlot), ISpecialArmor {
    init {
        // Makes the armor invincible
        maxDamage = 0
        // Block 80% of the damage up to 20
        maxDamageBlocked = 20
        percentOfDamageBlocked = 0.8
    }

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
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player
        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add("Magical armor will never break.")
            tooltip.add("Knocks back enemies that hit you.")
        } else {
            tooltip.add("I dont know how to use this.")
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param world     The world the player is in
     * @param player    The player that is wearing the armor
     * @param itemStack The itemstack of the armor item
     */
    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        super.onArmorTick(world, player, itemStack)
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

    /**
     * Armor can't be damaged, just return
     *
     * @param entity THe entity that is wearing the armor
     * @param stack  The itemstack that is being worn
     * @param source The damage source that is hitting the armor
     * @param damage The amount of damage to apply
     * @param slot   The slot that is damaged
     */
    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
    }

    /**
     * Returns the number of additional shields to display when wearing the armor
     *
     * @param player The player wearing the armor
     * @param armor  The armor itemstack
     * @param slot   The slot the item is in
     * @return THe number of shields to display when wearing the armor
     */
    override fun getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int {
        // 0, since we only want to display default armor values
        return 0
    }

    /**
     * Called when unblockable damage is applied, just return the default false
     *
     * @param entity The entity wearing the armor
     * @param armor  The armor itemstack
     * @param source The damage source applied
     * @param damage The damage inflicted
     * @param slot   The armor slot
     * @return False, let the damage be handled
     */
    override fun handleUnblockableDamage(
        entity: EntityLivingBase,
        armor: ItemStack,
        source: DamageSource,
        damage: Double,
        slot: Int
    ): Boolean {
        return false
    }

    /**
     * Returns the armor properties for a given item in the player's armor inventory
     *
     * @param entity The player that is wearing the armor
     * @param armor  The armor item that is being worn
     * @param source The damage source that hit the player
     * @param damage The damage inflicted
     * @param slot   The slot containing the armor block
     * @return The armor's properties for these damage types
     */
    override fun getProperties(
        entity: EntityLivingBase,
        armor: ItemStack,
        source: DamageSource,
        damage: Double,
        slot: Int
    ): ArmorProperties {
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
                    return ArmorProperties(0, getRatio(slot), Int.MAX_VALUE)
                }
                // Blocks no true damage
                else if (TRUE_DAMAGE_SOURCES.contains(source)) {
                    return ArmorProperties(0, getRatio(slot), 0)
                }
            } else {
                // Armor is useless without research
                return ArmorProperties(0, getRatio(slot), 0)
            }
        }

        // Default armor protection if no special set bonus applies
        return getDefaultProperties(slot)
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private fun getRatio(slot: Int): Double {
        // Total protection of each piece
        val totalProtection = 3 + 6 + 8 + 3
        return when (slot) {
            0, 3 -> 3.0 / totalProtection * percentOfDamageBlocked
            1 -> 6.0 / totalProtection * percentOfDamageBlocked
            2 -> 8.0 / totalProtection * percentOfDamageBlocked
            else -> 0.0
        }
    }

    /**
     * Gets the default armor properties when taking damage
     *
     * @param slot The slot that the armor piece is in
     * @return default armor properties for this set
     */
    private fun getDefaultProperties(slot: Int): ArmorProperties {
        return ArmorProperties(0, getRatio(slot), maxDamageBlocked)
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