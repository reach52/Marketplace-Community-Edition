package reach52.marketplace.community.fragments.residents

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import reach52.marketplace.community.R
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.resident.view.LogisticResidentsListFragment
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import kotlinx.android.synthetic.main.fragment_residents.view.*

@ExperimentalUnsignedTypes
class ResidentsFragment : androidx.fragment.app.Fragment() {
    private var selectedItem = 1

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_residents, container, false).apply {
            (activity as MainActivity).supportActionBar?.title = getString(R.string.residents)
            residentLogisticViewModel.resetResidentVM()

            val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
            val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
            val toggle = ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            toggle.isDrawerIndicatorEnabled = true
            drawer.addDrawerListener(toggle)
            toggle.syncState()

            parentFragmentManager
                    .beginTransaction()
                    .addToBackStack("residentsContainer")
                    .commit()

            this.isFocusableInTouchMode = true
            this.requestFocus()
            this.setOnKeyListener { _, keyCode, _ ->
                if(residentLogisticViewModel.residentCounter.value == 0){
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        (activity as MainActivity).finish()
                    }
                } else {
                    residentLogisticViewModel.residentCounter.value = 0
                }
                false
            }

            residentNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

            when (selectedItem) {
                1 -> {
                    residentNav.selectedItemId = R.id.nav_access_list
                }
                2 -> {
                    residentNav.selectedItemId = R.id.nav_logistic_list
                }
            }
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_cart).isVisible = true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_access_list -> {
                selectedItem = 1
                replaceFragment(ResidentsAccessFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_logistic_list -> {
                selectedItem = 2
                replaceFragment(LogisticResidentsListFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.NavMenuContainer, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            selectedItem = savedInstanceState.getInt("key", 1)
        }
    }
}
