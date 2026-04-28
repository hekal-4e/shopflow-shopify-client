package com.shopflow.app.data.mapper

import com.shopflow.app.CustomerRegisterMutation
import com.shopflow.app.CustomerUpdateMutation
import com.shopflow.app.FetchCustomerProfileQuery
import com.shopflow.app.domain.model.Address
import com.shopflow.app.domain.model.Customer

private fun mapAddress(
    id: String,
    address1: String?,
    address2: String?,
    city: String?,
    province: String?,
    country: String?,
    zip: String?
): Address {
    return Address(
        id = id,
        address1 = address1.orEmpty(),
        address2 = address2,
        city = city.orEmpty(),
        province = province,
        country = country.orEmpty(),
        zip = zip.orEmpty()
    )
}

fun FetchCustomerProfileQuery.Data.toDomainCustomer(): Customer? {
    val customerNode = customer ?: return null
    return Customer(
        id = customerNode.id,
        firstName = customerNode.firstName.orEmpty(),
        lastName = customerNode.lastName.orEmpty(),
        email = customerNode.email ?: "",
        phone = customerNode.phone,
        defaultAddress = customerNode.defaultAddress?.let {
            mapAddress(it.id, it.address1, it.address2, it.city, it.province, it.country, it.zip)
        },
        addresses = customerNode.addresses.edges.map { edge ->
            val node = edge.node
            mapAddress(node.id, node.address1, node.address2, node.city, node.province, node.country, node.zip)
        }
    )
}

fun CustomerRegisterMutation.Customer.toDomainCustomer(): Customer {
    return Customer(
        id = id,
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email ?: ""
    )
}

fun CustomerUpdateMutation.Customer.toDomainCustomer(): Customer {
    return Customer(
        id = id,
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email.orEmpty(),
        phone = phone
    )
}
