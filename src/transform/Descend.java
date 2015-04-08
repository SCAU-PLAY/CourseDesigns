package transform;

/*
 * 明天用文件来储存数据
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Descend
{

	public Descend(){}
public void WriteToFile(String  W)
{
	    File file= new File("E:/wordsize.txt");
	    try
		{
			PrintWriter output = new PrintWriter(file);
			output.print(W.substring(0, 2));
			output.close();
		} catch (FileNotFoundException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
}
public String ReadFromFile()
{
	File file = new File("E:/wordsize.txt");
	if(!file.exists())
	{
		JOptionPane.showMessageDialog(null, "文件不存在");
		System.exit(0);
	}
	String hahaString=null;
	try
	{
		Scanner input = new Scanner(file);
		hahaString = input.next();
		input.close();
	} catch (FileNotFoundException e)
	{
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	return hahaString;
	
}

}
