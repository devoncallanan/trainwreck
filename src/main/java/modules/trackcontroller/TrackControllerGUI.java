package modules.trackcontroller;

import java.io.File;
import javax.swing.JFileChooser;


public class TrackControllerGUI extends javax.swing.JFrame {

    boolean [] mainLine, msplit, side;
    TrackController tc;
    PLC plc;
    public TrackControllerGUI(TrackController t, boolean[] n, boolean[] r, boolean[] s, PLC p) {
        initComponents();
        plc = p;
        tc = t;
        mainLine = n;
        msplit = r;
        side = s;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        AutManGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        sideList = new javax.swing.JList<>();
        automaticButton = new javax.swing.JRadioButton();
        manualButton = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        msplitList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        mainLineList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        switchStatus = new javax.swing.JLabel();
        switchLightStatus = new javax.swing.JLabel();
        crossingStatus = new javax.swing.JLabel();
        crossingLightStatus = new javax.swing.JLabel();
        switchMainButton = new javax.swing.JRadioButton();
        switchSideButton = new javax.swing.JRadioButton();
        switchLightOnButton = new javax.swing.JRadioButton();
        switchLightOffButton = new javax.swing.JRadioButton();
        crossingUpButton = new javax.swing.JRadioButton();
        crossingDownButton = new javax.swing.JRadioButton();
        crossingLightOnButton = new javax.swing.JRadioButton();
        crossingLightOffButton = new javax.swing.JRadioButton();
        importPLCButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sideList.setModel(new javax.swing.AbstractListModel<String>() {
           boolean[] strings = side;
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return ""+strings[i]; }
        });
        jScrollPane1.setViewportView(sideList);

        automaticButton.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        automaticButton.setText("Automatic");
        automaticButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticButtonActionPerformed(evt);
            }
        });

        manualButton.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        manualButton.setText("Manual");
        manualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualButtonActionPerformed(evt);
            }
        });

        msplitList.setModel(new javax.swing.AbstractListModel<String>() {
            boolean[] strings =msplit;
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return ""+strings[i]; }
        });
        jScrollPane2.setViewportView(msplitList);

        mainLineList.setModel(new javax.swing.AbstractListModel<String>() {
            boolean[] strings = mainLine;
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return ""+strings[i]; }
        });
        jScrollPane3.setViewportView(mainLineList);

        jLabel1.setText("Main");

        jLabel2.setText("M2");

        jLabel3.setText("Side");

        switchStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        switchStatus.setText("Switch Status:");

        switchLightStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        switchLightStatus.setText("Switch Light Status:");

        crossingStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        crossingStatus.setText("Crossing Status:");

        crossingLightStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        crossingLightStatus.setText("Crossing Light Status:");

        switchMainButton.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        switchMainButton.setText("Main");
        switchMainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchMainButtonActionPerformed(evt);
            }
        });

        switchSideButton.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        switchSideButton.setText("Side");
        switchSideButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchSideButtonActionPerformed(evt);
            }
        });

        switchLightOnButton.setText("On");
        switchLightOnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchLightOnButtonActionPerformed(evt);
            }
        });

        switchLightOffButton.setText("Off");
        switchLightOffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchLightOffButtonActionPerformed(evt);
            }
        });

        crossingUpButton.setText("Up");
        crossingUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crossingUpButtonActionPerformed(evt);
            }
        });

        crossingDownButton.setText("Down");
        crossingDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crossingDownButtonActionPerformed(evt);
            }
        });

        crossingLightOnButton.setText("On");
        crossingLightOnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crossingLightOnButtonActionPerformed(evt);
            }
        });

        crossingLightOffButton.setText("Off");
        crossingLightOffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crossingLightOffButtonActionPerformed(evt);
            }
        });

        importPLCButton.setText("Import PLC");
        importPLCButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importPLCButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(automaticButton, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(switchMainButton)
                        .addGap(18, 18, 18)
                        .addComponent(switchSideButton))
                    .addComponent(switchLightStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(switchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(switchLightOnButton)
                        .addGap(18, 18, 18)
                        .addComponent(switchLightOffButton))
                    .addComponent(crossingStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(crossingLightOnButton)
                        .addGap(18, 18, 18)
                        .addComponent(crossingLightOffButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(crossingUpButton)
                        .addGap(18, 18, 18)
                        .addComponent(crossingDownButton))
                    .addComponent(crossingLightStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel1)
                                .addGap(48, 48, 48)
                                .addComponent(jLabel2)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel3))))
                    .addComponent(importPLCButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(automaticButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(manualButton)
                        .addGap(1, 1, 1)
                        .addComponent(switchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(switchMainButton)
                            .addComponent(switchSideButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(switchLightStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(switchLightOnButton)
                            .addComponent(switchLightOffButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crossingStatus)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(crossingUpButton)
                            .addComponent(crossingDownButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crossingLightStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(crossingLightOnButton)
                            .addComponent(crossingLightOffButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(importPLCButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(131, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void manualButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setMode(false);
    }

    private void automaticButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setMode(true);
    }

    private void switchMainButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setSwitch(true);
    }

    private void switchSideButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setSwitch(false);
    }

    private void switchLightOnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setSwitchLight(true);
    }

    private void switchLightOffButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setSwitchLight(false);
    }

    private void crossingUpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setCross(false);
    }

    private void crossingDownButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setCross(true);
    }

    private void crossingLightOnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setCrossLight(true);
    }

    private void crossingLightOffButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tc.setCrossLight(false);
    }

    private void importPLCButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        plc.importPLC(f);
    }

    public void changeSwitch(Boolean s){
         if(s)
          switchStatus.setText("Switch Status: Main");
         else
          switchStatus.setText("Switch Status: Side");
    }

    public void changeSwitchLight(boolean s){
         if(s)
          switchLightStatus.setText("Switch Light Status: On");
         else
          switchLightStatus.setText("Switch Light Status: Off");
    }

    public void changeCrossing(boolean s){
         if(s)
          crossingStatus.setText("Crossing Status: Down");
         else
          crossingStatus.setText("Crossing Status: Up");
    }

    public void changeCrossLight(boolean s){
         if(s)
          crossingLightStatus.setText("Crossing Light Status: On");
         else
          crossingLightStatus.setText("Crossing Light Status: Off");

    }
    public void updateMain(boolean[] a){
         mainLine = a;
         mainLineList.updateUI();
    }
    public void updateMsplit(boolean[] a){
         msplit = a;
         msplitList.updateUI();
    }
    public void updateSide(boolean[] a){
         side = a;
         sideList.updateUI();
    }



    // Variables declaration - do not modify
    private javax.swing.ButtonGroup AutManGroup;
    private javax.swing.JRadioButton automaticButton;
    private javax.swing.JRadioButton crossingDownButton;
    private javax.swing.JRadioButton crossingLightOffButton;
    private javax.swing.JRadioButton crossingLightOnButton;
    private javax.swing.JLabel crossingLightStatus;
    private javax.swing.JLabel crossingStatus;
    private javax.swing.JRadioButton crossingUpButton;
    private javax.swing.JButton importPLCButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> mainLineList;
    private javax.swing.JRadioButton manualButton;
    private javax.swing.JList<String> msplitList;
    private javax.swing.JList<String> sideList;
    private javax.swing.JRadioButton switchLightOffButton;
    private javax.swing.JRadioButton switchLightOnButton;
    private javax.swing.JLabel switchLightStatus;
    private javax.swing.JRadioButton switchMainButton;
    private javax.swing.JRadioButton switchSideButton;
    private javax.swing.JLabel switchStatus;
    // End of variables declaration
}
