package thp.csii.com.views;

import android.app.Activity;
import android.util.DisplayMetrics;

public class WindowInfo {
	private DisplayMetrics displayMetrics;
	private Activity context;

	public WindowInfo(Activity context) {
		this.context = context;
		this.displayMetrics = getDisplayMetrics();
	}

	private DisplayMetrics getDisplayMetrics() {
		if (displayMetrics == null) {
			displayMetrics = new DisplayMetrics();
		}
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics;
	}

	public int getWindowHeight() {
		return getDisplayMetrics().heightPixels;
	}

	public int getWindowWidth() {
		return getDisplayMetrics().widthPixels;
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
