package doIt.chapter03.section3
/*
코틀린의 다양한 함수

익명 함수 - Anonymous Function
일반 함수이지만 이름이 없는 함수. fun(x:Int):Int = x*x
익명함수를 람다함수형태를 받는 변수에 넣을 수 있다.
val add:(Int, Int)->Int = fun(x:Int, y:Int) = x + y
val add = {x:Int, y:Int -> x + y }
익명 함수를 사용하는 이유 : 람다함수에서는 return이나 break, continue처럼 제어문을 사용하기 어렵기 떄문에
                        함수 본문 조건식에 따라 함수를 중단하고 반환해야하는 경우에 익명함수를 사용
                        람다식 사용 가능한 경우에는 람다식 사용 추천
                        람다식에선 return@라벨이름 처럼 라벨 표기법을 사용해서 함

인라인 함수 - Inline Function
함수가 호출되는 곳에서 함수 본문의 내용을 모두 복사해 넣어 분기없이 처리시킴
즉 외부에 선언된 함수의 내용을 호출부에 코드를 추가해서 실행시키는 것과 같음
코드가 복사되어 들어가기 떄문에 대게 내용을 짧게 작성, 람다식 매개변수를 가지고 있는 함수에서 동작

인라인 함수 제한하기
인라인 함수에서 매개변수 람다식이 너무 길거나 본문 자체가 너무 길면 컴파일러에서 경고를 할 수 있음
람다식 매개변수도 호출부 코드로 그대로 들어가기 떄문에 코드의 양이 많아질 수 있음
noinline 키워드로 선언된 람다식은 인라인 되지 않는다.

인라인 함수와 비지역 반환
익명함수 종료하기 위해 return을 반환값 없이 사용 가능함
인라인 함수 안 람다식에서도 종료를 위해 return 반환 가능
crossinline 키워드를 쓰면 비지역 반환이 되지 않음


 */

fun main(){
    val add1:(Int, Int)->Int = fun(x:Int, y:Int) = x + y
    val add2 = {x:Int, y:Int -> x + y }
    println("${10*10}")
    println(add1(10,20))
    println(add1)
    println(add2)

    shortFunc(3, {println("First call $it")})
    println()
    shortFunc(5, {println("Second call $it")})
    println()

    // 역컴파일을 보면 위 아래의 코드 바디의 out()함수가 차이가 난다.
    // 위는 out() 코드가 직접 쓰여지고, 아래는 out() 호출된다.
    noShortFunc(3){println("No Inline Function call $it")}
    println()

    // 람다식에 return 넣기
    returnLambda(100,{println("크로스 라인 람다") }){
        println("$it * $it ")
        return // 의도하지 않게 함수가 반환 됨 - 비지역 반환
    }
}

inline fun shortFunc(a:Int, out:(Int)->Unit){
    println("inline Before calling out()")
    out(a)
    println("inline After calling out()")
}

inline fun noShortFunc(a:Int, noinline out:(Int)->Unit){
    println("noinline Before calling out()")
    out(a)
    println("noinline After calling out()")
}

inline fun returnLambda(a:Int, crossinline cross:()->Unit, out: (Int) -> Unit){
    println("return Before calling out()")
    cross()
    out(a)
    println("return After calling out()") // 람다식 안에서 return 발생해 실행되지 않음
}