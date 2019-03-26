package com.xd.decodeimage;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class DecodeUtils {

    public static String deocode_path = Environment.getExternalStorageDirectory() + "/xinda/decodeImg/";
    private static String imgPassword = "";

    public interface DeCodeCallBack{
        void Succ(int index,String name);
        void Fail(String msg);
        void maxLength(int length);
    }


    public static void decode(String type, String filePath1,String password,final DeCodeCallBack callBack){

        File file = new File(filePath1);
        imgPassword = password;
        if (file.getName().contains("zip")){
            unPickZip(type, file, new ZipCallBack() {
                @Override
                public void onSucc(int index, String name) {
                    callBack.Succ(index,name);
                }

                @Override
                public void onFail(String msg) {
                    callBack.Fail(msg);
                }

                @Override
                public void onMaxLength(int length) {
                    callBack.maxLength(length);
                }
            });
        }else {
            File[] fs = file.listFiles();

            if (fs != null && fs.length > 0){

                callBack.maxLength(fs.length);
                for (int i = 0; i < fs.length; i++) {

                    if (fs[i].getName().contains("zip")){
                        unPickZip(type, fs[i], new ZipCallBack() {
                            @Override
                            public void onSucc(int index, String name) {

                            }

                            @Override
                            public void onFail(String msg) {
                                callBack.Fail(msg);
                            }

                            @Override
                            public void onMaxLength(int length) {

                            }
                        });

                        callBack.Succ(i,fs[i].getName());
                    }else{

                        if (fs[i].isDirectory()){

                            File file2 = new File(deocode_path + fs[i].getName());
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }

                            File[] files = fs[i].listFiles();
                            for (int j = 0; j < files.length; j++) {
                                saveBitMap(type,files[j], String.valueOf(file2));
                                callBack.Succ(i,files[j].getName());
                            }
                        }else {
                            File file2 = new File(deocode_path + file.getName());
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }

                            saveBitMap(type,fs[i], String.valueOf(file2));
                            callBack.Succ(i,fs[i].getName());
                        }

                    }
                }
            }else{
                callBack.Fail("空文件夹");
            }
        }

    }

    public static void unPickZip(String type,File file,ZipCallBack back){

        String unZipFile = deocode_path + "decode_image";
        String[] name = file.getName().split("[.]");

        try {
            ZipUtils.UnZipFolder(file.toString(),unZipFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String creatNewFile = unZipFile + "/" + name[0];
        File file1 = new File(creatNewFile);
        if (file1.exists()){
            File[] zipFileList = file1.listFiles();
            if (zipFileList != null && zipFileList.length > 0) {

                back.onMaxLength(zipFileList.length);
                File decodeFile = new File(deocode_path + name[0]);
                if (!decodeFile.exists()){
                    decodeFile.mkdirs();
                }

                for (int j = 0; j < zipFileList.length; j++) {
                    saveBitMap(type,zipFileList[j], String.valueOf(decodeFile));
                    back.onSucc(j,zipFileList[j].getName());
                }

                deleteFile(new File(unZipFile));
            }else{
                back.onFail("空文件夹");
            }
        }else{
            back.onFail("解压文件不存在");
        }
    }

    public interface ZipCallBack{
        void onSucc(int index,String name);
        void onFail(String msg);
        void onMaxLength(int length);
    }



    public static void saveBitMap(String type,File file,String directoryName){
        if (type.equals("aes")){
            Bitmap bitmap = BitMapManager.aesDecrypt(String.valueOf(file),imgPassword);
            BitMapUtils.saveBitmap(bitmap,file.getName(), directoryName);
        }else{
            Bitmap bitmap = BitMapManager.removeByte(String.valueOf(file));
            BitMapUtils.saveBitmap(bitmap,file.getName(), directoryName);
        }
    }

    /**
     * 删除日志
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
}
