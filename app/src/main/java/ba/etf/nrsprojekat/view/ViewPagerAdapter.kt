package ba.etf.nrsprojekat.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ba.etf.nrsprojekat.data.models.Product
import java.util.*

class ViewPagerAdapter (fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle){


    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                FragmentSviProizvodi()

            }
            1->{
                FragmentPrimljeni()
            }
            2->{
                Fragment()
                //   FragmentPoslani()

            }
            3->{
                Fragment()
                //  FragmentIsporuceni()
            }
            else->{
                Fragment()
            }
        }
    }
}