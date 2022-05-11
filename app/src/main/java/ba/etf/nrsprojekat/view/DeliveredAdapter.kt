package ba.etf.nrsprojekat.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.deliveredProducts

class DeliveredAdapter(private var deliveredList : List<deliveredProducts>) : RecyclerView.Adapter<DeliveredAdapter.MyViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dostava_proizvodi_fragment,parent,false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val proizvodi : deliveredProducts = deliveredList[position]
            holder.name.text = proizvodi.name
            holder.poslovnica.text = proizvodi.poslovnicaName
            holder.kolicina.text= proizvodi.quantity.toString()
            holder.status.text = proizvodi.status
        }

        override fun getItemCount(): Int {
            return deliveredList.size
        }
        public class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
            val name : TextView = itemView.findViewById(R.id.proizvodDostava)
            val poslovnica : TextView = itemView.findViewById(R.id.poslovnicaDostava)
            val kolicina : TextView = itemView.findViewById(R.id.kolicinaDostava)
            val status : TextView = itemView.findViewById(R.id.statusDostava)

        }
        fun updateProducts(products: List<deliveredProducts>) {
            this.deliveredList = products
            notifyDataSetChanged()
        }

    }