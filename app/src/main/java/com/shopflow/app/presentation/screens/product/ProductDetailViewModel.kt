package com.shopflow.app.presentation.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductVariant
import com.shopflow.app.domain.usecase.cart.AddToCartUseCase
import com.shopflow.app.domain.usecase.product.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val selectedVariant: ProductVariant? = null,
    val selectedOptions: Map<String, String> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = URLDecoder.decode(
        checkNotNull(savedStateHandle["productId"]), "UTF-8"
    )

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getProductDetailUseCase(productId)) {
                is ApiResult.Success -> {
                    val product = result.data
                    val defaultVariant = product.variants.firstOrNull()
                    val defaultOptions = defaultVariant?.selectedOptions?.associate { it.name to it.value } ?: emptyMap()
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = product,
                            selectedVariant = defaultVariant,
                            selectedOptions = defaultOptions
                        )
                    }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { it.copy(isLoading = false, error = "Failed to load product") }
                }
                is ApiResult.Empty -> {
                    _uiState.update { it.copy(isLoading = false, error = "Product not found") }
                }
            }
        }
    }

    fun selectOption(name: String, value: String) {
        val currentProduct = _uiState.value.product ?: return
        val newOptions = _uiState.value.selectedOptions.toMutableMap()
        newOptions[name] = value

        // Find the variant that matches the selected options
        val newVariant = currentProduct.variants.find { variant ->
            variant.selectedOptions.all { option -> newOptions[option.name] == option.value }
        } ?: _uiState.value.selectedVariant

        _uiState.update {
            it.copy(
                selectedOptions = newOptions,
                selectedVariant = newVariant
            )
        }
    }

    fun addToCart() {
        val product = _uiState.value.product ?: return
        val variant = _uiState.value.selectedVariant ?: return
        viewModelScope.launch {
            addToCartUseCase(product, variant, 1)
        }
    }
}
