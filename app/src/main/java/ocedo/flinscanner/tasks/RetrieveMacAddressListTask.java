package ocedo.flinscanner.tasks;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

import ocedo.flinscanner.lists.HostListItem;
import ocedo.flinscanner.rest.RestClient;
import ocedo.flinscanner.rest.model.Vendor;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by stefan on 21.04.15.
 */
public class RetrieveMacAddressListTask {

    public static final String DEFAULT_MAC = "00:00:00:00:00:00";

    public RetrieveMacAddressListTask() {
    }

    public static String getHardwareAddressFromIP(String ip) {
        String result = DEFAULT_MAC;

        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = reader.readLine()) != null) {
               // Log.d("TAG", line);
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4 && ip.equals(splitted[0])) {
                    // Basic sanity check
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        result = mac;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(result.equals(DEFAULT_MAC)) {
            String alternativeMac = getHardwareAddressFromNetworkInterface(ip);
            if(!alternativeMac.equals(DEFAULT_MAC)) {
                result = alternativeMac;
            }
        }

        return result;
    }

    private static String getHardwareAddressFromNetworkInterface(String ip) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));

            if(network != null) {
                byte[] mac = network.getHardwareAddress();

                StringBuilder builder = new StringBuilder();

                for(int i = 0; i < mac.length; i ++) {
                    builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }

                return builder.toString();
            }

            return DEFAULT_MAC;

        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_MAC;
        }
    }

    public static String getVendorFromMacAddress(final HostListItem item) {
        String macAddress = item.getHardwareAddress();
        macAddress.replace(":", "-");

        Log.d("TAG", macAddress);

        RestClient client = new RestClient();
        client.getApiService().getVendorFromHardwareAddress(item.getHardwareAddress(), new Callback<List<Vendor>>() {
            @Override
            public void success(List<Vendor> vendorList, Response response) {
                Vendor vendor = vendorList.get(0);
                if(vendor != null) {
                    item.setVendor(vendor.getCompany());
                    item.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("TAG", error.toString());
            }
        });

        return "";
    }
}
