package pkgConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PopupMenuExample extends javax.swing.JPanel {
    public JPopupMenu popup;
    public PopupMenuExample() {
        initComponents();
    popup = new JPopupMenu();
    ActionListener menuListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.out.println("Popup menu item ["
            + event.getActionCommand() + "] was pressed.");
      }
    };
    JMenuItem item;
    popup.add(item = new JMenuItem("Left", new ImageIcon("1.gif")));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    popup.add(item = new JMenuItem("Center", new ImageIcon("2.gif")));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    popup.add(item = new JMenuItem("Right", new ImageIcon("3.gif")));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    popup.add(item = new JMenuItem("Full", new ImageIcon("4.gif")));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    popup.addSeparator();
    popup.add(item = new JMenuItem("Settings . . ."));
    item.addActionListener(menuListener);

    popup.setLabel("Justification");
    popup.setBorder(new BevelBorder(BevelBorder.RAISED));
    popup.addPopupMenuListener(new PopupPrintListener());

    addMouseListener(new MousePopupListener());        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

  class MousePopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      checkPopup(e);
    }

    public void mouseClicked(MouseEvent e) {
      checkPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
      checkPopup(e);
    }

    private void checkPopup(MouseEvent e) {
      //if (e.isPopupTrigger()) {
        popup.show(PopupMenuExample.this, e.getX(), e.getY());
      //}
    }
  }

  // An inner class to show when popup events occur
  class PopupPrintListener implements PopupMenuListener {
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
      System.out.println("Popup menu will be visible!");
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
      System.out.println("Popup menu will be invisible!");
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
      System.out.println("Popup menu is hidden!");
    }
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Popup Menu Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new PopupMenuExample());
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables







  // An inner class to check whether mouse events are the popup trigger


