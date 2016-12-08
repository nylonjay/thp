package thp.csii.com.callback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import cn.com.csii.mobile.http.util.LogUtil;

/**
 * Created by Administrator on 2016/11/1.
 */
public class PeedChangeListener implements TextWatcher{
    Handler hand;
    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置
    private EditText mEditTextMsg;
    private final int charMaxNum = 6;
    public PeedChangeListener(EditText mEditTextMsg, Handler handler) {
        this.mEditTextMsg = mEditTextMsg;
        this.hand=handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp=s;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Message msg=new Message();
        Bundle b=new Bundle();
        b.putString("slength",s.toString());
        msg.setData(b);
        //这个输入框有值
        msg.what=101;
        hand.sendMessage(msg);

        if ((charMaxNum-s.length())==0){
                hand.sendEmptyMessage(9);
        }
    }
    @Override
    public void afterTextChanged(Editable s) {
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
