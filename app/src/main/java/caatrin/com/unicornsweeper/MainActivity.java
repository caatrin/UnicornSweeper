package caatrin.com.unicornsweeper;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import caatrin.com.unicornsweeper.factory.Game;
import caatrin.com.unicornsweeper.factory.GameFactory;
import caatrin.com.unicornsweeper.factory.GameFactoryImpl;
import caatrin.com.unicornsweeper.model.Board;
import caatrin.com.unicornsweeper.views.Tile;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    public boolean isGameOver = false;
    private GameFactory gameFactory;
    private Game game;

    private Button easyBtn, mediumBtn, hardBtn;
    private TextView mBombsTextView;
    private TextView mGameStatusTextView;
    private TableLayout mMineGridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMineGrid();
        restartGame();
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
                game = gameFactory.createGame(GameFactory.GAME_EASY);
                break;
            case R.id.mediumBtn :
                game = gameFactory.createGame(GameFactory.GAME_MEDIUM);
                break;
            case R.id.hardBtn :
                game = gameFactory.createGame(GameFactory.GAME_HARD);
                break;
        }

        restartGame();
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

        gameFactory = new GameFactoryImpl();
        game = gameFactory.createGame(GameFactory.GAME_EASY);
        restartGame();
        //board = new Board(mRow, mCol);
    }

    private void restartGame() {
        final Board board = game.getBoard();
        //board.setBombsRemaining(numbombs);
        mBombsTextView.setText(String.valueOf(game.getBombs()));
        mGameStatusTextView.setText("Game started, good luck!");

        mMineGridLayout.removeAllViews();

        for (int x = 0; x < game.getRow(); x++) {
            TableRow tableRow = new TableRow(this);

            for (int y = 0; y < game.getCol(); y++) {
                final int cursorX = x;
                final int cursorY = y;
                board.setTile(x, y, this);
                final Tile tile = board.getTile(x, y);
                board.getTile(x, y).setOnClickListener(new View.OnClickListener() {
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
                        mBombsTextView.setText(Integer.toString(board.getBombsRemaining()));
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
                //if (mFlag[x][y] == true && mBomb[x][y] == false) {
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
            mGameStatusTextView.setText("Yey, you won!");
        }
    }

    /**
     * Called  if bomb is clicked or on the double click if flag is not on a bomb
     */
    private void gameover() {
        Board board = game.getBoard();
        for (int x = 0; x < game.getRow(); x++) {
            for (int y = 0; y < game.getCol(); y++) {
                if (board.getTile(x, y).isMine() == true) {
                    board.getTile(x, y).setText("*"); //exposes all bombs
                }
                board.getTile(x, y).setEnabled(false); //disable all buttons
            }
        }
    }

}
