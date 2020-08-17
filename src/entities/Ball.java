package entities;

import io.*;
import main.*;
import templates.*;

public class Ball extends Entity {
	private float directionX, directionY;
	private static final float acceleration = 0.0001f;
	private float speed;
	private Paddle leftPaddle, rightPaddle;
	
	public Ball(Level level) {
		super(level);
		init();
		
		this.leftPaddle = level.getLeftPaddle();
		this.rightPaddle = level.getRightPaddle();
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getAcceleration() {
		return acceleration;
	}

	@Override
	public void init() {
		setAABB(new AABB(0, 0, 10, 10));
		speed = 0.5f;
	}
	
	@Override
	public void tick() {
		if(!getLevel().isStarted()) return;
		
		move(directionX * speed, directionY * speed);
		speed += acceleration;
		
		if(getCollision(rightPaddle).isColliding())
			bounce(rightPaddle);
		if(getCollision(leftPaddle).isColliding())
			bounce(leftPaddle);
		
		Collision c = getCollision(getLevel());
		if(c.isColliding()) {
			switch(c.getSecondEdge()) {
				case RIGHT:
					getLevel().goal(leftPaddle);
					break;
					
				case LEFT:
					getLevel().goal(rightPaddle);
					break;
					
				default:
					bounce();
					break;
			}
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private void bounce() {
		directionY *= -1;
		
		getLevel().getSFX().play("wall");
	}
	
	private void bounce(Paddle player) {
		float y = getAABB().getY();
		float playerY = player.getAABB().getY();
		int degrees = (int)(y - playerY);

		directionX = (float)Math.cos(degrees * Math.PI / 180);
		directionY = (float)Math.sin(degrees * Math.PI / 180);
		
		if(player instanceof RightPaddle)
			directionX *= -1;
		
		getLevel().getSFX().play("paddle");
	}
	
	public void start() {
		if(getLevel().isStarted()) return;
		getLevel().start();
		
		boolean left = (boolean)(Math.round(Math.random()) > 0);
		int degrees = (int)(Math.random() * 90) + (left ? 180 : 0) - 45;
		directionX = (float)Math.cos(degrees * Math.PI / 180);
		directionY = (float)Math.sin(degrees * Math.PI / 180);
	}
}