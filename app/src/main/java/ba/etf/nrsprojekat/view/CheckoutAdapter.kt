package ba.etf.nrsprojekat.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService.products
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

class CheckoutAdapter(
    private var proizvodi: List<MutableMap<String, Any>>,
    private var orders: List<Narudzba>
) : RecyclerView.Adapter<CheckoutAdapter.OrderViewHolder>() {
    private val noDecimalFormat: NumberFormat = DecimalFormat.getInstance()
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
        holder.cijenaTextView.text = String.format("%.2f",proizvodi[position].get("productPrice")).replace(".",",") + " KM"
        var iznos = (proizvodi[position].get("quantity").toString().toInt() * proizvodi[position].get("productPrice").toString().toDouble())
        iznos = (iznos * 100.0).roundToInt() / 100.0
        holder.ukupnaCijenaTextView.text = String.format("%.2f", iznos).replace(".",",") + " KM"

        if( proizvodi[position].get("productPdvCategory").toString() == null) {
            holder.productPdvlinear.visibility = View.GONE
            holder.cijenaBezPdvLinear.visibility = View.GONE
            holder.PdvOdCijeneLinear.visibility = View.GONE
        }
        else {
            holder.productPdvlinear.visibility = View.VISIBLE
            val pdvCategory: PdvCategory? = PdvCategoriesService.pdvCategories.firstOrNull() { category -> category.name == proizvodi[position].get("productPdvCategory").toString() }
            if(pdvCategory == null) {
                holder.productPdvlinear.visibility = View.GONE
                holder.cijenaBezPdvLinear.visibility = View.GONE
                holder.PdvOdCijeneLinear.visibility = View.GONE
                holder.ukupnaCijenaSaPDVTextView.text = String.format("%.2f", iznos).replace(".",",") + " KM"
            }
            else {
                holder.PDVprocenat.text = noDecimalFormat.format(
                    pdvCategory.pdvPercent
                ) + "%"

                var iznospdv = (iznos * noDecimalFormat.format(pdvCategory.pdvPercent).toString().toInt())/100
                iznospdv = (iznospdv * 100.0).roundToInt() / 100.0
                holder.iznosPDVa.text = String.format("%.2f", iznospdv).replace(".",",") + " KM"
                var iznosSaPdv = iznos + iznospdv
                iznosSaPdv = (iznosSaPdv * 100.0).roundToInt() / 100.0
                holder.ukupnaCijenaSaPDVTextView.text = String.format("%.2f", iznosSaPdv).replace(".",",") + " KM"
            }
        }


    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imeProizvodaTextView: TextView = itemView.findViewById(R.id.imeProizvoda)
        val kolicinaTextView: TextView = itemView.findViewById(R.id.kolicinaTextView)
        val cijenaTextView: TextView = itemView.findViewById(R.id.cijenaTextView)
        val ukupnaCijenaTextView: TextView = itemView.findViewById(R.id.ukupnaCijenaProizvodaTextView)
        val PDVprocenat: TextView = itemView.findViewById(R.id.productPdvPercent)
        val productPdvlinear: LinearLayout = itemView.findViewById(R.id.productPdvLinear)
        val cijenaBezPdvLinear: LinearLayout = itemView.findViewById(R.id.cijenaBezPdvLinear)
        val PdvOdCijeneLinear: LinearLayout = itemView.findViewById(R.id.PdvOdCijeneLinear)
        val iznosPDVa: TextView  = itemView.findViewById(R.id.iznosPDVaTextView)
        val ukupnaCijenaSaPDVTextView: TextView = itemView.findViewById(R.id.ukupnaCijenaProizvodaSaPDVTextView)
    }
}