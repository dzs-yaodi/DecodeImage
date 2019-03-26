package com.xd.decodeimage.floder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.decodeimage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends BaseAdapter {

    private List<FolderInfo> folderInfoList = new ArrayList<>();
    private Context context;
    private CheckCallBack checkCallBack;

    public FolderAdapter(List<FolderInfo> folderInfoList, Context context,CheckCallBack callBack) {
        this.folderInfoList = folderInfoList;
        this.context = context;
        this.checkCallBack = callBack;
    }

    @Override
    public int getCount() {
        return folderInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_foler_item,null);

            holder.iv_type = convertView.findViewById(R.id.iv_type);
            holder.file_Name = convertView.findViewById(R.id.file_Name);
            holder.checkBox = convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final FolderInfo info = folderInfoList.get(position);
        holder.iv_type.setImageResource(info.getFolderIcon());
        holder.file_Name.setText(info.getFolderName());

        File file = new File(info.getFolderPath());
        if (!TextUtils.isEmpty(info.getFolderPath()) && file.isDirectory() || info.getFolderPath().contains("zip")){
            holder.checkBox.setVisibility(View.VISIBLE);

            holder.checkBox.setChecked(info.isChecked());
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.isChecked()){
                    checkCallBack.Call(position,false);
                }else{
                    checkCallBack.Call(position,true);
                }
            }
        });
        return convertView;
    }

    public void addList(List<FolderInfo> folderlist) {
        this.folderInfoList = folderlist;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView iv_type;
        TextView file_Name;
        CheckBox checkBox;
    }

    public interface CheckCallBack{
        void Call(int position,boolean isChecked);
    }
}
