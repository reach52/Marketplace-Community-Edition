package reach52.marketplace.community.fragments.follow_up

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import reach52.marketplace.community.R
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import kotlinx.android.synthetic.main.fragment_follow_up_container.view.*

class FollowUpContainerFragment : Fragment() {

    private var counter = 1

    private val followUpViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            counter = savedInstanceState.getInt("key", 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follow_up_container, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.follow_up_lists)
            followUpNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            followUpViewModel.resetFollowUpVM()
            followUpViewModel.isAllFollowUpList.value = true

            val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
            val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
            val toggle = ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            toggle.isDrawerIndicatorEnabled = true
            drawer.addDrawerListener(toggle)
            toggle.syncState()

            when (counter) {
                1 -> {
                    followUpNavigation.selectedItemId = R.id.nav_today_list
                }
                2 -> {
                    followUpNavigation.selectedItemId = R.id.nav_tomorrow_list
                }
                3 -> {
                    followUpNavigation.selectedItemId = R.id.nav_follow_up_list
                }
            }

            parentFragmentManager
                    .beginTransaction()
                    .addToBackStack("followUpContainer")
                    .commit()

            this.isFocusableInTouchMode = true
            this.requestFocus()
            this.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    parentFragmentManager.popBackStack()
                }
                false
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.followUpMenuContainer, fragment)
                .commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.nav_today_list -> {
                counter = 1
                replaceFragment(FollowUpTodayListsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_tomorrow_list -> {
                counter = 2
                replaceFragment(FollowUpTomorrowListsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_follow_up_list -> {
                counter = 3
                replaceFragment(FollowUpListsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("key", counter)
    }
}
