package au.com.carmina.radarviewscan;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import au.com.carmina.R;


/**
 * Created by aniket on 2015/8/19.
 */
public class ReceiverScanView extends View
{
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;

    private static final int CIRCLE_COUNT = 3;

    private int circleColor = DEFAULT_CIRCLE_COLOR;
    private int defaultWidth;
    private int defaultHeight;
    private int start;
    private int centerX;
    private int centerY;
    private int radarRadius;

    private Context ctx;
    private Paint mPaintCircle;
    private Paint mPaintRadar;
    private Paint paint;
    private Matrix matrix;

    private boolean showCircles = true;

    private boolean once = false;
    private Handler handler = new Handler();

    private Runnable run = new Runnable()
    {
        @Override
        public void run()
        {
            start += 1;
            matrix = new Matrix();
            matrix.postRotate(start, centerX, centerY);
            postInvalidate();
            handler.postDelayed(run, 10);
        }
    };

    public PointF myPos;

    public void setShowCircles(boolean showCircles){ this.showCircles = showCircles; }

    public ReceiverScanView(Context context)
    {
        super(context);
        ctx = context;
        init(null, context);
    }

    public ReceiverScanView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        ctx = context;
        init(attrs, context);
    }

    public ReceiverScanView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        ctx = context;
        init(attrs, context);
    }

    @TargetApi(21)
    public ReceiverScanView(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        ctx = context;
        init(attrs, context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radarRadius = (Math.min(w, h)/2) - 10;
    }

    private void init(AttributeSet attrs, Context context)
    {
        initPaint();

        defaultWidth = px2dip(context, DEFAULT_WIDTH);
        defaultHeight = px2dip(context, DEFAULT_HEIGHT);

        matrix = new Matrix();
        handler.post(run);
    }

    private void initPaint()
    {
        mPaintCircle = new Paint();
        mPaintCircle.setColor(ContextCompat.getColor(ctx, R.color.circlecolor));
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(2);

        paint = new Paint();
        paint.setAntiAlias(true);

        mPaintRadar = new Paint();
        mPaintRadar.setColor(ContextCompat.getColor(ctx, R.color.radarcolor));
        mPaintRadar.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int resultWidth = 0;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY)
        {
            resultWidth = sizeWidth;
        }
        else
        {
            resultWidth = defaultWidth;
            if (modeWidth == MeasureSpec.AT_MOST)
            {
                resultWidth = Math.min(resultWidth, sizeWidth);
            }
        }

        int resultHeight = 0;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight == MeasureSpec.EXACTLY)
        {
            resultHeight = sizeHeight;
        }
        else
        {
            resultHeight = defaultHeight;
            if (modeHeight == MeasureSpec.AT_MOST)
            {
                resultHeight = Math.min(resultHeight, sizeHeight);
            }
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (showCircles) {
            for(int i=1; i<CIRCLE_COUNT+1; i++) {
                canvas.drawCircle(centerX, centerY,  (radarRadius/CIRCLE_COUNT)*i, mPaintCircle);
            }
        }
        Shader shader = new SweepGradient(centerX, centerY, ContextCompat.getColor(ctx, R.color.transparent),
                ContextCompat.getColor(ctx, R.color.lightgreen));
        mPaintRadar.setShader(shader);
        canvas.concat(matrix);
        canvas.drawCircle(centerX, centerY, radarRadius, mPaintRadar);

    }

    /*@Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (showCircles) {
            canvas.drawCircle(centerX, centerY, radarRadius / 7, mPaintCircle);
            //myPos = getPosition(centerX,centerY,radarRadius,120);
            canvas.drawCircle(centerX, centerY, radarRadius / 4, mPaintCircle);
            canvas.drawCircle(centerX, centerY, radarRadius / 3, mPaintCircle);
            canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintCircle);
        }

        Shader shader = new SweepGradient(centerX, centerY, ContextCompat.getColor(ctx, R.color.transparent),
                ContextCompat.getColor(ctx, R.color.lightgreen));
        mPaintRadar.setShader(shader);
        canvas.concat(matrix);
        canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintRadar);

    }*/

    private PointF getPosition(int centerX, int centerY, float radius, float angle) {

        PointF p = new PointF((float) (centerX + radius * Math.cos(Math.toRadians(angle))),
                (float) (centerY + radius* Math.sin(Math.toRadians(angle))));

        return p;
    }

    private int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
