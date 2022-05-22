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
        holder.imeProizvodaTextView.text = proizvodi[position].get("productName").toString()
        holder.kolicinaTextView.text = proizvodi[position].get("quantity").toString() + "x"
        holder.cijenaTextView.text = proizvodi[position].get("productPrice").toString() + "KM"
        holder.ukupnaCijenaTextView.text =
            (proizvodi[position].get("quantity").toString().toInt() * proizvodi[position].get("productPrice").toString().toDouble()).toString() + "KM"
    }

inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imeProizvodaTextView: TextView = itemView.findViewById(R.id.imeProizvoda)
    val kolicinaTextView: TextView = itemView.findViewById(R.id.kolicinaTextView)
    val cijenaTextView: TextView = itemView.findViewById(R.id.cijenaTextView)
    val ukupnaCijenaTextView: TextView = itemView.findViewById(R.id.ukupnaCijenaProizvodaTextView)
}
}