package ocedo.flinscanner.lists;

import ocedo.flinscanner.R;

/**
 * Created by stefan on 21.04.15.
 */
public class HostListItem {

    public enum HOST_ITEM_TYPE {
        UNKNOWN,
        MOBILE,
        DESKTOP,

        NUMBER_OF_TYPES
    };

    private String hostName;

    private String hostAddress;

    private String hardwareAddress;

    private String vendor;

    private HOST_ITEM_TYPE type;

    public HostListItem(String hostName, String hostAddress) {
        this.hostName = hostName;
        this.hostAddress = hostAddress;

        hardwareAddress = "";
        vendor = "";

        type = HOST_ITEM_TYPE.UNKNOWN;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getHardwareAddress() {
        return hardwareAddress;
    }

    public void setHardwareAddress(String hardwareAddress) {
        this.hardwareAddress = hardwareAddress;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public HOST_ITEM_TYPE getType() {
        return type;
    }

    public void setType(HOST_ITEM_TYPE type) {
        this.type = type;
    }

    public int getDrawableIDForType() {
        switch(type) {
            case UNKNOWN:
                return R.drawable.charging1;
            case DESKTOP:
                return R.drawable.monitor2;
            case MOBILE:
                return R.drawable.smartphone66;
            default:
                return R.drawable.charging1;
        }
    }
}
