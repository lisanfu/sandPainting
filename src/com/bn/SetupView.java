package com.bn;


import static com.bn.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SetupView extends SurfaceView implements SurfaceHolder.Callback{
	SandPaintingActivity sandPainting;
	Paint paint;
	Bitmap [] bitmaps;
	public SetupView(SandPaintingActivity sandPainting) {
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initBitmap();
	}
	public void initBitmap(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setTextSize(18);
		bitmaps = SETUP_ARRAY;//得到处理后的图片数组
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		for(int i=0;i<SETUP_ARRAY.length;i++){
			canvas.drawBitmap(bitmaps[i],PIC_LOCATION_MSG[7+i][0],
					PIC_LOCATION_MSG[7+i][1], paint);		//绘制设置图片
		}		
	}
	int touchArea=0; //0-新建沙画  1-保存沙画  2-删除沙画  3-作品集   4-退出 
	
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();	
		//屏幕被按下事件
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{			
			if(x>=PIC_LOCATION_MSG[7][0]
			        &&x<=PIC_LOCATION_MSG[7][0]+bitmaps[0].getWidth()
					&&y>=PIC_LOCATION_MSG[7][1]					
					&&y<=PIC_LOCATION_MSG[7][1]+bitmaps[0].getHeight()
			)
			{//按下“新建沙画”按钮
				touchArea=0;
				alAction.clear();
				allActiongroup.clear();
				hmhb.clear();
				sandPainting.handler.sendEmptyMessage(3);
			}
			else if(x>=PIC_LOCATION_MSG[8][0]
			        &&x<=PIC_LOCATION_MSG[8][0]+bitmaps[0].getWidth()
					&&y>=PIC_LOCATION_MSG[8][1]					
					&&y<=PIC_LOCATION_MSG[8][1]+bitmaps[0].getHeight()
			)
			{//按下“保存沙画”按钮
				touchArea=1;
				sandPainting.handler.sendEmptyMessage(6);//进行保存对话框的显示
			}
			else if(x>=PIC_LOCATION_MSG[9][0]
			        &&x<=PIC_LOCATION_MSG[9][0]+bitmaps[0].getWidth()
					&&y>=PIC_LOCATION_MSG[9][1]					
					&&y<=PIC_LOCATION_MSG[9][1]+bitmaps[0].getHeight()
			)
			{//按下“画笔设置”按钮
				touchArea=2;
				sandPainting.handler.sendEmptyMessage(5);//跳转到画笔设置页面
			}
			else if(x>=PIC_LOCATION_MSG[10][0]
			        &&x<=PIC_LOCATION_MSG[10][0]+bitmaps[0].getWidth()
					&&y>=PIC_LOCATION_MSG[10][1]					
					&&y<=PIC_LOCATION_MSG[10][1]+bitmaps[0].getHeight()
			){//按下“作品集”按钮
				touchArea=3;
				sandPainting.handler.sendEmptyMessage(7);//显示画廊
			}
			else if(x>=PIC_LOCATION_MSG[11][0]
			        &&x<=PIC_LOCATION_MSG[11][0]+bitmaps[0].getWidth()
					&&y>=PIC_LOCATION_MSG[11][1]
					&&y<=PIC_LOCATION_MSG[11][1]+bitmaps[0].getHeight()
			){//按下“退出”按钮
				touchArea=4;		
				sandPainting.finish();
    			new Thread()
    			{
    				public void run()
    				{
    					try {
							Thread.sleep(1000);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
    					System.exit(0); 
    				}
    			}.start();	
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
