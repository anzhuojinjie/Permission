package com.hsg.permissionsdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 2016/11/29.
 */

public class MPermissionsActivity extends AppCompatActivity {
    private final String TAG = "MPermissions";
    private int REQUEST_CODE_PERMISSIONS = 0x00099;

    /**
     * 向系统请求权限
     * @param permissions   请求的权限
     * @param requestCode   请求权限的请求码
     */
    public void requestPermission(String[] permissions,int requestCode){
        this.REQUEST_CODE_PERMISSIONS = requestCode;
        if (checkPermission(permissions)){
            successPermission(REQUEST_CODE_PERMISSIONS);
        }else {
            List<String> needPermissions = getDeniedPermissons(permissions);
            ActivityCompat.requestPermissions(this,needPermissions.toArray(new String[needPermissions.size()]),REQUEST_CODE_PERMISSIONS);
        }
    }

    /**
     * 检查是否已经被授予权限
     * @param permissions
     * @return
     */
    private boolean checkPermission(String[] permissions){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissons(String[] permissions){
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 系统请求权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (verifyPermissions(grantResults)){
                successPermission(REQUEST_CODE_PERMISSIONS);
            }else {
                failPermission(REQUEST_CODE_PERMISSIONS);
                showTipsDialog();
            }
        }
    }
    private void showTipsDialog(){
        new AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSetting();
                    }
                })
                .show();
    }

    /**
     * 确认所有权限是否被授权
     * @param permissions
     * @return
     */
    private boolean verifyPermissions(int[] permissions){
        for (int permission : permissions){
            if (permission != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 启动当前应用设置界面
     */
    private void startAppSetting(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取权限成功
     * @param requestCode
     */
    public void successPermission(int requestCode){
        Log.i(TAG, "获取权限成功= "+requestCode);
    }

    /**
     * 获取权限失败
     * @param requestCode
     */
    public void failPermission(int requestCode){
        Log.i(TAG, "获取权限失败= "+requestCode);
    }
}
