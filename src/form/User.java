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
public class User extends javax.swing.JFrame {

    /**
     * Creates new form User
     */
    public User() {
        initComponents();
         clearInput(); disableInput();
        setTabelUser(); showDataUser();  
    }

    Connection _Cnn;
    KoneksiDB getCnn=new KoneksiDB();
    public String query;
    public PreparedStatement stat;
    public ResultSet res;
    public int ID_USER;
    
    String vid_user, vnama, vlev, vpass, vkonf, valamat, vnotelp, vuser;
    String sqlselect, sqlinsert, sqldelete, sqlUpdate;
    DefaultTableModel tbluser;
    
    public void clearInput(){
        txUserId.setText("");
        txNama.setText("");
        txTelepon.setText("");
        txAlamat.setText("");
        txUsername.setText("");
        txPassword.setText("");
        comboStatus.setSelectedItem(this);
    }
    
    public void disableInput(){
        txUserId.setEnabled(false);
        txNama.setEnabled(false);
        txTelepon.setEnabled(false);
        txAlamat.setEnabled(false);
        txUsername.setEnabled(false);
        txPassword.setEnabled(false);
        comboStatus.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    public void enableInput(){
        txUserId.setEnabled(true);
        txNama.setEnabled(true);
        txTelepon.setEnabled(true);
        txAlamat.setEnabled(true);
        txUsername.setEnabled(true);
        txPassword.setEnabled(true);
        comboStatus.setEnabled(true);
        btnSimpan.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    
    public void setTabelUser(){
        String[] kolom1 = {"ID. User", "Nama User", "No. Telepon", "Alamat", "Username", "Password", "Status"};
        tbluser = new DefaultTableModel(null, kolom1){
            Class[] types = new Class[]{
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
            //agar tidak bisa diedit
            public boolean isCellEditable(int row, int col){
                int cola= tbluser.getColumnCount();
                return(col < cola)?false : true;
            }
        };
        
        tableUser.setModel(tbluser);
        tableUser.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(6).setPreferredWidth(100);

    }
    
    public void clearTabelUser(){//mengkosongkan isi tabel
        int row = tbluser.getRowCount();//variable row diberi jumlah baris pada tabel(model)jurusan
        for(int i=0; i<row;i++){
            tbluser.removeRow(0);
        }
    }
    
    private void showDataUser(){
         try{
            _Cnn = null;
            _Cnn = getCnn.getConnection();          
            sqlselect = "select * from tb_user";     
            Statement stat = _Cnn.createStatement();    // membuat statement untuk menjalankan query
            ResultSet res = stat.executeQuery(sqlselect); //menjalankan query sqlselect ynag hasilnya ditampung pada variabel res
            clearTabelUser();
            while(res.next()){                  //perulangan while untuk menampilkan data hasil query select 
                vid_user = res.getString("id_user");      //memberikan nilai pada variabel vkd_prodi dimana nilainya adalah kolom kd_jur pada tabel jurusan
                vnama = res.getString("nama_user");
                vnotelp = res.getString("telepon");
                valamat = res.getString("alamat");
                vuser = res.getString("username");
                vpass = res.getString("password");
                vlev = res.getString("level");

                
                Object[] data = {vid_user, vnama, vnotelp,  valamat, vuser, vpass, vlev };    //memebuat object array untuk menampung data record 
                tbluser.addRow(data);      //menyisipkan baris yang nilainya sesuai array data
            }
//            lblRecord.setText("Record : " + tableUser.getRowCount()); //menampilkan jumlah baris
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this,"Data tidak ada / Kosong","Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
        }  
    
    }
    
    private void aksiSimpan(){
        
        if(txUserId.getText().equals("") || txNama.getText().equals("") || txTelepon.getText().equals("") || txAlamat.getText().equals("")|| txUsername.getText().equals("")|| txPassword.getText().equals("")){
            JOptionPane.showMessageDialog(this,  "Mohon lengkapi data!","informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        }else{
            vid_user = txUserId.getText();
            vnama = txNama.getText();
            vnotelp = txTelepon.getText();
            valamat = txAlamat.getText();
            vuser = txUsername.getText();
            vpass = txPassword.getText();
            vlev = (String) comboStatus.getSelectedItem();
            
//            try{
//                _Cnn = null;
//                _Cnn = getCnn.getConnection();
//                if(btnSimpan.getText().equals("Simpan")){
//                    sqlinsert = "INSERT INTO tb_user VALUES('"+vid_user+"','"+vnama+"','"+vnotelp+"','"+valamat+"','"+vuser+"','"+vpass+"','"+vlev+"')";
//                    Statement stat = _Cnn.createStatement();
//                stat.executeUpdate(sqlinsert);
//                JOptionPane.showMessageDialog(this,"Data berhasil disimpan", "Informasi",
//                         JOptionPane.INFORMATION_MESSAGE);
//                btnHapus.setText("Hapus");
//                }else if(btnSimpan.getText().equals("simpan")){
//                    sqlUpdate = "UPDATE tb_user SET nama_user='"+vnama+"', "
//                            + "telepon='"+vnotelp+"', "
//                            + "alamat='"+valamat+"', "
//                            + "username='"+vuser+"', "
//                            + "password='"+vpass+"', "
//                            + "level='"+vlev+"' "
//                            + "WHERE id_user='"+vid_user+"'";
//                    
//                    Statement stat = _Cnn.createStatement();
//                stat.executeUpdate(sqlUpdate);
//                JOptionPane.showMessageDialog(this,"Data berhasil diubah","Informasi",
//                        JOptionPane.INFORMATION_MESSAGE);
//                }
//                showDataUser();clearInput(); disableInput();
//                btnTambah.setText("Tambah");
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                if(btnSimpan.getText().equals("Simpan")){
                    sqlinsert = "INSERT INTO tb_user VALUES("+vid_user+",'"+vnama+"','"+vnotelp+"','"+valamat+"','"+vuser+"','"+vpass+"','"+vlev+"')";
                    Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqlinsert);
                JOptionPane.showMessageDialog(this,"Data berhasil disimpan", "Informasi",
                         JOptionPane.INFORMATION_MESSAGE);
                btnHapus.setText("Hapus");
//                btnHapus.setIcon(new javax.swing.ImageIcon(getClass().
//                    getResource("/Icons/trans-hapus.png")));
                }else if(btnSimpan.getText().equals("simpan")){
                    sqlUpdate = "UPDATE tb_user SET nama_user='"+vnama+"', "
                            + "telepon='"+vnotelp+"', "
                            + "alamat='"+valamat+"', "
                            + "username='"+vuser+"', "
                            + "password='"+vpass+"', "
                            + "level='"+vlev+"' "
                            + "WHERE id_user='"+vid_user+"'";
                    
                    Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqlUpdate);
                JOptionPane.showMessageDialog(this,"Data berhasil diubah","Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                showDataUser();clearInput(); disableInput();
                btnTambah.setText("Tambah");
                
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, ex);
                txUserId.requestFocus(true);
            }
        }
    }
    
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda ingin menghapus data ini? Kode "+vid_user,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(jawab == JOptionPane.YES_OPTION){
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "delete from tb_user where ID_USER='"+vid_user+"' ";    
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus", "Informasi",
                         JOptionPane.INFORMATION_MESSAGE);
                showDataUser(); clearInput(); disableInput();
                btnTambah.setText("Tambah");
            }catch(SQLException ex){
                 JOptionPane.showMessageDialog(this, "Data ini sudah di pakai");
            }
        }
    }
        //cari nama belum bener
        private void cariNama(){
            if(txtCari.getText().equals("")){
                 showDataUser();
            }else{
                try{
             _Cnn = null;
                _Cnn = getCnn.getConnection();
                clearTabelUser();
                sqlselect = "select * from tb_user where "
                    + "nama_user LIKE'%"+txtCari.getText()+"%' "
                   + " order by id_user asc";
                Statement stat = _Cnn.createStatement();
                ResultSet res = stat.executeQuery(sqlselect);
                while(res.next()){
                    vid_user = res.getString("id_user");      //memberikan nilai pada variabel vkd_prodi dimana nilainya adalah kolom kd_jur pada tabel jurusan
                    vnama = res.getString("nama_user");
                    vnotelp = res.getString("telepon");
                    valamat = res.getString("alamat");
                    vuser = res.getString("username");
                    vpass = res.getString("password");
                    vlev = res.getString("level");


                    Object[] data = {vid_user, vnama, vnotelp,  valamat, vuser, vpass, vlev };    //memebuat object array untuk menampung data record 
                    tbluser.addRow(data);
                    }
//                    lblRecord.setText("Record : "+tableUser.getRowCount());
              } catch(SQLException ex){
                  JOptionPane.showMessageDialog(this, "Error method cariNama() : "+ex);

              }
            }
            
        }
        
        private void back(){
            form.Menu menu = new form.Menu();
            menu.setVisible(true);
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
        jLabel10 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txUserId = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txNama = new javax.swing.JTextField();
        txAlamat = new javax.swing.JTextField();
        txUsername = new javax.swing.JTextField();
        txTelepon = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txPassword = new javax.swing.JPasswordField();
        comboStatus = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(681, 683));

        jLabel10.setFont(new java.awt.Font("Myriad Pro", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Form User");

        btnBack.setBackground(new java.awt.Color(255, 153, 0));
        btnBack.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/USER.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("User Id");

        jLabel6.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Nama");

        jLabel5.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Alamat");

        jLabel7.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("No.Telepon");

        jLabel8.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Username");

        jLabel11.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Kata Sandi");

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Operator" }));

        jLabel12.setFont(new java.awt.Font("Myriad Pro", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Level");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txNama)
                    .addComponent(txUserId)
                    .addComponent(txAlamat)
                    .addComponent(txUsername)
                    .addComponent(txTelepon)
                    .addComponent(txPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(comboStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNama, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id User", "Nama", "Alamat", "No.Telepon", "Username", "Kata Sandi", "Level"
            }
        ));
        tableUser.setGridColor(new java.awt.Color(204, 204, 204));
        tableUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableUser);

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        btnSimpan.setBorder(null);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(196, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
         if(btnHapus.getText().equals("Bersihkan")){
            clearInput();
        }else if(txUserId.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus","Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        }else{
            aksiHapus();
        }
        btnTambah.setText("Simpan");
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        if(btnTambah.getText().equals("Tambah")){
            clearInput();
            enableInput();
            txUserId.requestFocus(true);
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

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cariNama();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        cariNama();
    }//GEN-LAST:event_btnCariActionPerformed

    private void tableUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUserMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            int row = tableUser.getSelectedRow();
            vid_user = tableUser.getValueAt(row, 0).toString();
            vnama = tableUser.getValueAt(row, 1).toString();
            vnotelp = tableUser.getValueAt(row, 2).toString();
            valamat = tableUser.getValueAt(row, 3).toString();
            vuser = tableUser.getValueAt(row, 4).toString();
            vpass = tableUser.getValueAt(row, 5).toString();

            vlev = tableUser.getValueAt(tableUser.getSelectedRow(),6).toString();
            for (int i = 0; i <comboStatus.getItemCount(); i++) {
                if (vlev.equalsIgnoreCase((String) comboStatus.getItemAt(i))) {
                    comboStatus.setSelectedIndex(i);
                }
            }

            txUserId.setText(vid_user);
            txNama.setText(vnama);
            txTelepon.setText(vnotelp);
            txAlamat.setText(valamat);
            txUsername.setText(vuser);
            txPassword.setText(vpass);
            comboStatus.setSelectedItem(vlev);
            enableInput();
            btnSimpan.setText("Batal");
            btnHapus.setEnabled(true);
            txUserId.setEnabled(false);
            txNama.requestFocus(true);
            btnTambah.setText("simpan");
            btnHapus.setText("Hapus");
        }
    }//GEN-LAST:event_tableUserMouseClicked

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
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox comboStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableUser;
    private javax.swing.JTextField txAlamat;
    private javax.swing.JTextField txNama;
    private javax.swing.JPasswordField txPassword;
    private javax.swing.JTextField txTelepon;
    private javax.swing.JTextField txUserId;
    private javax.swing.JTextField txUsername;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
