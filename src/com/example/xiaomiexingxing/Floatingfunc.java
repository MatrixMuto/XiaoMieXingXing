package com.example.xiaomiexingxing;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class Floatingfunc {
    private static float x = 0;
    private static float y = 200;
    private static float state = 0;
    private static float mTouchStartX = 0;
    private static float mTouchStartY = 0;
    private static WindowManager wm = null;
    private static View floatingViewObj = null;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    public static int TOOL_BAR_HIGH = 0;
    private static View view_obj = null;
    public static void show(Context context, Window window, View floatingViewObj) {
        // ********************************Start**************************
        // LayoutInflater inflater =
        // LayoutInflater.from(getApplicationContext());
        // View view = inflater.inflate(R.layout.topframe, null);
        // wm =
        // (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // *********************************End***************************
        close(context);
        Floatingfunc.floatingViewObj = floatingViewObj;

        view_obj = floatingViewObj;
        Rect frame = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        TOOL_BAR_HIGH = frame.top;

        wm = (WindowManager) context// getApplicationContext()
                .getSystemService(Activity.WINDOW_SERVICE);
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 80;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x = (int) x;
        params.y = (int) y;
        // tv = new MyTextView(TopFrame.this);
        
//        mGestureDetector = new GestureDetector(mContext, mOnGestureListener);
//        mScaleGestureDetector = new ScaleGestureDetector(mContext, mOnScaleGestureListener);
        
        wm.addView(floatingViewObj, params);

        
    }

    public static boolean onTouchEvent(MotionEvent event, View view) {

        x = event.getRawX();
        y = event.getRawY() - 25;
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            state = MotionEvent.ACTION_DOWN;
         
            mTouchStartX = event.getX();
            mTouchStartY = event.getY();
            Log.i("startP", "startX" + mTouchStartX + "====startY"
                    + mTouchStartY);

            break;
        case MotionEvent.ACTION_MOVE:
            state = MotionEvent.ACTION_MOVE;
            updateViewPosition(view);
            break;

        case MotionEvent.ACTION_UP:
            state = MotionEvent.ACTION_UP;
            updateViewPosition(view);
            mTouchStartX = mTouchStartY = 0;
            break;
        }
        return true;
    }

    public static void close(Context context) {

        if (view_obj != null && view_obj.isShown()) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Activity.WINDOW_SERVICE);
            wm.removeView(view_obj);
        }
    }
    private static void updateViewPosition(View view) {
        params.x = (int) (x - mTouchStartX);
        params.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(Floatingfunc.floatingViewObj, params);
    }
//    public void relayout() {
//        if (mWindowVisible) {
//            updateWindowParams();
//            mWindowManager.updateViewLayout(mWindowContent, mWindowParams);
//        }
//    }

//    private void updateWindowParams() {
//        float scale = mWindowScale * mLiveScale;
//        scale = Math.min(scale, (float)720/mWidth);
//        scale = Math.min(scale, (float)1280 / mHeight);
//        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
//
//        float offsetScale = (scale / mWindowScale - 1.0f) * 0.5f;
//        int width = (int)(mWidth * scale);
//        int height = (int)(mHeight * scale);
//        int x = (int)(mWindowX + mLiveTranslationX - width * offsetScale);
//        int y = (int)(mWindowY + mLiveTranslationY - height * offsetScale);
//        x = Math.max(0, Math.min(x, 720 - width));
//        y = Math.max(0, Math.min(y, 1280 - height));
//
//        if (DEBUG) {
//            Log.d(TAG, "updateWindowParams: scale=" + scale
//                    + ", offsetScale=" + offsetScale
//                    + ", x=" + x + ", y=" + y
//                    + ", width=" + width + ", height=" + height);
//        }
//
//        mTextureView.setScaleX(scale);
//        mTextureView.setScaleY(scale);
//
//        mWindowParams.x = x;
//        mWindowParams.y = y;
//        mWindowParams.width = width;
//        mWindowParams.height = height;
//    }
//
//    private void saveWindowParams() {
//        mWindowX = mWindowParams.x;
//        mWindowY = mWindowParams.y;
//        mWindowScale = mTextureView.getScaleX();
//        clearLiveState();
//    }
//
//    private void clearLiveState() {
//        mLiveTranslationX = 0f;
//        mLiveTranslationY = 0f;
//        mLiveScale = 1.0f;
//    }
//    
//    private final GestureDetector.OnGestureListener mOnGestureListener =
//            new GestureDetector.SimpleOnGestureListener() {
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                float distanceX, float distanceY) {
//            mLiveTranslationX -= distanceX;
//            mLiveTranslationY -= distanceY;
//            relayout();
//            return true;
//        }
//    };
//
//    private final ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener =
//            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            mLiveScale *= detector.getScaleFactor();
//            relayout();
//            return true;
//        }
//    };

}