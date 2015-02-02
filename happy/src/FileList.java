import javax.swing.*;

/**
 * Created by hongyeah on 2015/1/30.
 */
public class FileList
        extends JList {
    //PathNode theNode;
    FileListModel dataModel;
    static final long serialVersionUID = 10;

    public FileList() {
        dataModel = new FileListModel();
        setModel(dataModel);
        this.setCellRenderer(new MyCellRenderer());
    }

    public void fireTreeSelectionChanged(I_fileSystem node) {
        //Vector files = node.getFiles();
        //theNode = node;
        dataModel.setNode(node);
        updateUI();
    }
}
