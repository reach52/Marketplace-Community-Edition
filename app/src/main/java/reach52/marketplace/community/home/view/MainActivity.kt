package reach52.marketplace.community.home.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.couchbase.lite.replicator.Replication
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.threetenabp.AndroidThreeTen
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.activities.SplashActivity
import reach52.marketplace.community.auth.Auth
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.fragments.SplashFragment
import reach52.marketplace.community.fragments.consumer_health.*
import reach52.marketplace.community.fragments.follow_up.FollowUpContainerFragment
import reach52.marketplace.community.fragments.medication.*
import reach52.marketplace.community.fragments.residents.ResidentsFragment
import reach52.marketplace.community.home.viewmodel.MainActivityViewModel
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.replication.SyncManager
import reach52.marketplace.community.resident.view.LogisticResidentsListFragment
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

@ExperimentalUnsignedTypes
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
		MenuItem.OnActionExpandListener {
	private val dispatchedOrdersFragment = DispatchedOrdersFragment()
	private val pendingOrdersFragment = PendingOrdersFragment()
	private val acceptedOrdersFragment = AcceptedOrdersFragment()
	private val onHoldFragment = OnHoldOrdersFragment()
	private val ordersFragment = OrdersFragment()
	private val pickupFragment = PickupFragment()
	private val rejectedFragment = RejectedOrdersFragment()
	private val receiveOrdersFragment = ReceivedOrdersFragment()
	private val deliveredOrdersFragment = DeliveredOrdersFragment()
	private val notDeliveredOrdersFragment = NotDeliveredOrdersFragment()
	private val residentsFragment = ResidentsFragment()
	private val residentLogisticFragment = LogisticResidentsListFragment()
	private val followUpLists = FollowUpContainerFragment()
	private val splashFragment = SplashFragment()
	private val consumerHealthOrders = ConsumerHealthOrdersFragment()
	private val consumerHealthPendingOrdersFragment = ConsumerHealthOrderPendingFragment()
	private val consumerHealthOrderOnHoldFragment = ConsumerHealthOrderOnHoldFragment()
	private val consumerHealthOrderRejectedOrdersFragment = ConsumerHealthOrderRejectedFragment()
	private val consumerHealthOrderAcceptedOrdersFragment = ConsumerHealthOrderAcceptedFragment()
	private val consumerHealthOrderDispatchedFragment = ConsumerHealthOrderDispatchedFragment()
	private val consumerHealthOrderReceivedFragment = ConsumerHealthOrderReceivedFragment()
	private val consumerHealthOrderPickedUpFragment = ConsumerHealthOrderPickedUpFragment()
	private val consumerHealthOrderNotDeliveredFragment = ConsumerHealthOrderNotDeliveredFragment()
	private val consumerHealthOrderDeliveredFragment = ConsumerHealthOrderDeliveredFragment()
	private lateinit var dispergoPull: Replication
	private lateinit var dispergoPush: Replication

	private lateinit var syncDialog: AlertDialog

	private lateinit var vm: MainActivityViewModel
	private lateinit var vm_resident: ResidentViewModel



	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		AndroidThreeTen.init(this)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		vm = ViewModelProvider(this)[MainActivityViewModel::class.java]
		vm_resident = ViewModelProvider(this)[ResidentViewModel::class.java]

		with(
				object : ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
					override fun onDrawerOpened(drawerView: View) {
						super.onDrawerOpened(drawerView)
						vm.getResidentCount(this@MainActivity)
					}
				}
		) {
			drawer_layout.addDrawerListener(this)
			this.setToolbarNavigationClickListener {
				Toast.makeText(this@MainActivity, "Click", Toast.LENGTH_SHORT).show()
			}
			syncState()
		}


		nav_view.setNavigationItemSelectedListener(this)
		nav_view.getHeaderView(0).apply {
			userDisplayName.text = vm.user.displayName
			userName.text = vm.user.username
			userCountry.text = CountryManager.getCountry(vm.user.country)
			vm.residentCount.observe(this@MainActivity, Observer {
				userResidentCount.text = "$it ${getString(R.string.residents)}"
			})

		}


//        toolbar.visibility = View.GONE

//        gotoFragment(splashFragment)

		syncDialog = AlertDialog.Builder(this)
				.setCancelable(false)
				.create()

		vm.syncManager.addPullListener(object : SyncManager.SyncListener {
			override fun onError(ex: Throwable) {
				when (ex) {
					is SyncManager.SyncOfflineException -> {
//						syncDialog.setMessage(getString(R.string.you_are_offline))
						offline_message.visibility = View.VISIBLE
					}
					else -> {
						Log.e("taaag", "error: ${ex.message}")
						displayMessage(getString(R.string.sync_error) + ": ${ex.message}")
						FirebaseCrashlytics.getInstance().recordException(ex)
					}
				}

			}

			override fun onProgress(progress: Int, total: Int) {
				if (total > 0) {
					syncDialog.setMessage(String.format("%d / %d", progress, total))
				} else {
					syncDialog.setMessage(getString(R.string.sync_in_progress))
				}
			}

			override fun onFinish() {
				syncDialog.setMessage(getString(R.string.sync_finished))
				if (residentLogisticFragment.isAdded) {
					residentLogisticFragment.refreshResidents()
				} else {
					gotoFragment(residentLogisticFragment, emptyStack = true, addToBackStack = false)
				}
				syncDialog.dismiss()
			}

		})
		vm.syncManager.addPushListener(object : SyncManager.SyncListener {
			override fun onError(ex: Throwable) {

				when (ex) {
					is SyncManager.SyncOfflineException -> {
						offline_message.visibility = View.VISIBLE
					}
				}
				Log.e("taaag", "push err: ${ex.message}")
			}

			override fun onProgress(progress: Int, total: Int) {
				if (offline_message.visibility == View.VISIBLE) {
					offline_message.visibility = View.GONE
				}
				displayMessage(R.string.uploading)
				Log.v("taaag", "push progress ($progress / $total)")
			}

			override fun onFinish() {
				Log.v("taaag", "push finish")
			}

		})
		vm.syncManager.startPushReplication()
		pullData()

		main_swipe_layout.setOnRefreshListener {
			pullData()
			main_swipe_layout.isRefreshing = false
		}

//        Auth.currentSession(this).map { session ->
//            // this will check the role of the login user first before emitting the document
//            if(session.user.roles.contains("agent")) {
//                disposables.add(DispergoDatabase.pullEvents.filter {
//                    it is DispergoDatabase.ReplicationEvent.Offline || it is DispergoDatabase.ReplicationEvent.Finished
//                }.observeOn(AndroidSchedulers.mainThread()).subscribe({
//                    toolbar.visibility = View.VISIBLE
//                    supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.fragment_container, residentsFragment)
//                            .commit()
//                }, {
//                    Log.e("MainActivity", "Replication error", it)
//                }))
//
//                supportFragmentManager
//                        .beginTransaction()
//                        .add(R.id.fragment_container, splashFragment)
//                        .commit()
//
//                DispergoDatabase.startReplication(this)
//
//            } else {
//                disposables.add(DispergoDatabase.pullEvents.filter {
//                    it is DispergoDatabase.ReplicationEvent.Offline || it is DispergoDatabase.ReplicationEvent.Finished
//                }.observeOn(AndroidSchedulers.mainThread()).subscribe({
//                    toolbar.visibility = View.VISIBLE
//                    supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.fragment_container, residentLogisticFragment)
//                            .commit()
//                }, {
//                    Log.e("MainActivity", "Replication error", it)
//                }))
//
//                supportFragmentManager
//                        .beginTransaction()
//                        .add(R.id.fragment_container, splashFragment)
//                        .commit()
//
//                DispergoDatabase.startReplication(this)
//            }
//        }

//        dispergoPull = DispergoDatabase.pullReplication(this).apply {
//            isContinuous = true
//            start()
//        }
//
//        dispergoPush = DispergoDatabase.pushReplication(this).apply {
//            isContinuous = true
//            start()
//        }

	}

	private fun pullData() {
		if (NetworkManager.isInternetAvailable()) {
			syncDialog.setMessage(getString(R.string.sync_started))
			syncDialog.show()
 		} else {
			offline_message.visibility = View.VISIBLE
			gotoFragment(residentLogisticFragment, emptyStack = true, addToBackStack = false)
		}
		vm.syncManager.startPullReplication()
	}

	private fun displayMessage(@StringRes resId: Int) {
		Snackbar.make(fragment_container, resId, Snackbar.LENGTH_SHORT)
				.show()
	}

	private fun displayMessage(string: String) {
		Snackbar.make(fragment_container, string, Snackbar.LENGTH_SHORT)
				.show()
	}

	override fun onDestroy() {
//        ResidentDatabase.stopReplication()
//        dispergoPull.stop()
//        dispergoPush.stop()
		vm.dispose()
//        SyncManager.dispose()
		super.onDestroy()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		menu.findItem(R.id.app_bar_search).let {
			it.setOnActionExpandListener(this)
			dispo.add((it.actionView as SearchView).queryTextChanges().subscribe({ charSequence ->
				Search.setQuery(charSequence.toString())
			}, {
				throw NotImplementedError()
			}))
		}

		menu.findItem(R.id.action_cart).let {
			menu.findItem(R.id.action_cart).isVisible = false
		}


		return true
	}

	override fun onBackPressed() {
		if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
			drawer_layout.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	// NavigationView.OnNavigationItemSelectedListener
	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
//			R.id.nav_residents -> {
//				Auth.currentSession(this).map { session ->
//					// this will check the role of the login user first before emitting the document
//					if (session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
//						navigation(residentLogisticFragment, getString(R.string.residents))
//					} else {
//						navigation(residentsFragment, getString(R.string.residents))
//					}
//				}
//			}
			//            Disable for India release

//            R.id.nav_follow_ups -> {
//                Auth.currentSession(this).map { session ->
//                    // this will check the role of the login user first before emitting the document
//                    if(session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
//                        Toast.makeText(this, this.getString(R.string.not_available), Toast.LENGTH_LONG).show()
//                    } else {
//                        navigation(followUpLists, getString(R.string.follow_up_list))
//                    }
//                }
//            }
//            R.id.nav_orders -> {
//                navigation(ordersFragment, getString(R.string.orders))
//            }
//            R.id.nav_pending -> {
//                navigation(pendingOrdersFragment, getString(R.string.pending_order))
//            }
//            R.id.nav_accepted -> {
//                navigation(acceptedOrdersFragment, getString(R.string.accepted_order))
//            }
//            R.id.nav_receives -> {
//                navigation(receiveOrdersFragment, getString(R.string.dispatched_orders))
//            }
//            R.id.nav_delivered -> {
//                navigation(dispatchedOrdersFragment, getString(R.string.picked_up_orders))
//            }
//            R.id.nav_on_hold -> {
//                navigation(onHoldFragment, getString(R.string.on_hold_order))
//            }
//            R.id.nav_rejected -> {
//                navigation(rejectedFragment, getString(R.string.reject_order))
//            }
//            R.id.nav_not_delivered -> {
//                navigation(notDeliveredOrdersFragment, getString(R.string.not_delivered))
//            }
//            R.id.nav_picked_up -> {
//                navigation(pickupFragment, getString(R.string.received_order))
//            }
//            R.id.nav_delivered_order -> {
//                navigation(deliveredOrdersFragment, getString(R.string.delivered_order))
//            }

//            Disable for India release

//            R.id.otcOrders -> {
//                navigation(consumerHealthOrders, getString(R.string.otc_all_orders))
//            }
//
//            R.id.nav_consumer_pending -> {
//                navigation(consumerHealthPendingOrdersFragment, getString(R.string.otc_pending_orders))
//            }
//
//            R.id.nav_consumer_on_hold -> {
//                navigation(consumerHealthOrderOnHoldFragment, getString(R.string.on_hold_consumer))
//            }
//
//            R.id.nav_consumer_rejected -> {
//                navigation(consumerHealthOrderRejectedOrdersFragment, getString(R.string.rejected_consumer))
//            }
//            R.id.nav_consumer_accepted -> {
//                navigation(consumerHealthOrderAcceptedOrdersFragment, getString(R.string.accepted_order))
//            }
//
//            R.id.nav_consumer_receives -> {
//                navigation(consumerHealthOrderReceivedFragment, getString(R.string.dispatched))
//            }
//
//            R.id.nav_consumer_delivered -> {
//                navigation(consumerHealthOrderDispatchedFragment, getString(R.string.picked_up))
//            }
//
//            R.id.nav_consumer_picked_up -> {
//                navigation(consumerHealthOrderPickedUpFragment, getString(R.string.received_order))
//            }
//
//            R.id.nav_not_consumer_delivered -> {
//                navigation(consumerHealthOrderNotDeliveredFragment, getString(R.string.not_delivered))
//            }
//
//            R.id.nav_consumer_delivered_order -> {
//                navigation(consumerHealthOrderDeliveredFragment, getString(R.string.delivered_order))
//            }

			R.id.nav_logout -> {
				Auth.logout(this)
				val intent = Intent(this, SplashActivity::class.java)
				intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				startActivity(intent)
			}

			R.id.nav_resident -> {
				pullData()
			}
		/*	R.id.nav_order -> {
				//Toast.makeText(this@MainActivity, "orde", Toast.LENGTH_SHORT).show()
				startActivity(Intent(this@MainActivity, MedicationOrderStatusActivity::class.java).apply {
					this.putExtra(MedicationOrderStatusActivity.KEY_RESIDENT_ID, vm_resident.resident.id)
				})

                //navigation(ordersFragment, getString(R.string.orders))
				//gotoFragment(ordersFragment, emptyStack = true, addToBackStack = false)

			}*/
		}

		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}

	// MenuItem.OnActionExpandListener
	override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
		return true
	}

	override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
		Search.setQuery("")
		return true
	}

	private fun navigation(fragment: androidx.fragment.app.Fragment, title: String) {
		supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
		supportActionBar?.title = title
		supportFragmentManager
				.beginTransaction()
				.replace(R.id.fragment_container, fragment)
				.commit()
	}

}