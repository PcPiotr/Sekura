package pl.redny.sekura.activity.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.redny.sekura.R
import pl.redny.sekura.util.ResourcesUtil

class MyPageAdapter(fm: FragmentManager, var activity: AppCompatActivity) : FragmentPagerAdapter(fm) {
    private val NUM_ITEMS = 3

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
            0 -> ResourcesUtil.getResource(activity.applicationContext, R.string.encryption_tab)
            1 -> ResourcesUtil.getResource(activity.applicationContext, R.string.control_tab)
            2 -> ResourcesUtil.getResource(activity.applicationContext, R.string.settings_tab)
            3 -> ResourcesUtil.getResource(activity.applicationContext, R.string.about_tab)
            else -> null!!
        }
    }
}