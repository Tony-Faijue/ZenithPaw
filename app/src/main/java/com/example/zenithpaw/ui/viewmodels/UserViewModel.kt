package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemDao
import com.example.zenithpaw.roomdatabase.user.User
import com.example.zenithpaw.roomdatabase.user.UserDao
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemDao
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.user.UserUiState
import com.example.zenithpaw.ui.user.toEntity
import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao,
    private val userInventoryItemDao: UserInventoryItemDao,
    private val shopItemDao: ShopItemDao,
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
        viewModelScope.launch {
            //1. Observe the users
            userDao.getUsers().collect { users ->
                val currentUser = users.firstOrNull()
                if (currentUser != null){
                    //2. Observe both the user inventory items for currentUser and shop items
                    combine(
                        userInventoryItemDao.getUserInventoryItemsByUserId(currentUser.userId),
                        shopItemDao.getShopItems()
                        ) { inventory, shopItems ->
                            //3. Combine the inventory items and shop items
                            val displayItems = inventory.map { inventoryItem ->
                                //Find the corresponding shop item
                                val details = shopItems.find { it.shopItemId == inventoryItem.shopItemId }
                                //Convert to the UI state
                                UserInventoryItemUiState(
                                    inventoryItemId = inventoryItem.inventoryItemId,
                                    name = details?.name ?: "",
                                    imageUrl = details?.imageUrl ?: "",
                                    quantity = inventoryItem.quantity,
                                )
                            }
                            //4. Update UI state with user data and inventory items
                            _uiState.update{ currentState ->
                                currentState.copy(
                                    userId = currentUser.userId,
                                    name = currentUser.name,
                                    email = currentUser.email,
                                    imageUrl = currentUser.imageUrl,
                                    lastLogin = currentUser.lastLogin,
                                    gold = currentUser.gold,
                                    inventory = displayItems,
                                    isLoading = false,
                                )
                            }
                        }.collect {}
                }
                else {
                    //No user found
                    _uiState.update {it.copy(isLoading = false)}
                }
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
            else -> {
                //If the user data is not loaded yet, stop events
                if(_uiState.value.userId.isEmpty()) return
                //Map each event to the appropriate function
                when (event){
                    is UserUiEvent.OnNameChange -> onNameChange(event.name)
                    is UserUiEvent.OnEmailChange -> onEmailChange(event.email)
                    is UserUiEvent.OnChangeProfileImage -> onChangeProfileImage(event.imageUrl)

                    UserUiEvent.OnSaveProfileClicked -> onSaveProfileClicked()
                    UserUiEvent.OnSyncCloudClicked -> onSyncCloudClicked()
                    UserUiEvent.OnDeleteAccountClicked -> onDeleteAccountClicked()
                    else -> { } //Do nothing
                }
            }
        }
    }

    /**
     * Update the user's name in the database
     */
    private fun onNameChange(newName: String){
        viewModelScope.launch {
            //Get the current state, convert to the Entity and update the property
            val currentUser = _uiState.value.toEntity().copy(name = newName)
            //Save to the database
            userDao.updateUser(currentUser)
        }
    }

    /**
     * Update the user's email in the database
     */
    private fun onEmailChange(newEmail: String){
        viewModelScope.launch {
            //Get the current state, convert to the Entity and update the property
            val currentUser = _uiState.value.toEntity().copy(email = newEmail)
            //Save to the database
            userDao.updateUser(currentUser)
        }
    }

    /**
     * Update the user's profile image in the database
     */
    private fun onChangeProfileImage(newImageUrl: String){
        viewModelScope.launch {
            //Get the current state, convert to the Entity and update the property
            val currentUser = _uiState.value.toEntity().copy(imageUrl = newImageUrl)
            //Save to the database
            userDao.updateUser(currentUser)
        }
    }

    private fun onSaveProfileClicked(){
        viewModelScope.launch {
            //Get the current state, convert to the Entity and update the property
            val currentUser = _uiState.value.toEntity()
            //Save to the database
            userDao.upsertUser(currentUser)
        }
    }

    /**
     * Create a new user account
     */
    private fun onCreateAccountClicked(name: String, email: String){
        viewModelScope.launch {
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
            userDao.upsertUser(newUser)
        }
    }

    /**
     * Delete the current user account
     */
    private fun onDeleteAccountClicked(){
        viewModelScope.launch {
            //Delete the current user
            val currentUser = _uiState.value.toEntity()
            userDao.deleteUser(currentUser)
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