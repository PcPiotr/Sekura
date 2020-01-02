package pl.redny.sekura.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val NUM_ITEMS = 4

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Tab1()
            1 -> Tab2()
            2 -> Tab3()
            3 -> Tab4()
            else -> null!!
        }
    }

    override fun getCount(): Int {
        return NUM_ITEMS;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            else -> null!!
        }
    }
}