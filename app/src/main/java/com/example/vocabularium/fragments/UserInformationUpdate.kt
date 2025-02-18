package com.example.vocabularium.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vocabularium.R
import com.example.vocabularium.activities.AuthenticationActivity
import com.example.vocabularium.databinding.FragmentUserInformationUpdateBinding
import com.example.vocabularium.dialog_fragments.ChangePasswordDialogFragment
import com.example.vocabularium.models.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInformationUpdate : Fragment() {
    private lateinit var design: FragmentUserInformationUpdateBinding
    val viewModel: UpdateUserViewModel by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    var isInitilized = false

    private val REQUEST_CODE_PERMISSIONS = 101
    private val PERMISSIONS_REQUIRED = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getProfileImage()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater, R.layout.fragment_user_information_update, container, false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.userUpdateFragmentObject = this
        viewModel.getUserInfo()
        viewModel.getProfileImage(design.profileImage)
        viewModel.toastMessage.observe(viewLifecycleOwner, {
            if (isInitilized) {
                showToast(it)
            } else isInitilized = true
        })
        viewModel.userInfo.observe(viewLifecycleOwner, {
            updateUI(it)
        })
    }

    fun updateUI(user: User) {
        design.updateName.setText(user.userName)
        design.updateEmail.setText(user.userEmail)
    }

    fun backToProfile() {
        findNavController().popBackStack()
    }

    fun launchPhotoPicker() {
        if (allPermissionsGranted()) {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            requestPermissions()
        }
    }

    private fun allPermissionsGranted() = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            PERMISSIONS_REQUIRED,
            REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                launchPhotoPicker()
            } else {
                showToast("permissions required")
            }
        }
    }

    fun getProfileImage() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(requireActivity()).load(uri).circleCrop().into(design.profileImage)
                viewModel.loadImageToFBStorage(uri)
            } else {
                showToast("image couldn't pick")
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun applyChanges() {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("Email Verification")
        dialog.setMessage("Verification mail was sent please verify your new email to access")
        dialog.setPositiveButton("Ok") { dialog, id ->
            val intent = Intent(requireActivity(), AuthenticationActivity::class.java)
            startActivity(intent)
        }
        dialog.create()
        viewModel.applyChanges(design.updateName, design.updateEmail, dialog)
    }

    fun changePassword() {
        val changePasswordDialog = ChangePasswordDialogFragment()
        changePasswordDialog.show(parentFragmentManager, "dialog")
    }
    fun deleteAccount(){
        viewModel.deleteAccount(requireContext(),design.deleteAccount!!)
    }
}
