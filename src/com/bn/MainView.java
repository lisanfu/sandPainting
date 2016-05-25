package com.bn;
import static com.bn.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback{
	SandPaintingActivity sandPainting;
	MainViewDrawThread mainViewDrawThread;
	Paint paint;//画笔
	public MainView(SandPaintingActivity sandPainting) 
	{
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initPaint();
		drawAllAction(canvasBuff,paint);	//绘制所有动作	
	}
	public void initPaint(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setTextSize(18);
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<MAIN_AN_ARRAY.length;i++)
		{//绘制按钮数组
			canvas.drawBitmap(MAIN_AN_ARRAY[i],PIC_LOCATION_MSG[i][0],PIC_LOCATION_MSG[i][1],paint);		
		}	
		canvas.drawBitmap(BGCOLOR_ARRAY_BIG[BgColorView.areaFlag],PIC_LOCATION_MSG[5][0], PIC_LOCATION_MSG[5][1], paint);
		//绘制动作和背景
		canvas.drawBitmap(bmBuff, PIC_LOCATION_MSG[5][0],PIC_LOCATION_MSG[5][1], paint);
	}
	
	//绘制所有动作的方法
	public void drawAllAction(Canvas canvas,Paint paint)
	{
		Bitmap bmBuffTemp=Bitmap.createBitmap
		(//确定在绘制区绘制
			(int)(AREA_WIDTH),
			(int)(AREA_HEIGHT), 
			Bitmap.Config.ARGB_8888
		);		
		bmBuffTemp.eraseColor(Color.TRANSPARENT);//用指定颜色填充位图的像素(当前为透明)
		Canvas canvasBuffTemp = new Canvas(bmBuffTemp); 
		synchronized(actionLock)
		{//绘制背景，每次绘制完成也可以再加			
			for(AtomAction aa:alAction)
			{//绘制每一笔相当于一幅图片
				aa.drawSelf(canvasBuffTemp,paint);
			}
		}	
		if(bmBuff!=null)
		{
			bmBuff.recycle();
		}
		
		bmBuff=bmBuffTemp;
		canvasBuff=canvasBuffTemp;
	}
	
	//增量绘制一个动作
	public void drawSpecAction(Canvas canvas,Paint paint,AtomAction aa)
	{
		synchronized(actionLock)
		{			
			aa.drawSelf(canvas,paint);
		}
	}	
	
	float xPre=-1;
	float yPre=-1;
	float actionXPre=-1;
	float actionYPre=-1;
	boolean moveFlag=false;
	int touchArea=0; //0-填沙  1-清沙  2-撤销  3-背景   4-设置  5-绘制区域
	
	ActionGroup ag;//动作排序组
	//屏幕监听方法
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();
		
		//屏幕被按下事件
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			xPre=x;
			yPre=y;
			moveFlag=false;
			if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[0][1]					
					&&y<=PIC_LOCATION_MSG[0][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“填沙”按钮
				touchArea=0;	
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[1][1]					
					&&y<=PIC_LOCATION_MSG[1][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“清沙”按钮
				touchArea=1;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[2][1]					
					&&y<=PIC_LOCATION_MSG[2][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//按下“撤销”按钮
				touchArea=2;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[3][1]					
					&&y<=PIC_LOCATION_MSG[3][1]+MAIN_AN_ARRAY[0].getHeight()
			){//按下“背景灯”按钮
				touchArea=3;
				sandPainting.handler.sendEmptyMessage(4);
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[4][1]
					&&y<=PIC_LOCATION_MSG[4][1]+MAIN_AN_ARRAY[0].getHeight()
			){//按下“设置”按钮跳转到设置页面进行具体的设置和选择，跳到SetupView
				touchArea=4;
				sandPainting.handler.sendEmptyMessage(2);
			}
			else
			{
				touchArea=5;
				ag=new ActionGroup(hbSize);
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(x-xPre>15||y-yPre>15)
			{//移动超过15个像素单位才能认为是移动了设置标志位为true
				moveFlag=true;
			}
			
			if(touchArea==5&&x<=PIC_LOCATION_MSG[5][2]&&x>=PIC_LOCATION_MSG[5][0]
			              &&y<=PIC_LOCATION_MSG[5][3]&&y>=PIC_LOCATION_MSG[5][1])
			{
				synchronized(actionLock)
				{
					if(actionXPre!=-1&&actionYPre!=-1)
					{
						if(state==0)
						{//填沙	
							AtomAction aa=new AtomAction(ActionType.TS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						} 
						else if(state==1)
						{//清沙
							AtomAction aa=new AtomAction(ActionType.QS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);	
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						}
					} 
				}	
				actionXPre=x;
				actionYPre=y;
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(!moveFlag)
			{
				switch(touchArea)
				{
					case 0://0-填沙     
						state=0;
					break;
					case 1://1-清沙
						state=1;
					break;
					case 2://2-撤销
						synchronized(actionLock)
						{//加锁，保证画沙存储的每一笔处于同步状态避免丢失不加锁将抛异常
							int delIndex=allActiongroup.size()-1;//
							if(delIndex>=0)
							{
								allActiongroup.remove(delIndex);
								alAction.clear();//清除当前的画笔
								for(ActionGroup tag:allActiongroup)
								{//将剩余的每一笔重新装进group
									for(AtomAction aa:tag.actionG)
									{
										alAction.add(aa);
									}
								}//绘制缓存在canvas缓存中的每一笔
								drawAllAction(canvasBuff,paint);
							}
						}	
					break;
					case 3://3-背景
						
					break;
					case 4://4-设置
						
					break;					
				}
			}
			else
			{
				switch(touchArea)
				{
					case 5://绘制完一笔填进画笔的group
						allActiongroup.add(ag);						
					break;
				}
			}
			moveFlag=false;	//绘制完一笔后将move标志位设为false
			actionXPre=-1;	//绘制完一笔后将actionX位置设为初始位置
			actionYPre=-1;  //绘制完一笔后将actionY位置设为初始位置
		}		
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{//启动线程刷帧
		mainViewDrawThread = new MainViewDrawThread(this);
		this.mainViewDrawThread.flag=true;
		mainViewDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean flag = true;//循环标志位
		mainViewDrawThread.flag=false;//设置循环标志位
        while (flag) {//循环
            try {
            	mainViewDrawThread.join();//得到线程结束
            	flag = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}

}
