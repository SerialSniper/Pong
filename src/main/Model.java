package main;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import enums.Solid;
import main.Rect.Edge;
import render.Texture;

public class Model {
	private int draw_count, v_id, t_id, i_id;
	private Texture tex;
	private Rect rect;
	private Solid solid;
	
	public Model(Rect rect, Texture tex, Solid solid) {
		draw_count = tex.indices.length;
		this.tex = tex;
		this.rect = rect;
		this.solid = solid;
		
		t_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, t_id);
		glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(tex.coords), GL_STATIC_DRAW);
		
		i_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(tex.indices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		tex.bind(0);
		
		v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(rect.getVertexes()), GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, t_id);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
		glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);
		
		glDrawArrays(GL_TRIANGLES, 0, draw_count);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	
	public Rect getRect() { return rect; }
	
	public boolean collides(Model target) {
		if(solid == Solid.NON_SOLID || target.solid == Solid.NON_SOLID)
			return false;
		
		if(solid == Solid.SOLID && target.solid == Solid.SOLID)
			return (rect.getEdge(Edge.TOP) >= target.getRect().getEdge(Edge.BOTTOM)) &&
				   (rect.getEdge(Edge.BOTTOM) <= target.getRect().getEdge(Edge.TOP)) &&
				   (rect.getEdge(Edge.LEFT) <= target.getRect().getEdge(Edge.RIGHT)) &&
				   (rect.getEdge(Edge.RIGHT) >= target.getRect().getEdge(Edge.LEFT));
		
		if(solid == Solid.REVERSED_SOLID || target.solid == Solid.REVERSED_SOLID)
			return (rect.getEdge(Edge.TOP) >= target.getRect().getEdge(Edge.TOP)) ||
				   (rect.getEdge(Edge.BOTTOM) <= target.getRect().getEdge(Edge.BOTTOM)) ||
				   (rect.getEdge(Edge.LEFT) <= target.getRect().getEdge(Edge.LEFT)) ||
				   (rect.getEdge(Edge.RIGHT) >= target.getRect().getEdge(Edge.RIGHT));
		
		return false;
	}
	
	private FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}