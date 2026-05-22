package com.example.zenithpaw

import android.content.Context
import android.util.Log
import com.example.zenithpaw.roomdatabase.pet.PetRepository
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.task.TaskRepository
import com.example.zenithpaw.roomdatabase.user.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.example.zenithpaw.roomdatabase.task.TaskState

@Singleton
/**
 * AppLifecycleManager class to handle the app's lifecycle events
 */
class AppLifecycleManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskRepository: TaskRepository,
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
    ){
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Save the timestamp from when the app was paused
     */
    fun handleAppPaused(){
        // Migrate from System.currentTimeMillis() to TrustedTime or SystemClock.elapsedRealtime()
        // To prevent clock spoofing
        prefs.edit { putLong("last_app_at_time", System.currentTimeMillis()) }
    }

    /**
     * Does the calculation for pet health loss based on time elapsed for the app being in the background
     * Calculate the user gold for the amount of time for the active task spent
     */
    fun handleAppResumed(){
        scope.launch{
            try {
                // Get the last time the app was at the foreground
                val timeLeft = prefs.getLong("last_app_at_time", 0L)
                if (timeLeft == 0L) {
                    return@launch
                } // No time found return

                // Get the current time
                val currentTime = System.currentTimeMillis()
                val totalSeconds = (currentTime - timeLeft) / 1000 // total seconds from app pause

                // Get the current user
                val users = userRepository.getUsers().first()
                val currentUser = users.firstOrNull()
                if (currentUser == null) {
                    return@launch
                }

                // Get the active task for the current user
                val task = taskRepository.getActiveTaskForUser(currentUser.userId)
                val activeTask = task.firstOrNull()
                if (activeTask == null) {
                    return@launch
                }

                // Calculate pet health loss based on time elapsed
                val healthLossRatePerSecond =
                    0.0001 // Adjust this value to control the rate of health loss
                val healthTotalLoss = (totalSeconds * healthLossRatePerSecond).toInt()

                // Get the pets for the user
                val pets = petRepository.getPetsForUser(currentUser.userId).first()

                val petsWithHealthLoss = pets.map { pet ->
                    // Calculate the health loss for each pet (All Pets for now)
                    val petHealthRemaining = pet.zen - healthTotalLoss
                    if (petHealthRemaining <= 0) {
                        pet.copy(zen = 0, petState = PetState.Death)
                    } else {
                        pet.copy(zen = petHealthRemaining)
                    }
                }

                // Update the pets' health in the database
                petRepository.upsertPets(petsWithHealthLoss)

                // --Gold Earning Logic--
                // Only reward gold for the time the task was actually running while away

                val goldRatePerSecond = 0.05

                // Get gold earned between the task's limit duration or the time the app was paused
                val secondsToReward = totalSeconds.coerceAtMost(activeTask.time) // get the value that is less

                // Calculate the gold earned for the user
                if (secondsToReward > 0L){
                    val goldEarned = (secondsToReward.toDouble() * goldRatePerSecond).toInt()

                    if (goldEarned > 0) {
                        val updatedUser = currentUser.copy(gold = currentUser.gold + goldEarned)
                        userRepository.upsertUser(updatedUser)
                        Log.d("AppLifecycleManager", "Catch-up: User earned $goldEarned gold.")
                    }
                    // Update the task time since time elapsed
                    val remainingTaskTime = activeTask.time - secondsToReward

                    // Task is completed if the remaining time is 0 or less
                    if (remainingTaskTime <= 0L){
                        taskRepository.upsertTask(activeTask.copy(time = 0L, taskState = TaskState.Completed))
                    } else {
                        // Task is still ongoing
                        taskRepository.upsertTask(activeTask.copy(time = remainingTaskTime))
                    }
                }

                // Reset the time stamp for the app pause
                prefs.edit { remove("last_app_at_time") }
            } catch (e: Exception) {
                Log.e("AppLifecycleManager", "Error in handleAppResumed: ", e)
                prefs.edit { remove("last_app_at_time") }
            }
        }
    }
}