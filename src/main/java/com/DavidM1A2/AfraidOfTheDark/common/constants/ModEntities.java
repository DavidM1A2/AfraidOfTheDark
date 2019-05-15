package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.*;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.afraidofthedark.common.entity.werewolf.EntityWerewolf;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

/**
 * A static class containing all of our entity references for us
 */
public class ModEntities
{
    // Various entity IDs
    public static final int WEREWOLF_ID = 0;
    public static final int IRON_BOLT_ID = 1;
    public static final int SILVER_BOLT_ID = 2;
    public static final int WOODEN_BOLT_ID = 3;
    public static final int DEEE_SYFT_ID = 4;
    public static final int IGNEOUS_BOLT_ID = 5;
    public static final int STAR_METAL_BOLT_ID = 6;
    public static final int ENCHANTED_SKELETON_ID = 7;
    public static final int ENARIA_ID = 8;
    public static final int SPLINTER_DRONE_ID = 9;
    public static final int SPLINTER_DRONE_PROJECTILE_ID = 10;
    public static final int SPELL_PROJECTILE_ID = 11;
    public static final int SPELL_PROJECTILE_DIVE_ID = 12;
    public static final int SPELL_MYSELF_ID = 13;
    public static final int SPELL_AOE_ID = 14;
    public static final int GHASTLY_ENARIA_ID = 15;
    public static final int ARTWORK_ID = 16;
    public static final int SPELL_EXTRA_EFFECTS_ID = 17;

    // All mod entity static fields
    public static final EntityEntry ENCHANTED_SKELETON = EntityEntryBuilder.create()
            .egg(0x996600, 0xe69900)
            .entity(EntityEnchantedSkeleton.class)
            .id(new ResourceLocation(Constants.MOD_ID, "enchanted_skeleton"), ENCHANTED_SKELETON_ID)
            .name("enchanted_skeleton")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry WEREWOLF = EntityEntryBuilder.create()
            .egg(0x3B170B, 0x181907)
            .entity(EntityWerewolf.class)
            .id(new ResourceLocation(Constants.MOD_ID, "werewolf"), WEREWOLF_ID)
            .name("werewolf")
            .tracker(50, 1, true)
            .spawn(EnumCreatureType.MONSTER, 25, 1, 4, ModBiomes.ERIE_FOREST) // Weight = 100 is for skeletons, use 1/4 of that
            .build();
    public static final EntityEntry GHASTLY_ENARIA = EntityEntryBuilder.create()
            .entity(EntityGhastlyEnaria.class)
            .id(new ResourceLocation(Constants.MOD_ID, "ghastly_enaria"), GHASTLY_ENARIA_ID)
            .name("ghastly_enaria")
            .tracker(AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands() / 2, 1, true)
            .build();
    public static final EntityEntry SPLINTER_DRONE = EntityEntryBuilder.create()
            .egg(0xcc6600, 0x63300)
            .entity(EntitySplinterDrone.class)
            .id(new ResourceLocation(Constants.MOD_ID, "splinter_drone"), SPLINTER_DRONE_ID)
            .name("splinter_drone")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry SPLINTER_DRONE_PROJECTILE = EntityEntryBuilder.create()
            .entity(EntitySplinterDroneProjectile.class)
            .id(new ResourceLocation(Constants.MOD_ID, "splinter_drone_projectile"), SPLINTER_DRONE_PROJECTILE_ID)
            .name("splinter_drone_projectile")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry ENARIA = EntityEntryBuilder.create()
            .entity(EntityEnaria.class)
            .id(new ResourceLocation(Constants.MOD_ID, "enaria"), ENARIA_ID)
            .name("enaria")
            .tracker(50, 1, true)
            .build();

    // 5 bolt entities
    public static final EntityEntry WOODEN_BOLT = EntityEntryBuilder.create()
            .entity(EntityWoodenBolt.class)
            .id(new ResourceLocation(Constants.MOD_ID, "wooden_bolt"), WOODEN_BOLT_ID)
            .name("wooden_bolt")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry IRON_BOLT = EntityEntryBuilder.create()
            .entity(EntityIronBolt.class)
            .id(new ResourceLocation(Constants.MOD_ID, "iron_bolt"), IRON_BOLT_ID)
            .name("iron_bolt")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry SILVER_BOLT = EntityEntryBuilder.create()
            .entity(EntitySilverBolt.class)
            .id(new ResourceLocation(Constants.MOD_ID, "silver_bolt"), SILVER_BOLT_ID)
            .name("silver_bolt")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry IGNEOUS_BOLT = EntityEntryBuilder.create()
            .entity(EntityIgneousBolt.class)
            .id(new ResourceLocation(Constants.MOD_ID, "igneous_bolt"), IGNEOUS_BOLT_ID)
            .name("igneous_bolt")
            .tracker(50, 1, true)
            .build();
    public static final EntityEntry STAR_METAL_BOLT = EntityEntryBuilder.create()
            .entity(EntityStarMetalBolt.class)
            .id(new ResourceLocation(Constants.MOD_ID, "star_metal_bolt"), STAR_METAL_BOLT_ID)
            .name("star_metal_bolt")
            .tracker(50, 1, true)
            .build();

    // An array containing a list of entities that AOTD adds
    public static EntityEntry[] ENTITY_LIST = new EntityEntry[]
            {
                    ENCHANTED_SKELETON,
                    WEREWOLF,
                    GHASTLY_ENARIA,
                    SPLINTER_DRONE,
                    SPLINTER_DRONE_PROJECTILE,
                    ENARIA,
                    WOODEN_BOLT,
                    IRON_BOLT,
                    SILVER_BOLT,
                    IGNEOUS_BOLT,
                    STAR_METAL_BOLT
            };
}
