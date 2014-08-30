package com.twitch.flappy.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	private String path;
	public final int WIDTH;
	public final int HEIGHT;
	public int pixels[];
	
	public static Sprite flappy = new Sprite("flappy.png",39,25);
	public static Sprite flappy2 = new Sprite("flappy2.png",39,25);
	public static Sprite flappy3 = new Sprite("flappy3.png",39,25);
	public static Sprite ground = new Sprite("ground.png",380,45);
	
	
	public Sprite(String path,int width, int height){
		this.path = path;
		WIDTH = width;
		HEIGHT = height;
		
		this.pixels = new int[WIDTH * HEIGHT];
		
		loadSprite();
	}
	
	private void loadSprite(){
		BufferedImage image;
		try {
			image = ImageIO.read(Sprite.class.getResource(path));
			int w = image.getWidth();
			int h = image.getHeight();
			System.out.println(w+" "+" "+h);
			
			image.getRGB(0, 0,w,h,pixels,0,w);
		} catch (IOException e) {
			System.out.println("oops! no image!!");
			e.printStackTrace();
		}
		
	}
	
}
