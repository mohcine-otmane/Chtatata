package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.geom.RoundRectangle2D;

public class Main {
//    Styling
    private static final Color DARK_BG = new Color(18, 18, 18);
    private static final Color DARKER_BG = new Color(24, 24, 24);
    private static final Color ACCENT = new Color(86, 71, 255);
    private static final Color ACCENT_HOVER = new Color(103, 89, 255);
    private static final Color TEXT = new Color(230, 230, 230);
    private static final Color SECONDARY_TEXT = new Color(180, 180, 180);
    private static final Color BORDER = new Color(38, 38, 38);

    private static JTextArea resultArea;
    private static JTextField searchField;
    private static JFrame frame;

    public static void main(String[] args) {
        setLookAndFeel();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Weather Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setBackground(DARK_BG);
        frame.setLayout(new BorderLayout());

        // Add main content
        frame.add(createMainPanel(), BorderLayout.CENTER);

        addWindowDraggability();

        // Custom window decoration
        frame.setUndecorated(true);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(BORDER, 1));

        frame.setVisible(true);
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title bar
        JPanel titleBar = createTitleBar();
        mainPanel.add(titleBar, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private static JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(DARK_BG);
        titleBar.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Chtatata");
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleBar.add(title, BorderLayout.WEST);

        JPanel windowControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        windowControls.setBackground(DARK_BG);

        JButton minimizeButton = createWindowButton("−");
        JButton closeButton = createImageButton("/xbutton.png");

        minimizeButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        closeButton.addActionListener(e -> System.exit(0));

        windowControls.add(minimizeButton);
        windowControls.add(closeButton);
        titleBar.add(windowControls, BorderLayout.EAST);

        return titleBar;
    }

    private static JPanel createSearchPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DARK_BG);

        // Search components
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchContainer.setBackground(DARK_BG);

        searchField = createStyledTextField();
        JButton searchButton = createImageButton("/searchbutton.png");
        
        // Set consistent size for search button
        searchButton.setPreferredSize(new Dimension(48, 40));

        searchContainer.add(searchField);
        searchContainer.add(searchButton);

        // Results area
        resultArea = createStyledTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);

        // Add components to content panel
        contentPanel.add(searchContainer);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);

        // Event handlers
        searchButton.addActionListener(e -> handleSearchAction());
        searchField.addActionListener(e -> handleSearchAction());

        return contentPanel;
    }

    private static JTextField createStyledTextField() {
        JTextField field = new JTextField("Casablanca");
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(DARKER_BG);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private static JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(DARKER_BG);
        area.setForeground(TEXT);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(ACCENT.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(ACCENT_HOVER);
                } else {
                    g2.setColor(ACCENT);
                }

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(100, 40));
        button.setForeground(TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static JButton createWindowButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(45, 30));
        button.setForeground(TEXT);
        button.setBackground(DARK_BG);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(text.equals("×") ? new Color(232, 17, 35) : BORDER);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARK_BG);
            }
        });

        return button;
    }

    private static JButton createImageButton(String imagePath) {
        JButton button = new JButton();
        boolean isCloseButton = imagePath.contains("xbutton");
        
        try {
            // Load image from resources
            ImageIcon icon = new ImageIcon(Main.class.getResource(imagePath));

            // Set appropriate size based on button type
            int iconSize = isCloseButton ? 86 : 44;
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            button.setText(isCloseButton ? "×" : "🔍"); // Fallback text
        }
    
        // Default size for close button
        if (isCloseButton) {
            button.setPreferredSize(new Dimension(64, 48));
        }
        
        button.setBackground(DARK_BG);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        // Different hover effects based on button type
        Color hoverColor = isCloseButton ? 
            new Color(232, 17, 35, 80) : 
            new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 80);
            
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });
    
        return button;
    }
    
    private static void addWindowDraggability() {
        MouseAdapter ma = new MouseAdapter() {
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                offset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (offset != null) {
                    Point current = e.getLocationOnScreen();
                    frame.setLocation(current.x - offset.x, current.y - offset.y);
                }
            }
        };

        frame.addMouseListener(ma);
        frame.addMouseMotionListener(ma);
    }

    private static void handleSearchAction() {
        String city = searchField.getText().trim();
        if (!city.isEmpty()) {
            resultArea.setText("Fetching weather data...");
            WeatherApi.fetchWeatherData(city)
                    .thenAccept(response -> SwingUtilities.invokeLater(() -> 
                        resultArea.setText(response)))
                    .exceptionally(error -> {
                        SwingUtilities.invokeLater(() -> 
                            resultArea.setText("Error: " + error.getMessage()));
                        return null;
                    });
        }
    }
}