package com.qf.gamelogsanalyze

import java.util.Calendar

import org.apache.commons.lang3.time.FastDateFormat

object TimeUtils {

 private val fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  private val calender: Calendar = Calendar.getInstance()

  // apply注入方法, 一般作为初始化方法, 此时是把String类型的时间转化为long类型的时间
  def apply(time: String): Long = {
    calender.setTime(fdf.parse(time))
    calender.getTimeInMillis // 得到Long类型的时间
  }

  // 通过Calendar改变日期
  def updateCalendar(amount: Int): Long = {
    calender.add(Calendar.DATE, amount)
    val time = calender.getTimeInMillis
    calender.add(Calendar.DATE, -amount)
    time
  }


}
