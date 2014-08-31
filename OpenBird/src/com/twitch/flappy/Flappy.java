package com.twitch.flappy;

import input.Input;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.twitch.flappy.graphics.Screen;
import com.twitch.flappy.graphics.Sprite;
import com.twitch.flappy.mobs.Bird;
import com.twitch.flappy.mobs.Goal;
import com.twitch.flappy.mobs.Obstacle;
import com.twitch.flappy.sound.Sound;
/**
 * Main Class and and the game loop.
 * @author Muriology
 *
 */
public class Flappy extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 380;
	public static final int  HEIGHT = 500;
	
	public static final String TITLE = "OPEN BIRD";
	
	public JFrame frame;
	
	public Thread thread;
	
	public boolean running;
	
	private Screen screen;
	private Input input;
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	private Bird bird;
	
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Goal> goals;
	
	public long seconds;
	
	private int spawnRate;
	private long nextSpawn;
	
	public int numGoals,bestScore;
	

	
	public static String GAMEOVER_MESSAGE = "GAME OVER";
	
	Random radom;
	
	public boolean gameOver,begin;
	
	public Flappy(){
		
		Dimension dim = new Dimension(WIDTH,HEIGHT);
		this.setPreferredSize(dim);
		
		this.screen = new Screen(WIDTH,HEIGHT);
		this.input = new Input();
		
		this.frame = new JFrame();
		
		Sprite sprites[] = new Sprite[3];
		sprites[0] = Sprite.flappy;
		sprites[1] = Sprite.flappy2;
		sprites[2] = Sprite.flappy3;
		this.bird = new Bird(80,(HEIGHT-50)/2,39,25,sprites,this);
		
		this.obstacles = new ArrayList<Obstacle>();
		this.goals = new ArrayList<Goal>();
		
		this.spawnRate = 1;
		this.nextSpawn = 0;
		
		this.radom = new Random();
		
		this.numGoals = 0;
		this.bestScore = 0;
		
		this.gameOver = true;
		this.begin  = false;
		
		
		addKeyListener(input);
		
	}
	/**
	 * Starts our main thread.
	 */
	public synchronized void start(){
		this.running = true;
		this.thread = new Thread(this,"Game");
		thread.start();

	}
	/**
	 * Stops our main thread
	 */
	public synchronized void stop(){
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is the responsable of the synchronization of all the game in 60fps and the render.
	 */
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0/60;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while(running){
			long now = System.nanoTime();
			delta += (now-lastTime) /ns;
			lastTime = now;
			while(delta >= 1){//60 times per sec
				tick();
				updates++;
				delta--;
			}
			render();//as fast as i can
			frames ++;
			if(System.currentTimeMillis() -timer > 1000){
				timer += 1000;
				System.out.println("Fps:"+frames+" Updates:"+updates);
				updates = 0;
				frames = 0;
				this.seconds++;
			}
		}
		
	}
	
	/**
	 * Spawns an obstacle filled with the desired color (int)
	 */
	public void spawnObstacle(){
		if(this.seconds > this.nextSpawn && !this.gameOver){
			int pX = this.radom.nextInt(350);
			this.obstacles.add(new Obstacle(WIDTH,0,30,pX,0));
			this.obstacles.add(new Obstacle(WIDTH,pX+110,30,HEIGHT-pX-111,0));
			this.goals.add(new Goal(WIDTH,pX,30,110,0xffffff));
			this.nextSpawn = this.seconds + this.spawnRate;
		}
	}
	
	/**
	 * The main update method, all update methods must be here.
	 */
	public void tick(){
		input.tick();
		
		if(!this.gameOver){
			this.updateGame();
		}else{
			if(input.down){
				bird.x = 80;
				bird.y = (HEIGHT-50)/2;
				this.numGoals = 0;
				this.obstacles.clear();
				this.goals.clear();
				this.gameOver = false;
				this.begin = true;
			}
			
		}
	}
	
	/**
	 * When the game is not over this updates all the elements of the game
	 */
	public void updateGame(){
		bird.tick(input);
		this.spawnObstacle();
		if(this.obstacles.size() > 0 && this.goals.size() > 0){
			for(int i=0;i<this.obstacles.size();i++){
				Obstacle obs = obstacles.get(i);
				obs.tick();
				if(this.bird.onCollision(obs.x, obs.y, obs.w,obs.h)){
					this.gameOver = true;
					Sound.dead.play();
				}
			}
			for(int i=0;i<this.goals.size();i++){
				Goal goal = goals.get(i);
				goal.tick();
				if(this.bird.onCollision(goal.x, goal.y, goal.w,goal.h) && goal.active){
					Sound.clink.play();
					this.numGoals++;
					if(this.numGoals > this.bestScore) this.bestScore = this.numGoals;
					goal.active = false;
					System.out.println(this.numGoals);
				}

			}
		}
	}
	
	
	/**
	 * Renders all our game pixel by pixel.
	 */
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		this.screen.renderBackground();
		
		
		for(int i=0;i<this.obstacles.size();i++){
			obstacles.get(i).render(screen);
		}
		
		this.screen.renderGround();
		
		
		this.bird.render(screen);

		screen.renderLeftLim();
		
		for(int i= 0;i<this.pixels.length;i++){
			this.pixels[i] = this.screen.getPixel(i);
		}
	

		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana",0,30));
		g.drawString(""+this.numGoals, 300, 60);
		if(this.gameOver){
			if(this.begin){
				g.setFont(new Font("Verdana",0,50));
				g.drawString(GAMEOVER_MESSAGE, (WIDTH-300)/2, 200);
				
				g.setFont(new Font("Verdana",0,20));
				g.drawString("press down key to restart", (WIDTH-250)/2, 300);
				g.drawString("Best score:"+this.bestScore, (WIDTH-135)/2, 350);
			}else{
				g.setFont(new Font("Verdana",0,50));
				g.drawString("OPEN BIRD", (WIDTH-270)/2, 200);
				
				g.setFont(new Font("Verdana",0,20));
				g.drawString("press down key to start", (WIDTH-250)/2, 300);
			}
		}
		g.dispose();
		bs.show();
	}
	
	public static void main(String[]args){
		Flappy game = new Flappy();
		game.frame.setResizable(false);
		game.frame.setTitle(TITLE);
		game.frame.add(game);
		game.frame.pack();
		
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
		
	}

}
