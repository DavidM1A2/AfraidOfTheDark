package com.davidm1a2.afraidofthedark.common.item;

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithSharedCooldown;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class representing a star metal staff that can do a (fizz e from LoL)
 */
public class ItemStarMetalStaff extends AOTDItemWithSharedCooldown
{
    // The amount of knockback the staff has once dropping out of it
    private static final double KNOCKBACK_STRENGTH = 6;
    // The maximum number of ticks a player can be on the troll poll
    private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;

    /**
     * Constructor sets the item's name
     */
    public ItemStarMetalStaff()
    {
        super("star_metal_staff");
    }

    /**
     * Called to fire the right click effect
     *
     * @param worldIn  The world that the item is being held in
     * @param playerIn The player that right clicked with the staff
     * @param handIn   The hand that the staff is being held in
     * @return Success if the staff went off, pass if not
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Server side processing only
        if (!worldIn.isRemote)
        {
            // Verify the player has the star metal research
            if (playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
            {
                // Get the item that the player was holding
                ItemStack heldItem = playerIn.getHeldItem(handIn);
                // If the item is not on cooldown fire it off
                if (!this.isOnCooldown(heldItem))
                {
                    // Make the player invincible
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        playerIn.capabilities.disableDamage = true;
                    }
                    // Set the player's velocity to 0 with a 0.5 vertical velocity
                    ((EntityPlayerMP) playerIn).connection.sendPacket(new SPacketEntityVelocity(playerIn.getEntityId(), 0, 0.5, 0));
                    // Set the item on cooldown
                    this.setOnCooldown(heldItem, playerIn);
                    // Set the player's active hand to the one that was right clicked with
                    playerIn.setActiveHand(handIn);
                    // We're good to go, return success
                    return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
                }
                else
                {
                    // If the staff is on cooldown say that
                    playerIn.sendMessage(new TextComponentTranslation("aotd.star_metal_staff.on_cooldown", this.cooldownRemainingInSeconds(heldItem)));
                }
            }
            else
            {
                // If the player has the wrong research print an error
                playerIn.sendMessage(new TextComponentTranslation("aotd.dont_understand"));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * Called every tick the item is in use
     *
     * @param stack The itemstack that is in use
     * @param entityLivingBase The entity using the item
     * @param count The ticks left before the use is done
     */
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entityLivingBase, int count)
    {
        // Server side processing only
        if (!entityLivingBase.world.isRemote)
        {
            // Ensure the entity using the item is a player
            if (entityLivingBase instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) entityLivingBase;
                // Figure out how many ticks the item has been in use
                count = this.getMaxItemUseDuration(stack) - count;
                // On the first tick set the fall distance to 0 so the player doesn't take fall damage after using this item
                if (count == 1)
                {
                    entityPlayer.fallDistance = 0.0f;
                }
                // On the 5th tick and after freeze the entity's position
                if (count >= 5)
                {
                    ((EntityPlayerMP) entityPlayer).connection.sendPacket(new SPacketEntityVelocity(entityPlayer.getEntityId(), 0, 0, 0));
                }
            }
        }
    }

    /**
     * Called when the item has finished being used, performs the knockback
     *
     * @param stack The itemstack that was used
     * @param worldIn The world the item was being used in
     * @param entityLiving The entity holding the item
     * @return The itemstack to return after the current stack was used
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        // Server side processing only
        if (!entityLiving.world.isRemote)
        {
            // Only test players
            if (entityLiving instanceof EntityPlayer)
            {
                // If the player is not creative let them take damage again
                EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
                if (!entityPlayer.capabilities.isCreativeMode)
                {
                    entityPlayer.capabilities.disableDamage = false;
                }
                // Perform the knockback
                performKnockback(worldIn, entityPlayer);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    /**
     * Called when the player stops using the staff without actually letting it finish using
     *
     * @param stack The stack that was being used
     * @param worldIn The world that the stack was being used in
     * @param entityLiving The entity that was using the staff
     * @param timeLeft The ticks left before the use would've been complete
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        // Server side processing only
        if (!worldIn.isRemote)
        {
            // Ensure the entity is a player
            if (entityLiving instanceof EntityPlayer)
            {
                // If the player is not in creative let them take damage again
                EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
                if (!entityPlayer.capabilities.isCreativeMode)
                {
                    entityPlayer.capabilities.disableDamage = false;
                }
                // If less than 5 ticks were left on the use still perform the knockback
                if (timeLeft < 5)
                {
                    performKnockback(worldIn, entityPlayer);
                }
            }
        }
    }

    /**
     * Performs the knockback of the cloak after hitting the ground
     *
     * @param worldIn The world the knockback is happening in
     * @param entityPlayer The player that caused the knockback
     */
    private void performKnockback(World worldIn, EntityPlayer entityPlayer)
    {
        // Grab all entities around the player
        List<Entity> entityList = worldIn.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().grow(10));
        // Go over all nearby entities
        for (Entity entity : entityList)
        {
            // If the entity is a player or anything living push it back
            if (entity instanceof EntityPlayer || entity instanceof EntityLiving)
            {
                // Compute the x,z force vector to push the entity in
                double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
                double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

                // Compute the magnitude of the force
                double hypotenuse = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

                // Push the entity away from the player
                entity.addVelocity(-motionX * KNOCKBACK_STRENGTH * 0.6D / hypotenuse, 0.1D, -motionZ * KNOCKBACK_STRENGTH * 0.6D / hypotenuse);
            }
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    @Override
    public int getItemCooldownInMilliseconds(ItemStack itemStack)
    {
        return 5000;
    }

    /**
     * The maximum number of ticks the item may be "in use"
     *
     * @param stack The itemstack to test
     * @return The number of ticks the item may be in use for
     */
    @Override
    public int getMaxItemUseDuration(final ItemStack stack)
    {
        return MAX_TROLL_POLE_TIME_IN_TICKS;
    }
}
