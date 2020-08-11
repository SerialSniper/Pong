package io;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Input {
	public static Window window;
	public static Keyboard keyboard;
	public static Mouse mouse;
	
	private static List<InputCallback> listeners;
	
	public static class Keyboard extends GLFWKeyCallback {
		private boolean[] keys;
		
		public Keyboard() {
			keys = new boolean[GLFW_KEY_LAST];
			glfwSetKeyCallback(window.getWindow(), this);
		}
		
		public boolean isKeyDown(int key) {
			return glfwGetKey(window.getWindow(), key) == 1;
		}
		
		public void keyLoop() {
			for(int i = 0; i < keys.length; i++) {
				if(keys[i] == true) {
					final int j = i;
					listeners.forEach(x -> x.onKeyHeld(j));
				}
			}
		}
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			keys[key] = action == 1 || action == 2;
			if(action == GLFW_PRESS)
				listeners.forEach(x -> x.onKeyPress(key, action));
		}
	}
	
	public static class Mouse extends GLFWMouseButtonCallback {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		
		public Mouse() {
			glfwSetMouseButtonCallback(window.getWindow(), this);
		}
		
		public boolean isButtonDown(int button) {
			return glfwGetMouseButton(window.getWindow(), button) == 1;
		}
		
		public double[] getMousePos() {
			glfwGetCursorPos(window.getWindow(), x, y);
			return new double[] {x.get(0), y.get(0)};
		}
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			listeners.forEach(x -> x.onMousePress(button, action));
		}
	}
	
	public static void addListener(InputCallback listener) {
		listeners.add(listener);
	}
	
	static void init(Window window) {
		Input.window = window;
		keyboard = new Keyboard();
		mouse = new Mouse();
		listeners = new ArrayList<InputCallback>();
	}
}