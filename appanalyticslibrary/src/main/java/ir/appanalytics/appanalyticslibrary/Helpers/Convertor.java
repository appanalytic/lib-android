package ir.appanalytics.appanalyticslibrary.Helpers;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by amir on 6/30/16.
 */
public class Convertor {
    public static String requestBodyToString(final RequestBody request) {
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
