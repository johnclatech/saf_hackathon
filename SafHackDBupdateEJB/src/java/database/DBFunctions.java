package database;

import Utilities.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

    public JSONObject queryOne(HashMap request) {
        JSONObject data = new JSONObject();

        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String query = "SELECT " + cols + " FROM " + request.get("TABLE") + " WHERE " + request.get("WHERE");
            DBconn.resultset = DBconn.statement.executeQuery(query);
            ResultSetMetaData md = DBconn.resultset.getMetaData();
            int columns = md.getColumnCount();
            while (DBconn.resultset.next()) {
                for (int i = 1; i <= columns; ++i) {
                    data.put(md.getColumnName(i).toLowerCase(), DBconn.resultset.getString(i));
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return data;
    }

//    public boolean exeCreateAccount(HashMap<String, String> adetails) {
//        boolean succ = false;
//        String SP_MOVE_EFTBULK_TOFLEX = "{call SP_CREATE_PARTNER(?,?,?,?,?)}";
//        try {
//            getDBconn();
//            callableStatement = DBconn.connection.prepareCall(SP_MOVE_EFTBULK_TOFLEX);
//            callableStatement.setString("IV_BUSINESSNAME", adetails.get("BUSINESSNAME"));
//            callableStatement.setString("IV_REGISTERBY", adetails.get("FULLNAME"));
//            callableStatement.setString("IV_EMAIL", adetails.get("EMAIL"));
//            callableStatement.setString("IV_PASSWORD", adetails.get("PASSWORD"));
//            callableStatement.setString("IV_VAL_TOKEN", adetails.get("TOKEN"));
//            callableStatement.executeUpdate();
//            succ = true;
//        } catch (Exception e) {
//
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            logs.log("Adaptor : YouthFundESB, Method: getConnection, Java Class: Database, Error: " + sw.toString(), "", "3");
//
//        }
//        DBconn.close();
//        return succ;
//    }
    public HashMap queryAll(HashMap request) {
//        ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
        HashMap data = new HashMap();
        HashMap Newdata = new HashMap();

        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String tableName = request.get("TABLE").toString();
            String where = request.get("WHERE").toString();
            String query = "SELECT " + cols + " FROM " + tableName + " WHERE " + where;
//            System.out.println("database.DBFunctions.queryAll()" + query);
            DBconn.resultset = DBconn.statement.executeQuery(query);
            ResultSetMetaData md = DBconn.resultset.getMetaData();

            int columns = md.getColumnCount();
            int rows = DBconn.resultset.getRow();
            int j = 0;
            String MyColumn = "";
            String MyColumn1 = "";
            String Myvalue = "";
            while (DBconn.resultset.next()) {
                for (int i = 1; i <= columns; ++i) {
                    MyColumn = md.getColumnName(i);
                    MyColumn1 = md.getColumnName(i) + "" + j;
                    Myvalue = DBconn.resultset.getString(MyColumn);
                    data.put(MyColumn1.toLowerCase(), Myvalue);
                }

                j++;
//                System.out.println("HashMap: = "+data);
            }
//            System.out.println("Final HashMap: = "+data);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return data;
    }

    public int InsertAll(HashMap request) {
        int update = 0;
        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String tableName = request.get("TABLE").toString();
            String values = request.get("VALUES").toString();
            String query = "INSERT INTO " + tableName + "(" + cols + ")values(" + values + ")";

            update = DBconn.statement.executeUpdate(query);
        } catch (Exception e) {
            update = 0;
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return update;
    }

    public JSONObject queryAllJson(HashMap request) {
        JSONObject data = new JSONObject();
        try {
            getDBconn();
            String cols = request.get("COLS").toString();
            String query = "SELECT " + cols + " FROM " + request.get("TABLE");
            DBconn.resultset = DBconn.statement.executeQuery(query);
            ResultSetMetaData md = DBconn.resultset.getMetaData();
            int columns = md.getColumnCount();
            while (DBconn.resultset.next()) {
                for (int i = 1; i <= columns; ++i) {
                    data.put(md.getColumnName(i).toLowerCase(), DBconn.resultset.getString(i));
                }
            }

        } catch (Exception e) {

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return data;
    }

   

    public int Update(HashMap request) {
        int update = 0;
        try {
            getDBconn();
            String cols = request.get("SET").toString();
            String tableName = request.get("TABLE").toString();
            String values = request.get("WHERE").toString();
            String query = "UPDATE " + tableName + " SET " + cols + " WHERE " + values + "";

            update = DBconn.statement.executeUpdate(query);;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            update = 0;
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }

        DBconn.close();
        return update;
    }

    public boolean ExeQuery(String query) {
        boolean update = false;
        try {
            getDBconn();
            DBconn.statement.executeUpdate(query);
            update = true;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return update;
    }

    public boolean Delete(HashMap request) {
        boolean update = false;
        try {
            getDBconn();

            String tableName = request.get("TABLE").toString();
            String where = request.get("WHERE").toString();
            String query = "DELETE FROM " + tableName + " WHERE " + where + "";
            DBconn.statement.executeUpdate(query);
            update = true;
        } catch (SQLException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.log(Logs.logPreString()+"\nError: Exception : - \n" + sw.toString(), "", field3);
        }
        DBconn.close();
        return update;
    }

}
