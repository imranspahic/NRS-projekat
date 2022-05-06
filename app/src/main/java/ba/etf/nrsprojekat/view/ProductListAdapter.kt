package ba.etf.nrsprojekat.view

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Product
import com.google.android.material.button.MaterialButton

class ProductListAdapter(
    private var products: List<Product>,

) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ProductViewHolder(view)
    }
    override fun getItemCount(): Int = products.size
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = products[position]
        holder.productName.text = product.name
        holder.productPoslovnica.text = product.poslovnicaName
        holder.productQuantity.text = product.quantity.toString()
        holder.productStatus.text = product.status

        if(product.status.lowercase() != "dostupno") {
            holder.productDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.productStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red)))

        }
        else {
            holder.productDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_green))
            holder.productStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.main_green)))
        }


        holder.editDugme.setOnClickListener {

        }

        holder.deleteDugme.setOnClickListener {

        }
    }
    fun updateProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productDivider: View = itemView.findViewById(R.id.productDivider)
        var productName: TextView = itemView.findViewById(R.id.productName)
        var productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        var productPoslovnica: TextView = itemView.findViewById(R.id.productPoslovnica)
        var productStatus: TextView = itemView.findViewById(R.id.productStatus)
        var editDugme: MaterialButton = itemView.findViewById(R.id.editProductDugme)
        var deleteDugme: MaterialButton = itemView.findViewById(R.id.deleteProductDugme)
    }
}