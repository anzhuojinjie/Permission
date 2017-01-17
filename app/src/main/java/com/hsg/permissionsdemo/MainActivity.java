package com.hsg.permissionsdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends MPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 打电话
     *
     * @param view
     */
    public void onClick1(View view) {
        requestPermission(new String[]{Manifest.permission.CALL_PHONE}, 0x0001);
    }

    /**
     * 写SD卡
     *
     * @param view
     */
    public void onClick2(View view) {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void onClick3(View view) {
        requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void successPermission(int requestCode) {
        super.successPermission(requestCode);
        switch (requestCode) {
            case 0x0001:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:15253052133"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;
        }

    }
}
