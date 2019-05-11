/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanReversal;

import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;

public class QueueBrowser {

    public final static String JMS_FACTORY = "java:jboss/exported/jms/RemoteConnectionFactory";

    Logs logs = new Logs();
    String field3 = "3";
    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private Queue queue;
    private javax.jms.QueueBrowser browser;
    private QueueReceiver receiver;
    public static String QUEUE;
    public Context ic;

    public QueueBrowser(Context cont) {
        this.ic = cont;
    }

    public boolean init(Context ctx, String queueName) {
        try {
            qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
            qcon = qconFactory.createQueueConnection();
            qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = (Queue) ctx.lookup(queueName);
            qcon.start();
            return true;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception : - \n" + sw.toString(), "", field3);
            return false;
        }
    }

    public Message browseQueue(String JMSCorrelationID, String url, String QUEUE) {

        Message msg = null;
        try {
            if (ic != null) {
                if (init(ic, QUEUE)) {
                    browser = qsession.createBrowser(queue, "JMSCorrelationID = '" + JMSCorrelationID + "'");  //queue
                    browser = qsession.createBrowser(queue);
                    Enumeration en = browser.getEnumeration();
                    while (en.hasMoreElements()) {
                        Message message = (Message) en.nextElement();
                        msg = message;
                        receiver = qsession.createReceiver(queue, "JMSCorrelationID = '" + JMSCorrelationID + "'");
                        //receiver = qsession.createReceiver(queue);
                        receiver.receiveNoWait();
                    }
                    browser.close();
                }
            }
        } catch (JMSException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception : - \n" + sw.toString(), "", field3);
        }
        close();
        return msg;
    }

    public Message getOriginalMessage() {
        Message msg = null;
        try {
            msg = receiver.receiveNoWait();
        } catch (JMSException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception : - \n" + sw.toString(), "", field3);
        }
        close();
        return msg;
    }

    public void close() {
        try {
            if (receiver != null) {
                receiver.close();
            }
            if (qsession != null) {
                qsession.close();
            }
            if (qcon != null) {
                qcon.close();
            }

        } catch (JMSException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception : - \n" + sw.toString(), "", field3);
        }
    }

}
