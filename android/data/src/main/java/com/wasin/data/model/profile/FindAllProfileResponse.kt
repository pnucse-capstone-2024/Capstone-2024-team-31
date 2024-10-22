package com.wasin.data.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class FindAllProfileResponse(
    val isAuto: Boolean = true,
    val activeProfileId: Long = -1,
    val profiles: List<ProfileEachDTO> = emptyList()
) {
    @Serializable
    data class ProfileEachDTO(
        val profileId: Long,
        val title: String,
        val description: String,
        val tip: String
    )
}
