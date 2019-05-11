/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Queus;

import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author jkaru
 */
public class ProcessQueues {

    Logs logs = new Logs();
    String field3 = "3";

    public HashMap getMessageFromQueue(String JMSCorrelationID, String Target_Queue) {
        Message msg = null;

        HashMap fields = new HashMap();
        int loops = 1;
        try {
            Context cont = getInitialContext();
            while (true) {
                Message message = new QueueBrowser(cont).browseMyQueue(JMSCorrelationID, Utilities.configUtils.Settings().get("PROVIDER_URL"), Target_Queue);

                if (message instanceof ObjectMessage) {
                    ObjectMessage tm = (ObjectMessage) message;
                    fields = (HashMap) (((ObjectMessage) tm).getObject());
                }

                if (loops > 15 || !fields.isEmpty()) {
                    break;
                } else {
                    Thread.sleep(1000);
                }
                loops++;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "ERROR: Exception " + sw.toString(), "", field3);

        }
        return fields;
    }

    public Context getInitialContext() throws NamingException {
        Properties p = new Properties();
        p.put(Context.PROVIDER_URL, Utilities.configUtils.Settings().get("PROVIDER_URL"));
        p.put(Context.SECURITY_PRINCIPAL, Utilities.configUtils.Settings().get("SECURITY_PRINCIPAL"));
        p.put(Context.SECURITY_CREDENTIALS, Utilities.configUtils.Settings().get("SECURITY_CREDENTIALS"));
        Context cont = new InitialContext(p);
        return cont;

    }
}
