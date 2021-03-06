package ba.etf.nrsprojekat.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import ba.etf.nrsprojekat.*
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.OrderServices
import com.google.android.material.button.MaterialButton


class FragmentProfile : Fragment() {

    private lateinit var logoutDugme: MaterialButton
    private lateinit var changePasswordDugme: MaterialButton
    private lateinit var profileEmail: TextView
    private lateinit var loggingDugme: MaterialButton
    private lateinit var poslovnicaDugme: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        logoutDugme = view.findViewById(R.id.logoutDugme)
        profileEmail = view.findViewById(R.id.profileEmail)
        changePasswordDugme = view.findViewById(R.id.changePasswordDugme)
        loggingDugme = view.findViewById(R.id.loggingDugme)
        poslovnicaDugme = view.findViewById(R.id.changePoslovnicaDugme)
        profileEmail.text = LoginService.logovaniKorisnik!!.getEmail()

        loggingDugme.setOnClickListener {
            onLogging()
        }

        logoutDugme.setOnClickListener {
            onLogout()
        }

        changePasswordDugme.setOnClickListener {
            onChangePassword()
        }

        poslovnicaDugme.setOnClickListener {
            onChangePoslovnica()
        }

        if(!LoginService.logovaniKorisnik!!.isAdmin()) {
            loggingDugme.visibility = View.GONE
            logoutDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = poslovnicaDugme.id
            }
        }
        else {
            poslovnicaDugme.visibility = View.GONE
            loggingDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = changePasswordDugme.id
            }
        }

        return view
    }

    private fun onLogout() {
        LoginService.logoutUser()
        OrderServices.resetData()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent);
        activity?.finishActivity(10)
    }

    private fun onChangePassword() {
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        startActivity(intent);
    }

    private fun onLogging() {
        val intent = Intent(activity, LoggingActivity::class.java)
        startActivity(intent);
    }

    private fun onChangePoslovnica() {
        val intent = Intent(activity, ChangeUserBranchActivity::class.java)
        startActivity(intent);
    }
 }