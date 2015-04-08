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
     * ��ʼ����
     * @return  ����һ��JTree����
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
    public JList InitListOfNew(){    //��ʼ���ļ��б����ڴ��ת����õ���HTML�ļ�
        jListOfNew = new JList();
        rootOfNew = new Jnode(1);
        jListOfNew.setModel(rootOfNew.listmodel1);
        jListOfNew.setCellRenderer(new LCellRender());
        jListOfNew.addMouseListener(new MouseListener() {   //��������������˫����HTML�ļ�
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
     * ɨ��ָ���ļ��е�HTML�ļ����������ڽ�������ʾ����
     * @param fileSystemView
     */
    public void addToList(FileSystemView fileSystemView){   //����ɨ����HTML�ļ����ļ��� �����ļ��б�
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML����");
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
        jList.addMouseListener(new MouseListener() {  //��Ӵ�ת���ļ��б�ļ����¼�����˫���������Ӧ��Դ����ת��
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
                        String aim = fileSystemView.getDefaultDirectory().getPath() + "\\HTML����\\" + name;
                        File file = new File(aim);
                        DefaultListModel defaultListModel;


                        /**
                         * ����ת����HTML�ļ�����ͬ�����
                         */
                        if(file.exists()){
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                            int choice = JOptionPane.showConfirmDialog(null,"�ļ� " + file.getName() + " �Ѿ����ڣ��Ƿ񸲸ǣ�","��ʾ",JOptionPane.YES_NO_OPTION);
                            if(choice == 0){
                                highLight.WriteToFile(aim);
                                addToList(fileSystemView);
                            }else{
                                int choice1 = JOptionPane.showConfirmDialog(null,"�Ƿ��������������棿","��ʾ",JOptionPane.YES_NO_OPTION);
                                if(choice1 == 0){
                                    String string = JOptionPane.showInputDialog(null,"�����������ļ����� ");
                                    String aim1 = fileSystemView.getDefaultDirectory().getPath() + "\\HTML����\\" + string + ".html";
                                    File file1 = new File(aim1);
                                    if(file1.exists()) {
                                        File file2 = file1;
                                        while (file2.exists()) {
                                            String name1 = JOptionPane.showInputDialog(null, "�ļ��Ѵ��ڣ����������룺");
                                            aim1 = fileSystemView.getDefaultDirectory().getPath() + "\\HTML����\\" + name1 + ".html";
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
     * ������һ���ڵ�չ��ʱ���ã���Ӹýڵ�����ӽڵ�
     * @param event
     */
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        if(node.getParent()!=null)
        {
            AddGrandNode(node);  //�������ӽڵ�
            jList.setModel(node.listmodel);  //�г��ýڵ��ļ����µ�Դ�����ļ�
        }
        jList.setModel(node.listmodel);
    }

    /**
     * �����е�һ����۵�ʱ����
     * @param event
     */
    @Override
    public void treeCollapsed (TreeExpansionEvent event){
        TreePath tp = event.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        jList.setModel(node.listmodel);
    }

    /**
     * ÿ��ѡ��ֵ��������ʱ����
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
 * ���ڻ�ȡϵͳ�ļ�ͼ��
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
 * �ڵ��࣬���ڴ����ڵ����
 */
class Jnode extends DefaultMutableTreeNode{
    public static FileSystemView Sys_file;
    public File dir = null;
    public DefaultListModel listmodel = null;
    public DefaultListModel listmodel1 = null;

    /**
     * ��ʼ���ڵ㣬�����ýڵ��ļ����е�Դ�����ļ���ʾ�ڴ�ת���ļ�������
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
     * ��ʼ���ڵ㣬�����ýڵ��ļ�������ת����HTML�ļ���ʾ�ڽ�����
     */
    public Jnode(int choice){
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File dir = new File(fileSystemView.getDefaultDirectory().getPath() + "\\HTML����");
        listmodel1 = new DefaultListModel();
        File[] thiss = dir.listFiles();
        for (int i = 0; i < thiss.length; i++) {
            if (thiss[i].isFile() && thiss[i].getName().endsWith(".html")) {
                listmodel1.addElement(new LNode(thiss[i]));
            }
        }

    }

    /**
     * ���췽����ʼ���ڵ㣬����Ӹýڵ���ӽڵ㣬ͬʱ����Ŀ¼��Դ�����ļ���ʾ�ڽ�����
     * @param dir �ýڵ��ļ�����
     */
    public Jnode(File dir){
        this.dir = dir;
        if(this.toString().equals("����")) return;
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