package com.dexin.testuser.common.lbs

import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.services.nearby.NearbySearchResult

/**
 * @author Ting
 * @date 2018/8/6
 * @function 定义地图服务通用抽象接口
 */
interface ILbsLayer {

    /**
     * 设置位置变化监听
     */
    fun setLocationChangeListener(locationChangeListener: CommonLocationChangeListener)


    /**
     * 生命周期函数
     */
    fun onCreate(state: Bundle?)

    fun onResume()
    fun onSaveInstanceState(outState: Bundle?)
    fun onPause()
    fun onDestroy()
}

interface CommonLocationChangeListener {
    fun onLocationChanged(location: AMapLocation)
}

