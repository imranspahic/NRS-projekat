package ba.etf.nrsprojekat.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.services.BranchesService
import com.google.android.material.button.MaterialButton

class MjestoListAdapter(
    public var mjesta: MutableList<String>,
  /*  private val context: Context,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val brojNarudzbiTextView: TextView */
) : RecyclerView.Adapter<MjestoListAdapter.MjestoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MjestoListAdapter.MjestoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.mjesto_list_item, parent, false)
        return MjestoViewHolder(view)
    }

    override fun getItemCount(): Int = mjesta.size

    override fun onBindViewHolder(holder: MjestoListAdapter.MjestoViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.mjestoText.setText(mjesta[position])
        holder.mjestoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mjesta[position] = holder.mjestoText.text.toString()
                BranchesService.mjesta = mjesta

            }
        })
        holder.deleteMjestoButton.setOnClickListener {
            mjesta.removeAt(position)
            BranchesService.mjesta = mjesta
            notifyDataSetChanged()
        }
    }

    inner class MjestoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mjestoText: EditText = itemView.findViewById(R.id.tekstZaMjesto)
        val deleteMjestoButton: MaterialButton = itemView.findViewById(R.id.izbrisiMjestoBtn)
    }

    fun updateMjesta(mjesta: MutableList<String>) {
        this.mjesta = mjesta
        notifyDataSetChanged()
    }
}