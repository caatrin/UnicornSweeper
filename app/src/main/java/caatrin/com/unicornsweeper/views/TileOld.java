package caatrin.com.unicornsweeper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import caatrin.com.unicornsweeper.R;

/**
 * Created by caatrin on 05/16/2015.
 */
public class TileOld extends View {

    private int shape;
    private float dim;
    private Paint mTilePaint;
    private Paint mTextPaint;

    public static final int SQUARE = 0;

    public TileOld(Context context) {
        super(context);
    }

    public TileOld(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TileOld, 0, 0);

        try {
            dim = arr.getDimension(R.styleable.TileOld_dim, 30f);
            shape = arr.getInteger(R.styleable.TileOld_shape, SQUARE);
        }
        finally {
            arr.recycle();
        }

        mTilePaint = new Paint();
        mTilePaint.setColor(0xfffed325);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(20f);
        mTextPaint.setColor(getResources().getColor(android.R.color.black));
    }

    public TileOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, 100, 100, mTilePaint);
        canvas.drawText("0", 100, 100, mTextPaint);
    }
}
