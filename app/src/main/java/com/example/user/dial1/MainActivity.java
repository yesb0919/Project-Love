package com.example.user.dial1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalizeView();
    }

    private void initalizeView() {
        screen = (EditText) findViewById(R.id.screen);
        int[] idList = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11, R.id.btn12, R.id.btncall};

        for (int d : idList) {
            View v = (View) findViewById(d);
            v.setOnClickListener(this);
        }
    }

    public void display(String val) {
        screen.append(val);
    }

        String[] permission = {Manifest.permission.CALL_PHONE};



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                display("1");
                break;
            case R.id.btn2:
                display("2");
                break;
            case R.id.btn3:
                display("3");
                break;
            case R.id.btn4:
                display("5");
                break;
            case R.id.btn5:
                display("5");
                break;
            case R.id.btn6:
                display("6");
                break;
            case R.id.btn7:
                display("7");
                break;
            case R.id.btn8:
                display("8");
                break;
            case R.id.btn9:
                display("9");
                break;
            case R.id.btn10:
                display("*");
                break;
            case R.id.btn11:
                display("0");
                break;
            case R.id.btn12:
                display("#");
                break;
            case R.id.btncall:
                ActivityCompat.requestPermissions(MainActivity.this, permission,100);
                String data=screen.getText().toString();
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data));
                startActivity(intent);
            default:
                break;
        }
    }
}


