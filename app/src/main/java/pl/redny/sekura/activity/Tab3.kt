package pl.redny.sekura.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import arrow.data.extensions.list.show.List.show
import com.guardanis.applock.AppLock
import com.guardanis.applock.dialogs.LockCreationDialogBuilder
import kotlinx.android.synthetic.main.tab1.*
import kotlinx.android.synthetic.main.tab3.*
import pl.redny.sekura.R

class Tab3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab3, container, false)
    }

    override fun onStart() {
        super.onStart()
        button_pin.setOnClickListener { setAuthButton() }
    }

    private fun setAuthButton() {
        LockCreationDialogBuilder(activity)
            .show()
    }

    fun resetAuthButton() {
        //AppLock.isUnlockRequired = true;
    }
}