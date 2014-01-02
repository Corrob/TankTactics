package com.CorGaming.TankTactic;

import com.CorGaming.framework.Screen;
import com.CorGaming.framework.impl.AndroidGame;

public class TankGame extends AndroidGame 
{
	private static final long serialVersionUID = -8410320842338994650L;

	@Override
	public Screen getStartScreen()
	{
		return new LoadingScreen(this);
	}
}
