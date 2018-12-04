/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chequeprinter;

import backendfiles.ManageReport;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author Sanari
 */
public final class ChequeConfig extends javax.swing.JDialog {

    Connection con = null;

    private final ManageReport manageReport = new ManageReport();

    private boolean accountPayeeState = true;
    private LoadWorker worker;
    private LoadWorkerSave workersave;
    private JComponent selectedcomp = null;

    public ChequeConfig(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        con = backendfiles.DatabaseConnection.Connect();

        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        /*ComboBox Selection Color*/
        AccessibleContext ac = profile.getAccessibleContext();
        BasicComboPopup pop = (BasicComboPopup) ac.getAccessibleChild(0);
        JList list = pop.getList();
        list.setSelectionForeground(Color.WHITE);
        list.setSelectionBackground(new java.awt.Color(229, 126, 49));

    }

    public String GetDefaultBank() {

        String ID = null;
        try {
            String sql = "select * from bank where Type='Default'";
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            Vector v = new Vector();
            while (rs.next()) {

                ID = rs.getString("bank_name");
            }
            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
        return ID;
    }

    private void FillcomboBankProfile() {

        try {
            String sql = "select DISTINCT bank_name from bank";
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("bank_name"));
            }
            pst.close();
            rs.close();
            profile.setModel(new javax.swing.DefaultComboBoxModel(v));

        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
    }

    public void CompileReport() {
        try {

            manageReport.updateReport("Default",
                    ChequePanel.getWidth(), ChequePanel.getHeight(),
                    inwords.getX(), inwords.getY(),
                    pay.getX(), pay.getY(),
                    amount.getX(), amount.getY(),
                    date.getX(), date.getY(),
                    cut.getX(), cut.getY(),
                    pay.getWidth(), pay.getHeight(),
                    amount.getWidth(), amount.getHeight(),
                    inwords.getWidth(), inwords.getHeight(),
                    accountPayeeState,true,false);
            // JOptionPane.showMessageDialog(null, "Success");
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public String generateId() {

        Integer id = null;

        if (con == null) {

            System.out.println("Database connection failiure.");
            return null;
        } else {

            String eid = null;
            String final_id = null;
            if (con == null) {
                System.out.println("Database connection failiure.");
                return null;
            } else {
                try {

                    String query = "SELECT MAX(rowid) as ID FROM cheque_design";

                    ResultSet r;
                    PreparedStatement pstmtId;
                    ResultSet rss;
                    PreparedStatement pstmt = con.prepareStatement(query);
                    r = pstmt.executeQuery();
                    while (r.next()) {
                        id = r.getInt(1);

                    }
                    r.close();
                    pstmt.close();
                    String queryCurrentId = "SELECT design_id FROM cheque_design WHERE rowid =? ";
                    pstmtId = con.prepareStatement(queryCurrentId);
                    pstmtId.setInt(1, id);
                    rss = pstmtId.executeQuery();
                    while (rss.next()) {
                        eid = rss.getString("design_id");
                    }

                    pstmtId.close();
                    rss.close();
                    if (id != 0) {
                        String original = eid.split("G")[1];
                        int i = Integer.parseInt(original) + 1;

                        if (i < 10) {
                            final_id = "DSG000" + i;
                        } else if (i >= 10 && i < 100) {
                            final_id = "DSG00" + i;
                        } else if (i >= 100 && i < 1000) {
                            final_id = "DSG0" + i;
                        } else if (i >= 1000) {
                            final_id = "DSG" + i;

                        }
                        return final_id;
                    } else {
                        return "DSG0001";
                    }

                } catch (ArrayIndexOutOfBoundsException | SQLException |
                        NullPointerException e) {

                    if (e instanceof ArrayIndexOutOfBoundsException) {

                        System.out.println("Exception tag --> "
                                + "Invalid entry location for list");

                    } else if (e instanceof SQLException) {

                        System.out.println("Exception tag --> "
                                + "Invalid sql statement " + e);

                    } else if (e instanceof NullPointerException) {

                        System.out.println("Exception tag --> "
                                + "Empty entry for list");

                    }
                    return null;
                } catch (Exception e) {

                    System.out.println("Exception tag --> " + "Error");

                    return null;
                }
            }
        }
    }

    public void FillProfileDetils(String profile_name) {
        try {
            String sql = "select * from cheque_design where profile_name='" + profile_name + "'";
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                designid.setText(rs.getString("design_id"));

                cut.setLocation(rs.getInt("ac_payee_only_top"), rs.getInt("ac_payee_only_hight"));
                date.setLocation(rs.getInt("date_top"), rs.getInt("date_hight"));
                pay.setLocation(rs.getInt("pay_top"), rs.getInt("pay_hight"));
                pay.setLocation(rs.getInt("pay_top"), rs.getInt("pay_hight"));
                inwords.setLocation(rs.getInt("rupees_top"), rs.getInt("rupees_hight"));
                amount.setLocation(rs.getInt("amount_top"), rs.getInt("amount_hight"));

                pay.setSize(rs.getInt("pay_w"), rs.getInt("pay_h"));
                amount.setSize(rs.getInt("amount_w"), rs.getInt("amount_h"));
                inwords.setSize(rs.getInt("rupees_w"), rs.getInt("rupees_h"));
                ChequePanel.setSize(rs.getInt("Width"), rs.getInt("Height"));
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void UpdateConfig() {

        try {

            String sql = "update cheque_design set profile_name='" + profile.getSelectedItem().toString() + "', "
                    + "ac_payee_only_top='" + cut.getX() + "',"
                    + "ac_payee_only_hight='" + cut.getY() + "', "
                    + "pay_top='" + pay.getX() + "', "
                    + "pay_hight='" + pay.getY() + "', "
                    + "rupees_top='" + inwords.getX() + "', "
                    + "rupees_hight='" + inwords.getY() + "', "
                    + "amount_top='" + amount.getX() + "', "
                    + "amount_hight='" + amount.getY() + "', "
                    + "date_top='" + date.getX() + "', "
                    + "date_hight='" + date.getY() + "', "
                    + "pay_w='" + pay.getWidth() + "', "
                    + "pay_h='" + pay.getHeight() + "', "
                    + "amount_w='" + amount.getWidth() + "', "
                    + "amount_h='" + amount.getHeight() + "', "
                    + "rupees_w='" + inwords.getWidth() + "', "
                    + "rupees_h='" + inwords.getHeight() + "',"
                    + "Width='" + ChequePanel.getWidth() + "',"
                    + "Height='" + ChequePanel.getHeight() + "'"
                    + "where design_id='" + designid.getText() + "'";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate();
            pst.close();

            
            Frame MessageBox = null;
            new MessageBox(MessageBox, true, "Updated Successfully").setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    //To make the query quick
    private class LoadWorker extends SwingWorker<Boolean, Void> {

        @Override
        protected void done() {
            try {
                if (get() != null) {
                    // jProgressBar1.setIndeterminate(false);
                }
            } catch (ExecutionException ex) {
                Logger.getLogger(ChequeConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChequeConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        protected Boolean doInBackground() throws SQLException, InterruptedException {
            //  jProgressBar1.setIndeterminate(true);

            FillcomboBankProfile();
            profile.setSelectedItem(GetDefaultBank());
            FillProfileDetils(profile.getSelectedItem().toString());

            return true;
        }
    }

    private class LoadWorkerSave extends SwingWorker<Boolean, Void> {

        @Override
        protected void done() {
            try {
                if (get() != null) {

                    jLabel7.setText("");
                    
                }
            } catch (ExecutionException ex) {
                Logger.getLogger(ChequeConfig.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChequeConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        protected Boolean doInBackground() throws SQLException, InterruptedException {
            Frame ConfirmationDialog = null;
            new ConfirmationDialog(ConfirmationDialog, true, "Are you sure you want to Update?", "Update Confirmation").setVisible(true);
            if (new ConfirmationDialog(ConfirmationDialog, true).option == true) {
                jLabel7.setText("Compiling cheque...");

                CompileReport();
                UpdateConfig();

            }
            return true;
        }
    }

    public void Arrange(java.awt.event.MouseEvent evt) {

        JComponent jc = (JComponent) evt.getSource();
        if (jc.getCursor().getName().startsWith("E") && jc.getX() + 1 + evt.getX() + 1 < ChequePanel.getWidth()) {

            jc.setSize(evt.getX() + 1, jc.getHeight());

        } else if (jc.getCursor().getName().startsWith("S") && jc.getY() + 1 + evt.getY() + 1 < ChequePanel.getHeight()) {
            jc.setSize(jc.getWidth(), evt.getY() + 1);
        } else {
            if (jc.getX() + evt.getX() > 0
                    && jc.getY() + evt.getY() > 0
                    && ChequePanel.getWidth() - jc.getWidth() > jc.getX() + evt.getX()
                    && ChequePanel.getHeight() - jc.getHeight() > jc.getY() + evt.getY()) {
                jc.setLocation(jc.getX() + evt.getX(), jc.getY() + evt.getY());

                pay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                inwords.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                cut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                amount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                jc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 166, 41)));
            }

        }

        SelectedX.setText((int) Math.ceil(jc.getX()/3.7795275590551)  + "");
        SelectedY.setText((int) Math.ceil(jc.getY()/3.7795275590551) + "");
        SelectedW.setText((int) Math.ceil(jc.getWidth()/3.7795275590551) + "");
        SelectedH.setText((int) Math.ceil(jc.getHeight()/3.7795275590551) + "");
        selectedcomp = jc;
    }

    public void ClickedEvent(java.awt.event.MouseEvent evt) {

        
        
        pay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        inwords.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        cut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        amount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        ChequePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        JComponent jc = (JComponent) evt.getSource();
        jc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 166, 41)));

        SelectedX.setText((int) Math.ceil(jc.getX()/3.7795275590551)  + "");
        SelectedY.setText((int) Math.ceil(jc.getY()/3.7795275590551) + "");
        SelectedW.setText((int) Math.ceil(jc.getWidth()/3.7795275590551) + "");
        SelectedH.setText((int) Math.ceil(jc.getHeight()/3.7795275590551) + "");
        
        selectedcomp = jc;
        
        if(selectedcomp==ChequePanel){
        
        SelectedX.setEditable(false);
        SelectedY.setEditable(false);
        SelectedW.setEditable(false);
        SelectedH.setEditable(false);
        }else{
        SelectedX.setEditable(true);
        SelectedY.setEditable(true);
        SelectedW.setEditable(true);
        SelectedH.setEditable(true);
        
        }

    }

    public void Cursor(java.awt.event.MouseEvent evt) {
        JComponent jc = selectedcomp;

        if (null != selectedcomp) {
            if (evt.getX() == jc.getWidth() - 6
                    || evt.getX() == jc.getWidth() - 5
                    || evt.getX() == jc.getWidth() - 4
                    || evt.getX() == jc.getWidth() - 3
                    || evt.getX() == jc.getWidth() - 2
                    || evt.getX() == jc.getWidth() - 1
                    || evt.getX() == jc.getWidth()
                    || evt.getX() == jc.getWidth() + 1) {
                jc.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
            } else if (evt.getY() == jc.getHeight() - 6
                    || evt.getY() == jc.getHeight() - 5
                    || evt.getY() == jc.getHeight() - 4
                    || evt.getY() == jc.getHeight() - 3
                    || evt.getY() == jc.getHeight() - 2
                    || evt.getY() == jc.getHeight() - 1
                    || evt.getY() == jc.getHeight()
                    || evt.getY() == jc.getHeight() + 1) {
                jc.setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
            } else {
                jc.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
            }
        }
    }

      public void ResizePanel(java.awt.event.MouseEvent evt) {

        JComponent jc = (JComponent) evt.getSource();
        if (jc.getCursor().getName().startsWith("E") && jc.getX() + 1 + evt.getX() + 1 < BackgroundPanel.getWidth()) {

            jc.setSize(evt.getX() + 1, jc.getHeight());

        } else if (jc.getCursor().getName().startsWith("S") && jc.getY() + 1 + evt.getY() + 1 < BackgroundPanel.getHeight()) {
            jc.setSize(jc.getWidth(), evt.getY() + 1);
        } else {
            if (jc.getX() + evt.getX() > 0
                    && jc.getY() + evt.getY() > 0
                    && BackgroundPanel.getWidth() - jc.getWidth() > jc.getX() + evt.getX()
                    && BackgroundPanel.getHeight() - jc.getHeight() > jc.getY() + evt.getY()) {
                jc.setLocation(jc.getX() + evt.getX(), jc.getY() + evt.getY());

                pay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                inwords.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                cut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                amount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
                jc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 166, 41)));
            }

        }
        SelectedX.setText((int) Math.ceil(jc.getX()/3.7795275590551)  + "");
        SelectedY.setText((int) Math.ceil(jc.getY()/3.7795275590551) + "");
        SelectedW.setText((int) Math.ceil(jc.getWidth()/3.7795275590551) + "");
        SelectedH.setText((int) Math.ceil(jc.getHeight()/3.7795275590551) + "");
        selectedcomp = jc;
      }
      
    public int MMtoPIXEL(int mm){
    
        int pixelint = (int) Math.ceil(mm*3.7795275590551);
   
    return pixelint;
    }
    
     public int PIXELtoMM(int pixel){
    
        int mmint = (int) Math.ceil(pixel/3.7795275590551);
   
    return mmint;
    }
    
      int xmax;
      int ymax;
     
     public void MaximumSizeDecieder(java.awt.event.MouseEvent evt){
         payeeOnly.getX();
         pay.getX();    
         inwords.getX();
         date.getX();
         cut.getX();
         amount.getX();
     
     }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        SelectedX = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        designid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        profile = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        SelectedY = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        BackgroundPanel = new javax.swing.JPanel();
        ChequePanel = new javax.swing.JPanel();
        pay = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        inwords = new javax.swing.JLabel();
        payeeOnly = new javax.swing.JLabel();
        cut = new javax.swing.JLabel();
        amount = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        SelectedW = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        SelectedH = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(227, 227, 227));
        jPanel4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel4MouseDragged(evt);
            }
        });
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, -1, 23));

        jPanel5.setBackground(new java.awt.Color(142, 182, 182));

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Cheque Configuration");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, -5, -1, 40));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, -1));

        jPanel9.setLayout(null);

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Cross Cheque");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox1);
        jCheckBox1.setBounds(95, 33, 111, 25);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel9.setText("Profile");
        jPanel9.add(jLabel9);
        jLabel9.setBounds(459, 37, 36, 16);

        SelectedX.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SelectedX.setSelectionColor(new java.awt.Color(229, 126, 49));
        SelectedX.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SelectedXKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SelectedXKeyTyped(evt);
            }
        });
        jPanel9.add(SelectedX);
        SelectedX.setBounds(180, 460, 54, 23);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel3MouseReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Close");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel3);
        jPanel3.setBounds(760, 500, 106, 37);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("X :");
        jPanel9.add(jLabel4);
        jLabel4.setBounds(160, 460, 15, 20);

        jPanel1.setBackground(new java.awt.Color(255, 126, 49));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Save");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel1);
        jPanel1.setBounds(650, 500, 103, 37);

        designid.setEditable(false);
        designid.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel9.add(designid);
        designid.setBounds(299, 34, 130, 23);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Design ID");
        jPanel9.add(jLabel3);
        jLabel3.setBounds(229, 37, 54, 16);

        profile.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileActionPerformed(evt);
            }
        });
        jPanel9.add(profile);
        profile.setBounds(509, 35, 225, 21);

        jLabel2.setForeground(new java.awt.Color(0, 153, 153));
        jLabel2.setText("mm");
        jPanel9.add(jLabel2);
        jLabel2.setBounds(240, 460, 21, 20);

        SelectedY.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SelectedY.setSelectionColor(new java.awt.Color(229, 126, 49));
        SelectedY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SelectedYKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SelectedYKeyTyped(evt);
            }
        });
        jPanel9.add(SelectedY);
        SelectedY.setBounds(310, 460, 54, 23);

        jLabel5.setForeground(new java.awt.Color(0, 153, 153));
        jLabel5.setText("mm");
        jPanel9.add(jLabel5);
        jLabel5.setBounds(370, 460, 21, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Y :");
        jPanel9.add(jLabel6);
        jLabel6.setBounds(280, 460, 16, 20);

        BackgroundPanel.setBackground(new java.awt.Color(153, 153, 153));
        BackgroundPanel.setForeground(new java.awt.Color(102, 102, 102));
        BackgroundPanel.setAutoscrolls(true);
        BackgroundPanel.setLayout(null);

        ChequePanel.setBackground(new java.awt.Color(255, 255, 255));
        ChequePanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ChequePanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ChequePanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ChequePanelMouseMoved(evt);
            }
        });
        ChequePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChequePanelMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ChequePanelMouseReleased(evt);
            }
        });
        ChequePanel.setLayout(null);

        pay.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        pay.setText("**Cash**");
        pay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        pay.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        pay.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                payMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                payMouseMoved(evt);
            }
        });
        pay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                payMousePressed(evt);
            }
        });
        ChequePanel.add(pay);
        pay.setBounds(80, 80, 444, 20);

        date.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        date.setText("D  D  M  M  M  Y  Y  Y  Y");
        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        date.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        date.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                dateMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                dateMouseMoved(evt);
            }
        });
        date.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dateMousePressed(evt);
            }
        });
        ChequePanel.add(date);
        date.setBounds(440, 20, 130, 24);

        inwords.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        inwords.setText("       **Amount in words**");
        inwords.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        inwords.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        inwords.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        inwords.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                inwordsMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                inwordsMouseMoved(evt);
            }
        });
        inwords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inwordsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inwordsMousePressed(evt);
            }
        });
        ChequePanel.add(inwords);
        inwords.setBounds(80, 130, 257, 72);

        payeeOnly.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        payeeOnly.setText("A/C PAYEE ONLY");
        payeeOnly.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        payeeOnly.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        payeeOnly.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                payeeOnlyMouseDragged(evt);
            }
        });
        payeeOnly.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                payeeOnlyMousePressed(evt);
            }
        });
        ChequePanel.add(payeeOnly);
        payeeOnly.setBounds(185, 13, 90, 17);

        cut.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        cut.setText("XXXX");
        cut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        cut.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        cut.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cutMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cutMouseMoved(evt);
            }
        });
        cut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cutMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cutMousePressed(evt);
            }
        });
        ChequePanel.add(cut);
        cut.setBounds(490, 50, 30, 24);

        amount.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        amount.setText("**000**");
        amount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        amount.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        amount.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                amountMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                amountMouseMoved(evt);
            }
        });
        amount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                amountMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                amountMousePressed(evt);
            }
        });
        ChequePanel.add(amount);
        amount.setBounds(450, 120, 146, 43);

        BackgroundPanel.add(ChequePanel);
        ChequePanel.setBounds(30, 30, 650, 252);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        BackgroundPanel.add(jLabel7);
        jLabel7.setBounds(610, 350, 207, 20);

        jPanel9.add(BackgroundPanel);
        BackgroundPanel.setBounds(39, 76, 820, 370);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Width :");
        jPanel9.add(jLabel12);
        jLabel12.setBounds(410, 460, 41, 20);

        SelectedW.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SelectedW.setSelectionColor(new java.awt.Color(229, 126, 49));
        SelectedW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SelectedWKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SelectedWKeyTyped(evt);
            }
        });
        jPanel9.add(SelectedW);
        SelectedW.setBounds(460, 460, 54, 23);

        jLabel13.setForeground(new java.awt.Color(0, 153, 153));
        jLabel13.setText("mm");
        jPanel9.add(jLabel13);
        jLabel13.setBounds(520, 460, 21, 20);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Height :");
        jPanel9.add(jLabel14);
        jLabel14.setBounds(550, 460, 44, 20);

        SelectedH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SelectedH.setSelectionColor(new java.awt.Color(229, 126, 49));
        SelectedH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SelectedHKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SelectedHKeyTyped(evt);
            }
        });
        jPanel9.add(SelectedH);
        SelectedH.setBounds(600, 460, 54, 23);

        jLabel20.setForeground(new java.awt.Color(0, 153, 153));
        jLabel20.setText("mm");
        jPanel9.add(jLabel20);
        jLabel20.setBounds(660, 460, 21, 20);

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 900, 560));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked

    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close2.png"))); // NOI18N
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img-close.png"))); // NOI18N
    }//GEN-LAST:event_jLabel1MouseExited

    private void jPanel4MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jPanel4MouseDragged

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanel4MousePressed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if (jCheckBox1.isSelected()) {
            accountPayeeState = false;
            payeeOnly.setVisible(true);
        } else {
            accountPayeeState = true;
            payeeOnly.setVisible(false);
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void SelectedXKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedXKeyReleased
        if ("".equals(SelectedX.getText())) {
            SelectedX.setText("0");
            SelectedX.selectAll();

        } else if ((int) Math.ceil(Integer.parseInt(SelectedX.getText())*3.7795275590551) > ChequePanel.getWidth() - selectedcomp.getWidth()) {
            SelectedX.setText((int) Math.ceil((ChequePanel.getWidth() - selectedcomp.getWidth())/3.7795275590551) + "");
        }
        selectedcomp.setLocation((int) Math.ceil(Integer.parseInt(SelectedX.getText())*3.7795275590551),(int) Math.ceil(Integer.parseInt(SelectedY.getText())*3.7795275590551));
    }//GEN-LAST:event_SelectedXKeyReleased

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked

    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

    }//GEN-LAST:event_jPanel1MouseClicked

    private void profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileActionPerformed
        FillProfileDetils(profile.getSelectedItem().toString());
    }//GEN-LAST:event_profileActionPerformed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased
        workersave = new LoadWorkerSave();
        workersave.execute();
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseReleased
        dispose();
    }//GEN-LAST:event_jPanel3MouseReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        worker = new LoadWorker();
        worker.execute();

    }//GEN-LAST:event_formWindowOpened

    private void SelectedYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedYKeyReleased
        if ("".equals(SelectedY.getText())) {
            SelectedY.setText("0");
            SelectedY.selectAll();
        } else if ((int) Math.ceil(Integer.parseInt(SelectedY.getText())*3.7795275590551) > ChequePanel.getHeight() - selectedcomp.getHeight()) {
            SelectedY.setText((int) Math.ceil((ChequePanel.getHeight() - selectedcomp.getHeight())/3.7795275590551) + "");
        }
        selectedcomp.setLocation((int) Math.ceil(Integer.parseInt(SelectedX.getText())*3.7795275590551), (int) Math.ceil(Integer.parseInt(SelectedY.getText())*3.7795275590551));
    }//GEN-LAST:event_SelectedYKeyReleased

    private void payMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payMouseDragged
        Arrange(evt);
    }//GEN-LAST:event_payMouseDragged

    private void payMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payMousePressed

    }//GEN-LAST:event_payMousePressed

    private void dateMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateMouseDragged
        Arrange(evt);
    }//GEN-LAST:event_dateMouseDragged

    private void dateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateMousePressed

    private void inwordsMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inwordsMouseDragged
        Arrange(evt);
    }//GEN-LAST:event_inwordsMouseDragged

    private void inwordsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inwordsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_inwordsMousePressed

    private void payeeOnlyMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payeeOnlyMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_payeeOnlyMouseDragged

    private void payeeOnlyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payeeOnlyMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_payeeOnlyMousePressed

    private void payMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payMouseClicked
        ClickedEvent(evt);

    }//GEN-LAST:event_payMouseClicked

    private void inwordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inwordsMouseClicked
        ClickedEvent(evt);
    }//GEN-LAST:event_inwordsMouseClicked

    private void cutMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cutMouseDragged
        Arrange(evt);
    }//GEN-LAST:event_cutMouseDragged

    private void cutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cutMouseClicked
        ClickedEvent(evt);
    }//GEN-LAST:event_cutMouseClicked

    private void cutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cutMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cutMousePressed

    private void amountMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amountMouseDragged
        Arrange(evt);
    }//GEN-LAST:event_amountMouseDragged

    private void amountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amountMouseClicked
        ClickedEvent(evt);
    }//GEN-LAST:event_amountMouseClicked

    private void amountMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amountMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountMousePressed

    private void dateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateMouseClicked
        ClickedEvent(evt);
    }//GEN-LAST:event_dateMouseClicked

    private void inwordsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inwordsMouseMoved
        Cursor(evt);
    }//GEN-LAST:event_inwordsMouseMoved

    private void ChequePanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChequePanelMouseDragged
        ResizePanel(evt);
    }//GEN-LAST:event_ChequePanelMouseDragged

    private void ChequePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChequePanelMouseClicked
      ClickedEvent(evt);
    }//GEN-LAST:event_ChequePanelMouseClicked

    private void ChequePanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChequePanelMouseReleased
        pay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        inwords.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        cut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));
        amount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 251)));

    }//GEN-LAST:event_ChequePanelMouseReleased

    private void SelectedWKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedWKeyReleased
        if ("".equals(SelectedW.getText())) {
            SelectedW.setText("1");
            SelectedW.selectAll();

        } else if ((int) Math.ceil(Integer.parseInt(SelectedW.getText())*3.7795275590551) > ChequePanel.getWidth() - selectedcomp.getWidth() + 1) {
            SelectedW.setText((int) Math.ceil((ChequePanel.getWidth() - selectedcomp.getX() - 1)/3.7795275590551) + "");
        }
        selectedcomp.setSize((int) Math.ceil(Integer.parseInt(SelectedW.getText())*3.7795275590551), (int) Math.ceil(Integer.parseInt(SelectedH.getText())*3.7795275590551));
    }//GEN-LAST:event_SelectedWKeyReleased

    private void SelectedHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedHKeyReleased
        if ("".equals(SelectedH.getText())) {
            SelectedH.setText("1");
            SelectedH.selectAll();

        } else if ((int) Math.ceil(Integer.parseInt(SelectedH.getText())*3.7795275590551) > ChequePanel.getWidth() - selectedcomp.getWidth() + 1) {
            SelectedH.setText((int) Math.ceil((ChequePanel.getWidth() - selectedcomp.getX() - 1)/3.7795275590551) + "");
        }
        selectedcomp.setSize((int) Math.ceil(Integer.parseInt(SelectedW.getText())*3.7795275590551), (int) Math.ceil(Integer.parseInt(SelectedH.getText())*3.7795275590551));
    }//GEN-LAST:event_SelectedHKeyReleased

    private void payMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payMouseMoved
        Cursor(evt);
    }//GEN-LAST:event_payMouseMoved

    private void dateMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateMouseMoved

    }//GEN-LAST:event_dateMouseMoved

    private void cutMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cutMouseMoved

    }//GEN-LAST:event_cutMouseMoved

    private void amountMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amountMouseMoved
        Cursor(evt);
    }//GEN-LAST:event_amountMouseMoved

    private void SelectedXKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedXKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACKSPACE || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_SelectedXKeyTyped

    private void SelectedYKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedYKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACKSPACE || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_SelectedYKeyTyped

    private void SelectedWKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedWKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACKSPACE || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_SelectedWKeyTyped

    private void SelectedHKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelectedHKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACKSPACE || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_SelectedHKeyTyped

    private void ChequePanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChequePanelMouseMoved
        Cursor(evt);
    }//GEN-LAST:event_ChequePanelMouseMoved

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
            java.util.logging.Logger.getLogger(ChequeConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChequeConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChequeConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChequeConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChequeConfig dialog = new ChequeConfig(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel BackgroundPanel;
    private javax.swing.JPanel ChequePanel;
    private javax.swing.JTextField SelectedH;
    private javax.swing.JTextField SelectedW;
    private javax.swing.JTextField SelectedX;
    private javax.swing.JTextField SelectedY;
    private javax.swing.JLabel amount;
    private javax.swing.JLabel cut;
    private javax.swing.JLabel date;
    private javax.swing.JTextField designid;
    private javax.swing.JLabel inwords;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel pay;
    private javax.swing.JLabel payeeOnly;
    private javax.swing.JComboBox profile;
    // End of variables declaration//GEN-END:variables
}
