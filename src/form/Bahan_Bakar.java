/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import Tools.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User1
 */
public class Bahan_Bakar extends javax.swing.JFrame {

    public Bahan_Bakar() {
        initComponents();
        clearInput();disableInput();
        setTabelBahanBakar();
        showDataBahanBakar();
    }
    
    Connection _Cnn;
    KoneksiDB getCnn = new KoneksiDB();
    public PreparedStatement stat;
    
    String vid, vnama, vharga;
    String sqlselect, sqlinsert, sqldelete;    
    DefaultTableModel tabelBahanBakar;

    
  
    private void clearInput(){
        txIdBahanBakar.setText("");
        txNamaBahanBakar.setText("");
    //        txHarga.setText("");
        txIdBahanBakar.requestFocus();
    }
    
    private void disableInput(){
        txIdBahanBakar.setEnabled(false);
        txNamaBahanBakar.setEnabled(false);
//        txHarga.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    private void enableInput(){
        txIdBahanBakar.setEnabled(true);
        txNamaBahanBakar.setEnabled(true);
//        txHarga.setEnabled(true);
        btnSimpan.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    
    private void setTabelBahanBakar(){
        String[] kolom1 = {"ID Bahan Bakar", "Nama Bahan Bakar"};
        tabelBahanBakar = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
            java.lang.String.class,
            java.lang.String.class
            };
            
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int row, int col){
                int cola = tabelBahanBakar.getColumnCount();
                return (col < cola) ? false : true;
            }
        };
        
        tblBahanBakar.setModel(tabelBahanBakar);
        tblBahanBakar.getColumnModel().getColumn(0).setPreferredWidth(75);
        tblBahanBakar.getColumnModel().getColumn(1).setPreferredWidth(75);
//        tblBahanBakar.getColumnModel().getColumn(2).setPreferredWidth(75);
    
    }
    
    private void clearTabel(){
        int row = tabelBahanBakar.getRowCount(); 
        for(int i=0; i<row; i++){
            tabelBahanBakar.removeRow(0); 
        }    
    }
    
    private void showDataBahanBakar(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection(); 
            sqlselect = "select * from tb_bahan_bakar order by id_bahan_bakar asc";
            Statement stat = _Cnn.createStatement(); 
            ResultSet res = stat.executeQuery(sqlselect); 
            clearTabel();
            while(res.next()){ 
                vid = res.getString("id_bahan_bakar"); 
                vnama = res.getString("nama_bahan_bakar");
//                vharga = res.getString("harga");
                
                Object[] data = {vid, vnama}; 
                tabelBahanBakar.addRow(data); 
            }
//            lblRecord.setText("Record : " + tblBahanBakar.getRowCount()); 
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error Method showDataKtg() : "+ex);
        }    
    }
    
    private void aksiSimpan(){
        if(txIdBahanBakar.getText().equals("")){
            JOptionPane.showMessageDialog(this, "ID Kategori harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(txNamaBahanBakar.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Nama Bahan Bakar Harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            vid = txIdBahanBakar.getText();
            vnama = txNamaBahanBakar.getText();
//            vharga = txHarga.getText();
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                if(btnSimpan.getText().equals("Simpan")){
                    sqlinsert = "INSERT INTO tb_bahan_bakar VALUES('"+vid+"','"+vnama+"')";
                    Statement stat = _Cnn.createStatement();
                    stat.executeUpdate(sqlinsert);
                    JOptionPane.showMessageDialog(this,"Data berhasil disimpan!","Informasi",JOptionPane.INFORMATION_MESSAGE);
                    btnHapus.setText("Hapus");
                }else if(btnSimpan.getText().equals("simpan")){
                    sqlinsert ="UPDATE tb_bahan_bakar SET nama_bahan_bakar='"+vnama+"' WHERE id_bahan_bakar='"+vid+"'";
                    btnSimpan.setText("Simpan");
                    Statement stat = _Cnn.createStatement();stat.executeUpdate(sqlinsert);
                    JOptionPane.showMessageDialog(this,"Data berhasil diubah!","Informasi",JOptionPane.INFORMATION_MESSAGE);
                }
                
                showDataBahanBakar();clearInput();disableInput();
                btnTambah.setText("Tambah");
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
            }
        }
    }
    
    private void aksiHapus(){
    int jawab = JOptionPane.showConfirmDialog(this, "Apakah Anda akan menghapus data ini ? Kode "+vid,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab == JOptionPane.YES_OPTION){
            try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqldelete = "delete from tb_bahan_bakar where id_bahan_bakar='"+vid+"' ";
            Statement stat = _Cnn.createStatement();
            stat.executeUpdate(sqldelete);
            JOptionPane.showMessageDialog(this, "Informasi",
                    "Data Berhasil Disimpan!", JOptionPane.INFORMATION_MESSAGE);
                      showDataBahanBakar();
                      clearInput();
                      disableInput();
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : "+ex);
            }
        }
    }
    
    private void cari(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabel();
            sqlselect = "select * from tb_bahan_bakar where "
                    + "nama_bahan_bakar LIKE'%"+txtCari.getText()+"%' "
                   + " order by id_bahan_bakar asc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while(res.next()){
                vid = res.getString("id_bahan_bakar"); 
                vnama = res.getString("nama_bahan_bakar");
//                vharga = res.getString("harga");
                
                Object[] data = {vid, vnama}; 
                tabelBahanBakar.addRow(data); 
            }
//            lblRecord.setText("Record : "+tblBahanBakar.getRowCount());
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error Method cari() : "+ex);
        }
    }
    
    private void back(){
        form.Asset asset = new form.Asset();
        asset.setVisible(true);
        this.dispose();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBahanBakar = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txIdBahanBakar = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txNamaBahanBakar = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bahanbak.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Form Bahan Bakar");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        tblBahanBakar.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBahanBakar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBahanBakarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBahanBakar);

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        jLabel12.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("ID Bahan Bakar");

        jLabel13.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText(" Bahan bakar");

        jLabel8.setFont(new java.awt.Font("Myriad Pro", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Pilih bahan bakar kendaraan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txNamaBahanBakar)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txIdBahanBakar, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                .addGap(85, 456, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txIdBahanBakar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txNamaBahanBakar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnHapus.setBackground(new java.awt.Color(255, 153, 51));
        btnHapus.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(255, 153, 51));
        btnTambah.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(255, 153, 51));
        btnSimpan.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        btnBack.setBackground(new java.awt.Color(255, 153, 0));
        btnBack.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(286, 286, 286)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if(btnHapus.getText().equals("Bersihkan")){
            clearInput();
        }else if(txIdBahanBakar.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else{
            aksiHapus();
        }
        btnSimpan.setText("Simpan");
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        if(btnTambah.getText().equals("Tambah")){
            clearInput();
            enableInput();
            txIdBahanBakar.requestFocus(true);
            btnTambah.setText("Batal");
            btnHapus.setText("Bersihkan");
        }else if(btnTambah.getText().equals("Batal")){
            clearInput();
            disableInput();
            btnTambah.setText("Tambah");
            btnSimpan.setText("Simpan");
            btnHapus.setText("Hapus");
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        aksiSimpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void tblBahanBakarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBahanBakarMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            int row = tblBahanBakar.getSelectedRow();
            vid = tblBahanBakar.getValueAt(row, 0).toString();
            vnama = tblBahanBakar.getValueAt(row, 1).toString();
//            vharga = tblBahanBakar.getValueAt(row, 2).toString();

            txIdBahanBakar.setText(vid); 
            txNamaBahanBakar.setText(vnama);
//            txHarga.setText(vharga);
            enableInput();
            btnTambah.setText("Batal");
            btnHapus.setEnabled(true);
            txIdBahanBakar.setEnabled(false);
            txIdBahanBakar.requestFocus(true);
            btnSimpan.setText("simpan");
            btnHapus.setText("Hapus");
        }
    }//GEN-LAST:event_tblBahanBakarMouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        back();
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(Bahan_Bakar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bahan_Bakar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bahan_Bakar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bahan_Bakar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bahan_Bakar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBahanBakar;
    private javax.swing.JTextField txIdBahanBakar;
    private javax.swing.JTextField txNamaBahanBakar;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
