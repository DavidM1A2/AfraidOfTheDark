package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.constants.ModToolMaterials;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDChargeableSword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Igneous sword lets you set fire to an area with its special ability
 */
public class ItemIgneousSword extends AOTDChargeableSword
{
    // The max range the fire can be cast
    private static final int SPECIAL_FIRE_RANGE_BLOCKS = 50;
    // The AOE fire range
    private static final int HIT_RANGE = 5;

    /**
     * Constructor sets the sword properties
     */
    public ItemIgneousSword()
    {
        super(ModToolMaterials.IGNEOUS, "igneous_sword");
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
        // If igneous is researched allow the sword to function
        if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.IGNEOUS))
        {
            // Ensure the clicked entity was non-null
            if (entity != null)
            {
                // The fire burn time is heavily upgraded by fire aspect enchantment
                entity.setFire(5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) * 10);
                // Attack the entity from silver damage
                entity.attackEntityFrom(ModDamageSources.getSilverDamage(player), this.getAttackDamage());
            }
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
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.IGNEOUS))
        {
            tooltip.add("Magical items will never break.");
            tooltip.add("Right click to use an AOE fire strike");
            tooltip.add("centered on the block you are looking");
            tooltip.add("at when charged to 100%");
        }
        else
        {
            tooltip.add("I'm not sure how to use this.");
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
    @Override
    public boolean performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        // Grab the player's eye posiiton and look vector
        Vec3d fromVec = entityPlayer.getPositionEyes(0f);
        Vec3d lookDir = entityPlayer.getLookVec();
        // The vector we want to ray trace to
        Vec3d toVec = fromVec.add(lookDir.scale(SPECIAL_FIRE_RANGE_BLOCKS));
        // Perform the ray trace
        RayTraceResult rayTraceResult = world.rayTraceBlocks(fromVec, toVec);
        // Ensure we hit something
        if (rayTraceResult != null && rayTraceResult.hitVec != null)
        {
            // Grab the hit block position
            BlockPos hitPos = new BlockPos(rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z);
            // Grab all surrounding entities
            List<Entity> surroundingEntities = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, new AxisAlignedBB(hitPos).grow(HIT_RANGE));

            // Set each entity living on fire
            for (Entity entity : surroundingEntities)
            {
                if (entity instanceof EntityLivingBase)
                {
                    entity.setFire(20);
                }
            }
            // True, the effect was procd
            return true;
        }
        return false;
    }
}
