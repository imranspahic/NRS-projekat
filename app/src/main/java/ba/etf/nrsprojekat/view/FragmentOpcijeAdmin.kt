package ba.etf.nrsprojekat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ba.etf.nrsprojekat.R



class FragmentOpcijeAdmin : Fragment() {
    private lateinit var btnIzmijeni: Button
    private lateinit var btnDodaj: Button
    private lateinit var btnIzbrisi: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_opcije, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



 /*   companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentOpcijeAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    } */
}