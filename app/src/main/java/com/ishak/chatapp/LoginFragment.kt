package com.ishak.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ishak.chatapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


    private var _binding: FragmentLoginBinding  ? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //kullanıcı daha önce giriş yaptıysa şifre vemaili girmeden otomatikmen giriş yapılır
        val currentUser = auth.currentUser
        if(currentUser != null){
            val goChatFragment=LoginFragmentDirections.actionLoginFragmentToChatFragment()
            findNavController().navigate(goChatFragment)
        }

        binding.signUpbutton.setOnClickListener{

            //kullanıcı auth sayesinde kaydoluyor
            auth.createUserWithEmailAndPassword(binding.emailText.text.toString(),binding.passwordText.text.toString()).addOnSuccessListener {

                //fragmentler arası geçişi sağlar
                val goChatFragment=LoginFragmentDirections.actionLoginFragmentToChatFragment()
                findNavController().navigate(goChatFragment)
            }.addOnFailureListener{
                //requireContext() yerine contex te yazılabilir
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

        binding.signInbutton.setOnClickListener{
            auth.signInWithEmailAndPassword(binding.emailText.text.toString(),binding.passwordText.text.toString()).addOnSuccessListener {
                val goChatFragment=LoginFragmentDirections.actionLoginFragmentToChatFragment()
                findNavController().navigate(goChatFragment)
            }.addOnFailureListener{
                //requireContext() yerine contex te yazılabilir
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //null yapılarak hafıza tasarrufu sağlanıyor
        _binding = null
    }


}