package com.aspharier.finora

import android.app.Application
import com.aspharier.finora.domain.repository.CategoryRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FinoraApp : Application() {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    override fun onCreate() {
        super.onCreate()
        
        // Initialize default categories
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            categoryRepository.initializeDefaultCategories()
        }
    }
}