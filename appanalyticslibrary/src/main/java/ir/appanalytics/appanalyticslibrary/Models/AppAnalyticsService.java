package ir.appanalytics.appanalyticslibrary.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by amir on 5/26/16.
 */
public interface AppAnalyticsService {
    @GET("androidservice/initialize/{uuid}")
    Call<SimpleResult> initializing(@Path("uuid") String uuid);

    @Deprecated
    @GET("androidservice/addevent/{uuid}/{name}/{value}")
    Call<SimpleResult> addEvent(@Path("uuid") String uuid,@Path("name") String name,@Path("value") String value);

    @POST("androidservice/addevent/{uuid}")
    Call<SimpleResult> addEvent(@Path("uuid") String uuid, @Body AppAnalyticsEvent event);

    @POST("androidservice/setdeviceinfo/{uuid}")
    Call<SimpleResult> setDeviceInfo(@Path("uuid") String uuid, @Body DeviceInfo deviceInfo);
}
