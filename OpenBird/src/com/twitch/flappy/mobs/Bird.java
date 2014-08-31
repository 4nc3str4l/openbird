package com.twitch.flappy.mobs;

import java.awt.Rectangle;

import input.Input;

import com.twitch.flappy.Flappy;
import com.twitch.flappy.graphics.Screen;
import com.twitch.flappy.graphics.Sprite;


/**
 * This is our player controller (handles key inputs ,"physics", animations...
 * @author Muriology
 *
 */
public class Bird extends Mob {
	
	Flappy flappy;
	Sprite actualSprite;
	int animationRate,nextAnimation;
	int ticks;
	public Bird(int x, int y,int w, int h, Sprite[] sprites, Flappy flappy) {
		super(x, y, w, h, sprites);
		this.flappy = flappy;
		this.animationRate = 0;
		this.nextAnimation = 0;
		this.sprites = sprites;
		this.actualSprite = sprites[0];
		ticks = 0;

	}

	@Override
	public void render(Screen screen) {

		screen.renderBird(x,y,w, h,this.actualSprite);
	}

	@Override
	public void tick() {

	}
	
	public void tick(Input input) {
		ticks++;
		updateAnimation();
		if(ticks == 60) ticks = 0;
		
		if(input.up && y > 10) this.y -=6;
		else{
			if(y < (Flappy.HEIGHT - h- 45)) this.y +=4;//this is some kind of weird gravity
		}

		if(this.y == 1 || (this.y+h) >= (Flappy.HEIGHT-45)){
			flappy.gameOver = true;
		}
		
	}
	
	/**
	 * Changes our sprite to make the illusion of an animation
	 */
	void updateAnimation(){
		if(ticks > 7500) ticks = 0;
		if(ticks % 30 > 10 && ticks < 20){
			this.actualSprite = sprites[0];
		}else if(ticks % 30 < 10){
			this.actualSprite = sprites[1];
		}else{
			this.actualSprite = sprites[2];
		}
	}
	
	public boolean onCollision(int x,int y,int w, int h){
		if(new Rectangle(this.x,this.y,this.w,this.h).intersects(new Rectangle(x,y,w,h))) return true;
		else return false;
	}

}
