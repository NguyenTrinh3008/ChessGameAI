/*
Name: Trinh Binh Nguyen
Purpose: Defines the game window.
*/
package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import chess.Board;
import chess.GamePanel;
import chess.Clock;
import chess.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;

import static java.awt.Color.*;


public class GameWindow {
    private JFrame gameWindow;

    int undoMove,redoMove;

    private int count;

    public Clock blackClock;
    public Clock whiteClock;

    private Timer timer;

    private GamePanel GP;



    public GameWindow(String blackName, String whiteName, GamePanel GP, int hh,
                      int mm, int ss) {

         this.GP = GP;


        blackClock = new Clock(hh, ss, mm);
        whiteClock = new Clock(hh, ss, mm);

        gameWindow = new JFrame("Chess Game by Trinh Nguyen");

        try {
            Image whiteImg = ImageIO.read(new File("PIECES/withe.png"));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("Game file white.png not found");
        }

        gameWindow.getContentPane().setBackground(getHSBColor(165, 0.002F, 0.87F));

        gameWindow.setLocation(200, 100);

        gameWindow.setLayout(new BorderLayout(30, 20));

        // Game Data window
        JPanel gameData = gameDataPanel(blackName, whiteName, hh, mm, ss);
        gameData.setBackground(getHSBColor(165, 0.002F, 0.87F));
        gameData.setSize(gameData.getPreferredSize());
        gameWindow.add(gameData, BorderLayout.NORTH);

        gameWindow.add(GP, BorderLayout.CENTER);

        gameWindow.add(downButtons(), BorderLayout.SOUTH);
        gameWindow.add(sideButtons(), BorderLayout.EAST);

        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);
        gameWindow.setSize(650, 650);

        // gameWindow.pack();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

// Helper function to create data panel

    private JPanel gameDataPanel(final String bn, final String wn,
                                 final int hh, final int mm, final int ss) {

        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(2, 2,  4, 2));

        JLabel w = new JLabel(wn);
        JLabel b = new JLabel(bn);

        w.setHorizontalAlignment(JLabel.CENTER);
        w.setVerticalAlignment(JLabel.CENTER);
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setVerticalAlignment(JLabel.CENTER);

        w.setSize(w.getMinimumSize());
        b.setSize(b.getMinimumSize());

        gameData.add(w);
        gameData.add(b);

        final JLabel bTime = new JLabel(blackClock.getTime());
        final JLabel wTime = new JLabel(whiteClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        if (!(hh == 0 && mm == 0 && ss == 0)) {
            timer = new Timer(1000, null);
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    Board board = GP.getBoard();
                    Piece.Color turn = board.getTurn();

                    if (turn == Piece.Color.White) {
                        whiteClock.decr();
                        wTime.setText(whiteClock.getTime());

                        if (whiteClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    gameWindow,
                                    bn + " wins by time! Play a new game? \n" +
                                            "Choosing \"No\" quits the game.",
                                    bn + " wins!",
                                    JOptionPane.YES_NO_OPTION);

                            if (n == JOptionPane.YES_OPTION) {
                                new GameWindow(bn, wn, GP, hh, mm, ss);
                                gameWindow.dispose();
                            } else gameWindow.dispose();
                        }
                    } else {
                        blackClock.decr();
                        bTime.setText(blackClock.getTime());

                        if (blackClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    gameWindow,
                                    wn + " wins by time! Play a new game? \n" +
                                            "Choosing \"No\" quits the game.",
                                    wn + " wins!",
                                    JOptionPane.YES_NO_OPTION);

                            if (n == JOptionPane.YES_OPTION) {
                                new GameWindow(bn, wn, GP, hh, mm, ss);
                                gameWindow.dispose();
                            } else gameWindow.dispose();
                        }
                    }
                }
            });
            timer.start();
        } else {
            wTime.setText("Untimed game");
            bTime.setText("Untimed game");
        }
        gameData.add(wTime);
        gameData.add(bTime);

        gameData.setPreferredSize(gameData.getMinimumSize());
       return gameData;

    }

    private JPanel downButtons() {

        JPanel downButtons = new JPanel();
     //   gameWindow.setLayout(null);
        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Are you sure you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    if (timer != null) timer.stop();
                    gameWindow.dispose();
                }
            }
        });

        final JButton nGame = new JButton("New Game");

        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Are you sure you want to begin a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindow.dispose();
                }
            }
        });

        final JButton instr = new JButton("How 2 play");

        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameWindow,
                        "To play, move chess pieces by clicking and dragging.\n"
                                + "The game prevents illegal moves. Win by either\n"
                                + "timing out your opponent or checkmating them.\n"
                                + "\nGood luck and enjoy the game!",
                        "How 2 Play",
                        JOptionPane.PLAIN_MESSAGE);
            }
    });

        // Set layout to null for absolute positioning
        downButtons.setLayout(null);

        // Set button bounds and locations
        instr.setBounds(20, 2, 100, 30);
        nGame.setBounds(185, 2, 100, 30);
        quit.setBounds(350, 2, 100, 30);

        downButtons.setPreferredSize(new Dimension(400, 50));

        downButtons.add(instr);
        downButtons.add(nGame);
        downButtons.add(quit);
        downButtons.setBackground(getHSBColor(165, 0.002F, 0.87F));
        instr.setBackground(gray);
        nGame.setBackground(gray);
        quit.setBackground(gray);

        return downButtons;
    }

    private JPanel sideButtons() {
        JPanel sideButtons = new JPanel();

        final JButton undo = new JButton("Undo");

        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undoMove++;
                GP.Undo(undoMove);
                GP.repaint();
            }
        });

        final JButton redo = new JButton("Redo");

        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redoMove++;
                GP.Redo(redoMove);
                GP.repaint();
            }
        });

        final JButton theme = new JButton("Theme");

        theme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                if(count == 1){
                    GP.setColorBoard(GamePanel.ColorBoard.C);
                    GP.repaint();
                }
                if(count == 2){
                    GP.setColorBoard(GamePanel.ColorBoard.N);
                    GP.repaint();
                }
                if(count == 3){
                    GP.setColorBoard(GamePanel.ColorBoard.D);
                    GP.repaint();
                    count = 0;
                }
            }
        });
        sideButtons.add(undo);
        sideButtons.add(redo);
        sideButtons.add(theme);
        sideButtons.setBackground(getHSBColor(165, 0.002F, 0.87F));
        undo.setBackground(GRAY);
        redo.setBackground(GRAY);
        theme.setBackground(GRAY);

        sideButtons.setLayout(null);

        // Set button bounds and locations
        undo.setBounds(1, 100, 100, 30);
        redo.setBounds(1, 200, 100, 30);
        theme.setBounds(1, 300, 100, 30);

        sideButtons.setPreferredSize(new Dimension(120, 25));


        return sideButtons;
    }
}
