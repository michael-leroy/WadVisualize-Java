
import java.awt.geom.Line2D;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Entry point for program used currently to test MAPGUI and WADREADER.
 */
public class Main {
    public static void main(final String[] argv) {

        final String doomSharewarePath = "src/doom1.wad";

        boolean isWadHeaderValid = false;

        int numberOfLumps = 0;

        int directoryPointer = 0;

        try (RandomAccessFile doomSharewareFile = new RandomAccessFile(doomSharewarePath, "r")) {


            try {
                isWadHeaderValid = WadReader.checkHeader(doomSharewareFile);
                numberOfLumps = WadReader.getNumLumps(doomSharewareFile);
                directoryPointer = WadReader.getDirectoryPointer(doomSharewareFile);

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException f) {
            f.printStackTrace();
        }

        try (RandomAccessFile doomSharewareFile = new RandomAccessFile(doomSharewarePath, "r")) {
            WadLump testFileFromWad = WadReader.getLump(doomSharewareFile, directoryPointer);
            System.out.println(testFileFromWad.getFileName());
            System.out.println(testFileFromWad.getFileSizeBytes());
            //System.out.println(WadReader.wadLS(doomSharewareFile));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException f) {
            f.printStackTrace();
        }


        if (isWadHeaderValid) {
            System.out.println("Wad is valid!");
            System.out.println(numberOfLumps);
            System.out.println(directoryPointer);

        } else {
            System.out.println("Something went wrong. Wad not valid.");
        }

        try (RandomAccessFile doomSharewareFile = new RandomAccessFile(doomSharewarePath, "r")) {

            List<WadMap> allDoomMaps = WadReader.getMaps(doomSharewareFile);

            System.out.println(allDoomMaps.size());

            for (WadMap aWadMap : allDoomMaps) {
                System.out.print("Map name: ");
                System.out.println(aWadMap.getMapName());
            }

            List<Line2D.Double> e1m1Lines = WadReader.getLINEDEFS(allDoomMaps.get(0));

            for (Line2D.Double aLine : e1m1Lines) {
                System.out.print("Line: ");
                System.out.print(aLine.getX1());
                System.out.print(",");
                System.out.print(aLine.getY1());
                System.out.print(" ");
                System.out.print(aLine.getX2());
                System.out.print(".");
                System.out.println(aLine.getY2());
            }

        } catch (java.io.IOException e) {
            System.out.println(e);
        }

        MapGui theGui = new MapGui();

    }
}