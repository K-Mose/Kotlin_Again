package doIt.chapter10.section3

import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
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

넌버퍼와 버퍼 방식
스트림 방식에서 1바이트를 쓰면 입력 스트림이 1바이트를 읽는다. 버퍼를 사용해 다수의 데이터를 익는 것보다 상당히 느리게 작동한다.
io 방식에서는 버퍼와 병합해 사용하는 BufferedInputStream과 BufferedOutputStream을 제공한다.
nio에서는 기본적으로 버퍼를 사용하는 입출력을 하기 떄문에 데이터를 일일이 읽는 것보다 더 나은 성능을 보여준다.

블로킹과 넌블로킹
프로그램에서 만일 쓰려고 하는데 쓸 공간이 없으면, 공간이 비워질 때까지 기다리게 된다.
읽으려고 할 때도 마찬가지이다.
따라서 공간이 비워지거나 채워지기 전까지는 쓰고 읽을 수 없기 때문에 호출한 코드에서 계속 멈춰 있는 것을 블로킹이라고 한다.
하지만 메인 코드의 흐름을 방해하지 않도록 입출력 작업 시 스레드나 비동기 루틴에 맡겨 별개의 흐름으로 작업하게 되는 것을 넌블로킹이라고 한다.
블로킹은 스스로 작업을 빠져나올 수 없고, 넌블로킹은 스스로 작업을 빠져놔어 다른 작업이 진행되도록 할 수 있다. 하지만 코드가 복잡해진다.

파일에 쓰기
Files 클래스와 그 연관된 Paths, StandardOpenOption
Files 클래스는 java.nio.file에 속해있으며 파일 조각을 위한 각종 static 메서드로 구성되어 있다.
* https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html 참고
filesWrite()에서 Files 클래스의 write()를 사용해 경로에 지정된 파일을 생성하고 내용을 쓴다.
경로는 Paths 클래스를 사용하고 있고 문자열 text를 toByteArray()로 변환하여 지정한다.
파일을 생성할 떄의 옵션으로 StandardOpenOption을 사용하는데 주요 옵션은 다음과 같다.
READ - 파일을 읽기용으로 연다.
WRITE - 파일을 쓰기용으로 연다.
APPEND - 파일이 존재하면 마지막에 추가한다.
CREATE - 파일이 없으면 새 파일을 생성한다.
파일 경로는 문자열로 지정할 수도 있지만 URI 객체로 특정 콘텐츠의 자료형에 대한 위치도 허용한다.
그리고 파일을 처리할 떄 발생할 수 있는 예외를 처리하도록 try-catch문으로 사용해 예외를 처리하고,
지정된 경로에 파이링 생성된다. 코틀린에서는 한글 텍스트가 UTF-8로 저장된다.

File의 PrintWriter 사용하기
java.io 패키지의 PrintWriter와 BufferedWriter
PrintWriter의 경우 기본적은 printWriter() 외에도 print(), println(), printf(), write() 처럼 파일에 출력하는 메서드를 제공하고 있어
기존에 콘솔에 출력하듯이 바이트 단위로 파일에 쓸 수 있다.
BufferedWriter는 버퍼를 사용해 데이터 메모리 영역에 두었다가 파일에 쓰는 좀 더 효율적인 파일 쓰기를 지원한다.
아래 printWriter() 함수

File의 BufferedWriter 이용하기
BufferedWriter는 버퍼를 사용한다는 차이점만 빼면 PrintWriter와 같다.
bufferedWriter()를 사용하면 먼저 내용을 메모리에 특정 공간에 저장한 뒤 파일로 다시 쓰여진다.
기존의 printWriter()는 다음과 같이 바꿀 수 있다.
File(path).bufferedWriter().use{ it.write(outString) }

File의 writeText() 사용하기
코틀린에서 확장해 감싼(wrapped) 메서드로 제공하는 writeText()
감싼 메서드란 기존의 존재하는 메서드를 또 다른 메서드로 감싼 후 기능을 더 추가해 편리하게 사용할 수 있게 한 메서드이다.
val file = File(path)
file.writeText(outString)
file.appendText("\nDo great work!")
writeText()를 사용하고 appendText()에서 문자열을 파일에 추가할 수 있다.
이후에 닫기(close) 처리가 없다.
writeText()가 writeBytes()를 감싸고 있고 writeBytes()에서는 FileOutputStream을 use를 이용하여 write()를 사용하고 있다.
내용이 null인 경우 printWriter()는 null을 파일에 쓸 수 있지만, bufferedWriter()는 NPE를 발생시킬 수 있다.
writeText()를 사용하는 경우 자료형 불일치 오류가 발생할 수 있으므로 주의해야 한다.

FileWriter 사용하기
try-catch-finally 구문을 통해 예외처리를 완료한 후 close()를 호출하고 있다.
use로 변경한다면 try-catch-finally로 close()를 처리하고 있고 이것은 인라인 함수로 설계되어있음을 알 수 있다.
FileWriter(path, true).use { it.write(outString) }

파일에서 읽기
File의 FileReader
텍스트 파일은 FileReader로부터 선언된 read의 readText() 멤버 메서드를 통해 읽어 오고 있다.
readText()는 내부적으로  StringWriter()를 호출해 텍스트를 메모리로 가져온 후 그 내용을 반환한다.

자바의 파일 읽기를 코틀린으로 변경하기
자바에서는 BufferedReader의 인자로 전달된 InputStreamReader를 통해 datafile.json 파일을
utf-8 인코딩 방식으로 열고 readLine()을 통해 읽어 들인다는 것이 핵심이다.
모든 작업이 완료되면 finally에서 close()를 호출해 안전하게 파일을 닫는다.

copyTo() 사용하기
copyTo()는 파일에 대한 복사 작업을 처리하고 있다.
public fun File.copyTo(target: File, overwrite: Boolean = false, bufferSize: Int = DEFAULT_BUFFER_SIZE): File
copyTo()는 목적지인 target에 파일을 버퍼 크기만큼 한 번에 복사한다.
이 때 기존의 파일이 존재하면 덮어쓸지 결정하기 위해 overwrite 매개변수를 통해 결정 가능하다.
bufferSize 매개변수는 버퍼 크기를 설정한다. 함수 선언부에서 보듯 overwirter나 bufferSize는 기본 값이 설정되어 생략 가능하다.
만일 복사사려는 파일이 없으면 FileNotFoundException이 발생한다.
또한 복사 대상은 파일이어야만 한다.

 */
fun main(){
//    usingReadLine()
//    scanner()
//    filesWrite()
//    printWriter()
//    fileWriter()
//    fileReader()
//    fileReadMethod()
    anythingElse()
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

fun filesWrite(){
    val path = "C:\\바탕화면\\test\\hello.txt"
    val text = "안녕히계세요!! 전 이 세상의 모든 굴레와 속박을 벗어던지고 제 행복을 찾아 떠납니다!"
    try{
        Files.write(Paths.get(path), text.toByteArray(), StandardOpenOption.CREATE)
    }catch (e: Exception){
        e.printStackTrace()
    }

}

fun printWriter(){
    val outString = "안녕히계세요!! \r\n전 이 세상의 모든 굴레와 속박을 벗어던지고 \r\n제 행복을 찾아 떠납니다!"
    val path = "C:\\바탕화면\\test\\testFile.txt"

//    val file = File(path)
//    val printWriter = PrintWriter(file)
//
//    printWriter.println(outString)
//    printWriter.close()
    // use로 줄이기
    File(path).printWriter().use { it.println(outString) }
}

fun fileWriter(){
    val outString = "안녕히계세요!! \r\n전 이 세상의 모든 굴레와 속박을 벗어던지고 \r\n제 행복을 찾아 떠납니다!"
    val path = "C:\\바탕화면\\test\\testFileWriter.txt"

    val writer = FileWriter(path, true) // 인자: 경로, append 여부
    try{
        writer.write(outString)
    }catch (e: Exception){

    }finally {
        writer.close()
    }
}

fun fileReader(){
    val path = "C:\\바탕화면\\test\\testFileWriter.txt"
    try {
        val read = FileReader(path)
        println(read.readText())
    }catch (e: Exception){
        println(e.message)
    }
}

fun fileReadMethod(){
    val path = "C:\\바탕화면\\test\\testFileWriter.txt"

    // 단순 변환
    /*val file = File(path)
    val inputStream = file.inputStream()
    val inputStreamReader = InputStreamReader(inputStream)
    val sb = StringBuilder()
    var line: String?
    val br = BufferedReader(inputStreamReader)
    try{
        line = br.readLine()
        while(line!=null){
            sb.append(line, '\n')
            line = br.readLine()
        }
        println(sb)
    }catch (e: Exception){

    }finally {
        br.close()
    }*/

    val file = File(path)
    val inputStream: InputStream = file.inputStream()
    val text = inputStream.bufferedReader().use { it.readText() }
    println(text)

    // file 생략
    var bufferedReader = File(path).bufferedReader()
    println(bufferedReader.use { it.readText() })

    // useLine()으로 줄 단위로 처리
    bufferedReader = File(path).bufferedReader()
    val lineList = mutableListOf<String>()
    bufferedReader.useLines{ line -> line.forEach { lineList.add(it) } }
    lineList.forEach { println(" >   $it") }
}

fun anythingElse(){
    val path = "C:\\바탕화면\\test\\testFileWriter.txt"
    // 파일 내용 출력
    File(path).forEachLine { println(it) }

    // 바이트 단위로 읽기 (쓰기는 writeByte())
    val bytes = File(path).readBytes()
    println(Arrays.toString(bytes))

    // 줄 단위로 읽기
    val lines = File(path).readLines()
    lines.forEach { println(it) }

    // 텍스트 단위로 읽기 (쓰기는 writeText())
    val text = File(path).readText()
    println(text)
}