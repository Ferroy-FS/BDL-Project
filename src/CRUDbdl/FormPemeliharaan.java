/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package CRUDbdl;

import Login.ConnectDatabaseLoginBDL;
import java.io.File;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author afrizal
 */
public class FormPemeliharaan extends javax.swing.JPanel {

    private boolean isNew = false;
    private boolean isUpdate = false;
    private DefaultTableModel modelTeknisi;
    private DefaultTableModel modelRiwayat;
    private DefaultTableModel modelParts;

    /**
     * Creates new form FormPemeliharaan
     */
    public FormPemeliharaan() {
        initComponents();

        initForm();
    }

    private void initForm() {
        modelTeknisi = new DefaultTableModel(new String[]{"ID Pegawai", "Nama Teknisi", "Peran"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes the cells un-editable
            }
        };
        jTable2.setModel(modelTeknisi);

        // 2. Setup Table Riwayat (Big table in History Tab)
        modelRiwayat = new DefaultTableModel(new String[]{"ID Maint", "Aset", "Tgl Mulai", "Tgl Selesai", "Biaya", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes the cells un-editable
            }
        };
        jTable1.setModel(modelRiwayat);

        // 3. Status Combo Box (Updates the Asset Status)
        cbxStatus.removeAllItems();
        cbxStatus.addItem("Sedang Diperbaiki");
        cbxStatus.addItem("Selesai (Tersedia)");
        cbxStatus.addItem("Rusak Berat");

        // 4. Default Date
        txtMulai.setText(java.time.LocalDate.now().toString());

        modelParts = new DefaultTableModel(new String[]{"ID Inv", "Nama Barang", "Qty"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblParts.setModel(modelParts);

        loadRiwayat();

    }

    private void loadRiwayat() {
        modelRiwayat.setRowCount(0);
        try {
            Connection conn = Login.ConnectDatabaseLoginBDL.getConnection();
            // Join with Aset to get Asset Name
            String sql = "SELECT p.id_pemeliharaan, a.nama_aset, p.tanggal_mulai, p.tanggal_selesai, p.total_biaya, a.status_aset "
             + "FROM pemeliharaan p "
             + "JOIN aset a ON p.kode_aset_unik = a.kode_aset_unik "
             + "ORDER BY p.tanggal_mulai DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelRiwayat.addRow(new Object[]{
                    rs.getString("id_pemeliharaan"),
                    rs.getString("nama_aset"),
                    rs.getString("tanggal_mulai"),
                    rs.getString("tanggal_selesai"),
                    rs.getDouble("total_biaya"),
                    rs.getString("status_aset") // Current status of the asset
                });
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Load Riwayat: " + e);
        }
    }

    private void loadDetailPemeliharaan(String idMaint) {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();

            // 1. GET MAIN DETAILS
            String sql = "SELECT * FROM pemeliharaan WHERE id_pemeliharaan = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idMaint);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Found it! Fill the fields
                txtIdMaintenance.setText(rs.getString("id_pemeliharaan"));
                txtIdAset.setText(rs.getString("kode_aset_unik"));
                txtMulai.setText(rs.getString("tanggal_mulai"));

                Date tglSelesai = rs.getDate("tanggal_selesai");
                txtSelesai.setText(tglSelesai == null ? "" : tglSelesai.toString());

                txtDeskripsi.setText(rs.getString("deskripsi_masalah"));
                txtBiaya.setText(String.valueOf(rs.getDouble("total_biaya")));

//                btnCariIdAset.doClick();
                // 2. GET TECHNICIANS (Sub-Table)
                modelTeknisi.setRowCount(0);
                String sqlTeknisi = "SELECT p.id_pegawai, p.nama_pegawai, pp.keterangan "
                 + "FROM pemeliharaan_pegawai pp "
                 + "JOIN pegawai p ON pp.id_pegawai = p.id_pegawai "
                 + "WHERE pp.id_pemeliharaan = ?";
                java.sql.PreparedStatement psTek = conn.prepareStatement(sqlTeknisi);
                psTek.setString(1, idMaint);
                java.sql.ResultSet rsTek = psTek.executeQuery();

                while (rsTek.next()) {
                    modelTeknisi.addRow(new Object[]{
                        rsTek.getString("id_pegawai"),
                        rsTek.getString("nama_pegawai"),
                        rsTek.getString("keterangan")
                    });
                }

                // 3. SET BUTTON STATES
                btnUpdate.setEnabled(true);
                btnBaru.setEnabled(true);
                btnSubmit.setEnabled(true);

                // Important: Set flags so Submit knows we are updating
                isNew = false;
                isUpdate = true;

                javax.swing.JOptionPane.showMessageDialog(this, "Data dimuat. Klik 'Update' jika ingin mengedit.");

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Data Maintenance ID '" + idMaint + "' tidak ditemukan!");
            }
            conn.close();

        } catch (Exception e) {
            System.out.println("Error Load Detail: " + e);
        }
    }

    private void saveTechnicians(Connection conn, String idMaint) throws SQLException {
        String sqlTeknisi = "INSERT INTO pemeliharaan_pegawai (id_pegawai, id_pemeliharaan, keterangan) VALUES (?, ?, ?)";
        PreparedStatement psTeknisi = conn.prepareStatement(sqlTeknisi);

        for (int i = 0; i < modelTeknisi.getRowCount(); i++) {
            String idPeg = modelTeknisi.getValueAt(i, 0).toString();
            String peran = modelTeknisi.getValueAt(i, 2).toString();

            psTeknisi.setInt(1, Integer.parseInt(idPeg));
            psTeknisi.setString(2, idMaint);
            psTeknisi.setString(3, peran);
            psTeknisi.addBatch();
        }
        psTeknisi.executeBatch();
    }

    private void saveParts(Connection conn, String idMaint) throws SQLException {
        // 1. Prepare Query for Linking Table
        String sqlInsert = "INSERT INTO suku_cadang_digunakan (id_inventaris, id_pemeliharaan, kuantitas) VALUES (?, ?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sqlInsert);

        // 2. Prepare Query for Stock Deduction
        String sqlUpdateStock = "UPDATE inventaris_stok SET stok_saat_ini = stok_saat_ini - ? WHERE id_inventaris = ?";
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock);

        // 3. Loop through the Parts Table
        for (int i = 0; i < modelParts.getRowCount(); i++) {
            String idInv = modelParts.getValueAt(i, 0).toString();
            int qty = Integer.parseInt(modelParts.getValueAt(i, 2).toString());

            // A. Insert into Usage Table
            psInsert.setString(1, idInv);
            psInsert.setString(2, idMaint);
            psInsert.setInt(3, qty);
            psInsert.addBatch();

            // B. Decrease Stock
            psUpdate.setInt(1, qty); // Subtract this amount
            psUpdate.setString(2, idInv);
            psUpdate.addBatch();
        }

        // Execute Batches
        psInsert.executeBatch();
        psUpdate.executeBatch();
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
        txtIdMaintenance = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTeknisi = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnTugaskanTeknisi = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtMulai = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSelesai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtIdAset = new javax.swing.JTextField();
        btnCariIdAset = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtNamaAset = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDeskripsi = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        txtBiaya = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbxStatus = new javax.swing.JComboBox<>();
        btnBaru = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        btnHapusTeknisi = new javax.swing.JButton();
        btnSearchIdMaintenance = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblParts = new javax.swing.JTable();
        txtPartName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnCariInventaris = new javax.swing.JButton();
        txtPartQty = new javax.swing.JTextField();
        btnHapusInventaris = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pemeliharaan");
        add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Input Data Pemeliharaan");

        jLabel3.setText("ID Maintenance");

        txtIdMaintenance.setEditable(false);
        txtIdMaintenance.setEnabled(false);

        jLabel4.setText("Teknisi");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        btnTugaskanTeknisi.setText("Tugaskan");
        btnTugaskanTeknisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTugaskanTeknisiActionPerformed(evt);
            }
        });

        jLabel5.setText("Tanggal Mulai");

        txtMulai.setEnabled(false);

        jLabel6.setText("Tanggal Selesai");

        jLabel7.setText("ID Aset");

        btnCariIdAset.setText("Cari");
        btnCariIdAset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariIdAsetActionPerformed(evt);
            }
        });

        jLabel8.setText("Nama Aset");

        txtNamaAset.setEditable(false);
        txtNamaAset.setEnabled(false);

        jLabel9.setText("Deskripsi");

        txtDeskripsi.setColumns(20);
        txtDeskripsi.setRows(5);
        jScrollPane3.setViewportView(txtDeskripsi);

        jLabel10.setText("Biaya");

        jLabel11.setText("Status");

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxStatusActionPerformed(evt);
            }
        });

        btnBaru.setText("Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnHapusTeknisi.setText("Hapus");
        btnHapusTeknisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusTeknisiActionPerformed(evt);
            }
        });

        btnSearchIdMaintenance.setText("Cari");
        btnSearchIdMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchIdMaintenanceActionPerformed(evt);
            }
        });

        tblParts.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblParts);

        jLabel13.setText("Inventaris");

        btnCariInventaris.setText("Cari");
        btnCariInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariInventarisActionPerformed(evt);
            }
        });

        btnHapusInventaris.setText("Hapus");
        btnHapusInventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusInventarisActionPerformed(evt);
            }
        });

        btnCetak.setText("Cetak Laporan");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(195, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCetak)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(btnBaru)
                            .addGap(18, 18, 18)
                            .addComponent(btnUpdate)
                            .addGap(18, 18, 18)
                            .addComponent(btnSubmit))
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(62, 62, 62))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addGap(52, 52, 52))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(67, 67, 67)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtIdAset, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnCariIdAset, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                                .addComponent(txtBiaya)
                                .addComponent(cbxStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSelesai)
                                .addComponent(txtNamaAset)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSearchIdMaintenance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(25, 25, 25))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(txtTeknisi, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                            .addComponent(txtPartName)
                                            .addComponent(txtPartQty))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnTugaskanTeknisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnHapusTeknisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnCariInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnHapusInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtMulai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(196, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchIdMaintenance))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTeknisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTugaskanTeknisi))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPartName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(btnCariInventaris))
                        .addGap(18, 18, 18)
                        .addComponent(txtPartQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHapusInventaris)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtIdAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCariIdAset))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtNamaAset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(txtBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBaru)
                            .addComponent(btnUpdate)
                            .addComponent(btnSubmit)))
                    .addComponent(btnHapusTeknisi))
                .addGap(32, 32, 32)
                .addComponent(btnCetak)
                .addGap(32, 32, 32))
        );

        jTabbedPane1.addTab("Input Data", jPanel1);

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab("Riwayat", jScrollPane1);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusTeknisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusTeknisiActionPerformed
        int row = jTable2.getSelectedRow();
        if (row != -1) {
            modelTeknisi.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih teknisi yang ingin dihapus dari tabel.");
        }
    }//GEN-LAST:event_btnHapusTeknisiActionPerformed

    private void btnCariIdAsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariIdAsetActionPerformed
        String idAset = txtIdAset.getText().trim();
        if (idAset.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Masukkan ID Aset!");
            return;
        }

        try {
            Connection conn = Login.ConnectDatabaseLoginBDL.getConnection();
            String sql = "SELECT nama_aset, status_aset FROM aset WHERE kode_aset_unik = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idAset);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nama = rs.getString("nama_aset");
                String status = rs.getString("status_aset");
                if (isNew && (status.equalsIgnoreCase("Perbaikan") || status.equalsIgnoreCase("Sedang Diperbaiki"))) {
                    JOptionPane.showMessageDialog(null,
                     "GAGAL: Aset '" + nama + "' sedang dalam perbaikan!\n"
                     + "Selesaikan maintenance sebelumnya terlebih dahulu.");

                    txtNamaAset.setText(""); // Clear the name so they can't submit
                    txtIdAset.setText("");
                    conn.close();
                    return;
                }

                txtNamaAset.setText(rs.getString("nama_aset"));

            } else {
                JOptionPane.showMessageDialog(null, "Aset tidak ditemukan!");
                txtNamaAset.setText("");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Cari Aset: " + e);
    }    }//GEN-LAST:event_btnCariIdAsetActionPerformed

    private void btnTugaskanTeknisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTugaskanTeknisiActionPerformed
        String keyword = txtTeknisi.getText().trim(); // User types Name or ID
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Masukkan Nama Teknisi!");
            return;
        }

        try {
            Connection conn = ConnectDatabaseLoginBDL.getConnection();

            String sql = "SELECT id_pegawai, nama_pegawai FROM pegawai WHERE nama_pegawai ILIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id_pegawai");
                String nama = rs.getString("nama_pegawai");

                // --- FIX START: CHECK AVAILABILITY ---
                // Check if this technician is in any maintenance where 'tanggal_selesai' is NULL
                String sqlCheck = "SELECT p.id_pemeliharaan FROM pemeliharaan p "
                 + "JOIN pemeliharaan_pegawai pp ON p.id_pemeliharaan = pp.id_pemeliharaan "
                 + "WHERE pp.id_pegawai = ? AND p.tanggal_selesai IS NULL";

                // If we are UPDATING, we must exclude the current maintenance ID from this check
                // (Otherwise the system thinks the tech is busy with the job we are currently editing)
                if (isUpdate) {
                    sqlCheck += " AND p.id_pemeliharaan != '" + txtIdMaintenance.getText() + "'";
                }

                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setInt(1, Integer.parseInt(id));
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    String busyId = rsCheck.getString("id_pemeliharaan");
                    JOptionPane.showMessageDialog(null,
                     "GAGAL: Teknisi '" + nama + "' sedang sibuk!\n"
                     + "Masih bertugas di Maintenance ID: " + busyId);
                    return; // STOP HERE
                }
                // -------------------------------------

                String peran = JOptionPane.showInputDialog(null, "Masukkan peran teknisi ini", "Teknisi Utama");
                if (peran == null) {
                    peran = "Teknisi";
                }

                // Check duplicate in the JTable
                for (int i = 0; i < modelTeknisi.getRowCount(); i++) {
                    if (modelTeknisi.getValueAt(i, 0).equals(id)) {
                        JOptionPane.showMessageDialog(this, "Teknisi ini sudah ada di list!");
                        return;
                    }
                }

                modelTeknisi.addRow(new Object[]{id, nama, peran});
                txtTeknisi.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Teknisi tidak ditemukan!");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Cari Teknisi: " + e);
        }
    }//GEN-LAST:event_btnTugaskanTeknisiActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        isNew = true;
        txtIdMaintenance.setText("(Auto)");
        txtIdAset.setText("");
        txtNamaAset.setText("");
        txtTeknisi.setText("");
        txtDeskripsi.setText("");
        txtBiaya.setText("0");
        txtSelesai.setText(""); // Empty means still ongoing

        txtPartName.setEnabled(false);
        txtPartQty.setEnabled(false);
        txtIdMaintenance.setEditable(false);
        txtIdMaintenance.setEnabled(false);

        modelTeknisi.setRowCount(0); // Clear technicians
        txtIdAset.requestFocus();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        if (txtNamaAset.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harap pilih Aset terlebih dahulu!");
            return;
        }

        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
            conn.setAutoCommit(false);

            String idAset = txtIdAset.getText();
            String tglMulai = txtMulai.getText();
            String tglSelesai = txtSelesai.getText();
            String deskripsi = txtDeskripsi.getText();
            String biayaStr = txtBiaya.getText();
            String statusPilihan = cbxStatus.getSelectedItem().toString();

            double biaya = 0;
            if (!biayaStr.isEmpty()) {
                biaya = Double.parseDouble(biayaStr);
            }

            Date sqlMulai = Date.valueOf(tglMulai);
            Date sqlSelesai = (tglSelesai.isEmpty()) ? null : Date.valueOf(tglSelesai);

            if (isNew) {
                String sqlMaint = "INSERT INTO pemeliharaan (kode_aset_unik, tanggal_mulai, tanggal_selesai, deskripsi_masalah, total_biaya) VALUES (?, ?, ?, ?, ?) RETURNING id_pemeliharaan";
                PreparedStatement psMaint = conn.prepareStatement(sqlMaint);
                psMaint.setString(1, idAset);
                psMaint.setDate(2, sqlMulai);
                psMaint.setDate(3, sqlSelesai);
                psMaint.setString(4, deskripsi);
                psMaint.setDouble(5, biaya);

                ResultSet rsMaint = psMaint.executeQuery();
                String idMaint = "";
                if (rsMaint.next()) {
                    idMaint = rsMaint.getString(1);
                }

                saveTechnicians(conn, idMaint);

//                saveParts(conn, idMaint);
                JOptionPane.showMessageDialog(null, "Data Pemeliharaan Tersimpan! ID: " + idMaint);
            } // --- CONDITION 2: UPDATE ---
            else if (isUpdate) {

                if (tglSelesai.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Tanggal Selesai Harus Diisi!");
                    return;
                }

                String idMaint = txtIdMaintenance.getText();

                // 1. Update Main Table
                String sqlUpd = "UPDATE pemeliharaan SET tanggal_mulai=?, tanggal_selesai=?, deskripsi_masalah=?, total_biaya=? WHERE id_pemeliharaan=?";
                PreparedStatement psUpd = conn.prepareStatement(sqlUpd);
                psUpd.setDate(1, sqlMulai);
                psUpd.setDate(2, sqlSelesai);
                psUpd.setString(3, deskripsi);
                psUpd.setDouble(4, biaya);
                psUpd.setString(5, idMaint);
                psUpd.executeUpdate();

                // 2. Reset Technicians (Delete Old -> Insert New)
                String sqlDelTek = "DELETE FROM pemeliharaan_pegawai WHERE id_pemeliharaan=?";
                PreparedStatement psDel = conn.prepareStatement(sqlDelTek);
                psDel.setString(1, idMaint);
                psDel.executeUpdate();

                // 3. Re-Insert Technicians from Table
                saveTechnicians(conn, idMaint);

                saveParts(conn, idMaint);

                JOptionPane.showMessageDialog(null, "Data Pemeliharaan Berhasil Diperbarui!");
            }

            // --- COMMON: UPDATE ASSET STATUS ---
            String statusDB = "Tersedia";
            if (statusPilihan.contains("Sedang")) {
                statusDB = "Perbaikan";
            } else if (statusPilihan.contains("Rusak")) {
                statusDB = "Rusak";
            } else {
                statusDB = "Tersedia";
            }

            String sqlUpd = "UPDATE aset SET status_aset = ? WHERE kode_aset_unik = ?";
            PreparedStatement psUpd = conn.prepareStatement(sqlUpd);
            psUpd.setString(1, statusDB);
            psUpd.setString(2, idAset);
            psUpd.executeUpdate();

            // COMMIT & CLEANUP
            conn.commit();
            loadRiwayat();
            btnBaru.doClick(); // Reset form

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            System.out.println("Error Submit Maintenance: " + e);
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void cbxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxStatusActionPerformed

    private void btnSearchIdMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchIdMaintenanceActionPerformed
        // TODO add your handling code here:
        String id = txtIdMaintenance.getText().trim();

        if (id.isEmpty() || id.equals("(Auto)")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Masukkan ID Maintenance yang valid!");
            return;
        }

        // Call the helper method we just created
        loadDetailPemeliharaan(id);
    }//GEN-LAST:event_btnSearchIdMaintenanceActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        isUpdate = true;
        txtIdMaintenance.setEnabled(true);
        txtIdMaintenance.setEditable(true);
        txtPartName.setEnabled(true);
        txtPartQty.setEnabled(true);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
//        int row = jTable1.getSelectedRow();
//        if (row != -1) {
//            String idMaint = modelRiwayat.getValueAt(row, 0).toString();
//
//            // Load the data
//            loadDetailPemeliharaan(idMaint);
//
//            // Automatically switch back to the "Input Data" tab
//            jTabbedPane1.setSelectedIndex(0);
//        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnCariInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariInventarisActionPerformed
        // TODO add your handling code here:

        String keyword = txtPartName.getText().trim(); // Input field for Part Name/ID
        String qtyStr = txtPartQty.getText().trim();   // Input field for Quantity

        if (keyword.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan Nama Barang dan Jumlah!");
            return;
        }

        try {
            int qtyUsed = Integer.parseInt(qtyStr);
            if (qtyUsed <= 0) {
                throw new NumberFormatException();
            }

            Connection conn = Login.ConnectDatabaseLoginBDL.getConnection();

            // 1. Check Stock in Database
            // We need ID, Name, and CURRENT STOCK
            String sql = "SELECT id_inventaris, nama_barang, stok_saat_ini FROM inventaris_stok WHERE nama_barang ILIKE ? OR id_inventaris = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, keyword);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String idInv = rs.getString("id_inventaris");
                String nama = rs.getString("nama_barang");
                int currentStock = rs.getInt("stok_saat_ini");

                // 2. VALIDATION: Do we have enough stock?
                if (currentStock < qtyUsed) {
                    JOptionPane.showMessageDialog(this,
                     "Stok tidak cukup!\nBarang: " + nama + "\nStok Tersedia: " + currentStock);
                    return;
                }

                // 3. Add to Table (UI Only)
                modelParts.addRow(new Object[]{idInv, nama, qtyUsed});

                // Clear inputs
                txtPartName.setText("");
                txtPartQty.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Barang inventaris tidak ditemukan!");
            }
            conn.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka valid > 0!");
        } catch (Exception e) {
            System.out.println("Error Add Part: " + e);
        }
    }//GEN-LAST:event_btnCariInventarisActionPerformed

    private void btnHapusInventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusInventarisActionPerformed
        // TODO add your handling code here:
        int row = tblParts.getSelectedRow();
        if (row != -1) {
            modelParts.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(null, "Pilih inventaris yang ingin dihapus dari tabel.");
        }
    }//GEN-LAST:event_btnHapusInventarisActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        javax.swing.JTextField txtMulai = new javax.swing.JTextField(10);
        javax.swing.JTextField txtSelesai = new javax.swing.JTextField(10);

        String currentYear = java.time.Year.now().toString();
        txtMulai.setText(currentYear + "-01-01");
        txtSelesai.setText(currentYear + "-12-31");

        Object[] message = {
            "Masukkan Periode Pemeliharaan:",
            "Dari Tanggal (YYYY-MM-DD):", txtMulai,
            "Sampai Tanggal (YYYY-MM-DD):", txtSelesai
        };

        int option = javax.swing.JOptionPane.showConfirmDialog(null, message, "Cetak Laporan Biaya", javax.swing.JOptionPane.OK_CANCEL_OPTION);

        if (option == javax.swing.JOptionPane.OK_OPTION) {
            try {
                // 2. Parse Dates (Capital Letters matter in Java -> Report Mapping!)
                Date tglMulai = Date.valueOf(txtMulai.getText());
                Date tglSelesai = Date.valueOf(txtSelesai.getText());

                Connection conn = ConnectDatabaseLoginBDL.getConnection();

                // 3. Load & Compile
                String reportPath = "src/LaporanPemeliharaan.jrxml";
                File reportFile = new File(reportPath);

                if (!reportFile.exists()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "File report tidak ditemukan!");
                    return;
                }

                JasperDesign design = net.sf.jasperreports.engine.xml.JRXmlLoader.load(reportFile);
                JasperReport report = net.sf.jasperreports.engine.JasperCompileManager.compileReport(design);

                // 4. Pass Parameters (Make sure these match your Jasper Parameter Names EXACTLY)
                java.util.HashMap<String, Object> parameters = new java.util.HashMap<>();
                parameters.put("StartDate", tglMulai);
                parameters.put("EndDate", tglSelesai);

                // 5. Print
                JasperPrint print
                 = JasperFillManager.fillReport(report, parameters, conn);

                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Error Cetak: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnCariIdAset;
    private javax.swing.JButton btnCariInventaris;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnHapusInventaris;
    private javax.swing.JButton btnHapusTeknisi;
    private javax.swing.JButton btnSearchIdMaintenance;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTugaskanTeknisi;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable tblParts;
    private javax.swing.JTextField txtBiaya;
    private javax.swing.JTextArea txtDeskripsi;
    private javax.swing.JTextField txtIdAset;
    private javax.swing.JTextField txtIdMaintenance;
    private javax.swing.JTextField txtMulai;
    private javax.swing.JTextField txtNamaAset;
    private javax.swing.JTextField txtPartName;
    private javax.swing.JTextField txtPartQty;
    private javax.swing.JTextField txtSelesai;
    private javax.swing.JTextField txtTeknisi;
    // End of variables declaration//GEN-END:variables
}
