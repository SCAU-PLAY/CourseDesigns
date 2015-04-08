package GUI;

import sun.awt.shell.ShellFolder;
import transform.HighLight;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by hongyeah on 2015/1/30.
 */
public class View_list implements TreeExpansionListener,TreeSelectionListener{
    public Jnode root;
    public Jnode rootOfNew;
    public View_list(){

    }

    public JTree jTree;

    /**
     * 初始化树
     * @return  返回一个JTree对象
     */
    public JTree InitTree(){
        root = new Jnode();
        jTree = new JTree(root);
        jTree.addTreeExpansionListener(this);
        jTree.addTreeSelectionListener(this);
        File[] seconds = ((ShellFolder)(root.dir)).listFiles(false);
        for(int i = 0; i<seconds.length;i++) {
            if (seconds[i].isDirectory()) {
                root.add(new Jnode(seconds[i]));
            }
        }
        jTree.setCellRenderer(new JCellRender());

        return jTree;
    }
    public void AddGrandNode(Jnode node){
        if(!node.isLeaf()&&node.getDepth()==1) {
            Enumeration<Jnode> e = (Enumeration<Jnode>)node.children();
            for( ; e.hasMoreElements();){
                Jnode n = e.nextElement();
                File[] nexts = ((ShellFolder) (n.dir)).listFiles(false);
                for (int i = 0; i < nexts.length; i++) {
                    if (nexts[i].isDirectory()) {
                        n.add(new Jnode(nexts[i],true));
                    }
                }
            }
        }
    }

    public JList jList;
    public JList jListOfNew;
    public JList InitListOfNew(){    //初始化文件列表用于存放转换后得到的HTML文件
        jListOfNew = new JList();
        rootOfNew = new Jnode(1);
        jListOfNew.setModel(rootOfNew.listmodel1);
        jListOfNew.setCellRenderer(new LCellRender());
        jListOfNew.addMouseListener(new MouseListener() {   //增加鼠标监听——双击打开HTML文件
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int index = jListOfNew.locationToIndex(e.getPoint());
                    String path = ((LNode) (jListOfNew.getModel().getElementAt(index))).file.getPath();
                    Runtime   rt   =   Runtime.getRuntime();
                    try {
                        rt.exec("cmd   /c   start  " + path);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return jListOfNew;
    }

    /**
     * 扫描指定文件夹的HTML文件，并将其在界面中显示出来
     * @param fileSystemView
     */
    public void addToList(FileSystemView fileSystemView){   //重新扫描存放HTML文件的文件夹 更新文件列表
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码");
        System.out.println(dir.getPath());
        DefaultListModel defaultListModel = new DefaultListModel();
        File[] thiss = dir.listFiles();
        for (int i = 0; i < thiss.length; i++) {
            if (thiss[i].isFile() && thiss[i].getName().endsWith(".html")) {
                defaultListModel.addElement(new LNode(thiss[i]));
            }
            System.out.println(defaultListModel.getElementAt(i));
        }
        rootOfNew.listmodel1 = defaultListModel;
        jListOfNew.setModel(rootOfNew.listmodel1);
    }

    public JList InitList(){
        jList = new JList();
        jList.setModel(root.listmodel);
        jList.setCellRenderer(new LCellRender());
        jList.addMouseListener(new MouseListener() {  //添加待转化文件列表的监听事件，若双击则进行相应的源代码转化
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int index = jList.locationToIndex(e.getPoint());
                    HighLight highLight = new HighLight();
                    try {

                        String name = ((LNode) (jList.getModel().getElementAt(index))).file.getName();
                        if(name.endsWith(".java")) {
                            name = name.replace(".java", ".html");
                            highLight.Login(((LNode) (jList.getModel().getElementAt(index))).file.getPath(),0);
                        }
                        else if(name.endsWith(".c")){
                            name = name.replace(".c", ".html");
                            highLight.Login(((LNode) (jList.getModel().getElementAt(index))).file.getPath(),1);
                        }
                        else if(name.endsWith(".h")){
                            name = name.replace(".h", ".html");
                            highLight.Login(((LNode) (jList.getModel().getElementAt(index))).file.getPath(),2);
                        }
                        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
                        String aim = fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码\\" + name;
                        File file = new File(aim);
                        DefaultListModel defaultListModel;


                        /**
                         * 处理转化后HTML文件名相同的情况
                         */
                        if(file.exists()){
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                            int choice = JOptionPane.showConfirmDialog(null,"文件 " + file.getName() + " 已经存在，是否覆盖？","提示",JOptionPane.YES_NO_OPTION);
                            if(choice == 0){
                                highLight.WriteToFile(aim);
                                addToList(fileSystemView);
                            }else{
                                int choice1 = JOptionPane.showConfirmDialog(null,"是否重新命名并保存？","提示",JOptionPane.YES_NO_OPTION);
                                if(choice1 == 0){
                                    String string = JOptionPane.showInputDialog(null,"请重新输入文件名： ");
                                    String aim1 = fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码\\" + string + ".html";
                                    File file1 = new File(aim1);
                                    if(file1.exists()) {
                                        File file2 = file1;
                                        while (file2.exists()) {
                                            String name1 = JOptionPane.showInputDialog(null, "文件已存在，请重新输入：");
                                            aim1 = fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码\\" + name1 + ".html";
                                            file2 = new File(aim1);
                                        }
                                        highLight.WriteToFile(aim1);
                                        addToList(fileSystemView);
                                    }else {
                                        addToList(fileSystemView);
                                    }
                                }
                            }
                        }
                        else {
                            highLight.WriteToFile(aim);
                            addToList(fileSystemView);
                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (UnsupportedLookAndFeelException e1) {
                        e1.printStackTrace();
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return jList;
    }

    /**
     * 当树的一个节点展开时调用，添加该节点的孙子节点
     * @param event
     */
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        if(node.getParent()!=null)
        {
            AddGrandNode(node);  //加入孙子节点
            jList.setModel(node.listmodel);  //列出该节点文件夹下的源代码文件
        }
        jList.setModel(node.listmodel);
    }

    /**
     * 当树中的一个项被折叠时调用
     * @param event
     */
    @Override
    public void treeCollapsed (TreeExpansionEvent event){
        TreePath tp = event.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        jList.setModel(node.listmodel);
    }

    /**
     * 每当选择值发生更改时调用
     * @param e
     */
    @Override
    public void valueChanged (TreeSelectionEvent e){
        TreePath tp = e.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        if(node.listmodel!=null)
        jList.setModel(node.listmodel);
    }
}

/**
 * 用于获取系统文件图标
 */
class JCellRender extends DefaultTreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Jnode node = (Jnode)value;
        Icon icon = node.getIcon();
        setLeafIcon(icon);
        setOpenIcon(icon);
        setClosedIcon(icon);
        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }

}

/**
 * 节点类，用于创建节点对象
 */
class Jnode extends DefaultMutableTreeNode{
    public static FileSystemView Sys_file;
    public File dir = null;
    public DefaultListModel listmodel = null;
    public DefaultListModel listmodel1 = null;

    /**
     * 初始化节点，并将该节点文件夹中的源代码文件显示在待转化文件界面中
     */
    public Jnode(){
        Sys_file = FileSystemView.getFileSystemView();
        dir = Sys_file.getHomeDirectory();
        if(listmodel == null) {
            listmodel = new DefaultListModel();
            File[] thiss = ((ShellFolder) (dir)).listFiles(false);
            for (int i = 0; i < thiss.length; i++) {
                if (thiss[i].isFile() && (thiss[i].getName().endsWith(".java") || thiss[i].getName().endsWith(".c") || thiss[i].getName().endsWith(".h")|| thiss[i].getName().endsWith(".cpp"))) {
                    listmodel.addElement(new LNode(thiss[i]));
                }
            }
        }
    }

    /**
     * 初始化节点，并将该节点文件夹中已转化的HTML文件显示在界面中
     */
    public Jnode(int choice){
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML代码");
        listmodel1 = new DefaultListModel();
        File[] thiss = dir.listFiles();
        for (int i = 0; i < thiss.length; i++) {
            if (thiss[i].isFile() && thiss[i].getName().endsWith(".html")) {
                listmodel1.addElement(new LNode(thiss[i]));
            }
        }

    }

    /**
     * 构造方法初始化节点，并添加该节点的子节点，同时将该目录下源代码文件显示在界面上
     * @param dir 该节点文件变量
     */
    public Jnode(File dir){
        this.dir = dir;
        if(this.toString().equals("网络")) return;
        File[] nexts = ((ShellFolder)(dir)).listFiles(false);
        if(listmodel == null){
            listmodel = new DefaultListModel();
        }
        for(int i = 0;i<nexts.length;i++){
            if(nexts[i].isDirectory()) {
                this.add(new Jnode(nexts[i],true));
            }
            else if(nexts[i].isFile() && (nexts[i].getName().endsWith(".java") || nexts[i].getName().endsWith(".c") || nexts[i].getName().endsWith(".h")|| nexts[i].getName().endsWith(".cpp"))){
                listmodel.addElement(new LNode(nexts[i]));
            }
        }
    }
    public Jnode(File dir , boolean grand){
        this.dir = dir;
        listmodel = new DefaultListModel();
        File[] thiss = ((ShellFolder) (dir)).listFiles(false);
        for (int i = 0; i < thiss.length; i++) {
            if (thiss[i].isFile() && (thiss[i].getName().endsWith(".java") || thiss[i].getName().endsWith(".c") || thiss[i].getName().endsWith(".h")|| thiss[i].getName().endsWith(".cpp"))) {
                listmodel.addElement(new LNode(thiss[i]));
            }
        }
    }
    public Icon getIcon() {
        return Sys_file.getSystemIcon(dir);
    }
    public String toString(){
        return Sys_file.getSystemDisplayName(dir);
    }
}

class LCellRender extends JLabel implements ListCellRenderer {
    public LCellRender(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        LNode node = (LNode)value;
        setIcon(node.getIcon());
        setText(value.toString());
        setBackground(isSelected ? Color.BLUE.darker().darker(): Color.WHITE);
        setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return this;
    }
}

class LNode{
    File file;
    public LNode(File file){
        this.file = file;
    }
    public Icon getIcon(){
        return Jnode.Sys_file.getSystemIcon(file);
    }
    public String toString(){
        return Jnode.Sys_file.getSystemDisplayName(file);
    }
}