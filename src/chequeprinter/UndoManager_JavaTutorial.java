/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chequeprinter;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Vincent
 */
public class UndoManager_JavaTutorial {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       
        JFrame window = new JFrame("Java Tutorial: UndoManager");
        final JTextArea area_1 = new JTextArea();
        JMenuItem redoEdit = new JMenuItem("Redo");
        JMenuItem undoEdit = new JMenuItem("Undo");
       
        UndoManager editManager = new UndoManager();

       
        area_1.setLineWrap(true);
        area_1.setWrapStyleWord(true);
        area_1.getDocument().addUndoableEditListener((UndoableEditEvent e) -> {
            editManager.addEdit(e.getEdit());
        });
        area_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (editManager.canRedo()) {
                        redoEdit.setEnabled(true);
                    } else {
                        redoEdit.setEnabled(false);
                    }
                    if (editManager.canUndo()) {
                        undoEdit.setEnabled(true);
                    } else {
                        undoEdit.setEnabled(false);
                    }
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(undoEdit);
                    popup.add(redoEdit);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        redoEdit.addActionListener((ActionEvent e) -> {
            if (editManager.canRedo()) {
                editManager.redo();
            }
        });
        undoEdit.addActionListener((ActionEvent e) -> {
            if (editManager.canUndo()) {
                editManager.undo();
            }
        });
        window.add(area_1);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 500);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
