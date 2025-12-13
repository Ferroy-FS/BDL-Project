/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package CRUDbdl;

import Login.ConnectDatabaseLoginBDL;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author afrizal
 */
public class FormDataMaster extends javax.swing.JPanel {

    private boolean isNewPegawai = false;
    private boolean isUpdatePegawai = false;
    DefaultTableModel modelPegawai;

    private boolean isNewLokasi = false;
    private boolean isUpdateLokasi = false;
    DefaultTableModel modelLokasi;

    private boolean isNewKategori = false;
    private boolean isUpdateKategori = false;
    DefaultTableModel modelKategori;

    /**
     * Creates new form FormDataMaster
     */
    public FormDataMaster() {
        initComponents();

        tablePegawai();
        loadDataPegawai();
        listDepartemen();

        tableLokasi();
        loadDataLokasi();

        tableKategori();
        loadDataKategori();
    }

    private void tablePegawai() {
        String[] columnPegawai = {"ID Pegawai", "Nama", "Username", "Departemen", "Kontak"};
        modelPegawai = new DefaultTableModel(columnPegawai, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelPegawai.setRowCount(0);
        tblPegawai.setModel(modelPegawai);
    }

    private void loadDataPegawai() {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (conn == null) {
            return;
        }
        modelPegawai.setRowCount(0);
        String sql = "select * from pegawai order by id_pegawai asc";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                String id = rs.getString("id_pegawai");
                String nama = rs.getString("nama_pegawai");
                String username = rs.getString("username");
                String dept = rs.getString("departemen");
                String kontak = rs.getString("kontak");

                modelPegawai.addRow(new Object[]{id, nama, username, dept, kontak});
            }

            ConnectDatabaseLoginBDL.closeConnection(conn, ps, rs);

        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void listDepartemen() {
        cbxDepartemen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Software Engineering", // Developers (High-spec Laptops)
            "Infrastructure & Network", // SysAdmins (Servers, Switches, Routers)
            "IT Support", // Internal IT (Spare parts, peripherals)
            "Quality Assurance (QA)", // Testers (Multiple devices, test phones)
            "UI/UX Design", // Designers (MacBooks, Tablets)
            "DevOps", // Cloud Ops (Security keys, servers)
            "Product Management", // PMs (Standard Laptops)
            "HR", // Admin (Office assets)
            "Finance & Legal" // Admin (Printers, Scanners)
        }));
    }

    private void resetFormState() {
        txtIdPegawai.setText("");
        txtNamaLengkap.setText("");
        txtKontak.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
//        txtJawaban.setText("");
        cbxDepartemen.setSelectedIndex(0);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);

        isNewPegawai = false;
        isUpdatePegawai = false;

        btnBaruPegawai.setEnabled(true);
        btnUpdatePegawai.setEnabled(true);
        btnHapusPegawai.setEnabled(true);
        btnResetKaryawan.setEnabled(true);
        btnSubmitPegawai.setEnabled(false);
    }

    private void searchDataPegawai(String keyword) {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        modelPegawai.setRowCount(0);

        try {
            String sql = "select * from pegawai where nama_pegawai ilike ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id_pegawai");
                String nama = rs.getString("nama_pegawai");
                String username = rs.getString("username");
                String departemen = rs.getString("departemen");
                String kontak = rs.getString("kontak");;
                modelPegawai.addRow(new Object[]{id, nama, username, departemen, kontak});
            }

            ConnectDatabaseLoginBDL.getConnection();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void tableLokasi() {
        String[] columnLokasi = {"ID Lokasi", "Nama Ruangan", "Gedung"};
        modelLokasi = new DefaultTableModel(columnLokasi, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLokasi.setModel(modelLokasi);
    }

    private void loadDataLokasi() {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (conn == null) {
            return;
        }

        modelLokasi.setRowCount(0);

        String sql = "select * from lokasi order by id_lokasi asc";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id_lokasi");
                String nama = rs.getString("nama_lokasi");
                String gedung = rs.getString("gedung");

                modelLokasi.addRow(new Object[]{id, nama, gedung});
            }
            ConnectDatabaseLoginBDL.closeConnection(conn, ps, rs);
        } catch (SQLException ex) {
            System.out.println("Error Load Lokasi: " + ex);
        }
    }

    private void resetFormLokasi() {
        txtIdLokasi.setText("");
        txtNamaRuangan.setText("");
        txtGedung.setText("");

        isNewLokasi = false;
        isUpdateLokasi = false;

        btnBaruLokasi.setEnabled(true);
        btnUpdateLokasi.setEnabled(true);
        btnHapusLokasi.setEnabled(true);
        btnResetLokasi.setEnabled(true);
        btnSubmitLokasi.setEnabled(false);
    }

    private void searchDataLokasi(String keyword) {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        modelLokasi.setRowCount(0);

        try {
            String sql = "select * from lokasi where nama_lokasi ilike ? or gedung ilike ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id_lokasi");
                String nama = rs.getString("nama_lokasi");
                String gedung = rs.getString("gedung");

                modelLokasi.addRow(new Object[]{id, nama, gedung});
            }

            ConnectDatabaseLoginBDL.getConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void tableKategori() {
        String[] columns = {"ID Kategori", "Nama Kategori", "Umur Ekonomis (Thn)"};
        modelKategori = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblKategori.setModel(modelKategori);
    }

    private void searchDataKategori(String keyword) {
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        modelKategori.setRowCount(0);

        try {
            String sql = "select * from kategori where nama_kategori ilike ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id_kategori");
                String nama = rs.getString("nama_kategori");
                String umurEkonomis = rs.getString("umur_ekonomis_tahun");
                modelKategori.addRow(new Object[]{id, nama, umurEkonomis});
            }

            ConnectDatabaseLoginBDL.getConnection();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void loadDataKategori() {
        Connection conn = null;

        modelKategori.setRowCount(0);
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
            String sql = "select * from kategori order by id_kategori asc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id_kategori");
                String nama = rs.getString("nama_kategori");
                int umur = rs.getInt("umur_ekonomis_tahun");

                modelKategori.addRow(new Object[]{id, nama, umur});
            }
            ConnectDatabaseLoginBDL.closeConnection(conn, ps, rs);
        } catch (Exception e) {
            System.out.println("Error Load Kategori: " + e);
        }
    }

    private void resetFormKategori() {
        txtIdKategori.setText("");
        txtNamaKategori.setText("");
        spnUmurEkonomis.setValue(0);

        isNewKategori = false;
        isUpdateKategori = false;

        btnBaruKategori.setEnabled(true);
        btnUpdateKategori.setEnabled(true);
        btnHapusKategori.setEnabled(true);
        btnResetKategori.setEnabled(true);
        btnSubmitLokasi.setEnabled(false);
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
        txtIdPegawai = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNamaLengkap = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbxDepartemen = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtKontak = new javax.swing.JTextField();
        btnBaruPegawai = new javax.swing.JButton();
        btnUpdatePegawai = new javax.swing.JButton();
        btnHapusPegawai = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCariPegawai = new javax.swing.JTextField();
        btnCariPegawai = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPegawai = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnSubmitPegawai = new javax.swing.JButton();
        btnResetKaryawan = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtIdLokasi = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtNamaRuangan = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtGedung = new javax.swing.JTextField();
        btnBaruLokasi = new javax.swing.JButton();
        btnUpdateLokasi = new javax.swing.JButton();
        btnHapusLokasi = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        btnCariLokasi = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblLokasi = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        btnResetLokasi = new javax.swing.JButton();
        btnSubmitLokasi = new javax.swing.JButton();
        txtCariLokasi = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtIdKategori = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtNamaKategori = new javax.swing.JTextField();
        btnBaruKategori = new javax.swing.JButton();
        btnUpdateKategori = new javax.swing.JButton();
        btnHapusKategori = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtCariKategori = new javax.swing.JTextField();
        btnCariKategori = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblKategori = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        spnUmurEkonomis = new javax.swing.JSpinner();
        ((javax.swing.JSpinner.DefaultEditor)spnUmurEkonomis.getEditor()).getTextField().setHorizontalAlignment(javax.swing.JTextField.LEFT);
        btnResetKategori = new javax.swing.JButton();
        btnSubmitKategori = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Data Master");
        jLabel1.setToolTipText("");
        add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tambah Pegawai");

        jLabel3.setText("ID Pegawai");

        txtIdPegawai.setEditable(false);
        txtIdPegawai.setEnabled(false);
        txtIdPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPegawaiActionPerformed(evt);
            }
        });

        jLabel4.setText("Nama Lengkap");

        jLabel5.setText("Departemen");

        cbxDepartemen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxDepartemen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDepartemenActionPerformed(evt);
            }
        });

        jLabel6.setText("Kontak");

        btnBaruPegawai.setText("Baru");
        btnBaruPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruPegawaiActionPerformed(evt);
            }
        });

        btnUpdatePegawai.setText("Update");
        btnUpdatePegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePegawaiActionPerformed(evt);
            }
        });

        btnHapusPegawai.setText("Hapus");
        btnHapusPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPegawaiActionPerformed(evt);
            }
        });

        jLabel7.setText("Cari");

        txtCariPegawai.setForeground(new java.awt.Color(153, 153, 153));
        txtCariPegawai.setText("Cari berdasarkan Nama...");
        txtCariPegawai.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCariPegawaiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCariPegawaiFocusLost(evt);
            }
        });
        txtCariPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariPegawaiActionPerformed(evt);
            }
        });

        btnCariPegawai.setText("Cari");
        btnCariPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPegawaiActionPerformed(evt);
            }
        });

        tblPegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPegawai.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPegawai.setShowGrid(false);
        jScrollPane1.setViewportView(tblPegawai);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("list Pegawai");

        jLabel8.setText("Username");

        jLabel11.setText("Password");

        btnSubmitPegawai.setText("Submit");
        btnSubmitPegawai.setEnabled(false);
        btnSubmitPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitPegawaiActionPerformed(evt);
            }
        });

        btnResetKaryawan.setText("Reset");
        btnResetKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnResetKaryawan)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnBaruPegawai)
                            .addGap(18, 18, 18)
                            .addComponent(btnUpdatePegawai)
                            .addGap(18, 18, 18)
                            .addComponent(btnHapusPegawai)
                            .addGap(18, 18, 18)
                            .addComponent(btnSubmitPegawai))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(txtCariPegawai, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnCariPegawai))
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel8)
                                .addComponent(jLabel11))
                            .addGap(33, 33, 33)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPassword)
                                .addComponent(txtIdPegawai)
                                .addComponent(txtNamaLengkap)
                                .addComponent(cbxDepartemen, 0, 300, Short.MAX_VALUE)
                                .addComponent(txtKontak)
                                .addComponent(txtUsername)))))
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
                    .addComponent(txtIdPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbxDepartemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBaruPegawai)
                    .addComponent(btnUpdatePegawai)
                    .addComponent(btnHapusPegawai)
                    .addComponent(btnSubmitPegawai))
                .addGap(18, 18, 18)
                .addComponent(btnResetKaryawan)
                .addGap(48, 48, 48)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCariPegawai)
                    .addComponent(txtCariPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pegawai", jPanel1);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Tambah Lokasi");

        jLabel16.setText("ID Lokasi");

        txtIdLokasi.setEditable(false);
        txtIdLokasi.setEnabled(false);
        txtIdLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdLokasiActionPerformed(evt);
            }
        });

        jLabel17.setText("Nama Ruangan");

        jLabel19.setText("Gedung");

        btnBaruLokasi.setText("Baru");
        btnBaruLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruLokasiActionPerformed(evt);
            }
        });

        btnUpdateLokasi.setText("Update");
        btnUpdateLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateLokasiActionPerformed(evt);
            }
        });

        btnHapusLokasi.setText("Hapus");
        btnHapusLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusLokasiActionPerformed(evt);
            }
        });

        jLabel20.setText("Cari");

        btnCariLokasi.setText("Cari");
        btnCariLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariLokasiActionPerformed(evt);
            }
        });

        tblLokasi.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLokasi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(tblLokasi);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("List Lokasi");

        btnResetLokasi.setText("Reset");
        btnResetLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetLokasiActionPerformed(evt);
            }
        });

        btnSubmitLokasi.setText("Submit");
        btnSubmitLokasi.setEnabled(false);
        btnSubmitLokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitLokasiActionPerformed(evt);
            }
        });

        txtCariLokasi.setForeground(new java.awt.Color(153, 153, 153));
        txtCariLokasi.setText("Cari Lokasi...");
        txtCariLokasi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCariLokasiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCariLokasiFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnResetLokasi)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel20)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(txtCariLokasi)
                            .addGap(18, 18, 18)
                            .addComponent(btnCariLokasi))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addComponent(jLabel17)
                                .addComponent(jLabel19))
                            .addGap(33, 33, 33)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtIdLokasi, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addComponent(txtNamaRuangan)
                                .addComponent(txtGedung)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnBaruLokasi)
                            .addGap(18, 18, 18)
                            .addComponent(btnUpdateLokasi)
                            .addGap(18, 18, 18)
                            .addComponent(btnHapusLokasi)
                            .addGap(18, 18, 18)
                            .addComponent(btnSubmitLokasi))
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel15)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtIdLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtNamaRuangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtGedung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBaruLokasi)
                    .addComponent(btnUpdateLokasi)
                    .addComponent(btnHapusLokasi)
                    .addComponent(btnSubmitLokasi))
                .addGap(18, 18, 18)
                .addComponent(btnResetLokasi)
                .addGap(48, 48, 48)
                .addComponent(jLabel9)
                .addGap(16, 16, 16)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCariLokasi)
                    .addComponent(txtCariLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Lokasi", jPanel2);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Tambah Kategori");

        jLabel21.setText("ID Kategori");

        txtIdKategori.setEditable(false);
        txtIdKategori.setEnabled(false);
        txtIdKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdKategoriActionPerformed(evt);
            }
        });

        jLabel22.setText("Nama Kategori");

        btnBaruKategori.setText("Baru");
        btnBaruKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruKategoriActionPerformed(evt);
            }
        });

        btnUpdateKategori.setText("Update");
        btnUpdateKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateKategoriActionPerformed(evt);
            }
        });

        btnHapusKategori.setText("Hapus");
        btnHapusKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusKategoriActionPerformed(evt);
            }
        });

        jLabel24.setText("Cari");

        txtCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariKategoriActionPerformed(evt);
            }
        });

        btnCariKategori.setText("Cari");
        btnCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKategoriActionPerformed(evt);
            }
        });

        tblKategori.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tblKategori);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("List Kategori");

        jLabel23.setText("Umur Ekonomis");

        btnResetKategori.setText("Reset");
        btnResetKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKategoriActionPerformed(evt);
            }
        });

        btnSubmitKategori.setText("Submit");
        btnSubmitKategori.setEnabled(false);
        btnSubmitKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitKategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCariKategori)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariKategori))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdKategori, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(txtNamaKategori)
                            .addComponent(spnUmurEkonomis)))
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24)
                    .addComponent(btnResetKategori)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnBaruKategori)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateKategori)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusKategori)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubmitKategori)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel18)
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtIdKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(spnUmurEkonomis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBaruKategori)
                    .addComponent(btnUpdateKategori)
                    .addComponent(btnHapusKategori)
                    .addComponent(btnSubmitKategori))
                .addGap(18, 18, 18)
                .addComponent(btnResetKategori)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(32, 32, 32)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCariKategori)
                    .addComponent(txtCariKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Kategori", jPanel3);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPegawaiActionPerformed

    private void txtIdLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdLokasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdLokasiActionPerformed

    private void txtIdKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdKategoriActionPerformed

    private void txtCariKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariKategoriActionPerformed

    private void btnBaruPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruPegawaiActionPerformed
        isNewPegawai = true;

        btnUpdatePegawai.setEnabled(false);
        btnHapusPegawai.setEnabled(false);
        btnBaruPegawai.setEnabled(false);

        txtIdPegawai.setText("(Auto)");
        txtNamaLengkap.setText("");
        cbxDepartemen.setSelectedIndex(0);
        txtKontak.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
//        txtJawaban.setText("");

//        autoIdPegawai();
        txtNamaLengkap.requestFocus();

        btnSubmitPegawai.setEnabled(true);
    }//GEN-LAST:event_btnBaruPegawaiActionPerformed

    private void cbxDepartemenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDepartemenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxDepartemenActionPerformed

    private void btnUpdatePegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePegawaiActionPerformed
        int rowIndex = tblPegawai.getSelectedRow();

        if (rowIndex != -1) {
            isUpdatePegawai = true;
            btnBaruPegawai.setEnabled(false);
            btnHapusPegawai.setEnabled(false);
//            btnUpdatePegawai.setEnabled(false);
            btnSubmitPegawai.setEnabled(true);

            String id = modelPegawai.getValueAt(rowIndex, 0).toString();
            String nama = modelPegawai.getValueAt(rowIndex, 1).toString();
            String departemen = modelPegawai.getValueAt(rowIndex, 3).toString();
            String kontak = modelPegawai.getValueAt(rowIndex, 4).toString();

            txtIdPegawai.setText(id);
            txtNamaLengkap.setText(nama);
            cbxDepartemen.setSelectedItem(departemen);
            txtKontak.setText(kontak);

            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);

            txtIdPegawai.setEnabled(false);
            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
//            txtPertanyaan.setEnabled(false);
//            txtJawaban.setEnabled(false);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }
    }//GEN-LAST:event_btnUpdatePegawaiActionPerformed

    private void btnSubmitPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitPegawaiActionPerformed
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (conn == null) {
            return;
        }

        PreparedStatement ps = null;
        String sql = "";

        String nama = txtNamaLengkap.getText();
        String departemen = cbxDepartemen.getSelectedItem().toString();
        String kontak = txtKontak.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
//        String pertanyaan = txtPertanyaan.getText();
//        String jawaban = txtJawaban.getText();

        String usernameDefault = nama.toLowerCase().replaceAll(" ", "");

        String pertanyaan = "Masukkan nama lengkap";
        String jawaban = usernameDefault;

        if (nama.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong!");
            resetFormState();
            return;
        } else if (kontak.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Kontak tidak boleh kosong!");
            resetFormState();
        } else if (!kontak.matches("\\d+")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Kontak harus berupa angka!");
            resetFormState();
            return;
        } else if (kontak.length() < 10 || kontak.length() > 13) {
            javax.swing.JOptionPane.showMessageDialog(null, "Panjang nomor kontak tidak valid!");
            resetFormState();
            return;
        }

        try {
            if (isNewPegawai) {
                sql = "insert into pegawai (nama_pegawai, departemen, kontak, username, password, pertanyaan, jawaban) values (?, ?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);

                if (username.isEmpty()) {
                    username = usernameDefault;
                } else if (password.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Password tidak boleh kosong!");
                    resetFormState();
                    return;
                }

//                ps.setInt(1, idPegawai);
                ps.setString(1, nama);
                ps.setString(2, departemen);
                ps.setString(3, kontak);
                ps.setString(4, username);
                ps.setString(5, password);
                ps.setString(6, pertanyaan);
                ps.setString(7, jawaban);

                ps.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data Pegawai Berhasil Disimpan!");

                loadDataPegawai();
                resetFormState();

            } else if (isUpdatePegawai) {
                int idPegawai = Integer.parseInt(txtIdPegawai.getText());

                sql = "update pegawai set nama_pegawai=?, departemen=?, kontak=? where id_pegawai=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, departemen);
                ps.setString(3, kontak);
                ps.setInt(4, idPegawai);

                ps.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data Profile Pegawai Berhasil DiUpdate!");

                loadDataPegawai();
                resetFormState();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_btnSubmitPegawaiActionPerformed

    private void btnResetKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKaryawanActionPerformed
        resetFormState();
        loadDataPegawai();
        txtCariPegawai.setText("");
    }//GEN-LAST:event_btnResetKaryawanActionPerformed

    private void btnHapusPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPegawaiActionPerformed
        Connection conn = null;

        int rowIndex = tblPegawai.getSelectedRow();

        if (rowIndex != -1) {

            try {
                conn = ConnectDatabaseLoginBDL.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (conn == null) {
                return;
            }
            int id = Integer.parseInt(modelPegawai.getValueAt(rowIndex, 0).toString());

            btnBaruPegawai.setEnabled(false);
            btnUpdatePegawai.setEnabled(false);
            btnResetKaryawan.setEnabled(false);
            btnSubmitPegawai.setEnabled(false);

            try {
                String sql = "delete from pegawai where id_pegawai=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);

                int deletePegawai = javax.swing.JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus data pegawai ini?", "Hapus Data Pegawai", 0);

                if (deletePegawai == JOptionPane.YES_OPTION) {

                    ps.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(null, "Data Pegawai Berhasil Dihapus!");
                    loadDataPegawai();
                    resetFormState();
                } else {
                    resetFormState();
                }

            } catch (SQLException e) {
                if (e.getSQLState() != null && e.getSQLState().equals("23503")) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                     "Gagal Hapus! Pegawai ini sedang menggunakan Aset/ Melakukan Perbaikan.\n"
                     + "Pastikan tidak ada aset/ perbaikan sebelum menghapus.");
                } else {
                    // Generic Error
                    javax.swing.JOptionPane.showMessageDialog(null, "Error Hapus: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }

    }//GEN-LAST:event_btnHapusPegawaiActionPerformed

    private void btnCariPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPegawaiActionPerformed
        String keyword = txtCariPegawai.getText();

        if (keyword.trim().isEmpty() || keyword.equals("Cari berdasarkan Nama...")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Nama Pegawai yang anda cari tidak ada");
            loadDataPegawai();
        } else {
            searchDataPegawai(keyword);

            if (tblPegawai.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(null,
                 "Data Pegawai tidak ditemukan!",
                 "Pencarian",
                 javax.swing.JOptionPane.INFORMATION_MESSAGE);

                loadDataPegawai();
                txtCariPegawai.setText("");
            }
        }
    }//GEN-LAST:event_btnCariPegawaiActionPerformed

    private void btnResetLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetLokasiActionPerformed
        resetFormLokasi();
        loadDataLokasi();
        txtCariLokasi.setText("");
    }//GEN-LAST:event_btnResetLokasiActionPerformed

    private void btnSubmitLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitLokasiActionPerformed
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nama = txtNamaRuangan.getText();
        String gedung = txtGedung.getText();

        PreparedStatement ps = null;
        String sql = "";

        try {
            if (isNewLokasi) {
                sql = "insert into lokasi (nama_lokasi, gedung) values (?, ?) returning id_lokasi";

                ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, gedung);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String id = rs.getString(1);
                    txtIdLokasi.setText(id);
                    javax.swing.JOptionPane.showMessageDialog(null, "Berhasil menambahkan lokasi baru!" + "\n" + "ID: " + id);
                }

                loadDataLokasi();
                resetFormLokasi();
            } else if (isUpdateLokasi) {
                String id = txtIdLokasi.getText();

                sql = "update lokasi set nama_lokasi=?, gedung=? where id_lokasi=?";
                ps = conn.prepareStatement(sql);

                ps.setString(1, nama);
                ps.setString(2, gedung);
                ps.setString(3, id);

                ps.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data Lokasi Berhasil Diperbarui!");

                loadDataLokasi();
                resetFormLokasi();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSubmitLokasiActionPerformed

    private void btnBaruLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruLokasiActionPerformed
        isNewLokasi = true;

        btnBaruLokasi.setEnabled(false);
        btnUpdateLokasi.setEnabled(false);
        btnHapusLokasi.setEnabled(false);
        btnSubmitLokasi.setEnabled(true);

        txtIdLokasi.setText("(Auto)");
        txtNamaRuangan.setText("");
        txtGedung.setText("");

        txtIdLokasi.requestFocus();
    }//GEN-LAST:event_btnBaruLokasiActionPerformed

    private void btnUpdateLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateLokasiActionPerformed
        int row = tblLokasi.getSelectedRow();

        if (row != -1) {
            isUpdateLokasi = true;

            String id = tblLokasi.getValueAt(row, 0).toString();
            String nama = tblLokasi.getValueAt(row, 1).toString();
            String gedung = tblLokasi.getValueAt(row, 2).toString();

            txtIdLokasi.setText(id);
            txtNamaRuangan.setText(nama);
            txtGedung.setText(gedung);

            txtNamaRuangan.requestFocus();

//            btnUpdateLokasi.setEnabled(false);
            btnHapusLokasi.setEnabled(false);
            btnBaruLokasi.setEnabled(false);
            btnSubmitLokasi.setEnabled(true);

        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }
    }//GEN-LAST:event_btnUpdateLokasiActionPerformed

    private void txtCariPegawaiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCariPegawaiFocusGained
        if (txtCariPegawai.getText().equals("Cari berdasarkan Nama...")) {
            txtCariPegawai.setText("");
            txtCariPegawai.setForeground(new java.awt.Color(0, 0, 0));

        }
    }//GEN-LAST:event_txtCariPegawaiFocusGained

    private void txtCariPegawaiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCariPegawaiFocusLost
        if (txtCariPegawai.getText().isEmpty()) {
            txtCariPegawai.setText("Cari berdasarkan Nama...");
            txtCariPegawai.setForeground(new java.awt.Color(153, 153, 153));
            loadDataPegawai();
        }
    }//GEN-LAST:event_txtCariPegawaiFocusLost

    private void btnHapusLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusLokasiActionPerformed
        Connection conn = null;

        int rowIndex = tblLokasi.getSelectedRow();

        if (rowIndex != -1) {
            try {
                conn = ConnectDatabaseLoginBDL.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (conn == null) {
                return;
            }
            String id = modelLokasi.getValueAt(rowIndex, 0).toString();

            btnBaruPegawai.setEnabled(false);
            btnUpdatePegawai.setEnabled(false);
            btnResetKaryawan.setEnabled(false);
            btnSubmitPegawai.setEnabled(false);

            try {
                String sql = "delete from lokasi where id_lokasi=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);

                int deleteLokasi = javax.swing.JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus data lokasi ini?", "Hapus Data Lokasi", 0);

                if (deleteLokasi == JOptionPane.YES_OPTION) {

                    ps.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(null, "Data Lokasi Berhasil Dihapus!");
                    loadDataLokasi();
                    resetFormLokasi();
                } else {
                    resetFormLokasi();
                }

            } catch (SQLException e) {
                if (e.getSQLState() != null && e.getSQLState().equals("23503")) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                     "Gagal Hapus! Lokasi ini sedang digunakan oleh Aset.\n"
                     + "Pastikan tidak ada aset di ruangan ini sebelum menghapus.");
                } else {
                    // Generic Error
                    javax.swing.JOptionPane.showMessageDialog(null, "Error Hapus: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }
    }//GEN-LAST:event_btnHapusLokasiActionPerformed

    private void txtCariLokasiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCariLokasiFocusGained
        if (txtCariLokasi.getText().equals("Cari Lokasi...")) {
            txtCariLokasi.setText("");
            txtCariLokasi.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_txtCariLokasiFocusGained

    private void txtCariLokasiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCariLokasiFocusLost
        if (txtCariLokasi.getText().isEmpty()) {
            txtCariLokasi.setText("Cari Lokasi...");
            txtCariLokasi.setForeground(java.awt.Color.GRAY);
            loadDataLokasi();
        }
    }//GEN-LAST:event_txtCariLokasiFocusLost

    private void btnCariLokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariLokasiActionPerformed
        String keyword = txtCariLokasi.getText();

        if (keyword.trim().isEmpty() || keyword.equals("Cari Lokasi...")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Lokasi yang anda cari tidak ada");
            loadDataLokasi();
        } else {
            searchDataLokasi(keyword);

            if (tblLokasi.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(null,
                 "Data Lokasi tidak ditemukan!",
                 "Pencarian",
                 javax.swing.JOptionPane.INFORMATION_MESSAGE);

                // Optional: Reset table so it doesn't stay empty
                loadDataLokasi();
                txtCariLokasi.setText("");
            }
        }
    }//GEN-LAST:event_btnCariLokasiActionPerformed

    private void txtCariPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariPegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariPegawaiActionPerformed

    private void btnBaruKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruKategoriActionPerformed
        isNewKategori = true;

        btnBaruKategori.setEnabled(false);
        btnUpdateKategori.setEnabled(false);
        btnHapusKategori.setEnabled(false);
        btnSubmitKategori.setEnabled(true);

        txtIdKategori.setText("(Auto)");
        txtNamaKategori.setText("");
        spnUmurEkonomis.setValue(0);

        txtIdLokasi.requestFocus();
    }//GEN-LAST:event_btnBaruKategoriActionPerformed

    private void btnSubmitKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitKategoriActionPerformed
        Connection conn = null;
        try {
            conn = ConnectDatabaseLoginBDL.getConnection();
        } catch (Exception e) {
            return;
        }

        String nama = txtNamaKategori.getText();
        int umur = (Integer) spnUmurEkonomis.getValue();

        if (nama.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Nama Kategori tidak boleh kosong!");
            return;
        }

        try {
            PreparedStatement ps = null;
            String sql = "";

            if (isNewKategori) {
                sql = "INSERT INTO kategori (nama_kategori, umur_ekonomis_tahun) VALUES (?, ?) RETURNING id_kategori";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setInt(2, umur);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Berhasil menambahkan kategori baru!" + "\n" + " ID: " + rs.getString(1));
                }
            } else if (isUpdateKategori) {
                String id = txtIdKategori.getText();
                sql = "UPDATE kategori SET nama_kategori=?, umur_ekonomis_tahun=? WHERE id_kategori=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setInt(2, umur);
                ps.setString(3, id);

                ps.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data Kategori Diperbarui!");
            }

            loadDataKategori();
            resetFormKategori();

        } catch (SQLException e) {
            System.out.println("Error Submit Kategori: " + e);
        }
    }//GEN-LAST:event_btnSubmitKategoriActionPerformed

    private void btnUpdateKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateKategoriActionPerformed
        int row = tblKategori.getSelectedRow();

        if (row != -1) {
            isUpdateKategori = true;

            String id = tblKategori.getValueAt(row, 0).toString();
            String nama = tblKategori.getValueAt(row, 1).toString();
            int umur = Integer.parseInt(tblKategori.getValueAt(row, 2).toString());

            txtIdKategori.setText(id);
            txtNamaKategori.setText(nama);
            spnUmurEkonomis.setValue(umur);

            txtNamaKategori.requestFocus();

            btnHapusKategori.setEnabled(false);
            btnBaruKategori.setEnabled(false);
            btnSubmitKategori.setEnabled(true);

        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }
    }//GEN-LAST:event_btnUpdateKategoriActionPerformed

    private void btnHapusKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusKategoriActionPerformed
        Connection conn = null;

        int rowIndex = tblKategori.getSelectedRow();

        if (rowIndex != -1) {
            try {
                conn = ConnectDatabaseLoginBDL.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(FormDataMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (conn == null) {
                return;
            }
            String id = modelKategori.getValueAt(rowIndex, 0).toString();

            btnBaruKategori.setEnabled(false);
            btnUpdateKategori.setEnabled(false);
            btnResetKategori.setEnabled(false);
            btnSubmitKategori.setEnabled(false);

            try {
                String sql = "delete from kategori where id_kategori=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);

                int deleteLokasi = javax.swing.JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus kategori ini?", "Hapus Data Kategori", 0);

                if (deleteLokasi == JOptionPane.YES_OPTION) {

                    ps.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(null, "Kategori Berhasil Dihapus!");
                    loadDataKategori();
                    resetFormKategori();
                } else {
                    resetFormKategori();
                }

            } catch (SQLException e) {
                if (e.getSQLState() != null && e.getSQLState().equals("23503")) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                     "Gagal Hapus! Kategori ini sedang digunakan oleh Aset.\n"
                     + "Pastikan tidak ada aset yang terhubung sebelum menghapus.");
                } else {
                    // Generic Error
                    javax.swing.JOptionPane.showMessageDialog(null, "Error Hapus: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Mohon pilih data terlebih dahulu!");
        }
    }//GEN-LAST:event_btnHapusKategoriActionPerformed

    private void btnCariKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKategoriActionPerformed
        // TODO add your handling code here:
        String keyword = txtCariKategori.getText();

        if (keyword.trim().isEmpty() || keyword.equals("Cari berdasarkan Nama...")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Kategori yang anda cari tidak ada");
            loadDataKategori();
        } else {
            searchDataKategori(keyword);

            if (tblKategori.getRowCount() == 0) {
                javax.swing.JOptionPane.showMessageDialog(null,
                 "Data Kategori tidak ditemukan!",
                 "Pencarian",
                 javax.swing.JOptionPane.INFORMATION_MESSAGE);

                loadDataKategori();
                txtCariKategori.setText("");
            }
        }
    }//GEN-LAST:event_btnCariKategoriActionPerformed

    private void btnResetKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKategoriActionPerformed
        // TODO add your handling code here:
        resetFormKategori();
        loadDataKategori();
        txtCariKategori.setText("");
    }//GEN-LAST:event_btnResetKategoriActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaruKategori;
    private javax.swing.JButton btnBaruLokasi;
    private javax.swing.JButton btnBaruPegawai;
    private javax.swing.JButton btnCariKategori;
    private javax.swing.JButton btnCariLokasi;
    private javax.swing.JButton btnCariPegawai;
    private javax.swing.JButton btnHapusKategori;
    private javax.swing.JButton btnHapusLokasi;
    private javax.swing.JButton btnHapusPegawai;
    private javax.swing.JButton btnResetKaryawan;
    private javax.swing.JButton btnResetKategori;
    private javax.swing.JButton btnResetLokasi;
    private javax.swing.JButton btnSubmitKategori;
    private javax.swing.JButton btnSubmitLokasi;
    private javax.swing.JButton btnSubmitPegawai;
    private javax.swing.JButton btnUpdateKategori;
    private javax.swing.JButton btnUpdateLokasi;
    private javax.swing.JButton btnUpdatePegawai;
    private javax.swing.JComboBox<String> cbxDepartemen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner spnUmurEkonomis;
    private javax.swing.JTable tblKategori;
    private javax.swing.JTable tblLokasi;
    private javax.swing.JTable tblPegawai;
    private javax.swing.JTextField txtCariKategori;
    private javax.swing.JTextField txtCariLokasi;
    private javax.swing.JTextField txtCariPegawai;
    private javax.swing.JTextField txtGedung;
    private javax.swing.JTextField txtIdKategori;
    private javax.swing.JTextField txtIdLokasi;
    private javax.swing.JTextField txtIdPegawai;
    private javax.swing.JTextField txtKontak;
    private javax.swing.JTextField txtNamaKategori;
    private javax.swing.JTextField txtNamaLengkap;
    private javax.swing.JTextField txtNamaRuangan;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
