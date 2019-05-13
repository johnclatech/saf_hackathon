/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.ServiceEntry;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Holder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.BASE64Decoder;

/**
 *
 * @author jkaru
 */
@WebService(serviceName = "Request")
@Stateless()
public class IncomingRequest {

    MessageRoutingEngine scheduler = new MessageRoutingEngine();

    /**
     * This is a sample web service operation that receives a request
     * comprising; a json encoded String value and a key named array
     *
     * @param txt string key of the request
     * @param value json encoded string request
     * @param Response the response object from the webservice
     * @param authkey the security authentication key to allow request
     * successful handshake
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    @WebMethod(operationName = "GetIndices")
    public void Requestenty(@WebParam(name = "value") String value, @WebParam(name = "key") String key, @WebParam(name = "Authkey") String authkey,
            @WebParam(name = "Response", mode = WebParam.Mode.OUT) Holder<String> Response) throws ClassNotFoundException, SQLException {

        try {
            HashMap<String, Object> MessageFromChannel = new HashMap<String, Object>();
            String RefNo = getMessageID("SAFARICOM");
            MessageFromChannel.put("refno", RefNo);
            System.out.println("com.johnclatech.ServiceEntry.IncomingRequest.RequestEnty() : EnrtyId:" + RefNo);
            if (!("").equals(value) && ("array").equalsIgnoreCase(key)) {
                MessageFromChannel = (JSONObject) new JSONParser().parse(DecodeMessage(value));

                //Authenticate the request with the salted hash
                boolean access = AuthenticateMessage(MessageFromChannel, authkey);
                if (access) {
                    MessageFromChannel = scheduler.PostData(MessageFromChannel);
                } else {
                    MessageFromChannel.put("Status", "01");
                    MessageFromChannel.put("StatusMessage", "Message Authentication Failed");
                }
            }
            Response.value = MessageFromChannel.toString();
        } catch (ParseException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
        }
    }

    private String getMessageID(String Mycountry) {
        try {
            Mycountry = Mycountry.substring(0, 2);
            DateFormat df = new SimpleDateFormat("yyMMdd");
            Date dtDateToday = new Date();
            Random rand = new Random();
            int value = rand.nextInt(1000000);
            String strPaddedNumber = String.format("%06d", value);
            String getM = Mycountry + df.format(dtDateToday) + strPaddedNumber;

            return getM;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            return "";
        }
    }

    private String DecodeMessage(String Message) {
        String MyDecodedMessage = "";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            String encodedBytes = Message;
            byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);

            MyDecodedMessage = new String(decodedBytes);
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            MyDecodedMessage = "";
        }
        return MyDecodedMessage;
    }

    private boolean AuthenticateMessage(HashMap Data, String Authkey) { //ENCODE THE DATA TO BASE64 THEN SHA512 THEN ADD THE SALT 
        boolean access = false;
        try {
            String MyAccessKey = "#$%^%^{}#$#?/\\|##@!%"; //for now i hardcode but should come from a config file
            String key = MyAccessKey + Data.get("channel").toString() + Data.get("timestamp").toString() + Data.get("processingcode").toString();
            String Sha512Key = GetSha512((EncodeMessage(key)));

            access = Authkey.equals(Sha512Key);

            if (access == true) { //edited on 22ndjan2018
                access = true;
            } else {
                access = false;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            access = false;
        }
        return access;
    }

    public String GetSha512(String MyValue) {
        String CheckSum = "";
        try {

            MessageDigest message = MessageDigest.getInstance("SHA-512");
            message.update(MyValue.getBytes());
            byte byteData[] = message.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            CheckSum = sb.toString();
            System.out.println("Hex format : " + sb.toString());
        } catch (NoSuchAlgorithmException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
        }
        return CheckSum;
    }

    private String EncodeMessage(String Message) {
        String MyEncodedMessage = "";
        try {
            MyEncodedMessage = DatatypeConverter.printBase64Binary(Message.getBytes());
            return MyEncodedMessage;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Logger.getLogger(IncomingRequest.class.getName()).log(Level.SEVERE, null, sw);
            MyEncodedMessage = "";
        }
        return MyEncodedMessage;
    }

}
