package nodomain.com.i_news.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import nodomain.com.i_news.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mukhamed.issa on 5/29/16.
 */

public class AppUtils {

    public static Typeface getTypeface(Context context){
        return Typeface.createFromAsset(context.getAssets(), "roboto.ttf");
    }

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isFirstRun(Context context) {
        return context.getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("isFirstRun", true);
    }

    public static void showErrorDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.error))
                .setMessage(context.getResources().getString(R.string.error_details))
                .setCancelable(true)
                .setNegativeButton(context.getResources().getString(R.string.close),
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showSnackbar(View view, Context context){
        Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.error_details),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction("Закрыть", v -> snackbar.dismiss());
        snackbar.show();
    }

}
