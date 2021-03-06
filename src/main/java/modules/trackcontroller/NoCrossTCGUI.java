package modules.trackcontroller;

import java.io.File;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author attila
 */
public class NoCrossTCGUI extends javax.swing.JFrame {

     boolean [] mainLine, msplit, side;
     TrackController tc;
     PLC plc;
     int i;
     public NoCrossTCGUI(TrackController t, boolean[] n, boolean[] r, boolean[] s, int z, PLC p) {
         plc = p;
         tc = t;
         mainLine = n;
         msplit = r;
         side = s;
         i = z;
         initComponents();
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        autManGroup = new javax.swing.ButtonGroup();
        switchGroup = new javax.swing.ButtonGroup();
        switchLightGroup = new javax.swing.ButtonGroup();
        automaticButton = new javax.swing.JRadioButton();
        manualButton = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        switchStatus = new javax.swing.JLabel();
        switchLightStatus = new javax.swing.JLabel();
        switchMainButton = new javax.swing.JRadioButton();
        switchSideButton = new javax.swing.JRadioButton();
        switchLightOnButton = new javax.swing.JRadioButton();
        switchLightOffButton = new javax.swing.JRadioButton();
        importPLCButton = new javax.swing.JButton();
        id = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        mainLineList = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        sideList = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        msplitList = new javax.swing.JTextArea();

        autManGroup.add(automaticButton);
        autManGroup.add(manualButton);
        switchGroup.add(switchMainButton);
        switchGroup.add(switchSideButton);
        switchLightGroup.add(switchLightOnButton);
        switchLightGroup.add(switchLightOffButton);

        id.setFont(new java.awt.Font("Ubuntu", 0, 24));
		if(i<7)
			id.setText("ID: RED "+i);
		else{
			int temp = i%7;
			i = 1 + temp;
			id.setText("ID: GREEN "+i);
		}

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel1.setText("Main");

        jLabel2.setText("M2");

        jLabel3.setText("Side");

        switchStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        switchStatus.setText("Switch Status:");

        switchLightStatus.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        switchLightStatus.setText("Switch Light Status:");

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

        importPLCButton.setText("Import PLC");
        importPLCButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importPLCButtonActionPerformed(evt);
            }
        });


        mainLineList.setColumns(20);
        mainLineList.setFont(new java.awt.Font("Ubuntu", 0, 20)); // NOI18N
        mainLineList.setRows(5);
        jScrollPane4.setViewportView(mainLineList);

        sideList.setColumns(20);
		sideList.setFont(new java.awt.Font("Ubuntu", 0, 20));
        sideList.setRows(5);
        jScrollPane5.setViewportView(sideList);

        msplitList.setColumns(20);
		msplitList.setFont(new java.awt.Font("Ubuntu", 0, 20));
        msplitList.setRows(5);
        jScrollPane6.setViewportView(msplitList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(id)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(switchMainButton)
                                .addGap(18, 18, 18)
                                .addComponent(switchSideButton))
                            .addComponent(switchLightStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(switchLightOnButton)
                                .addGap(18, 18, 18)
                                .addComponent(switchLightOffButton))
                            .addComponent(automaticButton, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(manualButton)
                            .addComponent(switchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importPLCButton, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(58, 58, 58)
                                        .addComponent(jLabel2)
                                        .addGap(29, 29, 29))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel3)))))))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
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
                            .addComponent(switchLightOffButton)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(importPLCButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(id)
                .addContainerGap())
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
        private void importPLCButtonActionPerformed(java.awt.event.ActionEvent evt) {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            plc.importPLC(f);
			tc.plcImported();
        }

        public void changeSwitch(Boolean s){
             if(s)
              switchStatus.setText("Switch Status: Main");
             else
              switchStatus.setText("Switch Status: Side");
        }

        public void changeSwitchLight(boolean s){
             if(s)
              switchLightStatus.setText("Switch Light Status: Green");
             else
              switchLightStatus.setText("Switch Light Status: Red");
        }

        public void updateMain(boolean[] a){
             StringBuilder info = new StringBuilder();
    		 for (int j = 0; j < a.length; j++){
    			 info.append(a[j] + "\n");
    		 }
    		 mainLineList.setText(info.toString());
        }
        public void updateMsplit(boolean[] a){
             StringBuilder info = new StringBuilder();
    		 for (int j = 0; j < a.length; j++){
    			 info.append(a[j] + "\n");
    		 }
    		 msplitList.setText(info.toString());
        }
        public void updateSide(boolean[] a){
             StringBuilder info = new StringBuilder();
    		 for (int j = 0; j < a.length; j++){
    			 info.append(a[j] + "\n");
    		 }
    		 sideList.setText(info.toString());
        }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup autManGroup;
    private javax.swing.ButtonGroup switchGroup;
    private javax.swing.ButtonGroup switchLightGroup;
    private javax.swing.JRadioButton automaticButton;
    private javax.swing.JLabel id;
    private javax.swing.JButton importPLCButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea mainLineList;
    private javax.swing.JRadioButton manualButton;
    private javax.swing.JTextArea msplitList;
    private javax.swing.JTextArea sideList;
    private javax.swing.JRadioButton switchLightOffButton;
    private javax.swing.JRadioButton switchLightOnButton;
    private javax.swing.JLabel switchLightStatus;
    private javax.swing.JRadioButton switchMainButton;
    private javax.swing.JRadioButton switchSideButton;
    private javax.swing.JLabel switchStatus;
    // End of variables declaration
}
