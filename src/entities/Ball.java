package entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import entities.Paddle.Side;
import enums.Solid;
import io.Input;
import io.InputCallback;
import main.Entity;
import main.Rect;
import models.Field;
import render.Texture;

public class Ball extends Entity implements InputCallback {
	private boolean started;
	private float directionX, directionY;
	private static float speed = 0.5f;
	private Field field;
	private Paddle player1, player2;
	
	public Ball(Field field, Paddle player1, Paddle player2) {
		super(new Rect(0, 0, 10, 10), new Texture("paddle"), Solid.SOLID);
		
		this.field = field;
		this.player1 = player1;
		this.player2 = player2;
		this.started = false;
		
		Input.addListener(this);
	}
	
	public void tick() {
		if(!started) return;
		
		move(directionX * speed, directionY * speed);
		
		if(collides(player1))
			bounce(player1);
		if(collides(player2))
			bounce(player2);
		if(collides(field))
			bounce();
	}
	
	private void bounce() {
		directionY *= -1;
	}
	
	private void bounce(Paddle player) {
		int y = getRect().getY();
		int playerY = player.getRect().getY();
		int degrees = (y - playerY);

		directionX = (float)Math.cos(degrees * Math.PI / 180);
		directionY = (float)Math.sin(degrees * Math.PI / 180);
		
		if(player.side == Side.RIGHT)
			directionX *= -1;
	}
	
	private void start() {
		if(started) return;
		started = true;
		
		boolean left = (boolean)(Math.round(Math.random()) > 0);
		int degrees = (int)(Math.random() * 90) + (left ? 180 : 0) - 45;
		directionX = (float)Math.cos(degrees * Math.PI / 180);
		directionY = (float)Math.sin(degrees * Math.PI / 180);
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	public void onKeyPress(int key, int action) {
		if(key == GLFW_KEY_UP || key == GLFW_KEY_DOWN || key == GLFW_KEY_W || key == GLFW_KEY_S)
			start();
		if(key == GLFW_KEY_R) {
			started = false;
			getRect().setX(0);
			getRect().setY(0);
			start();
		}
	}

	@Override
	public void onMousePress(int button, int action) {}

	@Override
	public void onKeyHeld(int key) {}
}