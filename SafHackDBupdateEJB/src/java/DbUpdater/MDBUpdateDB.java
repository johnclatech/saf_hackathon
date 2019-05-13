/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbUpdater;

import Utilities.Logs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import process.ProcessMessage;

/**
 *
 * @author jkaru
 */
@MessageDriven(mappedName = "java:/jms/queue/LoanReversalQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")
    ,   @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    ,   @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:jboss/exported/jms/RemoteConnectionFactory")
    ,   @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/LoanReversalQueue")
    ,   @ActivationConfigProperty(propertyName = "connectionFactoryName", propertyValue = "java:jboss/exported/jms/RemoteConnectionFactory")

})
public class MDBUpdateDB implements MessageListener {
String field3 = "3";
    public MDBUpdateDB() {
    }

    @Override
    public void onMessage(Message message) {
        Logs logs = new Logs();
        ProcessMessage NewRequest = new ProcessMessage();
        try {
            NewRequest.LoanData(((TextMessage) message).getText());
        } catch (Exception ex) {
            logs.log(Logs.logPreString() + "Error : Exception \n" + ex.getMessage(), "", field3);
        }
    }

}
