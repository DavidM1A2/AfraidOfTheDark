package com.davidm1a2.afraidofthedark.common.item;

import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Item representing a wand used to cast spells with an item
 */
public class ItemWand extends AOTDItem
{
    // NBT compound for spell id
    private static final String NBT_SPELL_ID = "spell_id";

    /**
     * Constructor sets up item properties
     */
    public ItemWand()
    {
        super("wand");
        // They don't stack
        this.setMaxStackSize(1);
    }

    /**
     * Called when the item is right clicked with a hand
     *
     * @param worldIn  The world the item was right clicked in
     * @param playerIn The player that right clicked
     * @param handIn   The hand the item is in
     * @return The result of the right click
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Grab the held itemstack
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        // Grab the player's spell manager
        IAOTDPlayerSpellManager spellManager = playerIn.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);

        // If the player is sneaking switch spells, otherwise cast the spell
        if (playerIn.isSneaking())
        {
            // If the item has no spell ID yet grab the player's first spell id and bind it
            if (!hasSpellId(heldItem))
            {
                // Set the spell id to the first player's spell
                this.setSpellToFirstAvailable(playerIn, heldItem);
            }
            else
            {
                // Grab the current spell ID
                UUID spellId = this.getSpellId(heldItem);
                // Grab the spell bound to this ID
                Spell currentSpell = spellManager.getSpellById(spellId);
                // If the spell was deleted set the spell to the player's first available spell
                if (currentSpell == null)
                {
                    setSpellToFirstAvailable(playerIn, heldItem);
                }
                // Otherwise find the next spell after the current one
                else
                {
                    // Grab the spell list
                    Collection<Spell> spells = spellManager.getSpells();
                    // Go over each spell in the list
                    Iterator<Spell> iterator = spells.iterator();
                    while (iterator.hasNext())
                    {
                        // Grab the spell
                        Spell spell = iterator.next();
                        // If it has the right ID break out of the loop
                        if (spell.getId().equals(spellId))
                        {
                            break;
                        }
                    }
                    // If our iterator has another spell after ours store that one
                    if (iterator.hasNext())
                    {
                        Spell next = iterator.next();
                        this.setSpellId(heldItem, next.getId());
                        // Send the message server side
                        if (!worldIn.isRemote)
                        {
                            playerIn.sendMessage(new TextComponentTranslation("aotd.wand.spell_set", next.getName()));
                        }
                    }
                    // Otherwise loop to the beginning again
                    else
                    {
                        this.setSpellToFirstAvailable(playerIn, heldItem);
                    }
                }
            }
        }
        else
        {
            // Server side processing only
            if (!worldIn.isRemote)
            {
                // Grab the spell that's on the item
                UUID spellId = getSpellId(heldItem);
                if (spellId != null)
                {
                    Spell toCast = spellManager.getSpellById(spellId);
                    // If the spell is null print an error, otherwise try and cast the spell
                    if (toCast != null)
                    {
                        toCast.attemptToCast(playerIn);
                    }
                    else
                    {
                        playerIn.sendMessage(new TextComponentTranslation("aotd.wand.invalid_spell"));
                    }
                }
                else
                {
                    playerIn.sendMessage(new TextComponentTranslation("aotd.wand.no_bound_spell"));
                }
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    /**
     * Sets the itemstacks spell UUID to the first one the player has
     *
     * @param entityPlayer The player who has spells
     * @param itemStack    The itemstack to set the first spell on
     */
    private void setSpellToFirstAvailable(EntityPlayer entityPlayer, ItemStack itemStack)
    {
        // Grab the player's spell manager
        IAOTDPlayerSpellManager spellManager = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
        // If they have at least one spell grab it
        if (spellManager.getSpells().size() != 0)
        {
            Spell next = spellManager.getSpells().iterator().next();
            // Server side sending only, tell the player the spell was updated
            if (!entityPlayer.world.isRemote)
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.wand.spell_set", next.getName()));
            }
            // Set the NBT spell ID
            this.setSpellId(itemStack, next.getId());
        }
        else
        {
            // Server side sending only, tell the player he/she has no spells to bind to the wand yet
            if (!entityPlayer.world.isRemote)
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.wand.no_spells"));
            }
        }
    }

    /**
     * True if the itemstack has a spell ID NBT tag, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if the itemstack has a spell ID, false otherwise
     */
    private boolean hasSpellId(ItemStack itemStack)
    {
        return NBTHelper.hasTag(itemStack, NBT_SPELL_ID);
    }

    /**
     * Sets the spell ID of the itemstack
     *
     * @param itemStack The itemstack to set the spell id on
     * @param spellId   The new spell id to USE
     */
    private void setSpellId(ItemStack itemStack, UUID spellId)
    {
        NBTHelper.setCompound(itemStack, NBT_SPELL_ID, NBTUtil.createUUIDTag(spellId));
    }

    /**
     * Gets the spell id off of the itemstack or null if it does not exist
     *
     * @param itemStack The itemstack to get the spell id from
     * @return The spell id on the itemstack or null if it doesn't exist
     */
    private UUID getSpellId(ItemStack itemStack)
    {
        NBTTagCompound uuidNBT = NBTHelper.getCompound(itemStack, NBT_SPELL_ID);
        if (uuidNBT != null)
        {
            return NBTUtil.getUUIDFromTag(uuidNBT);
        }
        return null;
    }

    /**
     * Called to add a tooltip to the journal.
     *
     * @param stack   The itemstack to add information about
     * @param worldIn The world that the item was hovered over in
     * @param tooltip The tooltip that we need to fill out
     * @param flagIn  The flag telling us if we should show advanced or normal tooltips
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        // Need to test if player is null during client init
        if (player != null)
        {
            // Test if the wand has a spell ID
            if (this.hasSpellId(stack))
            {
                // Grab the spell by ID
                Spell spell = player.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null).getSpellById(this.getSpellId(stack));
                // If the spell is non-null show the spell's stats
                if (spell != null)
                {
                    tooltip.add("Spell: " + spell.getName());
                    tooltip.add("Cost: " + spell.getCost());
                }
                // Show that the wand is invalid since the player does not have a matching spell ID
                else
                {
                    tooltip.add("Spell on wand is invalid.");
                }
            }
            // The wand isn't bound yet, so mention that the player can do it with crouch & right click
            else
            {
                tooltip.add("Wand does not have a spell bound yet.");
                tooltip.add("Do so with crouch & right click.");
            }
        }
    }
}
