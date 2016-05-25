package com.bn;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.Constant.*;

public  class WelcomeView extends SurfaceView implements SurfaceHolder.Callback
{
	static SandPaintingActivity sandPainting;
	WelcomeViewDrawThread viewDrawThread;
	Paint paint;//����
	static int currentAlpha;//��ǰ�Ĳ�͸��ֵ
	public WelcomeView(SandPaintingActivity sandPainting) 
	{
		super(sandPainting);
		WelcomeView.sandPainting=sandPainting;
		getHolder().addCallback(this);
		initBitmap();
	}

	public void initBitmap()
	{
		paint = new Paint();
		paint.setAlpha(255);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//�򿪿����
		paint.setTextSize(18);
	}
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);		
		paint.setAlpha(currentAlpha);
		canvas.drawBitmap(WELCOME_ARRAY[0],PIC_LOCATION_MSG[6][0],
				PIC_LOCATION_MSG[6][1], paint);		//���ƻ�ӭͼƬ
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		viewDrawThread = new WelcomeViewDrawThread(this);
		this.viewDrawThread.flag=true;
		viewDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		boolean flag = true;//ѭ����־λ
        viewDrawThread.flag=false;//����ѭ����־λ
        while (flag) {//ѭ��
            try {
            	viewDrawThread.join();//�õ��߳̽���
            	flag = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}
}
