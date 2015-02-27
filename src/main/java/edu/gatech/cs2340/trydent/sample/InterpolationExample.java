package edu.gatech.cs2340.trydent.sample;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import edu.gatech.cs2340.trydent.math.BaseVector;
import edu.gatech.cs2340.trydent.math.Position;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;
import edu.gatech.cs2340.trydent.math.curve.Curve;
import edu.gatech.cs2340.trydent.math.curve.Interpolation;
import edu.gatech.cs2340.trydent.math.curve.SplineCurve;

/**
 * Class to visually test the shapes of various interpolation curves.
 */
public class InterpolationExample extends JComponent {

    private static final long serialVersionUID = 7283474201296522376L;

    public static void main(String[] args) {
        int width = 800;
        int height = 750;
        final InterpolationExample example = new InterpolationExample(width, height);

        example.clear();

        Position[] pointsA = {
            new Position(60, 60),
            new Position(150, 60),
            new Position(250, 150),
            new Position(350, 100),
            new Position(450, 350),
            new Position(550, 200)
        };

        example.render(Color.BLACK, new SplineCurve<BaseVector<?>>(Interpolation.STRAIGHT, IndexWrapMode.CLAMP, pointsA), pointsA);
        example.render(Color.RED, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.REFLECT, pointsA), pointsA);
        example.render(Color.GREEN, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.WRAP, pointsA), pointsA);
        example.render(Color.BLUE, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.CLAMP, pointsA), pointsA);

        Position[] pointsB = {
            new Position(100, 400),
            new Position(100, 500),
            new Position(200, 600),
            new Position(300, 500),
            new Position(200, 400),
        };
        example.render(Color.BLACK, true, new SplineCurve<BaseVector<?>>(Interpolation.STRAIGHT, IndexWrapMode.WRAP, pointsB), pointsB);
        example.render(Color.RED, true, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.REFLECT, pointsB), pointsB);
        example.render(Color.GREEN, true, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.WRAP, pointsB), pointsB);
        example.render(Color.BLUE, true, new SplineCurve<BaseVector<?>>(Interpolation.SMOOTH, IndexWrapMode.CLAMP, pointsB), pointsB);

        JFrame frame = new JFrame("Interpolation Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(example);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try { Thread.sleep(10); } catch (InterruptedException x) {}
                    example.repaint();
                }
            }
        }).start();
    }

    private BufferedImage frameBuffer;
    private Graphics2D graphics;
    private double time = 0;
    private double speed = 0.2;
    private long lastMillis;

    private List<Runnable> renderActions;

    public InterpolationExample(int width, int height) {
        frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = frameBuffer.createGraphics();
        setPreferredSize(new Dimension(width, height));
        renderActions = new LinkedList<>();

        lastMillis = System.currentTimeMillis();
    }

    @Override
    public void paint(Graphics g) {
        time += (System.currentTimeMillis() - lastMillis)/1000.0 * speed;
        lastMillis = System.currentTimeMillis();

        for (Runnable r : renderActions) {
            r.run();
        }

        g.drawImage(frameBuffer, 0, 0, getWidth(), getHeight(), this);
    }

    private void addRenderAction(Runnable r) {
        renderActions.add(r);
        r.run();
    }

    private void clear() {
        addRenderAction(new Runnable() {
            @Override
            public void run() {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(-1, -1, frameBuffer.getWidth()+2, frameBuffer.getHeight()+2);
            }
        });
    }

    private void render(Color color, Curve<BaseVector<?>> curve, BaseVector<?> ... points) {
        render(color, false, curve, points);
    }

    private void render(final Color color, final boolean nPlus1, final Curve<BaseVector<?>> curve, final BaseVector<?> ... points) {
        addRenderAction(new Runnable() {
            @Override
            public void run() {
                doRender(color, nPlus1, curve, points);
            }
        });
    }

    private void doRender(Color color, boolean nPlus1, Curve<BaseVector<?>> curve, BaseVector<?> ... points) {
        Graphics2D g = this.graphics;
        g.setColor(color);
        g.setStroke(new BasicStroke(2.0f));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int subdivisions = 25 * points.length;

        double timeMultiplier = 1.0 / subdivisions;
        if (nPlus1) {
            timeMultiplier = 1.0 / (subdivisions - 25);
        }

        for (int n = 0; n < subdivisions; n++) {
            BaseVector<?> pA = curve.sample((n+0.0) * timeMultiplier);
            BaseVector<?> pB = curve.sample((n+1.0) * timeMultiplier);

            int ax = (int) Math.round(pA.getX()), ay = (int) Math.round(pA.getY());
            int bx = (int) Math.round(pB.getX()), by = (int) Math.round(pB.getY());
            g.drawLine(ax, ay, bx, by);
        }

        int w = 6;
        int h = 6;
        for (int i = 0; i < points.length; i++) {
            int x = (int) Math.round(points[i].getX() - w/2);
            int y = (int) Math.round(points[i].getY() - h/2);
            g.setColor(Color.WHITE);
            g.fillOval(x, y, w, h);
            g.setColor(color);
            g.drawOval(x, y, w, h);
        }

        int x = (int) curve.sample(time).getX();
        int y = (int) curve.sample(time).getY();
        g.setColor(Color.WHITE);
        g.fillOval(x, y, w, h);
        g.setColor(color);
        g.drawOval(x, y, w, h);
    }

}
