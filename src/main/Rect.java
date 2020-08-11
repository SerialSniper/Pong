package main;

public class Rect {
	public enum Edge { TOP, BOTTOM, LEFT, RIGHT }
	public enum Vertex { TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT }
	private float x, y;
	private int width, height;
	
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public float getFloatX() { return x; }
	public float getFloatY() { return y; }
	
	public int getEdge(Edge edge) {
		switch(edge) {
			case TOP: return (int)(y + height/2);
			case BOTTOM: return (int)(y - height/2);
			case LEFT: return (int)(x - width/2);
			case RIGHT: return (int)(x + width/2);
			default: throw new IllegalStateException("Edge must be an enum value");
		}
	}

	public float getFloatEdge(Edge edge) {
		switch(edge) {
			case TOP: return y + height/2;
			case BOTTOM: return y - height/2;
			case LEFT: return x - width/2;
			case RIGHT: return x + width/2;
			default: throw new IllegalStateException("Edge must be an enum value");
		}
	}
	
	public int[] getEdges() {
		return new int[] {
			getEdge(Edge.TOP),
			getEdge(Edge.BOTTOM),
			getEdge(Edge.LEFT),
			getEdge(Edge.RIGHT)
		};
	}
	
	public float[] getVertexes() {
		return new float[] {
			getFloatEdge(Edge.LEFT), getFloatEdge(Edge.TOP),
			getFloatEdge(Edge.RIGHT), getFloatEdge(Edge.TOP),
			getFloatEdge(Edge.RIGHT), getFloatEdge(Edge.BOTTOM),
			getFloatEdge(Edge.LEFT), getFloatEdge(Edge.BOTTOM)
		};
	}
	
//	public Point getVertex(Vertex vertex) {
//		switch(vertex) {
//			case TOP_LEFT: return new Point(getEdge(Edge.LEFT), getEdge(Edge.TOP));
//			case TOP_RIGHT: return new Point(getEdge(Edge.RIGHT), getEdge(Edge.TOP));
//			case BOTTOM_RIGHT: return new Point(getEdge(Edge.RIGHT), getEdge(Edge.BOTTOM));
//			case BOTTOM_LEFT: return new Point(getEdge(Edge.LEFT), getEdge(Edge.BOTTOM));
//			default: throw new IllegalStateException("Vertex must be an enum value");
//		}
//	}
}