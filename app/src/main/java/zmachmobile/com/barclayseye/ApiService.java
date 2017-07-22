package zmachmobile.com.barclayseye;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zmachmobile on 7/22/17.
 */

public interface ApiService {
    @GET("version")
    Call<Object> getVersion();

    @FormUrlEncoded
    @POST("nearest_branch")
    Call<Object> getNearestBranch(@Field("lat") Double latitude, @Field("longi") Double longitude, @Field("type") int type);

    @FormUrlEncoded
    @POST("direction")
    Call<Object> getDirection(@Field("lat_start") Double latitudeStart, @Field("long_start") Double longitudeStart, @Field("lat_end") Double latitudeEnd, @Field("long_end") Double longitudeEnd );

    @FormUrlEncoded
    @POST("request_uber")
    Call<Object> requestUber(@Field("lat_start") Double latitudeStart, @Field("long_start") Double longitudeStart, @Field("lat_end") Double latitudeEnd, @Field("long_end") Double longitudeEnd );
}
