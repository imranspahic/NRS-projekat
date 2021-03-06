package ba.etf.nrsprojekat.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firestore.v1.StructuredQuery
import org.simpleframework.xml.Order
import java.text.DecimalFormat
import java.text.NumberFormat

class ProductListAdapter(
    private var products: List<Product>,
    private val context: Context,
    private val fragmentActivity: FragmentActivity,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val brojProizvodaTextView: TextView,
    private val saveOrderDugme: MaterialButton,
    private val mListener: IHide,
    private val searchProductsField: TextInputEditText,

) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    var mapaProizvodaZaNarudzbu = mutableMapOf<String, Any>()
    private val noDecimalFormat: NumberFormat = DecimalFormat.getInstance()

    init {
        noDecimalFormat.maximumFractionDigits = 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ProductViewHolder(view, mListener)
    }
    override fun getItemCount(): Int = products.size
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = products[position]
        holder.productName.text = product.name
        holder.productPoslovnica.text = product.poslovnicaName
        if(product.rinfuza == "") {
            holder.productQuantity.text = product.quantity.toString()
        }
        else {
            holder.productQuantity.text = product.rinfuza.toString()
        }

        holder.productStatus.text = product.status
        holder.productPrice.text = String.format("%.2f", product.price) + " KM"

        if(product.quantity == 0) {
            product.status = "nema na stanju"
            holder.productStatus.text = product.status
            ProductsService.updateProductStatus(product.id, "nema na stanju")
        }

        if(product.status.lowercase() != "dostupno") {
            holder.productDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.productStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red)))
        }
        else {
            holder.productDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_green))
            holder.productStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.main_green)))
        }


        holder.editDugme.setOnClickListener {
            openEditProduct(product.id)
        }

        holder.deleteDugme.setOnClickListener {
            showConfirmationDialog(product.id)
        }

        if(product.pdvCategoryName == null) {
            holder.productPdvlinear.visibility = View.GONE
        }
        else {
            holder.productPdvlinear.visibility = View.VISIBLE
            holder.productPdvName.text = product.pdvCategoryName
            val pdvCategory: PdvCategory? = PdvCategoriesService.pdvCategories.firstOrNull() { category -> category.name == product.pdvCategoryName }
            if(pdvCategory == null) holder.productPdvlinear.visibility = View.GONE
            else {
                holder.productPdvPercent.text = noDecimalFormat.format(
                    pdvCategory.pdvPercent
                )+ "%"
            }
        }

        if(!LoginService.logovaniKorisnik!!.isAdmin()) {
            holder.editDugme.visibility = View.GONE
            holder.deleteDugme.visibility = View.GONE
            holder.addProductToOrderDugme.visibility = View.GONE
            holder.substractProductToOrderDugme.visibility = View.GONE
            holder.quantityEdit.visibility = View.GONE


        }
        if(OrderServices.imeTrenutneNarudzbe != null && product.status == "dostupno") {

            if(product.rinfuza != "") {
                holder.quantityEdit.visibility = View.VISIBLE
                holder.quantityEdit.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        var trenutnaVrijednost = holder.quantityEdit.text.toString()
                        if(trenutnaVrijednost != "") {
                            product.kolicinaNarudzbe = trenutnaVrijednost.toInt()
                            OrderServices.mapaZaNarudzbu.put(product.id, product.kolicinaNarudzbe)
                        }

                    }
                })
            }
            else {
                holder.addProductToOrderDugme.visibility = View.VISIBLE
                holder.substractProductToOrderDugme.visibility = View.VISIBLE
                holder.quantityEdit.visibility = View.VISIBLE
                brojProizvodaTextView.text = OrderServices.imeTrenutneNarudzbe
            }
            if(OrderServices.mapaZaNarudzbu.get(product.id) != null) {
                product.kolicinaNarudzbe = OrderServices.mapaZaNarudzbu.get(product.id).toString().toInt()
                holder.quantityEdit.setText(OrderServices.mapaZaNarudzbu.get(product.id).toString())
            }
            else {
                product.kolicinaNarudzbe = 0
                holder.quantityEdit.setText("0")
            }
        }
        else {
            holder.addProductToOrderDugme.visibility = View.GONE
            holder.substractProductToOrderDugme.visibility = View.GONE
            holder.quantityEdit.visibility = View.GONE
        }
        holder.addProductToOrderDugme.setOnClickListener {
            var trenutnaVrijednost = holder.quantityEdit.text.toString().toInt()
            if(trenutnaVrijednost + 1 > product.quantity) { }
            else {
                holder.quantityEdit.setText((++trenutnaVrijednost).toString())
                product.kolicinaNarudzbe = trenutnaVrijednost
                OrderServices.mapaZaNarudzbu.put(product.id, product.kolicinaNarudzbe)
            }
        }
        holder.substractProductToOrderDugme.setOnClickListener {
            var trenutnaVrijednost = holder.quantityEdit.text.toString().toInt()
            if(trenutnaVrijednost - 1 < 0) {}
            else {
                holder.quantityEdit.setText((--trenutnaVrijednost).toString())
                product.kolicinaNarudzbe = trenutnaVrijednost
                OrderServices.mapaZaNarudzbu.put(product.id, product.kolicinaNarudzbe)
            }
        }
        saveOrderDugme.setOnClickListener {
            showConfirmationDialogForOrderFinish()

        }
    }


    fun updateProducts(products: List<Product>, updateBrojProizvodaText: Boolean = true) {
        this.products = products.sortedWith(compareBy<Product> { it.createdAt }.reversed())
        if(updateBrojProizvodaText) brojProizvodaTextView.text = products.size.toString()
        notifyDataSetChanged()
    }
    inner class ProductViewHolder(itemView: View, mListener: IHide) : RecyclerView.ViewHolder(itemView) {
        var mListener: IHide = mListener
        var productDivider: View = itemView.findViewById(R.id.orderDivider)
        var productName: TextView = itemView.findViewById(R.id.productName)
        var productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        var productPoslovnica: TextView = itemView.findViewById(R.id.productPoslovnica)
        var productStatus: TextView = itemView.findViewById(R.id.productStatus)
        var editDugme: MaterialButton = itemView.findViewById(R.id.editProductDugme)
        var deleteDugme: MaterialButton = itemView.findViewById(R.id.deleteProductDugme)
        var addProductToOrderDugme: MaterialButton = itemView.findViewById(R.id.addProductToOrderDugme)
        var substractProductToOrderDugme: MaterialButton = itemView.findViewById(R.id.substractProductToOrderDugme)
        var quantityEdit: EditText = itemView.findViewById(R.id.quantityEdit)
        var productPdvlinear: LinearLayout = itemView.findViewById(R.id.productPdvLinear)
        var productPdvName: TextView = itemView.findViewById(R.id.productPdvName)
        var productPdvPercent: TextView = itemView.findViewById(R.id.productPdvPercent)
        var productPrice: TextView = itemView.findViewById(R.id.productPrice)
    }

    private fun openEditProduct(productID: String) {
        val intent = Intent(fragmentActivity, AddProductActivity::class.java).apply {
            putExtra("productID", productID)
        }
        activityResultLauncher.launch(intent)
    }

    private fun showConfirmationDialog(productID: String) {
        val product: Product = ProductsService.products.firstOrNull { product -> product.id == productID } ?: return
        MaterialAlertDialogBuilder(context)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Izbri??i proizvod?")
            .setMessage("Da li ??elite izbrisati proizvod ${product.name}?")

            .setNegativeButton("Odustani") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Izbri??i") { dialog, which ->
                onDeleteProduct(productID, dialog)

            }
            .show()
    }

    private fun onDeleteProduct(productID: String, dialog: DialogInterface) {
        ProductsService.deleteProduct(productID) {
                result ->
            if(result) {
                dialog.dismiss()
                updateProducts(ProductsService.products)
                if(searchProductsField.text.toString().isEmpty()) {
                    Log.d("search", "reseting products, size = ${ProductsService.products.size}")
                }
                else {
                    val searchedProducts: List<Product> = ProductsService.products.filter {
                            p ->
                        Log.d("search", "product = ${p.name.lowercase()}")
                        p.name.lowercase().contains(searchProductsField.text.toString().lowercase()) }
                    Log.d("search", "Broj proizvoda = ${searchedProducts.size}")
                    updateProducts(searchedProducts, updateBrojProizvodaText = false)
                }
                Snackbar.make(brojProizvodaTextView, "Proizvod uspje??no obrisan!", Snackbar.LENGTH_LONG)
                    .setAction("OK") { }
                    .show()
            }
        }
    }

    private fun showConfirmationDialogForOrderFinish() {
        MaterialAlertDialogBuilder(context)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Sa??uvaj narud??bu?")
            .setMessage("Da li ??elite sa??uvati narud??bu?")

            .setNegativeButton("Odustani") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Sa??uvaj") { dialog, which ->
                if(OrderServices.edit == true) {
                    for(item in OrderServices.editMapa) {
                    ProductsService.getProduct(item.key) {
                        for(proiz in products) if(proiz.id == item.key) proiz.quantity = it + item.value.toString().toInt()
                        notifyDataSetChanged()
                        ProductsService.updateProductQuantity(item.key, (it + item.value.toString().toInt()))

                    }
                    }
                        OrderServices.edit = false
                        OrderServices.editMapa = mutableMapOf<String, Any>()
                }
                for(item in products)
                    if(item.kolicinaNarudzbe != 0) {
                        mapaProizvodaZaNarudzbu.put(item.id, item.kolicinaNarudzbe)
                        if(item.rinfuza == "") {
                            ProductsService.updateProductQuantity(
                                item.id,
                                item.quantity - item.kolicinaNarudzbe
                            )
                            item.quantity = item.quantity - item.kolicinaNarudzbe
                            item.kolicinaNarudzbe = 0
                        }
                        else {
                            var broj = item.rinfuza?.filter {it.isDigit()}?.toInt()
                            var mjernaJedinica = item.rinfuza?.substring(broj.toString().length)
                            ProductsService.updateProductRinf(item.id, (broj!! - item.kolicinaNarudzbe).toString() + mjernaJedinica)
                            item.rinfuza = (broj!! - item.kolicinaNarudzbe).toString() + mjernaJedinica
                            item.kolicinaNarudzbe = 0
                        }
                    }
                if(OrderServices.id != null) {
                    OrderServices.deleteOrder(OrderServices.id.toString()) {}
                //    for(item in OrderServices.mapaZaNarudzbu)
                }
                OrderServices.addOrder(OrderServices.imeTrenutneNarudzbe.toString(), "pending", LoginService.logovaniKorisnik!!.getID().toString(), mapaProizvodaZaNarudzbu)
                OrderServices.imeTrenutneNarudzbe = null
                mListener.HideBtn()
                mapaProizvodaZaNarudzbu = mutableMapOf<String, Any>()
                OrderServices.mapaZaNarudzbu = mutableMapOf<String, Any>()
                OrderServices.lokacija = null
                OrderServices.mjesto = null
                OrderServices.id = null
                notifyDataSetChanged()
            }
            .show()
    }

    interface IHide {
        fun HideBtn()
    }

    /*
    val pdvCategoryBottomSheet = PdvCategoryBottomSheetFragment(null, brojKategorijaText, pdvCategoriesListAdapter)
        pdvCategoryBottomSheet.show(supportFragmentManager, PdvCategoryBottomSheetFragment.TAG)
     */

}