package DbUpdater;

import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * 
 * @author jkaru
 */
public final class QueueWriter {

    Logs logs = new Logs();
    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private final String queue;
    private TextMessage msg;
    private final String JMS_FACTORY;
    private HashMap objmsg = new HashMap();
    String field3 = "3";

    public QueueWriter(String queue, HashMap objmsg, String JMS_FACTORY) {
        this.JMS_FACTORY = JMS_FACTORY;
        this.queue = queue;
        this.objmsg = objmsg;
    }

    public HashMap cleanMap(HashMap<String, String> fields) {
        for (String key : fields.keySet()) {
            if ("".equals(fields.get(key))) {
                fields.remove(key);
            }
        }
        return fields;
    }

    public boolean sendObject(Context cont) throws NamingException, JMSException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            //Custom made ConnetionFactory
            ConnectionFactory conFact = (ConnectionFactory) cont.lookup("java:/ConnectionFactory");//(JMS_FACTORY);

            connection = conFact.createConnection();

            session = connection.createSession(false,
                    QueueSession.AUTO_ACKNOWLEDGE);
            Destination queue = (Queue) cont.lookup(this.queue);

            producer = session.createProducer(queue);
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(objmsg);
            String CorrelationId = objmsg.get("timestamp").toString();
            msg.setJMSCorrelationID(CorrelationId);
            producer.send(msg);
            System.out.println("Message Sent: " + msg.toString());
            return true;

        } catch (JMSException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception : - \n" + sw.toString(), "", field3);
            return false;
        } finally {
            producer.close();
            session.close();
            connection.close();
        }

    }

}
