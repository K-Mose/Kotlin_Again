package doIt.chapter11.section2

/*
코루틴과 시퀀스
코루틴의 표준 라이브러리 중에서 sequence()를 사용하면 아주 많은 값을 만들어 내는 코드로부터
특정 값의 ㅂ점위를 가져올 수 있다. sequence() 함수는 Sequence<T>를 반환하는데 Sequence() 함수 내부에서
지연함수를 사용할 수 있고 코루틴과 함께 최종 형태를 나중에 결정할 수 있는 늦은(lazy) 시퀀스를 만들 수도 있다.
늦은 시퀀스란 특정 요소가 완전히 구성되기 전에 사용 범위와 시점을 결정할 수 있다는 뜻.
예를 들어 무제한 스크롤링을 구현하는 UI에 적용할 목록을 가져올 때 이용할 수 있다.
Sequence()의 내부
public fun <T> sequence(@BuilderInference block: suspend SequenceScope<T>.() -> Unit): Sequence<T> = Sequence { iterator(block) }
람다식을 넘겨받는 block을 보면 suspend로 정의되어 있음을 알 수 있다.
그리고 SequenceScope를 통해 확장 함수를 실행한다.
피보나치 함수를 만드느 예제 - fibonacciSeq
  1번의 sequence 블록에서 지연 함수인 yield() 함수를 호출하면서 코루틴을 생성한다.
  2번의 while 루프는 매 단계를 무한하게 순회할 때 코루틴에서 다음 수를 계산하도록 실행된다.
  3번의 take().toList()에 의해 무한한 피보나치 수열 중 8개를 List로 변환해 화면상에 출력한다.
여기서 핵심은 yield() 함수의 작동 방식이다.
> 각 표현식을 계속 진행하기 전에 실행을 잠시 멈취고 요소를 반환한다.
이것은 값을 산출(yielding)한다고 이야기 할 수 있다.
그리고 멈춘 시점에서 다시 실행을 재개한다.
1번과 2번은 작업이 일시 중단되었다가 다시 재개되는 부분이다.
3번에서 이렇게 가져온 데이터는 사실 일회성이기 때문에 어딘가에 저장되어 있지 않고 단 한 번 사용될 뿐이다.
다음과 같이 수정 해보자 - seq
여기서 yieldAll()을 사용해 반복적으로 멈추게 되면서 특정 범위의 값을 산출할 수 있다.
또한 yieldAll()을 사용해 무한한 시퀀스를 만들어 내는 generateSequence() 함수를 사용해서도 요소 값을 산출할 수 있다.
모든 요소는 일회성이기 떄문에 각 요소에 대한 다음 요소를 직접 지정하려면 iterator()를 통해
next() 메서드를 사용해야 한다.
val saved = fibonacciSeq.iterator()
...
println("${saved.next()}, ${saved.next()}, ${saved.next()}")


 */
fun main(){
    println(fibonacciSeq.take(8).toList()) // 3
    println(seq.take(7 ).toList())
    println("${saved.next()}, ${saved.next()}, ${saved.next()}")
}
// sequence
val fibonacciSeq = sequence {
    var a = 0
    var b = 1
    yield(1) // 1. 지연 함수가 사용됨

    while(true){
        yield(a + b) // 2
        val tmp = a + b
        a = b
        b = tmp
    }
}
// yieldAll
val seq = sequence<Int> {
    val start = 0
    yield(start)
    yieldAll(1..5 step 2)
    yieldAll(generateSequence(8){it*3})
}
// iterator
val saved = fibonacciSeq.iterator()