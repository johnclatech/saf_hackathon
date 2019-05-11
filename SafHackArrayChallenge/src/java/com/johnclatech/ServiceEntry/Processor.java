/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.ServiceEntry;

import com.johnclatech.ArraytoTarget.ArrayToTarget;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
class Processor {

    static DBOperations dbops = new DBOperations();

    public static HashMap process(HashMap Mydata) {
        HashMap MessageToChannel = new HashMap();
        HashMap result = new HashMap();
        try {
            MessageToChannel = Mydata;

            if (!Mydata.isEmpty()) {
                String myArray = Mydata.get("array").toString();
                System.out.println("MyString Array is: " + myArray);
                int[] integers = ArrayConverter(myArray);
                System.out.println("MyInteger Array is: " + Arrays.toString(integers));

                result = ArrayToTarget.addtotarget(integers);
                MessageToChannel.putAll(result);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, sw);
        }
        return MessageToChannel;
    }

    public static HashMap ToOperations(HashMap Mydata) {

        HashMap MessageToChannel = new HashMap();
        HashMap result = new HashMap();

        if (!Mydata.isEmpty()) {
            String myArray = Mydata.get("array").toString();
            System.out.println("MyString Array is: " + myArray);
            int[] integers = ArrayConverter(myArray);
            System.out.println("MyInteger Array is: " + Arrays.toString(integers));

            result = ArrayToTarget.operations(integers, Mydata.get("operation").toString());

            MessageToChannel = Mydata;
            MessageToChannel.putAll(result);

            String ops_res = MessageToChannel.get("results").toString();
            String array = MessageToChannel.get("array").toString();
            String time_stamp = MessageToChannel.get("timestamp").toString();
            String operation = MessageToChannel.get("operation").toString();
            //[arraystring] ,[operation] ,[createdon] ,[results]
            int affectedrows = dbops.updateDB(array, operation, time_stamp, ops_res);

            if (affectedrows > 0) {
                MessageToChannel.put("dbaffected", affectedrows);
            }
            MessageToChannel.put("dbaffected", affectedrows);

        }

        return MessageToChannel;
    }

    private static int[] ArrayConverter(String input) {
        int[] result = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray();

        return result;
    }

}
