/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnclatech.engines;

import Utilities.Logs;
import database.DBFunctions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class DBOperations {

    private static final String field2 = "2";
    private static final String field3 = "3";
    private static final String field4 = "4";
    Logs logs = new Logs();
    DBFunctions dbf = new DBFunctions();

    public int SaveArrayToDB(String arraystring, String operation, String createdon, String results) {
        int affectedRows = 0;
        try {
            JSONObject qhm = new JSONObject();
            qhm.clear();
            qhm.put("COLS", "arraystring,operation,createdon,results");
            qhm.put("TABLE", "[HackathonDB].[dbo].[Arrays_Operations]");
            qhm.put("VALUES", "'" + arraystring + "','" + operation + "','" + createdon + "','" + results + "'");
            affectedRows = dbf.InsertAll(qhm);

            logs.log(Logs.logPreString() + "\nUPDATED  DELIVERED ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            System.out.println("com.johnclatech.engines.DBOperations.SaveArrayToDB(): Error Occurred:\n" + sw);
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }

    public int RegisterVehicle(HashMap vehicledetails) {
        int affectedRows = 0;
        try {

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());
            String creationdate = date;
            String vehiclemake = vehicledetails.get("vehiclename").toString().trim();
            String vehicleregno = vehicledetails.get("vehicleregno").toString().trim();
            String manufactureyear = vehicledetails.get("manufactureyear").toString().trim();
            String vehiclemodel = vehicledetails.get("car_type").toString().trim();
            String vehiclecolor = vehicledetails.get("vehiclecolor").toString().trim();
            String available = vehicledetails.get("available").toString().trim();

            //left out dateupdated and deleted flag
            HashMap ihm = new HashMap();
            ihm.clear();
            ihm.put("COLS", "[carmake],[regno],[manufactureyr],[type],[color],[creationdate],[available]");
            ihm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            ihm.put("VALUES", "'" + vehiclemake + "','" + vehicleregno + "','" + manufactureyear + "','" + vehiclemodel + "','" + vehiclecolor + "','" + creationdate + "','" + available + "'");
            affectedRows = dbf.InsertAll(ihm);
            logs.log(Logs.logPreString() + "\nVehicle make : " + vehiclemake + ".\n Vehicle model: " + vehiclemodel + ".\n Vehicle color :" + vehiclecolor + ".\n Registration Number : " + vehicleregno + ".\n The above has been registered in the CarManager ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }

    public int AddVehicleMake(String car) {
        int affectedRows = 0;
        try {
            JSONObject qhm = new JSONObject();
            qhm.clear();
            qhm.put("COLS", "[carmake]");
            qhm.put("TABLE", "[HackathonDB].[dbo].[tb_Vehicle]");
            qhm.put("VALUES", "'" + car + "'");
            affectedRows = dbf.InsertAll(qhm);

            logs.log(Logs.logPreString() + "\nVehicle make " + car + " added ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            System.out.println("com.johnclatech.engines.DBOperations.AddVehicleMake(): Error Occurred:\n" + sw);
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }

    public int AddVehicleType(String carname, String carno, String cartype) {
        int affectedRows = 0;
        try {
            JSONObject qhm = new JSONObject();
            qhm.clear();
            qhm.put("COLS", "[car_type],[carno]");
            qhm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleType]");
            qhm.put("VALUES", "'" + cartype + "', '" + carno + "'");
            affectedRows = dbf.InsertAll(qhm);

            logs.log(Logs.logPreString() + "\nVehicle model " + cartype + " added for " + carname + " ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            System.out.println("com.johnclatech.engines.DBOperations.AddVehicleType(): Error Occurred:\n" + sw);
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }

    public int AddVehicleColor(String color) {
        int affectedRows = 0;
        try {
            JSONObject qhm = new JSONObject();
            qhm.clear();
            qhm.put("COLS", "[color]");
            qhm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleColor]");
            qhm.put("VALUES", "'" + color + "'");
            affectedRows = dbf.InsertAll(qhm);

            logs.log(Logs.logPreString() + "\nVehicle color " + color + " added ", "", field2);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            System.out.println("com.johnclatech.engines.DBOperations.AddVehicleColor(): Error Occurred:\n" + sw);
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
        }
        return affectedRows;
    }

    /**
     * checks Vehicle Existence by name
     *
     * @param make HashMap containing a name from requesting function
     * @return
     */
    public boolean CheckVehicleByName(HashMap make) {
        String vehicle = !make.containsKey("vehiclename") ? null : make.get("vehiclename").toString() == null ? "" : make.get("vehiclename").toString().trim();
        boolean check = false;
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "[carmake]");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_Vehicle]");
            chm.put("WHERE", "[carmake] = '" + vehicle + "'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.CheckVehicleByName() Error Occurred:\n" + sw);
            check = false;
        }
        return check;
    }

    /**
     * checks Vehicle type Existence by cartype from vehicle type relation
     *
     * @param cartype HashMap containing a name from requesting function
     * @return
     */
    public boolean CheckVehicleByType(HashMap cartype) {
        String vehicle_make = !cartype.containsKey("car_type") ? null : cartype.get("car_type").toString() == null ? "" : cartype.get("car_type").toString().trim();

        boolean check = false;
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "[car_type] ,[carno]");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleType]");
            chm.put("WHERE", "[car_type] = '" + vehicle_make + "'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.CheckVehicleByType() Error Occurred:\n" + sw);
            check = false;
        }
        return check;
    }

    /**
     * checks Vehicle Existence by name
     *
     * @param Vehiclecolor HashMap containing a name from requesting function
     * @return
     */
    public boolean CheckVehicleByColor(HashMap Vehiclecolor) {
        String vehiclecolor = !Vehiclecolor.containsKey("vehiclecolor") ? null : Vehiclecolor.get("vehiclecolor").toString() == null ? "" : Vehiclecolor.get("vehiclecolor").toString().trim();
        boolean check = false;
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "[color]");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleColor]");
            chm.put("WHERE", "[color] = '" + vehiclecolor + "'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.CheckVehicleByColor() Error Occurred:\n" + sw);
            check = false;
        }
        return check;
    }

    /**
     * checks Vehicle Existence in the vehicle details relation
     *
     * @param vehiclereq HashMap containing a name from requesting function
     * @return
     */
    public boolean CheckVehicleDetails(HashMap vehiclereq) {
        String vehicleregno = !vehiclereq.containsKey("vehicleregno") ? null : vehiclereq.get("vehicleregno").toString() == "" ? "" : vehiclereq.get("vehicleregno").toString().trim();

        boolean check = false;
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "*");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            chm.put("WHERE", "[regno] = '" + vehicleregno + "'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.CheckVehicleDetails() Error Occurred:\n" + sw);
            check = false;
        }
        return check;
    }

    /**
     * Checks Vehicle model and corresponding make existence in the vehicle
     * details view
     *
     * @param vehicledetails HashMap containing a name from requesting function
     * @return
     */
    public boolean CheckVehicleMakeToModel(HashMap vehicledetails) {
        String vehiclemake = !vehicledetails.containsKey("vehiclename") ? null : vehicledetails.get("vehiclename").toString() == null ? "" : vehicledetails.get("vehiclename").toString().trim();
        String vehicletype = !vehicledetails.containsKey("car_type") ? null : vehicledetails.get("car_type").toString() == null ? "" : vehicledetails.get("car_type").toString().trim();

        boolean check = false;
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "*");
            chm.put("TABLE", "[HackathonDB].[dbo].[View_CarMaketoModel]");
            chm.put("WHERE", "[car_make] = '" + vehiclemake + "' and [vehicle_model] = '" + vehicletype + "'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.CheckVehicleMakeToModel() Error Occurred:\n" + sw);
            check = false;
        }
        return check;
    }

    /**
     * Gets Vehicle number Existence in the vehicle relation
     *
     * @param make HashMap containing a name from requesting function
     * @return
     */
    public HashMap GetVehicleCode(HashMap make) {
        String vehicle = !make.containsKey("vehiclename") ? null : make.get("vehiclename").toString() == null ? "" : make.get("vehiclename").toString().trim();
        HashMap cardetails = new HashMap();
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", " [carno],[carmake]");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_Vehicle]");
            chm.put("WHERE", "carmake LIKE '%" + vehicle + "%'");
            JSONObject checkquery = dbf.queryAllJsonNav(chm);
            if (checkquery.isEmpty()) {
                cardetails.put("success", false);
                cardetails.put("message", "Ensure you first add the vehicle make " + vehicle);
            } else {
                cardetails.put("success", true);
                cardetails.put("vehicleno", checkquery.get("carno").toString().trim());
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.GetVehicleCode() Error Occurred:\n" + sw);
        }
        return cardetails;
    }

    /**
     * update Vehicle in the vehicle details relation can only update (color and
     * availability)
     *
     * @param vehicledetails HashMap containing a name from requesting function
     * @return
     */
    public int UpdateVehicleDetails(HashMap vehicledetails) {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        String updatedate = date;
//        String vehiclemake = vehicledetails.get("vehiclename").toString().trim();
        String vehicleregno = vehicledetails.get("vehicleregno").toString().trim();
//        String manufactureyear = vehicledetails.get("manufactureyear").toString().trim();
//        String vehiclemodel = vehicledetails.get("car_type").toString().trim();
        String vehiclecolor = vehicledetails.get("vehiclecolor").toString().trim();
        String available = vehicledetails.get("available").toString().trim();
        int affectedrows = 0;
        try {
            JSONObject uhm = new JSONObject();
            uhm.clear();
            uhm.put("SET", "[color] = '" + vehiclecolor + "',[dateupdated] = '" + updatedate + "',[available] = '" + available + "'");
            uhm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            uhm.put("WHERE", "[regno] = '" + vehicleregno + "' AND [deleted] = '0'");
            affectedrows = dbf.Update(uhm);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);
            System.out.println("com.johnclatech.ServiceEntry.DBOperations.UpdateVehicleDetails() Error Occurred:\n" + sw);
        }
        return affectedrows;
    }

    /**
     * Gets all Vehicle in the show room
     *
     * @param make HashMap containing a name from requesting function
     * @return
     */
    public HashMap GetAllVehicles() {
        HashMap cardetails = new HashMap();
        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "*");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            chm.put("WHERE", "[deleted] = '0'");

            JSONObject checkquery = dbf.queryJsonNav(chm);
            if (checkquery.isEmpty()) {
                cardetails.put("success", false);
            } else {
                cardetails.put("success", true);
                cardetails.put("vehicledetails", checkquery);
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.GetAllVehicles() Error Occurred:\n" + sw);
        }
        return cardetails;
    }

    /**
     * Gets Vehicle with specific attributes from the show room
     *
     * @param vehiclereq HashMap containing a name from requesting function
     * @return
     */
    public HashMap GetSpecificVehicles(HashMap vehiclereq) {
        HashMap cardetails = new HashMap();
        String vehiclecolor = vehiclereq.get("vehiclecolor") == "" ? "" : vehiclereq.get("vehiclecolor").toString().trim();
        String available = vehiclereq.get("available") == "" ? "" : vehiclereq.get("available").toString().trim();
        String vehiclename = vehiclereq.get("vehiclename") == "" ? "" : vehiclereq.get("vehiclename").toString().trim();

        try {
            JSONObject chm = new JSONObject();
            chm.clear();
            chm.put("COLS", "*");
            chm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            chm.put("WHERE", "[carmake] LIKE '%" + vehiclename + "%' AND [color] LIKE '%" + vehiclecolor + "%' AND [available] LIKE '%" + available + "%' AND [deleted] = '0'");
            JSONObject checkquery = dbf.queryJsonNav(chm);
            if (checkquery.isEmpty()) {
                cardetails.put("success", false);
            } else {
                cardetails.put("success", true);
                cardetails.put("vehicledetails", checkquery);
            }

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.GetAllVehicles() Error Occurred:\n" + sw);
        }
        return cardetails;
    }

    /**
     * Gets Vehicle with specific attributes from the show room
     *
     * @param vehiclereq HashMap containing a name from requesting function
     * @return
     */
    public int DeleteSpecificVehicles(HashMap vehiclereq) {
        HashMap cardetails = new HashMap();
        String vehicleregno = vehiclereq.get("vehicleregno").toString().trim();

        int affectedrows = 0;
        try {
            JSONObject uhm = new JSONObject();
            uhm.clear();
            uhm.put("SET", "[deleted] = '1', [available] = '0'");
            uhm.put("TABLE", "[HackathonDB].[dbo].[tb_VehicleDetails]");
            uhm.put("WHERE", "[regno] = '"+vehicleregno+"' AND [available] = '1' and [deleted] = '0'");
            affectedrows = dbf.Update(uhm);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "\nEXCEPTION OCCURED: \n" + sw, "", field3);

            System.out.println("com.johnclatech.ServiceEntry.DBOperations.GetAllVehicles() Error Occurred:\n" + sw);
        }
        return affectedrows;
    }

}
