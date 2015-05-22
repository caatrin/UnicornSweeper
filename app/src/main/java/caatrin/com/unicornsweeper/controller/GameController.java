package caatrin.com.unicornsweeper.controller;

import android.app.Activity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import caatrin.com.unicornsweeper.factory.Game;
import caatrin.com.unicornsweeper.factory.GameFactory;
import caatrin.com.unicornsweeper.factory.GameFactoryImpl;
import caatrin.com.unicornsweeper.model.Board;
import caatrin.com.unicornsweeper.views.Tile;

/**
 * Created by caatrin on 05/21/2015.
 */
public class GameController {

    private GameFactory gameFactory = new GameFactoryImpl();
    private Game game;
    public boolean isGameOver = false;

    private OnGameChangeListener mOnGameChangedListener;
    private Activity activity;

    private static GameController gameController = null;

    private  GameController(Activity activity, OnGameChangeListener mOnGameChangedListener) {
        this.activity = activity;
        this.mOnGameChangedListener = mOnGameChangedListener;
    }

    public static GameController getInstance(Activity activity, OnGameChangeListener mOnGameChangedListener) {
        if(gameController == null) {
            gameController = new GameController(activity, mOnGameChangedListener);
        }
        return gameController;
    }

    public Game getGame() {
        return game;
    }

    public void createGame(String difficulty) {
        game = gameFactory.createGame(difficulty);
    }

    public void restartGame(TableLayout parent) {
        final Board board = game.getBoard();
        mOnGameChangedListener.setInitialText();

        for (int x = 0; x < game.getRow(); x++) {
            TableRow tableRow = new TableRow(activity);

            for (int y = 0; y < game.getCol(); y++) {
                final int cursorX = x;
                final int cursorY = y;
                board.setTile(x, y, activity);
                final Tile tile = board.getTile(x, y);
                tile.setHasWon(false);
                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tile.isFlag() == false) { //if left click, and there is no flag on the button
                            if (tile.isMine() == true) { // if you you click on a bomb, results in game over
                                tile.setText("*");
                                gameover();
                                mOnGameChangedListener.updateGameStatus("You lost! :(");
                                isGameOver = true;
                            } else {
                                tile.setIsExposed(true);
                                tile.setHasWon(true); // these set to true mean that the button has been clicked
                                tile.setSuroungingBombs(Integer.toString(getSurroundingBombs(cursorX, cursorY)));
                                tile.setText(String.valueOf(tile.getSuroungingBombs())); // sets the label to be the number of bombs in the 8 surrounding buttons
                                if (getSurroundingBombs(cursorX, cursorY) == 0) {
                                    //calls a recursive method so that if a "0" is there the surrounding 8
                                    // buttons must be exposed and if one of those is "0" it too must be exposed and so on
                                    check(cursorX, cursorY);
                                }
                            }
                            checkwin();
                        }
                    }
                });

                tile.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Board board = game.getBoard();
                        //if there is a flag already present set it so that there is no flag
                        if (tile.isFlag() == true) {
                            tile.setText("");
                            tile.setIsFlag(false);
                            tile.setHasWon(false);
                            board.setBombsRemaining(board.getBombsRemaining() + 1);
                            //if there is no flag, set it so there is a flag
                        } else if (tile.hasWon() == false || tile.isMine() == true) {
                            tile.setText("|>");
                            tile.setIsFlag(true);
                            tile.setHasWon(true);
                            board.setBombsRemaining(board.getBombsRemaining() - 1);
                        }
                        //mBombsTextView.setText(Integer.toString(board.getBombsRemaining()));
                        mOnGameChangedListener.updateRemainingBombs(Integer.toString(board.getBombsRemaining()));
                        return true;
                    }
                });
                tableRow.addView(tile);
            }
            parent.addView(tableRow);
        }

        plantBombs(board.getTileTable(), game.getBombs(), game.getRow(), game.getCol());
    }

    /**
     * Adds the bombs to random places on the grid
     * @param tile
     * @param numbombs
     * @param row
     * @param col
     */
    private void plantBombs(Tile[][] tile, int numbombs, int row, int col) {
        int count = 0;
        int randx, randy;         //random ints for bombs
        while (count < numbombs) {
            randx = (int) (Math.random() * (row));
            randy = (int) (Math.random() * (col));
            if (tile[randx][randy].isMine() == false) {
                tile[randx][randy].setIsMine(true);
                tile[randx][randy].setHasWon(true);
                count++;
            }
        }
    }


    /**
     * Checks surrounding 8 squares for number of bombs
     * (it does include itself, but has already been checked for a bomb so it won't matter)
     * @param x
     * @param y
     * @return
     */
    public int getSurroundingBombs(int x, int y) {
        int surBombs = 0;
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    // makes sure that it wont have an error for buttons next to the wall
                    if (q < 0 || w < 0 || q >= game.getRow() || w >= game.getCol()) {
                        break;
                    }
                    if (game.getBoard().getTile(q, w).isMine() == true) {
                        surBombs++;
                    }
                    break;
                }
            }
        }
        return surBombs;
    }

    /**
     * Exposes the surrounding 8 buttons
     * @param x
     * @param y
     */
    public void exposeSurroundingTiles(int x, int y) {
        String surrbombs;
        Board board = game.getBoard();
        board.getTile(x, y).setIsExposed(true);
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    if (q < 0 || w < 0 || q >= game.getRow() || w >= game.getCol()) // makes sure that it wont have an error for buttons next to the wall
                    {
                        break;
                    }
                    if (board.getTile(q, w).isFlag() == true) {
                        break;
                    }

                    board.getTile(q, w).setHasWon(true);
                    surrbombs = Integer.toString(getSurroundingBombs(q, w));
                    board.getTile(q, w).setText(String.valueOf(surrbombs));
                    break;

                }
            }
        }
    }

    /**
     * checks if a surrounding button has "0" is so expose it
     * and check around the exposed buttons until there is no more "0"'s
     * @param x
     * @param y
     */
    public void exposeEmptyTiles(int x, int y) {
        Board board = game.getBoard();
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    if (q < 0 || w < 0 || q >= game.getRow() || w >= game.getCol()) // makes sure that it wont have an error for buttons next to the wall
                    {
                        break;
                    }
                    if (board.getTile(q, w).isFlag() == true) {
                        break;
                    }
                    if (board.getTile(q, w).isExposed() == false && getSurroundingBombs(q, w) == 0) {
                        exposeSurroundingTiles(q, w);
                        check(q, w);
                    }
                    break;
                }
            }
        }
    }

    /**
     * //calls the exposeEmptyTiles() method but is necessary because of the expose first
     * @param x
     * @param y
     */
    public void check(int x, int y) {
        exposeSurroundingTiles(x, y);
        exposeEmptyTiles(x, y);
    }

    /**
     * Checks if all the button without bombs have been pressed
     */
    public void checkwin() {
        boolean allexposed = true;
        Board board = game.getBoard();
        for (int x = 0; x < game.getRow(); x++) {
            for (int y = 0; y < game.getCol(); y++) {
                if (board.getTile(x, y).isFlag() == true && board.getTile(x, y).isMine() == false) {
                    allexposed = false;
                }
                if (board.getTile(x, y).hasWon() == false) {
                    allexposed = false;
                    break;
                }
            }
        }
        if (allexposed) {
            gameover();
            mOnGameChangedListener.updateGameStatus("Yey, you won!");
        }
    }

    public void showAllBombs() {
        Board board = game.getBoard();
        for (int x = 0; x < game.getRow(); x++) {
            for (int y = 0; y < game.getCol(); y++) {
                exposeBomb(board.getTile(x, y));
            }
        }
    }

    /**
     * Called  if bomb is clicked or on the double click if flag is not on a bomb
     */
    private void gameover() {
        Board board = game.getBoard();
        for (int x = 0; x < game.getRow(); x++) {
            for (int y = 0; y < game.getCol(); y++) {
                /*if (board.getTile(x, y).isMine() == true) {
                    board.getTile(x, y).setText("*"); //exposes all bombs
                }*/
                exposeBomb(board.getTile(x, y));
                board.getTile(x, y).setEnabled(false); //disable all buttons
            }
        }
    }

    private void exposeBomb(Tile tile) {
        if (tile.isMine() == true) {
            tile.setText("*"); //exposes all bombs
        }
    }

}


