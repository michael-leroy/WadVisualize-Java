
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MapGui extends JFrame {

    /**
     *
     */

    private static final long serialVersionUID = 1L;

    // JFrame mainFrame = new JFrame();
    // JPanel gridPanel = new JPanel();






    public MapGui() {

        GridBagLayout frameLayout = new GridBagLayout();

        // JPanel mainPanel = new JPanel();

        GridBagConstraints scrollPaneLayout = new GridBagConstraints();
        scrollPaneLayout.anchor = GridBagConstraints.EAST;
        scrollPaneLayout.weighty = 1;
        scrollPaneLayout.weightx = 1;

        GridPanel gridPanel = new GridPanel();

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        scrollPane.setMaximumSize(new Dimension(600, 600));

        setLayout(frameLayout);
        setMinimumSize(new Dimension(800, 650));
        setSize(1150, 700);
        add(scrollPane, scrollPaneLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();

    }

}
