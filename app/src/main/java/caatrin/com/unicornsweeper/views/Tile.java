package caatrin.com.unicornsweeper.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.Button;

import caatrin.com.unicornsweeper.R;

/**
 * Created by caatrin on 05/17/2015.
 */
public class Tile extends Button {

    private boolean isMine;
    private boolean isFlag;
    private boolean isExposed;
    private boolean hasWon;
    private String numSuroungingBombs;

    public Tile(Context context, int difficulty) {
        super(context);
        setDefault(context, difficulty);
    }

    public Tile(Context context, AttributeSet attrs, int difficulty) {
        super(context, attrs);
        setDefault(context, difficulty);
    }

    private void setDefault(Context context, int difficulty) {
        this.isMine = false;
        this.isFlag = false;
        this.isExposed = false;
        this.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
        setMetrics(context, difficulty);
    }


    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setIsFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setIsExposed(boolean isExposed) {
        this.isExposed = isExposed;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public String getSuroungingBombs() {
        return numSuroungingBombs;
    }

    public void setSuroungingBombs(String numSuroungingBombs) {
        this.numSuroungingBombs = numSuroungingBombs;
    }


    private void setMetrics(Context context, int difficulty) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float w = displayMetrics.widthPixels/difficulty - 4;
        int width = (int) w;
        int defaultWidth = 100;

        if (width >= defaultWidth) {
            this.setMinimumWidth(defaultWidth);
            this.setWidth(defaultWidth);
        }
        else {
            this.setMinimumWidth(width);
            this.setWidth(width);
        }

    }
}
