package com.wasin.data.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.wasin.data._const.DataStoreKey
import com.wasin.data._const.HttpRoutes
import com.wasin.data.api.BackOfficeApi
import com.wasin.data.api.CompanyApi
import com.wasin.data.api.HandOffApi
import com.wasin.data.api.MonitoringApi
import com.wasin.data.api.ProfileApi
import com.wasin.data.api.RouterApi
import com.wasin.data.api.UserApi
import com.wasin.data.data_api.BackOfficeRepository
import com.wasin.data.data_api.CompanyRepository
import com.wasin.data.data_api.HandOffRepository
import com.wasin.data.data_api.MonitoringRepository
import com.wasin.data.data_api.ProfileRepository
import com.wasin.data.data_api.RouterRepository
import com.wasin.data.data_api.UserRepository
import com.wasin.data.data_db.WifiRepository
import com.wasin.data.database.WifiDatabase
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.user.ReissueRequest
import com.wasin.data.model.user.ReissueResponse
import com.wasin.data.repository.BackOfficeRepositoryImpl
import com.wasin.data.repository.CompanyRepositoryImpl
import com.wasin.data.repository.HandOffRepositoryImpl
import com.wasin.data.repository.MonitoringRepositoryImpl
import com.wasin.data.repository.ProfileRepositoryImpl
import com.wasin.data.repository.RouterRepositoryImpl
import com.wasin.data.repository.UserRepositoryImpl
import com.wasin.data.repository.WifiRepositoryImpl
import com.wasin.data.util.ApiUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val accessKey = DataStoreKey.ACCESS_TOKEN_KEY.name
    private val refreshKey = DataStoreKey.REFRESH_TOKEN_KEY.name

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context
    ): HttpClient {
        return HttpClient(CIO) {
            install(Logging) {
                logger = object: Logger {
                    override fun log(message: String) {
                        Log.d("wasin_api", message)
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json{
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 1000 * 60 * 3
                requestTimeoutMillis = 1000 * 60 * 3
                socketTimeoutMillis = 1000 * 60 * 3
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = WasinDataStore(context).getData(accessKey)
                        val refreshToken = WasinDataStore(context).getData(refreshKey)
                        if (accessToken.isNotEmpty()) {
                            BearerTokens(
                                accessToken = accessToken.parseToken(),
                                refreshToken = refreshToken.parseToken()
                            )
                        } else null
                    }
                    refreshTokens {
                        val token = getRefreshToken(context)
                        token
                    }
                }
            }
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
            defaultRequest{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTP
                    host = HttpRoutes.BASE_HOST.path
                    port = 8080
                    path(HttpRoutes.BASE_V1_PATH.path)
                }
            }
        }
    }

    private suspend fun RefreshTokensParams.getRefreshToken(context: Context): BearerTokens? {
        val refreshToken = WasinDataStore(context).getData(refreshKey)
        val apiResult = client
            .post(HttpRoutes.REISSUE.getPath1()) {
                setBody(ReissueRequest(refreshToken))
                markAsRefreshTokenRequest()
            }.body<ApiUtils.ApiResult<ReissueResponse>>()

        return if (!apiResult.success) null
            else getBearerTokens(apiResult, context)
    }

    private fun getBearerTokens(
        apiResult: ApiUtils.ApiResult<ReissueResponse>,
        context: Context
    ): BearerTokens {
        val newAccessToken = apiResult.response?.accessToken ?: ""
        val newRefreshToken = apiResult.response?.refreshToken ?: ""

        WasinDataStore(context).setData(accessKey, newAccessToken)
        WasinDataStore(context).setData(refreshKey, newRefreshToken)

        return BearerTokens(
            accessToken = newAccessToken.parseToken(),
            refreshToken = newRefreshToken.parseToken()
        )
    }

    private fun String.parseToken(): String {
        if (this.startsWith("Bearer ")) {
            return this.substring(7)
        }
        return this
    }

    @Provides
    @Singleton
    fun provideUserApi(client: HttpClient): UserRepository {
        return UserApi(client)
    }
    @Provides
    @Singleton
    fun provideUserRepositoryImpl(userApi: UserApi): UserRepositoryImpl {
        return UserRepositoryImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideCompanyApi(client: HttpClient): CompanyRepository {
        return CompanyApi(client)
    }
    @Provides
    @Singleton
    fun provideCompanyRepositoryImpl(companyApi: CompanyApi): CompanyRepositoryImpl {
        return CompanyRepositoryImpl(companyApi)
    }

    @Provides
    @Singleton
    fun provideBackOfficeApi(client: HttpClient): BackOfficeRepository {
        return BackOfficeApi(client)
    }
    @Provides
    @Singleton
    fun provideBackOfficeRepositoryImpl(backOfficeApi: BackOfficeApi): BackOfficeRepositoryImpl {
        return BackOfficeRepositoryImpl(backOfficeApi)
    }

    @Provides
    @Singleton
    fun provideRouterApi(client: HttpClient): RouterRepository {
        return RouterApi(client)
    }
    @Provides
    @Singleton
    fun provideRouterRepositoryImpl(routerApi: RouterApi): RouterRepositoryImpl {
        return RouterRepositoryImpl(routerApi)
    }

    @Provides
    @Singleton
    fun provideProfileApi(client: HttpClient): ProfileRepository {
        return ProfileApi(client)
    }
    @Provides
    @Singleton
    fun provideProfileRepositoryImpl(profileApi: ProfileApi): ProfileRepositoryImpl {
        return ProfileRepositoryImpl(profileApi)
    }

    @Provides
    @Singleton
    fun provideHandOffApi(client: HttpClient): HandOffRepository {
        return HandOffApi(client)
    }
    @Provides
    @Singleton
    fun provideHandOffRepositoryImpl(handOffApi: HandOffApi): HandOffRepositoryImpl {
        return HandOffRepositoryImpl(handOffApi)
    }

    @Provides
    @Singleton
    fun provideMonitoringApi(client: HttpClient): MonitoringRepository {
        return MonitoringApi(client)
    }
    @Provides
    @Singleton
    fun provideMonitoringRepositoryImpl(monitoringApi: MonitoringApi): MonitoringRepositoryImpl {
        return MonitoringRepositoryImpl(monitoringApi)
    }

    @Provides
    @Singleton
    fun provideWifiDatabase(app: Application): WifiDatabase {
        return Room.databaseBuilder(
            app,
            WifiDatabase::class.java,
            WifiDatabase.DATABASE_NAME
        ).build()
    }
    @Provides
    @Singleton
    fun provideWIfiRepositoryImpl(db: WifiDatabase): WifiRepository {
        return WifiRepositoryImpl(db.wifiDao)
    }

}
