package ba.etf.nrsprojekat.view

import android.content.Context
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
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class KorisnikAdapter(
    private var korisnikList:List<Korisnik>,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val context: Context,
    private val brojKorisnikaTextView: TextView
): RecyclerView.Adapter<KorisnikAdapter.KorisnikViewHolder>() {

    inner class KorisnikViewHolder(KorisnikView: View) : RecyclerView.ViewHolder(KorisnikView) {
    val emailTextView = itemView.findViewById<TextView>(R.id.emailTextView)
    val editDugme = itemView.findViewById<MaterialButton>(R.id.editKorisnikDugme)
    val deleteDugme = itemView.findViewById<MaterialButton>(R.id.deleteKorisnikDugme)
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
        holder.deleteDugme.setOnClickListener {
            showConfirmation(korisnikList[position].getID(), position)

        }
    }

    fun updateUsers(users: List<Korisnik>) {
        korisnikList = users.sortedWith(compareBy<Korisnik> { it.createdAt }.reversed()).toMutableList()
        brojKorisnikaTextView.text = users.size.toString()
        notifyDataSetChanged()
    }

    private fun openEditUser(id: String) {
        val intent = Intent(fragmentActivity, AddUserActivity::class.java)
            intent.putExtra("Bool", "true")
            intent.putExtra("userID", id)
        activityResultLauncher.launch(intent)
    }

    private fun showConfirmation(korisnikID: String, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Izbriši korisnika?")
            .setMessage("Da li želite izbrisati korisnika ${korisnikList[position].getEmail()}?")

            .setNegativeButton("Odustani") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Izbriši") { dialog, which ->
                UserService.deleteUser(korisnikID) {result ->
                    if(result) {
                        dialog.dismiss()
                        updateUsers(UserService.users)
                        Snackbar.make(brojKorisnikaTextView, "Korisnik uspješno obrisan!", Snackbar.LENGTH_LONG)
                            .setAction("OK") { }
                            .show()
                    }
                }
            }
            .show()
    }
}

