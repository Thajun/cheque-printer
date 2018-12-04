/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendfiles;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Sanari
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           // Date = sdf.format(Calendar.getInstance().getTime());
            System.out.println(sdf.format(Calendar.getInstance().getTime()));
    }
    
}
