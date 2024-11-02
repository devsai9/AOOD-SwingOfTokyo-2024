import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

// TODO: Maybe add row deletion
// TODO: Make the Other Options Panel have a border radius
// TODO: Make the Players Table have a border radius

public class KOT_Swing {

    JFrame frame;
    JPanelWithBg panel;

    JPanel otherOptions;
    JLabel ooHeading;
    JLabel ooNumOfPlayersL;
    JComboBox<String> ooNumOfPlayersC;
    JLabel ooReportResultsL;
    JComboBox<String> ooReportResultsC;
    JLabel ooPauseBetweenGamesL;
    JComboBox<String> ooPauseBetweenGamesC;
    JLabel ooNumOfGamesL;
    JTextField ooNumOfGamesT;
    int numOfGames = 1;
    JButton playButton;

    JLabel kotLogo;

    JScrollPane tableScrollPane;
    JTable playersTable;
    String[] columnNames = {"Player ID", "Player Type"};
    Object[][] playersData;
    DefaultTableModel tableModel;
    JLabel editInstructionLabel;

    static int windowWidth = 950;
    static int windowHeight = 600;
    final static String pathPrefix = "D:/Code_Projects/AOOD-KOTUI-2024/src/";

    public KOT_Swing() {
        frame = new JFrame("King of Tokyo GUI");
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                windowWidth = frame.getWidth();
                windowHeight = frame.getHeight();
                setSizeRelatedProperties();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);
        frame.setMinimumSize(new Dimension(windowWidth, windowHeight));

        panel = new JPanelWithBg(pathPrefix + "assets/background.jpeg");
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());

        otherOptions = new JPanel();
        initializeOtherOptions();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.33;
        panel.add(otherOptions, gbc);

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

        editInstructionLabel = new JLabel("Double click on player type cell to edit");
        editInstructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        editInstructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tablePanel.add(editInstructionLabel, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 0.33;
        panel.add(tablePanel, gbc);

        setSizeRelatedProperties();
        frame.add(panel);
        frame.setVisible(true);
    }

    // Helper methods
    private void initializePlayersTable() {
        playersData = new Object[][] {
                {"0", "Player_Naive.java"},
                {"1", "Player_AI.java"}
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
                dataL[i] = new Object[]{String.valueOf(i), "Player_Naive.java"};
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

    private void setSizeRelatedProperties() {
        panel.setBounds(0, 0, windowWidth, windowHeight);
        kotLogo.setText("");
        kotLogo.setIcon(scaleImage("assets/kingOfTokyoLogo.png", windowWidth / 3.0, 1105.0, 757.0));
        frame.revalidate();
        frame.repaint();
    }

    private void initializeOtherOptions() {
        otherOptions.setLayout(new GridBagLayout());
        otherOptions.setBackground(new Color(0, 51, 153));
        otherOptions.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ooHeading = new JLabel("Other Options");
        ooHeading.setForeground(Color.WHITE);
        ooHeading.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        otherOptions.add(ooHeading, gbc);

        ooNumOfPlayersL = new JLabel("Number of players:");
        ooNumOfPlayersL.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        otherOptions.add(ooNumOfPlayersL, gbc);

        ooNumOfPlayersC = new JComboBox<>(new String[]{"2", "3", "4", "5", "6"});
        gbc.gridx = 1;
        ooNumOfPlayersC.addActionListener(new numOfPlayersComboBoxChange());
        // ooNumOfPlayersC.setSelectedIndex(4);
        otherOptions.add(ooNumOfPlayersC, gbc);

        ooReportResultsL = new JLabel("Report results?");
        ooReportResultsL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        otherOptions.add(ooReportResultsL, gbc);

        ooReportResultsC = new JComboBox<>(new String[]{"Per game", "Overall"});
        gbc.gridx = 1;
        otherOptions.add(ooReportResultsC, gbc);

        ooPauseBetweenGamesL = new JLabel("Pause between games?");
        ooPauseBetweenGamesL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        otherOptions.add(ooPauseBetweenGamesL, gbc);

        ooPauseBetweenGamesC = new JComboBox<>(new String[]{"Yes", "No"});
        gbc.gridx = 1;
        otherOptions.add(ooPauseBetweenGamesC, gbc);

        ooNumOfGamesL = new JLabel("Number of games:");
        ooNumOfGamesL.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        otherOptions.add(ooNumOfGamesL, gbc);

        ooNumOfGamesT = new JTextField("" + numOfGames, 5);
        gbc.gridx = 1;
        otherOptions.add(ooNumOfGamesT, gbc);

        playButton = new JButton("PLAY");
        playButton.setBackground(new Color(102, 255, 255));
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        otherOptions.add(playButton, gbc);
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

    class numOfPlayersComboBoxChange implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox src = (JComboBox) e.getSource();
            reconfigureTable(src.getSelectedIndex() + 2);
        }
    }
}