package com.twitch.flappy.mobs;

import com.twitch.flappy.graphics.Screen;

public class Goal {

		public int x,y,w,h,color;
		public boolean active;
			
		public Goal(int x,int y, int w, int h,int color){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			
			this.active = true;
			
			this.color = color;
			
		}
		
		
		
		public void render(Screen screen) {
			screen.renderObstacle(x,y,w,h,color);
		}


		public void tick() {
			this.x-= 3;
			
		}

}
