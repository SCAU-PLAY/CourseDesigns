import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
/**
 * Description: File Explorer <BR>
 * Copyright: Copyright (c) 2001 <BR>
 * Email to <a href=mailto:SunKingEMail@163.net>Sunking </a> <BR>
 * @author Sunking
 * @version 1.0
 */

public class Explore extends JFrame
        implements TreeExpansionListener,TreeSelectionListener,ActionListener{

    //定义常量
    final JTree tree = new JTree(createTreeModel());
    final JPanel pSub=new JPanel(new GridLayout(100,3));
    final JSplitPane split;
    final JPanel statusbar=new JPanel(new BorderLayout());
    final JLabel lbStatus=new JLabel(" ");


    //构造函数
    public Explore() {

//设置窗口大小
        Dimension dimension = getToolkit().getScreenSize();
        int i = (dimension.width - 640) / 2;
        int j = (dimension.height - 480) / 2;
        setBounds(i,j,640,480);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowevent){
                System.exit(0);
            }
        });

//分割窗口
//水平分割，左scrollPane内放tree，右放pSub用于显示文件
        split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree),new JScrollPane(pSub));
        split.setDividerLocation(150);
        split.setLastDividerLocation(200);
        this.add(split);

//背景色为白
        pSub.setBackground(Color.white);

//给树添加展开监听器
        tree.addTreeExpansionListener(this);
        tree.addTreeSelectionListener(this);

//设置树的外形
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (Exception ex) {}
        getContentPane().add(statusbar,BorderLayout.SOUTH);
        statusbar.add(lbStatus,BorderLayout.WEST);
    }


    //树折叠
    public void treeCollapsed(TreeExpansionEvent e) {}

    //树展开
    public void treeExpanded(TreeExpansionEvent e) {
//getLastSelectedPathComponent()返回当前选择的第一个节点中的最后一个路径组件。
//首选节点的 TreePath 中的最后一个 Object，若未选择任何内容，则返回 null
        if (tree.getLastSelectedPathComponent()==null)
            return;
        if (tree.getLastSelectedPathComponent().toString().trim().equals("Local"))
            return;
        TreePath path = e.getPath();
        FileNode node = (FileNode)path.getLastPathComponent();
        if( ! node.isExplored()) {
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            node.Explore();
            model.nodeStructureChanged(node);
        }
    }


    //值变化
    public void valueChanged(TreeSelectionEvent e)
    {
        try{
            String s="";

//如果该结点最后一个对象返回值为空，即为底层结点，返回，清空psub
            if (tree.getLastSelectedPathComponent()==null)
                return;
            pSub.removeAll();

//如果是本地，则产生目录
            if (tree.getLastSelectedPathComponent().toString().trim().equals("Local")){
                File roots[]=File.listRoots();
                for(int i=1;i <roots.length;i++)
                {
                    String DiskName=roots[i].toString();
                    DiskName=DiskName.substring(0,DiskName.indexOf(":")+1);
                    addButton(DiskName,"");
                }
            }

//否则，。。。
            else{
                Object []path= e.getPath().getPath();
                String ss="";
                for(int i=1;i <path.length;i++)
                    ss+=File.separator+path[i].toString();
                File f = new File(ss.substring(1));
                lbStatus.setText(f.toString());
                String[] list= f.list();

                //定义Vector变量
                Vector vFile=new Vector(),vDir=new Vector();
                for(int i = 0; i < list.length; i++){
                    if ((new File(ss+File.separator+list[i])).isDirectory())
                        vDir.addElement(list[i]);
                    else
                        vFile.addElement(list[i]);
                }

//排序
                sortElements(vFile);
                sortElements(vDir);

                for(int i=0;i <vDir.size();i++)
                    addButton((String)(vDir.elementAt(i)),ss);
                for(int i=0;i <vFile.size();i++)
                    addButton((String)(vFile.elementAt(i)),ss);
            }
            pSub.doLayout();
            pSub.repaint();
        }catch(Exception ee){}
    }


    //排序
    public void sortElements(Vector v){
        for(int i=0;i <v.size();i++){
            int k=i;
            for(int j=i+1;j <v.size();j++)
                if(((String)(v.elementAt(j))).toLowerCase().compareTo(((String)(v.elementAt(k))).toLowerCase()) <0)
                    k=j;
            if(k!=i)swap(k,i,v);
        }
    }


    //交换
    private void swap(int loc1,int loc2,Vector v){
        Object tmp=v.elementAt(loc1);
        v.setElementAt(v.elementAt(loc2),loc1);
        v.setElementAt(tmp,loc2);
    }

    //添加右侧按钮
    private void addButton(String fileName,String filePath){
        JButton btt=new JButton(fileName);
        btt.setBorder(null);
        btt.setHorizontalAlignment(SwingConstants.LEFT);
        btt.setBackground(Color.white);
        if ((new File(filePath+File.separator+fileName)).isDirectory())
            btt.setIcon(UIManager.getIcon("Tree.closedIcon"));
        else
            btt.setIcon(UIManager.getIcon("Tree.leafIcon"));
        pSub.add(btt);
        btt.addActionListener(this);
    }


    //按钮动作
    public void actionPerformed(ActionEvent e){
        try{
            TreePath p=tree.getLeadSelectionPath();
            String text=((JButton)(e.getSource())).getText();
            Object []path= p.getPath();
            String ss="";
            for(int i=1;i <path.length;i++)
                ss+=File.separator+path[i].toString();
            ss=ss.substring(1);
            File f = new File(ss+File.separator+text);
            lbStatus.setText(f.toString());
            if(f.isDirectory()){
                int index=tree.getRowForPath(p);
                tree.expandRow(index);
                while (!(tree.getLastSelectedPathComponent().toString().trim().equals(text)))
                    tree.setSelectionRow(index++);
                tree.expandRow(index-1);
            }
            else{
                String postfix=text.toUpperCase();
                if(postfix.indexOf(".TXT")!=-1||postfix.indexOf(".JAVA")!=-1||
                        postfix.indexOf(".HTM")!=-1||postfix.indexOf(".LOG")!=-1)
                    Runtime.getRuntime().exec("NotePad.exe "+ss+File.separator+text);
            }
        }catch(Exception ee){}
    }


    //创建树
    private DefaultMutableTreeNode createTreeModel() {
        DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode("Local");
        File[] rootPath = File.listRoots();
        for(int i=1;i <rootPath.length;i++){
            FileNode Node = new FileNode(rootPath[i]);
            Node.Explore();
            rootNode.add(Node);
        }
        return rootNode;
    }


    public static void main(String args[]) {
        new Explore().setVisible(true);
    }


    class FileNode extends javax.swing.tree.DefaultMutableTreeNode {
        private boolean explored = false;
        public FileNode(File file) {
            setUserObject(file);
        }
        @Override
        public boolean getAllowsChildren() {
            return isDirectory();
        }
        @Override
        public boolean isLeaf() {
            return !isDirectory();
        }
        public File getFile() {
            return (File)getUserObject();
        }
        public boolean isExplored() {
            return explored;
        }
        public boolean isDirectory() {
            return getFile().isDirectory();
        }
        @Override
        public String toString() {
            File file = (File)getUserObject();
            String filename = file.toString();
            int index = filename.lastIndexOf(File.separator);
            return (index != -1 && index != filename.length()-1) ? filename.substring(index+1) : filename;
        }


        public void Explore() {
            if(!isDirectory()) return;
            if(!isExplored()) {
                File file = getFile();
                File[] children = file.listFiles();
                for(int i=0; i < children.length; ++i)
                {
                    File f=children[i];
                    if(f.isDirectory())
                        add(new FileNode(children[i]));
                }
                explored = true;
            }
        }

//try{
//    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//    SwingUtilities.updateComponentTreeUI(this); //注意
//  } catch (Exception ex) {
//  }

//UIManager.setLookAndFeel("javax.swing.plaf.motif.MotifLookAndFeel");

    }
}