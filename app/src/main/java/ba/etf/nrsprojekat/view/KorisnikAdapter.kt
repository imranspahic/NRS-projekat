package ba.etf.nrsprojekat.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik

class KorisnikAdapter(KorisnikList:List<Korisnik>,
                      private val listener: OnItemClickListener
): RecyclerView.Adapter<KorisnikAdapter.KorisnikViewHolder>() {
    private var korisnikList = KorisnikList
    inner class KorisnikViewHolder(KorisnikView: View) : RecyclerView.ViewHolder(KorisnikView), View.OnClickListener {
    val emailTextView = itemView.findViewById<TextView>(R.id.emailTextView)
    val passwordTextView = itemView.findViewById<TextView>(R.id.passwordTextView)
    val isAdminTextView = itemView.findViewById<TextView>(R.id.isAdminTextView)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
            listener.onItemClick(position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KorisnikViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return KorisnikViewHolder(view)
    }

    override fun getItemCount(): Int {
        return korisnikList.size
    }

    override fun onBindViewHolder(holder: KorisnikViewHolder, position: Int) {
        holder.emailTextView.text = korisnikList[position].getEmail()
        holder.passwordTextView.text = korisnikList[position].getPassword()
        if(korisnikList[position].isAdmin() == true) holder.isAdminTextView.text = "(Admin)"
        else holder.isAdminTextView.text = "(Korisnik)"
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    }

