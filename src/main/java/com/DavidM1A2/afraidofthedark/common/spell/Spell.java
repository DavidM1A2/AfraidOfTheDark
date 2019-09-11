package com.DavidM1A2.afraidofthedark.common.spell;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.particle.AOTDParticleRegistry;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncParticle;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Class representing a spell instance created by a player
 */
public class Spell implements INBTSerializable<NBTTagCompound>
{
    // Constants used for NBT serialization/deserialiation
    private static final String NBT_NAME = "name";
    private static final String NBT_ID = "id";
    private static final String NBT_OWNER_ID = "owner_id";
    private static final String NBT_POWER_SOURCE = "power_source";
    private static final String NBT_SPELL_STAGES = "spell_stages";

    // The spell's name, can't be null (empty by default)
    private String name;
    // The spell's universally unique identifier, cannot be null
    private UUID id;
    // The spell's owner's id (player's universally unique identifier), cannot be null
    private UUID ownerId;

    // The source that is powering the spell, can be null
    private SpellPowerSource powerSource;
    // The list of spell stages this spell can go through, can have 0 - inf elements
    private List<SpellStage> spellStages;

    /**
     * Constructor that takes the player that created the spell in as a parameter
     *
     * @param entityPlayer The player that owns the spell/made the spell
     */
    public Spell(EntityPlayer entityPlayer)
    {
        // Assign a random spell ID
        this.id = UUID.randomUUID();
        // Assign the owner id to the player's id
        this.ownerId = entityPlayer.getPersistentID();
        // Empty spell name is default
        this.name = StringUtils.EMPTY;
        // Null spell power source is default
        this.powerSource = null;
        // Spell stage list is empty by default
        this.spellStages = new ArrayList<>();
    }

    /**
     * Constructor that takes in an NBT compound and creates the spell from NBT
     *
     * @param spellNBT The NBT containing the spell's information
     */
    public Spell(NBTTagCompound spellNBT)
    {
        this.deserializeNBT(spellNBT);
    }

    /**
     * Called to cast the spell, notifies the player if something is wrong so the spell won't cast
     *
     * @param entityPlayer The player casting the spell
     */
    public void attemptToCast(EntityPlayer entityPlayer)
    {
        // Server side processing only
        if (!entityPlayer.world.isRemote)
        {
            // Make sure the player isn't in the nightmare realm
            if (entityPlayer.dimension != ModDimensions.NIGHTMARE.getId())
            {
                // If the spell is valid continue, if not print an error
                if (this.isValid())
                {
                    // Test if the spell can be cast, if not tell the player why
                    if (this.powerSource.canCast(entityPlayer, this))
                    {
                        // Consumer the power to cast the spell
                        this.powerSource.consumePowerToCast(entityPlayer, this);
                        // Play a cast sound
                        entityPlayer.world.playSound(null, entityPlayer.getPosition(), ModSounds.SPELL_CAST, SoundCategory.PLAYERS, 1.0f, (float) (0.8f + Math.random() * 0.4));
                        // Spawn 3-5 particles
                        List<Vec3d> positions = new ArrayList<>();
                        for (int i = 0; i < entityPlayer.getRNG().nextInt(4) + 2; i++)
                        {
                            positions.add(new Vec3d(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
                        }
                        // Send the particle packet
                        AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(
                                new SyncParticle(AOTDParticleRegistry.ParticleTypes.SPELL_CAST_ID, positions, Collections.nCopies(positions.size(), Vec3d.ZERO)),
                                new NetworkRegistry.TargetPoint(entityPlayer.dimension, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 100));
                        // Tell the first delivery method to fire
                        this.getStage(0)
                                .getDeliveryMethod()
                                .executeDelivery(new DeliveryTransitionStateBuilder()
                                        .withSpell(this)
                                        .withStageIndex(0)
                                        .withEntity(entityPlayer)
                                        .build());
                    }
                    else
                    {
                        entityPlayer.sendMessage(new TextComponentTranslation(this.powerSource.getUnlocalizedOutOfPowerMsg()));
                    }
                }
                else
                {
                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.spell.invalid"));
                }
            }
            else
            {
                entityPlayer.sendMessage(new TextComponentTranslation("aotd.spell.wrong_dimension"));
            }
        }
    }

    /**
     * Returns true if this spell is valid, false otherwise
     *
     * @return True if the power source method is non-null and at least one spell stage is registered
     */
    public boolean isValid()
    {
        boolean isValid = this.powerSource != null;
        // Ensure the power source is valid and the spell stages are non-empty
        if (isValid && !this.spellStages.isEmpty())
        {
            // Test to ensure all spell stages are valid
            return this.spellStages.stream().allMatch(SpellStage::isValid);
        }
        return false;
    }

    /**
     * Gets the cost of the spell
     *
     * @return The cost of the spell including all spell stages
     */
    public double getCost()
    {
        double cost = 0;
        // Keep a multiplier that will make each spell stage more and more expensive
        double costMultiplier = 1.0;
        // Go over each spell stage and add up costs
        for (SpellStage spellStage : this.spellStages)
        {
            // Add the cost of the stage times the multiplier
            cost = cost + spellStage.getCost() * costMultiplier;
            // Increase the cost of the next spell stage by 5%
            costMultiplier = costMultiplier + 0.05;
        }
        // If cost overflowed then set it to max double
        if (cost < 0)
        {
            cost = Double.MAX_VALUE;
        }
        return cost;
    }

    /**
     * True if the spell has a given stage, false otherwise
     *
     * @param index The index of the stage to get
     * @return True if the stage exists, false otherwise
     */
    public boolean hasStage(int index)
    {
        return index >= 0 && index < this.spellStages.size();
    }

    /**
     * Gets the spell stage at a given index
     *
     * @param index The spell stage index
     * @return The spell stage at a given index or null if it doesn't exist
     */
    public SpellStage getStage(int index)
    {
        if (this.hasStage(index))
        {
            return this.spellStages.get(index);
        }
        else
        {
            return null;
        }
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell's data
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        // Write each field to NBT
        nbt.setString(NBT_NAME, this.name);
        nbt.setTag(NBT_ID, NBTUtil.createUUIDTag(this.id));
        nbt.setTag(NBT_OWNER_ID, NBTUtil.createUUIDTag(this.ownerId));
        // The spell power source can be null, double check that it isn't before writing it and its state
        if (this.powerSource != null)
        {
            nbt.setTag(NBT_POWER_SOURCE, this.powerSource.serializeNBT());
        }
        // Write each spell stage to NBT
        NBTTagList spellStagesNBT = new NBTTagList();
        this.spellStages.forEach(spellStage -> spellStagesNBT.appendTag(spellStage.serializeNBT()));
        nbt.setTag(NBT_SPELL_STAGES, spellStagesNBT);

        return nbt;
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        // Read each field from NBT
        this.name = nbt.getString(NBT_NAME);
        this.id = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_ID));
        this.ownerId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_OWNER_ID));
        // The spell power source can be null, double check that it exists before reading it and its state
        if (nbt.hasKey(NBT_POWER_SOURCE))
        {
            // Grab the power source NBT and create a power source out of it
            this.powerSource = SpellPowerSource.createFromNBT(nbt.getCompoundTag(NBT_POWER_SOURCE));
        }
        // Read each spell stage from NBT
        NBTTagList spellStagesNBT = nbt.getTagList(NBT_SPELL_STAGES, Constants.NBT.TAG_COMPOUND);
        this.spellStages = new ArrayList<>();
        for (int i = 0; i < spellStagesNBT.tagCount(); i++)
        {
            // Grab the spell stage NBT, read it into the spell stage, and add it
            NBTTagCompound spellStageNBT = spellStagesNBT.getCompoundTagAt(i);
            SpellStage spellStage = new SpellStage(spellStageNBT);
            this.spellStages.add(spellStage);
        }
    }

    /**
     * Gets the owner of the spell
     *
     * @return The player who owns the spell, or null if the player is offline
     */
    public EntityPlayer getOwner()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.ownerId);
    }

    ///
    /// Getters/Setters
    ///

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public UUID getId()
    {
        return this.id;
    }

    public void setPowerSource(SpellPowerSource powerSource)
    {
        this.powerSource = powerSource;
    }

    public SpellPowerSource getPowerSource()
    {
        return powerSource;
    }

    public List<SpellStage> getSpellStages()
    {
        return spellStages;
    }
}
