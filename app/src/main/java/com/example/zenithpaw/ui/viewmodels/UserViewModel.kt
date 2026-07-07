package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.user.UserUiState
import com.example.zenithpaw.ui.user.toEntity
import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    // Inject the interface repositories
    private val userRepository: UserRepository,
    private val userInventoryItemRepository: UserInventoryItemRepository,
    private val shopItemRepository: ShopItemRepository,
): ViewModel() {
    //Single source of truth for UI state
    private val _uiState = MutableStateFlow(UserUiState(isLoading = true)) //loading user state first
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    /**
     * Initialize the ViewModel by observing user data
     */
    init {
        observeUserData()
    }

    /**
     * Observe user data and update UI state
     */
    private fun observeUserData(){
        // set state isLoading to true first
        _uiState.update{it.copy(isLoading = true)}
        // Observe the User Profile Name, Email, Gold, and Profile Image
        viewModelScope.launch(Dispatchers.IO) {
            //1. Observe the users
            userRepository.getUsers()
                // .distinctUntilChanged - Prevents UI components from re-rendering if the data hasn't changed
                .distinctUntilChanged { old, new -> // old and new list of users
                    //Only trigger if the user object changed
                    old.firstOrNull()?.userId == new.firstOrNull()?.userId && old.firstOrNull()?.gold == new.firstOrNull()?.gold
                }.collect { users ->
                    val currentUser = users.firstOrNull()
                    if (currentUser == null) {
                        //No user found
                        _uiState.update { it.copy(isLoading = false, userId = "") }
                    } else {
                        _uiState.update { currentState ->
                            val isNewUserMapping = currentState.userId != currentUser.userId
                            currentState.copy(
                                userId = currentUser.userId,
                                lastLogin = currentUser.lastLogin,
                                gold = currentUser.gold,
                                isLoading = false,
                                //If new user mapping: Seed from Database(currentUser)
                                //Else already loaded: Seed from UI State(currentState)
                                name = if (isNewUserMapping) currentUser.name else currentState.name,
                                email = if (isNewUserMapping) currentUser.email else currentState.email,
                                imageUrl = if (isNewUserMapping) currentUser.imageUrl else currentState.imageUrl,
                            )
                        }
                    }
                }
            }
        //Observe the Inventory and Shop Items
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUsers()
                .flatMapLatest { users ->
                    val user = users.firstOrNull()
                    if (user == null) {
                        flowOf(emptyList<UserInventoryItemUiState>())
                    } else {
                        combine(
                            userInventoryItemRepository.getUserInventoryItemsByUserId(user.userId),
                            shopItemRepository.getShopItems()
                        ){
                            inventory, shopItems ->
                            inventory.map { inventoryItem ->
                                val details = shopItems.find { it.shopItemId == inventoryItem.shopItemId }
                                UserInventoryItemUiState(
                                    inventoryItemId = inventoryItem.inventoryItemId,
                                    quantity = inventoryItem.quantity,
                                    name = details?.name ?: "",
                                    imageUrl = details?.imageUrl ?: "",
                                )
                            }
                        }
                    }
                }   .flowOn(Dispatchers.Default) //Run on the default dispatcher since simple transform/filter operations
                    .collect { displayItems ->
                    _uiState.update { it.copy(inventory = displayItems) }
                }
        }
    }

    /**
     * Handle the user interface events
     */
    fun onEvent(event : UserUiEvent){
        when (event){
            //Account Creation for new users
            is UserUiEvent.OnCreateAccountClicked -> {
                onCreateAccountClicked(event.name, event.email)
            }
            //Allow text field changes
            is UserUiEvent.OnNameChange -> {_uiState.update { it.copy(name = event.name) }}
            is UserUiEvent.OnEmailChange -> {_uiState.update { it.copy(email = event.email) }}
            is UserUiEvent.OnChangeProfileImage -> {_uiState.update { it.copy(imageUrl = event.imageUrl) }}

            //Protected Actions allowed only if user exists
            else -> {
                //If the user data is not loaded yet, stop events
                if(_uiState.value.userId.isEmpty()) return
                //Map each event to the appropriate function
                when (event){
                    //Database persistence actions
                    UserUiEvent.OnSaveProfileClicked -> onSaveProfileClicked() // Will save all the changes for User Data
                    UserUiEvent.OnSyncCloudClicked -> onSyncCloudClicked()
                    UserUiEvent.OnDeleteAccountClicked -> onDeleteAccountClicked()

                    //Dialog actions
                    UserUiEvent.OnShowRegisterDialogClicked -> {_uiState.update { it.copy(isRegisteringDialogVisible = true) }}
                    UserUiEvent.OnHideRegisterDialogClicked -> {_uiState.update { it.copy(isRegisteringDialogVisible = false) }}
                    else -> { } //Do nothing
                }
            }
        }
    }

    /**
     * Save the current user data
     */
    private fun onSaveProfileClicked(){
        viewModelScope.launch {
            //Get the current state, convert to the Entity and update the property
            val currentUser = _uiState.value.toEntity()
            //Save to the database
            userRepository.upsertUser(currentUser)
        }
    }

    /**
     * Create a new user account
     */
    private fun onCreateAccountClicked(name: String, email: String){
        viewModelScope.launch {
            // Clear the database since only one user can exist for the application on the device
            userRepository.deleteAllUsers()

            //Create a new user
            val newUser = User(
                userId = UUID.randomUUID().toString(),
                name = name,
                email = email,
                imageUrl = "",
                lastLogin = System.currentTimeMillis(),
                gold = 100,
            )
            //Save to the database
            userRepository.upsertUser(newUser)
            //Close the register dialog
            _uiState.update { it.copy(isRegisteringDialogVisible = false)}
        }
    }

    /**
     * Delete the current user account
     */
    private fun onDeleteAccountClicked(){
        viewModelScope.launch {
            //Delete the current user
            val currentUser = _uiState.value.toEntity()
            userRepository.deleteUser(currentUser)
        }
    }

    /**
     * Sync the user data with the cloud
     */
    private fun onSyncCloudClicked(){
        viewModelScope.launch {
            //1. Get the current user
            val currentUser = _uiState.value.toEntity()
            //2. Sync the user data with the cloud
            //Code to sync the data
        }
    }

}