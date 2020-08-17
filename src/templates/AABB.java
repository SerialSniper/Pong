package templates;

public class AABB {
	public enum Edge { TOP, BOTTOM, LEFT, RIGHT }
	private float x, y, initial_x, initial_y;
	private int width, height;
	
	public AABB(int x, int y, int width, int height) {
		this.x = (initial_x = x);
		this.y = (initial_y = y);
		this.width = width;
		this.height = height;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void resetPosition() {
		this.x = initial_x;
		this.y = initial_y;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public float getX() { return x; }
	public float getY() { return y; }

	public float getEdge(Edge edge) {
		switch(edge) {
			case TOP: return y + height/2;
			case BOTTOM: return y - height/2;
			case LEFT: return x - width/2;
			case RIGHT: return x + width/2;
			default: throw new IllegalStateException("Edge must be an enum value");
		}
	}
}