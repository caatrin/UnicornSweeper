package caatrin.com.unicornsweeper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import caatrin.com.unicornsweeper.controller.GameController;
import caatrin.com.unicornsweeper.controller.OnGameChangeListener;
import caatrin.com.unicornsweeper.controller.ShakeDetector;
import caatrin.com.unicornsweeper.factory.GameFactory;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, OnGameChangeListener {

    private GameController gameController;

    private Button easyBtn, mediumBtn, hardBtn;
    private TextView mBombsTextView;
    private TextView mGameStatusTextView;
    private TextView mLevelTextView;
    private TableLayout mMineGridLayout;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGameControls();

        gameController = new GameController(this, this);
        gameController.createGame(GameFactory.GAME_EASY);
        gameController.restartGame(mMineGridLayout);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });

        mBombsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleShakeEvent(3);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_hints :
                intent = new Intent(this, HintsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.action_about :
                intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easyBtn :
                gameController.createGame(GameFactory.GAME_EASY);
                mLevelTextView.setText(GameFactory.GAME_EASY);
                break;
            case R.id.mediumBtn :
                gameController.createGame(GameFactory.GAME_MEDIUM);
                mLevelTextView.setText(GameFactory.GAME_MEDIUM);
                break;
            case R.id.hardBtn :
                gameController.createGame(GameFactory.GAME_HARD);
                mLevelTextView.setText(GameFactory.GAME_HARD);
                break;
        }
        gameController.restartGame(mMineGridLayout);
    }

    /**
     * Initialize the mine grid with all the views
     */
    private void initGameControls() {
        mMineGridLayout = (TableLayout) findViewById(R.id.mineGridLayout);
        mMineGridLayout.removeAllViews();
        easyBtn = (Button) findViewById(R.id.easyBtn);
        easyBtn.setOnClickListener(this);
        mediumBtn = (Button) findViewById(R.id.mediumBtn);
        mediumBtn.setOnClickListener(this);
        hardBtn = (Button) findViewById(R.id.hardBtn);
        hardBtn.setOnClickListener(this);
        mBombsTextView = (TextView) findViewById(R.id.bombsTextView);
        mGameStatusTextView = (TextView) findViewById(R.id.gameStatusTextView);
        mLevelTextView = (TextView) findViewById(R.id.levelTextView);
    }

    @Override
    public void setInitialText() {
        mBombsTextView.setText(String.valueOf(gameController.getGame().getBombs()));
        mGameStatusTextView.setText(R.string.game_started);
        mMineGridLayout.removeAllViews();
    }

    @Override
    public void updateGameStatus(int status) {
        mGameStatusTextView.setText(status);
    }

    @Override
    public void updateRemainingBombs(String status) {
        mBombsTextView.setText(status);
    }

    public void handleShakeEvent(int count) {
        if (count == 3 && gameController.getGameStatus() == GameController.GAME_STATUS_STARTED) {
            gameController.cheat();
            mGameStatusTextView.setText(R.string.game_cheat);

            // Get instance of Vibrator from current Context
            Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            // Vibrate for 300 milliseconds
            mVibrator.vibrate(1000);
        }

    }


}
