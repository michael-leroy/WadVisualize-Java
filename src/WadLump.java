
public class WadLump {

    private int fileSizeBytes;

    private String fileName;

    private byte[] fileContents = new byte[fileSizeBytes];

    public WadLump(byte[] fileContents, int fileSizeBytes, String fileName){

        setFileContents(fileContents);
        setFileSizeBytes(fileSizeBytes);
        setFileName(fileName);

    }

    public int getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(int fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public byte[] getFileContents() {
        return fileContents;
    }

    public void setFileContents(byte[] fileContents) {
        this.fileContents = fileContents;
    }
}
