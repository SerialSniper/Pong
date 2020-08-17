package templates;

import enums.*;
import main.*;

public abstract class Entity extends Model {
	private Level level;
	
	public Entity(Level level) {
		this.level = level;
	}
	
	public void move(Direction direction, float amount) {
		float x = 0, y = 0;
		
		switch(direction) {
			case NORTH: y = amount; break;
			case SOUTH: y = -amount; break;
			case EAST: x = amount; break;
			case WEST: x = -amount; break;
		}
		
		move(x, y);
	}

	public abstract void init();
	public abstract void tick();
	public abstract void destroy();

	public void move(float x, float y) {
		getAABB().setX(getAABB().getX() + x);
		getAABB().setY(getAABB().getY() + y);
	}
	
	public Level getLevel() { return level; }
}