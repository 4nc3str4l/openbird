package com.twitch.flappy.graphics;

import com.twitch.flappy.Flappy;


/**
 * This class fills the pixel array to be rendered in the main render method.
 * @author Muriology
 *
 */
public class Screen {
	
	private int width,height;
	
	private int[] pixels;
	
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		
		this.pixels = new int[width*height];
	}
	
	/**
	 * Not used but clears the screen
	 */
	public void clear(){
		for(int i = 0;i<this.pixels.length;i++){
			this.pixels[i] = 0;
		}
	}
	
	/**
	 * renders the background with an specific color and clear the screen
	 */
	public void renderBackground(){
		for(int i = 0;i<this.pixels.length;i++){
			this.pixels[i] = 0x5d99de;
		}

	}
	
	public void renderGround(){
		for(int i=0;i<45;i++){
			int sY = i+this.height-45;
			for(int j=0;j<380;j++){
				this.pixels[j+sY*this.width] = Sprite.ground.pixels[j + i*380];
			}
		}
	}
	
	public int getPixel(int i){
		return this.pixels[i];
	}

	/**
	 * render the bird on a desired position on the screen
	 * @param x position x
	 * @param y
	 * @param w width (image)
	 * @param h height
	 * @param sprite (the sprite to be rendered)
	 */
	public void renderBird(int x,int y,int w, int h, Sprite sprite) {
		
		for(int i=0;i<h;i++){
			int sY = i+y;
			for(int j=0;j<w;j++){
				int sX = j+x;
				if(sprite.pixels[j + i*w] != 0xffff00ff){
					this.pixels[sX+sY*this.width] = sprite.pixels[j + i*w];
				}
			}
		}
	}
	
	/**
	 * renders limits to the right and left of the screen.
	 */
	public void renderLeftLim(){
		
		for(int i=0;i<this.height;i++){
			for(int j=0;j<30;j++){
				this.pixels[j + i*this.width] = 0xccccff;
				this.pixels[this.width-1-j + i*this.width] = 0xccccff;
				
			}
		}
	}


	/**
	 * renders an obstacle with the desired color passed as a paramenter.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param color
	 */
	public void renderObstacle(int x, int y, int w, int h, int color) {
		int jOffset = 0;
		if(x < 0) jOffset = -x;
		for(int i=0;i<h;i++){
			int sY = i+y;
			for(int j=0+jOffset;j<w;j++){
				int sX = j+x;
				if(sX+sY*this.width > pixels.length || sX+sY*this.width < 0 ) continue;
				this.pixels[sX+sY*this.width] = color;
			}
		}
	}
}
