package com.dexin.testuser

import com.amap.api.services.nearby.NearbyInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dexin.testuser.common.util.TimeUtils
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Ting
 * @date 2018/8/1
 */
class NearAdapter(val resLayout: Int, val list: MutableList<NearbyInfo>)
    : BaseQuickAdapter<NearbyInfo, BaseViewHolder>(resLayout, list) {
    override fun convert(helper: BaseViewHolder?, item: NearbyInfo?) {
        if (helper != null) {
            if (item != null) {
                // 格式：年－月－日 小时：分钟：秒

                helper.setText(R.id.mUserId, "用户ID=${item.userID}")
                helper.setText(R.id.mDistanceTv, "周围距离=${item.distance}")
                helper.setText(R.id.mDriverDistanceTv, "周围驾驶=${item.drivingDistance}")
                helper.setText(R.id.mTimeTv, "周围用户时间=${TimeUtils.secondToString(item.timeStamp)}")
                helper.setText(R.id.mPointTv, "周围用户点=${item.point}")
            }
        }
    }
}