package site.makingtalk.secondary;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthSharedPreferences {
    private static final String AUTH_PREFERENCES = "AuthPreferences";
    private static final String AUTH_PREFERENCES_ID = "AuthPreferencesID";
    private static final String AUTH_PREFERENCES_LOGIN = "AuthPreferencesLogin";
    private static final String AUTH_PREFERENCES_EMAIL = "AuthPreferencesEmail";
    private static final String AUTH_PREFERENCES_PWD = "AuthPreferencesPwd";

    public static void savePrefs(int id, String login, String email, String pwd, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AUTH_PREFERENCES_ID, id);
        editor.putString(AuthSharedPreferences.AUTH_PREFERENCES_LOGIN, login);
        editor.putString(AuthSharedPreferences.AUTH_PREFERENCES_EMAIL, email);
        editor.putString(AuthSharedPreferences.AUTH_PREFERENCES_PWD, MD5.encode(pwd));
        editor.apply();
    }

    public static boolean containsPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.contains(AUTH_PREFERENCES_LOGIN);
    }

    public static int getId(Context context) {
        if (AuthSharedPreferences.containsPrefs(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(AUTH_PREFERENCES_ID, -1);
        } else
            return -1;
    }

    public static String getLogin(Context context) {
        if (AuthSharedPreferences.containsPrefs(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
            return sharedPreferences.getString(AUTH_PREFERENCES_LOGIN, "");
        } else
            return null;
    }

    public static void updateLogin(String login, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_PREFERENCES_LOGIN, login);
        editor.apply();
    }

    public static String getEmail(Context context) {
        if (AuthSharedPreferences.containsPrefs(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
            return sharedPreferences.getString(AUTH_PREFERENCES_EMAIL, "");
        } else
            return null;
    }

    public static void updateEmail(String email, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_PREFERENCES_EMAIL, email);
        editor.apply();
    }

    public static String getPwd(Context context) {
        if (AuthSharedPreferences.containsPrefs(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
            return sharedPreferences.getString(AUTH_PREFERENCES_PWD, "");
        } else
            return null;
    }

    public static void updatePwd(String pwd, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_PREFERENCES_PWD, MD5.encode(pwd));
        editor.apply();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        AdditionalInfoSharedPreferences.clear(context);
        PrivacySharedPreferences.clear(context);
    }

}
