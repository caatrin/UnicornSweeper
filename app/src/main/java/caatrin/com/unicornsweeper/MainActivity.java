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

import caatrin.com.unicornsweeper.views.Tile;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    public boolean isGameOver = false;

    private Tile mTileTable[][];        //Array of Buttons
    private boolean mBomb[][];          //array true if bomb is present on button [x][y]
    private boolean mFlag[][];          //array true if flag is present at button [x][y]
    private boolean mExposed[][];       //used for exposing 0's. If true then a 0 has been exposed
    private boolean mCheckwinbool[][];  // if [x][y] = true then the button has a number on it or it is a bomb (used for checking if game is over)
    private int mCount = 0;
    private int mBombsremaining;        //counting the number of bombs placed
    private String mSurbombs;           //number of bombs surrounding button [x][y] (is a string so that we can setLabel for the button)
    private int mRandx, mRandy;         //random ints for bombs
    private int mRow = 8, mCol = 8;   //number of rows columns,
    private int mNumbombs = 10, Setup = 3; //number of rows columns, and bombs

    private Button easyBtn, mediumBtn, hardBtn;
    private TextView mGombsTextView;
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
        mGombsTextView = (TextView) findViewById(R.id.bombsTextView);
        mGameStatusTextView = (TextView) findViewById(R.id.gameStatusTextView);

        mTileTable = new Tile[mRow][mCol];
        mBomb = new boolean[mRow][mCol];
        mFlag = new boolean[mRow][mCol];
        mExposed = new boolean[mRow][mCol];
        mCheckwinbool = new boolean[mRow][mCol];
    }

    private void restartGame(int row, int col, int numbombs) {
        int count = 0;
        mBombsremaining = mNumbombs;
        mGombsTextView.setText(String.valueOf(mBombsremaining));
        mGameStatusTextView.setText("Game started, good luck!");

        mMineGridLayout.removeAllViews();

        for (int x = 0; x < mRow; x++) {
            TableRow tableRow = new TableRow(this);

            for (int y = 0; y < mCol; y++) {
                final int cursorX = x;
                final int cursorY = y;
                mTileTable[x][y] = new Tile(this);
                mTileTable[x][y].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFlag[cursorX][cursorY] == false) { //if left click, and there is no flag on the button
                            if (mBomb[cursorX][cursorY] == true) { // if you you click on a bomb, results in game over
                                mTileTable[cursorX][cursorY].setText("*");
                                gameover();
                                mGameStatusTextView.setText("You lost! :(");
                                isGameOver = true;
                            } else {
                                mExposed[cursorX][cursorY] = true;
                                mCheckwinbool[cursorX][cursorY] = true; // these set to true mean that the button has been clicked
                                mSurbombs = Integer.toString(getSurroundingBombs(cursorX, cursorY)); //gets the number of surrounding buttons with bombs and sets it to a string so that it is possible to setLabel
                                mTileTable[cursorX][cursorY].setText(String.valueOf(mSurbombs)); // sets the label to be the number of bombs in the 8 surrounding buttons
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

                mTileTable[x][y].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //if there is a flag already present set it so that there is no flag
                        if (mFlag[cursorX][cursorY] == true) {
                            mTileTable[cursorX][cursorY].setText("");
                            mFlag[cursorX][cursorY] = false;
                            mCheckwinbool[cursorX][cursorY] = false;
                            mBombsremaining++;
                            //if there is no flag, set it so there is a flag
                        } else if (mCheckwinbool[cursorX][cursorY] == false || mBomb[cursorX][cursorY] == true) {
                            mTileTable[cursorX][cursorY].setText("|>");
                            mFlag[cursorX][cursorY] = true;
                            mCheckwinbool[cursorX][cursorY] = true;
                            mBombsremaining--;
                        }
                        mGombsTextView.setText(Integer.toString(mBombsremaining));
                        return true;
                    }
                });

                mTileTable[x][y].setMinimumWidth(0);
                mTileTable[x][y].setWidth(90);
                mBomb[x][y] = false;
                mFlag[x][y] = false;
                mExposed[x][y] = false;
                mCheckwinbool[x][y] = false;
                tableRow.addView(mTileTable[x][y]);
            }
            mMineGridLayout.addView(tableRow);
        }

        //adds the bombs to random places on the grid
        while (count < numbombs) {
            mRandx = (int) (Math.random() * (row));
            mRandy = (int) (Math.random() * (col));
            if (mBomb[mRandx][mRandy] == false) {
                mBomb[mRandx][mRandy] = true;
                mCheckwinbool[mRandx][mRandy] = true;
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
                    if (mBomb[q][w] == true) {
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
        mExposed[x][y] = true;
        for (int q = x - 1; q <= x + 1; q++) {
            for (int w = y - 1; w <= y + 1; w++) {
                while (true) {
                    if (q < 0 || w < 0 || q >= mRow || w >= mCol) // makes sure that it wont have an error for buttons next to the wall
                    {
                        break;
                    }
                    if (mFlag[q][w] == true) {
                        break;
                    }

                    mCheckwinbool[q][w] = true;
                    surrbombs = Integer.toString(getSurroundingBombs(q, w));
                    mTileTable[q][w].setText(String.valueOf(surrbombs));
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
                    if (mFlag[q][w] == true) {
                        break;
                    }
                    if (mExposed[q][w] == false && getSurroundingBombs(q, w) == 0) {
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
                if (mFlag[x][y] == true && mBomb[x][y] == false) {
                    allexposed = false;
                }
                if (mCheckwinbool[x][y] == false) {
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
                if (mBomb[x][y] == true) {
                    mTileTable[x][y].setText("*"); //exposes all bombs
                }
                mTileTable[x][y].setEnabled(false); //disable all buttons
            }
        }
    }

}
