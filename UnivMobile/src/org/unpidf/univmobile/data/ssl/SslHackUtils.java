package org.unpidf.univmobile.data.ssl;


import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SslHackUtils {

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            Log.i("SslHackUtils", "hostname " + hostname);
            return true;
        }
    };

    /**
     *
     */
    public final static X509TrustManager TRUST_MANAGER = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    /**
     *
     */
    public static void trustAllHosts() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
            SSLContext sc = getSslContext();
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param client
     * @return
     */
    public static HttpClient getSslClient(HttpClient client) {
        try {
            SSLContext sc = getSslContext();
            SSLSocketFactory ssf = new TrustAllSSLSocketFactory(sc);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 8443));
            sr.register(new Scheme("https", ssf, 443));

            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     *
     */
    public static SSLContext getSslContext() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{TRUST_MANAGER}, null);
            return ctx;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}