package lk.payhere.androidsdk.util;

import com.google.firebase.perf.network.FirebasePerfUrlConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes7.dex */
public class NetworkHandler {
    public static String sendPost(String str, Map<String, String> map) throws IOException {
        String str2;
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) ((URLConnection) FirebasePerfUrlConnection.instrument(new URL(str).openConnection()));
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
            str2 = sb.toString();
        } else {
            str2 = "";
        }
        httpsURLConnection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
        dataOutputStream.writeBytes(str2);
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = httpsURLConnection.getResponseCode();
        PrintStream printStream = System.out;
        printStream.println("\nSending 'POST' request to URL : " + str);
        PrintStream printStream2 = System.out;
        printStream2.println("Post parameters : " + str2);
        PrintStream printStream3 = System.out;
        printStream3.println("Response Code : " + responseCode);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                stringBuffer.append(readLine);
            } else {
                bufferedReader.close();
                System.out.println(stringBuffer.toString());
                return stringBuffer.toString();
            }
        }
    }
}
