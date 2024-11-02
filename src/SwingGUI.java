import players.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

// TODO: Maybe add row deletion
// TODO: Make the Other Options Panel have a border radius
// TODO: Make the Players Table have a border radius
// TODO: Fix Players Table Resizing weirdness
// TODO: Add picture as the face for the Play button

@SuppressWarnings("StringTemplateMigration")
public class SwingGUI {

    JFrame frame;
    Timer resizeTimer;

    static JPanel mainPanel = new JPanel(new CardLayout());

    // Main Window
    JPanelWithBg panel;

    JPanel options;
    JLabel oHeading;
    JLabel oNumOfPlayersL;
    JComboBox<String> oNumOfPlayersC;
    JLabel oReportResultsL;
    JComboBox<String> oReportResultsC;
    JLabel oPauseBetweenGamesL;
    JComboBox<String> oPauseBetweenGamesC;
    JLabel oNumOfGamesL;
    JTextField oNumOfGamesT;
    JButton playButton;

    JLabel kotLogo;

    JScrollPane tableScrollPane;
    JTable playersTable;
    String[] columnNames = {"Player ID", "Player Type"};
    Object[][] playersData;
    DefaultTableModel tableModel;
    JTextArea editInstructionLabel;

    // Results Window
    static JPanelWithBg resPanel;

    static int windowWidth = 950;
    static int windowHeight = 600;
    final static String pathPrefix = "D:/Code_Projects/AOOD-KOTUI-2024/src/";

    public SwingGUI() {
        frame = new JFrame("King of Tokyo GUI");
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (resizeTimer != null && resizeTimer.isRunning()) {
                    resizeTimer.restart();
                } else {
                    resizeTimer = new Timer(200, e2 -> {
                        windowWidth = frame.getWidth();
                        windowHeight = frame.getHeight();
                        setSizeRelatedProperties();
                        resizeTimer.stop();
                    });
                    resizeTimer.setRepeats(false);
                    resizeTimer.start();
                }
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);
        frame.setMinimumSize(new Dimension(windowWidth, windowHeight));

        resPanel = new JPanelWithBg(pathPrefix + "assets/background.jpeg");
        panel = new JPanelWithBg(pathPrefix + "assets/background.jpeg");
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());

        options = new JPanel();
        initializeOptionsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.33;
        panel.add(options, gbc);

        kotLogo = new JLabel("KOT Logo");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.34;
        panel.add(kotLogo, gbc);

        initializePlayersTable();

        JPanel tablePanel = new JPanel(new BorderLayout());

        tableScrollPane = new JScrollPane(playersTable);
        tableScrollPane.setPreferredSize(new Dimension(windowWidth / 3, calculateTableHeight(playersTable.getRowCount()) + 3));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        editInstructionLabel = new JTextArea("Double click on player type cell to edit\nEnter the name of the desired Java file without the \".java\"");
        editInstructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        editInstructionLabel.setEditable(false);
        editInstructionLabel.setLineWrap(true);
        tablePanel.add(editInstructionLabel, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.33;
        panel.add(tablePanel, gbc);

        setSizeRelatedProperties();

        mainPanel.add("Main", panel);
        mainPanel.add("Results", resPanel);

        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Main");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Helper methods
    private void initializeOptionsPanel() {
        options.setLayout(new GridBagLayout());
        options.setBackground(new Color(0, 51, 153));
        options.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        oHeading = new JLabel("Options");
        oHeading.setForeground(Color.WHITE);
        oHeading.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        options.add(oHeading, gbc);

        oNumOfPlayersL = new JLabel("Number of players:");
        oNumOfPlayersL.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        options.add(oNumOfPlayersL, gbc);

        oNumOfPlayersC = new JComboBox<>(new String[]{"2", "3", "4", "5", "6"});
        gbc.gridx = 1;
        oNumOfPlayersC.addActionListener(new numOfPlayersComboBoxChange());
        options.add(oNumOfPlayersC, gbc);

        oReportResultsL = new JLabel("Report results?");
        oReportResultsL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        options.add(oReportResultsL, gbc);

        oReportResultsC = new JComboBox<>(new String[]{"Per Turn", "Per Game", "Overall"});
        oReportResultsC.setSelectedIndex(1);
        gbc.gridx = 1;
        options.add(oReportResultsC, gbc);

        oPauseBetweenGamesL = new JLabel("Pause between...");
        oPauseBetweenGamesL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        options.add(oPauseBetweenGamesL, gbc);

        oPauseBetweenGamesC = new JComboBox<>(new String[]{"turns", "games", "never"});
        gbc.gridx = 1;
        options.add(oPauseBetweenGamesC, gbc);

        oNumOfGamesL = new JLabel("Number of games:");
        oNumOfGamesL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        options.add(oNumOfGamesL, gbc);

        oNumOfGamesT = new JTextField(String.valueOf(1), 5);
        gbc.gridx = 1;
        options.add(oNumOfGamesT, gbc);

        playButton = new JButton("PLAY");
        playButton.setBackground(new Color(102, 255, 255));
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playButton.addActionListener(new playButtonClick());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        options.add(playButton, gbc);
    }

    private void setSizeRelatedProperties() {
        panel.setBounds(0, 0, windowWidth, windowHeight);
        kotLogo.setText("");
        kotLogo.setIcon(scaleImage("assets/kingOfTokyoLogo.png", windowWidth / 3.0, 1105.0, 757.0));
        frame.revalidate();
        frame.repaint();
    }

    private void initializePlayersTable() {
        playersData = new Object[][] {
                {"0", "PlayerNaive"},
                {"1", "PlayerAI_GeeterPriffin"}
        };

        tableModel = new DefaultTableModel(playersData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };

        playersTable = new JTable(tableModel);
        playersTable.getTableHeader().setReorderingAllowed(false);
        playersTable.setRowHeight(40);
        playersTable.setFont(new Font("Arial", Font.PLAIN, 16));
        playersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        playersTable.getTableHeader().setBackground(new Color(0, 0, 0));
        playersTable.getTableHeader().setForeground(Color.WHITE);
        playersTable.setGridColor(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < playersTable.getColumnCount(); i++) {
            playersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void reconfigureTable(int rows) {
        int currentRows = playersTable.getRowCount();

        Object[][] dataL = new Object[rows][];

        for (int i = 0; i < Math.min(rows, currentRows); i++) {
            dataL[i] = playersData[i];
        }

        if (rows > currentRows) {
            for (int i = currentRows; i < rows; i++) {
                dataL[i] = new Object[]{String.valueOf(i), "PlayerNaive"};
            }
        }

        playersData = dataL;

        tableModel = new DefaultTableModel(playersData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };

        playersTable.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < playersTable.getColumnCount(); i++) {
            playersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableScrollPane.setPreferredSize(new Dimension(windowWidth / 3, calculateTableHeight(playersTable.getRowCount()) + 3));
        frame.revalidate();
        frame.repaint();
    }

    private boolean checkFilesInPlayersFolder() {
        String playersFolderPath = pathPrefix + "/players/";

        for (int i = 0; i < playersTable.getRowCount(); i++) {
            String fileName = playersTable.getValueAt(i, 1) + ".java";
            File file = new File(playersFolderPath + fileName);

            if (fileName.equalsIgnoreCase("player.java")) {
                JOptionPane.showMessageDialog(frame, "Can not play with Player.java", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!file.exists() || file.isDirectory()) {
                System.out.println("File not found: " + fileName);
                JOptionPane.showMessageDialog(frame, "File not found: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!fileName.startsWith("Player")) {
                JOptionPane.showMessageDialog(frame, "File " + fileName + " does not follow naming convention for Player subclasses.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

             try {
                 Class playerClass = Class.forName("players." + playersTable.getValueAt(i, 1));

                 if (!Player.class.isAssignableFrom(playerClass)) {
                     JOptionPane.showMessageDialog(frame, fileName + " is not of type Player", "Error", JOptionPane.ERROR_MESSAGE);
                 }
             } catch (ClassNotFoundException e) {
                 JOptionPane.showMessageDialog(frame, "Error loading " + fileName + " class.", "Error", JOptionPane.ERROR_MESSAGE);
             }
        }

        return true;
    }

    // Utility methods
    private int calculateTableHeight(int numberOfRows) {
        final int rowHeight = 40;
        final int headerHeight = playersTable.getTableHeader().getPreferredSize().height;
        return headerHeight + (rowHeight * numberOfRows);
    }

    private ImageIcon scaleImage(String relPath, double desiredWidth, double imgNativeWidth, double imgNativeHeight) {
        return  new ImageIcon(new ImageIcon(pathPrefix + relPath).getImage().getScaledInstance((int) desiredWidth, (int) (desiredWidth * (imgNativeHeight / imgNativeWidth)), Image.SCALE_SMOOTH));
    }

    // Event Listener classes
    class numOfPlayersComboBoxChange implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox) e.getSource();
            reconfigureTable(src.getSelectedIndex() + 2);
        }
    }

    class playButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String numOfGamesS = oNumOfGamesT.getText();
            int numOfGames;

            if (numOfGamesS.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please enter the number of games", "Incomplete field", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                numOfGames = Integer.parseInt(numOfGamesS);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number of games. Please enter a positive integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (numOfGames <= 0) {
                JOptionPane.showMessageDialog(frame, "Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (numOfGames >= 100000) {
                int userResponse = JOptionPane.showConfirmDialog(frame, "That's a lot of games. Are you sure you want many?");
                if (userResponse == JOptionPane.NO_OPTION || userResponse == JOptionPane.CLOSED_OPTION || userResponse == JOptionPane.CANCEL_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Operation cancelled; no games were started.", "Operation Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            boolean res = checkFilesInPlayersFolder();

            if (res) {
                int numOfPlayers = oNumOfPlayersC.getSelectedIndex() + 2;
                int reportResults = oReportResultsC.getSelectedIndex();
                int pausing = oPauseBetweenGamesC.getSelectedIndex();

                Results r = new Results();

                GameEngine g = new GameEngine(numOfPlayers, numOfGames, reportResults, pausing, r);
            }
        }
    }

    // Results Window
    static class Results {
        private JTextArea resultsText;
        String logged = "";

        public Results() {
            resPanel.setLayout(new GridBagLayout());

            JPanel titleWrapper = new JPanel();
            titleWrapper.setOpaque(false);
            titleWrapper.setLayout(new BorderLayout());
            titleWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            JLabel titleLabel = new JLabel("King of Tokyo", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
            titleLabel.setForeground(Color.WHITE);
            titleWrapper.add(titleLabel, BorderLayout.CENTER);

            JPanel centerWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            centerWrapperPanel.setOpaque(false);

            JPanel resultsPanel = new JPanel();
            resultsPanel.setBackground(new Color(40, 100, 200));
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setPreferredSize(new Dimension(400, 400));

            JLabel resultsLabel = new JLabel("Results & Reporting");
            resultsLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            resultsLabel.setForeground(Color.WHITE);
            resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(resultsLabel);

            resultsText = new JTextArea();
            resultsText.setText("Waiting for output from game...");

            resultsText.setFont(new Font("SansSerif", Font.PLAIN, 14));
            resultsText.setForeground(Color.WHITE);
            resultsText.setBackground(new Color(40, 100, 200)); // Matching background
            resultsText.setEditable(false);

            resultsText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(resultsText);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(scrollPane);

            centerWrapperPanel.add(resultsPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            resPanel.add(titleWrapper, gbc);

            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weighty = 1;
            resPanel.add(centerWrapperPanel, gbc);

            CardLayout cl = (CardLayout) (mainPanel.getLayout());
            cl.show(mainPanel, "Results");
        }

        public void log(String s) {
            logged += s + "\n";
            resultsText.setText(logged);
            resultsText.setCaretPosition(resultsText.getDocument().getLength());
        }
    }
}