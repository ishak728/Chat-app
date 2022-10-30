package com.ishak.chatapp
 //sdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ChatRecyclerViewAdapter: RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatHolder>() {


    private val viewSentType=1
    private val viewreceivedType=2
   //private lateinit var chat: Chat
   //gönderilen textin boş olup olmadığını sorgular.boşsarecyclerviewimage gösterecektir aksi halde recyclerrow gösterilecektir
     var isblankMessage=true

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

        //sanırım geçerli olan listeyi alıyor.araştır diffutil'i
        get() =recylerListDiffer.currentList
        //sanırım chats listesine yeni elemanı ekliyor sadece
        set(value)=recylerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {

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


        //solda mesaj gösterilecek
        if(viewType==viewreceivedType){




                val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_image,parent,false)
                return ChatHolder(view)


        }
        //sağda mesaj gösterilecek
        else{

                val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_image_right,parent,false)
                return ChatHolder(view)

        }

    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        //image için recyclerview gösterilecek
        if(chats.get(position).image_url!=""){
            val text:TextView?=holder.itemView.findViewById<TextView>(R.id.Recycler_view_image_textView)
            val name=holder.itemView.findViewById<TextView>(R.id.name)
/*
            if(text?.text==""){
                text?.visibility=View.GONE
            }
            else{
                text?.text="${chats.get(position).text}"
            }
*/
            text?.text="${chats.get(position).text}"

            name.text="${chats.get(position).user}"
            val image=holder.itemView.findViewById<ImageView>(R.id.Recycler_view_image_imageView)

            if(chats.get(position).image_url!=""){
                println("nulllll değil...")
                Picasso.get().load(chats.get(position).image_url).into(image)
                println("*******************************************************("+chats.get(position).image_url+")")
            }


        }
        //text için recyclerview gösterileccek
        else{

            if(chats.get(position).image_url==""){
                val image=holder.itemView.findViewById<ImageView>(R.id.Recycler_view_image_imageView)
              image.visibility=View.GONE
            }
            val text:TextView?=holder.itemView.findViewById<TextView>(R.id.Recycler_view_image_textView)
            val name=holder.itemView.findViewById<TextView>(R.id.name)
            name.text="${chats.get(position).user}"
            text?.text="${chats.get(position).text}"
        }





    }

    override fun getItemCount(): Int {

        return chats.size
    }
}