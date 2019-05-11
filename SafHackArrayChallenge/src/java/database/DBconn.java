package database;

//import Connection.Database;
//import Timer.TrxnProcessTimer;
import Utilities.Logs;
import Utilities.configUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Creates single instance connection to database. Closes the connection Will
 * effectively manage your connections
 *
 * @author jkaru
 * @version 2.0
 */
public class DBconn {

    Connection connectionnav = null;
    Statement statementnav = null;
    ResultSet resultsetnav = null;
    ResultSet resultset2nav = null;
    DataSource dataSourcenav = null;
    Logs logs = new Logs();
    String field3 = "3";

    public DBconn() {
        try {
            HashMap configdata = configUtils.Settings();
            String datasource = (String) configdata.get("Datasource");

            Hashtable ht = new Hashtable();
            ht.put("java.naming.factory.initial.context.factory", "org.jboss.naming.remote.client.InitialContextFactory");
            ht.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
            ht.put("java.naming.provider.url", configdata.get("PROVIDER_URL"));
            ht.put("java.naming.security.credentials", configdata.get("SECURITY_CREDENTIALS"));
            ht.put("java.naming.security.principal", configdata.get("SECURITY_PRINCIPAL"));
            ht.put("jboss.naming.client.ejb.context", true);
            ht.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");

            InitialContext context = new InitialContext(ht);
            dataSourcenav = (DataSource) context.lookup(datasource);
            this.connectionnav = dataSourcenav.getConnection();
            this.statementnav = this.connectionnav.createStatement();
        } catch (Exception ex) {
            try {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                logs.log(Logs.logPreString() + "Error Exception : \n" + sw.toString(), "", field3);
            } catch (Exception ex1) {
                Logger.getLogger(DBconn.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    /**
     * Initialise this connection
     */
    public void close() {
        try {
            //Close JDBC objects as soon as possible
            if (statementnav != null) {
                statementnav.close();
                statementnav = null;
            }
            if (connectionnav != null) {
                connectionnav.close();
                connectionnav = null;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error Exception : \n" + sw.toString(), "", field3);
        }
    }

}
