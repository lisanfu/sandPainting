package com.bn;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainViewDrawThread extends Thread{
	MainView myMainView;
	private int sleepSpan = 40;
	boolean flag = true;//循环标记位
	private SurfaceHolder surfaceHolder;//surfaceHolder的引用
	public MainViewDrawThread(MainView myMainView)
	{
		this.myMainView = myMainView;
		surfaceHolder = myMainView.getHolder();
	}
	public void run() {//重写的run方法
		Canvas c;//声明画布
        while (this.flag) {//循环
            c = null;
            try {//锁定整个画布
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//同步
                	myMainView.onDraw(c);//调用绘制方法
                }
            } finally {//用finally保证一定被执行
                if (c != null) {                	
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(sleepSpan);
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
	}
	public void setFlag(boolean flag){//循环标记位的set方法
		this.flag = flag;
	}
}
