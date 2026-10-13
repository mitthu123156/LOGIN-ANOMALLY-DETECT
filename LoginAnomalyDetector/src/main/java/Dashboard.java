import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard {

    // =========================================================
    // DATABASE SETTINGS
    // =========================================================

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/login_anomaly_detector";

    private static final String DB_USERNAME = "root";

    // CHANGE ONLY THIS PASSWORD
    private static final String DB_PASSWORD =
            "Pupu1901@sdkv";


    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(244, 247, 248);

    private static final Color CARD =
            Color.WHITE;

    private static final Color TEXT =
            new Color(31, 41, 55);

    private static final Color MUTED =
            new Color(107, 114, 128);

    private static final Color BORDER =
            new Color(226, 232, 240);

    private static final Color TEAL =
            new Color(13, 148, 136);

    private static final Color TEAL_LIGHT =
            new Color(204, 251, 241);

    private static final Color INDIGO =
            new Color(79, 70, 229);

    private static final Color INDIGO_LIGHT =
            new Color(224, 231, 255);

    private static final Color ORANGE =
            new Color(234, 138, 0);

    private static final Color ORANGE_LIGHT =
            new Color(255, 237, 213);

    private static final Color RED =
            new Color(220, 38, 38);

    private static final Color RED_LIGHT =
            new Color(254, 226, 226);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color GREEN_LIGHT =
            new Color(220, 252, 231);


    // =========================================================
    // UI COMPONENTS
    // =========================================================

    private static JFrame frame;

    private static JLabel totalLabel;
    private static JLabel suspiciousLabel;
    private static JLabel alertsLabel;

    private static JLabel lowLabel;
    private static JLabel mediumLabel;
    private static JLabel highLabel;

    private static JLabel updatedLabel;

    private static DefaultTableModel recentModel;

    private static RiskChart riskChart;
    private static ActivityChart activityChart;

    private static JPanel systemDot;

    private static Timer pulseTimer;

    private static int pulseAlpha = 255;


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            createDashboard();
        });
    }


    // =========================================================
    // CREATE DASHBOARD
    // =========================================================

    private static void createDashboard() {

        frame = new JFrame("Login Anomaly Detector");

        frame.setSize(1250, 820);
        frame.setMinimumSize(new Dimension(1050, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        // -----------------------------------------------------
        // SIDEBAR
        // -----------------------------------------------------

        JPanel sidebar = createSidebar();

        // -----------------------------------------------------
        // MAIN AREA
        // -----------------------------------------------------

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(BACKGROUND);

        JPanel topBar = createTopBar();

        JPanel content = createMainContent();

        mainArea.add(topBar, BorderLayout.NORTH);
        mainArea.add(content, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);

        frame.setContentPane(root);

        startPulseAnimation();

        refreshDashboard();

        frame.setVisible(true);
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    private static JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BorderLayout());

        // -----------------------------------------------------
        // BRAND
        // -----------------------------------------------------

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(Color.WHITE);
        brand.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 22, 25, 22
                )
        );

        JLabel logo = new JLabel("◈");

        logo.setFont(
                new Font("Arial", Font.BOLD, 34)
        );

        logo.setForeground(TEAL);

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("SECUREWATCH");

        title.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        title.setForeground(TEXT);

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Login Security");

        subtitle.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        subtitle.setForeground(MUTED);

        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(logo);
        brand.add(Box.createVerticalStrut(8));
        brand.add(title);
        brand.add(Box.createVerticalStrut(3));
        brand.add(subtitle);

        // -----------------------------------------------------
        // NAVIGATION
        // -----------------------------------------------------

        JPanel navigation = new JPanel();

        navigation.setLayout(
                new BoxLayout(
                        navigation,
                        BoxLayout.Y_AXIS
                )
        );

        navigation.setBackground(Color.WHITE);

        navigation.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        JButton dashboardButton =
                createNavButton(
                        "▦   Dashboard",
                        true
                );

        JButton analyzeButton =
                createNavButton(
                        "＋   Analyze Login",
                        false
                );

        JButton historyButton =
                createNavButton(
                        "☷   Login History",
                        false
                );

        JButton alertsButton =
                createNavButton(
                        "⚠   Security Alerts",
                        false
                );

        navigation.add(dashboardButton);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(analyzeButton);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(historyButton);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(alertsButton);

        dashboardButton.addActionListener(e ->
                refreshDashboard()
        );

        analyzeButton.addActionListener(e ->
                LoginForm.main(null)
        );

        historyButton.addActionListener(e ->
                LoginHistory.main(null)
        );

        alertsButton.addActionListener(e ->
                AlertHistory.main(null)
        );

        // -----------------------------------------------------
        // BOTTOM
        // -----------------------------------------------------

        JPanel bottom = new JPanel();

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBackground(Color.WHITE);

        bottom.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 20, 15
                )
        );

        JButton shutdownButton =
                new JButton("⏻   Safe Shutdown");

        shutdownButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        shutdownButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        shutdownButton.setFocusPainted(false);

        shutdownButton.setBackground(
                new Color(249, 250, 251)
        );

        shutdownButton.setForeground(
                new Color(75, 85, 99)
        );

        shutdownButton.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        shutdownButton.addActionListener(e -> {

            int result =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to close the application?",
                            "Safe Shutdown",
                            JOptionPane.YES_NO_OPTION
                    );

            if (result ==
                    JOptionPane.YES_OPTION) {

                System.exit(0);
            }
        });

        bottom.add(shutdownButton);

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(navigation, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }


    // =========================================================
    // NAV BUTTON
    // =========================================================

    private static JButton createNavButton(
            String text,
            boolean active
    ) {

        JButton button = new JButton(text);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        if (active) {

            button.setBackground(
                    TEAL_LIGHT
            );

            button.setForeground(
                    TEAL
            );

        } else {

            button.setBackground(
                    Color.WHITE
            );

            button.setForeground(
                    new Color(75, 85, 99)
            );
        }

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (button.getBackground()
                                .equals(Color.WHITE)) {

                            button.setBackground(
                                    new Color(
                                            248,
                                            250,
                                            252
                                    )
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (!active) {

                            button.setBackground(
                                    Color.WHITE
                            );
                        }
                    }
                }
        );

        return button;
    }


    // =========================================================
    // TOP BAR
    // =========================================================

    private static JPanel createTopBar() {

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setBackground(
                Color.WHITE
        );

        top.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                17, 25, 17, 25
                        )
                )
        );

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setBackground(
                Color.WHITE
        );

        JLabel title =
                new JLabel(
                        "Security Overview"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "Monitor login activity and identify unusual behavior"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                MUTED
        );

        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(subtitle);

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                15,
                                5
                        )
                );

        right.setBackground(
                Color.WHITE
        );

        // System active

        systemDot =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        int alpha =
                                pulseAlpha;

                        g2.setColor(
                                new Color(
                                        22,
                                        163,
                                        74,
                                        alpha
                                )
                        );

                        g2.fillOval(
                                2,
                                5,
                                10,
                                10
                        );
                    }
                };

        systemDot.setOpaque(false);

        systemDot.setPreferredSize(
                new Dimension(14, 20)
        );

        JLabel activeLabel =
                new JLabel(
                        "System Active"
                );

        activeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        activeLabel.setForeground(
                GREEN
        );

        JPanel status =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                4,
                                0
                        )
                );

        status.setBackground(
                GREEN_LIGHT
        );

        status.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 10, 8, 12
                )
        );

        status.add(systemDot);
        status.add(activeLabel);
        JButton refresh =
                new JButton("↻  Refresh");

// Prevent Windows Look & Feel from overriding the colors
        refresh.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        refresh.setFocusPainted(false);
        refresh.setOpaque(true);
        refresh.setContentAreaFilled(true);

        refresh.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        refresh.setBackground(Color.WHITE);

        refresh.setForeground(TEAL);

        refresh.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TEAL),
                        BorderFactory.createEmptyBorder(
                                9, 16, 9, 16
                        )
                )
        );

        refresh.addActionListener(
                e -> refreshDashboard()
        );

        updatedLabel =
                new JLabel(
                        "Last updated: --"
                );

        updatedLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        updatedLabel.setForeground(
                MUTED
        );

        right.add(status);
        right.add(refresh);
        right.add(updatedLabel);

        top.add(
                left,
                BorderLayout.WEST
        );

        top.add(
                right,
                BorderLayout.EAST
        );

        return top;
    }


    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private static JPanel createMainContent() {

        JPanel container =
                new JPanel(
                        new BorderLayout()
                );

        container.setBackground(
                BACKGROUND
        );

        container.setBorder(
                BorderFactory.createEmptyBorder(
                        22, 25, 22, 25
                )
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                BACKGROUND
        );

        // -----------------------------------------------------
        // KPI CARDS
        // -----------------------------------------------------

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                16,
                                0
                        )
                );

        stats.setBackground(
                BACKGROUND
        );

        stats.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        120
                )
        );

        JPanel totalCard =
                createStatCard(
                        "TOTAL LOGINS",
                        "0",
                        "All recorded login attempts",
                        "↗",
                        TEAL,
                        TEAL_LIGHT
                );

        JPanel suspiciousCard =
                createStatCard(
                        "SUSPICIOUS LOGINS",
                        "0",
                        "Medium + High risk events",
                        "!",
                        ORANGE,
                        ORANGE_LIGHT
                );

        JPanel alertsCard =
                createStatCard(
                        "SECURITY ALERTS",
                        "0",
                        "High-risk events detected",
                        "⚠",
                        RED,
                        RED_LIGHT
                );

        totalLabel =
                (JLabel) totalCard.getClientProperty(
                        "valueLabel"
                );

        suspiciousLabel =
                (JLabel) suspiciousCard.getClientProperty(
                        "valueLabel"
                );

        alertsLabel =
                (JLabel) alertsCard.getClientProperty(
                        "valueLabel"
                );

        stats.add(totalCard);
        stats.add(suspiciousCard);
        stats.add(alertsCard);

        content.add(stats);

        content.add(
                Box.createVerticalStrut(18)
        );

        // -----------------------------------------------------
        // CHART ROW
        // -----------------------------------------------------

        JPanel charts =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                16,
                                0
                        )
                );

        charts.setBackground(
                BACKGROUND
        );

        JPanel riskPanel =
                createRiskPanel();

        JPanel activityPanel =
                createActivityPanel();

        charts.add(riskPanel);
        charts.add(activityPanel);

        charts.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        255
                )
        );

        content.add(charts);

        content.add(
                Box.createVerticalStrut(18)
        );

        // -----------------------------------------------------
        // RECENT ACTIVITY
        // -----------------------------------------------------

        JPanel recentPanel =
                createRecentPanel();

        content.add(recentPanel);

        content.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // QUICK ACTIONS
        // -----------------------------------------------------

        JPanel actions =
                createActionPanel();

        content.add(actions);

        JScrollPane scroll =
                new JScrollPane(
                        content
                );

        scroll.setBorder(null);

        scroll.setBackground(
                BACKGROUND
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        container.add(
                scroll,
                BorderLayout.CENTER
        );

        return container;
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private static JPanel createStatCard(
            String title,
            String value,
            String description,
            String icon,
            Color accent,
            Color accentLight
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                CARD
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                18,
                                18
                        );

                        g2.setColor(
                                BORDER
                        );

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                18,
                                18
                        );

                        g2.setColor(
                                accent
                        );

                        g2.fillRoundRect(
                                0,
                                15,
                                5,
                                getHeight() - 30,
                                5,
                                5
                        );
                    }
                };

        card.setOpaque(false);

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        17, 20, 15, 18
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        titleLabel.setForeground(
                MUTED
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        valueLabel.setForeground(
                accent
        );

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        descriptionLabel.setForeground(
                MUTED
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        text.add(titleLabel);
        text.add(
                Box.createVerticalStrut(5)
        );
        text.add(valueLabel);
        text.add(
                Box.createVerticalStrut(2)
        );
        text.add(descriptionLabel);

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        iconLabel.setForeground(
                accent
        );

        iconLabel.setOpaque(true);

        iconLabel.setBackground(
                accentLight
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        45,
                        45
                )
        );

        JPanel iconPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        iconPanel.setOpaque(false);

        iconPanel.add(iconLabel);

        card.add(
                text,
                BorderLayout.CENTER
        );

        card.add(
                iconPanel,
                BorderLayout.EAST
        );

        card.putClientProperty(
                "valueLabel",
                valueLabel
        );

        return card;
    }


    // =========================================================
    // RISK PANEL
    // =========================================================

    private static JPanel createRiskPanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel title =
                new JLabel(
                        "Risk Distribution"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "Current login risk levels"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        subtitle.setForeground(
                MUTED
        );

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setOpaque(false);

        heading.add(title);
        heading.add(
                Box.createVerticalStrut(3)
        );
        heading.add(subtitle);

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel center =
                new JPanel(
                        new BorderLayout()
                );

        center.setOpaque(false);

        riskChart =
                new RiskChart();

        riskChart.setPreferredSize(
                new Dimension(
                        200,
                        170
                )
        );

        center.add(
                riskChart,
                BorderLayout.CENTER
        );

        JPanel legend =
                new JPanel();

        legend.setLayout(
                new BoxLayout(
                        legend,
                        BoxLayout.Y_AXIS
                )
        );

        legend.setOpaque(false);

        lowLabel =
                createLegendLabel(
                        "LOW",
                        "0",
                        GREEN
                );

        mediumLabel =
                createLegendLabel(
                        "MEDIUM",
                        "0",
                        ORANGE
                );

        highLabel =
                createLegendLabel(
                        "HIGH",
                        "0",
                        RED
                );

        legend.add(lowLabel);
        legend.add(
                Box.createVerticalStrut(12)
        );
        legend.add(mediumLabel);
        legend.add(
                Box.createVerticalStrut(12)
        );
        legend.add(highLabel);

        center.add(
                legend,
                BorderLayout.EAST
        );

        panel.add(
                center,
                BorderLayout.CENTER
        );

        return panel;
    }


    // =========================================================
    // LEGEND LABEL
    // =========================================================

    private static JLabel createLegendLabel(
            String name,
            String count,
            Color color
    ) {

        JLabel label =
                new JLabel(
                        "●  " + name + "     " + count
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                color
        );

        label.putClientProperty(
                "riskName",
                name
        );

        return label;
    }


    // =========================================================
    // ACTIVITY PANEL
    // =========================================================

    private static JPanel createActivityPanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel title =
                new JLabel(
                        "Login Activity"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "Login volume over the last 7 days"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        subtitle.setForeground(
                MUTED
        );

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setOpaque(false);

        heading.add(title);
        heading.add(
                Box.createVerticalStrut(3)
        );
        heading.add(subtitle);

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        activityChart =
                new ActivityChart();

        panel.add(
                activityChart,
                BorderLayout.CENTER
        );

        return panel;
    }


    // =========================================================
    // RECENT PANEL
    // =========================================================

    private static JPanel createRecentPanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout()
        );

        JLabel title =
                new JLabel(
                        "Recent Suspicious Activity"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "Latest medium and high-risk login events"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        subtitle.setForeground(
                MUTED
        );

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setOpaque(false);

        heading.add(title);
        heading.add(
                Box.createVerticalStrut(3)
        );
        heading.add(subtitle);

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        String[] columns = {
                "ID",
                "USERNAME",
                "IP ADDRESS",
                "DEVICE",
                "SCORE",
                "RISK",
                "TIME"
        };

        recentModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        JTable table =
                new JTable(
                        recentModel
                );

        table.setRowHeight(38);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        table.setForeground(
                TEXT
        );

        table.setBackground(
                Color.WHITE
        );

        table.setGridColor(
                new Color(
                        241,
                        245,
                        249
                )
        );

        table.setShowVerticalLines(
                false
        );

        table.setSelectionBackground(
                new Color(
                        240,
                        253,
                        250
                )
        );

        table.setSelectionForeground(
                TEXT
        );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                11
                        )
                );

        table.getTableHeader()
                .setForeground(
                        MUTED
                );

        table.getTableHeader()
                .setBackground(
                        new Color(
                                248,
                                250,
                                252
                        )
                );

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                36
                        )
                );

        // Widths

        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(125);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(150);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(65);

        table.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);

        table.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(135);

        // Risk renderer

        table.getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new RiskCellRenderer()
                );

        JScrollPane scroll =
                new JScrollPane(
                        table
                );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scroll.setPreferredSize(
                new Dimension(
                        0,
                        235
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }


    // =========================================================
    // ACTION PANEL
    // =========================================================

    private static JPanel createActionPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        JButton analyze =
                createActionButton(
                        "＋  Analyze New Login",
                        TEAL
                );

        JButton history =
                createActionButton(
                        "☷  Login History",
                        INDIGO
                );

        JButton alerts =
                createActionButton(
                        "⚠  Security Alerts",
                        RED
                );

        analyze.addActionListener(
                e -> LoginForm.main(null)
        );

        history.addActionListener(
                e -> LoginHistory.main(null)
        );

        alerts.addActionListener(
                e -> AlertHistory.main(null)
        );

        panel.add(analyze);
        panel.add(history);
        panel.add(alerts);

        return panel;
    }


    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private static JButton createActionButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text);

        button.setFocusPainted(false);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                color
        );

        button.setBackground(
                Color.WHITE
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color
                        ),
                        BorderFactory.createEmptyBorder(
                                9, 15, 9, 15
                        )
                )
        );

        return button;
    }


    // =========================================================
    // CARD PANEL
    // =========================================================

    private static JPanel createCardPanel() {

        JPanel panel =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                Color.WHITE
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                18,
                                18
                        );

                        g2.setColor(
                                BORDER
                        );

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                18,
                                18
                        );
                    }
                };

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 18, 20
                )
        );

        return panel;
    }


    // =========================================================
    // REFRESH DASHBOARD
    // =========================================================

    private static void refreshDashboard() {

        try {

            updateStatistics();

            updateRecentActivity();

            updateActivityChart();

            updatedLabel.setText(
                    "Last updated: " +
                            new SimpleDateFormat(
                                    "HH:mm:ss"
                            ).format(
                                    new Date()
                            )
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Database connection/query failed.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // STATISTICS
    // =========================================================

    private static void updateStatistics()
            throws Exception {

        String totalQuery =
                "SELECT COUNT(*) FROM Login_History";

        String suspiciousQuery =
                "SELECT COUNT(*) FROM Login_History " +
                        "WHERE risk_level IN ('MEDIUM','HIGH')";

        String alertQuery =
                "SELECT COUNT(*) FROM Alerts";

        int total = executeCount(
                totalQuery
        );

        int suspicious = executeCount(
                suspiciousQuery
        );

        int alerts = executeCount(
                alertQuery
        );

        totalLabel.setText(
                String.valueOf(total)
        );

        suspiciousLabel.setText(
                String.valueOf(suspicious)
        );

        alertsLabel.setText(
                String.valueOf(alerts)
        );


        // Risk distribution

        int low =
                executeCount(
                        "SELECT COUNT(*) FROM Login_History " +
                                "WHERE risk_level = 'LOW'"
                );

        int medium =
                executeCount(
                        "SELECT COUNT(*) FROM Login_History " +
                                "WHERE risk_level = 'MEDIUM'"
                );

        int high =
                executeCount(
                        "SELECT COUNT(*) FROM Login_History " +
                                "WHERE risk_level = 'HIGH'"
                );

        lowLabel.setText(
                "●  LOW       " + low
        );

        mediumLabel.setText(
                "●  MEDIUM    " + medium
        );

        highLabel.setText(
                "●  HIGH      " + high
        );

        riskChart.setValues(
                low,
                medium,
                high
        );
    }


    // =========================================================
    // COUNT QUERY
    // =========================================================

    private static int executeCount(
            String query
    ) throws Exception {

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USERNAME,
                                DB_PASSWORD
                        );

                Statement statement =
                        connection.createStatement();

                ResultSet result =
                        statement.executeQuery(
                                query
                        )
        ) {

            if (result.next()) {

                return result.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // RECENT ACTIVITY
    // =========================================================

    private static void updateRecentActivity()
            throws Exception {

        recentModel.setRowCount(0);

        String query =
                """
                SELECT login_id,
                       username,
                       ip_address,
                       device,
                       risk_score,
                       risk_level,
                       login_time
                FROM Login_History
                WHERE risk_level IN ('MEDIUM','HIGH')
                ORDER BY login_id DESC
                LIMIT 8
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USERNAME,
                                DB_PASSWORD
                        );

                PreparedStatement statement =
                        connection.prepareStatement(
                                query
                        );

                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                String time =
                        result.getTimestamp(
                                "login_time"
                        ) != null
                                ? new SimpleDateFormat(
                                "dd MMM, HH:mm"
                        ).format(
                                result.getTimestamp(
                                        "login_time"
                                )
                        )
                                : "-";

                recentModel.addRow(
                        new Object[]{
                                result.getInt(
                                        "login_id"
                                ),

                                result.getString(
                                        "username"
                                ),

                                result.getString(
                                        "ip_address"
                                ),

                                result.getString(
                                        "device"
                                ),

                                result.getInt(
                                        "risk_score"
                                ),

                                result.getString(
                                        "risk_level"
                                ),

                                time
                        }
                );
            }
        }
    }


    // =========================================================
    // ACTIVITY CHART DATA
    // =========================================================

    private static void updateActivityChart()
            throws Exception {

        int[] values =
                new int[7];

        String[] days =
                new String[7];

        for (int i = 0; i < 7; i++) {

            Date date =
                    new Date(
                            System.currentTimeMillis()
                                    - (6 - i)
                                    * 24L
                                    * 60L
                                    * 60L
                                    * 1000L
                    );

            days[i] =
                    new SimpleDateFormat(
                            "dd"
                    ).format(date);

            String dateString =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    ).format(date);

            String query =
                    "SELECT COUNT(*) FROM Login_History " +
                            "WHERE DATE(login_time) = ?";

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USERNAME,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    query
                            )
            ) {

                statement.setString(
                        1,
                        dateString
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {

                        values[i] =
                                result.getInt(1);
                    }
                }
            }
        }

        activityChart.setData(
                days,
                values
        );
    }


    // =========================================================
    // PULSING GREEN SYSTEM DOT
    // =========================================================

    private static void startPulseAnimation() {

        pulseTimer =
                new Timer(
                        70,
                        e -> {

                            pulseAlpha -= 12;

                            if (pulseAlpha <= 60) {
                                pulseAlpha = 255;
                            }

                            if (systemDot != null) {
                                systemDot.repaint();
                            }
                        }
                );

        pulseTimer.start();
    }


    // =========================================================
    // RISK TABLE RENDERER
    // =========================================================

    private static class RiskCellRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );

            String risk =
                    value == null
                            ? ""
                            : value.toString();

            if (risk.equals("HIGH")) {

                label.setForeground(
                        RED
                );

                label.setBackground(
                        RED_LIGHT
                );

            } else if (risk.equals("MEDIUM")) {

                label.setForeground(
                        ORANGE
                );

                label.setBackground(
                        ORANGE_LIGHT
                );

            } else {

                label.setForeground(
                        GREEN
                );

                label.setBackground(
                        GREEN_LIGHT
                );
            }

            label.setBorder(
                    BorderFactory.createEmptyBorder(
                            5, 10, 5, 10
                    )
            );

            return label;
        }
    }


    // =========================================================
    // CUSTOM RISK DONUT CHART
    // =========================================================

    private static class RiskChart
            extends JPanel {

        private int low = 0;
        private int medium = 0;
        private int high = 0;

        public RiskChart() {

            setOpaque(false);
        }

        public void setValues(
                int low,
                int medium,
                int high
        ) {

            this.low = low;
            this.medium = medium;
            this.high = high;

            repaint();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int total =
                    low + medium + high;

            int size =
                    Math.min(
                            getWidth(),
                            getHeight()
                    ) - 30;

            int x =
                    (getWidth() - size) / 2;

            int y =
                    (getHeight() - size) / 2;

            if (total == 0) {

                g2.setColor(
                        new Color(
                                226,
                                232,
                                240
                        )
                );

                g2.setStroke(
                        new BasicStroke(
                                22
                        )
                );

                g2.drawOval(
                        x,
                        y,
                        size,
                        size
                );

            } else {

                g2.setStroke(
                        new BasicStroke(
                                22,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_ROUND
                        )
                );

                int start = 90;

                int lowAngle =
                        (int) Math.round(
                                360.0
                                        * low
                                        / total
                        );

                int mediumAngle =
                        (int) Math.round(
                                360.0
                                        * medium
                                        / total
                        );

                int highAngle =
                        360
                                - lowAngle
                                - mediumAngle;

                g2.setColor(
                        GREEN
                );

                g2.drawArc(
                        x,
                        y,
                        size,
                        size,
                        start,
                        lowAngle
                );

                start += lowAngle;

                g2.setColor(
                        ORANGE
                );

                g2.drawArc(
                        x,
                        y,
                        size,
                        size,
                        start,
                        mediumAngle
                );

                start += mediumAngle;

                g2.setColor(
                        RED
                );

                g2.drawArc(
                        x,
                        y,
                        size,
                        size,
                        start,
                        highAngle
                );
            }

            // Center text

            g2.setColor(
                    TEXT
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            24
                    )
            );

            String totalText =
                    String.valueOf(total);

            FontMetrics fm =
                    g2.getFontMetrics();

            int tx =
                    getWidth() / 2
                            - fm.stringWidth(
                            totalText
                    ) / 2;

            int ty =
                    getHeight() / 2
                            + 8;

            g2.drawString(
                    totalText,
                    tx,
                    ty
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            10
                    )
            );

            String label =
                    "LOGINS";

            fm =
                    g2.getFontMetrics();

            tx =
                    getWidth() / 2
                            - fm.stringWidth(
                            label
                    ) / 2;

            g2.setColor(
                    MUTED
            );

            g2.drawString(
                    label,
                    tx,
                    ty + 17
            );
        }
    }


    // =========================================================
    // CUSTOM ACTIVITY CHART
    // =========================================================

    private static class ActivityChart
            extends JPanel {

        private String[] days =
                {
                        "-", "-", "-", "-",
                        "-", "-", "-"
                };

        private int[] values =
                {
                        0, 0, 0, 0,
                        0, 0, 0
                };

        public ActivityChart() {

            setOpaque(false);
        }

        public void setData(
                String[] days,
                int[] values
        ) {

            this.days = days;
            this.values = values;

            repaint();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int left = 35;
            int right = 15;
            int top = 20;
            int bottom = 35;

            int width =
                    getWidth()
                            - left
                            - right;

            int height =
                    getHeight()
                            - top
                            - bottom;

            int max = 1;

            for (int value : values) {

                if (value > max) {
                    max = value;
                }
            }

            // Horizontal grid

            g2.setColor(
                    new Color(
                            241,
                            245,
                            249
                    )
            );

            for (int i = 0; i < 4; i++) {

                int y =
                        top
                                + i
                                * height
                                / 3;

                g2.drawLine(
                        left,
                        y,
                        left + width,
                        y
                );
            }

            // Points

            int[] xPoints =
                    new int[7];

            int[] yPoints =
                    new int[7];

            for (int i = 0; i < 7; i++) {

                xPoints[i] =
                        left
                                + i
                                * width
                                / 6;

                yPoints[i] =
                        top
                                + height
                                - (int)
                                (
                                        (double)
                                                values[i]
                                                / max
                                                * height
                                );
            }

            // Connecting line

            g2.setColor(
                    INDIGO
            );

            g2.setStroke(
                    new BasicStroke(
                            3,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            for (int i = 0; i < 6; i++) {

                g2.drawLine(
                        xPoints[i],
                        yPoints[i],
                        xPoints[i + 1],
                        yPoints[i + 1]
                );
            }

            // Points

            for (int i = 0; i < 7; i++) {

                g2.setColor(
                        Color.WHITE
                );

                g2.fillOval(
                        xPoints[i] - 5,
                        yPoints[i] - 5,
                        10,
                        10
                );

                g2.setColor(
                        INDIGO
                );

                g2.fillOval(
                        xPoints[i] - 3,
                        yPoints[i] - 3,
                        6,
                        6
                );
            }

            // Day labels

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            10
                    )
            );

            g2.setColor(
                    MUTED
            );

            FontMetrics fm =
                    g2.getFontMetrics();

            for (int i = 0; i < 7; i++) {

                String day =
                        days[i];

                int textWidth =
                        fm.stringWidth(day);

                g2.drawString(
                        day,
                        xPoints[i]
                                - textWidth / 2,
                        getHeight() - 10
                );
            }

            // Maximum value

            g2.drawString(
                    String.valueOf(max),
                    8,
                    top + 5
            );
        }
    }
}