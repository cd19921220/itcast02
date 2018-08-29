package cn.itcast.nsfw.complain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;

public class MyTimerTask extends TimerTask {

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("doing TimerTask..." + sdf.format(new Date()));
	}

}
