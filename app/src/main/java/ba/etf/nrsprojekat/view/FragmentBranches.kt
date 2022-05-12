import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.view.BranchListAdapter

class FragmentBranches : Fragment(R.layout.fragment_branches) {
    private lateinit var recyclerViewAll : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerViewAll = view.findViewById(R.id.poslovniceRecyclerView)
        recyclerViewAll.layoutManager = LinearLayoutManager(view.context)

        BranchesService.getBranches{ result ->
            val myAdapter = BranchListAdapter(result)
            recyclerViewAll.adapter = myAdapter
        }

    }
}