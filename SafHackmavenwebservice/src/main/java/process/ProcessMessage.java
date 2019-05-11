package process;

import Queus.ProcessQueues;
import Queus.WriteQueue;
import Utilities.Logs;
import database.DBFunctions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class ProcessMessage {

    Logs logs = new Logs();
    DBFunctions dbf = new DBFunctions();
    ProcessQueues QueueResponse = new ProcessQueues();
    WriteQueue MyWriteQ = new WriteQueue();
    String field1 = "1";
    String field2 = "2";
    String field3 = "3";
    String field4 = "4";

    String MyResponseCode = "";
    String MyResponseNarration = "";
    String MyResponseLoanId = "";
    CallableStatement callableStatement = null;
    Connection connection = null;
    ResultSet resultset = null;

    public  void Process2(String IDNumber) {

        SimpleDateFormat timexf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
        String timex = timexf.format(new Date());

        JSONObject data = new JSONObject();
        HashMap<String, String> listOfValues = new HashMap<String, String>();
        try {

            //======Get reversed loans from Nav==========
            HashMap qhm = new HashMap();
            qhm.clear();
            qhm.put("COLS", " DISTINCT TOP 1 *");
            qhm.put("TABLE", "[Customers]");
            qhm.put("WHERE", "IDNumber = '"+IDNumber+"'");

            HashMap<String, String> activities = dbf.queryAllJsonNav(qhm);

            if (!activities.isEmpty()) {

                System.out.println("== : Customer to be queried:  " + activities.get("idnumber"));
                //====load queue to be used
                String queue = Utilities.configUtils.Settings().get("NavQueueReverseLoan");

                activities.put("timestamp", timex);
                //===execute a jms queue to send reversed loan details for update==
                MyWriteQ.writeToQ(activities, queue);

                //====Receive response for the update from response queue
                HashMap<String, String> results = new HashMap<String, String>();

                results = QueueResponse.getMessageFromQueue(timex, "java:/jms/queue/NavResponseReversalQueue");//  JSONIncoming.settings.get("MAIN_ESBResponse_Queue")); //"java:/jms/queue/youthfund_loan_response_queue");

                if (("Successful").equalsIgnoreCase(results.get("description").toString())) {
                    HashMap uhm = new HashMap();
                    uhm.clear();
                    uhm.put("SET", "Rejected = 1");
                    uhm.put("TABLE", "[Customers]");
                    uhm.put("WHERE", "IDNumber = '"+IDNumber+"' ");

                    int activit = dbf.Update(uhm);
                    if (activit > 0) {
                    } else {
                        logs.log(Logs.logPreString() + "Fail update pending on date " + new Date(), "", field4);

                    }
                }
            } else {
                logs.log(Logs.logPreString() + "No to be Reversed on date " + new Date(), "", field4);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception \n" + sw.toString(), "", field3);
        }
    }

}
