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
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author jkaru
 */
public class WriteQueue {

    Logs logs = new Logs();
    String field3 = "3";

    public boolean writeToQ(HashMap FromChannel, String QueueName) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            InitialContext context = new InitialContext();
            //Context context = ContextUtil.getInitialContext();
            //System.out.println("Get connection facory");
            ConnectionFactory connectionFactory = (ConnectionFactory) context
                    .lookup("java:/ConnectionFactory");
            //System.out.println("Create connection");
            connection = connectionFactory.createConnection();
            //System.out.println("Create session");
            session = connection.createSession(false,
                    QueueSession.AUTO_ACKNOWLEDGE);
            //System.out.println("Lookup queue");
            Queue queue = (Queue) context.lookup(QueueName);  //java:/queue/HelloWorldQueue
            //System.out.println("Start connection");
            connection.start();
            //System.out.println("Create producer");
            producer = session.createProducer(queue);
            //System.out.println("Create hello world message");
            Message hellowWorldText = session.createTextMessage(FromChannel.toString());
            //System.out.println("Send hello world message");
            producer.send(hellowWorldText);
        } catch (NamingException ex) {
            // Verify=false;
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "ERROR: Exception \n" + sw.toString(), "", field3);

            // Logger.getLogger(WriteQueue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            //Verify=false;
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "ERROR: Exception \n" + sw.toString(), "", field3);

            //Logger.getLogger(WriteQueue.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {

                producer.close();
                session.close();
                connection.close();
            }
        }
        return true;
    }

    private static class ContextUtil {

        public static Context getInitialContext() throws NamingException {

            Properties props = new Properties();
            props.setProperty("java.naming.factory.initial",
                    "org.jnp.interfaces.NamingContextFactory");
            props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
            props.setProperty("java.naming.provider.url", "localhost:1099");
            Context context = new InitialContext(props);
            return context;

        }
    }

}
