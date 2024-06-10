package UI;


import chess.GamePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StartMenu implements Runnable {

    public void run() {
        final JFrame startWindow = new JFrame("Chess");
        // Set window properties
        startWindow.setLocation(500, 100);
        startWindow.setResizable(false);
        startWindow.setSize(400, 340);

        Box components = Box.createVerticalBox();
        startWindow.add(components);

        // Game title
        final JPanel titlePanel = new JPanel();
        components.add(titlePanel);
        final JLabel titleLabel = new JLabel("Chess");
        titlePanel.add(titleLabel);

        // Black player selections
        final JPanel blackPanel = new JPanel();
        components.add(blackPanel, BorderLayout.EAST);
        final JLabel blackPiece = new JLabel();
        try {
            Image blackImg = ImageIO.read(new File("PIECES/black.png"));
            blackPiece.setIcon(new ImageIcon(blackImg));
            blackPanel.add(blackPiece);
        } catch (Exception e) {
            System.out.println("Required game file lion.png missing");
        }


        final JTextField blackInput = new JTextField("Black", 10);
        blackPanel.add(blackInput);

        // White player selections
        final JPanel whitePanel = new JPanel();
        components.add(whitePanel);
        final JLabel whitePiece = new JLabel();

        try {
            Image whiteImg = ImageIO.read((new File("PIECES/white.png")));
            whitePiece.setIcon(new ImageIcon(whiteImg));
            whitePanel.add(whitePiece);
            startWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("Required game file tiger.png missing");
        }


        final JTextField whiteInput = new JTextField("White", 10);
        whitePanel.add(whiteInput);

        // Timer settings
        final String[] minSecInts = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minSecInts[i] = "0" + Integer.toString(i);
            } else {
                minSecInts[i] = Integer.toString(i);
            }
        }

        final JComboBox<String> seconds = new JComboBox<String>(minSecInts);
        final JComboBox<String> minutes = new JComboBox<String>(minSecInts);
        final JComboBox<String> hours =
                new JComboBox<String>(new String[] {"0","1","2","3"});

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(seconds);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(minutes);

        timerSettings.add(Box.createVerticalGlue());

        components.add(timerSettings);
        // Buttons
        Box buttons = Box.createHorizontalBox();
        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startWindow.dispose();
            }
        });

        final JButton instr = new JButton("Instructions");


        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(startWindow,
                        "To begin a new game, input player names\n" +
                                "next to the pieces. Set the clocks and\n" +
                                "click \"Start\". Setting the timer to all\n" +
                                "zeroes begins a new untimed game.",
                        "How to play",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        // interprets JOptionPane result

        final GamePanel GP = new GamePanel(startWindow.getWidth(), startWindow.getHeight());


        final JComboBox<String> theme = new JComboBox <String>(new String[]{"Default", "Christmas", "New Year", "Wood"});
        final JComboBox<String> mode = new JComboBox <String>(new String[]{"vsHuman", "vsComputer"});


        final Box exrtraSetting = Box.createHorizontalBox();

        exrtraSetting.add(theme);
        exrtraSetting.add(Box.createHorizontalStrut(10));

        exrtraSetting.add(mode);

        exrtraSetting.add(Box.createVerticalGlue());
        components.add(exrtraSetting);


        theme.setMaximumSize(theme.getPreferredSize());
        mode.setMaximumSize(mode.getPreferredSize());


        final JButton start = new JButton("Start");
      //  final GamePanel.ColorBoard colorTheme;
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel.ColorBoard colorTheme = null;
                // GamePanel.GameMode gameMode = null;
                if (theme.getSelectedItem().toString().equals("Christmas")) {
                    colorTheme = GamePanel.ColorBoard.C;
                } else if (theme.getSelectedItem().toString().equals("New Year")) {
                    colorTheme = GamePanel.ColorBoard.N;
                }  else if (theme.getSelectedItem().toString().equals("Wood")) {
                    colorTheme = GamePanel.ColorBoard.W;
                } else if (theme.getSelectedItem().toString().equals("Default")) {
                    colorTheme = GamePanel.ColorBoard.D;
                }

                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());

                if (mode.getSelectedItem().toString().equals("vsHuman")){
                    GP.newGame();
                }
                else GP.newAiGame();
                GP.setColorBoard(colorTheme);

                startWindow.dispose();
                //Player_screen p = new Player_screen(GP);
                UI.GameWindow gw = new GameWindow(blackInput.getText(), whiteInput.getText(), GP, hh, mm, ss);

            }});

        buttons.add(start);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(instr);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(quit);
        components.add(buttons);

        Component space = Box.createGlue();
        components.add(space);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }
}
