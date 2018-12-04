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
import java.sql.Connection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicComboPopup;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public final class PrintCheque extends javax.swing.JDialog {

    Connection con = null;

    private final ManageReport manageReport = new ManageReport();
    private boolean accountPayeeState = true;
    private boolean SealState = true;
    private boolean crossstate = false;
    private LoadWorker worker;

    public PrintCheque(java.awt.Frame parent, boolean modal) {
        super(parent,modal);

        initComponents();
        setLocationRelativeTo(null);

        Calendar c = Calendar.getInstance();
        jDateChooser1.setDate(c.getTime());
        con = DatabaseConnection.Connect();

        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        
    
    AccessibleContext ac = profile.getAccessibleContext();
    BasicComboPopup pop = (BasicComboPopup) ac.getAccessibleChild(0);
    JList list = pop.getList();
    list.setSelectionForeground(Color.WHITE);
    list.setSelectionBackground(new java.awt.Color(229, 126, 49));
    
    AutoCompleteDecorator.decorate(payee);
    AccessibleContext ac2 = payee.getAccessibleContext();
    BasicComboPopup pop2 = (BasicComboPopup) ac2.getAccessibleChild(0);
    JList list2 = pop2.getList();
    list2.setSelectionForeground(Color.WHITE);
    list2.setSelectionBackground(new java.awt.Color(229, 126, 49));
    
    
    AutoCompleteDecorator.decorate(txt_description);
    AccessibleContext ac3 = txt_description.getAccessibleContext();
    BasicComboPopup pop3 = (BasicComboPopup) ac3.getAccessibleChild(0);
    JList list3 = pop3.getList();
    list3.setSelectionForeground(Color.WHITE);
    list3.setSelectionBackground(new java.awt.Color(229, 126, 49));
    
    FillDescription();
    
    txt_description.setSelectedItem("");
    
    }

   
    
    public String GetFractionalUnit() {
        String fru=null;
        try {
            String sql = "select * from Login where Id='1'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                fru = rs.getString("Fractional_Unit");

            }

            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
        return fru;
    }
    
    
    private void FillCustomers() {
    
        try {
            String sql = "select * from Customer";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("Name"));
            }
            payee.setModel(new javax.swing.DefaultComboBoxModel(v));

            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
    }
    
    
    private void FillDescription() {
    
        try {
            String sql = "select * from Description";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector v = new Vector();
            
            while (rs.next()) {
                v.add(rs.getString("Content"));
            }
            txt_description.setModel(new javax.swing.DefaultComboBoxModel(v));

            pst.close();
            rs.close();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    
    
    private void InsertIntoDescription() {
    
        try {
            String sql = "select * from Description where Content like '"+txt_description.getSelectedItem()+"'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

          
            if (!rs.next()) {
             pst.close();
            rs.close();
            
            
            String sql2 = "Insert into Description(Content) values(?)";
            PreparedStatement pst2 = con.prepareStatement(sql2);

            pst2.setString(1, txt_description.getSelectedItem().toString());
            pst2.executeUpdate();
            pst2.close();

            
            }
           
           
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    public ArrayList<String> GetDefaultBank() {
        ArrayList<String> array = new ArrayList();
        try {
            String sql = "select * from bank where Type='Default'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                array.add(rs.getString("bank_name"));
                array.add(rs.getString("account_no"));

            }

            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
        return array;
    }

    private void FillcomboDefaultProfile() {
        /*
         String sql = "select bank.bank_name,bank.account_no,cheque_design.profile_name"
         + " from bank INNER JOIN cheque_design ON bank.bank_name=cheque_design.profile_name";
        
         */

        try {
            String sql = "select * from bank";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("bank_name") + "-" + rs.getString("account_no"));
            }
            profile.setModel(new javax.swing.DefaultComboBoxModel(v));

            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
    }

    
    
    public void PrintVoucher(){

        String Date = null;
        try {
            
          
           
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date = sdf.format(Calendar.getInstance().getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
            Connection conn = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
             String reportSource = System.getenv("APPDATA") + "\\ChequeBook\\ChequeVoucher.jrxml";
            JasperReport jasperreport = JasperCompileManager.compileReport(reportSource);

            params.put("Payee", payee.getSelectedItem());
            params.put("Date", Date);
            params.put("Words", convertToWords(txt_amount.getText()));
            params.put("Amount", Double.parseDouble(txt_amount.getText()));
            params.put("Bank", profile.getSelectedItem().toString().split("-")[0]);
            params.put("Description", txt_description.getSelectedItem());
            params.put("ChequeNo", txt_chequeNo.getText());
            params.put("Company", company.getSelectedItem().toString());
            JasperPrint jp = JasperFillManager.fillReport(jasperreport, params, conn);

            JasperViewer.viewReport(jp, false);
            //JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
    
    public void CompileReportWithSettings() {
        try {

            String sql = "select * from cheque_design where profile_name='" + profile.getSelectedItem().toString().split("-")[0] + "'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                manageReport.updateReport("Default",
                        rs.getInt("Width"),rs.getInt("Height"),
                        rs.getInt("rupees_top"), rs.getInt("rupees_hight"),
                        rs.getInt("pay_top"), rs.getInt("pay_hight"),
                        rs.getInt("amount_top"), rs.getInt("amount_hight"),
                        rs.getInt("date_top"), rs.getInt("date_hight"),
                        rs.getInt("ac_payee_only_top"), rs.getInt("ac_payee_only_hight"),
                        rs.getInt("pay_w"), rs.getInt("pay_h"),
                        rs.getInt("amount_w"), rs.getInt("amount_h"),
                        rs.getInt("rupees_w"), rs.getInt("rupees_h"),
                        accountPayeeState,SealState,crossstate);
            }
            // JOptionPane.showMessageDialog(null, "Success");

            pst.close();
            rs.close();
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }

    public void SelectSettings() {

        try {
            String sql = "Select * from default_settings where profile_name='" + profile.getSelectedItem().toString().split("-")[0] + "'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getInt("cross_cheque") == 1) {
                    crosscheque_check.setSelected(true);
                } else {
                    crosscheque_check.setSelected(false);
                }

                if (rs.getInt("date_with_year") == 1) {
                    datewithyear_ckeck.setSelected(true);
                } else {
                    datewithyear_ckeck.setSelected(false);
                }

                if (rs.getInt("print") == 1) {
                    print_check.setSelected(true);
                } else {
                    print_check.setSelected(false);
                }

                if (rs.getInt("print_preview") == 1) {
                    preview_check.setSelected(true);
                } else {
                    preview_check.setSelected(false);
                }

            }

            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }

    }

    public void Print() {
        String fileName ="Default";
        String Date = null;
        try {
            java.util.Date d = jDateChooser1.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date = sdf.format(d);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] parts = Date.split("-");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        String Y1 = part1.substring(0, 1);
        String Y2 = part1.substring(1, 2);
        String Y3 = part1.substring(2, 3);
        String Y4 = part1.substring(3, 4);

        String M1 = part2.substring(0, 1);
        String M2 = part2.substring(1, 2);

        String D1 = part3.substring(0, 1);
        String D2 = part3.substring(1, 2);

        HashMap param = new HashMap();
        if("YES".equals(seal.getSelectedItem())){
        param.put("Company", company.getSelectedItem());
        }else{
        param.put("Company", "");
        }
        
         if("KINGSWAY FOOD PRODUCTS (PVT) LTD".equals(company.getSelectedItem())){
        param.put("role", "Chairman");
        }else{
        param.put("role", "Chairman/MD");
        }
        if(cashtick.isSelected()==true){
            param.put("pay", "**Cash**");
        }else{ 
        param.put("pay", "**" + payee.getSelectedItem() + "**");
        }
        param.put("amount", Double.parseDouble(txt_amount.getText()));
        param.put("rupees", "       **" + convertToWords(txt_amount.getText())
                + "**");
        
        if(datetick.isSelected()==false){
            param.put("D1", "");
            param.put("D2", "");
            param.put("M1", "");
            param.put("M2", "");
            param.put("Y1", "");
            param.put("Y2", "");
            param.put("Y3", "");
            param.put("Y4", "");
            
        }else if (!datewithyear_ckeck.isSelected()) {
            param.put("D1", D1);
            param.put("D2", D2);
            param.put("M1", M1);
            param.put("M2", M2);
            param.put("Y1", "");
            param.put("Y2", "");
            param.put("Y3", Y3);
            param.put("Y4", Y4);
        } else {
            param.put("D1", D1);
            param.put("D2", D2);
            param.put("M1", M1);
            param.put("M2", M2);
            param.put("Y1", Y1);
            param.put("Y2", Y2);
            param.put("Y3", Y3);
            param.put("Y4", Y4);
        }
        //String path = new File(reportPath).getAbsolutePath();
        if (print_check.isSelected() == true && preview_check.isSelected() == false) {
             Frame ConfirmationDialog = null;
        new ConfirmationDialog(ConfirmationDialog, true, "Are you sure you want to Print the cheque?", "Print Confirmation").setVisible(true);

        if (new ConfirmationDialog(ConfirmationDialog, true).option == true) {
            InsertToHistory();
            InsertToBankStatement();
            directPrint(fileName, param);
            
             new ConfirmationDialog(ConfirmationDialog, true, "Do you want to print voucher?", "Voucher Print Confirmation").setVisible(true);

                    if (new ConfirmationDialog(ConfirmationDialog, true).option == true) {

                        PrintVoucher();
                    }
                    
          InsertIntoDescription();          
        }
        } else if (print_check.isSelected() == false && preview_check.isSelected() == true ) {
            Frame Preview = null;
            new Preview(fileName, param,Preview,true).setVisible(true);
        } else if ( print_check.isSelected() == true && preview_check.isSelected() == true ) {
             Frame ConfirmationDialog = null;
        new ConfirmationDialog(ConfirmationDialog, true, "Are you sure you want to Print  the cheque?", "Print Confirmation").setVisible(true);

        if (new ConfirmationDialog(ConfirmationDialog, true).option == true) {
            InsertToHistory();
            InsertToBankStatement();

            directPrint(fileName, param);

          Frame Preview = null;
          new Preview(fileName, param,Preview,true).setVisible(true);
          
           new ConfirmationDialog(ConfirmationDialog, true, "Do you want to print voucher?", "Voucher Print Confirmation").setVisible(true);

                    if (new ConfirmationDialog(ConfirmationDialog, true).option == true) {

                        PrintVoucher();
                    }
        }
        
        InsertIntoDescription();
        }

    }

    private String convertToWords(String value) {

        double amount = 0;
        double cents = 0;
        String[] valueTxt = null;
        String amountTxt = null;
        String inWords = null;
        String centsTxt = null;

        if (value.contains(".")) {

            valueTxt = value.split("\\.");

            centsTxt = valueTxt[1];
            amountTxt = valueTxt[0];
        }

        if (amountTxt != null) {
            amount = Double.parseDouble(amountTxt);
        }

        if (centsTxt != null) {
            cents = Double.parseDouble(centsTxt);

        }

        if (cents != 0) {
            
            inWords = EnglishNumberToWords.convert((long) amount) + " And "
                    + EnglishNumberToWords.convert((long) cents)
                    + " "+Pluralize(GetFractionalUnit())+" Only.";
            
        } else {

            inWords = EnglishNumberToWords.convert(
                    (long) Double.parseDouble(value)) + " Only";

        }

        return inWords;
    }

    public void directPrint(String fileName, HashMap parameter) {
        System.out.println("8");

        Connection conn = null;
        try {

            String report = System.getenv("APPDATA") + "\\ChequeBook\\" + fileName + ".jrxml";
            JasperReport jasperreport = JasperCompileManager.compileReport(report);

            /* JasperPrint print = JasperFillManager.
             fillReport(".//Reports//" + fileName + ".jrxml", parameter);

             JasperPrintManager.printReport(jasperReport, false);*/
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperreport, parameter, conn);
//            JasperViewer.viewReport(jasperPrint, false);
            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InsertToHistory() {

        String Date = null;
        try {
            java.util.Date d = jDateChooser1.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date = sdf.format(d);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String sql = "Insert into cheque_log(description,pay,date,amount,profile,account_no,cross_cheque,cheque_no) values(?,?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, txt_description.getSelectedItem().toString());
            pst.setString(2, payee.getSelectedItem().toString());
            pst.setString(3, Date);
            pst.setString(4, txt_amount.getText());
            pst.setString(5, profile.getSelectedItem().toString().split("-")[0]);
            pst.setString(6, profile.getSelectedItem().toString().split("-")[1]);
            if (crosscheque_check.isSelected()) {
                pst.setString(7, "YES");
            } else {
                pst.setString(7, "NO");                
            }
            pst.setString(8, txt_chequeNo.getText());

            pst.executeUpdate();
            // new MessageBox(jTextField2.getText()+" Bank Registered Succesfully").setVisible(true);

            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void InsertToBankStatement() {

        String Date = null;
        try {
            java.util.Date d = jDateChooser1.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date = sdf.format(d);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String sql1 = "Insert into bank_statement(Date,Description,Withdrawal,Deposit,Balance,Bank,account_no) values(?,?,?,?,?,?,?)";
            PreparedStatement pst1 = con.prepareStatement(sql1);

            pst1.setString(1, Date);
            pst1.setString(2, txt_chequeNo.getText());
            pst1.setString(3, txt_amount.getText());
            pst1.setString(4, "0");

            double newbalance = Double.parseDouble(GetBankBalance()) - Double.parseDouble(txt_amount.getText());
            System.out.println(newbalance + "");
            pst1.setString(5, newbalance + "");
            pst1.setString(6, profile.getSelectedItem().toString().split("-")[0]);
            pst1.setString(7, profile.getSelectedItem().toString().split("-")[1]);

            pst1.executeUpdate();
            // new MessageBox(jTextField2.getText()+" Bank Registered Succesfully").setVisible(true);

            pst1.close();

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private String GetBankBalance() {
        String balance = null;
        try {
            String sql1 = "select balance from bank_statement where Bank='" + profile.getSelectedItem().toString().split("-")[0] + "' and account_no='" + profile.getSelectedItem().toString().split("-")[1] + "' ORDER BY id DESC LIMIT 1 ";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            ResultSet rs1 = pst1.executeQuery();

            if (rs1.next()) {
                balance = rs1.getString("Balance");
            } else {

                balance = "0";
            }

            pst1.close();
            rs1.close();
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null,e);
        }
        return balance;
    }

    //To make the query quick
    private class LoadWorker extends SwingWorker<Boolean, Void> {

        protected Boolean doInBackground() throws SQLException, InterruptedException {
           // jProgressBar1.setIndeterminate(true);

            FillcomboDefaultProfile();
            FillCustomers();
            profile.setSelectedItem(GetDefaultBank().get(0) + "-" + GetDefaultBank().get(1));
            SelectSettings();

            if (crosscheque_check.isSelected()) {
                accountPayeeState = false;
            } else {
                accountPayeeState = true;
            }

            return true;
        }
    }

    
    
    public boolean CheckAvailability(){
    boolean available=false;
     try {
            String sql = "select * from bank_statement where bank='" + profile.getSelectedItem().toString().split("-")[0] + "' "
                    + "and account_no='" + profile.getSelectedItem().toString().split("-")[1] + "' ORDER BY Id DESC LIMIT 1;";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

         
            while (rs.next()) {
                
              
                double balance =Double.parseDouble(rs.getString("Balance"));
                  double typed =Double.parseDouble(txt_amount.getText());
                  
                  if(balance>typed){
                  
                  available=true;
                  }
            }
          
            pst.close();
            rs.close();
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }
    return available;
    }
    
    
    
    
    
    public String Pluralize(String singular)
{
  
            // Handle ending with "o" (if preceeded by a consonant, end with -es, otherwise -s: Potatoes and Radios)
            if (singular.endsWith("o"))
            {
                return singular + "es";
            }
            // Handle ending with "y" (if preceeded by a consonant, end with -ies, otherwise -s: Companies and Trays)
            if (singular.endsWith("y"))
            {
                return singular.substring(0, singular.length() - 1) + "ies";
            }
            // Ends with a whistling sound: boxes, buzzes, churches, passes
            if (singular.endsWith("s") || singular.endsWith("sh") || singular.endsWith("ch") || singular.endsWith("x") || singular.endsWith("z"))
            {
                return singular + "es";
            }
            return singular + "s";

    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_chequeNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        profile = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        txt_amount = new javax.swing.JTextField();
        crosscheque_check = new javax.swing.JCheckBox();
        datewithyear_ckeck = new javax.swing.JCheckBox();
        print_check = new javax.swing.JCheckBox();
        preview_check = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        payee = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        company = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        seal = new javax.swing.JComboBox();
        Dcrossstate = new javax.swing.JCheckBox();
        txt_description = new javax.swing.JComboBox();
        cashtick = new javax.swing.JCheckBox();
        datetick = new javax.swing.JCheckBox();

        jTextField3.setText("jTextField3");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Cheque No.");

        txt_chequeNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_chequeNo.setSelectionColor(new java.awt.Color(229, 126, 49));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Pay");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("Date");

        jDateChooser1.setDateFormatString("yyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Bank account");

        profile.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        profile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                profileItemStateChanged(evt);
            }
        });
        profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Amount");

        txt_amount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_amount.setText("0");
        txt_amount.setSelectionColor(new java.awt.Color(229, 126, 49));
        txt_amount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_amountMouseClicked(evt);
            }
        });
        txt_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_amountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_amountKeyTyped(evt);
            }
        });

        crosscheque_check.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        crosscheque_check.setText("Payee only");
        crosscheque_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crosscheque_checkActionPerformed(evt);
            }
        });

        datewithyear_ckeck.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        datewithyear_ckeck.setText("Date With Year");

        print_check.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        print_check.setText("Print");

        preview_check.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        preview_check.setText("Print Preview");

        jPanel1.setBackground(new java.awt.Color(229, 126, 49));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Print");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(229, 126, 49));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Clear");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel9)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Close");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel10)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, -1, 23));

        jPanel5.setBackground(new java.awt.Color(142, 182, 182));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Print Cheque");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, -5, -1, 40));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel11.setText("Description");

        jButton1.setText("Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        payee.setEditable(true);
        payee.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        payee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                payeeItemStateChanged(evt);
            }
        });
        payee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payeeActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel12.setText("Company ");

        company.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KINGSWAY FOOD PRODUCTS (PVT) LTD", "KINGSWAY ASSOCIATES (PVT) LTD" }));
        company.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                companyItemStateChanged(evt);
            }
        });
        company.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel13.setText("Seal");

        seal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        seal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "YES", "NO" }));
        seal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sealItemStateChanged(evt);
            }
        });
        seal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sealActionPerformed(evt);
            }
        });

        Dcrossstate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Dcrossstate.setText("Cross");
        Dcrossstate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DcrossstateActionPerformed(evt);
            }
        });

        txt_description.setEditable(true);
        txt_description.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_description.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                txt_descriptionItemStateChanged(evt);
            }
        });
        txt_description.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_descriptionActionPerformed(evt);
            }
        });

        cashtick.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cashtick.setSelected(true);
        cashtick.setText("Cash");

        datetick.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        datetick.setSelected(true);
        datetick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datetickActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profile, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(crosscheque_check)
                            .addComponent(print_check))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(preview_check)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Dcrossstate)
                                .addGap(10, 10, 10)
                                .addComponent(datewithyear_ckeck))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(company, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(datetick)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(40, 40, 40)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_amount))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cashtick, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(payee, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txt_chequeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(seal, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txt_description, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_description, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(seal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_chequeNo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(payee, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashtick))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datetick, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profile, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(crosscheque_check)
                            .addComponent(Dcrossstate)
                            .addComponent(datewithyear_ckeck))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(print_check)
                            .addComponent(preview_check))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(company, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void profileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_profileItemStateChanged

    }//GEN-LAST:event_profileItemStateChanged

    private void profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileActionPerformed
        try {
            String sql = "select * from cheque_design where profile_name='"+profile.getSelectedItem().toString().split("-")[0]+"'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector v = new Vector();
            if (rs.next()) {
                
                pst.close();
                rs.close();
                jPanel1.setEnabled(true);
                crosscheque_check.setEnabled(true);
                datewithyear_ckeck.setEnabled(true);
                print_check.setEnabled(true);
                preview_check.setEnabled(true);
                SelectSettings();

                if (crosscheque_check.isSelected()) {
                    accountPayeeState = false;
                } else {
                    accountPayeeState = true;
                }
                CompileReportWithSettings();

            } else {
                
                pst.close();
                rs.close();
                jPanel1.setEnabled(false);
                crosscheque_check.setEnabled(false);
                datewithyear_ckeck.setEnabled(false);
                print_check.setEnabled(false);
                preview_check.setEnabled(false);

            }

            
        } catch (Exception e) {

            // JOptionPane.showMessageDialog(null,e);
        }


    }//GEN-LAST:event_profileActionPerformed

    private void txt_amountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_amountMouseClicked
        txt_amount.selectAll();
    }//GEN-LAST:event_txt_amountMouseClicked

    private void txt_amountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_amountKeyReleased
        if ("".equals(txt_amount.getText())) {
            txt_amount.setText("0.00");
            txt_amount.selectAll();
        }
    }//GEN-LAST:event_txt_amountKeyReleased

    private void crosscheque_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crosscheque_checkActionPerformed
        if (crosscheque_check.isSelected()) {
            accountPayeeState = false;
        } else {
            accountPayeeState = true;
        }
        CompileReportWithSettings();
    }//GEN-LAST:event_crosscheque_checkActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked


    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        jPanel1.setBackground(new java.awt.Color(255, 140, 54));
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseExited
        jPanel1.setBackground(new java.awt.Color(229, 126, 49));
    }//GEN-LAST:event_jPanel1MouseExited

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked

    }//GEN-LAST:event_jPanel3MouseClicked

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

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased

        if (jPanel1.isEnabled() == true) {

           // if (CheckAvailability() == true) {
               
           // } else {

                    Print();

                    

                //}

          
            txt_chequeNo.setText(null);
            payee.setSelectedIndex(0);
            txt_amount.setText("0");
            txt_chequeNo.setText("");
            
            FillDescription();

        }
       
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jPanel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseReleased
        dispose();
    }//GEN-LAST:event_jPanel3MouseReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        worker = new LoadWorker();
        worker.execute();

        // CompileReportWithSettings();
    }//GEN-LAST:event_formWindowOpened

    private void txt_amountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_amountKeyTyped
         char c=evt.getKeyChar(); 
        if(Character.isLetter(c)&&!evt.isAltDown()){ 
            evt.consume(); 
        } 
    }//GEN-LAST:event_txt_amountKeyTyped

    private void jPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseReleased
       txt_chequeNo.setText(null);
       payee.setSelectedIndex(0);
       txt_amount.setText("0");
       txt_chequeNo.setText("");
    }//GEN-LAST:event_jPanel2MouseReleased

    private void payeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_payeeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_payeeItemStateChanged

    private void payeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payeeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     Frame CreateCustomer = null;
        
        new CreateCustomer(CreateCustomer, true).setVisible(true);

        FillCustomers();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void companyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_companyItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_companyItemStateChanged

    private void companyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_companyActionPerformed

    private void sealItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sealItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sealItemStateChanged

    private void sealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sealActionPerformed
          if ("NO".equals(seal.getSelectedItem())) {
            SealState = false;
            company.setEnabled(false);
        } else {
            SealState = true;
            company.setEnabled(true);
        }
        CompileReportWithSettings();
    }//GEN-LAST:event_sealActionPerformed

    private void DcrossstateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DcrossstateActionPerformed
       if (Dcrossstate.isSelected()) {
            crossstate = true;
        } else {
            crossstate = false;
        }
        CompileReportWithSettings();
    }//GEN-LAST:event_DcrossstateActionPerformed

    private void txt_descriptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_txt_descriptionItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_descriptionItemStateChanged

    private void txt_descriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_descriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_descriptionActionPerformed

    private void datetickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datetickActionPerformed
        if(datetick.isSelected()==true){
        
        jDateChooser1.setEnabled(true);
        
        }else{
        jDateChooser1.setEnabled(false);
        }
    }//GEN-LAST:event_datetickActionPerformed

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
            java.util.logging.Logger.getLogger(PrintCheque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrintCheque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            //         java.util.logging.Logger.getLogger(Register_customer.class.getName()).log(jtxt_accnogging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrintCheque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PrintCheque dialog = new PrintCheque(new javax.swing.JFrame(),true);
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
    private javax.swing.JCheckBox Dcrossstate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cashtick;
    private javax.swing.JComboBox company;
    private javax.swing.JCheckBox crosscheque_check;
    private javax.swing.JCheckBox datetick;
    private javax.swing.JCheckBox datewithyear_ckeck;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JComboBox payee;
    private javax.swing.JCheckBox preview_check;
    private javax.swing.JCheckBox print_check;
    private javax.swing.JComboBox profile;
    private javax.swing.JComboBox seal;
    private javax.swing.JTextField txt_amount;
    private javax.swing.JTextField txt_chequeNo;
    private javax.swing.JComboBox txt_description;
    // End of variables declaration//GEN-END:variables

}
