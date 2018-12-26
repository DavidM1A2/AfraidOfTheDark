package com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;

public class MCAVersionChecker
{
	public static final int VersionID = 5;

	/** Checks for the right version of the library. Should be called by each model class. */
	public static void checkForLibraryVersion(Class modelClass, int modelVersion)
	{
		// Added constants.isDebug
		if (modelVersion > VersionID && ConfigurationHandler.debugMessages)
		{
			System.out.println("MCA WARNING: " + modelClass.getName() + " needs a newer version of the library (" + modelVersion + "). Things could go wrong!");
		}
	}
}
