package io;

public interface InputCallback {
	public void onKeyPress(int key, int action);
	public void onKeyHeld(int key);
	public void onMousePress(int button, int action);
}