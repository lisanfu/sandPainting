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
		{//��̬����ͼƬ��͸����ֵ�������ػ�	
			WelcomeView.currentAlpha=i;
			if(WelcomeView.currentAlpha<0)
			{
				WelcomeView.currentAlpha=0;
			}
            c = null;
            try {
            	// ������������
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {
                	myWelcomeView.onDraw(c);//����
                }
            } finally {
                if (c != null) {
                	//�ͷ���
                    this.surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            try{
            	Thread.sleep(10);//˯��ָ��������
            }
            catch(Exception e){
            	e.printStackTrace();
            }
		}
		WelcomeView.sandPainting.handler.sendEmptyMessage(1);
	}
}
