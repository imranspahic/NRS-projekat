import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddBranchActivity
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.view.BranchListAdapter
import ba.etf.nrsprojekat.view.DostavaAdapter
import ba.etf.nrsprojekat.view.ProductListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class FragmentBranches : Fragment(R.layout.fragment_branches) {
    private lateinit var recyclerViewAll : RecyclerView
    private lateinit var addBranchButton: MaterialButton
    private lateinit var refreshBranchButton: MaterialButton
    private lateinit var brojPoslovnica : TextView



    private var branchActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val mode = result.data!!.getStringExtra("branches")
            var message = ""
            message = if(mode == "ADD") {
                "Poslovnica uspješno dodana!"
            } else {
                "Poslovnica uspješno ažurirana!"
            }
            Log.d("branches", "result ok")
            Snackbar.make(recyclerViewAll, message, Snackbar.LENGTH_LONG)
                .setAction("OK") { }
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBranchButton = view.findViewById(R.id.addBranchButton)
        refreshBranchButton = view.findViewById(R.id.refreshBranchButton)
        brojPoslovnica = view.findViewById(R.id.brojPoslovnica)
        brojPoslovnica.text = BranchesService.branches.size.toString()


        recyclerViewAll = view.findViewById(R.id.poslovniceRecyclerView)
        recyclerViewAll.layoutManager = LinearLayoutManager(view.context)

        BranchesService.getBranches{ result ->
            val myAdapter = BranchListAdapter(result,brojPoslovnica)
            recyclerViewAll.adapter = myAdapter
        }
        addBranchButton.setOnClickListener {
            val intent = Intent(activity, AddBranchActivity::class.java)
            branchActivityLauncher.launch(intent)
        }
        refreshBranchButton.setOnClickListener{
            BranchesService.getBranches { result ->
                val myAdapter = BranchListAdapter(result,brojPoslovnica)
                recyclerViewAll.adapter = myAdapter
            }
            recyclerViewAll.adapter!!.notifyDataSetChanged()
            brojPoslovnica.text = BranchesService.branches.size.toString()
                }
            }
        }