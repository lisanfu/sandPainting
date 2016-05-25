package com.bn;
import static com.bn.Constant.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ShowGalleryView extends View
{
	private Paint paint;
	Bitmap bj;						//��������ͼƬ
	static int index;				//ѡ��ͼƬ������ֵ
	float prex;						//���ص�ǰx����ֵ
	float prey;						//���ص�ǰy����ֵ
	float xoffset=0;
	boolean isMove=false;			//�Ƿ��ƶ���־λ
	boolean isSelect=false;			//�Ƿ�ѡ�б�־λ
	Context context;
	
	float currV=0;					//��ǰ�ٶ�
	float acce=0;					//���ٶ�
	float preXForV;					//Ϊ������ٶȵ�x��ֵ
	boolean isAutoGo=false;			//�Ƿ���Թ�����־λ
	
	static boolean isLongClick=false;//������־λ
	double hf;//����
	
	public ShowGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint=new Paint();						//��������
		paint.setAntiAlias(true);	
	}
	public void onDraw(Canvas canvas)			//��д��onDraw����
	{
		canvas.clipRect(new RectF(0,0,SCREEN_WIDTH,SCREEN_HEIGHT));//���������
		float xMin=SCREEN_WIDTH-(bitmap_between_width+pic_w)*alr.size();
		if(xMin>0)
		{
			xMin=0;
		}
		float xMax=0;		
		if(xoffset>xMax)						//x���ƫ�������ܴ���0����Ϊ����0ʱ�����л��пհף��հ׳��������
		{
			xoffset=xMax;						//��ƫ������Ϊ0
			isAutoGo=false;
		}		
		if(xoffset<xMin)
		{
			xoffset=xMin;						//��ƫ������Ϊ��Сֵ
			isAutoGo=false;
		}
		canvas.drawBitmap(WELCOME_ARRAY[3],(SCREEN_WIDTH-WELCOME_ARRAY[3].getWidth())/2, 0, paint);//���ơ���Ʒ��������ͼƬ
		if(alr.size()>0){
			int c=0;			
			for(Record pa:alr){
				Bitmap bitmap=pa.bmResult;	
				Bitmap bg=BGCOLOR_ARRAY_BIG[pa.bgIndex];
				float xTemp=(bitmap_between_width+pic_w)*c;
				float yTemp=(this.getHeight()-pic_h)/2;
				float ratioTemp=pic_w/bitmap.getWidth();
				float xr=pic_w/bg.getWidth();
				float yr=pic_h/bg.getHeight();							
				if(!(isSelect&&index==alr.indexOf(pa)))
				{//���Ʊ���ͼƬ�Լ�ɳ��ͼƬ
					canvas.save();
					canvas.translate(xTemp+xoffset, yTemp);
					canvas.scale(xr, yr);
					canvas.drawBitmap(bg,0,0, paint);					
					canvas.restore();
					
					canvas.save();
					canvas.translate(xTemp+xoffset, yTemp);
					canvas.scale(ratioTemp, ratioTemp);
					canvas.drawBitmap(bitmap,0,0, paint);
					canvas.restore();
					
					//������Ʒ��
					paint.setColor(Color.WHITE);
					paint.setTextSize(20);
					paint.setAntiAlias(true);
					String nameTemp=aln.get(alr.indexOf(pa));
					float nl=paint.measureText(nameTemp);//�õ�text�Ŀ��
					canvas.drawText(nameTemp, xTemp+xoffset+(pic_w-nl)/2.0f, yTemp+pic_h+20, paint);
					paint.reset();
				}				
				c++;
			}
			//���Ʒ�ҳͼƬ
			canvas.drawBitmap(WELCOME_ARRAY[4], 0, SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight(), paint);
			canvas.drawBitmap(WELCOME_ARRAY[5], SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth(), SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight(), paint);
		}
		else{//���ɳ����Ϊ�ս���ʾ����Ʒ��Ϊ�ա���ͼƬ
			canvas.drawBitmap(WELCOME_ARRAY[2], 
					(SCREEN_WIDTH-WELCOME_ARRAY[2].getWidth())/2, 
					(SCREEN_HEIGHT-WELCOME_ARRAY[2].getHeight())/2, paint);
		}
	}
	
	//�����¼�����
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=e.getX();								//�����Ļ�ϵ����x����
		float y=e.getY();								//�����Ļ�ϵ����y����
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:					//�����¼�
				isAutoGo=false;
				
				isLongClick=false;
				prex=x;										//��������ֵ����prex
				prey=y;										//��������ֵ����prey
				preXForV=x;
				index=(int)((x-xoffset)/(pic_w+bitmap_between_width));//��ȡ����λ������picAtomͼƬ������
				if(alr.size()==1&&x>=0&&x<=(pic_w)&&y>=(this.getHeight()-pic_h)/2&&y<=(this.getHeight()+pic_h)/2){
					index=0;//��ֻ��һ��ͼƬʱ����ͼƬ�ĵط�ʱΪ�������������indexΪ0������ֻ��һ��ͼƬ
				}
				if(alr.size()==1&&x>=(pic_w)||alr.size()<0){
					index=-1;//��ֻ��һ��ͼƬʱ������Ļ��û��ͼƬ�ĵط�ʱΪ�������������indexΪ��
				}
				
				if(y>((this.getHeight()-pic_h)/2)&&y<((this.getHeight()-pic_h)/2+pic_h))
				{//Ϊ�˱���㵽û��ͼƬ�ĵط������趨ѡ����ΪͼƬ�߶ȵ�����
					isSelect=true;
				}
				if(x>=0&&x<=WELCOME_ARRAY[4].getWidth()
					&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight()
					&&y<=SCREEN_HEIGHT)
				{//�����һҳͼƬ
					((SandPaintingActivity)context).handler.sendEmptyMessage(10);//����Ϣ������������Ϣ"��һҳ"					
				}
				else if(x<=SCREEN_WIDTH&&x>=SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth()
						&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight()
						&&y<=SCREEN_HEIGHT)
				{//�����һҳͼƬ
					((SandPaintingActivity)context).handler.sendEmptyMessage(11);//����Ϣ������������Ϣ"��һҳ"										
				}
				if(y>=(SCREEN_HEIGHT-pic_h)/2&&y<=(SCREEN_HEIGHT+pic_h)/2){
					//������������
					new Thread()
					{
						public void run()
						{
							double tempHf=Math.random();
							hf=tempHf;
							try 
							{
								Thread.sleep(1500);
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							if(hf==tempHf&&(!isMove))
							{
								isLongClick=true;
								String nameTemp=aln.get(index);
								Bundle b=new Bundle();
								b.putString("name",nameTemp);
								Message msg=new Message();
								msg.what=9;
								msg.setData(b);
								((SandPaintingActivity)context).handler.sendMessage(msg);
							}
						}
					}.start();
				}
				
				
				return true;
			case MotionEvent.ACTION_MOVE:			//�ƶ��¼�
				if(!isMove&&Math.abs(x-prex)>20)	//�����ٲ��Ŷ���ʱ����Ĩ���ľ��������ֵ10dipʱ
				{
					isMove=true;					//���Ŷ�����־λ��Ϊtrue
				}
				if(isMove)							//���Ŷ���ʱ
				{
					isSelect=false;					//�仯��־λ����Ϊfalse����ʱ���ܻ�ѡ��ͼƬ
					xoffset=(int)(xoffset+x-prex);	//����x��ƫ����
					prex=x;							//����ʱ������ֵ����prex
					prey=y;
					
					float dx=x-preXForV;
					currV=dx/2.0f;
					preXForV=x;
					
					this.postInvalidate();	//�ػ�
				}
				return true;
			case MotionEvent.ACTION_UP:				//̧��ʱ
				isMove=false;						//������־λ���false
				hf=Math.random();
				acce=10;
				if(currV>0)
				{
					acce=-10;
				}
				isAutoGo=true;
				
				new Thread()
				{
					public void run()
					{
						while(isAutoGo&&!isLongClick)
						{//��֤�ڳ�����ʱ��û�м��ٶȱ���ѡ���ͼƬ��ˢû��
							if(currV>=0&&acce>0||currV<=0&&acce<0)
							{
								break;
							}
							xoffset=xoffset+currV;
							currV=currV+acce;							
							postInvalidate();
							
							try 
							{
								Thread.sleep(40);
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
						}
					}
				}.start();				
				
				if(isSelect&&!isLongClick&&alr.size()>0&&index>=0)
				{
					hf=Math.random();
					isLongClick=false;
					Record pa=alr.get(index);
					Constant.alAction=pa.alAction;//����ǰ��ʾ��ԭ�Ӷ�����Ϊѡ���ɳ���еĶ�����Ϣ
					Constant.allActiongroup=pa.allActiongroup;//����ǰ��ʾ�Ķ�������Ϊѡ���ɳ���еĶ�������Ϣ
					Constant.hmhb=pa.hmhb;//ͼƬ����洢λ����Ϊѡ���ͼƬ����Ϣ
					Constant.bmBuff=pa.bmResult;//Bitmap���������洢�Ѿ����˵�ÿһ����Ϊѡ���ͼƬ����Ӧ��Ϣ
					BgColorView.areaFlag=pa.bgIndex;//��ǰͼƬ��������Ϊ�����е���Ϣ			//������Ϣ�����whatֵ
					((SandPaintingActivity)context).handler.sendEmptyMessage(8);//����Ϣ������������Ϣ				
				}	
			return true;
		}
		return false;
	}
}
