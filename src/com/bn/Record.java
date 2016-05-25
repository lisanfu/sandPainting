package com.bn;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//一幅沙画的数据
public class Record
{
	LinkedHashSet<AtomAction> alAction;
	ArrayList<ActionGroup> allActiongroup;
	HashMap<Long,Bitmap> hmhb;//图片缓存存储位置
	Bitmap bmResult;//Bitmap缓存用来存储已经画了的每一笔
	int bgIndex;    //背景图片的索引
	
	public Record()
	{//取得当前活动着的沙画的各种信息
		this.alAction=Constant.alAction;
		this.allActiongroup=Constant.allActiongroup;
		this.hmhb=Constant.hmhb;
		this.bmResult=Constant.bmBuff;
		this.bgIndex=BgColorView.areaFlag;
	}
	
	public Record(LinkedHashSet<AtomAction> alAction,ArrayList<ActionGroup> allActiongroup,HashMap<Long,Bitmap> hmhb,Bitmap bmResult,int bgIndex)
	{//创建沙画记录对象
		this.alAction=alAction;
		this.allActiongroup=allActiongroup;
		this.hmhb=hmhb;
		this.bmResult=bmResult;
		this.bgIndex=bgIndex;
	}
	
	public byte[] toBytes()
	{//将Record对象转为byte数组
		byte[] result=null;		
		  
		try 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			ObjectOutputStream oout=new ObjectOutputStream(baos);
			oout.writeObject(this.alAction);
			oout.writeObject(this.allActiongroup);
			oout.writeObject(fromBitmapMapToBytesMap(hmhb));
			oout.writeObject(fromBitmapToBytes(bmResult));
			oout.writeObject(Integer.valueOf(bgIndex));
			result=baos.toByteArray();
			oout.close();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Record fromBytesToRecord(byte[] data)
	{//将byte数组转为Record对象
		Record result=null;
		try 
		{
			ByteArrayInputStream bais=new ByteArrayInputStream(data);
			ObjectInputStream oin;
			oin = new ObjectInputStream(bais);			
			LinkedHashSet<AtomAction> alAction=(LinkedHashSet<AtomAction>)oin.readObject();
			ArrayList<ActionGroup> allActiongroup=(ArrayList<ActionGroup>)oin.readObject();
			HashMap<Long,byte[]>hmbb=(HashMap<Long,byte[]>)oin.readObject();
			HashMap<Long,Bitmap> hmhb=fromBytesMapToBitmapMap(hmbb);
			Bitmap bmResult=fromBytesToBitmap((byte[])oin.readObject());
			int bgIndex=(Integer)oin.readObject();			
			result=new Record(alAction,allActiongroup,hmhb,bmResult,bgIndex);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static HashMap<Long,byte[]> fromBitmapMapToBytesMap(HashMap<Long,Bitmap> hmhb)
	{//将BitmapMap转为BytesMap	
		Set<Long> ks=hmhb.keySet();
		HashMap<Long,byte[]> tm=new HashMap<Long,byte[]>();
		
		for(Long l:ks)
		{
			Bitmap bm=hmhb.get(l);
			byte[] data=fromBitmapToBytes(bm);
			tm.put(l, data);
		}
		return tm;
	}
	
	public static HashMap<Long,Bitmap> fromBytesMapToBitmapMap(HashMap<Long,byte[]> hmbb)
	{//将BytesMap转为BitmapMap	
		Set<Long> ks=hmbb.keySet();
		HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();
		
		for(Long l:ks)
		{
			byte[] data=hmbb.get(l);
			Bitmap bm=fromBytesToBitmap(data);
			hmhb.put(l, bm);
		}
		
		return hmhb;
	}
	
	public static byte[] fromBitmapToBytes(Bitmap bm)
	{//将Bitmap转为Byte数组
		ByteArrayOutputStream baos = new ByteArrayOutputStream();     
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);     
		return baos.toByteArray();   
	}
	
	public static Bitmap fromBytesToBitmap(byte[] data)
	{//将Byte数组转为BytesMap		
		if(data.length!=0)
		{   
		  Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
		  if(bm==null)
		  {
			  throw new RuntimeException(data.length+"zi hua kong");
		  }
		  else
		  {
			  return bm;
		  }		  
		}   
		else 
		{   
			throw new RuntimeException("data.length==0----");
			//return null;   
		}   
	}
}
