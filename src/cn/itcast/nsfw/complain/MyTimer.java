package cn.itcast.nsfw.complain;

import java.util.Timer;

public class MyTimer {

	public static void main(String[] args) {
		Timer timer = new Timer();
		//延迟2秒执行，每3秒执行一次
		timer.schedule(new MyTimerTask(), 2000, 3000);
		
	}

}
