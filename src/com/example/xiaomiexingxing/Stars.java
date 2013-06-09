package com.example.xiaomiexingxing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Stars extends Thread {
	
	Bitmap yourSelectedImage = null;
	Bitmap map = null;
	int[][] mStars = new int[10][10];
	int qMaxScore = 0;
	int[][] groupId = new int[10][10];
	LinkedList<int[][]> curStack = new  LinkedList<int[][]>();
	LinkedList<LinkedList<Point>> curAction = new LinkedList<LinkedList<Point>>();
	LinkedList<int[][]> bestStack = new  LinkedList<int[][]>();
	LinkedList<LinkedList<Point>>  bestAction = new LinkedList<LinkedList<Point>>();
	
	private ListIterator<int[][]> bestIter;
	private ListIterator<LinkedList<Point>> bestActionIter;

	ListIterator<int[][]>  getBestIterator() {
		return bestIter;
	}
	ListIterator<LinkedList<Point>>  getBestActionIter()
	{
		return bestActionIter;
	}
	public boolean mIamOk;
	private Handler mHandler;
	private Context mContext;
	
	public Stars(Context context,Handler handler,String path)
	{
		mHandler = handler;
		mContext = context;
		this.setName("XiaoMieXingXing"+this);
		init(path);
	}
	
	private void init(String path)
	{
	    yourSelectedImage = BitmapFactory.decodeFile(path);
		if (yourSelectedImage!=null) {
			int[] pixels = new int[720*720];
			yourSelectedImage.getPixels(pixels, 0, 720, 0, 463, 720, 720);
			map = Bitmap.createBitmap(pixels, 720, 720, Bitmap.Config.ARGB_8888);
		}	
		
		for (int i=0;i<10;i++)
		{
			int y = i*72;
			String colors = "";
			for (int j=0;j<10;j++)
			{
				int x =j*72;
    			int pix = map.getPixel(x+40, y+40) & 0xFFFFFF;
				colors+=String.format("%x",pix) + " ";
				mStars[j][i] = Utils.getColor(pix);
			}
			Log.d(MainActivity.TAG,colors);
		}
		Utils.showStarsInLog(mStars);
		for (int i=0;i<10;i++)
		{
			for (int j=0;j<10;j++)
			{
				if (mStars[j][i] == 6) mStars[j][i] = 0;
			}
		}
	}
	
	public void show(ImageView imgView)
	{
		Utils.drawStars(false,imgView,mStars,groupId,null);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mIamOk = false;
		qMaxScore = 0 ;
		long start = System.currentTimeMillis();
		
		fun(0,0);
		long end = System.currentTimeMillis();
		Log.e(MainActivity.TAG, qMaxScore+" "+" "+(end-start)+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "结束搜索了",
					     Toast.LENGTH_SHORT).show();	
			}
		});
		super.run();
	}
	
	LinkedList<LinkedList<Pair<String, Integer>>> stackList = new LinkedList<LinkedList<Pair<String, Integer>>>();

	void fun(int depth,int curScore){
		Log.d(MainActivity.TAG,"fun"+depth+"cur:"+curScore
				+" high:"+qMaxScore 
				+" curS:"+curStack.size()
				+" bestS:"+bestStack.size());
		
		LinkedList<Pair<String, Integer>> list = null; 
		if (depth >= stackList.size()) {
			list = new LinkedList<Pair<String, Integer>>();
			stackList.add(depth, list);
		}else {
			list = stackList.get(depth);
		}
			
		String now = Utils.getStarsInLog(depth,mStars);
		for (Pair<String, Integer> history : list)
		{
			if ( history.first.compareTo(now) == 0 )
			{
				if  (curScore<=history.second) 
					return ;
				else 
				{
					list.remove(history);
					break;
				}
			}
		}
		list.addLast(new Pair<String, Integer>(now,curScore));
//		if (list.size()>=20)
//		{
//			list.removeFirst();
//		}
//		Log.d(MainActivity.TAG, now);
		if (mIamOk) return ;
		
		HashMap<Integer, LinkedList<Point> > gro = new HashMap<Integer, LinkedList<Point>>();
		markChoices(gro);
		
		int[][] bakStar = new int[10][10];
		for(int x=0; x<10;x++)
			System.arraycopy(mStars[x], 0, bakStar[x], 0, 10);
		
		curStack.addLast(bakStar);
		
		if (gro.size() == 0) 
		{
			int r = 0;
			for(int x=0;x<10;x++)
				for(int y=0;y<10;y++)
				{
					if (mStars[x][y] != 0)
						r++;
				}
			
			int myScore = curScore;
			if (r<10) myScore += (2000-r*r*20);
			
			if (myScore > qMaxScore) {
				qMaxScore = myScore;
				Message msg = mHandler.obtainMessage(3, qMaxScore, 0);
			    mHandler.sendMessage(msg);
			}
				
			if (qMaxScore >=4000) mIamOk = true;
			
		
		    //handler.sendEmptyMessage(2);
		    bestStack.clear();
			for (int[][] ooo:curStack) {
				int[][] uuu = new int[10][10];
				for(int x=0; x<10;x++)
					System.arraycopy(ooo[x], 0, uuu[x], 0, 10);
				bestStack.addLast(uuu);
			}
			
			bestAction.clear();
			for ( LinkedList<Point> action : curAction)
			{
				LinkedList<Point> newAction = new LinkedList<Point>(action);
				bestAction.addLast(newAction);
			}
			bestIter = bestStack.listIterator();
			bestActionIter = bestAction.listIterator();
			
			curStack.removeLast();
			return;
		}

	
		Iterator iter = gro.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Integer key = (Integer) entry.getKey(); 
		    LinkedList<Point> val = (LinkedList<Point>) entry.getValue(); 
		    
		    remove(val);
		    
		    curAction.addLast(val);
		    int newScore = curScore + val.size() * val.size()*5;
			fun(depth+1,newScore);
			curAction.removeLast();
		    for(int x=0; x<10;x++)
				System.arraycopy(bakStar[x], 0, mStars[x], 0, 10);
		} 
		
//		Random random = new Random();
//		while (gro.size() > 0) {
//			Set<Integer> keySet = gro.keySet();
//			Integer[] keyArray = new Integer[keySet.size()];
//			keySet.toArray(keyArray);
//			Integer keyIndex  = random.nextInt(keyArray.length);
//		    LinkedList<Point> val = (LinkedList<Point>) gro.remove(keyArray[keyIndex]);
//		    
//		    remove(val);
//		    
//			curAction.addLast(val);
//			
//		    int newScore = curScore + val.size()*val.size()*5;
//			fun(depth+1,newScore);
//			
//		    curAction.removeLast();
//		    
//		    for(int x=0; x<10;x++)
//				System.arraycopy(bakStar[x], 0, mStars[x], 0, 10);
//		} 
		
		curStack.removeLast();
	}
	
	int markChoices(HashMap<Integer, LinkedList<Point>> gro){
		int maxGID = 0;
		for (int x=0;x<10;x++)
			for (int y=0;y<10;y++)
				groupId[x][y] = -1;
		for (int x=0;x<10;x++)
			for (int y=0;y<10;y++)
			{
				int c = mStars[x][y];
				if (c < Utils.xRED || c > Utils.xMagenta) continue;
				int g = groupId[x][y];
				
				if (y+1<10 && c == mStars[x][y+1])
				{
					if (g ==-1 && groupId[x][y+1] == -1)
					{
						groupId[x][y] = maxGID;
						groupId[x][y+1] = maxGID;
						
						LinkedList<Point> list = new LinkedList<Point>();
						list.add(new Point(x,y));
						list.add(new Point(x,y+1));
						gro.put(maxGID, list);
						
						maxGID++;	
					}
					else if (g ==-1 && groupId[x][y+1]!=-1) {
						groupId[x][y] = groupId[x][y+1];
						gro.get(groupId[x][y+1]).add(new Point(x,y));
					}
					else if (g!=-1 && groupId[x][y+1]==-1){
						
						groupId[x][y+1] = g;
						
						gro.get(g).add(new Point(x,y+1));
					}
					else {
						//int a = 1 / 0;
						int a = g;
						int b = groupId[x][y+1];
//							Log.d(TAG,"a="+a+" b="+bs);
						if (a>b) {
							for(Point p: gro.get(a)){
								groupId[p.x][p.y] = b;
								gro.get(b).add(p);
							}
							gro.remove(a);
						}
						else if (a<b){
							for(Point p: gro.get(b)){
								groupId[p.x][p.y] = a;
								gro.get(a).add(p);
							}
							gro.remove(b);
						}
					
//							Log.e(TAG,"error error error error error error ");
					}
				}
				
				g = groupId[x][y];
				if (x+1<10 && c == mStars[x+1][y])
				{
					if (g ==-1)
					{
						groupId[x][y] = maxGID;
						groupId[x+1][y] = maxGID;
						
						LinkedList<Point> list = new LinkedList<Point>();
						list.add(new Point(x,y));
						list.add(new Point(x+1,y));
						gro.put(maxGID, list);
						
						maxGID++;
					}
					else {
						groupId[x+1][y] = g;
						gro.get(g).add(new Point(x+1,y));
					}
				}
			}
//			for (int y=0;y<10;y++) {
//				String gro1  ="";
//				for (int x=0;x<10;x++)
//				{
//					gro1 += groupId[x][y] + " ";
//				}
//				Log.d(TAG,gro1);
//			}
		//Log.d(TAG,"we have "+gro.size()+" choices!");
	//handler.sendEmptyMessage(1);
		
	
		return 0;
	}
	


	void remove(LinkedList<Point> list){
		for ( Point p : list){
			mStars[p.x][p.y] = 0;
		}
		
		//纵向清理方块
		for(int x =0; x<10; x++) {
			int m = 9;
			for (int y=9; y>=0; y--){
				if (mStars[x][y] != 0)
				{
					mStars[x][m] = mStars[x][y];
					m--;
				}
			}
			for(int y=m; y>=0; y--)
			{
				mStars[x][y] = 0;
			}
		}
		//横向清理方块
		int n = 0;
		for(int x =0; x<10; x++) {
			if (mStars[x][9] != 0){
				for (int y=0;y<10;y++) mStars[n][y] = mStars[x][y];
				n++;
			}
		}
		for (int x=n;x<10;x++){
			for (int y=0;y<10;y++) mStars[x][y] = 0;
		}
		//handler.sendEmptyMessage(2);
	}



	public void quit() {
		// TODO Auto-generated method stub
		mIamOk = true;
	}
	
}
