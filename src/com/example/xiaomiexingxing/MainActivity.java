package com.example.xiaomiexingxing;

import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	int ACTIVITY_SELECT_IMAGE = 0x1001;
	final int REQ_CODE_PICK_IMAGE  = 0x1001;
	ImageView imgView = null;
	ImageView imgView2 = null;
	Bitmap yourSelectedImage = null;
	Bitmap map = null;
	int stars[][] = new int[10][10];
	Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imgView = (ImageView) findViewById(R.id.imageView1);
		imgView2 = (ImageView) findViewById(R.id.imageView2);
		
		
		
		//06-07 15:55:44.008: D/(18432): /storage/emulated/0/Pictures/Screenshots/Screenshot_2013-06-07-14-37-12.png
		String path = "/storage/emulated/0/Pictures/Screenshots/Screenshot_2013-06-07-19-16-16.png";

		init(path);
		
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d("","btn");
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
			}
		});
		Button btnPrev = (Button) findViewById(R.id.button2);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] bData = new int[700*700];
				int ww = 60;
				int hh = 60;
				for (int i=0;i<10;i++)
				{
					String colors = "";
					for (int j=0;j<10;j++)
					{
						colors += stars[j][i];
						//int x = j*30;
						//int y = i*30;
						//bData[i*300*30+j*30] = Color.RED;
						int c = stars[j][i] == xRED ? Color.RED
								: stars[j][i] == xYELLOW ? Color.YELLOW
								: stars[j][i] == xBLUE ? Color.BLUE
								: stars[j][i] == xGREEN ? Color.GREEN
								: stars[j][i] == xMagenta ? Color.MAGENTA
								: Color.BLACK;
						for (int x=0; x<ww; x++) {
							for (int y=0;y<hh;y++)
							{
								bData[i*ww*10*hh + y*ww*10+ (j*ww) + x] = c;
							}
						}
					}
					Log.d("",colors);
				}
				Bitmap backBmp = Bitmap.createBitmap(bData, ww*10, hh*10, Bitmap.Config.ARGB_8888);
				imgView.setImageBitmap(backBmp);
			}
		});
		Button btnNext = (Button) findViewById(R.id.button3);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				XiaoMieXX x = new XiaoMieXX(stars);
				x.start();
			}
		});
	}
	final int xNONE = 0;
	final int xRED = 1;
	final int xYELLOW = 2;
	final int xBLUE = 3;
	final int xGREEN = 4;
	final int xMagenta = 5;
	final int xUNKNOWN = 6;
	
	private int getColor(int pix) {
		// TODO Auto-generated method stub
		int r = pix >> 16 & 0xFF;
		int g = pix >> 8 & 0xFF;
		int b = pix & 0xFF;
		if  ( (r >= 0xde && r <=0xe1)
			 && (g >= 0x3a && g <= 0x3f)
			 && (b >= 0x3e && b <= 0x44) )
			return xRED;
		if  ( (r >= 0xd2 && r <=0xd4)
				 && (g >= 0xd0 && g <= 0xd4)
				 && (b >= 0x42 && b <= 0x46) )
			return xYELLOW;
		if  ( (r >= 0x56 && r <=0x5f)
				 && (g >= 0x8e && g <= 0x95)
				 && (b >= 0xd6 && b <= 0xdb) )
			return xBLUE;
		if  ( (r >= 0x44 && r <=0x4a)
				 && (g >= 0xc5 && g <= 0xc9)
				 && (b >= 0x37 && b <= 0x3d) )
			return xGREEN;
		if  ( (r >= 0xc6 && r <=0xca)
				 && (g >= 0x35 && g <= 0x3a)
				 && (b >= 0xbc && b <= 0xbf) )
			return xMagenta;
		
		return xUNKNOWN;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void init(String path)
	{
	    yourSelectedImage = BitmapFactory.decodeFile(path);
	    //imgView.setImageBitmap(yourSelectedImage);
	    int w = yourSelectedImage.getWidth();
	    int h = yourSelectedImage.getHeight();

		if (yourSelectedImage!=null) {
			int[] pixels = new int[720*720];
			yourSelectedImage.getPixels(pixels, 0, 720, 0, 463, 720, 720);
			
			map = Bitmap.createBitmap(pixels, 720, 720, Bitmap.Config.ARGB_8888);
			imgView2.setImageBitmap(map);
		}	
		
		Log.d("",String.format("red=%x",Color.RED));
		Log.d("",String.format("yellow=%x",Color.YELLOW));
		Log.d("",String.format("green=%x",Color.GREEN));
		Log.d("",String.format("blue=%x",Color.BLUE));
		//Log.d("",String.format("=%x",Color.));
		for (int i=0;i<10;i++)
		{
			int y = i*72;
			String colors = "";
			for (int j=0;j<10;j++)
			{
				int x =j*72;
    			int pix = map.getPixel(x+40, y+40) & 0xFFFFFF;
				//pix = pix & 0x00FFFFFF;
				colors+=String.format("%x",pix) + " ";
				//colors += "("+(pix >>16 &0xFF) + ",";
				stars[j][i] = getColor(pix);
				
				//colors += (pix >>8 &0xFF) + ",";
				//colors +=  (pix & 0xFF) + ")";
			}
			Log.d("",colors);
		}
		
		int[] bData = new int[700*700];
		int ww = 60;
		int hh = 60;
		for (int i=0;i<10;i++)
		{
			String colors = "";
			for (int j=0;j<10;j++)
			{
				colors += stars[j][i];
				//int x = j*30;
				//int y = i*30;
				//bData[i*300*30+j*30] = Color.RED;
				int c = stars[j][i] == xRED ? Color.RED
						: stars[j][i] == xYELLOW ? Color.YELLOW
						: stars[j][i] == xBLUE ? Color.BLUE
						: stars[j][i] == xGREEN ? Color.GREEN
						: stars[j][i] == xMagenta ? Color.MAGENTA
						: Color.BLACK;
				for (int x=0; x<ww; x++) {
					for (int y=0;y<hh;y++)
					{
						bData[i*ww*10*hh + y*ww*10+ (j*ww) + x] = c;
					}
				}
			}
			Log.d("",colors);
		}
		Bitmap backBmp = Bitmap.createBitmap(bData, ww*10, hh*10, Bitmap.Config.ARGB_8888);
		imgView.setImageBitmap(backBmp);
	}
	
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
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	           Log.d("",filePath);
	           init(filePath);
	        }
	    }
	}
	class StarInfo {
		int nRed;
		int nYellow;
		int nGreen;
		int nBlue;
		int nMagenta;
		int total;
		
		public int maxHighScore()
		{
			int maxScore = 0;
			int remain = 0;
			
			if (nRed>1) maxScore += nRed*nRed*5;
			else remain+=1;
			if (nYellow>1) maxScore += nYellow*nYellow*5;
			else remain+=1;
			if (nGreen>1) maxScore += nGreen*nGreen*5;
			else remain+=1;
			if (nBlue>1) maxScore += nBlue*nBlue*5;
			else remain+=1;
			if (nMagenta>1) maxScore += nMagenta*nMagenta*5;
			else remain+=1;
			
			maxScore+= 2000-remain*remain*20;
			return maxScore;
		}
		
	};
	class XiaoMieXX extends Thread {
		int[][] mStars ;
		public XiaoMieXX(int[][] stars){
			mStars = stars;
			setName("XiaoMieXX");
		}
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			fun(0);
			Log.e("","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			super.run();
		}
		
		void fun(int depth){
			Log.d("","fun"+depth);
			int remain = findAll();
			//Log.d("","remain="+remain);
			if (remain == 0) return;
			
			
			remove(0,0);
			
			fun(depth+1);
			
			restore(0,0);
			
			fun(depth+1);
		}
		
		int findAll(){
			int maxGID = 0;
			int[][] groupId = new int[10][10];
			for (int x=0;x<10;x++)
				for (int y=0;y<10;y++)
					groupId[x][y] = -1;
			for (int x=0;x<10;x++)
				for (int y=0;y<10;y++)
				{
					int c = mStars[x][y];
					int g = groupId[x][y];
					
					if (y+1<10 && c == mStars[x][y+1])
					{
						if (g ==-1)
						{
							groupId[x][y] = maxGID;
							groupId[x][y+1] = maxGID;
							maxGID++;
						}
						else 
							groupId[x][y+1] = g;
					}
					g = groupId[x][y];
					if (x+1<10 && c == mStars[x+1][y])
					{
						if (g ==-1)
						{
							groupId[x][y] = maxGID;
							groupId[x+1][y] = maxGID;
							maxGID++;
						}
						else 
							groupId[x+1][y] = g;
					}
				}
			
			final int[] bData = new int[700*700];
			final int ww = 60;
			final int hh = 60;
			for (int i=0;i<10;i++)
			{
				String colors = "";
				for (int j=0;j<10;j++)
				{
					colors += stars[j][i];
					//int x = j*30;
					//int y = i*30;
					//bData[i*300*30+j*30] = Color.RED;
					int c = stars[j][i] == xRED ? Color.RED
							: stars[j][i] == xYELLOW ? Color.YELLOW
							: stars[j][i] == xBLUE ? Color.BLUE
							: stars[j][i] == xGREEN ? Color.GREEN
							: stars[j][i] == xMagenta ? Color.MAGENTA
							: Color.BLACK;
					if (groupId[j][i]==-1) c = Color.BLACK;
					for (int x=0; x<ww; x++) {
						for (int y=0;y<hh;y++)
						{
							bData[i*ww*10*hh + y*ww*10+ (j*ww) + x] = c;
						}
					}
				}
				Log.d("",colors);
			}
			
			handler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap backBmp = Bitmap.createBitmap(bData, ww*10, hh*10, Bitmap.Config.ARGB_8888);
					imgView.setImageBitmap(backBmp);
				}});
			return 0;
		}
		void remove(int x, int y){
			
		}
		void restore(int x, int y){
			
		}
	}
}
