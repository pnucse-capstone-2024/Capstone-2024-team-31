package com.wasin.domain.utils

import com.wasin.data.model.router.ImageSize
import com.wasin.data.model.router.RouterPosition

object ImagePositionMapper {

    fun positionLocalToServer(
        localPosition: RouterPosition,
        serverImageSize: ImageSize,
        localImageWidth: Int
    ): RouterPosition? {
        val localImageHeight = getLocalHeight(serverImageSize, localImageWidth)
        if (localImageHeight == null || localImageHeight == 0 || localImageWidth == 0) {
            return null
        }
        val x = (localPosition.x * serverImageSize.width).div(localImageWidth)
        val y = (localPosition.y * serverImageSize.height).div(localImageHeight)
        return RouterPosition(x, y)
    }

    fun positionServerToLocal(
        serverPosition: RouterPosition,
        serverImageSize: ImageSize,
        localImageWidth: Int
    ): RouterPosition? {
        val localImageHeight = getLocalHeight(serverImageSize, localImageWidth)
        if (localImageHeight == null || serverImageSize.width == 0 || serverImageSize.height == 0) {
            return null
        }
        val x = (serverPosition.x * localImageWidth).div(serverImageSize.width)
        val y = (serverPosition.y * localImageHeight).div(serverImageSize.height)
        return RouterPosition(x, y)
    }

    private fun getLocalHeight(
        serverImageSize: ImageSize,
        localWidth: Int,
    ): Int? {
        if (serverImageSize.width == 0) {
            return null
        }
        return (serverImageSize.height * localWidth).div(serverImageSize.width)
    }
}
