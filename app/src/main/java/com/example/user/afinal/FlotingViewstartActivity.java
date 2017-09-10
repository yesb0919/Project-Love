package com.example.user.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by USER on 2017-08-13.
 */

public class FlotingViewstartActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OHTER_APP_PERMISSION = 2017;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ! Settings.canDrawOverlays(this)){

            //if the draw over permission is not available open th settings screen to grant the permission
            Intent intent= new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OHTER_APP_PERMISSION);
        }else{
            InitializeView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CODE_DRAW_OVER_OHTER_APP_PERMISSION){
            if(resultCode==RESULT_OK){
                InitializeView();
            }
            else{
                Toast.makeText(this,"permission is denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void InitializeView(){
        startService(new Intent(this,FloatingViewService.class));
        finish();
    }
}
