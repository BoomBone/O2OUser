package com.dexin.testuser.common.lbs

import android.content.Context
import android.os.Bundle
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption

/**
 * @author Ting
 * @date 2018/8/6
 * @function 定位实现类
 */

class GaodeLbsLayerImpl(context: Context) : ILbsLayer {


    private val tag = "GaodeLbsLayerImpl"
    var mLocationChangeListener: CommonLocationChangeListener? = null
    var mLocationClient: AMapLocationClient = AMapLocationClient(context)


    init {
        //创建定位对象
        val mLocationOption = getDefaultOption()
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
    }

    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = false//可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mOption.isMockEnable = false;
        return mOption
    }

    /**
     * 位置变化监听
     */
    override fun setLocationChangeListener(locationChangeListener: CommonLocationChangeListener) {
        mLocationChangeListener = locationChangeListener
    }

    /**
     * 设置附近的人监听
     */
    override fun setNearByListener() {

    }

    override fun onCreate(state: Bundle?) {
    }

    override fun onResume() {
        setUpLocation()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
    }

    override fun onPause() {
        mLocationClient.stopLocation()
    }

    override fun onDestroy() {
        mLocationClient.onDestroy()
    }

    /**
     *启动定位
     */
    private fun setUpLocation() {
        //设置监听器
        mLocationClient.setLocationListener {
            if (mLocationChangeListener != null) {
                mLocationChangeListener!!.onLocationChanged(it)
            }
        }
        mLocationClient.startLocation()

    }
}