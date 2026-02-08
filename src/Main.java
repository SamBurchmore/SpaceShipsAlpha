import Data.Board;
import Logic.AutoPlay;
import Logic.BoardState;
import Logic.MainController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        MainController mainController = new MainController();
        AutoPlay autoPlay = new AutoPlay(mainController);
        autoPlay.nextStates(mainController.getGame().deepCopy(), mainController.getGame(), 5000);

    }
}