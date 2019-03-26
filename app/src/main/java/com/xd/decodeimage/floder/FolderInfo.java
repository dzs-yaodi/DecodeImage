package com.xd.decodeimage.floder;

public class FolderInfo {

    private Integer folderIcon;//文件图标
    private String folderName;//名称
    private String folderPath;//路径
    private boolean isChecked;

    public Integer getFolderIcon() {
        return folderIcon;
    }

    public void setFolderIcon(Integer folderIcon) {
        this.folderIcon = folderIcon;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }


    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
