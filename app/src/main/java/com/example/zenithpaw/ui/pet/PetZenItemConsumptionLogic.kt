package com.example.zenithpaw.ui.pet

import com.example.zenithpaw.roomdatabase.pet.Pet
import com.example.zenithpaw.roomdatabase.pet.PetType
import com.example.zenithpaw.roomdatabase.shopitem.ShopItem

object PetZenItemConsumptionLogic {

    /**
     * Check if the item is favorable for the pet type
     */
    private fun checkItemWithFavorablePetType(petType: PetType, shopItem: ShopItem): Boolean{
        when (petType){
            PetType.CAT -> {
                if (shopItem.shopItemId == "fish_shop_item_1"){
                    return true
                }
                return false
            }
            PetType.RABBIT -> {
                if (shopItem.shopItemId == "carrot_shop_item_2"){
                    return true
                }
                return false
            }
        }
    }

    /**
     * Consume the item for zen for the pet
     */
    fun consumeItemForZen(shopItem: ShopItem, pet: Pet): Int {
        /**
         * The item multiplier determined by PetType and Item
         */
        var itemFactor = 1
        /**
         * The final value of the item
         */
        var finalValue = 0

        // Determine the value of the item
        val itemValue = shopItem.value
        // Determine the type of Pet
        val petType = pet.species
        // Determine the zen of the pet
        val petZen = pet.zen

        // Logic holds true for only 2 shop items and pet types here
        if (petType == PetType.CAT) {
            if(checkItemWithFavorablePetType(petType, shopItem)){
                itemFactor = 4
            }
            finalValue = itemValue * itemFactor
            finalValue += petZen
        } else if (petType == PetType.RABBIT) {
            if(checkItemWithFavorablePetType(petType, shopItem)){
                itemFactor = 4
            }
            finalValue = itemValue * itemFactor
            finalValue += petZen
        }

        return finalValue
    }
}