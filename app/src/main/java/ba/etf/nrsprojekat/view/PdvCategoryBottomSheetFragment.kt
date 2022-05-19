package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.services.PdvCategoriesService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat


class PdvCategoryBottomSheetFragment(
    private val pdvCategoryID: String?,
    private val brojKategorijaText: TextView,
    private val pdvCategoryListAdapter: PdvCategoriesListAdapter
) : BottomSheetDialogFragment() {

    private lateinit var categoryNameField: TextInputEditText
    private lateinit var categoryPercentField: TextInputEditText
    private lateinit var saveDugme: MaterialButton
    private val noDecimalFormat: NumberFormat = DecimalFormat.getInstance()


    init {
        noDecimalFormat.maximumFractionDigits = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view = inflater.inflate(R.layout.pdv_category_bottom_sheet, container, false)
        categoryNameField = view.findViewById(R.id.addPdvCategoryNameField)
        categoryPercentField = view.findViewById(R.id.addPdvCategoryPercentField)
        saveDugme = view.findViewById(R.id.addPdvCategorySaveDugme)
        saveDugme.isEnabled = false

        categoryNameField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState()
            }
        })

        categoryPercentField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState()
            }
        })

        if(pdvCategoryID != null) {
            initializePdvCategoryData(pdvCategoryID)
        }

        saveDugme.setOnClickListener {
            onSaveDugme(pdvCategoryID)
        }
      return view
    }

    companion object {
        const val TAG = "PdvCategoryBottomSheet"
    }

    private fun checkButtonState() {
        var pdvCategoryNameCondition = categoryNameField.text?.isNotEmpty() ?: false
        var pdvCategoryPercentCondition =  categoryPercentField.text?.isNotEmpty() ?: false &&
                !categoryPercentField.text.toString().startsWith("0") &&
                categoryPercentField.text.toString().toDouble() <100
                saveDugme.isEnabled = pdvCategoryNameCondition && pdvCategoryPercentCondition
    }

    private fun initializePdvCategoryData(pdvCategoryID: String) {
        val pdvCategory: PdvCategory = PdvCategoriesService.pdvCategories.first { category -> category.id == pdvCategoryID }
        categoryNameField.setText(pdvCategory.name)
        categoryPercentField.setText(noDecimalFormat.format(pdvCategory.pdvPercent))
        saveDugme.text = "SAČUVAJ"
        saveDugme.isEnabled = true
    }

    private fun onSaveDugme(pdvCategoryID: String?) {
        //ADING CATEGORY
         if(pdvCategoryID == null) {
            PdvCategoriesService.addCategory(
                categoryNameField.text.toString(),
                categoryPercentField.text.toString()
            ) {
                result -> dismissBottomSheet()
            }
         }
        else {
             PdvCategoriesService.updateCategory(
                 pdvCategoryID,
                 categoryNameField.text.toString(),
                 categoryPercentField.text.toString()
             ) {
                     result -> dismissBottomSheet()
             }
         }
    }

    private fun dismissBottomSheet() {
        dismiss()
        pdvCategoryListAdapter.updatePdvCategories(PdvCategoriesService.pdvCategories)
        var message = ""
        message = if(pdvCategoryID == null) {
            "PDV kategorija uspješno dodana!"
        } else {
            "PDV kategorija uspješno ažurirana!"
        }
        Snackbar.make(brojKategorijaText, message, Snackbar.LENGTH_LONG)
            .setAction("OK") { }
            .show()
    }

}