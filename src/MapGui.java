
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Used to display the main GUI frame.
 * All other swing objects are added to here.
 */
public class MapGui extends JFrame {

    private static final long serialVersionUID = 1L;

    public RandomAccessFile wadFile;

    MapPanel mapPanel;


    public MapGui() {

        var menuBar = new JMenuBar();

        var fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        var fileMenuOpenWad = new JMenuItem("Open");
        fileMenu.add(fileMenuOpenWad);
        fileMenuOpenWad.addActionListener((actionEvent -> openWadFile()));


        var fileMenuClose = new JMenuItem("Exit");
        fileMenuClose.addActionListener((actionEvent -> System.exit(0)));
        fileMenu.add(fileMenuClose);

        //Pin file menu to top left corner.
        GridBagConstraints menuLayout = new GridBagConstraints();
        menuLayout.anchor = GridBagConstraints.NORTHWEST;

        // Pin scroll pane to bottom right.
        GridBagConstraints scrollPaneLayout = new GridBagConstraints();
        scrollPaneLayout.anchor = GridBagConstraints.SOUTHEAST;
        scrollPaneLayout.weighty = 1;
        scrollPaneLayout.weightx = 1;

        // Gridpanel displays doom map data.
        // Perhaps I need to rename it? :-)
        this.mapPanel = new MapPanel();

        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        scrollPane.setMaximumSize(new Dimension(600, 600));

        // Center scroll bars. No idea why calling it twice works, but not once....
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum() / 2);
        scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum() / 2);

        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum() / 2);
        scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum() / 2);

        //Layout for JFrame/MapGui.
        GridBagLayout frameLayout = new GridBagLayout();
        setLayout(frameLayout);

        setMinimumSize(new Dimension(800, 650));
        setSize(1150, 700);
        add(menuBar, menuLayout);
        add(scrollPane, scrollPaneLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();

    }

    /**
     * Used to get all doom maps after the open dialog.
     * @param wadFile A WAD file.
     * @return Returns list of parsed WAD maps.
     */
    public List<WadMap> getAllWadMapsOnLoadFile(RandomAccessFile wadFile) {

        List<WadMap> allDoomMaps = WadReader.getMaps(wadFile);

        //Currently hard code to E1M1. Need to implement menu to select level.
        List<Line2D.Double> e1m1Lines = WadReader.getLINEDEFS(allDoomMaps.get(0));

        //For now always show the first map.
        displayMap(e1m1Lines);

        return allDoomMaps;

    }

    /**
     * @param doomLINEDEFS A list of WAD LINESEGS.
     */
    public void displayMap(List<Line2D.Double> doomLINEDEFS) {
        this.mapPanel.setWallsToDraw(doomLINEDEFS);
    }

    /**
     * Called after the open event is called. Loads file and checks if it contains the proper file header.
     * Any errors are displayed to the user as a popup.
     */
    public void openWadFile() {

        var fileChooser = new JFileChooser();
        var filterToWads = new FileNameExtensionFilter("Doom WAD files", "wad", "WAD");
        fileChooser.setFileFilter(filterToWads);
        int returnVal = fileChooser.showOpenDialog(MapGui.this);

        if (returnVal == 0) {
            try {
                this.wadFile = new RandomAccessFile(fileChooser.getSelectedFile().getAbsolutePath(), "r");
                // Used later to put all the maps into a list.
                List<WadMap> allWadMaps = getAllWadMapsOnLoadFile(this.wadFile);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this.rootPane, e.toString());
            }
        } else {
            JOptionPane.showMessageDialog(this.rootPane, "Error opening WAD file!");
        }

        // Now validate if the wad file has correct header.
        try {

            if (! WadReader.checkHeader(wadFile)) {
                JOptionPane.showMessageDialog(this.rootPane, "WAD File missing header!");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this.rootPane, "Error reading from WAD FILE");
        }
    }
}
