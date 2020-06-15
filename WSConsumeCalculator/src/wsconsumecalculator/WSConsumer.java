/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsconsumecalculator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class WSConsumer {

    Utilities utils = new Utilities();
//    Logs logs = new Logs();
    private static transient String SERVICE_NAMESPACE_TNS = "http://tempuri.org/";
    private static transient String SOAPACTIONHEAD = null;

    public static HashMap<String, String> settings = new HashMap();

    @PostConstruct
    public void init() {
        InputStream input = null;
        try {
            Properties prop = new Properties();
            String path = System.getProperty("jboss.server.data.dir");

            path = path + "/youthfund.properties";  // linux
            //  logs.log("Method: DetailsOk, Java Class: Confpath, Error: " + path, "", "6");

            input = new FileInputStream(path);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                settings.put(key, value);
            }
            System.out.println(settings);
            settings.put("LOADCONFIG", "SUCCESS");
        } catch (Exception e) {
            settings.put("LOADCONFIG", "FAILLED");
            System.out.println("Fatal Error!. Failld to set Configuration");

            System.exit(0);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.exit(0);
                }
            }
        }
    }

    public JSONObject sendRequest(HashMap Mydata) throws Exception {

        String requestXML = "";
        String methodName = "";
        String XMLResponse = "";
        JSONObject MessageFromIPRS = new JSONObject();
        JSONObject obj = new JSONObject();

        if (Mydata.containsKey("soapaction")) {
            try {
                SOAPACTIONHEAD = Mydata.get("soapaction").toString().trim();
                String apiURL = "http://www.dneonline.com/calculator.asmx?WSDL";
                requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n"
                        + "   <soapenv:Header/>\n"
                        + "   <soapenv:Body>\n"
                        + "      <tem:" + SOAPACTIONHEAD + ">\n"
                        + "         <tem:intA>" + Mydata.get("element0") + "</tem:intA>\n"
                        + "         <tem:intB>" + Mydata.get("element1") + "</tem:intB>\n"
                        + "      </tem:" + SOAPACTIONHEAD + ">\n"
                        + "   </soapenv:Body>\n"
                        + "</soapenv:Envelope>";
                methodName = SOAPACTIONHEAD;

                byte[] byteData = requestXML.getBytes();
                URL url = new URL(apiURL);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

                // Set the appropriate HTTP parameters.
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(byteData.length));
                httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                httpURLConnection.setRequestProperty("SOAPAction", SERVICE_NAMESPACE_TNS + SOAPACTIONHEAD);
                httpURLConnection.setRequestMethod("POST");
                //set the timeout in milliseconds
                httpURLConnection.setConnectTimeout(2500);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(requestXML);
                dataOutputStream.flush();
                dataOutputStream.close();

                // Read the response and write it to standard out.
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    XMLResponse = inputLine;
                }
                Map<String, String> fields = null;
                fields = utils.ParseXml(XMLResponse);
                System.out.println("IPRS RAW RESP: ~" + fields);

                try {

                    if (!fields.isEmpty()) {

                        if (fields.containsKey("AddResult")) {
                            String AddResult = !fields.containsKey("AddResult") ? "" : "".equals(fields.get("AddResult")) ? "" : fields.get("AddResult").trim();
                            MessageFromIPRS.clear();
                            MessageFromIPRS.put("AddResult", AddResult);
                            MessageFromIPRS.put("RESPONSE", "SUCCESS");
                            MessageFromIPRS.put("Status", "000");

                        } else if (fields.containsKey("MultiplyResult")) {

                            String MultiplyResult = !fields.containsKey("MultiplyResult") ? "" : "".equals(fields.get("MultiplyResult")) ? "" : fields.get("MultiplyResult").trim();
                            MessageFromIPRS.clear();
                            MessageFromIPRS.put("MultiplyResult", MultiplyResult);
                            MessageFromIPRS.put("RESPONSE", "SUCCESS");
                            MessageFromIPRS.put("Status", "000");

                        } else if (fields.containsKey("DivideResult")) {

                            String DivideResult = !fields.containsKey("DivideResult") ? "" : "".equals(fields.get("DivideResult")) ? "" : fields.get("DivideResult").trim();
                            MessageFromIPRS.clear();
                            MessageFromIPRS.put("DivideResult", DivideResult);
                            MessageFromIPRS.put("RESPONSE", "SUCCESS");
                            MessageFromIPRS.put("Status", "000");

                        } else if (fields.containsKey("SubtractResult")) {

                            String SubtractResult = !fields.containsKey("SubtractResult") ? "" : "".equals(fields.get("SubtractResult")) ? "" : fields.get("SubtractResult").trim();
                            MessageFromIPRS.clear();
                            MessageFromIPRS.put("SubtractResult", SubtractResult);
                            MessageFromIPRS.put("RESPONSE", "SUCCESS");
                            MessageFromIPRS.put("Status", "000");

                        } else {
                            MessageFromIPRS.clear();
                            MessageFromIPRS.put("Status", "001");
                            MessageFromIPRS.put("StatusMessage", "Failed to retrive details");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error :" + ex.getMessage());
                    MessageFromIPRS.clear();
                    MessageFromIPRS.put("Status", "998");
                    MessageFromIPRS.put("StatusMessage", "Oops! Error Occurred with an exception: \n" + ex.getMessage());
                }
            } catch (Exception ex) {
                System.out.println("Error :" + ex.getMessage());
                MessageFromIPRS.clear();
                MessageFromIPRS.put("Status", "998");
                MessageFromIPRS.put("StatusMessage", "Oops! Error Occurred with an exception: \n" + ex.getMessage());

                System.out.println("Error : " + ex.getMessage());
            }
        } else {
            MessageFromIPRS.clear();
            MessageFromIPRS.put("Status", "001");
            MessageFromIPRS.put("StatusMessage", "Failed to retrive details");
        }

        System.out.println("Response: - " + MessageFromIPRS);
        return MessageFromIPRS;

    }
}
