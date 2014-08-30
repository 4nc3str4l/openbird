package com.twitch.flappy.mobs;

import com.twitch.flappy.graphics.Screen;

public class Obstacle{
	
	public int x,y,w,h,color;
		
	public Obstacle(int x,int y, int w, int h,int color){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
	}
	
	
	
	public void render(Screen screen) {
		screen.renderObstacle(x,y,w,h,color);
	}


	public void tick() {
		this.x-= 3;
		
	}
}
