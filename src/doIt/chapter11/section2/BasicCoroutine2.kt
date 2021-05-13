package doIt.chapter11.section2

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

/*
코루틴의 문맥
코루틴이 실행될 때 여러 가지 문맥은 CoroutineContext에 의해 정의된다.
launch{...}와 같이 인자가 없는 경우에는 CoroutineScope에서 상위의 문맵이 상속외어 결정되고
launch(Dispatchers.Default){...}와 같이 사용되면 GlobalScope에서 실행한 문맥과 동일하게 사용된다.
GlobalScope는 메인 스레드의 생명주기가 끝나면 같이 종료된다.
내부적으로 보통 CommonPool이 지정되어 코루틴이 사용할 스레드의 공동 풀(pool)을 사용한다.
이것은 이미 초기화되어 있는 스레드 중 하나 혹은 그 이상이 선택되며 초기화하기 떄문에 스레디를 생성하는 오버헤드가 없이 빠른 기법이다.
그리고 한의 스레드에 다수의 코뤁니이 동작할 수 있다. 만일 특정 스레드 개수를 직접 지정하려면 다음과 같이 사용자 문맥을 만들어 지정할 수 있다.
val threadPool = Executor.newFixedThreadPool(4)
val MyContext = threadPool.asCoroutineDispatcher()
...
async(MyContext){...}
...

시작 시점에 대한 속성
필요한 경우 launch()나 async()에 인자를 지정해 코루틴에 필요한 속성을 줄 수 있다.
먼저 launch() 함수의 운형을 간략히 살펴 보면,
public fun kotlinx.coroutines.CoroutineScope.launch(context: kotlin.coroutines.CoroutineContext, start: kotlinx.coroutines.CoroutineStart, block: suspend kotlinx.coroutines.CoroutineScope.() -> kotlin.Unit): kotlinx.coroutines.Job { ...}
context 매개변수 이외에도 start 매개변수를 지정할 수 있는데 CoroutineStart는 다음과 같은 시작 방법을 정의할 수 있다.
 - DEFAULT : 즉시 시작
 - LAZY : 코루틴을 느리게 시작(처음에는 중단된 상태이며 start()나 await() 등으로 시작됨)
 - ATOMIC : 최적화된 방법으로 시작
 - UNDISPATCHED : 분산 처리 방법으로 시작
예를 들어 매개변수 이름을 사용해 다음과 같이 지정하면 start() 혹은 await()가 호출될 때 실제 루틴이 시작된다.
val job = async(start = CoroutineStart.LAZY) { doWork1() }
...
job.start()

runBlocking의 사용 - runRoutine()
runBlocking은 새로운 코루틴을 실행하고 완료되기 전까지 현재 스레드를 블로킹한다.
다음과 같이 runBlocking에서는 지연함수를 사용할 수 있다.
runBlocking{
    delay(2000) // 블록을 2초간 붙잡아 둠
}
전 예제에서는 main 블록, 메인 스레드가 종료되어 나가는 것을 방지하기 위해 readLine()을 사용했는데
메인 스레드 자체를 잡아두기 위해 다음과 같이 main() 함수 자체로 블로킹 모드에서 실행할 수 있다.

runBlocking()은 클래스 내의 맴버 메서드에서도 사용할 수 있다.
class MyTest{
    fun mySuspendMethod() = runBlocking<Unit> {...}
}

join() 함수의 결과 기다리기 - waitJoin()
명시적으로 코루틴의 작업이 완료되는 것을 기다리게 하려면 Job 객체의 join() 함수를 사용하면 된다.
launch는 Job 인스턴스를 반환한다. 이 경우에 main()[여기서는 jobRoutine()]함수는 job에서 지정한 작업이 완료되기 전까지 기다린다.
작업을 취소하려면 cancel() 함수를 사용할 수 있다.

* Job은 백그라운드에서 실행하는 작업을 가르킨다. 개념적으로는 간단한 생명주기를 가지고 있고 부모-자식 관계가 형성되며
  부모가 작업이 취소될 때 하위 자식의 작업이 모두 취소된다. 보통 Job() 팩토리 함수나 launch에 의해 job 객체가 생성된다.
Job의 상태             isActive    isCompleted     isCancelled
New                     false       false           false
Active                  true        false           false
Completing              true        false           false
Cancelling              false       false           ture
Cancelled(최종 상태)      false       true            true
Completed(최종 상태)      false       true            false
Job의 상태를 판별하기 위해 job에는 isActive, isCompleted, isCancelled 변수가 있다.
보통 Job이 생성되면 활성화 상태인 Active를 가진다. 하지만 Job() 팩토리 함수에 인자로 CoroutineStart.LAZY를 설정하면
아직은 Job이 활성화 되지 않고 New 상태로 만들어진다. Job을 Active 상태로 만들기 위해서는 start()나 join() 함수를 호출하면 된다.
job을 취소하려면 cancel() 함수를 사용할 수 있다. 그러면 Job은 Cancelling 상태로 즉시 바뀌고 이후 Cancelled 상태로 바뀐다.

async() 함수의 시작 시점 조절하기 - lazyRoutine()
async에서 기본 인수는 문맥을 지정할 수 있는데 문맥 이외에도 몇가지 매개변수를 더 지정 할 수 있다.
함수의 선언부
public fun <T> kotlinx.coroutines.CoroutineScope.async(context: kotlin.coroutines.CoroutineContext, start: kotlinx.coroutines.CoroutineStart, block: suspend kotlinx.coroutines.CoroutineScope.() -> T): kotlinx.coroutines.Deferred<T> { ...}
여기서 start 매개변수를 사용하면 async() 함수의 시작 시점을 조절할 수 있다. 예를 들어 CoroutineStart.LAZY를 사용하면 코루틴의 함수를 호출하거나
await() 함수를 호출하는 시점에서 async() 함수가 실행되도록 코드를 작성할 수 있다.

많은 작업의 처리 - manyRoutine()
10만 개의 코루틴을 List로 생성하고 각 루틴으로 화면에 점(.)을 찍도록 작성해 보자
이런 코드를 스레드로 바꾸면 Out-of-memory 오류가 발생한다.
하지만 코루틴으로 작업하면 내부적으로 단 몇 개의 스레드로 수많은 코루틴을 생성해 실행할 수 있기 때문에
오류가 발생하지 않는다. 또 메모리나 실행 속도 면에서 큰 장점을 가진다.
또 다른 방법으로 repeat() 함수를 사용하면 손쉽게 많은 양의 코루틴을 생성할 수 있다.

 */
private val HELLO = "HELLO!!"
private val WORLD = "WORLD??"
// 기존 : fun main() = runBlocking<Unit> { ... } // main() 함수가 코루틴 환경에서 실행
fun main(){
//    runRoutine()
//    waitJoin()
//    lazyRoutine()
    manyRoutine()


}
// 수정
fun runRoutine() = runBlocking{ // 함수가 코루틴 환경에서 실행
    launch {
        delay(1000)
        println(WORLD)
    }
    println(HELLO)
}
// join 기다리기
fun waitJoin() = runBlocking{
    val job = launch {
        delay(1000L)
        println(WORLD)
    }
    println(HELLO)
    job.join() // 명시적으로 코루틴이 완료되길 기다림. 취소할 경우 job.cancel() 함수를 사용
}

// 시작 시점 늦춰 보기
fun lazyRoutine() = runBlocking{
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY){
            doWork1()
        }
        val two = async(start = CoroutineStart.LAZY){
            doWork2()
        }
        println("AWAIT: ${one.await()+"_"+two.await()}")
    }
    println("Completed in $time ms")
}
// 많은 양의 작업 생성
fun manyRoutine() = runBlocking {
    val jobs = List(100_000){
        launch {
            delay(1000L)
            print('.')
        }
    }
    jobs.forEach { it.join() }
    println('\n')
    repeat(100_000){
        launch {
            delay(1000)
            print("#")
        }
    }
}