package ba.etf.nrsprojekat.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.PromijeniStatusDostaveActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.data.models.receivedProducts
import ba.etf.nrsprojekat.services.ProductsService

class ProizvodiUPoslovniciAdapter(private var productList : List<Product>) : RecyclerView.Adapter<ProizvodiUPoslovniciAdapter.PregledUPoslovniciViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregledUPoslovniciViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.proizvod_u_posl_list_item,parent,false)
        return PregledUPoslovniciViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PregledUPoslovniciViewHolder, position: Int) {
        val proizvodi : Product = productList[position]
        holder.name.text = proizvodi.name
        // holder.poslovnica.text = proizvodi.poslovnicaName
        holder.kolicina.text= proizvodi.quantity.toString()
        holder.status.text = proizvodi.status
        holder.cijena.text = proizvodi.price.toString()
        holder.pdv.text = proizvodi.pdvCategoryName
        //ProductsService.FilterProducts("Mostar")


    }

    override fun getItemCount(): Int {
        return productList.size
    }
    public class PregledUPoslovniciViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.productNamePoslovnica)
        val kolicina : TextView = itemView.findViewById(R.id.productQuantityPoslovnica)
        val status : TextView = itemView.findViewById(R.id.productStatusPoslovnica)
        val pdv : TextView = itemView.findViewById(R.id.productPdvNamePosl)
        val cijena : TextView = itemView.findViewById(R.id.productPricePosl)

    }
}