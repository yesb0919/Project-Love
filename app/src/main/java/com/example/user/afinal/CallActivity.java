package com.example.user.afinal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Created by USER on 2017-08-13.
 */

public class CallActivity extends Activity {

    private static final String TAG = CallActivity.class.getSimpleName();
    private static final int CALL_PERMISSION = 1000;
    private static final int FLOATING_VIEW_DRAW = 1001;

    private boolean alreadyCallState = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ! Settings.canDrawOverlays(this)){

            //if the draw over permission is not available open th settings screen to grant the permission
            Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, FLOATING_VIEW_DRAW);
            Log.d(TAG,"1");
        } else{
            Log.d(TAG,"2");
            handleResult();
        }
    }

    private void handleResult() {
        Log.d(TAG, "onCreate is called");

        String[] permissions = {Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(this, permissions, CALL_PERMISSION);

        call();
    }

    private void call() {
        if (alreadyCallState)
            return;

        alreadyCallState = true;

        Log.d(TAG, "call is called");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:01072285946"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "전화 권한을 주세요", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "start my service");
        startService(new Intent(this, FloatingViewService.class));
        Log.d(TAG,"3");
        startActivity(intent);
        Log.d(TAG,"4");

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == FLOATING_VIEW_DRAW){
            if(resultCode==RESULT_OK){
                handleResult();
            }
            else{
                Toast.makeText(this,"permission is denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult is called with requestCode: " + requestCode);
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    call();
                }
                else {
                    Toast.makeText(this, "전화 권한을 주세요", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
