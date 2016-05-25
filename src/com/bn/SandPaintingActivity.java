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

enum WhichView {WELCOME_VIEW,MAIN_VIEW,SETUP_VIEW,BGCOLOR_VIEW,PARAMSSET_VIEW,GALLERY_VIEW}//����VIEW���б�
public class SandPaintingActivity extends Activity 
{

	AttributeSet attrs;
	private ShowGalleryView sgView;
	private WelcomeView myWelcomeView;	//������ӭҳ����
	private MainView myMainView;		//��������ҳ����
	private SetupView mySetupView;		//����������ҳ����
	private BgColorView myBgColorView;	//������������ҳ����
	WhichView curr;						//��ǰ����view
	Dialog nameInputdialog; 
	String name;						//������ѡ���ɳ��������
	//����Dialog����
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what==1)
			{			//�ӻ�ӭҳ��ת����ҳ��
				if(myWelcomeView!=null)
				{
					myWelcomeView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==2)
			{		//��mainҳ����ת��������ҳ��
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoSetupView(); 
			}
			else if(msg.what==3)
			{		//�ӱ���ѡ��ҳ����ת����ҳ��
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView(); 
			}
			else if(msg.what==4)
			{		//ͨ��mainҳ��ġ���������ť��ת������ѡ��ҳ��
				if(myBgColorView!=null)
				{
					myBgColorView=null;
				}
				gotoBgColorView(); 
			}
			else if(msg.what==5)
			{		//ͨ��mainҳ��ġ��������á���ť���л��ʺ���ɰ�뾶����ҳ�����ת
				gotoParamsSetView(); 
			}
			else if(msg.what==6)
			{
				showSaveDiaolg();		//��ʾ����Ի���
				
			}
			else if(msg.what==7)
			{
				galleryPageNo=0;
				galleryPageCount=history.size()/galleryPageSpan;
				if(history.size()%galleryPageSpan!=0)
				{
					galleryPageCount=galleryPageCount+1;
				}
				gotoGalleryView();			//��ʾ����
			}
			else if(msg.what==8)
			{	//����Ʒ��ҳ��ת����ҳ��
				if(myMainView!=null)
				{
					myMainView=null;
				}
				gotoMainView();//��ת����ʾҳ��
			}
			else if(msg.what==9)//ɾ��ָ����Ʒ
			{
				Bundle b=msg.getData();
				name=b.getString("name");
				showDialog(DELETE_DIALOG_ID);
			}		
			else if(msg.what==10){//�����Ʒ����һҳ��ת
				prePage();				
			}
			else if(msg.what==11){//�����Ʒ����һҳ��ת
				nextPage();
			}
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);          
        //������Ϊ����ȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        if(dm.widthPixels>dm.heightPixels)
        {//�õ�ʵ����Ļwidth��height����������ת��
        	 SCREEN_WIDTH=dm.widthPixels;
             SCREEN_HEIGHT=dm.heightPixels;
        }
        else
        {
        	SCREEN_WIDTH=dm.heightPixels;
            SCREEN_HEIGHT=dm.widthPixels;
        }
		Constant.getRatio();			//�õ����ű�		
		Constant.initPicture(this.getResources());//����ͼƬ����Ӧ��Ļ
		Constant.getPicLocationMsg();	//��ʼ��ͼƬ��Ϣ����x,y,(w,h)

        
		gotoWelcomeView();				//��ת����ӭҳ
		myWelcomeView.requestFocus();	//��ȡ����
		myWelcomeView.setFocusableInTouchMode(true);//����Ϊ�ɴ���
    }
    
    public void gotoWelcomeView()
    {		//��ת����ӭ����
    	if(null==myWelcomeView)
    	{
        	myWelcomeView= new WelcomeView(this);
    	}
        setContentView(myWelcomeView);
    	curr=WhichView.WELCOME_VIEW;
    }
    public void gotoMainView()
    {			//��ת��Main����
    	if(myMainView==null)
    	{
    		myMainView = new MainView(SandPaintingActivity.this);
    	}
		SandPaintingActivity.this.setContentView(myMainView);
		curr=WhichView.MAIN_VIEW;
    }
    public void gotoSetupView()
    {		//��ת�����ý���
    	if(null==mySetupView)
    	{
        	mySetupView = new SetupView(this);
    	}
		SandPaintingActivity.this.setContentView(mySetupView);
		curr=WhichView.SETUP_VIEW;
    }
    public void gotoBgColorView()
    {		//��ת����������
    	if(null==myBgColorView)
    	{
        	myBgColorView = new BgColorView(this);
    	}
		SandPaintingActivity.this.setContentView(myBgColorView);
		curr=WhichView.BGCOLOR_VIEW;	//���õ�ǰViewΪ��������View
    }
    public void showSaveDiaolg()
    {		//��ʾ����Ի���
    	showDialog(NAME_INPUT_DIALOG_ID);
    }	 
    
    public void nextPage()
    {//��ת����һҳ��ѭ����ת���������һҳ��תʱ�Զ���ת����һҳ
    	galleryPageNo=(galleryPageNo+1)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }
    
    public void prePage()
    {//��ת����һҳ��ѭ����ת�����ӵ�һҳ��תʱ�����Զ���ת�����һҳ
    	galleryPageNo=(galleryPageNo-1+galleryPageCount)%galleryPageCount;
    	flushHistory();
    	sgView.postInvalidate();
    }    
    
    public void flushHistory()
    {//ˢ����ʷ��¼
    	if(alr!=null)
		{//�����ǻ���bitmap�Ļ��棬���������Ż�ϵͳ
			for(Record r:alr)
			{//��record�洢���Ѿ����˵�ÿһ�ʻ���
				r.bmResult.recycle();
			}
		}    	
    	int start=galleryPageNo*galleryPageSpan;//����ÿһҳ�ĵ�һ��ͼƬ������
    	int count=0;
		
		ArrayList<Record> alr=new ArrayList<Record>();//����ɳ��ͼƬ��ϢRecord��ArrayList
		ArrayList<String> aln=new ArrayList<String>();//����ɳ��ͼƬ����ArrayList
		Set<String> ks=history.keySet();
		for(String key:ks)
		{
			if(count>=start&&count<start+galleryPageSpan)
			{//����ҳ��ȡ����Ӧ��ûҹ��ͼ��ÿ��ֻ����һҳ��ͼƬ�������Ͳ�������ڴ����
				byte[] data=history.get(key);
				Record rTemp=Record.fromBytesToRecord(data);
				if(rTemp.bmResult==null)
				{
					throw new RuntimeException("hua kong");
				}
				aln.add(key);//����ҳ�����ݴ��������
				alr.add(rTemp);//��ͼƬ���ݴ��ͼƬ��
			}
			count++;
		}			
		Constant.alr=alr;//���丳ֵ����̬����
		Constant.aln=aln;
    }
	
	public void gotoGalleryView()
	{
		flushHistory();
		SandPaintingActivity.this.setContentView(R.layout.showgallerymain);
        sgView=(ShowGalleryView)findViewById(R.id.sgView);//��ȡ��ʾͼƬ��View�Ķ��������л���ͼƬ     
		curr=WhichView.GALLERY_VIEW;
	}
	
    @Override
    public Dialog onCreateDialog(int id)//�����Ի���
    {    	
        Dialog result=null;
    	switch(id)
    	{
	    	case NAME_INPUT_DIALOG_ID://��������Ի���
		    	nameInputdialog=new MyDialog(this); 	    
				result=nameInputdialog;				
			break;	
	    	case DELETE_DIALOG_ID://����ͼƬ�Ƿ�ɾ���������ѡ��ȷ����ɾ����Ӧɳ��
	    		Builder b=new AlertDialog.Builder(this);  
	    		  b.setMessage("�Ƿ�ɾ����ͼƬ��");//������Ϣ
	    		  b.setPositiveButton//Ϊ�Ի������ð�ť
	    		  (
	    				"ȷ��", 
	    				new DialogInterface.OnClickListener()
		        		{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(SandPaintingActivity.this, "ɾ��ѡ�е���Ŀ"+name, Toast.LENGTH_SHORT).show();					
								history.remove(name);//ɾ����һ���ֵ�ɳ��
								gotoGalleryView();
							}      			
		        		}
	    		  );
	    		  b.setNegativeButton
	    		  (
	    				"ȡ��",
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
    
    //ÿ�ε����Ի���ʱ���ص��Զ�̬���¶Ի������ݵķ���
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//�����ǵȴ��Ի����򷵻�
    	switch(id)
    	{
    	   case NAME_INPUT_DIALOG_ID://��������Ի���
    		   //ȷ����ť
    		   Button bjhmcok=(Button)nameInputdialog.findViewById(R.id.saveOk);
    		   //ȡ����ť
       		   Button bjhmccancel=(Button)nameInputdialog.findViewById(R.id.saveCancle);
       		   //��ȷ����ť��Ӽ�����
       		   bjhmcok.setOnClickListener
               (
    	          new OnClickListener()
    	          {
    				@Override
    				public void onClick(View v) 
    				{
    					//��ȡ�Ի���������ݲ���Toast��ʾ
    					EditText et=(EditText)nameInputdialog.findViewById(R.id.etname);
    					
    					String name=et.getText().toString();    					
    					Record r=new Record();
    					history.put(name, r.toBytes()); 
    					Toast.makeText
    					(
    						SandPaintingActivity.this,
    						"�ɹ���������壡", 
    						Toast.LENGTH_SHORT
    					).show(); 
    					nameInputdialog.cancel();
    					
    					//ÿ�λָ��Զ���ȡ
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
    			    			"������ȷ��дSD���������˳���\n�����п��ܻᶪʧ��", 
    			    			Toast.LENGTH_SHORT
    			    		).show();
    			    	}
    				}        	  
    	          }
    	        );   
       		    //��ȡ����ť��Ӽ�����
       		    bjhmccancel.setOnClickListener
	            (
	 	          new OnClickListener()
	 	          {
	 				@Override
	 				public void onClick(View v) 
	 				{
	 					//�رնԻ���
	 					nameInputdialog.cancel();					
	 				}        	  
	 	          }
	 	        );   
    	   break;	
    	}
    }    
    public void gotoParamsSetView()
    {	//��ת����������ҳ�棬ʹ��xml���
		this.setContentView(R.layout.paramset);
		SeekBar sbHB=(SeekBar)this.findViewById(R.id.SeekBar01);//�õ����ʰ뾶SeekBar����			
        int currValue=(int) ((hbSize-HBMIN)/(HBMAX-HBMIN)*100);
		sbHB.setProgress(currValue);
		sbHB.setOnSeekBarChangeListener(//Ϊ���ʰ뾶SeekBar���ü���
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//�����ʰ뾶����Ϊ�������ֵ	
					float f=progress;
					hbSize=(float) (HBMIN+(HBMAX-HBMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	        
        SeekBar sbTCL=(SeekBar)this.findViewById(R.id.SeekBar02);//�õ������SeekBar����	
        currValue=(int) ((tcl-TCLMIN)/(TCLMAX-TCLMIN)*100);
		sbTCL.setProgress(currValue);
        sbTCL.setOnSeekBarChangeListener(//Ϊ�����SeekBar���ü���
            new SeekBar.OnSeekBarChangeListener()
            {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{//�����������Ϊ�������ֵ					
					float f=progress;
					tcl=(float) (TCLMIN+(TCLMAX-TCLMIN)*f/100.0f);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) { }            	
            }
        );	    
		curr=WhichView.PARAMSSET_VIEW;		//���õ�ǰViewΪ��������View
    }
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {//������ؼ�������ת    	
    	if(keyCode==4)
    	{	
    		if(curr==WhichView.BGCOLOR_VIEW){//�ӱ�������View��ת������View
    			gotoMainView();
			}
    		else if(curr==WhichView.SETUP_VIEW){//��������View��ת������View
    			gotoMainView();
    		}
    		else if(curr==WhichView.PARAMSSET_VIEW){//�Ӳ�������View��ת������View
    			gotoMainView();
    		}
    		else if(curr==WhichView.GALLERY_VIEW){//�ӻ���View��ת������View
    			gotoMainView();
    		}
    		else{							//�˳�ɱ���߳�
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
		//ÿ����ͣ�Զ�����
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
		//ÿ�λָ��Զ���ȡ
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
