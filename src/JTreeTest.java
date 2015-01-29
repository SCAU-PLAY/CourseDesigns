import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

/**
 * Created by hongyeah on 2015/1/29.
 */
public class JTreeTest extends JFrame implements TreeExpansionListener ,TreeSelectionListener{
    private JSplitPane split;
    private JScrollPane treeOfFile;
    private JPanel panel;
    public JTree jtree;
    public static void main(String[] agr) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new JTreeTest();
        File file = new File("d://Desktop");
        if (file.exists()) {
            System.out.println(file.getName());
        }
    }
    public JTreeTest() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super("JTree");
        InitJtree();
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        this.setSize(800,500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void InitJtree() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        JNode mycomputer = new JNode("我的电脑");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        jtree = new JTree(mycomputer);
        jtree.addTreeExpansionListener(this);
        jtree.addTreeSelectionListener(this);
        for(char temp = 'A' ; temp<='Z' ; temp++)
        {
            File file = new File(temp +":\\");
            if(file.exists()){
                JNode node = new JNode(file,new String(String.valueOf(temp)));
                mycomputer.add(node);
                addNode(node,1);
            }
        }
        treeOfFile = new JScrollPane(jtree);
        panel = new JPanel();
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeOfFile,panel);
        this.add(split);
        split.setDividerLocation(150);
        split.setLastDividerLocation(200);
        split.setDividerSize(5);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }

    public void addNode(JNode node,int times){
        if(times == 2) return ;
        File[] files = node.path.listFiles();
        if(files==null) return;
        for(int i=0 ; i<files.length ; i++)
        {
            if(files[i].canRead()&&files[i].isDirectory()&&!files[i].isHidden()){
                JNode jNode = new JNode(files[i]);
                node.add(jNode);
                addNode(jNode,++times);
                times--;
            }
        }
    }
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        JNode node = (JNode)tp.getLastPathComponent();
        if(!node.isFileNode()||node.path.isFile()) return;
        else if(node.path.isDirectory()){
            node.removeAllChildren();
            addNode(node, 0);
        }
        jtree.updateUI();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {

        System.out.println("KO");
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath tp = e.getPath();
        JNode node = (JNode)tp.getLastPathComponent();
        if(!node.isFileNode()||node.path.isFile()) return;
        else if(node.path.isDirectory()){
            node.removeAllChildren();
            addNode(node,0);
        }

    }
}

class JNode extends DefaultMutableTreeNode{
    public File path = null;
    public JNode(File path){
        super(path.getName());
        this.path = path;
    }

    public JNode(String name){
        super(name);
    }

    public boolean isFileNode(){
        if(path == null)
            return false;
        else return true;
    }

    public JNode(File path , String name){
        super(name);
        this.path = path;
    }

    public void addNode(int time) {
        if(time == 1) return;
        File[] files = this.path.listFiles();
        for(int i=0 ; i<files.length ; i++)
        {
            JNode jNode = new JNode(files[i]);
            this.add(jNode);

        }
    }

}