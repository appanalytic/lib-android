/**
 * Class for initializing AppAnalytics library
 *
 * This class is used for initializing AppAnalytics Library
 *
 * @author Amir
 * @version 2016.0228
 */

package ir.appanalytics.appanalyticslibrary;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.appanalytics.appanalyticslibrary.Models.DeviceInfo;
import ir.appanalytics.appanalyticslibrary.Models.SimpleResult;
import ir.appanalytics.appanalyticslibrary.Models.AppAnalyticsService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppAnalytics {

    /**
     *  For using in functions that use context
     */
    private Context _ctx;

    /**
     *  User AccessKey
     */
    private static String _accessKey;

    /**
     * Base URL of AppAnalytics API
     */
    private static String baseUrl = "http://appanalytics.ir/api/v1/";//get device

    private static Retrofit client;



    /**
     * Initializing Method of AppAnanlytics
     *
     * @param ctx get a context
     * @param accessKey AccessKey that you can find it in your AppAnanlytics Dashboard
     * @return Void
     */
    public AppAnalytics(Context ctx,String accessKey){

        Log.d("Msg:", "Construct");

        _ctx = ctx;
        _accessKey = accessKey;


        //make client ready with access_key in header for every request
        //more info in http://stackoverflow.com/a/34678380/2629645
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Access-Key", _accessKey)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public void SubmitCampaign() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_ctx);
        if (!prefs.getBoolean("firstTimeAppAnalytics"+_accessKey, false)) {
            // <---- run your one time code here

            Log.d("Msg:", "Submit Start");

            Log.d("Msg:", "Retrofit Built2");
            AppAnalyticsService appService = client.create(AppAnalyticsService.class);
            Log.d("Msg:", "Retrofit appService");
            Log.d("Msg:Android ID ", Settings.Secure.getString(_ctx.getContentResolver(), Settings.Secure.ANDROID_ID));

            String uuid = Settings.Secure.getString(_ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

            Call<SimpleResult> call = appService.initializing(uuid);

            Log.d("Msg:", call.request().url().toString());
            try {
                call.enqueue(new Callback<SimpleResult>() {
                    @Override
                    public void onResponse(Call<SimpleResult> call, Response<SimpleResult> response) {
                        Log.d("initializing:", "Done:" + response.body().status);
                    }

                    @Override
                    public void onFailure(Call<SimpleResult> call, Throwable t) {
                        Log.d("initializing:", "Error:"+t.getMessage());
                    }
                });
            }catch (Exception exc){
                Log.d("Exception:", exc.toString());
            }

            DeviceInfo df = new DeviceInfo();
            try{
                TelephonyManager tMgr = (TelephonyManager)_ctx.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();

                df.os_version = System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                df.os_api_level =  android.os.Build.VERSION.SDK_INT;
                df.device = android.os.Build.DEVICE;
                df.model = android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

                df.release = android.os.Build.VERSION.RELEASE;
                df.brand = android.os.Build.BRAND;
                df.display = android.os.Build.DISPLAY;
                df.cpu_abi = android.os.Build.CPU_ABI;
                df.cpu_abi2 = android.os.Build.CPU_ABI2;
                df.hardware = android.os.Build.HARDWARE;
                df.buid_id = android.os.Build.ID;
                df.manufacturer = android.os.Build.MANUFACTURER;
                df.serial = android.os.Build.SERIAL;
                df.user = android.os.Build.USER;
                df.host = android.os.Build.HOST;
                df.phone_number = mPhoneNumber;

            }catch (Exception e){

            }

            Call<SimpleResult> callDeviceInfo = appService.setDeviceInfo(uuid,df);
            Log.d("Msg:setDeviceInfo:", callDeviceInfo.request().url().toString());
            Log.d("Msg:setDeviceInfo:", bodyToString(callDeviceInfo.request().body()));
            try {
                callDeviceInfo.enqueue(new Callback<SimpleResult>() {
                    @Override
                    public void onResponse(Call<SimpleResult> callDeviceInfo, Response<SimpleResult> response) {
                        Log.d("Msg:setDeviceInfo:", "Done:" + response.body().status);
                    }

                    @Override
                    public void onFailure(Call<SimpleResult> callDeviceInfo, Throwable t) {
                        Log.d("Msg:setDeviceInfo:", "Error:"+t.getMessage());
                    }
                });
            }catch (Exception exc){
                Log.d("Exception:", exc.toString());
            }
            Log.d("Msg:", "End");

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimeAppAnalytics"+_accessKey, true);
            editor.commit();
        }

    }

    public void SubmitEvent(String event_name,String event_value){
        Log.d("Msg:", "Submit event Start");

        Log.d("Msg:", "Retrofit Built2");
        AppAnalyticsService appService = client.create(AppAnalyticsService.class);
        Log.d("Msg:", "Retrofit appService");
        Log.d("Msg:Android ID ", Settings.Secure.getString(_ctx.getContentResolver(), Settings.Secure.ANDROID_ID));

        String uuid = Settings.Secure.getString(_ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

        Call<SimpleResult> call = appService.addEvent(uuid,event_name,event_value);

        Log.d("Msg:", call.request().url().toString());
        try {
            call.enqueue(new Callback<SimpleResult>() {
                @Override
                public void onResponse(Call<SimpleResult> call, Response<SimpleResult> response) {
                    Log.d("Msg:", "Done:" + response.body().status);
                }

                @Override
                public void onFailure(Call<SimpleResult> call, Throwable t) {
                    Log.d("Msg:", "Error:"+t.getMessage());
                }
            });
        }catch (Exception exc){
            Log.d("Exception:", exc.toString());
        }
        Log.d("Msg:", "End");
    }
    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}