package fr.newstaz.istore.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class ToastComponent {

    private static ToastMessage toastMessage;

    public enum ToastPosition {
        TOP_RIGHT,
        // Ajoutez d'autres positions si nécessaire
    }

    public static void showSuccessToast(JPanel mainFrame, String message) {
        showToast(mainFrame, message, new Color(0, 200, 0), ToastPosition.TOP_RIGHT, 500);
    }

    public static void showFailedToast(JPanel mainFrame, String message) {
        showToast(mainFrame, message, new Color(200, 0, 0), ToastPosition.TOP_RIGHT, 500);
    }

    public static void showToast(JPanel parentPanel, String message, Color backgroundColor,
                                 ToastPosition position, int duration) {
        if (toastMessage != null && toastMessage.isVisible()) {
            toastMessage.updateMessage(message);
        } else {
            toastMessage = new ToastMessage(parentPanel, message, backgroundColor, position);
            SwingUtilities.invokeLater(() -> {
                toastMessage.display(duration);
            });
        }
    }

    public static class ToastMessage extends JWindow {

        private final JLabel messageLabel;

        public ToastMessage(JPanel parentPanel, final String message, Color backgroundColor,
                            ToastPosition position) {
            setLayout(new GridBagLayout());
            getContentPane().setBackground(backgroundColor);
            setSize(300, 50);

            Point parentLocation = parentPanel.getLocationOnScreen();
            setLocation(calculatePosition(parentLocation, parentPanel.getWidth(), position));

            messageLabel = new JLabel(message);
            messageLabel.setForeground(Color.WHITE);  // Couleur du texte
            add(messageLabel);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                }
            });
        }

        public void updateMessage(String newMessage) {
            messageLabel.setText(newMessage);
            setOpacity(1);
        }

        public void display(int duration) {
            try {
                setOpacity(1);
                setVisible(true);

                Timer timer = new Timer(duration, (e) -> {
                    for (double d = 1.0; d > 0.2; d -= 0.1) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        setOpacity((float) d);
                    }
                    setVisible(false);
                });
                timer.setRepeats(false);
                timer.start();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private Point calculatePosition(Point parentLocation, int parentWidth, ToastPosition position) {
            int x = 0;
            int y = parentLocation.y;

            switch (position) {
                case TOP_RIGHT:
                    x = parentLocation.x + parentWidth - getWidth() - 10;
                    y += 10;
                    break;
                // Ajoutez d'autres positions si nécessaire
            }

            return new Point(x, y);
        }
    }
}

