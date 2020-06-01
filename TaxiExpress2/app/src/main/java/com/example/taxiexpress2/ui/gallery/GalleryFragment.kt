package com.example.taxiexpress2.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.taxiexpress2.R
import com.example.taxiexpress2.models.Alfil
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Probando poner la imagen de usuario **/

        val storage = FirebaseStorage.getInstance().getReference("/alfil_profile_image/")
        //val storage = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/taxi-express-sistema-2.appspot.com/o/alfil_profile_image%2F97506cd5-7809-4ffa-81f1-69ec93e76559?alt=media&token=4d8cf1d8-52c7-4814-a41e-ab09a07b1d75")
        //Picasso.get().load().into(imageView_gallery_alfil_image)
        //val ImageRef = storage.getReference("/alfil_profile_image/")
        val ref = FirebaseStorage.getInstance().reference
        //val image = findViewById<ImageView>(R.id.imageView_gallery_alfil_image)

//        var user: Alfil? = null
//        val uri = user?.profileImageUrl
//        val targetImageView = imageView_gallery_alfil_image
//        Log.d("Gallery", "Alfil: ${user}")
//        Log.d("Gallery", "uri: ${uri}")
        Log.d("Gallery", "Storage: ${storage}")

        /** Probando poner la imagen de usuario **/
    }
}
