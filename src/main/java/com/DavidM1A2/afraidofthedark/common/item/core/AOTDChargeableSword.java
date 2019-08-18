package com.DavidM1A2.afraidofthedark.common.item.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Base class for swords that don't have durability but charge instead
 */
public abstract class AOTDChargeableSword extends AOTDSword
{
    // Constant for storing charge on the item NBT
    private static final String NBT_CHARGE = "charge";

    // The percent the sword will charge per entity hit, defaults to 5%
    protected double percentChargePerAttack = 5;

    /**
     * Constructor takes a tool material and name of the item in the constructor
     *
     * @param toolMaterial The tool material to be used for the sword
     * @param baseName     The name of the sword
     */
    public AOTDChargeableSword(ToolMaterial toolMaterial, String baseName)
    {
        super(toolMaterial, baseName);
        // This is required to make the sword unbreakable
        this.setMaxDamage(0);
        // Emit a charged = 1 property when charged, 0 otherwise
        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "charged"), (stack, worldIn, entityIn) -> this.isFullyCharged(stack) ? 1 : 0);
    }

    /**
     * Called when you hit an entity with the sword
     *
     * @param stack    THe itemstack that was hit with
     * @param target   The target of the attack
     * @param attacker The attacker that initiated the attack
     * @return True to let the attack happen, false otherwise
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        // Only charge on hitting entity living entities not armor stands
        if (target instanceof EntityPlayer || target instanceof EntityLiving)
        {
            this.addCharge(stack, this.percentChargePerAttack);
        }
        return true;
    }

    /**
     * Gets the durability to display for the itemstack which will be the inverse of charge (since charge goes from 0 -> 100
     * and durability goes from 1 -> 0
     *
     * @param stack The itemstack to get durability for
     * @return 0 for 100% charged or 1 for 0% charged
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1.0 - getCharge(stack) / 100.0;
    }

    /**
     * @return False, chargable swords don't take damage
     */
    @Override
    public boolean isDamageable()
    {
        return false;
    }

    /**
     * Chargable swords always show the charge bar
     *
     * @param stack The itemstack to show charge bar for
     * @return True to show 'durability'
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    /**
     * Right click fires the charge attack if it is fully charged
     *
     * @param worldIn  The world the item was right clicked in
     * @param playerIn The player that right clicked with the sword
     * @param handIn   The hand the right click was triggered from
     * @return Success if the sword fired its ability, pass otherwise
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack swordStack = playerIn.getHeldItem(handIn);
        // Server side processing only
        if (!worldIn.isRemote)
        {
            // Test if the sword is charged
            if (this.isFullyCharged(swordStack))
            {
                // Perform the attack, if it succeeds clear the charge
                if (this.performChargeAttack(swordStack, worldIn, playerIn))
                {
                    // Reset the sword charge
                    this.addCharge(swordStack, -100);
                }
            }
            // Otherwise display an error to the player
            else
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.chargable_sword.not_enough_energy"));
            }
        }

        // Fail to avoid the move animation on item use
        return ActionResult.newResult(EnumActionResult.FAIL, swordStack);
    }

    /**
     * Performs the attack once the sword is charged
     *
     * @param itemStack    The itemstack that was charged
     * @param world        The world the charge attack happened in
     * @param entityPlayer The player who used the charge attack
     * @return True if the charge attack went off, false otherwise
     */
    public abstract boolean performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer);

    /**
     * Returns the amount of charge a given sword has
     *
     * @param itemStack The itemstack to test
     * @return The charge from 0% to 100%
     */
    public double getCharge(ItemStack itemStack)
    {
        ensureChargeInit(itemStack);
        return NBTHelper.getDouble(itemStack, NBT_CHARGE);
    }

    /**
     * Adds charge to the sword
     *
     * @param itemStack The itemstack to add charge to
     * @param charge    The charge to add (or subtract) from 0.0 to 100.0
     */
    public void addCharge(ItemStack itemStack, double charge)
    {
        ensureChargeInit(itemStack);
        double newCharge = MathHelper.clamp(NBTHelper.getDouble(itemStack, NBT_CHARGE) + charge, 0, 100);
        NBTHelper.setDouble(itemStack, NBT_CHARGE, newCharge);
    }

    /**
     * True if the sword was fully charged, false otherwise
     *
     * @param itemStack The itemstack to test charge of
     * @return True if the sword is fully charged, false if not
     */
    public boolean isFullyCharged(ItemStack itemStack)
    {
        ensureChargeInit(itemStack);
        return NBTHelper.getDouble(itemStack, NBT_CHARGE) == 100.0;
    }

    /**
     * Ensures an itemstack has the charge tag at default value of 0
     *
     * @param itemStack The itemstack to test
     */
    private void ensureChargeInit(ItemStack itemStack)
    {
        if (!NBTHelper.hasTag(itemStack, NBT_CHARGE))
        {
            NBTHelper.setDouble(itemStack, NBT_CHARGE, 0.0);
        }
    }
}
