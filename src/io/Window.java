package io;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window implements InputCallback {
	private long window;
	private int x, y, width, height;
	private String title;
	private boolean fullscreen = false;
	private GLFWVidMode videoMode;
	
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
		glEnable(GL_TEXTURE_2D);
		
		orthoInit();
		
		glfwShowWindow(window);
		
		Input.init(this);
		Input.addListener(this);
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
	
	@Override
	public void onKeyPress(int key, int action) {
		switch(key) {
			case GLFW_KEY_ESCAPE:
				System.exit(0);
				break;
				
			case GLFW_KEY_F11:
				switchFullscreen();
				break;
		}
	}

	@Override
	public void onMousePress(int button, int action) {}

	@Override
	public void onKeyHeld(int key) {}
}