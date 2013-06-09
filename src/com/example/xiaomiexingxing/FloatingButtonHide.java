package com.example.xiaomiexingxing;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler.Callback;
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

public class FloatingButtonHide {
    private static int x = 300;
    private static int y = 200;
    
    private static float state = 0;
    private static float mTouchStartX = 0;
    private static float mTouchStartY = 0;
    private static WindowManager wm = null;
    private static View floatingViewObj = null;
    private static GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
	protected static float mLiveTranslationX;
	protected static float mLiveTranslationY;
    public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    public static int TOOL_BAR_HIGH = 0;
    private static View view_obj = null;
	private static Callback mCallback;
    
    public static void show(Context context, Window window, View floatingViewObj, Callback callback) {
        // ********************************Start**************************
        // LayoutInflater inflater =
        // LayoutInflater.from(getApplicationContext());
        // View view = inflater.inflate(R.layout.topframe, null);
        // wm =
        // (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // *********************************End***************************
        close(context);
        mCallback = callback;
        FloatingButtonHide.floatingViewObj = floatingViewObj;

        view_obj = floatingViewObj;
        
        view_obj.setOnTouchListener(mOnTouchListener);
        Rect frame = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        TOOL_BAR_HIGH = frame.top;

        wm = (WindowManager) context// getApplicationContext()
                .getSystemService(Activity.WINDOW_SERVICE);
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.flags  = LayoutParams.FLAG_NOT_FOCUSABLE;
//        params.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
        params.flags |= LayoutParams.FLAG_NOT_TOUCH_MODAL;

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 80;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x = (int) x;
        params.y = (int) y;
        // tv = new MyTextView(TopFrame.this);
        
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
//        mScaleGestureDetector = new ScaleGestureDetector(context, mOnScaleGestureListener);
        
        wm.addView(floatingViewObj, params);

        
    }

//    public static boolean onTouchEvent(MotionEvent event, View view) {
//
//        x = event.getRawX();
//        y = event.getRawY() - 25;
//        Log.i("currP", "currX" + x + "====currY" + y);
//        switch (event.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//            state = MotionEvent.ACTION_DOWN;
//         
//            mTouchStartX = event.getX();
//            mTouchStartY = event.getY();
//            Log.i("startP", "startX" + mTouchStartX + "====startY"
//                    + mTouchStartY);
//
//            break;
//        case MotionEvent.ACTION_MOVE:
//            state = MotionEvent.ACTION_MOVE;
//            updateViewPosition(view);
//            break;
//
//        case MotionEvent.ACTION_UP:
//            state = MotionEvent.ACTION_UP;
//            updateViewPosition(view);
//            mTouchStartX = mTouchStartY = 0;
//            break;
//        }
//        return true;
//    }

    public static void close(Context context) {

        if (view_obj != null && view_obj.isShown()) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Activity.WINDOW_SERVICE);
            wm.removeView(view_obj);
        }
    }
//    private static void updateViewPosition(View view) {
//        params.x = (int) (x - mTouchStartX);
//        params.y = (int) (y - mTouchStartY);
//        wm.updateViewLayout(Floatingfunc.floatingViewObj, params);
//    }
    private final static View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // Work in screen coordinates.
            final float oldX = event.getX();
            final float oldY = event.getY();
            event.setLocation(event.getRawX(), event.getRawY());

            mGestureDetector.onTouchEvent(event);
//            mScaleGestureDetector.onTouchEvent(event);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    saveWindowParams();
                    mCallback.handleMessage(null);
                    break;
            }

            // Revert to window coordinates.
            event.setLocation(oldX, oldY);
            return true;
        }
    };

    public static void relayout() {
//        if (mWindowVisible) {
            updateWindowParams();
            wm.updateViewLayout(view_obj, params);
//        }
    }

    private static void updateWindowParams() {
//        float scale = mWindowScale * mLiveScale;
//        scale = Math.min(scale, (float)720/x);
//        scale = Math.min(scale, (float)1280 / y);
//        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));

//        float offsetScale = (scale / mWindowScale - 1.0f) * 0.5f;
        int width = view_obj.getWidth();
        int height = view_obj.getHeight();
         x = (int)(x + mLiveTranslationX );
         y = (int)(y + mLiveTranslationY );
        x = Math.max(0, Math.min(x, 720 - width));
        y = Math.max(0, Math.min(y, 1280 - height));

//        if (DEBUG) {
            Log.d(MainActivity.TAG, //"updateWindowParams: scale=" + scale
//                    + ", offsetScale=" + offsetScale
                     ", x=" + x + ", y=" + y
                    + ", width=" + width + ", height=" + height);
//        }

//        mTextureView.setScaleX(scale);
//        mTextureView.setScaleY(scale);
//
        params.x = (int) x;
        params.y = (int) y;
//        params.width = width;
//        params.height = height;
    }

    private static void saveWindowParams() {
    	Log.d(MainActivity.TAG,"saveWindowParams");
        x = params.x;
        y = params.y;
//        mWindowScale = mTextureView.getScaleX();
        clearLiveState();
    }

    private static void clearLiveState() {
    	Log.d(MainActivity.TAG,"clearLiveState");
        mLiveTranslationX = 0f;
        mLiveTranslationY = 0f;
//        mLiveScale = 1.0f;
    }
    
    private final static GestureDetector.OnGestureListener mOnGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
        	Log.d(MainActivity.TAG,"disX:"+distanceX + " disY:" + distanceY);
            mLiveTranslationX = -distanceX;
            mLiveTranslationY = -distanceY;
            Log.d(MainActivity.TAG,"transX:"+mLiveTranslationX + " transY:" + mLiveTranslationY);
            relayout();
            return true;
        }
    };
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