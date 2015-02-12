package edu.gatech.cs2340.trydent.sample;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.sun.javafx.geom.Rectangle;

import edu.gatech.cs2340.trydent.math.curve.Curve;
import edu.gatech.cs2340.trydent.math.curve.IndexWrapMode;

/**
 * Class to visually test the shapes of various interpolation curves.
 */
public class IndexWrapExample extends JComponent {

    private static final long serialVersionUID = 7283474201296522376L;

    public static void main(String[] args) {
        int width = 800;
        int height = 750;
        final IndexWrapExample example = new IndexWrapExample(width, height);

        example.clear();

        double[] domain = new double[1001];
        for (int i = 0; i < domain.length; i++) {
            domain[i] = 50 * i/(domain.length-1.0);
        }

        example.render("sin(x)", Color.BLUE, new Rectangle(30, 30, width/4, height/4), -1, 1, new Curve<Double>() {
            @Override
            public Double sample(double t) {
                return Math.sin(t*Math.PI*3/50);
            }
        }, domain);

        example.render("clamp(x, 10)", Color.BLUE, new Rectangle(30+width/2, 30, width/4, height/4), 0, 10, new Curve<Double>() {
            @Override
            public Double sample(double t) {
                return (double) IndexWrapMode.CLAMP.handle((int)t, 10);
            }
        }, domain);

        example.render("wrap(x, 10)", Color.BLUE, new Rectangle(30, 30+height/2, width/4, height/4), 0, 10, new Curve<Double>() {
            @Override
            public Double sample(double t) {
                return (double) IndexWrapMode.WRAP.handle((int)t, 10);
            }
        }, domain);

        example.render("reflect(x, 10)", Color.BLUE, new Rectangle(30+width/2, 30+height/2, width/4, height/4), 0, 10, new Curve<Double>() {
            @Override
            public Double sample(double t) {
                return (double) IndexWrapMode.REFLECT.handle((int)t, 10);
            }
        }, domain);

        JFrame frame = new JFrame("Index Wrapping Testing");
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

    private List<Runnable> renderActions;

    public IndexWrapExample(int width, int height) {
        frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = frameBuffer.createGraphics();
        setPreferredSize(new Dimension(width, height));
        renderActions = new LinkedList<>();
    }

    @Override
    public void paint(Graphics g) {
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

    private void render(final String label, final Color color, final Rectangle chart, final double minRange, final double maxRange, final Curve<Double> function, final double ... independent) {
        addRenderAction(new Runnable() {
            @Override
            public void run() {
                doRender(label, color, chart, minRange, maxRange, function, independent);
            }
        });
    }

    private void doRender(String label, Color color, Rectangle chart, double minRange, double maxRange, Curve<Double> function, double ... independent) {
        Graphics2D g = this.graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Arrays.sort(independent);

        double[] dependent = new double[independent.length];
        for (int i = 0; i < independent.length; i++) {
            dependent[i] = function.sample(independent[i]);
            if (dependent[i] < minRange) minRange = dependent[i];
            if (dependent[i] > maxRange) maxRange = dependent[i];
        }

        g.setStroke(new BasicStroke(1.0f));
        g.setColor(Color.GRAY);
        int lines = 10;
        for (int i = 0; i < lines; i++) {
            double s = (i+1.0)/(lines);
            int y = (int)Math.round(chart.y + s * chart.height);
            g.drawLine(chart.x, y, chart.x + chart.width, y);
        }

        g.setStroke(new BasicStroke(2.0f));
        g.setColor(color);
        for (int i = 0; i < independent.length-1; i++) {
            int x0 = (int) Math.round(chart.x + chart.width * (independent[i] - independent[0])/(independent[independent.length-1] - independent[0]));
            int y0 = (int) Math.round(chart.y + chart.height - chart.height * (dependent[i] - minRange)/(maxRange - minRange));
            int x1 = (int) Math.round(chart.x + chart.width * (independent[i+1] - independent[0])/(independent[independent.length-1] - independent[0]));
            int y1 = (int) Math.round(chart.y + chart.height - chart.height * (dependent[i+1] - minRange)/(maxRange - minRange));
            g.drawLine(x0, y0, x1, y1);
        }

        g.setStroke(new BasicStroke(1.0f));
        g.setColor(Color.BLACK);
        g.drawRect(chart.x, chart.y, chart.width, chart.height);

        String s = null;

        s = String.valueOf(maxRange);
        g.drawString(s, chart.x - g.getFontMetrics().stringWidth(s), chart.y);
        s = String.valueOf(minRange);
        g.drawString(s, chart.x - g.getFontMetrics().stringWidth(s), chart.y + chart.height);

        s = String.valueOf(independent[0]);
        g.drawString(s, chart.x, chart.y + chart.height + g.getFontMetrics().getHeight());
        s = String.valueOf(independent[1]);
        g.drawString(s, chart.x + chart.width, chart.y + chart.height + g.getFontMetrics().getHeight());

        s = label;
        g.drawString(s, chart.x + chart.width/2 - g.getFontMetrics().stringWidth(s)/2, chart.y - g.getFontMetrics().getDescent() - 2);
    }

}
