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
		this.r=r;//��ɰ�뾶
		Bitmap bm=Bitmap.createBitmap
		(
				(int)(2*r),
				(int)(2*r), 
				Bitmap.Config.ARGB_8888
		);		//��ɰ���γɵ�bitmap
		Canvas ct = new Canvas(bm); 
		Paint p=new Paint();
		p.setColor(Color.rgb(196, 165, 43));//���û�����ɫ
		for(int i=0;i<tcl;i++)
		{//��ɰ����		
			float rt=(float) (r*Math.random());//��ɰ�İ뾶
			double angle=2*Math.PI*Math.random();//��ɰ�������ˮƽλ�õĽǶ�
			float xt=(float) (rt*Math.sin(angle));//��ɰ��ɳ����x����
			float yt=(float) (rt*Math.cos(angle));//��ɰ��ɳ����y����
            ct.drawPoint(xt+r, yt+r, p);            
		}
		hmhb.put(id, bm);//���洢���ʵ�hashmap�������
	}
}
