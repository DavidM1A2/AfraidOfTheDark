package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.constants.ModToolMaterials;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDChargeableSword;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Star metal sword is a khopesh
 */
public class ItemStarMetalKhopesh extends AOTDChargeableSword
{
    // The AOE knockback range
    private static final int HIT_RANGE = 5;
    // NBT containing spin info
    private static final String NBT_SPIN_TICKS_LEFT = "spin_ticks_left";
    // Number of ticks to spin
    private static final int TICKS_TO_SPIN = 8;
    // Number of degrees to spin per tick
    private static final float DEGREES_PER_TICK = 360.0f / TICKS_TO_SPIN;

    /**
     * Constructor sets the sword properties
     */
    public ItemStarMetalKhopesh()
    {
        super(ModToolMaterials.STAR_METAL, "star_metal_khopesh");
        this.percentChargePerAttack = 10;
    }

    /**
     * Called when you left click an entity with the sword
     *
     * @param stack The itemstack that the entity was clicked with
     * @param player The player that clicked the entity
     * @param entity The entity that was clicked
     * @return True to cancel the interaction, false otherwise
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        // If star metal is researched allow the sword to function
        if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
        {
            // Ensure the clicked entity was non-null
            if (entity != null)
            {
                // Attack the entity from silver damage
                entity.attackEntityFrom(ModDamageSources.getSilverDamage(player), this.getAttackDamage());
            }
        }
        // Can't use the sword if you don't have the right research
        else
        {
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    /**
     * Adds tooltip information to the sword
     *
     * @param stack The itemstack to get information about
     * @param worldIn The world that the item is in
     * @param tooltip The tooltip to return
     * @param flagIn True if advanced tooltips are on, false otherwise
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
        {
            tooltip.add("Magical items will never break.");
            tooltip.add("Right click to use an AOE knockback and");
            tooltip.add("damage attack when charged to 100%");
        }
        else
        {
            tooltip.add("I'm not sure how to use this.");
        }
    }

    /**
     * Called to knock back entities around the player
     *
     * @param itemStack    The itemstack that was charged
     * @param world        The world the charge attack happened in
     * @param entityPlayer The player who used the charge attack
     * @return True if the charge attack went off, false otherwise
     */
    @Override
    public boolean performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().grow(HIT_RANGE));
        // Iterate over all entities within 5 blocks of the player
        for (Entity entity : nearbyEntities)
        {
            // Only knock back living entities
            if (entity instanceof EntityPlayer || entity instanceof EntityLiving)
            {
                // Compute the vector from player to entity
                double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
                double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

                // Compute the magnitude of the distance vector
                double hypotenuse = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

                // Compute the strength we knock back with
                double knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, itemStack) + 2;
                // Compute the damage we hit with based on sharpness
                int sharpnessDamage = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack);

                // Attack the entity with player damage and move them back
                entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), this.getAttackDamage() + 4.0F + sharpnessDamage * 1.5F);
                entity.addVelocity(-motionX * knockbackStrength * 0.6D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6D / hypotenuse);
            }
        }

        // Spin the player
        this.resetSpin(itemStack);

        return true;
    }

    /**
     * Used to spin the player when the khopesh is right clicked
     *
     * @param stack The itemstack of the sword that is spinning the player
     * @param worldIn The world that the player is in
     * @param entityIn The entity that activated the sword to be spun
     * @param itemSlot The slot the khopesh is in
     * @param isSelected True if the sword is selected, false otherwise
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        // Only spin the player if the stack is spinning the player
        if (this.shouldSpin(stack))
        {
            // Reduce the ticks remaining by one
            this.decrementSpinTicks(stack);
            // Spin the entity/player
            entityIn.rotationYaw = entityIn.rotationYaw + DEGREES_PER_TICK;
        }
    }

    /**
     * Resets the number of ticks to spin left to TICKS_TO_SPIN
     *
     * @param itemStack The itemstack to set NBT data for
     */
    private void resetSpin(ItemStack itemStack)
    {
        NBTHelper.setInteger(itemStack, NBT_SPIN_TICKS_LEFT, TICKS_TO_SPIN);
    }

    /**
     * True if the khopesh should spin the player currently, false otherwise
     *
     * @param itemStack The itemstack to test spin for
     * @return True if the khopesh should spin the player, false otherwise
     */
    private boolean shouldSpin(ItemStack itemStack)
    {
        return NBTHelper.hasTag(itemStack, NBT_SPIN_TICKS_LEFT) && NBTHelper.getInteger(itemStack, NBT_SPIN_TICKS_LEFT) > 0;
    }

    /**
     * Reduces the number of spin ticks by 1
     *
     * @param itemStack The itemstack to decrement the spin ticks for
     */
    private void decrementSpinTicks(ItemStack itemStack)
    {
        if (NBTHelper.hasTag(itemStack, NBT_SPIN_TICKS_LEFT))
        {
            NBTHelper.setInteger(itemStack, NBT_SPIN_TICKS_LEFT, NBTHelper.getInteger(itemStack, NBT_SPIN_TICKS_LEFT) - 1);
        }
    }
}
