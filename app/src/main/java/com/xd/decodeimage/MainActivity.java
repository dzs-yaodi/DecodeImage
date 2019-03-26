package com.xd.decodeimage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xd.decodeimage.floder.SelectFloderActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_aes;
    private Button btn_byte;
    private Context mContext;
    public static final int AES_REQUEST_CODE = 10001;
    public static final int RESULT_CODE = 10002;
    public static final int BYTE_REQUEST_CODE = 10003;
    private String filePath;
    private EditText imgPwd;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_aes = findViewById(R.id.btn_aes);
        btn_byte = findViewById(R.id.btn_byte);
        imgPwd = findViewById(R.id.imgPwd);

        mContext = MainActivity.this;
        btn_aes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = imgPwd.getText().toString().trim();
                if (TextUtils.isEmpty(password))
                    Toast.makeText(mContext, "图片密码不能为空", Toast.LENGTH_SHORT).show();
                else
                    startActivityForResult(new Intent(mContext,SelectFloderActivity.class),AES_REQUEST_CODE);
            }
        });
        btn_byte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext,SelectFloderActivity.class),BYTE_REQUEST_CODE);
            }
        });

    }

    private class DecodImg extends AsyncTask<String,Integer,Void>{

        private int max = 0;
        private TextView tv_nums_vedio;
        private SeekBar seekBar_vedio;
        private Dialog dialog;
        private String imgName;
        private TextView tv_image_name;
        private int progressline;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_decode_loading,null);
            tv_nums_vedio = view.findViewById(R.id.tv_nums_vedio);
            seekBar_vedio = view.findViewById(R.id.seekBar_vedio);
            tv_image_name = view.findViewById(R.id.tv_vedio_name);

            tv_nums_vedio.setText("0");
            dialog.setContentView(view);
            dialog.show();
        }
        @Override
        protected Void doInBackground(String... strings) {

            DecodeUtils.decode(strings[0],filePath,password, new DecodeUtils.DeCodeCallBack() {
                @Override
                public void Succ(int index ,String name) {

                    imgName = name;
                    progressline = index;
                    publishProgress(index);
                }

                @Override
                public void Fail(String msg) {
                    imgName = msg;
                }

                @Override
                public void maxLength(int length) {
                    max = length;
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            tv_image_name.setText(imgName);
            tv_nums_vedio.setText(values[0] + "/" + max);
            seekBar_vedio.setMax((int) max);
            seekBar_vedio.setProgress(progressline);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_CODE){

            if (requestCode == AES_REQUEST_CODE ){
                filePath = data.getStringExtra("filePath");

                DecodImg decodImg = new DecodImg();
                decodImg.execute("aes");
            }else if (requestCode == BYTE_REQUEST_CODE){
                filePath = data.getStringExtra("filePath");

                DecodImg decodImg = new DecodImg();
                decodImg.execute("byte");
            }
        }
    }
}
