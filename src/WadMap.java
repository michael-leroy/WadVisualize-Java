
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Stores several WAD 'lumps' as a Java object.
 */
public class WadMap {

    private String mapName = null;

    // Filter to ensure the lump name is a Doom1/2 MAP.
    private static final Pattern mapRegex = Pattern.compile("^E[1-4]M[0-9]$|^MAP\\d\\d$");

    // Set of valid names a wad map lump can have.
    private static final HashSet<String> validMapLumps = new HashSet<>(Arrays.asList("THINGS",
            "LINEDEFS", "SIDEDEFS", "VERTEXES", "SEGS", "SSECTORS", "NODES",
            "SECTORS", "REJECT", "BLOCKMAP", "BEHAVIOR", "SCRIPTS"));

    // Used to store the lumps that describe a WAD map.
    public HashMap<String, WadLump> mapData = new HashMap<>();

    /**
     * Use this constructor when you don't yet have all the lumps needed to
     * build a complete WadMap. This constructor expects a WAD map filename.
     * EX. E1M1 or MAP20.
     * @param wadLump WAD lump that contains a valid map filename.
     */
    public WadMap(final WadLump wadLump) {

        // Ensure lump file name matches a WAD level name.
        Matcher mapMatch = mapRegex.matcher(wadLump.getFileName());

        if (mapMatch.find()) {
            this.mapData.put(wadLump.getFileName(), wadLump);
            setMapName(wadLump.getFileName());

        }
    }

    /**
     * Use this constructor when you already have all the Correct map lumps in a List.
     * @param wadLumps A list of *all* wad lumps to describe a map.
     */
    public WadMap(List<WadLump> wadLumps) {
        for (WadLump wadLump : wadLumps ) {

            // Check if lump name is the map file name.
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

    /**
     * @return String of valid filenames for map lumps.
     */
    public static String getValidMapLumps() {
        return validMapLumps.toString();
    }

    /**
     * @param stringToCheck A WAD Lump file name. Taken from WAD directory.
     * @return True if the lump is a valid map LUMP file name.
     */
    public static boolean isValidMapLump(final String stringToCheck) {
        if (validMapLumps.contains(stringToCheck)) {
            return true;
        }
        return false;
    }

    /**
     * @return WAD map Lump name.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * @param mapName Sets map name from WAS directory.
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
