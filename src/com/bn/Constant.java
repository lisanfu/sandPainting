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
	public static final int NAME_INPUT_DIALOG_ID=0;//ɳ����������Ի���id
	public static final int DELETE_DIALOG_ID=1;
	public static final float hs=480; 	//�ֻ���Ļ��׼���
	public static final float ws=800; 	//�ֻ���Ļ��׼�߶�
	public static float SCREEN_WIDTH;	//��Ļ���
	public static float SCREEN_HEIGHT;	//��Ļ�߶�
	public static float SCALE;	        //ʹ�õ����ű���
	public static float[][] PIC_LOCATION_MSG;
	public static int [] SANDCOLOR={196, 165, 43};//ɳ����ɫ������AtomAction�л�ȡ
	public static int state=0;			//��ǰ����״̬   0-��ɳ  1-��ɳ
	public static float hbSize=10;		//���ʰ뾶
	public static float tcl=40;			//�����  
	public static final float STANDARD_LENGTH=30; //����ÿ �ʵı�׼��λ����
	public final static float TCLMAX=100;//����ʵ����ֵ
	public final static float TCLMIN=10;//����ʵ���Сֵ
	public final static float HBMAX=20;	//���ʰ뾶�����ֵ
	public final static float HBMIN=5;	//���ʰ뾶����Сֵ
	public static float AREA_WIDTH;	//ɳ��������
	public static float AREA_HEIGHT;//ɳ������߶�
	public static float pic_w;		//СͼƬ��
	public static float pic_h;		//СͼƬ��
	public static float bitmap_between_width=5;//СͼƬ���
	public static Object actionLock=new Object();//������
	
	public static int galleryPageNo=0;//��ǰɳ��ҳ��
	public static int galleryPageSpan=6;//ÿҳ��ʾ��
	public static int galleryPageCount=0;//ɳ��������
	
	//�����б�
	static LinkedHashSet<AtomAction> alAction=new LinkedHashSet<AtomAction>();//ԭ�Ӷ���
	static ArrayList<ActionGroup> allActiongroup=new ArrayList<ActionGroup>();//�������
	static HashMap<Long,Bitmap> hmhb=new HashMap<Long,Bitmap>();//ͼƬ����洢λ��
	//��ǰͼ�񻺳�
	static Bitmap bmBuff;//Bitmap���������洢�Ѿ����˵�ÿһ��
	static Canvas canvasBuff; 
	
	//��ʷ��¼�б�
	static LinkedHashMap<String,byte[]> history=new LinkedHashMap<String,byte[]>();
	public static ArrayList<Record> alr;//�����洢ɳ����¼
	static ArrayList<String> aln;//�����洢ɳ����
	public static int[] SETUP_AN_ID = new int[]
  	{//SETUP�а�ťͼƬ��ԴID
  		R.drawable.setup0,R.drawable.setup1,
  		R.drawable.setup2,R.drawable.setup3,
  		R.drawable.setup4,R.drawable.s5
  	};
	public static int[] BG_PIC_ID = new int[]
	{//����ͼƬ��ԴID
		R.drawable.bj0,R.drawable.bj1,
		R.drawable.bj2,R.drawable.bj3,
		R.drawable.bj4,R.drawable.bj5,
		R.drawable.bj6,R.drawable.bj7
	};	
	public static int[] MAIN_AN_ID = new int[]
	{//MAINVIEW�а�ťͼƬ��ԴID
		R.drawable.a1,R.drawable.a2,
		R.drawable.a3,R.drawable.a4,
		R.drawable.a5
	};
	public static int[] WELCOME_PIC_ID = new int[]{//��ӭҳ��ͼƬ��ԴID
		R.drawable.z6,R.drawable.choicebg,
		R.drawable.zpjkong,R.drawable.zpj,
		R.drawable.prepage,R.drawable.nextpage
	};

	public static void getRatio(){		//�����ʵ��ʹ��ʱ�����ű�
		float wratio=SCREEN_WIDTH/ws;				//���������	
		float hratio=SCREEN_HEIGHT/hs;				//���������	
		if(wratio<hratio){
			SCALE=wratio;
		}else{
			SCALE=hratio; 
		}
		hbSize=SCALE*hbSize;		//���ʰ뾶ʵ��ֵ
	}
	public static Bitmap []BGCOLOR_ARRAY;//����ҳ��ͼƬ��Դ����
	public static Bitmap []BGCOLOR_ARRAY_BIG;//����ҳ��ͼƬ��Դ����
	public static Bitmap [] SETUP_ARRAY;//����ҳ��ͼƬ��Դ����
	public static Bitmap [] WELCOME_ARRAY;//��ӭҳ��ͼƬ��Դ����
	public static Bitmap [] MAIN_AN_ARRAY;//MAINVIEW�а�ťͼƬ��Դ����
	
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//����ͼƬ�ķ���
	{
	   	float width = bm.getWidth(); 	//ͼƬ���
	   	float height = bm.getHeight();	//ͼƬ�߶�
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(xRatio, yRatio);   	
	   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ   
	   	return bmResult;
	}
	public static void initPicture(Resources res)
	{//��ʼ��ͼƬ����
		BGCOLOR_ARRAY = new Bitmap[BG_PIC_ID.length];
		BGCOLOR_ARRAY_BIG=new Bitmap[BG_PIC_ID.length];
		SETUP_ARRAY = new Bitmap[SETUP_AN_ID.length];
		WELCOME_ARRAY = new Bitmap[WELCOME_PIC_ID.length];
		MAIN_AN_ARRAY = new Bitmap[MAIN_AN_ID.length];
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//����ͼƬ����Ӧ������Ļ�ֱ�ΪͼƬ����
			BGCOLOR_ARRAY[i]=BitmapFactory.decodeResource(res, BG_PIC_ID[i]);
			BGCOLOR_ARRAY[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<SETUP_AN_ID.length;i++)
		{//����ͼƬ����Ӧ������Ļ�ֱ�Ϊ��ӭҳͼƬ��MAINVIEW�а�ťͼƬ����
			SETUP_ARRAY[i]=BitmapFactory.decodeResource(res, SETUP_AN_ID[i]);
			SETUP_ARRAY[i]=Constant.scaleToFitXYRatio(SETUP_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<WELCOME_PIC_ID.length;i++)
		{//����ͼƬ����Ӧ������Ļ�ֱ�Ϊ��ӭҳͼƬ��MAINVIEW�а�ťͼƬ����
			WELCOME_ARRAY[i]=BitmapFactory.decodeResource(res, WELCOME_PIC_ID[i]);
			WELCOME_ARRAY[i]=Constant.scaleToFitXYRatio(WELCOME_ARRAY[i], SCALE,SCALE);
		}
		for(int i=0;i<MAIN_AN_ID.length;i++)
		{
			MAIN_AN_ARRAY[i]=BitmapFactory.decodeResource(res, MAIN_AN_ID[i]);
			MAIN_AN_ARRAY[i]=Constant.scaleToFitXYRatio(MAIN_AN_ARRAY[i], SCALE,SCALE);
		}		
	}
	public static float an_xOffset=90;//��ť��xƫ����
	public static float an_yOffset=5; //��ť��yƫ����
	public static float bg_Offset=10; //������ť��yƫ����
	public static int jg_Num=6;       //��ť�ָ�հ׵�����

	public static float su_xOffset=90;//���ð�ť��xƫ����
	public static float su_yOffset=60; //���ð�ť��yƫ����
	public static int sujg_Num=4;       //���ð�ť�ָ�հ׵�����
	public static float choicebg_xOffset=100;//���ñ����������ʾ��ͼƬ""��xƫ����
	public static float choicebg_yOffset=5;//���ñ����������ʾ��ͼƬ""��yƫ����
	public static float sh_xOffset=35;//���ñ��������ɳ��ͼƬ""��xƫ����
	public static float sh_yOffset=20;//���ñ��������ɳ��ͼƬ""��yƫ����
	public static float shqOffset=5;//����ɳ�������xyƫ����		

	public static float [] [] getPicLocationMsg()
	{
		PIC_LOCATION_MSG=new float [][]
		{
				{//��ɳ��ť��xy:PIC_LOCATION_MSG[0]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num					
				},
				{//��ɳ��ť��xy:PIC_LOCATION_MSG[1]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*1	
				},
				{//������ť��xy:PIC_LOCATION_MSG[2]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*2	
				},
				{//�����ư�ť��xy:PIC_LOCATION_MSG[3]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*3	
				},
				{//���ð�ť��xy:PIC_LOCATION_MSG[4]
					SCREEN_WIDTH-an_xOffset*SCALE,(SCREEN_HEIGHT-an_yOffset*MAIN_AN_ARRAY[0].getHeight())/jg_Num+(SCREEN_HEIGHT+MAIN_AN_ARRAY[0].getHeight())/jg_Num*4	
				},
				{//���ε�xy�ĵ�:PIC_LOCATION_MSG[5]
					//shqOffset*SCALE,shqOffset*SCALE,w-100*SCALE,h-3
					0,0,SCREEN_WIDTH-100*SCALE,SCREEN_HEIGHT-3
				},
				{//��ӭͼƬ��xy:PIC_LOCATION_MSG[6]
					(SCREEN_WIDTH-WELCOME_ARRAY[0].getWidth())/2,(SCREEN_HEIGHT-WELCOME_ARRAY[0].getHeight())/2
				},
				{//����ͼƬ"�½�����"��xy:PIC_LOCATION_MSG[7]
					su_xOffset*SCALE,su_yOffset*SCALE
				},
				{//����ͼƬ"���滭��"��xy:PIC_LOCATION_MSG[8]
					su_xOffset*SCALE,su_yOffset*SCALE+(SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight()
				},
				{//����ͼƬ"��������"��xy:PIC_LOCATION_MSG[9]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*2
				},
				{//����ͼƬ"��Ʒ��"��xy:PIC_LOCATION_MSG[10]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*3
				},
				{//����ͼƬ"�˳�"��xy:PIC_LOCATION_MSG[11]
					su_xOffset*SCALE,su_yOffset*SCALE+((SCREEN_HEIGHT-SETUP_ARRAY[0].getHeight()*5-2*su_yOffset*SCALE)/4+SETUP_ARRAY[0].getHeight())*4
				},
				{//����ͼƬ""��xy:PIC_LOCATION_MSG[12]
					(SCREEN_WIDTH+SETUP_ARRAY[0].getWidth()+su_xOffset*SCALE-SETUP_ARRAY[5].getWidth())/2,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//���ñ��������ɳ��ͼƬ""��xy:PIC_LOCATION_MSG[13]
					sh_xOffset*SCALE,(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+sh_yOffset*SCALE
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[14]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[15]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[16]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[17]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[18]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[19]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+2*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+2*BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[20]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+an_xOffset*SCALE,
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ�������ı�����ɫͼƬ""��xy:PIC_LOCATION_MSG[21]
					bg_Offset*SCALE+SETUP_ARRAY[5].getWidth()+2*an_xOffset*SCALE+BGCOLOR_ARRAY[0].getWidth(),
					(SCREEN_HEIGHT-SETUP_ARRAY[5].getHeight())/2+3*(SETUP_ARRAY[5].getHeight()-4*BGCOLOR_ARRAY[0].getHeight())/3+3*BGCOLOR_ARRAY[0].getHeight()
				},
				{//���ñ������������ͼƬ""��xy:PIC_LOCATION_MSG[22]
					choicebg_xOffset*SCALE,choicebg_yOffset*SCALE
				}
		};	
		
		AREA_WIDTH=(int)(PIC_LOCATION_MSG[5][2]-PIC_LOCATION_MSG[5][0]);	//ɳ��������
		AREA_HEIGHT=(int)(PIC_LOCATION_MSG[5][3]-PIC_LOCATION_MSG[5][1]);//ɳ������߶�
		pic_w=444;		//СͼƬ��
		pic_h=pic_w*AREA_HEIGHT/AREA_WIDTH;		//СͼƬ��		
		for(int i=0;i<BG_PIC_ID.length;i++)
		{//����ͼƬ����Ӧ������Ļ�ֱ�ΪͼƬ����
			float xratio=PIC_LOCATION_MSG[5][2]/BGCOLOR_ARRAY[1].getWidth();
			float yratio=PIC_LOCATION_MSG[5][3]/BGCOLOR_ARRAY[1].getHeight();
			BGCOLOR_ARRAY_BIG[i]=Constant.scaleToFitXYRatio(BGCOLOR_ARRAY[i], xratio,yratio);
		}
		
		return PIC_LOCATION_MSG;
	}
}
