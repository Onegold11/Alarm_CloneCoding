package com.clonecoding.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import java.util.*

/**
 * Main activity
 */
class MainActivity : AppCompatActivity() {

    /**
     * 알람 on/off 버튼
     */
    private val onOffButton: Button by lazy {
        findViewById(R.id.onOffButton)
    }

    /**
     * 시간 재설정 버튼
     */
    private val changeAlarmButton: Button by lazy {
        findViewById(R.id.changeAlarmTimeButton)
    }

    /**
     * am/pm 텍스트 뷰
     */
    private val ampmTextView: TextView by lazy {
        findViewById(R.id.ampmTextView)
    }

    /**
     * 시간 텍스트 뷰
     */
    private val timeTextView: TextView by lazy {
        findViewById(R.id.timeTextView)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 알람 켜기 버튼 초기화
        this.initOnOffButton()
        
        // 시간 재설정 버튼 초기화
        this.initChangeAlarmTimeButton()

        // 알람 설정 시간 렌더링
        val model = this.fetchDataFromSharedPreferences()
        this.renderView(model)

        // 이전 알람 취소
        this.cancelAlarm()
    }

    /**
     * on/off 버튼 초기화
     */
    private fun initOnOffButton() {

        this.onOffButton.setOnClickListener {

            // 버튼의 tag 에 있는 model 로 시간 표시
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = this.saveAlarmModel(
                model.hour,
                model.minute,
                model.onOff.not()
            )
            this.renderView(newModel)

            // 알람 on/off 에 따른 처리
            if (newModel.onOff)
                this.turnOnAlarm(newModel)
            else
                this.cancelAlarm()
        }
    }

    /**
     * 알람 on
     *
     * @param 알람 정보 모델
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun turnOnAlarm(model: AlarmDisplayModel) {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, model.hour)
            set(Calendar.MINUTE, model.minute)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        // 알람 인텐트 설정
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java  )
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 알람 매니저 설정
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    /**
     * 알람 시간 변경 버튼 초기화
     */
    private fun initChangeAlarmTimeButton() {

        this.changeAlarmButton.setOnClickListener {

            val calendar = Calendar.getInstance()

            TimePickerDialog(
                this,
                { picker, hour, minute ->

                    // 선택한 시간을 저장
                    val model = this.saveAlarmModel(hour, minute, false)
                    
                    // 저장한 시간을 표시
                    this.renderView(model)
                    
                    // 이전 알람이 있는 경우 알람 삭제
                    this.cancelAlarm()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    /**
     * 알람 데이터 모델 저장
     */
    private fun saveAlarmModel(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {

        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ON_OFF_KEY, model.onOff)
        }

        return model
    }

    /**
     * 알람 데이터 모델 불러오기
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {

        val sharedPreferences = getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ON_OFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if ((pendingIntent == null) and alarmModel.onOff) {
            
            // 알람은 꺼져있는데, 데이터는 켜져있는 경우
            alarmModel.onOff = false
        } else if((pendingIntent != null) and alarmModel.onOff.not()) {
            
            // 알람은 켜져있는데, 데이터는 꺼져있는 경우
            pendingIntent.cancel()
        }

        return alarmModel
    }

    /**
     * 알람 관련 뷰 렌더
     */
    private fun renderView(model: AlarmDisplayModel) {

        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }

        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }

        this.onOffButton.apply {
            text = model.onOffText
            tag = model
        }
    }

    /**
     * 알람 취소
     */
    private fun cancelAlarm() {

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    companion object {

        private const val SHARED_PREFERENCES_NAME = "time"

        private const val ALARM_KEY = "alarm"

        private const val ON_OFF_KEY = "onOff"

        private const val ALARM_REQUEST_CODE = 1000
    }
}