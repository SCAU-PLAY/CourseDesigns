package GUI;

/**
 * Created by Jiarui on 2015/1/30.
 */
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

class FileListModel
        implements ListModel {
    FileList theList;
    I_fileSystem node;
    char fileType = I_fileSystem.ALL;

    public void setNode (I_fileSystem node) {
        this.node = node;
    }

    public Object getElementAt(int index) {
        if (node != null) {
            return ((I_fileSystem)node).getChild(fileType, index);
        } else {
            return null;
        }
    }

    public int getSize() {
        if (node != null) {
            return ((I_fileSystem)node).getChildCount(fileType);
        } else {
            return 0;
        }
    }

    public void addListDataListener(ListDataListener l) {
    }
    public void removeListDataListener(ListDataListener l) {
    }
}

class MyCellRenderer extends JLabel implements ListCellRenderer {
    public MyCellRenderer() {
        setOpaque(true);
    }
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        FolderNode node = (FolderNode)value;
        setIcon(node.getIcon());
        setText(value.toString());
        setBackground(isSelected ? Color.BLUE.darker().darker(): Color.WHITE);
        setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return this;
    }
}