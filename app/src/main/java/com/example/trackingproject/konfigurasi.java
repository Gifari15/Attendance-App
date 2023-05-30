package com.example.trackingproject;

public class konfigurasi {
    //PEMBUATAN VARIABEL YANG MEMANGGIL FILE PHP DARI SERVER

    public static final String
            URL_ADD="https://tracking-v.000webhostapp.com/tracking/tambah.php";

    public static final String URL_GET_ALL = "https://tracking-v.000webhostapp.com/tracking/tampil.php";
    public static final String URL_GET_TTLSAKIT = "https://tracking-v.000webhostapp.com/tracking/ttlsakit.php?id_karyawan=";
    public static final String URL_GET_TTLIZIN = "https://tracking-v.000webhostapp.com/tracking/ttlizin.php?id_karyawan=";
    public static final String URL_GET_TTLCUTI = "https://tracking-v.000webhostapp.com/tracking/ttlcuti.php?id_karyawan=";
    public static final String URL_GET_BATASCUTI = "https://tracking-v.000webhostapp.com/tracking/batascuti.php";
    public static final String URL_GET_CUTIKHUSUS = "https://tracking-v.000webhostapp.com/tracking/cutikhusus.php?id_karyawan=";
    public static final String URL_GET_ALLK = "https://tracking-v.000webhostapp.com/tracking/tampilkaryawan.php";

    public static final String URL_GET_ASCAB = "https://tracking-v.000webhostapp.com/tracking/tampilabasc.php?id_karyawan=";

    public static final String URL_GET_EMP =
            "https://tracking-v.000webhostapp.com/tracking/tampildetail.php?id_karyawan=";
    public static final String URL_UPDATE_EMP =
            "https://tracking-v.000webhostapp.com/tracking/edit.php";
    public static final String URL_DELETE_EMP =
            "https://tracking-v.000webhostapp.com/tracking/hapus.php?id=";
    public static final String URL_GET_ABSEN_DETAIL = "https://tracking-v.000webhostapp.com/tracking/absendetail.php?id_karyawan=";

    public static final String URL_ABSEN_OUT = "https://tracking-v.000webhostapp.com/tracking/absenout.php";
    public static final String URL_ABSEN_DETAIL = "https://tracking-v.000webhostapp.com/tracking/detailabs.php?id_karyawan=";
    public static final String URL_GET_KARYAWAN_DETAIL= "https://tracking-v.000webhostapp.com/tracking/karyawandetail.php?id_karyawan=";
    public static final String URL_GET_COUNT_TPT = "https://tracking-v.000webhostapp.com/tracking/count_tpt.php?id_karyawan=";
    public static final String URL_GET_COUNT_TLT = "https://tracking-v.000webhostapp.com/tracking/count_tlt.php?id_karyawan=";
    public static final String URL_GET_COUNT_MAX = "https://tracking-v.000webhostapp.com/tracking/maxid.php";

    //VARIABEL YANG ISINYA SESUAI DENGAN NAMA TABEL

    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_IDK = "id_karyawan";
    public static final String KEY_EMP_NAMA = "nama";
    public static final String KEY_EMP_JABATAN = "jabatan";
    public static final String KEY_EMP_LATITUDE = "latitude";
    public static final String KEY_EMP_LONGTITUDE = "longitude";
    public static final String KEY_EMP_ALTITUDE = "altitude";
    public static final String KEY_EMP_ALAMAT = "alamat";
    public static final String KEY_EMP_GAMBAR = "gambar";
    public static final String KEY_EMP_TANGGAL = "jam_masuk";
    public static final String KEY_EMP_JAM = "jam";
    public static final String KEY_EMP_STATUS = "status";
    public static final String KEY_JAM_KELUAR = "jam_keluar";
    public static final String KEY_LOKASI_KELUAR = "lokasi_keluar";


    //JSON Tags

    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_IDK = "id_karyawan";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JABATAN = "jabatan";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_SISA = "sisa_cuti";

    //VARIABEL UNTUK TABEL ABSENSI
    public static final String TAG_TANGGAL = "jam_masuk";
    public static final String TAG_TIME = "jam";
    public static final String TAG_JAM_OUT = "jam_keluar";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TPT = "Tepat";
    public static final String TAG_TLT = "Telat";
    public static final String TAG_MAX = "idk";
    public static final String TAG_KAT = "kategori";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_HP = "hp";
    public static final String TAG_TTLCUTI = "total";
    public static final String TAG_CUTIKHUSUS = "cutikhusus";
    public static final String TAG_TTLIZIN = "totalizin";
    public static final String TAG_BATASCUTI = "batascuti";
    public static final String TAG_TTLSAKIT = "totalsakit";

    //VARIABEL UNTUK TABEL CUTI
    public static final String TAG_IDC = "id_cuti";
    public static final String TAG_ALASAN = "alasan";

    public static final String TAG_IDKC = "id_karyawan";
    public static final String TAG_NAMAC = "nama";
    public static final String TAG_MULAI = "mulai";
    public static final String TAG_AKHIR = "akhir";
    public static final String TAG_KET = "ket";
    public static final String TAG_STATUSC = "status";
    public static final String TAG_MK = "masuk_kerja";
    public static final String TAG_JML = "jml_cuti";


    //ID karyawan
    //emp merupakan singkatan dari Employee

    public static final String EMP_ID = "emp_id";
}
