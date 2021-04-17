package doIt.chapter03.section3

// 람다식과 고차 함수 호출하기
/*
값의 의한 호출이 일반적, 포인터 주소 연산이 없어 참조에 의한 호출은 자바나 코틀린에서 사용하지 않음.

값에 의한 호출 - Call by Value
함수가 또다른 함수의 인자로 전달되었을 떄,
인자로 전달된 람다식이 바로 실행되어 결과값이 인자로 넘어감


이름에 의한 호출 - Call by Name
람다식을 매개변수로 받는 함수에서 람다식을 받으면
람다식이 바로 호출이 되지 않고 원하는 순간 호출할 수 있다.
 */

fun main(){
    val result = callByValue(lambda("callByValue"))
    println(result)
    println()

    val result2 = callByName(lambda)
    println(result2)
    println()

//    funcParam(10,29, sum()) // Type miss match
// colon 2개를 이용해서 소괄호 인자를 생략하고 받을 수 있다.
    val result3 = funcParam(10,20, ::sum) // Intent에서 Activity::java.class와 같은 것인가 알아보기
    println(result3)

    val likeLambda = ::sum // 일반 변수에 값처럼 할당 가능
    println(likeLambda(1,2))
    println()

    hello(::text)
}

fun callByValue(b: Boolean):Boolean{ // 인자 값에 일반 변수로 선언되어 있음
    println("callByValue function")
    return b
}

fun callByName(b: (String)->Boolean):Boolean{
    println(b("callByName_1")) // 어디서든지 호출 가능하다.
    println("callByName function")
    return b("callByName_2") // 어느곳에서든 받은 람다식을 호출시킬 수 있음.
}

val lambda:(String)->Boolean = { str ->
    println("lambda function : $str")
    true
}

/*
다른 함수의 참조에 의한 일반 함수 호출
람다식이 아닌 일반 함수를 함수의 인자로 호출 할 떄
 */
fun sum(x:Int, y:Int):Int = x+y // 일반 함수
fun funcParam(a:Int, b:Int, c:(Int, Int)->Int):Int{ // 람다식을 인자로 받는 함수
    return c(a,b)
}
fun text(a:String, b:String) = "Hi! $a $b"
fun hello(body:(String, String)->String):Unit = print(body("Hello!","World!"))