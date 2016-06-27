package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.network;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.R;

public class SocketActivity extends Activity {

    private static final String IP = "ip";
    private static final String PORT = "port";
    private static final String SSL = "ssl";

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        String sIP = settings.getString(IP, getResources().getString(R.string.txt_socket_default_ip));
        String sPort = settings.getString(PORT, getResources().getString(R.string.txt_socket_default_port));
        ClientTask task = new ClientTask(sIP, Integer.valueOf(sPort), settings.getBoolean(SSL, false));

    }

    public class ClientTask extends AsyncTask<Void, Void, Void> {

        String ip;
        int port;
        boolean ssl;
        String response;

        ClientTask(String ip, int port, boolean ssl) {
            this.ip = ip;
            this.port = port;
            this.ssl = ssl;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if(ssl) {
                SSLSocket sslSocket = null;
                try {
                    SocketFactory socketFactory = SSLSocketFactory.getDefault();
                    sslSocket = (SSLSocket) socketFactory.createSocket(ip, port);
                    HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                    SSLSession sslSession = sslSocket.getSession();

                    // Verify that the certificate hostname is for destination
                    if(!hostnameVerifier.verify("s", sslSession)) {
                        throw new SSLHandshakeException("Expected ..." + sslSession.getPeerPrincipal());
                    }
                    //Code


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (sslSocket != null) {
                        try {
                            sslSocket.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }

            } else {
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
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
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
