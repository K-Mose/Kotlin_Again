package doIt.chapter10.section2

import java.util.*

/*
파일 입출력
표준 입출력의 기본 개념
입력과 출력은 프로그래밍 세계에서 빈번히 일어난다.
앞서 사용한 print()와 println()은 내부적으로 표준 출력 라이브러리인 자바의 System.out.println()을 호출한다.
System.out은 표준 출력을 위한 라이브러리를 가르킨다.
표준 입력은 기본적인 API로 readLine() 함수가 있다.
fun readLine(): String? = readLine(System.`in`, decoder)
internal fun readLine(inputString: InputStream, decoder: CharsetDecoder): String?{ ... }

기본적으로 표준 입력인 System.in을 사용하고 in은 코틀린의 범위 연선자이기 떄문에 백틱(`)으로 감싸 지정되었다.
그런 다음 오버로디오딘 가시성 지시자 internal로 지정된 내부 함수 readLine()을 호출하고,
이것의 매개변수에 InputStream과 CharsetDecoder를 사용해 입력 처리를 구현한다.

readLine() 함수 사용해 보기
아래 예에서 input에는 표준 입력 장치인 콘솔로부터 입력받아 String형으로 할당한다.
입력에 실패할 경우 null 가능성이 생기므로 !! 혹은 ?를 사용해 NPE 발생 여부를 처리한다.
입력 받은 값은 문자열 값이 기본이므로 정수형으로 변환 하려면 readLine()!!.toInt() 형태로 호출 할 수 있다.
자바 표준 라이브러리인 Scanner를 이용할 수도 있다.

Kotlin의 입출력 API
kotlin.io 패키지는 다음과 같은 자바 라이브러리를 확장한다.
파일 처리 - java.io.File
바이트 단위의 입력 처리 - java.io.InputStream
바이트 단위의 출력 처리 - java.io.OutputStream
문자 기반 입력 처리 - java.io.Reader
문자 기반 쓰기 처리 - java.io.Writer
버퍼를 가진 읽기 처리 - java.io.BufferedReader

이러한 라이브러리는 파일이나 콘솔과 같은 스트림(Stream)에서 읽거나 쓸 수 있는 API를 제공한다.
스트림은 데이터가 강물에 띄운 것처럼 흘러간다는 의미로 데이터가 머물러있지 않고 전달되는 개념이다.
처리할 데이터의 양에 따라 간단한 데이터는 readBytes, readLines, readText 계열의 함수를 사용할 수 있다.
대량의 데이터는 copyTo, forEachBlock, forEachLine과 같은 API를 사용한다.
InputStream, Reader, Writer를 쓸 때는 호출 후 사용이 완료되면 반드시 닫아야 한다.


자바의 io, nio의 개념
자바에는 입출력을 위한 기본적인 패키지인 java.io와 기능이 대폭 확장된 java.nio 패키지가 있다.
nio(New Input Output)는 자바 7부터 강화된 라이브러리이다. 코틀린에서는 자바의 라이브러리를 그대로 사용할 수도 있다.
비동기 관련 루틴은 코틀린의 코루틴(coroutine)에서 지원하고 있으므로 비동기 관련 부분은 11장에서 살펴본다.
java.io와 java.nio의 기본적인 차이점은 버버에 있다.
            입출력         버퍼 방식    비동기 지원
java.io     스트림 방식     넌버퍼      비동기 지원 안함(블로킹 방식)
java.nio    채널 방식       버퍼       비동기 지원 함(넌블로킹 지원)

입출력의 구분으로는 발생한 데이터를 물 흐르듯 바로 전송시키는 스트림 방식과 여러 개의 수로를 사용해 병목 현상을 줄이는 채널 방식이 있다.
버퍼는 송/수신 사이에 임시적으로 사용하는 공간이 있는지에 따라 결정된다.
공간이 있는 버퍼 방식은 좀 더 유연한 처리가 가능하다. 비동기 지원 여부로 구분하면
java.io의 경우 블로킹 방식으로 비동기 동작을 지원하지 않는 대신, 단순하게 구성이 가능하고
java.nio는 넌블로킹을 지원해 입출력 동작의 멈춤없이 또 다른 작업을 할 수 있는 비동기를 지원한다.

스트림과 채널
스트림(Stream)은 데이터가 흘러가는 방향성에 따라 입력 스트림과 출력 스트림으로 구분된다.
데이터를 읽고 저장하는 양방향성을 가지는 작업을 할 때, 예를 들어 파일의 경우 FileInputStream과 FileOutputStream을 별도로 지정해야 한다.
채널 방식은 양방향으로 입력과 출력이 모두 가능하기 때문에 입출력을 별도로 지정하지 않아도 된다.
여러 개의 수로를 가진다고 비유하면 이해하기 좋다.
예로 파일을 처리하기 위해 FileChannel을 생성하면 입력과 출력을 동시에 사용할 수 있게 된다.
채널을 위한 nio 패키지는 다음과 같은 것들이 있다.
java.nio - 다양한 버퍼 클래스
java.nio.channels - 파일 채널, TCP/UDP 채널 등
java.nio.charset - 문자 세트, 인코더, 디코더 등
java.nio.file - 파일 및 파일 시스템 접근 클래스
 */
fun main(){
//    usingReadLine()
    scanner()
}

fun usingReadLine(){
    print("ENTER : ")
    val input = readLine()!!
    println("YOU ENTERED : $input")
}
fun scanner(){
    print("ENTER NUMBER : ")
    val reader = Scanner(System.`in`)
    var integer: Int = reader.nextInt()
    println("YOU ENTERED : $integer")
}