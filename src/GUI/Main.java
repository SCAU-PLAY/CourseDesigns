package GUI;

import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

/**
 * Created by Jiarui on 2015/1/30.
 */
public class Main extends JFrame{
    private JSplitPane jSplitPane;
    private JPanel List;
    private JPanel newFile;
    private JScrollPane directory;
    private JScrollPane jScrollPaneOfList;
    private JScrollPane jScrollPaneOfNewFile;
    private JSplitPane right;
    private JPanel ListButton;

    public Main() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super("源代码自动转换系统");
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码");
        if(!dir.exists()){
            dir.mkdir();
        }
        View_list view_list = new View_list();
        JTree tree = view_list.InitTree();
        JList list = view_list.InitList();
        JList list1 = view_list.InitListOfNew();
        directory = new JScrollPane(tree);
        directory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "文件夹", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", directory.getFont().getStyle(), 12)));
        List = new JPanel();
        jScrollPaneOfList = new JScrollPane(list);
        jScrollPaneOfList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "待转换源代码文件", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", jScrollPaneOfList.getFont().getStyle(), 12)));
        jScrollPaneOfNewFile = new JScrollPane(list1);
        jScrollPaneOfNewFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "转换后HTML文件", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("黑体", jScrollPaneOfNewFile.getFont().getStyle(), 12)));
        newFile = new JPanel();
        right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jScrollPaneOfList, jScrollPaneOfNewFile);
        right.setDividerLocation(220);
        right.setLastDividerLocation(200);
        right.setDividerSize(5);
        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,directory, right);
        this.add(jSplitPane);
        jSplitPane.setDividerLocation(200);
        jSplitPane.setLastDividerLocation(200);
        jSplitPane.setDividerSize(5);
        this.setSize(800,500);
        this.setLocation(250,100);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new Main();
    }
}