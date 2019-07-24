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

    ArrayList<Line2D.Double> wallsToDraw = new ArrayList<>();






    public GridPanel() {

        setPreferredSize(new Dimension((int) MAX_X / (int) this.scaleFactor,
                (int) MAX_Y / (int) this.scaleFactor));
        setMaximumSize(new Dimension((int) MAX_X, (int) MAX_Y));
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






    public int drawWalls(ArrayList<Line2D.Double> walls, Graphics2D g2) {
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

        ArrayList<Line2D.Double> testArr = new ArrayList<>();

        // TODO: Replace with a doom map array once support is completed.
        testArr.add(new Line2D.Double(1088, -3680, 1024, -3680));
        testArr.add(new Line2D.Double(1024, -3680, 1024, -3648));
        testArr.add(new Line2D.Double(1088, -3648, 1088, -3680));
        testArr.add(new Line2D.Double(1152, -3648, 1088, -3648));
        testArr.add(new Line2D.Double(1024, -3648, 960, -3648));
        testArr.add(new Line2D.Double(1280, -3552, 1152, -3648));
        testArr.add(new Line2D.Double(960, -3648, 832, -3552));
        testArr.add(new Line2D.Double(1344, -3552, 1280, -3552));
        testArr.add(new Line2D.Double(832, -3552, 704, -3552));
        testArr.add(new Line2D.Double(896, -3392, 928, -3392));
        testArr.add(new Line2D.Double(928, -3392, 928, -3360));
        testArr.add(new Line2D.Double(928, -3360, 896, -3360));
        testArr.add(new Line2D.Double(896, -3360, 896, -3392));
        testArr.add(new Line2D.Double(1184, -3392, 1216, -3392));
        testArr.add(new Line2D.Double(1216, -3392, 1216, -3360));
        testArr.add(new Line2D.Double(1216, -3360, 1184, -3360));
        testArr.add(new Line2D.Double(1184, -3360, 1184, -3392));
        testArr.add(new Line2D.Double(896, -3072, 896, -3104));
        testArr.add(new Line2D.Double(896, -3104, 928, -3104));
        testArr.add(new Line2D.Double(928, -3104, 928, -3072));
        testArr.add(new Line2D.Double(928, -3072, 896, -3072));
        testArr.add(new Line2D.Double(1216, -3072, 1184, -3072));
        testArr.add(new Line2D.Double(1184, -3072, 1184, -3104));
        testArr.add(new Line2D.Double(1184, -3104, 1216, -3104));
        testArr.add(new Line2D.Double(1216, -3104, 1216, -3072));
        testArr.add(new Line2D.Double(1344, -3360, 1344, -3552));
        testArr.add(new Line2D.Double(1344, -3264, 1344, -3360));
        testArr.add(new Line2D.Double(1344, -3200, 1344, -3264));
        testArr.add(new Line2D.Double(1344, -2880, 1344, -3104));
        testArr.add(new Line2D.Double(1344, -3104, 1344, -3200));
        testArr.add(new Line2D.Double(1376, -3200, 1376, -3104));
        testArr.add(new Line2D.Double(1376, -3360, 1376, -3264));
        testArr.add(new Line2D.Double(1344, -3264, 1376, -3264));
        testArr.add(new Line2D.Double(1376, -3360, 1344, -3360));
        testArr.add(new Line2D.Double(1376, -3200, 1344, -3200));
        testArr.add(new Line2D.Double(1344, -3104, 1376, -3104));
        testArr.add(new Line2D.Double(1376, -3264, 1376, -3200));
        testArr.add(new Line2D.Double(1376, -3648, 1376, -3360));
        testArr.add(new Line2D.Double(1376, -3104, 1376, -2944));
        testArr.add(new Line2D.Double(1184, -3392, 928, -3392));
        testArr.add(new Line2D.Double(1344, -3360, 1216, -3392));
        testArr.add(new Line2D.Double(1216, -3072, 1344, -3104));
        testArr.add(new Line2D.Double(704, -2944, 832, -2944));
        testArr.add(new Line2D.Double(832, -2944, 968, -2880));
        testArr.add(new Line2D.Double(968, -2880, 1216, -2880));
        testArr.add(new Line2D.Double(1376, -2944, 1472, -2880));
        testArr.add(new Line2D.Double(1472, -2880, 1856, -2880));
        testArr.add(new Line2D.Double(1528, -3680, 1376, -3648));
        testArr.add(new Line2D.Double(1672, -3744, 1528, -3680));
        testArr.add(new Line2D.Double(1088, -3648, 1024, -3648));
        testArr.add(new Line2D.Double(928, -3104, 1184, -3104));
        testArr.add(new Line2D.Double(1184, -3360, 928, -3360));
        testArr.add(new Line2D.Double(928, -3360, 928, -3104));
        testArr.add(new Line2D.Double(896, -3360, 896, -3104));
        testArr.add(new Line2D.Double(928, -3072, 1184, -3072));
        testArr.add(new Line2D.Double(704, -3552, 704, -3360));
        testArr.add(new Line2D.Double(704, -3104, 704, -2944));
        testArr.add(new Line2D.Double(704, -3104, 704, -3360));
        testArr.add(new Line2D.Double(512, -3328, 512, -3304));
        testArr.add(new Line2D.Double(512, -3160, 512, -3136));
        testArr.add(new Line2D.Double(512, -3136, 680, -3104));
        testArr.add(new Line2D.Double(680, -3104, 704, -3104));
        testArr.add(new Line2D.Double(704, -3360, 680, -3360));
        testArr.add(new Line2D.Double(680, -3360, 512, -3328));
        testArr.add(new Line2D.Double(496, -3160, 496, -3304));
        testArr.add(new Line2D.Double(512, -3304, 496, -3304));
        testArr.add(new Line2D.Double(496, -3160, 512, -3160));
        testArr.add(new Line2D.Double(496, -3304, 496, -3328));
        testArr.add(new Line2D.Double(496, -3328, 448, -3456));
        testArr.add(new Line2D.Double(448, -3456, 128, -3456));
        testArr.add(new Line2D.Double(128, -3008, 448, -3008));
        testArr.add(new Line2D.Double(496, -3136, 496, -3160));
        testArr.add(new Line2D.Double(448, -3008, 496, -3136));
        testArr.add(new Line2D.Double(128, -3264, 160, -3264));
        testArr.add(new Line2D.Double(160, -3264, 192, -3264));
        testArr.add(new Line2D.Double(192, -3264, 224, -3264));
        testArr.add(new Line2D.Double(224, -3264, 256, -3264));
        testArr.add(new Line2D.Double(256, -3264, 288, -3264));
        testArr.add(new Line2D.Double(288, -3264, 320, -3264));
        testArr.add(new Line2D.Double(320, -3264, 320, -3200));
        testArr.add(new Line2D.Double(320, -3200, 288, -3200));
        testArr.add(new Line2D.Double(288, -3200, 256, -3200));
        testArr.add(new Line2D.Double(256, -3200, 224, -3200));
        testArr.add(new Line2D.Double(224, -3200, 192, -3200));
        testArr.add(new Line2D.Double(192, -3200, 160, -3200));
        testArr.add(new Line2D.Double(160, -3200, 128, -3200));
        testArr.add(new Line2D.Double(160, -3264, 160, -3200));
        testArr.add(new Line2D.Double(192, -3264, 192, -3200));
        testArr.add(new Line2D.Double(224, -3264, 224, -3200));
        testArr.add(new Line2D.Double(256, -3264, 256, -3200));
        testArr.add(new Line2D.Double(288, -3264, 288, -3200));
        testArr.add(new Line2D.Double(128, -3264, 128, -3200));
        testArr.add(new Line2D.Double(128, -3200, 64, -3072));
        testArr.add(new Line2D.Double(64, -3072, 128, -3008));
        testArr.add(new Line2D.Double(128, -3456, 64, -3392));
        testArr.add(new Line2D.Double(64, -3392, 128, -3264));
        testArr.add(new Line2D.Double(64, -3392, 48, -3392));
        testArr.add(new Line2D.Double(48, -3392, -64, -3328));
        testArr.add(new Line2D.Double(-64, -3136, 48, -3072));
        testArr.add(new Line2D.Double(48, -3072, 64, -3072));
        testArr.add(new Line2D.Double(-256, -3328, -320, -3296));
        testArr.add(new Line2D.Double(-320, -3168, -256, -3136));
        testArr.add(new Line2D.Double(-128, -3120, -128, -3136));
        testArr.add(new Line2D.Double(-256, -3136, -256, -3120));
        testArr.add(new Line2D.Double(-256, -3344, -256, -3328));
        testArr.add(new Line2D.Double(-128, -3328, -128, -3344));
        testArr.add(new Line2D.Double(-320, -3296, -336, -3296));
        testArr.add(new Line2D.Double(-336, -3168, -320, -3168));
        testArr.add(new Line2D.Double(-256, -3120, -336, -3120));
        testArr.add(new Line2D.Double(-336, -3120, -336, -3168));
        testArr.add(new Line2D.Double(-336, -3296, -336, -3344));
        testArr.add(new Line2D.Double(-336, -3344, -256, -3344));
        testArr.add(new Line2D.Double(-128, -3344, -96, -3344));
        testArr.add(new Line2D.Double(-96, -3344, 64, -3520));
        testArr.add(new Line2D.Double(-96, -3120, -128, -3120));
        testArr.add(new Line2D.Double(64, -2944, -96, -3120));
        testArr.add(new Line2D.Double(-64, -3136, -64, -3328));
        testArr.add(new Line2D.Double(-128, -3328, -256, -3328));
        testArr.add(new Line2D.Double(-256, -3344, -128, -3344));
        testArr.add(new Line2D.Double(-256, -3136, -128, -3136));
        testArr.add(new Line2D.Double(-128, -3120, -256, -3120));
        testArr.add(new Line2D.Double(-320, -3296, -320, -3168));
        testArr.add(new Line2D.Double(-336, -3168, -336, -3296));
        testArr.add(new Line2D.Double(64, -2816, 64, -2944));
        testArr.add(new Line2D.Double(64, -3520, 64, -3648));
        testArr.add(new Line2D.Double(64, -3648, -640, -3648));
        testArr.add(new Line2D.Double(-640, -3648, -768, -3520));
        testArr.add(new Line2D.Double(-768, -3520, -768, -2944));
        testArr.add(new Line2D.Double(-768, -2944, -640, -2816));
        testArr.add(new Line2D.Double(-640, -2816, 64, -2816));
        testArr.add(new Line2D.Double(64, -3648, -640, -3520));
        testArr.add(new Line2D.Double(-640, -3520, -640, -2944));
        testArr.add(new Line2D.Double(-640, -2944, 64, -2816));
        testArr.add(new Line2D.Double(-128, -3136, -88, -3136));
        testArr.add(new Line2D.Double(-88, -3136, -64, -3136));
        testArr.add(new Line2D.Double(-64, -3328, -88, -3328));
        testArr.add(new Line2D.Double(-88, -3328, -128, -3328));
        testArr.add(new Line2D.Double(256, -3136, 320, -3136));
        testArr.add(new Line2D.Double(320, -3136, 320, -3072));
        testArr.add(new Line2D.Double(320, -3072, 256, -3072));
        testArr.add(new Line2D.Double(256, -3072, 256, -3136));
        testArr.add(new Line2D.Double(256, -3392, 320, -3392));
        testArr.add(new Line2D.Double(320, -3392, 320, -3328));
        testArr.add(new Line2D.Double(320, -3328, 256, -3328));
        testArr.add(new Line2D.Double(256, -3328, 256, -3392));
        testArr.add(new Line2D.Double(1216, -2880, 1248, -2528));
        testArr.add(new Line2D.Double(1384, -2592, 1344, -2880));
        testArr.add(new Line2D.Double(1472, -2560, 1384, -2592));
        testArr.add(new Line2D.Double(1248, -2528, 1472, -2432));
        testArr.add(new Line2D.Double(1344, -2880, 1216, -2880));
        testArr.add(new Line2D.Double(1472, -2432, 1472, -2560));
        testArr.add(new Line2D.Double(1536, -2432, 1536, -2560));
        testArr.add(new Line2D.Double(1552, -2560, 1552, -2432));
        testArr.add(new Line2D.Double(1536, -2560, 1472, -2560));
        testArr.add(new Line2D.Double(1472, -2432, 1536, -2432));
        testArr.add(new Line2D.Double(1536, -2432, 1552, -2432));
        testArr.add(new Line2D.Double(1552, -2560, 1536, -2560));
        testArr.add(new Line2D.Double(1664, -2560, 1552, -2560));
        testArr.add(new Line2D.Double(1552, -2432, 1664, -2432));
        testArr.add(new Line2D.Double(2736, -3648, 2488, -3744));
        testArr.add(new Line2D.Double(2488, -3744, 2240, -3776));
        testArr.add(new Line2D.Double(1984, -3776, 1672, -3744));
        testArr.add(new Line2D.Double(1856, -2880, 1920, -2920));
        testArr.add(new Line2D.Double(1920, -2920, 2240, -2920));
        testArr.add(new Line2D.Double(1520, -3168, 1672, -3104));
        testArr.add(new Line2D.Double(1672, -3104, 1896, -3104));
        testArr.add(new Line2D.Double(1896, -3104, 2040, -3144));
        testArr.add(new Line2D.Double(2040, -3144, 2128, -3272));
        testArr.add(new Line2D.Double(2128, -3272, 2064, -3408));
        testArr.add(new Line2D.Double(2064, -3408, 1784, -3448));
        testArr.add(new Line2D.Double(1784, -3448, 1544, -3384));
        testArr.add(new Line2D.Double(1544, -3384, 1520, -3168));
        testArr.add(new Line2D.Double(2752, -2784, 2624, -2784));
        testArr.add(new Line2D.Double(2520, -2560, 2752, -2560));
        testArr.add(new Line2D.Double(2752, -2560, 2944, -2656));
        testArr.add(new Line2D.Double(2880, -2912, 2880, -2880));
        testArr.add(new Line2D.Double(3048, -2880, 3048, -2944));
        testArr.add(new Line2D.Double(2752, -3048, 2752, -2912));
        testArr.add(new Line2D.Double(2752, -3584, 2752, -3360));
        testArr.add(new Line2D.Double(2736, -3360, 2736, -3648));
        testArr.add(new Line2D.Double(2752, -3360, 2736, -3360));
        testArr.add(new Line2D.Double(3048, -2944, 3304, -3040));
        testArr.add(new Line2D.Double(3136, -3072, 3304, -3040));
        testArr.add(new Line2D.Double(3112, -3360, 2944, -3536));
        testArr.add(new Line2D.Double(2816, -3232, 3112, -3360));
        testArr.add(new Line2D.Double(3280, -3320, 2984, -3200));
        testArr.add(new Line2D.Double(2976, -3072, 2816, -3232));
        testArr.add(new Line2D.Double(2984, -3200, 3136, -3072));
        testArr.add(new Line2D.Double(3264, -3616, 3072, -3648));
        testArr.add(new Line2D.Double(2944, -3648, 2752, -3584));
        testArr.add(new Line2D.Double(3072, -3648, 3072, -4000));
        testArr.add(new Line2D.Double(2944, -3776, 2944, -3648));
        testArr.add(new Line2D.Double(2752, -2784, 2944, -2656));
        testArr.add(new Line2D.Double(2752, -3360, 2944, -3536));
        testArr.add(new Line2D.Double(2944, -3536, 3072, -3648));
        testArr.add(new Line2D.Double(2752, -3048, 3048, -2880));
        testArr.add(new Line2D.Double(2944, -3536, 2752, -3584));
        testArr.add(new Line2D.Double(3104, -3552, 3280, -3320));
        testArr.add(new Line2D.Double(3264, -3616, 3104, -3552));
        testArr.add(new Line2D.Double(3352, -3568, 3264, -3616));
        testArr.add(new Line2D.Double(3472, -3432, 3408, -3432));
        testArr.add(new Line2D.Double(3408, -3432, 3312, -3496));
        testArr.add(new Line2D.Double(3312, -3496, 3352, -3568));
        testArr.add(new Line2D.Double(2240, -3776, 2208, -3680));
        testArr.add(new Line2D.Double(2208, -3680, 2176, -3680));
        testArr.add(new Line2D.Double(2016, -3680, 1984, -3776));
        testArr.add(new Line2D.Double(2048, -3680, 2016, -3680));
        testArr.add(new Line2D.Double(2176, -3776, 2176, -3808));
        testArr.add(new Line2D.Double(2176, -3808, 2176, -3840));
        testArr.add(new Line2D.Double(2048, -3808, 2048, -3776));
        testArr.add(new Line2D.Double(2048, -3840, 2048, -3808));
        testArr.add(new Line2D.Double(2048, -3872, 2048, -3840));
        testArr.add(new Line2D.Double(2048, -3904, 2048, -3872));
        testArr.add(new Line2D.Double(2176, -3840, 2176, -3872));
        testArr.add(new Line2D.Double(2176, -3872, 2176, -3904));
        testArr.add(new Line2D.Double(2240, -4096, 2112, -4032));
        testArr.add(new Line2D.Double(2176, -3680, 2048, -3680));
        testArr.add(new Line2D.Double(2048, -3776, 2176, -3776));
        testArr.add(new Line2D.Double(2048, -3808, 2176, -3808));
        testArr.add(new Line2D.Double(2048, -3840, 2176, -3840));
        testArr.add(new Line2D.Double(2048, -3872, 2176, -3872));
        testArr.add(new Line2D.Double(2048, -3904, 2176, -3904));
        testArr.add(new Line2D.Double(2240, -4096, 2240, -3968));
        testArr.add(new Line2D.Double(2368, -3968, 2368, -4096));
        testArr.add(new Line2D.Double(2880, -3776, 2880, -3904));
        testArr.add(new Line2D.Double(2848, -3776, 2848, -3904));
        testArr.add(new Line2D.Double(2816, -3776, 2816, -3904));
        testArr.add(new Line2D.Double(2784, -3776, 2784, -3904));
        testArr.add(new Line2D.Double(2752, -3776, 2752, -3904));
        testArr.add(new Line2D.Double(2688, -3776, 2720, -3904));
        testArr.add(new Line2D.Double(2632, -3792, 2688, -3920));
        testArr.add(new Line2D.Double(2632, -3792, 2688, -3776));
        testArr.add(new Line2D.Double(2720, -3904, 2688, -3920));
        testArr.add(new Line2D.Double(2688, -3776, 2752, -3776));
        testArr.add(new Line2D.Double(2752, -3776, 2784, -3776));
        testArr.add(new Line2D.Double(2784, -3776, 2816, -3776));
        testArr.add(new Line2D.Double(2816, -3776, 2848, -3776));
        testArr.add(new Line2D.Double(2848, -3776, 2880, -3776));
        testArr.add(new Line2D.Double(2880, -3904, 2848, -3904));
        testArr.add(new Line2D.Double(2848, -3904, 2816, -3904));
        testArr.add(new Line2D.Double(2816, -3904, 2784, -3904));
        testArr.add(new Line2D.Double(2784, -3904, 2752, -3904));
        testArr.add(new Line2D.Double(2752, -3904, 2720, -3904));
        testArr.add(new Line2D.Double(2880, -3776, 2912, -3776));
        testArr.add(new Line2D.Double(2912, -3904, 2880, -3904));
        testArr.add(new Line2D.Double(2944, -3904, 2912, -3904));
        testArr.add(new Line2D.Double(2912, -3776, 2944, -3776));
        testArr.add(new Line2D.Double(2944, -3904, 2944, -3776));
        testArr.add(new Line2D.Double(2912, -3776, 2912, -3904));
        testArr.add(new Line2D.Double(2944, -4000, 2944, -3904));
        testArr.add(new Line2D.Double(2736, -3648, 2240, -3648));
        testArr.add(new Line2D.Double(2240, -3648, 1984, -3648));
        testArr.add(new Line2D.Double(1984, -3648, 1376, -3648));
        testArr.add(new Line2D.Double(1984, -3648, 1984, -3776));
        testArr.add(new Line2D.Double(2240, -3776, 2240, -3648));
        testArr.add(new Line2D.Double(2688, -3920, 2672, -3920));
        testArr.add(new Line2D.Double(2672, -3920, 2368, -4096));
        testArr.add(new Line2D.Double(2368, -3968, 2616, -3792));
        testArr.add(new Line2D.Double(2616, -3792, 2632, -3792));
        testArr.add(new Line2D.Double(2176, -3904, 2176, -3920));
        testArr.add(new Line2D.Double(2176, -3920, 2240, -3968));
        testArr.add(new Line2D.Double(2112, -4032, 2048, -3920));
        testArr.add(new Line2D.Double(2048, -3920, 2048, -3904));
        testArr.add(new Line2D.Double(2752, -3048, 2976, -3072));
        testArr.add(new Line2D.Double(2880, -2880, 2752, -2800));
        testArr.add(new Line2D.Double(2752, -2800, 2752, -2784));
        testArr.add(new Line2D.Double(2944, -2656, 2960, -2656));
        testArr.add(new Line2D.Double(2960, -2656, 3048, -2880));
        testArr.add(new Line2D.Double(3400, -3152, 3472, -3432));
        testArr.add(new Line2D.Double(3472, -3432, 3448, -3520));
        testArr.add(new Line2D.Double(3448, -3520, 3352, -3568));
        testArr.add(new Line2D.Double(2240, -2920, 2272, -3008));
        testArr.add(new Line2D.Double(2272, -3008, 2432, -3112));
        testArr.add(new Line2D.Double(2432, -3112, 2736, -3112));
        testArr.add(new Line2D.Double(2736, -3112, 2752, -3112));
        testArr.add(new Line2D.Double(2752, -3360, 2752, -3112));
        testArr.add(new Line2D.Double(2736, -3112, 2736, -3360));
        testArr.add(new Line2D.Double(2752, -3112, 2752, -3048));
        testArr.add(new Line2D.Double(3200, -4128, 3328, -4128));
        testArr.add(new Line2D.Double(2688, -4128, 2816, -4128));
        testArr.add(new Line2D.Double(2816, -4128, 2856, -4160));
        testArr.add(new Line2D.Double(2912, -4160, 2912, -4128));
        testArr.add(new Line2D.Double(3104, -4128, 3104, -4160));
        testArr.add(new Line2D.Double(3160, -4160, 3200, -4128));
        testArr.add(new Line2D.Double(3104, -4352, 3104, -4384));
        testArr.add(new Line2D.Double(3104, -4384, 3160, -4384));
        testArr.add(new Line2D.Double(3160, -4384, 3160, -4352));
        testArr.add(new Line2D.Double(2856, -4352, 2856, -4384));
        testArr.add(new Line2D.Double(2856, -4384, 2912, -4384));
        testArr.add(new Line2D.Double(2912, -4384, 2912, -4352));
        testArr.add(new Line2D.Double(2856, -4160, 2888, -4160));
        testArr.add(new Line2D.Double(2888, -4160, 2912, -4160));
        testArr.add(new Line2D.Double(3160, -4352, 3128, -4352));
        testArr.add(new Line2D.Double(3128, -4352, 3104, -4352));
        testArr.add(new Line2D.Double(3104, -4160, 3128, -4160));
        testArr.add(new Line2D.Double(3128, -4160, 3160, -4160));
        testArr.add(new Line2D.Double(2912, -4352, 2888, -4352));
        testArr.add(new Line2D.Double(2888, -4352, 2856, -4352));
        testArr.add(new Line2D.Double(2888, -4352, 2888, -4320));
        testArr.add(new Line2D.Double(2888, -4320, 2888, -4192));
        testArr.add(new Line2D.Double(2888, -4192, 2888, -4160));
        testArr.add(new Line2D.Double(3128, -4320, 3128, -4352));
        testArr.add(new Line2D.Double(3128, -4160, 3128, -4192));
        testArr.add(new Line2D.Double(3128, -4192, 3128, -4320));
        testArr.add(new Line2D.Double(3328, -4544, 3072, -4544));
        testArr.add(new Line2D.Double(2944, -4544, 2688, -4544));
        testArr.add(new Line2D.Double(3072, -4544, 3072, -4608));
        testArr.add(new Line2D.Double(2944, -4608, 2944, -4544));
        testArr.add(new Line2D.Double(2912, -4160, 3104, -4160));
        testArr.add(new Line2D.Double(3104, -4352, 2912, -4352));
        testArr.add(new Line2D.Double(3072, -4000, 2944, -4000));
        testArr.add(new Line2D.Double(2688, -4544, 2688, -4128));
        testArr.add(new Line2D.Double(3328, -4128, 3328, -4544));
        testArr.add(new Line2D.Double(2856, -4352, 2856, -4160));
        testArr.add(new Line2D.Double(3160, -4160, 3160, -4352));
        testArr.add(new Line2D.Double(2944, -4544, 3072, -4544));
        testArr.add(new Line2D.Double(3072, -4608, 3040, -4608));
        testArr.add(new Line2D.Double(2976, -4608, 2944, -4608));
        testArr.add(new Line2D.Double(2976, -4632, 2976, -4608));
        testArr.add(new Line2D.Double(3040, -4608, 3040, -4632));
        testArr.add(new Line2D.Double(3040, -4632, 3040, -4648));
        testArr.add(new Line2D.Double(3040, -4648, 3040, -4672));
        testArr.add(new Line2D.Double(2976, -4672, 2976, -4648));
        testArr.add(new Line2D.Double(2976, -4648, 2976, -4632));
        testArr.add(new Line2D.Double(2976, -4648, 3040, -4648));
        testArr.add(new Line2D.Double(3040, -4632, 2976, -4632));
        testArr.add(new Line2D.Double(3040, -4672, 3104, -4672));
        testArr.add(new Line2D.Double(2912, -4672, 2976, -4672));
        testArr.add(new Line2D.Double(3104, -4672, 3104, -4864));
        testArr.add(new Line2D.Double(2912, -4864, 2912, -4800));
        testArr.add(new Line2D.Double(2912, -4800, 2912, -4736));
        testArr.add(new Line2D.Double(2912, -4736, 2912, -4672));
        testArr.add(new Line2D.Double(3104, -4864, 2912, -4864));
        testArr.add(new Line2D.Double(2976, -4672, 3040, -4672));
        testArr.add(new Line2D.Double(2944, -4016, 2944, -4000));
        testArr.add(new Line2D.Double(3072, -4000, 3072, -4016));
        testArr.add(new Line2D.Double(2912, -4128, 2944, -4032));
        testArr.add(new Line2D.Double(2944, -4032, 2944, -4016));
        testArr.add(new Line2D.Double(3072, -4016, 3072, -4032));
        testArr.add(new Line2D.Double(3072, -4032, 3104, -4128));
        testArr.add(new Line2D.Double(3072, -4016, 2944, -4016));
        testArr.add(new Line2D.Double(2944, -4032, 3072, -4032));
        testArr.add(new Line2D.Double(3024, -4592, 2992, -4592));
        testArr.add(new Line2D.Double(2992, -4600, 3024, -4600));
        testArr.add(new Line2D.Double(3024, -4600, 3024, -4592));
        testArr.add(new Line2D.Double(2992, -4592, 2992, -4600));
        testArr.add(new Line2D.Double(3040, -4608, 2976, -4608));
        testArr.add(new Line2D.Double(3024, -4840, 2992, -4840));
        testArr.add(new Line2D.Double(2992, -4848, 3024, -4848));
        testArr.add(new Line2D.Double(3024, -4848, 3024, -4840));
        testArr.add(new Line2D.Double(2992, -4840, 2992, -4848));
        testArr.add(new Line2D.Double(2752, -2912, 2880, -2912));
        testArr.add(new Line2D.Double(-240, -3264, -208, -3264));
        testArr.add(new Line2D.Double(-208, -3264, -192, -3248));
        testArr.add(new Line2D.Double(-192, -3248, -192, -3216));
        testArr.add(new Line2D.Double(-192, -3216, -208, -3200));
        testArr.add(new Line2D.Double(-208, -3200, -240, -3200));
        testArr.add(new Line2D.Double(-240, -3200, -256, -3216));
        testArr.add(new Line2D.Double(-256, -3216, -256, -3248));
        testArr.add(new Line2D.Double(-256, -3248, -240, -3264));
        testArr.add(new Line2D.Double(1664, -2368, 1600, -2368));
        testArr.add(new Line2D.Double(1600, -2368, 1600, -2112));
        testArr.add(new Line2D.Double(1600, -2624, 1664, -2624));
        testArr.add(new Line2D.Double(2560, -2112, 2560, -2496));
        testArr.add(new Line2D.Double(2560, -2496, 2496, -2496));
        testArr.add(new Line2D.Double(2176, -2752, 2176, -2816));
        testArr.add(new Line2D.Double(1600, -2048, 1664, -2048));
        testArr.add(new Line2D.Double(1664, -2048, 2496, -2048));
        testArr.add(new Line2D.Double(2496, -2048, 2560, -2048));
        testArr.add(new Line2D.Double(2560, -2048, 2560, -2112));
        testArr.add(new Line2D.Double(1600, -2112, 1600, -2048));
        testArr.add(new Line2D.Double(1664, -2368, 1664, -2112));
        testArr.add(new Line2D.Double(1664, -2112, 2496, -2112));
        testArr.add(new Line2D.Double(2496, -2112, 2496, -2496));
        testArr.add(new Line2D.Double(2176, -2816, 1664, -2816));
        testArr.add(new Line2D.Double(1664, -2816, 1600, -2816));
        testArr.add(new Line2D.Double(1600, -2816, 1600, -2752));
        testArr.add(new Line2D.Double(1600, -2752, 1600, -2624));
        testArr.add(new Line2D.Double(2176, -2752, 1664, -2752));
        testArr.add(new Line2D.Double(1664, -2752, 1664, -2624));
        testArr.add(new Line2D.Double(2496, -2688, 2496, -2752));
        testArr.add(new Line2D.Double(2624, -2784, 2520, -2688));
        testArr.add(new Line2D.Double(2496, -2560, 2520, -2560));
        testArr.add(new Line2D.Double(2520, -2688, 2496, -2688));
        testArr.add(new Line2D.Double(1664, -2560, 1664, -2432));
        testArr.add(new Line2D.Double(2496, -2560, 2496, -2688));
        testArr.add(new Line2D.Double(1984, -2304, 1984, -2240));
        testArr.add(new Line2D.Double(1984, -2240, 1792, -2240));
        testArr.add(new Line2D.Double(1792, -2240, 1792, -2304));
        testArr.add(new Line2D.Double(1792, -2304, 1984, -2304));
        testArr.add(new Line2D.Double(1664, -2624, 1664, -2600));
        testArr.add(new Line2D.Double(1664, -2600, 1664, -2560));
        testArr.add(new Line2D.Double(1664, -2432, 1664, -2392));
        testArr.add(new Line2D.Double(1664, -2392, 1664, -2368));
        testArr.add(new Line2D.Double(2496, -2496, 2496, -2520));
        testArr.add(new Line2D.Double(2496, -2520, 2496, -2560));
        testArr.add(new Line2D.Double(2496, -2752, 2200, -2752));
        testArr.add(new Line2D.Double(2200, -2752, 2176, -2752));
        testArr.add(new Line2D.Double(2112, -2592, 2336, -2592));
        testArr.add(new Line2D.Double(2336, -2592, 2336, -2272));
        testArr.add(new Line2D.Double(2336, -2272, 2112, -2272));
        testArr.add(new Line2D.Double(2112, -2272, 2112, -2304));
        testArr.add(new Line2D.Double(2112, -2304, 2144, -2304));
        testArr.add(new Line2D.Double(2144, -2304, 2176, -2304));
        testArr.add(new Line2D.Double(2176, -2304, 2208, -2304));
        testArr.add(new Line2D.Double(2208, -2304, 2304, -2304));
        testArr.add(new Line2D.Double(2304, -2304, 2304, -2560));
        testArr.add(new Line2D.Double(2304, -2560, 2208, -2560));
        testArr.add(new Line2D.Double(2208, -2560, 2176, -2560));
        testArr.add(new Line2D.Double(2176, -2560, 2144, -2560));
        testArr.add(new Line2D.Double(2144, -2560, 2112, -2560));
        testArr.add(new Line2D.Double(2112, -2560, 2112, -2592));
        testArr.add(new Line2D.Double(2144, -2304, 2144, -2560));
        testArr.add(new Line2D.Double(2176, -2304, 2176, -2560));
        testArr.add(new Line2D.Double(2208, -2304, 2208, -2560));
        testArr.add(new Line2D.Double(1984, -2624, 1984, -2560));
        testArr.add(new Line2D.Double(1984, -2560, 1792, -2560));
        testArr.add(new Line2D.Double(1792, -2560, 1792, -2624));
        testArr.add(new Line2D.Double(1792, -2624, 1984, -2624));
        testArr.add(new Line2D.Double(1992, -2552, 1784, -2552));
        testArr.add(new Line2D.Double(1784, -2552, 1784, -2632));
        testArr.add(new Line2D.Double(1784, -2632, 1992, -2632));
        testArr.add(new Line2D.Double(1992, -2632, 1992, -2552));
        testArr.add(new Line2D.Double(1784, -2312, 1992, -2312));
        testArr.add(new Line2D.Double(1992, -2312, 1992, -2232));
        testArr.add(new Line2D.Double(1992, -2232, 1784, -2232));
        testArr.add(new Line2D.Double(1784, -2232, 1784, -2312));
        testArr.add(new Line2D.Double(2624, -2784, 2752, -2560));
        testArr.add(new Line2D.Double(3520, -3904, 3328, -3968));
        testArr.add(new Line2D.Double(3200, -3968, 3200, -3744));
        testArr.add(new Line2D.Double(3328, -3744, 3360, -3648));
        testArr.add(new Line2D.Double(3328, -3968, 3328, -3744));
        testArr.add(new Line2D.Double(3448, -3520, 3472, -3520));
        testArr.add(new Line2D.Double(3472, -3520, 3520, -3584));
        testArr.add(new Line2D.Double(3360, -3648, 3352, -3592));
        testArr.add(new Line2D.Double(3352, -3592, 3352, -3568));
        testArr.add(new Line2D.Double(3328, -3968, 3304, -3968));
        testArr.add(new Line2D.Double(3304, -3968, 3200, -3968));
        testArr.add(new Line2D.Double(3200, -3744, 3304, -3744));
        testArr.add(new Line2D.Double(3304, -3744, 3328, -3744));
        testArr.add(new Line2D.Double(2368, -4096, 2344, -4096));
        testArr.add(new Line2D.Double(2344, -4096, 2264, -4096));
        testArr.add(new Line2D.Double(2264, -4096, 2240, -4096));
        testArr.add(new Line2D.Double(2240, -3968, 2264, -3968));
        testArr.add(new Line2D.Double(2264, -3968, 2344, -3968));
        testArr.add(new Line2D.Double(2344, -3968, 2368, -3968));
        testArr.add(new Line2D.Double(2176, -3680, 2176, -3704));
        testArr.add(new Line2D.Double(2176, -3704, 2176, -3776));
        testArr.add(new Line2D.Double(2048, -3776, 2048, -3704));
        testArr.add(new Line2D.Double(2048, -3704, 2048, -3680));
        testArr.add(new Line2D.Double(3520, -3584, 3520, -3840));
        testArr.add(new Line2D.Double(3680, -3904, 3584, -3904));
        testArr.add(new Line2D.Double(3744, -3808, 3680, -3904));
        testArr.add(new Line2D.Double(3584, -3840, 3616, -3776));
        testArr.add(new Line2D.Double(3616, -3776, 3552, -3552));
        testArr.add(new Line2D.Double(3552, -3552, 3552, -3392));
        testArr.add(new Line2D.Double(3552, -3392, 3648, -3264));
        testArr.add(new Line2D.Double(3680, -3552, 3744, -3808));
        testArr.add(new Line2D.Double(3680, -3392, 3680, -3552));
        testArr.add(new Line2D.Double(3808, -3264, 3680, -3392));
        testArr.add(new Line2D.Double(3648, -3264, 3496, -3032));
        testArr.add(new Line2D.Double(3584, -2880, 3808, -3264));
        testArr.add(new Line2D.Double(3496, -3032, 3456, -3032));
        testArr.add(new Line2D.Double(3360, -2880, 3584, -2880));
        testArr.add(new Line2D.Double(3304, -3040, 3400, -3152));
        testArr.add(new Line2D.Double(3520, -3840, 3520, -3904));
        testArr.add(new Line2D.Double(3584, -3840, 3584, -3904));
        testArr.add(new Line2D.Double(3304, -3040, 3320, -3040));
        testArr.add(new Line2D.Double(3320, -3040, 3360, -2880));
        testArr.add(new Line2D.Double(3456, -3032, 3416, -3152));
        testArr.add(new Line2D.Double(3416, -3152, 3400, -3152));
        testArr.add(new Line2D.Double(3520, -3840, 3536, -3840));
        testArr.add(new Line2D.Double(3536, -3840, 3584, -3840));
        testArr.add(new Line2D.Double(3584, -3904, 3536, -3904));
        testArr.add(new Line2D.Double(3536, -3904, 3520, -3904));

        // testArr.add(new Line2D.Double(0, 0, 12000, 20000));
        // testArr.add(new Line2D.Double(0, 0, 30000, 30000));
        // testArr.add(new Line2D.Double(-25000, 25000, -15000, 12000));
        // testArr.add(new Line2D.Double(MIN_X, MAX_Y, MAX_X, MIN_Y));
        // testArr.add(new Line2D.Double(-28000, -28000, 28000, 28000));
        // testArr.add(new Line2D.Double(0, 0, 28000, -15000));

        // testArr.add(new Line2D.Double(-31000, 31000, -31000, -31000));

        // testArr.add(new Line2D.Double(8000, 8000, 8000, -8000));
        // testArr.add(new Line2D.Double(-8000, -8000, 8000, -8000));
        // testArr.add(new Line2D.Double(-8000, -8000, -8000, 8000));
        // testArr.add(new Line2D.Double(-8000, 8000, 8000, 8000));

        // detUsedMap(testArr);

        // setMaxX(400.);

        System.out.println("maxX: " + this.maxX);
        System.out.println("maxY: " + this.maxY);

        System.out.println("minX: " + this.minX);
        System.out.println("minX: " + this.minY);

        // drawGrid(g2d);
        drawWalls(testArr, g2d);

    }
}