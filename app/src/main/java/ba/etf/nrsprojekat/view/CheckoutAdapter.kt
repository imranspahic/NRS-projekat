package ba.etf.nrsprojekat.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import org.w3c.dom.Text

class CheckoutAdapter(
    private var proizvodi: List<MutableMap<String, Any>>,
    private var orders: List<Narudzba>
    ) : RecyclerView.Adapter<CheckoutAdapter.OrderViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.OrderViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.checkout_single_product_item, parent, false)
            return OrderViewHolder(view)
    }
    override fun getItemCount(): Int = proizvodi.size

    override fun onBindViewHolder(holder: CheckoutAdapter.OrderViewHolder, position: Int) {
        //this.orders = orders.sortedWith(compareBy<Narudzba> { it.datumNarucivanja }.reversed())
        //if(proizvodi[position].isDeleted == false) {
        //if(brojac < proizvodi[0].proizvodi.size) {
        //Log.d("oki", proizvodi.get(1).get("productName").toString())
        //Log.d("oki", proizvodi[position].quantity.toString())

        //Log.d("oki", orders[0].lokacija.toString())

            holder.orderNameAndQuantity.text = proizvodi.get(position).get("quantity").toString() + "x  " + proizvodi.get(position).get("productName")
            holder.prices.text = proizvodi.get(position).get("productPrice").toString() + "       " + proizvodi.get(position).get("productPrice").toString().toDouble() * proizvodi.get(position).get("quantity").toString().toInt()
    //           holder.datum.text = orders[0].datumNarucivanja.toString()
   //         holder.lokacija.text = orders[0].lokacija
     //       holder.mjesto.text = orders[0].mjesto
    //}
  //          holder.datumNarucivanja.text = DateFormat.getDateInstance()
//                .format(proizvodi[position].datumNarucivanja)
        //}
        //Log.d("orders", orders[position].proizvodi.toString())
    }

inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val orderNameAndQuantity: TextView = itemView.findViewById(R.id.quantityAndName)
    //val datumNarucivanja: TextView = itemView.findViewById(R.id.orderDateText)
    val prices: TextView = itemView.findViewById(R.id.initialAndFinalPrice)
    //val datum: TextView = itemView.findViewById(R.id.datumRacun)
    //val lokacija : TextView = itemView.findViewById(R.id.lokacijaRacun)
    //val mjesto : TextView = itemView.findViewById(R.id.stoRacun)
}
}