package com.neo.mytalker.myinterface;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("WrongCall")
public class SideBar extends View{
	
	private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;
	public static String[] con = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;//选中按钮
	private Paint paint = new Paint();//画笔
	private TextView mTextDialog;
/*	private Context mContext;
	private View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_chat_rules, null, false);*/
	
	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	public SideBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
/*		this.mContext = mContext;*/
	}
	
	
	protected void OnDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height/(con.length);
		
		for(int i = 0; i<con.length; i++) {
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);//设置为抗锯齿
			paint.setTextSize(20);
			//状态标识为选中
			if(i == choose) {
				paint.setColor(Color.YELLOW);
				paint.setFakeBoldText(true);//设置为粗体
			}
			
			float Xpos = width/2 - paint.measureText(con[i]);
			float Ypos = singleHeight * (i + 1);
		//	canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
			canvas.drawText(con[i], Xpos, Ypos, paint);
			paint.reset();//重置画笔
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener onTouchingLetterChangedListener = mOnTouchingLetterChangedListener;
		final int count = (int)(y/getHeight() * con.length);//计算现在点击的总数列表
		
		switch(action) {
			case MotionEvent.ACTION_UP:
				setBackgroundDrawable(new ColorDrawable((0x00000000)));
/*				mView.findViewWithTag(R.id.question_title).setVisibility(View.VISIBLE);
				mView.findViewWithTag(R.id.add_rules_btn).setVisibility(View.VISIBLE);*/
				choose = -1;
				invalidate();
				if(mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				setBackgroundResource(R.drawable.sidebar_background);
/*				mView.findViewWithTag(R.id.question_title).setVisibility(View.GONE);
				mView.findViewWithTag(R.id.add_rules_btn).setVisibility(View.GONE);*/
				if(choose != count) {
					if(onTouchingLetterChangedListener != null) {
						onTouchingLetterChangedListener.onTouchingLetterChanged(con[count]);
					}if(mTextDialog != null) {
						mTextDialog.setText(con[count]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = count;
					invalidate();
				}
				break;
		}
		
		return true;
	}


	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}
	

	//新建接口
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	
}
