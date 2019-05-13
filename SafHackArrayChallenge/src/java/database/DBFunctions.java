package database;

import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author jkaru
 */
public class DBFunctions {

    Logs logs = new Logs();
    private DBconn DBconn;
    private PreparedStatement preparedstatement = null;
    String field3 = "3";

    public DBFunctions() {

    }

    private void getDBconn() {
        this.DBconn = new DBconn();

    }

    public JSONObject queryAllJsonNav(HashMap request) {
        JSONObject data = new JSONObject();
        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String query = "SELECT " + cols + " FROM " + request.get("TABLE") + " WHERE " + request.get("WHERE");
            DBconn.resultsetnav = DBconn.statementnav.executeQuery(query);
            ResultSetMetaData md = DBconn.resultsetnav.getMetaData();
            int columns = md.getColumnCount();
            while (DBconn.resultsetnav.next()) {
                for (int i = 1; i <= columns; ++i) {
                    data.put(md.getColumnName(i).toLowerCase(), DBconn.resultsetnav.getString(i));
                }
            }

        } catch (SQLException e) {

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error Exception : \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return data;
    }

    public JSONObject queryJsonNav(HashMap request) {
        JSONObject data = new JSONObject();
        JSONObject msgobject = new JSONObject();

        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String tableName = request.get("TABLE").toString();
            String where = request.get("WHERE").toString();
            String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + where;
            DBconn.resultsetnav = DBconn.statementnav.executeQuery(query);
            ResultSetMetaData md = DBconn.resultsetnav.getMetaData();

            int columns = md.getColumnCount();
            int rows = DBconn.resultsetnav.getRow();
            int j = 0;
            String MyColumn = "";
            String Myvalue = "";
            while (DBconn.resultsetnav.next()) {
                for (int i = 1; i <= columns; ++i) {
                    MyColumn = md.getColumnName(i);
                    Myvalue = DBconn.resultsetnav.getString(MyColumn);
                    data.put(MyColumn.toLowerCase().trim(), Myvalue);
                }
                msgobject.put(j + "", data.toJSONString());
                j++;
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "EXCEPTION OCCURRED: " + sw.toString(), "", field3);
        }
        DBconn.close();
        return msgobject;
    }

    public int Update(HashMap request) {
        int update = 0;
        try {
            getDBconn();
            String cols = request.get("SET").toString();
            String tableName = request.get("TABLE").toString();
            String values = request.get("WHERE").toString();
            String query = "UPDATE " + tableName + " SET " + cols + " WHERE " + values + "";

            update = DBconn.statementnav.executeUpdate(query);
        } catch (SQLException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            update = 0;
            logs.log(Logs.logPreString() + "Error Exception : \n" + sw.toString(), "", field3);
        }

        DBconn.close();
        return update;
    }

    /**
     * Performs insertion of data to the db
     *
     * @param request HashMap Request containing request data
     * @return integer return type
     */
    public int InsertAll(HashMap request) {
        int update = 0;
        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String tableName = request.get("TABLE").toString();
            String values = request.get("VALUES").toString();
            String query = "INSERT INTO " + tableName + " (" + cols + ") values ( " + values + " )";
            update = DBconn.statementnav.executeUpdate(query);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception " + sw.toString(), "", field3);
            update = 0;
        }
        DBconn.close();
        return update;
    }

    /**
     * performs deletion of data in the database
     *
     * @param request HashMap containing request data
     * @return integer return type
     */
    public int Delete(HashMap request) {
        int update = 0;
        try {
            getDBconn();

            String tableName = request.get("TABLE").toString();
            String where = request.get("WHERE").toString();
            String query = "DELETE FROM " + tableName + " WHERE " + where + "";
            update = DBconn.statementnav.executeUpdate(query);

        } catch (SQLException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString() + "Error: Exception " + sw.toString(), "", field3);
            update = 0;
        }
        DBconn.close();
        return update;
    }
}
