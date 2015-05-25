package caatrin.com.unicornsweeper.model;

import android.content.Context;

import caatrin.com.unicornsweeper.views.Tile;

/**
 * Created by caatrin on 05/20/2015.
 */
public class Board {

    private Tile[][] tileTable;
    private int numBombsRemaining;

    public Board(int row, int col) {
        this.tileTable = new Tile[row][col];
    }

    public Tile[][] getTileTable() {
        return tileTable;
    }

    public void setTileTable(Tile[][] tileTable) {
        this.tileTable = tileTable;
    }

    public Tile getTile(int row, int col) {
        return tileTable[row][col];
    }

    public void setTile(int row, int col, Context context, int difficulty) {
        tileTable[row][col] = new Tile(context, difficulty);
    }

    public int getBombsRemaining() {
        return numBombsRemaining;
    }

    public void setBombsRemaining(int numBombsRemaining) {
        this.numBombsRemaining = numBombsRemaining;
    }
}
