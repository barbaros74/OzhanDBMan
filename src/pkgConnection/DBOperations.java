package pkgConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static pkgMain.MainFrame.v_tableModelFull;

public class DBOperations {
        public  static       Statement  durum       = null; 
	    public  static       Connection con         = null;
        public  static       ResultSet  sonuc       = null;        
		private static final String     module      = "OzhanDBMan";
		private static final String     action      = "login";
		private static final String     client      = "Barbaros Özhan";
        public  static       String     dbname      = " ";
        public  static       String     uname       = null;
        public  static       String     passwd      = null; 
        public  static       String[]   sql         = new String[10];
        public  static       boolean    sirala_     = true;
        public  static       String     err         = " ";        
        private static       Boolean    isConnected = false;        
        
	public static Connection sqlBaglan()
	{
            try
		{
			Class.forName("oracle.jdbc.OracleDriver");
                        con = DriverManager.getConnection("jdbc:oracle:thin:@//"+dbname,uname,passwd); 
                        CallableStatement call=con.prepareCall("begin dbms_application_info.set_module(module_name => ?, action_name => ?); dbms_application_info.set_client_info(client_info => ?); end;");
	    		call.setString(1,module);
	    		call.setString(2,action);
	    		call.setString(3,client);	    		
	    		call.execute();
		}
		catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        System.exit(0);
		}
            return null;
	}
        
        public static boolean statusConnect()
        {
            return isConnected;
        }        
	
	public static void sqlKapat()
	{
		try {
			if(!con.isClosed())
			    con.close();
		} catch (SQLException e) {
                    err += "\r\n Bağlantı Kapanmadı";
                        JOptionPane.showMessageDialog(null, err);                        
		}		
	}
        
	public void       setSql( String[] i_sql ) { this.sql = i_sql; }
        
        public static int sqlSelect( String i_sql, int i_sql_no, JTable i_table, Boolean i_mod )
        { 
            int satir_say=0;
           
            DefaultTableModel tableModel = new DefaultTableModel(){                
            @Override
            public Class getColumnClass(int col) { 
                        if ( getColumnName(col).endsWith("_NR") ) {
                            return Double.class;
                        }                  
                        else {
                            return String.class;
                        }
            }            
            };
            
            i_table.setDefaultRenderer(Object.class, new pkgOrtak.ColorizeZebra());             
            i_table.setDefaultRenderer(Double.class, i_table.getDefaultRenderer(Object.class));
            i_table.setAutoCreateRowSorter(true);
            try {
                durum =(Statement) con.createStatement();
                sonuc=durum.executeQuery(i_sql);
                ResultSetMetaData md = sonuc.getMetaData();
                int columns = md.getColumnCount();
                for (int i = 1; i <= columns; i++)
                {
                    String v_baslik = md.getColumnName(i);
                    if ( v_baslik.trim().endsWith("_NR") ) { v_baslik = v_baslik.substring(0,v_baslik.lastIndexOf("_NR")); }                    
                    tableModel.addColumn(v_baslik);
                }

                while (sonuc.next())
                {
                   Vector dataVector = new Vector();
                    for(int i=1;i<=columns;i++){                        
                        if ( md.getColumnName(i).endsWith("_NR") ) {
                              dataVector.add(Integer.parseInt(sonuc.getString(i)));                                                                             
                        }
                        else {
                              dataVector.add(sonuc.getString(i));                            
                        }
                    }
                        tableModel.addRow(dataVector);     
                        
                }
                        satir_say=sonuc.getRow();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            } 
            i_table.setModel(tableModel);
            i_table.repaint();
            v_tableModelFull=(DefaultTableModel) i_table.getModel();
            
            return (satir_say);
        }
             public static void sqlInsert(String table_name, String sicil,String sicilHedef, JTable table)
        { 
            String a="select * from "+table_name+" where sicil_no='"+ sicil + "' and os_user not like 'abc%'";
          int satir_say=0;
            table.removeAll();
            try {
                durum =(Statement) con.createStatement();
                sonuc=durum.executeQuery(a);
                String str;
                while (sonuc.next())
                {
                    str ="INSERT INTO "+table_name+" VALUES("+sicilHedef+", ";
                    for(int i=2;i<=6;i++){
                        if(i==2 || i==3 || i==6)
                            str +="'"+ sonuc.getString(i) +"',"; 
                        else
                            str +=sonuc.getString(i) +","; 
                    }
                    str=str.substring(0, str.length()-1)+")";
                    System.err.println(str);
                    PreparedStatement pstmt=con.prepareStatement(str);
                    pstmt.executeUpdate();
                    pstmt.clearParameters();
                }
                    satir_say=sonuc.getRow();
                }
            catch (Exception e) {
                    System.out.println(e.toString());
                }  
            }
}


