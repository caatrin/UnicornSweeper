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


public class MainActivity extends ActionBarActivity implements View.OnClickListener, OnGameChangeListener {

    public boolean isGameOver = false;
    private GameController gameController;

    private Button easyBtn, mediumBtn, hardBtn;
    private TextView mBombsTextView;
    private TextView mGameStatusTextView;
    private TableLayout mMineGridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMineGrid();

        gameController = GameController.getInstance(this, this);
        gameController.createGame(GameFactory.GAME_EASY);

        gameController.restartGame(mMineGridLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easyBtn :
                gameController.createGame(GameFactory.GAME_EASY);
                break;
            case R.id.mediumBtn :
                gameController.createGame(GameFactory.GAME_MEDIUM);
                break;
            case R.id.hardBtn :
                gameController.createGame(GameFactory.GAME_HARD);
                break;
        }

        gameController.restartGame(mMineGridLayout);
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

    }

    @Override
    public void setInitialText() {
        mBombsTextView.setText(String.valueOf(gameController.getGame().getBombs()));
        mGameStatusTextView.setText("Game started, good luck!");
        mMineGridLayout.removeAllViews();
    }

    @Override
    public void updateGameStatus(String status) {
        mGameStatusTextView.setText(status);
    }

    @Override
    public void updateRemainingBombs(String status) {
        mBombsTextView.setText(status);
    }



}
