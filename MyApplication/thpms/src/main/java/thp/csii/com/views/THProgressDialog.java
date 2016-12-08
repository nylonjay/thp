package thp.csii.com.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import thp.csii.com.R;


/**
 * 自定义加载框
 * 
 * @author 刘建久
 * 
 */
public class THProgressDialog extends Dialog {
	/** 加载框动画*/
	private Animation mAnimationRate;
	/** 动画控件*/
	private ImageView mImageView;
	
	private Activity mActivity;
	
	private THProgressDialog(Context context, int theme) {
		super(context, theme);
		
		mActivity = (Activity) context;
		mAnimationRate = AnimationUtils.loadAnimation(context, R.anim.progess);
	}

	public static THProgressDialog createDialog(Context context) {
		THProgressDialog mTHProgressDialog = new THProgressDialog(context, R.style.CustomProgressDialog);
		mTHProgressDialog.setCancelable(true);
		mTHProgressDialog.setCanceledOnTouchOutside(true);
		mTHProgressDialog.setContentView(R.layout.dialog_progess);
		mTHProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return mTHProgressDialog;
	}

	@Override
	public void show() {
		if(!mActivity.isFinishing()&&!isShowing()){
			super.show();
			if(mImageView == null){
				mImageView = (ImageView) findViewById(R.id.loadingImageView);
			}
			mImageView.startAnimation(mAnimationRate);
		}
	}
	
	
	/**
	 * 设置提示内容 *
	 * 
	 * @param strMessage
	 *            提示内容
	 * @return
	 */
	public THProgressDialog setMessage(String strMessage) {
		//不显示文字
		return this;
	}
	
	/**
	 * 设置提示内容 *
	 * 
	 * @param strMessage
	 *            提示内容
	 * @return
	 */
	public THProgressDialog setMessage(int str_id) {
		return this;
	}
	
	@Override
	public void dismiss() {
		if(!mActivity.isFinishing()&&isShowing()){

			if(mImageView == null){
				mImageView = (ImageView) findViewById(R.id.loadingImageView);
			}
			mImageView.postDelayed(new Runnable() {

				@Override
				public void run() {
					THProgressDialog.super.dismiss();
				}
			},100);
		}
	}

	/**
	 *
	 * @param deley
	 */
	public void dismiss(boolean deley){

		if(deley){
			dismiss();
		}
		else{
			super.dismiss();
		}
	}
}
