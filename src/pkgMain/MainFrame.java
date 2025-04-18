package pkgMain;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import pkgConnection.LoginFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.metal.*;
import pkgConnection.DBOperations;
import static pkgConnection.LoginFrame.setLang;
import static pkgConnection.LoginFrame.v_conn_dsWarn;
import static pkgOrtak.LanguageChoice.setExpression;

public class MainFrame extends javax.swing.JFrame {
    public static DefaultTableModel v_tableModelFull       = new DefaultTableModel();
    final  static String            v_lookandfeel          = "Metal";  
    final  static String            v_theme                = "Test"; 
    public static int               v_yks_mb               =  0 ; 
    public static int               v_gns_pn               =  0 ;
    public static int               v_yks_pn               =  0 ; 
    public static int               v_pay                  =  0 ;
    public static Color             v_renkSatirSecilmis    = new Color(204,0,102);
    public static Color             v_renkSatirZeminCiftli = new Color(128,128,128);     
    public static Color             v_renkZemin                 ;
    public static int               v_prg_kodu             =  0 ;
    public        String[]          v_kss                  = new String[10];     
    public        DBOperations      v_dbOpr                     ;    
    public static String            v_sql_ek               = ""; 
    public static String            v_tooltip;
                  
    public MainFrame() {
        initComponents(); 
        callLogin(new LoginFrame());            
        setTooltip(jBtRefresh.getToolTipText());
        jBtRefresh.setToolTipText(v_tooltip);                   
    }

    public static void setTooltip( String i_text ) {
        v_tooltip        = "<html>\n<head>\n<style>\n .etiket  { background-color : \"#6495ED\" ; color: \"#F0FFFF\" ; font-weight:bold; font-family:tahoma; font-size: 12pt;}\n" +
                           "</style>\n</head>\n<body>\n<span class=\"etiket\">"+i_text+"</span>\n</body>\n</html>";        
    }    
    public static void getCoordinate() {               
        final Dimension v_byt_mb = jMnBar1.getSize();
                        v_yks_mb = v_byt_mb.height;        

        final Dimension v_byt_pn = jDsPane1.getSize();
                        v_gns_pn = v_byt_pn.width;
                        v_yks_pn = v_byt_pn.height;                    
                        v_pay    = 8;                        
    }    
    
    public static void callMainScreen() {  
        MainFrame.jDsPane1.setEnabled(true);
        MainFrame.jDsPane1.setFocusable(true);
        MainFrame.jMnBar1.setVisible(true);
        MainFrame.setDefaultLookAndFeelDecorated(true);
    }
    
    public static void callLogin( javax.swing.JFrame i_frame ) {               
        jMnBar1.setVisible(false);        
        jTxArea1.setEditable(false);
        jTxArea1.setBackground(v_renkZemin);  
        getCoordinate();        
        final int v_yks_tp =v_yks_pn +v_yks_mb;        
      
        i_frame.setVisible(true);            
        i_frame.setLocation( v_gns_pn / 4, v_yks_tp / 4 );
    } 

    public void callScreen( int i_prg_kodu ) {            
        showScreen(i_prg_kodu);  
        if ( i_prg_kodu == 2 ) { refreshSql(1); }
    }   
    
    public static void showScreen( int i_prg_kodu ) {
        jDsPane1.setVisible(true);        
      if       ( i_prg_kodu == 0 ) { jDsPane1.setVisible(false); jDsPane2.setVisible(false); }
      else if  ( i_prg_kodu == 1 ) { jDsPane1.setVisible(false); jDsPane2.setVisible(true); } 
      else if  ( i_prg_kodu == 2 ) { jDsPane1.setVisible(true); }      
    }

    public void closeScreen() {    
        if ( jDsPane1.getWidth()>0 || jDsPane2.getWidth()>0 ) {
             jDsPane1.setVisible(false); jDsPane2.setVisible(false);           
        }
        else { System.exit(0); }
    }
    
    public void refreshSql( int i_sql_no ) {
        v_sql_ek = "";
        if ( jChBox1.isSelected() ) { v_sql_ek = " and STATUS='ACTIVE'"+v_sql_ek; } 
        if ( jChBox2.isSelected() ) { v_sql_ek = " and ( USERNAME not like 'SYS%' and USERNAME != 'DBSNMP')"+v_sql_ek; }        
        if ( jChBox3.isSelected() ) { v_sql_ek = " and USERNAME is not null"+v_sql_ek; }         
        if ( jChBox4.isSelected() ) { v_sql_ek = " and audsid != userenv('sessionid')"+v_sql_ek; }        
        
        String i_sql="";
        if ( i_sql_no==1 ) {
          i_sql = " select inst_id inst_id_nr, sid sid_nr, serial# serial#_nr, status, logon_time, floor(last_call_et / 60) duration_mins_nr, username, module, client_info, osuser, machine, terminal, action, blocking_instance, blocking_session, blocking_session_status  from gv$session where 1=1"+v_sql_ek;               
        }
        int v_dil = pkgConnection.LoginFrame.jCmBoxDil.getSelectedIndex();
        jTxArea1.setText(setExpression(0,v_dil) + String.valueOf(v_dbOpr.sqlSelect(i_sql, i_sql_no, jTable1, false))+setExpression(1,v_dil));
        jScPane1.setVisible(true);
        jScPane1.revalidate();
        jScPane1.repaint();               
    }

    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException {
        initLookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame anaPencere = new MainFrame();
                anaPencere.setVisible(true);
            }
        });
    }

    private static void initLookAndFeel() {
        String lookAndFeel;        
        if (v_lookandfeel != null) {
            if (v_lookandfeel.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            
            else if (v_lookandfeel.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } 
            
            else if (v_lookandfeel.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } 
            
            else if (v_lookandfeel.equals("GTK")) { 
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } 
            
            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "+ v_lookandfeel);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {
                UIManager.setLookAndFeel(lookAndFeel);
                
                // If L&F = "Metal", set the theme                
                if (v_lookandfeel.equals("Metal")) {
                  if (v_theme.equals("DefaultMetal"))
                     MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                  else if (v_theme.equals("Ocean"))
                     MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                //else
                   //MetalLookAndFeel.setCurrentTheme(new abcTheme());                     
                //UIManager.setLookAndFeel(new MetalLookAndFeel()); 
                }	
            }             
            catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"+ lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            } 
            
            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("+ lookAndFeel+ ") on this platform.");
                System.err.println("Using the default look and feel.");
            } 
            
            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("+ lookAndFeel+ "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDsPane2 = new javax.swing.JDesktopPane();
        jIntFrameConn = new javax.swing.JInternalFrame();
        jSpinnerConnSize = new javax.swing.JSpinner();
        LblDispSize = new java.awt.Label();
        jIntFrameText = new javax.swing.JInternalFrame();
        jLabel1 = new javax.swing.JLabel();
        jCmBoxDil2 = new javax.swing.JComboBox();
        jDsPane1 = new javax.swing.JDesktopPane();
        jScPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTxArea1 = new javax.swing.JTextArea();
        jChBox3 = new javax.swing.JCheckBox();
        jChBox2 = new javax.swing.JCheckBox();
        jChBox1 = new javax.swing.JCheckBox();
        jChBox4 = new javax.swing.JCheckBox();
        jBtRefresh = new javax.swing.JButton();
        jMnBar1 = new javax.swing.JMenuBar();
        jMenuScreens = new javax.swing.JMenu();
        jMnItemSessions = new javax.swing.JMenuItem();
        jMnItemExit = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMnItemPreferences = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("OzhanDBMan");
        setAutoRequestFocus(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jDsPane2.setBackground(new java.awt.Color(255, 204, 204));

        jIntFrameConn.setTitle("Connection");
        jIntFrameConn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jIntFrameConn.setVisible(true);

        jSpinnerConnSize.setModel(new javax.swing.SpinnerNumberModel(0, 0, 25, 1));
        jSpinnerConnSize.setAutoscrolls(true);
        jSpinnerConnSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerConnSizeStateChanged(evt);
            }
        });

        LblDispSize.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        LblDispSize.setName(""); // NOI18N
        LblDispSize.setText("Display Size");

        javax.swing.GroupLayout jIntFrameConnLayout = new javax.swing.GroupLayout(jIntFrameConn.getContentPane());
        jIntFrameConn.getContentPane().setLayout(jIntFrameConnLayout);
        jIntFrameConnLayout.setHorizontalGroup(
            jIntFrameConnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jIntFrameConnLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(LblDispSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jSpinnerConnSize, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jIntFrameConnLayout.setVerticalGroup(
            jIntFrameConnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jIntFrameConnLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jIntFrameConnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblDispSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerConnSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jIntFrameText.setTitle("Text");
        jIntFrameText.setToolTipText("");
        jIntFrameText.setPreferredSize(new java.awt.Dimension(326, 304));
        jIntFrameText.setVisible(true);

        jLabel1.setText("Language");
        jLabel1.setToolTipText("");

        jCmBoxDil2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCmBoxDil2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCmBoxDil2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jIntFrameTextLayout = new javax.swing.GroupLayout(jIntFrameText.getContentPane());
        jIntFrameText.getContentPane().setLayout(jIntFrameTextLayout);
        jIntFrameTextLayout.setHorizontalGroup(
            jIntFrameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jIntFrameTextLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addComponent(jCmBoxDil2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jIntFrameTextLayout.setVerticalGroup(
            jIntFrameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jIntFrameTextLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jIntFrameTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jCmBoxDil2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDsPane2Layout = new javax.swing.GroupLayout(jDsPane2);
        jDsPane2.setLayout(jDsPane2Layout);
        jDsPane2Layout.setHorizontalGroup(
            jDsPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDsPane2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jIntFrameConn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jIntFrameText, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(478, Short.MAX_VALUE))
        );
        jDsPane2Layout.setVerticalGroup(
            jDsPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDsPane2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jDsPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jIntFrameConn)
                    .addComponent(jIntFrameText, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                .addContainerGap(683, Short.MAX_VALUE))
        );
        jDsPane2.setLayer(jIntFrameConn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane2.setLayer(jIntFrameText, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jDsPane1.setBackground(new java.awt.Color(204, 204, 204));
        jDsPane1.setForeground(new java.awt.Color(255, 204, 204));
        jDsPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jDsPane1FocusGained(evt);
            }
        });
        jDsPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jDsPane1ComponentResized(evt);
            }
        });

        jScPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScPane1ComponentResized(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setToolTipText("");
        jTable1.setGridColor(new java.awt.Color(204, 204, 255));
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScPane1.setViewportView(jTable1);

        jTxArea1.setBackground(new java.awt.Color(204, 204, 255));
        jTxArea1.setColumns(20);
        jTxArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTxArea1.setRows(5);

        jChBox3.setToolTipText("");
        jChBox3.setMaximumSize(new java.awt.Dimension(127, 23));
        jChBox3.setMinimumSize(new java.awt.Dimension(127, 23));
        jChBox3.setName(""); // NOI18N
        jChBox3.setPreferredSize(new java.awt.Dimension(127, 23));
        jChBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBox3ActionPerformed(evt);
            }
        });

        jChBox2.setSelected(true);
        jChBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBox2ActionPerformed(evt);
            }
        });

        jChBox1.setSelected(true);
        jChBox1.setMaximumSize(new java.awt.Dimension(127, 23));
        jChBox1.setMinimumSize(new java.awt.Dimension(127, 23));
        jChBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBox1ActionPerformed(evt);
            }
        });

        jChBox4.setSelected(true);
        jChBox4.setToolTipText("");
        jChBox4.setMaximumSize(new java.awt.Dimension(127, 23));
        jChBox4.setMinimumSize(new java.awt.Dimension(127, 23));
        jChBox4.setName(""); // NOI18N
        jChBox4.setPreferredSize(new java.awt.Dimension(127, 23));
        jChBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBox4ActionPerformed(evt);
            }
        });

        jBtRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgMain/gtk_refresh_k.png"))); // NOI18N
        jBtRefresh.setToolTipText("refresh");
        jBtRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDsPane1Layout = new javax.swing.GroupLayout(jDsPane1);
        jDsPane1.setLayout(jDsPane1Layout);
        jDsPane1Layout.setHorizontalGroup(
            jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDsPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDsPane1Layout.createSequentialGroup()
                        .addComponent(jChBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jChBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jChBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jChBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 548, Short.MAX_VALUE)
                        .addComponent(jBtRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDsPane1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTxArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jDsPane1Layout.setVerticalGroup(
            jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDsPane1Layout.createSequentialGroup()
                .addGroup(jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDsPane1Layout.createSequentialGroup()
                        .addContainerGap(24, Short.MAX_VALUE)
                        .addGroup(jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDsPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jChBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jChBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jChBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jChBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE))
                    .addGroup(jDsPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jBtRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTxArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(327, 327, 327))
        );
        jDsPane1.setLayer(jScPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jTxArea1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jChBox3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jChBox2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jChBox1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jChBox4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDsPane1.setLayer(jBtRefresh, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMnBar1.setForeground(new java.awt.Color(51, 51, 255));
        jMnBar1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jMnBar1ComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jMnBar1ComponentShown(evt);
            }
        });

        jMenuScreens.setText("Screens");

        jMnItemSessions.setText("Sessions");
        jMnItemSessions.setToolTipText("");
        jMnItemSessions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemSessionsActionPerformed(evt);
            }
        });
        jMnItemSessions.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jMnItemSessionsKeyPressed(evt);
            }
        });
        jMenuScreens.add(jMnItemSessions);

        jMnItemExit.setForeground(new java.awt.Color(255, 51, 51));
        jMnItemExit.setText("Exit");
        jMnItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemExitActionPerformed(evt);
            }
        });
        jMenuScreens.add(jMnItemExit);

        jMnBar1.add(jMenuScreens);

        jMenuTools.setText("Tools");

        jMnItemPreferences.setText("Preferences");
        jMnItemPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemPreferencesActionPerformed(evt);
            }
        });
        jMenuTools.add(jMnItemPreferences);

        jMnBar1.add(jMenuTools);

        setJMenuBar(jMnBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDsPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jDsPane2, javax.swing.GroupLayout.Alignment.TRAILING))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDsPane1)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jDsPane2))
        );

        getAccessibleContext().setAccessibleName("OzhanOracleDBMan");
        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMnItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemExitActionPerformed
        closeScreen();        
    }//GEN-LAST:event_jMnItemExitActionPerformed

    private void jMnItemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemPreferencesActionPerformed
        v_prg_kodu = 1;  callScreen(v_prg_kodu); 
    }//GEN-LAST:event_jMnItemPreferencesActionPerformed

    private void jMnItemSessionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemSessionsActionPerformed
        v_prg_kodu = 2;  callScreen(v_prg_kodu);
    }//GEN-LAST:event_jMnItemSessionsActionPerformed

    private void jMnItemSessionsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMnItemSessionsKeyPressed

    }//GEN-LAST:event_jMnItemSessionsKeyPressed

    private void jMnBar1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jMnBar1ComponentHidden
        this.enable(false);
    }//GEN-LAST:event_jMnBar1ComponentHidden

    private void jMnBar1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jMnBar1ComponentShown
        this.enable(true);
    }//GEN-LAST:event_jMnBar1ComponentShown

    private void jDsPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDsPane1FocusGained
        //mtCagirEkranAna();
    }//GEN-LAST:event_jDsPane1FocusGained

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       
//System.out.println(tblSes.getValueAt(tblSes.getCellRenderer(WIDTH, WIDTH),0).toString());
         //System.out.println(v_kss[0]);  System.out.println(v_kss[1]);         
         //System.out.println(tblSes.getSelectedRowCount());
           int r = jTable1.getSelectedRowCount();
        if ( SwingUtilities.isRightMouseButton(evt) && r > 0 )
        {
         //System.out.println(r);
           for (int i = 0; i < 2; i++) { 
            v_kss[i] = jTable1.getValueAt(jTable1.getSelectedRow(),i).toString(); 
           }            
            JOptionPane.showConfirmDialog( null, " inst_id : "+ v_kss[0] + "   sid : "+ v_kss[1] ); 
        }
        /*else {
            v_kss[0] = ""; v_kss[1] = "";
        }*/
    }//GEN-LAST:event_jTable1MouseClicked

    private void jChBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBox3ActionPerformed
        refreshSql(1);
    }//GEN-LAST:event_jChBox3ActionPerformed

    private void jChBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBox2ActionPerformed
        refreshSql(1);
    }//GEN-LAST:event_jChBox2ActionPerformed

    private void jChBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBox1ActionPerformed
        refreshSql(1);
    }//GEN-LAST:event_jChBox1ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeScreen();
    }//GEN-LAST:event_formWindowClosing

    private void jDsPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jDsPane1ComponentResized
      //System.out.println(v_gns_pn+" pnDesktopAnaComponentResized");       
    }//GEN-LAST:event_jDsPane1ComponentResized

    private void jScPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScPane1ComponentResized
      //System.out.println(v_gns_pn);
    }//GEN-LAST:event_jScPane1ComponentResized

    private void jSpinnerConnSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerConnSizeStateChanged
        try {
             String s = jSpinnerConnSize.getValue().toString();
           //int i = Integer.parseInt(s);  
             LoginFrame.setConf("v_conn_dsSize", s );
             jSpinnerConnSize.setToolTipText(v_conn_dsWarn);
            } catch (IOException e) {
             Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
            }
    }//GEN-LAST:event_jSpinnerConnSizeStateChanged

    private void jCmBoxDil2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCmBoxDil2ActionPerformed
        setLang( 1 );
    }//GEN-LAST:event_jCmBoxDil2ActionPerformed

    private void jChBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBox4ActionPerformed
        refreshSql(1);
    }//GEN-LAST:event_jChBox4ActionPerformed

    private void jBtRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtRefreshActionPerformed
        refreshSql(1);
    }//GEN-LAST:event_jBtRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static java.awt.Label LblDispSize;
    public static javax.swing.JButton jBtRefresh;
    public static javax.swing.JCheckBox jChBox1;
    public static javax.swing.JCheckBox jChBox2;
    public static javax.swing.JCheckBox jChBox3;
    public static javax.swing.JCheckBox jChBox4;
    public static javax.swing.JComboBox jCmBoxDil2;
    private javax.swing.JDialog jDialog1;
    public static javax.swing.JDesktopPane jDsPane1;
    public static javax.swing.JDesktopPane jDsPane2;
    public static javax.swing.JInternalFrame jIntFrameConn;
    public static javax.swing.JInternalFrame jIntFrameText;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JMenu jMenuScreens;
    public static javax.swing.JMenu jMenuTools;
    public static javax.swing.JMenuBar jMnBar1;
    public static javax.swing.JMenuItem jMnItemExit;
    public static javax.swing.JMenuItem jMnItemPreferences;
    public static javax.swing.JMenuItem jMnItemSessions;
    private javax.swing.JScrollPane jScPane1;
    public static javax.swing.JSpinner jSpinnerConnSize;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTextArea jTxArea1;
    // End of variables declaration//GEN-END:variables
}
