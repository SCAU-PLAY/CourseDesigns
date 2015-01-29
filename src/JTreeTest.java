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

    public static void main(String[] agr) {
        new JTreeTest();
        File file = new File("d://Desktop");
        if (file.exists()) {
            System.out.println(file.getName());
            System.out.println("OK");
        }
    }
    public JTreeTest(){
        super("JTree");
        InitJtree();
        this.setSize(800,500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void InitJtree(){
        JNode mycomputer = new JNode("我的电脑");
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
        this.add(jtree);
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