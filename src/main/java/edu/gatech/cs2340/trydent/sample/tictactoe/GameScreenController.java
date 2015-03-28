package edu.gatech.cs2340.trydent.sample.tictactoe;

import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.log.Log;

public class GameScreenController {
    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;

    private boolean player;

    public GameScreenController() {
        TrydentEngine.runOnce(() -> {
                Log.debug("Initialized tic-tac-toe game screen.");
                Arrays.asList(
                        button00, button01, button02, button10, button11, button12, button20, button21, button22
                   ).forEach(button -> {
                           button.setText("Click me!");
                           button.setUserData(true);
                       }
                    );
            }
        );
    }

    @FXML
    private void pressed(ActionEvent event) {
        TrydentEngine.runOnce(() -> {
                Button target = (Button) event.getSource();
                if((boolean) target.getUserData()){
                    if(player){
                        target.setText("X");
                    } else {
                        target.setText("O");
                    }
                    player = !player;
                    target.setUserData(false);
                }
            }
        );
    }
}
