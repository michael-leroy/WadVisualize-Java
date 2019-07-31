/**
 * Stores a WAD lump in a Java object instead of being a collection of bytes in a flat file.
 */
public class WadLump {

    private int fileSizeBytes;

    private String fileName;

    // Stored as x86 little endian bytes.
    private byte[] fileContents = new byte[fileSizeBytes];

    /**
     * @param fileContents An array of x86 little endian bytes.
     * @param fileSizeBytes How large the lump is. This data is in the WAD file directory entry.
     * @param fileName Ex. LINEDEFS.
     */
    public WadLump(byte[] fileContents, int fileSizeBytes, String fileName){

        setFileContents(fileContents);
        setFileSizeBytes(fileSizeBytes);
        setFileName(fileName);

    }

    /**
     * @return Lump size in bytes.
     */
    public int getFileSizeBytes() {
        return fileSizeBytes;
    }

    /**
     * @param fileSizeBytes Set after finding the file from the WAD directory.
     */
    public void setFileSizeBytes(int fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    /**
     * @return Lump file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName The file name from the WAD directory. Usually max 8 char but not enforced here.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return Raw bytes from WAD Lump.
     */
    public byte[] getFileContents() {
        return fileContents;
    }

    /**
     * @param fileContents Raw bytes from WAS Lump.
     */
    public void setFileContents(byte[] fileContents) {
        this.fileContents = fileContents;
    }
}
