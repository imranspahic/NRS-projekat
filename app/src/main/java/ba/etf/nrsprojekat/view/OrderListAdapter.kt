package ba.etf.nrsprojekat.view

import android.content.Context
import android.content.Intent
import android.util.Log
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
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.DateFormat
import java.time.format.DateTimeFormatter

class OrderListAdapter(
    private var orders: MutableList<Narudzba>,
    private val context: Context,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val brojNarudzbiTextView: TextView
) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.OrderViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(view)
    }
    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderListAdapter.OrderViewHolder, position: Int) {
        this.orders = orders.sortedWith(compareBy<Narudzba> { it.datumNarucivanja }.reversed()).toMutableList()
                holder.orderName.text = orders[position].nazivNarudzbe
                holder.orderStatus.text = orders[position].status
                holder.datumNarucivanja.text = DateFormat.getDateInstance()
                .format(orders[position].datumNarucivanja)
                Log.d("orders", orders[position].proizvodi.toString())


        holder.infoOrderButton.setOnClickListener {
            orders[position].imaFiskalni = true
            OrderServices.updateRacun(orders[position].id)
            notifyDataSetChanged()
            openInfoOrder(orders[position].id)
        }
        OrderServices.getRacunStatus(orders[position].id) {
            if(!it) {
                holder.editOrderButton.visibility = View.VISIBLE
                holder.editOrderButton.setOnClickListener {
                    //    fragmentActivity.supportFragmentManager
                    //        .beginTransaction().replace(R.id.frame, FragmentProducts()).commitNow()
                    var mapaProizvodiNarudzbe = mutableMapOf<String, Any>()
                    for(item in orders[position].proizvodi)
                    //   println("" + item["productID"].toString() + item["quantity"].toString().toInt())
                        mapaProizvodiNarudzbe.put(item["productID"].toString(), item["quantity"].toString())
                    OrderServices.id = orders[position].id
                    OrderServices.imeTrenutneNarudzbe = orders[position].nazivNarudzbe
                    OrderServices.mapaZaNarudzbu = mapaProizvodiNarudzbe
                    fragmentActivity.findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.proizvodi


                }

            }
            else
                holder.editOrderButton.visibility = View.GONE
        }

            /*

            holder.editOrderButton.visibility = View.VISIBLE
            holder.editOrderButton.setOnClickListener {
            //    fragmentActivity.supportFragmentManager
            //        .beginTransaction().replace(R.id.frame, FragmentProducts()).commitNow()
                var mapaProizvodiNarudzbe = mutableMapOf<String, Any>()
                for(item in orders[position].proizvodi)
                 //   println("" + item["productID"].toString() + item["quantity"].toString().toInt())
                     mapaProizvodiNarudzbe.put(item["productID"].toString(), item["quantity"].toString())
                     OrderServices.id = orders[position].id
                     OrderServices.imeTrenutneNarudzbe = orders[position].nazivNarudzbe
                     OrderServices.mapaZaNarudzbu = mapaProizvodiNarudzbe
                     fragmentActivity.findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.proizvodi

        }
        else
            holder.editOrderButton.visibility = View.GONE */



    }


    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderName: TextView = itemView.findViewById(R.id.orderName)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val infoOrderButton: MaterialButton = itemView.findViewById(R.id.InfoOrderDugme)
        val datumNarucivanja: TextView = itemView.findViewById(R.id.orderDateText)
        val editOrderButton: MaterialButton = itemView.findViewById(R.id.EditOrderDugme)


    }

    private fun openInfoOrder(OrderID: String) {
        val intent = Intent(fragmentActivity, OrderInfoActivity::class.java).apply {
            putExtra("OrderID", OrderID)
        }
        activityResultLauncher.launch(intent)
    }



    fun updateOrders(orders: MutableList<Narudzba>) {
        this.orders = orders.sortedWith(compareBy<Narudzba> { it.datumNarucivanja }.reversed()).toMutableList()
        brojNarudzbiTextView.text = this.orders.size.toString()
        notifyDataSetChanged()
    }




    }






