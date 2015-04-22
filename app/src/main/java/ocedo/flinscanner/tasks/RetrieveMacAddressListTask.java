package ocedo.flinscanner.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by stefan on 21.04.15.
 */
public class RetrieveMacAddressListTask extends AsyncTask<Void, Integer, HashMap<String, String>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("TAG", "GOOO");
    }

    @Override
    protected void onPostExecute(HashMap<String, String> stringStringHashMap) {
        super.onPostExecute(stringStringHashMap);
        Log.d("TAG", "FiNALLY");
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
        Log.d("TAG", "HJFKSDHJKL");

        HashMap<String, String> result = new HashMap<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("TAG", line);

                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    // Basic sanity check
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        result.put(splitted[0], mac);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("TAG", "Done");

        return result;
    }
}
