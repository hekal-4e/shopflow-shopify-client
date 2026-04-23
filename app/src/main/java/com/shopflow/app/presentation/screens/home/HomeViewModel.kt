package com.shopflow.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Collection
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.usecase.product.GetCollectionsUseCase
import com.shopflow.app.domain.usecase.product.GetFeaturedProductsUseCase
import com.shopflow.app.domain.usecase.product.SearchProductsUseCase
import com.shopflow.app.domain.usecase.profile.GetCustomerProfileUseCase
import com.shopflow.app.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.collectLatest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val isSearchMode: Boolean = false,
    val searchQuery: String = "",
    val collections: List<Collection> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val banners: List<String> = emptyList(),
    val error: String? = null,
    val customerName: String = "",
    val customerAvatarUrl: String? = null,
    val unreadNotificationCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCustomerProfileUseCase: GetCustomerProfileUseCase,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
        observeNotifications()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // In a real app, you might want to fetch these in parallel
            val collectionsResult = getCollectionsUseCase()
            val featuredResult = getFeaturedProductsUseCase()
            val profileResult = getCustomerProfileUseCase()
            
            var collections: List<Collection> = emptyList()
            var featured: List<Product> = emptyList()
            var name = ""
            var error: String? = null

            if (collectionsResult is ApiResult.Success) collections = collectionsResult.data
            else if (collectionsResult is ApiResult.NetworkError) error = collectionsResult.exception.message

            if (featuredResult is ApiResult.Success) featured = featuredResult.data
            else if (featuredResult is ApiResult.NetworkError && error == null) error = featuredResult.exception.message

            if (profileResult is ApiResult.Success) {
                name = profileResult.data.firstName
            }

            // Mock banners for now
            val banners = listOf("https://picsum.photos/800/400", "https://picsum.photos/800/401")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    collections = collections,
                    featuredProducts = featured,
                    customerName = name,
                    customerAvatarUrl = if (name.isNotEmpty()) "https://i.pravatar.cc/150?u=$name" else "https://i.pravatar.cc/150",
                    banners = banners,
                    error = error
                )
            }
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount().collectLatest { count ->
                _uiState.update { it.copy(unreadNotificationCount = count) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, isSearchMode = query.isNotEmpty()) }
        if (query.length > 2) {
            performSearch(query)
        } else if (query.isEmpty()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = searchProductsUseCase(query)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, searchResults = result.data) }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { it.copy(isLoading = false, error = "Search failed") }
                }
                is ApiResult.Empty -> {
                    _uiState.update { it.copy(isLoading = false, searchResults = emptyList()) }
                }
            }
        }
    }
}
