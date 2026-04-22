# API Contracts: Shopify Storefront API (GraphQL)

**Feature**: `001-shopflow-ecommerce-app`
**Date**: 2026-04-22
**API**: Shopify Storefront API (2024-10 or latest stable version)
**Client**: Apollo Kotlin
**Auth**: `X-Shopify-Storefront-Access-Token` header (public access token)

> These contracts define the GraphQL queries and mutations the app will use.
> Each `.graphql` file MUST be placed in the Apollo `graphql/` source set
> and codegen'd — never raw strings.

---

## Queries

### Q-001: FetchCollections

Fetches all product collections (categories) for the Home screen.

```graphql
query FetchCollections($first: Int!) {
  collections(first: $first) {
    edges {
      node {
        id
        title
        description
        handle
        image {
          url
          altText
          width
          height
        }
      }
    }
  }
}
```

### Q-002: FetchFeaturedProducts

Fetches featured products for the Home screen (e.g., from a "Featured"
collection or first N products).

```graphql
query FetchFeaturedProducts($first: Int!, $query: String) {
  products(first: $first, query: $query, sortKey: BEST_SELLING) {
    edges {
      node {
        id
        title
        description
        vendor
        productType
        availableForSale
        images(first: 5) {
          edges {
            node { url altText width height }
          }
        }
        variants(first: 10) {
          edges {
            node {
              id
              title
              availableForSale
              price { amount currencyCode }
              compareAtPrice { amount currencyCode }
              selectedOptions { name value }
              image { url altText }
            }
          }
        }
        priceRange {
          minVariantPrice { amount currencyCode }
          maxVariantPrice { amount currencyCode }
        }
      }
    }
  }
}
```

### Q-003: FetchProductsByCollection

Fetches products within a specific collection (category filtering).

```graphql
query FetchProductsByCollection($collectionId: ID!, $first: Int!, $after: String) {
  collection(id: $collectionId) {
    id
    title
    products(first: $first, after: $after) {
      pageInfo { hasNextPage endCursor }
      edges {
        node {
          id
          title
          vendor
          productType
          availableForSale
          images(first: 1) {
            edges { node { url altText } }
          }
          priceRange {
            minVariantPrice { amount currencyCode }
          }
          variants(first: 10) {
            edges {
              node {
                id title availableForSale
                price { amount currencyCode }
                selectedOptions { name value }
              }
            }
          }
        }
      }
    }
  }
}
```

### Q-004: FetchProductDetail

Fetches full product details for the Product Details screen.

```graphql
query FetchProductDetail($id: ID!) {
  product(id: $id) {
    id
    title
    description
    descriptionHtml
    vendor
    productType
    availableForSale
    images(first: 10) {
      edges {
        node { url altText width height }
      }
    }
    variants(first: 30) {
      edges {
        node {
          id
          title
          availableForSale
          price { amount currencyCode }
          compareAtPrice { amount currencyCode }
          selectedOptions { name value }
          image { url altText }
        }
      }
    }
    priceRange {
      minVariantPrice { amount currencyCode }
      maxVariantPrice { amount currencyCode }
    }
  }
}
```

### Q-005: SearchProducts

Searches products by keyword (Home search bar).

```graphql
query SearchProducts($query: String!, $first: Int!) {
  products(first: $first, query: $query) {
    edges {
      node {
        id
        title
        vendor
        productType
        availableForSale
        images(first: 1) {
          edges { node { url altText } }
        }
        priceRange {
          minVariantPrice { amount currencyCode }
        }
      }
    }
  }
}
```

### Q-006: FetchCustomerOrders

Fetches order history for an authenticated customer.

```graphql
query FetchCustomerOrders($accessToken: String!, $first: Int!, $after: String) {
  customer(customerAccessToken: $accessToken) {
    orders(first: $first, after: $after, sortKey: PROCESSED_AT, reverse: true) {
      pageInfo { hasNextPage endCursor }
      edges {
        node {
          id
          orderNumber
          name
          processedAt
          fulfillmentStatus
          totalPrice { amount currencyCode }
          lineItems(first: 10) {
            edges {
              node {
                title
                quantity
                variant {
                  title
                  price { amount currencyCode }
                  image { url altText }
                }
              }
            }
          }
          shippingAddress {
            address1 address2 city province country zip
          }
        }
      }
    }
  }
}
```

### Q-007: FetchCustomerProfile

Fetches the authenticated customer's profile data.

```graphql
query FetchCustomerProfile($accessToken: String!) {
  customer(customerAccessToken: $accessToken) {
    id
    firstName
    lastName
    email
    phone
    defaultAddress {
      id address1 address2 city province country zip
    }
    addresses(first: 10) {
      edges {
        node { id address1 address2 city province country zip }
      }
    }
  }
}
```

---

## Mutations

### M-001: CustomerLogin

```graphql
mutation CustomerLogin($input: CustomerAccessTokenCreateInput!) {
  customerAccessTokenCreate(input: $input) {
    customerAccessToken {
      accessToken
      expiresAt
    }
    customerUserErrors {
      code
      field
      message
    }
  }
}
```

**Input**: `{ email: String, password: String }`

### M-002: CustomerRegister

```graphql
mutation CustomerRegister($input: CustomerCreateInput!) {
  customerCreate(input: $input) {
    customer {
      id
      firstName
      lastName
      email
    }
    customerUserErrors {
      code
      field
      message
    }
  }
}
```

**Input**: `{ firstName: String, lastName: String, email: String, password: String }`

### M-003: CustomerUpdate

```graphql
mutation CustomerUpdate($accessToken: String!, $input: CustomerUpdateInput!) {
  customerUpdate(customerAccessToken: $accessToken, customer: $input) {
    customer {
      id firstName lastName email phone
    }
    customerUserErrors {
      code field message
    }
  }
}
```

### M-004: CheckoutCreate

```graphql
mutation CheckoutCreate($input: CheckoutCreateInput!) {
  checkoutCreate(input: $input) {
    checkout {
      id
      webUrl
      totalPrice { amount currencyCode }
      lineItems(first: 20) {
        edges {
          node {
            title
            quantity
            variant { title price { amount currencyCode } }
          }
        }
      }
    }
    checkoutUserErrors {
      code field message
    }
  }
}
```

**Input**: `{ lineItems: [{ variantId: ID!, quantity: Int! }] }`

### M-005: CustomerAccessTokenRenew

```graphql
mutation CustomerAccessTokenRenew($accessToken: String!) {
  customerAccessTokenRenew(customerAccessToken: $accessToken) {
    customerAccessToken {
      accessToken
      expiresAt
    }
    userErrors {
      field message
    }
  }
}
```

---

## Error Handling Contract

All API responses MUST be wrapped in a sealed `Result` type:

```text
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class GraphQLError(val errors: List<String>) : ApiResult<Nothing>()
    data class NetworkError(val exception: Throwable) : ApiResult<Nothing>()
    data object Empty : ApiResult<Nothing>()
}
```

Mapping rules:
- Apollo `ApolloResponse.data != null` → `Success`
- Apollo `ApolloResponse.errors` non-empty → `GraphQLError`
- Apollo `ApolloException` → `NetworkError`
- `data` is null and no errors → `Empty`
