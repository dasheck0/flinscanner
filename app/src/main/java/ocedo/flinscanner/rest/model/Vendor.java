package ocedo.flinscanner.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by stefan on 22.04.15.
 */
public class Vendor {

    @SerializedName("startHex")
    private String startHex;

    @SerializedName("endHex")
    private String endHex;

    @SerializedName("startDec")
    private String startDec;

    @SerializedName("endDec")
    private String endDec;

    @SerializedName("company")
    private String company;

    @SerializedName("addressL1")
    private String addressL1;

    @SerializedName("addressL2")
    private String addressL2;

    @SerializedName("addressL3")
    private String addressL3;

    @SerializedName("country")
    private String country;

    @SerializedName("type")
    private String type;

    public Vendor(String startHex, String endHex, String startDec, String endDec, String company, String addressL1, String addressL2, String addressL3, String country, String type) {
        this.startHex = startHex;
        this.endHex = endHex;
        this.startDec = startDec;
        this.endDec = endDec;
        this.company = company;
        this.addressL1 = addressL1;
        this.addressL2 = addressL2;
        this.addressL3 = addressL3;
        this.country = country;
        this.type = type;
    }

    public String getStartHex() {
        return startHex;
    }

    public void setStartHex(String startHex) {
        this.startHex = startHex;
    }

    public String getEndHex() {
        return endHex;
    }

    public void setEndHex(String endHex) {
        this.endHex = endHex;
    }

    public String getStartDec() {
        return startDec;
    }

    public void setStartDec(String startDec) {
        this.startDec = startDec;
    }

    public String getEndDec() {
        return endDec;
    }

    public void setEndDec(String endDec) {
        this.endDec = endDec;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddressL1() {
        return addressL1;
    }

    public void setAddressL1(String addressL1) {
        this.addressL1 = addressL1;
    }

    public String getAddressL2() {
        return addressL2;
    }

    public void setAddressL2(String addressL2) {
        this.addressL2 = addressL2;
    }

    public String getAddressL3() {
        return addressL3;
    }

    public void setAddressL3(String addressL3) {
        this.addressL3 = addressL3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "startHex='" + startHex + '\'' +
                ", endHex='" + endHex + '\'' +
                ", startDec='" + startDec + '\'' +
                ", endDec='" + endDec + '\'' +
                ", company='" + company + '\'' +
                ", addressL1='" + addressL1 + '\'' +
                ", addressL2='" + addressL2 + '\'' +
                ", addressL3='" + addressL3 + '\'' +
                ", country='" + country + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
