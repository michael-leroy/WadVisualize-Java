import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Used to display WAD maps.
 */
public class MapPanel extends JPanel {

    // Describes MIN/MAX of a WAD map.
    public static final double MIN_X = -32768.;
    public static final double MIN_Y = -32768.;
    public static final double MAX_X = 32767.;
    public static final double MAX_Y = 32767.;

    // Set various scaling options.
    public static final int MAX_SCALE = 50;
    public static final int MIN_SCALE = 2;
    public static final int STARTUP_SCALE = 4;

    // Scale the map to get a better view. A 4x scale seems to provide a decent
    // starting view.
    private double scaleFactor = STARTUP_SCALE;

    // Eclipse wanted this. Great I guess?
    private static final long serialVersionUID = 1L;

    // Walls/LINEDEFS that make up the map.
    private List<Line2D.Double> wallsToDraw;

    /**
     * Builds MapPanel with semi-sane defaults.
     */
    public MapPanel() {
        setPreferredSize(new Dimension((int) MAX_X / (int) this.scaleFactor,
                (int) MAX_Y / (int) this.scaleFactor));

        setMaximumSize(new Dimension((int) MAX_X, (int) MAX_Y));

        // Used to store map LINEDEFS.
        this.wallsToDraw = new ArrayList<>();

        //Adding line to help the panel draw properly on startup.
        this.wallsToDraw.add(new Line2D.Double(1088, -3680, 1024, -3680));
    }

    /**
     * @return The current scale factor used to draw the map.
     */
    public double getScaleFactor() {
        return this.scaleFactor;
    }

    /**
     * @param newValue Change scale to display the map with.
     * @return
     */
    public void setScaleFactor(int newValue) {
        this.scaleFactor = newValue;
    }

    /**
     * @param wallsToDraw List of map LINEDEFS.
     */
    public void setWallsToDraw(List<Line2D.Double> wallsToDraw) {
        this.wallsToDraw = wallsToDraw;
    }

    /**
     * @param walls List of map LINEDEFS.
     * @param g2 graphics object that displays the map.
     */
    public void drawWalls(List<Line2D.Double> walls, Graphics2D g2) {
        Dimension currentSize = getSize();
        double currentX = currentSize.getWidth();
        double currentY = currentSize.getHeight();

        g2.setColor(Color.red);

        System.out.println(walls.size());

        for (Line2D.Double wall : walls) {

            double scaleX1;
            double scaleY1;
            double scaleX2;
            double scaleY2;

            scaleX1 = ((wall.getX1()) / (getScaleFactor() * 2))
                    + (currentX / 2);

            scaleY1 = (((-wall.getY1())) / (getScaleFactor() * 2))
                    + (currentY / 2);

            scaleX2 = ((wall.getX2()) / (getScaleFactor() * 2))
                    + (currentX / 2);

            scaleY2 = (((-wall.getY2())) / (getScaleFactor() * 2))
                    + (currentY / 2);

            g2.draw(new Line2D.Double(scaleX1, scaleY1, scaleX2, scaleY2));

        }
    }

    /**
     * Used to draw a grid. Needs to be fixed.
     * @param g2d Graphics object to display maps/grid
     */
    public void drawGrid(Graphics2D g2d) {
        Dimension currentSize = getSize();
        double currentX = currentSize.getWidth();
        double currentY = currentSize.getHeight();
        for (double i = 0.; i < getScaleFactor(); i = i
                + (getScaleFactor() / currentX)) {
            g2d.draw(new Line2D.Double(i, 0., i, currentY));
            g2d.draw(new Line2D.Double(0., i, currentX, i));
        }
    }

    /**
     * @param g Graphics object that displays the WAD map.
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        System.out.println("Running paint!");

        Graphics2D g2d = (Graphics2D) g;

        Dimension currentSize = getSize();
        System.out.println(currentSize);

        // drawGrid(g2d);
        drawWalls(this.wallsToDraw, g2d);

    }
}