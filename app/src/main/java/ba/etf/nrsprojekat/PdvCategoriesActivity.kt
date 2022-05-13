package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.view.PdvCategoriesListAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class PdvCategoriesActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var brojKategorijaText: TextView
    private lateinit var addPdvCategoryDugme: MaterialButton
    private lateinit var refreshPdvCategoriesDugme: MaterialButton
    private lateinit var pdvCategoriesRecylclerView: RecyclerView
    private lateinit var pdvCategoriesListAdapter: PdvCategoriesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdv_categories)
        toolbar = findViewById(R.id.pdvCategoriesToolbar)
        brojKategorijaText = findViewById(R.id.brojPdvKategorija)
        addPdvCategoryDugme = findViewById(R.id.addPdvCategoryDugme)
        refreshPdvCategoriesDugme = findViewById(R.id.refreshPdvCategoriesDugme)
        pdvCategoriesRecylclerView = findViewById(R.id.pdvCategoriesRecyclerView)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        pdvCategoriesRecylclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        pdvCategoriesListAdapter = PdvCategoriesListAdapter(
            PdvCategoriesService.pdvCategories,
            this,
            brojKategorijaText
        )
        pdvCategoriesRecylclerView.adapter = pdvCategoriesListAdapter
        brojKategorijaText.text = PdvCategoriesService.pdvCategories.size.toString()

        PdvCategoriesService.fetchPdvCategories() { result ->
            if(result) {
                pdvCategoriesListAdapter.updatePdvCategories(PdvCategoriesService.pdvCategories)
            }
        }

        refreshPdvCategoriesDugme.setOnClickListener {
            PdvCategoriesService.fetchPdvCategories() { result ->
                if(result) {
                    pdvCategoriesListAdapter.updatePdvCategories(PdvCategoriesService.pdvCategories)
                }
            }
        }

    }

    private fun onToolbarBackButton() {
        finish()
    }

}