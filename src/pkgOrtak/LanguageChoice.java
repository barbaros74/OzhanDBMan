package pkgOrtak;

import static pkgMain.MainFrame.*;
import static pkgConnection.LoginFrame.*;
public class LanguageChoice{
    public static String setExpression( int i_ifade_no, int i_dil  ) {
        String[] ifade = new String[60];          

        if      ( i_dil == 0 ) { ifade[0]="Total ";  ifade[1]=" rows selected "; }
        else if ( i_dil == 1 ) { ifade[0]="Toplam "; ifade[1]=" satır seçildi. "; }
        else if ( i_dil == 2 ) { ifade[0]="Totale "; ifade[1]=" file selezionati. "; }        
        
        return ifade[i_ifade_no];
    }   
    
    public static void setLanguage( int i_dil ) {
        
        if      ( i_dil == 0 ) { 
                                jMenuScreens.setText("Screens");  jMenuTools.setText("Tools");     jMnItemPreferences.setText("Preferences");  jMnItemSessions.setText("Sessions");  jMnItemExit.setText("Exit");
                                jChBox1.setText("Active Sessions"); jChBox2.setText("Non-system Sessions"); jChBox3.setText("Non-null Username Sessions"); jChBox4.setText("Exclude My Session");
                                jButtonEnter.setText("Enter"); jButtonCancel.setText("Cancel");
                                v_conn_dsWarn = "Value should be in the range 0-25";
                                pkgMain.MainFrame.LblDispSize.setText("Recent Conn. Display Size");
                                pkgMain.MainFrame.jIntFrameConn.setTitle("Connection");  
                                pkgMain.MainFrame.jIntFrameText.setTitle("Text");
                                jBtRefresh.setToolTipText("refresh");
                               }
        else if ( i_dil == 1 ) { 
                                jMenuScreens.setText("Ekranlar"); jMenuTools.setText("Gereçler");   jMnItemPreferences.setText("Tercihler");    jMnItemSessions.setText("Oturumlar"); jMnItemExit.setText("Çıkış");
                                jChBox1.setText("Faal Oturumlar");  jChBox2.setText("Sistem Dışı Oturumlar"); jChBox3.setText("UName Bilgisi Olan Oturumlar"); jChBox4.setText("Oturumumu Gösterme");                               
                                jButtonEnter.setText("Giriş"); jButtonCancel.setText("İptal");
                                v_conn_dsWarn = "0-25 aralığında değer almalıdır!";                                
                                pkgMain.MainFrame.LblDispSize.setText("Geçmiş Bağlantı Adedi");                                
                                pkgMain.MainFrame.jIntFrameConn.setTitle("Bağlantı");                                
                                pkgMain.MainFrame.jIntFrameText.setTitle("Metin");                                
                                jBtRefresh.setToolTipText("tazele");
                               }
        else if ( i_dil == 2 ) { 
                                jMenuScreens.setText("Schermi");  jMenuTools.setText("Strumenti"); jMnItemPreferences.setText("Preferenze");   jMnItemSessions.setText("Sessioni");  jMnItemExit.setText("Uscita");
                                jChBox1.setText("Le Sessioni Attivi"); jChBox2.setText("Le Sessioni Non Sisteme "); jChBox3.setText("Le Sessioni Con Pieno d'UName"); jChBox4.setText("Non Mostrare La Mia Sessione");
                                jButtonEnter.setText("Ingresso"); jButtonCancel.setText("Annulla");
                                v_conn_dsWarn = "Il valore dovrebbe essere nel intervallo 0-25";                                
                                pkgMain.MainFrame.LblDispSize.setText("Dimensione dei Recenti Collegamenti");                                  
                                pkgMain.MainFrame.jIntFrameConn.setTitle("Collegamento");
                                pkgMain.MainFrame.jIntFrameText.setTitle("Testo");                                
                                jBtRefresh.setToolTipText("rinfresca");
                               }        
    }     
    
    public static String getTitleFrame( int i_dil ) {        
        String    o_baslik = "";
        if      ( i_dil == 0 ) { o_baslik = "Sessions"; }        
        else if ( i_dil == 1 ) { o_baslik = "Oturumlar"; }
        else if ( i_dil == 2 ) { o_baslik = "Sessioni";  } 
        
        return o_baslik;
    }      
    
}
