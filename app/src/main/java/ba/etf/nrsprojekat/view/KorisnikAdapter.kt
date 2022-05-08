package ba.etf.nrsprojekat.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.AddUserActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.android.material.button.MaterialButton

class KorisnikAdapter(KorisnikList:List<Korisnik>,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>
                  //    private val listener: OnItemClickListener
                     // private val fragmentActivity: FragmentActivity,
                    //  private val activityResultLauncher: ActivityResultLauncher<Intent>
): RecyclerView.Adapter<KorisnikAdapter.KorisnikViewHolder>() {

    private var korisnikList = KorisnikList
    inner class KorisnikViewHolder(KorisnikView: View) : RecyclerView.ViewHolder(KorisnikView)/*, View.OnClickListener*/ {
    val emailTextView = itemView.findViewById<TextView>(R.id.emailTextView)
    val editDugme = itemView.findViewById<MaterialButton>(R.id.editKorisnikDugme)
    val deleteDugme = itemView.findViewById<MaterialButton>(R.id.deleteKorisnikDugme)
 /*       init {
            itemView.setOnClickListener(this)
        } */

   /*     override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
            listener.onItemClick(position)
        } */
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
        holder.editDugme.setOnClickListener {
            openEditUser(korisnikList[position].getID())
        }
    }

    private fun openEditUser(id: String) {
        val intent = Intent(fragmentActivity, AddUserActivity::class.java)
            intent.putExtra("Bool", "true")
            intent.putExtra("userID", id)
        activityResultLauncher.launch(intent)


       // activityResultLauncher.launch(intent)
    }

    /*  interface OnItemClickListener {
          fun onItemClick(position: Int)
      }   */

    }

