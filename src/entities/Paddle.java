package entities;

import enums.*;
import io.*;
import main.*;
import templates.*;
import templates.AABB.*;

public class Paddle extends Entity implements InputCallback {
	protected Controls controls;
	protected int distance = 50;
	
	private int speed = 1;
	private int score = 0;
	
	public Paddle(Level level) {
		super(level);
		Input.addListener(this);
	}
	
	@Override
	public void init() {
		getAABB().resetPosition();
	}
	
	@Override
	public void tick() {
		Collision c = getCollision(getLevel());
		if(c.isColliding()) {
			int y = Pong.getInstance().getHeight() / 2 - getAABB().getHeight() / 2;
			
			if(c.getFirstEdge() == Edge.BOTTOM)
				y *= -1;
			
			getAABB().setY(y);
		}
	}
	
	@Override
	public void destroy() {
		
	}
	
	public void incrementScore() {
		score++;
	}
	
	public int getScore() { return score; }
	public int getDistance() { return distance; }
	
	@Override
	public void onKeyPress(int key, int action) {}

	@Override
	public void onMousePress(int button, int action) {}

	@Override
	public void onKeyHeld(int key) {
		if(key == controls.getKey(Controls.UP)) {
			move(Direction.NORTH, speed);
			getLevel().getBall().start();
		} else if(key == controls.getKey(Controls.DOWN)) {
			move(Direction.SOUTH, speed);
			getLevel().getBall().start();
		}
	}
}