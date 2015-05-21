package caatrin.com.unicornsweeper.factory;

import caatrin.com.unicornsweeper.model.Board;

/**
 * Created by caatrin on 05/20/2015.
 */
public abstract class Game {

    private Board board;
    private int row;
    private int col;
    private int numBombs;

    public Game(int row, int col, int numBombs) {
        this.board = new Board(row, col);
        this.board.setBombsRemaining(numBombs);
        this.row = row;
        this.col = col;
        this.numBombs = numBombs;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getBombs() {
        return numBombs;
    }

    public void setBombs(int numBombs) {
        this.numBombs = numBombs;
    }
}
