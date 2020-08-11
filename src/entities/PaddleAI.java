package entities;

import models.Field;

public class PaddleAI extends Paddle {
	public PaddleAI(Field field) {
		super(field, Side.LEFT);
	}
	
	@Override
	public void onKeyHeld(int key) {}
}