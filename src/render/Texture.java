package render;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
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
	private int id, width, height, amount;
	public float[] coords = new float[] {
		0, 0,
		1, 0,
		1, 1,
		0, 1
	};
	public int[] indices = new int[] {
		0, 1, 2,
		2, 3, 0
	};
	
	public Texture(String filename) {
		BufferedImage bi;
		
		try {
			bi = ImageIO.read(this.getClass().getResourceAsStream("/res/textures/" + filename + ".png"));
			width = bi.getWidth();
			height = bi.getHeight();
			amount = width * height * 4;
			
			int[] pixels_raw = new int[amount];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(amount);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int pixel = pixels_raw[i * width + j];
					pixels.put((byte)((pixel >> 16) & 0xFF)); //red
					pixels.put((byte)((pixel >> 8) & 0xFF));  //green
					pixels.put((byte)(pixel & 0xFF));		  //blue
					pixels.put((byte)((pixel >> 24) & 0xFF)); //alpha
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bind(int sampler) {
		if(sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
}