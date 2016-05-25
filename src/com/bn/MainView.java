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
	Paint paint;//����
	public MainView(SandPaintingActivity sandPainting) 
	{
		super(sandPainting);
		getHolder().addCallback(this);
		this.sandPainting=sandPainting;
		initPaint();
		drawAllAction(canvasBuff,paint);	//�������ж���	
	}
	public void initPaint(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);//�򿪿����
		paint.setTextSize(18);
	}
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK); 
		for(int i=0;i<MAIN_AN_ARRAY.length;i++)
		{//���ư�ť����
			canvas.drawBitmap(MAIN_AN_ARRAY[i],PIC_LOCATION_MSG[i][0],PIC_LOCATION_MSG[i][1],paint);		
		}	
		canvas.drawBitmap(BGCOLOR_ARRAY_BIG[BgColorView.areaFlag],PIC_LOCATION_MSG[5][0], PIC_LOCATION_MSG[5][1], paint);
		//���ƶ����ͱ���
		canvas.drawBitmap(bmBuff, PIC_LOCATION_MSG[5][0],PIC_LOCATION_MSG[5][1], paint);
	}
	
	//�������ж����ķ���
	public void drawAllAction(Canvas canvas,Paint paint)
	{
		Bitmap bmBuffTemp=Bitmap.createBitmap
		(//ȷ���ڻ���������
			(int)(AREA_WIDTH),
			(int)(AREA_HEIGHT), 
			Bitmap.Config.ARGB_8888
		);		
		bmBuffTemp.eraseColor(Color.TRANSPARENT);//��ָ����ɫ���λͼ������(��ǰΪ͸��)
		Canvas canvasBuffTemp = new Canvas(bmBuffTemp); 
		synchronized(actionLock)
		{//���Ʊ�����ÿ�λ������Ҳ�����ټ�			
			for(AtomAction aa:alAction)
			{//����ÿһ���൱��һ��ͼƬ
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
	
	//��������һ������
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
	int touchArea=0; //0-��ɳ  1-��ɳ  2-����  3-����   4-����  5-��������
	
	ActionGroup ag;//����������
	//��Ļ��������
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();
		
		//��Ļ�������¼�
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
			{//���¡���ɳ����ť
				touchArea=0;	
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[1][1]					
					&&y<=PIC_LOCATION_MSG[1][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//���¡���ɳ����ť
				touchArea=1;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[2][1]					
					&&y<=PIC_LOCATION_MSG[2][1]+MAIN_AN_ARRAY[0].getHeight()
			)
			{//���¡���������ť
				touchArea=2;
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[3][1]					
					&&y<=PIC_LOCATION_MSG[3][1]+MAIN_AN_ARRAY[0].getHeight()
			){//���¡������ơ���ť
				touchArea=3;
				sandPainting.handler.sendEmptyMessage(4);
			}
			else if(x>=PIC_LOCATION_MSG[0][0]
			        &&x<=PIC_LOCATION_MSG[0][0]+MAIN_AN_ARRAY[0].getWidth()
					&&y>=PIC_LOCATION_MSG[4][1]
					&&y<=PIC_LOCATION_MSG[4][1]+MAIN_AN_ARRAY[0].getHeight()
			){//���¡����á���ť��ת������ҳ����о�������ú�ѡ������SetupView
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
			{//�ƶ�����15�����ص�λ������Ϊ���ƶ������ñ�־λΪtrue
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
						{//��ɳ	
							AtomAction aa=new AtomAction(ActionType.TS,actionXPre,actionYPre,x,y,hbSize,ag.id);
							boolean flag=alAction.add(aa);
							if(flag)
							{
								ag.actionG.add(aa);	
								drawSpecAction(canvasBuff,paint,aa);
							}
						} 
						else if(state==1)
						{//��ɳ
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
					case 0://0-��ɳ     
						state=0;
					break;
					case 1://1-��ɳ
						state=1;
					break;
					case 2://2-����
						synchronized(actionLock)
						{//��������֤��ɳ�洢��ÿһ�ʴ���ͬ��״̬���ⶪʧ�����������쳣
							int delIndex=allActiongroup.size()-1;//
							if(delIndex>=0)
							{
								allActiongroup.remove(delIndex);
								alAction.clear();//�����ǰ�Ļ���
								for(ActionGroup tag:allActiongroup)
								{//��ʣ���ÿһ������װ��group
									for(AtomAction aa:tag.actionG)
									{
										alAction.add(aa);
									}
								}//���ƻ�����canvas�����е�ÿһ��
								drawAllAction(canvasBuff,paint);
							}
						}	
					break;
					case 3://3-����
						
					break;
					case 4://4-����
						
					break;					
				}
			}
			else
			{
				switch(touchArea)
				{
					case 5://������һ��������ʵ�group
						allActiongroup.add(ag);						
					break;
				}
			}
			moveFlag=false;	//������һ�ʺ�move��־λ��Ϊfalse
			actionXPre=-1;	//������һ�ʺ�actionXλ����Ϊ��ʼλ��
			actionYPre=-1;  //������һ�ʺ�actionYλ����Ϊ��ʼλ��
		}		
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{//�����߳�ˢ֡
		mainViewDrawThread = new MainViewDrawThread(this);
		this.mainViewDrawThread.flag=true;
		mainViewDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean flag = true;//ѭ����־λ
		mainViewDrawThread.flag=false;//����ѭ����־λ
        while (flag) {//ѭ��
            try {
            	mainViewDrawThread.join();//�õ��߳̽���
            	flag = false;
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}

}
