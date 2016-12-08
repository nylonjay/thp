package thp.csii.com.callback;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/28.
 */
public class EditChangedListener implements TextWatcher{
    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置
    private final int charMaxNum = 6;
    private EditText mEditTextMsg;
    private Handler hand;

    public EditChangedListener(EditText mEditTextMsg, Handler handler) {
        this.mEditTextMsg = mEditTextMsg;
        this.hand=handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       // mTvAvailableCharNum.setText("还能输入" + (charMaxNum - s.length()) + "字符");
        if ((charMaxNum-s.length())==0){
            hand.sendEmptyMessage(9);
        }else{
            hand.sendEmptyMessage(8);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
        editStart = mEditTextMsg.getSelectionStart();
        editEnd = mEditTextMsg.getSelectionEnd();
        if (temp.length() > charMaxNum) {
          //  Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
            s.delete(editStart - 1, editEnd);
            int tempSelection = editStart;
            mEditTextMsg.setText(s);
            mEditTextMsg.setSelection(tempSelection);
        }

    }
}
