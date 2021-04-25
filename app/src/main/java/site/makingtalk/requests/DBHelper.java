package site.makingtalk.requests;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import site.makingtalk.requests.api.ArticleListAPI;
import site.makingtalk.requests.api.ArticlesParamsAPI;
import site.makingtalk.requests.api.ThemesAPI;
import site.makingtalk.requests.api.UserAddInfoAPI;
import site.makingtalk.requests.api.UserMainInfoAPI;
import site.makingtalk.requests.api.UserPrivacyAPI;
import site.makingtalk.requests.entities.ArticleParams;

public class DBHelper {
    private static DBHelper mInstance;
    private static final String URL_BASE = "http://a0495512.xsph.ru/";
    private Retrofit mRetrofit;

    private DBHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static DBHelper getInstance() {
        if (mInstance == null) {
            Log.d("http_requests", "4");
            mInstance = new DBHelper();
        }
        return mInstance;
    }

    public UserMainInfoAPI getUserMainInfoAPI() {
        return mRetrofit.create(UserMainInfoAPI.class);
    }

    public UserAddInfoAPI getUserAddInfoAPI() {
        return mRetrofit.create(UserAddInfoAPI.class);
    }

    public UserPrivacyAPI getUserPrivacyAPI() {
        return mRetrofit.create(UserPrivacyAPI.class);
    }

    public ArticleListAPI getArticleListAPI() {
        return mRetrofit.create(ArticleListAPI.class);
    }

    public ThemesAPI getThemesAPI() {
        return mRetrofit.create(ThemesAPI.class);
    }

    public ArticlesParamsAPI getArticleParamsAPI() {
        return mRetrofit.create(ArticlesParamsAPI.class);
    }


}

