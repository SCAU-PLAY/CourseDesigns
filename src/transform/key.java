package transform;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by hongyeah on 2015/1/25.
 */
public class key {
    public key(){

    }

    BufferedReader br = null;
    InputStreamReader isr = null;
    public ArrayList<String> getJavakey() {

            ArrayList<String> keys = new ArrayList<String>();
        try {
            File file = new File("keys.txt");
            if (file.isFile() && file.exists()) {
                isr = new InputStreamReader(new FileInputStream(file));
                br = new BufferedReader(isr);
                String get = null;
                while ((get = br.readLine()) != null) {
                    keys.add(get);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
}
