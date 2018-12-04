/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chequeprinter;

import backendfiles.DatabaseConnection;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sanari
 */
public final class RegisterBank extends javax.swing.JDialog {

    Connection con = null;
    
    String branch = null;

    public RegisterBank(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        con = DatabaseConnection.Connect();
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        ViewBank();
        jTextField1.setText(generateBankId());
    }

    
    
     public ArrayList<String> GetDefaultBank(){
    
         try {
            String sql = "select * from bank where Type='Default'";
            PreparedStatement pst = con.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
            ArrayList<String> array= new ArrayList();
            Vector v = new Vector();
            while (rs.next()) {
               array.add(rs.getString("bank_name"));
               array.add(rs.getString("account_no"));
                
            }
           pst.close();
           rs.close();
           return array;
        } catch (Exception e) {

            return null;
            // JOptionPane.showMessageDialog(null,e);
        } 
    
    }
    
    
    
    public void ViewBank() {

        try {
            String sql = "Select * from bank";
           PreparedStatement pst = con.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("bank_id"));

                v.add(rs.getString("bank_name"));
                v.add(rs.getString("branch_code"));
                v.add(rs.getString("account_no"));

                dtm.addRow(v);
                branch = rs.getString("branch");
            }
          
            pst.close();
           rs.close();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        } 
    }

    public void BankSearch() {

        try {
            String sql = "Select * from bank where bank_name like('" + iSearch.getText() + "%%%" + "')";
           PreparedStatement pst = con.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("bank_id"));
                v.add(rs.getString("bank_name"));
                v.add(rs.getString("branch_code"));
                v.add(rs.getString("account_no"));

                dtm.addRow(v);
                branch = rs.getString("branch");
            }
           
            pst.close();
           rs.close();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        } 
    }

    private void UpdateBank() {

        try {

            String sql = "update Bank set bank_name='" + jTextField2.getText() + "', "
                    + "branch='" + jTextField3.getText() + "',"
                    + "branch_code='" + jTextField4.getText() + "', "
                    + "account_no='" + jTextField5.getText() + "' where bank_id='" + jTextField1.getText() + "'";

           PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate();
            
            pst.close();
           
            Frame MessageBox = null;
            new MessageBox(MessageBox, true, "Updated Successfully").setVisible(true);
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        } 
    }

    private void RegisterBank() {

        try {

            String sql = "Insert into bank(bank_id,bank_name,branch,branch_code,account_no,type) values(?,?,?,?,?,?)";
           PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, jTextField1.getText());
            pst.setString(2, jTextField2.getText());
            pst.setString(3, jTextField3.getText());
            pst.setString(4, jTextField4.getText());
            pst.setString(5, jTextField5.getText());
            pst.setString(6, "Normal");

            pst.executeUpdate();
            
            pst.close();
        
            Frame MessageBox = null;
            new MessageBox(MessageBox, true, jTextField2.getText() + " Bank Registered Succesfully").setVisible(true);
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } 
    }

    public String generateBankId() {

        Integer id = null;
        String cid = null;
        String final_id = null;

        try {

            PreparedStatement pst1;
            PreparedStatement pst2;
            ResultSet rs1;
            ResultSet rs2;

            String sql1 = "SELECT MAX(id) as ID FROM bank";
            pst1 = con.prepareStatement(sql1);
            rs1 = pst1.executeQuery();

            while (rs1.next()) {
                id = rs1.getInt("id");
            }

             pst1.close();
             rs1.close();
            String sql2 = "SELECT bank_id FROM bank WHERE id= " + id + "";
            pst2 = con.prepareStatement(sql2);
            rs2 = pst2.executeQuery();

            while (rs2.next()) {
                cid = rs2.getString("bank_id");
            }

           
            pst2.close();
           rs2.close();
        
            if (id != 0) {
                String original = cid.split("K")[1];
                int i = Integer.parseInt(original) + 1;

                if (i < 10) {
                    final_id = "BNK000" + i;
                } else if (i >= 10 && i < 100) {
                    final_id = "BNK00" + i;
                } else if (i >= 100 && i < 1000) {
                    final_id = "BNK0" + i;
                } else if (i >= 1000 && i < 10000) {
                    final_id = "BNK" + i;
                }
                
                return final_id;

            } else {
                
                return "BNK0001";
            }
            
            
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException |
                SQLException e) {

            if (e instanceof ArrayIndexOutOfBoundsException) {
                //  log.error("Exception tag --> " + "Split character error");
            } else if (e instanceof NumberFormatException) {
                    //log.error("Exception tag --> "
                //  + "Invalid number found in current id");
            } else if (e instanceof SQLException) {
                    //log.error("Exception tag --> " + "Invalid sql statement "
                // + e.getMessage());
            }
            return null;

        } catch (Exception e) {
            //log.error("Exception tag --> " + "Error");
            return null;
        }

    }

    
    public void CreateReport(){
    
    try {
                String sql = "select * from cheque_design where  profile_name='" + jTextField2.getText() + "'";
              PreparedStatement  pst = con.prepareStatement(sql);
               ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                   
                } else {
                    
                    pst.close();
                    rs.close();
                    InsertConfig();

                 /*   File jrxml_source = new File(System.getenv("APPDATA") + "\\ChequeBook\\Default.jrxml");
                    File jrxml_destination = new File(System.getenv("APPDATA") + "\\ChequeBook\\" + jTextField2.getText() + ".jrxml");
                    copyFile(jrxml_source, jrxml_destination);

                    File jasper_source = new File(System.getenv("APPDATA") + "\\ChequeBook\\Default.jasper");
                    File jasper_destination = new File(System.getenv("APPDATA") + "\\ChequeBook\\" + jTextField2.getText() + ".jasper");
                    copyFile(jasper_source, jasper_destination);*/
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);
            } 
    
    
    }
    
    
     public void DeleteReport(){
    
         //It will check wheher more Bank design is available or not and decieds to delete  
    try {
                String sql = "select * from cheque_design where  profile_name='" +jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()+ "'";
              PreparedStatement  pst = con.prepareStatement(sql);
               ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                   
                } else {
                    
                    pst.close();
                    rs.close();
                    
                   /* File jrxml_file = new File(System.getenv("APPDATA") + "\\ChequeBook\\" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + ".jrxml");
                    File jasper_file = new File(System.getenv("APPDATA") + "\\ChequeBook\\" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + ".jasper");
                    //Delete the report file
                     jrxml_file.delete();
                     jasper_file.delete();
                    */
                    
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);
            } 
    
    
    }
    
  
        public void InsertConfig() {

        try {

            String sql = "INSERT INTO `cheque_design` ("
                    + "`design_id`, "
                    + "`profile_name`,"
                    + "`ac_payee_only_top`, "
                    + "`ac_payee_only_hight`, "
                    + "`pay_top`, "
                    + "`pay_hight`, "
                    + "`rupees_top`, "
                    + "`rupees_hight`, "
                    + "`amount_top`, "
                    + "`amount_hight`, "
                    + "`date_top`, "
                    + "`date_hight`,"
                    + "`pay_w`, "
                    + "`pay_h`, "
                    + "`amount_w`, "
                    + "`amount_h`, "
                    + "`rupees_w`, "
                    + "`rupees_h`,"
                    + "`Height`, "
                    + "`Width`"
                    + ") "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst2 = con.prepareStatement(sql);
            pst2.setString(1, generateId());
            pst2.setString(2, jTextField2.getText());
            pst2.setString(3, "494");
            pst2.setString(4, "57");
            pst2.setString(5, "104");
            pst2.setString(6, "100");
            pst2.setString(7, "90");
            pst2.setString(8, "149");
            pst2.setString(9, "416");
            pst2.setString(10, "119");
            pst2.setString(11, "439");
            pst2.setString(12, "24");
            pst2.setString(13, "444");
            pst2.setString(14, "20");
            pst2.setString(15, "146");
            pst2.setString(16, "43");
            pst2.setString(17, "257");
            pst2.setString(18, "72");
            pst2.setString(19, "252");
            pst2.setString(20, "650");

            pst2.executeUpdate();
            
            pst2.close();
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } 
        //Create Settings
        try {

            String sql = "INSERT INTO `default_settings` ("
                    + "`date_with_year`, "
                    + "`cross_cheque`, "
                    + "`print`,"
                    + "`print_preview`,"
                    + "`profile_name`) "
                    + "VALUES(?,?,?,?,?)";
           PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, 1);
            pst.setInt(2, 0);
            pst.setInt(3, 1);
            pst.setInt(4, 1);
            pst.setString(5, jTextField2.getText());

            pst.executeUpdate();
          
            pst.close();
         
        } catch (Exception e) {
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

                    PreparedStatement pstmt = con.prepareStatement(query);

                    ResultSet r = pstmt.executeQuery();

                    while (r.next()) {
                        id = r.getInt(1);

                    }
                    
                    pstmt.close();
                    r.close();
                    
                    String queryCurrentId
                            = "SELECT design_id FROM cheque_design WHERE rowid =? ";

                    PreparedStatement pstmtId = con.prepareStatement(
                            queryCurrentId);
                    pstmtId.setInt(1, id);

                    ResultSet rss = pstmtId.executeQuery();
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
         
         
          public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile, "rw").getChannel();
            destination = new RandomAccessFile(destFile, "rw").getChannel();

            long position = 0;
            long count = source.size();

            source.transferTo(position, count, destination);
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
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

        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        iSearch = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Bank ID");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(229, 126, 49));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Save");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(36, 36, 36))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("Bank");

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField2.setSelectionColor(new java.awt.Color(229, 126, 49));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Branch Code");

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField4.setSelectionColor(new java.awt.Color(229, 126, 49));

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField3.setSelectionColor(new java.awt.Color(229, 126, 49));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setText("Branch");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Account No");

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField5.setSelectionColor(new java.awt.Color(229, 126, 49));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bank ID", "Bank", "Branch Code", "Account No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
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
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, 23));

        jPanel7.setBackground(new java.awt.Color(142, 182, 182));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Register Bank");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(30, 30, 30))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, -5, -1, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(343, 343, 343))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        jTextField1.setText((jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0).toString()));
        jTextField2.setText((jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()));
        jTextField3.setText(branch);
        jTextField4.setText((jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2).toString()));
        jTextField5.setText((jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3).toString()));
        
       
        jTextField2.setEnabled(false);
       
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            if(!(GetDefaultBank().get(0)+"-"+GetDefaultBank().get(1)).equals(jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()+"-"+jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3).toString())){
            
            int selectedRow = jTable1.getSelectedRow();

             Frame ConfirmationDialog = null;
            new ConfirmationDialog(ConfirmationDialog, true, "Are you sure you want to Delete?","Delete Confirmation").setVisible(true);      
         
            if (new ConfirmationDialog(ConfirmationDialog,true).option==true) {

                try {

                    String sql1 = "delete from bank where "
                            + "bank_id='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0).toString()) + "' "
                            + " and bank_name='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + "'"
                            + " and branch_code='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2).toString()) + "' "
                            + " and account_no='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 3).toString()) + "'";
                   PreparedStatement pst = con.prepareStatement(sql1);
                    pst.executeUpdate();

                     pst.close();
                    jTextField1.setText(generateBankId());

                  
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);

                } 
                
                
                //Delete Report
                
                try {

                    String sql1 = "delete from cheque_design where profile_name='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + "'";
                   PreparedStatement pst = con.prepareStatement(sql1);
                    pst.executeUpdate();

                    pst.close();
          
                     
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);

                } 

                try {

                    String sql1 = "delete from default_settings where profile_name='" + (jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 1).toString()) + "'";
                   PreparedStatement pst = con.prepareStatement(sql1);
                    pst.executeUpdate();

                    pst.close();
           
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);

                } 
                
                DeleteReport();

                dtm.removeRow(selectedRow);

                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");
                jTextField1.setText(generateBankId());
            }
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
            BankSearch();
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
                    ViewBank();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_iSearchKeyReleased

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
      
    }//GEN-LAST:event_jLabel2MouseClicked

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

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void jPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseReleased
       if ("".equals(jTextField2.getText()) || "".equals(jTextField3.getText()) || "".equals(jTextField4.getText()) || "".equals(jTextField3.getText()) || "".equals(jTextField1.getText())) {
            Frame MessageBox = null;
            new MessageBox(MessageBox, true, "Please fill all the fields").setVisible(true);

        } else {

            try {
                String sql = "select * from bank where bank_id = '" + jTextField1.getText() + "'";
               PreparedStatement pst = con.prepareStatement(sql);
               ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                     pst.close();
                     rs.close();
            Frame ConfirmationDialog = null;
            new ConfirmationDialog(ConfirmationDialog, true, "Are you sure you want to Update?","Update Confirmation").setVisible(true);      
         
            if (new ConfirmationDialog(ConfirmationDialog,true).option==true) {

                        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                        dtm.setRowCount(0);
                        UpdateBank();
                        ViewBank();

                    }

                } else {

                     pst.close();
                     rs.close();
                    
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.setRowCount(0);
                    RegisterBank();
                    CreateReport();
                    ViewBank();

                }

             
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jTextField2.setEnabled(true);

            jTextField1.setText(generateBankId());
        }
    }//GEN-LAST:event_jPanel2MouseReleased

    private void jPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseReleased
         dispose();
    }//GEN-LAST:event_jPanel3MouseReleased

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        jTextField1.setText(generateBankId());
        jTextField2.setEnabled(true);
    }//GEN-LAST:event_jPanel1MouseReleased

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
            java.util.logging.Logger.getLogger(RegisterBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegisterBank dialog = new RegisterBank(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
