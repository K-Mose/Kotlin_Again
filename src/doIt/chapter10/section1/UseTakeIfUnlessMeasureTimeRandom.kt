package doIt.chapter10.section1

import java.io.File
import java.io.PrintWriter
import kotlin.system.measureNanoTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/*
Use() 함수 활용하기
객체를 사용하고 나서 닫아야 할 때 use() 함수를 사용하면 객체 사용 후 자동으로 close() 함수를 자동적으로 호출 해 준다.
public inline fun <T : Closeable?, R> T.use(block: (T) -> R) : R
T의 제한된 자료형을 보면 Closeable?로 block은 닫힐 수 있는 객체를 지정해야 한다.
파일 객체의 경우 사용하고 나서 닫아야 하는 대표적인 Closeable 객체이다.

기타 함수 활용
takeIf() takeUnless()
takeIf() 함수는 람다식이 true이면 결과를 반환하고,
takeUnless() 함수는 false이면 반환한다.
public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T?
    = if (predicate(this)) this else null
predicate는 T 객체를 매개변수로 받아서 true이면 반환 아니면 null을 반환한다.
takeUnless()는 !predicate()가 사용이 되어 false일 때 반환한다.

시간 측정
kotlin.system 패키지에 있는 measureTimeMillis()와 MeasureNanoTime() 사용

난수 생성
java.util.Random은 JVM에서만 특화되었지만
코틀린에서는 멀티 플랫폼에서 사용 가능한
kotlin.random.Random 패키지를 제공한다.
eg.
import kotlin.random.Random
println(Random.nextInt(21))
 */

fun main(){
//    printWriter()
//    fileRead()
//    takeIfFunc()
//    takeIfUnless()
    measureTimes()


}

fun printWriter(){
    PrintWriter("C:\\MySQL\\text.txt").use{
        it.println("hello")
        it.println("Mayonnaise Kim")
    }
}
fun fileRead(){
    val file = File("C:\\MySQL\\text.txt")
    file.bufferedReader().use{
        println(it.readText())
    }
}

fun takeIfFunc(){
    class OBJ(val status: Boolean){fun doThis(){println("I'm $status!")}}
    val someObj = OBJ(true)
    someObj?.takeIf { it.status }?.apply { doThis() }
}
fun takeIfUnless(){
    val input = "kotlin"
    val keyword = "in"
    input.indexOf(keyword).takeIf { it >= 0 }.apply { println("If 있음") } ?: println("키워드 없음")
    input.indexOf(keyword).takeUnless { it < 0 }.apply { "Unless 있음" } ?: println("키워드 없음")
}

@OptIn(ExperimentalTime::class)
fun measureTimes(){
    fun loop(){
        var a = 0
        for(i in 0..100)
            a++
        println(a)
    }
    val executionTime = measureTime {
        loop()
    }
    val executionNanoTime = measureNanoTime {
        loop()
    }
    println("Execution Time = $executionTime")
    println("Execution Time = $executionNanoTime ms")
}