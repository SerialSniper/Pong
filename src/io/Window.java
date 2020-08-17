package io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import gui.*;
import main.*;

public class Window implements InputCallback {
	private long window;
	private int x, y, width, height;
	private String title;
	private boolean fullscreen = false;
	private GLFWVidMode videoMode;
	
	private Font font;
	
	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		if(!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");
		
		videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		x = (videoMode.width() - width) / 2;
		y = (videoMode.height() - height) / 2;
	}
	
	public void create() {
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		if(fullscreen)
			window = glfwCreateWindow(videoMode.width(), videoMode.height(), title, glfwGetPrimaryMonitor(), 0);
		else
			window = glfwCreateWindow(width, height, title, 0, 0);
		
		if(window == 0)
			throw new IllegalStateException("Failed to create window");
		
		if(!fullscreen)
			glfwSetWindowPos(window, x, y);
		
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		orthoInit();
		
		setIcon();
		
		glfwShowWindow(window);
		
		Input.init(this);
		Input.addListener(this);

		font = new Font(this, "font");
	}
	
	private void setIcon() {
		ByteBuffer icon16, icon32;
		try {
		    icon16 = IOUtil.ioResourceToByteBuffer("res/icon16.png", 2048);
		    icon32 = IOUtil.ioResourceToByteBuffer("res/icon32.png", 4096);
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		
		IntBuffer w = memAllocInt(1);
		IntBuffer h = memAllocInt(1);
		IntBuffer comp = memAllocInt(1);
		
		try(GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
		    ByteBuffer pixels16 = stbi_load_from_memory(icon16, w, h, comp, 4);
		    icons
		        .position(0)
		        .width(w.get(0))
		        .height(h.get(0))
		        .pixels(pixels16);
		    
		    ByteBuffer pixels32 = stbi_load_from_memory(icon32, w, h, comp, 4);
		    icons
		        .position(1)
		        .width(w.get(0))
		        .height(h.get(0))
		        .pixels(pixels32);
		    
		    icons.position(0);
		    glfwSetWindowIcon(window, icons);
		    
		    stbi_image_free(pixels32);
		    stbi_image_free(pixels16);
		}
	}
	
	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallback() {
			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	private void orthoInit() {
		glViewport(0, 0, getWidth(), getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-getWidth() / 2, getWidth() / 2, -getHeight() / 2, getHeight() / 2, 0, 1);
	}
	
	public void switchFullscreen() {
		fullscreen = !fullscreen;

		if(fullscreen) {
			glViewport(0, 0, videoMode.width(), videoMode.height());
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), x, y, videoMode.width(), videoMode.height(), 60);
		} else {
			glViewport(0, 0, width, height);
			glfwSetWindowMonitor(window, 0, x, y, width, height, 60);
		}
	}
	
	public boolean shouldClose() { return glfwWindowShouldClose(window); }
	public void swapBuffers() { glfwSwapBuffers(window); }
	public void update() { glfwPollEvents(); }
	
	public long getWindow() { return window; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Font getFont() { return font; }
	
	@Override
	public void onKeyPress(int key, int action) {
		switch(key) {
			case GLFW_KEY_ESCAPE:
				System.exit(0);
				break;
				
			case GLFW_KEY_F11:
				switchFullscreen();
				break;
				
			case GLFW_KEY_F3:
				Pong.getInstance().getCurrentLevel().getDebugScreen().toggleActive();
		}
	}

	@Override
	public void onMousePress(int button, int action) {}

	@Override
	public void onKeyHeld(int key) {}
}