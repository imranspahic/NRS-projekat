package ba.etf.nrsprojekat.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.*
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton

class BranchListAdapter(private var branches : List<Branch>,
                        private var brojPoslovnica: TextView,
                        private val fragmentActivity: FragmentActivity,
                        private val activityResultLauncher: ActivityResultLauncher<Intent>
                        ) : RecyclerView.Adapter<BranchListAdapter.BranchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.branch_item,parent,false)
        return BranchViewHolder(view)
    }
    override fun getItemCount(): Int = branches.size

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        val branch : Branch = branches[position]

        holder.branchName.text =branch.nazivPoslovnice
        brojPoslovnica.text = branches.size.toString()
        holder.editBranchButton.setOnClickListener {
            openEditProduct(branch.id)
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v:View?){
                val activity=v!!.context as AppCompatActivity
                val intent = Intent(v.getContext(), PregledUPoslovnicamaActivity::class.java)
                intent.putExtra("Poslovnice",branch.nazivPoslovnice);
                v.getContext().startActivity(intent)
            }
        })
    }
    fun updateBranches(branches: List<Branch>) {
        this.branches = branches.sortedWith(compareBy<Branch> { it.updatedAt }.reversed())
        brojPoslovnica.text = branches.size.toString()
        notifyDataSetChanged()
    }

    inner class BranchViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val branchName : TextView = itemView.findViewById(R.id.branchName)
        var editBranchButton: MaterialButton = itemView.findViewById(R.id.editBranchButton)
    }

    private fun openEditProduct(productID: String) {
        val intent = Intent(fragmentActivity,AddBranchActivity::class.java).apply {
            putExtra("branches", productID)
        }
        activityResultLauncher.launch(intent)
    }
}