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
import ba.etf.nrsprojekat.MainActivity2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Product

class DostavaAdapter(private var productList : List<Product>) : RecyclerView.Adapter<DostavaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dostava_proizvodi_fragment,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val proizvodi : Product = productList[position]
        holder.name.text = proizvodi.name
        holder.poslovnica.text = proizvodi.poslovnicaName
        holder.kolicina.text= proizvodi.quantity.toString()
        holder.status.text = proizvodi.status
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v:View?){
                val activity=v!!.context as AppCompatActivity
                val newFragment = FragmentStatusDostave(proizvodi)
                (activity as MainActivity2?)?.openFragment(newFragment)
            }
        })
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    public class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.proizvodDostava)
        val poslovnica : TextView = itemView.findViewById(R.id.poslovnicaDostava)
        val kolicina : TextView = itemView.findViewById(R.id.kolicinaDostava)
        val status : TextView = itemView.findViewById(R.id.statusDostava)

    }
}