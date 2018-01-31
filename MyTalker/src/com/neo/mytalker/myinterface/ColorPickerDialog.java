package com.neo.mytalker.myinterface;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
 
public class ColorPickerDialog extends Dialog {
    private final boolean debug = true;
    private final String TAG = "ColorPicker";
     
    Context context;
    private String title;//����
    private int mInitialColor;//��ʼ��ɫ
    private OnColorChangedListener mListener;
 
    /**
     * ��ʼ��ɫ��ɫ
     * @param context
     * @param title �Ի������
     * @param listener �ص�
     */
    
    public ColorPickerDialog(Context context,int theme, String title, 
            OnColorChangedListener listener) {
        this(context, theme, Color.BLACK, title, listener);
    }
   /* public ColorPickerDialog(Context context, String title, 
            OnColorChangedListener listener) {
        this(context, Color.BLACK, listener);
    }*/
    /**
     * 
     * @param context
     * @param initialColor ��ʼ��ɫ
     * @param title ����
     * @param listener �ص�
     */
    public ColorPickerDialog(Context context, int theme, int initialColor, 
            String title, OnColorChangedListener listener) {
        super(context, theme);
        this.context = context;
        mListener = listener;
        mInitialColor = initialColor;
        this.title = title;
    }
    
    public ColorPickerDialog(Context context, int theme) {
    	super(context, theme);
    }

        /*public ColorPickerDialog(Context context, int initialColor, 
           OnColorChangedListener listener) {
        super(context);
        this.context = context;
        mListener = listener;
        mInitialColor = initialColor;
   //     this.title = title;
    }*/
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager manager = getWindow().getWindowManager();
        int height = (int) (manager.getDefaultDisplay().getHeight() * 0.5f);
        int width = (int) (manager.getDefaultDisplay().getWidth() * 0.7f);
        ColorPickerView myView = new ColorPickerView(context, height, width);
        setContentView(myView);
        setTitle(title);
        
    }
     
    private class ColorPickerView extends View {
        private Paint mPaint;//����ɫ������
        private Paint mCenterPaint;//�м�Բ����
        private Paint mLinePaint;//�ָ��߻���
        private Paint mRectPaint;//���䷽�黭��
         
        private Shader rectShader;//���䷽�齥��ͼ��
        private float rectLeft;//���䷽����x����
        private float rectTop;//���䷽����x����
        private float rectRight;//���䷽����y����
        private float rectBottom;//���䷽����y����
         
        private final int[] mCircleColors;//����ɫ����ɫ
        private final int[] mRectColors;//���䷽����ɫ
         
        private int mHeight;//View��
        private int mWidth;//View��
        private float r;//ɫ���뾶(paint�в�)
        private float centerRadius;//����Բ�뾶
         
        private boolean downInCircle = true;//���ڽ��价��
        private boolean downInRect;//���ڽ��䷽����
        private boolean highlightCenter;//����
        private boolean highlightCenterLittle;//΢��
         
        public ColorPickerView(Context context, int height, int width) {
            super(context);
            this.mHeight = height - 36;
            this.mWidth = width;
            setMinimumHeight(height - 36);
            setMinimumWidth(width);
             
            //����ɫ������
            mCircleColors = new int[] {0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 
                    0xFF00FFFF, 0xFF00FF00,0xFFFFFF00, 0xFFFF0000};
            Shader s = new SweepGradient(0, 0, mCircleColors, null);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(50);
            r = width / 2 * 0.7f - mPaint.getStrokeWidth() * 0.5f;
             
            //����Բ����
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(mInitialColor);
            mCenterPaint.setStrokeWidth(5);
            centerRadius = (r - mPaint.getStrokeWidth() / 2 ) * 0.7f;
             
            //�߿����
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(Color.parseColor("#72A1D1"));
            mLinePaint.setStrokeWidth(4);
             
            //�ڰ׽������
            mRectColors = new int[]{0xFF000000, mCenterPaint.getColor(), 0xFFFFFFFF};
            mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectPaint.setStrokeWidth(5);
            rectLeft = -r - mPaint.getStrokeWidth() * 0.5f;
            rectTop = r + mPaint.getStrokeWidth() * 0.5f + 
                    mLinePaint.getStrokeMiter() * 0.5f + 15;
            rectRight = r + mPaint.getStrokeWidth() * 0.5f;
            rectBottom = rectTop + 50;
        }
 
        @Override
        protected void onDraw(Canvas canvas) {
            //�ƶ�����
            canvas.translate(mWidth / 2, mHeight / 2 - 50);
            //������Բ
            canvas.drawCircle(0, 0, centerRadius,  mCenterPaint);
            //�Ƿ���ʾ����Բ���СԲ��
            if (highlightCenter || highlightCenterLittle) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                if(highlightCenter) {
                    mCenterPaint.setAlpha(0xFF);
                }else if(highlightCenterLittle) {
                    mCenterPaint.setAlpha(0x90);
                }
                canvas.drawCircle(0, 0, 
                        centerRadius + mCenterPaint.getStrokeWidth(),  mCenterPaint);
                 
                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }
            //��ɫ��
            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
            //���ڰ׽����
            if(downInCircle) {
                mRectColors[1] = mCenterPaint.getColor();
            }
            rectShader = new LinearGradient(rectLeft, 0, rectRight, 0, mRectColors, null, Shader.TileMode.MIRROR);
            mRectPaint.setShader(rectShader);
            canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mRectPaint);
            float offset = mLinePaint.getStrokeWidth() / 2;
            canvas.drawLine(rectLeft - offset, rectTop - offset * 2, 
                    rectLeft - offset, rectBottom + offset * 2, mLinePaint);//��
            canvas.drawLine(rectLeft - offset * 2, rectTop - offset, 
                    rectRight + offset * 2, rectTop - offset, mLinePaint);//��
            canvas.drawLine(rectRight + offset, rectTop - offset * 2, 
                    rectRight + offset, rectBottom + offset * 2, mLinePaint);//��
            canvas.drawLine(rectLeft - offset * 2, rectBottom + offset, 
                    rectRight + offset * 2, rectBottom + offset, mLinePaint);//��
            super.onDraw(canvas);
        }
         
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - mWidth / 2;
            float y = event.getY() - mHeight / 2 + 50;
            boolean inCircle = inColorCircle(x, y, 
                    r + mPaint.getStrokeWidth() / 2, r - mPaint.getStrokeWidth() / 2);
            boolean inCenter = inCenter(x, y, centerRadius);
            boolean inRect = inRect(x, y);
             
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downInCircle = inCircle;
                    downInRect = inRect;
                    highlightCenter = inCenter;
                case MotionEvent.ACTION_MOVE:
                    if(downInCircle && inCircle) {//down���ڽ���ɫ����, ��moveҲ�ڽ���ɫ����
                        float angle = (float) Math.atan2(y, x);
                        float unit = (float) (angle / (2 * Math.PI));
                        if (unit < 0) {
                            unit += 1;
                        }
                        mCenterPaint.setColor(interpCircleColor(mCircleColors, unit));
                        if(debug) Log.v(TAG, "ɫ����, ����: " + x + "," + y);
                    }else if(downInRect && inRect) {//down�ڽ��䷽����, ��moveҲ�ڽ��䷽����
                        mCenterPaint.setColor(interpRectColor(mRectColors, x));
                    }
                    if(debug) Log.v(TAG, "[MOVE] ����: " + highlightCenter + "΢��: " + highlightCenterLittle + " ����: " + inCenter);
                    if((highlightCenter && inCenter) || (highlightCenterLittle && inCenter)) {//�������Բ, ��ǰ�ƶ�������Բ
                        highlightCenter = true;
                        highlightCenterLittle = false;
                    } else if(highlightCenter || highlightCenterLittle) {//���������Բ, ��ǰ�Ƴ�����Բ
                        highlightCenter = false;
                        highlightCenterLittle = true;
                    } else {
                        highlightCenter = false;
                        highlightCenterLittle = false;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if(highlightCenter && inCenter) {//���������Բ, �ҵ�ǰ����������Բ
                        if(mListener != null) {
                            mListener.colorChanged(mCenterPaint.getColor());
                            ColorPickerDialog.this.dismiss();
                        }
                    }
                    if(downInCircle) {
                        downInCircle = false;
                    }
                    if(downInRect) {
                        downInRect = false;
                    }
                    if(highlightCenter) {
                        highlightCenter = false;
                    }
                    if(highlightCenterLittle) {
                        highlightCenterLittle = false;
                    }
                    invalidate();
                    break;
            }
            return true;
        }
 
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(mWidth, mHeight);
        }
 
        /**
         * �����Ƿ���ɫ����
         * @param x ����
         * @param y ����
         * @param outRadius ɫ����뾶
         * @param inRadius ɫ���ڰ뾶
         * @return
         */
        private boolean inColorCircle(float x, float y, float outRadius, float inRadius) {
            double outCircle = Math.PI * outRadius * outRadius;
            double inCircle = Math.PI * inRadius * inRadius;
            double fingerCircle = Math.PI * (x * x + y * y);
            if(fingerCircle < outCircle && fingerCircle > inCircle) {
                return true;
            }else {
                return false;
            }
        }
         
        /**
         * �����Ƿ�������Բ��
         * @param x ����
         * @param y ����
         * @param centerRadius Բ�뾶
         * @return
         */
        private boolean inCenter(float x, float y, float centerRadius) {
            double centerCircle = Math.PI * centerRadius * centerRadius;
            double fingerCircle = Math.PI * (x * x + y * y);
            if(fingerCircle < centerCircle) {
                return true;
            }else {
                return false;
            }
        }
         
        /**
         * �����Ƿ��ڽ���ɫ��
         * @param x
         * @param y
         * @return
         */
        private boolean inRect(float x, float y) {
            if( x <= rectRight && x >=rectLeft && y <= rectBottom && y >=rectTop) {
                return true;
            } else {
                return false;
            }
        }
         
        /**
         * ��ȡԲ������ɫ
         * @param colors
         * @param unit
         * @return
         */
        private int interpCircleColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }
             
            float p = unit * (colors.length - 1);
            int i = (int)p;
            p -= i;
 
            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
             
            return Color.argb(a, r, g, b);
        }
         
        /**
         * ��ȡ���������ɫ
         * @param colors
         * @param x
         * @return
         */
        private int interpRectColor(int colors[], float x) {
            int a, r, g, b, c0, c1;
            float p;
            if (x < 0) {
                c0 = colors[0]; 
                c1 = colors[1];
                p = (x + rectRight) / rectRight;
            } else {
                c0 = colors[1];
                c1 = colors[2];
                p = x / rectRight;
            }
            a = ave(Color.alpha(c0), Color.alpha(c1), p);
            r = ave(Color.red(c0), Color.red(c1), p);
            g = ave(Color.green(c0), Color.green(c1), p);
            b = ave(Color.blue(c0), Color.blue(c1), p);
            return Color.argb(a, r, g, b);
        }
         
        private int ave(int s, int d, float p) {
            return s + Math.round(p * (d - s));
        }
    }
     
    /**
     * �ص��ӿ�
     * @author <a href="clarkamx@gmail.com">LynK</a>
     * 
     * Create on 2012-1-6 ����8:21:05
     *
     */
    public interface OnColorChangedListener {
        /**
         * �ص�����
         * @param color ѡ�е���ɫ
         */
        void colorChanged(int color);
    }
     
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public int getmInitialColor() {
        return mInitialColor;
    }
 
    public void setmInitialColor(int mInitialColor) {
        this.mInitialColor = mInitialColor;
    }
 
    public OnColorChangedListener getmListener() {
        return mListener;
    }
 
    public void setmListener(OnColorChangedListener mListener) {
        this.mListener = mListener;
    }
}
