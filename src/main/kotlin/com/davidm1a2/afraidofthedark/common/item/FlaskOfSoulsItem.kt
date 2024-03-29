package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDPerItemCooldownItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager
import kotlin.random.Random

/**
 * Class representing the flask of souls item used to summon entities
 *
 * @constructor sets the item name
 */
class FlaskOfSoulsItem : AOTDPerItemCooldownItem("flask_of_souls", Properties()), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>> {
        return listOf(ResourceLocation(Constants.MOD_ID, "complete") to IItemPropertyGetter { stack, _, _ ->
            if (isComplete(stack)) 1f else 0f
        })
    }

    /**
     * Called when the player right clicks, ray trace for the hit location and summon the entity there if the flask is complete
     *
     * @param world  The world to ray trace at
     * @param player The player right clicking
     * @param hand   The hand holding the item
     * @return The result of the right click
     */
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Grab the held item
        val itemStack = player.getItemInHand(hand)

        // Server side processing only
        if (!world.isClientSide) {
            // Ensure the player has the right research
            if (player.getResearch().isResearched(ModResearches.FLASK_OF_SOULS)) {
                // Ray trace where the player is looking
                val rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE)

                // Test if we hit a block
                if (rayTraceResult.type == RayTraceResult.Type.BLOCK && rayTraceResult is BlockRayTraceResult) {
                    val hitPos = rayTraceResult.blockPos
                    val hitFace = rayTraceResult.direction
                    val spawnPos = hitPos.relative(hitFace)
                    // Test if the block right clicked is modifiable
                    if (world.mayInteract(player, hitPos) && player.mayUseItemAt(hitPos, hitFace, itemStack)) {
                        // If the entity was selected to spawn inside a liquid spawn it inside a liquid
                        spawnEntity(world, spawnPos.x + 0.5, spawnPos.y + 0.5, spawnPos.z + 0.5, itemStack, player)
                    }
                }
            } else {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
        return ActionResult.success(itemStack)
    }

    /**
     * Spawns this flask's entity at a given position
     *
     * @param world        The world to spawn the entity in
     * @param x            The x position to spawn the entity at
     * @param y            The y position to spawn the entity at
     * @param z            The z position to spawn the entity at
     * @param itemStack    The itemstack used to spawn the entity
     * @param entityPlayer The player that is spawning the entity
     */
    private fun spawnEntity(world: World, x: Double, y: Double, z: Double, itemStack: ItemStack, entityPlayer: PlayerEntity) {
        // Server side processing only
        if (!world.isClientSide) {
            // Flask has enough charges to be used
            if (isComplete(itemStack)) {
                // The flask is not on cooldown
                if (!isOnCooldown(itemStack)) {
                    // Grab the spawned entity ID
                    val entityToSpawnID = getSpawnedEntity(itemStack)

                    // Ensure it's not null, this should always be true
                    if (entityToSpawnID != null) {
                        // Create the entity from the ID
                        val entity = EntityType.byString(entityToSpawnID.toString()).map { it.create(world) }.orElse(null)
                        // If the entity is non-null spawn it
                        if (entity != null) {
                            entity.moveTo(x, y, z, Random.nextDouble(0.0, 360.0).toFloat(), 0.0f)
                            world.addFreshEntity(entity)
                            setOnCooldown(itemStack, entityPlayer)
                        } else {
                            logger.error("Unknown entity: $entityToSpawnID")
                        }
                    }
                } else {
                    entityPlayer.sendMessage(
                        TranslationTextComponent("message.afraidofthedark.flask_of_souls.on_cooldown", cooldownRemainingInSeconds(itemStack))
                    )
                }
            } else {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.flask_of_souls.incomplete"))
            }
        }
    }

    /**
     * The durability to show for this item is based on how much cooldown is left
     *
     * @param stack The itemstack to show durability for
     * @return Value between 0 to 1 of what percent of the durability bar to show
     */
    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        // If the flask is not charged then show the flask charge
        return if (!isComplete(stack)) {
            if (getSpawnedEntity(stack) == null) {
                1.0
            } else {
                1 - getKills(stack).toDouble() / getKillsRequired(stack)
            }
        } else {
            // Otherwise default to the standard charge durability
            super.getDurabilityForDisplay(stack)
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getCooldownInMilliseconds(itemStack: ItemStack): Int {
        return KILL_COUNT_TO_COOLDOWN[getKillsRequired(itemStack)] ?: 0
    }

    /**
     * Adds a tooltip to the flask item
     *
     * @param stack   The stack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced tooltip is set on, false otherwise
     */
    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        // If the player has the right research then show them flask stats
        if (player != null && player.getResearch().isResearched(ModResearches.FLASK_OF_SOULS)) {
            // If the flask is unbound show them information how to bind it
            if (getSpawnedEntity(stack) == null) {
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.flask_of_souls.empty.line1"))
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.flask_of_souls.empty.line2"))
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.flask_of_souls.empty.line3"))
            } else {
                tooltip.add(
                    TranslationTextComponent(
                        "tooltip.afraidofthedark.flask_of_souls.incomplete.line1",
                        EntityType.byString(getSpawnedEntity(stack)!!.toString()).get().description
                    )
                )
                tooltip.add(
                    TranslationTextComponent(
                        "tooltip.afraidofthedark.flask_of_souls.incomplete.line2",
                        getKills(stack),
                        getKillsRequired(stack)
                    )
                )
            }
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    fun getKillsRequired(stack: ItemStack): Int {
        return when (getSpawnedEntity(stack)) {
            in ModCommonConfiguration.flaskOfSoulsBasicEntities -> BASIC_ENTITY_KILL_COUNT
            in ModCommonConfiguration.flaskOfSoulsRareEntities -> RARE_ENTITY_KILL_COUNT
            in ModCommonConfiguration.flaskOfSoulsEpicEntities -> EPIC_ENTITY_KILL_COUNT
            else -> 0
        }
    }

    fun isComplete(stack: ItemStack): Boolean {
        return getSpawnedEntity(stack) != null && getKills(stack) == getKillsRequired(stack)
    }

    /**
     * Adds kills to the flask
     *
     * @param itemStack The item to add kills to
     * @param kills     The number of kills to add
     */
    fun addKills(itemStack: ItemStack, kills: Int) {
        // Grab the current kill count
        var currentKills = getKills(itemStack)

        // Grab the current entity, if this is null ignore the call
        val spawnedEntity = getSpawnedEntity(itemStack)
        if (spawnedEntity != null) {
            // Grab the max kills required to fill the flask
            val killsRequired = getKillsRequired(itemStack)

            // Update the current kills on the flask
            currentKills = (currentKills + kills).coerceIn(0, killsRequired)
            NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, currentKills)
        }
    }

    /**
     * Sets the souls in the flask
     *
     * @param itemStack The item to add kills to
     * @param kills     The number of kills to add
     */
    fun setKills(itemStack: ItemStack, kills: Int) {
        // Grab the current entity, if this is null ignore the call
        val spawnedEntity = getSpawnedEntity(itemStack)
        if (spawnedEntity != null) {
            // Grab the max kills required to fill the flask
            val killsRequired = getKillsRequired(itemStack)

            NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, kills.coerceIn(0, killsRequired))
        }
    }

    /**
     * Gets the number of kills on this flask
     *
     * @param itemStack The itemstack to get kills from
     * @return The number of kills on the flask
     */
    fun getKills(itemStack: ItemStack): Int {
        // Ensure we have the kills tag, and if not initialize it to 0
        if (!NBTHelper.hasTag(itemStack, NBT_FLASK_KILLS)) {
            NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, 0)
        }
        return NBTHelper.getInteger(itemStack, NBT_FLASK_KILLS)!!
    }

    fun supportsEntity(entity: ResourceLocation?): Boolean {
        if (entity == null) {
            return false
        }
        return ModCommonConfiguration.flaskOfSoulsBasicEntities.contains(entity) ||
            ModCommonConfiguration.flaskOfSoulsRareEntities.contains(entity) ||
            ModCommonConfiguration.flaskOfSoulsEpicEntities.contains(entity)
    }

    /**
     * Sets the spawned entity of this flask. If null is passed in the spawned entity is cleared
     *
     * @param itemStack The itemstack to set the spawned entity of
     * @param entity    The entity to set, or null to clear
     */
    fun setSpawnedEntity(itemStack: ItemStack, entity: ResourceLocation?) {
        if (entity != null) {
            NBTHelper.setString(itemStack, NBT_FLASK_TYPE, entity.toString())
        } else {
            NBTHelper.removeTag(itemStack, NBT_FLASK_TYPE)
        }
    }

    /**
     * Returns the entity ID that this flask will spawn or null if it's not yet known
     *
     * @param itemStack The itemstack to get the entity for
     * @return THe resourcelocation of the ID of the flask or null if it does not have an entity yet
     */
    fun getSpawnedEntity(itemStack: ItemStack): ResourceLocation? {
        return if (NBTHelper.hasTag(itemStack, NBT_FLASK_TYPE)) {
            ResourceLocation(NBTHelper.getString(itemStack, NBT_FLASK_TYPE)!!)
        } else {
            null
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Two constants, one for the flask type and one for flask kill count
        private const val NBT_FLASK_TYPE = "flask_type"
        private const val NBT_FLASK_KILLS = "flask_kills"

        // A mapping of kills -> cooldown
        private const val BASIC_ENTITY_KILL_COUNT = 32
        private const val RARE_ENTITY_KILL_COUNT = 16
        private const val EPIC_ENTITY_KILL_COUNT = 8
        private val KILL_COUNT_TO_COOLDOWN = mapOf(
            EPIC_ENTITY_KILL_COUNT to 20000,
            RARE_ENTITY_KILL_COUNT to 10000,
            BASIC_ENTITY_KILL_COUNT to 5000
        )

        val BASIC_ENTITIES = listOf(
            ModEntities.ENCHANTED_SKELETON,
            ModEntities.SPLINTER_DRONE,
            EntityType.BAT,
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ENDERMITE,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.LLAMA,
            EntityType.MAGMA_CUBE,
            EntityType.MOOSHROOM,
            EntityType.PIG,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.RAVAGER,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.SNOW_GOLEM,
            EntityType.SPIDER,
            EntityType.SQUID,
            EntityType.STRAY,
            EntityType.TROPICAL_FISH,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIFIED_PIGLIN
        ).map { it.registryName!!.toString() }
        val RARE_ENTITIES = listOf(
            ModEntities.WEREWOLF,
            ModEntities.ENCHANTED_FROG,
            EntityType.BEE,
            EntityType.CAT,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.ENDERMAN,
            EntityType.FOX,
            EntityType.GUARDIAN,
            EntityType.HORSE,
            EntityType.MULE,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PHANTOM,
            EntityType.POLAR_BEAR,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON_HORSE,
            EntityType.STRIDER,
            EntityType.TURTLE,
            EntityType.VEX,
            EntityType.TRADER_LLAMA,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.WOLF,
            EntityType.ZOMBIE_HORSE
        ).map { it.registryName!!.toString() }
        val EPIC_ENTITIES = listOf(
            EntityType.GHAST,
            EntityType.ELDER_GUARDIAN,
            EntityType.EVOKER,
            EntityType.ILLUSIONER,
            EntityType.IRON_GOLEM,
            EntityType.VILLAGER,
            EntityType.VINDICATOR,
            EntityType.WANDERING_TRADER,
            EntityType.ZOMBIE_VILLAGER
        ).map { it.registryName!!.toString() }
    }
}