package com.yk.nodie;

import com.yk.nodie.runnable.MyThread;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 
 * @author yk
 * 
 */
public class MainActivity extends Activity implements OnClickListener, Callback, OnTouchListener {
	/** 游戏等级 */
	private int gameLevel;
	/** 难度选中 */
	private ImageView img_normal, img_nightmare, img_hell, img_pur;
	private MyThread myThread;
	private SurfaceView sv;
	private int w;
	private int h;
	private int gameSpan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 加载游戏菜单视图
		gameMenuView();

	}

	/**
	 * 游戏菜单视图
	 */
	private void gameMenuView() {
		setContentView(R.layout.activity_main);
		findViews();
		initViews();
	}

	private void findViews() {
		img_normal = (ImageView) findViewById(R.id.normal);
		img_nightmare = (ImageView) findViewById(R.id.nightmare);
		img_hell = (ImageView) findViewById(R.id.hell);
		img_pur = (ImageView) findViewById(R.id.pur);
	}

	private void initViews() {
		img_normal.setOnClickListener(this);
		img_nightmare.setOnClickListener(this);
		img_hell.setOnClickListener(this);
		img_pur.setOnClickListener(this);
	}

	/**
	 * 开始游戏视图
	 */
	private void startGameView() {
		sv = new SurfaceView(this);
		// 注册回调方法
		sv.getHolder().addCallback(this);
		sv.setOnTouchListener(this);
		// 将画板加载到activity
		setContentView(sv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal:
			gameLevel = 2;
			break;

		case R.id.nightmare:
			gameLevel = 3;
			break;

		case R.id.hell:
			gameLevel = 4;
			break;

		case R.id.pur:
			gameLevel = 5;
			break;
		default:
			break;
		}
		// 根据游戏等级开始游戏
		startGameView();
	}

	/**
	 * 画板已经加载完成，开始绘制
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		w = sv.getWidth();
		h = sv.getHeight();
		gameSpan = h * 4 / (5 * gameLevel);
		// 开启一个线程来绘制动画
		myThread = new MyThread(this, holder, w, h, gameLevel);
		myThread.start();
	}

	/** 画板改变 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/** 画板销毁 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myThread.setIsStart(false);
	}

	/**
	 * 根据坐标让指定区域的人跳起来
	 * 
	 * @param y
	 */
	private void roleJump(float y) {
		for (int i = 0; i < gameLevel; i++) {
			float line_u = h / 10 + i * gameSpan;
			float line_d = h / 10 + (i + 1) * gameSpan;
			if (y >= line_u && y < line_d && !myThread.roles[i].getIsJump()) {
				// 让指定区域的人物跳起来
				myThread.roles[i].setSpeedY(-h / 55);
				myThread.roles[i].setIsJump(true);
			}
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (myThread.getGameStatus()) {
		case MyThread.RUNNING://游戏正常运行
			controlRole(event);
			break;
		case MyThread.GAMEOVER://游戏结束
			backOrRestart(event);
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * 返回或者重来
	 * @param event
	 */
	private void backOrRestart(MotionEvent event) {
		float y=event.getY();
		if(y>h/2&&y<=3*h/4){//(4/6+5/6)/2=3/4
			//执行返回操作
			gameMenuView();
		}else if(y>3*h/4){
			//执行重来操作
			restart();
		}
		
	}
	/**
	 * 
	 */
	private void restart() {
		//初始化精灵
		myThread.initSpirit();
		//开始运行游戏
		myThread.setGameStatus(MyThread.RUNNING);
	}

	/**
	 * 控制人物动作
	 * @param event
	 */
	private void controlRole(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 第一根手指按下
			float y = event.getY();
			roleJump(y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// 非第一根手指按下
			int index = event.getPointerCount() - 1;
			y = event.getY(index);
			roleJump(y);
			break;
		default:
			break;
		}
	}
}
