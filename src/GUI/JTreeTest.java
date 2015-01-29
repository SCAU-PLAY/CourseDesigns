import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * Created by hongyeah on 2015/1/29.
 */
public class JTreeTest extends JFrame implements TreeExpansionListener ,TreeSelectionListener{
    private JSplitPane split;
    private JScrollPane treeOfFile;
    private JPanel panel;
    public static void main(String[] agr) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new JTreeTest();
        File file = new File("d://Desktop");
        if (file.exists()) {
            System.out.println(file.getName());
            System.out.println("OK");
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
        JTree jtree = new JTree(mycomputer);
        jtree.addTreeExpansionListener(this);
        jtree.addTreeSelectionListener(this);
        for(char temp = 'A' ; temp<='Z' ; temp++)
        {
            File file = new File(temp +":");
            if(file.exists()){
                mycomputer.add(new JNode(file,new String(String.valueOf(temp))));
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

    public void addnode(){
    }
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        System.out.println("OK");
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        System.out.println("KO");
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        System.out.println("双击");

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
}