import sun.awt.shell.ShellFolder;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.Enumeration;

/**
 * Created by hongyeah on 2015/1/30.
 */
public class View_list extends JFrame implements TreeExpansionListener,TreeSelectionListener{
    public static void main(String[] agr){
        new View_list();
    }
    private JSplitPane split;
    private JScrollPane treeOfFile;
    private JScrollPane listOffile;
    private JPanel panel;
    public Jnode root;
    public View_list(){
        treeOfFile = new JScrollPane(InitTree());
        listOffile = new JScrollPane(InitList());
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeOfFile,listOffile);
        split.setDividerLocation(150);
        split.setLastDividerLocation(200);
        split.setDividerSize(5);
        this.add(split);
        this.setSize(800,500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public JTree jTree;
    public JTree InitTree(){
        root = new Jnode();
        jTree = new JTree(root);
        jTree.addTreeExpansionListener(this);
        File[] seconds = ((ShellFolder)(root.dir)).listFiles();
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
    public JList InitList(){
        jList = new JList();
        jList.setModel(root.listmodel);
        jList.setCellRenderer(new LCellRender());
        return jList;
    }
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        Jnode node = (Jnode) tp.getLastPathComponent();
        if(node.getParent()!=null)
        {
            AddGrandNode(node);
            jList.setModel(node.listmodel);

        }
    }
    @Override
    public void treeCollapsed (TreeExpansionEvent event){
    }
    @Override
    public void valueChanged (TreeSelectionEvent e){
    }
}

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

class Jnode extends DefaultMutableTreeNode{
    public static FileSystemView Sys_file;
    public File dir = null;
    public DefaultListModel listmodel = null;
    public Jnode(){
        Sys_file = FileSystemView.getFileSystemView();
        dir = Sys_file.getHomeDirectory();
        if(listmodel == null) {
            listmodel = new DefaultListModel();
            File[] thiss = ((ShellFolder) (dir)).listFiles(false);
            for (int i = 0; i < thiss.length; i++) {
                if (thiss[i].isFile()) {
                    listmodel.addElement(new LNode(thiss[i]));
                }
            }
        }
    }
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
            else if(nexts[i].isFile()){
                listmodel.addElement(new LNode(nexts[i]));
            }
        }
    }
    public Jnode(File dir , boolean grand){
        this.dir = dir;
        listmodel = new DefaultListModel();
        File[] thiss = ((ShellFolder) (dir)).listFiles(false);
        for (int i = 0; i < thiss.length; i++) {
            if (thiss[i].isFile()) {
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