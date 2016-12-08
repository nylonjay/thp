package thp.csii.com.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;

import thp.csii.com.R;

public class MyDialog extends BaseDialog {

	public MyDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.mystyle);
	}

	@Override
	protected void onTouchOutside() {
	}
}
