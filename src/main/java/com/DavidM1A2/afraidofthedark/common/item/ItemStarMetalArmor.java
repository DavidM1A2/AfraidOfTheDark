package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModArmorMaterials;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDArmor;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Class representing the 4 different pieces of star metal armor
 */
public class ItemStarMetalArmor extends AOTDArmor implements ISpecialArmor
{
    // When this armor piece last proc'd absorption
    private static final String NBT_LAST_ABSORPTION_PROC = "last_absorption_proc";
    // Number of milliseconds until the absoroption proc
    private static final int ABSORPTION_PROC_CD_MILLIS = 120000; // 60000 millis aka 120s
    // Amount of absorption from each armor piece in hearts
    private static final int ABSORPTION_PER_PIECE = 4;
    // Damage sources that relate to unblockable damage
    private static final Set<DamageSource> TRUE_DAMAGE_SOURCES = ImmutableSet.of(DamageSource.DROWN, DamageSource.FALL, DamageSource.IN_WALL, DamageSource.OUT_OF_WORLD, DamageSource.STARVE, DamageSource.LAVA);

    /**
     * Constructor sets up armor item properties
     *
     * @param baseName        The name of the item to be used by the game registry
     * @param equipmentSlotIn The slot that this armor pieces goes on, can be one of 4 options
     */
    public ItemStarMetalArmor(String baseName, EntityEquipmentSlot equipmentSlotIn)
    {
        super(baseName, ModArmorMaterials.IGNEOUS, 3, equipmentSlotIn);
        // Makes the armor invincible
        this.setMaxDamage(0);
        // Block 70% of the damage up to 20
        this.maxDamageBlocked = 20;
        this.percentOfDamageBlocked = 0.7;
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
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        // Star metal 1 is for helm, boots, and chest while Star metal 2 is for leggings
        if (slot == EntityEquipmentSlot.LEGS)
        {
            return "afraidofthedark:textures/armor/star_metal_2.png";
        }
        else
        {
            return "afraidofthedark:textures/armor/star_metal_1.png";
        }
    }

    /**
     * Adds a tooltip to the armor piece
     *
     * @param stack   The itemstack to add a tooltip to
     * @param worldIn The world the item is in
     * @param tooltip The tooltip list to add to
     * @param flagIn  The flag telling us if advanced tooltips are on or not
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
        {
            tooltip.add("Magical armor will never break.");
            tooltip.add("Gives you two absorption hearts per piece.");
        }
        else
        {
            tooltip.add("I dont know how to use this.");
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param world     The world the player is in
     * @param player    The player that is wearing the armor
     * @param itemStack The itemstack of the armor item
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        // We have to test client and server side since absorption is client side :(

        // Test if the player has the star metal research
        if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
        {
            // If the stack is ready to proc absorption, add 2 absorption hearts
            if (this.readyToProcAbsorption(itemStack))
            {
                // The number of star metal armor pieces worn
                long numberStarMetalPiecesWorn = player.inventory.armorInventory.stream().map(ItemStack::getItem).filter(ItemStarMetalArmor.class::isInstance).count();
                if (numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE >= player.getAbsorptionAmount())
                {
                    // Add 4 to absorption up to a max for the proc
                    player.setAbsorptionAmount(MathHelper.clamp(player.getAbsorptionAmount() + ABSORPTION_PER_PIECE, 0, numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE));
                }
                // Update the last proc time to now
                NBTHelper.setLong(itemStack, NBT_LAST_ABSORPTION_PROC, System.currentTimeMillis());
            }
        }
    }

    /**
     * True if the armor is ready to proc absorption, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if absorption can be proc'd, false otherwise
     */
    private boolean readyToProcAbsorption(ItemStack itemStack)
    {
        return !NBTHelper.hasTag(itemStack, NBT_LAST_ABSORPTION_PROC) || System.currentTimeMillis() > NBTHelper.getLong(itemStack, NBT_LAST_ABSORPTION_PROC) + ABSORPTION_PROC_CD_MILLIS;
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
    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {}

    /**
     * Returns the number of additional shields to display when wearing the armor
     *
     * @param player The player wearing the armor
     * @param armor  The armor itemstack
     * @param slot   The slot the item is in
     * @return THe number of shields to display when wearing the armor
     */
    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
    {
        // 0, since we only want to display default armor values
        return 0;
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
    @Override
    public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
    {
        return false;
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
    @Override
    public ArmorProperties getProperties(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
    {
        // Compute armor properties for players only
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            // Ensure the player has the right research
            if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.STAR_METAL))
            {
                // No damage reduction against true sources
                if (TRUE_DAMAGE_SOURCES.contains(source))
                {
                    return new ArmorProperties(0, getRatio(slot), 0);
                }
            }
            else
            {
                // Armor is useless without research
                return new ArmorProperties(0, getRatio(slot), 0);
            }
        }
        else
        {
            // Armor is useless without research
            return new ArmorProperties(0, getRatio(slot), 0);
        }

        // Default armor protection if no special set bonus applies
        return getDefaultProperties(slot);
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private double getRatio(int slot)
    {
        // Total protection of each piece
        final int totalProtection = 3 + 6 + 8 + 3;
        // Protection of this piece over the total in a given ratio
        switch (slot)
        {
            case 0:
            case 3:
                return 3.0 / totalProtection * this.percentOfDamageBlocked;
            case 1:
                return 6.0 / totalProtection * this.percentOfDamageBlocked;
            case 2:
                return 8.0 / totalProtection * this.percentOfDamageBlocked;
            default:
                return 0;
        }
    }

    /**
     * Gets the default armor properties when taking damage
     *
     * @param slot The slot that the armor piece is in
     * @return default armor properties for this set
     */
    private ArmorProperties getDefaultProperties(int slot)
    {
        return new ArmorProperties(0, getRatio(slot), this.maxDamageBlocked);
    }
}
