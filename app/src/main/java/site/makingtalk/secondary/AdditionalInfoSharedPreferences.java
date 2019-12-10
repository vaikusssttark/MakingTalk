package site.makingtalk.secondary;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdditionalInfoSharedPreferences {
    private static final String ADDITIONAL_INFO_PREFERENCES = "AdditionalInfoPreferences";
    private static final String ADDITIONAL_INFO_PREFERENCES_NAME = "AdditionalInfoPreferencesName";
    private static final String ADDITIONAL_INFO_PREFERENCES_DESCRIPTION = "AdditionalInfoPreferencesDescription";
    private static final String ADDITIONAL_INFO_PREFERENCES_LIKED_ARTICLES_IDS = "AdditionalInfoPreferencesLikedArticlesId";

    public static void savePrefs(String name, String description, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AdditionalInfoSharedPreferences.ADDITIONAL_INFO_PREFERENCES_NAME, name);
        editor.putString(AdditionalInfoSharedPreferences.ADDITIONAL_INFO_PREFERENCES_DESCRIPTION, description);
        editor.putStringSet(AdditionalInfoSharedPreferences.ADDITIONAL_INFO_PREFERENCES_LIKED_ARTICLES_IDS, new HashSet<String>());
        editor.apply();
    }

    public static boolean containsPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ADDITIONAL_INFO_PREFERENCES_NAME);
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ADDITIONAL_INFO_PREFERENCES_NAME, "");
    }

    public static void updateName(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ADDITIONAL_INFO_PREFERENCES_NAME, name);
        editor.apply();
    }

    public static String getDescription(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ADDITIONAL_INFO_PREFERENCES_DESCRIPTION, "");
    }

    public static void updateDescription(String description, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ADDITIONAL_INFO_PREFERENCES_DESCRIPTION, description);
        editor.apply();
    }

    public static ArrayList<Integer> getLikedArticlesIds(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> likedArticlesString = sharedPreferences.getStringSet(ADDITIONAL_INFO_PREFERENCES_LIKED_ARTICLES_IDS, null);
        ArrayList<Integer> likedArticlesInt = new ArrayList<>();

        if (likedArticlesString != null) {
            for (String s : likedArticlesString) {
                likedArticlesInt.add(Integer.parseInt(s));
            }
            return likedArticlesInt;
        } else
            return null;

    }

    public static void addArticleIdInLiked(int articleId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Integer> oldSet = AdditionalInfoSharedPreferences.getLikedArticlesIds(context);
        if (oldSet != null) {
            oldSet.add(articleId);
        } else {
            oldSet = new ArrayList<>();
            oldSet.add(articleId);
        }
        Set<String> newSet = new HashSet<>();
        for (Integer integer : oldSet) {
            newSet.add(integer.toString());
        }
        editor.putStringSet(ADDITIONAL_INFO_PREFERENCES_LIKED_ARTICLES_IDS, newSet);
        editor.apply();
    }

    public static void removeArticleIdInLiked(int articleId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Integer> oldSet = AdditionalInfoSharedPreferences.getLikedArticlesIds(context);
        oldSet.remove((Integer) articleId);

        Set<String> newSet = new HashSet<>();
        for (Integer integer : oldSet) {
            newSet.add(integer.toString());
        }
        editor.putStringSet(ADDITIONAL_INFO_PREFERENCES_LIKED_ARTICLES_IDS, newSet);
        editor.apply();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ADDITIONAL_INFO_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
