package com.example.zenithpaw.ui.pet

import com.example.zenithpaw.R
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.pet.PetType

/**
 * Class that maps pet states to their corresponding animation resources.
 */
object PetAnimationMapper {
    /**
     * Animation map for pet types and their states & animation resources
     */
    private val animationMap: Map<PetType, Map<PetState, Int>> = mapOf(
        PetType.CAT to mapOf(
            PetState.Idle to R.drawable.cat_idle,
            PetState.Walk to R.drawable.cat_walk,
            PetState.Run to R.drawable.cat_run,
        ),
        PetType.RABBIT to mapOf(
            PetState.Idle to R.drawable.bunny_idle,
            PetState.Run to R.drawable.bunny_running
        ),
    )

    /**
     * Get the animation resource ID for a given pet type and state.
     */
    fun getAnimation(type: PetType, state: PetState):Int {
        val petMap = animationMap[type] ?: animationMap[PetType.CAT]!!
        return petMap[state] ?: R.drawable.cat_idle
    }
}