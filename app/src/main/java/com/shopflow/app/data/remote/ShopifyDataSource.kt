package com.shopflow.app.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.shopflow.app.CheckoutCreateMutation
import com.shopflow.app.CustomerAccessTokenRenewMutation
import com.shopflow.app.CustomerLoginMutation
import com.shopflow.app.CustomerRegisterMutation
import com.shopflow.app.CustomerUpdateMutation
import com.shopflow.app.FetchCollectionsQuery
import com.shopflow.app.FetchCustomerOrdersQuery
import com.shopflow.app.FetchCustomerProfileQuery
import com.shopflow.app.FetchFeaturedProductsQuery
import com.shopflow.app.FetchProductDetailQuery
import com.shopflow.app.FetchProductsByCollectionQuery
import com.shopflow.app.SearchProductsQuery
import com.shopflow.app.type.CheckoutCreateInput
import com.shopflow.app.type.CustomerAccessTokenCreateInput
import com.shopflow.app.type.CustomerCreateInput
import com.shopflow.app.type.CustomerUpdateInput
import javax.inject.Inject

class ShopifyDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun fetchCollections(first: Int): ApolloResponse<FetchCollectionsQuery.Data> {
        return apolloClient.query(FetchCollectionsQuery(first = first)).execute()
    }

    suspend fun fetchFeaturedProducts(
        first: Int,
        query: String?
    ): ApolloResponse<FetchFeaturedProductsQuery.Data> {
        return apolloClient.query(
            FetchFeaturedProductsQuery(first = first, query = Optional.presentIfNotNull(query))
        ).execute()
    }

    suspend fun fetchProductsByCollection(
        collectionId: String,
        first: Int,
        after: String?
    ): ApolloResponse<FetchProductsByCollectionQuery.Data> {
        return apolloClient.query(
            FetchProductsByCollectionQuery(
                collectionId = collectionId,
                first = first,
                after = Optional.presentIfNotNull(after)
            )
        ).execute()
    }

    suspend fun fetchProductDetail(id: String): ApolloResponse<FetchProductDetailQuery.Data> {
        return apolloClient.query(FetchProductDetailQuery(id = id)).execute()
    }

    suspend fun searchProducts(
        query: String,
        first: Int
    ): ApolloResponse<SearchProductsQuery.Data> {
        return apolloClient.query(SearchProductsQuery(query = query, first = first)).execute()
    }

    suspend fun fetchCustomerOrders(
        accessToken: String,
        first: Int,
        after: String?
    ): ApolloResponse<FetchCustomerOrdersQuery.Data> {
        return apolloClient.query(
            FetchCustomerOrdersQuery(
                accessToken = accessToken,
                first = first,
                after = Optional.presentIfNotNull(after)
            )
        ).execute()
    }

    suspend fun fetchCustomerProfile(accessToken: String): ApolloResponse<FetchCustomerProfileQuery.Data> {
        return apolloClient.query(FetchCustomerProfileQuery(accessToken = accessToken)).execute()
    }

    suspend fun customerLogin(input: CustomerAccessTokenCreateInput): ApolloResponse<CustomerLoginMutation.Data> {
        return apolloClient.mutation(CustomerLoginMutation(input = input)).execute()
    }

    suspend fun customerRegister(input: CustomerCreateInput): ApolloResponse<CustomerRegisterMutation.Data> {
        return apolloClient.mutation(CustomerRegisterMutation(input = input)).execute()
    }

    suspend fun customerUpdate(
        accessToken: String,
        input: CustomerUpdateInput
    ): ApolloResponse<CustomerUpdateMutation.Data> {
        return apolloClient.mutation(
            CustomerUpdateMutation(accessToken = accessToken, input = input)
        ).execute()
    }

    suspend fun checkoutCreate(input: CheckoutCreateInput): ApolloResponse<CheckoutCreateMutation.Data> {
        return apolloClient.mutation(CheckoutCreateMutation(input = input)).execute()
    }

    suspend fun customerAccessTokenRenew(accessToken: String): ApolloResponse<CustomerAccessTokenRenewMutation.Data> {
        return apolloClient.mutation(CustomerAccessTokenRenewMutation(accessToken = accessToken)).execute()
    }
}
