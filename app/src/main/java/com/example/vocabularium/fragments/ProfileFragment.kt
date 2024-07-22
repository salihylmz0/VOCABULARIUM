package com.example.vocabularium.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.vocabularium.R
import com.example.vocabularium.databinding.FragmentProfileBinding
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var design: FragmentProfileBinding
    val auth = Firebase.auth
    private val viewModel: ProfileFragmentViewModel by viewModels ()
    private val user = Firebase.auth.currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.profileFragmentObject = this
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
            viewModel.geProfileImage(design.profilePicture)
            design.log.text = "Log out"
            design.profilName.text = viewModel.userInfo.value?.userName?.uppercase()
            design.profileProgress.progress = ceil(viewModel.progressValue.value!!).toInt()
            design.wordsRate.text = "%${ ceil(viewModel.progressValue.value!!).toInt()} Completed"
            design.profileComplationRate.text = "${viewModel.learntWordsNumber.value}/${viewModel.userData.value?.size?.minus(1)}"
        }
        else{
            design.log.text = "Log in"
        }
    }
    fun animate(){
        viewModel.animate(design.topOfProfile,design.bottomProfileCard,design.profilePicture)
    }
    fun getToUpdateUserInfo(){
        user?.let {
            Navigation.findNavController(design.root).navigate(R.id.action_profileFragment_to_userInformationUpdate2)
        }
    }
    fun goToAuthentication(){
        auth.signOut()
        Navigation.findNavController(design.root).navigate(R.id.action_profileFragment_to_authenticationActivity)
    }

}