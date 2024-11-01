import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

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
    JButton playButton;

    JLabel kotLogo;
    JTable playersTable;

    static int windowWidth = 950;
    static int windowHeight = 600;
    final static String pathPrefix = "D:/Code_Projects/AOOD-KOTUI-2024/src/";

    public KOT_Swing() {
        frame = new JFrame("King of Tokyo GUI");
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (frame.getWidth() < 950) frame.setBounds(0, 0, 950, frame.getHeight());

                windowWidth = frame.getWidth();
                windowHeight = frame.getHeight();
                setSizeRelatedProperties();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);

        try {
            panel = new JPanelWithBg(pathPrefix + "assets/background.jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());

        otherOptions = new JPanel();
        initializeOtherOptions();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        panel.add(otherOptions, gbc);

        kotLogo = new JLabel("KOT Logo");
        gbc.gridx = 1;
        gbc.weightx = 0.34;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(kotLogo, gbc);

        initializePlayersTable();

        JScrollPane scrollPane = new JScrollPane(playersTable);
        scrollPane.setPreferredSize(new Dimension(windowWidth / 3, calculateTableHeight(playersTable.getRowCount()) + 3));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.33;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(scrollPane, gbc);

        setSizeRelatedProperties();

        frame.add(panel);
        frame.setVisible(true);
    }

    private void initializePlayersTable() {
        String[] columnNames = {"Player ID", "Player Type"};

        Object[][] data = {
                {"0", "Player_Naive.java"},
                {"1", "Player_AI.java"},
                {"2", "Player_AI.java"},
                {"3", "Player_Naive.java"},
                {"4", "Player_Naive.java"},
                {"5", "Player_Naive.java"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };

        playersTable = new JTable(model);
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

    private int calculateTableHeight(int numberOfRows) {
        final int rowHeight = 40;
        final int headerHeight = playersTable.getTableHeader().getPreferredSize().height;
        return headerHeight + (rowHeight * numberOfRows);
    }

    private void setSizeRelatedProperties() {
        panel.setBounds(0, 0, windowWidth, windowHeight);
        kotLogo.setText("");
        kotLogo.setIcon(new ImageIcon(new ImageIcon(pathPrefix + "assets/kingOfTokyoLogo.png").getImage().getScaledInstance(windowWidth / 3, (int) ((windowWidth / 3.0) * (995.0 / 1543.0)), Image.SCALE_SMOOTH)));
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

        ooNumOfGamesT = new JTextField(5);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KOT_Swing::new);
    }
}