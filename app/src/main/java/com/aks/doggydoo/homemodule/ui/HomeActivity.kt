package com.aks.doggydoo.homemodule.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.aks.doggydoo.R
import com.aks.doggydoo.chatMessage.ChatMessageRequestActivity
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.ActivityHomeBinding
import com.aks.doggydoo.login.ui.LoginActivity
import com.aks.doggydoo.myprofile.ui.MyProfileActivity
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.network.ApiConstant
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "homeActTag"

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private var doubleBackToExitPressedOnce = false
    lateinit var avatar: ImageView
    lateinit var name: TextView
    lateinit var userMobile: TextView
    lateinit var email: TextView

    @SuppressLint("RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_fragment)
        navView.setupWithNavController(navController)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        val header = navView.getHeaderView(0)
        avatar = header.findViewById(R.id.imageView)
        name = header.findViewById(R.id.tvUserName)
        userMobile = header.findViewById(R.id.tvMobile)
        email = header.findViewById(R.id.tvEmail)

        drawerLayout.closeDrawer(Gravity.LEFT)


        //open drawer from hamburger icon
        binding.mainContent.hamburger.setOnClickListener {
            CommonMethod.hideKeyboard(this)
            setuserDetail()
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        navView.setNavigationItemSelectedListener(this)

        avatar.setOnClickListener {
            startActivity(Intent(this, MyProfileActivity::class.java))
            drawerLayout.closeDrawer(Gravity.LEFT)
        }

        val closeBar = header.findViewById<ImageView>(R.id.ivClose)
        closeBar.setOnClickListener {
            drawerLayout.closeDrawer(Gravity.LEFT)
        }

        binding.llLogout.setOnClickListener {
            logoutDialog()
        }
    }



    private fun setuserDetail() {
        name.text = MyApp.getSharedPref().userName
        userMobile.text = MyApp.getSharedPref().userMobile
        email.text = MyApp.getSharedPref().userEmail
        avatar.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
        )
    }

    @Override
    override fun onResume() {
        super.onResume()
    }

    private fun logoutDialog() {
        val dialog = Dialog(this@HomeActivity, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)

        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val yesButton = dialog.findViewById<View>(R.id.btYes) as Button
        val noButton = dialog.findViewById<View>(R.id.btNo) as Button

        yesButton.setOnClickListener {
            MyApp.getSharedPref().userId = ""
            MyApp.getSharedPref().userName = ""
            MyApp.getSharedPref().userImage =""
            MyApp.getSharedPref().userMobile = ""
            MyApp.getSharedPref().userRegistered = ""
            MyApp.getSharedPref().userEmail = ""
            MyApp.getSharedPref().userReqType = ""
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
            dialog.dismiss()
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.close()
        when (item.itemId) {
            R.id.home -> {
                navController.navigate(R.id.nav_home)
            }
            R.id.notification -> {
                navController.navigate(R.id.nav_notification)
            }
            R.id.setting -> {
                navController.navigate(R.id.nav_setting)
            }
            R.id.friend -> {
                navController.navigate(R.id.myFriendsFrag)
            }

            R.id.request -> {
                // navController.navigate(R.id.requestFrag)
                startActivity(
                    Intent(this, ChatMessageRequestActivity::class.java).putExtra(
                        "from",
                        "request"
                    )
                )
            }
            R.id.message -> {
                //navController.navigate(R.id.messageFrag)
                startActivity(
                    Intent(this, ChatMessageRequestActivity::class.java).putExtra(
                        "from",
                        "message"
                    )
                )
            }
            R.id.myCalendar -> {
                navController.navigate(R.id.UpcomingFrag)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        CommonMethod.showSnack(binding.drawerLayout, "Please click BACK again to exit")
        // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}