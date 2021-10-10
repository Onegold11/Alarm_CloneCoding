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
[개발자 가이드](https://developer.android.com/training/scheduling/alarms?hl=ko)

애플리케이션이 사용되지 않을 때 시간 기반 작업을 실행할 수 있음
앱 데이터를 호스팅하는 서버를 가지고 있다면 동기화 어댑터와 Google 클라우드 메시징을 사용하는 것이 더 좋은 방법

### 알람 유형
실제 경과 시간: 시스템 부팅 이후 시간
실시간 시계: UTC 시간

일반적으로 실제 경과 시간을 권장

## Broadcast receiver
[개발자 가이드](https://developer.android.com/guide/components/broadcasts?hl=ko)

Publish–subscribe 디자인 패턴과 유사한 브로드캐스트 메시지를 받거나 보낼 수 있음
특정 이벤트에 대한 메시지를 앱에서 처리할 수 있다.
