package site.makingtalk.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DBHelper {
    private static DBHelper mInstance;
    private static final String URL_BASE = "http://makingtalk.site/";
    private Retrofit mRetrofit;

    private DBHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static DBHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DBHelper();
        }
        return mInstance;
    }

    public UserMainInfoMakingTalkAPI getUserMainInfoMakingTalkAPI() {
        return mRetrofit.create(UserMainInfoMakingTalkAPI.class);
    }

    public UserAddInfoMakingTalkAPI getUserAddInfoMakingTalkAPI() {
        return mRetrofit.create(UserAddInfoMakingTalkAPI.class);
    }

    public UserPrivacyMakingTalkAPI getUserPrivacyMakingTalkAPI () {
        return mRetrofit.create(UserPrivacyMakingTalkAPI.class);
    }

    public ArticleListMakingTalkAPI getArticleListMakingTalkAPI () {
        return mRetrofit.create(ArticleListMakingTalkAPI.class);
    }
}

