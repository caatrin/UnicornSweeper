package caatrin.com.unicornsweeper.factory;

/**
 * Created by caatrin on 05/20/2015.
 */
public class GameFactoryImpl implements GameFactory {


    @Override
    public Game createGame(String type) {
        Game game = null;

        switch (type) {
            case GAME_EASY :
                game = new EasyGame(5, 5, 3);
                break;
            case GAME_MEDIUM :
                game = new MediumGame(7, 7, 6);
                break;
            case GAME_HARD :
                game = new HardGame(8, 8, 10);
                break;
        }
        return game;
    }
}
