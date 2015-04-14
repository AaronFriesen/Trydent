package edu.gatech.cs2340.trydent.sample.tictactoe;

import edu.gatech.cs2340.trydent.TrydentEngine;

/**
 * A basic game of tic tac toe that doesn't check for the win condition.
 * There are three modes: keyboard, mouse, and GUI.
 * In the keyboard mode, use keys qwe-asd-zxc.
 * In the mouse mode, click on the tiles.
 * In the GUI mode, click on the buttons.
 */
public class TicTacToeExample implements Runnable {
    private static final String TITLE_SCREEN = "edu/gatech/cs2340/trydent/sample/tictactoe/TicTacToeTitle.fxml";

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Tic-Tac-Toe");
        TrydentEngine.setWindowSize(720, 480);
        TrydentEngine.runOnce(new TicTacToeExample());
    }

    @Override
    public void run() {
        TrydentEngine.setForeground(getClass().getClassLoader().getResourceAsStream(TITLE_SCREEN));
    }

}
