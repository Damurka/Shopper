package com.example.shopper

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.shopper.databinding.FragmentProfileBinding
import com.example.shopper.models.Profile
import com.example.shopper.viewmodels.ProfileViewModel
import com.example.shopper.viewmodels.ProfileViewModelFactory
import java.io.ByteArrayOutputStream


class ProfileFragment : AuthenticatedFragment() {

    private var hasStoragePermission = false
    private var photo: ByteArray? = null
    private lateinit var profile: Profile
    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(authViewModel.userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.saveButton.setOnClickListener {
            profile.name = binding.name.text.toString()
            profile.phone = binding.phone.text.toString()

            profileViewModel.updateProfile(profile, photo)
            photo = null
        }

        binding.photo.setOnClickListener {
            if (hasStoragePermission) {
                showDialog(PhotoFragment())
            } else {
                requestStoragePermissions()
            }
        }

        checkPermissions()

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, Observer {
            profile = it
            binding.name.setText(it.name)
            binding.phone.setText(it.phone)
            binding.email.setText(authViewModel.email)
            val url = if (it.imageUrl.isNullOrBlank()) "https://via.placeholder.com/300x200?text=Click to set photo" else it.imageUrl

            Glide.with(this)
                .load(url)
                .into(binding.photo)
        })

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                //TODO: Check is granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE -> {
                val result = data!!.getIntExtra(PhotoFragment.PHOTO_EXTRA, -1)
                processPhoto(result)
            }
            REQUEST_CODE_PICK_FILE -> {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data?.data)
                ImageResize().execute(bitmap)
            }
            REQUEST_CODE_CAMERA -> {
                val bitmap = data?.extras?.get("data") as Bitmap
                ImageResize().execute(bitmap)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processPhoto(resultCode: Int) {
        val intent: Intent
        val code: Int
        when (resultCode) {
            0 -> {
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                code = REQUEST_CODE_PICK_FILE
            }
            1 -> {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                code = REQUEST_CODE_CAMERA
            }
            else -> return
        }

        startActivityForResult(intent, code)
    }

    private fun showDialog(dialog: DialogFragment) {
        dialog.setTargetFragment(this@ProfileFragment, REQUEST_CODE)
        dialog.show(fragmentManager!!, PHOTO_TAG)
    }

    private fun checkPermissions() {
        val context = requireActivity().applicationContext
        hasStoragePermission = ContextCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permissions[2]) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val REQUEST_CODE_PICK_FILE = 2
        private const val REQUEST_CODE_CAMERA = 3

        private const val THRESHOLD_MB = 5
        private const val MB = 1024 * 1024

        private const val PHOTO_TAG = "photo"

        private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
    }

    private inner class ImageResize : AsyncTask<Bitmap, Void, ByteArray?>() {

        override fun doInBackground(vararg params: Bitmap): ByteArray? {
            var bytes: ByteArray? = null
            for (i in 1..10) {
                if (i == 10) {
                    break
                }
                bytes = getBytesFromBitmap(params[0], 100 / i)
                if (bytes.size / MB < THRESHOLD_MB) {
                    return bytes
                }
            }

            return bytes
        }

        override fun onPostExecute(result: ByteArray?) {
            photo = result
            Glide.with(this@ProfileFragment).clear(binding.photo)
            Glide.with(this@ProfileFragment)
                .load(photo)
                .into(binding.photo)
        }

        private fun getBytesFromBitmap(bitmap: Bitmap?, quality: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            return stream.toByteArray()
        }

    }
}
