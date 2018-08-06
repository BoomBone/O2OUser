package com.dexin.testuser.common.lbs

import android.os.Bundle
import com.amap.api.services.nearby.NearbySearchResult

/**
 * @author Ting
 * @date 2018/8/6
 */
interface INearByListener {

    /**
     * 设置附近的人监听
     */
    fun setNearByListener(commonNearByListener: CommonNearByListener)

    /**
     * 搜索附近的人
     */
    fun searchNear(latitude: Double, longitude: Double)

    /**
     * 生命周期函数
     */
    fun onCreate(state: Bundle?)

    fun onResume()
    fun onSaveInstanceState(outState: Bundle?)
    fun onPause()
    fun onDestroy()

}

interface CommonNearByListener {
    fun onUserInfoCleared(p0: Int)
    fun onNearbyInfoUploaded(p0: Int)
    fun onNearbyInfoSearched(nearbySearchResult: NearbySearchResult?, resultCode: Int)
}