package ba.etf.nrsprojekat.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.data.models.receivedProducts


class ReceivedAdapter(private var productList : List<receivedProducts>) : RecyclerView.Adapter<ReceivedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dostava_proizvodi_fragment,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val proizvodi : receivedProducts = productList[position]
        holder.name.text = proizvodi.name
        holder.poslovnica.text = proizvodi.poslovnicaName
        holder.kolicina.text= proizvodi.quantity.toString()
        holder.status.text = proizvodi.status
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
    fun updateProducts(products: List<receivedProducts>) {
        this.productList = products
        notifyDataSetChanged()
    }
}