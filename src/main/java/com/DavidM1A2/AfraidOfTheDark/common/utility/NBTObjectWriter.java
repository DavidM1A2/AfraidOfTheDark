/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import net.minecraft.nbt.NBTTagCompound;

public class NBTObjectWriter
{
	public static void writeObjectToNBT(String key, Object object, NBTTagCompound compound)
	{
		byte[] value = objectToByteArray(object);

		if (value == null)
		{
			return;
		}

		if (!compound.hasKey("StoredObjects"))
		{
			compound.setTag("StoredObjects", new NBTTagCompound());
		}

		NBTTagCompound objects = compound.getCompoundTag("StoredObjects");
		objects.setByteArray(key, value);
	}

	public static byte[] objectToByteArray(Object object)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = null;
		byte[] value = null;

		try
		{
			objectOutput = new ObjectOutputStream(byteArrayOutputStream);
			objectOutput.writeObject(object);
			value = byteArrayOutputStream.toByteArray();
		}
		catch (IOException exception)
		{
			LogHelper.info("Error in object serialization: " + exception.getMessage());
		}
		finally
		{
			if (objectOutput != null)
			{
				try
				{
					objectOutput.close();
				}
				catch (IOException e)
				{
				}
			}
			if (byteArrayOutputStream != null)
			{
				try
				{
					byteArrayOutputStream.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return value;
	}

	public static Object readObjectFromNBT(String key, NBTTagCompound compound)
	{
		if (!compound.hasKey("StoredObjects"))
		{
			compound.setTag("StoredObjects", new NBTTagCompound());
		}

		NBTTagCompound objects = compound.getCompoundTag("StoredObjects");

		if (!objects.hasKey(key))
		{
			return null;
		}

		return byteArrayToObject(objects.getByteArray(key));
	}

	public static Object byteArrayToObject(byte[] bytes)
	{
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		ObjectInput objectInput = null;
		Object toReturn = null;

		try
		{
			objectInput = new ObjectInputStream(byteArrayInputStream);
			toReturn = objectInput.readObject();
		}
		catch (IOException exception)
		{
			LogHelper.info("Error in object serialization: " + exception.getMessage());
		}
		catch (ClassNotFoundException exception)
		{
		}
		finally
		{
			if (objectInput != null)
			{
				try
				{
					objectInput.close();
				}
				catch (IOException exception)
				{
				}
			}
			if (byteArrayInputStream != null)
			{
				try
				{
					byteArrayInputStream.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return toReturn;
	}
}
