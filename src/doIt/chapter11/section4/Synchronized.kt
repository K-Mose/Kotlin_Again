package doIt.chapter11.section4

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/*
공유 데이터 문제
병행 프로그래밍에서는 공유 자원에 접근할 때 값의 무결성을 보장할 수 있는 방법이 필요하다.
자바에서는 sycronized 키워드를 사용해서 매서드나 특정 코드를 동기화하고 보호한다.
코틀린에서는 추가로 공유자원의 보호와 스레드 안전(Thread-safe)을 구현하기 위해 원자 변수(Atomic Variable),
스레드 가두기(THread Confinement), 상호 배제(Mutual Exclusion)등을 사용할 수 있다.
 */
/*
동기화 기법
synchronized 메서드와 블록
자바에서 synchronized는 메서드나 코드 블록에서 사용할 수 있다. 스레드 간 서로 공유하는 데이터가 있을 때 동기화해서 데이터의 안정성을 보장한다.
특정 스레드가 이미 자원을 사용하는 주이면 나머지 스레드의 접근을 막는 것이다.
코틀린에서 이것을 메서드에 사용하려면 @Synchronized 애노테이션 표기법을 사용해야 한다.
@Synchronized fun synchronizedMethod(){
    println("inside : ${Thread.currentThread()}")
}
@Synchronized 애노테이션 표기법은 내부적으로 자바의 synchronized를 사용하는 것과 같다. 특정 코드 블록에 적용 가능하다.

자바의 volatile
자바의 volatile도 같은 방법으로 사용할 수 있다. 보통 변수는 성능 떄문에 데이터를 캐시 사이에 넣고 작업하는데
이때 ㅈ여러 스레드로부터 값을 읽거나 쓰면 데이터가 일치하지 않고 꺠진다. 이것을 방지하기 위해 데이터를 캐시에 넣지 않도록
volatile키워드와 함께 변수를 선한할 수 있다. 또 volatile키워드를 사용하면 코드가 최적화 되면서 순서가 바뀌는 경우도 방지할 수 있다.
쉽게 말해 volatile을 사용하면 항상 프로그래머가 의도한 순서대로 읽기 및 쓰기를 수행한다.
그런데 두 스레드에서 공유 변서에 대한 읽기와 쓰기 연산이 있으면 volatile키워드만으로는 충분하지 않다.
이 경우에는 synchronized를 통해 변수의 읽기 및 쓰기 연산의 원자성(Atomicity)를 보장해 줘야 한다.
단, 한 스레드에서 volatile 변수의 값을 읅고 쓰고, 다른 스레드에서는 오직 valatile 변수의 값을 읽기만 할 경우, 읽는 스레드에서는
volatile 변수가 가정 최근에 쓰여졌다는 것을 보장한다.
usingVolatile() -
일정 시간이 지난 후 stop() 함수에 의해 running의 상태를 변경하고 start() 함수의 while 조건이 false가 되면서 프로그램이 중단된다.
하지만 @Volatile은 값 쓰기에 대해서는 보장하지 않는다. 여전히 원자성 보장이 필요하다.

원자 변수 - atomicVariable()
원자 변수(Atomic Variable)란 특정 변수의 증가나 감소, 더하거나 빼기가 단일 기계어 명령으로 수행되는 것을 말하며
해당 연산이 수행되는 도중에는 누구도 방해하지 못하기 때문에 값의 무결성을 보장할 수 있게 된다.
단일 기계어 명령이란 CPU가 명령을 처리할 때의 초소의 단위이다. 이 최소 단위는 누구도 방해할 수 없다.
기존 var counter = 0 으로 사용한다면, 순차적 프로그래밍에서는 문제가 없으나 많은 수의 독립적인 루틴이 이 코드에 접근해
counter를 공유하면 언제든 코드가 중단될 수 있다. 중단 시점은 CPU의 최소 단위인 명령어가 실행될 때 결정된다.
코드상에서 counter++ 이라는 한 줄의 코드이지만 이것이 컴파일되서 CPU가 실행할 명령어로 변환되면 어러 개의 명령어로 분할되므로
프로그래머가 예상하지 못한 결과를 초래할 수 있다. 즉, counter의 증가를 시작했지만 CPU의 최소 명령어가 마무리 되지 않은 시점에 루틴이 중단되어서
다른 루틴이 counter를 건드릴 수 있다.
코드상에서는 한 줄로 작성된 코드에 진입하는 것처럼 보이지만 내부적으로는 값이 증가되지 못하고 다른 루틴이 실행되어 버린다.
이때 다른 루틴이 해당 변수를 조잘할 수 있기 때문에 값의 무결성은 보장할 수 없게되고 실행 결과가 매번 달라진다.
원자 변수를 사용하면 counter의 증가 연산 부분을 CPU의 기계어 명령 하나로 컴파일 하게되고, 무결성을 보장한다.
 >> 원자 변수로 변경 AtomicInteger()
counter 변수는 원자 변수의 정수형으로 선언되었다. 그래서 증가할 때나 값을 가져올 때는 incrementAndGet()이나 get()과 같은 전용 메서드를 사용한다.
전용 메서드는 코드 단일 기계 명령으로 변하므로 값의 무결성을 보장하고 예상할 수 있는 값으로 실행된다.

스레드 가두기 - threadConfinement()
또 다른 방버으로 특정 문맥에서 작동하도록 단일 스레드에 가두는(Thread Confinement) 방법이 있다.
보통 UI애플리케이션에서 UI상태는 단일 이벤트에 따라 작동해야 한다.
이때 단일 스레드 문맥인 newSingleThreadContext를 사용할 수 있다.
스레드에 가두는 방법은 실행시간이 많이 걸린다.
massiveRun에 스레드 가두기를 실행하면 빨라진다.
massiveRun(counterContext){...}

상호 배제 - mutualExclusion()
상호 배제는 코드가 임계 구역에 있는 경우 절대로 동싱성이 일어나지 않게 하고 하나의 루틴만 접근하는 것을 보장한다.
임계 구역 또는 공유 변수영역은 병령 컴퓨팅에서 둘 이상의 스레드가 동시에 접근해서는 안 되는 배타적 공유 자원의 영역으로 정의할 수 있다.
임계 구역은 잘못된 변경이 일어나지 않도록 보호해야 하는 코드가 있는 구역이므로 임계 영역의 처리가 필요한 경우
임계 구역에 들어간 루틴은 다른 루틴으 못들어오게 잠가야 한다.
상호 배제의 특징은 소유자(Owner) 개념이 있는데, 일단 잠근 루틴만이 잠금을 해제할 수 있다는 뜻이다.
다른 루틴은 잠금을 해제할 수 없다.
자바에서 비슷한 개념으로 보통 synchronized 키워드를 사용해 코드를 보호했다.
코루틴에서는 Mutex의 lock과 unlock을 사용해 임계 구역을 만들 수 있다.
람다식 withLock을 사용한다면 mutex.lock(); try - finally{mutext.unlock()}와 같은 패턴을 손쉽게 사용할 수 있다.

이 밖에도 Mutext에는 검사를 위한 프로퍼티 isLocked가 있다. isLocked는 mutext가 잠금상태일 때 true를 반환한다.
onLock은 잠금 상태로 들어갈 때 select 표현식을 사용해 특정 지연 함수를 선택할 수 있다.
 */

fun main(){
//    usingVolatile()
//    atomicVariable()
//    threadConfinement()
    mutualExclusion()

}

// 상호 배제
val mutex = Mutex()
var mutexCounter = 0
fun mutualExclusion() = runBlocking{
    massiveRun {
        mutex.withLock {
            mutexCounter++
        }
    }
    println("Counter = $mutexCounter")
}

// 스레드 가두기
val counterContext = newSingleThreadContext("CounterContext")
var threadCounter = 0
suspend fun massiveRun(context: CoroutineContext, action: suspend () -> Unit){
    val n = 1000 // 실행할 코루틴의 수
    val k = 1000 // 각 코루틴을 반복할 수
    val time = measureTimeMillis{
        val jobs = List(n) {
            GlobalScope.launch(context) {
                repeat(k) {action()}
            }
        }
        jobs.forEach{it.join()}
    }
    println("Completed ${n * k} actions in $time ms")
}
fun threadConfinement() = runBlocking{
    massiveRun{
        withContext(counterContext){ // 단일 스레드에 가둠
            threadCounter++
        }
    }
    println("Counter = $threadCounter")
}

// 원자 변수
// var counter = 0 // 병행 처리 중 문제가 발생할 수 있는 변수
var counter = AtomicInteger(0) // 원자 변수로 초기화
suspend fun massiveRun(action: suspend () -> Unit){
    val n = 1000 // 실행할 코루틴의 수
    val k = 1000 // 각 코루틴을 반복할 수
    val time = measureTimeMillis{
        val jobs = List(n) {
            GlobalScope.launch {
                repeat(k) {action()}
            }
        }
        jobs.forEach{it.join()}
    }
    println("Completed ${n * k} actions in $time ms")
}
fun atomicVariable() = runBlocking{
    massiveRun { counter.incrementAndGet() }
    println("Counter = ${counter.get()} ")
}

// Volatile 사용하기
@Volatile private var running = false
private var count = 0
fun start(){
    running = true
    thread(start = true){
        while (running)println("${Thread.currentThread()}, count: ${count++} $running")
    }
}
fun stop() {running = false}
fun usingVolatile(){
    start()
    start()
    Thread.sleep(5)
    stop()
}