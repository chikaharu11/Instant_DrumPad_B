package jp.chikaharu11.instant_drumpad_breakbeats

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.*
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.arthenica.mobileffmpeg.FFmpeg
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), CustomAdapterListener {

    private lateinit var mediaProjectionManager: MediaProjectionManager

    private val handler = Handler()
    private var audioName = ""
    var start = 0
    var stop = 0

    companion object {
        private const val READ_REQUEST_CODE2: Int = 43
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 41
        private const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 42
        private const val MEDIA_PROJECTION_REQUEST_CODE = 13
    }

    fun selectAudio() {
        val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fjp.chikaharu11.instant_drumpad_breakbeats%2Ffiles%2FMusic")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            type = "audio/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE2)
    }

    fun selectEX() {
        if (!isReadExternalStoragePermissionGranted()) {
            requestReadExternalStoragePermission()
        } else {
            tSoundList.clear()
            val audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor = contentResolver.query(audioUri, null, null, null, null)
            cursor!!.moveToFirst()
            val path: Array<String?> = arrayOfNulls(cursor.count)
            for (i in path.indices) {
                path[i] = cursor.getString(cursor.getColumnIndex("_data"))
                tSoundList.add(SoundList(path[i].toString()))
                cursor.moveToNext()
            }

            cursor.close()
        }
    }

    private lateinit var soundPool: SoundPool

    private lateinit var mp: MediaPlayer

    private lateinit var lmp: LoopMediaPlayer

    private lateinit var aCustomAdapter: CustomAdapter
    private lateinit var bCustomAdapter: CustomAdapter
    private lateinit var cCustomAdapter: CustomAdapter
    private lateinit var dCustomAdapter: CustomAdapter

    private lateinit var nCustomAdapter: CustomAdapter
    private lateinit var oCustomAdapter: CustomAdapter
    private lateinit var pCustomAdapter: CustomAdapter
    private lateinit var qCustomAdapter: CustomAdapter

    private lateinit var sCustomAdapter: CustomAdapter
    private lateinit var tCustomAdapter: CustomAdapter

    private lateinit var aSoundList: MutableList<SoundList>
    private lateinit var bSoundList: MutableList<SoundList>
    private lateinit var cSoundList: MutableList<SoundList>
    private lateinit var dSoundList: MutableList<SoundList>

    private lateinit var nSoundList: MutableList<SoundList>
    private lateinit var oSoundList: MutableList<SoundList>
    private lateinit var pSoundList: MutableList<SoundList>
    private lateinit var qSoundList: MutableList<SoundList>

    private lateinit var sSoundList: MutableList<SoundList>
    private lateinit var tSoundList: MutableList<SoundList>

    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0
    private var sound5 = 0
    private var sound6 = 0
    private var sound7 = 0
    private var sound8 = 0
    private var sound9 = 0
    private var sound10 = 0
    private var sound11 = 0
    private var sound12 = 0
    private var sound13 = 0
    private var sound14 = 0
    private var sound15 = 0
    private var sound16 = 0


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = "ca_snare01"
        textView2.text = "b_clhat_01"
        textView3.text = "b_fizz_01"
        textView4.text = "b_conga_01"
        textView5.text = "b_conga_02"
        textView6.text = "b_conga_03"
        textView7.text = "b_ophat_02"
        textView8.text = "ca_kick01"
        textView9.text = "ca_kick02"
        textView10.text = "break_b_01"
        textView11.text = "break_b_04"
        textView12.text = "chirper_a_01"
        textView13.text = "chirper_a_02"
        textView14.text = "goingbass_a_02"
        textView15.text = "goingbass_a_03"

        MobileAds.initialize(this) {}

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        aSoundList = arrayListOf(
            SoundList("boos_a_01.ogg"),
            SoundList("boos_a_02.ogg"),
            SoundList("boos_a_03.ogg"),
            SoundList("boos_a_04.ogg"),
            SoundList("boos_c_01.ogg"),
            SoundList("boos_c_02.ogg"),
            SoundList("boos_c_03.ogg"),
            SoundList("boos_c_04.ogg"),
            SoundList("boos_c1.ogg"),
            SoundList("boos_c2.ogg"),
            SoundList("boos_c3.ogg"),
            SoundList("boos_e_01.ogg"),
            SoundList("boos_e_02.ogg"),
            SoundList("boos_e_03.ogg"),
            SoundList("boos_e_04.ogg"),
            SoundList("boos_e1.ogg"),
            SoundList("boos_e2.ogg"),
            SoundList("boos_e3.ogg"),
            SoundList("boos_g_01.ogg"),
            SoundList("boos_g_02.ogg"),
            SoundList("boos_g_03.ogg"),
            SoundList("boos_g_04.ogg"),
            SoundList("boos_g_s1.ogg"),
            SoundList("boos_g_s2.ogg"),
            SoundList("boos_g_s3.ogg"),
            SoundList("break_ca_01.ogg"),
            SoundList("break_ca_02.ogg"),
            SoundList("break_ca_03.ogg"),
            SoundList("break_ca_04.ogg"),
            SoundList("break_ca_05.ogg"),
            SoundList("break_ca_06.ogg"),
            SoundList("break_cb_01.ogg"),
            SoundList("break_cb_02.ogg"),
            SoundList("break_cb_03.ogg"),
            SoundList("break_cb_04.ogg"),
            SoundList("break_cb_05.ogg"),
            SoundList("break_cb_06.ogg"),
            SoundList("ca_clhat01.ogg"),
            SoundList("ca_clhat02.ogg"),
            SoundList("ca_kick01.ogg"),
            SoundList("ca_kick02.ogg"),
            SoundList("ca_ophat01.ogg"),
            SoundList("ca_ophat02.ogg"),
            SoundList("ca_snare01.ogg"),
            SoundList("ca_snare02.ogg"),
            SoundList("ca_tamb01.ogg"),
            SoundList("ca_tamb02.ogg"),
            SoundList("cb_clhat01.ogg"),
            SoundList("cb_clhat02.ogg"),
            SoundList("cb_kick01.ogg"),
            SoundList("cb_kick02.ogg"),
            SoundList("cb_ophat01.ogg"),
            SoundList("cb_ophat02.ogg"),
            SoundList("cb_snare01.ogg"),
            SoundList("cb_snare02.ogg"),
            SoundList("chirper_a_01.ogg"),
            SoundList("chirper_a_02.ogg"),
            SoundList("chirper_a_03.ogg"),
            SoundList("chirper_a_04.ogg"),
            SoundList("chirper_c_01.ogg"),
            SoundList("chirper_c_02.ogg"),
            SoundList("chirper_c_03.ogg"),
            SoundList("chirper_c_04.ogg"),
            SoundList("chirper_e_01.ogg"),
            SoundList("chirper_e_02.ogg"),
            SoundList("chirper_e_03.ogg"),
            SoundList("chirper_e_04.ogg"),
            SoundList("chirper_g_01.ogg"),
            SoundList("chirper_g_02.ogg"),
            SoundList("chirper_g_03.ogg"),
            SoundList("chirper_g_04.ogg"),
            SoundList("worp_a_01.ogg"),
            SoundList("worp_a_02.ogg"),
            SoundList("worp_a_03.ogg"),
            SoundList("worp_a_04.ogg"),
            SoundList("worp_c_01.ogg"),
            SoundList("worp_c_02.ogg"),
            SoundList("worp_c_03.ogg"),
            SoundList("worp_c_04.ogg"),
            SoundList("worp_e_01.ogg"),
            SoundList("worp_e_02.ogg"),
            SoundList("worp_e_03.ogg"),
            SoundList("worp_e_04.ogg"),
            SoundList("worp_g_01.ogg"),
            SoundList("worp_g_02.ogg"),
            SoundList("worp_g_03.ogg"),
            SoundList("worp_g_04.ogg"),
        )

        bSoundList = arrayListOf(
            SoundList("break_a_01.ogg"),
            SoundList("break_a_02.ogg"),
            SoundList("break_a_03.ogg"),
            SoundList("break_a_04.ogg"),
            SoundList("break_a_05.ogg"),
            SoundList("break_a_06.ogg"),
            SoundList("break_a_07.ogg"),
            SoundList("break_a_08.ogg"),
            SoundList("break_a_09.ogg"),
            SoundList("break_a_10.ogg"),
            SoundList("breakfx_a_01.ogg"),
            SoundList("breakfx_a_02.ogg"),
            SoundList("breakfx_a_03.ogg"),
            SoundList("breakfx_a_04.ogg"),
            SoundList("breakfx_a_05.ogg"),
            SoundList("breakfx_a_06.ogg"),
            SoundList("breakfx_a_07.ogg"),
            SoundList("breakfx_a_08.ogg"),
            SoundList("breakfx_a_09.ogg"),
            SoundList("breakfx_a_10.ogg"),
            SoundList("goingbass_a_01.ogg"),
            SoundList("goingbass_a_02.ogg"),
            SoundList("goingbass_a_03.ogg"),
            SoundList("goingbass_a_04.ogg"),
            SoundList("goingbass_c_01.ogg"),
            SoundList("goingbass_c_02.ogg"),
            SoundList("goingbass_c_03.ogg"),
            SoundList("goingbass_c_04.ogg"),
            SoundList("goingbass_d_01.ogg"),
            SoundList("goingbass_d_02.ogg"),
            SoundList("goingbass_d_03.ogg"),
            SoundList("goingbass_d_04.ogg"),
            SoundList("goingbass_g_01.ogg"),
            SoundList("goingbass_g_02.ogg"),
            SoundList("goingbass_g_03.ogg"),
            SoundList("goingbass_g_04.ogg"),
            SoundList("goingbass_c1.ogg"),
            SoundList("goingbass_c2.ogg"),
            SoundList("goingbass_c3.ogg"),
            SoundList("goingbass_e1.ogg"),
            SoundList("goingbass_e2.ogg"),
            SoundList("goingbass_e3.ogg"),
            SoundList("goingbass_g_s1.ogg"),
            SoundList("goingbass_g_s2.ogg"),
            SoundList("goingbass_g_s3.ogg"),
            SoundList("peel_a_01.ogg"),
            SoundList("peel_a_02.ogg"),
            SoundList("peel_a_03.ogg"),
            SoundList("peel_a_04.ogg"),
            SoundList("peel_c_01.ogg"),
            SoundList("peel_c_02.ogg"),
            SoundList("peel_c_03.ogg"),
            SoundList("peel_c_04.ogg"),
            SoundList("peel_d_01.ogg"),
            SoundList("peel_d_02.ogg"),
            SoundList("peel_d_03.ogg"),
            SoundList("peel_d_04.ogg"),
            SoundList("peel_g_01.ogg"),
            SoundList("peel_g_02.ogg"),
            SoundList("peel_g_03.ogg"),
            SoundList("peel_g_04.ogg"),
            SoundList("spooch_a_01.ogg"),
            SoundList("spooch_a_02.ogg"),
            SoundList("spooch_a_03.ogg"),
            SoundList("spooch_a_04.ogg"),
            SoundList("spooch_a_05.ogg"),
            SoundList("spooch_a_06.ogg"),
            SoundList("spooch_a_07.ogg"),
            SoundList("spooch_a_08.ogg"),
            SoundList("spooch_b_01.ogg"),
            SoundList("spooch_b_02.ogg"),
            SoundList("spooch_b_03.ogg"),
            SoundList("spooch_b_04.ogg"),
            SoundList("spooch_b_05.ogg"),
            SoundList("spooch_b_06.ogg"),
            SoundList("spooch_b_07.ogg"),
            SoundList("spooch_b_08.ogg"),
            SoundList("synjun_a_01.ogg"),
            SoundList("synjun_a_02.ogg"),
            SoundList("synjun_a_03.ogg"),
            SoundList("synjun_a_04.ogg"),
            SoundList("synjun_c_01.ogg"),
            SoundList("synjun_c_02.ogg"),
            SoundList("synjun_c_03.ogg"),
            SoundList("synjun_c_04.ogg"),
            SoundList("synjun_d_01.ogg"),
            SoundList("synjun_d_02.ogg"),
            SoundList("synjun_d_03.ogg"),
            SoundList("synjun_d_04.ogg"),
            SoundList("synjun_g_01.ogg"),
            SoundList("synjun_g_02.ogg"),
            SoundList("synjun_g_03.ogg"),
            SoundList("synjun_g_04.ogg")
        )
        cSoundList = arrayListOf(
            SoundList("break_d_01.ogg"),
            SoundList("break_d_02.ogg"),
            SoundList("break_d_03.ogg"),
            SoundList("break_d_04.ogg"),
            SoundList("break_d_05.ogg"),
            SoundList("break_d_06.ogg"),
            SoundList("break_d_07.ogg"),
            SoundList("break_d_08.ogg"),
            SoundList("break_d_09.ogg"),
            SoundList("break_d_10.ogg"),
            SoundList("breakfx_d_01.ogg"),
            SoundList("breakfx_d_02.ogg"),
            SoundList("breakfx_d_03.ogg"),
            SoundList("breakfx_d_04.ogg"),
            SoundList("breakfx_d_05.ogg"),
            SoundList("breakfx_d_06.ogg"),
            SoundList("breakfx_d_07.ogg"),
            SoundList("breakfx_d_08.ogg"),
            SoundList("breakfx_d_09.ogg"),
            SoundList("breakfx_d_10.ogg"),
            SoundList("d_clhat01.ogg"),
            SoundList("d_clhat02.ogg"),
            SoundList("d_crash.ogg"),
            SoundList("d_kick01.ogg"),
            SoundList("d_kick02.ogg"),
            SoundList("d_ophat.ogg"),
            SoundList("d_ride01.ogg"),
            SoundList("d_ride02.ogg"),
            SoundList("d_ridebell.ogg"),
            SoundList("d_snare01.ogg"),
            SoundList("d_snare02.ogg"),
            SoundList("gnatter_a_01.ogg"),
            SoundList("gnatter_a_02.ogg"),
            SoundList("gnatter_a_03.ogg"),
            SoundList("gnatter_a_04.ogg"),
            SoundList("gnatter_c_01.ogg"),
            SoundList("gnatter_c_02.ogg"),
            SoundList("gnatter_c_03.ogg"),
            SoundList("gnatter_c_04.ogg"),
            SoundList("gnatter_e_01.ogg"),
            SoundList("gnatter_e_02.ogg"),
            SoundList("gnatter_e_03.ogg"),
            SoundList("gnatter_e_04.ogg"),
            SoundList("gnatter_g_01.ogg"),
            SoundList("gnatter_g_02.ogg"),
            SoundList("gnatter_g_03.ogg"),
            SoundList("gnatter_g_04.ogg"),
            SoundList("seerpad_a_01.ogg"),
            SoundList("seerpad_a_02.ogg"),
            SoundList("seerpad_a_03.ogg"),
            SoundList("seerpad_a_04.ogg"),
            SoundList("seerpad_c_01.ogg"),
            SoundList("seerpad_c_02.ogg"),
            SoundList("seerpad_c_03.ogg"),
            SoundList("seerpad_c_04.ogg"),
            SoundList("seerpad_e_01.ogg"),
            SoundList("seerpad_e_02.ogg"),
            SoundList("seerpad_e_03.ogg"),
            SoundList("seerpad_e_04.ogg"),
            SoundList("seerpad_g_01.ogg"),
            SoundList("seerpad_g_02.ogg"),
            SoundList("seerpad_g_03.ogg"),
            SoundList("seerpad_g_04.ogg"),
            SoundList("skbass_a_01.ogg"),
            SoundList("skbass_a_02.ogg"),
            SoundList("skbass_a_03.ogg"),
            SoundList("skbass_a_04.ogg"),
            SoundList("skbass_c_01.ogg"),
            SoundList("skbass_c_02.ogg"),
            SoundList("skbass_c_03.ogg"),
            SoundList("skbass_c_04.ogg"),
            SoundList("skbass_c1.ogg"),
            SoundList("skbass_c2.ogg"),
            SoundList("skbass_c3.ogg"),
            SoundList("skbass_e_01.ogg"),
            SoundList("skbass_e_02.ogg"),
            SoundList("skbass_e_03.ogg"),
            SoundList("skbass_e_04.ogg"),
            SoundList("skbass_e1.ogg"),
            SoundList("skbass_e2.ogg"),
            SoundList("skbass_e3.ogg"),
            SoundList("skbass_g_01.ogg"),
            SoundList("skbass_g_02.ogg"),
            SoundList("skbass_g_03.ogg"),
            SoundList("skbass_g_04.ogg"),
            SoundList("skbass_g_s1.ogg"),
            SoundList("skbass_g_s2.ogg"),
            SoundList("skbass_g_s3.ogg")
        )
        dSoundList = arrayListOf(
            SoundList("b_bongo.ogg"),
            SoundList("b_clhat_01.ogg"),
            SoundList("b_clhat_02.ogg"),
            SoundList("b_conga_01.ogg"),
            SoundList("b_conga_02.ogg"),
            SoundList("b_conga_03.ogg"),
            SoundList("b_fizz_01.ogg"),
            SoundList("b_fizz_02.ogg"),
            SoundList("b_fizz_03.ogg"),
            SoundList("b_kick_01.ogg"),
            SoundList("b_kick_02.ogg"),
            SoundList("b_ophat_02.ogg"),
            SoundList("b_ophat01.ogg"),
            SoundList("b_ride_01.ogg"),
            SoundList("b_ride_02.ogg"),
            SoundList("b_ride_03.ogg"),
            SoundList("b_shaker.ogg"),
            SoundList("b_snare_01.ogg"),
            SoundList("b_snare_02.ogg"),
            SoundList("b_snare_03.ogg"),
            SoundList("b_tamb_01.ogg"),
            SoundList("b_tamb_02.ogg"),
            SoundList("b_tamb_03.ogg"),
            SoundList("b_woodblock.ogg"),
            SoundList("bludge_a_01.ogg"),
            SoundList("bludge_a_02.ogg"),
            SoundList("bludge_a_03.ogg"),
            SoundList("bludge_a_04.ogg"),
            SoundList("bludge_c_01.ogg"),
            SoundList("bludge_c_02.ogg"),
            SoundList("bludge_c_03.ogg"),
            SoundList("bludge_c_04.ogg"),
            SoundList("bludge_e_01.ogg"),
            SoundList("bludge_e_02.ogg"),
            SoundList("bludge_e_03.ogg"),
            SoundList("bludge_e_04.ogg"),
            SoundList("bludge_g_01.ogg"),
            SoundList("bludge_g_02.ogg"),
            SoundList("bludge_g_03.ogg"),
            SoundList("bludge_g_04.ogg"),
            SoundList("bludge_c1.ogg"),
            SoundList("bludge_c2.ogg"),
            SoundList("bludge_c3.ogg"),
            SoundList("bludge_e1.ogg"),
            SoundList("bludge_e2.ogg"),
            SoundList("bludge_e3.ogg"),
            SoundList("bludge_g_s1.ogg"),
            SoundList("bludge_g_s2.ogg"),
            SoundList("bludge_g_s3.ogg"),
            SoundList("break_b_01.ogg"),
            SoundList("break_b_02.ogg"),
            SoundList("break_b_03.ogg"),
            SoundList("break_b_04.ogg"),
            SoundList("break_b_05.ogg"),
            SoundList("break_b_06.ogg"),
            SoundList("break_b_07.ogg"),
            SoundList("break_b_08.ogg"),
            SoundList("break_b_09.ogg"),
            SoundList("break_b_10.ogg"),
            SoundList("halema_a_01.ogg"),
            SoundList("halema_a_02.ogg"),
            SoundList("halema_a_03.ogg"),
            SoundList("halema_c_01.ogg"),
            SoundList("halema_c_02.ogg"),
            SoundList("halema_c_03.ogg"),
            SoundList("halema_e_01.ogg"),
            SoundList("halema_e_02.ogg"),
            SoundList("halema_e_03.ogg"),
            SoundList("halema_g_01.ogg"),
            SoundList("halema_g_02.ogg"),
            SoundList("halema_g_03.ogg"),
            SoundList("thereman_a_01.ogg"),
            SoundList("thereman_a_02.ogg"),
            SoundList("thereman_a_03.ogg"),
            SoundList("thereman_a_04.ogg"),
            SoundList("thereman_c_01.ogg"),
            SoundList("thereman_c_02.ogg"),
            SoundList("thereman_c_03.ogg"),
            SoundList("thereman_c_04.ogg"),
            SoundList("thereman_e_01.ogg"),
            SoundList("thereman_e_02.ogg"),
            SoundList("thereman_e_03.ogg"),
            SoundList("thereman_e_04.ogg"),
            SoundList("thereman_g_01.ogg"),
            SoundList("thereman_g_02.ogg"),
            SoundList("thereman_g_03.ogg"),
            SoundList("thereman_g_04.ogg")
        )

        nSoundList = arrayListOf(
            SoundList("boos_a_01.ogg"),
            SoundList("boos_a_02.ogg"),
            SoundList("boos_a_03.ogg"),
            SoundList("boos_a_04.ogg"),
            SoundList("boos_c_01.ogg"),
            SoundList("boos_c_02.ogg"),
            SoundList("boos_c_03.ogg"),
            SoundList("boos_c_04.ogg"),
            SoundList("boos_c1.ogg"),
            SoundList("boos_c2.ogg"),
            SoundList("boos_c3.ogg"),
            SoundList("boos_e_01.ogg"),
            SoundList("boos_e_02.ogg"),
            SoundList("boos_e_03.ogg"),
            SoundList("boos_e_04.ogg"),
            SoundList("boos_e1.ogg"),
            SoundList("boos_e2.ogg"),
            SoundList("boos_e3.ogg"),
            SoundList("boos_g_01.ogg"),
            SoundList("boos_g_02.ogg"),
            SoundList("boos_g_03.ogg"),
            SoundList("boos_g_04.ogg"),
            SoundList("boos_g_s1.ogg"),
            SoundList("boos_g_s2.ogg"),
            SoundList("boos_g_s3.ogg"),
            SoundList("break_ca_01.ogg"),
            SoundList("break_ca_02.ogg"),
            SoundList("break_ca_03.ogg"),
            SoundList("break_ca_04.ogg"),
            SoundList("break_ca_05.ogg"),
            SoundList("break_ca_06.ogg"),
            SoundList("break_cb_01.ogg"),
            SoundList("break_cb_02.ogg"),
            SoundList("break_cb_03.ogg"),
            SoundList("break_cb_04.ogg"),
            SoundList("break_cb_05.ogg"),
            SoundList("break_cb_06.ogg"),
            SoundList("ca_clhat01.ogg"),
            SoundList("ca_clhat02.ogg"),
            SoundList("ca_kick01.ogg"),
            SoundList("ca_kick02.ogg"),
            SoundList("ca_ophat01.ogg"),
            SoundList("ca_ophat02.ogg"),
            SoundList("ca_snare01.ogg"),
            SoundList("ca_snare02.ogg"),
            SoundList("ca_tamb01.ogg"),
            SoundList("ca_tamb02.ogg"),
            SoundList("cb_clhat01.ogg"),
            SoundList("cb_clhat02.ogg"),
            SoundList("cb_kick01.ogg"),
            SoundList("cb_kick02.ogg"),
            SoundList("cb_ophat01.ogg"),
            SoundList("cb_ophat02.ogg"),
            SoundList("cb_snare01.ogg"),
            SoundList("cb_snare02.ogg"),
            SoundList("chirper_a_01.ogg"),
            SoundList("chirper_a_02.ogg"),
            SoundList("chirper_a_03.ogg"),
            SoundList("chirper_a_04.ogg"),
            SoundList("chirper_c_01.ogg"),
            SoundList("chirper_c_02.ogg"),
            SoundList("chirper_c_03.ogg"),
            SoundList("chirper_c_04.ogg"),
            SoundList("chirper_e_01.ogg"),
            SoundList("chirper_e_02.ogg"),
            SoundList("chirper_e_03.ogg"),
            SoundList("chirper_e_04.ogg"),
            SoundList("chirper_g_01.ogg"),
            SoundList("chirper_g_02.ogg"),
            SoundList("chirper_g_03.ogg"),
            SoundList("chirper_g_04.ogg"),
            SoundList("worp_a_01.ogg"),
            SoundList("worp_a_02.ogg"),
            SoundList("worp_a_03.ogg"),
            SoundList("worp_a_04.ogg"),
            SoundList("worp_c_01.ogg"),
            SoundList("worp_c_02.ogg"),
            SoundList("worp_c_03.ogg"),
            SoundList("worp_c_04.ogg"),
            SoundList("worp_e_01.ogg"),
            SoundList("worp_e_02.ogg"),
            SoundList("worp_e_03.ogg"),
            SoundList("worp_e_04.ogg"),
            SoundList("worp_g_01.ogg"),
            SoundList("worp_g_02.ogg"),
            SoundList("worp_g_03.ogg"),
            SoundList("worp_g_04.ogg"),
        )

        oSoundList = arrayListOf(
            SoundList("break_a_01.ogg"),
            SoundList("break_a_02.ogg"),
            SoundList("break_a_03.ogg"),
            SoundList("break_a_04.ogg"),
            SoundList("break_a_05.ogg"),
            SoundList("break_a_06.ogg"),
            SoundList("break_a_07.ogg"),
            SoundList("break_a_08.ogg"),
            SoundList("break_a_09.ogg"),
            SoundList("break_a_10.ogg"),
            SoundList("breakfx_a_01.ogg"),
            SoundList("breakfx_a_02.ogg"),
            SoundList("breakfx_a_03.ogg"),
            SoundList("breakfx_a_04.ogg"),
            SoundList("breakfx_a_05.ogg"),
            SoundList("breakfx_a_06.ogg"),
            SoundList("breakfx_a_07.ogg"),
            SoundList("breakfx_a_08.ogg"),
            SoundList("breakfx_a_09.ogg"),
            SoundList("breakfx_a_10.ogg"),
            SoundList("goingbass_a_01.ogg"),
            SoundList("goingbass_a_02.ogg"),
            SoundList("goingbass_a_03.ogg"),
            SoundList("goingbass_a_04.ogg"),
            SoundList("goingbass_c_01.ogg"),
            SoundList("goingbass_c_02.ogg"),
            SoundList("goingbass_c_03.ogg"),
            SoundList("goingbass_c_04.ogg"),
            SoundList("goingbass_d_01.ogg"),
            SoundList("goingbass_d_02.ogg"),
            SoundList("goingbass_d_03.ogg"),
            SoundList("goingbass_d_04.ogg"),
            SoundList("goingbass_g_01.ogg"),
            SoundList("goingbass_g_02.ogg"),
            SoundList("goingbass_g_03.ogg"),
            SoundList("goingbass_g_04.ogg"),
            SoundList("goingbass_c1.ogg"),
            SoundList("goingbass_c2.ogg"),
            SoundList("goingbass_c3.ogg"),
            SoundList("goingbass_e1.ogg"),
            SoundList("goingbass_e2.ogg"),
            SoundList("goingbass_e3.ogg"),
            SoundList("goingbass_g_s1.ogg"),
            SoundList("goingbass_g_s2.ogg"),
            SoundList("goingbass_g_s3.ogg"),
            SoundList("peel_a_01.ogg"),
            SoundList("peel_a_02.ogg"),
            SoundList("peel_a_03.ogg"),
            SoundList("peel_a_04.ogg"),
            SoundList("peel_c_01.ogg"),
            SoundList("peel_c_02.ogg"),
            SoundList("peel_c_03.ogg"),
            SoundList("peel_c_04.ogg"),
            SoundList("peel_d_01.ogg"),
            SoundList("peel_d_02.ogg"),
            SoundList("peel_d_03.ogg"),
            SoundList("peel_d_04.ogg"),
            SoundList("peel_g_01.ogg"),
            SoundList("peel_g_02.ogg"),
            SoundList("peel_g_03.ogg"),
            SoundList("peel_g_04.ogg"),
            SoundList("spooch_a_01.ogg"),
            SoundList("spooch_a_02.ogg"),
            SoundList("spooch_a_03.ogg"),
            SoundList("spooch_a_04.ogg"),
            SoundList("spooch_a_05.ogg"),
            SoundList("spooch_a_06.ogg"),
            SoundList("spooch_a_07.ogg"),
            SoundList("spooch_a_08.ogg"),
            SoundList("spooch_b_01.ogg"),
            SoundList("spooch_b_02.ogg"),
            SoundList("spooch_b_03.ogg"),
            SoundList("spooch_b_04.ogg"),
            SoundList("spooch_b_05.ogg"),
            SoundList("spooch_b_06.ogg"),
            SoundList("spooch_b_07.ogg"),
            SoundList("spooch_b_08.ogg"),
            SoundList("synjun_a_01.ogg"),
            SoundList("synjun_a_02.ogg"),
            SoundList("synjun_a_03.ogg"),
            SoundList("synjun_a_04.ogg"),
            SoundList("synjun_c_01.ogg"),
            SoundList("synjun_c_02.ogg"),
            SoundList("synjun_c_03.ogg"),
            SoundList("synjun_c_04.ogg"),
            SoundList("synjun_d_01.ogg"),
            SoundList("synjun_d_02.ogg"),
            SoundList("synjun_d_03.ogg"),
            SoundList("synjun_d_04.ogg"),
            SoundList("synjun_g_01.ogg"),
            SoundList("synjun_g_02.ogg"),
            SoundList("synjun_g_03.ogg"),
            SoundList("synjun_g_04.ogg")
        )
        pSoundList = arrayListOf(
            SoundList("break_d_01.ogg"),
            SoundList("break_d_02.ogg"),
            SoundList("break_d_03.ogg"),
            SoundList("break_d_04.ogg"),
            SoundList("break_d_05.ogg"),
            SoundList("break_d_06.ogg"),
            SoundList("break_d_07.ogg"),
            SoundList("break_d_08.ogg"),
            SoundList("break_d_09.ogg"),
            SoundList("break_d_10.ogg"),
            SoundList("breakfx_d_01.ogg"),
            SoundList("breakfx_d_02.ogg"),
            SoundList("breakfx_d_03.ogg"),
            SoundList("breakfx_d_04.ogg"),
            SoundList("breakfx_d_05.ogg"),
            SoundList("breakfx_d_06.ogg"),
            SoundList("breakfx_d_07.ogg"),
            SoundList("breakfx_d_08.ogg"),
            SoundList("breakfx_d_09.ogg"),
            SoundList("breakfx_d_10.ogg"),
            SoundList("d_clhat01.ogg"),
            SoundList("d_clhat02.ogg"),
            SoundList("d_crash.ogg"),
            SoundList("d_kick01.ogg"),
            SoundList("d_kick02.ogg"),
            SoundList("d_ophat.ogg"),
            SoundList("d_ride01.ogg"),
            SoundList("d_ride02.ogg"),
            SoundList("d_ridebell.ogg"),
            SoundList("d_snare01.ogg"),
            SoundList("d_snare02.ogg"),
            SoundList("gnatter_a_01.ogg"),
            SoundList("gnatter_a_02.ogg"),
            SoundList("gnatter_a_03.ogg"),
            SoundList("gnatter_a_04.ogg"),
            SoundList("gnatter_c_01.ogg"),
            SoundList("gnatter_c_02.ogg"),
            SoundList("gnatter_c_03.ogg"),
            SoundList("gnatter_c_04.ogg"),
            SoundList("gnatter_e_01.ogg"),
            SoundList("gnatter_e_02.ogg"),
            SoundList("gnatter_e_03.ogg"),
            SoundList("gnatter_e_04.ogg"),
            SoundList("gnatter_g_01.ogg"),
            SoundList("gnatter_g_02.ogg"),
            SoundList("gnatter_g_03.ogg"),
            SoundList("gnatter_g_04.ogg"),
            SoundList("seerpad_a_01.ogg"),
            SoundList("seerpad_a_02.ogg"),
            SoundList("seerpad_a_03.ogg"),
            SoundList("seerpad_a_04.ogg"),
            SoundList("seerpad_c_01.ogg"),
            SoundList("seerpad_c_02.ogg"),
            SoundList("seerpad_c_03.ogg"),
            SoundList("seerpad_c_04.ogg"),
            SoundList("seerpad_e_01.ogg"),
            SoundList("seerpad_e_02.ogg"),
            SoundList("seerpad_e_03.ogg"),
            SoundList("seerpad_e_04.ogg"),
            SoundList("seerpad_g_01.ogg"),
            SoundList("seerpad_g_02.ogg"),
            SoundList("seerpad_g_03.ogg"),
            SoundList("seerpad_g_04.ogg"),
            SoundList("skbass_a_01.ogg"),
            SoundList("skbass_a_02.ogg"),
            SoundList("skbass_a_03.ogg"),
            SoundList("skbass_a_04.ogg"),
            SoundList("skbass_c_01.ogg"),
            SoundList("skbass_c_02.ogg"),
            SoundList("skbass_c_03.ogg"),
            SoundList("skbass_c_04.ogg"),
            SoundList("skbass_c1.ogg"),
            SoundList("skbass_c2.ogg"),
            SoundList("skbass_c3.ogg"),
            SoundList("skbass_e_01.ogg"),
            SoundList("skbass_e_02.ogg"),
            SoundList("skbass_e_03.ogg"),
            SoundList("skbass_e_04.ogg"),
            SoundList("skbass_e1.ogg"),
            SoundList("skbass_e2.ogg"),
            SoundList("skbass_e3.ogg"),
            SoundList("skbass_g_01.ogg"),
            SoundList("skbass_g_02.ogg"),
            SoundList("skbass_g_03.ogg"),
            SoundList("skbass_g_04.ogg"),
            SoundList("skbass_g_s1.ogg"),
            SoundList("skbass_g_s2.ogg"),
            SoundList("skbass_g_s3.ogg")
        )
        qSoundList = arrayListOf(
            SoundList("b_bongo.ogg"),
            SoundList("b_clhat_01.ogg"),
            SoundList("b_clhat_02.ogg"),
            SoundList("b_conga_01.ogg"),
            SoundList("b_conga_02.ogg"),
            SoundList("b_conga_03.ogg"),
            SoundList("b_fizz_01.ogg"),
            SoundList("b_fizz_02.ogg"),
            SoundList("b_fizz_03.ogg"),
            SoundList("b_kick_01.ogg"),
            SoundList("b_kick_02.ogg"),
            SoundList("b_ophat_02.ogg"),
            SoundList("b_ophat01.ogg"),
            SoundList("b_ride_01.ogg"),
            SoundList("b_ride_02.ogg"),
            SoundList("b_ride_03.ogg"),
            SoundList("b_shaker.ogg"),
            SoundList("b_snare_01.ogg"),
            SoundList("b_snare_02.ogg"),
            SoundList("b_snare_03.ogg"),
            SoundList("b_tamb_01.ogg"),
            SoundList("b_tamb_02.ogg"),
            SoundList("b_tamb_03.ogg"),
            SoundList("b_woodblock.ogg"),
            SoundList("bludge_a_01.ogg"),
            SoundList("bludge_a_02.ogg"),
            SoundList("bludge_a_03.ogg"),
            SoundList("bludge_a_04.ogg"),
            SoundList("bludge_c_01.ogg"),
            SoundList("bludge_c_02.ogg"),
            SoundList("bludge_c_03.ogg"),
            SoundList("bludge_c_04.ogg"),
            SoundList("bludge_e_01.ogg"),
            SoundList("bludge_e_02.ogg"),
            SoundList("bludge_e_03.ogg"),
            SoundList("bludge_e_04.ogg"),
            SoundList("bludge_g_01.ogg"),
            SoundList("bludge_g_02.ogg"),
            SoundList("bludge_g_03.ogg"),
            SoundList("bludge_g_04.ogg"),
            SoundList("bludge_c1.ogg"),
            SoundList("bludge_c2.ogg"),
            SoundList("bludge_c3.ogg"),
            SoundList("bludge_e1.ogg"),
            SoundList("bludge_e2.ogg"),
            SoundList("bludge_e3.ogg"),
            SoundList("bludge_g_s1.ogg"),
            SoundList("bludge_g_s2.ogg"),
            SoundList("bludge_g_s3.ogg"),
            SoundList("break_b_01.ogg"),
            SoundList("break_b_02.ogg"),
            SoundList("break_b_03.ogg"),
            SoundList("break_b_04.ogg"),
            SoundList("break_b_05.ogg"),
            SoundList("break_b_06.ogg"),
            SoundList("break_b_07.ogg"),
            SoundList("break_b_08.ogg"),
            SoundList("break_b_09.ogg"),
            SoundList("break_b_10.ogg"),
            SoundList("halema_a_01.ogg"),
            SoundList("halema_a_02.ogg"),
            SoundList("halema_a_03.ogg"),
            SoundList("halema_c_01.ogg"),
            SoundList("halema_c_02.ogg"),
            SoundList("halema_c_03.ogg"),
            SoundList("halema_e_01.ogg"),
            SoundList("halema_e_02.ogg"),
            SoundList("halema_e_03.ogg"),
            SoundList("halema_g_01.ogg"),
            SoundList("halema_g_02.ogg"),
            SoundList("halema_g_03.ogg"),
            SoundList("thereman_a_01.ogg"),
            SoundList("thereman_a_02.ogg"),
            SoundList("thereman_a_03.ogg"),
            SoundList("thereman_a_04.ogg"),
            SoundList("thereman_c_01.ogg"),
            SoundList("thereman_c_02.ogg"),
            SoundList("thereman_c_03.ogg"),
            SoundList("thereman_c_04.ogg"),
            SoundList("thereman_e_01.ogg"),
            SoundList("thereman_e_02.ogg"),
            SoundList("thereman_e_03.ogg"),
            SoundList("thereman_e_04.ogg"),
            SoundList("thereman_g_01.ogg"),
            SoundList("thereman_g_02.ogg"),
            SoundList("thereman_g_03.ogg"),
            SoundList("thereman_g_04.ogg")
        )

        sSoundList = arrayListOf()
        tSoundList = arrayListOf()

        val listView = findViewById<ListView>(R.id.list_view)

        aCustomAdapter = CustomAdapter(this, aSoundList, this)
        bCustomAdapter = CustomAdapter(this, bSoundList, this)
        cCustomAdapter = CustomAdapter(this, cSoundList, this)
        dCustomAdapter = CustomAdapter(this, dSoundList, this)
        nCustomAdapter = CustomAdapter(this, nSoundList, this)
        oCustomAdapter = CustomAdapter(this, oSoundList, this)
        pCustomAdapter = CustomAdapter(this, pSoundList, this)
        qCustomAdapter = CustomAdapter(this, qSoundList, this)
        sCustomAdapter = CustomAdapter(this, sSoundList, this)
        tCustomAdapter = CustomAdapter(this, tSoundList, this)

        listView.adapter = aCustomAdapter

        mp = MediaPlayer()

        supportActionBar?.title ="break_a_01"


            val audioUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            val cursor = contentResolver.query(audioUri, null, null, null, null)
            cursor!!.moveToFirst()
            val path: Array<String?> = arrayOfNulls(cursor.count)
            for (i in path.indices) {
                path[i] = cursor.getString(cursor.getColumnIndex("_data"))
                sSoundList.add(SoundList(path[i].toString()))
                cursor.moveToNext()
            }

            cursor.close()


        val meSpinner = findViewById<Spinner>(R.id.menu_spinner)

        val adapter3 = ArrayAdapter.createFromResource(this, R.array.spinnerItems, android.R.layout.simple_spinner_item)

        adapter3.setDropDownViewResource(R.layout.custom_spinner_dropdown)



        meSpinner.adapter = adapter3


        meSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
            ) {
                if (!meSpinner.isFocusable) {
                    meSpinner.isFocusable = true
                    return
                }

                val soundListView = findViewById<ListView>(R.id.list_view)

                when(position){
                    0 -> {
                        radioButton19.performClick()
                        soundListView.adapter = aCustomAdapter
                        aCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    1 -> {
                        radioButton19.performClick()
                        soundListView.adapter = bCustomAdapter
                        bCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    2 -> {
                        radioButton19.performClick()
                        soundListView.adapter = cCustomAdapter
                        cCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    3 -> {
                        radioButton19.performClick()
                        soundListView.adapter = dCustomAdapter
                        dCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    4 -> {
                        radioButton18.performClick()
                        soundListView.adapter = sCustomAdapter
                        sCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    5 -> {
                        selectEX()
                        radioButton18.performClick()
                        soundListView.adapter = tCustomAdapter
                        tCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    6 -> selectAudio()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        meSpinner.isFocusable = false


        val audioAttributes = AudioAttributes.Builder()

                .setUsage(AudioAttributes.USAGE_GAME)

                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

        soundPool = SoundPool.Builder()

                .setAudioAttributes(audioAttributes)

                .setMaxStreams(20)
                .build()

        sound1 = soundPool.load(assets.openFd("ca_snare01.ogg"), 1)

        sound2 = soundPool.load(assets.openFd("b_clhat_01.ogg"), 1)

        sound3 = soundPool.load(assets.openFd("b_fizz_01.ogg"), 1)

        sound4 = soundPool.load(assets.openFd("b_conga_01.ogg"), 1)

        sound5 = soundPool.load(assets.openFd("b_conga_02.ogg"), 1)

        sound6 = soundPool.load(assets.openFd("b_conga_03.ogg"), 1)

        sound7 = soundPool.load(assets.openFd("b_ophat_02.ogg"), 1)

        sound8 = soundPool.load(assets.openFd("ca_kick01.ogg"), 1)

        sound9 = soundPool.load(assets.openFd("ca_kick02.ogg"), 1)

        sound10 = soundPool.load(assets.openFd("break_b_01.ogg"), 1)

        sound11 = soundPool.load(assets.openFd("break_b_04.ogg"), 1)

        sound12 = soundPool.load(assets.openFd("chirper_a_01.ogg"), 1)

        sound13 = soundPool.load(assets.openFd("chirper_a_02.ogg"), 1)

        sound14 = soundPool.load(assets.openFd("goingbass_a_02.ogg"), 1)

        sound15 = soundPool.load(assets.openFd("goingbass_a_03.ogg"), 1)

        lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://" + packageName + "/raw/" + R.raw.break_a_01))


        imageView.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound1, 1.0f, 1.0f, 1, 0, 1.0f)
                }
                false
        }

        imageView2.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound2, 1.0f, 1.0f, 1, 0, 1.0f)
                }
                false
        }

        imageView3.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound3, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView4.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound4, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView5.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound5, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView6.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound6, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView7.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound7, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView8.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound8, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView9.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound9, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false

        }

        imageView10.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound10, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView11.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound11, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView12.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound12, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView13.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound13, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView14.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound14, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }

        imageView15.setOnTouchListener { _, event ->
            if (listView.isVisible) {
                listView.visibility = View.INVISIBLE
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(sound15, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                false
        }


        imageView.setOnLongClickListener {
            radioButton.performClick()
            meSpinner.performClick()
            true
        }
        imageView2.setOnLongClickListener {
            radioButton2.performClick()
            meSpinner.performClick()
            true
        }
        imageView3.setOnLongClickListener {
            radioButton3.performClick()
            meSpinner.performClick()
            true
        }
        imageView4.setOnLongClickListener {
            radioButton4.performClick()
            meSpinner.performClick()
            true
        }
        imageView5.setOnLongClickListener {
            radioButton5.performClick()
            meSpinner.performClick()
            true
        }
        imageView6.setOnLongClickListener {
            radioButton6.performClick()
            meSpinner.performClick()
            true
        }
        imageView7.setOnLongClickListener {
            radioButton7.performClick()
            meSpinner.performClick()
            true
        }
        imageView8.setOnLongClickListener {
            radioButton8.performClick()
            meSpinner.performClick()
            true
        }
        imageView9.setOnLongClickListener {
            radioButton9.performClick()
            meSpinner.performClick()
            true
        }
        imageView10.setOnLongClickListener {
            radioButton10.performClick()
            meSpinner.performClick()
            true
        }
        imageView11.setOnLongClickListener {
            radioButton11.performClick()
            meSpinner.performClick()
            true
        }
        imageView12.setOnLongClickListener {
            radioButton12.performClick()
            meSpinner.performClick()
            true
        }
        imageView13.setOnLongClickListener {
            radioButton13.performClick()
            meSpinner.performClick()
            true
        }
        imageView14.setOnLongClickListener {
            radioButton14.performClick()
            meSpinner.performClick()
            true
        }
        imageView15.setOnLongClickListener {
            radioButton15.performClick()
            meSpinner.performClick()
            true
        }
    }

    override fun clicked(soundList: SoundList) {
        sound16 = if (radioButton18.isChecked) {
            soundPool.load(soundList.name, 1)
        } else {
            soundPool.load(assets.openFd(soundList.name), 1)
        }
            soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f)
            }
    }

    override fun clicked2(soundList: SoundList) {
        when {
            radioButton.isChecked && radioButton18.isChecked -> {
                imageView.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound1 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton2.isChecked && radioButton18.isChecked -> {
                imageView2.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView2.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound2 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView2.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton3.isChecked && radioButton18.isChecked -> {
                imageView3.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView3.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound3 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView3.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton4.isChecked && radioButton18.isChecked -> {
                imageView4.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView4.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound4 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView4.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton5.isChecked && radioButton18.isChecked -> {
                imageView5.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView5.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound5 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView5.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton6.isChecked && radioButton18.isChecked -> {
                imageView6.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView6.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound6 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView6.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton7.isChecked && radioButton18.isChecked -> {
                imageView7.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView7.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound7 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView7.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton8.isChecked && radioButton18.isChecked -> {
                imageView8.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView8.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound8 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView8.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton9.isChecked && radioButton18.isChecked -> {
                imageView9.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView9.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound9 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView9.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton10.isChecked && radioButton18.isChecked -> {
                imageView10.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView10.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound10 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView10.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton11.isChecked && radioButton18.isChecked -> {
                imageView11.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView11.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound11 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView11.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton12.isChecked && radioButton18.isChecked -> {
                imageView12.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView12.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound12 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView12.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton13.isChecked && radioButton18.isChecked -> {
                imageView13.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView13.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound13 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView13.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton14.isChecked && radioButton18.isChecked -> {
                imageView14.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView14.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound14 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView14.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton15.isChecked && radioButton18.isChecked -> {
                imageView15.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView15.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound15 = soundPool.load(soundList.name, 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView15.text = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
            }
            radioButton16.isChecked && radioButton18.isChecked -> {
                lmp.release()
                lmp = LoopMediaPlayer(this@MainActivity, Uri.parse(soundList.name))
                supportActionBar?.title = soundList.name.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
            }
            radioButton.isChecked && radioButton19.isChecked -> {
                imageView.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound1 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton2.isChecked && radioButton19.isChecked -> {
                imageView2.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView2.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound2 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView2.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton3.isChecked && radioButton19.isChecked -> {
                imageView3.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView3.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound3 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView3.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton4.isChecked && radioButton19.isChecked -> {
                imageView4.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView4.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound4 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView4.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton5.isChecked && radioButton19.isChecked -> {
                imageView5.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView5.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound5 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView5.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton6.isChecked && radioButton19.isChecked -> {
                imageView6.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView6.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound6 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView6.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton7.isChecked && radioButton19.isChecked -> {
                imageView7.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView7.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound7 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView7.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton8.isChecked && radioButton19.isChecked -> {
                imageView8.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView8.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound8 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView8.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton9.isChecked && radioButton19.isChecked -> {
                imageView9.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView9.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound9 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView9.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton10.isChecked && radioButton19.isChecked -> {
                imageView10.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView10.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound10 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView10.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton11.isChecked && radioButton19.isChecked -> {
                imageView11.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView11.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound11 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView11.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton12.isChecked && radioButton19.isChecked -> {
                imageView12.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView12.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound12 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView12.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton13.isChecked && radioButton19.isChecked -> {
                imageView13.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView13.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound13 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView13.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton14.isChecked && radioButton19.isChecked -> {
                imageView14.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView14.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound14 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView14.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton15.isChecked && radioButton19.isChecked -> {
                imageView15.setColorFilter(Color.parseColor("#6B6A71"))
                handler.postDelayed({ imageView15.setColorFilter(Color.parseColor("#2C2A34")) }, 1000)
                sound15 = soundPool.load(assets.openFd(soundList.name), 1)
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
                textView15.text = soundList.name.replaceAfterLast(".", "").replace(".", "")
            }
            radioButton16.isChecked -> {
                lmp.release()
                lmp = LoopMediaPlayer(this@MainActivity, Uri.parse("android.resource://" + packageName + "/raw/" + soundList.name.replace(".ogg", "")))
                supportActionBar?.title = soundList.name.replaceAfterLast(".", "").replace(".", "")
                soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                    soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                }
            }
            radioButton17.isChecked -> {
                audioName = soundList.name
                button4.performClick()
            }
        }
        findViewById<ListView>(R.id.list_view).visibility = View.INVISIBLE
    }

    private fun startCapturing() {
        if (!isRecordAudioPermissionGranted()) {
            requestRecordAudioPermission()
        } else {
            startMediaProjectionRequest()
        }
    }

    private fun stopCapturing() {

        startService(Intent(this, AudioCaptureService::class.java).apply {
            action = AudioCaptureService.ACTION_STOP
        })
        menuSwitch0 = true
        switch0.isChecked = false
        invalidateOptionsMenu()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5A5A66")))
    }

    private fun isRecordAudioPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestRecordAudioPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
        )
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult1,
                        Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult2,
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult1,
                        Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult2,
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun startMediaProjectionRequest() {
        mediaProjectionManager =
                applicationContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                MEDIA_PROJECTION_REQUEST_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                menuSwitch0 = false
                switch0.isChecked = true
                invalidateOptionsMenu()
                supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#EC7357")))
                Toast.makeText(
                        this,
                        R.string.onActivityResult1,
                        Toast.LENGTH_SHORT
                ).show()

                val audioCaptureIntent = Intent(this, AudioCaptureService::class.java).apply {
                    action = AudioCaptureService.ACTION_START
                    putExtra(AudioCaptureService.EXTRA_RESULT_DATA, resultData!!)
                }
                startForegroundService(audioCaptureIntent)
            } else {
                Toast.makeText(
                        this,
                        R.string.onActivityResult2,
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {

            READ_REQUEST_CODE2 -> {

                resultData?.data?.also { uri ->
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        val item = "/storage/emulated/0/" + split[1]
                        when {
                            radioButton.isChecked -> {
                                sound1 = soundPool.load(item, 1)
                                textView.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton2.isChecked -> {
                                sound2 = soundPool.load(item, 1)
                                textView2.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton3.isChecked -> {
                                sound3 = soundPool.load(item, 1)
                                textView3.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton4.isChecked -> {
                                sound4 = soundPool.load(item, 1)
                                textView4.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton5.isChecked -> {
                                sound5 = soundPool.load(item, 1)
                                textView5.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton6.isChecked -> {
                                sound6 = soundPool.load(item, 1)
                                textView6.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton7.isChecked -> {
                                sound7 = soundPool.load(item, 1)
                                textView7.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton8.isChecked -> {
                                sound8 = soundPool.load(item, 1)
                                textView8.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton9.isChecked -> {
                                sound9 = soundPool.load(item, 1)
                                textView9.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton10.isChecked -> {
                                sound10 = soundPool.load(item, 1)
                                textView10.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton11.isChecked -> {
                                sound11 = soundPool.load(item, 1)
                                textView11.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton12.isChecked -> {
                                sound12 = soundPool.load(item, 1)
                                textView12.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton13.isChecked -> {
                                sound13 = soundPool.load(item, 1)
                                textView13.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton14.isChecked -> {
                                sound14 = soundPool.load(item, 1)
                                textView14.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton15.isChecked -> {
                                sound15 = soundPool.load(item, 1)
                                textView15.text = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton16.isChecked -> {
                                lmp.release()
                                lmp = LoopMediaPlayer(this@MainActivity, Uri.parse(item))
                                supportActionBar?.title = item.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                            }
                            radioButton17.isChecked -> {
                                audioName = item
                                button4.performClick()
                            }
                        }
                    } else {
                        try {
                            val item2 = "/stroage/" + type + "/" + split[1]
                            when {
                                radioButton.isChecked -> {
                                    sound1 = soundPool.load(item2, 1)
                                    textView.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton2.isChecked -> {
                                    sound2 = soundPool.load(item2, 1)
                                    textView2.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton3.isChecked -> {
                                    sound3 = soundPool.load(item2, 1)
                                    textView3.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton4.isChecked -> {
                                    sound4 = soundPool.load(item2, 1)
                                    textView4.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton5.isChecked -> {
                                    sound5 = soundPool.load(item2, 1)
                                    textView5.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton6.isChecked -> {
                                    sound6 = soundPool.load(item2, 1)
                                    textView6.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton7.isChecked -> {
                                    sound7 = soundPool.load(item2, 1)
                                    textView7.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton8.isChecked -> {
                                    sound8 = soundPool.load(item2, 1)
                                    textView8.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton9.isChecked -> {
                                    sound9 = soundPool.load(item2, 1)
                                    textView9.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton10.isChecked -> {
                                    sound10 = soundPool.load(item2, 1)
                                    textView10.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton11.isChecked -> {
                                    sound11 = soundPool.load(item2, 1)
                                    textView11.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton12.isChecked -> {
                                    sound12 = soundPool.load(item2, 1)
                                    textView12.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton13.isChecked -> {
                                    sound13 = soundPool.load(item2, 1)
                                    textView13.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton14.isChecked -> {
                                    sound14 = soundPool.load(item2, 1)
                                    textView14.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton15.isChecked -> {
                                    sound15 = soundPool.load(item2, 1)
                                    textView15.text = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton16.isChecked -> {
                                    lmp.release()
                                    lmp = LoopMediaPlayer(this@MainActivity, Uri.parse(item2))
                                    supportActionBar?.title = item2.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace(".", "")
                                }
                                radioButton17.isChecked -> {
                                    audioName = item2
                                    button4.performClick()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, R.string.READ_REQUEST_CODE2, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun selectCh() {

        val chSpinner = findViewById<Spinner>(R.id.choose_loop_spinner)

        val adapterC = ArrayAdapter.createFromResource(this, R.array.spinnerItems2, android.R.layout.simple_spinner_item)

        adapterC.setDropDownViewResource(R.layout.custom_spinner_dropdown)


        chSpinner.adapter = adapterC

        chSpinner.performClick()


        chSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
            ) {
                if (!chSpinner.isFocusable) {
                    chSpinner.isFocusable = true
                    return
                }

                val soundListView = findViewById<ListView>(R.id.list_view)

                when(position){
                    0 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton19.performClick()
                        soundListView.adapter = nCustomAdapter
                        nCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    1 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton19.performClick()
                        soundListView.adapter = oCustomAdapter
                        oCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    2 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton19.performClick()
                        soundListView.adapter = pCustomAdapter
                        pCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    3 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton19.performClick()
                        soundListView.adapter = qCustomAdapter
                        qCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    4 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton18.performClick()
                        soundListView.adapter = sCustomAdapter
                        sCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    5 -> {
                        lmp.stop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1.isChecked = false
                        radioButton16.performClick()
                        radioButton18.performClick()
                        selectEX()
                        soundListView.adapter = tCustomAdapter
                        tCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    6 -> {
                        radioButton16.performClick()
                        selectAudio()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        chSpinner.isFocusable = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val menuLamp = menu!!.findItem(R.id.menu1)
        if (menuSwitch) {
            menuLamp.setIcon(R.drawable.ic_baseline_play_arrow_24)
        } else {
            menuLamp.setIcon(R.drawable.ic_baseline_stop_24)
        }

        val menuLamp3 = menu.findItem(R.id.menu8)
        if (menuSwitch0) {
            menuLamp3.setIcon(R.drawable.ic_baseline_radio_button_checked_24_2)
        } else {
            menuLamp3.setIcon(R.drawable.ic_baseline_radio_button_checked_24)
        }

        return true
    }

    private var menuSwitch = true
    private var menuSwitch2 = true
    private var menuSwitch0 = true
    private var mediaRecorder = MediaRecorder()

    private val locale: Locale = Locale.getDefault()


    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val timeStamp: String = SimpleDateFormat("MM月dd日HH時mm分ss秒").format(Date())
        val soundFilePathJA = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() + "/$timeStamp" + "の録音.ogg"

        val timestamp2: String = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.US).format(Date())
        val soundFilePathEN = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() + "/Record-$timestamp2.ogg"

        fun startRecording() {
            try {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                if (locale == Locale.JAPAN) {
                    mediaRecorder.setOutputFile(soundFilePathJA)
                } else {
                    mediaRecorder.setOutputFile(soundFilePathEN)
                }
                mediaRecorder.setMaxDuration(180000)
                mediaRecorder.setOnInfoListener { _, what, _ ->
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        mediaRecorder.stop()
                        menuSwitch2 = true
                        invalidateOptionsMenu()
                        switch2.isChecked = false
                        Toast.makeText(applicationContext, R.string.startRecording1, Toast.LENGTH_LONG).show()
                    }
                }
                mediaRecorder.prepare()
                mediaRecorder.start()
                Toast.makeText(applicationContext, R.string.startRecording2, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, R.string.startRecording3, Toast.LENGTH_LONG).show()

            }
        }

        fun stopRecording() {
            try {
                mediaRecorder.stop()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, R.string.stopRecording, Toast.LENGTH_LONG).show()
            }
        }

            button4.setOnClickListener {
                try {
                val myDir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/showwavespics.png"
                FFmpeg.execute("-i $audioName -filter_complex showwavespic=s=2560x1280:colors=blue:scale=0 -y $myDir")

                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dialog, null)

                mp.release()
                mp = MediaPlayer()
                mp.setDataSource(this, Uri.parse(audioName))
                mp.prepare()

                val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBar)
                val seekBar2 = dialogView.findViewById<SeekBar>(R.id.seekBar2)

                seekBar.progress = 0
                seekBar2.progress = 0

                seekBar.max = mp.duration

                seekBar2.max = mp.duration
                seekBar2.progress = mp.duration

                start = 0
                stop = mp.duration

                val text1 = dialogView.findViewById<TextView>(R.id.textView16)
                val text2 = dialogView.findViewById<TextView>(R.id.textView17)
                val text3 = dialogView.findViewById<TextView>(R.id.textView18)

                text1.text = SimpleDateFormat("mm:ss.SSS").format(Date(0.toLong())).toString()
                text2.text = SimpleDateFormat("mm:ss.SSS").format(Date(mp.duration.toLong())).toString()
                text3.text = audioName.replaceBeforeLast("/", "").replace("/", "")

                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {


                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            text1.text = SimpleDateFormat("mm:ss.SSS").format(Date(progress.toLong()))
                                start = progress

                        }


                        override fun onStartTrackingTouch(seekBar: SeekBar?) {

                        }


                        override fun onStopTrackingTouch(seekBar: SeekBar?) {

                        }
                    })

                    seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {


                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            text2.text = SimpleDateFormat("mm:ss.SSS").format(Date(progress.toLong()))
                                stop = progress
                        }


                        override fun onStartTrackingTouch(seekBar: SeekBar?) {

                        }


                        override fun onStopTrackingTouch(seekBar: SeekBar?) {

                        }
                    })


                    val image = dialogView.findViewById<View>(R.id.imageView16) as ImageView

                    image.setImageURI(Uri.parse(myDir))

                    val button = dialogView.findViewById(R.id.button) as Button
                    val button2 = dialogView.findViewById(R.id.button2) as Button
                    val button3 = dialogView.findViewById(R.id.button3) as Button

                    button.setOnClickListener {
                            when {
                                    start < stop -> {
                                            val builder2 = AlertDialog.Builder(this)
                                            val inflater2 = layoutInflater
                                            val dialogView2 = inflater2.inflate(R.layout.file_name, null)
                                            builder2.setView(dialogView2)
                                                    .setTitle(R.string.button_setOnClickListener1)
                                                    .setPositiveButton(R.string.button_setOnClickListener2) { _, _ ->
                                                            val nt = dialogView2.findViewById<EditText>(R.id.filename)
                                                            val fnt = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() + "/" + nt.text.replace("/".toRegex(), "") + audioName.replaceBeforeLast(".", "")
                                                            try {
                                                                FFmpeg.execute("-ss ${text1.text} -to ${text2.text} -i $audioName -y $fnt")
                                                                button3.performClick()
                                                                Toast.makeText(applicationContext, R.string.button_setOnClickListener3, Toast.LENGTH_LONG).show()
                                                            } catch (e: Exception) {
                                                                Toast.makeText(applicationContext, R.string.button_setOnClickListener4, Toast.LENGTH_LONG).show()
                                                            }
                                                    }
                                                    .setNegativeButton(R.string.button_setOnClickListener5) { _, _ ->

                                                    }
                                                    .show()

                                    }
                                    start > stop -> Toast.makeText(applicationContext, R.string.button_setOnClickListener6, Toast.LENGTH_LONG).show()
                                    start == stop -> Toast.makeText(applicationContext, R.string.button_setOnClickListener6, Toast.LENGTH_LONG).show()
                            }
                    }

                    button2.setOnClickListener {
                        if (switch3.isChecked) {
                            mp.stop()
                            mp.prepare()
                            handler.removeCallbacksAndMessages(null)
                            switch3.isChecked = false
                        } else {
                            mp.seekTo(start)
                            mp.start()
                            switch3.isChecked = true
                            if (mp.isPlaying)
                                handler.postDelayed({
                                    mp.stop()
                                    mp.prepare()
                                    switch3.isChecked = false
                                }, (stop - start).toLong())
                        }
                    }



                    builder.setView(dialogView)
                            .setOnCancelListener {
                                mp.stop()
                                mp.prepare()
                                switch3.isChecked = false
                            }
                            val dialog = builder.create()
                            dialog.show()

                button3.setOnClickListener {
                    dialog.cancel()
                }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext,
                        R.string.error,
                        Toast.LENGTH_LONG).show()
                }

            }

        val soundListView = findViewById<ListView>(R.id.list_view)

        when (item.itemId) {

            R.id.menu1 -> {
                if (soundListView.isVisible) {
                    soundListView.visibility = View.INVISIBLE
                }
                if (switch1.isChecked) {
                    lmp.stop()
                    soundPool.autoPause()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1.isChecked = false
                } else {
                    lmp.start()
                    menuSwitch = false
                    invalidateOptionsMenu()
                    switch1.isChecked = true
                }
                return true
            }

            R.id.menu1a -> {
                radioButton17.performClick()
                selectAudio()
                return true
            }

            R.id.menu6 -> {
                AlertDialog.Builder(this)
                        .setTitle(R.string.menu6)
                        .setPositiveButton("YES") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("NO") { _, _ ->

                        }
                        .show()

                return true
            }

            R.id.menu8 -> {
                if (soundListView.isVisible) {
                    soundListView.visibility = View.INVISIBLE
                }
                when {

                    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> Toast.makeText(applicationContext, R.string.menu8, Toast.LENGTH_LONG).show()

                    switch0.isChecked -> stopCapturing()

                    else -> startCapturing()

                }

                return true
            }

            R.id.menuPlus -> {
                lmp.volumePlus()
                return true
            }

            R.id.menuMinus -> {
                lmp.volumeMinus()
                return true
            }

            R.id.menu9 -> {
                radioButton17.performClick()
                radioButton18.performClick()
                selectEX()
                soundListView.adapter = tCustomAdapter
                tCustomAdapter.notifyDataSetChanged()
                soundListView.visibility = View.VISIBLE
                return true
            }

            R.id.menu9b -> {
                if (!isRecordAudioPermissionGranted()) {
                    requestRecordAudioPermission()
                } else {
                    if (switch0.isChecked) {
                        stopCapturing()
                    }
                    val builder3 = AlertDialog.Builder(this)
                    val inflater3 = layoutInflater
                    val dialogView3 = inflater3.inflate(R.layout.record_dialog, null)
                    val rec = dialogView3.findViewById<ImageView>(R.id.imageView17)
                    rec.setImageResource(R.drawable.ic_baseline_mic_24)
                    rec.setOnClickListener {
                        if (switch2.isChecked) {
                            menuSwitch2 = true
                            stopRecording()
                            Toast.makeText(applicationContext, R.string.button_setOnClickListener3, Toast.LENGTH_LONG).show()
                            rec.setImageResource(R.drawable.ic_baseline_mic_24)
                            switch2.isChecked = false
                        } else {
                            menuSwitch2 = false
                            startRecording()
                            rec.setImageResource(R.drawable.ic_baseline_mic_24_2)
                            switch2.isChecked = true
                        }
                    }
                    builder3.setView(dialogView3)
                            .setTitle(R.string.builder3)
                            .setNegativeButton(R.string.back) { _, _ ->
                                if (switch2.isChecked) {
                                    menuSwitch2 = true
                                    stopRecording()
                                    Toast.makeText(applicationContext, R.string.button_setOnClickListener3, Toast.LENGTH_LONG).show()
                                    rec.setImageResource(R.drawable.ic_baseline_mic_24)
                                    switch2.isChecked = false
                                }
                            }
                            .setOnCancelListener {
                                if (switch2.isChecked) {
                                    menuSwitch2 = true
                                    stopRecording()
                                    Toast.makeText(applicationContext, R.string.button_setOnClickListener3, Toast.LENGTH_LONG).show()
                                    rec.setImageResource(R.drawable.ic_baseline_mic_24)
                                    switch2.isChecked = false
                                }
                            }
                    val dialog = builder3.create()
                    dialog.show()

                }
                return true
            }

            R.id.menu10 -> {
                if (soundListView.isVisible) {
                    soundListView.visibility = View.INVISIBLE
                }
                selectCh()
                return true
            }

            R.id.action_settings -> {
                if (soundListView.isVisible) {
                    soundListView.visibility = View.INVISIBLE
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
            lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://" + packageName + "/raw/" + R.raw.ta))
        lmp.reset()
        lmp.release()
        mp.reset()
        mp.release()
        soundPool.autoPause()
        soundPool.release()

        mediaRecorder.reset()
        mediaRecorder.release()
        super.onDestroy()
    }

    override fun onPause() {
        menuSwitch = true
        invalidateOptionsMenu()
        switch1.isChecked = false
        if (mp.isPlaying) {
            mp.stop()
            mp.prepare()
            switch3.isChecked = false
        }
        if (!menuSwitch2) {
            menuSwitch2 = true
            invalidateOptionsMenu()
            mediaRecorder.stop()
            Toast.makeText(applicationContext, R.string.button_setOnClickListener3, Toast.LENGTH_LONG).show()
            switch2.isChecked = false
        }

            lmp.stop()
            soundPool.autoPause()

        super.onPause()
    }
}
