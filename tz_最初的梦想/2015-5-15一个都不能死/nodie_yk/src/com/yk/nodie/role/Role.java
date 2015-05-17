package com.yk.nodie.role;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 小人
 * @author yk
 *
 */
public class Role {
	/**
	 * 宽度和高度
	 */
	private int width,heith;
	/**
	 * 坐标
	 */
	private float x,y;
	/**
	 * 速度
	 */
	private int speedX,speedY;
	/**
	 * 当前显示的图片
	 */
	private Bitmap bitmap;
	/**
	 * 当前图片位置
	 */
	private int index;
	/**
	 * 图片数组
	 */
	private Bitmap[] bms;
	/**
	 * 上一次时间点
	 */
	private long lastTime;
	
	public Role(Bitmap[] bms) {
		this.bms = bms;
		//显示第一种图片
		this.bitmap=bms[0];
		this.width=bitmap.getWidth();
		this.heith=bitmap.getHeight();
	}
	/**
	 * 根据指定时间间隔切换小人图片
	 * @param span 时间间隔
	 */
	private void switchRole(int span){
		if(System.currentTimeMillis()-lastTime>span){
			index++;
			if(index==bms.length){
				index=0;
			}
			bitmap=bms[index];
			lastTime=System.currentTimeMillis();
		}
	}
	
	public void drawSelf(Canvas canvas){
		switchRole(50);
		//绘制自己
		canvas.drawBitmap(bitmap, this.getX(), this.getY(),null);
	}
	
	
	

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeith() {
		return heith;
	}

	public void setHeith(int heith) {
		this.heith = heith;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Bitmap[] getBms() {
		return bms;
	}

	public void setBms(Bitmap[] bms) {
		this.bms = bms;
	}
}

