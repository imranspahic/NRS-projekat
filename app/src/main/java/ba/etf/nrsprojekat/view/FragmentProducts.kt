package ba.etf.nrsprojekat.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.PdvCategoriesActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


class FragmentProducts : Fragment(), ProductListAdapter.IHide {

    private lateinit var brojProizvodaText: TextView
    private lateinit var refreshDugme: MaterialButton
    private lateinit var addDugme: MaterialButton
    private lateinit var proizvodiRecyclerView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var addOrderDugme: MaterialButton
    private lateinit var discardOrderDugme: MaterialButton
    private lateinit var saveOrderDugme: MaterialButton
    private lateinit var pdvCategoriesDugme: MaterialButton
    private lateinit var brojProizvodaLabel: TextView
    private lateinit var searchProductsField: TextInputEditText
    private lateinit var searchProductsLayout: TextInputLayout

    private var productActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val mode = result.data!!.getStringExtra("mode")
            var message = ""
            message = if(mode == "ADD") {
                "Proizvod uspješno dodan!"
            } else {
                "Proizvod uspješno ažuriran!"
            }
            Log.d("products", "result ok")
            Snackbar.make(proizvodiRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction("OK") { }
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_products, container, false)
        brojProizvodaLabel = view.findViewById(R.id.brojProizvodaLabel)
        brojProizvodaText = view.findViewById(R.id.brojProizvoda)
        refreshDugme = view.findViewById(R.id.refreshProductDugme)
        addDugme = view.findViewById(R.id.addProductDugme)
        proizvodiRecyclerView = view.findViewById(R.id.proizvodiRecyclerView)
        addOrderDugme = view.findViewById(R.id.addOrderDugme)
        discardOrderDugme = view.findViewById(R.id.discardOrderDugme)
        saveOrderDugme = view.findViewById(R.id.saveOrderDugme)
        pdvCategoriesDugme = view.findViewById(R.id.pdvCategoriesDugme)
        searchProductsField = view.findViewById(R.id.searchProductsField)
        searchProductsLayout = view.findViewById(R.id.searchProductsLayout)

        if(PdvCategoriesService.pdvCategories.isEmpty()) {
            PdvCategoriesService.fetchPdvCategories {  }
        }

        addOrderDugme.visibility = View.GONE
        discardOrderDugme.visibility = View.GONE
        saveOrderDugme.visibility = View.GONE
        proizvodiRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        productListAdapter = ProductListAdapter(
            ProductsService.products,
            requireContext(),
            requireActivity(),
            productActivityLauncher,
            brojProizvodaText,
            saveOrderDugme,
            this,
            searchProductsField
        )
        brojProizvodaText.text = ProductsService.products.size.toString()
        proizvodiRecyclerView.adapter = productListAdapter
        ProductsService.fetchProducts() {result ->
            if(result) {
                productListAdapter.updateProducts(ProductsService.products)
                brojProizvodaText.text = ProductsService.products.size.toString()
            }
        }

        refreshDugme.setOnClickListener {
            ProductsService.fetchProducts() {result ->
                if(result) {
                    productListAdapter.updateProducts(ProductsService.products)
                    brojProizvodaText.text = ProductsService.products.size.toString()
                    if(searchProductsField.text.toString().isNotEmpty()) {
                        val searchedProducts: List<Product> = ProductsService.products.filter {
                                p ->
                            Log.d("search", "product = ${p.name.lowercase()}")
                            p.name.lowercase().contains(searchProductsField.text.toString().lowercase()) }
                        Log.d("search", "Broj proizvoda = ${searchedProducts.size}")
                        productListAdapter.updateProducts(searchedProducts, updateBrojProizvodaText = false)
                    }
                }
            }
        }

        addDugme.setOnClickListener {
            otvoriDodavanjeProizvoda()
        }

        pdvCategoriesDugme.setOnClickListener {
            otvoriPdvKategorije()
        }

        if(!LoginService.logovaniKorisnik!!.isAdmin()) {
            addDugme.visibility = View.GONE
            pdvCategoriesDugme.visibility = View.GONE
            addOrderDugme.visibility = View.VISIBLE
        }
        if(OrderServices.imeTrenutneNarudzbe != null) {
            addOrderDugme.visibility = View.GONE
            discardOrderDugme.visibility = View.VISIBLE
            saveOrderDugme.visibility = View.VISIBLE
            brojProizvodaLabel.text="Ime narudžbe: "
            brojProizvodaText.text = OrderServices.imeTrenutneNarudzbe
        }

        searchProductsField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onSearchProducts()
            }
        })

        searchProductsField.setOnEditorActionListener { textView, actionId, keyEvent ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Log.d("search", "on IME_ACTION_SEARCH pressed")
                    val inputManager: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                       searchProductsField.findFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    searchProductsField.clearFocus()
                    true
                }
                else -> false
            }
        }

        searchProductsLayout.setEndIconOnClickListener {
            if(searchProductsField.isFocused) {
                val inputManager: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    searchProductsField.findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                searchProductsField.setText("")
                searchProductsField.clearFocus()
            }
        }

        return view
    }
    // ON VIEW CREATED ---------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOrderDugme.setOnClickListener {
          //  var imeNarudzbe =
                showdialog()
        }
        discardOrderDugme.setOnClickListener {
            showDeleteConfirmation()

        }
    }



    private fun otvoriPdvKategorije() {
        val intent = Intent(activity, PdvCategoriesActivity::class.java)
        productActivityLauncher.launch(intent)
    }

    private fun otvoriDodavanjeProizvoda() {
        val intent = Intent(activity, AddProductActivity::class.java)
        productActivityLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        brojProizvodaText.text = ProductsService.products.size.toString()
        if(searchProductsField.text.toString().isEmpty()) {
            Log.d("search", "reseting products, size = ${ProductsService.products.size}")
            productListAdapter.updateProducts(ProductsService.products)
        }
        else {
            val searchedProducts: List<Product> = ProductsService.products.filter {
                    p ->
                Log.d("search", "product = ${p.name.lowercase()}")
                p.name.lowercase().contains(searchProductsField.text.toString().lowercase()) }
            Log.d("search", "Broj proizvoda = ${searchedProducts.size}")
            productListAdapter.updateProducts(searchedProducts, updateBrojProizvodaText = false)
        }
    }
    fun showdialog(){
      //  var m_Text: String = String()
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Da li želite napraviti novu narudžbu?")
      //  val input = EditText(requireContext())
      //  input.setHint("Unesite ime narudžbe")
      //  input.inputType = InputType.TYPE_CLASS_TEXT
      //  builder.setView(input)
        builder.setPositiveButton("Napravi", DialogInterface.OnClickListener { dialog, which ->
           // m_Text = input.text.toString()
            addOrderDugme.visibility = View.GONE
            discardOrderDugme.visibility = View.VISIBLE
            saveOrderDugme.visibility = View.VISIBLE
          //  brojProizvodaLabel.text="Ime narudžbe: "
          //  brojProizvodaText.text = m_Text
          //  OrderServices.imeTrenutneNarudzbe = "Narudžba" + (brojProizvodaText.text.toString().toInt() + 1).toString()
            OrderServices.getOrders(LoginService.logovaniKorisnik!!.getID()) {
                OrderServices.imeTrenutneNarudzbe = "Narudžba " + (it.size + 1).toString()
                productListAdapter.notifyDataSetChanged()
                OrderServices.setMapa()
                dialog.dismiss()
                val LocationPickBottomSheetFragment = LocationPickBottomSheetFragment()
                LocationPickBottomSheetFragment.show(parentFragmentManager, LocationPickBottomSheetFragment.tag)
            }
         //   OrderServices.imeTrenutneNarudzbe = "tekst"
         /*   productListAdapter.notifyDataSetChanged()
            OrderServices.setMapa()
            dialog.dismiss()
            val LocationPickBottomSheetFragment = LocationPickBottomSheetFragment()
            LocationPickBottomSheetFragment.show(parentFragmentManager, LocationPickBottomSheetFragment.tag) */
        })
        builder.setNegativeButton("Odustani", DialogInterface.OnClickListener {
                dialog, which -> dialog.cancel()
        })
        builder.show()
     //   return m_Text
    }

    //////////////


        private fun showDeleteConfirmation() {
            MaterialAlertDialogBuilder(requireContext())
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle("Poništi narudžbu?")
                .setMessage("Da li želite poništiti trenutnu narudžbu?")

                .setNegativeButton("Odustani") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Poništi") { dialog, which ->
                            addOrderDugme.visibility = View.VISIBLE
                            saveOrderDugme.visibility = View.GONE
                            brojProizvodaLabel.text = "Broj proizvoda:"
                            brojProizvodaText.text = ProductsService.products.size.toString()
                            discardOrderDugme.visibility = View.GONE
                            OrderServices.imeTrenutneNarudzbe = null
                            OrderServices.mapaZaNarudzbu = mutableMapOf<String, Any>()
                            OrderServices.lokacija = null
                            OrderServices.mjesto = null
                            OrderServices.id = null
                            OrderServices.resetKolicinaProducts()
                            productListAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                          //  onResume()
                        //    Snackbar.make(brojKorisnikaTextView, "Korisnik uspješno obrisan!", Snackbar.LENGTH_LONG)
                         //       .setAction("OK") { }
                         //       .show()
                    }
                .show()
                }

  /////////////////////////

    override fun HideBtn() {
        discardOrderDugme.visibility = View.GONE
        saveOrderDugme.visibility = View.GONE
        brojProizvodaLabel.text = "Broj proizvoda:"
        brojProizvodaText.text = ProductsService.products.size.toString()
        addOrderDugme.visibility = View.VISIBLE
    }

    private fun onSearchProducts() {
        Log.d("search", "onSearchProducts()")
        Log.d("search", "search text: ${searchProductsField.text}")
     GlobalScope.launch(Dispatchers.Main) {
            Log.d("search", "searching...  ${searchProductsField.text}")
            if(searchProductsField.text.toString().isEmpty()) {
                Log.d("search", "reseting products, size = ${ProductsService.products.size}")
                productListAdapter.updateProducts(ProductsService.products)

            }
            else {
                val searchedProducts: List<Product> = ProductsService.products.filter {
                        p ->
                    Log.d("search", "product = ${p.name.lowercase()}")
                    p.name.lowercase().contains(searchProductsField.text.toString().lowercase()) }
                Log.d("search", "Broj proizvoda = ${searchedProducts.size}")
                productListAdapter.updateProducts(searchedProducts, updateBrojProizvodaText = false)

            }
        }
    }

}
