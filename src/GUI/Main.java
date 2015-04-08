package GUI;

import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import transform.Descend;
/**
 * Created by Jiarui on 2015/1/30.
 */
public class Main extends JFrame implements ActionListener{
    private JSplitPane jSplitPane;
    private JPanel List;
    private JPanel newFile;
    private JScrollPane directory;
    private JScrollPane jScrollPaneOfList;
    private JScrollPane jScrollPaneOfNewFile;
    private JSplitPane right;
    private JPanel ListButton;
    private JLabel WordSize;
    private JComboBox<String>  WordSizeSelect;
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
    private JPanel jPanel;
    
    private String Size1;
    

    public String getSize1()
	{
		return Size1;
	}

	public void setSize1(String size1)
	{
		Size1 = size1;
	}

	public Main() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super("源代码自动转换系统");
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码"); //创建存放转换后HTML文件的文件夹
        if(!dir.exists()){
            dir.mkdir();
        }
        
        
        
        WordSize = new JLabel("请选择字体大小:");
        WordSize.setSize(120, 30);
        WordSize.setLocation(600, 0);
        
        WordSizeSelect = new JComboBox<String>(model);
        WordSizeSelect.setSize(80, 30);
        WordSizeSelect.addActionListener(this);
        WordSizeSelect.setLocation(705, 0);
        for(int i=1;i<=6;i++)
        {
        	WordSizeSelect.addItem("+"+i+"号字体");
        }
        for(int i=-6;i<=-1;i++)
        {
        	WordSizeSelect.addItem(""+i+"号字体");
        }
        
        View_list view_list = new View_list();
        JTree tree = view_list.InitTree();  //获取初始化后的JTree对象
        JList list = view_list.InitList();  //获取初始化后的待转化原代码文件列表
        JList list1 = view_list.InitListOfNew();  //获取转化后的HTML文件列表
        directory = new JScrollPane(tree);
        directory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "文件夹", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", directory.getFont().getStyle(), 12)));
        List = new JPanel();
        jScrollPaneOfList = new JScrollPane(list);
        jScrollPaneOfList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "待转换源代码文件", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", jScrollPaneOfList.getFont().getStyle(), 12)));
        jScrollPaneOfNewFile = new JScrollPane(list1);
        jScrollPaneOfNewFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "转换后HTML文件", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", jScrollPaneOfNewFile.getFont().getStyle(), 12)));
        newFile = new JPanel();
        newFile.add(WordSize);
        newFile.add(WordSizeSelect);
        right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jScrollPaneOfList, jScrollPaneOfNewFile);
        right.setDividerLocation(220);
        right.setLastDividerLocation(200);
        right.setDividerSize(5);
        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,directory, right);
        jSplitPane.setSize(800, 500);
        jSplitPane.setLocation(0, 30);
        
        jPanel = new JPanel();
        jPanel.setSize(500, 30);
        jPanel.setLocation(0, 0);
        jPanel.setBackground(Color.pink);
        jPanel.setLayout(null);
        jPanel.add(WordSize);
        jPanel.add(WordSizeSelect);
        
        
        this.add(jSplitPane);
        this.add(jPanel);
        
        jSplitPane.setDividerLocation(200);
        jSplitPane.setLastDividerLocation(200);
        jSplitPane.setDividerSize(5);
        

        this.setSize1((String)WordSizeSelect.getSelectedItem());
        
        new Descend().WriteToFile(this.getSize1());
        this.setSize(800,530);
        this.setLocation(250,100);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new Main();
    }

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO 自动生成的方法存根
		this.setSize1((String)WordSizeSelect.getSelectedItem());
		new Descend().WriteToFile(this.getSize1());
	}

}