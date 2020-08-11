package enums;

import static org.lwjgl.glfw.GLFW.*;

public enum Controls {
	UP, DOWN, LEFT, RIGHT,
	WASD(GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D),
	UDLR(GLFW_KEY_UP, GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT);
	
	private int[] controls;
	
	private Controls() {}
	
	private Controls(int up, int down, int left, int right) {
		this.controls = new int[] { up, down, left, right };
	}
	
	public int getKey(Controls control) {
		switch(control) {
			case UP: return controls[0];
			case DOWN: return controls[1];
			case LEFT: return controls[2];
			case RIGHT: return controls[3];
			default: throw new IllegalStateException("Controls must be an enum");
		}
	}
}