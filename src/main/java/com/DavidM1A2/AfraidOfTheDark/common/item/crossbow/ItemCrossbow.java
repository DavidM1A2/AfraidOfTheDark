package com.DavidM1A2.afraidofthedark.common.item.crossbow;

import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.utility.BoltOrderHelper;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class representing the crossbow item
 */
public class ItemCrossbow extends AOTDItem
{
    // Store the reload time of the crossbow in ticks
    private static final int RELOAD_TIME = 50;

    // Strings used as keys by NBT
    private static final String NBT_BOLT_TYPE = "bolt_type";

    /**
     * Constructor just sets the item name and ensures it can't stack
     */
    public ItemCrossbow()
    {
        super("crossbow");
        this.setMaxStackSize(1);
    }

    /**
     * Called every tick, we test if the bow is not selected and ensure it is either in damage state 0 or 3 based on if it is charged or not
     *
     * @param stack      The itemstack to update
     * @param worldIn    The world the item is in
     * @param entityIn   The entity holding the item
     * @param itemSlot   The slot the item is in
     * @param isSelected If the item is selected
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        // If the itemstack is in state 1 or 2 then reset it to 0
        if (!worldIn.isRemote && !isSelected)
        {
            if (stack.getItemDamage() == 1 || stack.getItemDamage() == 2)
            {
                stack.setItemDamage(0);
            }
        }
    }

    /**
     * Called when you right click with an item
     *
     * @param worldIn  The world the click happens in
     * @param playerIn The player that right clicks
     * @param handIn   The hand that the player is using
     * @return The result of the click
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Grab the itemstack that is held
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        // Server side processing only
        if (!worldIn.isRemote)
        {
            // If the player is sneaking then select the next bolt type for this crossbow
            if (playerIn.isSneaking())
            {
                this.selectNextBoltType(itemStack, playerIn);
            }
            else
            {
                // If the player is not sneaking and the bow has no charge begin charging
                if (itemStack.getItemDamage() == 0)
                {
                    // If we are in creative, no ammo is required or if we have ammo begin charging he bow
                    if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItemStack(new ItemStack(this.getCurrentBoltType(itemStack).getBoltItem())))
                    {
                        // Play the load sound
                        worldIn.playSound(null, playerIn.getPosition(), ModSounds.CROSSBOW_LOAD, SoundCategory.PLAYERS, 0.9F, worldIn.rand.nextFloat() * 0.8F + 1.2F);
                        // Set the player's hand to active
                        playerIn.setActiveHand(handIn);
                    }
                    // Else we print out that the player needs bolts to fire
                    else
                    {
                        playerIn.sendMessage(new TextComponentTranslation("aotd.crossbow.no_bolt", new TextComponentTranslation(this.getCurrentBoltType(itemStack).getUnLocalizedName())));
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    /**
     * Called when the player is using an item
     *
     * @param stack  The stack being used
     * @param entity The entity that is using the item, should be a player
     * @param count  The count of the current using tick
     */
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count)
    {
        // Server side processing only
        if (!entity.world.isRemote)
        {
            // Only the player can charge the bow
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                count = this.getMaxItemUseDuration(stack) - count + 1;
                // On using we play a sound
                if (count == 1)
                {
                    stack.setItemDamage(1);
                }
                else if (count == RELOAD_TIME / 4)
                {
                    stack.setItemDamage(1);
                }
                else if (count == RELOAD_TIME / 4 * 2)
                {
                    stack.setItemDamage(2);
                }
                else if (count == RELOAD_TIME / 4 * 3)
                {
                    stack.setItemDamage(2);
                }
                else if (count == RELOAD_TIME)
                {
                    if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.clearMatchingItems(this.getCurrentBoltType(stack).getBoltItem(), -1, 1, null) == 1)
                    // Bow is loaded at damage = 3
                    {
                        stack.setItemDamage(3);
                    }
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
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        // Only fire server side and from players
        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer && this.bowIsCharged(stack))
        {
            // Reset the charge state and fire
            stack.setItemDamage(0);
            this.fireBolt((EntityPlayer) entityLiving, entityLiving.world, stack);
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    /**
     * Fires a bolt from the crossbow into the world
     *
     * @param entityPlayer The player shooting the bow
     * @param world        The world the bow is being shot in
     * @param itemStack    The bow item stack
     */
    private void fireBolt(EntityPlayer entityPlayer, World world, ItemStack itemStack)
    {
        // Play a fire sound effect
        world.playSound(null, entityPlayer.getPosition(), ModSounds.CROSSBOW_FIRE, SoundCategory.PLAYERS, 0.5f, world.rand.nextFloat() * 0.4f + 0.8f);
        // Instantiate bolt!
        EntityBolt bolt = this.getCurrentBoltType(itemStack).getBoltEntityFactory().apply(world, entityPlayer);
        // Push the bolt slightly forward so it does not collide with the player
        bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f);
        bolt.posX = bolt.posX + bolt.motionX;
        bolt.posY = bolt.posY + bolt.motionY;
        bolt.posZ = bolt.posZ + bolt.motionZ;
        world.spawnEntity(bolt);
    }

    /**
     * The metadata of the item is just it's damage value in our case
     *
     * @param damage The damage of the crossbow which represents its charge state
     * @return The metadata value of the item
     */
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    /**
     * Called when the player stops using the crossbow meaning they are no longer pulling it back
     *
     * @param stack        The stack that is being processed
     * @param worldIn      The world the item is in
     * @param entityLiving The entity using the item
     * @param timeLeft     The time left before the bow can no longer be used
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        // If the bow is not charged, then
        if (stack.getItemDamage() != 3)
        {
            stack.setItemDamage(0);
        }
    }

    /**
     * Selects the next bolt type to be fired in a circular order
     *
     * @param itemStack The itemstack to update bolt type on
     */
    private void selectNextBoltType(ItemStack itemStack, EntityPlayer entityPlayer)
    {
        // First test if the itemstack is not fully charged
        if (!this.bowIsCharged(itemStack))
        {
            // Grab the current bolt type index
            int currentBoltTypeIndex = this.getCurrentBoltTypeIndex(itemStack);
            // Compute the next bolt index
            currentBoltTypeIndex = BoltOrderHelper.getNextBoltIndex(entityPlayer, currentBoltTypeIndex);
            // Set the next bolt index
            NBTHelper.setInteger(itemStack, NBT_BOLT_TYPE, currentBoltTypeIndex);

            // Tell the user that they have a new bolt loaded
            if (!entityPlayer.world.isRemote)
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.crossbow.bolt_change", new TextComponentTranslation(this.getCurrentBoltType(itemStack).getUnLocalizedName())));
            }
        }
    }

    /**
     * True if the bow is charged and ready to fire
     *
     * @param itemStack The itemstack to test
     * @return True if the bow is ready to fire, false otherwise
     */
    private boolean bowIsCharged(ItemStack itemStack)
    {
        return itemStack.getItemDamage() == 3;
    }

    /**
     * Returns the current bolt type selected for a given itemstack
     *
     * @param itemStack The bolt type selected
     * @return The bolt type tripe represented by this bow
     */
    private BoltEntry getCurrentBoltType(ItemStack itemStack)
    {
        return BoltOrderHelper.getBoltAt(this.getCurrentBoltTypeIndex(itemStack));
    }

    /**
     * Returns the current bolt type index from the NBT data of the itemstack
     *
     * @param itemStack The itemstack to get the current bolt type index from
     * @return The index of the bolt type into AOTDBoltType.values()
     */
    private int getCurrentBoltTypeIndex(ItemStack itemStack)
    {
        if (!NBTHelper.hasTag(itemStack, NBT_BOLT_TYPE))
        {
            NBTHelper.setInteger(itemStack, NBT_BOLT_TYPE, 0);
        }
        return NBTHelper.getInteger(itemStack, NBT_BOLT_TYPE);
    }

    /**
     * Adds a tooltop to the crossbow
     *
     * @param stack   The item stack to tooltip
     * @param worldIn The world the item is in
     * @param tooltip The tooltip to add to
     * @param flagIn  True if show advanced info is on, false otherwise
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("Shift & Right click to change crossbow bolt type.");
        tooltip.add("Bow will fire: " + I18n.format(this.getCurrentBoltType(stack).getUnLocalizedName()) + " bolts.");
    }

    /**
     * Returns the amount of time the item can be in use
     *
     * @param stack The itemstack in question
     *              \	 * @return An integer representing the reload time of the bow
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return RELOAD_TIME;
    }
}
