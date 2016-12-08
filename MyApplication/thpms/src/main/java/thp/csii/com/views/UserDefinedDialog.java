package thp.csii.com.views;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;


public class UserDefinedDialog extends Dialog
{
	private Context ctx;
	private String msg;
	private Button btnleft, btncenter, btnright;
	private boolean IsTwoButton = false;
	private View.OnClickListener okListener;
	private View.OnClickListener cancelListener;
	
	private TextView tvtitle,tvcontent;

	public UserDefinedDialog(Context context, String message, View.OnClickListener onclicklistener, View.OnClickListener cancelListener)
	{
		super(context, R.style.dialog);
		this.ctx = context;
		this.msg = message;
		if (onclicklistener != null) 
		{
			this.okListener=onclicklistener;
		}
		if(cancelListener != null)
		{
			IsTwoButton=true;
			this.cancelListener=cancelListener;
		}
	}

	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iognore);
		
		
//		tvtitle = (TextView) findViewById(R.id.dialogtitle);
		tvcontent = (TextView) findViewById(R.id.tv_ignore);
//
//		btnleft = (Button) findViewById(R.id.btnleft);
//		btnright = (Button) findViewById(R.id.btnright);
//
//		btnleft.setOnClickListener(this);
//		btnright.setOnClickListener(this);
//
//		btncenter = (Button) findViewById(R.id.btncenter);
//		btncenter.setOnClickListener(this);
//		if(IsTwoButton)
//		{
//			btnleft.setVisibility(View.VISIBLE);
//			btnright.setVisibility(View.VISIBLE);
			//btncenter.setVisibility(View.GONE);
//		}
	//	tvtitle.setText("提示");
		tvcontent.setText(msg);
//
//		WindowManager m = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
//		Display d = m.getDefaultDisplay();
//		LayoutParams p = getWindow().getAttributes();
//		p.height = (int) (d.getHeight() * 0.3);
//		p.width = (int) (d.getWidth() * 0.9);
//		p.alpha = 0.8f;
//		p.dimAmount = 0.7f;
//		getWindow().setAttributes(p);
//		getWindow().setGravity(Gravity.CENTER);
		
	}

	public void setMessage(String msg){
		tvcontent.setText(msg);
	}

}
