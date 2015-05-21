package caatrin.com.unicornsweeper;

/**
 * Created by caatrin on 05/21/2015.
 */
public interface OnGameChangeListener {

    void setInitialText();
    void updateGameStatus(String status);
    void updateRemainingBombs(String status);

}
