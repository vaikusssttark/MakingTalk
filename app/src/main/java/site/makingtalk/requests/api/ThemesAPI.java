package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import site.makingtalk.requests.entities.Themes;

public interface ThemesAPI {
    @GET("articles/get_themes_by_channel_id.php")
    Call<Themes> getThemesByChannelId(@Query("channel_id") String channelId);
}
