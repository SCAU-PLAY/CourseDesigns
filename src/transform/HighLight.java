package transform;

import javax.swing.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jiarui on 2015/1/25.
 */
public class HighLight {
	
	String code = "";
    //String color = "#00688B";
    String color = "Blue";  //设置高量关键字颜色

    public HighLight() {
    }

    public void Login(String filepath,int choice) throws FileNotFoundException {
        File file = new File(filepath);
        Scanner input = new Scanner(file);
        File file1 = new File(filepath);
        Scanner input1 = new Scanner(file1);
        String temp = null;
        String t = "<html>"+"<font size=\""+new Descend().ReadFromFile()+"\">"+"<body><pre><head>" +
                "<style>" +
                "#left{float:left;border:2px:width:0}" +
                "#right{float:left;border:2px;width:0}" +
                "</style>" +
                "</head>";
        int i = 1;
        if (file1.isFile() && file1.exists()) {
            while(input1.hasNext()){
                String ss = input1.nextLine();
                i++;
            }
        }
        int n = i;
        String count = "";
        code += "<div id=\"left\">";
        while(i!=0){
            i = i/10;
            count += "0";
        }
        DecimalFormat df=new DecimalFormat(count);
        for(i = 1; i < n; i++){
            code += df.format(i) + "  </br>";
        }
        code += "</div><div id=\"right\">";
        if (file.isFile() && file.exists()) {
            int j = 1;
            while (input.hasNext()) {
                String s = input.nextLine();
                //s = Change(s);
                temp = s;
                /*String t = temp.replace("//", "<font color=green>//");
                if (!temp.equals(t)) {
                    System.out.println("t:"+t);
                    temp = t + "</font>";
                }*/
                temp = temp.replace("<","&lt;");
                temp = temp.replace(">","&gt;");
                temp = temp.replaceAll("(\".*?[^\\\\]\")","<font color=green>$1</font>");
                if(choice == 2){
                    temp = temp.replace("/*", "<font color=Gray >/*");
                    temp = temp.replace("*/", "*/</font>");
                }
                else if(choice == 0 || choice == 1) {
                    temp = temp.replace("/*", "<font color=green>/*");
                    temp = temp.replace("*/", "*/</font>");
                }
                temp += "</br>";
                temp = Change(temp,choice);
                code += temp;
                j++;
            }
            code += "</div></body>"+"</font>"+"</pre></html>";
            if(choice == 1 || choice == 2){
                code = code.replaceAll("(#.*?)</br>","<font color=green>$1</font></br>");
                System.out.println("OK");
            }
            code = code.replaceAll("(//.*?)</br>","<font color=green>$1</font></br>");
            code = t + code;
        } else {
            System.out.println("文件不存在");
        }
        input.close();
    }

    /**
     * 根据不同的语言获取关键字，并对关键字进行高量
     * @param temp  源代码行
     * @param choice 不同的程序语言类型
     * @return 返回高量后的HTML语法代码
     */
    public String Change(String temp,int choice){
        key k = new key(choice);
        ArrayList<String> s = k.getJavakey();
        int is = 0;
        if(temp.matches("^//.*?$")){
        	System.out.println("Ok");
        	is = 1;
        }
        for(int i = 0; i < s.size(); i++) {
            temp = temp.replaceAll("\\b" + s.get(i) + "\\b", "<b><font color='"+color+"'>" + s.get(i) + "</font></b>");
        }
        if(is == 1){
        	temp = temp.replaceAll("(\'.*?[^\\\\]\')","");
        	//temp.replaceAll("<font color='" + color + "'>","<font>");
        }
        return temp;
    }

    /**
     * 将转化后代码写入相应的文件
     * @param path  写入文件路径
     * @throws IOException
     */
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
}