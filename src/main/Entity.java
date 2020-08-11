package main;

import enums.Direction;
import enums.Solid;
import render.Texture;

public class Entity extends Model {
	public Entity(Rect rect, Texture tex, Solid solid) {
		super(rect, tex, solid);
	}
	
	public void move(Direction direction, int amount) {
		switch(direction) {
			case NORTH:
				getRect().setY(getRect().getY() + amount);
				break;
				
			case SOUTH:
				getRect().setY(getRect().getY() - amount);
				break;
				
			case EAST:
				getRect().setX(getRect().getX() + amount);
				break;
				
			case WEST:
				getRect().setX(getRect().getX() - amount);
				break;
		}
	}

	public void move(float x, float y) {
		getRect().setX(getRect().getFloatX() + x);
		getRect().setY(getRect().getFloatY() + y);
	}
}