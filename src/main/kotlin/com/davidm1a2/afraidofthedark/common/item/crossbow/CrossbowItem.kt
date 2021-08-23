package com.davidm1a2.afraidofthedark.common.item.crossbow

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry
import com.davidm1a2.afraidofthedark.common.utility.BoltOrderHelper
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the crossbow item
 *
 * @constructor Sets the item name and ensures it can't stack
 */
class CrossbowItem : AOTDItem("crossbow", Properties().stacksTo(1)), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>> {
        return listOf(
            // The charge level property is used to determine how far along in the charge the bow is
            ResourceLocation(Constants.MOD_ID, "charge_level") to IItemPropertyGetter { stack: ItemStack, _: World?, entity: LivingEntity? ->
                if (entity is PlayerEntity) {
                    // If the selected item is the current one update the charge level
                    if (entity.inventory.items[entity.inventory.selected] == stack) {
                        return@IItemPropertyGetter entity.useItem.useDuration.toFloat() / getUseDuration(stack).toFloat()
                    }
                }
                0f
            },
            // True if the bow is loaded (1f) or false (0f) otherwise
            ResourceLocation(Constants.MOD_ID, "is_loaded") to IItemPropertyGetter { stack: ItemStack, _: World?, _: LivingEntity? ->
                if (isLoaded(stack)) 1f else 0f
            }
        )
    }

    /**
     * Called when you right click with an item
     *
     * @param world  The world the click happens in
     * @param player The player that right clicks
     * @param hand   The hand that the player is using
     * @return The result of the click
     */
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Grab the itemstack that is held
        val itemStack = player.getItemInHand(hand)

        // Server side processing only
        if (!world.isClientSide) {
            // If the player is sneaking then select the next bolt type for this crossbow
            if (player.isCrouching) {
                selectNextBoltType(itemStack, player)
            } else {
                // If the player is not sneaking and the bow is not loaded begin loading
                if (!isLoaded(itemStack)) {
                    // If we are in creative, no ammo is required or if we have ammo begin charging he bow
                    if (player.isCreative || player.inventory.contains(
                            ItemStack(
                                getCurrentBoltType(
                                    itemStack
                                ).item
                            )
                        )
                    ) {
                        // Play the load sound
                        world.playSound(
                            null,
                            player.blockPosition(),
                            ModSounds.CROSSBOW_LOAD,
                            SoundCategory.PLAYERS,
                            0.9f,
                            world.random.nextFloat() * 0.8f + 1.2f
                        )
                        // Set the player's hand to active
                        player.startUsingItem(hand)
                    } else {
                        player.sendMessage(
                            TranslationTextComponent(
                                "message.afraidofthedark.crossbow.no_bolt",
                                TranslationTextComponent(getCurrentBoltType(itemStack).getUnlocalizedName())
                            ),
                            player.uuid
                        )
                    }
                }
            }
        }

        // We have to "fail" the action so that the bow doesn't play the item use animation and bounce
        return ActionResult.fail(itemStack)
    }

    /**
     * Called when the player is using an item
     *
     * @param stack  The stack being used
     * @param entity The entity that is using the item, should be a player
     * @param count  The count of the current using tick
     */
    override fun onUsingTick(stack: ItemStack, entity: LivingEntity, count: Int) {
        // Only the player can charge the bow
        if (entity is PlayerEntity) {
            // Load the bow at 2 ticks left instead of 1 so the unloaded texture doesn't flicker
            if (count == 2) {
                if (entity.isCreative || ItemStackHelper.clearOrCountMatchingItems(
                        entity.inventory,
                        { it.item == getCurrentBoltType(stack).item },
                        1,
                        false
                    ) == 1
                ) {
                    setLoaded(stack, true)
                }
            }
        }
    }

    /**
     * Called when the player left clicks with an item
     *
     * @param entityLiving The entity that is swinging
     * @param stack        The item being swung
     * @return True to cancel the swing, false otherwise
     */
    override fun onEntitySwing(stack: ItemStack, entityLiving: LivingEntity): Boolean {
        // Only fire server side and from players
        if (!entityLiving.level.isClientSide && entityLiving is PlayerEntity && isLoaded(stack)) {
            // Reset the charge state and fire
            setLoaded(stack, false)
            fireBolt(entityLiving, entityLiving.level, stack)
        }
        return super.onEntitySwing(stack, entityLiving)
    }

    /**
     * Fires a bolt from the crossbow into the world
     *
     * @param entityPlayer The player shooting the bow
     * @param world        The world the bow is being shot in
     * @param itemStack    The bow item stack
     */
    private fun fireBolt(entityPlayer: PlayerEntity, world: World, itemStack: ItemStack) {
        // Play a fire sound effect
        world.playSound(
            null,
            entityPlayer.blockPosition(),
            ModSounds.CROSSBOW_FIRE,
            SoundCategory.PLAYERS,
            0.5f,
            world.random.nextFloat() * 0.4f + 0.8f
        )

        // Instantiate bolt!
        val bolt = getCurrentBoltType(itemStack).boltEntityFactory(world)
        bolt.setShotFrom(entityPlayer)
        world.addFreshEntity(bolt)
    }

    /**
     * Selects the next bolt type to be fired in a circular order
     *
     * @param itemStack The itemstack to update bolt type on
     */
    private fun selectNextBoltType(itemStack: ItemStack, entityPlayer: PlayerEntity) {
        // First test if the itemstack is not fully charged
        if (!isLoaded(itemStack)) {
            // Grab the current bolt type index
            var currentBoltTypeIndex = getCurrentBoltTypeIndex(itemStack)
            // Compute the next bolt index
            currentBoltTypeIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltTypeIndex)
            // Set the next bolt index
            NBTHelper.setInteger(itemStack, NBT_BOLT_TYPE, currentBoltTypeIndex)
            // Tell the user that they have a new bolt loaded
            if (!entityPlayer.level.isClientSide) {
                entityPlayer.sendMessage(
                    TranslationTextComponent(
                        "message.afraidofthedark.crossbow.bolt_change",
                        TranslationTextComponent(getCurrentBoltType(itemStack).getUnlocalizedName())
                    ),
                    entityPlayer.uuid
                )
            }
        }
    }

    /**
     * Returns the current bolt type selected for a given itemstack
     *
     * @param itemStack The bolt type selected
     * @return The bolt type tripe represented by this bow
     */
    private fun getCurrentBoltType(itemStack: ItemStack): BoltEntry {
        return BoltOrderHelper.getBoltAt(getCurrentBoltTypeIndex(itemStack))
    }

    /**
     * Returns the current bolt type index from the NBT data of the itemstack
     *
     * @param itemStack The itemstack to get the current bolt type index from
     * @return The index of the bolt type into AOTDBoltType.values()
     */
    private fun getCurrentBoltTypeIndex(itemStack: ItemStack): Int {
        if (!NBTHelper.hasTag(itemStack, NBT_BOLT_TYPE)) {
            NBTHelper.setInteger(itemStack, NBT_BOLT_TYPE, 0)
        }
        return NBTHelper.getInteger(itemStack, NBT_BOLT_TYPE)!!
    }

    /**
     * Sets the bow's loaded state
     *
     * @param itemStack THe stack to set
     * @param isLoaded  True if the bow is loaded, false otherwise
     */
    private fun setLoaded(itemStack: ItemStack, isLoaded: Boolean) {
        NBTHelper.setBoolean(itemStack, NBT_LOADED, isLoaded)
    }

    /**
     * True if the bow is loaded, false otherwise
     *
     * @param itemStack The stack to test
     * @return True if the bow is loaded, false otherwise
     */
    private fun isLoaded(itemStack: ItemStack): Boolean {
        if (!NBTHelper.hasTag(itemStack, NBT_LOADED)) {
            setLoaded(itemStack, false)
        }
        return NBTHelper.getBoolean(itemStack, NBT_LOADED)!!
    }

    /**
     * Adds a tooltop to the crossbow
     *
     * @param stack   The item stack to tooltip
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if show advanced info is on, false otherwise
     */
    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        if (ModRegistries.BOLTS.isEmpty) {
            // Skip adding information before the bolts registry is initialized
            return
        }
        tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.crossbow.change_bolt"))
        tooltip.add(
            TranslationTextComponent(
                "tooltip.afraidofthedark.crossbow.fire_bolt_type",
                TranslationTextComponent(getCurrentBoltType(stack).getUnlocalizedName())
            )
        )
        if (isLoaded(stack)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.crossbow.loaded"))
        } else {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.crossbow.unloaded"))
        }
    }

    /**
     * Returns the amount of time the item can be in use
     *
     * @param stack The itemstack in question
     * @return An integer representing the reload time of the bow
     */
    override fun getUseDuration(stack: ItemStack): Int {
        return RELOAD_TIME
    }

    companion object {
        // Store the reload time of the crossbow in ticks
        private const val RELOAD_TIME = 40

        // Strings used as keys by NBT
        private const val NBT_BOLT_TYPE = "bolt_type"
        private const val NBT_LOADED = "is_loaded"
    }
}