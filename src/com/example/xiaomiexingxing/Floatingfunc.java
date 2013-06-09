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

/**
 * 鍙互姘歌繙鏄剧ず鍦╝ndroid灞忓箷鏈�笂鏂圭殑娴姩鑿滃崟
 * 
 * @author liujl v1.0 闇�娣诲姞 <uses-permission
 *         android:name="android.permission.SYSTEM_ALERT_WINDOW"
 *         /><!--绯荤粺寮瑰嚭绐楀彛鏉冮檺-->鏉冮檺涓嶇劧浼氭姤閿�
 */
public class Floatingfunc {
    /**
     * 娴姩绐楀彛鍦ㄥ睆骞曚腑鐨剎鍧愭爣
     */
    private static float x = 0;
    /**
     * 娴姩绐楀彛鍦ㄥ睆骞曚腑鐨剏鍧愭爣
     */
    private static float y = 200;
    /**
     * 灞忓箷瑙︽懜鐘舵�锛屾殏鏃舵湭浣跨敤
     */
    private static float state = 0;
    /**
     * 榧犳爣瑙︽懜寮�浣嶇疆
     */
    private static float mTouchStartX = 0;
    /**
     * 榧犳爣瑙︽懜缁撴潫浣嶇疆
     */
    private static float mTouchStartY = 0;
    /**
     * windows 绐楀彛绠＄悊鍣�
     */
    private static WindowManager wm = null;

    /**
     * 娴姩鏄剧ず瀵硅薄
     */
    private static View floatingViewObj = null;

    /**
     * 鍙傛暟璁惧畾绫�
     */
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    public static int TOOL_BAR_HIGH = 0;
    /**
     * 瑕佹樉绀哄湪绐楀彛鏈�墠闈㈢殑瀵硅薄
     */
    private static View view_obj = null;

    /**
     * 瑕佹樉绀哄湪绐楀彛鏈�墠闈㈢殑鏂规硶
     * 
     * @param context
     *            璋冪敤瀵硅薄Context getApplicationContext()
     * @param window
     *            璋冪敤瀵硅薄 Window getWindow()
     * @param floatingViewObj
     *            瑕佹樉绀虹殑娴姩瀵硅薄 View
     */
    public static void show(Context context, Window window, View floatingViewObj) {
        // 鍔犺浇xml鏂囦欢涓牱寮忎緥瀛愪唬鐮�
        // ********************************Start**************************
        // LayoutInflater inflater =
        // LayoutInflater.from(getApplicationContext());
        // View view = inflater.inflate(R.layout.topframe, null);
        // wm =
        // (WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // 鍔犺浇xml鏂囦欢涓牱寮忎緥瀛愪唬鐮�
        // *********************************End***************************
        //
        // 鍏抽棴娴姩鏄剧ず瀵硅薄鐒跺悗鍐嶆樉绀�
        close(context);
        Floatingfunc.floatingViewObj = floatingViewObj;

        view_obj = floatingViewObj;
        Rect frame = new Rect();
        // 杩欎竴鍙ユ槸鍏抽敭锛岃鍏跺湪top 灞傛樉绀�
        // getWindow()
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        TOOL_BAR_HIGH = frame.top;

        wm = (WindowManager) context// getApplicationContext()
                .getSystemService(Activity.WINDOW_SERVICE);
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;

        // 璁剧疆鎮诞绐楀彛闀垮鏁版嵁
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 璁惧畾閫忔槑搴�
        params.alpha = 80;
        // 璁惧畾鍐呴儴鏂囧瓧瀵归綈鏂瑰紡
        params.gravity = Gravity.LEFT | Gravity.TOP;

        // 浠ュ睆骞曞乏涓婅涓哄師鐐癸紝璁剧疆x銆亂鍒濆鍊贾�
        params.x = (int) x;
        params.y = (int) y;
        // tv = new MyTextView(TopFrame.this);
        
//        mGestureDetector = new GestureDetector(mContext, mOnGestureListener);
//        mScaleGestureDetector = new ScaleGestureDetector(mContext, mOnScaleGestureListener);
        
        wm.addView(floatingViewObj, params);

        
    }

    /**
     * 璺熻皝婊戝姩绉诲姩
     * 
     * @param event
     *            浜嬩欢瀵硅薄
     * @param view
     *            寮瑰嚭瀵硅薄瀹炰緥锛圴iew锛�
     * @return
     */
    public static boolean onTouchEvent(MotionEvent event, View view) {

        // 鑾峰彇鐩稿灞忓箷鐨勫潗鏍囷紝鍗充互灞忓箷宸︿笂瑙掍负鍘熺偣
        x = event.getRawX();
        y = event.getRawY() - 25; // 25鏄郴缁熺姸鎬佹爮鐨勯珮搴�
        Log.i("currP", "currX" + x + "====currY" + y);// 璋冭瘯淇℃伅
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            state = MotionEvent.ACTION_DOWN;
            // panTime();
            // 鑾峰彇鐩稿View鐨勫潗鏍囷紝鍗充互姝iew宸︿笂瑙掍负鍘熺偣
            mTouchStartX = event.getX();
            mTouchStartY = event.getY();
            Log.i("startP", "startX" + mTouchStartX + "====startY"
                    + mTouchStartY);// 璋冭瘯淇℃伅

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

    /**
     * 鍏抽棴娴姩鏄剧ず瀵硅薄
     */
    public static void close(Context context) {

        if (view_obj != null && view_obj.isShown()) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Activity.WINDOW_SERVICE);
            wm.removeView(view_obj);
        }
    }
    private static void updateViewPosition(View view) {
        // 鏇存柊娴姩绐楀彛浣嶇疆鍙傛暟
        params.x = (int) (x - mTouchStartX);
        params.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(Floatingfunc.floatingViewObj, params);
    }
    /**
     * 鏇存柊寮瑰嚭绐楀彛浣嶇疆
     */
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