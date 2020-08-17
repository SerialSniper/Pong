package gui;

import java.awt.*;

import enums.Align.*;
import main.*;

public class DebugScreen {
	private boolean active = false;
	private Level level;
	
	public DebugScreen(Level level) {
		this.level = level;
	}
	
	public void toggleActive() {
		active = !active;
	}
	
	public void render() {
		if(active)
			level.getWindow().getFont().drawString(String.format("ball acceleration: %.4fpx/ms2\nball speed: %.4fpx/ms",
													level.getBall().getAcceleration(), level.getBall().getSpeed()),
					16, V.BOTTOM, H.RIGHT, Color.DARK_GRAY, Color.BLACK, 0, 0);
	}
}