/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanReversal;


import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author jkaru
 */
public class Function {

    Logs logger = new Logs();

    String field2 = "2";
    String field3 = "3";
    String field4 = "4";
    
    public Boolean sendToQueue(String Queue, Map<String, String> Fields) {
        QueueWriter WLI = null;
        Context cont = null;
        boolean sentToQ = false;
        try {
            WLI = new QueueWriter(Queue, (HashMap) Fields, Utilities.configUtils.Settings().get("RemoteConnectionFactory")); //"java:jboss/exported/jms/RemoteConnectionFactory");
            cont = getInitialContext();
            int trials = 0;
            do {
                sentToQ = WLI.sendObject(cont);
                trials++;
            } while (sentToQ == false & trials < 5);
        } catch (NamingException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.log(Logs.logPreString()+"ERROR: Exception \n" + sw.toString(), "", field3);
        } catch (JMSException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.log(Logs.logPreString()+"ERROR: Exception \n" + sw.toString(), "", field3);
        } 

        return sentToQ;
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
