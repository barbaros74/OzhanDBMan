package pkgOrtak;

import java.awt.Color;
import static pkgMain.MainFrame.*;

public class ColorizeSurface{
    public static void colorizeSurface( Color i_cl_bg ){
      //jLabelTooltipDB.setForeground(i_cl_bg);
        v_renkZemin          = i_cl_bg; 
        jDsPane1.setBackground(v_renkZemin);
        jDsPane2.setBackground(v_renkZemin);        
    }    
}
