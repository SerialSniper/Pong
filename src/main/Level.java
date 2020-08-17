package main;

import java.awt.*;

import entities.*;
import enums.*;
import gui.*;
import io.*;
import io.Window;
import io.sound.*;
import models.*;
import templates.*;

public class Level extends Model {
	private Window window;
	private DebugScreen debug;
	private Scoreboard scoreboard;
	private SoundCollection sfx;

	private boolean started;
	
	private RightPaddle player;
	private PaddleAI playerAI;
	private Ball ball;
	
	public Level(Window window) {
		setAABB(new AABB(0, 0, window.getWidth(), window.getHeight()));
		setColor(Color.BLACK);
		setSolid(Solid.REVERSED_SOLID);
		
		this.window = window;
		this.started = false;
		
		sfx = new SoundCollection()
				.add("paddle")
				.add("goal")
				.add("wall");
		
		debug = new DebugScreen(this);
		
		player = new RightPaddle(this);
		playerAI = new PaddleAI(this, Difficulty.NORMAL);

		scoreboard = new Scoreboard(this);
		
		ball = new Ball(this);
	}
	
	@Override
	public void render() {
		super.render();
		
		debug.render();
		scoreboard.render();
		
		player.render();
		playerAI.render();
		ball.render();
	}
	
	public void init() {
		
	}
	
	public void start() {
		if(started) return;
		started = true;
		
		ball.start();
	}
	
	public void stop() {
		if(!started) return;
		started = false;
	}
	
	public void tick() {
		player.tick();
		playerAI.tick();
		ball.tick();
	}
	
	public void goal(Paddle paddle) {
		ball.init();
		player.init();
		playerAI.init();
		stop();
		
		paddle.incrementScore();
		scoreboard.update();
		
		sfx.play("goal");
	}
	
	public void destroy() {
		sfx.destroy();
		ball.destroy();
		player.destroy();
		playerAI.destroy();
	}
	
	public Window getWindow() { return window; }
	public DebugScreen getDebugScreen() { return debug; }
	public Scoreboard getScoreboard() { return scoreboard; }
	public SoundCollection getSFX() { return sfx; }
	
	public boolean isStarted() { return started; }
	
	public Paddle getRightPaddle() { return player; }
	public Paddle getLeftPaddle() { return playerAI; }
	public Ball getBall() { return ball; }
}