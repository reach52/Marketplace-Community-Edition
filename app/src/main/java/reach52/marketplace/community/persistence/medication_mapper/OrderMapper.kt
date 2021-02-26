package reach52.marketplace.community.persistence.medication_mapper

import arrow.core.Option
import reach52.marketplace.community.models.medication.Order
import reach52.marketplace.community.persistence.DiscountMapper
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.OrderStatusMapper
import reach52.marketplace.community.persistence.Unmarshaler

class OrderMapper : Marshaler<Order>, Unmarshaler<Order> {
//    companion object {
//        const val KEY_CURRENCY = "currency"
//        const val KEY_CURRENT_STATUS = "currentStatus"
//        const val KEY_DELIVERY = "delivery"
//        const val KEY_DISCOUNT = "discount"
//        const val KEY_DISCOUNT_ID_NUMBER = "discountIdNumber"
//        const val KEY_ITEMS = "items"
//        const val KEY_LANGUAGE = "language"
//        const val KEY_ORDER_SUB_TOTAL= "orderSubTotal"
//        const val KEY_ORDER_TOTAL_PAYABLE = "orderTotalPayable"
//        const val KEY_LINE_SUB_TOTAL = "lineSubTotal"
//        const val KEY_PAST_STATUSES = "pastStatuses"
//        const val KEY_PATIENT_ADDRESS = "patientAddress"
//        const val KEY_PATIENT_AGE = "patientAge"
//        const val KEY_PATIENT_GENDER = "patientGender"
//        const val KEY_PATIENT_NAME = "patientName"
//        const val KEY_PHYSICIAN_ID = "physicianId"
//        const val KEY_PHYSICIAN_LICENSE_NUMBER = "physicianLicenseNumber"
//        const val KEY_PHYSICIAN_NAME = "physicianName"
//        const val KEY_PRESCRIPTION_NUMBER = "prescriptionNumber"
//        const val KEY_RECIPIENT = "recipient"
//        const val KEY_RESIDENT_ID = "residentId"
//        const val KEY_REQ_RX = "reqRx"
//        const val KEY_SUPPLIER = "supplier"
//        const val TYPE_ORDER = "order"
//        const val KEY_VAT_PAYABLE = "vatPayable"
//        const val KEY_CREATED_BY = "createdBy"
//        const val KEY_UPDATED_BY = "updatedBy"
//
//        private val orderItemMapper = OrderItemMapper()
//        private val orderStatusMapper = OrderStatusMapper()
//        private val discountMapper = DiscountMapper()
//        private val createdByMapper = CreatedByMapper()
//    }

    companion object {
        const val KEY_CURRENT_STATUS = "currentStatus"
        const val KEY_DISCOUNT = "discount"
        const val KEY_DISCOUNT_ID_NUMBER = "discountIdNumber"
        const val KEY_ITEMS = "items"
        const val KEY_PAST_STATUSES = "pastStatuses"
        const val KEY_PATIENT_ADDRESS = "patientAddress"
        const val KEY_PATIENT_AGE = "patientAge"
        const val KEY_PATIENT_GENDER = "patientGender"
        const val KEY_PATIENT_NAME = "patientName"
        const val KEY_PHYSICIAN_ID = "physicianId"
        const val KEY_PHYSICIAN_LICENSE_NUMBER = "physicianLicenseNumber"
        const val KEY_PHYSICIAN_NAME = "physicianName"
        const val KEY_PRESCRIPTION_NUMBER = "prescriptionNumber"
        const val KEY_RECIPIENT = "recipient"
        const val KEY_RESIDENT_ID = "residentId"
        const val KEY_SUPPLIER = "supplier"
        const val TYPE_ORDER = "order"

        private val orderItemMapper = OrderItemMapper()
        private val orderStatusMapper = OrderStatusMapper()
        private val discountMapper = DiscountMapper()
    }

    override fun marshal(t: Order): Map<String, Any> {

//        return mutableMapOf(
//                KEY_CURRENCY to t.currency,
//                KEY_CURRENT_STATUS to orderStatusMapper.marshal(t.currentStatus),
//                KEY_DELIVERY to t.delivery,
//                KEY_ITEMS to t.items.list.map(orderItemMapper::marshal),
//                KEY_LANGUAGE to t.language,
//                KEY_LINE_SUB_TOTAL to t.lineSubTotal,
//                KEY_ORDER_SUB_TOTAL to t.orderSubTotal,
//                KEY_ORDER_TOTAL_PAYABLE to t.orderTotalPayable,
//                KEY_PAST_STATUSES to t.pastStatuses.map(orderStatusMapper::marshal),
//                KEY_PATIENT_ADDRESS to t.patientAddress,
//                KEY_PATIENT_AGE to t.patientAge,
//                KEY_PATIENT_GENDER to t.patientGender,
//                KEY_PATIENT_NAME to t.patientName,
//                KEY_PHYSICIAN_ID to t.physicianId,
//                KEY_PHYSICIAN_LICENSE_NUMBER to t.physicianLicenseNumber,
//                KEY_PHYSICIAN_NAME to t.physicianName,
//                KEY_RESIDENT_ID to t.residentId,
//                KEY_REQ_RX to t.reqRx,
//                KEY_SUPPLIER to t.supplier,
//                KEY_VAT_PAYABLE to t.vatPayable,
//                KEY_CREATED_BY to createdByMapper.marshal(t.createdBy),
//                KEY_UPDATED_BY to t.updatedBy.map(createdByMapper::marshal)
//
//        ).also { properties ->
//            t.discount.map {
//                properties[KEY_DISCOUNT] = discountMapper.marshal(it)
//            }
//
//            t.discountIdNumber.map {
//                properties[KEY_DISCOUNT_ID_NUMBER] = it
//            }
//
//            t.prescriptionNumber.map {
//                properties[KEY_PRESCRIPTION_NUMBER] = it
//            }
//
//            t.recipient.map {
//                properties[KEY_RECIPIENT] = it
//            }
//        }
//    }
//

        return mutableMapOf(
                KEY_CURRENT_STATUS to orderStatusMapper.marshal(t.currentStatus),
//                KEY_ITEMS to t.items.list.map(orderItemMapper::marshal),
                KEY_PAST_STATUSES to t.pastStatuses.map(orderStatusMapper::marshal),
                KEY_PATIENT_ADDRESS to t.patientAddress,
                KEY_PATIENT_AGE to t.patientAge,
                KEY_PATIENT_GENDER to t.patientGender,
                KEY_PATIENT_NAME to t.patientName,
                KEY_PHYSICIAN_ID to t.physicianId,
                KEY_PHYSICIAN_LICENSE_NUMBER to t.physicianLicenseNumber,
                KEY_PHYSICIAN_NAME to t.physicianName,
                KEY_RESIDENT_ID to t.residentId,
                KEY_SUPPLIER to t.supplier
        ).also { properties ->
            t.discount.map {
                properties[KEY_DISCOUNT] = discountMapper.marshal(it)
            }

            t.discountIdNumber.map {
                properties[KEY_DISCOUNT_ID_NUMBER] = it
            }

            t.prescriptionNumber.map {
                properties[KEY_PRESCRIPTION_NUMBER] = it
            }

            t.recipient.map {
                properties[KEY_RECIPIENT] = it
            }
        }

//    override fun unmarshal(properties: Map<String, Any>): Order {
//        val currency = properties[KEY_CURRENCY] as String
//        @Suppress("UNCHECKED_CAST")
//        val currentStatus =
//                orderStatusMapper.unmarshal(properties[KEY_CURRENT_STATUS] as Map<String, Any>)
//        val delivery = (properties[KEY_DELIVERY] as Number).toDouble()
//
//        @Suppress("UNCHECKED_CAST")
//        val discount = Option.fromNullable(properties[KEY_DISCOUNT]).map {
//            discountMapper.unmarshal(it as Map<String, Any>)
//        }
//        val discountIdNumber = Option.fromNullable(properties[KEY_DISCOUNT_ID_NUMBER]).map {
//            it as String
//        }
//        val items = (properties[KEY_ITEMS] as List<*>)
//                .filterIsInstance<Map<String, Any>>()
//                .map(orderItemMapper::unmarshal)
//                .let { OrderItems.fromList(it) }
//        val language = properties[KEY_LANGUAGE] as String
//        val lineSubTotal = (properties[KEY_LINE_SUB_TOTAL] as Number).toDouble()
//        val orderSubTotal = (properties[KEY_ORDER_SUB_TOTAL] as Number).toDouble()
//        val orderTotalPayable = (properties[KEY_ORDER_TOTAL_PAYABLE] as Number).toDouble()
//        val pastStatuses = (properties[KEY_PAST_STATUSES] as List<*>)
//                .filterIsInstance<Map<String, Any>>()
//                .map(orderStatusMapper::unmarshal)
//        val residentId = properties[KEY_RESIDENT_ID] as String
//        val patientName = properties[KEY_PATIENT_NAME] as String
//        val patientGender = properties[KEY_PATIENT_GENDER] as String
//        val patientAge = (properties[KEY_PATIENT_AGE] as Number).toInt()
//        val patientAddress = properties[KEY_PATIENT_ADDRESS] as String
//        val physicianId = properties[KEY_PHYSICIAN_ID] as String
//        val physicianLicenseNumber = properties[KEY_PHYSICIAN_LICENSE_NUMBER] as String
//        val physicianName = properties[KEY_PHYSICIAN_NAME] as String
//        val prescriptionNumber =
//                Option.fromNullable(properties[KEY_PRESCRIPTION_NUMBER]).map { it as String }
//        val recipient: Option<String> =
//                Option.fromNullable(properties[KEY_RECIPIENT]).map { it as String }
//        val rexRx = properties[KEY_REQ_RX] as Boolean
//        val supplier = properties[KEY_SUPPLIER] as String
//        val vatPayable = (properties[KEY_VAT_PAYABLE] as Number).toDouble()
//
//        val createdBayMap = (properties[KEY_CREATED_BY]  as Map<String, Any>)
//        val createdBy = CreatedByMapper().unmarshal(createdBayMap)
//        val updatedBy = (properties[KEY_UPDATED_BY] as List<*>)
//                .filterIsInstance<Map<String, Any>>()
//                .map(createdByMapper::unmarshal)
//
//
//        return Order(
//                currency = currency,
//                currentStatus = currentStatus,
//                delivery = delivery,
//                discount = discount,
//                discountIdNumber = discountIdNumber,
//                items = items,
//                language = language,
//                lineSubTotal = lineSubTotal,
//                orderSubTotal = orderSubTotal,
//                orderTotalPayable = orderTotalPayable,
//                pastStatuses = pastStatuses,
//                patientAddress = patientAddress,
//                patientAge = patientAge,
//                patientGender = patientGender,
//                patientName = patientName,
//                physicianId = physicianId,
//                physicianLicenseNumber = physicianLicenseNumber,
//                physicianName = physicianName,
//                prescriptionNumber = prescriptionNumber,
//                recipient = recipient,
//                residentId = residentId,
//                reqRx = rexRx,
//                supplier = supplier,
//                vatPayable = vatPayable,
//                createdBy = createdBy,
//                updatedBy = updatedBy
//        )
    }

    override fun unmarshal(properties: Map<String, Any>): Order {
        @Suppress("UNCHECKED_CAST")
        val currentStatus =
                orderStatusMapper.unmarshal(properties[KEY_CURRENT_STATUS] as Map<String, Any>)

        @Suppress("UNCHECKED_CAST")
        val discount = Option.fromNullable(properties[KEY_DISCOUNT]).map {
            discountMapper.unmarshal(it as Map<String, Any>)
        }
        val discountIdNumber = Option.fromNullable(properties[KEY_DISCOUNT_ID_NUMBER]).map {
            it as String
        }
//        val items = (properties[KEY_ITEMS] as List<*>)
//                .filterIsInstance<Map<String, Any>>()
//                .map(orderItemMapper::unmarshal)
//                .let { OrderItems.fromList(it) }
        val pastStatuses = (properties[KEY_PAST_STATUSES] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(orderStatusMapper::unmarshal)
        val residentId = properties[KEY_RESIDENT_ID] as String
        val patientName = properties[KEY_PATIENT_NAME] as String
        val patientGender = properties[KEY_PATIENT_GENDER] as String
        val patientAge = (properties[KEY_PATIENT_AGE] as Number).toInt()
        val patientAddress = properties[KEY_PATIENT_ADDRESS] as String
        val physicianId = properties[KEY_PHYSICIAN_ID] as String
        val physicianLicenseNumber = properties[KEY_PHYSICIAN_LICENSE_NUMBER] as String
        val physicianName = properties[KEY_PHYSICIAN_NAME] as String
        val prescriptionNumber =
                Option.fromNullable(properties[KEY_PRESCRIPTION_NUMBER]).map { it as String }
        val recipient: Option<String> =
                Option.fromNullable(properties[KEY_RECIPIENT]).map { it as String }
        val supplier = properties[KEY_SUPPLIER] as String


        return Order(
                currentStatus = currentStatus,
                discount = discount,
                discountIdNumber = discountIdNumber,
                null,
                pastStatuses = pastStatuses,
                patientAddress = patientAddress,
                patientAge = patientAge,
                patientGender = patientGender,
                patientName = patientName,
                physicianId = physicianId,
                physicianLicenseNumber = physicianLicenseNumber,
                physicianName = physicianName,
                prescriptionNumber = prescriptionNumber,
                recipient = recipient,
                residentId = residentId,
                supplier = supplier
        )
    }
}
