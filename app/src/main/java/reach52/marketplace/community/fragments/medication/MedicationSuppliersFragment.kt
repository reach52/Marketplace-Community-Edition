package reach52.marketplace.community.fragments.medication

import androidx.fragment.app.Fragment

class MedicationSuppliersFragment : Fragment() {
//
//    private val disposables = CompositeDisposable()
//    private val orderViewModel by lazy {
//        activity?.run {
//            ViewModelProvider(this)[OrderViewModel::class.java]
//        } ?: throw Exception("Invalid Activity")
//    }
//
//    @ExperimentalUnsignedTypes
//    private val supplierAdapter = MedicationSupplierAdapter{
//        orderViewModel.selectedSupplier.value = it
//        parentFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container, MedicationsFragment())
//                .addToBackStack(null)
//                .commit()
//    }
//
//    private val medicationSupplierQuery by lazy {
//        DispergoDatabase.medicationSupplierView(context!!).createQuery()
//    }
//
//    private val medicationSupplierLiveQuery by lazy {
//        medicationSupplierQuery.toLiveQuery()
//    }
//
//    private val factory by lazy {
//        EntityDataSourceFactory(medicationSupplierQuery::run, SuppliersMapper())
//    }
//
//    private val medicationSupplier by lazy {
//        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//
//        medicationSupplierLiveQuery.apply {
//            addChangeListener { event ->
//                if (event.source == this) {
//                    factory.fnEnumerator = { event.rows }
//                    medicationSupplier.value?.dataSource?.invalidate()
//                }
//            }
//            start()
//        }
//
//        disposables.add(Search.query.subscribe({
//            medicationSupplierLiveQuery.apply {
//                startKey = if (it.isNotEmpty()) it else null
//                endKey = if (it.isNotEmpty()) it + "\u9999" else null
//                queryOptionsChanged()
//            }
//        }, { throw NotImplementedError() }))
//    }
//
//    @ExperimentalUnsignedTypes
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.choose_supplier)
//        medicationSupplier.observe(viewLifecycleOwner, Observer { supplierAdapter.submitList(it) })
//        return inflater.inflate(R.layout.fragment_medicine_supplier, container, false).apply{
//            medicineRecyclerView.apply{
//                layoutManager = LinearLayoutManager(activity)
//                adapter = supplierAdapter
//            }
//        }
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        super.onPrepareOptionsMenu(menu)
//        menu.findItem(R.id.app_bar_search).isVisible = true
//    }
//
//    override fun onDestroy() {
//        disposables.clear()
//        super.onDestroy()
//    }
}
