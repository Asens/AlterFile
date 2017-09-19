package cn.asens.util;

import cn.asens.controller.SampleController;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Asens
 * create 2017-09-19 0:44
 **/

public class TomcatUtils {
    private static Logger log = LogManager.getLogger(TomcatUtils.class);

    public final static String RELOAD_URL = "http://localhost:8080/manager/text/reload?path=/docs";

    private final static String USERNAME = "Asens";

    private final static String PASSWORD = "aaaaaa";

    public static boolean reload() throws IOException {
        URL url = new URL(RELOAD_URL);
        String userPassword = USERNAME + ":" + PASSWORD;
        String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encoding);
        int statusCode = conn.getResponseCode();
        return statusCode == HttpStatus.SC_OK;
    }


}
