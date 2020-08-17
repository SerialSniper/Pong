package gui;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;

import javax.imageio.*;

import org.lwjgl.*;

import enums.*;
import io.Window;
import templates.*;

public class Font extends HashMap<Character, Texture> {
	private static final long serialVersionUID = 1L;
	private Window window;
	private String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
	
	public Font(Window window, String filename) {
		super();
		this.window = window;
		load(filename);
	}
	
	public void drawString(String string, int size, Align.V verticalAlign, Align.H horizontalAlign, Color fg, Color bg, int verticalPadding, int horizontalPadding) {
		int x = 0, y = 0;
		
		switch(verticalAlign) {
			case TOP:
				y = window.getHeight() / 2;
				break;
				
			case BOTTOM:
				y = -window.getHeight() / 2;
				break;
				
			case CENTER: break;
		}
		
		switch(horizontalAlign) {
			case LEFT:
				x = -window.getWidth() / 2;
				break;
				
			case RIGHT:
				x = window.getWidth() / 2;
				break;
				
			case CENTER: break;
		}
		
		drawString(string, x, y, size, verticalAlign, horizontalAlign, horizontalAlign, fg, bg, verticalPadding, horizontalPadding);
	}
	
	public void drawString(String string, int x, int y, int size, Align.V vertical, Align.H horizontal, Align.H text_horizontal, Color fg, Color bg, int v_padding, int h_padding) {
		String[] lines = string.split("\n");
		
		int height = lines.length * size;
		int width = 0;
		
		for(int i = 0; i < lines.length; i++)
			if(lines[i].length() > width)
				width = lines[i].length() * size;
		
		x += size / 2;
		y -= size / 2;
		
		int[] lines_width = new int[lines.length];
		int[] lines_x = new int[lines.length];
		
		for(int i = 0; i < lines.length; i++)
			lines_width[i] = lines[i].length() * size;
		
		switch(text_horizontal) {
			case LEFT: break;
			case RIGHT:
				for(int i = 0; i < lines_x.length; i++)
					lines_x[i] = x - lines_width[i];
				break;
				
			case CENTER:
				for(int i = 0; i < lines_x.length; i++)
					lines_x[i] = x - lines_width[i] / 2;
				break;
		}
		
		switch(vertical) {
			case TOP: break;
			case BOTTOM:
				y += height;
				break;
				
			case CENTER:
				y += height / 2;
				break;
		}
		
		switch(horizontal) {
			case LEFT: break;
			case RIGHT:
				x -= width;
				break;
				
			case CENTER:
				x -= width / 2;
				break;
		}
		
		new Model()
		.setAABB(new AABB(x - size / 2 + width / 2,
						   y + size / 2 - height / 2,
						   width + h_padding * 2,
						   height + v_padding * 2))
		.setColor(bg)
		.setSolid(Solid.NON_SOLID)
		.render();
		
		for(int i = 0; i < lines.length; i++)
			for(int j = 0; j < lines[i].length(); j++)
				new Model()
				.setAABB(new AABB(lines_x[i] + j * size, y - i * size, size, size))
				.setTexture(get(lines[i].charAt(j)))
				.setColor(fg)
				.setSolid(Solid.NON_SOLID)
				.render();
	}
	
	private void load(String filename) {
		try {
			BufferedImage bi;
			ByteBuffer pixels;
			int[] pixels_raw;
			int amount, width, height, rows, cols, size = 16;
			
			bi = ImageIO.read(this.getClass().getResourceAsStream("/res/textures/gui/fonts/" + filename + ".png"));
			
			width = bi.getWidth();
			height = bi.getHeight();
			amount = size * size * 4;
			rows = height / size;
			cols = width / size;
			
			int c = 0;
			for(int row = 0; row < rows; row++) {
				for(int col = 0; col < cols; col++, c++) {
					if(c >= chars.length())
						break;
					
					pixels_raw = bi.getRGB(col * size, row * size, size, size, null, 0, size);
					pixels = BufferUtils.createByteBuffer(amount);
					
					for(int y = 0; y < size; y++) {
						for(int x = 0; x < size; x++) {
							int pixel = pixels_raw[y * size + x];
							pixels.put((byte)((pixel >> 16) & 0xFF)); //red
							pixels.put((byte)((pixel >> 8) & 0xFF));  //green
							pixels.put((byte)(pixel & 0xFF));		  //blue
							pixels.put((byte)((pixel >> 24) & 0xFF)); //alpha
						}
					}
					
					pixels.flip();
					
					put(chars.charAt(c), new Texture(pixels, size, size));
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}