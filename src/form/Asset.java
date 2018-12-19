/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import Tools.KoneksiDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author User1
 */
public class Asset extends javax.swing.JFrame {

    /**
     * Creates new form Asset
     */
    Connection _Cnn;
    KoneksiDB getCnn = new KoneksiDB();
    SimpleDateFormat tglinput = new SimpleDateFormat("yy-MM-dd");

    
    String vPlatNomor, vseries, vBahanBakar, vjenis, vkondisi, vkepemilikan, vstatus, vkmawal, vkmakhir;
    String sqlselect, sqlinsert, sqldelete;
    DefaultTableModel tabelAsset;
    String[] KeyBakar;
    
    public Asset() {
        initComponents();
        setTableAsset();
        showDataAsset();
        disableInput();
        listBahanBakar(); 
        clearInput();
    }
    
    private void formBahanBakar(){
        Bahan_Bakar bahan = new Bahan_Bakar();
        bahan.setVisible(true);
        this.dispose();
    }
    
    private void clearInput(){
        cmbJenis.setSelectedIndex(0);
        txNomorPlat.setText("");
        cmbBahanBakar.setSelectedIndex(0);
        txSeries.setText("");
        cmbKondisi.setSelectedIndex(0);
        cmbKepemilikan.setSelectedIndex(0);
        txKmAwal.setText("");
        cmbStatus.setSelectedIndex(0);
        btnTambah.setText("Tambah");
        btnSimpan.setText("Simpan");
        
    }
    
    private void disableInput(){
        cmbJenis.setEnabled(false);
        txNomorPlat.setEnabled(false);
        cmbBahanBakar.setEnabled(false);
        txSeries.setEnabled(false);
        cmbKondisi.setEnabled(false);
        cmbKepemilikan.setEnabled(false);
        txKmAwal.setEnabled(false);
        cmbStatus.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    private void enableInput(){
        cmbJenis.setEnabled(true);
        txNomorPlat.setEnabled(true);
        cmbBahanBakar.setEnabled(true);
        txSeries.setEnabled(true);
        cmbKondisi.setEnabled(true);
        cmbKepemilikan.setEnabled(true);
        txKmAwal.setEnabled(true);
        cmbStatus.setEnabled(true);
        btnSimpan.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    
    private void setTableAsset(){
        String[] kolom1 = {"Jenis Asset", "Nomor Plat", "Bahan Bakar", "Series", "kondisi", "Kepemilikan", "KM awal", "KM Akhir", "Status"};
        tabelAsset = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class
            };
            
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int row, int col){
                int cola = tabelAsset.getColumnCount();
                return (col < cola) ? false : true;
            }
        };
        tblAsset.setModel(tabelAsset);
        tblAsset.getColumnModel().getColumn(0).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(1).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(2).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(3).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(4).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(5).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(6).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(7).setPreferredWidth(75);
        tblAsset.getColumnModel().getColumn(8).setPreferredWidth(75);
    }
    
    private void clearTabelMotor(){
        int row = tabelAsset.getRowCount(); 
        for(int i=0; i<row; i++){
            tabelAsset.removeRow(0); 
        }    
    }
    
    private void showDataAsset(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection(); 
            sqlselect = "SELECT * FROM tb_asset a, tb_bahan_bakar bb WHERE a.id_bahan_bakar=bb.id_bahan_bakar ORDER BY a.plat_nomor ASC";
            Statement stat = _Cnn.createStatement(); 
            ResultSet res = stat.executeQuery(sqlselect); 
            clearTabelMotor();
            while(res.next()){ 
                vjenis = res.getString("jenis_asset");
                vPlatNomor = res.getString("plat_nomor");
                vBahanBakar = res.getString("nama_bahan_bakar");
                vseries = res.getString("nama_asset");
                vkondisi = res.getString("kondisi_asset");
                vkepemilikan = res.getString("kepemilikan_asset");
                vkmawal = res.getString("km_awal");
                vkmakhir = res.getString("km_akhir");
                vstatus = res.getString("status_asset");

                Object[] data = {vjenis, vPlatNomor, vBahanBakar, vseries, vkondisi, vkepemilikan, vkmawal, vkmakhir, vstatus}; 
                tabelAsset.addRow(data); 
            }
//            lblRecord.setText("Record : " + tblAsset.getRowCount()); 
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error Method showDataAsset() : "+ex);
        }    
    }
       
    private void listBahanBakar(){
        try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tb_bahan_bakar";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            cmbBahanBakar.removeAllItems();
            cmbBahanBakar.repaint();
            cmbBahanBakar.addItem("--- PILIH ---");
            int i = 1;
            while(res.next()){
                cmbBahanBakar.addItem(res.getString("nama_bahan_bakar"));
                i++;
                
            }
            res.first();
            KeyBakar = new String[i+1]; 
            for(Integer x=1; x<i; x++){
                KeyBakar[x]=res.getString(1);
                res.next();
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method listBahanBakar(); "+ex);
        }
    }
    
    private void aksiSimpan(){
        if(cmbJenis.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "Jenis kendaraan harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(txNomorPlat.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Nomor plat harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbBahanBakar.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "Jenis bahan bakar harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(txSeries.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Series kendaraan harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbKondisi.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "Kondisi kendaraan harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(cmbKepemilikan.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "kepemilikan kendaraan harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);    
         }else if(txKmAwal.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Kilometer Awal harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
         }else if(cmbStatus.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "status kendaraan harus diisi!",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);  
        }else{
            vjenis = (String) cmbJenis.getSelectedItem();
            vPlatNomor = txNomorPlat.getText();
            vBahanBakar = KeyBakar[cmbBahanBakar.getSelectedIndex()];
            vseries = txSeries.getText();
            vkondisi = (String) cmbKondisi.getSelectedItem();
            vkepemilikan = (String) cmbKepemilikan.getSelectedItem();
            vkmawal = txKmAwal.getText();
            vkmakhir = txKmAwal.getText();
            vstatus = cmbStatus.getSelectedItem().toString();
            
            
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                if(btnSimpan.getText().equals("Simpan")){
                    
                    sqlinsert ="INSERT INTO tb_asset VALUES('"+vPlatNomor+"',"
                            + "'"+vBahanBakar+"',"
                            + "'"+vjenis+"',"
                            + "'"+vseries+"',"
                            + "'"+vkondisi+"',"
                            + "'"+vkepemilikan+"',"
                            + "'"+vstatus+"',"
                            + "'"+vkmawal+"',"
                            + "'"+vkmakhir+"')";
                }else if(btnSimpan.getText().equals("simpan")){
                    sqlinsert ="UPDATE tb_asset SET id_bahan_bakar='"+vBahanBakar+"',"
                            + "jenis_asset='"+vjenis+"',"
                            + "nama_asset='"+vseries+"',"
                            + "kondisi_asset='"+vkondisi+"',"
                            + "kepemilikan_asset='"+vkepemilikan+"', "
                            + "status_asset='"+vstatus+"', "
                            + "km_awal='"+vkmawal+"', "
                            + "km_akhir='"+vkmakhir+"' "
                            + "WHERE plat_nomor='"+vPlatNomor+"'";
                }
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqlinsert);
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                showDataAsset();clearInput();disableInput();
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex);
            }
        }
    }
    
    private void aksiHapus(){
    int jawab = JOptionPane.showConfirmDialog(this, "Apakah Anda akan menghapus data ini ? dengan Plat Nomor "+vPlatNomor,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab == JOptionPane.YES_OPTION){
            try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqldelete = "delete from tb_asset where plat_nomor='"+vPlatNomor+"' ";
            Statement stat = _Cnn.createStatement();
            stat.executeUpdate(sqldelete);
            JOptionPane.showMessageDialog(this, "Informasi",
                    "Data Berhasil Disimpan!", JOptionPane.INFORMATION_MESSAGE);
                      showDataAsset();clearInput();disableInput();
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : "+ex);
            }
        }
    }
    
    private void clearTableAsset(){
        int row = tabelAsset.getRowCount();
        for (int i = 0; i < row; i++) {
            tabelAsset.removeRow(0);
        }
    }
    
    private void cari(){
         if(txtCari.getText().equals("")){
                 showDataAsset();
            }else{
                try{
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    clearTabelMotor();
                    sqlselect = "select * from tb_asset a, tb_bahan_bakar bb WHERE a.id_bahan_bakar=bb.id_bahan_bakar AND "
                            + "a.plat_nomor LIKE'%"+txtCari.getText()+"%' "
                           + " order by plat_nomor asc";
                    Statement stat = _Cnn.createStatement();
                    ResultSet res = stat.executeQuery(sqlselect);
                    while(res.next()){
                        vjenis = res.getString("jenis_asset");
                        vPlatNomor = res.getString("plat_nomor");
                        vBahanBakar = res.getString("nama_bahan_bakar");
                        vseries = res.getString("nama_asset");
                        vkondisi = res.getString("kondisi_asset");
                        vkepemilikan = res.getString("kepemilikan_asset");
                        vkmawal = res.getString("km_awal");
                        vkmakhir = res.getString("km_akhir");
                        vstatus = res.getString("status_asset");

                        Object[] data = {vjenis, vPlatNomor, vBahanBakar, vseries, vkondisi, vkepemilikan, vkmawal, vkmakhir, vstatus}; 
                        tabelAsset.addRow(data); 
                    }
        //             lblRecord.setText("Record : " + tblAsset.getRowCount());
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error Method cari() : "+ex);
                }
         }    
    }
    
    private void back(){
        form.Menu menu = new form.Menu();
        menu.setVisible(true);
        this.dispose();
    }
    
//    private void cetakLaporan(){
//        String pth = System.getProperty("user.dir") + "/laporan/rpAsset.jrxml";
//        String logo = System.getProperty("user.dir") + "/laporan/";
//        try {
//            Map<String, Object> parameters = new HashMap<>();
//            _Cnn = null;
//            _Cnn = getCnn.getConnection();
//
//            parameters.put("parLogo", logo);
//
//            JasperReport jrpt = JasperCompileManager.compileReport(pth);
//                                                        JOptionPane.showMessageDialog(this, "sampai sini");
//
////           JasperPrint jprint = JasperFillManager.fillReport(jrpt, parameters, _Cnn);
////           
//
////            JasperViewer.viewReport(jprint, false);
//            
//        } catch (SQLException | JRException ex) {
//            JOptionPane.showMessageDialog(this, "Error method cetakLaporan1() : "
//            + ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbJenis = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        txNomorPlat = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cmbBahanBakar = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbKepemilikan = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cmbKondisi = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        txSeries = new javax.swing.JTextField();
        txKmAwal = new javax.swing.JTextField();
        btnBahanBakar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCari = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAsset = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204)));
        jPanel1.setPreferredSize(new java.awt.Dimension(681, 683));

        jLabel9.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Form Asset Kendaraan");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ASSETQ.png"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Jenis Kendaraan");

        cmbJenis.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        cmbJenis.setForeground(new java.awt.Color(102, 102, 102));
        cmbJenis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--- PILIH ---", "Mobil", "Motor" }));

        jLabel11.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Plat Nomor");

        txNomorPlat.setBackground(new java.awt.Color(230, 230, 237));

        jLabel14.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Bahan Bakar");

        cmbBahanBakar.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        cmbBahanBakar.setForeground(new java.awt.Color(102, 102, 102));
        cmbBahanBakar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pertamax", "Pertalite", "Solar", " " }));

        jLabel12.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Series");

        jLabel7.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Kepimilikan Asset");

        cmbKepemilikan.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        cmbKepemilikan.setForeground(new java.awt.Color(102, 102, 102));
        cmbKepemilikan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--- PILIH ---", "Milik Perusahaan", "Pinjaman" }));

        jLabel13.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Kondisi Asset");

        cmbKondisi.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        cmbKondisi.setForeground(new java.awt.Color(102, 102, 102));
        cmbKondisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--- PILIH ---", "Layak Pakai", "Perlu Service", "Rusak" }));

        jLabel16.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Kilometer Awal");

        jLabel17.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Status");

        cmbStatus.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        cmbStatus.setForeground(new java.awt.Color(102, 102, 102));
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--- PILIH ---", "Siap Pakai", "Terpinjam" }));

        txSeries.setBackground(new java.awt.Color(230, 230, 237));

        txKmAwal.setBackground(new java.awt.Color(230, 230, 237));

        btnBahanBakar.setBackground(new java.awt.Color(255, 153, 51));
        btnBahanBakar.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnBahanBakar.setForeground(new java.awt.Color(255, 255, 255));
        btnBahanBakar.setText("+");
        btnBahanBakar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBahanBakarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbJenis, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txSeries, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(txNomorPlat)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cmbBahanBakar, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(btnBahanBakar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbKondisi, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKepemilikan, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txKmAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKepemilikan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNomorPlat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKondisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbBahanBakar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txKmAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBahanBakar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txSeries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnCari.setBackground(new java.awt.Color(255, 153, 51));
        btnCari.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        tblAsset.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAsset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAssetMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAsset);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(16, 16, 16))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(327, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(175, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1337, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        if(btnTambah.getText().equals("Tambah")){
            clearInput();
            enableInput();
            btnHapus.setEnabled(true);
            btnTambah.setText("Batal");btnHapus.setText("Bersihkan");
        }else if(btnTambah.getText().equals("Batal")){
            clearInput();
            disableInput();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        aksiSimpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if(txNomorPlat.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Informasi",
                "Anda belum memilih data yang akan dihapus", JOptionPane.INFORMATION_MESSAGE);
        }else if(btnHapus.getText().equals("Bersihkan")){
            clearInput();
        }else{
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_txtCariActionPerformed

    private void tblAssetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAssetMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            int row = tblAsset.getSelectedRow();
           
            vjenis = tblAsset.getValueAt(row, 0).toString();
            vPlatNomor = tblAsset.getValueAt(row, 1).toString();
            vBahanBakar = tblAsset.getValueAt(row, 2).toString();
            vseries = tblAsset.getValueAt(row, 3).toString();
            vkondisi = tblAsset.getValueAt(row, 4).toString();
            vkepemilikan = tblAsset.getValueAt(row, 5).toString();
            vkmawal = tblAsset.getValueAt(row, 6).toString();
            vkmakhir = tblAsset.getValueAt(row, 7).toString();
            vstatus = tblAsset.getValueAt(row, 8).toString();

            cmbJenis.setSelectedItem(vjenis);
            txNomorPlat.setText(vPlatNomor);
            cmbBahanBakar.setSelectedItem(vBahanBakar);
            txSeries.setText(vseries);
            cmbKondisi.setSelectedItem(vkondisi);
            cmbKepemilikan.setSelectedItem(vkepemilikan);
            txKmAwal.setText(vkmawal);
//            tx.setText(vkmakhir);
            cmbStatus.setSelectedItem(vstatus);
            
            enableInput();

            txNomorPlat.setEnabled(true);
            btnHapus.setEnabled(true);
            btnHapus.setText("Hapus");
            btnSimpan.setText("simpan");
        }
    }//GEN-LAST:event_tblAssetMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        back();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnBahanBakarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBahanBakarActionPerformed
        // TODO add your handling code here:
        formBahanBakar();
    }//GEN-LAST:event_btnBahanBakarActionPerformed

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
            java.util.logging.Logger.getLogger(Asset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Asset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Asset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asset().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBahanBakar;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox cmbBahanBakar;
    private javax.swing.JComboBox cmbJenis;
    private javax.swing.JComboBox cmbKepemilikan;
    private javax.swing.JComboBox cmbKondisi;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAsset;
    private javax.swing.JTextField txKmAwal;
    private javax.swing.JTextField txNomorPlat;
    private javax.swing.JTextField txSeries;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
