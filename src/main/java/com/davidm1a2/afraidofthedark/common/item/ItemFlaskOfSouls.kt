package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.block.BlockLiquid
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityList
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import kotlin.random.Random

/**
 * Class representing the flask of souls item used to summon entities
 *
 * @constructor sets the item name
 */
class ItemFlaskOfSouls : AOTDItemWithPerItemCooldown("flask_of_souls")
{
    init
    {
        addPropertyOverride(ResourceLocation(Constants.MOD_ID, "complete"))
        { stack, _, _ ->
            if (isComplete(stack)) 1f else 0f
        }
    }

    /**
     * Called when the player right clicks, ray trace for the hit location and summon the entity there if the flask is complete
     *
     * @param world  The world to ray trace at
     * @param player The player right clicking
     * @param hand   The hand holding the item
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        // Grab the held item
        val itemStack = player.getHeldItem(hand)

        // Server side processing only
        if (!world.isRemote)
        {
            // Ensure the player has the right research
            if (player.getResearch().isResearched(ModResearches.PHYLACTERY_OF_SOULS))
            {
                // Ray trace where the player is looking
                val rayTraceResult: RayTraceResult? = rayTrace(world, player, true)

                // Test if we hit a block, Intellij is wrong here, ray trace CAN be null!!!
                if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    val hitPos = rayTraceResult.blockPos
                    // Test if the block right clicked is modifiable
                    if (world.isBlockModifiable(player, hitPos) && player.canPlayerEdit(hitPos, rayTraceResult.sideHit, itemStack))
                    {
                        // If the entity was selected to spawn inside a liquid spawn it inside a liquid
                        if (world.getBlockState(hitPos).block is BlockLiquid)
                        {
                            spawnEntity(world, hitPos.x + 0.5, hitPos.y + 0.5, hitPos.z + 0.5, itemStack, player)
                        }
                        else
                        {
                            val spawnPos = hitPos.offset(rayTraceResult.sideHit)
                            spawnEntity(world, spawnPos.x + 0.5, spawnPos.y + 0.5, spawnPos.z + 0.5, itemStack, player)
                        }
                    }
                }
            }
            else
            {
                player.sendMessage(TextComponentTranslation("aotd.dont_understand"))
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack)
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
    private fun spawnEntity(world: World, x: Double, y: Double, z: Double, itemStack: ItemStack, entityPlayer: EntityPlayer)
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Flask is complete
            if (isComplete(itemStack))
            {
                // The flask is not on cooldown
                if (!isOnCooldown(itemStack))
                {
                    // Grab the spawned entity ID
                    val entityToSpawnID = getSpawnedEntity(itemStack)

                    // Ensure it's not null, this should always be true
                    if (entityToSpawnID != null)
                    {
                        // Create the entity from the ID
                        val entity = EntityList.createEntityByIDFromName(entityToSpawnID, world)
                        // If the entity is non-null spawn it
                        if (entity != null)
                        {
                            entity.setLocationAndAngles(x, y, z, Random.nextDouble(0.0, 360.0).toFloat(), 0.0f)
                            world.spawnEntity(entity)
                            setOnCooldown(itemStack, entityPlayer)
                        }
                        else
                        {
                            AfraidOfTheDark.INSTANCE.logger.error("Unknown entity: $entityToSpawnID")
                        }
                    }
                }
                else
                {
                    entityPlayer.sendMessage(TextComponentTranslation("aotd.flask_of_souls.on_cooldown", cooldownRemainingInSeconds(itemStack)))
                }
            }
            else
            {
                entityPlayer.sendMessage(TextComponentTranslation("aotd.flask_of_souls.incomplete"))
            }
        }
    }

    /**
     * The durability to show for this item is based on how much cooldown is left
     *
     * @param stack The itemstack to show durability for
     * @return Value between 0 to 1 of what percent of the durability bar to show
     */
    override fun getDurabilityForDisplay(stack: ItemStack): Double
    {
        // If the flask is not charged then show the flask charge
        return if (!isComplete(stack))
        {
            if (getSpawnedEntity(stack) == null)
            {
                1.0
            }
            else
            {
                1 - getKills(stack).toDouble() / (ENTITY_TO_KILLS_REQUIRED[getSpawnedEntity(stack)] ?: DEFAULT_KILLS_REQUIRED).toDouble()
            }
        }
        else
        {
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
    override fun getItemCooldownInMilliseconds(itemStack: ItemStack): Int
    {
        val killsRequired = ENTITY_TO_KILLS_REQUIRED[getSpawnedEntity(itemStack)] ?: DEFAULT_KILLS_REQUIRED
        return KILL_COUNT_TO_COOLDOWN[killsRequired] ?: DEFAULT_COOLDOWN
    }

    /**
     * Adds a tooltip to the flask item
     *
     * @param stack   The stack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip to add to
     * @param flag  True if the advanced tooltip is set on, false otherwise
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val player = Minecraft.getMinecraft().player
        // If the player has the right research then show them flask stats
        if (player != null && player.getResearch().isResearched(ModResearches.PHYLACTERY_OF_SOULS))
        {
            // If the flask is unbound show them information how to bind it
            if (getSpawnedEntity(stack) == null)
            {
                tooltip.add("Flask unbound.")
                tooltip.add("Hold this in your hotbar while")
                tooltip.add("killing a mob to bind this flask.")
            }
            else
            {
                // If the flask is done show info one way, otherwise show it the other way
                if (isComplete(stack))
                {
                    tooltip.add("Flask bound to: ${EntityList.getTranslationName(getSpawnedEntity(stack))}")
                    tooltip.add("Flask complete, cooldown is ${getItemCooldownInMilliseconds(stack) / 1000 + 1}s")
                }
                else
                {
                    tooltip.add("Flask bound to: ${EntityList.getTranslationName(getSpawnedEntity(stack))}")
                    tooltip.add("Kills: ${getKills(stack)}/${ENTITY_TO_KILLS_REQUIRED[getSpawnedEntity(stack)] ?: DEFAULT_KILLS_REQUIRED}")
                }
            }
        }
        else
        {
            tooltip.add("I'm not sure how to use this.")
        }
    }

    /**
     * Returns true if the flask of souls is complete or false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if the flask is done, false otherwise
     */
    fun isComplete(itemStack: ItemStack): Boolean
    {
        val flaskComplete = NBTHelper.getBoolean(itemStack, NBT_FLASK_COMPLETE)
        return flaskComplete ?: false
    }

    /**
     * Adds kills to the flask
     *
     * @param itemStack The item to add kills to
     * @param kills     The number of kills to add
     */
    fun addKills(itemStack: ItemStack, kills: Int)
    {
        // Grab the current kill count
        var currentKills = getKills(itemStack)

        // Grab the current entity, if this is null ignore the call
        val spawnedEntity = getSpawnedEntity(itemStack)
        if (spawnedEntity != null)
        {
            // Grab the max kills required to fill the flask
            val killsRequired = ENTITY_TO_KILLS_REQUIRED[spawnedEntity] ?: DEFAULT_KILLS_REQUIRED

            // Update the current kills on the flask
            currentKills = (currentKills + kills).coerceIn(0, killsRequired)
            NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, currentKills)

            // If we have enough kills update the NBT data to 1 so that it is complete
            if (currentKills == killsRequired)
            {
                NBTHelper.setBoolean(itemStack, NBT_FLASK_COMPLETE, true)
            }
        }
    }

    /**
     * Gets the number of kills on this flask
     *
     * @param itemStack The itemstack to get kills from
     * @return The number of kills on the flask
     */
    private fun getKills(itemStack: ItemStack): Int
    {
        // Ensure we have the kills tag, and if not initialize it to 0
        if (!NBTHelper.hasTag(itemStack, NBT_FLASK_KILLS))
        {
            NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, 0)
        }
        return NBTHelper.getInteger(itemStack, NBT_FLASK_KILLS)!!
    }

    /**
     * Sets the spawned entity of this flask. If null is passed in the spawned entity is cleared
     *
     * @param itemStack The itemstack to set the spawned entity of
     * @param entity    The entity to set, or null to clear
     */
    fun setSpawnedEntity(itemStack: ItemStack, entity: ResourceLocation?)
    {
        if (entity != null)
        {
            NBTHelper.setString(itemStack, NBT_FLASK_TYPE, entity.toString())
        }
        else
        {
            NBTHelper.removeTag(itemStack, NBT_FLASK_TYPE)
        }
    }

    /**
     * Returns the entity ID that this flask will spawn or null if it's not yet known
     *
     * @param itemStack The itemstack to get the entity for
     * @return THe resourcelocation of the ID of the flask or null if it does not have an entity yet
     */
    fun getSpawnedEntity(itemStack: ItemStack): ResourceLocation?
    {
        return if (NBTHelper.hasTag(itemStack, NBT_FLASK_TYPE))
        {
            ResourceLocation(NBTHelper.getString(itemStack, NBT_FLASK_TYPE)!!)
        }
        else
        {
            null
        }
    }

    companion object
    {
        // Two constants, one for the flask type and one for flask kill count
        private const val NBT_FLASK_TYPE = "flask_type"
        private const val NBT_FLASK_KILLS = "flask_kills"
        private const val NBT_FLASK_COMPLETE = "flask_complete"

        // A default kills required count
        private const val DEFAULT_KILLS_REQUIRED = 32

        // A mapping of entity -> kills required. Default kills required is 32, all other entities are listed below
        private val ENTITY_TO_KILLS_REQUIRED = mapOf(
            ResourceLocation("villager") to 8,
            ResourceLocation("ghast") to 8,
            ResourceLocation("villager_golem") to 8,
            ModEntities.WEREWOLF.registryName to 8,
            ResourceLocation("enderman") to 16,
            ResourceLocation("zombie_pigman") to 16,
            ResourceLocation("blaze") to 16,
            ResourceLocation("magma_cube") to 16,
            ResourceLocation("slime") to 16,
            ResourceLocation("witch") to 16,
            ResourceLocation("horse") to 16,
            ResourceLocation("wolf") to 16
        )

        // Default cooldown for unknown kill count
        private const val DEFAULT_COOLDOWN = 60000

        // A mapping of kills -> cooldown
        private val KILL_COUNT_TO_COOLDOWN = mapOf(
            8 to 20000,
            16 to 10000,
            32 to 5000
        )
    }
}