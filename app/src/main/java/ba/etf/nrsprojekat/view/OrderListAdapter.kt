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
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.data.models.Product

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
        holder.orderName.text = orders[position].nazivNarudzbe
        holder.orderStatus.text = orders[position].status


    }


    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderName: TextView = itemView.findViewById(R.id.orderName)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)

    }


    }

