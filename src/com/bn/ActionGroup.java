package com.bn;

import static com.bn.Constant.*;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ActionGroup implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	long id=System.nanoTime();
	ArrayList<AtomAction> actionG=new ArrayList<AtomAction>();
	float r;
	
	public ActionGroup(float r)
	{
		this.r=r;//喷砂半径
		Bitmap bm=Bitmap.createBitmap
		(
				(int)(2*r),
				(int)(2*r), 
				Bitmap.Config.ARGB_8888
		);		//喷砂所形成的bitmap
		Canvas ct = new Canvas(bm); 
		Paint p=new Paint();
		p.setColor(Color.rgb(196, 165, 43));//设置画笔颜色
		for(int i=0;i<tcl;i++)
		{//喷砂过程		
			float rt=(float) (r*Math.random());//喷砂的半径
			double angle=2*Math.PI*Math.random();//喷砂的相对于水平位置的角度
			float xt=(float) (rt*Math.sin(angle));//喷砂的沙粒的x坐标
			float yt=(float) (rt*Math.cos(angle));//喷砂的沙粒的y坐标
            ct.drawPoint(xt+r, yt+r, p);            
		}
		hmhb.put(id, bm);//往存储画笔的hashmap添加数据
	}
}
