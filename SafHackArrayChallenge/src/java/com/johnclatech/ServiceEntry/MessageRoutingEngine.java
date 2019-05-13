package com.johnclatech.ServiceEntry;

import com.johnclatech.engines.CarManagerEngine;
import java.sql.SQLException;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class MessageRoutingEngine {

    JSONObject MessageToChannel = new JSONObject();
    static ArrayEngine toarrayengine = new ArrayEngine();
    static CarManagerEngine tocarmanagerengine = new CarManagerEngine();

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
                        toarrayengine.ArrayToTarget(Mydata);
                        break;
                    case "000200":
                        toarrayengine.ArrayOperations(Mydata);
                        break;

                    default:
                        break;
                }
                break;
                    case "0001":
                        switch (processingcode.trim()) {
                            case "00010":
                                //add a vehicle makes
                                tocarmanagerengine.SubmitVehicleMake(Mydata);
                                break;
                            case "00020":
                                //add vehicle type
                                tocarmanagerengine.SubmitMakeType(Mydata);

                                break;
                            case "00030":
                                //add vehicle color
                                tocarmanagerengine.SubmitVehicleColor(Mydata);
                                break;
                            case "00040":
                                //register vehicle

                                tocarmanagerengine.ShowRoomManager(Mydata);
                                break;
                            case "00050":
                                //Retrieve all cars in the Show room
                                tocarmanagerengine.GetAllVehicles(Mydata);
                                break;
                            case "00060":
                                //Retrieve all cars with specific attributes(make and color)
                                tocarmanagerengine.GetSpecificVehicles(Mydata);

                                break;
                            case "00070":
                                //Update cars information. Only allow a user to update the color and the availability.
                                tocarmanagerengine.UpdateVehicleDetails(Mydata);

                                break;
                            case "00080":
                                // Delete all Unavailable cars
                                tocarmanagerengine.DeleteVehicleDetails(Mydata);
                                break;

                            default:
                                break;

                        }
                }        
        return MessageToChannel;
    }

}
