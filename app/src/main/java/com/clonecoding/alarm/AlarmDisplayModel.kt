package com.clonecoding.alarm

/**
 * 알람 표시 관련 클래스
 */
data class AlarmDisplayModel(
    val hour: Int,
    val minute: Int,
    var onOff: Boolean
) {

    /**
     * 시간 텍스트
     */
    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }

    /**
     * 오전/오후 텍스트
     */
    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    /**
     * 알람 on/off 텍스트
     */
    val onOffText: String
        get() {
            return if (onOff) "알람 끄기" else "알람 켜기"
        }

    /**
     * DB에 저장하기 위한 데이터 형식
     */
    fun makeDataForDB(): String {

        return "$hour:$minute"
    }
}
