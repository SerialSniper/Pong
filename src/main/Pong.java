package main;

import static org.lwjgl.opengl.GL11.*;

import enums.*;
import io.*;

public class Pong extends Window implements Runnable {
	public static Pong instance;
	private int gameLoopInterval = 1;
	Level level;
	
	public Pong(Difficulty difficulty) {
		super("Pong", 800, 450);
		setFullscreen(false);
		create();
		setCallbacks();
		
		new Discord().updatePresence();
		
		level = new Level(this);
		instance = this;
		
		new Thread(this).start();
		
		while(!shouldClose()) {
			update();
			glClear(GL_COLOR_BUFFER_BIT);
			
			level.render();
			
			swapBuffers();
		}
		
//		glfwTerminate();
		level.destroy();
		System.exit(0);
	}
	
	@Override
	public void run() {
		while(true) {
			level.tick();
			Input.keyboard.keyLoop();
			
			try {
				Thread.sleep(gameLoopInterval);
			} catch (InterruptedException e) {}
		}
	}
	
	public static Pong getInstance() { return instance; }
	public Level getCurrentLevel() { return level; }
	
	public static void main(String[] args) {
		Difficulty diff;
		if(args.length > 0 && (diff = Difficulty.valueOf(args[0].toUpperCase())) != null)
			new Pong(diff);
		else
			new Pong(Difficulty.NORMAL);
	}
}