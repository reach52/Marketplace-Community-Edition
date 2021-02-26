package reach52.marketplace.community.persistence.medication

class OrderMapperTest {
//    private val orderFixture = Order(
//            currency = "PH peso",
//            currentStatus = OrderStatus(
//                    status = OrderStatus.Status.PENDING,
//                    statusDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
//                    username = None,
//                    userDisplayName = None,
//                    userId = None,
//                    reason = None
//            ),
//            delivery = 50.00,
//            discount = None,
//            discountIdNumber = None,
//            items = OrderItems(
//                    OrderItem(
//                            barCode = "barCode",
//                            country = "Philippines",
//                            isVatExclusive = true,
//                            localMedicationDosage = "7mg",
//                            localMedicationForm = "med form",
//                            localMedicationIngredients = "local ingredients",
//                            localMedicationName = "local med name",
//                            localMedicationSupplier = "local med supplier",
//                            medCode = "modCode",
//                            medicationDosage = "dosage",
//                            medicationForm = "form",
//                            medicationId = "id",
//                            medicationIngredients = listOf(Language(
//                                    text = "sample text",
//                                    isoLanguage = "Tagalog"
//                            )),
//                            medicationName = "name",
//                            medicationPackageSize = 1,
//                            price = 1.5,
//                            r52CatNo = "r52CatNo",
//                            r52SuppCode = "r52SuppCode",
//                            suppCatNo = "suppCatNo",
//                            supplier = "supplier",
//                            quantity = 1,
//                            type = "order",
//                            vatRate = 0.10
//                    ), listOf()
//            ),
//            language = "Tagalog",
//            lineSubTotal = 100.00,
//            orderSubTotal = 100.00,
//            orderTotalPayable = 150.00,
//            pastStatuses = listOf(),
//            patientAddress = "Patient Address",
//            patientAge = 21,
//            patientGender = "Male",
//            patientName = "Patient Name",
//            physicianId = "Physician Id",
//            physicianLicenseNumber = "Physician License Number",
//            physicianName = "Physician Name",
//            prescriptionNumber = None,
//            recipient = None,
//            residentId = "Resident Id",
//            reqRx = true,
//            supplier = "medExpress",
//            vatPayable = 20.00,
//            createdBy = CreatedBy(
//                dateTime = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
//                userId = "agent01"
//            ),
//            updatedBy = listOf(CreatedBy(
//                dateTime = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
//                userId = "agent01"
//            ))
//    )
//
//    private val mapFixture = mapOf(
//            OrderMapper.KEY_CURRENCY to "PH peso",
//            OrderMapper.KEY_CURRENT_STATUS to mapOf(
//                    OrderStatusMapper.KEY_STATUS to "PENDING",
//                    OrderStatusMapper.KEY_STATUS_DATE to "1970-01-01T00:00:00Z"
//            ),
//            OrderMapper.KEY_DELIVERY to 50.00,
//            OrderMapper.KEY_ITEMS to listOf(
//                    mapOf(
//                            OrderItemMapper.KEY_BAR_CODE to "barCode",
//                            OrderItemMapper.KEY_COUNTRY to "Philippines",
//                            OrderItemMapper.KEY_IS_VAT_EXCLUSIVE to true,
//                            OrderItemMapper.KEY_LOCAL_MEDICATION_DOSAGE to "7mg",
//                            OrderItemMapper.KEY_LOCAL_MEDICATION_FORM to "med form",
//                            OrderItemMapper.KEY_LOCAL_MEDICATION_INGREDIENTS to "local ingredients",
//                            OrderItemMapper.KEY_LOCAL_MEDICATION_NAME to "local med name",
//                            OrderItemMapper.KEY_LOCAL_MEDICATION_SUPPLIER to "local med supplier",
//                            OrderItemMapper.KEY_MED_CODE to "modCode",
//                            OrderItemMapper.KEY_MEDICATION_DOSAGE to "dosage",
//                            OrderItemMapper.KEY_MEDICATION_FORM to "form",
//                            OrderItemMapper.KEY_MEDICATION_ID to "id",
//                            OrderItemMapper.KEY_MEDICATION_INGREDIENTS to listOf(
//                                    mapOf(
//                                            LanguageMapper.KEY_TEXT to "sample text",
//                                            LanguageMapper.KEY_LANGUAGE to "Tagalog"
//                                    )
//                            ),
//                            OrderItemMapper.KEY_MEDICATION_NAME to "name",
//                            OrderItemMapper.KEY_MEDICATION_PACKAGE_SIZE to 1,
//                            OrderItemMapper.KEY_PRICE to 1.5,
//                            OrderItemMapper.KEY_R52_CAT_NO to "r52CatNo",
//                            OrderItemMapper.KEY_R52_SUPP_CODE to "r52SuppCode",
//                            OrderItemMapper.KEY_SUPP_CAT_NO to "suppCatNo",
//                            OrderItemMapper.KEY_SUPPLIER to "supplier",
//                            OrderItemMapper.KEY_QUANTITY to 1,
//                            OrderItemMapper.KEY_TYPE to "order",
//                            OrderItemMapper.KEY_VAT_RATE to 0.10
//                    )
//            ),
//            OrderMapper.KEY_LANGUAGE to "Tagalog",
//            OrderMapper.KEY_LINE_SUB_TOTAL to 100.00,
//            OrderMapper.KEY_ORDER_SUB_TOTAL to 100.00,
//            OrderMapper.KEY_ORDER_TOTAL_PAYABLE to 150.00,
//            OrderMapper.KEY_PAST_STATUSES to listOf<Map<String, Any>>(),
//            OrderMapper.KEY_PATIENT_ADDRESS to "Patient Address",
//            OrderMapper.KEY_PATIENT_AGE to 21,
//            OrderMapper.KEY_PATIENT_GENDER to "Male",
//            OrderMapper.KEY_PATIENT_NAME to "Patient Name",
//            OrderMapper.KEY_PHYSICIAN_ID to "Physician Id",
//            OrderMapper.KEY_PHYSICIAN_LICENSE_NUMBER to "Physician License Number",
//            OrderMapper.KEY_PHYSICIAN_NAME to "Physician Name",
//            OrderMapper.KEY_RESIDENT_ID to "Resident Id",
//            OrderMapper.KEY_REQ_RX to true,
//            OrderMapper.KEY_SUPPLIER to "medExpress",
//            OrderMapper.KEY_VAT_PAYABLE to 20.00,
//            OrderMapper.KEY_CREATED_BY to mapOf(
//                    CreatedByMapper.KEY_DATE_TIME to "1970-01-01T00:00Z",
//                    CreatedByMapper.KEY_USER_ID to "agent01"
//            ),
//            OrderMapper.KEY_UPDATED_BY to listOf(
//                    mapOf(
//                            CreatedByMapper.KEY_DATE_TIME to "1970-01-01T00:00Z",
//                            CreatedByMapper.KEY_USER_ID to "agent01"
//                    )
//            )
//
//    )
//
//    @Test
//    fun marshal() {
//        assertEquals(mapFixture, OrderMapper().marshal(orderFixture))
//    }
//
//    @Test
//    fun unmarshal() {
//        assertEquals(orderFixture, OrderMapper().unmarshal(mapFixture))
//    }
}