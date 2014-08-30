package com.twitch.flappy.mobs;

import com.twitch.flappy.graphics.Screen;
import com.twitch.flappy.graphics.Sprite;


public abstract class Mob {
	public int w;
	public int h;
	public int x;
	public int y;
	
	public Sprite[] sprites;
	
	public Mob(int x, int y, int w, int h,Sprite[] sprites){
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.sprites = sprites;
	}
	
	abstract void render(Screen screen);
	abstract void tick();
	
}
