package caatrin.com.unicornsweeper.factory;

/**
 * Created by caatrin on 05/20/2015.
 */
public interface GameFactory {

    String GAME_EASY = "Easy";
    String GAME_MEDIUM = "Medium";
    String GAME_HARD = "Hard";

    Game createGame(String type);
}
