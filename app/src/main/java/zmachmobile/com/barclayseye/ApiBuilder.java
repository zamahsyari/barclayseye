package zmachmobile.com.barclayseye;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zmachmobile on 7/22/17.
 */

public class ApiBuilder{
    public static ApiService getService(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        return service;
    }
}
