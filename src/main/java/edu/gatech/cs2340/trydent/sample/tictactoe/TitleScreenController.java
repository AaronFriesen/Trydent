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
import edu.gatech.cs2340.trydent.OneTimeEvent;
import edu.gatech.cs2340.trydent.TrydentEngine;
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
        Rectangle target;
        if(Keyboard.isKeyDownOnce(KeyCode.Q)){
            target = tiles.get("0_0");
        } else if(Keyboard.isKeyDownOnce(KeyCode.A)){
            target = tiles.get("0_1");
        } else if(Keyboard.isKeyDownOnce(KeyCode.Z)){
            target = tiles.get("0_2");
        } else if(Keyboard.isKeyDownOnce(KeyCode.W)){
            target = tiles.get("1_0");
        } else if(Keyboard.isKeyDownOnce(KeyCode.S)){
            target = tiles.get("1_1");
        } else if(Keyboard.isKeyDownOnce(KeyCode.X)){
            target = tiles.get("1_2");
        } else if(Keyboard.isKeyDownOnce(KeyCode.E)){
            target = tiles.get("2_0");
        } else if(Keyboard.isKeyDownOnce(KeyCode.D)){
            target = tiles.get("2_1");
        } else if(Keyboard.isKeyDownOnce(KeyCode.C)){
            target = tiles.get("2_2");
        } else {
            target = null;
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
                    if(target.contains(target.sceneToLocal(Mouse.getMouseX(), Mouse.getMouseY()))
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
        OneTimeEvent.simplified(() -> {
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
                (new GameObject(name, rect)).setPosition(new Position(i * 60, j * 60));
            }
        }
        start1.setOpacity(0);
        start2.setOpacity(0);
        start3.setOpacity(0);
    }

    @FXML
    private void toGameScreen2(ActionEvent event) {
        OneTimeEvent.simplified(() -> {
                if(started2 || started3) return;
                Log.debug("Started game type 2.");
                initEventScreen();
                started2 = true;
            }
        );
    }

    @FXML
    private void toGameScreen3(ActionEvent event) {
        OneTimeEvent.simplified(() -> {
                if(started2 || started3) return;
                Log.debug("Started game type 3.");
                initEventScreen();
                started3 = true;
            }
        );
    }

}
