import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMenu extends JFrame {
    // index baris yang diklik
    private int selectedIndex = -1;
    private Database database;

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JRadioButton tetapRadioButton;
    private JRadioButton tidakTetapRadioButton;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable personTable;

    private ButtonGroup statusGroup;

    public static void main(String[] args) {
        // buat objek window
        PersonMenu menu = new PersonMenu();

        // atur ukuran window
        menu.setSize(750, 650);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // constructor
    public PersonMenu() {
        // kelompokkan radio button
        statusGroup = new ButtonGroup();
        statusGroup.add(tetapRadioButton);
        statusGroup.add(tidakTetapRadioButton);
        tetapRadioButton.setSelected(true);

        // buat objek database
        database = new Database();

        // isi tabel person
        personTable.setModel(setTable());

        // ubah styling title
        if (titleLabel != null) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        }

        // atur isi combo box
        String[] kategoriData = { "???", "Staff", "Manager", "Intern", "Supervisor" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = personTable.getSelectedRow();

                // value textfield dan combo box (all input)
                String curId = personTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = personTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahunLahir = personTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = personTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curStatus = personTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahunLahir);
                kategoriComboBox.setSelectedItem(curKategori);

                if (curStatus.equalsIgnoreCase("Tetap")) {
                    tetapRadioButton.setSelected(true);
                } else {
                    tidakTetapRadioButton.setSelected(true);
                }

                // ubah button "Add" jadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Status" };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM person");

            // isi tabel dengan hasil query
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = i + 1;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("thn_lahir");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getString("status");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tmp;
    }

    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunLahirStr = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String status = tetapRadioButton.isSelected() ? "Tetap" : "Tidak Tetap";

            // dialog/prompt error jika masih ada kolom input yang kosong
            if (id.isEmpty() || nama.isEmpty() || tahunLahirStr.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int tahun = Integer.parseInt(tahunLahirStr);

            // dialog/prompt error jika sudah ada ID yang sama saat insert
            ResultSet rs = database.selectQuery("SELECT * FROM person WHERE id = '" + id + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // tambahkan data ke database
            String sqlQuery = "INSERT INTO person VALUES ('" + id + "', '" + nama + "', " + tahun + ", '" + kategori + "', '" + status + "')";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunLahirStr = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String status = tetapRadioButton.isSelected() ? "Tetap" : "Tidak Tetap";

            // dialog/prompt error jika masih ada kolom input yang kosong
            if (id.isEmpty() || nama.isEmpty() || tahunLahirStr.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int tahun = Integer.parseInt(tahunLahirStr);
            String curId = personTable.getModel().getValueAt(selectedIndex, 1).toString();

            // cek jika ID diubah ke ID lain yang sudah ada di database
            if (!id.equals(curId)) {
                ResultSet rs = database.selectQuery("SELECT * FROM person WHERE id = '" + id + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "ID sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // ubah data di database
            String sqlQuery = "UPDATE person SET id = '" + id + "', nama = '" + nama + "', thn_lahir = " + tahun + ", kategori = '" + kategori + "', status = '" + status + "' WHERE id = '" + curId + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteData() {
        // hapus data dari database
        String curId = personTable.getModel().getValueAt(selectedIndex, 1).toString();
        String sqlQuery = "DELETE FROM person WHERE id = '" + curId + "'";
        database.insertUpdateDeleteQuery(sqlQuery);

        // update tabel
        personTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        tetapRadioButton.setSelected(true);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}