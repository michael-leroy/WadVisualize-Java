import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GridPanel extends JPanel {

    public static final double MIN_X = -32768.;
    public static final double MIN_Y = -32768.;
    public static final double MAX_X = 32767.;
    public static final double MAX_Y = 32767.;

    public static final int MAX_SCALE = 55;
    public static final int MIN_SCALE = 10;
    public static final int STARTUP_SCALE = 4;

    public double minX = MIN_X;
    public double minY = MIN_Y;
    public double maxX = MAX_X;
    public double maxY = MAX_Y;

    private double scaleFactor = STARTUP_SCALE;

    private static final long serialVersionUID = 1L;


    private List<Line2D.Double> wallsToDraw;

    public GridPanel() {

        setPreferredSize(new Dimension((int) MAX_X / (int) this.scaleFactor,
                (int) MAX_Y / (int) this.scaleFactor));
        setMaximumSize(new Dimension((int) MAX_X, (int) MAX_Y));

        this.wallsToDraw = new ArrayList<>();

        this.wallsToDraw.add(new Line2D.Double(1088, -3680, 1024, -3680));

    }






    public double getScaleFactor() {
        return this.scaleFactor;
    }






    public double setScaleFactor(int newValue) {
        return this.scaleFactor = newValue;
    }






    public int setMinX(double newValue) {
        if (newValue >= MIN_X) {
            this.minX = newValue;
            return 1;
        }
        return 0;
    }






    public int setMaxX(double newValue) {
        if (newValue <= MAX_X) {
            this.maxX = newValue;
            return 1;
        }
        return 0;
    }






    public int setMinY(double newValue) {
        if (newValue >= MIN_Y) {
            this.minY = newValue;
            return 1;
        }
        return 0;
    }






    public int setMaxY(double newValue) {
        if (newValue <= MAX_Y) {
            this.maxY = newValue;
            return 1;
        }
        return 0;
    }






    public void detUsedMap(List<Line2D.Double> lineList) {

        for (Line2D.Double aDouble : lineList) {

            double x1 = aDouble.getX1();
            double x2 = aDouble.getX2();

            double y1 = aDouble.getY1();
            double y2 = aDouble.getY2();

            System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);

            setMinX(Math.min(this.minX, x1));
            setMaxX(Math.max(this.maxX, x1));

            setMinY(Math.min(this.minY, -y1));
            setMaxY(Math.max(this.maxY, -y1));

            setMinX(Math.min(this.minX, x2));
            setMaxX(Math.max(this.maxX, x2));

            setMinY(Math.min(this.minY, -y2));
            setMaxY(Math.max(this.maxY, -y2));

        }
    }

    public List<Line2D.Double> getWallsToDraw() {
        return this.wallsToDraw;
    }

    public void setWallsToDraw(List<Line2D.Double> wallsToDraw) {
        this.wallsToDraw = wallsToDraw;
    }

    public int drawWalls(List<Line2D.Double> walls, Graphics2D g2) {
        Dimension currentSize = getSize();
        double currentX = currentSize.getWidth();
        double currentY = currentSize.getHeight();

        // double originTrans = ((currentX + currentY) / 4) / 2;

        // double currentScale = currentX / (this.maxX - this.minX);

        // System.out.println(originTrans);

        g2.setColor(Color.red);

        System.out.println(walls.size());

        for (Line2D.Double wall : walls) {

            double scaleX1;
            double scaleY1;
            double scaleX2;
            double scaleY2;
            // g2.translate(currentX / 2, currentY / 2);

            scaleX1 = ((wall.getX1()) / (getScaleFactor() * 2))
                    + (currentX / 2);

            scaleY1 = (((-wall.getY1())) / (getScaleFactor() * 2))
                    + (currentY / 2);

            scaleX2 = ((wall.getX2()) / (getScaleFactor() * 2))
                    + (currentX / 2);

            scaleY2 = (((-wall.getY2())) / (getScaleFactor() * 2))
                    + (currentY / 2);

            //System.out.println(
            //       scaleX1 + " " + scaleY1 + " " + scaleX2 + " " + scaleY2);

            // g2.scale(0.1, 0.1);
            g2.draw(new Line2D.Double(scaleX1, scaleY1, scaleX2, scaleY2));

            // g2.translate(currentX, currentY);
            // g2.draw(walls.get(i));
            // g2.translate((MAX_X + currentX) / getScaleFactor(),
            // (MAX_Y + currentY) / getScaleFactor());
        }
        return 1;
    }






    public int drawGrid(Graphics2D g2d) {
        Dimension currentSize = getSize();
        double currentX = currentSize.getWidth();
        double currentY = currentSize.getHeight();
        for (double i = 0.; i < getScaleFactor(); i = i
                + (getScaleFactor() / currentX)) {
            g2d.draw(new Line2D.Double(i, 0., i, currentY));
            g2d.draw(new Line2D.Double(0., i, currentX, i));
        }
        return 1;
    }






    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        System.out.println("Running paint!");

        Graphics2D g2d = (Graphics2D) g;

        Dimension currentSize = getSize();
        System.out.println(currentSize);

        //ArrayList<Line2D.Double> mapArr = new ArrayList<>();

        // TODO: Replace with a doom map array once support is completed.

        System.out.println("maxX: " + this.maxX);
        System.out.println("maxY: " + this.maxY);

        System.out.println("minX: " + this.minX);
        System.out.println("minX: " + this.minY);

        // drawGrid(g2d);
        drawWalls(this.wallsToDraw, g2d);

    }
}