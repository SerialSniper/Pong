package entities;

import enums.Difficulty;
import main.Pong;
import models.Field;

public class PaddleAI extends Paddle {
	private Difficulty difficulty;
	private int[] history;
	
	public PaddleAI(Field field, Difficulty difficulty) {
		super(field, Side.LEFT);
		this.difficulty = difficulty;
		history = new int[this.difficulty.getValue()];
	}
	
	private void move_history() {
		for(int i = history.length - 1; i > 0; i--)
			history[i] = history[i - 1];
	}
	
	public void tick() {
		getRect().setY(history[history.length - 1]);
		move_history();
		history[0] = Pong.getInstance().ball.getRect().getY();
		
		super.tick();
	}
	
	@Override
	public void onKeyHeld(int key) {}
}