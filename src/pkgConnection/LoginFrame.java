package pkgConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import static pkgConnection.LoginFrame.jButton1;
import static pkgConnection.LoginFrame.v_conn_dsSize;
import static pkgMain.MainFrame.*;
import static pkgOrtak.ColorizeSurface.colorizeSurface;

public class LoginFrame extends javax.swing.JFrame {    
     Connection conn = null;
     OraclePreparedStatement pst = null;
     OracleResultSet rs = null;     
     public static BufferedReader v_dosya_conBR;
     public static FileReader v_dosya_conFR;
     public static String v_dosya_connSTR,v_dosya_connSTR_TT="";     
     public static String a[];
     public static String a2[][]; 
     public static int    v_dil;      
     public static String v_dosya_conn  = System.getProperty("user.home")+"\\odbmConn";
     public static String v_dosya_conn_ = System.getProperty("user.home")+"\\odbmConn_";     
     public static String v_dosya_conf  = System.getProperty("user.home")+"\\odbmConf";
     public static String v_dosya_conf_ = System.getProperty("user.home")+"\\odbmConf_";          
     public static String v_tooltip_db  = "Host:Port/Service Name [ Sample : db01-vip:1521/proddb ]";
     public static int    v_conn_dsSize ;
     public static String v_conn_dsWarn ;
     
    public LoginFrame() {
        try{
            initComponents();
            getLang();
            getConn();
        } catch( Exception e ){ JOptionPane.showMessageDialog(null, e); }
    }
    
    public static void getLang(){ 
         try {
             int v_dil_;
             v_dil =  getConf("v_dil",0,v_dosya_conf,"="); 
             v_dil_ = v_dil;
             jCmBoxDil.removeAllItems();                             
             jCmBoxDil2.removeAllItems();             
             String[] v_diller = {"English","Türkçe","Italiano"};   
             for (int i = 0; i < 3; i++) { jCmBoxDil.addItem(v_diller[i]); pkgMain.MainFrame.jCmBoxDil2.addItem(v_diller[i]); }
             v_dil = v_dil_;  
             jCmBoxDil.setSelectedIndex(v_dil);           
         } catch (IOException ex) {
             Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
         }        
    } 

    public static void setLang( int i_nereden ) {
     try {     
            if      ( i_nereden == 0 )  { v_dil = jCmBoxDil.getSelectedIndex();  jCmBoxDil2.setSelectedItem(jCmBoxDil.getSelectedItem()); }
            else if ( i_nereden == 1 )  { v_dil = jCmBoxDil2.getSelectedIndex(); }             
            pkgOrtak.LanguageChoice.setLanguage(v_dil);
            String s = Integer.toString(v_dil);
            setConf("v_dil",s);
         } catch (IOException e) {
             Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, e);
         }        
    }
    
    public void getConn() throws IOException
    {
        int j=0;         
        try{
            v_conn_dsSize =  getConf("v_conn_dsSize",3,v_dosya_conf,"="); 
            v_dosya_connSTR_TT="";
    	    File v_dosya0 = new File(v_dosya_conn); if( !v_dosya0.exists()){ v_dosya0.createNewFile(); }                        
            jPpMenu1.removeAll();
            v_dosya_conFR = new FileReader(v_dosya_conn);
            v_dosya_conBR = new BufferedReader(v_dosya_conFR);
        while ( j < v_conn_dsSize && (v_dosya_connSTR=v_dosya_conBR.readLine())!=null ) {
            j++;
            if ( v_conn_dsSize>0 ) { for(int i=0;i<v_dosya_connSTR.length();i++){ v_dosya_connSTR_TT=v_dosya_connSTR_TT+v_dosya_connSTR.substring(i,i+1); }}
          //else                   { v_dosya_connSTR_TT="";  }
                JMenuItem MenuItem0 = new JMenuItem(v_dosya_connSTR_TT);
                MenuItem0.addActionListener(JPopupMenuListener);
                jPpMenu1.add(MenuItem0); v_dosya_connSTR_TT="";            
        }
            v_dosya_conFR.close();
            v_dosya_conBR.close();
           }
        catch ( IOException e ){ JOptionPane.showMessageDialog(null, e); }
    }

    public static boolean chkIfExists( String i_satir, String i_dosya ) throws IOException {         
            boolean v_varmi = false;
            FileReader fr = null; 
         try {
              fr = new FileReader(i_dosya);
         } catch (FileNotFoundException ex) {
             Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
        BufferedReader br = new BufferedReader(fr); 
        String s; 
        while((s = br.readLine()) != null) { 
            if ( s.equals(i_satir) ) { v_varmi = true; }                
        }
        fr.close(); 
        
        return v_varmi;        
    } 
    
    public void setConn( String i_uname, String i_db ) throws IOException {    
            int j=0; 
            String s = i_uname+"@"+i_db;        
      try {      
            File inputFile = new File(v_dosya_conn);  
            File tempFile  = new File(v_dosya_conn_); 
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); 
        if ( !chkIfExists(s,v_dosya_conn) && ( v_conn_dsSize > 0 ) ) { writer.write(s+ ("\n")); j++; }
        while( j < v_conn_dsSize && (s = reader.readLine()) != null ) { 
            j++;          
            writer.write(s+ ("\n"));
        }        
            renFile( reader, writer, inputFile, tempFile );
      }
      catch ( IOException e ){ JOptionPane.showMessageDialog(null, e); }              
    }    
    public static void setConf( String i_prm_adi, String i_deger ) throws IOException{
            String s;         
        try{    
            File inputFile = new File(v_dosya_conf);  
            File tempFile  = new File(v_dosya_conf_); 
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); 
            while( (s = reader.readLine()) != null ) { 
            if   ( s.contains(i_prm_adi) ) { writer.write(i_prm_adi+"="+i_deger+("\n")); }
            else { writer.write(s+ ("\n")); }
            }          
            renFile( reader, writer, inputFile, tempFile );
           } catch(IOException e){ JOptionPane.showMessageDialog(null, e); }        
    }  
    
    public static int getConf( String i_prm_adi, int o_deger, String i_dosya, String i_chr ) throws IOException
    {
            BufferedReader reader=null;
      
            String s; 
            File inputFile = new File(i_dosya);
            if( !inputFile.exists()){ inputFile.createNewFile(); }
            reader = new BufferedReader(new FileReader(inputFile));
            
        while((s = reader.readLine()) != null) {             
            if      ( i_chr=="@" ) { if ( s.lastIndexOf(i_chr)>0 ) { o_deger++; } }
            else if ( i_chr=="=" ) { 
                if ( i_prm_adi.equals(s.substring(0,s.lastIndexOf(i_chr))) ) { o_deger = Integer.parseInt(s.substring(s.lastIndexOf(i_chr)+1,s.length())); }  }
        }

        return o_deger;
    }
    /*  Aşağıdaki metod, dosya sistemine atılan Config ya da Connection gibi dosyaların adlarının değiştirilmesi içindir. */
    public static void renFile( BufferedReader i_reader, BufferedWriter i_writer, File i_inputFile, File i_tempFile  ) {
         try {
             i_reader.close(); i_reader = null;
             i_writer.flush(); i_writer.close(); i_writer = null;
             System.gc();
             
             i_inputFile.setWritable(true);
             i_inputFile.delete();
             i_tempFile.renameTo(i_inputFile);
         } catch(IOException e){ JOptionPane.showMessageDialog(null, e); }        
    }
    
    public String encrypt(String ana)
    {
        String sifreli="";

        for(int i=0;i<ana.length();i++)
            sifreli=sifreli+String.format("%03d", (int) 999-ana.substring(i,i+1).toCharArray()[0]);
        return sifreli;
    }
    
    public String decrypt(String sifreli)
        {
            String ana="";
        for(int i=0;i<sifreli.length()/3;i++)
        {   
            int aInt = 999- Integer.parseInt(sifreli.substring(i*3+0,i*3+1)+sifreli.substring(i*3+1,i*3+2)+sifreli.substring(i*3+2,i*3+3));
            char deneme=(char)aInt;
            ana=ana+ deneme;
        }
        return ana;
        }
    
  //final File parent = new File(v_dosya_conn);       
    ActionListener JPopupMenuListener = new ActionListener() {@Override
    
    public void actionPerformed(ActionEvent evt) {  
            String s = evt.getActionCommand();
            int i = s.lastIndexOf('@');
            int l = s.length();
            TFuname.setText(s.substring(0,i));
            TFDbname.setText(s.substring(i+1,l));
            TFPasswd.requestFocusInWindow();
            }  };

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtGroup1 = new javax.swing.ButtonGroup();
        jPpMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jRdButton6 = new javax.swing.JRadioButton();
        jRdButton5 = new javax.swing.JRadioButton();
        jRdButton4 = new javax.swing.JRadioButton();
        jRdButton3 = new javax.swing.JRadioButton();
        jRdButton2 = new javax.swing.JRadioButton();
        jRdButton1 = new javax.swing.JRadioButton();
        TFuname = new javax.swing.JTextField();
        TFPasswd = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TFDbname = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jButtonEnter = new javax.swing.JButton();
        jCmBoxDil = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabelTooltipDB = new javax.swing.JLabel();

        jPpMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("L o g o n");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(204, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setComponentPopupMenu(jPpMenu1);
        jPanel1.setPreferredSize(new java.awt.Dimension(434, 65));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        jRdButton6.setBackground(new java.awt.Color(102, 153, 255));
        jBtGroup1.add(jRdButton6);
        jRdButton6.setToolTipText("");
        jRdButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton6ActionPerformed(evt);
            }
        });

        jRdButton5.setBackground(new java.awt.Color(255, 51, 204));
        jBtGroup1.add(jRdButton5);
        jRdButton5.setToolTipText("");
        jRdButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton5ActionPerformed(evt);
            }
        });

        jRdButton4.setBackground(new java.awt.Color(204, 204, 0));
        jBtGroup1.add(jRdButton4);
        jRdButton4.setToolTipText("");
        jRdButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton4ActionPerformed(evt);
            }
        });

        jRdButton3.setBackground(new java.awt.Color(0, 204, 153));
        jBtGroup1.add(jRdButton3);
        jRdButton3.setToolTipText("");
        jRdButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton3ActionPerformed(evt);
            }
        });

        jRdButton2.setBackground(new java.awt.Color(255, 153, 153));
        jBtGroup1.add(jRdButton2);
        jRdButton2.setToolTipText("");
        jRdButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton2ActionPerformed(evt);
            }
        });

        jRdButton1.setBackground(new java.awt.Color(153, 153, 153));
        jBtGroup1.add(jRdButton1);
        jRdButton1.setSelected(true);
        jRdButton1.setToolTipText("");
        jRdButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdButton1ActionPerformed(evt);
            }
        });

        TFuname.setNextFocusableComponent(TFPasswd);

        TFPasswd.setNextFocusableComponent(TFDbname);

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        jLabel3.setText("Database");
        jLabel3.setToolTipText("[ Host:Port/Service Name ]");

        TFDbname.setToolTipText("");
        TFDbname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TFDbnameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFDbnameFocusLost(evt);
            }
        });
        TFDbname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                TFDbnameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                TFDbnameMouseExited(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonEnter.setText("Enter");
        jButtonEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnterActionPerformed(evt);
            }
        });

        jCmBoxDil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCmBoxDil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCmBoxDilActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        jLabelTooltipDB.setForeground(new java.awt.Color(255, 0, 0));
        jLabelTooltipDB.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jRdButton2)
                        .addComponent(jRdButton3))
                    .addComponent(jRdButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRdButton5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRdButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRdButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TFPasswd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(TFuname, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TFDbname, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addGap(35, 35, 35)
                        .addComponent(jButtonEnter))
                    .addComponent(jLabelTooltipDB, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jCmBoxDil, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFuname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFDbname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTooltipDB, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jCmBoxDil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCancel)
                            .addComponent(jButtonEnter))))
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jRdButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRdButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRdButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRdButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRdButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRdButton6)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnterActionPerformed
        DBOperations.uname  = TFuname.getText(); 
        DBOperations.dbname = TFDbname.getText();
        DBOperations.passwd = TFPasswd.getText();       
      //JOptionPane.showMessageDialog(null, TFuname.getText());            
        conn =   DBOperations.sqlBaglan();
         try{
              this.setVisible(false); 
              callMainScreen();
              setConn(DBOperations.uname,DBOperations.dbname);
              pkgMain.MainFrame.jSpinnerConnSize.setValue(v_conn_dsSize);
            } 
         catch (Exception e)
            {
             JOptionPane.showMessageDialog(null, e);
             System.exit(0);
            }
    }//GEN-LAST:event_jButtonEnterActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jRdButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton1ActionPerformed
        colorizeSurface( jRdButton1.getBackground() ); //new Color(0,102,204)
    }//GEN-LAST:event_jRdButton1ActionPerformed

    private void jRdButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton2ActionPerformed
        colorizeSurface( jRdButton2.getBackground() ); //new Color(204,0,204)
    }//GEN-LAST:event_jRdButton2ActionPerformed

    private void jRdButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton3ActionPerformed
        colorizeSurface( jRdButton3.getBackground() ); // new Color(0,102,51)
    }//GEN-LAST:event_jRdButton3ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        colorizeSurface( jRdButton1.getBackground() ); //new Color(0,102,204)
    }//GEN-LAST:event_formComponentShown

    private void jRdButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton4ActionPerformed
        colorizeSurface( jRdButton4.getBackground() );
    }//GEN-LAST:event_jRdButton4ActionPerformed

    private void jCmBoxDilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCmBoxDilActionPerformed
        setLang( 0 );
    }//GEN-LAST:event_jCmBoxDilActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        showScreen(v_prg_kodu); 
    }//GEN-LAST:event_formWindowActivated

    private void jRdButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton5ActionPerformed
        colorizeSurface( jRdButton5.getBackground() );
    }//GEN-LAST:event_jRdButton5ActionPerformed

    private void jRdButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdButton6ActionPerformed
        colorizeSurface( jRdButton6.getBackground() );
    }//GEN-LAST:event_jRdButton6ActionPerformed
    
    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
         try {
             getConn();
             showPopupMenu(evt);
           //jButton1ActionPerformed(evt);
         } catch (IOException ex) {
             Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
    }//GEN-LAST:event_jButton1MousePressed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed

    }//GEN-LAST:event_jPanel1MousePressed

    private void TFDbnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFDbnameFocusGained
        jLabelTooltipDB.setText(v_tooltip_db);
    }//GEN-LAST:event_TFDbnameFocusGained

    private void TFDbnameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TFDbnameMouseEntered
        jLabelTooltipDB.setText(v_tooltip_db);
    }//GEN-LAST:event_TFDbnameMouseEntered

    private void TFDbnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFDbnameFocusLost
        jLabelTooltipDB.setText("");
    }//GEN-LAST:event_TFDbnameFocusLost

    private void TFDbnameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TFDbnameMouseExited
        jLabelTooltipDB.setText("");
    }//GEN-LAST:event_TFDbnameMouseExited

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
        } 
        catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TFDbname;
    private javax.swing.JPasswordField TFPasswd;
    private javax.swing.JTextField TFuname;
    private javax.swing.ButtonGroup jBtGroup1;
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButtonCancel;
    public static javax.swing.JButton jButtonEnter;
    public static javax.swing.JComboBox jCmBoxDil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabelTooltipDB;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPpMenu1;
    public static javax.swing.JRadioButton jRdButton1;
    private javax.swing.JRadioButton jRdButton2;
    private javax.swing.JRadioButton jRdButton3;
    private javax.swing.JRadioButton jRdButton4;
    private javax.swing.JRadioButton jRdButton5;
    private javax.swing.JRadioButton jRdButton6;
    // End of variables declaration//GEN-END:variables
    private void showPopupMenu(MouseEvent evt) { jPpMenu1.show(evt.getComponent(), evt.getX(), evt.getY()); }
    }

