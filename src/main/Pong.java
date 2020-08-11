package main;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import entities.Ball;
import entities.Paddle;
import entities.Paddle.Side;
import io.Input;
import io.Window;
import models.Field;
import render.Shader;

public class Pong extends Window implements Runnable {
	public static Pong instance;
	public static Shader shader;
	public static int scale = 64, gameLoopInterval = 1;

	Field field;
	Paddle player1;
	Paddle player2;
	Ball ball;
	
	public Pong() {
		super("Pong", 800, 450);
		setFullscreen(false);
		create();
		setCallbacks();
		
		instance = this;
		shader = new Shader("shader");
		
		field = new Field(getWidth(), getHeight());
		player1 = new Paddle(field, Side.RIGHT);
		player2 = new Paddle(field, Side.LEFT);
		ball = new Ball(field, player1, player2);
		
		new Thread(this).start();
		
		while(!shouldClose()) {
			super.update();
			glClear(GL_COLOR_BUFFER_BIT);
			
//			shader.bind();
//			shader.setUniform("sampler", 0);
			
			player1.render();
			player2.render();
			
			ball.render();
			
			super.swapBuffers();
		}
		
//		glfwTerminate();
		System.exit(0);
	}
	
	@Override
	public void run() {
		while(true) {
			ball.tick();
			Input.keyboard.keyLoop();
			
			try {
				Thread.sleep(gameLoopInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Pong getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		new Pong();
	}
}