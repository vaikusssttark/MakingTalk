package site.makingtalk.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DBHelper {
    private static DBHelper mInstance;
    private static final String URL_BASE = "http://a0339016.xsph.ru/";
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

    public TestTesterAPI getTesterAPI() {
        return mRetrofit.create(TestTesterAPI.class);
    }
}

