package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.roomdatabase.pet.PetRepository
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.shopitem.ShopItemRepository
import com.example.zenithpaw.roomdatabase.task.TaskRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import com.example.zenithpaw.roomdatabase.userinventoryitem.UserInventoryItemRepository
import com.example.zenithpaw.ui.pet.PetScreenUiState
import com.example.zenithpaw.ui.pet.toEntity
import com.example.zenithpaw.ui.pet.toPetUiState
import com.example.zenithpaw.ui.uievents.PetUiEvent
import com.example.zenithpaw.ui.userinventoryitem.UserInventoryItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val taskRepository: TaskRepository,
    private val shopItemRepository: ShopItemRepository,
    private val userRepository: UserRepository,
    private val userInventoryItemRepository: UserInventoryItemRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(PetScreenUiState(isLoading = true))
    val uiState: StateFlow<PetScreenUiState> = _uiState.asStateFlow()

    /**
     * Initialize the ViewModel by observing pet data
     */
    init{
        observePetScreenData()
    }

    /**
     * Observe pet screen data and update UI state
     */
    private fun observePetScreenData(){
        // Observe the user data
        viewModelScope.launch {
            userRepository.getUsers()
                .distinctUntilChanged { old, new ->
                    old.firstOrNull()?.userId == new.firstOrNull()?.userId
                }.flatMapLatest { users ->
                    val currentUser = users.firstOrNull()
                    if (currentUser == null){
                        flowOf(PetScreenUiState(isLoading = false))
                    } else {
                        // Observe the pets and the user inventory with shop inventory details
                        combine(
                        petRepository.getPetsForUser(currentUser.userId),
                        userInventoryItemRepository.getUserInventoryItemsByUserId(currentUser.userId),
                            shopItemRepository.getShopItems()
                        ){ pets, inventory, shopItems ->
                            // Get the current selected pet
                            val currentSelection = _uiState.value.selectedPet
                            val newPetList = pets.map { it.toPetUiState() } // Convert Pet entities to PetUiState

                            // If a pet is selected, find the updated version
                            // Otherwise, default to the first pet in the list
                            val updateSelection = newPetList.find { it.petId == currentSelection?.petId}
                                ?: newPetList.firstOrNull()

                            PetScreenUiState(
                                pets = newPetList,
                                isLoading = false,
                                selectedPet = updateSelection,
                                // Map the inventory items
                                // get the details for the inventory items from shopItems list
                                items = inventory.map { item ->
                                    val details = shopItems.find { it.shopItemId == item.shopItemId }
                                    UserInventoryItemUiState(
                                        inventoryItemId = item.inventoryItemId,
                                        quantity = item.quantity,
                                        // Get the details for the inventory item from the shopItems list
                                        name = details?.name ?: "",
                                        imageUrl = details?.imageUrl ?: "",
                                    )
                                }
                            )
                        }
                    }
                }.collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    /**
     * Handle the pet screen interface events
     */
    fun onEvent(event: PetUiEvent){
        when (event){
            //Selection Events
            is PetUiEvent.OnPetSelected -> onPetSelected(event.petId)
            is PetUiEvent.OnInventoryItemSelected -> onInventoryItemSelected(event.inventoryItemId)

            // Name Change Dialog visibility
            PetUiEvent.OnShowNameChangeDialogClicked -> _uiState.update { it.copy(isNameChangeDialogVisible = true)}
            PetUiEvent.OnDismissNameChangeDialogClicked -> _uiState.update {it.copy(isNameChangeDialogVisible = false)}

            // Pet Feed Dialog visibility
            PetUiEvent.OnShowFeedDialogClicked -> _uiState.update { it.copy( isItemSelectionDialogVisible = true)}
            PetUiEvent.OnDismissFeedDialogClicked -> _uiState.update { it.copy(isItemSelectionDialogVisible = false)}

            // Save Pet Name
            is PetUiEvent.OnSavePetNameClicked -> {onSavePetNameClicked(event.newName)}

            // Pet Actions
            PetUiEvent.OnFeedPetConfirmed -> {onFeedPetConfirmed()}
            PetUiEvent.OnPlayWithPetClicked ->{ onPlayWithPetClicked()}
            PetUiEvent.OnStopPlayingWithPetClicked ->{ onStopPlayingWithPet()}
        }
    }

    /**
     * Feed the selected pet and perform the corresponding feed logic
     */
    private fun onFeedPetConfirmed(){
        // Logic for feeding the pet
        val selectedItem = _uiState.value.selectedItem
        val selectedPet = _uiState.value.selectedPet

        if (selectedItem == null) {
            _uiState.update { it.copy(errorMessage = "No Item Selected") }
            return
        }
        if (selectedPet == null) {
            _uiState.update { it.copy(errorMessage = "No Pet Selected") }
            return
        }

        // Check if the pet is already eating
        if (selectedPet.petState == PetState.Eat){
            return
        }

        viewModelScope.launch{
            val inventoryItem = userInventoryItemRepository.getUserInventoryItemById(selectedItem.inventoryItemId)

            //Decrement quantity if greater than 0
            if (inventoryItem.quantity > 0){
                val newQuantity = inventoryItem.quantity - 1

                // Update the inventory item in the database; when the item quantity is 0, delete the item
                if (newQuantity <= 0) {
                    // Delete the existing inventory item
                    userInventoryItemRepository.deleteUserInventoryItem(inventoryItem)
                    _uiState.update { it.copy(selectedItem = null) } //Clear the selected item in the UI
                } else {
                    // Update the inventory item with the new decreased quantity
                    val updatedInventoryItem = inventoryItem.copy(quantity = newQuantity)
                    userInventoryItemRepository.upsertUserInventoryItem(updatedInventoryItem)
                }

                // Start the Animation for pet eating
                // Need to setup animation url to reference once the pet state changes in ComposeUI
                val petUpdating = petRepository.getPetById(selectedPet.petId)
                petUpdating?.let {
                    // Update the pet state to eating
                    petRepository.upsertPet(it.copy(petState = PetState.Eat))
                }
                _uiState.update { it.copy(isItemSelectionDialogVisible = false)}

                //-- Delay for 10 seconds then stop eating--
                delay(10000)

                // Get the latest snapshot of the pet before updating back to idle to avoid overwriting background/other changes to the pet
                petRepository.getPetById(selectedPet.petId)?.let { latestPet ->
                    petRepository.upsertPet(latestPet.copy(petState = PetState.Idle))
                }
            }
        }
    }

    /**
     * Play with the selected pet
     */
    private fun onPlayWithPetClicked(){
        // Animation logic for pet play
        viewModelScope.launch {
            _uiState.value.selectedPet?.let {pet ->
                // For now make pet jump up and down
                // --Need check if the assets for pet are already downloaded
                // --Need to setup the animation Url to reference for the jump animation in Compose UI
                val updatedPet = pet.copy(petState = PetState.Jump)
                val updatedEntity = updatedPet.toEntity()
                // Update the existing pet in the database
                petRepository.upsertPet(updatedEntity)
            }
        }
    }

    /**
     * Stop playing with the selected pet
     */
    private fun onStopPlayingWithPet(){
        viewModelScope.launch {
            _uiState.value.selectedPet?.let { pet ->
                // Set animation url to default
                val updatedPet = pet.copy(petState = PetState.Idle)
                val updatedEntity = updatedPet.toEntity()
                // Update the existing pet in the database
                petRepository.upsertPet(updatedEntity)
            }
        }
    }

    /**
     * Save the new name of the selected pet
     */
   private fun onSavePetNameClicked(newName: String){
       if (newName.isBlank()) {
           _uiState.update { it.copy(errorMessage = "Pet name cannot be blank") }
           return
       }
       viewModelScope.launch{
           _uiState.value.selectedPet?.let { pet ->
               val updatedPet = pet.copy(name = newName)
               val updatedEntity = updatedPet.toEntity() // converts the UI state to pet entity
               // Update the existing pet in the database
               petRepository.upsertPet(updatedEntity)
               _uiState.update { it.copy(isNameChangeDialogVisible = false, errorMessage = null)}
           }
       }
   }

    /**
     * Preview the selected pet in the UI state
     */
    private fun onPetSelected(petId: String){
        if (selectPet(petId)){
            _uiState.update { it.copy(isPetSelectionDialogVisible = true)}
        }
    }

    /**
     * Preview the selected inventory item in the UI state
     */
    private fun onInventoryItemSelected(inventoryItemId: String){
        if (selectItem(inventoryItemId)) {
            _uiState.update { it.copy(isItemSelectionDialogVisible = true)}
        }
    }

    /**
     * Update the selected inventory item in the UI state
     */
    private fun selectItem(inventoryItemId : String): Boolean{
        val item = _uiState.value.items.find { it.inventoryItemId == inventoryItemId }
        if (item != null ){
            _uiState.update {it.copy(selectedItem = item, errorMessage = null)}
            return true
        }
        return false
    }

    /**
     * Update the selected pet in the UI state
     */
    private fun selectPet(petId: String): Boolean{
        val pet = _uiState.value.pets.find {it.petId == petId}
        if (pet != null){
            _uiState.update { it.copy(selectedPet = pet, errorMessage = null)}
            return true
        }
        return false
    }

}