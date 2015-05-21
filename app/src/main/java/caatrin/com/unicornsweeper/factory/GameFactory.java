package caatrin.com.unicornsweeper.factory;

/**
 * Created by caatrin on 05/20/2015.
 */
public interface GameFactory {

    String GAME_EASY = "easy";
    String GAME_MEDIUM = "medium";
    String GAME_HARD = "hard";

    Game createGame(String type);
}
