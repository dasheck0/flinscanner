package ocedo.flinscanner.rest.service;

import ocedo.flinscanner.rest.model.VendorList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by stefan on 22.04.15.
 */
public interface MacVendorLookupAPIService {

    @GET("/{mac-address}")
    public void getVendorFromHardwareAddress(@Path("mac-address") String mac_address,
                                             Callback<VendorList> callback);
}
