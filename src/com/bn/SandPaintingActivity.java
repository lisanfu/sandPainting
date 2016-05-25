package com.bn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import static com.bn.Constant.*;

enum WhichView {WELCOME_VIEW,MAIN_VIEW,SETUP_VIEW,BGCOLOR_VIEW,PARAMSSET_VIEW,GALLERY_VIEW}//所有VIEW的列表
public class SandPaintingActivity extends Activity 
{

	AttributeSet attrs;
	private ShowGalleryView sgView;
	private WelcomeView myWelcomeView;	//声明欢迎页引用
	private MainView myMainView;		//声明绘制页引用
	private SetupView mySetupView;		//声明主设置页引用
	private BgColorView myBgColorView;	//声明背景设置页引用
	WhichView curr;						//当前所在view
	Dialog nameInputdialog; 
	String name;						//声明所选择的沙画名引用
	//声明Dialog引用
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what==1)
			{			//从欢迎页跳转到主页面
				if(myWelcomeView!=null)
				{
					myWelcomeView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==2)
			{		//从main页面跳转到主设置页面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoSetupView(); 
			}
			else if(msg.what==3)
			{		//从背景选择页面跳转到主页面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==4)
			{		//通过main页面的“背景”按钮跳转到背景选择页面
				if(myBgColorView!=null)
				{
					myBgColorView=null;
				}
				gotoBgColorView(); 
			}
			else if(msg.what==5)
			{		//通过main页面的“画笔设置”按钮进行画笔和喷砂半径设置页面的跳转
				gotoParamsSetView(); 
			}
			else if(msg.what==6)
			{
				showSaveDiaolg();		//显示保存对话框
				
			}
			else if(msg.what==7)
			{
				galleryPageNo=0;
				galleryPageCount=history.size()/galleryPageSpan;
				if(history.size()%galleryPageSpan!=0)
				{
					galleryPageCount=galleryPageCount+1;
				}
				gotoGalleryView();			//显示画廊
			}
			else if(msg.what==8)
			{	//从作品集页跳转到主页面
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView();//跳转到显示页面
			}
			else if(msg.what==9)//删除指定作品
			{
				Bundle b=msg.getData();
				name=b.getString("name");
				showDialog(DELETE_DIALOG_ID);
			}		
			else if(msg.what==10){//点击作品集上一页跳转
				prePage();				
			}
			else if(msg.what==11){//点击作品集上一页跳转
				nextPage();
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);          
        //下两句为设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {//得到实际屏幕width和height并进行切屏转换
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
		Constant.getRatio();			//得到缩放比		
		Constant.initPicture(this.getResources());//处理图片以适应屏幕
		Constant.getPicLocationMsg();	//初始化图片信息包括x,y,(w,h)

        
		gotoWelcomeView();				//跳转到欢迎页
		myWelcomeView.requestFocus();	//获取焦点
		myWelcomeView.setFocusableInTouchMode(true);//设置为可触控
    }
    
    public void gotoWelcomeView()
    {		//跳转到欢迎界面
    	if(null==myWelcomeView)
    	{
        	myWelcomeView= new WelcomeView(this);
    	}
        setContentView(myWelcomeView);
    	curr=WhichView.WELCOME_VIEW;
    }
    public void gotoMainView()
    {			//跳转到Main界面
    	if(myMainView==null)
    	{
    		myMainView = new MainView(SandPaintingActivity.this);
    	}
		SandPaintingActivity.this.setContentView(myMainView);
		curr=WhichView.MAIN_VIEW;
    }
    public void gotoSetupView()
    {		//跳转到设置界面
    	if(null==mySetupView)
    	{
        	mySetupView = new SetupView(this);
    	}
		SandPaintingActivity.this.setContentView(mySetupView);
		curr=WhichView.SETUP_VIEW;
    }
    public void gotoBgColorView()
    {		//跳转到背景界面
    	if(null==myBgColorView)
    	{
        	myBgColorView = new BgColorView(this);
    	}
		SandPaintingActivity.this.setContentView(myBgColorView);
		curr=WhichView.BGCOLOR_VIEW;	//设置当前View为参数设置View
    }
    public void showSaveDiaolg()
    {		//显示保存对话框
    	showDialog(NAME_INPUT_DIALOG_ID);
    }	 
    
    public void nextPage()
    {//跳转到下一页，循环跳转，当在最后一页跳转时自动跳转到第一页
    	galleryPageNo=(galleryPageNo+1)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }
    
    public void prePage()
    {//跳转到上一页，循环跳转，当从第一页跳转时，将自动跳转到最后一页
    	galleryPageNo=(galleryPageNo-1+galleryPageCount)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }    
    
    public void flushHistory()
    {//刷新历史记录
    	if(alr!=null)
		{//这里是回收bitmap的缓存，这样可以优化系统
			for(Record r:alr)
			{//将record存储的已经画了的每一笔回收
				r.bmResult.recycle();
			}
		}    	
    	int start=galleryPageNo*galleryPageSpan;//计算每一页的第一幅图片的索引
    	int count=0;
		
		ArrayList<Record> alr=new ArrayList<Record>();//声明沙画图片信息Record的ArrayList
		ArrayList<String> aln=new ArrayList<String>();//声明沙画图片名的ArrayList
		Set<String> ks=history.keySet();
		for(String key:ks)
		{
			if(count>=start&&count<start+galleryPageSpan)
			{//根据页数取出相应的没夜的图，每次只缓存一页的图片，这样就不会造成内存溢出
				byte[] data=history.get(key);
				Record rTemp=Record.fromBytesToRecord(data);
				if(rTemp.bmResult==null)
				{
					throw new RuntimeException("hua kong");
				}
				aln.add(key);//将该页的数据存进名称列
				alr.add(rTemp);//将图片数据存进图片列
			}
			count++;
		}			
		Constant.alr=alr;//将其赋值给静态数据
		Constant.aln=aln;
    }
	
	public void gotoGalleryView()
	{
		flushHistory();
		SandPaintingActivity.this.setContentView(R.layout.showgallerymain);
        sgView=(ShowGalleryView)findViewById(R.id.sgView);//获取显示图片的View的对象并在其中绘制图片     
		curr=WhichView.GALLERY_VIEW;
	}
	
    @Override
    public Dialog onCreateDialog(int id)//创建对话框
    {    	
        Dialog result=null;
    	switch(id)
    	{
	    	case NAME_INPUT_DIALOG_ID://姓名输入对话框
		    	nameInputdialog=new MyDialog(this); 	    
				result=nameInputdialog;				
			break;	
	    	case DELETE_DIALOG_ID://长按图片是否删除它，如果选择确定将删除相应沙画
	    		Builder b=new AlertDialog.Builder(this);  
	    		  b.setMessage("是否删除该图片？");//设置信息
	    		  b.setPositiveButton//为对话框设置按钮
	    		  (
	    				"确定", 
	    				new DialogInterface.OnClickListener()
		        		{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(SandPaintingActivity.this, "删除选中的项目"+name, Toast.LENGTH_SHORT).show();					
								history.remove(name);//删除这一名字的沙画
								gotoGalleryView();
							}      			
		        		}
	    		  );
	    		  b.setNegativeButton
	    		  (
	    				"取消",
	    				new DialogInterface.OnClickListener()
	    				{
	    					public void onClick(DialogInterface dialog, int which){}
						}
	    		  );
	    		  result=b.create();
	    	break;
    	}   
		return result;
    }
    
    //每次弹出对话框时被回调以动态更新对话框内容的方法
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//若不是等待对话框则返回
    	switch(id)
    	{
    	   case NAME_INPUT_DIALOG_ID://姓名输入对话框
    		   //确定按钮
    		   Button bjhmcok=(Button)nameInputdialog.findViewById(R.id.saveOk);
    		   //取消按钮
       		   Button bjhmccancel=(Button)nameInputdialog.findViewById(R.id.saveCancle);
       		   //给确定按钮添加监听器
       		   bjhmcok.setOnClickListener
               (
    	          new OnClickListener()
    	          {
    				@Override
    				public void onClick(View v) 
    				{
    					//获取对话框里的内容并用Toast显示
    					EditText et=(EditText)nameInputdialog.findViewById(R.id.etname);
    					
    					String name=et.getText().toString();    					
    					Record r=new Record();
    					history.put(name, r.toBytes()); 
    					Toast.makeText
    					(
    						SandPaintingActivity.this,
    						"成功保存进缓冲！", 
    						Toast.LENGTH_SHORT
    					).show(); 
    					nameInputdialog.cancel();
    					
    					//每次恢复自动读取
    					try
    			    	{
    			    		File f=new File("/sdcard/sp.data");
    			    		FileInputStream fin=new FileInputStream(f);    			    		
    			    		fin.close();
    			    	}
    			    	catch(Exception e)
    			    	{
    			    		Toast.makeText
    			    		(
    			    			SandPaintingActivity.this, 
    			    			"不能正确读写SD卡，程序退出后\n数据有可能会丢失！", 
    			    			Toast.LENGTH_SHORT
    			    		).show();
    			    	}
    				}        	  
    	          }
    	        );   
       		    //给取消按钮添加监听器
       		    bjhmccancel.setOnClickListener
	            (
	 	          new OnClickListener()
	 	          {
	 				@Override
	 				public void onClick(View v) 
	 				{
	 					//关闭对话框
	 					nameInputdialog.cancel();					
	 				}        	  
	 	          }
	 	        );   
    	   break;	
    	}
    }    
    public void gotoParamsSetView()
    {	//跳转到参数设置页面，使用xml组成
		this.setContentView(R.layout.paramset);
		SeekBar sbHB=(SeekBar)this.findViewById(R.id.SeekBar01);//得到画笔半径SeekBar引用			
        int currValue=(int) ((hbSize-HBMIN)/(HBMAX-HBMIN)*100);
		sbHB.setProgress(currValue);
		sbHB.setOnSeekBarChangeListener(//为画笔半径SeekBar设置监听
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//将画笔半径设置为拖拉后的值	
					float f=progress;
					hbSize=(float) (HBMIN+(HBMAX-HBMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	        
        SeekBar sbTCL=(SeekBar)this.findViewById(R.id.SeekBar02);//得到填充率SeekBar引用	
        currValue=(int) ((tcl-TCLMIN)/(TCLMAX-TCLMIN)*100);
		sbTCL.setProgress(currValue);
        sbTCL.setOnSeekBarChangeListener(//为填充率SeekBar设置监听
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//将填充率设置为拖拉后的值					
					float f=progress;
					tcl=(float) (TCLMIN+(TCLMAX-TCLMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	    
		curr=WhichView.PARAMSSET_VIEW;		//设置当前View为参数设置View
    }
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {//点击返回键进行跳转    	
    	if(keyCode==4)
    	{	
    		if(curr==WhichView.BGCOLOR_VIEW){//从背景设置View跳转到绘制View
    			gotoMainView();
			}
    		else if(curr==WhichView.SETUP_VIEW){//从主设置View跳转到绘制View
    			gotoMainView();
    		}
    		else if(curr==WhichView.PARAMSSET_VIEW){//从参数设置View跳转到绘制View
    			gotoMainView();
    		}
    		else if(curr==WhichView.GALLERY_VIEW){//从画廊View跳转到绘制View
    			gotoMainView();
    		}
    		else{							//退出杀死线程
    			this.finish();
    			new Thread()
    			{
    				public void run()
    				{
    					try {
							Thread.sleep(1000);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
    					System.exit(0); 
    				}
    			}.start();	    		  
			} 		
    	}    	
    	return true;
    }

	@Override
	protected void onPause() 
	{
		super.onPause();
		//每次暂停自动存盘
		try
    	{
    		File f=new File("/sdcard/sp.data");
    		FileOutputStream fout=new FileOutputStream(f);
    		ObjectOutputStream oout=new ObjectOutputStream(fout);
    		oout.writeObject(Constant.history);
    		oout.close();
    		fout.close();
    	}
    	catch(Exception e)
    	{    		
    		e.printStackTrace();
    	}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() 
	{
		super.onResume();
		//每次恢复自动读取
		try
    	{
    		File f=new File("/sdcard/sp.data");
    		FileInputStream fin=new FileInputStream(f);
    		ObjectInputStream oin=new ObjectInputStream(fin);    		
    		Constant.history=(LinkedHashMap<String,byte[]>)oin.readObject();
    		oin.close();
    		fin.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
   
    
    
}
