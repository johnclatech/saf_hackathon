/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.ServiceEntry;

import com.johnclatech.engines.DBOperations;
import com.johnclatech.ArraytoTarget.ArrayToTarget;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jkaru
 */
class ArrayEngine {

    static DBOperations dbops = new DBOperations();

    public static HashMap ArrayToTarget(HashMap Mydata) {
        HashMap MessageToChannel = new HashMap();
        HashMap result = new HashMap();
        try {
            MessageToChannel = Mydata;

            if (!Mydata.isEmpty()) {
                String myArray = Mydata.get("array").toString();
                System.out.println("MyString Array is: " + myArray);
                int[] integers = ArrayConverter(myArray);
                String intarray = Arrays.toString(integers);
                if (!intarray.equals("null")) {

                    System.out.println("MyInteger Array is: " + Arrays.toString(integers));
                    result = ArrayToTarget.GetIndextotarget(integers);
                    MessageToChannel.putAll(result);
                    MessageToChannel.put("success: ", true);

                } else {
                    MessageToChannel.put("Status", "01");
                    MessageToChannel.put("success: ", false);
                    MessageToChannel.put("StatusMessage", "could not convert the array provided");
                }

            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, sw);
            MessageToChannel.put("Status", "02");
            MessageToChannel.put("Error Occured: ", "" + sw);
        }
        return MessageToChannel;
    }

    public static HashMap ArrayOperations(HashMap Mydata) {

        HashMap MessageToChannel = (HashMap) Mydata;
        HashMap result = new HashMap();
        int affectedrows = 0;
        try {
            if (!Mydata.isEmpty()) {
                String myArray = Mydata.get("array").toString();
                System.out.println("MyString Array is: " + myArray);
                int[] integers = ArrayConverter(myArray);
                String intarray = Arrays.toString(integers);
                if (!intarray.equals("null")) {

                    System.out.println("MyInteger Array is: " + Arrays.toString(integers));
                    result = ArrayToTarget.GetToComputeOperations(integers, Mydata.get("operation").toString());
                    MessageToChannel.putAll(result);

                    if (result.containsKey("results")) {
                        MessageToChannel.put("success: ", true);

                        String ops_res = MessageToChannel.get("results").toString();
                        String array = MessageToChannel.get("array").toString();
                        String time_stamp = MessageToChannel.get("timestamp").toString();
                        String operation = MessageToChannel.get("operation").toString();
                        //[arraystring] ,[operation] ,[createdon] ,[results]
                        affectedrows = dbops.SaveArrayToDB(array, operation, time_stamp, ops_res);

                        if (affectedrows > 0) {
                            MessageToChannel.put("dbaffected", affectedrows);
                            MessageToChannel.put("Status", "00");
                            MessageToChannel.put("success: ", true);
                            MessageToChannel.put("StatusMessage", "Process completed successfully");
                        } else {
                            MessageToChannel.put("dbaffected", affectedrows);
                            MessageToChannel.put("Status", "01");
                            MessageToChannel.put("success: ", false);
                            MessageToChannel.put("StatusMessage", "Process failed");
                        }

                    }

                } else {
                    MessageToChannel.put("Status", "01");
                    MessageToChannel.put("success: ", false);
                    MessageToChannel.put("StatusMessage", "could not convert the array provided");
                }

            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, sw);
            MessageToChannel.put("Status", "02");
            MessageToChannel.put("Error Occured: ", "" + sw);
        }
        return MessageToChannel;
    }
    
   
    private static int[] ArrayConverter(String input) {
        boolean success = false;
        int[] integerArray = null;
        try {
            int[] result = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray();
//            long[] resul = Arrays.stream(input.split(" ")).mapToLong(Long::parseLong).toArray();
            integerArray = new int[result.length];

            // copy elements from object array to integer array
            for (int i = 0; i < result.length; i++) {
                integerArray[i] = (int) result[i];//incompatible types: long cannot be converted to Integer
//                System.arraycopy(result, 0, integerArray, 0, result.length);//arracopy method
            }
            System.out.println("The converted form of long to int :" + Arrays.toString(integerArray));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, sw);
            System.out.println("com.johnclatech.ServiceEntry.Processor.ArrayConverter(): Error occurred: " + sw);;
        }

        return integerArray;
    }

}
