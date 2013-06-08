package com.example.xiaomiexingxing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

public class Utils {
	public static final int xNONE = 0;
	public static final int xRED = 1;
	public static final int xYELLOW = 2;
	public static final int xBLUE = 3;
	public static final int xGREEN = 4;
	public static final int xMagenta = 5;
	public static final int xUNKNOWN = 6;
	
	
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
	public static void showStarsInLog(int[][] stars)
	{
		for (int i=0;i<10;i++)
		{
			String colors = "";
			for (int j=0;j<10;j++)
			{
				colors += stars[j][i];
			}
			Log.d(MainActivity.TAG,colors);
		}
	}
	public static void drawStars(boolean havegroup , ImageView imgView, int[][] stars, int[][] groupId) {
		// TODO Auto-generated method stub
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
				int c = stars[j][i] == Utils.xRED ? Color.RED
						: stars[j][i] == Utils.xYELLOW ? Color.YELLOW
						: stars[j][i] == Utils.xBLUE ? Color.BLUE
						: stars[j][i] == Utils.xGREEN ? Color.GREEN
						: stars[j][i] == Utils.xMagenta ? Color.MAGENTA
						: Color.BLACK;
				if (havegroup) { if (groupId[j][i]==-1) c = Color.BLACK;}
				for (int x=0; x<ww; x++) {
					for (int y=0;y<hh;y++)
					{
						bData[i*ww*10*hh + y*ww*10+ (j*ww) + x] = c;
					}
				}
			}
			Log.d(MainActivity.TAG,colors);
		}
		
		Bitmap backBmp = Bitmap.createBitmap(bData, ww*10, hh*10, Bitmap.Config.ARGB_8888);
		imgView.setImageBitmap(backBmp);
		
	
	}
	public static  int getColor(int pix) {
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
				 && (b >= 0xbc && b <= 0xc0) )
			return xMagenta;
		
		return xUNKNOWN;
	}
}
