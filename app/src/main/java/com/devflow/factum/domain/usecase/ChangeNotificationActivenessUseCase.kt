package com.devflow.factum.domain.usecase

import android.app.Application
import com.devflow.factum.R
import com.devflow.factum.domain.model.Time
import com.devflow.factum.domain.repository.SharedPrefRepository
import javax.inject.Inject

class ChangeNotificationActivenessUseCase @Inject constructor(
    private val repository: SharedPrefRepository,
    private val appContext: Application
) {
    suspend fun execute(time: Time) {
        val name = appContext.getString(R.string.shared_pref_main_notifications)
        val data = repository.read(name, emptySet<String>()).replace(
            oldData = time.toString(),
            newData = time.copy(isActive = !time.isActive).toString()
        )

        repository.write(
            name = name,
            data = data
        )
    }
}

fun Set<String>.replace(
    oldData: String,
    newData: String
): Set<String> {
    return this.minus(oldData).plus(newData)
}