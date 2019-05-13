/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.ArraytoTarget;

import com.johnclatech.ServiceEntry.IncomingRequest;
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
public class ArrayToTarget {

    public static HashMap GetIndextotarget(int[] arry) throws Exception {

        HashMap result = new HashMap();
        int target = 9;
        result.put("settarget", target);

        int arr_size = arry.length;
        try {
            result = hasArrayTwoCandidates(arry, arr_size, target);

            if (!result.isEmpty()) {
                int[] indices = new int[2];
                indices[0] = Integer.parseInt(result.get("index1").toString());
                indices[1] = Integer.parseInt(result.get("index2").toString());
                result.put("Status", "00");
                result.put("StatusMessage", "Array has two elements with given sum");
                result.put("Result", Arrays.toString(indices));

                System.out.println("Array has two elements with given sum: " + Arrays.toString(indices));
            } else {
                result.put("Status", "01");
                result.put("StatusMessage", "Array doesn't have two elements with given sum");
                result.put("Result", "no indices found");

                System.out.println("Array doesn't have "
                        + "two elements with given sum");
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            result.put("Status", "02");
            result.put("Error Occured: ", "" + sw);
        }
        return result;
    }

    public static HashMap GetToComputeOperations(int[] arry, String operation) {

        HashMap result = new HashMap();
        HashMap data = new HashMap();

        int arr_size = arry.length;

        try {
            data = compute(arry, arr_size, operation);

            result.putAll(data);

            if (!result.isEmpty()) {
                result.put("Status", "00");
                result.put("StatusMessage", "Array has been operated on succefully");

                System.out.println("Array has two elements operated on");
            } else {

                result.put("Status", "01");
                result.put("StatusMessage", "Array doesn't have two elements to be operated on");

                System.out.println("Array doesn't have "
                        + "two elements to be operated on");
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            result.put("Status", "02");
            result.put("Error Occured: ", "" + sw);
            
        }

        return result;
    }

    static HashMap hasArrayTwoCandidates(int A[], int arr_size, int sum) throws Exception {
        HashMap result = new HashMap();
        int l, r;
        try {
            /* Sort the elements */
            Arrays.sort(A);

            /* Now look for the two candidates in the sorted array*/
            l = 0;
            r = arr_size - 1;
            while (l < r) {
                if (sum == (A[l] + A[r])) {
                    result.put("index1", l);
                    result.put("index2", r);
                    r--;
                } else if (A[l] + A[r] < sum) {
                    l++;
                } else // A[i] + A[j] > sum 
                {
                    r--;
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            result.put("Status", "02");
            result.put("Error Occured: ", "" + sw);
        }
        return result;
    }

    static HashMap compute(int A[], int arr_size, String operation) {
        HashMap result = new HashMap();
        int l, r;
        try {
            /* Sort the elements */
            Arrays.sort(A);
            /* Now look for the operation in the sorted array*/
            l = 0;
            r = arr_size - 1;
            while (l < r) {
                if (("sum").equalsIgnoreCase(operation)) {
                    long num1 = A[l];
                    long num2 = A[r];
                    long sum = (num2 + num1);
                    r--;
                    result.put("results", sum);
                } else if (("divide").equalsIgnoreCase(operation)) {
                    long num1 = A[l];
                    long num2 = A[r];
                    double division = ((double)num2 / (double)num1);
                    String div = String.format("%.2f", division);
//                    double new_value = Double.valueOf(div);
                    r--;
                    result.put("results", div);
                } else if (("multiply").equalsIgnoreCase(operation)) {
                    long num1 = A[l];
                    long num2 = A[r];
                    double multiple = (num2 * num1);
                    r--;
                    result.put("results", multiple);
                } else if (("subtract").equalsIgnoreCase(operation)) {
                    long num1 = A[l];
                    long num2 = A[r];
                    long subtract = (num2 - num1);
                    r--;
                    result.put("results", subtract);
                } else {
                    result.put("results", 0);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            result.put("Status", "02");
            result.put("Error Occured: ", "" + sw);
        }
        return result;
    }
}
