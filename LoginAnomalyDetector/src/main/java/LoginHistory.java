import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LoginHistory {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/login_anomaly_detector";

    private static final String DB_USERNAME =
            "root";

    private static final String DB_PASSWORD =
            "Pupu1901@sdkv";


    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(245, 247, 250);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color TEXT =
            new Color(30, 41, 59);

    private static final Color SECONDARY =
            new Color(100, 116, 139);

    private static final Color BORDER =
            new Color(226, 232, 240);

    private static final Color TEAL =
            new Color(15, 118, 110);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color GREEN_LIGHT =
            new Color(220, 252, 231);

    private static final Color RED =
            new Color(220, 38, 38);

    private static final Color RED_LIGHT =
            new Color(254, 226, 226);

    private static final Color ORANGE =
            new Color(217, 119, 6);

    private static final Color ORANGE_LIGHT =
            new Color(254, 243, 199);


    private static JFrame frame;

    private static DefaultTableModel model;

    private static JTextField searchField;

    private static JComboBox<String> filterBox;


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

            createWindow();
        });
    }


    // =========================================================
    // CREATE WINDOW
    // =========================================================

    private static void createWindow() {

        frame =
                new JFrame(
                        "Login History"
                );

        frame.setSize(
                1200,
                720
        );

        frame.setMinimumSize(
                new Dimension(
                        950,
                        600
                )
        );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                WHITE
        );

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );


        JPanel heading =
                new JPanel();

        heading.setBackground(
                WHITE
        );

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Login History"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Complete authentication activity recorded by the system"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                SECONDARY
        );


        heading.add(title);

        heading.add(
                Box.createVerticalStrut(4)
        );

        heading.add(subtitle);


        JPanel headerButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        headerButtons.setBackground(
                WHITE
        );


        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        styleButton(
                refreshButton,
                TEAL
        );


        JButton closeButton =
                new JButton(
                        "Close"
                );

        styleButton(
                closeButton,
                TEXT
        );


        refreshButton.addActionListener(
                e -> loadData()
        );

        closeButton.addActionListener(
                e -> frame.dispose()
        );


        headerButtons.add(
                refreshButton
        );

        headerButtons.add(
                closeButton
        );


        header.add(
                heading,
                BorderLayout.WEST
        );

        header.add(
                headerButtons,
                BorderLayout.EAST
        );


        root.add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // TOOLBAR
        // =====================================================

        JPanel toolbar =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        toolbar.setBackground(
                BACKGROUND
        );

        toolbar.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        12,
                        25
                )
        );


        JPanel searchPanel =
                new JPanel(
                        new BorderLayout()
                );

        searchPanel.setBackground(
                WHITE
        );

        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );


        JLabel searchLabel =
                new JLabel(
                        "Search:"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        searchLabel.setForeground(
                TEAL
        );


        searchField =
                new JTextField();

        searchField.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );


        searchPanel.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );


        JPanel filterPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        filterPanel.setBackground(
                BACKGROUND
        );


        JLabel filterLabel =
                new JLabel(
                        "Risk:"
                );

        filterLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        filterLabel.setForeground(
                SECONDARY
        );


        filterBox =
                new JComboBox<>(
                        new String[]{
                                "ALL",
                                "LOW",
                                "MEDIUM",
                                "HIGH"
                        }
                );

        filterBox.setPreferredSize(
                new Dimension(
                        110,
                        38
                )
        );


        filterPanel.add(
                filterLabel
        );

        filterPanel.add(
                filterBox
        );


        toolbar.add(
                searchPanel,
                BorderLayout.CENTER
        );

        toolbar.add(
                filterPanel,
                BorderLayout.EAST
        );


        root.add(
                toolbar,
                BorderLayout.BEFORE_FIRST_LINE
        );


        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "ID",
                "USERNAME",
                "IP ADDRESS",
                "DEVICE",
                "FAILED",
                "HOUR",
                "RISK SCORE",
                "RISK LEVEL",
                "LOGIN TIME"
        };


        model =
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
                new JTable(model);


        table.setRowHeight(
                42
        );

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
                WHITE
        );

        table.setGridColor(
                BORDER
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


        // HEADER

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                10
                        )
                );

        table.getTableHeader()
                .setForeground(
                        SECONDARY
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
                                42
                        )
                );


        // COLUMN WIDTHS

        int[] widths = {
                45,
                100,
                125,
                140,
                70,
                65,
                95,
                95,
                150
        };


        for (int i = 0;
             i < widths.length;
             i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }


        // RISK BADGE

        table.getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new RiskRenderer()
                );


        // SCORE

        table.getColumnModel()
                .getColumn(6)
                .setCellRenderer(
                        new ScoreRenderer()
                );


        // CENTER SOME COLUMNS

        CenterRenderer center =
                new CenterRenderer();

        table.getColumnModel()
                .getColumn(0)
                .setCellRenderer(center);

        table.getColumnModel()
                .getColumn(4)
                .setCellRenderer(center);

        table.getColumnModel()
                .getColumn(5)
                .setCellRenderer(center);


        // =====================================================
        // DOUBLE CLICK DETAILS
        // =====================================================

        table.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {

                            int row =
                                    table.rowAtPoint(
                                            e.getPoint()
                                    );

                            if (row >= 0) {

                                showDetails(
                                        table,
                                        row
                                );
                            }
                        }
                    }
                }
        );


        JScrollPane scroll =
                new JScrollPane(
                        table
                );

        scroll.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(
                                0,
                                25,
                                25,
                                25
                        ),
                        BorderFactory.createLineBorder(
                                BORDER
                        )
                ));


        root.add(
                scroll,
                BorderLayout.CENTER
        );


        // =====================================================
        // SEARCH
        // =====================================================

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                loadData();
                            }

                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                loadData();
                            }

                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                loadData();
                            }
                        }
                );


        filterBox.addActionListener(
                e -> loadData()
        );


        frame.setContentPane(root);

        frame.setVisible(true);

        loadData();
    }


    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private static void styleButton(
            JButton button,
            Color color
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(
                color
        );

        button.setBackground(
                WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                15,
                                8,
                                15
                        )
                )
        );
    }


    // =========================================================
    // LOAD DATA
    // =========================================================

    private static void loadData() {

        if (model == null) {
            return;
        }


        model.setRowCount(0);


        String search =
                searchField == null
                        ? ""
                        : searchField
                        .getText()
                        .trim();


        String risk =
                filterBox == null
                        ? "ALL"
                        : String.valueOf(
                        filterBox.getSelectedItem()
                );


        StringBuilder query =
                new StringBuilder(
                        "SELECT login_id, username, "
                                + "ip_address, device, "
                                + "failed_attempts, "
                                + "login_hour, "
                                + "risk_score, "
                                + "risk_level, "
                                + "login_time "
                                + "FROM Login_History "
                                + "WHERE 1=1 "
                );


        if (!search.isEmpty()) {

            query.append(
                    "AND (username LIKE ? "
                            + "OR ip_address LIKE ? "
                            + "OR device LIKE ?) "
            );
        }


        if (!"ALL".equals(risk)) {

            query.append(
                    "AND risk_level = ? "
            );
        }


        query.append(
                "ORDER BY login_id DESC"
        );


        try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL,
                                DB_USERNAME,
                                DB_PASSWORD
                        );

                PreparedStatement statement =
                        connection.prepareStatement(
                                query.toString()
                        )
        ) {

            int index = 1;


            if (!search.isEmpty()) {

                String pattern =
                        "%" + search + "%";


                statement.setString(
                        index++,
                        pattern
                );

                statement.setString(
                        index++,
                        pattern
                );

                statement.setString(
                        index++,
                        pattern
                );
            }


            if (!"ALL".equals(risk)) {

                statement.setString(
                        index,
                        risk
                );
            }


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    Timestamp timestamp =
                            result.getTimestamp(
                                    "login_time"
                            );


                    String time =
                            timestamp == null
                                    ? "-"
                                    : timestamp.toString();


                    model.addRow(
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
                                            "failed_attempts"
                                    ),

                                    result.getInt(
                                            "login_hour"
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


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Unable to load login history.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // DETAILS WINDOW
    // =========================================================

    private static void showDetails(
            JTable table,
            int row
    ) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "Login Details",
                        true
                );

        dialog.setSize(
                500,
                560
        );

        dialog.setLocationRelativeTo(
                frame
        );


        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );


        // HEADER

        JPanel header =
                new JPanel();

        header.setBackground(
                TEAL
        );

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );


        JLabel title =
                new JLabel(
                        "LOGIN EVENT DETAILS"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(
                WHITE
        );


        JLabel id =
                new JLabel(
                        "Login ID: "
                                + table.getValueAt(
                                row,
                                0
                        )
                );

        id.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        id.setForeground(
                new Color(
                        204,
                        251,
                        241
                )
        );


        header.add(title);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(id);


        root.add(
                header,
                BorderLayout.NORTH
        );


        // CONTENT

        JPanel content =
                new JPanel();

        content.setBackground(
                WHITE
        );

        content.setLayout(
                new GridLayout(
                        9,
                        2,
                        10,
                        10
                )
        );

        content.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );


        addDetail(
                content,
                "Username",
                table.getValueAt(
                        row,
                        1
                )
        );

        addDetail(
                content,
                "IP Address",
                table.getValueAt(
                        row,
                        2
                )
        );

        addDetail(
                content,
                "Device",
                table.getValueAt(
                        row,
                        3
                )
        );

        addDetail(
                content,
                "Failed Attempts",
                table.getValueAt(
                        row,
                        4
                )
        );

        addDetail(
                content,
                "Login Hour",
                table.getValueAt(
                        row,
                        5
                ) + ":00"
        );

        addDetail(
                content,
                "Risk Score",
                table.getValueAt(
                        row,
                        6
                ) + " / 100"
        );

        addDetail(
                content,
                "Risk Level",
                table.getValueAt(
                        row,
                        7
                )
        );

        addDetail(
                content,
                "Login Time",
                table.getValueAt(
                        row,
                        8
                )
        );

        addDetail(
                content,
                "Status",
                "Recorded in database"
        );


        root.add(
                content,
                BorderLayout.CENTER
        );


        JButton close =
                new JButton(
                        "CLOSE"
                );

        close.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        close.setForeground(
                WHITE
        );

        close.setBackground(
                TEAL
        );

        close.setFocusPainted(
                false
        );

        close.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        30,
                        10,
                        30
                )
        );

        close.addActionListener(
                e -> dialog.dispose()
        );


        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottom.setBackground(
                BACKGROUND
        );

        bottom.add(close);


        root.add(
                bottom,
                BorderLayout.SOUTH
        );


        dialog.setContentPane(
                root
        );

        dialog.setVisible(true);
    }


    // =========================================================
    // DETAIL ROW
    // =========================================================

    private static void addDetail(
            JPanel panel,
            String label,
            Object value
    ) {

        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        labelComponent.setForeground(
                SECONDARY
        );


        JLabel valueComponent =
                new JLabel(
                        String.valueOf(value)
                );

        valueComponent.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        valueComponent.setForeground(
                TEXT
        );


        panel.add(
                labelComponent
        );

        panel.add(
                valueComponent
        );
    }


    // =========================================================
    // CENTER RENDERER
    // =========================================================

    private static class CenterRenderer
            extends DefaultTableCellRenderer {

        public CenterRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );
        }
    }


    // =========================================================
    // SCORE RENDERER
    // =========================================================

    private static class ScoreRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    selected,
                                    focus,
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
                            12
                    )
            );


            if (!selected) {

                int score;

                try {

                    score =
                            Integer.parseInt(
                                    String.valueOf(
                                            value
                                    )
                            );

                } catch (Exception e) {

                    score = 0;
                }


                if (score >= 61) {

                    label.setForeground(
                            RED
                    );

                } else if (score >= 31) {

                    label.setForeground(
                            ORANGE
                    );

                } else {

                    label.setForeground(
                            GREEN
                    );
                }
            }


            return label;
        }
    }


    // =========================================================
    // RISK RENDERER
    // =========================================================

    private static class RiskRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    selected,
                                    focus,
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
                            10
                    )
            );


            if (!selected) {

                String risk =
                        String.valueOf(
                                value
                        );


                if ("HIGH".equalsIgnoreCase(
                        risk
                )) {

                    label.setForeground(
                            RED
                    );

                    label.setBackground(
                            RED_LIGHT
                    );

                } else if (
                        "MEDIUM".equalsIgnoreCase(
                                risk
                        )
                ) {

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

            } else {

                label.setBackground(
                        new Color(
                                240,
                                253,
                                250
                        )
                );

                label.setForeground(
                        TEXT
                );
            }


            return label;
        }
    }
}