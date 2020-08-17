package models;

import main.*;
import templates.*;

public class RightScore extends Score {
	public RightScore(Level level) {
		super(level);
		player = level.getRightPaddle();
		setAABB(new AABB(distance, y, 50, 50));
	}
}