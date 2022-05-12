package ba.etf.nrsprojekat.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.AddUserActivity
import ba.etf.nrsprojekat.MainActivity2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService

class BranchListAdapter(private var branches : List<Branch>,


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
        ProductsService.FilterProducts("Mostar"){
            //it ->
        }
    }



    public class BranchViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val branchName : TextView = itemView.findViewById(R.id.branchName)


    }
}