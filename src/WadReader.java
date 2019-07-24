import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WadReader {

    public static final int FILE_START = 0;

    public static final String IWAD_HEADER = "IWAD";

    // Highly doubt I will bother with PWAD support...
    public static final String PWAD_HEADER = "PWAD";

    /**
     * File offset location for numlumps which specifies the number of lumps in
     * a wad. This is zero indexed.
     *
     */
    public static final int NUM_LUMPS_FILE_OFFSET = 4;

    /**
     * File offset location for the directory which holds
     *
     */
    public static final int DIRECTORY_POINTER_OFFSET = 8;






    public static RandomAccessFile wadFile(String filePath)
            throws java.io.FileNotFoundException {

        Path wPath = Paths.get(filePath);

        return new RandomAccessFile(wPath.toFile(), "r");

    }


    public static String getStringFromWad(RandomAccessFile wadFile, int stringPointer, int stringSize) throws java.io.IOException {

        wadFile.seek(stringPointer);
        return new String(getLumpDataBytes(wadFile, stringPointer, stringSize));

    }



    public static boolean checkHeader(RandomAccessFile wadFile) throws java.io.IOException {

        String header = getStringFromWad(wadFile, FILE_START, IWAD_HEADER.length());

        return header.equals(IWAD_HEADER) || header.equals(PWAD_HEADER);
    }

    public static byte[] convertToLittleEndianBytes(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return bb.array();

    }
    public static int convertToLittleEndianInts(byte[] bytesToConvert) {
        ByteBuffer bb = ByteBuffer.wrap(bytesToConvert);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        return bb.getInt();

    }

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


            byte[] lumpName = new byte[8];

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
    public static byte[] getLumpDataBytes(RandomAccessFile wadFile, int fileOffset, int fileSize)
            throws java.io.IOException {
        wadFile.seek(fileOffset);
        byte[] assetBytes = new byte[fileSize];

        wadFile.read(assetBytes, 0, fileSize);

        return convertToLittleEndianBytes(assetBytes);
    }




    public static int wadByteToInt(RandomAccessFile wadFile, int fileOffset) throws java.io.IOException {

        // Wad int's are x86, little endian, 4 byte longs.
        int sizeOfWadByte = 4;

        byte[] numberOfLumps = new byte[sizeOfWadByte];

        wadFile.seek(fileOffset);

        wadFile.read(numberOfLumps, 0, 4);

        //byte[] lumpNumber = convertToLittleEndian(numberOfLumps);
        //ByteBuffer bb = ByteBuffer.wrap(numberOfLumps);
        //bb.order(ByteOrder.LITTLE_ENDIAN);

        //return new BigInteger(lumpNumber).intValue();
        return convertToLittleEndianInts(numberOfLumps);
    }





    public static int getNumLumps(RandomAccessFile wadFile) throws java.io.IOException {

        return wadByteToInt(wadFile, NUM_LUMPS_FILE_OFFSET);
    }




    public static int getDirectoryPointer(RandomAccessFile wadFile) throws java.io.IOException {
        return wadByteToInt(wadFile, DIRECTORY_POINTER_OFFSET);
    }
}

