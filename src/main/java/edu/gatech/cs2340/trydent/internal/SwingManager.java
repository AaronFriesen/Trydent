package edu.gatech.cs2340.trydent.internal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.gatech.cs2340.trydent.log.Log;

public class SwingManager implements JavaFXManager {

    private volatile JFrame frame;
    private volatile boolean starting = false;
    private volatile boolean stopping = false;

    private String windowTitle = "TrydentEngine";
    private int width = 640, height = 480;
    private Color backgroundColor = Color.TRANSPARENT;
    private Runnable updateAction;

    private Scene scene;
    private StackPane root;
    private Group background;

    private Timeline timeline;

    @Override
    public void startJavaFX() {
        if (starting)
            return;
        if (isRunning()) {
            Log.warn("TrydentEngine is already running.");
            return;
        }
        starting = true;
        createFrame();
    }

    private void createFrame() {
        frame = new JFrame(windowTitle);

        final JFXPanel panel = new JFXPanel();
        frame.add(panel);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene(panel);
            }
        });
    }

    private void createScene(JFXPanel panel) {
        root = new StackPane();
        scene = new Scene(root, backgroundColor);
        panel.setScene(scene);

        background = new Group();
        root.getChildren().addAll(new Pane(background));

        long sleepTimeMillis = 5;
        timeline = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (updateAction == null)
                    return;
                updateAction.run();
            }
        }), new KeyFrame(Duration.millis(sleepTimeMillis)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        starting = false;
    }

    @Override
    public void stopJavaFX() {
        if (stopping)
            return;

        if (frame != null && !starting) {
            stopping = true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setVisible(false);
                    frame.dispose();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            timeline.stop();
                            frame = null;
                            stopping = false;
                        }
                    });
                }
            });
        } else {
            Log.warn("Stop called before JavaFX started.");
        }
    }

    @Override
    public void setUpdateAction(Runnable updateAction) {
        this.updateAction = updateAction;
    }

    @Override
    public boolean isRunning() {
        return frame != null;
    }

    @Override
    public void setWindowTitle(String title) {
        windowTitle = String.valueOf(title);
        if (frame != null) {
            frame.setTitle(windowTitle);
        }
    }

    @Override
    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (frame != null) {
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
        }
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        if (fullscreen)
            throw new TrydentInternalException("Fullscreen not implemented yet.");
    }

    @Override
    public Group getBackground() {
        return background;
    }

    @Override
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        if (scene != null) {
            scene.setFill(color);
        }
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public Node setForeground(Node foreground) {
        if (root.getChildren().size() == 1) {
            root.getChildren().add(foreground);
            return null;
        } else {
            Node previous = root.getChildren().get(1);
            root.getChildren().set(1, foreground);
            return previous;
        }
    }
}
