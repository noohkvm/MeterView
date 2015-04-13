package com.anwios.meterview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.anwios.meterview.R;
import com.nineoldandroids.animation.ObjectAnimator;


public class MeterView extends View {
    // ===========================================================
    // Constants
    // ===========================================================
    private  static final int DEFAULT_SIZE = 300;

    // ===========================================================
    // Fields
    // ===========================================================

    private Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint markerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float minValue = 0;
    private float maxValue = 300;


    private boolean showMarkerBig = true;
    private boolean showMarkerSmall = true;
    private float markerBigSize;
    private float markerSmallSiae;
    private int markerBigColor;
    private int markerSmallColor;
    private float markerWidth;

    private boolean showMarkerText;
    private float markerTextSize;
    private int markerTextColor;

    private boolean showCenterPoint;
    private float centerPointSize;
    private int centerPointColor;

    private float headWidth;
    private int headColor;

    private float textSize;
    private int textColor;
    private int textPadding;
    private  float value;

    private int backgroundColor;

    private ObjectAnimator animator;
    private Interpolator interpolator;
    private float currentPoint;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MeterView(Context context) {
        super(context);
        init(context, null);
    }

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public MeterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public float getValue() {
        return value;

    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int chosenWidth = getDimension(widthMode, widthSize);
        final int chosenHeight = getDimension(heightMode, heightSize);
        setMeasuredDimension(chosenWidth, chosenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float drawbleWidth = getWidth();
        float drawblHight = getHeight();



        float radius = (Math.min(drawbleWidth, drawblHight)) / 2;


        float markerTextHeight = 0;
        float centerX = drawbleWidth / 2;
        float centerY = drawblHight / 2;
        float halfWidth = centerX;
        float halfHight = centerY;
        float padding = dpToPx(4);

        //background
        p.setColor(backgroundColor);
        canvas.drawCircle(halfWidth, halfHight,radius, p);
        radius-=padding;
        p.setStrokeWidth(markerWidth);
        p.setColor(Color.parseColor("#3F51B5"));

        //draw Big markers and Text
        if (showMarkerBig || showMarkerText) {
            markerTextPaint.setColor(markerTextColor);
            markerTextPaint.setTextSize(markerTextSize);

            float drawMarkerTextValue = minValue;
            float difference = (maxValue - minValue) / 10;

            canvas.rotate(-150, halfWidth, halfHight);
            for (int i = -150; i <= 150; i = i + 30) {
                if (showMarkerBig) {
                    p.setColor(markerBigColor);
                    canvas.drawLine(halfWidth , halfHight - radius + markerBigSize, halfWidth, halfHight - radius, p);
                }
                if (showMarkerText) {
                    String text = (int) drawMarkerTextValue + "";
                    Rect bounds = new Rect();
                    markerTextPaint.getTextBounds(text, 0, text.length(), bounds);
                    markerTextHeight = bounds.height();
                    int width = bounds.width();
                    canvas.drawText(text, halfWidth - width / 2, halfHight - radius + markerBigSize + markerTextHeight + padding, markerTextPaint);

                    drawMarkerTextValue += difference;
                }
                canvas.rotate(30, halfWidth, halfHight);
            }
            canvas.rotate(-150, halfWidth, halfHight);
        }



        //draw small markers
        if (showMarkerSmall) {
            p.setColor(markerSmallColor);
            canvas.rotate(-165, halfWidth, halfHight);
            for (int i = -165; i <= 120; i = i + 30) {
                canvas.drawLine(halfWidth ,  halfHight - radius + markerSmallSiae, halfWidth, halfHight - radius, p);
                canvas.rotate(30, halfWidth, halfHight);
            }
            canvas.rotate(-165, halfWidth, halfHight);
        }

        //draw value text
        p.setColor(textColor);
        String text = (int) value + "";
        Rect bounds = new Rect();
        p.setTextSize(textSize);
        p.getTextBounds(text, 0, text.length(), bounds);
        int textheight = bounds.height();
        int textwidth = bounds.width();
        canvas.drawText(text, halfWidth - textwidth / 2, halfHight+textPadding+textSize, p);

        //draw head
        p.setColor(headColor);
        float rotation = ((value - minValue) / (maxValue - minValue)) * 300f;
        rotation = value < minValue ? 0 : rotation;
        rotation = value > maxValue ? 300f : rotation;
        canvas.rotate(rotation - 150, halfWidth, halfHight);
        p.setStrokeWidth(headWidth);
        canvas.drawLine(halfWidth, halfHight - dpToPx(6), halfWidth , halfHight - radius, p);
        canvas.rotate(-rotation + 150, halfWidth, halfHight);

        //draw center point
        if (showCenterPoint) {
            p.setColor(centerPointColor);
            canvas.drawCircle(halfWidth, halfHight, centerPointSize, p);
        }

        super.onDraw(canvas);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeterView);
        showMarkerBig = typedArray.getBoolean(R.styleable.MeterView_mv_showMarkerBig, true);
        showMarkerSmall = typedArray.getBoolean(R.styleable.MeterView_mv_showMarkerSmall, true);
        markerBigSize = typedArray.getDimensionPixelSize(R.styleable.MeterView_mv_markerBigSzie, getResources().getDimensionPixelSize(R.dimen.mv_big_marker));
        markerSmallSiae = typedArray.getDimensionPixelSize(R.styleable.MeterView_mv_markerSmallSzie, getResources().getDimensionPixelSize(R.dimen.mv_small_marker));
        markerBigColor = typedArray.getColor(R.styleable.MeterView_mv_markerBigColor, Color.parseColor("#3F51B5"));
        markerSmallColor = typedArray.getColor(R.styleable.MeterView_mv_markerSmallColor, Color.parseColor("#B71C1C"));
        markerWidth = getResources().getDimensionPixelSize(R.dimen.mv_marker_width);

        showMarkerText = typedArray.getBoolean(R.styleable.MeterView_mv_showMarkerText, true);
        markerTextSize = typedArray.getDimensionPixelSize(R.styleable.MeterView_mv_markerTextSzie, getResources().getDimensionPixelSize(R.dimen.mv_marker_text_size));

        markerTextColor = typedArray.getColor(R.styleable.MeterView_mv_markerTextColor, Color.parseColor("#3F51B5"));

        showCenterPoint = typedArray.getBoolean(R.styleable.MeterView_mv_showCenterPoint, true);
        centerPointSize = typedArray.getDimensionPixelSize(R.styleable.MeterView_mv_centerPointSize, getResources().getDimensionPixelSize(R.dimen.mv_center_point_size));
        centerPointColor = typedArray.getColor(R.styleable.MeterView_mv_centerPointColor, Color.parseColor("#3F51B5"));

        headWidth = typedArray.getDimensionPixelSize(R.styleable.MeterView_mv_headWidth, getResources().getDimensionPixelSize(R.dimen.mv_head_width));
        headColor = typedArray.getColor(R.styleable.MeterView_mv_headColor, Color.parseColor("#B71C1C"));


        minValue = typedArray.getFloat(R.styleable.MeterView_mv_minValue, 0);
        maxValue = typedArray.getFloat(R.styleable.MeterView_mv_maxValue, 300);
        value = typedArray.getFloat(R.styleable.MeterView_mv_value, minValue);
        currentPoint = value;

        textSize = typedArray.getDimension(R.styleable.MeterView_mv_textSize, getResources().getDimensionPixelSize(R.dimen.mv_text_size));
        textColor = typedArray.getColor(R.styleable.MeterView_mv_textColor, Color.parseColor("#3F51B5"));
        textPadding = getResources().getDimensionPixelSize(R.dimen.mv_text_padding);

        backgroundColor=typedArray.getColor(R.styleable.MeterView_mv_backgroundColor, Color.parseColor("#FFFFFF"));
    }

    private int getDimension(final int mode, final int size) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
            default:
                return DEFAULT_SIZE;
        }
    }

    public void moveHeadTo(float value) {
        if(animator!=null&&animator.isRunning()){
            animator.cancel();
        }
        currentPoint = this.value;
        this.value = value;
        movePointer();
    }

    public void setInterpolator(Interpolator i){
        interpolator=i;
    }
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void movePointer() {
        animator = ObjectAnimator.ofFloat(this, "value", currentPoint, value);
        int duration=(int)((Math.abs(value-currentPoint)/maxValue)*4000);
        animator.setDuration(duration);
        if(interpolator!=null){
            animator.setInterpolator(interpolator);
        }
        animator.start();
    }
}
