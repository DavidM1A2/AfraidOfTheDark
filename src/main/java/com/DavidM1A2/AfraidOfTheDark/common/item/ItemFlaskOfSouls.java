package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModEntities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Class representing the flask of souls item used to summon entities
 */
public class ItemFlaskOfSouls extends AOTDItemWithPerItemCooldown
{
	// Two constants, one for the flask type and one for flask kill count
	private static final String NBT_FLASK_TYPE = "flask_type";
	private static final String NBT_FLASK_KILLS = "flask_kills";
	// A default kills required count
	private static final Integer DEFAULT_KILLS_REQUIRED = 32;
	// A mapping of entity -> kills required. Default kills required is 32, all other entities are listed below
	private static final Map<ResourceLocation, Integer> ENTITY_TO_KILLS_REQUIRED = ImmutableMap.<ResourceLocation, Integer>builder()
			.put(new ResourceLocation("villager"), 8)
			.put(new ResourceLocation("ghast"), 8)
			.put(new ResourceLocation("villager_golem"), 8)
			.put(ModEntities.WEREWOLF.getRegistryName(), 8)
			.put(new ResourceLocation("enderman"), 16)
			.put(new ResourceLocation("zombie_pigman"), 16)
			.put(new ResourceLocation("blaze"), 16)
			.put(new ResourceLocation("magma_cube"), 16)
			.put(new ResourceLocation("slime"), 16)
			.put(new ResourceLocation("witch"), 16)
			.put(new ResourceLocation("horse"), 16)
			.put(new ResourceLocation("wolf"), 16)
			.build();
	// Default cooldown for unknown kill count
	private static final Integer DEFAULT_COOLDOWN = 60000;
	// A mapping of kills -> cooldown
	private static final Map<Integer, Integer> KILL_COUNT_TO_COOLDOWN = ImmutableMap.<Integer, Integer>builder()
			.put(8, 20000)
			.put(16, 10000)
			.put(32, 5000)
			.build();

	/**
	 * Constructor sets the item name
	 */
	public ItemFlaskOfSouls()
	{
		super("flask_of_souls");
	}

	/**
	 * Called when the player right clicks, ray trace for the hit location and summon the entity there if the flask is complete
	 *
	 * @param worldIn The world to ray trace at
	 * @param playerIn The player right clicking
	 * @param handIn The hand holding the item
	 * @return The result of the right click
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		// Grab the held item
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		// Server side processing only
		if (!worldIn.isRemote)
		{
			// Ensure the player has the right research
			if (playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.PHYLACTERY_OF_SOULS))
			{
				// Ray trace where the player is looking
				RayTraceResult rayTraceResult = this.rayTrace(worldIn, playerIn, true);
				// Test if we hit a block, Intellij is wrong here, ray trace CAN be null!!!
				if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
				{
					BlockPos hitPos = rayTraceResult.getBlockPos();
					// Test if the block right clicked is modifiable
					if (worldIn.isBlockModifiable(playerIn, hitPos) && playerIn.canPlayerEdit(hitPos, rayTraceResult.sideHit, itemStack))
					{
						// If the entity was selected to spawn inside a liquid spawn it inside a liquid
						if (worldIn.getBlockState(hitPos).getBlock() instanceof BlockLiquid)
							this.spawnEntity(worldIn, hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5, itemStack, playerIn);
						// If the entity was selected to spawn on a block offset off the side of the block and spawn the entity there
						else
						{
							BlockPos spawnPos = hitPos.offset(rayTraceResult.sideHit);
							this.spawnEntity(worldIn, spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, itemStack, playerIn);
						}
					}
				}
			}
			else
			{
				playerIn.sendMessage(new TextComponentString("I'm not sure how to operate this."));
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
	}

	/**
	 * Spawns this flask's entity at a given position
	 *
	 * @param world The world to spawn the entity in
	 * @param x The x position to spawn the entity at
	 * @param y The y position to spawn the entity at
	 * @param z The z position to spawn the entity at
	 * @param itemStack The itemstack used to spawn the entity
	 * @param entityPlayer The player that is spawning the entity
	 */
	private void spawnEntity(World world, double x, double y, double z, ItemStack itemStack, EntityPlayer entityPlayer)
	{
		// Server side processing only
		if (!world.isRemote)
		{
			// Flask is complete
			if (this.isComplete(itemStack))
			{
				// The flask is not on cooldown
				if (!this.isOnCooldown(itemStack))
				{
					// Grab the spawned entity ID
					ResourceLocation entityToSpawnID = this.getSpawnedEntity(itemStack);
					// Ensure it's not null, this should always be true
					if (entityToSpawnID != null)
					{
						// Create the entity from the ID
						Entity entity = EntityList.createEntityByIDFromName(entityToSpawnID, world);
						// If the entity is non-null spawn it
						if (entity != null)
						{
							entity.setLocationAndAngles(x, y, z, RandomUtils.nextFloat(0, 360), 0.0F);
							world.spawnEntity(entity);
							this.setOnCooldown(itemStack, entityPlayer);
						}
						else
						{
							AfraidOfTheDark.INSTANCE.getLogger().error("Unknown entity: " + entityToSpawnID);
						}
					}
				}
				else
				{
					entityPlayer.sendMessage(new TextComponentString("Flask is on cooldown (" + this.cooldownRemainingInSeconds(itemStack) + "s)!"));
				}
			}
			else
			{
				entityPlayer.sendMessage(new TextComponentString("Flask is unbound or incomplete!"));
			}
		}
	}

	/**
	 * The durability to show for this item is based on how much cooldown is left
	 *
	 * @param stack The itemstack to show durability for
	 * @return Value between 0 to 1 of what percent of the durability bar to show
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		// If the flask is not charged then show the flask charge
		if (!this.isComplete(stack))
		{
			if (this.getSpawnedEntity(stack) == null)
				return 1;
			else
				return 1 - (double) this.getKills(stack) / (double) ENTITY_TO_KILLS_REQUIRED.getOrDefault(this.getSpawnedEntity(stack), DEFAULT_KILLS_REQUIRED);
		}
		// Otherwise default to the standard charge durability
		return super.getDurabilityForDisplay(stack);
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
		Integer killsRequired = ENTITY_TO_KILLS_REQUIRED.getOrDefault(this.getSpawnedEntity(itemStack), DEFAULT_KILLS_REQUIRED);
		return KILL_COUNT_TO_COOLDOWN.getOrDefault(killsRequired, DEFAULT_COOLDOWN);
	}

	/**
	 * Adds a tooltip to the flask item
	 *
	 * @param stack   The stack to add a tooltip to
	 * @param worldIn The world the item is in
	 * @param tooltip The tooltip to add to
	 * @param flagIn  True if the advanced tooltip is set on, false otherwise
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		// If the player has the right research then show them flask stats
		if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.PHYLACTERY_OF_SOULS))
		{
			// If the flask is unbound show them information how to bind it
			if (this.getSpawnedEntity(stack) == null)
			{
				tooltip.add("Flask unbound.");
				tooltip.add("Hold this in your hotbar while");
				tooltip.add("killing a mob to bind this flask.");
			}
			// If flask is bound then show the information about the kills
			else
			{
				// If the flask is done show info one way, otherwise show it the other way
				if (this.isComplete(stack))
				{
					tooltip.add("Flask bound to: " + EntityList.getTranslationName(this.getSpawnedEntity(stack)));
					tooltip.add("Flask complete, cooldown is " + (this.getItemCooldownInMilliseconds(stack) / 1000 + 1) + "s");
				}
				else
				{
					tooltip.add("Flask bound to: " + EntityList.getTranslationName(this.getSpawnedEntity(stack)));
					tooltip.add("Kills: " + this.getKills(stack) + "/" + ENTITY_TO_KILLS_REQUIRED.getOrDefault(this.getSpawnedEntity(stack), DEFAULT_KILLS_REQUIRED));
				}
			}
		}
		// Wrong research, show that info
		else
		{
			tooltip.add("I'm not sure how to use this.");
		}
	}

	/**
	 * Returns true if the flask of souls is complete or false otherwise
	 *
	 * @param itemStack The itemstack to test
	 * @return True if the flask is done, false otherwise
	 */
	public boolean isComplete(ItemStack itemStack)
	{
		return itemStack.getItemDamage() == 1;
	}

	/**
	 * Adds kills to the flask
	 *
	 * @param itemStack The item to add kills to
	 * @param kills The number of kills to add
	 */
	public void addKills(ItemStack itemStack, int kills)
	{
		// Grab the current kill count
		int currentKills = this.getKills(itemStack);
		// Grab the current entity, if this is null ignore the call
		ResourceLocation spawnedEntity = this.getSpawnedEntity(itemStack);
		if (spawnedEntity != null)
		{
			// Grab the max kills required to fill the flask
			Integer killsRequired = ENTITY_TO_KILLS_REQUIRED.getOrDefault(spawnedEntity, DEFAULT_KILLS_REQUIRED);
			// Update the current kills on the flask
			currentKills = MathHelper.clamp(currentKills + kills, 0, killsRequired);
			NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, currentKills);
			// If we have enough kills update the NBT data to 1 so that it is complete
			if (currentKills == killsRequired)
				itemStack.setItemDamage(1);
		}
	}

	/**
	 * Gets the number of kills on this flask
	 *
	 * @param itemStack The itemstack to get kills from
	 * @return The number of kills on the flask
	 */
	public int getKills(ItemStack itemStack)
	{
		// Ensure we have the kills tag, and if not initialize it to 0
		if (!NBTHelper.hasTag(itemStack, NBT_FLASK_KILLS))
			NBTHelper.setInteger(itemStack, NBT_FLASK_KILLS, 0);
		return NBTHelper.getInteger(itemStack, NBT_FLASK_KILLS);
	}

	/**
	 * Sets the spawned entity of this flask. If null is passed in the spawned entity is cleared
	 *
	 * @param itemStack The itemstack to set the spawned entity of
	 * @param entity    The entity to set, or null to clear
	 */
	public void setSpawnedEntity(ItemStack itemStack, ResourceLocation entity)
	{
		if (entity != null)
			NBTHelper.setString(itemStack, NBT_FLASK_TYPE, entity.toString());
		else
			NBTHelper.removeTag(itemStack, NBT_FLASK_TYPE);
	}

	/**
	 * Returns the entity ID that this flask will spawn or null if it's not yet known
	 *
	 * @param itemStack The itemstack to get the entity for
	 * @return THe resourcelocation of the ID of the flask or null if it does not have an entity yet
	 */
	public ResourceLocation getSpawnedEntity(ItemStack itemStack)
	{
		if (NBTHelper.hasTag(itemStack, NBT_FLASK_TYPE))
			return new ResourceLocation(NBTHelper.getString(itemStack, NBT_FLASK_TYPE));
		return null;
	}
}
