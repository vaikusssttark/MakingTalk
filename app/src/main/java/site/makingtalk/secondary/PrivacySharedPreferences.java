package site.makingtalk.secondary;

import android.content.Context;
import android.content.SharedPreferences;

public class PrivacySharedPreferences {

    private static final String PRIVACY_PREFERENCES = "PrivacyPreferences";
    private static final String PRIVACY_PREFERENCES_LOGIN = "PrivacyPreferencesLogin";
    private static final String PRIVACY_PREFERENCES_EMAIL = "PrivacyPreferencesEmail";
    private static final String PRIVACY_PREFERENCES_NAME = "PrivacyPreferencesName";
    private static final String PRIVACY_PREFERENCES_DESCRIPTION = "PrivacyPreferencesDescription";
    private static final String PRIVACY_PREFERENCES_PROGRESS = "PrivacyPreferencesProgress";

    public static void savePrefs(int login, int email, int name, int description, int progress, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PrivacySharedPreferences.PRIVACY_PREFERENCES_LOGIN, login);
        editor.putInt(PrivacySharedPreferences.PRIVACY_PREFERENCES_EMAIL, email);
        editor.putInt(PrivacySharedPreferences.PRIVACY_PREFERENCES_NAME, name);
        editor.putInt(PrivacySharedPreferences.PRIVACY_PREFERENCES_DESCRIPTION, description);
        editor.putInt(PrivacySharedPreferences.PRIVACY_PREFERENCES_PROGRESS, progress);

        editor.apply();
    }

    public static boolean containsPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.contains(PRIVACY_PREFERENCES_LOGIN);
    }

    public static int getLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PRIVACY_PREFERENCES_LOGIN, -1);
    }

    public static void updateLogin(int login, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PRIVACY_PREFERENCES_LOGIN, login);
        editor.apply();
    }

    public static int getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PRIVACY_PREFERENCES_EMAIL, -1);
    }

    public static void updateEmail(int email, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PRIVACY_PREFERENCES_EMAIL, email);
        editor.apply();
    }

    public static int getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PRIVACY_PREFERENCES_NAME, -1);
    }

    public static void updateName(int name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PRIVACY_PREFERENCES_NAME, name);
        editor.apply();
    }

    public static int getDescription(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PRIVACY_PREFERENCES_DESCRIPTION, -1);
    }

    public static void updateDescription(int description, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PRIVACY_PREFERENCES_DESCRIPTION, description);
        editor.apply();
    }

    public static int getProgress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PRIVACY_PREFERENCES_PROGRESS, -1);
    }

    public static void updateProgress(int progress, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PRIVACY_PREFERENCES_PROGRESS, progress);
        editor.apply();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRIVACY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
