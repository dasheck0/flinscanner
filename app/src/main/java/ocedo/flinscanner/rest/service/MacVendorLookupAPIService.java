package ocedo.flinscanner.rest.service;

import java.util.List;

import ocedo.flinscanner.rest.model.Vendor;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by stefan on 22.04.15.
 */
public interface MacVendorLookupAPIService {

    @GET("/{mac-address}")
    public void getVendorFromHardwareAddress(@Path("mac-address") String mac_address,
                                             Callback<List<Vendor>> callback);
}
