package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.network;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.R;

public class SocketActivity extends Activity {

    private static final String IP = "ip";
    private static final String PORT = "port";

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getPreferences(MODE_PRIVATE);

        String sIP = settings.getString(IP, getString(R.string.txt_socket_default_ip));
        String sPort = settings.getString(PORT, getString(R.string.txt_socket_default_port));
        ClientTask task = new ClientTask(sIP, Integer.valueOf(sPort));

    }

    public class ClientTask extends AsyncTask<Void, Void, Void> {

        String ip;
        int port;
        String response;

        ClientTask(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Socket socket = null;

            try {
                socket = new Socket(ip, port);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            } catch (IOException ioe) {
                response = "IOException: " + ioe.toString();
                ioe.printStackTrace();
            } finally {
                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
