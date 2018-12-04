/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chequeprinter;

import backendfiles.DatabaseConnection;
import backendfiles.ManageReport;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sanari
 */
public final class BankStatement extends javax.swing.JDialog {

    
    Connection con = null;

    private final ManageReport manageReport = new ManageReport();
    private final boolean accountPayeeState = true;

    public BankStatement(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        con = DatabaseConnection.Connect();
        FillcomboDefaultProfile();
        FillDetailTable();
        Total();
        
        /*ComboBox Selection Color*/
    AccessibleContext ac = jComboBox1.getAccessibleContext();
    BasicComboPopup pop = (BasicComboPopup) ac.getAccessibleChild(0);
    JList list = pop.getList();
    list.setSelectionForeground(Color.WHITE);
    list.setSelectionBackground(new java.awt.Color(229, 126, 49));
    }

    
     private void FillcomboDefaultProfile() {

        try {
            String sql = "select * from bank";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector v = new Vector();
            v.add("All");
            while (rs.next()) {
                v.add(rs.getString("bank_name") + "-" + rs.getString("account_no"));
            }
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(v));
            pst.close();
            rs.close();
          
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,e);
        } 

    }
    
    
    
   
    private void FillDetailTable() {

        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        if ("".equals(iSearch.getText()) && "All".equals(jComboBox1.getSelectedItem())) {
            try {
                String sql = "Select * from bank_statement ";
               PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Vector v = new Vector();
                v.add(rs.getString("date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("withdrawal"));
                v.add(rs.getString("deposit"));
                v.add(rs.getString("balance"));

                dtm.addRow(v);

                }

                pst.close();
                rs.close();
               
            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, e);

            }

        } else if ("".equals(iSearch.getText()) && !"All".equals(jComboBox1.getSelectedItem())) {

            try {
                String sql = "Select * from bank_statement where Bank='" + jComboBox1.getSelectedItem().toString().split("-")[0] + "' "
                        + "and account_no='" + jComboBox1.getSelectedItem().toString().split("-")[1] + "' ";
               PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                     Vector v = new Vector();
                v.add(rs.getString("date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("withdrawal"));
                v.add(rs.getString("deposit"));
                v.add(rs.getString("balance"));

                dtm.addRow(v);
                }
                
                pst.close();
                rs.close();
            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, e);

            }
        } else if (!"".equals(iSearch.getText()) && "All".equals(jComboBox1.getSelectedItem())) {

            try {
                String sql = "Select * from bank_statement where description like('" + iSearch.getText() + "%%%" + "') ";
               PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                     Vector v = new Vector();
                v.add(rs.getString("date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("withdrawal"));
                v.add(rs.getString("deposit"));
                v.add(rs.getString("balance"));

                dtm.addRow(v);

                }
               pst.close();
               rs.close();
            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, e);

            }

        } else if (!"".equals(iSearch.getText()) && !"All".equals(jComboBox1.getSelectedItem())) {

            try {
                String sql = "Select * from bank_statement where Description like('" + iSearch.getText() + "%%%" + "') "
                        + "and  Bank='" + jComboBox1.getSelectedItem().toString().split("-")[0] + "' "
                        + "and account_no='" + jComboBox1.getSelectedItem().toString().split("-")[1] + "' ";
               PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                     Vector v = new Vector();
                v.add(rs.getString("date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("withdrawal"));
                v.add(rs.getString("deposit"));
                v.add(rs.getString("balance"));

                dtm.addRow(v);
                }
                pst.close();
                rs.close();
            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    private void Total() {

        try {
            double sum2 = 0;
            double sum3 = 0;
           
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                sum2 = sum2 + Double.parseDouble(jTable1.getValueAt(i, 2).toString());
                sum3 = sum3 + Double.parseDouble(jTable1.getValueAt(i, 3).toString());
               
            }

            total_withdrawal.setText(sum2 + "");
            total_deposit.setText(sum3 + "");
         
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
        }

    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        iSearch = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        total_deposit = new javax.swing.JLabel();
        total_withdrawal = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel3MouseReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Close");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Description", "Withdrawal", "Deposit", "Balanace"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionBackground(new java.awt.Color(229, 126, 49));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel8.setText("Search");

        iSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        iSearch.setSelectionColor(new java.awt.Color(229, 126, 49));
        iSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                iSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                iSearchKeyReleased(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(227, 227, 227));
        jPanel5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel5MouseDragged(evt);
            }
        });
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 2, -1, 30));

        jPanel7.setBackground(new java.awt.Color(142, 182, 182));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Bank Statement");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, -5, -1, 40));

        total_deposit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        total_deposit.setForeground(new java.awt.Color(153, 0, 0));
        total_deposit.setText("0");

        total_withdrawal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        total_withdrawal.setForeground(new java.awt.Color(153, 0, 0));
        total_withdrawal.setText("0");

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Bank");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(iSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(total_withdrawal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total_deposit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iSearch)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total_deposit)
                    .addComponent(total_withdrawal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            int selectedRow = jTable1.getSelectedRow();

            Frame ConfirmationDialog = null;
            new ConfirmationDialog(ConfirmationDialog, true, "Dou you really want to delete?","Delete Confirmation").setVisible(true);      
         
            
            if (new ConfirmationDialog(ConfirmationDialog,true).option==true) {

                try {

                    String sql1 = "delete from bank_statement where "
                            + "Date='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0).toString()) + "' "
                            + " and Description='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + "'"
                            + " and Withdrawal='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2).toString()) + "' "
                            + " and Deposit='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3).toString()) + "'";
                   PreparedStatement pst = con.prepareStatement(sql1);
                    pst.executeUpdate();
                    
                   pst.close();
            
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);

                } 
                dtm.removeRow(selectedRow);
                Total();

            }
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void iSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iSearchKeyPressed
        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

        } catch (Exception e) {
        }
    }//GEN-LAST:event_iSearchKeyPressed

    private void iSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iSearchKeyReleased

        try {
            FillDetailTable();
            Total();
        } catch (Exception e) {
        }
        try {
            if (iSearch.getText().isEmpty()) {
                try {
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.setRowCount(0);

                } catch (Exception e) {
                }
                try {
                    FillDetailTable();
                    Total();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_iSearchKeyReleased

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close2.png"))); // NOI18N
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close.png"))); // NOI18N
    }//GEN-LAST:event_jLabel2MouseExited

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jPanel5MouseDragged

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        FillDetailTable();
        Total();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void jPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseReleased
        dispose();
    }//GEN-LAST:event_jPanel3MouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BankStatement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BankStatement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BankStatement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BankStatement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BankStatement dialog = new BankStatement(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    int xMouse;
    int yMouse;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField iSearch;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel total_deposit;
    private javax.swing.JLabel total_withdrawal;
    // End of variables declaration//GEN-END:variables
}
