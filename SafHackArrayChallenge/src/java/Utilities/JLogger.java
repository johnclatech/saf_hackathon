package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author jkaru
 * @version 1.0 Free
 */
public class JLogger {

    Logs log = new Logs();
    public String logtext = "";
    private String APPNAME = "";
    BufferedWriter bw = null;
    FileWriter fw = null;
    String field3 = "3";

    String LOG_DIR = configUtils.settings.get("logESB");

    public void JLogger(String TypeofLog, String filename, String text) {
        logtext = text;
        String Extension = ".log";
        filename = filename + Extension;
        APPNAME = TypeofLog;

        try {
            SimpleDateFormat timexf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
            String timex = timexf.format(new Date());
            SimpleDateFormat todaydateformat = new SimpleDateFormat("dd-MMM-yyyy");
            String Today = todaydateformat.format(new Date());
            String date_dir_name = todaydateformat.format(new Date());
            String todaysfolder = LOG_DIR + "/" + APPNAME + "/" + date_dir_name;
            File new_dir = new File(todaysfolder);
            if (!new_dir.exists()) {
                FileUtils.forceMkdir(new_dir);
            }
            String content = "\n\n------------------------------ " + timex + " ------------------------------------------\n\n";
            content += "\n" + logtext + "\n";

            fw = new FileWriter(new_dir + "/" + filename, true);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.log(Logs.logPreString() + "Error: Exception \n" + sw.toString(), "", field3);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }

            } catch (IOException ex) {

                System.out.println("EXCEPTION OCCURS");
                log.log(Logs.logPreString() + "Error: Exception \n" + ex.toString(), "", field3);

            }
        }
    }

}
