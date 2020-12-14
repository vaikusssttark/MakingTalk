package site.makingtalk.requests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkManager {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo.isConnected()) {
            System.out.println("Connected");
        } else {
            System.out.println("not connected");
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
