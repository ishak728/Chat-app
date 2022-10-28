package com.ishak.chatapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ishak.chatapp.databinding.FragmentAddFotoBinding
import com.ishak.chatapp.databinding.FragmentChatBinding
import io.grpc.Context
import java.util.*
import java.util.jar.Manifest


class AddFotoFragment : Fragment() {

    private lateinit var fireStore:FirebaseFirestore
    private lateinit var storage:FirebaseStorage

    var image_uri:Uri?=null
    private var _binding: FragmentAddFotoBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStore=Firebase.firestore
        storage=Firebase.storage

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFotoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener{

            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(binding.upload,"need to permission",Snackbar.LENGTH_INDEFINITE).setAction("give"){
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
                    }
                }
                else{
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

                }
            }
            else{
                //galeriye gidilercek
                val intent_gallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent_gallery,2)
            }

        }


        val uuid=UUID.randomUUID()
        val image="$uuid.jpg"
        binding.upload.setOnClickListener{
            //storage'ye resim atanacak.resim linki indirilip firestore'a atanacak
            if(image_uri!=null){
                val reference=storage.reference
println(1)
                val image_reference=reference.child("image").child(image)
                image_reference.putFile(image_uri!!).addOnSuccessListener {
                    println(2)

                    val downloaded_image=image_reference.downloadUrl
                   image_reference.downloadUrl.addOnSuccessListener {
                       println(3)
                       val image_map= hashMapOf<String,Any>()
                       image_map.put("imageUrl",downloaded_image.toString())
                       //şu anki zamanı alır
                       image_map.put("date",Timestamp.now())

                        fireStore.collection("Images").add(image_map)

                       val goChatFragment=AddFotoFragmentDirections.actionAddFotoFragmentToChatFragment()
                       findNavController().navigate(goChatFragment)

                    }.addOnFailureListener{
                        Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"select a photo",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1){
            if (grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),"Allowed",Toast.LENGTH_LONG).show()
                //galeriye gidilecek
                val intent_gallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent_gallery,2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2&&resultCode==RESULT_OK){
            image_uri= data?.data
            binding.imageView.setImageURI(image_uri)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}