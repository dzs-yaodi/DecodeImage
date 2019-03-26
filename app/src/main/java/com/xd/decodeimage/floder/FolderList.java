package com.xd.decodeimage.floder;

import android.os.Environment;

import com.xd.decodeimage.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FolderList {
    private static final String rootDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static List<FolderInfo> getFolderList(String path){

        File[] files = new File(path).listFiles();
        List<FolderInfo> list = new ArrayList<FolderInfo>();
        if (!rootDirectory.equals(path)) {
            FolderInfo fi = new FolderInfo();
            fi.setFolderIcon(R.mipmap.iv_folder);
            fi.setFolderName("...");
            fi.setFolderPath("");
            list.add(fi);
        }

        List<File> fileList = Arrays.asList(files);
        //文件排序--按照名称排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }

        });
        for (File file : files) {
            //判断是文件还是文件夹&&隐藏首字母名称为“.”
            if (file.isDirectory() && file.getName().indexOf(".") != 0) {
                FolderInfo fol = new FolderInfo();
                fol.setFolderIcon(R.mipmap.iv_folder);
                fol.setFolderName(file.getName());
                fol.setFolderPath(file.getPath());
                list.add(fol);
            }else if (file.getName().contains("zip") && file.getName().indexOf(".") != 0){
                FolderInfo fol = new FolderInfo();
                fol.setFolderIcon(R.mipmap.iv_zip);
                fol.setFolderName(file.getName());
                fol.setFolderPath(file.getPath());
                list.add(fol);
            }else {
                //判断文件是否为图片
                if (MediaFileUtil.isImageFileType(file.getPath())) {
                    FolderInfo fol = new FolderInfo();
                    fol.setFolderIcon(R.mipmap.iv_image);
                    fol.setFolderName(file.getName());
                    fol.setFolderPath(file.getPath());
                    list.add(fol);
                }else if (MediaFileUtil.isVideoFileType(file.getPath())) { //判断文件是否为视频文件
                    FolderInfo fol = new FolderInfo();
                    fol.setFolderIcon(R.mipmap.iv_video);
                    fol.setFolderName(file.getName());
                    fol.setFolderPath(file.getPath());
                    list.add(fol);
                }else {
                    FolderInfo fol = new FolderInfo();
                    fol.setFolderIcon(R.mipmap.iv_file);
                    fol.setFolderName(file.getName());
                    fol.setFolderPath(file.getPath());
                    list.add(fol);
                }
            }
        }
        return list;
    }
}
