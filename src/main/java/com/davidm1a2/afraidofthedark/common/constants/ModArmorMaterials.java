package com.davidm1a2.afraidofthedark.common.constants;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Class storing armor material constants. These are registered through the enum helper class and not through the standard forge system
 */
public class ModArmorMaterials
{
    public static final ItemArmor.ArmorMaterial IGNEOUS = EnumHelper.addArmorMaterial("igneous", "texture", 45, new int[] { 3, 8, 6, 3 }, 20,  SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0f);

    // Set material repair items
    static
    {
        IGNEOUS.setRepairItem(new ItemStack(ModItems.IGNEOUS_GEM));
    }
}
