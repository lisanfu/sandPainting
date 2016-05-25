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
	Bitmap bj;						//声明背景图片
	static int index;				//选中图片的索引值
	float prex;						//触控点前x坐标值
	float prey;						//触控点前y坐标值
	float xoffset=0;
	boolean isMove=false;			//是否移动标志位
	boolean isSelect=false;			//是否选中标志位
	Context context;
	
	float currV=0;					//当前速度
	float acce=0;					//加速度
	float preXForV;					//为计算加速度的x轴值
	boolean isAutoGo=false;			//是否惯性滚动标志位
	
	static boolean isLongClick=false;//长按标志位
	double hf;//虎符
	
	public ShowGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint=new Paint();						//创建画笔
		paint.setAntiAlias(true);	
	}
	public void onDraw(Canvas canvas)			//重写的onDraw方法
	{
		canvas.clipRect(new RectF(0,0,SCREEN_WIDTH,SCREEN_HEIGHT));//可提高性能
		float xMin=SCREEN_WIDTH-(bitmap_between_width+pic_w)*alr.size();
		if(xMin>0)
		{
			xMin=0;
		}
		float xMax=0;		
		if(xoffset>xMax)						//x轴的偏移量不能大于0，因为大于0时界面中会有空白，空白出现在左侧
		{
			xoffset=xMax;						//将偏移量置为0
			isAutoGo=false;
		}		
		if(xoffset<xMin)
		{
			xoffset=xMin;						//将偏移量置为最小值
			isAutoGo=false;
		}
		canvas.drawBitmap(WELCOME_ARRAY[3],(SCREEN_WIDTH-WELCOME_ARRAY[3].getWidth())/2, 0, paint);//绘制“作品集”标题图片
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
				{//绘制背景图片以及沙画图片
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
					
					//绘制作品名
					paint.setColor(Color.WHITE);
					paint.setTextSize(20);
					paint.setAntiAlias(true);
					String nameTemp=aln.get(alr.indexOf(pa));
					float nl=paint.measureText(nameTemp);//得到text的宽度
					canvas.drawText(nameTemp, xTemp+xoffset+(pic_w-nl)/2.0f, yTemp+pic_h+20, paint);
					paint.reset();
				}				
				c++;
			}
			//绘制翻页图片
			canvas.drawBitmap(WELCOME_ARRAY[4], 0, SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight(), paint);
			canvas.drawBitmap(WELCOME_ARRAY[5], SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth(), SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight(), paint);
		}
		else{//如果沙画集为空将显示“作品集为空”的图片
			canvas.drawBitmap(WELCOME_ARRAY[2], 
					(SCREEN_WIDTH-WELCOME_ARRAY[2].getWidth())/2, 
					(SCREEN_HEIGHT-WELCOME_ARRAY[2].getHeight())/2, paint);
		}
	}
	
	//触摸事件方法
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=e.getX();								//获得屏幕上点击的x坐标
		float y=e.getY();								//获得屏幕上点击的y坐标
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:					//按下事件
				isAutoGo=false;
				
				isLongClick=false;
				prex=x;										//将此坐标值赋予prex
				prey=y;										//将此坐标值赋予prey
				preXForV=x;
				index=(int)((x-xoffset)/(pic_w+bitmap_between_width));//获取按下位置所在picAtom图片的索引
				if(alr.size()==1&&x>=0&&x<=(pic_w)&&y>=(this.getHeight()-pic_h)/2&&y<=(this.getHeight()+pic_h)/2){
					index=0;//当只有一幅图片时单击图片的地方时为避免错误所以置index为0，表明只有一幅图片
				}
				if(alr.size()==1&&x>=(pic_w)||alr.size()<0){
					index=-1;//当只有一幅图片时单击屏幕上没有图片的地方时为避免错误所以置index为负
				}
				
				if(y>((this.getHeight()-pic_h)/2)&&y<((this.getHeight()-pic_h)/2+pic_h))
				{//为了避免点到没有图片的地方所以设定选择区为图片高度的区域
					isSelect=true;
				}
				if(x>=0&&x<=WELCOME_ARRAY[4].getWidth()
					&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[4].getHeight()
					&&y<=SCREEN_HEIGHT)
				{//点击上一页图片
					((SandPaintingActivity)context).handler.sendEmptyMessage(10);//向消息处理器发送消息"上一页"					
				}
				else if(x<=SCREEN_WIDTH&&x>=SCREEN_WIDTH-WELCOME_ARRAY[5].getWidth()
						&&y>=SCREEN_HEIGHT-WELCOME_ARRAY[5].getHeight()
						&&y<=SCREEN_HEIGHT)
				{//点击下一页图片
					((SandPaintingActivity)context).handler.sendEmptyMessage(11);//向消息处理器发送消息"下一页"										
				}
				if(y>=(SCREEN_HEIGHT-pic_h)/2&&y<=(SCREEN_HEIGHT+pic_h)/2){
					//启动长按监听
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
			case MotionEvent.ACTION_MOVE:			//移动事件
				if(!isMove&&Math.abs(x-prex)>20)	//当不再播放动画时，且抹动的距离大于阈值10dip时
				{
					isMove=true;					//播放动画标志位设为true
				}
				if(isMove)							//播放动画时
				{
					isSelect=false;					//变化标志位设置为false，此时不能画选中图片
					xoffset=(int)(xoffset+x-prex);	//计算x轴偏移量
					prex=x;							//将此时的坐标值赋予prex
					prey=y;
					
					float dx=x-preXForV;
					currV=dx/2.0f;
					preXForV=x;
					
					this.postInvalidate();	//重绘
				}
				return true;
			case MotionEvent.ACTION_UP:				//抬起时
				isMove=false;						//动画标志位设成false
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
						{//保证在长按的时候没有加速度避免选择的图片被刷没了
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
					Constant.alAction=pa.alAction;//将当前显示的原子动作置为选择的沙画中的动作信息
					Constant.allActiongroup=pa.allActiongroup;//将当前显示的动作组置为选择的沙画中的动作组信息
					Constant.hmhb=pa.hmhb;//图片缓存存储位置置为选择的图片的信息
					Constant.bmBuff=pa.bmResult;//Bitmap缓存用来存储已经画了的每一笔置为选择的图片的相应信息
					BgColorView.areaFlag=pa.bgIndex;//当前图片背景设置为对象中的信息			//设置消息对象的what值
					((SandPaintingActivity)context).handler.sendEmptyMessage(8);//向消息处理器发送消息				
				}	
			return true;
		}
		return false;
	}
}
