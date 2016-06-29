package ir.appanalytics.appanalyticslibrary.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amir on 6/28/16.
 */
public class DeviceInfo {
    @SerializedName("OS Version")
    public String os_version;

    @SerializedName("OS API Level")
    public int os_api_level;

    @SerializedName("Device")
    public String device;

    @SerializedName("Model (and Product)")
    public String model;

    @SerializedName("RELEASE")
    public String release;

    @SerializedName("BRAND")
    public String brand;

    @SerializedName("DISPLAY")
    public String display;

    @SerializedName("CPU_ABI")
    public String cpu_abi;

    @SerializedName("CPU_ABI2")
    public String cpu_abi2;

    @SerializedName("HARDWARE")
    public String hardware;

    @SerializedName("Build ID")
    public String buid_id;

    @SerializedName("MANUFACTURER")
    public String manufacturer;

    @SerializedName("SERIAL")
    public String serial;

    @SerializedName("USER")
    public String user;

    @SerializedName("HOST")
    public String host;

    @SerializedName("Phone Number")
    public String phone_number;
}
