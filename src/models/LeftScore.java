package models;

import main.*;
import templates.*;

public class LeftScore extends Score {
	public LeftScore(Level level) {
		super(level);
		player = level.getLeftPaddle();
		setAABB(new AABB(-distance, y, 50, 50));
	}
}