package doIt.chapter11.section3

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import java.util.*


/*
Produce 생산자 소비자 패턴 - productRoutine()
produce는 채널이 붙어 있는 코루틴으로 생산자 측면의 코드를 쉽게 구성할 수 있다.
채널에 값을 보내면 생산자로 볼 수 있고 소비자는 consumeEach 함수를 확장해 for문 대신 해서 지정된 요소를 소비한다.
produce<E>는 값을 생산하고 ReceiveChannel<E>를 반환한다.
그런 다음 result에서 ReceiveChannel의 확장 함수인 consumeEach를 사용하여 각 요소를 처리한다

버퍼를 가진 채널 - bufferedRoutine()
채널에는 기본 버퍼가 없으므로 send() 함수가 호출되면 receive() 함수가 호출되기 전까지 send() 함수는 일시 지연된다.
반대의 경우도 receive() 함수가 호출되면 send() 함수가 호출되기 전까지 receive() 함수는 지연된다.
하지만 채널에 버퍼 크기를 주면 지연 없이 여러 개의 요소를 보낼 수 있게 된다.
Channel() 생성자에는 capacity 매개변수가 있어 이것이 버퍼 크기를 정한다.

select 표현식 - selectRoutine()
다양한 채널에서 무언가 응답해야 한다면 각 채널의 실행 시간에 따라 결과가 달라질 수 있는데,
이때 select를 사용하면 표현식을 통해 결과를 받을 수 있다.


*/

fun main(){
    productRoutine()
    bufferedRoutine()
    selectRoutine()
}

fun selectRoutine() = runBlocking{
    val routine1 = GlobalScope.produce {
        delay(Random().nextInt(1000).toLong())
        send("A")
    }

    val routine2 = GlobalScope.produce {
        delay(Random().nextInt(1000).toLong())
        send("B")
    }

    val result = select<String>{ // 먼저 수행되는 것을 받는다.
        routine1.onReceive { result -> result}
        routine2.onReceive { result -> result}
    }
    println("result is $result")

}

// 버퍼 크기
fun bufferedRoutine() = runBlocking {
    val channel = Channel<Int>(3) // 버퍼의 capacity = 3
    val sender = launch(coroutineContext){ // 송신자 측
        repeat(10){
            println("Sending $it")
            channel.send(it)
        }
    }
    delay(1000L) // 아무것도 받지 않고 1초를 기다린 후
    sender.cancel() // 송신자의 작업을 취소
}

// produce
fun productRoutine() = runBlocking {
    val result = producer() // 값의 생산
    result.consumeEach { print("$it ") } // 소비자 루틴 구성
    println()
}
// 생산자를 위한 함수 생성 // 코루틴은 리시버로 CoroutineScope 인터페이스를 받기 때문에 여기에 확장함수를 만든다.
fun CoroutineScope.producer(): ReceiveChannel<Int> = produce{
    var total: Int = 0
    for (x in 1..5){
        total += x
        send(total)
    }
}

