package Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author jkaru
 */
public class Logs {

    public Logs() {
    }

    public void log(String details, String uniqueid, String Loglevel) {
        
        JLogger logger = new JLogger();
        SimpleDateFormat Logtime = new SimpleDateFormat("dd-MMM-yyyy-HHmm");
        String logtime = Logtime.format(new Date());
        String TypeOfLog = "";
        switch (Loglevel) {
            case "1":
                TypeOfLog = "UpdateMessage";
                break;
            case "2":
                TypeOfLog = "UpdateProcessed";
                break;
            case "3":
                TypeOfLog = "UpdateAdapterErrors";
                break;
            case "4":
                TypeOfLog = "UpdateReponse";
                break;
            default:
                TypeOfLog = "UpdateReversalOthers";
                break;
        }
       uniqueid = logtime;
        logger.JLogger(TypeOfLog, uniqueid, details);

    }

       public static String logPreString() {
        return "SERVICE | ADAPTOR: dbUpdate | CLASS NAME: "
                + Thread.currentThread().getStackTrace()[2].getClassName() + ""
                + " | LINE NUMBER: " + Thread.currentThread().getStackTrace()[2].getLineNumber() + ""
                + " | METHOD: " + Thread.currentThread().getStackTrace()[2].getMethodName() + "() | \n";
    }

}
