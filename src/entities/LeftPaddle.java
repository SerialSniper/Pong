package entities;

import enums.*;
import main.*;
import templates.*;

public class LeftPaddle extends Paddle {
	public LeftPaddle(Level level) {
		super(level);
		
		int x = -(level.getWindow().getWidth() / 2 - distance);
		setAABB(new AABB(x, 0, 20, 100));
		
		controls = Controls.WASD;
	}
}