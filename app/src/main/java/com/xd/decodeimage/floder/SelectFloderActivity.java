package com.xd.decodeimage.floder;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.decodeimage.MainActivity;
import com.xd.decodeimage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFloderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FolderAdapter.CheckCallBack, View.OnClickListener {

    private ImageView iv_back;
    private ListView listView;
    private TextView tv_finish;
    private Context mContext;
    //根节点路径
    private static final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private List<FolderInfo> folderlist = new ArrayList<>();
    //默认打开路径
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private FolderAdapter folderAdapter;
    private int checkPosition = 0;
    //https://blog.csdn.net/sinat_31062885/article/details/84567879

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_floder);

        iv_back = findViewById(R.id.iv_back);
        listView = findViewById(R.id.listView);
        tv_finish = findViewById(R.id.tv_finish);
        mContext = SelectFloderActivity.this;

        folderAdapter = new FolderAdapter(folderlist,mContext,this);
        listView.setAdapter(folderAdapter);
        setClick();
        refreshListItems(path);
    }

    private void setClick() {
        listView.setOnItemClickListener(this);
        iv_back.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }

    private void refreshListItems(String path) {
        folderlist = FolderList.getFolderList(path);
        folderAdapter.addList(folderlist);
        listView.setSelection(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (folderlist.get(position).getFolderPath().isEmpty())
            goToParent();
        else {
            path = folderlist.get(position).getFolderPath();
            File file = new File(path);
            if (file.isDirectory())
                refreshListItems(path);
        }
    }

    /**
     * 返回上一级目录
     */
    private void goToParent() {
        if (!absolutePath.equals(path)) {
            File file = new File(path);
            File str_pa = file.getParentFile();
            if (str_pa.equals(absolutePath)) {
                Toast.makeText(mContext, "已经是根目录",Toast.LENGTH_SHORT).show();
                refreshListItems(path);
            } else {
                path = str_pa.getAbsolutePath();
                refreshListItems(path);
            }
        } else {
            Toast.makeText(mContext, "已经是根目录",Toast.LENGTH_SHORT).show();
            refreshListItems(path);
        }
    }

    @Override
    public void Call(int position, boolean isChecked) {

        if (isChecked){
            for (int i = 0; i < folderlist.size(); i++) {
                folderlist.get(i).setChecked(false);
            }

            checkPosition = position;
        }

        folderlist.get(position).setChecked(isChecked);
        folderAdapter.addList(folderlist);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_finish:
                Toast.makeText(mContext, "选择路径:" + folderlist.get(checkPosition).getFolderPath(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("filePath",folderlist.get(checkPosition).getFolderPath());
                setResult(MainActivity.RESULT_CODE,intent);
                finish();
                break;
        }
    }
}
