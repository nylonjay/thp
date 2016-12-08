package thp.csii.com.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import thp.csii.com.BaseActivity;
import thp.csii.com.R;
import thp.csii.com.adapter.Bg_Grid_Adapter;

/**
*ChooseBgActivity  选择卡面背景
*@author nylon
 * created at 2016/9/9 17:07
*/

public class ChooseBgActivity extends BaseActivity {
    Bg_Grid_Adapter mBgAdapter;
    String[] arr=new String[]{"1","2","3","4","5","6","7","8"};
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bg);
        setTitleText(R.string.choose_bg);
        setBackView(R.drawable.u194);
        initView();
    }

    private void initView() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseBgActivity.this.finish();
            }
        });
        mBgAdapter=new Bg_Grid_Adapter(context,arr);
        mGridView= (GridView) findViewById(R.id.bg_grid);
        mGridView.setAdapter(mBgAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initDialog();
            }
        });

    }

    public void initDialog(){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.bg_dialog);
        dialog.getWindow().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮
                dialog.dismiss();
            }
        });

    }
}
