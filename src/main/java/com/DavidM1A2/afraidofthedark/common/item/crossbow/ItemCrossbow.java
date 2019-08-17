package com.DavidM1A2.afraidofthedark.common.item.crossbow;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.utility.BoltOrderHelper;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class representing the crossbow item
 */
public class ItemCrossbow extends AOTDItem
{
    // Store the reload time of the crossbow in ticks
    private static final int RELOAD_TIME = 40;

    // Strings used as keys by NBT
    private static final String NBT_BOLT_TYPE = "bolt_type";
    private static final String NBT_LOADED = "is_loaded";

    /**
     * Constructor just sets the item name and ensures it can't stack
     */
    public ItemCrossbow()
    {
        super("crossbow");
        this.setMaxStackSize(1);
        // The charge level property is used to determine how far along in the charge the bow is
        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "charge_level"), (stack, worldIn, entityIn) ->
        {
            if (entityIn instanceof EntityPlayer)
            {
                // Grab the player
                EntityPlayer entityPlayer = ((EntityPlayer) entityIn);
                // If the selected item is the current one update the charge level
                if (entityPlayer.inventory.mainInventory.get(entityPlayer.inventory.currentItem) == stack)
                {
                    return (float) entityIn.getItemInUseMaxCount() / (float) this.getMaxItemUseDuration(stack);
                }
            }
            return 0f;
        });
        // True if the bow is loaded (1f) or false (0f) otherwise
        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "is_loaded"), ((stack, worldIn, entityIn) -> this.isLoaded(stack) ? 1 : 0));
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
                // If the player is not sneaking and the bow is not loaded begin loading
                if (!this.isLoaded(itemStack))
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

        // We have to "fail" the action so that the bow doesn't play the item use animation and bounce
        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
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
        // Only the player can charge the bow
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            // Load the bow at 2 ticks left instead of 1 so the unloaded texture doesn't flicker
            if (count == 2)
            {
                if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.clearMatchingItems(this.getCurrentBoltType(stack).getBoltItem(), -1, 1, null) == 1)
                {
                    this.setLoaded(stack, true);
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
        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer && this.isLoaded(stack))
        {
            // Reset the charge state and fire
            this.setLoaded(stack, false);
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
        // Aim and fire the bolt
        bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f);
        // Push the bolt slightly forward so it does not collide with the player
        bolt.posX = bolt.posX + bolt.motionX;
        bolt.posY = bolt.posY + bolt.motionY;
        bolt.posZ = bolt.posZ + bolt.motionZ;
        world.spawnEntity(bolt);
    }

    /**
     * Selects the next bolt type to be fired in a circular order
     *
     * @param itemStack The itemstack to update bolt type on
     */
    private void selectNextBoltType(ItemStack itemStack, EntityPlayer entityPlayer)
    {
        // First test if the itemstack is not fully charged
        if (!this.isLoaded(itemStack))
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
     * Sets the bow's loaded state
     *
     * @param itemStack THe stack to set
     * @param isLoaded  True if the bow is loaded, false otherwise
     */
    private void setLoaded(ItemStack itemStack, boolean isLoaded)
    {
        NBTHelper.setBoolean(itemStack, NBT_LOADED, isLoaded);
    }

    /**
     * True if the bow is loaded, false otherwise
     *
     * @param itemStack The stack to test
     * @return True if the bow is loaded, false otherwise
     */
    private boolean isLoaded(ItemStack itemStack)
    {
        if (!NBTHelper.hasTag(itemStack, NBT_LOADED))
        {
            this.setLoaded(itemStack, false);
        }
        return NBTHelper.getBoolean(itemStack, NBT_LOADED);
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("Crouch & Right click to change crossbow bolt type.");
        tooltip.add("Bow will fire: " + I18n.format(this.getCurrentBoltType(stack).getUnLocalizedName()) + " bolts.");
        tooltip.add(this.isLoaded(stack) ? "Bow is loaded" : "Bow is unloaded");
    }

    /**
     * Returns the amount of time the item can be in use
     *
     * @param stack The itemstack in question
     * @return An integer representing the reload time of the bow
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return RELOAD_TIME;
    }
}
