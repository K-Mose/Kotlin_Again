package doIt.chapter11.section1

import kotlin.concurrent.thread

/*
동시성 프로그래밍
프로그래밍에서 순서대로 작업하는 동기 프로그래밍보다는 점점 비동기 프로그래밍이 늘고있다.
비동기 프로그래밍은 RxJava, Reactive 같은 서드파티(third-party)라이브러리에서 제공하고 있다.

코틀린에서는 코루틴을 서드파티가 아닌 기본으로 제공하고 있다.
하나의 개별 작업을 루틴으로 부르는데, 코루틴은 여러 개의 루틴이 협력 한다 해서 만들어진 합성어다.
순차적으로 루틴을 실행하는 동기 코드는 복잡도가 낮지만 코드의 여러 구간에서 요청 작업이 마무리 될 때까지
멈춰있는 현상이 나타난다. 코루틴을 사용하면 넌블로킹 코드를 동기 코드처럼 쉽게 작성하고 비동기 처럼 작동시킨다.

블로킹과 넌블로킹
블로킹 - 운영체제 스케쥴링에 따라 우선 순위를 정해 작업을 하나씩 진행함
넌블로킹 - 다른 루틴을 수행 하다가 완료 후 콜백 루틴 등을 호출해 완료된 이후의 작업을 처리함

프로세스와 스레드
 * 문맥 교환 혹은 컨텍스트-스위칭(Context-Switching)은 하나의 프로세스나 스레드가 CPU를 사용하고 있는 상태에서 다른 프로세스나 스레드가
   CPU를 사용하도록 하기 위해, 이전의 프로세스의 상태(문맥)를 보관하고 새로운 프로세스의 상태를 적재하는 과정을 말한다.
   프로세스는 코드, 데이터, 열린 파일의 식별자, 동적 할당 영역, 스택 등을 가지고 있는데, 이러한 것들을 문맥(Context)라고 한다.
   프로세스는 서로 완전히 독립되었기 때문에 프로세스 간의 실행을 전환하려면 이러한 문맥을 저장해 두었다가
   새로운 프로세스의 문맥을 불러들이는 과정을 거쳐야 한다.
   스레드는 프로세스의 코드, 데이터, 열린 파일 등을 공유하는 작은 작업 단위. 스레드 간 문맥 전환은 프로세스 간 전환보다 훨씬 빠르고 비용이 낮음

코루틴은 스레드 개념을 만들지 않고도 좀 더 쉽게 비동기 프로그래밍을 할 수 있다.
코루틴은 문맥 교환이 없고 최적화된 비동기 함수를 통해 비선점형으로 작동하는 특징이 있어 협력형(Cooperative Multitasking)을 구현할 수 있게 한다.

스레드 생성하기 - runThread()
Thread 클래스나 Runnable 인터페이스를 상속해서 구현 가능하다.
SimpleThread 클래스에서는 Thread 클래스를 직접 상속 받았고 이 경우에는 다중상속을 할 수 없어서 Thread 클래스 이외에는 사용 할 수 없다.
SimpleRunnable 클래스는 Runnable 인터페이스를 구현한 것이므로 다른 클래스도 상속 가능하다.
스레드에서 실행할 코드는 run() 메서드를 오버라이딩 해서 구현한다.
이것을 실행하려면 해당 클래스 객체에서 start() 메서드를 호출하면 각 스레드의 run() 본문을 수행하는 독립된 루틴이 작동하게 된다.
익명 객체를 만든다면 클래스의 객체 없이 바로 사용 가능하다.
Thread 안에 람다식으로 Runnable 객체를 전달 가능하다.

사용자 함수를 통한 스레드 생성하기 - useCustomThread()
start나 기타 매개변수로 상태 제어가 쉽도록 다음과 같이 추가 람다식을 직접 만들어도 된다.
각종 옵션 변수를 손쉽게 설정 가능하고 옵션을 비우면 기본 값이 사용되기 때문에 문제가 없다.
보일러플레이트한 코드를 숨길 수 있으므로 thread() 함수만으로 깔끔한 코딩이 가능하다.
우선순위는 운영체제마다 다르지만 JVM에서는 10단계의 우선순위 레벨을 가질 수 있다.
데몬 여부를 결정하는 isDaemon은 백그라운드 서비스를 제공하기 위한 스레드를 생성한다.
 * 보일러 플레이트 Boilerplate 반복되어 자주 쓰지만 매번 작성하기 번거롭고 읽기 어려운 많은 양의 코드

스레드풀 사용하기
애플리케이션의 비즈니스 로직을 설계할 때는 스레드가 자주 재사용된다.
따라서 몇개의 스레드를 먼저 만들어 놓고 필요에 따라 재사용하도록 설계할 수 있다.
보통 이런 경우에는 newFixedThreadPool()로 스레드를 인자의 수 만큼 만들고 작업을 수행할 때 여기서 재사용 가능한 스레드를 고르게 한다.
예를 들어 8개의 스레드로 특정 백그라운드 서버시를 하도록 만든다고 했을 때 다음과 같이 코드를 작성 할 수 있다.
val myService: ExecutorService = Executors.newFixedThreadFool(8)
var i = 0
while(i < items.size) { // 아주 큰 데이터를 처리할 때
    val item = items[i]
    myService.submit {
        processItem(item)
    }
    i+=1
}
기존 동시성 프로그래밍의 방버에 대해 알아보았다.
코루틴을 사용하면 이런 방식을 사용하지 않아도 된다.
 */
fun main(){
//    runThread()
    useCustomThread()


}
// 스레드 만들기
class SimpleThread : Thread(){
    override fun run() {
        println("Current Thread : ${Thread.currentThread()}")
    }
}
class SimpleRunnable : Runnable{
    override fun run() {
        println("Current Thread : ${Thread.currentThread()}")
    }
}
fun runThread(){
    val thread = SimpleThread()
    thread.start()
    val runnable = SimpleRunnable()
    val thread1 = Thread(runnable)
    thread1.start()
    // 익명 객체
    object : Thread() {
        override fun run() {
            println("Current Thread : ${Thread.currentThread()}")
        }
    }.start()
    // 람다식으로 Runnable 전달하는 스레드
    Thread {
        println("Current Thread(lambda) : ${Thread.currentThread()}")
    }.start()
}

// 사용자 함수를 통한 스레드 생성
public fun customThread(
    start: Boolean = true, isDaemon: Boolean = false,
    contextClassLoader:ClassLoader? = null, name: String? = null,
    priority: Int = -1, block: ()->Unit): Thread{
    val thread = object : Thread(){
        public override fun run() {
            block()
        }
    }
    if (isDaemon) // 백그라운드 실해 여부
        thread.isDaemon = true
    if (priority > 0) // 우선순위 (1: 낮음 ~ 5: 보통 ~ 10: 높음)
        thread.priority = priority
    if (name != null) // 이름
        thread.name = name
    if (contextClassLoader != null)
        thread.contextClassLoader = contextClassLoader
    if (start)
        thread.start()
    return thread
}
fun useCustomThread(){
    // 스레드의 옵션 변수를 쉽게 설정 가능
    thread (start = true){
        println("Current Thread : ${Thread.currentThread()}")
        println("Priority : ${Thread.currentThread().priority}")
        println("Name : ${Thread.currentThread().name }")
        println("Name : ${Thread.currentThread().isDaemon }")
    }
}