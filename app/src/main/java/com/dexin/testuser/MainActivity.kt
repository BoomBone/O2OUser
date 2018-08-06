package com.dexin.testuser

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.services.nearby.NearbyInfo
import com.amap.api.services.nearby.NearbySearchResult
import com.dexin.testuser.common.lbs.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var latitude = 0.0
    var longitude = 0.0
    private val WRITE_COARSE_LOCATION_REQUEST_CODE = 0x11
    private lateinit var mLbsLayer: ILbsLayer
    private lateinit var mNearByLayer: INearByListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        initTestMap(savedInstanceState)
        initListener()
    }

    private fun initTestMap(savedInstanceState: Bundle?) {
        /**
         * 定位
         */
        mLbsLayer = GaoDeLocationLayerImpl(this)
        mLbsLayer.onCreate(savedInstanceState)
        mLbsLayer.setLocationChangeListener(object : CommonLocationChangeListener {

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
                }
            }

        })

        /**
         * 检索附近的人
         */
        mNearByLayer = GaoDeNearByLayerImp(this)
        mNearByLayer.onCreate(savedInstanceState)
        mNearByLayer.setNearByListener(object : CommonNearByListener {
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
                    } else {
                        mUserSize.text = "周边搜索结果为空"
                    }
                } else {
                    mUserSize.text = "周边搜索出现异常，异常码为：$resultCode"
                }
            }

        })


    }

    private fun initListener() {
        button2.setOnClickListener {
            mNearByLayer.searchNear(latitude, longitude)
        }
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun initRecyclerView(nearbyInfoList: MutableList<NearbyInfo>) {
        mNeatRv.layoutManager = LinearLayoutManager(this)
        mNeatRv.adapter = NearAdapter(R.layout.position_item, nearbyInfoList)
    }

    /*----------------------------生命周期------------------------------------------*/
    override fun onResume() {
        super.onResume()
        mLbsLayer.onResume()
    }

    override fun onPause() {
        super.onPause()
        mLbsLayer.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mLbsLayer.onSaveInstanceState(outState)
    }
}