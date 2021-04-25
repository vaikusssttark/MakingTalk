package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.GET;
import site.makingtalk.requests.entities.Themes;

public interface ThemesAPI {
    @GET("articles/get_themes.php")
    Call<Themes> getThemes();
}
