package transform;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by hongyeah on 2015/1/25.
 */
public class key {
    private int choice;
    public key(int choice){
        this.choice = choice;
    }

    BufferedReader br = null;
    InputStreamReader isr = null;
    public ArrayList<String> getJavakey() {

            ArrayList<String> keys = new ArrayList<String>();
        try {
            if(choice == 0) {
                File file = new File("C:\\Users\\Jiarui\\Desktop\\keys.txt");
                if (file.isFile() && file.exists()) {
                    isr = new InputStreamReader(new FileInputStream(file));
                    br = new BufferedReader(isr);
                    String get = null;
                    while ((get = br.readLine()) != null) {
                        keys.add(get);
                    }
                }
            }
            else if(choice == 1 || choice == 2){
                File file = new File("C:\\Users\\Jiarui\\Desktop\\keys1.txt");
                if (file.isFile() && file.exists()) {
                    isr = new InputStreamReader(new FileInputStream(file));
                    br = new BufferedReader(isr);
                    String get = null;
                    while ((get = br.readLine()) != null) {
                        keys.add(get);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
}
