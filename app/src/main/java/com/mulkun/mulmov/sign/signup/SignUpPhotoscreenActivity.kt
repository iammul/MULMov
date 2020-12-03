package com.mulkun.mulmov.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mulkun.mulmov.home.HomeActivity
import com.mulkun.mulmov.R
import com.mulkun.mulmov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTCURE=1
    var statusAdd:Boolean=false
    lateinit var filePath: Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReferensi : StorageReference
    lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReferensi = storage.getReference()

        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd){
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.user_pic)
            }else{
                //Dexter.withActivity(this)
                  //      .withPermission(Manifest.permission.CAMERA)
                    //    .withListener(this)
                      //  .check()

                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }
        }

        btn_home.setOnClickListener {
            finishAffinity()

            var goHome = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_save.setOnClickListener {
            if (filePath != null){

                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReferensi.child("image/"+UUID.randomUUID().toString())
                ref.putFile(filePath)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                            ref.downloadUrl.addOnSuccessListener {
                                preferences.setValues("url", it.toString())
                            }

                            finishAffinity()

                            var goHome = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
                            startActivity(goHome)
                        }
                        .addOnFailureListener{
                            progressDialog.dismiss()
                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                        }
                        .addOnProgressListener {
                            taskSnapshot -> var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                            progressDialog.setMessage("Upload "+progress.toInt()+" %")
                        }


            }else{

            }
        }

    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTCURE)
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik tombol upload nanti saja", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {

    }

//    @SuppressLint("MissingSuperCall")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode == REQUEST_IMAGE_CAPTCURE &&resultCode == Activity.RESULT_OK){
//            var bitmap = data?.extras?.get("data") as Bitmap
//            statusAdd =true
//
//            filePath = data.getData()!!
//            Glide.with(this)
//                    .load(bitmap)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(iv_profile)
//
//            btn_save.visibility = View.VISIBLE
//            iv_add.setImageResource(R.drawable.ic_btn_delete)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            //Log.v("tamvan", "file uri upload"+filePath)
            btn_save.visibility=View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        }else if(resultCode==ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}