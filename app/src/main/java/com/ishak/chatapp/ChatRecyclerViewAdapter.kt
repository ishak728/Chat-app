package com.ishak.chatapp
 //sdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
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
            var w=0
            println("w"+w++)
            return oldItem==newItem
        }

        //bu da içerikleri kontrol eder
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            var d=0
            println("d"+d++)
            return oldItem==newItem
        }

    }

    //async olarak çalışmasını sağlıyor
    private val recylerListDiffer= AsyncListDiffer(this,diffUtil)

    var chats:List<Chat>

        //sanırım geçerli olan listeyi alıyor.araştır diffutil'i
        get() =recylerListDiffer.currentList
        //sanırım chats listesine yeni elemanı ekliyor sadece
        set(value)=recylerListDiffer.submitList(value)



    override fun getItemViewType(position: Int): Int {
        var m=0
        println("m"+m++)
        val chat=chats.get(position)

        if(chat.user==FirebaseAuth.getInstance().currentUser?.email.toString()){

            //currentuser'ın viewtype'ını 1 yapıyor
            return viewSentType
        }
        else{
            return viewreceivedType
        }
    }

    //not:bu fonk.altında zaten her bir itemin viewType'ı hazır olarak veriliyo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        var t=0
        println("t"+t++)

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
        var i=0
        println("i"+i++)

       val text=holder.itemView.findViewById<TextView>(R.id.recyclerTextView)
        val name=holder.itemView.findViewById<TextView>(R.id.name)
        name.text="${chats.get(position).user} "
        text.text="${chats.get(position).text}"
    }

    override fun getItemCount(): Int {
        var k=0
        println("k"+k++)
        return chats.size
    }
}