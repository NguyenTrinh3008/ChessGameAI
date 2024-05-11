package chessgame;

import chess.GamePanel;

public class GameWindow extends javax.swing.JFrame {

    GamePanel gameScreen;
    public GameWindow() {
        initComponents();
        init();
    }
    private void init() {
        gameScreen = new GamePanel(jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.add(gameScreen);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jMenuBar_Main = new javax.swing.JMenuBar();
        jMenu_Game = new javax.swing.JMenu();
        jMenuItem_New1P = new javax.swing.JMenuItem();
        jMenuItem_New2P = new javax.swing.JMenuItem();
        jMenuItem_Undo = new javax.swing.JMenuItem();
        jMenuItem_Redo = new javax.swing.JMenuItem(); // Redo menu item
        jMenuItem_Close = new javax.swing.JMenuItem();
        jMenu_File = new javax.swing.JMenu();
        jMenuItem_Save = new javax.swing.JMenuItem();
        jMenuItem_Load = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChessGame by NguyenTrinh");
        setLocationByPlatform(true);

        jPanel1.setMaximumSize(new java.awt.Dimension(10000, 10000));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1_componentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 800, Short.MAX_VALUE)
        );

        jMenu_Game.setText("Game");

        jMenuItem_New1P.setText("New 1-Player");
        jMenuItem_New1P.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_New1PActionPerformed(evt);
            }
        });
        jMenu_Game.add(jMenuItem_New1P);

        jMenuItem_New2P.setText("New 2-Player");
        jMenuItem_New2P.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_New2PActionPerformed(evt);
            }
        });
        jMenu_Game.add(jMenuItem_New2P);

        jMenuItem_Undo.setText("Undo");
        jMenuItem_Undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_UndoActionPerformed(evt);
            }
        });
        jMenu_Game.add(jMenuItem_Undo);

        jMenuItem_Redo.setText("Redo"); // Setting text for the Redo menu item
        jMenuItem_Redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_RedoActionPerformed(evt); // Action for the Redo menu item
            }
        });
        jMenu_Game.add(jMenuItem_Redo);

        jMenuItem_Close.setText("Close");
        jMenuItem_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CloseActionPerformed(evt);
            }
        });
        jMenu_Game.add(jMenuItem_Close);

        jMenuBar_Main.add(jMenu_Game);

        jMenu_File.setText("File");

        jMenuItem_Save.setText("Save");
        jMenuItem_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_SaveActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_Save);

        jMenuItem_Load.setText("Load");
        jMenuItem_Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_LoadActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_Load);

        jMenuBar_Main.add(jMenu_File);

        setJMenuBar(jMenuBar_Main);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void jMenuItem_CloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void jMenuItem_New2PActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.newGame();
    }

    private void jMenuItem_LoadActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.loadBoard();
    }

    private void jMenuItem_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.saveBoard();
    }

    private void jMenuItem_UndoActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.undo();
    }

    private void jMenuItem_RedoActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.redo();
    }

    private void jMenuItem_New1PActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.newAiGame();
    }

    private void jPanel1_componentResized(java.awt.event.ComponentEvent evt) {
        if (jPanel1 != null && gameScreen != null) {
            int smallerDimension = Math.min(jPanel1.getWidth(), jPanel1.getHeight());
            gameScreen.setSize(smallerDimension, smallerDimension);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new GameWindow().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JMenuBar jMenuBar_Main;
    private javax.swing.JMenuItem jMenuItem_Close;
    private javax.swing.JMenuItem jMenuItem_Load;
    private javax.swing.JMenuItem jMenuItem_New1P;
    private javax.swing.JMenuItem jMenuItem_New2P;
    private javax.swing.JMenuItem jMenuItem_Save;
    private javax.swing.JMenuItem jMenuItem_Undo;
    private javax.swing.JMenuItem jMenuItem_Redo;
    private javax.swing.JMenu jMenu_File;
    private javax.swing.JMenu jMenu_Game;
    private javax.swing.JPanel jPanel1;
}
