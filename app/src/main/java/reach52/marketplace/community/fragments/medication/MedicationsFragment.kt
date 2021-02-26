package reach52.marketplace.community.fragments.medication

@ExperimentalUnsignedTypes
class MedicationsFragment : androidx.fragment.app.Fragment() {
//    private val disposables = CompositeDisposable()
//    private val viewDisposables = CompositeDisposable()
//    private val selectedMedicationAdapter = SelectedMedicationAdapter()
//    private val selectMedicationFragment = SelectMedicationFragment()
//
//    private lateinit var vm: ResidentViewModel
//
//    private val residentLogisticViewModel by lazy {
//        activity?.run {
//            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
//        } ?: throw Exception("Invalid Activity")
//    }
//
//    private val orders by lazy {
//        Orders(DispergoDatabase.getInstance(context!!))
//    }
//
//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }
//
//    private val orderViewModel by lazy {
//        activity?.run {
//            ViewModelProvider(this)[OrderViewModel::class.java]
//        } ?: throw Exception("Invalid Activity")
//    }
//
//    private lateinit var mModel: Option<OrderViewModel>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//
//        vm = ViewModelProvider(activity as ResidentDetailsActivity)[ResidentViewModel::class.java]
//
//        mModel = Option.fromNullable(activity).map {
//            ViewModelProvider(it)[OrderViewModel::class.java]
//        }
//
//        orderViewModel.selectedLogisticResident = Some(vm.resident.toLogisticResident())
//
//        disposables.add(selectMedicationFragment.selectedMedication.subscribe({
//            val medicine = it.copy()
//            selectedMedicationAdapter.addMedication(medicine)
//            parentFragmentManager.popBackStack()
//        }, {
//            Log.w("MedicationFragment", "selectedMedication", it)
//            throw NotImplementedError()
//        }))
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.order_cart_fragment, container, false).apply {
//            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.order_medication)
//            selectedMedicationsRecyclerView.apply {
//                layoutManager = LinearLayoutManager(context!!)
//                adapter = selectedMedicationAdapter
//            }
//
//            mModel.fold({}, {
//                it.discountIdNumber.fold({
//                    discountIdNumberTextView.visibility = View.INVISIBLE
//                }, { id ->
//                    discountIdNumberTextView.text = id
//                })
//            })
//
//            viewDisposables.add(selectedMedicationAdapter.orderTotal.subscribe({
//                checkoutButton.isEnabled = selectedMedicationAdapter.itemCount > 0
//                if (selectedMedicationAdapter.itemCount > 0) {
//                    val orderItems = selectedMedicationAdapter.orderItems()
//                    subTotalTextView.text = orderItems.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
//                    mModel.flatMap { model -> model.selectedDiscount }.fold({
//                        vatExemptTextView.text = 0.0.toPhilippinesCurrency()
//                        discountTextView.text = 0.0.toPhilippinesCurrency()
//                        feeTextView.text = 24.0.toPhilippinesCurrency()
//                        totalTextView.text = (orderItems.list.map { it.basePriceTotal }.sum() + 24.00).toPhilippinesCurrency()
//                    }, {
//                        val vatExempted = orderItems.list.map { it.vatValue }.sum()
//                        val discountValue = orderItems.list.map { it.discountValue() }.sum()
//                        val fee = 24.00
//                        vatExemptTextView.text = vatExempted.toPhilippinesCurrency()
//                        discountTextView.text = discountValue.toPhilippinesCurrency()
//                        feeTextView.text = fee.toPhilippinesCurrency()
//                        totalTextView.text = (orderItems.list.map { it.total() }.sum() + 24.00).toPhilippinesCurrency()
//                    })
//                }
//            }, {
//                Log.w("MedicationsFragment", "orderTotal", it)
//                throw NotImplementedError()
//            }))
//
//            if (residentLogisticViewModel.isLogisticResident.value == true) {
//                mModel.flatMap { it.selectedLogisticResident }.map {
//                    residentNameTextView.text = it.name
//                    residentAddressTextView.text = it.address
//                    residentGenderTextView.text = it.gender
//                    residentAgeTextView.text = it.age.toString()
//                }
//            } else {
//                mModel.flatMap { it.selectedResident }.map {
//                    residentNameTextView.text = it.name
//                    residentAddressTextView.text = it.address.text
//                    residentGenderTextView.text = when (it.gender) {
//                        "M" -> "Male"
//                        "F" -> "Female"
//                        else -> "Unknown: ${it.gender}"
//                    }
//                    residentAgeTextView.text = it.age.toString()
//                }
//            }
//
//            addMedicationButton.setOnClickListener {
//                parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, selectMedicationFragment)
//                        .addToBackStack(null)
//                        .commit()
//            }
//
//            checkoutButton.setOnClickListener {
//                checkoutButton.isEnabled = false
//                checkoutButton.text = getText(R.string.order_processing)
//
//                val ctx = context
//                binding {
//                    val model = mModel.bind()
//                    val items = selectedMedicationAdapter.orderItems()
//                    val photoPath = model.photoPath.bind()
//                    val physician = model.selectedPhysician.bind()
//                    val orderId: String
//                    val medication = Option.fromNullable(orderViewModel.selectedMedication.value).bind()
//
//                    if (residentLogisticViewModel.isLogisticResident.value == true) {
//                        val resident = model.selectedLogisticResident.bind()
//                        orderId = orders.residentLogistic(
//                                discount = model.selectedDiscount,
//                                discountIdNumber = model.discountIdNumber,
//                                items = items,
//                                medication = medication,
//                                photoPath = photoPath,
//                                physician = physician,
//                                prescriptionNumber = model.prescriptionNumber,
//                                resident = resident,
//                                user = user
//                        )
//                    } else {
//                        val resident = model.selectedResident.bind()
//                        orderId = orders.residentAccess(
//                                discount = model.selectedDiscount,
//                                discountIdNumber = model.discountIdNumber,
//                                items = items,
//                                medication = medication,
//                                photoPath = photoPath,
//                                physician = physician,
//                                prescriptionNumber = model.prescriptionNumber,
//                                resident = resident,
//                                user = user
//                        )
//                    }
//
//                    val base58 = UUID.fromString(orderId).base58String()
//
//                    AlertDialog.Builder(ctx).setTitle(getString(R.string.order_created)).setMessage("Tracking Code: $base58").setPositiveButton(getString(R.string.okay)) { _, _ ->
//                        model.reset()
//
//                        val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
//                        val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
//                        val toggle = ActionBarDrawerToggle(
//                                activity, drawer, toolbar, R.string.navigation_drawer_open,
//                                R.string.navigation_drawer_close)
//                        toggle.syncState()
//
//                        parentFragmentManager.popBackStack("residentDetails", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                    }.create().apply {
//                        setOnCancelListener {
//                            model.reset()
//
//                            val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
//                            val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
//                            val toggle = ActionBarDrawerToggle(
//                                    activity, drawer, toolbar, R.string.navigation_drawer_open,
//                                    R.string.navigation_drawer_close)
//                            toggle.syncState()
//
//                            parentFragmentManager.popBackStack("residentDetails", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                        }
//                    }.show()
//                }
//            }
//        }
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        super.onPrepareOptionsMenu(menu)
//        menu.findItem(R.id.app_bar_search).isVisible = false
//    }
//
//    override fun onDestroyView() {
//        viewDisposables.clear()
//        super.onDestroyView()
//    }
//
//    override fun onDestroy() {
//        disposables.clear()
//        super.onDestroy()
//    }

}