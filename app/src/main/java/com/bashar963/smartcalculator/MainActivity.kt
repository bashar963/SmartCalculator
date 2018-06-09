package com.bashar963.smartcalculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.bashar963.smartcalculator.Fragments.AnglesFragment
import com.bashar963.smartcalculator.Fragments.AreaFragment
import com.bashar963.smartcalculator.Fragments.DataFragment
import com.bashar963.smartcalculator.Fragments.EnergyFragment
import com.bashar963.smartcalculator.Fragments.GPACalculator
import com.bashar963.smartcalculator.Fragments.LengthFragment
import com.bashar963.smartcalculator.Fragments.PowerFragment
import com.bashar963.smartcalculator.Fragments.PressureFragment
import com.bashar963.smartcalculator.Fragments.Programmer
import com.bashar963.smartcalculator.Fragments.SpeedFragment
import com.bashar963.smartcalculator.Fragments.TempFragment
import com.bashar963.smartcalculator.Fragments.TimeFragment
import com.bashar963.smartcalculator.Fragments.VolumeFragment
import com.bashar963.smartcalculator.Fragments.WeightAndMassFragment
import com.bashar963.smartcalculator.Fragments.scientific
import com.bashar963.smartcalculator.Fragments.standard

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var id: Int = 0
    private var mPrefs: SharedPreferences? = null
    private var mInterstitialAd: InterstitialAd? = null

    private var doubleBackToExitPressedOnce = false
    private var adNow = 1
    private var myTheme: Int = 0
    private var myMode: Int = 0
    private var isNight: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {


        mPrefs = this.getSharedPreferences("main", Context.MODE_PRIVATE)
        myTheme = mPrefs!!.getInt("theme", R.style.AppTheme)
        myMode = mPrefs!!.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(myMode)
        setTheme(myTheme)
        isNight = mPrefs!!.getBoolean("isNight", false)

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, "ca-app-pub-2274591714099465~1425715837")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = "ca-app-pub-2274591714099465/9402334236"
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mInterstitialAd!!.adListener = object : AdListener() {
            override fun onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
            }

        }


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)



        id = mPrefs!!.getInt("id", R.id.nav_standard)
        val fm = supportFragmentManager
        if (id == R.id.nav_standard) {
            fm.beginTransaction().replace(R.id.content_frame, standard()).commit()
            title = resources.getString(R.string.standard)
        } else if (id == R.id.nav_scientific) {
            fm.beginTransaction().replace(R.id.content_frame, scientific()).commit()
            title = resources.getString(R.string.scientific)
        } else if (id == R.id.nav_programmer) {
            fm.beginTransaction().replace(R.id.content_frame, Programmer()).commit()
            title = resources.getString(R.string.programmer)
        } else if (id == R.id.nav_gpa) {
            fm.beginTransaction().replace(R.id.content_frame, GPACalculator()).commit()
            title = resources.getString(R.string.gpaCalc)
        } else if (id == R.id.nav_volume) {
            fm.beginTransaction().replace(R.id.content_frame, VolumeFragment()).commit()
            title = resources.getString(R.string.volume)
        } else if (id == R.id.nav_length) {
            fm.beginTransaction().replace(R.id.content_frame, LengthFragment()).commit()
            title = resources.getString(R.string.length)
        } else if (id == R.id.nav_weightMass) {
            fm.beginTransaction().replace(R.id.content_frame, WeightAndMassFragment()).commit()
            title = resources.getString(R.string.weightMass)
        } else if (id == R.id.nav_temp) {
            fm.beginTransaction().replace(R.id.content_frame, TempFragment()).commit()
            title = resources.getString(R.string.temp)
        } else if (id == R.id.nav_energy) {
            fm.beginTransaction().replace(R.id.content_frame, EnergyFragment()).commit()
            title = resources.getString(R.string.energy)
        } else if (id == R.id.nav_area) {
            fm.beginTransaction().replace(R.id.content_frame, AreaFragment()).commit()
            title = resources.getString(R.string.area)
        } else if (id == R.id.nav_speed) {
            fm.beginTransaction().replace(R.id.content_frame, SpeedFragment()).commit()
            title = resources.getString(R.string.speed)
        } else if (id == R.id.nav_time) {
            fm.beginTransaction().replace(R.id.content_frame, TimeFragment()).commit()
            title = resources.getString(R.string.time)
        } else if (id == R.id.nav_power) {
            fm.beginTransaction().replace(R.id.content_frame, PowerFragment()).commit()
            title = resources.getString(R.string.power)
        } else if (id == R.id.nav_data) {
            fm.beginTransaction().replace(R.id.content_frame, DataFragment()).commit()
            title = resources.getString(R.string.data)
        } else if (id == R.id.nav_pressure) {
            fm.beginTransaction().replace(R.id.content_frame, PressureFragment()).commit()
            title = resources.getString(R.string.pressure)
        } else if (id == R.id.nav_angle) {
            fm.beginTransaction().replace(R.id.content_frame, AnglesFragment()).commit()
            title = resources.getString(R.string.angle)
        }
    }


    override fun onBackPressed() {

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce && !drawer.isDrawerOpen(GravityCompat.START)) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        when (id) {
            R.id.NightMode -> {
                if (myTheme != R.style.DayNight) {
                    myTheme = R.style.DayNight
                    myMode = AppCompatDelegate.MODE_NIGHT_YES
                    isNight = true
                    Toast.makeText(this, "Night Mode On", Toast.LENGTH_SHORT).show()
                } else {
                    myTheme = R.style.AppTheme
                    myMode = AppCompatDelegate.MODE_NIGHT_NO
                    isNight = false
                    Toast.makeText(this, "Night Mode Off", Toast.LENGTH_SHORT).show()
                }

                val ed = mPrefs!!.edit()
                ed.putInt("theme", myTheme)
                ed.putInt("mode", myMode)
                ed.putBoolean("isNight", isNight)
                ed.apply()
                recreate()
            }
        }



        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val fm = supportFragmentManager
        when (id) {
            R.id.nav_standard -> {
                fm.beginTransaction().replace(R.id.content_frame, standard()).commit()
                title = resources.getString(R.string.standard)
                this.id = id
            }
            R.id.nav_scientific -> {
                fm.beginTransaction().replace(R.id.content_frame, scientific()).commit()
                title = resources.getString(R.string.scientific)
                this.id = id
            }
            R.id.nav_programmer -> {
                fm.beginTransaction().replace(R.id.content_frame, Programmer()).commit()
                title = resources.getString(R.string.programmer)
                this.id = id
            }
            R.id.nav_gpa -> {
                fm.beginTransaction().replace(R.id.content_frame, GPACalculator()).commit()
                title = resources.getString(R.string.gpaCalc)
                this.id = id
            }
            R.id.nav_volume -> {
                fm.beginTransaction().replace(R.id.content_frame, VolumeFragment()).commit()
                title = resources.getString(R.string.volume)
                this.id = id
            }
            R.id.nav_length -> {
                fm.beginTransaction().replace(R.id.content_frame, LengthFragment()).commit()
                title = resources.getString(R.string.length)
            }
            R.id.nav_weightMass -> {
                fm.beginTransaction().replace(R.id.content_frame, WeightAndMassFragment()).commit()
                title = resources.getString(R.string.weightMass)
                this.id = id
            }
            R.id.nav_temp -> {
                fm.beginTransaction().replace(R.id.content_frame, TempFragment()).commit()
                title = resources.getString(R.string.temp)
                this.id = id
            }
            R.id.nav_energy -> {
                fm.beginTransaction().replace(R.id.content_frame, EnergyFragment()).commit()
                title = resources.getString(R.string.energy)
                this.id = id
            }
            R.id.nav_area -> {
                fm.beginTransaction().replace(R.id.content_frame, AreaFragment()).commit()
                title = resources.getString(R.string.area)
                this.id = id
            }
            R.id.nav_speed -> {
                fm.beginTransaction().replace(R.id.content_frame, SpeedFragment()).commit()
                title = resources.getString(R.string.speed)
                this.id = id
            }
            R.id.nav_time -> {
                fm.beginTransaction().replace(R.id.content_frame, TimeFragment()).commit()
                title = resources.getString(R.string.time)
            }
            R.id.nav_power -> {
                fm.beginTransaction().replace(R.id.content_frame, PowerFragment()).commit()
                title = resources.getString(R.string.power)
                this.id = id
            }
            R.id.nav_data -> {
                fm.beginTransaction().replace(R.id.content_frame, DataFragment()).commit()
                title = resources.getString(R.string.data)
                this.id = id

            }
            R.id.nav_pressure -> {
                fm.beginTransaction().replace(R.id.content_frame, PressureFragment()).commit()
                title = resources.getString(R.string.pressure)
                this.id = id
            }
            R.id.nav_angle -> {
                fm.beginTransaction().replace(R.id.content_frame, AnglesFragment()).commit()
                title = resources.getString(R.string.angle)
                this.id = id
            }
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        adNow++
        if (adNow >= 5) {
            if (mInterstitialAd!!.isLoaded) {
                mInterstitialAd!!.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }

            adNow = 1
        }
        val d = mPrefs!!.getBoolean("doRecreate", true)
        val ed = mPrefs!!.edit()
        if (d) {
            ed.putBoolean("doRecreate", false)
            ed.putBoolean("recreateTheme", false)
        } else {
            ed.putBoolean("recreateTheme", true)
        }

        ed.apply()
        return true

    }

    public override fun onPause() {
        super.onPause()
        //startAppAd.onPause();
        val ed = mPrefs!!.edit()
        ed.putInt("id", this.id)
        ed.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isNight) {
            val ed = mPrefs!!.edit()
            ed.putBoolean("doRecreate", true)
            ed.apply()
        } else {
            val ed = mPrefs!!.edit()
            ed.putBoolean("doRecreate", false)
            ed.apply()
        }
    }
}
