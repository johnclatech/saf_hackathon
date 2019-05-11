package com.johnclatech.ServiceEntry;

import java.sql.SQLException;
import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;
import javax.jms.JMSException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.misc.BASE64Decoder;

/**
 *
 * @author jkaru
 */
public class ProcessMessage {

    JSONObject MessageToChannel = new JSONObject();
    Processor func = new Processor();

    /**
     * posted data is routed to the appropriate functions that excecutes their
     * intended logics
     *
     * @param Mydata request data from entry point
     * @return JSONObject return type results
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public JSONObject PostData(HashMap<String, Object> Mydata) throws ClassNotFoundException, SQLException {
        JSONObject MessageToChannel = (JSONObject) Mydata;

        final String messagetype = Mydata.get("messagetype").toString();
        final String processingcode = Mydata.get("processingcode").toString();

        switch (messagetype) {
            case "0000":
                switch (processingcode) {
                    case "000000":
                        func.process(Mydata);
                        break;
                    case "000200":
                        func.ToOperations(Mydata);
                        break;
                    default:
                        break;
                }
        }
        return MessageToChannel;
    }

}
