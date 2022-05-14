package ba.etf.nrsprojekat.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.OrderInfoActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.OrderServices
import com.google.android.material.button.MaterialButton

class OrderListAdapter(
    private var orders: List<Narudzba>,
    private val context: Context,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
   // private val brojProizvodaTextView: TextView
) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.OrderViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(view)
    }
    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderListAdapter.OrderViewHolder, position: Int) {
        this.orders = orders.sortedWith(compareBy<Narudzba> { it.datumNarucivanja }.reversed())

            if(orders[position].isDeleted == false) {
                holder.orderName.text = orders[position].nazivNarudzbe
                holder.orderStatus.text = orders[position].status
            }

        holder.deleteOrderButton.setOnClickListener {
        OrderServices.updateOrder(orders[position].id)
            orders[position].isDeleted = true
            notifyDataSetChanged()
        }
        holder.infoOrderButton.setOnClickListener {
            openInfoOrder(orders[position].id)
        }

    }


    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderName: TextView = itemView.findViewById(R.id.orderName)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val deleteOrderButton: MaterialButton = itemView.findViewById(R.id.deleteOrderDugme)
        val infoOrderButton: MaterialButton = itemView.findViewById(R.id.InfoOrderDugme)


    }

    private fun openInfoOrder(OrderID: String) {
        val intent = Intent(fragmentActivity, OrderInfoActivity::class.java).apply {
            putExtra("OrderID", OrderID)
        }
        activityResultLauncher.launch(intent)
    }

    fun updateOrders(products: List<Product>) {
        this.orders = orders.sortedWith(compareBy<Narudzba> { it.datumNarucivanja }.reversed())
      //  brojProizvodaTextView.text = products.size.toString()
        notifyDataSetChanged()
    }


    }

