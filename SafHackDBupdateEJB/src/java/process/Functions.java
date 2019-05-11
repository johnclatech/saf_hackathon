/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import Utilities.Logs;
import database.DBFunctions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class Functions {

    DBFunctions dbf = new DBFunctions();
    Logs logs = new Logs();
    int count = 0;
    String field2 = "2";
    String field3 = "3";
    String field4 = "4";
    String failcode = "010";
    String successcode = "00";
    
    public JSONObject UpdateLoanReversal(HashMap Codesdata) {
        JSONObject columnsVal = (JSONObject) Codesdata;
        try {
            HashMap lhm = new HashMap();
            lhm.clear();
            lhm.put("SET", "Lastvisiteddate = 'WASHERE'");
                    lhm.put("TABLE", "[Customers]");
                    lhm.put("WHERE", "IDNumber = '"+Codesdata.get("idnumber")+"' ");

                    int activit = dbf.Update(lhm);

            int activity = dbf.Update(lhm);
            if (activity > 0) {

        
                        System.out.println(" UPDATE Successful!!");
                        columnsVal.put("description", " Successful!!");
                        columnsVal.put("responsecode", successcode);
                        logs.log(Logs.logPreString()+"\n" + columnsVal.get("description"), "", field2);
                    }
                
             

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        return columnsVal;
    }

}
