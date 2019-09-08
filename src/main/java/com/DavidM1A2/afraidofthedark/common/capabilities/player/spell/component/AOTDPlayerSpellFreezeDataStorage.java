package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Default storage implementation for the AOTD freeze spell effect data
 */
public class AOTDPlayerSpellFreezeDataStorage implements Capability.IStorage<IAOTDPlayerSpellFreezeData>
{
    // NBT constants used for serialization
    private static final String NBT_FREEZE_TICKS = "freeze_ticks";
    private static final String NBT_POSITION = "position";
    private static final String NBT_DIRECTION = "direction";

    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IAOTDPlayerSpellFreezeData> capability, IAOTDPlayerSpellFreezeData instance, EnumFacing side)
    {
        // Create a compound to write
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger(NBT_FREEZE_TICKS, instance.getFreezeTicks());

        nbt.setDouble(NBT_POSITION + "_x", instance.getFreezePosition().x);
        nbt.setDouble(NBT_POSITION + "_y", instance.getFreezePosition().y);
        nbt.setDouble(NBT_POSITION + "_z", instance.getFreezePosition().z);

        nbt.setFloat(NBT_DIRECTION + "_x", instance.getFreezeDirection().x);
        nbt.setFloat(NBT_DIRECTION + "_y", instance.getFreezeDirection().y);

        return nbt;
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    @Override
    public void readNBT(Capability<IAOTDPlayerSpellFreezeData> capability, IAOTDPlayerSpellFreezeData instance, EnumFacing side, NBTBase nbt)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt instanceof NBTTagCompound)
        {
            // The compound to read from
            NBTTagCompound compound = (NBTTagCompound) nbt;

            instance.setFreezeTicks(compound.getInteger(NBT_FREEZE_TICKS));
            instance.setFreezePosition(new Vec3d(
                    compound.getDouble(NBT_POSITION + "_x"),
                    compound.getDouble(NBT_POSITION + "_y"),
                    compound.getDouble(NBT_POSITION + "_z")));
            instance.setFreezeDirection(new Vec2f(
                    compound.getFloat(NBT_DIRECTION + "_x"),
                    compound.getFloat(NBT_DIRECTION + "_y")));
        }
        // There's an error, this should not be possible
        else
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
        }
    }
}
