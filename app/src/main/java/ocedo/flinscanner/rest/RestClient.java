package ocedo.flinscanner.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ocedo.flinscanner.rest.service.MacVendorLookupAPIService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by stefan on 22.04.15.
 */
public class RestClient {

    private final String baseUrl = "http://macvendorlookup.com/api/v2";

    private MacVendorLookupAPIService apiService;

    public RestClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(baseUrl)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = adapter.create(MacVendorLookupAPIService.class);
    }

    public MacVendorLookupAPIService getApiService() {
        return apiService;
    }
}
