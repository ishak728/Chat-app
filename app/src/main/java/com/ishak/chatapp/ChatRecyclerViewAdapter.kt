package com.ishak.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerViewAdapter: RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatHolder>() {

    private val viewSentType=1
    private val viewreceivedType=2

    class ChatHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    //recyclerda bütün verileri tekrardan göstermez sadece yeni olan veriyi eklemeyi sağlar.
    //kısacası Chatler içinde farklılığa bakar
    private val diffUtil=object :DiffUtil.ItemCallback<Chat>(){
        //bu itemları kontrol eder
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem==newItem
        }

        //bu da içerikleri kontrol eder
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem==newItem
        }

    }

    //async olarak çalışmasını sağlıyor
    private val recylerListDiffer= AsyncListDiffer(this,diffUtil)

    var chats:List<Chat>
    //anladığım kadarıyla bu recyclerlistdiffer'a gidip farklı olanı alıyor
    get() =recylerListDiffer.currentList
        set(value)=recylerListDiffer.submitList(value)


    //hazır fonksiyondur
    override fun getItemViewType(position: Int): Int {

        if(chats.get(position).user==FirebaseAuth.getInstance().currentUser?.email.toString()){

            //currentuser'ın viewtype'ını 1 yapıyor
            return viewSentType
        }
        else{
            return viewreceivedType
        }
    }

    //not:bu fonk.altında zaten her bir itemin viewType'ı hazır olarak veriliyo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {


        if(viewType==viewreceivedType){
            val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
            return ChatHolder(view)
        }
        else{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_right,parent,false)
            return ChatHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
       val recyclerTextView=holder.itemView.findViewById<TextView>(R.id.recyclerTextView)
        recyclerTextView.text="${chats.get(position).user}-->${chats.get(position).text}"
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}