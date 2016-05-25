package com.bn;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog 
{
	public MyDialog(Context context) {
        super(context,R.style.FullHeightDialog);
    }
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		this.setContentView(R.layout.dialog_name_input);
	}
	
	@Override
	public String toString()
	{
		return "MyDialog";
	}
}
