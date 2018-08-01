package com.dexin.testuser

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.nearby.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AMapLocationListener, NearbySearch.NearbyListener {

    var latitude = 0.0
    var longitude = 0.0
    private val WRITE_COARSE_LOCATION_REQUEST_CODE = 0x11
    private lateinit var mNearbySearch: NearbySearch

    //声明AMapLocationClient类对象
    private lateinit var mLocationClient: AMapLocationClient
    private lateinit var mLocationOption: AMapLocationClientOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNearbySearch = NearbySearch.getInstance(this)
        requestPermission()

        initLocation()
        initListener()
    }

    private fun initListener() {
        mNearbySearch.addNearbyListener(this);
        button.setOnClickListener {
            if (mInputNumEt.text.toString().trim().isNotEmpty()) {
                Log.e("main", mInputNumEt.text.toString().trim())
                mNearbySearch.stopUploadNearbyInfoAuto()
                initAmapNear(mInputNumEt.text.toString().trim())
            } else {
                Toast.makeText(this, "请输入用户id", Toast.LENGTH_SHORT).show()
                Log.e("main", "用户输入为空")
            }
        }
        button2.setOnClickListener {
            searchNear()
        }
    }

    private fun initAmapNear(id: String) {
        //附近派单功能初始化的核心代码如下：
        Log.e("main", "开始上传位置信息")
        mNearbySearch.startUploadNearbyInfoAuto({
            val loadInfo = UploadInfo()
            loadInfo.coordType = NearbySearch.AMAP
            //位置信息
            if (latitude != 0.0 && longitude != 0.0) {
                loadInfo.point = LatLonPoint(latitude, longitude)
            } else {
                Toast.makeText(this, "经纬度获取错误", Toast.LENGTH_SHORT).show()
            }
            //用户id信息
            loadInfo.userID = id
            loadInfo
        }, 10000)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    WRITE_COARSE_LOCATION_REQUEST_CODE)
        }
    }

    private fun initLocation() {
        //初始化client
        mLocationClient = AMapLocationClient(applicationContext)

        mLocationOption = AMapLocationClientOption()

        mLocationOption = getDefaultOption()
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)

        mLocationClient.startLocation();
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


    override fun onLocationChanged(location: AMapLocation) {
        val code = location.errorCode
        Log.e("main", "定位返回错误码=$code")
        if (code == 0) {
            //显示定位城市
            val cityName = location.city
            //获取地区编码
            val cityCode = location.adCode
            //获取纬度
            latitude = location.latitude
            //获取精度
            longitude = location.longitude
            //存储定位城市
            Log.e("main", "经度=$latitude，纬度=$longitude")

//            mLocationClient.stopLocation();
        }
    }


    private fun searchNear() {
        //设置搜索条件
        val query = NearbySearch.NearbyQuery()
        //设置搜索的中心点
        query.centerPoint = LatLonPoint(latitude, longitude)
        //设置搜索的坐标体系
        query.coordType = NearbySearch.AMAP
        //设置搜索半径
        query.radius = 10000
        //设置查询的时间
        query.timeRange = 10000
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH)
        //调用异步查询接口
        mNearbySearch.searchNearbyInfoAsyn(query)
    }

    /*-------------------------NearByListener-------------------------------------------*/
    override fun onUserInfoCleared(p0: Int) {

    }

    override fun onNearbyInfoUploaded(p0: Int) {
    }

    override fun onNearbyInfoSearched(nearbySearchResult: NearbySearchResult?, resultCode: Int) {
        Log.e("main", "request返回=$resultCode")
        if (resultCode == 1000) {
            Log.e("main", "request返回")
            if (nearbySearchResult?.nearbyInfoList != null
                    && nearbySearchResult.nearbyInfoList.size > 0) {
                initRecyclerView(nearbySearchResult.nearbyInfoList)
                mUserSize.text = "附近共有${nearbySearchResult.nearbyInfoList.size}个用户"
                for (nearbyInfo in nearbySearchResult.nearbyInfoList) {
                    val a = "周边搜索结果为size=${nearbySearchResult.nearbyInfoList.size},userId=${nearbyInfo.userID}," +
                            "距离=${nearbyInfo.distance},驾驶距离=${nearbyInfo.drivingDistance},时间=${nearbyInfo.timeStamp},点=${nearbyInfo.point}"
                    Log.e("main2", a)
                }
            } else {
                val a = "周边搜索结果为空";
                Log.e("main2", a)
            }
        } else {
            val a = "周边搜索出现异常，异常码为：$resultCode"
            Log.e("main2", a)
        }
    }

    private fun initRecyclerView(nearbyInfoList: MutableList<NearbyInfo>) {
        mNeatRv.layoutManager = LinearLayoutManager(this)
        mNeatRv.adapter = NearAdapter(R.layout.position_item, nearbyInfoList)
    }


    /*-------------------------NearByListener-------------------------------------------*/


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}