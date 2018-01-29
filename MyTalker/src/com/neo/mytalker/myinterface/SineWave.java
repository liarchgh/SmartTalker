package com.neo.mytalker.myinterface;

import com.neo.mytalker.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * Siri那样的随声音变化的正弦波动
 * <p/>
 * Created by CyanFlxy on 2014/6/15.
 */
public class SineWave extends View {
    // 波线宽度
    private final float[] WAVE_WIDTH = {3, 2, 2, 1, 1};
    // 透明度
    private final float[] WAVE_ALPHA = {1.0f, 0.9f, 0.7f, 0.4f, 0.2f};
    // 周期起点偏移
    private final int[] NORMED_PHASE = {0, 6, -9, 15, -21};

    // 可设置参数
    private int waveColor;
    private float horizontalSpeed; // 最小振幅下，每秒左右移动距离像素数
    private float verticalSpeed; // (中间位置)垂直扩展时的移动速度，每秒移动像素数
    private float verticalRestoreSpeed; // (中间位置)垂直恢复时的移动速度，每秒移动像素数
    private float maxValue;//最大振幅对应的值
    private float period;// 范围内有做少个周期
    private float minAmplitude;// 最小振幅

    private int width;
    private int height;
    private float cx;
    private float cy;
    private float midWidth;
    private float midHeight;

    // 动画参数
    private volatile float phase = 0;// 周期起点 - 波形右移变量
    private volatile float volumeAmplitude;// 音量振幅修正

    private final Paint paint;
    private MoveThread moveThread;// 右移线程
    private AmplitudeAlgorithm amplitudeAlgorithm;
    private final Path sinPath;

    public SineWave(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 针对屏幕密度修正线宽
        DisplayMetrics metric = getResources().getDisplayMetrics();
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）

        for (int i = 0; i < WAVE_WIDTH.length; i++) {
            WAVE_WIDTH[i] = WAVE_WIDTH[i] * density / 2;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SineWave, 0, 0);
        waveColor = a.getColor(R.styleable.SineWave_SineWave_wave_color, 0xFF4890D6);
        maxValue = a.getFloat(R.styleable.SineWave_SineWave_max_value, 100);
        period = a.getFloat(R.styleable.SineWave_SineWave_wave_period, 1.5f);
        minAmplitude = a.getFloat(R.styleable.SineWave_SineWave_min_amplitude, 0.23f);
        horizontalSpeed = a.getDimension(R.styleable.SineWave_SineWave_horizontal_speed, dip2px(270));
        verticalSpeed = a.getDimension(R.styleable.SineWave_SineWave_vertical_speed, dip2px(1));
        verticalRestoreSpeed = a.getDimension(R.styleable.SineWave_SineWave_vertical_restore_speed, dip2px(0.5f));
        a.recycle();

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(waveColor);

        sinPath = new Path();
        amplitudeAlgorithm = new ReciprocalAmplitudeAlgorithm();
        volumeAmplitude = minAmplitude;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = measureWidth(widthMeasureSpec);
        heightMeasureSpec = measureHeight(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = getWidth();
        int h = getHeight();

        if (width == w && height == h) {
            return;
        }

        width = w;
        height = h;

        int left = getPaddingLeft();
        int right = getPaddingRight();
        int top = getPaddingTop();
        int bottom = getPaddingBottom();

        int drawWidth = width - left - right;
        int drawHeight = height - top - bottom;
        cx = left + drawWidth / 2f;
        cy = top + drawHeight / 2f;

        midWidth = drawWidth / 2f;
        midHeight = drawHeight / 2f;// 最高振幅

    }

    private int measureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.UNSPECIFIED) {
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);

            @SuppressWarnings("deprecation")
            int width = wm.getDefaultDisplay().getWidth();
            spec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        } else if (mode == MeasureSpec.AT_MOST) {
            int value = MeasureSpec.getSize(spec);
            spec = MeasureSpec.makeMeasureSpec(value, MeasureSpec.EXACTLY);
        }

        return spec;
    }

    private int measureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.EXACTLY) {
            return spec;
        }

        int height = (int) dip2px(50);

        if (mode == MeasureSpec.AT_MOST) {
            int preValue = MeasureSpec.getSize(spec);
            if (preValue < height) {
                height = preValue;
            }
        }

        spec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        return spec;
    }

    @API
    public void setMaxValue(float value) {
        maxValue = value;
    }

    @API
    public float getMaxValue() {
        return maxValue;
    }

    @API
    public void setPeriod(float p) {
        period = p;
    }

    @API
    public float getPeriod() {
        return period;
    }

    @API
    public void setMinAmplitude(float amplitude) {
        minAmplitude = amplitude;
    }

    @API
    public void setHorizontalSpeed(float speed) {
        horizontalSpeed = speed;
    }

    @API
    public float getHorizontalSpeed() {
        return horizontalSpeed;
    }

    @API
    public void setVerticalSpeed(float speed) {
        verticalSpeed = speed;
    }

    @API
    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    @API
    public void setVerticalRestoreSpeed(float speed) {
        verticalRestoreSpeed = speed;
    }

    @API
    public float getVerticalRestoreSpeed() {
        return verticalRestoreSpeed;
    }

    @API
    public void setWaveColor(int color) {
        paint.setColor(color);
        waveColor = color;
    }

    @API
    public int getWaveColor() {
        return waveColor;
    }

    @API
    public void setAmplitudeAlgorithm(AmplitudeAlgorithm algorithm) {
        if (algorithm == null) {
            throw new NullPointerException("AmplitudeAlgorithm is null.");
        }
        amplitudeAlgorithm = algorithm;
    }

    @API
    public void setValue(float value) {
        if (Float.compare(value, 0) < 0 || Float.compare(value, maxValue) > 0) {
            throw new IllegalArgumentException("Value range [0," + maxValue + "],now is " + value);
        }

        float amplitude = amplitudeAlgorithm.amplitude(value / maxValue);

        if (amplitude < minAmplitude) {
            amplitude = minAmplitude;
        } else if (amplitude > 1.0f) {
            amplitude = 1.0f;
        }

        if (moveThread != null) {
            moveThread.insertAmplitude(amplitude);
        }

    }

    @API
    public void startAni() {
        phase = 0;
        volumeAmplitude = minAmplitude;
        invalidate();

        if (moveThread != null) {
            moveThread.stopRunning();
        }
        moveThread = new MoveThread();
        moveThread.start();
    }

    @API
    public void stopAni() {
        if (moveThread != null) {
            moveThread.stopRunning();
            moveThread = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(cx, cy);

        for (int i = 0; i < WAVE_WIDTH.length; i++) {
            float progress = 1.0f - (float) i / WAVE_WIDTH.length;
            // 不同线上的振幅
            float lineAmplitude = (1.5f * progress - 0.5f);

            paint.setStrokeWidth(WAVE_WIDTH[i]);
            paint.setAlpha((int) (WAVE_ALPHA[i] * 255));

            sinPath.reset();
            sinPath.moveTo(-midWidth, 0);

            for (float x = -midWidth; x < midWidth; x++) {
                double scaling = 1 - Math.pow(1 / midWidth * x, 2);
                double sine = Math.sin(2 * Math.PI * period * ((x + phase + NORMED_PHASE[i]) / width));//该点上的正弦值

                float y = (float) (scaling // 振幅与和水平中心的距离平方成反比，这样才有左右两端的直线效果
                        * midHeight // 最高振幅
                        * lineAmplitude// 按第几条线修正振幅
                        * volumeAmplitude // 按音量修正振幅
                        * sine);
                sinPath.lineTo(x, y);
            }
            canvas.drawPath(sinPath, paint);
            canvas.save();
        }
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAni();
    }

    private float dip2px(float dp) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    private class MoveThread extends Thread {

        private volatile boolean isStop;

        private long amplitudeSetTime;
        private float lastAmplitude;
        private float nextTargetAmplitude;

        public void stopRunning() {
            isStop = true;
        }

        @Override
        public void run() {
            isStop = false;

            long lastTime = System.currentTimeMillis();
            amplitudeSetTime = lastTime;
            lastAmplitude = volumeAmplitude;
            nextTargetAmplitude = lastAmplitude;
            phase = 0;

            while (!isStop) {

                try {
                    sleep(79);
                } catch (InterruptedException e) {
                    // ignore
                }

                long cur = System.currentTimeMillis();

                // 移动距离跟时间、像素密度、振幅加速正相关
                phase -= (cur - lastTime) / 1000f * horizontalSpeed
                        * (1 + nextTargetAmplitude - minAmplitude);
                lastTime = cur;

                // 计算音量振幅
                volumeAmplitude = currentVolumeAmplitude(cur);

                postInvalidate();

            }

        }

        public void insertAmplitude(float amplitude) {
            long curTime = System.currentTimeMillis();
            lastAmplitude = currentVolumeAmplitude(curTime);
            amplitudeSetTime = curTime;
            nextTargetAmplitude = amplitude;

            interrupt();
        }

        // 计算当前时间下的振幅
        private float currentVolumeAmplitude(long curTime) {
            if (lastAmplitude == nextTargetAmplitude) {
                return nextTargetAmplitude;
            }

            if (curTime == amplitudeSetTime) {
                return lastAmplitude;
            }

            // 走设置流程，改变速度快
            if (nextTargetAmplitude > lastAmplitude) {
                float target = lastAmplitude + verticalSpeed
                        * (curTime - amplitudeSetTime) / 1000;
                if (target >= nextTargetAmplitude) {
                    target = nextTargetAmplitude;
                    lastAmplitude = nextTargetAmplitude;
                    amplitudeSetTime = curTime;
                    nextTargetAmplitude = minAmplitude;
                }
                return target;
            }

            // 走恢复流程，改变速度慢
            if (nextTargetAmplitude < lastAmplitude) {
                float target = lastAmplitude - verticalRestoreSpeed
                        * (curTime - amplitudeSetTime) / 1000;
                if (target <= nextTargetAmplitude) {
                    target = nextTargetAmplitude;
                    lastAmplitude = nextTargetAmplitude;
                    amplitudeSetTime = curTime;
                    nextTargetAmplitude = minAmplitude;
                }
                return target;
            }

            return minAmplitude;
        }

    }

    public interface AmplitudeAlgorithm {
        /**
         * 通过当前比值计算振幅
         *
         * @param value (0.0, 1.0]
         * @return amplitude
         */
        float amplitude(float value);
    }

    @API
    public static class SimpleAmplitudeAlgorithm implements AmplitudeAlgorithm {

        @Override
        public float amplitude(float value) {
            return value;
        }
    }

    /**
     * 倒数算法
     */
    @API
    public static class ReciprocalAmplitudeAlgorithm implements AmplitudeAlgorithm {

        @Override
        public float amplitude(float value) {
            return 0.75f * (2 - 1 / (value + 0.5f));
        }
    }
}