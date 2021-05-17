package doIt.chapter11.section4

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlin.system.measureTimeMillis

/*
actor 코루틴 빌더
코루틴의 결합으로 만든 actor는 코루틴과 채널에 통신하거나 상태를 관리한다.
다른 언어의 actor개념은 들어오고 나가는 메일 박스 기능과 비슷하지만 코틀린에서는 들어오는 메일 박스 기능만 한다고 볼 수 있다.
 > 메일 박스란 특정 연산, 상태, 메시지 등을 담아 보내는 것으로 위치에 상관없이 완전히 비동기적으로 수행되도록 디자인된 개념
actor는 한 번에 1개의 메시지만 처리하는 것을 보장한다.
이 코드에서는 특정 루프를 만들고 isClosedForReceive로 닫힌 상태가 아니라면 recevie()를 사용해 desc를 반복 출력하도록 했다.
data class Task(val desc: String)

val me = actor<Task>{
    while(!isClodeForReceive){
        println(receive().desc.repeat(5))
    }
}

프로그램은 함수로 쓰인 actor 블록을 생헝하고 실행한다. actor는 코루틴이고 순차적으로 실행되며 각 상태는 특정 actor 코루틴에 한정되므로
공유된 변경 가능한 상태에도 문제가 없다. 이 방법은 문맥 전환이 없기 때문에 lock 기법보다 유용하다.
들어오는 메시지 msg는 CounterMsg자료형이다.
만일 IncCounter가 사용되면 coutner 상태를 증가시킨다.
actor는 어떤 특정 상태를 관리하기 위한 백그라운드 태스크에 유용하다.

// 잘 모르겠으므로 다른 곳 참고하기
// https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#actors
// https://medium.com/@jagsaund/kotlin-concurrency-with-actors-34bd12531182

 > 어쨌든 자원 공유 문제를 해결함
 */
fun main(){
    actorBuilder()
}
fun actorBuilder() = runBlocking {
    val counter = counterActor()
    GlobalScope.massiveRun{
        counter.send(CounterMsg.IncCounter)
    }
    val response = CompletableDeferred<Int>()
    counter.send(CounterMsg.GetCounter(response =response))
    println("Counter = ${response.await()}")
    counter.close()
}
suspend fun GlobalScope.massiveRun(action: suspend () -> Unit){
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
sealed class CounterMsg{
    object IncCounter: CounterMsg()
    class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg()
}
// 새로운 counter actor를 위한 함수
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor의 상태로 공유되지 않음
    for(msg in channel){ // 들어오는 메시지 처리
        when (msg){
            is CounterMsg.IncCounter -> counter ++
            is CounterMsg.GetCounter -> msg.response.complete(counter)
        }
    }
}
fun SendChannel<Int>.complete(counter: Int){
    println(counter)
}


