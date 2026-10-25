package com.example.zenithpaw.ui.shopitem

import com.example.zenithpaw.R

/**
 * Class that maps shop item names(keys) to their corresponding image resources(values).
 */
object ShopItemResourceMapper{
    private val shopItemResourceMap: Map<String, Int> = mapOf(
        "broccoli" to R.drawable.broccoli,
        "carrot" to R.drawable.carrot,
        "strawberry" to R.drawable.strawberry,
        "cod" to R.drawable.cod,
        "koi" to R.drawable.koi,
        "tilapia" to R.drawable.tilapia,
        "tuna" to R.drawable.tuna,
        "blue_charm" to R.drawable.blue_charm,
        "brown_charm" to R.drawable.brown_charm,
        "green_charm" to R.drawable.green_charm,
        "pink_charm" to R.drawable.pink_charm,
    )

    /**
     * Get the resource ID for a given shop item name.
     */
    fun getResourceId(name: String): Int {
        return shopItemResourceMap[name] ?: R.drawable.blue_charm
    }
}