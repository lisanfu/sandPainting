package com.bn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Constant 
{
	public static final int NAME_INPUT_DIALOG_ID=0;//沙画名称输入对话框id
	public static final int DELETE_DIALOG_ID=1;
	public static final float hs=480; 	//手机屏幕标准宽度
	public static final float ws=800; 	//手机屏幕标准高度
	public static float SCREEN_WIDTH;	//屏幕宽度
	public static float SCREEN_HEIGHT;	//屏幕高度
	public static float SCALE;	        //使用的缩放比例
	public static float[][] PIC_LOCATION_MSG;
	public static int [] SANDCOLOR={196, 165, 43};//沙子颜色数组在AtomAction中获取
	public static int state=0;			//当前绘制状态   0-填沙  1-清沙
	public static float hbSize=10;		//画笔半径
	public static float tcl=40;			//填充率  
	public static final float STANDARD_LENGTH=30; //画笔每 笔的标准单位长度
	public final static float TCLMAX=100;//填充率的最大值
	public final static float TCLMIN=10;//填充率的最小值
	public final static float HBMAX=20;	//画笔半径的最大值
	public final static float HBMIN=5;	//画笔半径的最小值
	public static float AREA_WIDTH;	//沙画区域宽度
	public static float AREA_HEIGHT;//沙画区域高度
	public static float pic_w;		//小图片宽
	public static float pic_h;		//小图片高
	public static float bitmap_between_width=5;//小图片间隔
	public static Object actionLock=new Object();//锁对象
	
	public static int galleryPageNo=0;//当前沙画页数
	public static int galleryPageSpan=6;//每页显示数
	public static int galleryPageCount=0;//沙画的数量
	
	//动作列表
	static LinkedHashSet<AtomAction> alAction=new LinkedHashSet<AtomAction>();//原子动作
	static ArrayList<ActionGroup> allActiongroup=new ArrayList<ActionGroup>();//动作组合
	static HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();//图片缓存存储位置
	//当前图像缓冲
	static Bitmap bmBuff;//Bitmap缓存用来存储已经画了的每一笔
	static Canvas canvasBuff; 
	
	//历史记录列表
	static LinkedHashMap<String,byte[]> history=new LinkedHashMap<String,byte[]>();
	public static ArrayList<Record> alr;//用来存储沙画记录
	static ArrayList<String> aln;//用来存储沙画名
	public static int[] SETUP_AN_ID = new int[]
  	{//SETUP中按钮图片资源ID
  		R.drawable.setup0,R.drawable.setup1,
  		R.drawable.setup2,R.drawable.setup3,
  		R.drawable.setup4,R.drawable.s5
  	};
	public static int[] BG_PIC_ID = new int[]
	{//背景图片资源ID
		R.drawable.bj0,R.drawable.bj1,
		R.drawable.bj2,R.drawable.bj3,
		R.drawable.bj4,R.drawable.bj5,
		R.drawable.bj6,R.drawable.bj7
	};	
	public static int[] MAIN_AN_ID = new int[]
	{//MAINVIEW中按钮图片资源ID
		R.drawable.a1,R.drawable.a2,
		R.drawable.a3,R.drawable.a4,
		R.drawable.a5
	};
	public static int[] WELCOME_PIC_ID = new int[]{//欢迎页面图片资源ID
		R.drawable.z6,R.drawable.choicebg,
		R.drawable.zpjkong,R.drawable.zpj,
		R.drawable.prepage,R.drawable.nextpage
	};

	public static void getRatio(){		//求出在实际使用时的缩放比
		float wratio=SCREEN_WIDTH/ws;				//横坐标比例	
		float hratio=SCREEN_HEIGHT/hs;				//纵坐标比例	
		if(wratio<hratio){
			SCALE=wratio;
		}else{
			SCALE=hratio; 
		}
		hbSize=SCALE*hbSize;		//画笔半径实际值
	}
	public static Bitmap []BGCOLOR_ARRAY;//设置页面图片资源数组
	public static Bitmap []BGCOLOR_ARRAY_BIG;//设置页面图片资源数组
	public static Bitmap [] SETUP_ARRAY;//设置页面图片资源数组
	public static Bitmap [] WELCOME_ARRAY;//欢迎页面图片资源数组
	public static Bitmap [] MAIN_AN_ARRAY;//MAINVIEW中按钮图片资源数组
	
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//缩放图片的方法
	{
	   	float width = bm.getWidth(); 	//图片宽度
	   	float height = bm.getHeight();	//图片高度
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(xRatio, yRatio);   	
	   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图   
	   	return bmResult;
	}
	public static void initPicture(Resources res)
	{//初始化图片数组
		BGCOLOR_ARRAY = new Bitmap[BG_PIC_ID.length];
		BGCOLOR_ARRAY_BIG=new Bitmap[BG_PIC_ID.length];
		SETUP_ARRAY = new Bitmap[SETUP_AN_ID.length];
		WELCOME_ARRAY = new Bitmap[WELCOME_PIC_ID.length];
		MAIN_AN_ARRAY = new Bitmap[MAIN_AN_ID.length];
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//处理图片以适应本机屏幕分别为图片数组
			BGCOLOR_ARRAY[i]=BitmapFactory.decodeResource(res, BG_PIC_ID[i]);
			BGCOLOR_ARRAY[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<SETUP_AN_ID.length;i++)
		{//处理图片以适应本机屏幕分别为欢迎页图片和MAINVIEW中按钮图片数组
			SETUP_ARRAY[i]=BitmapFactory.decodeResource(res, SETUP_AN_ID[i]);
			SETUP_ARRAY[i]=Constant.scaleToFitXYRatio(SETUP_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<WELCOME_PIC_ID.length;i++)
		{//处理图片以适应本机屏幕分别为欢迎页图片和MAINVIEW中按钮图片数组
			WELCOME_ARRAY[i]=BitmapFactory.decodeResource(res, WELCOME_PIC_ID[i]);
			WELCOME_ARRAY[i]=Constant.scaleToFitXYRatio(WELCOME_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<MAIN_AN_ID.length;i++)
		{
			MAIN_AN_ARRAY[i]=BitmapFactory.decodeResource(res, MAIN_AN_ID[i]);
			MAIN_AN_ARRAY[i]=Constant.scaleToFitXYRatio(MAIN_AN_ARRAY[i], SCALE,SCALE);
		}		
	}
	public static float an_xOffset=90;//按钮的x偏移量
	public static float an_yOffset=5; //按钮的y偏移量
	public static float bg_Offset=10; //背景按钮的y偏移量
	public static int jg_Num=6;       //按钮分割空白的数量

	public static float su_xOffset=90;//设置按钮的x偏移量
	public static float su_yOffset=60; //设置按钮的y偏移量
	public static int sujg_Num=4;       //设置按钮分割空白的数量
	public static float choicebg_xOffset=100;//设置背景界面的提示语图片""的x偏移量
	public static float choicebg_yOffset=5;//设置背景界面的提示语图片""的y偏移量
	public static float sh_xOffset=35;//设置背景界面的沙画图片""的x偏移量
	public static float sh_yOffset=20;//设置背景界面的沙画图片""的y偏移量
	public static float shqOffset=5;//进行沙画区域的xy偏移量		

	public static float [] [] getPicLocationMsg()
	{
		PIC_LOCATION_MSG=new float [][]
		{
				{//填沙按钮的xy:PIC_LOCATION_MSG[0]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num					
				},
				{//清沙按钮的xy:PIC_LOCATION_MSG[1]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*1	
				},
				{//撤销按钮的xy:PIC_LOCATION_MSG[2]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*2	
				},
				{//背景灯按钮的xy:PIC_LOCATION_MSG[3]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*3	
				},
				{//设置按钮的xy:PIC_LOCATION_MSG[4]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*4	
				},
				{//矩形的xy四点:PIC_LOCATION_MSG[5]
					//shqOffset*SCALE,shqOffset*SCALE,w-100*SCALE,h-3
					0,0,SCREEN_WIDTH-100*SCALE,SCREEN_HEIGHT-3
				},
				{//欢迎图片的xy:PIC_LOCATION_MSG[6]
					(SCREEN_WIDTH-WELCOME_ARRAY[0].getWidth())/2,(SCREEN_HEIGHT-WELCOME_ARRAY[0].getHeight())/2
				},
				{//设置图片"新建画板"的xy:PIC_LOCATION_MSG[7]
					su_xOffset*SCALE,su_yOffset*SCALE
				},
				{//设置图片"保存画板"的xy:PIC_LOCATION_MSG[8]
					su_xOffset*SCALE,su_yOffset*SCALE+(SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight()
				},
				{//设置图片"参数设置"的xy:PIC_LOCATION_MSG[9]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*2
				},
				{//设置图片"作品集"的xy:PIC_LOCATION_MSG[10]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*3
				},
				{//设置图片"退出"的xy:PIC_LOCATION_MSG[11]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*4
				},
				{//设置图片""的xy:PIC_LOCATION_MSG[12]
					(SCREEN_WIDTH+SETUP_ARRAY[0].getWidth()+su_xOffset*SCALE-SETUP_ARRAY[5].getWidth())/2,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//设置背景界面的沙画图片""的xy:PIC_LOCATION_MSG[13]
					sh_xOffset*SCALE,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+sh_yOffset*SCALE
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[14]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[15]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[16]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[17]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[18]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[19]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[20]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的背景颜色图片""的xy:PIC_LOCATION_MSG[21]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//设置背景界面的文字图片""的xy:PIC_LOCATION_MSG[22]
					choicebg_xOffset*SCALE,choicebg_yOffset*SCALE
				}
		};	
		
		AREA_WIDTH=(int)(PIC_LOCATION_MSG[5][2]-PIC_LOCATION_MSG[5][0]);	//沙画区域宽度
		AREA_HEIGHT=(int)(PIC_LOCATION_MSG[5][3]-PIC_LOCATION_MSG[5][1]);//沙画区域高度
		pic_w=444;		//小图片宽
		pic_h=pic_w*AREA_HEIGHT/AREA_WIDTH;		//小图片高		
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//处理图片以适应本机屏幕分别为图片数组
			float xratio=PIC_LOCATION_MSG[5][2]/BGCOLOR_ARRAY[1].getWidth();
			float yratio=PIC_LOCATION_MSG[5][3]/BGCOLOR_ARRAY[1].getHeight();
			BGCOLOR_ARRAY_BIG[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], xratio,yratio);
		}
		
		return PIC_LOCATION_MSG;
	}
}
