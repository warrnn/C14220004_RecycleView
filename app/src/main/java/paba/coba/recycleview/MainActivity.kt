package paba.coba.recycleview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    private lateinit var _nama: MutableList<String>
    private lateinit var _karakter: MutableList<String>
    private lateinit var _deskripsi: MutableList<String>
    private lateinit var _gambar: MutableList<String>

    private var arrWayang = arrayListOf<wayang>()

    private lateinit var _rvWayang: RecyclerView

    fun siapkanData() {
        _nama = resources.getStringArray(R.array.namaWayang).toMutableList()
        _karakter = resources.getStringArray(R.array.karakterUtamaWayang).toMutableList()
        _deskripsi = resources.getStringArray(R.array.deskripsiWayang).toMutableList()
        _gambar = resources.getStringArray(R.array.gambarWayang).toMutableList()
    }

    fun tambahData() {
        arrWayang.clear()
        for (position in _nama.indices) {
            var data = wayang(
                _gambar[position],
                _nama[position],
                _karakter[position],
                _deskripsi[position]
            )
            arrWayang.add(data)
        }
    }

    fun tampilkanData() {
        _rvWayang.layoutManager = LinearLayoutManager(this)
//        _rvWayang.layoutManager = GridLayoutManager(this, 2)
//        _rvWayang.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        _rvWayang.adapter = adapterRecView(arrWayang)

        val adapterWayang = adapterRecView(arrWayang)
        _rvWayang.adapter = adapterWayang

        adapterWayang.setOnItemClickCallBack(object : adapterRecView.OnItemClickCallBack {
            override fun onItemClicked(data: wayang) {
//                Toast.makeText(this@MainActivity, data.nama, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, detWayang::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)
            }

            override fun delData(pos: Int) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("HAPUS DATA")
                    .setMessage("Apakah Benar Data " + _nama[pos] + " akan dihapus?")
                    .setPositiveButton(
                        "HAPUS",
                        { dialog, which ->
                            _nama.removeAt(pos)
                            _karakter.removeAt(pos)
                            _deskripsi.removeAt(pos)
                            _gambar.removeAt(pos)
                            tambahData()
                            tampilkanData()
                        }
                    )
                    .setNegativeButton(
                        "BATAL",
                        { dialog, which ->
                            Toast.makeText(
                                this@MainActivity,
                                "Data Batal Dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _rvWayang = findViewById<RecyclerView>(R.id.rvWayang)
        siapkanData()
        tambahData()
        tampilkanData()
    }
}