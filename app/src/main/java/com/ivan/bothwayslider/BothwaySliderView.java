package com.ivan.bothwayslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.math.BigDecimal;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.ivan.bothwayslider.R.attr.backProColor;

/**
 * @author: yifan.lin
 * @description:
 * @projectName: BothwaySlider
 * @date: 2018-06-01
 * @time: 20:15
 */
public class BothwaySliderView extends View {

    private static final int CLICK_ON_LOW = 1;
    private static final int CLICK_ON_HIGH = 2;
    private static final int CLICK_IN_LOW_AREA = 3;
    private static final int CLICK_IN_HIGH_AREA = 4;
    private static final int CLICK_OUT_AREA = 5;
    private static final int CLICK_INVALID = 0;

    private Drawable mLowBlockBg;
    private Drawable mHighBlockBg;

    private Paint mFrontPaint;
    private Paint mBackPaint;

    private int mScrollBarWidth;
    private int mScrollBarHeight = 20;
    private int mBlockDiameter;//滑块直径
    private int mBlockRadius;//滑块半径
    private double mOffsetLow = 0;
    private double mOffsetHigh = 0;
    private int mDistance;
    private double mDefaultScreenLow = -999;
    private double mDefaultScreenHigh = -999;
    private int mBlockPaddingTop = 30;
    private int mMaxValue = 89;
    private int mMinValue = 0;
    private int mValueLength;

    private int mLowValue, mHighValue;

    private int mFlag = CLICK_INVALID;

    private static final int[] STATE_NORMAL = {};
    private static final int[] STATE_PRESSED = {
            android.R.attr.state_pressed, android.R.attr.state_window_focused
    };

    private Paint mTextPaint;

    public BothwaySliderView(Context context) {
        this(context, null);
    }

    public BothwaySliderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BothwaySliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(Color.RED);
        mFrontPaint.setStyle(Paint.Style.FILL);

        mBackPaint = new Paint();
        mBackPaint.setColor(Color.GRAY);
        mBackPaint.setStyle(Paint.Style.FILL);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BothwaySliderView);
        initDrawable(ta);

        mHighBlockBg.setState(STATE_NORMAL);
        mLowBlockBg.setState(STATE_NORMAL);

        mScrollBarHeight = 10;
        mBlockDiameter = mLowBlockBg.getIntrinsicWidth();
        mBlockRadius = mBlockDiameter / 2;
        ta.recycle();
    }

    private void initDrawable(TypedArray ta) {
        int backProColor = ta.getColor(R.styleable.BothwaySliderView_backProColor, Color.GRAY);
        mBackPaint.setColor(backProColor);

        int frontProColor = ta.getColor(R.styleable.BothwaySliderView_frontProColor, Color.RED);
        mFrontPaint.setColor(frontProColor);

        mHighBlockBg = ta.getDrawable(R.styleable.BothwaySliderView_highBtnDrawable);
        if (mHighBlockBg == null) {
            mHighBlockBg = getResources().getDrawable(R.drawable.seekbarpressure_thumb, null);
        }
        mLowBlockBg = ta.getDrawable(R.styleable.BothwaySliderView_lowBtnDrawable);
        if (mLowBlockBg == null) {
            mLowBlockBg = getResources().getDrawable(R.drawable.seekbarpressure_thumb, null);
        }
        int textColor = ta.getColor(R.styleable.BothwaySliderView_textColor, Color.RED);
        mTextPaint.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mScrollBarWidth = width;
        mOffsetLow = mBlockRadius;
        mOffsetHigh = width - mBlockRadius;
        mDistance = width - mBlockDiameter;
        mValueLength = mMaxValue - mMinValue;
        if (mDefaultScreenLow != -999) {
            mOffsetLow = formatInt((mDefaultScreenLow - mMinValue) * (mScrollBarWidth - mBlockDiameter) / mValueLength) + mBlockRadius;
        }
        if (mDefaultScreenHigh != -999) {
            mOffsetHigh = formatInt((mDefaultScreenHigh - mMinValue) * (mScrollBarWidth - mBlockDiameter) / mValueLength) + mBlockRadius;
        }
        setMeasuredDimension(width, mBlockDiameter + mBlockPaddingTop + 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = mBlockPaddingTop + mBlockRadius - mScrollBarHeight / 2;
        int bottom = top + mScrollBarHeight;

//        mBackScrollBarBg.setBounds(mBlockRadius, top, mScrollBarWidth - mBlockRadius, bottom);
//        mBackScrollBarBg.draw(canvas);

//        mFrontScrollBarBg.setBounds((int) mOffsetLow, top, (int) mOffsetHigh, bottom);
//        mFrontScrollBarBg.draw(canvas);

        canvas.drawRect(mBlockRadius, top, mScrollBarWidth - mBlockRadius, bottom, mBackPaint);

        canvas.drawRect((int) mOffsetLow, top, (int) mOffsetHigh, bottom, mFrontPaint);

        mLowBlockBg.setBounds((int) (mOffsetLow - mBlockRadius), mBlockPaddingTop, (int) (mOffsetLow + mBlockRadius), mBlockDiameter + mBlockPaddingTop);
        mLowBlockBg.draw(canvas);

        mHighBlockBg.setBounds((int) (mOffsetHigh - mBlockRadius), mBlockPaddingTop, (int) (mOffsetHigh + mBlockRadius), mBlockDiameter + mBlockPaddingTop);
        mHighBlockBg.draw(canvas);

        mLowValue = formatInt((mOffsetLow - mBlockRadius) * mValueLength / mDistance + mMinValue);
        mHighValue = formatInt((mOffsetHigh - mBlockRadius) * mValueLength / mDistance + mMinValue);

        canvas.drawText(mLowValue + "", (int) mOffsetLow - 4, 30, mTextPaint);
        canvas.drawText(mHighValue + "", (int) mOffsetHigh - 4, 30, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFlag = getAreaFlag(event.getX(), event.getY());
                switch (mFlag) {
                    case CLICK_ON_LOW:
                        mLowBlockBg.setState(STATE_PRESSED);
                        break;
                    case CLICK_ON_HIGH:
                        mHighBlockBg.setState(STATE_PRESSED);
                        break;
                    case CLICK_IN_LOW_AREA:
                        mLowBlockBg.setState(STATE_PRESSED);
                        mHighBlockBg.setState(STATE_NORMAL);
                        if (event.getX() < 0 || event.getX() <= mBlockRadius) {
                            mOffsetLow = mBlockRadius;
                        } else if (event.getX() > mScrollBarWidth - mBlockRadius) {
                            mOffsetLow = mBlockRadius + mDistance;
                        } else {
                            mOffsetLow = formatInt(event.getX());
                        }
                        break;
                    case CLICK_IN_HIGH_AREA:
                        mLowBlockBg.setState(STATE_NORMAL);
                        mHighBlockBg.setState(STATE_PRESSED);
                        if (event.getX() >= mScrollBarWidth - mBlockRadius) {
                            mOffsetHigh = mDistance + mBlockRadius;
                        } else {
                            mOffsetHigh = formatInt(event.getX());
                        }
                        break;
                }
                refresh();
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mFlag) {
                    case CLICK_ON_LOW:
                        if (event.getX() < 0 || event.getX() <= mBlockRadius) {
                            mOffsetLow = mBlockRadius;
                        } else if (event.getX() >= mScrollBarWidth - mBlockRadius) {
                            mOffsetLow = mBlockRadius + mDistance;
                            mOffsetHigh = mOffsetLow;
                        } else {
                            mOffsetLow = formatInt(event.getX());
                            if (mOffsetHigh - mOffsetLow <= 0) {
                                mOffsetHigh = (mOffsetLow <= mDistance + mBlockRadius) ? mOffsetLow : (mDistance + mBlockRadius);
                            }
                        }
                        break;
                    case CLICK_ON_HIGH:
                        if (event.getX() < mBlockRadius) {
                            mOffsetHigh = mBlockRadius;
                            mOffsetLow = mBlockRadius;
                        } else if (event.getX() >= mScrollBarWidth - mBlockRadius) {
                            mOffsetHigh = mBlockRadius + mDistance;
                        } else {
                            mOffsetHigh = formatInt(event.getX());
                            if (mOffsetHigh - mOffsetLow <= 0) {
                                mOffsetLow = (mOffsetHigh >= mBlockRadius) ? mOffsetHigh : (mBlockRadius);
                            }
                        }
                        break;
                }
                refresh();
                break;
            case MotionEvent.ACTION_UP:
                mLowBlockBg.setState(STATE_NORMAL);
                mHighBlockBg.setState(STATE_NORMAL);
                break;
        }
        return true;
    }

    private int getAreaFlag(float ex, float ey) {
        int top = mBlockPaddingTop;
        int bottom = mBlockDiameter + mBlockPaddingTop;
        if (ey >= top && ey <= bottom && ex >= (mOffsetLow - mBlockRadius) && ex <= mOffsetLow + mBlockRadius) {
            return CLICK_ON_LOW;
        } else if (ey >= top && ey <= bottom && ex >= (mOffsetHigh - mBlockRadius) && ex <= (mOffsetHigh + mBlockRadius)) {
            return CLICK_ON_HIGH;
        } else if (ey >= top && ey <= bottom && ((ex >= 0 && ex < (mOffsetLow - mBlockRadius))
                || ((ex > (mOffsetLow + mBlockRadius)) && ex <= (mOffsetHigh + mOffsetLow) / 2))) {
            return CLICK_IN_LOW_AREA;
        } else if (ey >= top && ey <= bottom && (((ex > (mOffsetHigh + mOffsetLow) / 2) && ex < (mOffsetHigh - mBlockRadius))
                || (ex > (mOffsetHigh + mBlockRadius) && ex <= mScrollBarWidth))) {
            return CLICK_IN_HIGH_AREA;
        } else if (!(ex >= 0 && ex <= mScrollBarWidth && ey >= top && ey <= bottom)) {
            return CLICK_OUT_AREA;
        } else {
            return CLICK_INVALID;
        }
    }

    private void refresh() {
        invalidate();
    }

    private int formatInt(double value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_UP);
        return bd1.intValue();
    }

    public int getLowValue() {
        return mLowValue;
    }

    public int getHighValue() {
        return mHighValue;
    }

    public void setMinValue(int value) {
        mMinValue = value;
    }

    public void setMaxValue(int value) {
        mMaxValue = value;
    }

    public void setLowValue(int value) {
        mDefaultScreenLow = value;
    }

    public void setHighValue(int value) {
        mDefaultScreenHigh = value;
    }

    public void setLowBlockBg(int value) {
        mLowBlockBg = getResources().getDrawable(value, null);
    }

    public void setHighBlockBg(int value) {
        mHighBlockBg = getResources().getDrawable(value, null);
    }

    public void setFrontProColor(int colorId) {
        mFrontPaint.setColor(colorId);
    }

    public void setBackProColor(int colorId) {
        mBackPaint.setColor(colorId);
    }
}
