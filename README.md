# Reflection [module-4]

### Test-Driven Development (TDD) Workflow Evaluation
Berdasarkan panduan *self-reflection* dari Percival (2017), alur kerja TDD (*Red-Green-Refactor*) yang saya terapkan pada modul ini terbukti sangat bermanfaat bagi proses *development*. Menulis tes terlebih dahulu (fase *Red*) memaksa saya untuk memikirkan spesifikasi, *behavior*, dan *interface* dari sebuah sistem (seperti `OrderRepository` dan `OrderService`) dari sudut pandang *client* sebelum memikirkan implementasi teknisnya.

Fase *Green* membantu saya untuk tetap fokus menulis kode secukupnya hanya agar tes tersebut *passed*, sehingga terhindar dari *over-engineering*. Selain itu, keberadaan unit test yang komprehensif bertindak sebagai *safety net* yang sangat esensial saat saya memasuki fase *Refactor*. Contohnya, ketika saya harus mengganti tipe data status dari *hardcoded string* menjadi Enum `OrderStatus`, saya dapat melakukan perubahan struktural tersebut dengan percaya diri karena jika ada fungsionalitas yang rusak, tes akan langsung gagal (*fail-fast*).

**Evaluasi untuk kedepannya:** Terkadang saya masih merasa kesulitan untuk menentukan seberapa spesifik atau seberapa luas cakupan sebuah unit test pada iterasi pertama. Untuk pengembangan selanjutnya, saya perlu melatih diri untuk memecah *requirements* menjadi skenario tes yang jauh lebih kecil dan atomik. Hal ini akan membuat siklus TDD menjadi lebih cepat dan mempermudah proses *debugging* apabila ada tes yang gagal.

### F.I.R.S.T. Principles in Unit Testing
Setelah mengevaluasi rangkaian unit test yang telah saya buat (terutama pada `OrderTest`, `OrderRepositoryTest`, dan `OrderServiceImplTest`), saya menyadari bahwa tes tersebut secara umum telah mengimplementasikan prinsip F.I.R.S.T. dengan cukup baik:

* **Fast**: Tes tereksekusi dengan sangat cepat (dalam hitungan milidetik) karena berjalan di memori lokal tanpa perlu melakukan koneksi ke sistem eksternal atau *database* fisik.
* **Independent**: Setiap *test case* berdiri sendiri dan tidak bergantung pada urutan eksekusi tes lainnya. Saya memanfaatkan anotasi `@BeforeEach` untuk mengatur ulang kondisi (*state*) objek sebelum tiap tes dimulai, sehingga menghindari *data pollution*.
* **Repeatable**: Dengan menggunakan *mocking framework* seperti Mockito pada lapisan *Service*, tes dapat dijalankan secara konsisten di *environment* manapun (lokal maupun CI/CD *pipeline*) dan selalu memberikan hasil yang sama.
* **Self-Validating**: Tes secara otomatis memvalidasi kebenarannya menggunakan *assertions* bawaan JUnit (seperti `assertEquals`, `assertTrue`, dan `assertThrows`). Tidak diperlukan pengecekan log atau output secara manual untuk mengetahui apakah tes lulus atau tidak.
* **Timely**: Karena saya mengadopsi pendekatan TDD, tes selalu ditulis secara tepat waktu (tepat sebelum kode produksi diimplementasikan), bukan sebagai aktivitas *afterthought* di akhir siklus pengembangan.