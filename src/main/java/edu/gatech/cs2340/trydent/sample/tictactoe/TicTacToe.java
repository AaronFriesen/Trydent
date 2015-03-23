package edu.gatech.cs2340.trydent.sample.tictactoe;

import edu.gatech.cs2340.trydent.TrydentEngine;

public class TicTacToe implements Runnable {

    public static void main(String[] args) {
        TrydentEngine.start();
        TrydentEngine.setWindowTitle("TrydentEngine - Tic-Tac-Toe");
        TrydentEngine.setWindowSize(720, 480);
        TrydentEngine.runLater(new TicTacToe());
    }

    @Override
    public void run() {
        TrydentEngine.setForeground(getClass().getResource("TicTacToeTitle.fxml"));
    }

}
