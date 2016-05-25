package com.bn;
import static com.bn.Constant.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BgColorView extends SurfaceView implements SurfaceHolder.Callback{
	SandPaintingActivity sandPainting;//activity引用
	Paint paint;
	public BgColorView(SandPaintingActivity sandPainting) {
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initBitmap();
	}
	public void initBitmap(){//初始化paint
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setTextSize(18);
		
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<BGCOLOR_ARRAY.length;i++)
		{//绘制按钮数组
			canvas.drawBitmap(BGCOLOR_ARRAY[i],PIC_LOCATION_MSG[i+14][0],PIC_LOCATION_MSG[i+14][1],paint);		
		}//绘制左部分的沙画图片及提示图片
		canvas.drawBitmap(SETUP_ARRAY[5],PIC_LOCATION_MSG[13][0],PIC_LOCATION_MSG[13][1],paint);
		canvas.drawBitmap(WELCOME_ARRAY[1],PIC_LOCATION_MSG[22][0],PIC_LOCATION_MSG[22][1] , paint);
	}
	public static int areaFlag=0;//背景图片的标志位
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();	
		//屏幕被按下事件
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{	//以下的判断情况是对每一种背景色所做的监听，从第一排以左往右开始，均跳转到绘制页面
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
		Canvas canvas = holder.lockCanvas();//获取画布
		try{
			synchronized(holder){
				onDraw(canvas);//绘制
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
