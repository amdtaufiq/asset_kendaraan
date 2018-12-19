/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import Tools.KoneksiDB;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User1
 */
public class Pengembalian extends javax.swing.JFrame {

    /**
     * Creates new form Pengembalian
     */
    
    
    Connection _Cnn;
    KoneksiDB getCnn = new KoneksiDB();
    
    private String v_IDUser, nama_gambar, v_idPinjam, ImgPath,km_akhir_aset,date_kembali,v_tagihan,v_nota,plat_nomor, v_namaNota = null;
    private String sqlSelect, sqlInsert, sqlDelete, sqlUpdate;
    DefaultTableModel tblpengembalian;
    int kmAkhir, tagihan, km_akhir, km_awal, km_hasil_motor, km_hasil_mobil;
    
    //note : 
    // hasil_km_motor dan mobil merupakan hasil dari penjumlahan kilometer awal dengan batas jarak tempuh untuk mengganti oli/service
    public void Back(){
        form.Menu menu = new form.Menu();
        menu.setVisible(true);
        this.dispose();
    }
    
    public Pengembalian() {
        initComponents();
         Tanggal();
        btnNota.setEnabled(false);
        Clear();
        Tampil();
    }
    
    public void Tanggal(){
       Date tgl = new Date();
       dateKembali.setDate(tgl);
    }
    
    public void Clear(){
        txtIdPeminjam.setText("");
        txtkmAkhir.setText("");
        txtTagihan.setText("0");
        txtNota.setText("");
        Tanggal();
        txGambar.setIcon(null);
    }
    
    public void AmbilGambar(){
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images","jpg","jpeg","png");
        jfc.addChoosableFileFilter(filter);
        int result = jfc.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = jfc.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            ImgPath = path;
            nama_gambar = selectedFile.getName();
            txtNota.setText(nama_gambar);
            txGambar.setIcon(ResizeImage(path,null));
            
        }else{
            System.out.println("No File Selected");
        }
        
    }
    
//    private void focustxtKilometer(){
//        km_akhir_aset = txtkmAkhir.getText();
//        kmAkhir = Integer.parseInt(km_akhir_aset);
//        try{
//            
//                _Cnn = null;
//                _Cnn = getCnn.getConnection();
//                
//            sqlSelect = "select * from tb_peminjaman where id_peminjaman ='"+v_idPinjam+"'";
//                    Statement stat = _Cnn.createStatement(); 
//                    ResultSet res = stat.executeQuery(sqlSelect);
//          if(res.first()){
//              plat_nomor = res.getString("plat_nomor");
//            sqlSelect = "select * from tb_asset where plat_nomor='"+plat_nomor+"'";
//                    Statement statem = _Cnn.createStatement(); 
//                    ResultSet rest = statem.executeQuery(sqlSelect);
//          if(rest.first()){
//                km_awal = rest.getInt(km_awal);
//                km_akhir  = rest.getInt(km_akhir);
//                if(kmAkhir<=km_akhir){
//                    JOptionPane.showMessageDialog(null,"Kilometer yang dimasukkan tidak valid");
//                }
//                
//            }
//          }
//        }catch(Exception e){
//            JOptionPane.showMessageDialog(this,e.getMessage());
//        }
//    }
//    
    public void Simpan(){
        
        v_idPinjam = txtIdPeminjam.getText();
        v_namaNota = txtNota.getText();
    
        //mengganti format tanggal
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        date_kembali = dFormat.format(dateKembali.getDate());
        
        //convert String to integer
        km_akhir_aset = txtkmAkhir.getText();
        v_tagihan = txtTagihan.getText();
        tagihan = Integer.parseInt(v_tagihan);
        kmAkhir = Integer.parseInt(km_akhir_aset);
        
        v_IDUser = String.valueOf(Session.IDPengguna).toString();//mengambil value id_user dari tb_user
        
        if(v_idPinjam.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi ID Peminjaman !");
        }else if(km_akhir_aset.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi Kilometer Akhir !");
        }else{
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                
                    //pengecekan id peminjaman 
                    sqlSelect = "select * from tb_peminjaman where id_peminjaman ='"+v_idPinjam+"'";
                    Statement stat = _Cnn.createStatement(); 
                    ResultSet res = stat.executeQuery(sqlSelect);
          if(res.first()){
            if(v_tagihan.equals("0")){
                
                sqlInsert = "INSERT INTO tb_pengembalian SET id_peminjaman = '"+v_idPinjam+"',"
                      +"id_user = '"+v_IDUser+"',"
                      +"km_akhir ='"+km_akhir_aset+"',"
                      +"tanggal_pengembalian='"+date_kembali+"',"
                      +"tagihan ='"+tagihan+"',"
                      +"nota='"+null+"',"
                      +"nama_nota = '-'";
                Statement state = _Cnn.createStatement();
                state.executeUpdate(sqlInsert);
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            }else{    
                
             if(ImgPath != null){
                
                InputStream img = new FileInputStream(new File(ImgPath));
                sqlInsert = "INSERT INTO tb_pengembalian SET id_peminjaman = '"+v_idPinjam+"',"
                      +"id_user = '"+v_IDUser+"',"
                      +"km_akhir ='"+km_akhir_aset+"',"
                      +"tanggal_pengembalian='"+date_kembali+"',"
                      +"tagihan ='"+tagihan+"',"
                      +"nota='"+img+"',"
                      +"nama_nota = '"+v_namaNota+"'";
               Statement state = _Cnn.createStatement();
               state.executeUpdate(sqlInsert);
               JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                    
                }else{
                    JOptionPane.showMessageDialog(this, "Scan Nota Harus diUpload");
                }
            }
            
           
                    String keterangan = res.getString("keterangan");
                    plat_nomor = res.getString("plat_nomor");
                    
                    sqlSelect = "select * from tb_asset where plat_nomor='"+plat_nomor+"'";
                    Statement statem = _Cnn.createStatement(); 
                    ResultSet rest = statem.executeQuery(sqlSelect);
          if(rest.first()){
               //update table asset ketika pergi service
              if(keterangan.equals("Service")){
                sqlUpdate ="UPDATE tb_asset SET km_akhir ='"+kmAkhir+"', status_asset ='Siap Pakai',"
                        +"kondisi_asset='Layak Pakai' WHERE plat_nomor = '"+plat_nomor+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlUpdate);
              }else{
                  sqlUpdate ="UPDATE tb_asset SET km_akhir ='"+kmAkhir+"', status_asset ='Siap Pakai' WHERE plat_nomor = '"+plat_nomor+"'";
                Statement setm = _Cnn.createStatement();
                setm.execute(sqlUpdate);
              }
              
              //update table pengembalian untuk service ganti oli
                km_akhir = rest.getInt(km_akhir);
                km_awal = rest.getInt(km_awal);
                String jenis_aset = rest.getString("jenis_asset");
                
                if(jenis_aset.equals("Motor")){
                    km_hasil_motor = km_awal + 3000;
                    if(km_akhir >= km_hasil_motor){
                        sqlUpdate ="UPDATE tb_asset SET km_awal ='"+km_akhir+"'"
                                +"kondisi_asset ='Tidak Layak Pakai' WHERE plat_nomor = '"+plat_nomor+"'";
                        Statement set2 = _Cnn.createStatement();
                        set2.execute(sqlUpdate);
                    }
                }else{
                    km_hasil_mobil = km_awal+10000;
                    if(km_akhir >= km_hasil_mobil){
                        sqlUpdate ="UPDATE tb_asset SET km_awal ='"+km_akhir+"'"
                                +"kondisi_asset ='Tidak Layak Pakai' WHERE plat_nomor = '"+plat_nomor+"'";
                        Statement set2 = _Cnn.createStatement();
                        set2.execute(sqlUpdate);
                    }
                }
                Clear();
                Tampil();
          }
              
               Tanggal();
            }else{
              JOptionPane.showMessageDialog(null,"id peminjaman tidak terdaftar !");
          }
            }catch(Exception e){
//                JOptionPane.showMessageDialog(this, "DISINI ? "+e.getMessage());
            }
        }
        Tampil();
        Clear();
        Tanggal();
    }
    
    public void Delete(){
        v_idPinjam = txtIdPeminjam.getText();
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda ingin menghapus data ini? Kode "+v_idPinjam,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab == JOptionPane.YES_OPTION){
             try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqlDelete = "DELETE FROM tb_pengembalian WHERE id_peminjaman = '"+v_idPinjam+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlDelete);
                JOptionPane.showMessageDialog(this,"berhasil dihapus");
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(this, e);

            }
        } 
        Tampil();
        Clear();
        Tanggal();
    }
    
    public void Update(){
        v_idPinjam = txtIdPeminjam.getText();
        v_namaNota = txtNota.getText();
    
        //mengganti format tanggal
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        date_kembali = dFormat.format(dateKembali.getDate());
        
        //convert String to integer
        km_akhir_aset = txtkmAkhir.getText();
        v_tagihan = txtTagihan.getText();
        tagihan = Integer.parseInt(v_tagihan);
        kmAkhir = Integer.parseInt(km_akhir_aset);
        
        v_IDUser = String.valueOf(Session.IDPengguna);//mengambil value id_user dari tb_user
        
        if(v_idPinjam.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi ID Peminjaman !");
        }else if(km_akhir_aset.equals("")){
            JOptionPane.showMessageDialog(null,"Anda Harus Mengisi Kilometer Akhir !");
        }else{
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                if(ImgPath == null){
                       sqlUpdate ="UPDATE tb_pengembalian SET id_peminjaman = '"+v_idPinjam+"',"
                      +"id_user = '"+v_IDUser+"',"
                      +"km_akhir ='"+km_akhir_aset+"',"
                      +"tanggal_pengembalian='"+date_kembali+"',"
                      +"tagihan ='"+tagihan+"',"
                      +"nota='"+null+"',"
                      +"nama_nota = '-' WHERE id_peminjaman = '"+v_idPinjam+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlUpdate);
                JOptionPane.showMessageDialog(this,"berhasil diupdate");
                }else{
                    InputStream img = new FileInputStream(new File(ImgPath));
                       sqlUpdate ="UPDATE tb_pengembalian SET id_peminjaman = '"+v_idPinjam+"',"
                      +"id_user = '"+v_IDUser+"',"
                      +"km_akhir ='"+km_akhir_aset+"',"
                      +"tanggal_pengembalian='"+date_kembali+"',"
                      +"tagihan ='"+tagihan+"',"
                      +"nota='"+img+"',"
                      +"nama_nota = '"+v_namaNota+"' WHERE id_peminjaman = '"+v_idPinjam+"'";
                Statement set = _Cnn.createStatement();
                set.execute(sqlUpdate);
                JOptionPane.showMessageDialog(this,"berhasil diupdate");
                }
                Tanggal();
                Clear();
                Tampil();
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage());
                
            }
        }
    }
    
    public void Tampil(){
        tblpengembalian = new DefaultTableModel();

        tblpengembalian.addColumn("N0");
        tblpengembalian.addColumn("ID PEMINJAMAN");
        tblpengembalian.addColumn("KILOMETER AKHIR");
        tblpengembalian.addColumn("TANGGAL PENGEMBALIAN");
        tblpengembalian.addColumn("TAGIHAN");
        tblpengembalian.addColumn("NOTA");
        tblpengembalian.setRowCount(0);
       
        try {
            int num = 1;
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlSelect ="SELECT * FROM tb_pengembalian";
            Statement stat = _Cnn.createStatement();   
            ResultSet res = stat.executeQuery(sqlSelect);
            while(res.next()){
                tblpengembalian.addRow(new Object[]{
                num++,
                res.getString(2),
                res.getString(4),
                res.getString(5),
                res.getString(6),
                res.getString(8)
            });
            }tblKembali.setModel(tblpengembalian);
        } catch (Exception e) {
        
        }
    }
    
    public ImageIcon ResizeImage(String imagePath, byte[]pic){
        ImageIcon myImage = null;
        if(imagePath != null){
            myImage = new ImageIcon(imagePath);
        }else{
            myImage = new ImageIcon(pic);
        }
        
        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(txGambar.getWidth(), txGambar.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;
    }

    private void clearTable(){
        int row = tblpengembalian.getRowCount();
        for (int i = 0; i < row; i++) {
            tblpengembalian.removeRow(0);
        }
    }
    
    private void cari(){
        if(txtCari.getText().equals("")){
            Tampil();
        }else{
            try {
                    int num = 1;
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    clearTable();
                    sqlSelect ="SELECT * FROM tb_pengembalian WHERE id_peminjaman LIKE '%"+txtCari.getText()+"%'";
                    Statement stat = _Cnn.createStatement();   
                    ResultSet res = stat.executeQuery(sqlSelect);
                    while(res.next()){
                        v_IDUser = res.getString(num++);
                        v_idPinjam = res.getString("id_peminjaman");
                        km_akhir_aset = res.getString("km_akhir");
                        date_kembali = res.getString("tanggal_pengembalian");
                        v_tagihan = res.getString("tagihan");
                        v_nota = res.getString("nama_nota");
                        
                        Object[] data = {v_IDUser,v_idPinjam,km_akhir_aset,date_kembali,v_tagihan,v_nota};
                        tblpengembalian.addRow(data);
//                        
//                        JOptionPane.showMessageDialog(this, "hay");
//                        tblpengembalian.addRow(new Object[]{
//                        num++,
//                        res.getString(2),
//                        res.getString(4),
//                        res.getString(5),
//                        res.getString(6),
//                        res.getString(8)
//                        });
                    }
                } catch (Exception e) {
                        System.out.println(e.getMessage());
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
        jLabel17 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKembali = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtIdPeminjam = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtTagihan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        dateKembali = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtkmAkhir = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        btnNota = new javax.swing.JButton();
        txtNota = new javax.swing.JTextField();
        txGambar = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(681, 683));

        jLabel17.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Form Pengembalian Kendaraan");

        btnBack.setBackground(new java.awt.Color(255, 153, 0));
        btnBack.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pengembalian.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblKembali.setModel(new javax.swing.table.DefaultTableModel(
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
        tblKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKembaliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKembali);

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        btnCari.setBackground(new java.awt.Color(255, 153, 0));
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("ID Peminjaman");

        jLabel20.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Tagihan");

        txtTagihan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTagihanFocusLost(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Tanggal Pengembalian");

        jLabel19.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Kilometer Akhir");

        jLabel9.setFont(new java.awt.Font("Myriad Pro", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Kilometer akhir tempuh kendaraan");

        jLabel22.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Scan Kwitansi");

        btnNota.setBackground(new java.awt.Color(255, 153, 0));
        btnNota.setForeground(new java.awt.Color(255, 255, 255));
        btnNota.setText("Upload");
        btnNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotaActionPerformed(evt);
            }
        });

        txGambar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txGambar.setToolTipText("IMAGE");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel18)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtkmAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtIdPeminjam, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dateKembali, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22)
                    .addComponent(txtNota)
                    .addComponent(btnNota, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(txGambar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(dateKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtkmAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txGambar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNota, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDelete.setBackground(new java.awt.Color(255, 153, 51));
        btnDelete.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
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

        btnCancel.setBackground(new java.awt.Color(255, 153, 51));
        btnCancel.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(1189, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1445, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotaActionPerformed
        // TODO add your handling code here:
        AmbilGambar();
    }//GEN-LAST:event_btnNotaActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        Update();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        Simpan();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        Clear();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtTagihanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTagihanFocusLost
        // TODO add your handling code here:
        if(txtTagihan.getText().equals("0")){
            btnNota.setEnabled(false);
        }else{
            btnNota.setEnabled(true);
        }
    }//GEN-LAST:event_txtTagihanFocusLost

    private void tblKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKembaliMouseClicked
        // TODO add your handling code here:
        
        btnNota.setEnabled(true);
        int baris = tblKembali.rowAtPoint(evt.getPoint());
        v_idPinjam = tblKembali.getValueAt(baris,1).toString();
        txtIdPeminjam.setText(v_idPinjam);
        km_akhir_aset = tblKembali.getValueAt(baris,2).toString();
        txtkmAkhir.setText(km_akhir_aset);
        v_tagihan = tblKembali.getValueAt(baris,4).toString();
        txtTagihan.setText(v_tagihan);
        nama_gambar = tblKembali.getValueAt(baris,5).toString();
        txtNota.setText(nama_gambar);
        
        try {
            Date tanggal_peminjam = new SimpleDateFormat("yyyy-MM-dd").parse((String)tblKembali.getValueAt(baris,3).toString());
            dateKembali.setDate(tanggal_peminjam);
        } catch (ParseException ex) {
            Logger.getLogger(Peminjaman.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
        sqlSelect = "Select nota from tb_pengembalian where id_peminjaman='"+v_idPinjam+"'";
        Statement stat = _Cnn.createStatement(); 
        ResultSet res = stat.executeQuery(sqlSelect);
        byte[] image = null;
            if (res.next()) {
                image = res.getBytes("nota"); 
            }
            Image img = Toolkit.getDefaultToolkit().createImage(image);
            ImageIcon icon =new ImageIcon(img);
            txGambar.setIcon(icon);
            add(txGambar);

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_tblKembaliMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        Back();
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        cari();
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
            java.util.logging.Logger.getLogger(Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pengembalian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pengembalian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNota;
    private javax.swing.JButton btnSubmit;
    private com.toedter.calendar.JDateChooser dateKembali;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKembali;
    private javax.swing.JLabel txGambar;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdPeminjam;
    private javax.swing.JTextField txtNota;
    private javax.swing.JTextField txtTagihan;
    private javax.swing.JTextField txtkmAkhir;
    // End of variables declaration//GEN-END:variables
}
