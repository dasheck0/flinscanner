package ocedo.flinscanner.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stefan on 22.04.15.
 */
public class VendorList {

    @SerializedName("")
    private List<Vendor> vendorList;

    public VendorList() {
    }

    public List<Vendor> getVendorList() {
        return vendorList;
    }

    public void setVendorList(List<Vendor> vendorList) {
        this.vendorList = vendorList;
    }
}
