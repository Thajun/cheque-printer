/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendfiles;

import java.io.File;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author lightway
 */
public class ManageReport {

    public void updateReport( String reportName, int Width,int Height,
            int amountInWordsX,
            int amountInWordsY,
            int payX, int payY,
            int amountX, int amountY,
            int dateX, int dateY,
            int cutX,int cutY,
            int payW, int payH,
            int amountW, int amountH,
            int amountInWordsW,int amountInWordsH,
            
            boolean enablePayee,
            boolean sealState,
            boolean crossState
    ) {
        
        String path = System.getenv("APPDATA") + "\\ChequeBook\\" + reportName + ".jrxml";
        if (new File(path).exists()) {

            try {

                JasperDesign d = JRXmlLoader.load(path);
                d.setPageHeight(Height);
                d.setPageWidth(Width);
                d.setColumnWidth(Width);
                JRElement[] field = d.getSummary().getElements();

                JRElement rtxtCash = field[0];//Cash

                //Amount In Words Field
                JRDesignTextField rtxtAmountInWords = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtAmountInWords");

                rtxtAmountInWords.setX(amountInWordsX);
                rtxtAmountInWords.setY(amountInWordsY);
                rtxtAmountInWords.setWidth(amountInWordsW);
                rtxtAmountInWords.setHeight(amountInWordsH);
                //Pay Field
                JRDesignTextField rtxtPay = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtPay");

                rtxtPay.setX(payX);
                rtxtPay.setY(payY);
                rtxtPay.setWidth(payW);
                rtxtPay.setHeight(payH);

                //Amount
                JRDesignTextField rtxtAmount = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtAmount");

                rtxtAmount.setX(amountX);
                rtxtAmount.setY(amountY);
                rtxtAmount.setWidth(amountW);
                rtxtAmount.setHeight(amountH);

                //Date
                //Amount
                JRDesignTextField rtxtDateOne = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtDateOne");

                JRDesignTextField rtxtDateTwo = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtDateTwo");

                JRDesignTextField rtxtMonthOne = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtMonthOne");

                JRDesignTextField rtxtMonthTwo = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtMonthTwo");

                JRDesignTextField rtxtYearOne = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtYearOne");

                JRDesignTextField rtxtYearTwo = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtYearTwo");

                JRDesignTextField rtxtYearThree = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtYearThree");

                JRDesignTextField rtxtYearFour = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "rtxtYearFour");

                rtxtDateOne.setX(dateX);
                rtxtDateOne.setY(dateY);

                rtxtDateTwo.setX(dateX + 18);
                rtxtDateTwo.setY(dateY);

                rtxtMonthOne.setX(dateX + 36);
                rtxtMonthOne.setY(dateY);

                rtxtMonthTwo.setX(dateX + 54);
                rtxtMonthTwo.setY(dateY);

                rtxtYearOne.setX(dateX + 72);
                rtxtYearOne.setY(dateY);

                rtxtYearTwo.setX(dateX + 90);
                rtxtYearTwo.setY(dateY);

                rtxtYearThree.setX(dateX + 108);
                rtxtYearThree.setY(dateY);

                rtxtYearFour.setX(dateX + 126);
                rtxtYearFour.setY(dateY);

                //Account Payee 
                JRDesignElement rtxtPayee = (JRDesignStaticText) d.
                        getSummary().getElementByKey(
                                "rtxtPayee");

                JRDesignElement lineTop = (JRDesignLine) d.
                        getSummary().getElementByKey(
                                "lineTop");

                JRDesignElement lineBottom = (JRDesignLine) d.
                        getSummary().getElementByKey(
                                "lineBottom");
                
                
                JRDesignElement rtxtCut = (JRDesignStaticText) d.
                        getSummary().getElementByKey(
                                "rtxtCut");
                
                JRDesignElement sealLine = (JRDesignLine) d.
                        getSummary().getElementByKey(
                                "sealLine");
                
                JRDesignElement txtChairman = (JRDesignTextField) d.
                        getSummary().getElementByKey(
                                "txtChairman");
                
                JRDesignElement DCross = (JRDesignImage) d.
                        getSummary().getElementByKey(
                                "dcross");

                 rtxtCut.setX(cutX);
                 rtxtCut.setY(cutY);
                 //System.getenv("APPDATA") + "\\ChequeBook\\Default.jrxml"   
                 
                if (enablePayee == true) {

                   /* rtxtPayee.setX(payeeX);
                    rtxtPayee.setY(payeeY);

                    lineTop.setX(payeeX);
                    lineTop.setY(payeeY + 1);

                    lineBottom.setX(payeeX);
                    lineBottom.setY(payeeY + 13);*/

                    //Hide the Cross cheque
                    rtxtPayee.setWidth(0);
                    rtxtPayee.setHeight(0);
                    lineTop.setWidth(0);
                    lineTop.setHeight(0);

                    lineBottom.setWidth(0);
                    lineBottom.setHeight(0);

                } else {

                    //Show the Cross cheque
                    rtxtPayee.setWidth(97);
                    rtxtPayee.setHeight(16);

                   lineTop.setWidth(97);
                    lineTop.setHeight(1);

                    lineBottom.setWidth(97);
                    lineBottom.setHeight(1);

                }

                
                if(sealState==false){
                sealLine.setWidth(0);
                txtChairman.setWidth(0);
                txtChairman.setHeight(0);
                }else{
                 sealLine.setWidth(140);
                 txtChairman.setWidth(134);
                 txtChairman.setHeight(16);
                }
                
                 if(crossState==false){
               
                DCross.setWidth(0);
                DCross.setHeight(0);
                }else{
                
                 DCross.setWidth(71);
                 DCross.setHeight(64);
                }
                
                
                JRReport jRReport = d;
                
                JasperCompileManager.writeReportToXmlFile(jRReport, path);
                JasperCompileManager.compileReportToFile(path);

//                loadReport(reportName);
            } catch (JRException ex) {
               ex.printStackTrace();
               
            }
        }

    }

    
    
    
}
