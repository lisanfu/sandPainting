package com.bn;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class WelcomeViewDrawThread extends Thread {
	boolean flag = true;
	int sleepSpan = 100;
	WelcomeView myWelcomeView;
	SurfaceHolder surfaceHolder;
	public  void setFlag(boolean flag){
		this.flag=flag;
	}
	public WelcomeViewDrawThread(WelcomeView myWelcomeView){
		this.myWelcomeView = myWelcomeView;
		this.surfaceHolder = myWelcomeView.getHolder();
	}
	public void run(){
		Canvas c;
		for(int i=255;i>-10;i=i-20)
		{//动态更改图片的透明度值并不断重绘	
			WelcomeView.currentAlpha=i;
			if(WelcomeView.currentAlpha<0)
			{
				WelcomeView.currentAlpha=0;
			}
            c = null;
            try {
            	// 锁定整个画布
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	myWelcomeView.onDraw(c);//绘制
                }
            } finally {
                if (c != null) {
                	//释放锁
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(10);//睡眠指定毫秒数
            }
            catch(Exception e){
            	e.printStackTrace();
            }
		}
		WelcomeView.sandPainting.handler.sendEmptyMessage(1);
	}
}
