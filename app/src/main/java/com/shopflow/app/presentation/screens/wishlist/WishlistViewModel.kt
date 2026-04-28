package com.shopflow.app.presentation.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.WishlistItem
import com.shopflow.app.domain.usecase.cart.AddToCartUseCase
import com.shopflow.app.domain.usecase.product.GetProductDetailUseCase
import com.shopflow.app.domain.usecase.wishlist.AddToWishlistUseCase
import com.shopflow.app.domain.usecase.wishlist.GetWishlistUseCase
import com.shopflow.app.domain.usecase.wishlist.RemoveFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOption {
    NEWEST, PRICE_LOW_HIGH, PRICE_HIGH_LOW
}

data class WishlistUiState(
    val products: List<WishlistItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedBrand: String? = null,
    val priceRange: ClosedFloatingPointRange<Float>? = null,
    val sortOption: SortOption = SortOption.NEWEST
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _selectedBrand = MutableStateFlow<String?>(null)
    private val _priceRange = MutableStateFlow<ClosedFloatingPointRange<Float>?>(null)
    private val _sortOption = MutableStateFlow(SortOption.NEWEST)

    val uiState: StateFlow<WishlistUiState> = combine(
        getWishlistUseCase(),
        _selectedBrand,
        _priceRange,
        _sortOption
    ) { wishlist, brand, price, sort ->
        var filtered = wishlist
        if (brand != null) {
            filtered = filtered.filter { it.brand.equals(brand, ignoreCase = true) }
        }
        if (price != null) {
            filtered = filtered.filter { it.price.toFloat() in price }
        }
        filtered = when (sort) {
            SortOption.NEWEST -> filtered.sortedByDescending { it.addedAt }
            SortOption.PRICE_LOW_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.price }
        }

        WishlistUiState(
            products = filtered,
            selectedBrand = brand,
            priceRange = price,
            sortOption = sort
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WishlistUiState(isLoading = true)
    )

    fun setBrandFilter(brand: String?) {
        _selectedBrand.value = brand
    }

    fun setPriceRangeFilter(range: ClosedFloatingPointRange<Float>?) {
        _priceRange.value = range
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            removeFromWishlistUseCase(productId)
        }
    }

    fun addProductToCart(productId: String) {
        viewModelScope.launch {
            when (val result = getProductDetailUseCase(productId)) {
                is ApiResult.Success -> {
                    val product = result.data
                    val variant = product.variants.firstOrNull() ?: return@launch
                    addToCartUseCase(product, variant, 1)
                }
                else -> Unit
            }
        }
    }
}
