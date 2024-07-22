package com.example.vocabularium.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.vocabularium.R
import com.example.vocabularium.activities.ProfileActivity
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.animations.Slide
import com.example.vocabularium.databinding.FragmentProfileBinding
import com.example.vocabularium.databinding.FragmentSettingsBinding
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.roundToInt

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var tasarim: FragmentProfileBinding
    val auth = Firebase.auth
    private val viewModel: ProfileFragmentViewModel by viewModels ()
    private val user = Firebase.auth.currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        return tasarim.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasarim.profileFragmentObject = this
        animate()

        if(user == null){
            val dialog = LoginDialogFragment()
            dialog.show(parentFragmentManager,"login dialog")

        }else{
            viewModel.getUserInfo()
            viewModel.userInfo.observe(viewLifecycleOwner,{
                viewModel.getUserProgress()
            })
            viewModel.progressValue.observe(viewLifecycleOwner,{
                updateUI()
            })
        }
    }
    fun updateUI(){
        if (user != null) {
            viewModel.geProfileImage(tasarim.profilePicture)
            tasarim.log.text = "Log out"
            tasarim.profilName.text = viewModel.userInfo.value?.userName?.uppercase()
            tasarim.profileProgress.progress = ceil(viewModel.progressValue.value!!).toInt()
            tasarim.wordsRate.text = "%${ ceil(viewModel.progressValue.value!!).toInt()} Completed"
            tasarim.profileComplationRate.text = "${viewModel.learntWordsNumber.value}/${viewModel.userData.value?.size?.minus(1)}"
        }
        else{
            tasarim.log.text = "Log in"
        }
    }
    fun animate(){
        viewModel.animate(tasarim.topOfProfile,tasarim.bottomProfileCard,tasarim.profilePicture)
    }
    fun getToUpdateUserInfo(){
        user?.let {
            Navigation.findNavController(tasarim.root).navigate(R.id.action_profileFragment_to_userInformationUpdate2)
        }
    }
    fun goToAuthentication(){
        auth.signOut()
        Navigation.findNavController(tasarim.root).navigate(R.id.action_profileFragment_to_authenticationActivity)
    }

}