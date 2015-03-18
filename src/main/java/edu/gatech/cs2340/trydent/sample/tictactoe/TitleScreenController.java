package edu.gatech.cs2340.trydent.sample.tictactoe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import edu.gatech.cs2340.trydent.ContinuousEvent;
import edu.gatech.cs2340.trydent.GameObject;
import edu.gatech.cs2340.trydent.Keyboard;
import edu.gatech.cs2340.trydent.Mouse;
import edu.gatech.cs2340.trydent.TrydentEngine;
import edu.gatech.cs2340.trydent.internal.MouseImpl;
import edu.gatech.cs2340.trydent.log.Log;
import edu.gatech.cs2340.trydent.math.Position;


public class TitleScreenController {
    @FXML
    private Button start1;
    @FXML
    private Button start2;
    @FXML
    private Button start3;

    private boolean started2;
    private boolean started3;
    private Map<String, Rectangle> tiles;
    private Map<KeyCode, String> keyToName;
    private Set<Rectangle> used;
    private boolean player;

    public TitleScreenController() {
        Log.debug("Initialized tic-tac-toe title screen.");
        new ContinuousEvent(){
            @Override
            public void onUpdate() {
                if(started2) {
                    handleKeyboard();
                } else if(started3) {
                    handleMouse();
                }
            }
        };
    }

    private void handleKeyboard() {
        Rectangle target = null;
        for(KeyCode key : keyToName.keySet()){
            if(Keyboard.isKeyDownOnce(key)) {
                target = tiles.get(keyToName.get(key));
                break;
            }
        }

        if(target != null && !used.contains(target)) {
            if(player) {
                target.setFill(Color.RED);
            } else {
                target.setFill(Color.BLUE);
            }
            player = !player;
            used.add(target);
        }
    }

    private void handleMouse() {
        if(Mouse.isMouseDownOnce(MouseButton.PRIMARY)){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    Rectangle target = tiles.get(i + "_" + j);
                    if(target.contains(target.sceneToLocal(MouseImpl.getMouseX(), MouseImpl.getMouseY()))
                            && !used.contains(target)) {
                        if(player) {
                            target.setFill(Color.RED);
                        } else {
                            target.setFill(Color.BLUE);
                        }
                        player = !player;
                        used.add(target);
                    }
                }
            }
        }
    }

    @FXML
    private void toGameScreen(ActionEvent event) {
        TrydentEngine.runLater(() -> {
                if(started2 || started3) return;
                Log.debug("Switching to game screen.");
                TrydentEngine.setForeground(getClass().getResource("TicTacToeGameScreen.fxml"));
            }
        );
    }

    private void initEventScreen() {
        tiles = new HashMap<>();
        used = new HashSet<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String name = i + "_" + j;
                Rectangle rect = new Rectangle(50, 50, Color.BLACK);
                tiles.put(name, rect);
                new GameObject(name, rect).setPosition(new Position(i * 60, j * 60));
            }
        }
        keyToName = new HashMap<>();
        keyToName.put(KeyCode.Q, "0_0");
        keyToName.put(KeyCode.A, "0_1");
        keyToName.put(KeyCode.Z, "0_2");
        keyToName.put(KeyCode.W, "1_0");
        keyToName.put(KeyCode.S, "1_1");
        keyToName.put(KeyCode.X, "1_2");
        keyToName.put(KeyCode.E, "2_0");
        keyToName.put(KeyCode.D, "2_1");
        keyToName.put(KeyCode.C, "2_2");

        start1.setOpacity(0);
        start2.setOpacity(0);
        start3.setOpacity(0);
    }

    @FXML
    private void toGameScreen2(ActionEvent event) {
        TrydentEngine.runLater(() -> {
                if(started2 || started3) return;
                Log.debug("Started game type 2.");
                initEventScreen();
                started2 = true;
            }
        );
    }

    @FXML
    private void toGameScreen3(ActionEvent event) {
        TrydentEngine.runLater(() -> {
                if(started2 || started3) return;
                Log.debug("Started game type 3.");
                initEventScreen();
                started3 = true;
            }
        );
    }

}
