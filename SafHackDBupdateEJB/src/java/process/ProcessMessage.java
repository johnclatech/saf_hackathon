package process;

import DbUpdater.Function;
import Utilities.Logs;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author jkaru
 */
public class ProcessMessage {

    Logs logs = new Logs();
    Functions Func = new Functions();
    String field2 = "2";
    String field3 = "3";
    String field4 = "4";
    String failcode = "010";
    String successcode = "00";

    Function sendQ = new Function();

    public HashMap LoanData(String LoanDetails) throws ClassNotFoundException, SQLException, org.json.simple.parser.ParseException {

        HashMap Mydata = null;
        HashMap MessageToChannel = null;
        try {
            Mydata = ConvertToHashMap(LoanDetails);

            MessageToChannel = Func.UpdateLoanReversal(Mydata);
            MessageToChannel.put("timestamp", Mydata.get("timestamp").toString());
            if ((successcode).equalsIgnoreCase(MessageToChannel.get("responsecode").toString())) {
                logs.log(Logs.logPreString() + "\nLoan: " + Mydata.get("loanid") + " Reversed on date " + new Date(), "", field2);
                sendQ.sendToQueue("java:/jms/queue/NavResponseReversalQueue", MessageToChannel);
            } else {                
                logs.log(Logs.logPreString() + "\nQueueing Loan Reversal fail on : " + new Date(), "", field4);
                sendQ.sendToQueue("java:/jms/queue/NavResponseReversalQueue", MessageToChannel);
            }
        } catch (ParseException ex) {
            Logger.getLogger(ProcessMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return MessageToChannel;
    }

    private HashMap ConvertToHashMap(String Message) throws ParseException {
        JSONObject hm = null;

        try {
            hm = (JSONObject) new JSONParser().parse(Message);

        } catch (Exception ex) {
            logs.log(Logs.logPreString() + "\nError : Exception : - \n" + ex.getMessage(), "", field3);
        }

        return hm;

    }
}
