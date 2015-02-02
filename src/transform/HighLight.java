package transform;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jiarui on 2015/1/25.
 */
public class HighLight {
    String code = "";
    String color = "#00688B";
    public void Login(String filepath) throws FileNotFoundException {
        File file = new File(filepath);
        Scanner input = new Scanner(file);
        String temp = null;
        code += "<html><body><pre>";
        //int i = 1;
        if (file.isFile() && file.exists()) {
            while (input.hasNext()) {
                String s = input.nextLine();
                //s = Change(s);

                temp = s;
                /*String t = temp.replace("//", "<font color=green>//");
                if (!temp.equals(t)) {
                    System.out.println("t:"+t);
                    temp = t + "</font>";
                }*/
                temp = temp.replaceAll("(\".*?[^\\\\]\")","<font color=green>$1</font>");
                temp = temp.replace("/*", "<font color=green>/*");
                temp = temp.replace("*/", "*/</font>");
                temp += "</br>";
                code += temp;
                //i++;
            }
            code += "</html></body></pre>";
            code = Change(code);

            code = code.replaceAll("(//.*?)</br>","<font color=green>$1</font></br>");
        } else {
            System.out.println("文件不存在");
        }
        input.close();
    }
    public String Change(String temp){
        key k = new key();
        ArrayList<String> s = k.getJavakey();
        for(int i = 0; i < s.size(); i++) {
            temp = temp.replaceAll("\\b" + s.get(i) + "\\b", "<b><font color='"+color+"'>" + s.get(i) + "</font></b>");
            //temp = temp.replaceAll("//.*?(<b><font color='"+color+"'>" + s.get(i) + "</font></b>)",s.get(i));
        }
        return temp;
    }
    public void WriteToFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        PrintWriter output = new PrintWriter(file);
        output.print(code);
        System.out.println(code);
        output.close();
    }
    public static void main(String[] args) throws IOException {
        HighLight highLight = new HighLight();
        highLight.Login("C:\\Users\\Jiarui\\Desktop\\Card1.java");
        highLight.WriteToFile("C:\\Users\\Jiarui\\Desktop\\123.html");
        System.out.println("OK");
    }
}