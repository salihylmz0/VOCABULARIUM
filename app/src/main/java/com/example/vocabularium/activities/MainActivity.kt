package com.example.vocabularium.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivityMainBinding
import com.example.vocabularium.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val auth = Firebase.auth
    val user : FirebaseUser?
    private  val viewModel : MainActivityViewModel by viewModels()
    private lateinit var design: ActivityMainBinding
    init {
         user = auth.currentUser
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this,R.color.backgroundtype2)//I tried to change this on Xml but it did not work
        design = DataBindingUtil.setContentView(this,R.layout.activity_main)
     

        updateUI()
        viewModel.getUserInfo()
        //Connect Bottom Navigation Bar and Navigation Host Layout
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(design.mainActivityBottomNavigationView,navHostFragment.navController)

        //Connect Navigation Drawer
        design.include.menuItem.setOnClickListener{
            design.drawer.open()
        }
        //closing naviagition drawer with back pressed
        val callBack = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(design.drawer.isDrawerOpen(GravityCompat.START)){
                    design.drawer.closeDrawer(GravityCompat.START)
                }else finish()
            }
        }
        viewModel.userInfo.observe(this,{
            NawDrawerUserInfo(it)
        })
        onBackPressedDispatcher.addCallback(callBack)
        setUpDrawerContent(design.navigationView)
    }

    private fun setUpDrawerContent(navigationView:NavigationView){
        navigationView.setNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.authentication->{
                    val intent = Intent(this,AuthenticationActivity::class.java)
                    startActivity(intent)
                    if (user != null) auth.signOut()
                }
                R.id.nawDrawerExit->{
                    finishAffinity()
                }
            }
            true
        }

    }
    fun updateUI(){
        if (auth.currentUser != null){
            design.navigationView.menu.findItem(R.id.authentication).setIcon(R.drawable.logout_2_svgrepo_com)
            design.navigationView.menu.findItem(R.id.authentication).setTitle(R.string.log_out)
        }else{
            design.navigationView.menu.findItem(R.id.authentication).setIcon(R.drawable.login_2_svgrepo_com)
            design.navigationView.menu.findItem(R.id.authentication).setTitle(R.string.log_in)
        }
    }
    fun NawDrawerUserInfo(user: User){
        viewModel.geProfileImageUri( design.navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerProfileImage) as ImageView)
        design.navigationView.getHeaderView(0).findViewById<TextView>(R.id.mainUserName).text = user.userName
        design.navigationView.getHeaderView(0).findViewById<TextView>(R.id.mainUserEmail).text = user.userEmail
    }
}