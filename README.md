## Alarm_CloneCoding
설정된 시간에 알람을 울리는 앱

## 사용한 요소
- Background 작업
  - Immediate
    - Thread
    - Handler
    - Coroutines
  - Exact
    - WorkManager
  - Deferred
    - AlarmManager
- Broadcast receiver

## Background 작업
### Immediate
[개발자 가이드](https://developer.android.com/guide/background?hl=ko#recommended-immediate)

[자바](https://developer.android.com/guide/background/threading?hl=ko)

[코틀린](https://developer.android.com/kotlin/coroutines?hl=ko)

### Exact
[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager?hl=ko)

### Deferred
[AlarmManager](https://developer.android.com/training/scheduling/alarms?hl=ko)

## AlarmManager

## Broadcast receiver
