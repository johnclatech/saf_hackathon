/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.engines;

import Utilities.Logs;
import com.johnclatech.ArraytoTarget.ArrayToTarget;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class CarManagerEngine {

    static Logs logs = new Logs();
    static DBOperations dbops = new DBOperations();
    String field1 = "1";
    String field2 = "2";
    String field3 = "3";
    String field4 = "4";
    String field6 = "6";
    String field7 = "7";
    String field9 = "9";
    String errorcode = "02";
    String failcode = "01";
    String nullcode = "03";
    String successcode = "00";
    String existcode = "005";

    /**
     *
     * @param Mydata
     * @return
     */
    public JSONObject ShowRoomManager(HashMap Mydata) {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap result = new HashMap();
        int affectedrows = 0;
        try {
            //check if vehicle is already the vehicle is registered in the system
            boolean checkvehicleDetails = dbops.CheckVehicleDetails(MessageToChannel);

            if (checkvehicleDetails == true) {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "Vehicle already exist in the system");
            } else {
                //check details of the v-make to v-model(entering a vehicle whose make dont exist,entering a vehicle make whose model is for another v-make)
                boolean checkvehiclemake = dbops.CheckVehicleByName(MessageToChannel);
                if (checkvehiclemake) {
                    boolean checkvehiclemodel = dbops.CheckVehicleByType(MessageToChannel);
                    if (checkvehiclemodel) {
                        boolean checkvehiclecolor = dbops.CheckVehicleByColor(MessageToChannel);
                        if (checkvehiclecolor) {
                            boolean checkvehiclemaketomodel = dbops.CheckVehicleMakeToModel(MessageToChannel);
                            if (checkvehiclemaketomodel) {

                                //register the vehicle into the system
                                affectedrows = dbops.RegisterVehicle(MessageToChannel);
                                if (affectedrows > 0) {
                                    MessageToChannel.put("dbaffected", affectedrows);
                                    MessageToChannel.put("Status", successcode);
                                    MessageToChannel.put("success: ", true);
                                    MessageToChannel.put("StatusMessage", "Process completed successfully");
                                } else {
                                    MessageToChannel.put("dbaffected", affectedrows);
                                    MessageToChannel.put("Status", failcode);
                                    MessageToChannel.put("success: ", false);
                                    MessageToChannel.put("StatusMessage", "Process failed");
                                }

                            } else {
                                MessageToChannel.put("Status", existcode);
                                MessageToChannel.put("StatusMessage", "The vehicle model does not belong to this car make");
                            }
                        } else {
                            MessageToChannel.put("Status", existcode);
                            MessageToChannel.put("StatusMessage", "The vehicle color does not exist");
                        }

                    } else {
                        MessageToChannel.put("Status", existcode);
                        MessageToChannel.put("StatusMessage", "The vehicle model for the car make does not exist\n ensure it is added in the system");
                    }
                } else {
                    MessageToChannel.put("Status", existcode);
                    MessageToChannel.put("StatusMessage", "The vehicle make does not exist in the system");
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin stating refno: " + MessageToChannel.get("refno").toString().trim());
            logs.log(Logs.logPreString() + "Error: Exception:- " + sw.toString(), MessageToChannel.get("refno").toString().trim(), field3);
        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     */
    public JSONObject SubmitVehicleMake(HashMap Mydata) {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap result = new HashMap();
        int affectedrows = 0;

        try {
            String vehiclename = MessageToChannel.get("vehiclename").toString().trim();
            //check if vehicle is already in the system
            boolean checkvehicle = dbops.CheckVehicleByName(MessageToChannel);
            if (checkvehicle == true) {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "Vehicle already exist in the system");
            } else {
                affectedrows = dbops.AddVehicleMake(vehiclename);
                if (affectedrows > 0) {
                    MessageToChannel.put("dbaffected", affectedrows);
                    MessageToChannel.put("Status", successcode);
                    MessageToChannel.put("success: ", true);
                    MessageToChannel.put("StatusMessage", "Process completed successfully");
                } else {
                    MessageToChannel.put("dbaffected", affectedrows);
                    MessageToChannel.put("Status", failcode);
                    MessageToChannel.put("success: ", false);
                    MessageToChannel.put("StatusMessage", "Process failed");
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin for further assistance ");
            logs.log(Logs.logPreString() + "Error: Exception:- " + sw.toString(), MessageToChannel.get("refno").toString().trim(), field3);
        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     */
    public JSONObject SubmitMakeType(HashMap Mydata) {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap result = new HashMap();
        int affectedrows = 0;

        try {
            String vehiclemodel = MessageToChannel.get("car_type").toString().trim();
            String vehiclemake = MessageToChannel.get("vehiclename").toString().trim();
            //check if vehicle model is already in the system
            boolean checkvehicletype = dbops.CheckVehicleByType(MessageToChannel);
            if (checkvehicletype == true) {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "The provided vehicle model already belongs to another car make");
            } else {
                //Get vehicle number from the system
                HashMap carnum = dbops.GetVehicleCode(MessageToChannel);
                boolean success = (boolean) carnum.get("success");
                if (success) {
                    String vehiclenum = carnum.get("vehicleno").toString().trim();
                    //verify if vehicle model is assigned to another car make
//                    boolean checktypetomake = dbops.CheckVehicleTypeToMake(carnum,vehiclemodel);
//
//                    if (checktypetomake == true) {
//                        MessageToChannel.put("Status", existcode);
//                        MessageToChannel.put("StatusMessage", "The provided vehicle model already belongs to another car make");
//                    } else {
                    affectedrows = dbops.AddVehicleType(vehiclemake, vehiclenum, vehiclemodel);
                    if (affectedrows > 0) {
                        MessageToChannel.put("dbaffected", affectedrows);
                        MessageToChannel.put("Status", successcode);
                        MessageToChannel.put("success: ", true);
                        MessageToChannel.put("StatusMessage", "Process completed successfully");
                    } else {
                        MessageToChannel.put("dbaffected", affectedrows);
                        MessageToChannel.put("Status", failcode);
                        MessageToChannel.put("success: ", false);
                        MessageToChannel.put("StatusMessage", "Process failed");
                    }
//                    }
                } else {
                    MessageToChannel.putAll(carnum);
                }
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin for further assistance ");
            logs.log(Logs.logPreString() + "Error: Exception:- " + sw.toString(), MessageToChannel.get("refno").toString().trim(), field3);
        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     */
    public JSONObject SubmitVehicleColor(HashMap Mydata) {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap result = new HashMap();
        int affectedrows = 0;

        try {
            String vehiclecolor = MessageToChannel.get("vehiclecolor").toString().trim();
            //check if color is already in the system
            boolean checkbycolor = dbops.CheckVehicleByColor(MessageToChannel);
            if (checkbycolor == true) {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "Vehicle color already exist in the system");
            } else {
                affectedrows = dbops.AddVehicleColor(vehiclecolor);
                if (affectedrows > 0) {
                    MessageToChannel.put("dbaffected", affectedrows);
                    MessageToChannel.put("Status", successcode);
                    MessageToChannel.put("success: ", true);
                    MessageToChannel.put("StatusMessage", "Process completed successfully");
                } else {
                    MessageToChannel.put("dbaffected", affectedrows);
                    MessageToChannel.put("Status", failcode);
                    MessageToChannel.put("success: ", false);
                    MessageToChannel.put("StatusMessage", "Process failed");
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin for further assistance ");
            logs.log(Logs.logPreString() + "Error: Exception:- " + sw.toString(), MessageToChannel.get("refno").toString().trim(), field3);
        }
        return MessageToChannel;
    }

    public JSONObject UpdateVehicleDetails(HashMap Mydata) throws ClassNotFoundException, SQLException {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        int affectedrows = 0;

//        the commented region can be uncommented if more values requires to be updated in the system
        try {
            //check if vehicle is already the vehicle is registered in the system
            boolean checkvehicleDetails = dbops.CheckVehicleDetails(MessageToChannel);

            if (checkvehicleDetails == true) {
                //check details of the v-make to v-model(entering a vehicle whose make dont exist,entering a vehicle make whose model is for another v-make)
//                boolean checkvehiclemake = dbops.CheckVehicleByName(MessageToChannel);
//                if (checkvehiclemake) {
//                    boolean checkvehiclemodel = dbops.CheckVehicleByType(MessageToChannel);
//                    if (checkvehiclemodel) {
                boolean checkvehiclecolor = dbops.CheckVehicleByColor(MessageToChannel);
                if (checkvehiclecolor) {
//                            boolean checkvehiclemaketomodel = dbops.CheckVehicleMakeToModel(MessageToChannel);
//                            if (checkvehiclemaketomodel) {
                    //update the vehicle in the system
                    affectedrows = dbops.UpdateVehicleDetails(MessageToChannel);
                    if (affectedrows > 0) {
                        MessageToChannel.put("dbaffected", affectedrows);
                        MessageToChannel.put("Status", successcode);
                        MessageToChannel.put("success: ", true);
                        MessageToChannel.put("StatusMessage", "Process completed successfully");
                    } else {
                        MessageToChannel.put("dbaffected", affectedrows);
                        MessageToChannel.put("Status", failcode);
                        MessageToChannel.put("success: ", false);
                        MessageToChannel.put("StatusMessage", "Process failed");
                    }

//                            } else {
//                                MessageToChannel.put("Status", existcode);
//                                MessageToChannel.put("StatusMessage", "The vehicle model does not belong to this car make");
//                            }
                } else {
                    MessageToChannel.put("Status", existcode);
                    MessageToChannel.put("StatusMessage", "The vehicle color does not exist");
                }

//                    } else {
//                        MessageToChannel.put("Status", existcode);
//                        MessageToChannel.put("StatusMessage", "The vehicle model for the car make does not exist\n ensure it is added in the system");
//                    }
//                } else {
//                    MessageToChannel.put("Status", existcode);
//                    MessageToChannel.put("StatusMessage", "The vehicle make does not exist in the system");
//                }
            } else {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "Vehicle does not exist in the system");
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception:- " + sw.toString(), MessageToChannel.get("refno").toString().trim(), field3);
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin for further assistance ");

        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public JSONObject GetAllVehicles(HashMap Mydata) throws ClassNotFoundException, SQLException {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap alldata = new HashMap();
        try {
            HashMap allvehicles = dbops.GetAllVehicles();
            boolean success = (boolean) allvehicles.get("success");
            if (success) {
                alldata = (HashMap) allvehicles.get("vehicledetails");

                MessageToChannel.put("Status", successcode);
                MessageToChannel.put("success: ", true);
                MessageToChannel.put("StatusMessage", "Process completed successfully");
                MessageToChannel.put("AllAvailableVehicles", alldata);

            } else {
                MessageToChannel.put("Status", failcode);
                MessageToChannel.put("success: ", false);
                MessageToChannel.put("StatusMessage", "Process failed");
                MessageToChannel.put("AllAvailableVehicles", alldata);
                logs.log(Logs.logPreString() + "" + MessageToChannel.get("StatusMessage").toString(), MessageToChannel.get("refno").toString().trim(), field4);

            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception: - \n " + sw.toString(), MessageToChannel.get("refno").toString().trim(), "3");
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin stating refno: " + MessageToChannel.get("refno").toString().trim());
        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public JSONObject GetSpecificVehicles(HashMap Mydata) throws ClassNotFoundException, SQLException {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        HashMap somedata = new HashMap();
        try {
            HashMap allvehicles = dbops.GetSpecificVehicles(MessageToChannel);
            boolean success = (boolean) allvehicles.get("success");
            if (success) {
                somedata = (HashMap) allvehicles.get("vehicledetails");

                MessageToChannel.put("Status", successcode);
                MessageToChannel.put("success: ", true);
                MessageToChannel.put("StatusMessage", "Process completed successfully");
                MessageToChannel.put("VehiclesDetails", somedata);

            } else {
                MessageToChannel.put("Status", failcode);
                MessageToChannel.put("success: ", false);
                MessageToChannel.put("StatusMessage", "No car with the specified attributes in the show room");
                MessageToChannel.put("AllAvailableVehicles", somedata);

            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception: - \n " + sw.toString(), MessageToChannel.get("refno").toString().trim(), "3");
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin stating refno: " + MessageToChannel.get("refno").toString().trim());
        }
        return MessageToChannel;
    }

    /**
     *
     * @param Mydata
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public JSONObject DeleteVehicleDetails(HashMap Mydata) throws ClassNotFoundException, SQLException {
        JSONObject MessageToChannel = (JSONObject) Mydata;
        int affectedrows = 0;

        try {
            boolean checkvehicleDetails = dbops.CheckVehicleDetails(MessageToChannel);

            if (checkvehicleDetails == true) {
                affectedrows = dbops.DeleteSpecificVehicles(MessageToChannel);
                if (affectedrows > 0) {
                    MessageToChannel.put("Status", successcode);
                    MessageToChannel.put("success: ", true);
                    MessageToChannel.put("StatusMessage", "Process completed successfully");
                } else {
                    MessageToChannel.put("Status", failcode);
                    MessageToChannel.put("success: ", false);
                    MessageToChannel.put("StatusMessage", "Process delete failed");
                    logs.log(Logs.logPreString() + "" + MessageToChannel.get("StatusMessage").toString(), MessageToChannel.get("refno").toString().trim(), field4);
                }
            } else {
                MessageToChannel.put("Status", existcode);
                MessageToChannel.put("StatusMessage", "Vehicle does not exist in the system");
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception: - \n " + sw.toString(), MessageToChannel.get("refno").toString().trim(), "3");
            MessageToChannel.put("Status", errorcode);
            MessageToChannel.put("StatusMessage", "Ops!! The system has encountered an error, your request has been rejected. Please contact admin stating refno: " + MessageToChannel.get("refno").toString().trim());
        }
        return MessageToChannel;
    }

}
