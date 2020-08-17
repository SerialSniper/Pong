package entities;

import enums.*;
import main.*;

public class PaddleAI extends LeftPaddle {
	private Difficulty difficulty;
	private float[] history;
	
	public PaddleAI(Level level, Difficulty difficulty) {
		super(level);
		
		this.difficulty = difficulty;
		history = new float[this.difficulty.getValue()];
	}
	
	private void move_history() {
		for(int i = history.length - 1; i > 0; i--)
			history[i] = history[i - 1];
	}
	
	@Override
	public void init() {
		super.init();
		history = new float[this.difficulty.getValue()];
	}
	
	@Override
	public void tick() {
		getAABB().setY(history[history.length - 1]);
		move_history();
		history[0] = getLevel().getBall().getAABB().getY();
		
		super.tick();
	}
	
	@Override
	public void onKeyHeld(int key) {}
}