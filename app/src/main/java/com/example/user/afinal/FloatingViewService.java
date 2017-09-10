package com.example.user.afinal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by USER on 2017-08-13.
 */


public class FloatingViewService extends Service {
    private static final String TAG = FloatingViewService.class.getSimpleName();

    private boolean activateFlag = false;

    private WindowManager mWindowManager;
    private View mFloatingView;
    private ImageView mImageView;
    TelephonyManager manager;

    // 주기적 실행을 위한 timer object
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "1");

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.view_floating_head, null);

        manager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager !=null){
            manager.listen(new PhoneStateListener(){
                public void onCallStateChanged(int state) {
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            if(activateFlag)
                                break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            activateFlag =true;
                            break;
                        default: break;
                    }
                }
            } , PhoneStateListener.LISTEN_CALL_STATE);
        }


    //add the view to the window
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);Log.d(TAG,"2");

        // specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT; //initially iew will be added to top-left corner
        params.x = 0;
        params.y = 100;

        mFloatingView= LayoutInflater.from(this).inflate(R.layout.view_floating_head, null);

        //image 크기를 키워야 디기 때문에 image 객체를 가져온다.
        // mFloatingView 내에 imageview 가 있기 때문에 mFloatingview로 가져온다.
        mImageView=(ImageView)mFloatingView.findViewById(R.id.image);
        Log.d(TAG,"3");

        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    //return true;

                    case MotionEvent.ACTION_MOVE:
                        //calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        mFloatingView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "onClick mFloatingView is done");
                //이미 timer 가 켜져있다면 cancel()
                if(timer !=null){
                    timer.cancel();
                    timer=null;
                    return;
                }

                // 새로운 timer 를 만든다.
                timer = new Timer();
                //timer 에 새로운 timerTask를 부여한다.
                //100ms 이후 실행하면 20ms 주기로 run method가 실행된다.
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams viewParam = mImageView.getLayoutParams();
                                Log.d(TAG, "viewparam width: " + viewParam.width + " height:" + viewParam.height);
                                viewParam.height += 2;
                                viewParam.width += 2;
                                mImageView.setLayoutParams(viewParam);
                            }
                        });
                    }
                }, 100, 50);
            }
        });

        //길게 누르면 끄기
        /*mFloatingView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                stopSelf();
                return false;
            }
        });*/

        //Add th view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        //timer가 있다면 끄기
        if(timer !=null) timer.cancel();
    }

}
