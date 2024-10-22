package com.wasin.domain.db

import com.wasin.data.data_db.WifiRepository
import com.wasin.domain.usecase.wifi.DeleteAllWifi
import com.wasin.domain.usecase.wifi.GetWifiList
import com.wasin.domain.usecase.wifi.InsertAllWifi
import com.wasin.domain.usecase.wifi.WifiUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWIfiUseCase(repository: WifiRepository): WifiUseCase {
        return WifiUseCase(
            getWifiList = GetWifiList(repository),
            deleteAllWifi = DeleteAllWifi(repository),
            insertAllWifi = InsertAllWifi(repository),
        )
    }
}
