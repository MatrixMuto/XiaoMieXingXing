package com.example.xiaomiexingxing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.example.xiaomiexingxing.Utils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int ACTIVITY_SELECT_IMAGE = 0x1001;
	final int REQ_CODE_PICK_IMAGE  = 0x1001;
	ImageView overlayImageView = null;
	TextView stv = null;
	boolean showing = false;
	private String filePath;
	public static String kBestScore = "key_bestScore";
	public static String kBestStack = "key_bestStack";
	public static String kCurStars = "key_curStars";
	public static String kRawBmp = "key_rawBmp";

	

	public static final String TAG = "XMXX";
	Handler handler = new Handler(){
			
		public void handleMessage(android.os.Message msg) {
			//Log.d(TAG,"msg " + msg.what);
//			if (msg.what == 1) 
//				Utils.drawStars(true,imgView,stars,groupId,null);
//			else
			if (mStars == null) return ;
			if (msg.what == 2)
				mStars.show(overlayImageView);
			else if (msg.what == 3)
				stv.setText(""+msg.arg1);
		};
	};

	
	OverlayDisplayWindow window;
	protected Stars mStars;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate"+filePath);
		setContentView(R.layout.activity_main);
//		imgView = (ImageView) findViewById(R.id.imageView1);
		overlayImageView = new ImageView(this);
//		imgView2 = (ImageView) findViewById(R.id.imageView2);
		stv = (TextView) findViewById(R.id.textView1);
//		window = new OverlayDisplayWindow(this, "xx", 300, 300, 0, Gravity.LEFT, null);
//		window.show();
		TextView tv = new TextView(this);
		tv.setText("下一步");
		tv.setTextSize(20f);
		tv.setBackgroundColor(Color.WHITE);
		FloatingButton.show(this, getWindow(), tv , new Callback() {
		
			@Override
			public boolean handleMessage(Message msg) {
				if (mStars == null) return true;
				ListIterator<int[][]> bestIter = mStars.getBestIterator();
				ListIterator<LinkedList<Point>> bestActionIter = mStars.getBestActionIter();
				
		      	if (bestIter!= null && bestIter.hasNext()) {
					int[][] _stars = (int[][])bestIter.next();
					if ( bestActionIter.hasNext()) {
						LinkedList<Point> action = bestActionIter.next();
						Utils.drawStars(false,overlayImageView,_stars,null,action);
					}
					else
						Utils.drawStars(false,overlayImageView,_stars,null,null);
				}
				return true;
			}
		});
		TextView tv2 = new TextView(this);
		tv2.setText("上一步");
		tv2.setTextSize(20f);
		tv2.setBackgroundColor(Color.WHITE);

		FloatingButtonPrev.show(this, getWindow(), tv2 , new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				if (mStars == null) return true;
				ListIterator<int[][]> bestIter = mStars.getBestIterator();
				ListIterator<LinkedList<Point>> bestActionIter = mStars.getBestActionIter();
				
				if (bestIter==null) return true;
				if (bestIter.hasNext())
				{
					if (bestActionIter.hasPrevious())
						bestActionIter.previous();
				}
				if (bestIter.hasPrevious()) {
					int[][] _stars = (int[][])bestIter.previous();
					Utils.drawStars(false,overlayImageView,_stars,null,null);
				}
				return true;
			}
		});
		
		TextView tv3 = new TextView(this);
		tv3.setText("隐藏");
		tv3.setTextSize(20f);
		tv3.setBackgroundColor(Color.WHITE);

		FloatingButtonHide.show(this, getWindow(), tv3 , new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				
				if (showing)
					Floatingfunc.close(getApplicationContext());
				else 
					Floatingfunc.show(getApplicationContext(), getWindow(), overlayImageView);
				showing = !showing;
				return true;
			}
		});
		
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"btn");
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
			}
		});
		Button btnPrev = (Button) findViewById(R.id.button2);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mStars == null) return;
				ListIterator<int[][]> bestIter = mStars.getBestIterator();
				ListIterator<LinkedList<Point>> bestActionIter = mStars.getBestActionIter();
				Log.d(TAG,"previousIndex="+bestIter.previousIndex()
						+ "nextIndex="+bestIter.nextIndex());
				if (bestIter.hasNext())
				{
					if (bestActionIter.hasPrevious())
						bestActionIter.previous();
				}
				if (bestIter.hasPrevious()) {
					int[][] _stars = (int[][])bestIter.previous();
					Utils.drawStars(false,overlayImageView,_stars,null,null);
				}
			}
		});
		Button btnNext = (Button) findViewById(R.id.button3);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mStars == null) return;
				ListIterator<int[][]> bestIter = mStars.getBestIterator();
				ListIterator<LinkedList<Point>> bestActionIter = mStars.getBestActionIter();
				if (bestIter.hasNext()) {
					int[][] _stars = (int[][])bestIter.next();
					if ( bestActionIter.hasNext()) {
						LinkedList<Point> action = bestActionIter.next();
						Utils.drawStars(false,overlayImageView,_stars,null,action);
					}
					else
						Utils.drawStars(false,overlayImageView,_stars,null,null);
				}
			}
		});
		Button btnStar = (Button) findViewById(R.id.button4);
		btnStar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (filePath == null) return;
				mStars = new Stars(getApplicationContext(),handler,filePath);
				mStars.start();
			}
		});
		Button btnStop = (Button) findViewById(R.id.button5);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mStars == null) return ;
				mStars.quit();
			}
		});
	}
	
//	@Override
//	protected void onResume() {
//		Log.d(TAG,"onResume"+filePath);
//		super.onResume();
//	}
//	@Override
//	protected void onPause() {
//		Log.d(TAG,"onPause"+filePath);
//
//		
//		super.onPause();
//	}
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		Log.d(TAG,"onDestroy"+filePath);
////		window.dismiss();
//		super.onDestroy();
//	}
	

	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case REQ_CODE_PICK_IMAGE:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = imageReturnedIntent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            filePath = cursor.getString(columnIndex);
	            cursor.close();

	           Log.d(TAG,filePath);
	           mStars = new Stars(getApplicationContext(),handler,filePath);
	           mStars.show(overlayImageView);
			   mStars.start();
	           Floatingfunc.show(this, getWindow(), overlayImageView);
	           showing = true;
	        }
	    }
	}
	
	

}
