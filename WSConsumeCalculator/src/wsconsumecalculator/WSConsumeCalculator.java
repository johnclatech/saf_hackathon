/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsconsumecalculator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class WSConsumeCalculator {

    static ENUMSOperations enums = null;
    static WSConsumer Test = new WSConsumer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String value = "";

        String name = "";
        JSONObject MessageFromChannel = new JSONObject();
        JSONObject RecievedFromChannel = new JSONObject();
        try {
            try (Scanner input = new Scanner(System.in)) {
                System.out.println("Enter the Operation :");
                value = input.nextLine();
                enums = ENUMSOperations.valueOf(value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase());
            
            }
            
            System.out.println("Operation : ( " + enums + " ) Accepted!");
            
            switch (enums) {
                case Add:
                    MessageFromChannel.put("soapaction", "Add");
                    MessageFromChannel.put("element0", "5");
                    MessageFromChannel.put("element1", "8");
                    break;
                case Divide:
                    MessageFromChannel.put("soapaction", "Divide");
                    MessageFromChannel.put("element0", "17");
                    MessageFromChannel.put("element1", "3");
                    break;
                case Multiply:
                    MessageFromChannel.put("soapaction", "Multiply");
                    MessageFromChannel.put("element0", "1999999");//"1999999999999999999999999" generates an error 500
                    MessageFromChannel.put("element1", "3");

                    break;
                case Subtract:
                    MessageFromChannel.put("soapaction", "Subtract");
                    MessageFromChannel.put("element0", "21");
                    MessageFromChannel.put("element1", "13");
                    break;
                default:
                    break;
            }

            System.out.println("The HashMap Data :" + MessageFromChannel.toJSONString());

            RecievedFromChannel = Test.sendRequest(MessageFromChannel);

            System.out.println("wsconsumecalculator.WSConsumeCalculator.main() : response from SOAP WebService :\n " + RecievedFromChannel.toJSONString());

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            System.err.println("Operation : ( " + value + " ) Rejected");
            System.out.println("Exception: " + sw.toString());
        }
    }

}
