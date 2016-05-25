package com.bn;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import static com.bn.Constant.*;

enum ActionType{TS,QS};//填沙|清沙

public class AtomAction implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	//动作类型
	ActionType at;
	//动作起始xy位置
	float xStart;
	float yStart;
	float xEnd;
	float yEnd;
	//动作半径
	float r;
	//原子路径长度
	double length;
	//画笔ID
	long hbid;
	
	public AtomAction(ActionType at,float xStart,float yStart,float xEnd,float yEnd,float r,long hbid)
	{
		this.at=at;
		this.xStart=xStart;
		this.yStart=yStart;
		this.xEnd=xEnd;
		this.yEnd=yEnd;
		this.r=r;		
		this.hbid=hbid;
		float xSpan=xEnd-xStart;
		float ySpan=yEnd-yStart;
		length=Math.sqrt(xSpan*xSpan+ySpan*ySpan);		//某次喷砂的长度
	}
	
	public void drawSelf(Canvas canvas,Paint paint)
	{		
		paint.reset();//重新设置画笔
		if(at==ActionType.QS)
		{//动作为清沙
			paint.setColor(0xFF000000);//设置画笔颜色 	(在解决搽除背景这个问题中起作用)
			paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			paint.setStyle(Style.STROKE);//设置paint的风格为“空心”
			paint.setStrokeWidth(2*r);	 //设置其宽度		
			canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);	//绘制
			paint.reset();
		}
		else if(at==ActionType.TS)
		{
			paint.setStyle(Style.FILL);//设置画笔填充方式为实心
			paint.setColor(Color.rgb(SANDCOLOR[0],SANDCOLOR[1],SANDCOLOR[2]));//设置沙子颜色				 
			int steps=(int)(length/hbSize)*4;
			float xSpan=xEnd-xStart;  
			float ySpan=yEnd-yStart;
			float xStep=xSpan/steps;
			Bitmap bm=hmhb.get(hbid);
			for(int i=-1;i<=steps;i++)
			{
				float xc=xStart+i*xStep;
				float yc=ySpan*(xc-xStart)/xSpan+yStart;
				canvas.drawBitmap(bm, xc-r,yc-r, paint);
			}
		}
	}   
	
	@Override
	public int hashCode()
	{
		if(at==ActionType.TS)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o==null)
		{
			return false;
		}
		
		if(!(o instanceof AtomAction))
		{
			return false;
		}
		AtomAction aa=(AtomAction)o;
		if(this.at==aa.at&&this.xStart==aa.xStart&&this.xEnd==aa.xEnd
						 &&this.yStart==aa.yStart&&this.yEnd==aa.yEnd&&this.r==aa.r)
		{
			return true;
		}
		
		return false;
	}
}
