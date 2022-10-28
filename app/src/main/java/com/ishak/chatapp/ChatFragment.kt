package com.ishak.chatapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ishak.chatapp.databinding.FragmentChatBinding
import com.ishak.chatapp.databinding.FragmentLoginBinding


class ChatFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore:FirebaseFirestore


    private lateinit var adapter:ChatRecyclerViewAdapter
    private var chats= arrayListOf<Chat>()

    private var _binding: FragmentChatBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fireStore=Firebase.firestore
        auth=Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ChatRecyclerViewAdapter()
        binding.recyclerView.adapter=adapter
        //activity'de olmadığımız için this veremiyoruz.
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())


        binding.recyclerViewButton.setOnClickListener{
            val userEmail=auth.currentUser!!.email
            val message=binding.recyclerViewText.text.toString()
            //güncel tarihi alır
            val date=FieldValue.serverTimestamp()

            val saveWithMap= HashMap<String,Any>()
            if (userEmail != null) {
                saveWithMap.put("userEmail",userEmail)
            }
            saveWithMap.put("message",message)
            saveWithMap.put("date",date)

            //fireStore'da Chats adlı collection oluşturup ve otomatikmen oluşan documentin altına saveWithMap ekleniyor
            fireStore.collection("Chats").add(saveWithMap).addOnSuccessListener {
                binding.recyclerViewText.setText("")

            }.addOnFailureListener{
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                binding.recyclerViewText.setText("")
            }

        }

        fireStore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener{value,error->

            if(error!=null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                if (value != null) {
                    if(value.isEmpty){
                        Toast.makeText(requireContext(),"there is not message",Toast.LENGTH_LONG).show()
                    }
                    else{

                        val documents=value.documents

                        chats.clear()
                        for(document in documents){
                            val message=document.get("message") as String
                            val userEmail=document.get("userEmail") as String
                            val chat=Chat(userEmail,message)

                            chats.add(chat)
                            //bunu for dışında yapsak daha iyi olur sanırım
                            adapter.chats=chats
                        }

                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        //null yapılarak hafıza tasarrufu sağlanıyor
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menuuu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.addfoto){

            val go_AddFotoFragment=ChatFragmentDirections.actionChatFragmentToAddFotoFragment()
            findNavController().navigate(go_AddFotoFragment)
        }

        //R.id.yazaraktan hersınıftaki view'a erişilebilir
        if(item.itemId==R.id.exit_to_app){
            auth.signOut()
            val goLoginFragments= ChatFragmentDirections.actionChatFragmentToLoginFragment()

            findNavController().navigate(goLoginFragments)

        }


        return super.onOptionsItemSelected(item)
    }


}