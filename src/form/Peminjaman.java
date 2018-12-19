/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import Tools.KoneksiDB;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author User1
 */
public class Peminjaman extends javax.swing.JFrame {

    /**
     * Creates new form Peminjaman
     */
    Connection _Cnn;
    KoneksiDB getCnn = new KoneksiDB();
    
    private String v_Nama, v_Divisi, v_noTelepon, v_Tujuan, v_IDUser, v_id;
    private String v_Keterangan, v_Plat, km_awal_aset;
    String date;
    private String sqlSelect, sqlInsert, sqlDelete, sqlUpdate;
    DefaultTableModel tblpeminjaman;
   
    
    public Peminjaman() {
        initComponents();
        Tampil();
        Tanggal();
        enableButton();
    }
    
    public void enableButton(){
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnPrint.setEnabled(false);
    }
    
    public void Tanggal(){
       Date tgl = new Date();
       dcPinjam.setDate(tgl);
    }
    
    public void clearPeminjaman (){
        txtNoTelpon.setText("");
        txtPlat.setText("");
        txtNama.setText("");
        txtDivisi.setText("");
        txtTujuan.setText("");
        cbKeterangan.setSelectedIndex(0);
        dcPinjam.setDate(null);
        btnSubmit.setEnabled(true);
    }
    
    public void deklarasi(){
        v_id=vId.getText();
        v_noTelepon = txtNoTelpon.getText();
        v_Tujuan = txtTujuan.getText();
        v_Plat = txtPlat.getText().toUpperCase();
        v_Nama = txtNama.getText();
        v_Divisi = txtDivisi.getText();
        v_Keterangan = (String) cbKeterangan.getSelectedItem().toString();
        v_IDUser = String.valueOf(Session.IDPengguna).toString();
       
        
        //setting tanggal
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dFormat.format(dcPinjam.getDate());
        
} 
    
    public void SQLSimpan(){
        deklarasi();
        try{
        sqlInsert = "INSERT INTO tb_peminjaman SET id_user = '"+v_IDUser+"',"
                      +"plat_nomor = '"+v_Plat+"',"
                      +"nama_peminjam='"+v_Nama+"',"
                      +"divisi ='"+v_Divisi+"',"
                      +"telepon='"+v_noTelepon+"',"
                      +"tanggal_peminjaman='"+date+"',"
                      +"tujuan ='"+v_Tujuan+"',"
                      +"keterangan='"+v_Keterangan+"'";
               Statement state = _Cnn.createStatement();
               state.executeUpdate(sqlInsert);
                  
               
               //update status aset 
               sqlUpdate ="UPDATE tb_asset SET status_asset ='Terpakai' WHERE plat_nomor = '"+v_Plat+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlUpdate);
                
               JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }
    
    public void Simpan(){
        deklarasi();
         
        //validasi form pengisian
        if(v_noTelepon.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi Nomor Telepon !");
        }else if(v_Plat.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi Kilometer Awal !");
        }else if(v_Tujuan.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi Tujuan !");
        }else if(cbKeterangan.getSelectedIndex()<= 0){
            JOptionPane.showMessageDialog(null,"Anda Harus Memilih Keterangan !");
        }else{
        
        try{
             _Cnn = null;
            _Cnn = getCnn.getConnection();
                    //pengecekan plat nomor 
                    sqlSelect = "select * from tb_asset where plat_nomor ='"+v_Plat+"'";
                    Statement stat = _Cnn.createStatement(); 
                    ResultSet res = stat.executeQuery(sqlSelect);
                    
          if(res.first()){
              //insert ke table peminjaman
              String status = res.getString("status_asset");
              String kondisi = res.getString("kondisi_asset");
              if(status.equals("Siap Pakai")){
                  if(kondisi.equals("Layak Pakai")){
                      SQLSimpan();
                  }else{
                      if(cbKeterangan.getSelectedItem().equals("Berpergian")){
                        JOptionPane.showMessageDialog(this, "kendaraan harus di service!");
                      }else{
                         SQLSimpan();
                      }
                  }
                  
               clearPeminjaman();
               Tanggal();
              }else{
                  JOptionPane.showMessageDialog(null,"Kendaraan Sudah Digunakan.");
              }
          }else{
              JOptionPane.showMessageDialog(null,"Plat Tidak Terdaftar!");
          }
        }catch(Exception e){
           JOptionPane.showMessageDialog(this, e.getMessage());
        }
        }
        Tampil();
    }
    
    public void AksiDelete(){
        v_Plat = txtPlat.getText();
       
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda ingin menghapus data ini? Kode "+v_Plat,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab == JOptionPane.YES_OPTION){
             try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqlDelete = "DELETE FROM tb_peminjaman WHERE plat_nomor = '"+txtPlat.getText()+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlDelete);
                JOptionPane.showMessageDialog(this,"berhasil dihhapus");
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(this, e);
                txtPlat.requestFocus(true);
            }
        } 
        Tampil();
        clearPeminjaman();
        Tanggal();
    }
    
    public void Tampil(){
     tblpeminjaman = new DefaultTableModel();

        tblpeminjaman.addColumn("ID");
        tblpeminjaman.addColumn("NOMOR PLAT");
        tblpeminjaman.addColumn("NAMA");
        tblpeminjaman.addColumn("DIVISI");
        tblpeminjaman.addColumn("TELEPON");
        tblpeminjaman.addColumn("TANGGAL PEMINJAMAN");
        tblpeminjaman.addColumn("TUJUAN");
        tblpeminjaman.addColumn("KETERANGAN");
        tblpeminjaman.setRowCount(0);
        
         
        try {
            int num = 1;
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlSelect ="SELECT * FROM tb_peminjaman";
            Statement stat = _Cnn.createStatement();   
            ResultSet res = stat.executeQuery(sqlSelect);
            while(res.next()){
                tblpeminjaman.addRow(new Object[]{
//                num++,
                res.getString(1),
                res.getString(3),
                res.getString(4),
                res.getString(5),
                res.getString(6),
                res.getString(7),
                res.getString(8),
                res.getString(9),
            });
            }tblPinjam.setModel(tblpeminjaman);
        } catch (Exception e) {
        
        }
}
    
    public void AksiUpdate(){
        v_noTelepon = txtNoTelpon.getText();
        v_Tujuan = txtTujuan.getText();
        v_Plat = txtPlat.getText();
        v_Nama = txtNama.getText();
        v_Divisi = txtDivisi.getText();
        v_Keterangan = (String) cbKeterangan.getSelectedItem().toString();
        v_IDUser = String.valueOf(Session.IDPengguna).toString();
       SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dFormat.format(dcPinjam.getDate());
    
        if (v_Divisi.equals("")|| v_Nama.equals("")|| v_noTelepon.equals("")|| v_Tujuan.equals("")|| v_Keterangan.equals("") || v_Plat.equals("") ||dcPinjam.getDate() == null) {
            JOptionPane.showMessageDialog(this,"harap lengkapi data");
        }else{
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqlUpdate ="UPDATE tb_peminjaman SET plat_nomor ='"+v_Plat
                        +"', nama_peminjam ='"+v_Nama
                        +"', divisi ='"+v_Divisi
                        +"', telepon ='"+v_noTelepon
                        +"', tanggal_peminjaman ='"+date
                        +"', tujuan ='"+v_Tujuan
                        +"', keterangan ='"+v_Keterangan+"' where plat_nomor = '"+v_Plat+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlUpdate);
                JOptionPane.showMessageDialog(this,"berhasil diupdate");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
                txtPlat.requestFocus(true);
                
            }
        }
        Tampil();
        clearPeminjaman();
        Tanggal();
}
    
    private void Back(){
        form.Menu menu = new form.Menu();
        menu.setVisible(true);
        this.dispose();
    }
    
    public PageFormat getPageFormat(PrinterJob pj){
    
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    

        double middleHeight =8.0;  
        double headerHeight = 2.0;                  
        double footerHeight = 2.0;                  
        double width = convert_CM_To_PPI(8);
        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
        paper.setSize(width, height);
        paper.setImageableArea(                    
            0,
            10,
            width,            
            height - convert_CM_To_PPI(1)
        );   
            
        pf.setOrientation(PageFormat.PORTRAIT);           
        pf.setPaper(paper);    

        return pf;
    }
    
    protected static double convert_CM_To_PPI(double cm) {            
	return toPPI(cm * 0.393600787);            
    }
 
    protected static double toPPI(double inch) {            
        return inch * 72d;            
    }

    public class BillPrintable implements Printable {
        public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) throws PrinterException {    
            int result = NO_SUCH_PAGE;    
              if (pageIndex == 0) {                    
                  Graphics2D g2d = (Graphics2D) graphics;                    
                  double width = pageFormat.getImageableWidth();                    
                  g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 
                  FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
                    try{
                        int y=20;
                        int yShift = 10;
                        int headerRectHeight=15;
                        int headerRectHeighta=40;

                        String pn0a="KODE UNIK";
                        String pn1a="PLAT NOMOR";
                        String pn2a="PEMINJAM";
                        String pn3a="DIVISI";
                        String pn4a="TELEPON";
                        String pn5a="TANGGAL PINJAM";
                        String pn6a="TUJUAN";
                        String pn7a="KETERANGAN";
                        deklarasi();
                        g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
                        g2d.drawString("-------------------------------------",12,y);y+=yShift;
                        g2d.drawString("           BUKTI PEMINJAMAN          ",12,y);y+=yShift;
                        g2d.drawString("-------------------------------------",10,y);y+=yShift;
                        g2d.drawString(" DATA                    DETAIL    ",10,y);y+=yShift;
                        g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
                        g2d.drawString(" "+pn0a+"               "+v_id+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn1a+"              "+v_Plat+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn2a+"                "+v_Nama+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn3a+"                  "+v_Divisi+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn4a+"                 "+v_noTelepon+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn5a+"          "+date+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn6a+"                  "+v_Tujuan+"  ",10,y);y+=yShift;
                        g2d.drawString(" "+pn7a+"              "+v_Keterangan+"  ",10,y);y+=yShift;
                        g2d.drawString("-------------------------------------",10,y);y+=yShift;
                        g2d.drawString("-------------------------------------",10,y);y+=yShift;
                        g2d.drawString("            AUAH DEVELOPER           ",10,y);y+=yShift;
                        g2d.drawString("                  911                ",10,y);y+=yShift;
                        g2d.drawString("*************************************",10,y);y+=yShift;
                        g2d.drawString("         JANGAN SAMPAI HILANG        ",10,y);y+=yShift;
                        g2d.drawString("*************************************",10,y);y+=yShift;
                    }
                    catch(Exception r){
                    r.printStackTrace();
                    }
                    result = PAGE_EXISTS;    
                }    
                return result;    
            }
   }
    
     private void clearTable(){
        int row = tblpeminjaman.getRowCount();
        for (int i = 0; i < row; i++) {
            tblpeminjaman.removeRow(0);
        }
    }
     
     private void Cari(){
         if(txtCari.getText().equals("")){
            Tampil();
        }else{
              try {
            int num = 1;
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTable();
            sqlSelect ="SELECT * FROM tb_peminjaman WHERE plat_nomor LIKE '%"+txtCari.getText()+"%'";
            Statement stat = _Cnn.createStatement();   
            ResultSet res = stat.executeQuery(sqlSelect);
            while(res.next()){
                tblpeminjaman.addRow(new Object[]{
//                num++,
                res.getString(1),
                res.getString(3),
                res.getString(4),
                res.getString(5),
                res.getString(6),
                res.getString(7),
                res.getString(8),
                res.getString(9),
            });
            }
        } catch (Exception e) {
        
        }
        }
     }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDivisi = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtNoTelpon = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        dcPinjam = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTujuan = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cbKeterangan = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPlat = new javax.swing.JTextField();
        btnPrint = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        vId = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPinjam = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(681, 683));

        jLabel11.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Form Peminjaman Kendaraan");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/peminjaman.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Nama Peminjam");

        jLabel14.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Divisi");

        jLabel21.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("No. Telephone");

        jLabel20.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Tanggal Peminjaman");

        dcPinjam.setDateFormatString("yyyy-MM-d");

        jLabel16.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Tujuan");

        jLabel17.setFont(new java.awt.Font("Myriad Pro", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Pulang Pergi (PP)");

        jLabel19.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Keterangan");

        cbKeterangan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Service", "Berpergian" }));

        jLabel18.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Cetak Bukti Peminjaman");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/006-print.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Plat Nomor");

        btnPrint.setBackground(new java.awt.Color(255, 153, 51));
        btnPrint.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Kode : ");

        vId.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        vId.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(txtPlat, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(txtNama)
                    .addComponent(txtDivisi)
                    .addComponent(txtNoTelpon)
                    .addComponent(jLabel15)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(dcPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(vId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3)))
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPlat, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(txtDivisi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txtTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(cbKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPinjam.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPinjamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPinjam);

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        btnCari.setBackground(new java.awt.Color(255, 153, 51));
        btnCari.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnUpdate.setBackground(new java.awt.Color(255, 153, 51));
        btnUpdate.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 153, 51));
        btnCancel.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSubmit.setBackground(new java.awt.Color(255, 153, 51));
        btnSubmit.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 153, 51));
        btnDelete.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Hapus");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        jButton7.setBackground(new java.awt.Color(255, 153, 0));
        jButton7.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Back");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(260, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1346, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 912, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        AksiUpdate();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
         clearPeminjaman();
        Tanggal();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        Simpan();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void tblPinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPinjamMouseClicked
        // TODO add your handling code here:
        btnSubmit.setEnabled(false);
        btnPrint.setEnabled(true);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
        txtPlat.setEditable(false);

        int baris = tblPinjam.rowAtPoint(evt.getPoint());
        String id = tblPinjam.getValueAt(baris, 0).toString();
        vId.setText(id);
        
        String no_plat = tblPinjam.getValueAt(baris,1).toString();
        txtPlat.setText(no_plat);
        String namapeminjam = tblPinjam.getValueAt(baris,2).toString();
        txtNama.setText(namapeminjam);
        String divisi = tblPinjam.getValueAt(baris,3).toString();
        txtDivisi.setText(divisi);
        String telepon = tblPinjam.getValueAt(baris,4).toString();
        txtNoTelpon.setText(telepon);
        
        try {
            Date tanggal_peminjam = new SimpleDateFormat("yyyy-MM-dd").parse((String)tblPinjam.getValueAt(baris,5).toString());
            dcPinjam.setDate(tanggal_peminjam);
        } catch (ParseException ex) {
            Logger.getLogger(Peminjaman.class.getName()).log(Level.SEVERE, null, ex);
        }
        String tujuan = tblPinjam.getValueAt(baris,6).toString();
        txtTujuan.setText(tujuan);
        String keterangan = tblPinjam.getValueAt(baris,7).toString();
        cbKeterangan.setSelectedItem(keterangan);
    }//GEN-LAST:event_tblPinjamMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        AksiDelete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        PrinterJob pj = PrinterJob.getPrinterJob();        
        pj.setPrintable(new BillPrintable(),getPageFormat(pj));
        try {
             pj.print();
          
        }
         catch (PrinterException ex) {
                 ex.printStackTrace();
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Back();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        Cari();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        Cari();
    }//GEN-LAST:event_btnCariActionPerformed

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cbKeterangan;
    private com.toedter.calendar.JDateChooser dcPinjam;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPinjam;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDivisi;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoTelpon;
    private javax.swing.JTextField txtPlat;
    private javax.swing.JTextField txtTujuan;
    private javax.swing.JLabel vId;
    // End of variables declaration//GEN-END:variables
}
