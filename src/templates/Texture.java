package templates;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	private int id, width, height;
	private ByteBuffer pixels;
	
	public Texture(String filename) {
		load(filename);
		glLoad();
	}
	
	public Texture(ByteBuffer pixels, int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		glLoad();
	}
	
	private void glLoad() {
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getPixels() {
		return pixels;
	}
	
	public void load(String filename) {
		try {
			BufferedImage bi;
			int amount;
			
			bi = ImageIO.read(this.getClass().getResourceAsStream("/res/textures/" + filename + ".png"));
			width = bi.getWidth();
			height = bi.getHeight();
			amount = width * height * 4;
			
			int[] pixels_raw = new int[amount];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			
			pixels = BufferUtils.createByteBuffer(amount);
			
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					int pixel = pixels_raw[y * width + x];
					pixels.put((byte)((pixel >> 16) & 0xFF)); //red
					pixels.put((byte)((pixel >> 8) & 0xFF));  //green
					pixels.put((byte)(pixel & 0xFF));		  //blue
					pixels.put((byte)((pixel >> 24) & 0xFF)); //alpha
				}
			}
			
			pixels.flip();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bind() {
		glEnable(GL_TEXTURE_2D);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		glDisable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}