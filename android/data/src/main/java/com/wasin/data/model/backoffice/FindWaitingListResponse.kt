package com.wasin.data.model.backoffice

import kotlinx.serialization.Serializable

@Serializable
data class FindWaitingListResponse (
    val waitingList: List<WaitingItem>
) {
    @Serializable
    data class WaitingItem (
        val userId: Long,
        val name: String
    )
}
