package com.CorGaming.TankTactic;

import java.io.File;
import java.io.Serializable;

public class Settings implements Serializable
{
	private static final long serialVersionUID = 137264633580679552L;
	
	public static final String saveLoc = "Tanktactics" + File.separator + "CurrentLevel.dat";
	public static final String bestSaveLoc = "Tanktactics" + File.separator + "BestTimes.dat";
	public String inputMethod;
	public boolean sound;
	
	public Settings()
	{
		inputMethod = "DPad";
        sound = true;
	}
}