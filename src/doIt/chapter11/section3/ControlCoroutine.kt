package doIt.chapter11.section3

import kotlinx.coroutines.*

/*
코루틴 동작 제어하기
코루틴들 사용하다 보면 특정 문맥에서 실행되면서 반복하거나 작업의 취소, 실행의 보장이나
시간 만료의 처리처럼 다양한 동작을 제어해야 한다.

코루틴의 문맥 - dispatcherRoutine()
코루틴은 항상 특정 문맥에서 실행된다. 이때 어떤 문맥에서 코루틴을 실행할지는 디스패처(Dispatcher)가 결정한다.
코루틴은 CoroutineContext으로 구현된 형식의 문맥을 가진다.
CoroutineDispatcher는 추상 클래스로 몇 가지 디스패처 객체를 정의한다.
    1. 기본 문맥 (CommonPool == GlobalScope)
    먼저 Dispatchers.Default는 기본 문맥인 CommonPool에서 실행되고 GlobalScope로도 표현된다.
    따라서 launch(Dispatcher.Default){...}와 GlobalScope.launch{...}는 같은 표현이다.
    이것은 공유된 백그라운드 스레드 CommonPool에서 코루틴을 실행하도록 한다.
    다시 말해서 스레드를 새로 생성하지 않고 기존에 있는 것을 이용한다. 따라서 연산 중심의 코드에 적합하다.

    2. I/O를 위한 문맥
    Dispatcher.IO는 입출력 위주의 동작을 하는 코드에 적합한 공유된 풀이다. 따라서 블로킹 동작이 많은 파일아나
    소켓 I/O 처리에 사용하면 좋다.

    3. Unconfined 문맥
    Dispatchers.Unconfined는 호출자 스레드에서 코루틴을 시작하지만 첫 번째 지연점까지만 실행한다. 특정 스레드나 풀에 거두지 않고,
    첫 번째 일시 중단 후 호출된 지연 함수에 의해 재개된다. 이 옵션을 사용하는 것은 권장하지 않는다.

    4. 새 스레드를 생성하는 문맥
    newSingleThreadContext는 사용자가 직접 새 스레드 풀을 만들 수 있다. 새 스레드를 만들기 떄문에 비용이 많이 들고
    더 이상 필요하지 않으면 해제하거나 종료시켜야 한다. 이 옵션은 성능상의 이유로 향후 변경될 가능성이 크다.
    코루틴 안에 또 다른 코루틴을 정의하면 자식 코루틴이 된다. 부모가 취소(cancel)되는 경우 자식 코루틴은 재귀적으로 취소된다.
    따라서 피요한 경우 join()함수를 사용해 명시적으로 처리를 기다리게 만들 수 있다.

기본 동작 제어하기
repeat() 함수를 사용한 반복 동작하기 - repeatRoutine()
지속적으로 반복하는 코드를 작서아기 위해 repeat() 함수를 이용했다.
그러면 백그라운드에서 실행하는 일종의 데몬(daemon)스레드를 구성할 수 있다.
1000회 반복하기 위해 repeat() 함수에 1000이라는 인자를 주고 있다.
하지만 GlobalScope로 생명 주기를 한정했기 때문에 메인 스레드가 종료되어 버리면 더 이상 진행되지 않는다.
여기서는 1.3초 뒤 종료되므로 약 3번 정도 진행한 뒤 종료된다.
만을 GlobalScope를 제거하면 모든 횟수를 진행할 때까지 프로그램이 종료되지 않는다.

코루틴 작업 취소하기 - cancelRoutine()
join() 함수만 사용하면, main() 함수가 job의 완료를 기다리기 때문에
repeat() 함수의 1000번 반복 실행이 모두 진행된다. cancel() 함수를 사용하면

finally의 실행 보장 - finallyRoutine()
try ~ finally 구문을 사용해 finally 블록에서 코루틴의 종료 과정을 처리하도록 할 수 있다.
일반적인 finally 블록에서 지연 함수를 사용하려고 하면 코루틴이 취소되므로 지연 함수를 사용할 수 없었다.
그 외에 파일을 닫거나 통신 채널을 닫는 등의 작업은 넌블로킹 형태로 작동하며 지연 함수를 포함하고 있지 않기 때문에 문제가 없다.
만일 finally 블록에 시간이 걸리는 작업이나 지연 함수가 사용될 경우 실행을 보장하기 위해서는 NonCancellable 문맥에서 작동하도록 해야한다.
이것을 위해 withContext(NonCancellable){...}을 사용해 다음과 같이 finally 블록을 구성할 수 있다.

실행 상태의 판단 - currentRoutine()
만일 코드를 중단하기 위해 코루틴에 조건식을 넣으려고 할 때 연산이 마무리되기 전까지는
조건식에 의해 루틴이 중단되지 않는다는 것을 기억해야 한다.
delay(1300L) 이후 작업이 취소 함수에 의해 시그널을 받아 루틴이 바로 취소될 것 같지만
while(i<5){...}루프를 사용하면 루프가 완료될 때까지 루틴이 끝나지 않는다.

코루틴의 시간 만료 - timeoutRoutine()
일정 시간 뒤에 코루틴을 취소할 수 있게 하기.
withTimeout() 함수의 내부
public suspend fun <T> withTimeout(timeMillis: kotlin.Long, block: suspend kotlin.coroutines.CoroutineScope.()->T): T {...}
시간이 만료되면 block을 취소히키고 TimeoutCancellationException 오류가 발생하는 코드이다.
예외를 발생하지 않고 null로 처리하면 withTimeoutOrNull()을 사용한다.


 */

fun main(){
//    dispatcherRoutine()
//    repeatRoutine()
//    cancelRoutine()
//    finallyRoutine()
//    currentRoutine()
    timeoutRoutine()



}

fun timeoutRoutine() = runBlocking {
    try{
        withTimeout(1300L){ // 1.3초 지나면 에러 발생시켜 종료, withTimeoutOrNull로 하면 null로 던지며 종료 
            repeat(1000){ i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
    }catch (e: TimeoutCancellationException){
        println("timed out with $e")
    }
}

fun currentRoutine() = runBlocking{
    val startTime = System.currentTimeMillis()
    val job = GlobalScope.launch {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5){ // while(isActive)로 변경하면 의도한 시간에 루프가 취소되어 중단된다.
            if(System.currentTimeMillis() >= nextPrintTime){
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting")
    job.cancelAndJoin() // cancel()로 해도 while(isActive)와 결과가 같다.
    println("main: Now I can quit.")

}

fun finallyRoutine() = runBlocking{
    val job  = launch {
        try{
            repeat(1000){ i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("Bye")
            withContext(NonCancellable){
                println("I'm running finally")
                delay(1000L)
                println("Non-Cancellable")
            }
        }
    }
    delay(1300L)
    job.cancel()
    println("main : Quit!")
}

// 취소하기
fun cancelRoutine() = runBlocking{
     val job = launch {
        repeat(20){ i ->
            println("I'm sleeping $i ...")
            delay(500L)
         }
     }
    delay(1300L)
    job.cancel()
}
// repeat()
fun repeatRoutine() = runBlocking{
    GlobalScope.launch { // launch만 사용하면 종료되지 않는다.
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)
}
// 문맥 찾기
fun dispatcherRoutine() = runBlocking {
    val jobs = arrayListOf<Job>()
    jobs += launch(Dispatchers.Unconfined){
        println("Unconfined: \t\t ${Thread.currentThread().name}")
    }

    jobs += launch(coroutineContext){
        println("coroutineContext: \t\t ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.Default){
        println("Default: \t\t ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.IO){
        println("IO: \t\t ${Thread.currentThread().name}")
    }

    jobs += launch{
        println("main runBlocking: \t\t ${Thread.currentThread().name}")
    }

    jobs += launch(newSingleThreadContext("MyThread")) {
        println("newSingleThreadContext: \t\t ${Thread.currentThread().name}")
    }

}
