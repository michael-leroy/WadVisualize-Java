
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WadMap {

    private String mapName = null;

    private static final Pattern mapRegex = Pattern.compile("^E[1-4]M[0-9]$|^MAP\\d\\d$");

    private static final HashSet<String> validMapLumps = new HashSet<>(Arrays.asList("THINGS",
            "LINEDEFS", "SIDEDEFS", "VERTEXES", "SEGS", "SSECTORS", "NODES",
            "SECTORS", "REJECT", "BLOCKMAP", "BEHAVIOR", "SCRIPTS"));

    public HashMap<String, WadLump> mapData = new HashMap<>();

    public WadMap(WadLump wadLump) {

        Matcher mapMatch = mapRegex.matcher(wadLump.getFileName());

        if (mapMatch.find()) {
            System.out.println("Adding map name to mapData...");
            this.mapData.put(wadLump.getFileName(), wadLump);
            setMapName(wadLump.getFileName());

        }
    }

    public WadMap(List<WadLump> wadLumps) {
        for (WadLump wadLump : wadLumps ) {

            Matcher mapMatch = mapRegex.matcher(wadLump.getFileName());

            if (mapMatch.find()) {
                System.out.println("Adding map name to mapData...");
                this.mapData.put(wadLump.getFileName(), wadLump);
                setMapName(wadLump.getFileName());

            }

            if (validMapLumps.contains(wadLump.getFileName())) {
                this.mapData.put(wadLump.getFileName(), wadLump);
            }
        }
    }

    public static HashSet<String> getValidMapLumps() {
        return validMapLumps;
    }

    public static boolean isValidMapLump(String stringToCheck) {
        if (validMapLumps.contains(stringToCheck)) {
            return true;
        }
        return false;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
