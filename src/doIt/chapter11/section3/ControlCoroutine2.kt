package doIt.chapter11.section3

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
채널의 동작 - channelRoutine()
채널은 자료를 서로 주고받기 위해 약속된 일종의 통로 역할을 한다.
코루틴의 채널은 넌블로킹 전송 개념으로 사용되고 있다.
채널을 구현할 떄는 SendChannel과 ReceiveChannel인터페이스를 이용해
값들의 스트림을 전송하는 방법을 제공한다.
실제 전송에는 다음과 같이 지연함수의 send()와 receive() 함수를 이용한다.
    - SendChannel의 suspend fun send(element: E)
    - ReceiveChannel의 suspend fun receive(): E
채널을 통해 send() 함수로 값을 보내 놓으면 이후 receive() 함수를 통해 값을 받을 수 있다.
일반 큐와는 다르게 더이상 전달 요소가 없으면 채널을 달을 수 있다.
보통 for문을 구성해 채널을 받고 close()를 사용하면 바로 채널을 닫는 것이 아니라 닫겠다는 특수한 토큰을 보낸다.

보내는 쪽과 받는 쪽에 몇 가지 중요한 상태가 있다. 송신자는 SendChannel에서 채널이 꽉 차있는지,
즉 isFull 값이 true인지 살펴보고 꽉 차있으면 일시 지연된다.
만일 close()에 의해 닫으면 isClosedForSend가 true로 지정되어 isFull은 false를 반환할 수 있다.
수신자는 isEmpty가 true라면 비어 있으므로 가져갈 게 없는 루틴은 일시 지연된다.
마찬가지로 닫을 경우 isClosedForReceive에 의해 false를 반환할 수 있다.
그 밖의 SendChannel과 ReceiveChannel에는 다음과 같은 메서드를 사용할 수 있다.
    - SendChannel.offer(element: E): Boolean // 가능하면 요소를 채널에 추가. 채널이 꽉 찬 경우 false를 반환
    - ReceiveChannel.poll(): E // 요소를 반환. 채널이 비어 있으면 null을 반환
 * 확장된 채널의 자료형
   개본 채널 개념 이외의 확장된 채널 자료형
    - RendezvousChannel : 내부에 버퍼를 두지 않는 채널. 모든 send 동작은 recevie가 즉각 가져가기 전까지는 일시 중단된다.
                          물론 반대로 모든 receive도 누군가 send하기 전까지 일시 중단된다.
    - ArrayChannel : 특정한 크기로 고정된 버퍼를 가진 채널. 따라서 해당 버퍼가 꽉 차기 전까진 send가 지연되지 않고 보낼 수 있다.
                     receive도 버퍼가 비어 있기 전까지 계속 받을 수 있다.
    - LinkedListChannel : 링크드 리스트 형태로 구성했기 때문에 버퍼의 크기에 제한이 없어 send 시 일시 중단인 상태를 가지지 않는다.
                          다만 send를 지속할 경우 메모리 부족 오류를 만날 수 있다. receive는 비어있는 경우 일시 중단된다.
    - ConflatedChannel : 버퍼는 하나의 요소만 허용하기 때문에 모든 send 동작은 일시 지연되지는 않는다. 다만 기존의 값을 덮어 씌운다.

 */
fun main(){
    channelRoutine()
}

fun channelRoutine() = runBlocking {
    val channel = Channel<Int>()
    launch {
        // 여기에 다량의 CPU 연산 작업이나 비동기 로직을 둘 수 있음
        for (x in 1..5) channel.send(x * x) // 채널 전송
    }
    repeat(5) {print("${channel.receive()} ")} // 채널 수신
    println("\nDone!")
    // close()로 닫기
    launch {
        for (x in 1..50) channel.send(x * x) // 채널 전송
        channel.close() // 모두 보내고 닫기 명시
    }
    for (element in channel) print("$element ")
    println("\nDone!")
}
