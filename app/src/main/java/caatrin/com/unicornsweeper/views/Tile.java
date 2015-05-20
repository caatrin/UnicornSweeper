package caatrin.com.unicornsweeper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by caatrin on 05/17/2015.
 */
public class Tile extends Button {

    public final static int TILE_NUMBER = 0;
    public final static int TILE_MINE = 1;

    private int mTileType;
    private int mSurroundingMines;

    public Tile(Context context) {
        super(context);
        this.setWidth(20);
        this.setHeight(20);
    }

    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWidth(30);
        this.setHeight(30);
    }

    public int getTileType() {
        return mTileType;
    }

    public void setTileType(int mTileType) {
        this.mTileType = mTileType;
    }

    public int getSurroundingMines() {
        return mSurroundingMines;
    }

    public void setSurroundingMines(int mSurroundingMines) {
        this.mSurroundingMines = mSurroundingMines;
    }
}
