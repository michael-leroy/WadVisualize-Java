import java.awt.geom.Line2D;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WadReader {

    /**
     * Starting seek position for WAD file.
     */
    public static final int FILE_START = 0;

    /**
     * Header used for WAD's in games like Doom.
     */
    public static final String IWAD_HEADER = "IWAD";


    /**
     * Header for a 'patch' WAD used for modifying the game.
     * Highly doubt I will bother with PWAD support...
     */
    public static final String PWAD_HEADER = "PWAD";

    /**
     * File offset location for numlumps which specifies the number of lumps in
     * a wad. This is zero indexed.
     */
    public static final int NUM_LUMPS_FILE_OFFSET = 4;

    /**
     * File offset location for the directory which holds
     */
    public static final int DIRECTORY_POINTER_OFFSET = 8;

    /**
     * @param wadFile WAD file to search for string
     * @param stringPointer Starting point for the string.
     * @param stringSize How much of the string to attempt to read.
     * @return String from WAD file. Usually a file name.
     * @throws java.io.IOException
     */
    public static String getStringFromWad(RandomAccessFile wadFile, int stringPointer, int stringSize) throws java.io.IOException {

        wadFile.seek(stringPointer);

        StringBuilder fileName = new StringBuilder();

        byte[] rawByteFileName = getLumpDataBytes(wadFile, stringPointer, stringSize);

        // Some file names like E1M1 have trailing zero byte chars. We will trim those off.
        for (byte aByte : rawByteFileName) {
            if (aByte > 0) { fileName.append((char)aByte); }
        }

        return fileName.toString();

    }

    /**
     * Checks to see if a WAD has a valid header. Used to see if the file is sane
     * and if the other methods will be able to read the file.
     * @param wadFile WAD file to check if header is valid.
     * @return True if proper header is found.
     * @throws java.io.IOException
     */
    public static boolean checkHeader(RandomAccessFile wadFile) throws java.io.IOException {

        String header = getStringFromWad(wadFile, FILE_START, IWAD_HEADER.length());

        return header.equals(IWAD_HEADER) || header.equals(PWAD_HEADER);
    }

    /**
     * Ensures byte array is translated properly from the bytes in the WAD file.
     * WAD data is in x86 little endian bytes.
     * @param bytesToConvert
     * @return byte array in proper format.
     */
    public static byte[] convertToLittleEndianBytes(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return bb.array();

    }

    /**
     * Ensures byte array is translated to proper java string.
     * @param bytesToConvert byte array that contains the int you wish to translate.
     * @return WAD --> Java int.
     */
    public static int convertToLittleEndianInts(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return bb.getInt();

    }

    /**
     * WAD --> Signed short. This is used often for map/wad VERTEXES.
     * @param bytesToConvert Usually 2 bytes long. Not enforced.
     * @return Java Signed short.
     */
    public static short convertToLittleEndianSignedShort(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return bb.getShort();
    }

    /**
     * WADS use unsigned shorts for things like LINESEGS data.
     * Java has no proper data structure for this so a char is used instead.
     * @param bytesToConvert Any unsigned WAD short.
     * @return WAD ushort --> Java char.
     */
    public static char convertToLittleEndianUnsignedShort(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        //int testX = ((char) bytesToConvert[0]) | (((char) bytesToConvert[1]) << 8);

        System.out.println();

        return bb.getChar();
        //return (char)((bytesToConvert[0] & 0xFF | bytesToConvert[1] & 0xFF) >> 4 );

    }

    /**
     * Returns all Maps contained in a WAD file.
     * @param wadFile A WAD file.
     * @return A list of WAD maps represented as a Java object.
     */
    public static List<WadMap> getMaps(RandomAccessFile wadFile) {

        List<WadMap> allWadMaps = new ArrayList<>();

        Pattern mapRegex = Pattern.compile("^E[1-4]M[0-9]$|^MAP\\d\\d$");

        HashSet<String> mapsDiscovered = new HashSet<>(Arrays.asList());

        int mapCounter = 0;

        List<WadLump> allWadLumps = getAllWadLumps(wadFile);

        for (WadLump wadLump : allWadLumps) {

            Matcher isMapRegex = mapRegex.matcher(wadLump.getFileName());
            boolean isMap = isMapRegex.find();
            System.out.println(isMap);

            if (isMap) {
                System.out.println(wadLump.getFileName());
                WadMap tempWadMap = new WadMap(wadLump);

                allWadMaps.add(tempWadMap);

                mapsDiscovered.add(wadLump.getFileName());
                //System.out.println(allWadMaps.get(mapCounter).mapData.get("E1M1").getFileName());

            // TODO: Find an improved way of finding the next map.
            } else if (wadLump.getFileName().equals("BLOCKMAP")) {
                System.out.println("matched BLOCKMAP");
                allWadMaps.get(mapCounter).mapData.put(wadLump.getFileName(), wadLump);
                mapCounter += 1;

            }else if (WadMap.isValidMapLump(wadLump.getFileName())) {
                System.out.println("Adding map asset...");
                System.out.println(wadLump.getFileName());
                allWadMaps.get(mapCounter).mapData.put(wadLump.getFileName(), wadLump);

            } else {
                System.out.print("Didn't match... ");
                System.out.println(wadLump.getFileName());
            }

        }

        return allWadMaps;
    }

    /**
     * Collect all VERTEXES from a lump of the same name.
     * @param vertexLUMP A VERTEXES lump with the file name 'VERTEXES'
     * @return If valid lump, list of all vertexes in lump.
     */
    public static List<DVertex> getAllVERTEX(WadLump vertexLUMP) {

        byte[] vertLumpData = vertexLUMP.getFileContents();

        List<DVertex> returnList = new ArrayList<>();

        if (! vertexLUMP.getFileName().equals("VERTEXES")) {
            return  returnList;
        }

        int lumpOffset = 0;

        while (lumpOffset < vertexLUMP.getFileSizeBytes()) {
            DVertex tempDVertex = new DVertex();

            byte[] xBucket = new byte[2];
            byte[] yBucket = new byte[2];

            // Each X and Y is 2 bytes long.

            // Getting X in vertex.
            xBucket[0] = vertLumpData[lumpOffset];
            xBucket[1] = vertLumpData[lumpOffset + 1];

            // Jumping ahead for Y coord
            lumpOffset += 2;

            yBucket[0] = vertLumpData[lumpOffset];
            yBucket[1] = vertLumpData[lumpOffset + 1];

            tempDVertex.setX(convertToLittleEndianSignedShort(xBucket));
            tempDVertex.setY(convertToLittleEndianSignedShort(yBucket));

            returnList.add(tempDVertex);

            // Jumping ahead for next loop
            lumpOffset += 2;

        }
        return returnList;
    }

    /**
     * Returns all LINEDEFS from a WADMAP.
     * @param wadMap WAD map complete with LINEDEFS and VERTEXES.
     * @return All lines used to draw a level map.
     */
    public static List<Line2D.Double> getLINEDEFS(WadMap wadMap) {

        List<DVertex> mapVertexes = getAllVERTEX(wadMap.mapData.get("VERTEXES"));

        WadLump mapLineSegsLump = wadMap.mapData.get("LINEDEFS");

        byte[] lineDefsFromLump = mapLineSegsLump.getFileContents();

        List<Line2D.Double> returnList = new ArrayList<>();

        int lumpOffset = 0;

        while (lumpOffset < mapLineSegsLump.getFileSizeBytes()) {

            byte[] startVertexRaw = new byte[2];
            char startVertex;

            byte[] endVertexRaw = new byte[2];
            char endVertex;

            startVertexRaw[0] = lineDefsFromLump[lumpOffset];
            startVertexRaw[1] = lineDefsFromLump[lumpOffset + 1];
            startVertex = convertToLittleEndianUnsignedShort(startVertexRaw);

            int testSV = (char)startVertex;

            //Read next two bytes for end vertex.
            lumpOffset += 2;

            endVertexRaw[0] = lineDefsFromLump[lumpOffset];
            endVertexRaw[1] = lineDefsFromLump[lumpOffset + 1];
            endVertex = convertToLittleEndianUnsignedShort(endVertexRaw);

            //Jump head 2 more bytes for next loop
            // FIX OFFSET!
            lumpOffset += 12;

            Line2D.Double tempStartLine = new Line2D.Double(
                    (double)mapVertexes.get(startVertex).getX(),
                    (double)mapVertexes.get(startVertex).getY(),
                    (double)mapVertexes.get(endVertex).getX(),
                    (double)mapVertexes.get(endVertex).getY()
            );

            returnList.add(tempStartLine);

        }
        return returnList;
    }

    /**
     * Helper method to print out all files in WAD file like the ls command.
     * @param wadFile A wad file with proper header.
     * @return String displaying all WAD file names.
     */
    public static String wadLS(RandomAccessFile wadFile) {

        //Intended format <lump name> <lump size> <lump pointer>
        StringBuilder files = new StringBuilder();
        int directoryPointer = 0;
        int numberOfLumps = 0;

        try {
            directoryPointer = getDirectoryPointer(wadFile);
            numberOfLumps = getNumLumps(wadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Each loop increment by 16, the length of each descriptor
        // in the directory.
        int pointerOffset = 0;
        for (int i = 0; i < numberOfLumps; i++) {

            try {

                //Jumping ahead 8 bytes to get lump name first
                // Appending lump file name.
                pointerOffset += 8;

                // Read lump name.
                files.append(getStringFromWad(wadFile, directoryPointer + pointerOffset, 8));
                files.append(" ");

                // Going back 4 bytes to read the lump size.
                pointerOffset -= 4;

                // Appending lump size
                files.append(wadByteToInt(wadFile, directoryPointer + pointerOffset));
                files.append(" bytes ");

                // Going back 4 bytes to read the pointer int.
                pointerOffset -= 4;

                // Appending lump pointer.
                files.append(wadByteToInt(wadFile, directoryPointer + pointerOffset));
                files.append(" pointer\n");

                // Increment to start the next loop.
                pointerOffset += 16;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files.toString();
    }

    /**
     * Get lump from known directory pointer position.
     * @param wadFile
     * @param directoryPointer
     * @return WAD lump that contains data, file name and file size.
     */
    public static WadLump getLump(RandomAccessFile wadFile, int directoryPointer) {
        int filenameStringSize = 8;

        int fileSize = 0;

        int filePointer = 0;

        String fileName = null;

        try {

            //First 4 bytes are the file pointer
            filePointer = wadByteToInt(wadFile, directoryPointer);

            //Next 4 bytes is the pointer to the actual file data
            fileSize = wadByteToInt(wadFile, directoryPointer + 4);


            //Next 8 bytes is the file name
            fileName = getStringFromWad(wadFile, directoryPointer + 8, filenameStringSize);


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        byte[] fileData = new byte[fileSize];

        try {
            //Now get the actual file data
            fileData = getLumpDataBytes(wadFile, filePointer, fileSize);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return new WadLump(fileData, fileSize, fileName);

    }

    /**
     * Collects all WAD lumps in a file. This is sounds, textures, maps, everything.
     * @param wadFile
     * @return List of all wad lumps(files) complete with raw byte data.
     */
    public static List<WadLump> getAllWadLumps(RandomAccessFile wadFile) {

        int directoryPointer = 0;
        int numberOfLumps = 0;

        try {
            directoryPointer = getDirectoryPointer(wadFile);
            numberOfLumps = getNumLumps(wadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<WadLump> allWadLumps = new ArrayList<>();

        // Each loop increment by 16, the length of each descriptor
        // in the directory.
        int pointerOffset = 0;

        for (int i = 0; i < numberOfLumps; i++) {

            //Jumping ahead 8 bytes to get lump name first
            // Appending lump file name.
            pointerOffset += 8;

            try {

                String lumpName = getStringFromWad(wadFile, directoryPointer + pointerOffset, 8);

                // Going back 4 bytes to read the lump size.
                pointerOffset -= 4;

                int lumpSize = wadByteToInt(wadFile, directoryPointer + pointerOffset);

                // Going back 4 bytes to read the pointer int.
                pointerOffset -= 4;


                int lumpPointer = wadByteToInt(wadFile, directoryPointer + pointerOffset);

                byte[] lumpData = getLumpDataBytes(wadFile, lumpPointer, lumpSize);
                WadLump tempWadLump = new WadLump(lumpData, lumpSize, lumpName);

                allWadLumps.add(tempWadLump);

                // Jump ahead 16 bytes to capture next lump.
                pointerOffset += 16;

            } catch (java.io.IOException e) {
                System.out.println(e);
            }
        }
        return allWadLumps;
    }

    /**
     * Gets lump raw data.
     * @param wadFile
     * @param fileOffset A pointer for start point of raw data bytes.
     * @param fileSize Used to determin how many bytes to grab.
     * @return Array of x86 little endian bytes.
     * @throws java.io.IOException
     */
    public static byte[] getLumpDataBytes(RandomAccessFile wadFile, int fileOffset, int fileSize)
            throws java.io.IOException {
        wadFile.seek(fileOffset);
        byte[] assetBytes = new byte[fileSize];

        wadFile.read(assetBytes, 0, fileSize);

        return convertToLittleEndianBytes(assetBytes);
    }

    /**
     * Changes little endian bytes into java int. Used for things like getting file size from the
     * wad directory.
     * @param wadFile Valid wad file.
     * @param fileOffset Pointer for start point of int.
     * @return byte --> Java int.
     * @throws java.io.IOException
     */
    public static int wadByteToInt(RandomAccessFile wadFile, int fileOffset) throws java.io.IOException {

        // Wad int's are x86, little endian, 4 byte longs.
        int sizeOfWadByte = 4;

        byte[] numberOfLumps = new byte[sizeOfWadByte];

        wadFile.seek(fileOffset);

        wadFile.read(numberOfLumps, 0, 4);

        return convertToLittleEndianInts(numberOfLumps);
    }

    /**
     * Gets number of lumps (wad files) in wad. This can be used to process the wad file/lump directory.
     * @param wadFile Valid wad file.
     * @return A count of all the lumps in a directory.
     * @throws java.io.IOException
     */
    public static int getNumLumps(RandomAccessFile wadFile) throws java.io.IOException {

        return wadByteToInt(wadFile, NUM_LUMPS_FILE_OFFSET);
    }

    /**
     * Each directory entry has a pointer for where the file data (in bytes) is stored in the wad.
     * @param wadFile Valid WAD file.
     * @return A pointer to the start point of the lump/files data.
     * @throws java.io.IOException
     */
    public static int getDirectoryPointer(RandomAccessFile wadFile) throws java.io.IOException {
        return wadByteToInt(wadFile, DIRECTORY_POINTER_OFFSET);
    }
}

