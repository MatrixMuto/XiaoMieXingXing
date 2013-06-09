package com.example.xiaomiexingxing;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
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
	public static String showStarsInLog(int depth, int[][] stars)
	{
		String colors = "";
		for (int i=0;i<10;i++)
		{
			for (int j=0;j<10;j++)
			{
				colors += stars[j][i];
			}
		}
		Log.d(MainActivity.TAG, colors);
		return colors;
	}
	public static String getStarsInLog(int depth, int[][] stars)
	{
		String colors = "";
		for (int i=0;i<10;i++)
		{
			for (int j=0;j<10;j++)
			{
				colors += stars[j][i];
			}
		}
		return colors;
	}
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
	public static void drawStars(boolean havegroup , ImageView imgView, int[][] stars, int[][] groupId, LinkedList<Point> action) {
		// TODO Auto-generated method stub
	
		final int ww = 72;
		final int hh = 72;
		final int[] bData = new int[ww*10*hh*10];
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
		if (action != null)
		{
			for (Point p : action){
				Log.d(MainActivity.TAG,"("+p.x+","+p.y+")");
				for (int x = 0; x< ww; x++)
				{
					for(int y=0;y<10;y++)
						bData[(p.y*hh+y)*ww*10+ p.x*ww +x] = Color.WHITE;
					for (int y=hh-1;y>hh-11;y--)
						bData[(p.y*hh+y)*ww*10+ p.x*ww +x] = Color.WHITE;
				}
				for (int y = 0; y< hh; y++)
				{
					for(int x=0;x<10;x++)
						bData[(p.y*hh+y)*ww*10+ p.x*ww +x] = Color.WHITE;
					for (int x=ww-1;x>ww-11;x--)
						bData[(p.y*hh+y)*ww*10+ p.x*ww +x] = Color.WHITE;
				}
			}
		}
		Bitmap backBmp = Bitmap.createBitmap(bData, ww*10, hh*10, Bitmap.Config.ARGB_8888);
		imgView.setImageBitmap(backBmp);
		
	
	}
	public static  int getColor(int pix) {
		// TODO Auto-generated method stub
		int r = pix >> 16 & 0xFF;
		int g = pix >> 8 & 0xFF;
		int b = pix & 0xFF;
		if  ( (r >= 0xde && r <=0xef)
			 && (g >= 0x3a && g <= 0x4a)
			 && (b >= 0x3e && b <= 0x4a) )
			return xRED;
		if  ( (r >= 0xd2 && r <=0xd6)
				 && (g >= 0xd0 && g <= 0xd4)
				 && (b >= 0x42 && b <= 0x4a) )
			return xYELLOW;
		if  ( (r >= 0x56 && r <=0x63)
				 && (g >= 0x8e && g <= 0x9a)
				 && (b >= 0xd6 && b <= 0xde) )
			return xBLUE;
		if  ( (r >= 0x44 && r <=0x4a)
				 && (g >= 0xc5 && g <= 0xcf)
				 && (b >= 0x37 && b <= 0x4a) )
			return xGREEN;
		if  ( (r >= 0xc6 && r <=0xcf)
				 && (g >= 0x35 && g <= 0x3f)
				 && (b >= 0xbc && b <= 0xca) )
			return xMagenta;
		
		return xUNKNOWN;
	}
}
