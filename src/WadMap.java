
import java.util.*;


public class WadMap {

    private static final HashSet<String> validMapLumps = new HashSet<>(Arrays.asList(
            "THINGS", "LINEDEFS", "SIDEDEFS", "VERTEXES", "SEGS", "SSECTORS", "NODES",
            "SECTORS", "REJECT", "BLOCKMAP", "BEHAVIOR", "SCRIPTS"));

    public HashMap<String, WadLump> mapData = new HashMap<>();

    public WadMap(List<WadLump> wadLumps) {
        for (WadLump mapLump : wadLumps ) {
            if (validMapLumps.contains(mapLump.getFileName())) {
                mapData.put(mapLump.getFileName(), mapLump);
            }
        }
    }
}
