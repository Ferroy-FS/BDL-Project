/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package CRUDbdl;

import Login.ConnectDatabaseLoginBDL;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author afrizal
 */
public class FormPembelian extends javax.swing.JPanel {

    DefaultTableModel modelTambahAset;
    HashMap<String, String> mapKategoriAset = new HashMap<>();

    DefaultTableModel modelTambahInventaris;
    HashMap<String, String> mapKategoriInventaris = new HashMap<>();

    /**
     * Creates new form FormPembelian
     */
    public FormPembelian() {
        initComponents();

        tableAset();
        loadKategoriAset();

        TableInventaris();
        loadKategoriInventaris();

        txtTglPembelianAset.setText(java.time.LocalDate.now().toString());
        txtTglPembelianInventaris.setText(java.time.LocalDate.now().toString());
    }

    private void tableAset() {
        String[] columns = {"Kategori", "Nama Aset", "Jumlah", "Harga Satuan", "Subtotal"};
        modelTambahAset = new DefaultTableModel(columns, 0) {
            @Override // Make table read-only
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblTambahAset.setModel(modelTambahAset);
    }

    private void loadKategoriAset() {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (Exception e) {
            return;
        }
        try {
            String sql = "SELECT id_kategori, nama_kategori FROM kategori";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbxKategoriAset.removeAllItems();
            mapKategoriAset.clear();

            while (rs.next()) {
                String id = rs.getString("id_kategori");
                String nama = rs.getString("nama_kategori");

                cbxKategoriAset.addItem(nama); // Show Name
                mapKategoriAset.put(nama, id); // Store ID
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Load Kategori: " + e);
        }
    }

    private String saveFileToUploads(String originalPath, String kategoriBarang) {
        try {
            File sourceFile = new File(originalPath);

            String projectRoot = projectRoot = System.getProperty("user.dir");

            File parentDir = new File(projectRoot + "/uploads");

            File uploadDir = null;

            if (!parentDir.exists()) {
                parentDir.mkdir();
            }

            if (kategoriBarang.equals("aset")) {
                uploadDir = new File(parentDir + "/aset");
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
            } else if (kategoriBarang.equals("inventaris")) {
                uploadDir = new File(parentDir + "/inventaris");
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
            }

            // 2. Generate a Unique Filename
            // We use current time to ensure no duplicate names (e.g., 17098822_nota.jpg)
            String fileName = sourceFile.getName();
            String extension = "";

            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i); // Get .jpg or .png
            }

            String newName = System.currentTimeMillis() + extension;

            // 3. Create the Target Path
            Path sourcePath = sourceFile.toPath();
            Path targetPath = Paths.get(uploadDir.getAbsolutePath(), newName);

            // 4. Perform the Copy
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 5. Return the Relative Path (This is what we save to DB)
            // We return "uploads/17098822.jpg"
            return "uploads/" + newName;

        } catch (Exception e) {
            System.out.println("Error Copying File: " + e);
            return null;
        }
    }

    private void TableInventaris() {
        String[] columns = {"Nama Inventaris", "Kategori", "Jumlah", "Harga Satuan", "Subtotal"};
        modelTambahInventaris = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblTambahInventaris.setModel(modelTambahInventaris);
    }

    private void loadKategoriInventaris() {
        try {
            java.sql.Connection conn = Login.ConnectDatabaseLoginBDL.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT id_kategori, nama_kategori FROM kategori");

            cbxKategoriInventaris.removeAllItems();
            mapKategoriInventaris.clear();

            while (rs.next()) {
                String id = rs.getString("id_kategori");
                String nama = rs.getString("nama_kategori");

                cbxKategoriInventaris.addItem(nama);
                mapKategoriInventaris.put(nama, id);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
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

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdPembelianAset = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtUploadFileAset = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTglPembelianAset = new javax.swing.JTextField();
        btnUploadFileAset = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbxKategoriAset = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtNamaAset = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtHargaAset = new javax.swing.JTextField();
        btnTambahAset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTambahAset = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtTotalHargaAset = new javax.swing.JTextField();
        btnSimpanAset = new javax.swing.JButton();
        spnrJumlahAset = new javax.swing.JSpinner();
        ((javax.swing.JSpinner.DefaultEditor)spnrJumlahAset.getEditor()).getTextField().setHorizontalAlignment(javax.swing.JTextField.LEFT);
        btnHapusAset = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtIdPembelianInventaris = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtUploadFileInventaris = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTglPembelianInventaris = new javax.swing.JTextField();
        btnUploadFileInventaris = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        cbxKategoriInventaris = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtNamaInventaris = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtHargaInventaris = new javax.swing.JTextField();
        btnTambahInventaris = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTambahInventaris = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        txtTotalHargaInventaris = new javax.swing.JTextField();
        btnSimpanInventaris = new javax.swing.JButton();
        spnrJumlahInventaris = new javax.swing.JSpinner();
        ((javax.swing.JSpinner.DefaultEditor)spnrJumlahInventaris.getEditor()).getTextField().setHorizontalAlignment(javax.swing.JTextField.LEFT);
        btnHapusInventaris = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pembelian");
        add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tambahkan Aset");

        jLabel3.setText("ID Pembelian");

        txtIdPembelianAset.setEditable(false);
        txtIdPembelianAset.setEnabled(false);

        jLabel4.setText("Upload File");

        txtUploadFileAset.setEditable(false);
        txtUploadFileAset.setEnabled(false);

        jLabel5.setText("Tanggal Pembelian");

        btnUploadFileAset.setText("Upload");
        btnUploadFileAset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadFileAsetActionPerformed(evt);
            }
        });

        jLabel6.setText("Kategori");

        cbxKategoriAset.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Nama Aset");

        jLabel8.setText("Jumlah");

        jLabel9.setText("Harga");

        btnTambahAset.setText("Tambah");
        btnTambahAset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahAsetActionPerformed(evt);
            }
        });

        tblTambahAset.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblTambahAset);

        jLabel10.setText("Total Harga");

        txtTotalHargaAset.setEditable(false);

        btnSimpanAset.setText("Simpan");
        btnSimpanAset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanAsetActionPerformed(evt);
            }
        });

        btnHapusAset.setText("Hapus");
        btnHapusAset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusAsetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambahAset)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusAset))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdPembelianAset, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtUploadFileAset, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(cbxKategoriAset, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNamaAset)
                                    .addComponent(spnrJumlahAset))))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUploadFileAset)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHargaAset)
                                    .addComponent(txtTglPembelianAset, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))))))
                .addContainerGap(70, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtTotalHargaAset, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpanAset))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdPembelianAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtTglPembelianAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUploadFileAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUploadFileAset))
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxKategoriAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHargaAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spnrJumlahAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahAset)
                    .addComponent(btnHapusAset))
                .addGap(48, 48, 48)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTotalHargaAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSimpanAset)
                .addContainerGap(278, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Aset", jPanel1);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Tambahkan Inventaris");

        jLabel12.setText("ID Pembelian");

        txtIdPembelianInventaris.setEditable(false);
        txtIdPembelianInventaris.setEnabled(false);

        jLabel13.setText("Upload File");

        txtUploadFileInventaris.setEditable(false);
        txtUploadFileInventaris.setEnabled(false);
        txtUploadFileInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUploadFileInventarisActionPerformed(evt);
            }
        });

        jLabel14.setText("Tanggal Pembelian");

        btnUploadFileInventaris.setText("Upload");
        btnUploadFileInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadFileInventarisActionPerformed(evt);
            }
        });

        jLabel15.setText("Kategori");

        cbxKategoriInventaris.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel16.setText("Nama Inventaris");

        jLabel17.setText("Jumlah");

        jLabel18.setText("Harga");

        btnTambahInventaris.setText("Tambah");
        btnTambahInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahInventarisActionPerformed(evt);
            }
        });

        tblTambahInventaris.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblTambahInventaris);

        jLabel19.setText("Total Harga");

        txtTotalHargaInventaris.setEditable(false);

        btnSimpanInventaris.setText("Simpan");
        btnSimpanInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanInventarisActionPerformed(evt);
            }
        });

        btnHapusInventaris.setText("Hapus");
        btnHapusInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusInventarisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(txtTotalHargaInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpanInventaris))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnTambahInventaris)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusInventaris))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUploadFileInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(txtIdPembelianInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(cbxKategoriInventaris, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtNamaInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(spnrJumlahInventaris))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUploadFileInventaris)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHargaInventaris)
                                    .addComponent(txtTglPembelianInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))))))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel11)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtIdPembelianInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtTglPembelianInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtUploadFileInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUploadFileInventaris))
                .addGap(58, 58, 58)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxKategoriInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHargaInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(spnrJumlahInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahInventaris)
                    .addComponent(btnHapusInventaris))
                .addGap(48, 48, 48)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTotalHargaInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSimpanInventaris)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inventaris", jPanel2);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahAsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahAsetActionPerformed
        String kategori = cbxKategoriAset.getSelectedItem().toString();
        String nama = txtNamaAset.getText();
        int jumlah = (Integer) spnrJumlahAset.getValue();
        String hargaStr = txtHargaAset.getText();

        if (nama.isEmpty() || jumlah <= 0 || hargaStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon lengkapi data aset!");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            double subtotal = jumlah * harga;

            modelTambahAset.addRow(new Object[]{
                kategori,
                nama,
                jumlah,
                harga,
                subtotal
            });
            updateTotalAset();

            txtNamaAset.setText("");
            spnrJumlahAset.setValue(0);
            txtHargaAset.setText("");
            txtNamaAset.requestFocus();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Jumlah dan Harga harus berupa angka!");
        }
    }//GEN-LAST:event_btnTambahAsetActionPerformed

    private void btnSimpanAsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanAsetActionPerformed
        if (tblTambahAset.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "Keranjang pembelian kosong!");
            return;
        }

        String originalFilePath = txtUploadFileAset.getText();
        if (originalFilePath.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Harap upload bukti pembelian terlebih dahulu!");
            return;
        }

        String finalPath = saveFileToUploads(originalFilePath, "aset");
        if (finalPath == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal mengupload file gambar!");
            return;
        }

        Connection conn = null;

        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
            conn.setAutoCommit(false);

            String sqlBukti = "insert into bukti_pembelian (filepath) values (?) returning id_bukti";
            PreparedStatement psBukti = conn.prepareStatement(sqlBukti);

            psBukti.setString(1, finalPath);

            ResultSet rsBukti = psBukti.executeQuery();

            String idBukti = "";

            if (rsBukti.next()) {
                idBukti = rsBukti.getString(1);
            }

            String sqlItem = "insert into pembelian_aset (id_bukti, id_kategori, nama_aset_dibeli, harga, tanggal_pembelian, jumlah) values (?, ?, ?, ?, ?, ?) RETURNING id_pembelian";
            PreparedStatement psItem = conn.prepareStatement(sqlItem);

            String sqlAset = "INSERT INTO aset (id_lokasi, id_kategori, id_pembelian, nama_aset, tanggal_perolehan, harga_perolehan, status_aset, nilai_buku) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psAset = conn.prepareStatement(sqlAset);

            String defaultLokasi = "LOK-004"; // <--- CHANGE THIS to your actual Gudang/Warehouse ID

            String tanggal = txtTglPembelianAset.getText();

            Date sqlDate = Date.valueOf(tanggal);

            for (int i = 0; i < tblTambahAset.getRowCount(); i++) {
                String namaKategori = tblTambahAset.getValueAt(i, 0).toString();
                String idKategori = mapKategoriAset.get(namaKategori); // Convert Name -> ID
                String namaAset = tblTambahAset.getValueAt(i, 1).toString();
                int jumlah = Integer.parseInt(tblTambahAset.getValueAt(i, 2).toString());
                double harga = Double.parseDouble(tblTambahAset.getValueAt(i, 3).toString());

                psItem.setString(1, idBukti);
                psItem.setString(2, idKategori);
                psItem.setString(3, namaAset);
                psItem.setDouble(4, harga);
                // Convert String Date to SQL Date
                psItem.setDate(5, sqlDate);
                psItem.setInt(6, jumlah);

                ResultSet rsItem = psItem.executeQuery();
                String idPembelian = "";
                if (rsItem.next()) {
                    idPembelian = rsItem.getString(1); // Get "PBA-xxx"
                }

                for (int k = 0; k < jumlah; k++) {
                    psAset.setString(1, defaultLokasi); // Default to Warehouse
                    psAset.setString(2, idKategori);
                    psAset.setString(3, idPembelian);   // Link to the specific purchase
                    psAset.setString(4, namaAset);      // "Macbook Pro"
                    psAset.setDate(5, sqlDate);
                    psAset.setDouble(6, harga);
                    psAset.setString(7, "Tersedia");    // Default Status
                    psAset.setDouble(8, harga);    // Default Status

                    psAset.addBatch(); // Queue this asset
                }
            }
            psAset.executeBatch();

            conn.commit();
            javax.swing.JOptionPane.showMessageDialog(null, "Transaksi Berhasil! No Bukti: " + idBukti);

            modelTambahAset.setRowCount(0);
            txtTotalHargaAset.setText("0");
            txtUploadFileAset.setText("");
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            System.out.println("Error: " + e);
            if (e.getMessage().contains("LOK-004")) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal: ID Lokasi 'LOK-004' tidak ditemukan di database. \nPastikan Anda sudah membuat lokasi Gudang.");
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal: " + e.getMessage());
            }

        }
    }//GEN-LAST:event_btnSimpanAsetActionPerformed

    private void btnUploadFileAsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadFileAsetActionPerformed
        JFileChooser chooser = new javax.swing.JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File f = chooser.getSelectedFile();
            txtUploadFileAset.setText(f.getAbsolutePath());
        }
    }//GEN-LAST:event_btnUploadFileAsetActionPerformed

    private void btnHapusAsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusAsetActionPerformed
        int selectedRow = tblTambahAset.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silahkan pilih item di tabel yang ingin dihapus!");
            return;
        }

        modelTambahAset.removeRow(selectedRow);

        updateTotalAset();
    }//GEN-LAST:event_btnHapusAsetActionPerformed

    private void btnTambahInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahInventarisActionPerformed

        String kategori = cbxKategoriInventaris.getSelectedItem().toString();
        String nama = txtNamaInventaris.getText();
        int jumlah = (Integer) spnrJumlahInventaris.getValue();
        String hargaStr = txtHargaInventaris.getText();

        String originalFilePath = txtUploadFileInventaris.getText();

        if (nama.isEmpty() || jumlah <= 0 || hargaStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Data tidak lengkap!");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            double subtotal = jumlah * harga;

            modelTambahInventaris.addRow(new Object[]{
                nama, kategori, jumlah, harga, subtotal
            });

            double total = 0;
            for (int i = 0; i < modelTambahInventaris.getRowCount(); i++) {
                total += (double) modelTambahInventaris.getValueAt(i, 4);
            }
            txtTotalHargaInventaris.setText(String.valueOf(total));

            txtNamaInventaris.setText("");
            spnrJumlahInventaris.setValue(0);
            txtHargaInventaris.setText("");

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Harga harus angka!");
        }
    }//GEN-LAST:event_btnTambahInventarisActionPerformed

    private void btnHapusInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusInventarisActionPerformed
        int selectedRow = tblTambahInventaris.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(null, "Silahkan pilih item di tabel yang ingin dihapus!");
            return;
        }

        modelTambahInventaris.removeRow(selectedRow);

        updateTotalInventaris();
    }//GEN-LAST:event_btnHapusInventarisActionPerformed

    private void btnUploadFileInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadFileInventarisActionPerformed
        JFileChooser chooser = new javax.swing.JFileChooser();
        int result = chooser.showOpenDialog(null);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File f = chooser.getSelectedFile();
            txtUploadFileInventaris.setText(f.getAbsolutePath());
        }
    }//GEN-LAST:event_btnUploadFileInventarisActionPerformed

    private void txtUploadFileInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUploadFileInventarisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUploadFileInventarisActionPerformed

    private void btnSimpanInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanInventarisActionPerformed
        if (tblTambahInventaris.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "Keranjang kosong!");
            return;
        }

        String originalFile = txtUploadFileInventaris.getText();
        if (originalFile.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Upload bukti dulu!");
            return;
        }

        String finalPath = saveFileToUploads(originalFile, "inventaris");
        if (finalPath == null) {
            return;
        }

        java.sql.Connection conn = null;

        String tanggal = txtTglPembelianAset.getText();

        Date sqlDate = Date.valueOf(tanggal);
        try {
            conn = Login.ConnectDatabaseLoginBDL.getConnection();
            conn.setAutoCommit(false); // Transaction Mode

            // 1. Insert Bukti
            String sqlBukti = "INSERT INTO bukti_pembelian (filepath) VALUES (?) RETURNING id_bukti";
            java.sql.PreparedStatement psBukti = conn.prepareStatement(sqlBukti);
            psBukti.setString(1, finalPath);
            java.sql.ResultSet rsBukti = psBukti.executeQuery();
            String idBukti = "";
            if (rsBukti.next()) {
                idBukti = rsBukti.getString(1);
            }

            String sqlMaster = "INSERT INTO inventaris_stok (id_kategori, nama_barang, stok_saat_ini, stok_minimum) VALUES (?, ?, ?, ?) RETURNING id_inventaris";
            java.sql.PreparedStatement psMaster = conn.prepareStatement(sqlMaster);

            String sqlLog = "INSERT INTO pembelian_inventaris (id_bukti, id_inventaris, tanggal_transaksi, tipe_transaksi, kuantitas, keterangan) VALUES (?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement psLog = conn.prepareStatement(sqlLog);

            for (int i = 0; i < tblTambahInventaris.getRowCount(); i++) {
                String namaKategori = tblTambahInventaris.getValueAt(i, 1).toString();
                String idKategori = mapKategoriInventaris.get(namaKategori);
                String namaBarang = tblTambahInventaris.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tblTambahInventaris.getValueAt(i, 2).toString());

                psMaster.setString(1, idKategori);
                psMaster.setString(2, namaBarang);
                psMaster.setInt(3, jumlah);
                psMaster.setInt(4, 100);

                System.out.println(idKategori + "  :  " + namaBarang + "  :  " + namaKategori);

                java.sql.ResultSet rsMaster = psMaster.executeQuery();
                String idInventaris = "";
                if (rsMaster.next()) {
                    idInventaris = rsMaster.getString(1); // Get "INV-001"
                }

                // 2. Insert Transaction Log linked to that Item
                psLog.setString(1, idBukti);
                psLog.setString(2, idInventaris); // Link to the item we just created
                psLog.setDate(3, sqlDate);
                psLog.setString(4, "MASUK");      // tipe_transaksi (In/Out)
                psLog.setInt(5, jumlah);          // kuantitas
                psLog.setString(6, "Pembelian Baru"); // keterangan

                psLog.addBatch();
            }

            psLog.executeBatch();

            conn.commit();
            javax.swing.JOptionPane.showMessageDialog(null, "Berhasil! Bukti: " + idBukti);

            modelTambahInventaris.setRowCount(0);
            txtTotalHargaInventaris.setText("0");
            txtUploadFileInventaris.setText("");

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            System.out.println("Error: " + e);
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal: " + e.getMessage());
    }    }//GEN-LAST:event_btnSimpanInventarisActionPerformed

    private void updateTotalAset() {
        double total = 0;
        for (int i = 0; i < modelTambahAset.getRowCount(); i++) {
            total += (double) modelTambahAset.getValueAt(i, 4); // Column 4 is Subtotal
        }
        txtTotalHargaAset.setText(String.valueOf(total));
    }

    private void updateTotalInventaris() {
        double total = 0;
        for (int i = 0; i < modelTambahInventaris.getRowCount(); i++) {
            total += (double) modelTambahInventaris.getValueAt(i, 4); // Column 4 is Subtotal
        }
        txtTotalHargaInventaris.setText(String.valueOf(total));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusAset;
    private javax.swing.JButton btnHapusInventaris;
    private javax.swing.JButton btnSimpanAset;
    private javax.swing.JButton btnSimpanInventaris;
    private javax.swing.JButton btnTambahAset;
    private javax.swing.JButton btnTambahInventaris;
    private javax.swing.JButton btnUploadFileAset;
    private javax.swing.JButton btnUploadFileInventaris;
    private javax.swing.JComboBox<String> cbxKategoriAset;
    private javax.swing.JComboBox<String> cbxKategoriInventaris;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner spnrJumlahAset;
    private javax.swing.JSpinner spnrJumlahInventaris;
    private javax.swing.JTable tblTambahAset;
    private javax.swing.JTable tblTambahInventaris;
    private javax.swing.JTextField txtHargaAset;
    private javax.swing.JTextField txtHargaInventaris;
    private javax.swing.JTextField txtIdPembelianAset;
    private javax.swing.JTextField txtIdPembelianInventaris;
    private javax.swing.JTextField txtNamaAset;
    private javax.swing.JTextField txtNamaInventaris;
    private javax.swing.JTextField txtTglPembelianAset;
    private javax.swing.JTextField txtTglPembelianInventaris;
    private javax.swing.JTextField txtTotalHargaAset;
    private javax.swing.JTextField txtTotalHargaInventaris;
    private javax.swing.JTextField txtUploadFileAset;
    private javax.swing.JTextField txtUploadFileInventaris;
    // End of variables declaration//GEN-END:variables
}
