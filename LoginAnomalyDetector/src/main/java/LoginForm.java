import javax.swing.*;
import java.awt.*;

public class LoginForm {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Analyze New Login");

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // =====================================================
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel();

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBackground(
                new Color(15, 23, 42)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "ANALYZE NEW LOGIN"
                );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Enter login information for security analysis"
                );

        subtitle.setForeground(
                new Color(203, 213, 225)
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        header.add(title);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(subtitle);

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // FORM
        // =====================================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                12,
                                15
                        )
                );

        formPanel.setBackground(
                Color.WHITE
        );

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(226, 232, 240)
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        JLabel usernameLabel =
                new JLabel("Username:");

        JTextField usernameField =
                new JTextField();

        JLabel ipLabel =
                new JLabel("IP Address:");

        JTextField ipField =
                new JTextField();

        JLabel deviceLabel =
                new JLabel("Device:");

        JTextField deviceField =
                new JTextField();

        JLabel failedLabel =
                new JLabel("Failed Attempts:");

        JTextField failedField =
                new JTextField();

        JLabel hourLabel =
                new JLabel("Login Hour (0-23):");

        JTextField hourField =
                new JTextField();

        JButton analyzeButton =
                new JButton(
                        "ANALYZE LOGIN"
                );

        analyzeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        analyzeButton.setFocusPainted(false);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(ipLabel);
        formPanel.add(ipField);

        formPanel.add(deviceLabel);
        formPanel.add(deviceField);

        formPanel.add(failedLabel);
        formPanel.add(failedField);

        formPanel.add(hourLabel);
        formPanel.add(hourField);

        formPanel.add(
                new JLabel("")
        );

        formPanel.add(
                analyzeButton
        );

        JPanel formContainer =
                new JPanel(
                        new BorderLayout()
                );

        formContainer.setBackground(
                new Color(245, 247, 250)
        );

        formContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        formContainer.add(
                formPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                formContainer,
                BorderLayout.CENTER
        );

        frame.add(mainPanel);

        frame.setVisible(true);

        // =====================================================
        // ANALYZE BUTTON
        // =====================================================

        analyzeButton.addActionListener(e -> {

            try {

                String username =
                        usernameField.getText().trim();

                String ipAddress =
                        ipField.getText().trim();

                String device =
                        deviceField.getText().trim();

                int failedAttempts =
                        Integer.parseInt(
                                failedField.getText().trim()
                        );

                int loginHour =
                        Integer.parseInt(
                                hourField.getText().trim()
                        );

                // -------------------------------------------------
                // Basic validation
                // -------------------------------------------------

                if (username.isEmpty()
                        || ipAddress.isEmpty()
                        || device.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Please fill in all fields.",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if (loginHour < 0
                        || loginHour > 23) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Login hour must be between 0 and 23.",
                            "Invalid Login Hour",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                if (failedAttempts < 0) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Failed attempts cannot be negative.",
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                // -------------------------------------------------
                // Create login event
                // -------------------------------------------------

                LoginEvent event =
                        new LoginEvent(
                                username,
                                ipAddress,
                                device,
                                failedAttempts,
                                loginHour
                        );

                // -------------------------------------------------
                // Analyze
                // -------------------------------------------------

                AnomalyDetector.analyze(
                        event
                );

                // -------------------------------------------------
                // Save to database
                // -------------------------------------------------

                LoginDatabase.saveLogin(
                        event
                );

                // -------------------------------------------------
                // Show result
                // -------------------------------------------------

                showResult(
                        frame,
                        event
                );

                // -------------------------------------------------
                // Save HIGH alert
                // -------------------------------------------------

                if (event.riskLevel.equals("HIGH")) {

                    AlertDatabase.saveAlert(
                            event
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter valid numbers for failed attempts and login hour.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    // =========================================================
    // SHOW RESULT
    // =========================================================

    private static void showResult(
            JFrame parent,
            LoginEvent event
    ) {

        JDialog dialog =
                new JDialog(
                        parent,
                        "Login Analysis Result",
                        true
                );

        dialog.setSize(
                550,
                600
        );

        dialog.setLocationRelativeTo(
                parent
        );

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        // =====================================================
        // RESULT HEADER
        // =====================================================

        JPanel header =
                new JPanel();

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBackground(
                new Color(15, 23, 42)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "LOGIN ANALYSIS RESULT"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        header.add(title);

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // RESULT CONTENT
        // =====================================================

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                Color.WHITE
        );

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        // =====================================================
        // SCORE
        // =====================================================

        JLabel scoreTitle =
                new JLabel(
                        "RISK SCORE"
                );

        scoreTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        scoreTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel scoreLabel =
                new JLabel(
                        event.riskScore + " / 100"
                );

        scoreLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        42
                )
        );

        scoreLabel.setForeground(
                getRiskColor(
                        event.riskLevel
                )
        );

        scoreLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(scoreTitle);

        content.add(
                Box.createVerticalStrut(5)
        );

        content.add(scoreLabel);

        content.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // RISK BAR
        // =====================================================

        JProgressBar riskBar =
                new JProgressBar(
                        0,
                        100
                );

        riskBar.setValue(
                event.riskScore
        );

        riskBar.setString(
                event.riskScore + "%"
        );

        riskBar.setStringPainted(
                true
        );

        riskBar.setForeground(
                getRiskColor(
                        event.riskLevel
                )
        );

        riskBar.setBackground(
                new Color(
                        226,
                        232,
                        240
                )
        );

        riskBar.setPreferredSize(
                new Dimension(
                        450,
                        28
                )
        );

        riskBar.setMaximumSize(
                new Dimension(
                        450,
                        28
                )
        );

        content.add(
                riskBar
        );

        content.add(
                Box.createVerticalStrut(15)
        );

        // =====================================================
        // RISK LEVEL
        // =====================================================

        JLabel levelLabel =
                new JLabel(
                        event.riskLevel
                );

        levelLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        levelLabel.setForeground(
                getRiskColor(
                        event.riskLevel
                )
        );

        levelLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(
                levelLabel
        );

        content.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // DETECTED ISSUES
        // =====================================================

        JLabel issuesTitle =
                new JLabel(
                        "Detected Issues"
                );

        issuesTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        issuesTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(
                issuesTitle
        );

        content.add(
                Box.createVerticalStrut(8)
        );

        JTextArea reasonsArea =
                new JTextArea();

        if (event.riskReasons.isEmpty()) {

            reasonsArea.setText(
                    "✓ No suspicious activity detected."
            );

        } else {

            reasonsArea.setText(
                    event.riskReasons
            );
        }

        reasonsArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        reasonsArea.setEditable(
                false
        );

        reasonsArea.setLineWrap(
                true
        );

        reasonsArea.setWrapStyleWord(
                true
        );

        reasonsArea.setBackground(
                new Color(
                        248,
                        250,
                        252
                )
        );

        reasonsArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        226,
                                        232,
                                        240
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        reasonsArea.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(
                reasonsArea
        );

        mainPanel.add(
                content,
                BorderLayout.CENTER
        );

        // =====================================================
        // CLOSE BUTTON
        // =====================================================

        JButton closeButton =
                new JButton(
                        "CLOSE"
                );

        closeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        closeButton.setFocusPainted(
                false
        );

        closeButton.setPreferredSize(
                new Dimension(
                        100,
                        40
                )
        );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                12
                        )
                );

        bottomPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        bottomPanel.add(
                closeButton
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        dialog.add(
                mainPanel
        );

        dialog.setVisible(
                true
        );
    }

    // =========================================================
    // RISK COLOR
    // =========================================================

    private static Color getRiskColor(
            String riskLevel
    ) {

        if (riskLevel.equals("HIGH")) {

            return new Color(
                    220,
                    38,
                    38
            );

        } else if (
                riskLevel.equals("MEDIUM")
        ) {

            return new Color(
                    234,
                    146,
                    0
            );

        } else {

            return new Color(
                    22,
                    163,
                    74
            );
        }
    }
}