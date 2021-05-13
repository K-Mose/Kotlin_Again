package doIt.chapter11.section2

import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/*
코루틴의 개념과 사용 방법

스레드와 같은 기법으로 구성하는 넌블로킹 코드는 성능이 뛰어나지만 코드가 복잡하므로 안전하지 못한 코드를 만들 가능성이 높아진다.
코틀린은 코루틴을 통해 코드의 복잡성을 줄이고 손쉽게 일시 중단하거나 다시 시작하는 루틴을 만들어 낼 수 있다.

코루틴의 기본 개념
프로세스나 스레드는 해당 작업을 중단(stopped)하고 다른 루틴을 실행하기 위한 문맥 교환을 시도할 때 많은 비용이 든다.
코루틴은 비용이 많이 드는 문맥 교환 없이 해당 루틴을 일시 중단(suspended)해서 이러한 비용을 줄인다.
다르게 표현하면 운영체제가 스케쥴링에 개입하는 과정이 필요하지 않다는 뜻이다.
또한 일시 중단은 사용자가 제어할 수 있다.

코루틴 라이브러리 추가하기
코틀린 1.1 버전 이후에 사용 가능하고 1.2까지는 실험적 성격의 라이브러리였다.
따라서 코틀린 1.2 버전에는 0.3x 버전의 코루틴 라이브러리를 사용해야 한다.
코틀린 1.3 이후 버전에서는 코루틴 1.0 버전이 정식 라이브러리가 된다.
따라서 1.3 이후는 1.0.x 이후 버전의 코루틴을 추가해야 한다.
 * 추가 방법은 책 참조 / Kotlin version 확인은 아래 Build 탭에서 확인 가능 예) Kotlin: kotlinc-jvm 1.4.32-release-371 (JRE 1.8.0_282-b08)
File - Project Structure - Global Library - Kotlinx-coroutines - org.jetbrains.kotlinx-coroutines 에서 알맞는 버전 설치 (현재는 1.5RC가 최신)

코틀린 주요 패키지
common 패키지의 주요 기능
launch / async - 코루틴 빌ㄷ
Job / Deferred - cancellation 지원
Dispatchers - Default는 백그라운드 코루틴을 위한것이고, Main은 Android나 Swing, JavaFx를 위해 사용
delay / yield - 상위 레벨 지연(suspending)함수
Channel / Mutex - 통신과 동기화를 위한 기능
coroutineScope / supervisorScope - 범위 빌더
select - 표현식 지원

core 패키지의 주요 기능
CommonPool - 코루틴 문맥
produce / actor - 코루틴 빌더

launch와 async
코루틴 패키지를 사용한 기본적인 예제 - egCoroutine()
실행 결과 "Hello?"는 메인 스레드에 의해 바로 출력된다.
"World!"는 코루틴의 부분으로 메인 스레드와 분리되어 백그라운드에서 1초 뒤 실행된다.
따라서 메인 스레드의 코드보다 지연되어 실행된다. 또한 메인 스레드와 별도로 실행되므로 넌브로킹 코드이기도 하다.

코루틴에서 사용되는 suspend()로 선언된 지연 함수여야 코루틴 기능을 사용할 수 있다.
suspend로 표현함으로서 이 함수는 실행이 일시 중단(suspended)될 수 있으며 필요한 경우에 다시 재게(resume)할 수 있게 된다.
Intellij IDEA에는 지연 함수인 delay()가 사용된 곳에 아이콘을 확인 할 수 있다.
delay()의 선언부 - public suspend fun delay(timeMillis: kotlin.Long): kotlin.Unit { /* compiled code */ }
delay()는 suspend와 함께 선언된 함수로 코루틴 블록에서 사용될 수 있다.
suspend 키워드는 사용자 함수에서도 사용할 수 있다. suspend 함수는 사용자가 실행을 일시중단 할 수 있음을 의미하고
코루틴 블록 안에서 사용할 수 있다. 만일 suspend 함수를 코루틴 블록 외에 사용한다면 다음과 같은 오류를 만나게 된다.
 * Error() Kotlin: Suspend function 'delay' should be call only from a coroutine or another suspend function
사용자 함수를 추가하기 위해서 예제에 다음과 같은 suspend 함수를 직접 선언하고 코루틴에 사용해 보자.
컴파일러는 suspend가 붙은 함수를 자동적으로 추출해 Continuation 클래스로부터 분리된 루틴을 만든다.
이러한 지연 함수는 코루틴 빌더인 launch와 async에서 사용 가능하지만 메인에서는 사용 불가능하다.
지연 함수는 또 다른 지연 함수 내에서 사용하거나 코루틴 블록에서만 사용해야 한다.
*/
/*
launch 코루틴 빌더 생성하기 - jobRoutine()
이렇게 launch를 통해 코루틴 블록을 만들어 내는 것은 코루틴 빌더의 생성이라고 한다.
launch에는 현재 스레드를 차단하지 않고 새로운 코루틴을 생설할 수 있게 하며 특정 결괏값 없이 Job 객체를 반환한다.
Job 객체를 받아 코루틴의 상태를 출력해 보는 예제를 작성해 보자.
launch를 살펴보면 실행 범위를 결정하는 GlobalScope가 지정되어 있다.
이것은 코루틴의 생명 주기가 프로그램의 생명 주기에 의존되므로 main()이 종료되면 같이 종료된다.
코루틴을 실행하기 위해 내부적으로 스레드를 통해 실행된다.
단 실행 루틴이 많지 않은 경우에는 내부적으로 하나의 스레드에서 여러 개의 코루틴이 실행할 수 있기 때문에
1개의 스레드면 충분하다.

지연 함수 2개 만들고 delay() 사용하기 - measure(){workInSerial()}
suspend 키워드를 붙여 함수 두개를 선언 한 뒤 다른 지연시간을 줌
launch에 정의된 doWork1()과 doWork2() 함수는 순차적으로 표현할 수 있다.
2개의 함수는 내부적으로 비동기 코드로서 동시에 작동할 수 있지만 코드만 봤을 때는 순차적으로
실행되는 것 처럼 표현함으로서 프로그래밍의 복잡도를 낮추게 된다.

Async 코루틴 빌더 생성하기
이번에는 async를 사용하여 코루틴을 실행한다.
launch와 다른점은 Deferred<T>를 통해 결괏값을 반환한다는 것이다.
이때 지연된 결괏값을 받기 위해 await()를 사용하여 현재 스레드의 블로킹 없이 먼저 종료되면
결과가 반환되면 문자를 합쳐 할당한다.

 * 이런 기법은 안드로이드 UI 스레드에서 블로킹 가능성이 있는 코드를 사용하면 에플리케이션이 중단되거나 멈추는 경우가 발생하는데,
   이 경우 await()를 사용하면 UI를 제외한 루틴만 블로킹 되므로 UI가 멈추는 경우를 해결 할 수 있다.
 */

fun main(){
//    egCoroutine()
//    jobRoutine()
//    worksInSerial()
    // 두 함수는 doWork1()의 동기, 비동기식 실행만큼 시간 차이가 난다
    measure()
    worksInParallel()

}
// 기본 예제1
fun egCoroutine(){
    // 코루틴 코드
    GlobalScope.launch{ // 새로운 코루틴을 백그라운드에서 실행
        doSomething()
        delay(1000L) // 1초의 넌블로킹 지연(시간 단위 ms)
        println("World!") // 지연 후 출력
    }
    // 메인 스레드의 코루틴이 지연되는 동안 계속 실행
    println("Hello?")
    Thread.sleep(1500L) // 메인 스레드가 JVM에서 바로 종료되지 않게 1.5초 딜레이

}
// suspend 함수
suspend fun doSomething(){
    println("Do Something")
}

// Job 객체 반환
fun jobRoutine(){
    val job = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello, ")
    println("job.isActive: ${job.isActive}, completed: ${job.isCompleted}")
    Thread.sleep(2000L)
    println("job.isActive: ${job.isActive}, completed: ${job.isCompleted}")
}

// suspend 함수 2개
suspend fun doWork1(): String{
    delay(1000)
    return "Work1"
}
suspend fun doWork2(): String{
    delay(3000)
    return "Work2"
}
@OptIn(ExperimentalTime::class)
private fun worksInSerial(){
    GlobalScope.launch {
        println("지연 시작")
        val timeMeasure = measureTime {
            val one = doWork1()
            val two = doWork2()
            println("Kotlin One : $one")
            println("Kotlin Two : $two")
        }
        println("launch Time :: $timeMeasure")
    }
}
@OptIn(ExperimentalTime::class)
fun measure(){
    val executionTime = measureTime {
        worksInSerial()
    }
    println(executionTime)
//    readLine() // 메인 종료 방지
    Thread.sleep(6000)
}
// async 코루틴추가
@OptIn(ExperimentalTime::class)
private fun worksInParallel(){
    // Deferred<T>를 통해 반환
    val one: Deferred<String>
    val two: Deferred<String>
    val timeMeasure = measureTime {
        one = GlobalScope.async {
            doWork1()
        }
        two = GlobalScope.async {
            doWork2()
        }
    }
    println("time measure : $timeMeasure")
    GlobalScope.launch {
        println("launch time : "+measureTime {
            val combined = one.await() + "_" + two.await()
            println("Kotlin Combined : $combined")
        })
    }
    Thread.sleep(5000)

}