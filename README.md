# Tugas Praktikum 5 - Desain Pemrograman Berorientasi Objek (DPBO)

## 1. Janji
Saya Afzaal Zaidan Febryanto dengan NIM 2508692 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

---

## 2. Desain dan Fungsi Program
Program ini merupakan pengembangan dari aplikasi pengelolaan data karyawan berbasis desktop (**Java Swing GUI**) pada TP4 yang kini telah diintegrasikan secara penuh dengan basis data relasional **MySQL** menggunakan **Java Database Connectivity (JDBC)**.

### a. Class Database (Koneksi & Eksekusi Query JDBC)
Class ini berfungsi sebagai jembatan komunikasi antara aplikasi Java dengan server database MySQL (`jdbc:mysql://localhost:3306/db_people`).
- **Atribut**:
  - `connection` (`Connection`): Menyimpan sesi koneksi aktif ke database MySQL.
  - `statement` (`Statement`): Objek untuk mengeksekusi perintah/query SQL ke database.
- **Method**:
  - `Database()` (Constructor): Membuka koneksi ke database MySQL menggunakan `DriverManager.getConnection()` dan menginisialisasi objek `statement`.
  - `selectQuery(String sql)`: Mengeksekusi perintah SQL `SELECT` dan mengembalikan hasilnya dalam bentuk objek `ResultSet`.
  - `insertUpdateDeleteQuery(String sql)`: Mengeksekusi perintah manipulasi data (`INSERT`, `UPDATE`, `DELETE`) menggunakan `executeUpdate()` dan mengembalikan jumlah baris yang terpengaruh.
  - `getStatement()`: Getter untuk mengambil objek `Statement`.

### b. Class Person (Model Data)
Class ini merepresentasikan struktur entitas data seorang karyawan yang bersesuaian dengan kolom-kolom pada tabel `person` di database.
- **Atribut**:
  - `id` (String): ID identitas karyawan (*Primary Key*).
  - `nama` (String): Nama lengkap karyawan.
  - `tahunLahir` (int): Tahun kelahiran karyawan.
  - `kategori` (String): Jabatan atau divisi pekerjaan (`Staff`, `Manager`, `Intern`, `Supervisor`).
  - `status` (String): Status kepegawaian (`Tetap` / `Tidak Tetap`).
- **Method**:
  - Constructor tanpa parameter dan constructor berparameter lengkap.
  - Setter dan Getter untuk masing-masing atribut.

### c. Class PersonMenu extends JFrame (GUI & Controller)
Class ini mengatur tampilan antarmuka pengguna (GUI), menampung *event listener* untuk interaksi komponen, melakukan validasi *input*, serta memanggil objek `Database` untuk menjalankan query CRUD SQL.

<img width="743" height="641" alt="gui" src="https://github.com/user-attachments/assets/4ca3f855-ab5a-4705-a044-be6c2ea42dd6" />
<br>

**Komponen Utama GUI:**
- **`JTextField`** $\rightarrow$ untuk input ID (`idField`), Nama (`namaField`), dan Tahun Lahir (`tahunLahirField`).
- **`JComboBox`** $\rightarrow$ untuk memilih Kategori pekerjaan (`kategoriComboBox`).
- **`JRadioButton`** $\rightarrow$ untuk menentukan Status kepegawaian Tetap / Tidak Tetap (`tetapRadioButton`, `tidakTetapRadioButton`) yang dikelompokkan ke dalam `ButtonGroup`.
- **`JTable`** $\rightarrow$ untuk menampilkan seluruh daftar data karyawan dari database di dalam `JScrollPane` (`personTable`).
- **Tombol Add/Update, Delete, dan Cancel** $\rightarrow$ untuk mengeksekusi operasi data ke database dan mengatur mode form input (`addUpdateButton`, `deleteButton`, `cancelButton`).

**Fungsi utama dalam class ini:**
- `setTable()` – Mengambil seluruh baris data dari tabel `person` di MySQL menggunakan query `SELECT * FROM person` dan memetakannya ke dalam `DefaultTableModel`.
- `insertData()` – Memvalidasi kekosongan *input* dan duplikasi ID di database, lalu menambahkan data karyawan baru menggunakan query `INSERT INTO person VALUES (...)`.
- `updateData()` – Memvalidasi *input* dan memperbarui data karyawan pada baris yang dipilih menggunakan query `UPDATE person SET ... WHERE id = ...`.
- `deleteData()` – Menghapus data karyawan yang dipilih dari database menggunakan query `DELETE FROM person WHERE id = ...` setelah memperoleh konfirmasi pengguna.
- `clearForm()` – Mengosongkan isian *textfield*, mereset *combo box* dan *radio button*, serta mengembalikan tombol ke *state* awal.

---

## 3. Penjelasan Atribut dan Method Setiap Kelas

### a. Kelas `Database`
- **`connection` (`Connection`)**: Menyimpan objek koneksi ke server MySQL lokal.
- **`statement` (`Statement`)**: Menyimpan objek *statement* SQL untuk pengiriman query.
- **`Database()`**: Konstruktor untuk menginisialisasi koneksi ke `jdbc:mysql://localhost:3306/db_people` dengan *user* `"root"` dan *password* `""`.
- **`selectQuery(String sql)`**: Menjalankan `statement.executeQuery(sql)` dan mengembalikan `ResultSet`.
- **`insertUpdateDeleteQuery(String sql)`**: Menjalankan `statement.executeUpdate(sql)` untuk mengubah isi tabel di database.
- **`getStatement()`**: Mengembalikan nilai atribut `statement`.

### b. Kelas `Person`
- **`id` (String)**: Menyimpan nomor identitas karyawan.
- **`nama` (String)**: Menyimpan nama lengkap karyawan.
- **`tahunLahir` (int)**: Menyimpan angka tahun kelahiran karyawan.
- **`kategori` (String)**: Menyimpan tingkatan posisi/divisi kerja.
- **`status` (String)**: Menyimpan status kerja karyawan ("Tetap" atau "Tidak Tetap").
- **`Person(...)`**: Menginisialisasi nilai seluruh properti saat objek dibentuk.
- **Getter dan Setter**: Mengakses serta memodifikasi nilai setiap atribut secara terenkapsulasi.

### c. Kelas `PersonMenu`
- **`database` (`Database`)**: Objek koneksi database untuk mengeksekusi query SQL pada aplikasi.
- **`selectedIndex` (int)**: Variabel penanda indeks baris tabel yang aktif dipilih (bernilai `-1` jika tidak ada baris yang dipilih).
- **`main(String[] args)`**: Titik awal eksekusi program untuk membuat instansi *window*, mengatur ukuran jendela (750x650), memposisikan *window* di tengah layar, dan menampilkannya.
- **`PersonMenu()`**: Konstruktor untuk menginisialisasi objek `Database`, mengelompokkan `ButtonGroup` *radio button*, memuat data tabel dari database melalui `setTable()`, serta mengatur *event listener* tombol dan klik tabel.
- **`setTable()`**: Membuat model tabel baru (`DefaultTableModel`) dengan kolom No, ID, Nama, Tahun Lahir, Kategori, dan Status, lalu mengisi barisnya melalui perulangan `while (resultSet.next())` dari hasil query `SELECT * FROM person`.
- **`insertData()`**: Mengambil nilai dari komponen form, menampilkan dialog *error* jika ada *field* kosong, bukan angka, atau jika `id` sudah terdaftar di database (`SELECT * FROM person WHERE id = ...`). Jika lolos validasi, program menjalankan query `INSERT` dan memperbarui tabel.
- **`updateData()`**: Mengambil nilai dari form dan ID asli dari baris tabel yang dipilih (`curId`), memvalidasi kekosongan *input* dan konflik ID, lalu mengeksekusi query `UPDATE` ke database dan memperbarui tabel.
- **`deleteData()`**: Mengambil ID dari baris tabel pada `selectedIndex` (`personTable.getModel().getValueAt(selectedIndex, 1)`), mengeksekusi query `DELETE FROM person WHERE id = ...`, lalu memperbarui tabel dan membersihkan form.
- **`clearForm()`**: Mengosongkan seluruh kolom input teks, memilih opsi default pada *combo box*, mereset *radio button* ke "Tetap", mengembalikan teks tombol menjadi "Add", menyembunyikan tombol "Delete", serta mengatur `selectedIndex` kembali ke nilai `-1`.

---

## 4. Penjelasan Alur Program

Alur kerja program berjalan secara terstruktur sebagai berikut:

1. **Inisialisasi Program dan Menampilkan Data dari Database (Read)**:
   - Program dijalankan melalui method `main()`, yang membuat instansi dari kelas `PersonMenu`.
   - Konstruktor `PersonMenu()` menginisialisasi objek `database = new Database()` untuk membuka koneksi ke MySQL.
   - Method `setTable()` dipanggil untuk mengeksekusi query `SELECT * FROM person`. Seluruh baris pada `ResultSet` dipetakan ke dalam `DefaultTableModel` dan ditampilkan pada `personTable`.
   - Pada kondisi awal ini, form input dalam keadaan kosong, tombol aksi bertuliskan "Add", dan tombol "Delete" disembunyikan.

2. **Alur Menambahkan Data Baru dan Validasi Error (Create)**:
   - Pengguna mengisi kolom ID, Nama, dan Tahun Lahir, memilih Kategori pada *dropdown*, serta memilih Status pada *Radio Button* ("Tetap" atau "Tidak Tetap"), lalu menekan tombol **"Add"**.
   - **Validasi Input Kosong**: Program mengecek apakah ada *field* yang kosong atau kategori masih bernilai `"???"`. Jika ya, muncul dialog error *"Semua field harus diisi!"* dan proses dihentikan.
   - **Validasi Format Angka**: Program mengonversi Tahun Lahir ke tipe `int`. Jika input bukan angka, blok `catch (NumberFormatException)` akan menampilkan dialog error *"Tahun Lahir harus berupa angka!"*.
   - **Validasi Duplikasi ID**: Program menjalankan query `SELECT * FROM person WHERE id = '...'`. Jika `rs.next()` bernilai `true` (ID sudah ada), muncul dialog error *"ID sudah digunakan!"* dan proses penambahan dibatalkan.
   - Jika seluruh validasi terpenuhi, program mengeksekusi query `INSERT INTO person VALUES (...)`, memperbarui tabel dengan `setTable()`, mereset form dengan `clearForm()`, dan menampilkan dialog *"Data berhasil ditambahkan"*.

3. **Alur Memilih dan Mengubah Data (Update)**:
   - Pengguna mengklik salah satu baris karyawan pada tabel (`personTable`).
   - Event `mousePressed` menyimpan indeks baris ke `selectedIndex` dan mengambil nilai tiap kolom langsung dari model tabel (`personTable.getModel().getValueAt(...)`) tanpa menggunakan `ArrayList`.
   - Nilai tersebut dimasukkan ke komponen form, teks tombol "Add" berubah menjadi **"Update"**, dan tombol **"Delete"** ditampilkan.
   - Pengguna mengubah data pada form lalu menekan tombol **"Update"**.
   - Program melakukan validasi input kosong, format angka tahun lahir, serta memeriksa apakah ada bentrok ID baru di database.
   - Jika valid, program mengeksekusi query `UPDATE person SET ... WHERE id = '...'`, merefresh tabel, membersihkan form, dan menampilkan dialog *"Data berhasil diubah"*.

4. **Alur Menghapus Data dengan Prompt Konfirmasi (Delete)**:
   - Pengguna memilih baris data pada tabel, lalu menekan tombol **"Delete"**.
   - Program memunculkan dialog konfirmasi *"Apakah Anda yakin ingin menghapus data ini?"* (Yes/No).
   - Jika pengguna memilih **Yes**, method `deleteData()` mengambil ID baris terpilih dari model tabel, mengeksekusi query `DELETE FROM person WHERE id = '...'`, merefresh tabel, membersihkan form, dan menampilkan dialog *"Data berhasil dihapus"*.
   - Jika pengguna memilih **No**, penghapusan dibatalkan.

5. **Alur Pembatalan Operasi (Cancel)**:
   - Pengguna menekan tombol **"Cancel"** kapan saja untuk memanggil `clearForm()`, yang akan mengosongkan seluruh form input, mengembalikan tombol ke "Add", menyembunyikan tombol "Delete", dan mereset `selectedIndex` menjadi `-1`.

---

## 5. Dokumentasi Program

### a. Tampilan Awal Program (Read Data dari Database)
Tampilan program saat pertama kali dibuka, memuat data karyawan langsung dari tabel `person` pada database MySQL.

<img width="918" height="793" alt="read" src="https://github.com/user-attachments/assets/416f3ea7-1b9d-4969-8ece-15385ade62b3" />


### b. Operasi Tambah Data (Create)
Pengisian data baru pada form input yang langsung tersimpan ke database MySQL dan memperbarui tampilan tabel.

https://github.com/user-attachments/assets/ef87452e-dc1d-4a74-8e9c-d8d55e3bd66c

### c. Operasi Pemilihan Baris dan Ubah Data (Update)
Pemilihan baris tabel tanpa `ArrayList`, pengubahan nilai pada form, dan pembaruan data ke database dengan menekan tombol "Update".

https://github.com/user-attachments/assets/0dc1b0b3-2c69-4296-a4cf-ffa0cac9aea0

### d. Prompt Konfirmasi dan Operasi Hapus Data (Delete)
Munculnya jendela konfirmasi pop-up saat tombol "Delete" ditekan serta kondisi tabel setelah data berhasil dihapus dari database.


https://github.com/user-attachments/assets/5f6275dd-2903-4312-b14c-6cee7545aecf



### e. Dialog Error (Input Kosong & Duplikasi ID)
Tampilan *prompt error* ketika masih terdapat kolom input yang kosong saat *insert/update* serta *prompt error* ketika pengguna memasukkan ID yang sudah terdaftar di database.


https://github.com/user-attachments/assets/258d3d4e-4317-4bb8-990e-7a1003950d1b

<br>


https://github.com/user-attachments/assets/9b4d7bac-2118-4f1c-8562-2055b1e11842

