package caatrin.com.unicornsweeper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by caatrin on 05/17/2015.
 */
public class Tile extends Button {

    private boolean isMine;
    private boolean isFlag;
    private boolean isExposed;
    private boolean hasWon;
    private String numSuroungingBombs;

    public Tile(Context context) {
        super(context);
        setDefault();
    }

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefault();
    }

    private void setDefault() {
        this.isMine = false;
        this.isFlag = false;
        this.isExposed = false;
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

}
