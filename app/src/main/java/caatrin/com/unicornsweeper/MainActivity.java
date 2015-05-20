package caatrin.com.unicornsweeper;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import caatrin.com.unicornsweeper.model.Game;
import caatrin.com.unicornsweeper.views.Tile;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    public boolean isGameOver = false;

    //private Tile mTileTable[][];        //Array of Buttons
    private Game game;
    private int mBombsremaining;        //counting the number of bombs placed
    private String mSurbombs;           //number of bombs surrounding button [x][y] (is a string so that we can setLabel for the button)

    private int mRow = 8, mCol = 8;   //number of rows columns,
    private int mNumbombs = 10;     //number of rows columns, and bombs

    private Button easyBtn, mediumBtn, hardBtn;
    private TextView mBombsTextView;
    private TextView mGameStatusTextView;
    private TableLayout mMineGridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMineGrid();
        restartGame(mRow, mCol, mNumbombs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easyBtn :
                mRow = 5;
                mCol = 5;
                mNumbombs = 3;
                break;
            case R.id.mediumBtn :
                mRow = 7;
                mCol = 7;
                mNumbombs = 5;
                break;
            case R.id.hardBtn :
                mRow = 8;
                mCol = 8;
                mNumbombs = 10;
                break;
        }

        restartGame(mRow, mCol, mNumbombs);
        isGameOver = false;
    }

    /**
     * Initialize the mine grid with all the views
     */
    private void initMineGrid() {
        mMineGridLayout = (TableLayout) findViewById(R.id.mineGridLayout);
        easyBtn = (Button) findViewById(R.id.easyBtn);
        easyBtn.setOnClickListener(this);
        mediumBtn = (Button) findViewById(R.id.mediumBtn);
        mediumBtn.setOnClickListener(this);
        hardBtn = (Button) findViewById(R.id.hardBtn);
        hardBtn.setOnClickListener(this);
        mBombsTextView = (TextView) findViewById(R.id.bombsTextView);
        mGameStatusTextView = (TextView) findViewById(R.id.gameStatusTextView);

        //mTileTable = new Tile[mRow][mCol];
        game = new Game(mRow, mCol);
    }

    private void restartGame(int row, int col, int numbombs) {
        mBombsremaining = mNumbombs;
        mBombsTextView.setText(String.valueOf(mBombsremaining));
        mGameStatusTextView.setText("Game started, good luck!");

        mMineGridLayout.removeAllViews();

        for (int x = 0; x < mRow; x++) {
            TableRow tableRow = new TableRow(this);

            for (int y = 0; y < mCol; y++) {
                final int cursorX = x;
                final int cursorY = y;
                game.setTile(x, y, this);
                //mTileTable[x][y] = new Tile(this);
                final Tile tile = game.getTile(x, y);
                game.getTile(x, y).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tile.isFlag() == false) { //if left click, and there is no flag on the button
                            if (tile.isMine() == true) { // if you you click on a bomb, results in game over
                                tile.setText("*");
                                gameover();
                                mGameStatusTextView.setText("You lost! :(");
                                isGameOver = true;
                            } else {
                                tile.setIsExposed(true);
                                tile.setHasWon(true); // these set to true mean that the button has been clicked
                                mSurbombs = Integer.toString(getSurroundingBombs(cursorX, cursorY)); //gets the number of surrounding buttons with bombs and sets it to a string so that it is possible to setLabel
                                tile.setText(String.valueOf(mSurbombs)); // sets the label to be the number of bombs in the 8 surrounding buttons
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
                        //if there is a flag already present set it so that there is no flag
                        if (tile.isFlag() == true) {
                            tile.setText("");
                            tile.setIsFlag(false);
                            tile.setHasWon(false);
                            mBombsremaining++;
                            //if there is no flag, set it so there is a flag
                        } else if (tile.hasWon() == false || tile.isMine() == true) {
                            tile.setText("|>");
                            tile.setIsFlag(true);
                            tile.setHasWon(true);
                            mBombsremaining--;
                        }
                        mBombsTextView.setText(Integer.toString(mBombsremaining));
                        return true;
                    }
                });

                tile.setMinimumWidth(0);
                tile.setWidth(90);
                tile.setHasWon(false);
                tableRow.addView(tile);
            }
            mMineGridLayout.addView(tableRow);
        }


        plantBombs(game.getTileTable(), numbombs, row, col);
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
                    if (q < 0 || w < 0 || q >= mRow || w >= mCol) {
                        break;
                    }
                    if (game.getTile(q, w).isMine() == true) {
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
        game.getTile(x, y).setIsExposed(true);
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    if (q < 0 || w < 0 || q >= mRow || w >= mCol) // makes sure that it wont have an error for buttons next to the wall
                    {
                        break;
                    }
                    if (game.getTile(q, w).isFlag() == true) {
                        break;
                    }

                    game.getTile(q, w).setHasWon(true);
                    surrbombs = Integer.toString(getSurroundingBombs(q, w));
                    game.getTile(q, w).setText(String.valueOf(surrbombs));
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
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    if (q < 0 || w < 0 || q >= mRow || w >= mCol) // makes sure that it wont have an error for buttons next to the wall
                    {
                        break;
                    }
                    if (game.getTile(q, w).isFlag() == true) {
                        break;
                    }
                    if (game.getTile(q, w).isExposed() == false && getSurroundingBombs(q, w) == 0) {
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
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mRow; y++) {
                if (game.getTile(x, y).isFlag() == true && game.getTile(x, y).isMine() == false) {
                //if (mFlag[x][y] == true && mBomb[x][y] == false) {
                    allexposed = false;
                }
                if (game.getTile(x, y).hasWon() == false) {
                    allexposed = false;
                    break;
                }
            }
        }
        if (allexposed) {
            gameover();
            mGameStatusTextView.setText("Yey, you won!");
        }
    }

    /**
     * Called  if bomb is clicked or on the double click if flag is not on a bomb
     */
    private void gameover() {
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mCol; y++) {
                if (game.getTile(x, y).isMine() == true) {
                    game.getTile(x, y).setText("*"); //exposes all bombs
                }
                game.getTile(x, y).setEnabled(false); //disable all buttons
            }
        }
    }

}
