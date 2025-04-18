package pkgOrtak;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import static pkgMain.MainFrame.*;

public class ColorizeZebra extends DefaultTableCellRenderer {
            public Component getTableCellRendererComponent( JTable table, Object value, boolean  isSelected, boolean  hasFocus, int row, int column ) {

            JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, false, false, row, column);
            
            cell.setToolTipText(table.getColumnName(column)+" : "+table.getValueAt(row, column));
            
                        if (row%2==0) { cell.setBackground(v_renkZemin); cell.setForeground(Color.white); }
                        else          { cell.setBackground(Color.white); cell.setForeground(v_renkZemin); }
                        
                        if ( isSelected ){
                              cell.setBackground(v_renkSatirSecilmis);    
                              cell.setForeground(Color.white);
                        }                        
                        
                return cell;                
            }
            
    }

class ColumnHeaderToolTips extends MouseMotionAdapter {
  TableColumn curCol;
  Map tips = new HashMap();
  public void setToolTip(TableColumn col, String tooltip) {
    if (tooltip == null) {
      tips.remove(col);
    } else {
      tips.put(col, tooltip);
    }
  }
}


