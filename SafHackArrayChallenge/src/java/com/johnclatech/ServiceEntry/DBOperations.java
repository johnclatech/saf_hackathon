/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.ServiceEntry;

import Utilities.Logs;
import database.DBFunctions;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class DBOperations {

    private static final String field2 = "2";
    private static final String field3 = "3";
    private static final String field4 = "4";
    Logs logs = new Logs();
    DBFunctions dbf = new DBFunctions();

    //[arraystring] ,[operation] ,[createdon] ,[results]
    public int updateDB(String arraystring, String operation, String createdon, String results) {
        int affectedRows = 0;
        try {
            JSONObject qhm = new JSONObject();
            qhm.clear();
            qhm.put("COLS", "arraystring,operation,createdon,results");
            qhm.put("TABLE", "[Arrays_Operations]");
            qhm.put("VALUES", "'" + arraystring + "','" + operation + "','" + createdon + "','" + results + "'"); //,'" + mothersurname + "','" + favhobby + "'
            affectedRows = dbf.InsertAll(qhm);

            logs.log(Logs.logPreString() + "\nUPDATED  DELIVERED ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }
}
