package zhet.board.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity.START
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.skydoves.colorpickerpreference.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import zhet.board.R
import zhet.board.view.CustomFlag
import zhet.board.view.DrawingView
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val PERMISSION_REQUEST = 1
        private const val ACTIVITY_REQUEST_SEND = 2
    }

    private lateinit var ad: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        drawer_layout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        Toast.makeText(this, R.string.press_back_to_get_toolbar, LENGTH_LONG).show()
        MobileAds.initialize(this, getString(R.string.app_id))
        ad = InterstitialAd(this)
        ad.adUnitId = getString(R.string.int_id)
        ad.loadAd(AdRequest.Builder().build())
    }

    override fun onBackPressed() {
        if (drawerLayout().isDrawerOpen(START)) {
            drawerLayout().closeDrawer(START)
        } else {
            drawerLayout().openDrawer(START)
        }
    }

    fun showPalette(item: MenuItem) {
        drawerLayout().closeDrawer(GravityCompat.START)
        val builder = ColorPickerDialog.Builder(this)
        builder.setTitle(R.string.pick_color)
        builder.setPreferenceName("color")
        builder.setFlagView(CustomFlag(this, R.layout.layout_flag))
        builder.setPositiveButton(getString(android.R.string.ok)) { colorEnvelope ->
            Log.i(TAG, "${colorEnvelope.color}")
            drawing_view.setColor(colorEnvelope.color)
        }
        builder.setNegativeButton(getString(android.R.string.cancel), { dialogInterface, _ -> dialogInterface.dismiss() })
        builder.show()

    }

    fun showSizePicker(item: MenuItem) {
        drawerLayout().closeDrawer(GravityCompat.START)
        val view = layoutInflater.inflate(R.layout.edt_size, null)

        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(android.R.string.ok, { dialog, _ ->
                    val edt = view.findViewById<EditText>(R.id.edt)
                    Log.i(TAG, "${edt.text}")
                    drawingView().setSize(edt.text.toString().toFloat())
                })
                .setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.dismiss() })
                .show()
    }

    fun exit(item: MenuItem) {
        if (ad.isLoaded) {
            ad.show()

        }
        finish()
    }

    fun erase(item: MenuItem) {
        val size = drawingView().getSize()
        val color = drawingView().getColor()
        setContentView(R.layout.activity_main)
        drawingView().setSize(size)
        drawingView().setColor(color)
        drawerLayout().setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
    }

    private fun drawingView() = findViewById<DrawingView>(R.id.drawing_view)

    private fun drawerLayout() = findViewById<DrawerLayout>(R.id.drawer_layout)

    fun share(item: MenuItem) {
        send()
    }

    private fun send() {
        val listener: DrawerLayout.DrawerListener = object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {
                drawing_view.isDrawingCacheEnabled = true
                val b = Bitmap.createBitmap(drawing_view.drawingCache)
                val file = File(cacheDir, "img.png")
                val stream = FileOutputStream(file)
                b.compress(Bitmap.CompressFormat.PNG, 90, stream)
                stream.close()
                val intent = Intent(ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this@MainActivity, "$packageName.provider",
                        file))
                intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
                drawerLayout().removeDrawerListener(this)
            }

            override fun onDrawerOpened(drawerView: View) {}
        }
        drawerLayout().addDrawerListener(listener)
        drawerLayout().closeDrawer(START)
    }
}

