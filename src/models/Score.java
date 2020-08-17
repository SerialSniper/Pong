package models;

import java.awt.*;

import entities.*;
import enums.*;
import main.*;
import templates.*;

public class Score extends Model {
	protected int distance = 50;
	protected int y;
	private Level level;
	protected Paddle player;
	
	public Score(Level level) {
		setColor(Color.LIGHT_GRAY);
		setTexture(level.getWindow().getFont().get('0'));
		setSolid(Solid.NON_SOLID);
		
		this.level = level;
		
		y = level.getWindow().getHeight() / 2 - distance * 2;
	}
	
	public void update() {
		super.setTexture(level.getWindow().getFont().get((char)(player.getScore() + 48)));
	}
}