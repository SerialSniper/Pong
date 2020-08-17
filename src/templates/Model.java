package templates;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import entities.*;
import enums.*;
import io.*;
import templates.AABB.*;

public class Model {
    private AABB boundingBox;
    private Texture texture;
    private Color color = Color.WHITE;
    private Solid solid = Solid.SOLID;
    
	public Model setAABB(AABB bounding_box) {
		this.boundingBox = bounding_box;
		return this;
	}
	
	public Model setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}
	
	public Model setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Model setSolid(Solid solid) {
		this.solid = solid;
		return this;
	}
	
    
	public void render() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glColor3ub((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
		
		if(texture != null)
			texture.bind();
		
		glBegin(GL_QUADS);
			glTexCoord2i(0, 0);
			glVertex2f(boundingBox.getEdge(Edge.LEFT), boundingBox.getEdge(Edge.TOP));
			glTexCoord2i(1, 0);
			glVertex2f(boundingBox.getEdge(Edge.RIGHT), boundingBox.getEdge(Edge.TOP));
			glTexCoord2i(1, 1);
			glVertex2f(boundingBox.getEdge(Edge.RIGHT), boundingBox.getEdge(Edge.BOTTOM));
			glTexCoord2i(0, 1);
			glVertex2f(boundingBox.getEdge(Edge.LEFT), boundingBox.getEdge(Edge.BOTTOM));
		glEnd();
		
		if(texture != null)
			texture.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public boolean collide(Model target) {
		getCollision(target);
		
		if(solid == Solid.SOLID && target.solid == Solid.SOLID)
			return (boundingBox.getEdge(Edge.TOP) >= target.getAABB().getEdge(Edge.BOTTOM)) &&
				   (boundingBox.getEdge(Edge.BOTTOM) <= target.getAABB().getEdge(Edge.TOP)) &&
				   (boundingBox.getEdge(Edge.LEFT) <= target.getAABB().getEdge(Edge.RIGHT)) &&
				   (boundingBox.getEdge(Edge.RIGHT) >= target.getAABB().getEdge(Edge.LEFT));
		
		if(target.solid == Solid.REVERSED_SOLID)
			return (boundingBox.getEdge(Edge.TOP) >= target.getAABB().getEdge(Edge.TOP)) ||
				   (boundingBox.getEdge(Edge.BOTTOM) <= target.getAABB().getEdge(Edge.BOTTOM)) ||
				   (boundingBox.getEdge(Edge.LEFT) <= target.getAABB().getEdge(Edge.LEFT)) ||
				   (boundingBox.getEdge(Edge.RIGHT) >= target.getAABB().getEdge(Edge.RIGHT));
		
		return false;
	}
	
	public Collision getCollision(Model target) {
		Collision collision = new Collision();

		float depth[] = new float[4];
		boolean collides;
		int indexMin = 0;
		float min = Float.MAX_VALUE;
		Edge firstEdge = null, secondEdge = null;
		
		if(target.solid == Solid.REVERSED_SOLID)
			collides = false;
		else
			collides = true;
		
		depth[0] = boundingBox.getEdge(Edge.TOP) - target.getAABB().getEdge(Edge.BOTTOM);
		depth[1] = target.getAABB().getEdge(Edge.TOP) - boundingBox.getEdge(Edge.BOTTOM);
		depth[2] = target.getAABB().getEdge(Edge.RIGHT) - boundingBox.getEdge(Edge.LEFT);
		depth[3] = boundingBox.getEdge(Edge.RIGHT) - target.getAABB().getEdge(Edge.LEFT);
		
		if(target.solid == Solid.REVERSED_SOLID) {
			depth[0] = boundingBox.getEdge(Edge.TOP) - target.getAABB().getEdge(Edge.TOP);
			depth[1] = target.getAABB().getEdge(Edge.BOTTOM) - boundingBox.getEdge(Edge.BOTTOM);
			depth[2] = target.getAABB().getEdge(Edge.LEFT) - boundingBox.getEdge(Edge.LEFT);
			depth[3] = boundingBox.getEdge(Edge.RIGHT) - target.getAABB().getEdge(Edge.RIGHT);
		}
		
		for(int i = 0; i < 4; i++) {
			if(target.solid == Solid.REVERSED_SOLID)
				collides |= depth[i] >= 0;
			else
				collides &= depth[i] >= 0;
			
			if(depth[i] < min && depth[i] >= 0) {
				min = depth[i];
				indexMin = i;
			}
		}
		
		switch(indexMin) {
			case 0:
				firstEdge = Edge.TOP;
				secondEdge = Edge.BOTTOM;
				break;
				
			case 1:
				firstEdge = Edge.BOTTOM;
				secondEdge = Edge.TOP;
				break;

			case 2:
				firstEdge = Edge.LEFT;
				secondEdge = Edge.RIGHT;
				break;

			case 3:
				firstEdge = Edge.RIGHT;
				secondEdge = Edge.LEFT;
				break;
		}
		
		if(target.solid == Solid.REVERSED_SOLID) {
			switch(indexMin) {
				case 0:
					firstEdge = Edge.TOP;
					secondEdge = Edge.TOP;
					break;
					
				case 1:
					firstEdge = Edge.BOTTOM;
					secondEdge = Edge.BOTTOM;
					break;
	
				case 2:
					firstEdge = Edge.LEFT;
					secondEdge = Edge.LEFT;
					break;
	
				case 3:
					firstEdge = Edge.RIGHT;
					secondEdge = Edge.RIGHT;
					break;
			}
		}
		
		collision
		.setCollision(collides)
		.setFirst(this, firstEdge)
		.setSecond(target, secondEdge);
		
		return collision;
	}
	
	public AABB getAABB() { return boundingBox; }
	public Texture getTexture() { return texture; }
	public Color getColor() { return color; }
	public Solid getSolid() { return solid; }
}