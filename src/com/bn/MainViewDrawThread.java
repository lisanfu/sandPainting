package com.bn;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainViewDrawThread extends Thread{
	MainView myMainView;
	private int sleepSpan = 40;
	boolean flag = true;//ѭ�����λ
	private SurfaceHolder surfaceHolder;//surfaceHolder������
	public MainViewDrawThread(MainView myMainView)
	{
		this.myMainView = myMainView;
		surfaceHolder = myMainView.getHolder();
	}
	public void run() {//��д��run����
		Canvas c;//��������
        while (this.flag) {//ѭ��
            c = null;
            try {//������������
                c = this.surfaceHolder.lockCanvas(null);
                synchronized (this.surfaceHolder) {//ͬ��
                	myMainView.onDraw(c);//���û��Ʒ���
                }
            } finally {//��finally��֤һ����ִ��
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
	public void setFlag(boolean flag){//ѭ�����λ��set����
		this.flag = flag;
	}
}
