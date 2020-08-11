package entities;

import enums.Controls;
import enums.Direction;
import enums.Solid;
import io.Input;
import io.InputCallback;
import main.Entity;
import main.Pong;
import main.Rect;
import models.Field;
import render.Texture;

public class Paddle extends Entity implements InputCallback {
	private static int distance = 50;
	private static int speed = 1;
	private Field field;
	
	public static enum Side {
		RIGHT(Pong.getInstance().getWidth() / 2 - distance, Controls.UDLR),
		LEFT(-(Pong.getInstance().getWidth() / 2 - distance), Controls.WASD);
		
		private int x;
		private Controls controls;
		
		private Side(int x, Controls controls) {
			this.x = x;
			this.controls = controls;
		}
		
		public int getX() {
			return x;
		}
		
		public Controls getControls() {
			return controls;
		}
	}
	
	Side side;
	
	public Paddle(Field field, Side side) {
		super(new Rect(side.getX(), 0, 20, 100), new Texture("paddle"), Solid.SOLID);
		
		this.field = field;
		this.side = side;
		
		Input.addListener(this);
	}

	@Override
	public void onKeyPress(int key, int action) {}

	@Override
	public void onMousePress(int button, int action) {}

	@Override
	public void onKeyHeld(int key) {
		if(key == side.getControls().getKey(Controls.UP))
			move(Direction.NORTH, speed);
		else if(key == side.getControls().getKey(Controls.DOWN))
			move(Direction.SOUTH, speed);
		
		if(collides(field)) {
			int y = Pong.getInstance().getHeight() / 2 - getRect().getHeight() / 2;
			
			if(Math.round(getRect().getY()) < 0)
				y *= -1;
			
			getRect().setY(y);
		}
	}
}