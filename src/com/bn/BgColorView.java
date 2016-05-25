package com.bn;
import static com.bn.Constant.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BgColorView extends SurfaceView implements SurfaceHolder.Callback{
	SandPaintingActivity sandPainting;//activity����
	Paint paint;
	public BgColorView(SandPaintingActivity sandPainting) {
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initBitmap();
	}
	public void initBitmap(){//��ʼ��paint
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//�򿪿����
		paint.setTextSize(18);
		
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<BGCOLOR_ARRAY.length;i++)
		{//���ư�ť����
			canvas.drawBitmap(BGCOLOR_ARRAY[i],PIC_LOCATION_MSG[i+14][0],PIC_LOCATION_MSG[i+14][1],paint);		
		}//�����󲿷ֵ�ɳ��ͼƬ����ʾͼƬ
		canvas.drawBitmap(SETUP_ARRAY[5],PIC_LOCATION_MSG[13][0],PIC_LOCATION_MSG[13][1],paint);
		canvas.drawBitmap(WELCOME_ARRAY[1],PIC_LOCATION_MSG[22][0],PIC_LOCATION_MSG[22][1] , paint);
	}
	public static int areaFlag=0;//����ͼƬ�ı�־λ
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();	
		//��Ļ�������¼�
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{	//���µ��ж�����Ƕ�ÿһ�ֱ���ɫ�����ļ������ӵ�һ���������ҿ�ʼ������ת������ҳ��
			if(x>=PIC_LOCATION_MSG[14][0]
			      			        &&x<=PIC_LOCATION_MSG[14][0]+BGCOLOR_ARRAY[0].getWidth()
			      					&&y>=PIC_LOCATION_MSG[14][1]					
			      					&&y<=PIC_LOCATION_MSG[14][1]+BGCOLOR_ARRAY[0].getHeight()
  			)
  			{	
				areaFlag=0;
				sandPainting.handler.sendEmptyMessage(3);
  			}
			else if(x>=PIC_LOCATION_MSG[15][0]
  			        &&x<=PIC_LOCATION_MSG[15][0]+BGCOLOR_ARRAY[0].getWidth()
  					&&y>=PIC_LOCATION_MSG[15][1]					
  					&&y<=PIC_LOCATION_MSG[15][1]+BGCOLOR_ARRAY[0].getHeight()
  			)
  			{
  				areaFlag=1;
				sandPainting.handler.sendEmptyMessage(3);
  			}
  			else if(x>=PIC_LOCATION_MSG[16][0]
			        &&x<=PIC_LOCATION_MSG[16][0]+BGCOLOR_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[16][1]					
					&&y<=PIC_LOCATION_MSG[16][1]+BGCOLOR_ARRAY[0].getHeight()
			)
			{
				areaFlag=2;
				sandPainting.handler.sendEmptyMessage(3);
			}
  			else if(x>=PIC_LOCATION_MSG[17][0]
			        &&x<=PIC_LOCATION_MSG[17][0]+BGCOLOR_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[17][1]					
					&&y<=PIC_LOCATION_MSG[17][1]+BGCOLOR_ARRAY[0].getHeight()
			)
			{
				areaFlag=3;
				sandPainting.handler.sendEmptyMessage(3);
			}else if(x>=PIC_LOCATION_MSG[18][0]
   			        &&x<=PIC_LOCATION_MSG[18][0]+BGCOLOR_ARRAY[0].getWidth()
  					&&y>=PIC_LOCATION_MSG[18][1]					
  					&&y<=PIC_LOCATION_MSG[18][1]+BGCOLOR_ARRAY[0].getHeight()
  			)
  			{
  				areaFlag=4;
				sandPainting.handler.sendEmptyMessage(3);
  			}
  			else if(x>=PIC_LOCATION_MSG[19][0]
			        &&x<=PIC_LOCATION_MSG[19][0]+BGCOLOR_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[19][1]					
					&&y<=PIC_LOCATION_MSG[19][1]+BGCOLOR_ARRAY[0].getHeight()
			)
			{
				areaFlag=5;
				sandPainting.handler.sendEmptyMessage(3);
			}
  			else if(x>=PIC_LOCATION_MSG[20][0]
  			        &&x<=PIC_LOCATION_MSG[20][0]+BGCOLOR_ARRAY[0].getWidth()
  					&&y>=PIC_LOCATION_MSG[20][1]					
  					&&y<=PIC_LOCATION_MSG[20][1]+BGCOLOR_ARRAY[0].getHeight()
  			)
  			{
  				areaFlag=6;
  				sandPainting.handler.sendEmptyMessage(3);
  			}else if(x>=PIC_LOCATION_MSG[21][0]
     			    &&x<=PIC_LOCATION_MSG[21][0]+BGCOLOR_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[21][1]					
					&&y<=PIC_LOCATION_MSG[21][1]+BGCOLOR_ARRAY[0].getHeight()
			)
			{
				areaFlag=7;
				sandPainting.handler.sendEmptyMessage(3);
			}
		}
		return true;			
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		repaint();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {}
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas = holder.lockCanvas();//��ȡ����
		try{
			synchronized(holder){
				onDraw(canvas);//����
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
