package de.schnittie.frontend;

import de.schnittie.businesscode.Board;
import de.schnittie.businesscode.BoardImageGenerator;
import de.schnittie.businesscode.MapGenerationException;
import de.schnittie.database.DBTileLoader;
import de.schnittie.database.RuleCreationGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class FrontendGUI extends JFrame implements ActionListener {

    public static final String FILES_FOLDER = "C:\\Users\\laure\\Documents\\Dev\\LegoBattlesMapGenerator\\";
    // maybe consider something like this instead:
// String path = FrontendGUI.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//    public static final String FILES_FOLDER = "/home/artur/git/LegoBattlesMapGenerator/";

    private final JButton generateButton;
    private final JButton addRulesButton;
    private final JButton addTilesButton;
    private MapPanel mapPanel;
    private InfoPanel infoPanel;
    private int height = 10;
    private int width = 10;


    public FrontendGUI() {
        super("FrontendGUI");
        // reconsider, since the DB-Connection is never closed this way
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mapPanel = new MapPanel();
        infoPanel = new InfoPanel();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        addRulesButton = new JButton("add Rules");
        addRulesButton.addActionListener(this);
        addTilesButton = new JButton("add Tiles");
        addTilesButton.addActionListener(this);

        buttonPanel.add(generateButton);
        buttonPanel.add(addRulesButton);
        buttonPanel.add(addTilesButton);


        mainPanel.add(mapPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        // consider setting a different L&F
        // https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html#themes
        FrontendGUI gui = new FrontendGUI();
    }


    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateButton) {
            mapPanel.reset();
            try {
                Board b = new Board(width, height);
                infoPanel.infoForGeneration(b);
                b.fill();
                /**
                 * regarding the path, I'd suggest using the following lib:
                 * <dependency>
                 *      <groupId>net.harawata</groupId>
                 *      <artifactId>appdirs</artifactId>
                 *      <version>1.2.1</version>
                 * </dependency>
                 *
                 * see here: https://github.com/harawata/appdirs#supported-directories
                 */
                BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
                mapPanel.fillWithImage(FILES_FOLDER + "generatedMapRender.png");
            } catch (MapGenerationException | RuntimeException exception) {
                System.out.println("TODO: what if an exception occurs");
                // NEVER EVER hide a thrown exception. at least print the stacktrace, unless you really, REALLY don't give a shit what went wrong
                // in this case all i saw was that it didn't work...
                exception.printStackTrace();
                mapPanel.fillWithImage(FILES_FOLDER + "generatedMapRender.png");
            }
        }
        if (e.getSource() == addRulesButton){
            RuleCreationGUI gui = new RuleCreationGUI();
        }
        if (e.getSource() == addTilesButton){
            try {
                DBTileLoader.loadTiles();
            } catch (SQLException | URISyntaxException | IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

    }
}

