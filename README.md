# Reflection 1 [module-1]

### Coding Standards & Clean Code Principles
Dalam mengerjakan fitur *Edit* dan *Delete* Product, saya berusaha untuk menerapkan beberapa prinsip *Clean Code* agar kode dapat mudah dibaca dan dikembangkan nantinya, diantaranya:

* **Meaningful Names**: Saya menggunakan penamaan yang deskriptif. Contohnya, menggunakan `productData` untuk menyimpan list product.
* **Single Responsibility Principle (SRP)**: Saya membagi responsibility code secara modular. Contohnya, saya membuat `ProductController` yang menangani navigasi, `ProductService` yang mengelola logika bisnis, dan `ProductRepository` yang berfokus pada manage data product.
* **Fail-Fast Principle**: Pada bagian `ProductRepository`, saya menerapkan fungsi `findById` menggunakan `.orElseThrow()`. Jika ID yang dicari tidak ada, sistem akan langsung melempar `IllegalArgumentException` agar error dapat terdeteksi dengan baik.
* **DRY (Don't Repeat Yourself)**: Saya memastikan tidak ada logic yang berulang dengan memanfaatkan method yang sudah ada untuk mencari dan memvalidasi data product.

### Secure Coding Practices
Beberapa praktik *Secure Coding* yang saya implementasikan antara lain:

* **Non-Predictable Object Identifiers**: Untuk `productId`, saya menggunakan `UUID.randomUUID().toString()` di dalam *constructor* model. Hal ini nantinya dapat mencegah potensi serangan di mana pihak luar mencoba mengakses atau memanipulasi data hanya dengan mengganti urutan angka di URL.
* **Method Restriction for Destructive Actions**: Saya memastikan operasi yang bersifat menghapus data tidak bisa dilakukan melalui metode `GET`. Proses penghapusan data dilakukan melalui metode `POST` setelah melalui halaman konfirmasi, supaya menghindari penghapusan data yang tidak disengaja melalui URL.

### Self-Correction & Future Improvements
* **User-Friendly Error Handling**: Saat ini, jika terjadi kesalahan seperti ID tidak ditemukan, sistem melempar `IllegalArgumentException` yang memunculkan halaman error bawaan Java. Kedepannya, saya perlu mengalihkan user ke halaman error yang lebih *User-Friendly*.
* **Input Validation**: Kedepannya, Saya perlu menambahkan validasi input pada model (seperti jumlah product tidak boleh negatif). [update : saya sudah menambahkan validasi input di bagian model. Namun, untuk bagian UI, handling error yang akan ditampilkan ke user belum diimplementasikan.]

# Reflection 2 [module-1]

Membuat unit test membuat saya merasa aman karena setiap komponen kode saya dapat divalidasi secara independen dan cepat. Terkait kuantitas, sebuah class idealnya memiliki test yang seharusnya mencakup seluruh kemungkinan jalur logic bisnis, termasuk *edge cases*. Untuk memastikan hal tersebut, saya kedepannya dapat menggunakan *code coverage* agar dapat mengidentifikasi bagian *source code* mana yang belum dieksekusi. Namun, *coverage* 100% bukan jaminan kode bebas dari bug. *Coverage* hanya mengukur aspek kuantitas eksekusi baris kode, namun tidak menjamin kebenaran logic bisnis untuk segala variasi input di lingkungan *real-time*.

Mengenai pembuatan *functional test suite* baru untuk memverifikasi jumlah item, melakukan *copy-paste* prosedur *setup* dari test sebelumnya merupakan praktik yang buruk bagi kualitas kode. Masalah *clean code* utama yang muncul adalah pengulangan kode atau pelanggaran prinsip **DRY (Don't Repeat Yourself)**. Hal ini menyebabkan kode menjadi *rigid* dan sulit di-develop kedepannya karena setiap perubahan infrastruktur, seperti konfigurasi URL atau logic sinkronisasi halaman, harus diubah secara manual di setiap class test.

Untuk memperbaiki masalah tersebut, saya menerapkan prinsip **Inheritance** dengan mengekstraksi logika umum ke dalam sebuah `BaseProductFunctionalTest`. Dengan memindahkan *instance variable* serta *helper methods* ke dalam class induk, class test spesifik hanya perlu melakukan *extends*. Pendekatan ini juga memastikan setiap test suite tetap fokus pada tanggung jawab fungsionalnya saja (*Single Responsibility Principle*), meningkatkan keterbacaan kode, dan memudahkan skalabilitas testing di masa mendatang tanpa menimbulkan *redundance*.

# Reflection [module-2]

## Project Links
- **Deployed Application (Koyeb):** [link](https://handicapped-crissy-mug-playground-bae751cb.koyeb.app/)
- **Deployed Application (Railway [updated since Koyeb trial expired])** [link](https://module-1-coding-standards-production.up.railway.app)
- **SonarCloud Project:** [Code Quality Analysis](https://sonarcloud.io/project/overview?id=B-Bermulya-Anugrah-Putra-2406347424_Module-1-Coding-Standards)

### Code Quality Issue(s) & Fixing Strategy
Dalam proses mengintegrasikan *Quality code analysis tools* (SonarCloud) ke dalam *pipeline*, terdapat beberapa isu kualitas kode (*Code Smell*) yang berhasil dideteksi dan saya perbaiki:

* **Empty Methods (Maintainability)**: SonarCloud mendeteksi adanya *method* kosong (seperti `setUp()` dan `contextLoads()`) pada `ProductRepositoryTest` dan `EshopApplicationTests`. Strategi perbaikan yang saya lakukan adalah menghapus *method* tersebut karena memang tidak ada logika *setup* yang dibutuhkan.
* **Duplicated Literals (Maintainability)**: Pada `ProductController`, *string* literal seperti `"product"` dan `"redirect:list"` digunakan secara berulang. Berdasarkan prinsip **DRY (Don't Repeat Yourself)**, saya mengubah *string* tersebut menjadi sebuah *constant variables* (`private static final String`). Strategi ini mempermudah proses *maintenance*, karena jika ada perubahan nama *redirect* atau model, saya hanya perlu mengubahnya di satu tempat.

### CI/CD Implementation Evaluation
Menurut saya, implementasi *workflows* (GitHub Actions) dan *pipelines* yang telah saya buat saat ini sudah memenuhi definisi dari *Continuous Integration* (CI), namun **belum sepenuhnya memenuhi** definisi *Continuous Deployment* (CD).

Untuk **Continuous Integration**, saya telah mengonfigurasi *workflow* yang berjalan otomatis setiap kali ada proses `push` atau `pull request`. *Pipeline* ini secara otomatis melakukan proses *build*, menjalankan seluruh *unit test* dan menghasilkan laporan *code coverage* menggunakan Jacoco (yang saat ini sudah mencapai 100%), serta memindai kode dari potensi *bug* atau *code smell* menggunakan SonarCloud. Hal ini memastikan bahwa setiap iterasi kode baru telah divalidasi kebenarannya.

Namun, untuk proses perilisan ke tahap *production* (PaaS Koyeb), implementasi saya saat ini belum memenuhi prinsip **Continuous Deployment** dengan sepenuhnya. Hal ini dikarenakan meskipun *codebase* sudah terintegrasi dan siap untuk dirilis (*deployable*), proses *deployment*-nya belum sepenuhnya otomatis. Saya masih perlu melakukan intervensi manual (menekan tombol *redeploy* di *dashboard* Koyeb) untuk memperbarui aplikasi yang *live*. Ke depannya, hal ini dapat ditingkatkan dengan memastikan konfigurasi *webhook/auto-deploy* dari GitHub ke Koyeb berjalan secara otomatis tanpa campur tangan developer.

# Reflection [module-3]

### SOLID Principles Implementation
Dalam pengerjaan modul ini, saya berusaha menerapkan prinsip SOLID agar struktur kode menjadi lebih modular, terorganisir, dan mudah dikembangkan kedepannya. Beberapa prinsip yang saya implementasikan antara lain:

* **Single Responsibility Principle (SRP)**: Saya memisahkan tanggung jawab *class* agar masing-masing hanya memiliki satu fokus fungsi. Contohnya, saya memisahkan `CarController` dari `ProductController` agar pengelolaan *routing* untuk entitas `Car` dan `Product` tidak bercampur dalam satu *class* yang sama.
* **Open-Closed Principle (OCP)**: Saya memanfaatkan abstraksi melalui *interface* pada lapisan *service*. Dengan pendekatan ini, jika kedepannya ada kebutuhan *development* logika bisnis baru, saya cukup membuat implementasi *class* baru tanpa harus memodifikasi kode lama yang sudah stabil.
* **Liskov Substitution Principle (LSP)**: Saya memastikan setiap *subclass* dapat menggantikan fungsi *parent class* tanpa merusak integritas sistem. Dalam implementasinya, saya menghilangkan dependensi yang tidak perlu antara `CarController` dan `ProductController` karena keduanya memiliki karakteristik fungsional yang berbeda.
* **Interface Segregation Principle (ISP)**: Saya memastikan *interface* yang dibuat bersifat spesifik. *Class* hanya mengimplementasikan *method* yang benar-benar dibutuhkan, sehingga tidak ada ketergantungan pada *method* yang tidak relevan dengan tanggung jawab *class* tersebut.
* **Dependency Inversion Principle (DIP)**: Saya menerapkan dependensi pada level abstraksi (*interface*), bukan pada implementasi konkret. Saat ini, *controller* bergantung pada `CarService` (*interface*) dan bukan langsung pada `CarServiceImpl`. Hal ini membuat sistem menjadi lebih *decoupled* dan mudah diuji.

### Advantages of SOLID Principles Application
Penerapan prinsip SOLID memberikan beberapa keuntungan signifikan terhadap kualitas dan skalabilitas *source code* pada proyek ini:

* **Kemudahan Testing**: Dengan penerapan **DIP**, saya dapat melakukan *unit testing* pada *controller* dengan jauh lebih mudah menggunakan teknik *mocking*. Saya tidak perlu lagi melakukan *setup* seluruh lapisan *database* hanya untuk memvalidasi logika pada level *controller*.
* **Minimalisir Side Effects**: Berkat **SRP**, setiap perubahan atau perbaikan *bug* pada fitur tertentu (seperti `Car`) tidak akan berdampak negatif pada stabilitas fitur lainnya. Cakupan modifikasi menjadi lebih terisolasi dan aman.
* **Skalabilitas yang Lebih Baik**: Melalui **OCP**, proses penambahan fitur baru di masa mendatang menjadi lebih efisien. Saya dapat langsung menambahkan modul atau *class* baru tanpa risiko merusak fungsionalitas lama di *environment production*.

### Disadvantages of Ignoring SOLID Principles
Sebaliknya, mengabaikan prinsip SOLID dapat memunculkan tumpukan *Technical Debt* yang akan menyulitkan proses *development* kedepannya:

* **Rigid Code**: Tanpa adanya **DIP**, perubahan pada satu komponen kecil (misalnya penyesuaian skema *database*) akan memaksa saya untuk mengubah banyak *class* lain secara manual. Hal ini membuat proses *maintenance* menjadi sangat melelahkan dan memakan waktu.
* **Fragile Code**: Jika tidak menerapkan **SRP**, sebuah *class* dapat berkembang terlalu besar dan menangani terlalu banyak *logic*. Modifikasi pada satu baris kode di fitur A berpotensi besar merusak fitur B karena *coupling* antar logika yang terlalu ketat.
* **Code Duplication**: Tanpa abstraksi yang terstruktur melalui **LSP** dan **ISP**, saya akan cenderung melakukan *copy-paste* potongan kode yang mirip di berbagai tempat. Hal ini jelas melanggar prinsip **DRY (Don't Repeat Yourself)** yang sudah saya terapkan di modul sebelumnya, serta meningkatkan risiko *human error* ketika ada pembaruan sistem.