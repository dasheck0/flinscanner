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

    public enum LIFE_STATE {
        UNKNOWN,
        A_LIVE,
        DEAD,

        NUMBER_OF_STATES
    }

    private String hostName;

    private String hostAddress;

    private String hardwareAddress;

    private String vendor;

    private HOST_ITEM_TYPE type;

    private LIFE_STATE state;

    public HostListItem(String hostName, String hostAddress) {
        this.hostName = hostName;
        this.hostAddress = hostAddress;

        hardwareAddress = "";
        vendor = "";

        type = HOST_ITEM_TYPE.UNKNOWN;
        state = LIFE_STATE.UNKNOWN;
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

    public LIFE_STATE getState() {
        return state;
    }

    public void setState(LIFE_STATE state) {
        this.state = state;
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
