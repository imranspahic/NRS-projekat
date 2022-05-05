package ba.etf.nrsprojekat.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ba.etf.nrsprojekat.ChangePasswordActivity
import ba.etf.nrsprojekat.MainActivity
import ba.etf.nrsprojekat.MainActivity2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.LoginService
import com.google.android.material.button.MaterialButton


class FragmentProfile : Fragment() {

    private lateinit var logoutDugme: MaterialButton
    private lateinit var changePasswordDugme: MaterialButton
    private lateinit var profileEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        logoutDugme = view.findViewById(R.id.logoutDugme)
        profileEmail = view.findViewById(R.id.profileEmail)
        changePasswordDugme = view.findViewById(R.id.changePasswordDugme)
        profileEmail.text = LoginService.logovaniKorisnik!!.getEmail()
        logoutDugme.setOnClickListener {
            onLogout()
        }

        changePasswordDugme.setOnClickListener {
            onChangePassword()
        }


        return view
    }

    private fun onLogout() {
        LoginService.logoutUser()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent);
        activity?.finishActivity(10)
    }

    private fun onChangePassword() {
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        startActivity(intent);
    }
}