package ba.etf.nrsprojekat.view

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

class PdvCategoriesListAdapter(
    private var pdvCategories: List<PdvCategory>,
    private val context: Context,
    private val supportFragmentManager: FragmentManager,
    private val brojKategorijaTextView: TextView,

) : RecyclerView.Adapter<PdvCategoriesListAdapter.PdvCategoryViewHolder>() {

    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
    private val noDecimalFormat: NumberFormat = DecimalFormat.getInstance()

    init {
        noDecimalFormat.maximumFractionDigits = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdvCategoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.pdv_category_item, parent, false)
        return PdvCategoryViewHolder(view)
    }
    override fun getItemCount(): Int = pdvCategories.size

    override fun onBindViewHolder(holder: PdvCategoryViewHolder, position: Int) {
        val pdvCategory: PdvCategory = pdvCategories[position]


        holder.pdvCategoryName.text = pdvCategory.name

        holder.pdvCategoryDateUpdated.text = simpleDateFormat.format(pdvCategory.updatedAt)

        holder.pdvCategoryDateCreated.text = simpleDateFormat.format(pdvCategory.createdAt)

        holder.pdvCategoryPercent.text = noDecimalFormat.format(pdvCategory.pdvPercent) + "%"

        val categoryProductsCount = ProductsService.products.filter { product ->
            product.pdvCategoryName != null
                    &&  product.pdvCategoryName == pdvCategory.name }.size

        holder.pdvCategoryProductNumber.text = categoryProductsCount.toString()

        if(categoryProductsCount == 0) {
            holder.pdvCategoryDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.pdvCategoryProductNumber.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red)))
        } else {
            holder.pdvCategoryDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_green))
            holder.pdvCategoryProductNumber.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.main_green)))
        }

        holder.pdvCategoryEditDugme.setOnClickListener {
            openEditPdvCategory(pdvCategory.id)
        }

        holder.pdvCategoryDeleteDugme.setOnClickListener {
            showConfirmationDialog(pdvCategory.id)
        }
    }


    fun updatePdvCategories(newPdvCategories: List<PdvCategory>) {
        pdvCategories = newPdvCategories.sortedWith(compareBy<PdvCategory> { it.updatedAt }.reversed())
        brojKategorijaTextView.text = pdvCategories.size.toString()
        notifyDataSetChanged()
    }
    inner class PdvCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pdvCategoryDivider: View = itemView.findViewById(R.id.pdvCategoryDivider)
        var pdvCategoryName: TextView = itemView.findViewById(R.id.pdvCategoryName)
        var pdvCategoryProductNumber: TextView = itemView.findViewById(R.id.pdvCategoryProductNumber)
        var pdvCategoryDateUpdated: TextView = itemView.findViewById(R.id.pdvCategoryDateUpdated)
        var pdvCategoryDateCreated: TextView = itemView.findViewById(R.id.pdvCategoryDateCreated)
        var pdvCategoryPercent: TextView = itemView.findViewById(R.id.pdvCategoryPercent)
        var pdvCategoryEditDugme: MaterialButton = itemView.findViewById(R.id.editPdvCategoryDugme)
        var pdvCategoryDeleteDugme: MaterialButton = itemView.findViewById(R.id.deletePdvCategoryDugme)
    }

    private fun openEditPdvCategory(pdvCategoryID: String) {
        val pdvCategoryBottomSheet = PdvCategoryBottomSheetFragment(pdvCategoryID, brojKategorijaTextView, this)
        pdvCategoryBottomSheet.show(supportFragmentManager, PdvCategoryBottomSheetFragment.TAG)
    }

    private fun showConfirmationDialog(pdvCategoryID: String) {
        val pdvCategory: PdvCategory = PdvCategoriesService.pdvCategories.firstOrNull { pdvCategory -> pdvCategory.id == pdvCategoryID } ?: return
        MaterialAlertDialogBuilder(context)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Izbriši kategoriju?")
            .setMessage("Da li želite izbrisati PDV kategoriju ${pdvCategory.name}?")

            .setNegativeButton("Odustani") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Izbriši") { dialog, which ->
                onDeletePdvCategory(pdvCategoryID, dialog)
            }
            .show()
    }

    private fun onDeletePdvCategory(pdvCategoryID: String, dialog: DialogInterface) {
        PdvCategoriesService.deleteCategory(pdvCategoryID) {
                result ->
            if(result) {
                dialog.dismiss()
                updatePdvCategories(PdvCategoriesService.pdvCategories)
                Snackbar.make(brojKategorijaTextView, "PDV Kategorija uspješno obrisana!", Snackbar.LENGTH_LONG)
                    .setAction("OK") { }
                    .show()
            }
        }
    }
}