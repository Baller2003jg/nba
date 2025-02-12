import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class NBAStatsApp extends JFrame {

    private JComboBox<String> teamComboBox;
    private JComboBox<String> playerComboBox;
    private JTextArea playerStatsArea;
    private CustomChartPanel chartPanel;

    private Map<String, String[]> teamToPlayersMap;
    private Map<String, PlayerStats> playerStatsMap;

    public NBAStatsApp() {
        setTitle("NBA Stats Predictor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize data
        initData();

        // Top Panel for team and player selection
        JPanel topPanel = new JPanel();
        teamComboBox = new JComboBox<>(teamToPlayersMap.keySet().toArray(new String[0]));
        playerComboBox = new JComboBox<>();
        playerStatsArea = new JTextArea(10, 20);
        playerStatsArea.setEditable(false);

        teamComboBox.addActionListener(e -> updatePlayerComboBox());
        playerComboBox.addActionListener(e -> updatePlayerStats());

        topPanel.add(new JLabel("Team:"));
        topPanel.add(teamComboBox);
        topPanel.add(new JLabel("Player:"));
        topPanel.add(playerComboBox);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(playerStatsArea), BorderLayout.WEST);

        
        chartPanel = new CustomChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        
        updatePlayerComboBox();
        updatePlayerStats();
    }

    private void initData() {
        
        teamToPlayersMap = new HashMap<>();
        teamToPlayersMap.put("Lakers", new String[]{"LeBron James", "Anthony Davis"});
        teamToPlayersMap.put("Mavericks", new String[]{"Kyrie Irving", "Luka Doncic"});
        teamToPlayersMap.put("Nuggets", new String[]{"Nikola Jokic", "Jamal Murray"});

        playerStatsMap = new HashMap<>();
        playerStatsMap.put("LeBron James", new PlayerStats(22.6, 8.1, 9.4));
        playerStatsMap.put("Anthony Davis", new PlayerStats(28.6, 11.5, 3.2));
        playerStatsMap.put("Kyrie Irving", new PlayerStats(24.8, 4.8, 5.6));
        playerStatsMap.put("Luka Doncic", new PlayerStats(28.6, 7.6, 8.0));
        playerStatsMap.put("Nikola Jokic", new PlayerStats(29.6, 13.2, 10.7));
        playerStatsMap.put("Jamal Murray", new PlayerStats(18.1, 4.1, 6.2));
    }

    private void updatePlayerComboBox() {
        String selectedTeam = (String) teamComboBox.getSelectedItem();
        String[] players = teamToPlayersMap.get(selectedTeam);
        playerComboBox.removeAllItems();
        for (String player : players) {
            playerComboBox.addItem(player);
        }
    }

    private void updatePlayerStats() {
        String selectedPlayer = (String) playerComboBox.getSelectedItem();
        if (selectedPlayer != null) {
            PlayerStats stats = playerStatsMap.get(selectedPlayer);
            playerStatsArea.setText(stats.toString());

            chartPanel.updateChart(stats);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NBAStatsApp().setVisible(true));
    }
}


class CustomChartPanel extends JPanel {

    private PlayerStats currentStats;

    public void updateChart(PlayerStats stats) {
        this.currentStats = stats;
        repaint(); 
// Refresh the chart
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentStats == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 25;

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        
        int numberOfYDivisions = 5;
        int numberOfXDivisions = 3;
        int maxStatValue = 30;

        // Y-Axis and grid lines
        for (int i = 0; i <= numberOfYDivisions; i++) {
            int y = height - padding - i * (height - 2 * padding) / numberOfYDivisions;
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(padding, y, width - padding, y);

            // Y-Axis labels
            g2d.setColor(Color.BLACK);
            String yLabel = String.valueOf(i * maxStatValue / numberOfYDivisions);
            g2d.drawString(yLabel, padding - labelPadding, y + 5);
        }

        // X-Axis labels
        String[] statLabels = {"Points", "Rebounds", "Assists"};
        for (int i = 0; i < statLabels.length; i++) {
            int x = padding + i * (width - 2 * padding) / numberOfXDivisions + 50;
            g2d.setColor(Color.BLACK);
            g2d.drawString(statLabels[i], x - 15, height - padding + labelPadding);
        }

        
        int[] stats = {
            (int) currentStats.getPoints(),
            (int) currentStats.getRebounds(),
            (int) currentStats.getAssists()
        };

        int pointWidth = 10;

        for (int i = 0; i < stats.length; i++) {
            int x = padding + i * (width - 2 * padding) / numberOfXDivisions + 50;
            int y = height - padding - (stats[i] * (height - 2 * padding) / maxStatValue);
            g2d.setColor(Color.BLUE);
            g2d.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);
            g2d.drawString(String.valueOf(stats[i]), x - pointWidth / 2, y - pointWidth);
        }
    }
}

// Class to store player stats
class PlayerStats {
    private double points;
    private double rebounds;
    private double assists;

    public PlayerStats(double points, double rebounds, double assists) {
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
    }

    public double getPoints() {
        return points;
    }

    public double getRebounds() {
        return rebounds;
    }

    public double getAssists() {
        return assists;
    }

    @Override
    public String toString() {
        return String.format("Points: %.1f\nRebounds: %.1f\nAssists: %.1f", points, rebounds, assists);
    }
}
