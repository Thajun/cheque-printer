/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backendfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Silencer
 */
public class ReportJasper {

    public static void printInvoice(String filename, HashMap<String, Object> params) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(filename);
            String dbFile = System.getenv("APPDATA") + "\\ChequeBook\\Database\\ChequeBook.db";
         Class.forName("org.sqlite.JDBC");
         Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbFile+"");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
            JasperViewer.viewReport(jasperPrint, false);
           //JasperPrintManager.printReport(jasperPrint, false);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
