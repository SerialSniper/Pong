package models;

import main.*;

public class Scoreboard {
	private Score left, right;
	
	public Scoreboard(Level level) {
		left = new LeftScore(level);
		right = new RightScore(level);
	}
	
	public void update() {
		left.update();
		right.update();
	}
	
	public void render() {
		getLeft().render();
		getRight().render();
	}
	
	public Score getLeft() { return left; }
	public Score getRight() { return right; }
}