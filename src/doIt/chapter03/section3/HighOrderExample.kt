package doIt.chapter03.section3

import java.util.concurrent.locks.ReentrantLock

/*
고차 함수와 람다식의 사례 - 자세한 것은 더 알아보기

동기화를 위한 코드 - lock, synchronized, monitor 등과 같은 역할
임계영역 잠금 후 수행, 완료후 잠금 해제
kotlin:lock 구현 예제

네트워크 호출 구현 - Java 코드는 책 136쪽 참고
Java 에서 Interface 구현된 onSuccess(),onError() 등과 같은 Callback 함수를
인터페이스나 익명 객체 없이 람다식으로 처리
 */

var sharable = 1 // 공유 가능한 자원, 전역 변수로 여러 루틴에서 접근 가능 -> 특정 연산시 보호해 줘야됨
fun main(){
    val reLock = ReentrantLock()
    lock(reLock, { criticalFunc()})
    lock(reLock) { criticalFunc() }
    lock(reLock, ::criticalFunc)
    println(sharable)

    // 네트워크 결과 값
    val success = 1
    val error = 0
    networkCall(success,{ println("Success") }, { println("Error") })
    networkCall(error, { println("Success") }, { println("Error") })
}
fun criticalFunc(){sharable+=1} // 공유자원 접근 코드
fun <T> lock(reLock: ReentrantLock, body: ()->T):T{
    reLock.lock()
    try{
        return body()
    } finally {
        reLock.unlock()
    }
}

fun networkCall(result:Int ,onSuccess:()->Unit, onError:()->Unit){
    if(result==1){
        onSuccess()
    }else{
        onError()
    }
}