package reach52.marketplace.community.persistence

import android.content.Context
import android.util.Log
import arrow.core.Option
import com.couchbase.lite.Database
import com.couchbase.lite.Manager
import com.couchbase.lite.View
import com.couchbase.lite.android.AndroidContext
import com.couchbase.lite.auth.AuthenticatorFactory
import com.couchbase.lite.replicator.Replication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.extensions.utils.CountryCode
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.models.follow_up.FollowUp
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ServiceOrProductMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ShoppingCartMapper
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import reach52.marketplace.community.persistence.insurer_mapper.InsurerDocumentMapper
import reach52.marketplace.community.persistence.medication_mapper.MedicationMapper
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import reach52.marketplace.community.persistence.medication_mapper.SuppliersMapper
import reach52.marketplace.community.persistence.policyOwner_mapper.PolicyOwnerMapper
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.mapper.ResidentMapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.net.URL

abstract class DispergoDatabase {
	sealed class ReplicationEvent {
		data class Error(val t: Throwable) : ReplicationEvent()
		data class InProgress(val completed: Int, val total: Int) : ReplicationEvent()
		object Finished : ReplicationEvent()
		object Offline : ReplicationEvent()
		object Started : ReplicationEvent()
		object UpdatingIndex : ReplicationEvent()
	}

	companion object {

		@Volatile
		private var INSTANCE: Database? = null

		@Volatile
		private var ACCEPTED_ORDERS: View? = null

		@Volatile
		private var DELIVERED_ORDERS: View? = null

		@Volatile
		private var DISPATCHED_ORDERS: View? = null

		@Volatile
		private var INSURANCE_PLANS: View? = null

		@Volatile
		private var POLICY_ORDERS: View? = null

		@Volatile
		private var LOGISTIC_RESIDENT: View? = null

		@Volatile
		private var INSURED_PLAN: View? = null

		@Volatile
		private var PURCHASED_INSURANCE_PLANS: View? = null

		@Volatile
		private var MEDICATIONS: View? = null

		@Volatile
		private var NOT_DELIVERED_ORDERS: View? = null

		@Volatile
		private var ON_HOLD_ORDERS: View? = null

		@Volatile
		private var ORDERS: View? = null

		@Volatile
		private var PENDING_ORDERS: View? = null

		@Volatile
		private var PICKUP_ORDERS: View? = null

		@Volatile
		private var REJECTED_ORDERS: View? = null

		@Volatile
		private var TO_DELIVER_ORDERS: View? = null

		@Volatile
		private var TO_FOLLOW_UP: View? = null

		@Volatile
		private var ALL_FOLLOW_UP: View? = null

		@Volatile
		private var RESIDENT_FOLLOW_UP: View? = null

		@Volatile
		private var SERVICE_OR_PRODUCT: View? = null

		@Volatile
		private var PRODUCT: View? = null

		@Volatile
		private var CONSUMER_ORDERS: View? = null

		@Volatile
		private var PENDING_CONSUMER_ORDERS: View? = null

		@Volatile
		private var ON_HOLD_CONSUMER_ORDERS: View? = null

		@Volatile
		private var REJECTED_CONSUMER_ORDERS: View? = null

		@Volatile
		private var ACCEPTED_CONSUMER_ORDERS: View? = null

		@Volatile
		private var TO_DELIVER_CONSUMER_ORDERS: View? = null

		@Volatile
		private var CONSUMER_DISPATCHED_ORDERS: View? = null

		@Volatile
		private var PICKUP_CONSUMER_ORDERS: View? = null

		@Volatile
		private var NOT_DELIVERED_CONSUMER_ORDERS: View? = null

		@Volatile
		private var DELIVERED_CONSUMER_ORDERS: View? = null

		@Volatile
		private var INSURER: View? = null

		@Volatile
		private var SAVE_CART: View? = null

		@Volatile
		private var MEDICATION_SUPPLIER: View? = null

		@Volatile
		private var SUPPLIERS: View? = null

		@Volatile
		private var PHYSICIAN: View? = null

		@Volatile
		private var MEDICINE: View? = null

		@Volatile
		private var DISCOUNTS: View? = null

		@Volatile
		private var ORDER_MEDICINE: View? = null

		//Please add your database name here
		private val DB_NAME = when (BuildConfig.FLAVOR) {
			// Database name for demo
			"demo" -> ""
			// Database name for live
			"live" -> ""
			else -> when (CountryManager.getCountryCode()) {
				// Database name for UAT
				CountryCode.IND -> ""

				else -> ""
			}
		}

		private val REPLICATION_URL = when (BuildConfig.FLAVOR) {
			//Not used anymore
			"live" -> URL("")

			//Not used anymore
			else -> URL("")

		}

		private val AUTHENTICATOR = when (BuildConfig.FLAVOR) {
			//Not used anymore
			"live" -> AuthenticatorFactory.createBasicAuthenticator("", "")

			//Not used anymore
			else -> AuthenticatorFactory.createBasicAuthenticator("", "")
		}

		private fun getMedication(document: Map<String, Any>) =
				Utils.getModel(MedicationMapper.TYPE_MEDICATION, MedicationMapper(), document)

		private fun getOrder(document: Map<String, Any>) =
				Utils.getModel(OrderMapper.TYPE_ORDER, OrderMapper(), document)

		private fun getInsurance(document: Map<String, Any>) =
				Utils.getModel(InsuranceMapper.KEY_TYPE_INSURANCE_PLAN, InsuranceMapper(), document)

		private fun getInsured(document: Map<String, Any>) =
				Utils.getModel(InsuredMapper.KEY_INSURANCE_OWNER, InsuredMapper(), document)

		private fun getPurchasedPlan(document: Map<String, Any>) =
				Utils.getModel(PolicyOwnerMapper.TYPE_POLICY_OWNER, PolicyOwnerMapper(), document)

		//        private fun getResident(document: Map<String, Any>): Option<LogisticResident> =
//                Utils.getModel(LogisticResidentMapper.KEY_DISPERGO_RESIDENT, LogisticResidentMapper(), document)
		private fun getResident(document: Map<String, Any>): Option<Resident> =
				Utils.getModel(VIEW_RESIDENT_LOGISTIC, ResidentMapper(), document)

		private fun getFollowUp(document: Map<String, Any>): Option<FollowUp> =
				Utils.getModel(FollowUpMapper.TYPE_FOLLOWUP, FollowUpMapper(), document)

		private fun getServiceOrProduct(document: Map<String, Any>) =
				Utils.getModel(ServiceOrProductMapper.KEY_CONSUMER_PRODUCT, ServiceOrProductMapper(), document)

		private fun getConsumerHealthOrders(document: Map<String, Any>) =
				Utils.getModel(ConsumerHealthOrderMapper.KEY_CONSUMER_ORDERS, ConsumerHealthOrderMapper(), document)

		private fun getSavedProducts(document: Map<String, Any>) =
				Utils.getModel(ShoppingCartMapper.TYPE_SHOPPING_CART, ShoppingCartMapper(), document)

		private fun getInsurer(document: Map<String, Any>) =
				Utils.getModel(InsurerDocumentMapper.TYPE_INSURER, InsurerDocumentMapper(), document)

		private fun getMedicationSupplier(document: Map<String, Any>) =
				Utils.getModel(SuppliersMapper.TYPE_SUPPLIER, SuppliersMapper(), document)

		private fun getPhysician(document: Map<String, Any>) =
				Utils.getModel("physician", reach52.marketplace.community.medicine.mapper.PhysicianMapper(), document)

		fun resetDatabase() {
			synchronized(this) {
				INSTANCE = null
				ACCEPTED_ORDERS = null
				DELIVERED_ORDERS = null
				DISPATCHED_ORDERS = null
				INSURANCE_PLANS = null
				LOGISTIC_RESIDENT = null
				INSURED_PLAN = null
				PURCHASED_INSURANCE_PLANS = null
				MEDICATIONS = null
				NOT_DELIVERED_ORDERS = null
				ON_HOLD_ORDERS = null
				ORDERS = null
				PENDING_ORDERS = null
				PICKUP_ORDERS = null
				REJECTED_ORDERS = null
				TO_DELIVER_ORDERS = null
				TO_FOLLOW_UP = null
				ALL_FOLLOW_UP = null
				RESIDENT_FOLLOW_UP = null
				SERVICE_OR_PRODUCT = null
				PRODUCT = null
				CONSUMER_ORDERS = null
				PENDING_CONSUMER_ORDERS = null
				ON_HOLD_CONSUMER_ORDERS = null
				REJECTED_CONSUMER_ORDERS = null
				ACCEPTED_CONSUMER_ORDERS = null
				TO_DELIVER_CONSUMER_ORDERS = null
				CONSUMER_DISPATCHED_ORDERS = null
				PICKUP_CONSUMER_ORDERS = null
				NOT_DELIVERED_CONSUMER_ORDERS = null
				DELIVERED_CONSUMER_ORDERS = null
				INSURER = null
				SAVE_CART = null
				MEDICATION_SUPPLIER = null
				SUPPLIERS = null
				PHYSICIAN = null
				MEDICINE = null
				ORDER_MEDICINE = null
			}
		}

		fun getInstance(context: Context): Database = INSTANCE ?: synchronized(this) {
			var databaseName = DB_NAME

			Manager.enableLogging("View", Log.INFO)
			Manager.enableLogging("Query", Log.INFO)
			Manager.enableLogging("Sync", Log.DEBUG)

			val user = LocalUserRepo.getUser(context)

//			Auth.currentSession(context).map { session ->

//				if (session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
					databaseName += "_" + user.id
//				}
//			}
			INSTANCE ?: Manager(AndroidContext(context.applicationContext), Manager.DEFAULT_OPTIONS)
					.getDatabase(databaseName)
					.apply {
						INSTANCE = this
					}
		}

		fun pullReplication(context: Context): Replication =
				getInstance(context).createPullReplication(REPLICATION_URL).apply {
					Log.d("ryann", "replication url = $REPLICATION_URL")
					authenticator = AUTHENTICATOR
//					Auth.currentSession(context).map { session ->
//						val userId = session.user.id
//						val country = session.user.country
//						if (session.user.roles.contains("self_signup_mam")) {
//							channels = listOf(userId, country, "consumerOrders", INSURANCE_PRODUCT_TYPE, "medicationSupplier", "medication", "physician", "PolicyOrder")
//							Log.d("taaag", "channels: $channels")
//						}
//					}
				}

		private val pullChangeEvents: PublishSubject<ReplicationEvent> = PublishSubject.create()

		val pullEvents: Observable<ReplicationEvent> = pullChangeEvents

		fun startReplication(context: Context) = pullReplication(context).run {
			addChangeListener {
				Log.i("taaag", "event: ${it.status}, (${it.completedChangeCount} / ${it.changeCount}) err: ${it.error}")
				if (it.error != null) {

					FirebaseCrashlytics.getInstance().recordException(it.error)
					pullChangeEvents.onError(it.error)

				} else {
					when (it.status!!) {
						Replication.ReplicationStatus.REPLICATION_ACTIVE -> {
							pullChangeEvents.onNext(
									ReplicationEvent.InProgress(
											it.completedChangeCount,
											it.changeCount
									)
							)
						}
						Replication.ReplicationStatus.REPLICATION_OFFLINE -> {
							pullChangeEvents.onNext(ReplicationEvent.Offline)
						}
						Replication.ReplicationStatus.REPLICATION_STOPPED -> {

							pullChangeEvents.onNext(ReplicationEvent.UpdatingIndex)
							logisticResidentView(context).updateIndex()
							pullChangeEvents.onNext(ReplicationEvent.Finished)

						}
						Replication.ReplicationStatus.REPLICATION_IDLE -> {
							// NOOP
						}
					}
				}
			}
			Log.i("taaag", "in dd started")
			start()
			pullChangeEvents.onNext(ReplicationEvent.Started)
		}


		fun pushReplication(context: Context): Replication =
				getInstance(context).createPushReplication(REPLICATION_URL).apply {
					authenticator = AUTHENTICATOR
				}

		private const val VERSION_ACCEPTED_ORDERS = "1"
		private const val VIEW_ACCEPTED_ORDERS = "acceptedOrders"

		fun acceptedOrdersView(context: Context): View = ACCEPTED_ORDERS ?: synchronized(this) {
			ACCEPTED_ORDERS ?: getInstance(context).getView(VIEW_ACCEPTED_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.ACCEPTED) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_ACCEPTED_ORDERS)
				ACCEPTED_ORDERS = this
			}
		}

		private const val VERSION_DELIVERED_ORDERS = "2"
		private const val VIEW_DELIVERED_ORDERS = "deliveredOrders"

		fun deliveredOrdersView(context: Context): View = DELIVERED_ORDERS ?: synchronized(this) {
			DELIVERED_ORDERS ?: getInstance(context).getView(VIEW_DELIVERED_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.DELIVERED) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_DELIVERED_ORDERS)
				DELIVERED_ORDERS = this
			}
		}

		private const val VERSION_DISPATCHED_ORDERS = "2"
		private const val VIEW_DISPATCHED_ORDERS = "dispatchedOrders"

		fun dispatchedOrdersView(context: Context): View = DISPATCHED_ORDERS ?: synchronized(this) {
			DISPATCHED_ORDERS ?: getInstance(context).getView(VIEW_DISPATCHED_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.DISPATCHED) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_DISPATCHED_ORDERS)
				DISPATCHED_ORDERS = this
			}
		}

		private const val VERSION_INSURANCE_VIEW = "24"
		const val INSURANCE_PRODUCT_TYPE = "InsuranceProduct"

		fun insuranceProductsView(context: Context): View = INSURANCE_PLANS ?: synchronized(this) {
			INSURANCE_PLANS ?: getInstance(context).getView(INSURANCE_PRODUCT_TYPE).apply {
				setMap({ document, emitter ->
//					getInsurance(document).map {
//						emitter.emit(it.domainResource.name, null)
//					}

					if (document["type"] == INSURANCE_PRODUCT_TYPE) {
						emitter.emit(document["suppCode"] as String, null)
					}

				}, VERSION_INSURANCE_VIEW)
				INSURANCE_PLANS = this
			}
		}

		private const val VERSION_RESIDENT_VIEW = "24"
		private const val VIEW_RESIDENT_LOGISTIC = "logisticResident"

		fun logisticResidentView(context: Context): View = LOGISTIC_RESIDENT ?: synchronized(this) {
			LOGISTIC_RESIDENT ?: getInstance(context).getView(VIEW_RESIDENT_LOGISTIC).apply {
				setMap({ document, emitter ->
					getResident(document).map {
						emitter.emit(it.lastName.toLowerCase(), null)
					}
				}, VERSION_RESIDENT_VIEW)
				LOGISTIC_RESIDENT = this
			}
		}

		private const val VERSION_INSURED_VIEW = "1"
		private const val VIEW_INSURED_PLAN_DETAILS = "insuredPlanDetails"

		fun insuredPlanView(context: Context): View = INSURED_PLAN ?: synchronized(this) {
			INSURED_PLAN ?: getInstance(context).getView(VIEW_INSURED_PLAN_DETAILS).apply {
				setMap({ document, emitter ->
					getInsured(document).map {
						emitter.emit(it.domainResource.subject.profileID, null)
					}
				}, VERSION_INSURED_VIEW)
				INSURED_PLAN = this
			}
		}

		private const val VERSION_INSURER = "12"
		private const val VIEW_INSURER = "insurer"

		fun insurerView(context: Context): View = INSURER ?: synchronized(this) {
			INSURER ?: getInstance(context).getView(VIEW_INSURER).apply {
				setMap({ document, emitter ->

					if (document["type"] == VIEW_INSURER) {
						emitter.emit(document["code"], null)
					}

//					getInsurer(document).map {
//						emitter.emit(it.name, null)
//					}
				}, VERSION_INSURER)
				INSURER = this
			}
		}

		private const val VERSION_PURCHASED_INSURANCE_VIEW = "1"
		private const val VIEW_PURCHASED_INSURANCE_PLANS = "purchasedInsurancePlans"

		fun purchasedInsurancePlansView(context: Context): View = PURCHASED_INSURANCE_PLANS
				?: synchronized(this) {
					PURCHASED_INSURANCE_PLANS
							?: getInstance(context).getView(VIEW_PURCHASED_INSURANCE_PLANS).apply {
								setMap({ document, emitter ->
									getPurchasedPlan(document).map {
										emitter.emit(it.policyOwnerID, null)
									}
								}, VERSION_PURCHASED_INSURANCE_VIEW)
								PURCHASED_INSURANCE_PLANS = this
							}
				}

		private const val VERSION_MEDICATIONS = "3"
		private const val VIEW_MEDICATIONS = "medications"

		fun medicationsView(context: Context): View = MEDICATIONS ?: synchronized(this) {
			MEDICATIONS ?: getInstance(context).getView(VIEW_MEDICATIONS).apply {
				setMap({ document, emitter ->
					getMedication(document).map {
//                        emitter.emit(it.suppliers, null)
//                        emitter.emit(it.names, null)

						emitter.emit(it.supplier, null)
						emitter.emit(it.name, null)
					}
				}, VERSION_MEDICATIONS)
				MEDICATIONS = this
			}
		}

		private const val VERSION_NOT_DELIVERED_ORDERS = "1"
		private const val VIEW_NOT_DELIVERED_ORDERS = "notDeliveredOrders"

		fun notDeliveredOrdersView(context: Context): View =
				NOT_DELIVERED_ORDERS ?: synchronized(this) {
					NOT_DELIVERED_ORDERS
							?: getInstance(context).getView(VIEW_NOT_DELIVERED_ORDERS).apply {
								setMap({ document, emitter ->
									getOrder(document).map {
										if (it.currentStatus.status == OrderStatus.Status.NOT_DELIVERED) {
											emitter.emit(it.patientName, null)
										}
									}
								}, VERSION_NOT_DELIVERED_ORDERS)
								NOT_DELIVERED_ORDERS = this
							}
				}


		private const val VERSION_ON_HOLD_ORDERS = "1"
		private const val VIEW_ON_HOLD_ORDERS = "onHoldOrders"

		fun onHoldOrdersView(context: Context): View = ON_HOLD_ORDERS ?: synchronized(this) {
			ON_HOLD_ORDERS ?: getInstance(context).getView(VIEW_ON_HOLD_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.ON_HOLD) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_ON_HOLD_ORDERS)
				ON_HOLD_ORDERS = this
			}
		}


		private const val VERSION_ORDERS = "2"
		private const val VIEW_ORDERS = "orders"

		fun ordersView(context: Context): View = ORDERS ?: synchronized(this) {
			ORDERS ?: getInstance(context).getView(VIEW_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						emitter.emit(it.patientName, null)
					}
				}, VERSION_ORDERS)
				ORDERS = this
			}
		}

		private const val VERSION_PENDING_ORDERS = "2"
		private const val VIEW_PENDING_ORDERS = "pendingOrders"

		fun pendingOrdersView(context: Context): View = PENDING_ORDERS ?: synchronized(this) {
			PENDING_ORDERS ?: getInstance(context).getView(VIEW_PENDING_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.PENDING) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_PENDING_ORDERS)
				PENDING_ORDERS = this
			}
		}

		private const val VERSION_PICKUP_ORDERS = "3"
		private const val VIEW_PICKUP_ORDERS = "pickupOrders"

		fun pickupOrdersView(context: Context): View = PICKUP_ORDERS ?: synchronized(this) {
			PICKUP_ORDERS ?: getInstance(context).getView(VIEW_PICKUP_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.RECEIVED) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_PICKUP_ORDERS)
				PICKUP_ORDERS = this
			}
		}

		private const val VERSION_REJECTED_ORDERS = "1"
		private const val VIEW_REJECTED_ORDERS = "rejectedOrders"

		fun rejectedOrdersView(context: Context): View = REJECTED_ORDERS ?: synchronized(this) {
			REJECTED_ORDERS ?: getInstance(context).getView(VIEW_REJECTED_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.REJECTED) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_REJECTED_ORDERS)
				REJECTED_ORDERS = this
			}
		}

		private const val VERSION_TO_DELIVER_ORDERS = "2"
		private const val VIEW_TO_DELIVER_ORDERS = "toDeliverOrders"

		fun toDeliverOrdersView(context: Context): View = TO_DELIVER_ORDERS ?: synchronized(this) {
			TO_DELIVER_ORDERS ?: getInstance(context).getView(VIEW_TO_DELIVER_ORDERS).apply {
				setMap({ document, emitter ->
					getOrder(document).map {
						if (it.currentStatus.status == OrderStatus.Status.PICKED_UP) {
							emitter.emit(it.patientName, null)
						}
					}
				}, VERSION_TO_DELIVER_ORDERS)
				TO_DELIVER_ORDERS = this
			}
		}

		private const val VERSION_TO_FOLLOW_UP = "6"
		private const val VIEW_TO_FOLLOW_UP = "followUps"

		fun toFollowUpsView(context: Context): View = TO_FOLLOW_UP ?: synchronized(this) {
			TO_FOLLOW_UP ?: getInstance(context).getView(VIEW_TO_FOLLOW_UP).apply {
				setMap({ document, emitter ->
					getFollowUp(document).map {
						emitter.emit(it.residentId, null)
					}
				}, VERSION_TO_FOLLOW_UP)
				TO_FOLLOW_UP = this
			}
		}

		private const val VERSION_ALL_FOLLOW_UP = "1"
		private const val VIEW_ALL_FOLLOW_UP = "allFollowUps"

		fun allFollowUpsView(context: Context): View = ALL_FOLLOW_UP ?: synchronized(this) {
			ALL_FOLLOW_UP ?: getInstance(context).getView(VIEW_ALL_FOLLOW_UP).apply {
				setMap({ document, emitter ->
					getFollowUp(document).map {
						emitter.emit(it.dateFollowUp, null)
					}
				}, VERSION_ALL_FOLLOW_UP)
				ALL_FOLLOW_UP = this
			}
		}

		private const val VERSION_RESIDENT_FOLLOW_UP = "1"
		private const val VIEW_RESIDENT_FOLLOW_UP = "residentFollowUps"

		fun residentFollowUpsView(context: Context): View = RESIDENT_FOLLOW_UP
				?: synchronized(this) {
					RESIDENT_FOLLOW_UP
							?: getInstance(context).getView(VIEW_RESIDENT_FOLLOW_UP).apply {
								setMap({ document, emitter ->
									getFollowUp(document).map {
										emitter.emit(it.residentName, null)
									}
								}, VERSION_RESIDENT_FOLLOW_UP)
								RESIDENT_FOLLOW_UP = this
							}
				}

		private const val VERSION_SERVICE_OR_PRODUCT = "1"
		private const val VIEW_SERVICE_OR_PRODUCT = "consumerProducts"

		fun serviceOrProductView(context: Context): View = SERVICE_OR_PRODUCT
				?: synchronized(this) {
					SERVICE_OR_PRODUCT
							?: getInstance(context).getView(VIEW_SERVICE_OR_PRODUCT).apply {
								setMap({ document, emitter ->
									getServiceOrProduct(document).map {
										emitter.emit(it.category, null)
									}
								}, VERSION_SERVICE_OR_PRODUCT)
								SERVICE_OR_PRODUCT = this
							}
				}

		private const val VERSION_PRODUCT = "1"
		private const val VIEW_PRODUCT = "consumerProducts"

		fun productView(context: Context): View = PRODUCT ?: synchronized(this) {
			PRODUCT ?: getInstance(context).getView(VIEW_PRODUCT).apply {
				setMap({ document, emitter ->
					getServiceOrProduct(document).map {
						emitter.emit(it.products.last(), null)
					}
				}, VERSION_PRODUCT)
				PRODUCT = this
			}
		}

		private const val VERSION_CONSUMER_ORDERS = "2"
		private const val VIEW_CONSUMER_ORDERS = "consumerOrders"

		fun consumerOrdersView(context: Context): View = CONSUMER_ORDERS ?: synchronized(this) {
			CONSUMER_ORDERS ?: getInstance(context).getView(VIEW_CONSUMER_ORDERS).apply {
				setMap({ document, emitter ->
					getConsumerHealthOrders(document).map {
						emitter.emit(it.customer.firstName, null)
					}
				}, VERSION_CONSUMER_ORDERS)
				CONSUMER_ORDERS = this
			}
		}

		private const val VERSION_PENDING_CONSUMER_ORDERS = "1"
		private const val VIEW_PENDING_CONSUMER_ORDERS = "pendingConsumerOrders"

		fun consumerPendingOrdersView(context: Context): View = PENDING_CONSUMER_ORDERS
				?: synchronized(this) {
					PENDING_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_PENDING_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.PENDING) {
											emitter.emit(it.customer.firstName, null)
										}
									}
								}, VERSION_PENDING_CONSUMER_ORDERS)
								PENDING_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_ON_HOLD_CONSUMER_ORDERS = "1"
		private const val VIEW_ON_HOLD_CONSUMER_ORDERS = "onHoldConsumerOrders"

		fun consumerOnHoldOrdersView(context: Context): View = ON_HOLD_CONSUMER_ORDERS
				?: synchronized(this) {
					ON_HOLD_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_ON_HOLD_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.ON_HOLD) {
											emitter.emit(it.customer.firstName, null)
										}
									}
								}, VERSION_ON_HOLD_CONSUMER_ORDERS)
								ON_HOLD_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_REJECTED_CONSUMER_ORDERS = "1"
		private const val VIEW_REJECTED_CONSUMER_ORDERS = "rejectedConsumerOrders"

		fun consumerRejectedOrdersView(context: Context): View = REJECTED_CONSUMER_ORDERS
				?: synchronized(this) {
					REJECTED_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_REJECTED_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.REJECTED) {
											emitter.emit(it.customer.firstName, null)
										}
									}
								}, VERSION_REJECTED_CONSUMER_ORDERS)
								REJECTED_CONSUMER_ORDERS = this
							}
				}


		private const val VERSION_ACCEPTED_CONSUMER_ORDERS = "1"
		private const val VIEW_ACCEPTED_CONSUMER_ORDERS = "acceptedConsumerOrders"

		fun consumerAcceptedOrdersView(context: Context): View = ACCEPTED_CONSUMER_ORDERS
				?: synchronized(this) {
					ACCEPTED_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_ACCEPTED_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.ACCEPTED) {
											emitter.emit(it.customer.firstName, null)
										}
									}
								}, VERSION_ACCEPTED_CONSUMER_ORDERS)
								ACCEPTED_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_TO_DELIVER_CONSUMER_ORDERS = "1"
		private const val VIEW_TO_DELIVER_CONSUMER_ORDERS = "toDeliverConsumerOrders"

		fun toDeliverConsumerOrdersView(context: Context): View = TO_DELIVER_CONSUMER_ORDERS
				?: synchronized(this) {
					TO_DELIVER_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_TO_DELIVER_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.PICKED_UP) {
											emitter.emit(it.customer.firstName, null)
										}
									}
								}, VERSION_TO_DELIVER_CONSUMER_ORDERS)
								TO_DELIVER_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_CONSUMER_DISPATCHED_ORDERS = "1"
		private const val VIEW_CONSUMER_DISPATCHED_ORDERS = "consumerDispatchedOrders"

		fun consumerDispatchedOrdersView(context: Context): View = CONSUMER_DISPATCHED_ORDERS
				?: synchronized(this) {
					CONSUMER_DISPATCHED_ORDERS
							?: getInstance(context).getView(VIEW_CONSUMER_DISPATCHED_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.DISPATCHED) {
											emitter.emit(it.customer, null)
										}
									}
								}, VERSION_CONSUMER_DISPATCHED_ORDERS)
								DISPATCHED_ORDERS = this
							}
				}

		private const val VERSION_PICKUP_CONSUMER_ORDERS = "2"
		private const val VIEW_PICKUP_CONSUMER_ORDERS = "pickupConsumerOrders"

		fun pickupConsumerOrdersView(context: Context): View = PICKUP_CONSUMER_ORDERS
				?: synchronized(this) {
					PICKUP_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_PICKUP_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.RECEIVED) {
											emitter.emit(it.customer, null)
										}
									}
								}, VERSION_PICKUP_CONSUMER_ORDERS)
								PICKUP_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_NOT_DELIVERED_CONSUMER_ORDERS = "1"
		private const val VIEW_NOT_DELIVERED_CONSUMER_ORDERS = "notDeliveredConsumerOrders"

		fun notDeliveredConsumerOrdersView(context: Context): View =
				NOT_DELIVERED_CONSUMER_ORDERS ?: synchronized(this) {
					NOT_DELIVERED_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_NOT_DELIVERED_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.NOT_DELIVERED) {
											emitter.emit(it.customer, null)
										}
									}
								}, VERSION_NOT_DELIVERED_CONSUMER_ORDERS)
								NOT_DELIVERED_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_DELIVERED_CONSUMER_ORDERS = "2"
		private const val VIEW_DELIVERED_CONSUMER_ORDERS = "deliveredConsumerOrders"

		fun deliveredConsumerOrdersView(context: Context): View = DELIVERED_CONSUMER_ORDERS
				?: synchronized(this) {
					DELIVERED_CONSUMER_ORDERS
							?: getInstance(context).getView(VIEW_DELIVERED_CONSUMER_ORDERS).apply {
								setMap({ document, emitter ->
									getConsumerHealthOrders(document).map {
										if (it.currentStatus.status == OrderStatus.Status.DELIVERED) {
											emitter.emit(it.customer, null)
										}
									}
								}, VERSION_DELIVERED_CONSUMER_ORDERS)
								DELIVERED_CONSUMER_ORDERS = this
							}
				}

		private const val VERSION_SAVE_CART = "3"
		private const val VIEW_SAVE_CART = "shoppingCarts"

		fun consumerSavedCart(context: Context): View = SAVE_CART ?: synchronized(this) {
			SAVE_CART ?: getInstance(context).getView(VIEW_SAVE_CART).apply {
				setMap({ document, emitter ->
					getSavedProducts(document).map {
						emitter.emit(it.customerID, null)
					}
				}, VERSION_SAVE_CART)
				SAVE_CART = this
			}
		}

		private const val VERSION_MEDICATION_SUPPLIER = "1"
		private const val VIEW_MEDICATION_SUPPLIER = "medicationSuppliers"

		fun medicationSupplierView(context: Context): View = MEDICATION_SUPPLIER
				?: synchronized(this) {
					MEDICATION_SUPPLIER
							?: getInstance(context).getView(VIEW_MEDICATION_SUPPLIER).apply {
								setMap({ document, emitter ->
									getMedicationSupplier(document).map {
										emitter.emit(it.supplierName, null)
									}
								}, VERSION_MEDICATION_SUPPLIER)
								MEDICATION_SUPPLIER = this
							}
				}

		private const val VERSION_POLICY_ORDERS = "4"
		private const val VIEW_POLICY_ORDERS = "policyOrders"
		fun policyOrdersView(context: Context): View = POLICY_ORDERS ?: synchronized(this) {
			POLICY_ORDERS ?: getInstance(context).getView(VIEW_POLICY_ORDERS).apply {
				setMap({ document, emitter ->

					if (document["type"] == "PolicyOrder") {
						if (document.containsKey("residentId")) {
							emitter.emit(document["residentId"] as String, null)
						}
					}

				}, VERSION_POLICY_ORDERS)
				POLICY_ORDERS = this
			}
		}

		private const val VERSION_SUPPLIER = "4"
		private const val VIEW_SUPPLIER = "suppliers"

		fun suppliersView(context: Context): View = SUPPLIERS ?: synchronized(this) {
			SUPPLIERS ?: getInstance(context).getView(VIEW_SUPPLIER).apply {
				setMap({ document, emitter ->
					if (document["type"] == "medicationSupplier") {
						emitter.emit(document["supplierCode"], null)
					}
				}, VERSION_SUPPLIER)
				SUPPLIERS = this
			}
		}

		private const val VERSION_PHYSICIAN = "0"
		private const val VIEW_PHYSICIAN = "physicians"

		fun physicianView(context: Context): View = PHYSICIAN ?: synchronized(this) {
			PHYSICIAN ?: getInstance(context).getView(VIEW_PHYSICIAN).apply {
				setMap({ document, emitter ->
					getPhysician(document).map {
						emitter.emit(it.physicianName.familyName, null)
					}
				}, VERSION_PHYSICIAN)
			}
		}

		private const val VERSION_MEDICINE = "6"
		private const val VIEW_MEDICINE = "medicines"


		fun medicineView(context: Context): View = MEDICINE ?: synchronized(this) {
			MEDICINE ?: getInstance(context).getView(VIEW_MEDICINE).apply {
				setMap({ document, emitter ->

					if (document.containsKey("type")) {
						if (document["type"] == "medication") {
							val supp = document["r52SupplierCode"] as String
							val id = document["_id"]
							if (supp != null) {
								emitter.emit(supp, null)
							}
						}
					}

				}, VERSION_MEDICINE)
			}
		}

		private const val VERSION_DISCOUNTS = "1"
		fun discountsView(context: Context): View = DISCOUNTS ?: synchronized(this) {
			DISCOUNTS ?: getInstance(context).getView("discounts").apply {
				setMap({ document, emitter ->

					if (document.containsKey("type")) {
						if (document["type"] == "discounts") {
							val country = document["isoCountry"] as String
							emitter.emit(country, null)
						}
					}

				}, VERSION_DISCOUNTS)
			}
		}

		private const val VERSION_MEDICINE_ORDER = "0"
		private const val VIEW_MEDICINE_ORDER = "orderMedicines"

		fun orderMedicineView(context: Context): View = ORDER_MEDICINE ?: synchronized(this) {
			ORDER_MEDICINE ?: getInstance(context).getView(VIEW_MEDICINE_ORDER).apply {
				setMap({ document, emitter ->
					if (document["type"] == "order") {
						emitter.emit(document["residentId"], null)
					}
				}, VERSION_MEDICINE_ORDER)
			}
		}
	}
}
