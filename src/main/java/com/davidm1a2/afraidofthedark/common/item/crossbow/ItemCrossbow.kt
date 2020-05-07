package com.davidm1a2.afraidofthedark.common.item.crossbow

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry
import com.davidm1a2.afraidofthedark.common.utility.BoltOrderHelper
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing the crossbow item
 *
 * @constructor Sets the item name and ensures it can't stack
 */
class ItemCrossbow : AOTDItem("crossbow") {
    init {
        setMaxStackSize(1)

        // The charge level property is used to determine how far along in the charge the bow is
        addPropertyOverride(ResourceLocation(Constants.MOD_ID, "charge_level"))
        { stack: ItemStack, _: World?, entity: EntityLivingBase? ->
            if (entity is EntityPlayer) {
                // If the selected item is the current one update the charge level
                if (entity.inventory.mainInventory[entity.inventory.currentItem] == stack) {
                    return@addPropertyOverride entity.getItemInUseMaxCount().toFloat() / getMaxItemUseDuration(stack).toFloat()
                }
            }
            0f
        }

        // True if the bow is loaded (1f) or false (0f) otherwise
        addPropertyOverride(ResourceLocation(Constants.MOD_ID, "is_loaded"))
        { stack: ItemStack, _: World?, _: EntityLivingBase? ->
            if (isLoaded(stack)) 1f else 0f
        }
    }

    /**
     * Called when you right click with an item
     *
     * @param world  The world the click happens in
     * @param player The player that right clicks
     * @param hand   The hand that the player is using
     * @return The result of the click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        // Grab the itemstack that is held
        val itemStack = player.getHeldItem(hand)

        // Server side processing only
        if (!world.isRemote) {
            // If the player is sneaking then select the next bolt type for this crossbow
            if (player.isSneaking) {
                selectNextBoltType(itemStack, player)
            } else {
                // If the player is not sneaking and the bow is not loaded begin loading
                if (!isLoaded(itemStack)) {
                    // If we are in creative, no ammo is required or if we have ammo begin charging he bow
                    if (player.capabilities.isCreativeMode || player.inventory.hasItemStack(
                            ItemStack(
                                getCurrentBoltType(
                                    itemStack
                                ).boltItem
                            )
                        )
                    ) {
                        // Play the load sound
                        world.playSound(
                            null,
                            player.position,
                            ModSounds.CROSSBOW_LOAD,
                            SoundCategory.PLAYERS,
                            0.9f,
                            world.rand.nextFloat() * 0.8f + 1.2f
                        )
                        // Set the player's hand to active
                        player.activeHand = hand
                    } else {
                        player.sendMessage(
                            TextComponentTranslation(
                                LocalizationConstants.Item.CROSSBOW_NO_BOLT,
                                TextComponentTranslation(getCurrentBoltType(itemStack).getUnlocalizedName())
                            )
                        )
                    }
                }
            }
        }

        // We have to "fail" the action so that the bow doesn't play the item use animation and bounce
        return ActionResult.newResult(EnumActionResult.FAIL, itemStack)
    }

    /**
     * Called when the player is using an item
     *
     * @param stack  The stack being used
     * @param entity The entity that is using the item, should be a player
     * @param count  The count of the current using tick
     */
    override fun onUsingTick(stack: ItemStack, entity: EntityLivingBase, count: Int) {
        // Only the player can charge the bow
        if (entity is EntityPlayer) {
            // Load the bow at 2 ticks left instead of 1 so the unloaded texture doesn't flicker
            if (count == 2) {
                if (entity.capabilities.isCreativeMode || entity.inventory.clearMatchingItems(
                        getCurrentBoltType(stack).boltItem,
                        -1,
                        1,
                        null
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
    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        // Only fire server side and from players
        if (!entityLiving.world.isRemote && entityLiving is EntityPlayer && isLoaded(stack)) {
            // Reset the charge state and fire
            setLoaded(stack, false)
            fireBolt(entityLiving, entityLiving.world, stack)
        }
        return super.onEntitySwing(entityLiving, stack)
    }

    /**
     * Fires a bolt from the crossbow into the world
     *
     * @param entityPlayer The player shooting the bow
     * @param world        The world the bow is being shot in
     * @param itemStack    The bow item stack
     */
    private fun fireBolt(entityPlayer: EntityPlayer, world: World, itemStack: ItemStack) {
        // Play a fire sound effect
        world.playSound(
            null,
            entityPlayer.position,
            ModSounds.CROSSBOW_FIRE,
            SoundCategory.PLAYERS,
            0.5f,
            world.rand.nextFloat() * 0.4f + 0.8f
        )

        // Instantiate bolt!
        val bolt = getCurrentBoltType(itemStack).boltEntityFactory(world, entityPlayer)
        // Aim and fire the bolt
        bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f)
        // Push the bolt slightly forward so it does not collide with the player
        bolt.posX = bolt.posX + bolt.motionX
        bolt.posY = bolt.posY + bolt.motionY
        bolt.posZ = bolt.posZ + bolt.motionZ
        world.spawnEntity(bolt)
    }

    /**
     * Selects the next bolt type to be fired in a circular order
     *
     * @param itemStack The itemstack to update bolt type on
     */
    private fun selectNextBoltType(itemStack: ItemStack, entityPlayer: EntityPlayer) {
        // First test if the itemstack is not fully charged
        if (!isLoaded(itemStack)) {
            // Grab the current bolt type index
            var currentBoltTypeIndex = getCurrentBoltTypeIndex(itemStack)
            // Compute the next bolt index
            currentBoltTypeIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltTypeIndex)
            // Set the next bolt index
            NBTHelper.setInteger(itemStack, NBT_BOLT_TYPE, currentBoltTypeIndex)
            // Tell the user that they have a new bolt loaded
            if (!entityPlayer.world.isRemote) {
                entityPlayer.sendMessage(
                    TextComponentTranslation(
                        LocalizationConstants.Item.CROSSBOW_BOLT_CHANGE,
                        TextComponentTranslation(getCurrentBoltType(itemStack).getUnlocalizedName())
                    )
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
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip.add(I18n.format(LocalizationConstants.Item.CROSSBOW_TOOLTIP_CHANGE_BOLT))
        tooltip.add(I18n.format(LocalizationConstants.Item.CROSSBOW_TOOLTIP_FIRE_BOLT_TYPE, I18n.format(getCurrentBoltType(stack).getUnlocalizedName())))
        if (isLoaded(stack)) {
            tooltip.add(I18n.format(LocalizationConstants.Item.CROSSBOW_TOOLTIP_LOADED))
        } else {
            tooltip.add(I18n.format(LocalizationConstants.Item.CROSSBOW_TOOLTIP_UNLOADED))
        }
    }

    /**
     * Returns the amount of time the item can be in use
     *
     * @param stack The itemstack in question
     * @return An integer representing the reload time of the bow
     */
    override fun getMaxItemUseDuration(stack: ItemStack): Int {
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