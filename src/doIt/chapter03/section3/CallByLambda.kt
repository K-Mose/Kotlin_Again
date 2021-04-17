package doIt.chapter03.section3

// 람다식과 고차 함수 호출하기
/*
값의 의한 호출이 일반적, 포인터 주소 연산이 없어 참조에 의한 호출은 자바나 코틀린에서 사용하지 않음.

값에 의한 호출 - Call by Value
함수가 또다른 함수의 인자로 전달되었을 떄,
인자로 전달된 람다식이 바로 실행되어 결과값이 인자로 넘어감


이름에 의한 호출 - Call by Name

 */

fun main(){
    val result = callByValue(lambda("callByValue"))
    println(result)

    println()
    val result2 = callByName(lambda)
    println(result2)
}

fun callByValue(b: Boolean):Boolean{ // 인자 값에 일반 변수로 선언되어 있음
    println("callByValue function")
    return b
}

fun callByName(b: (String)->Boolean):Boolean{
    b("callByName_1") // 어디서든지 호출 가능하다.
    println("callByName function")
    // 호출 전까지는
    return b("callByName_2") // 어느곳에서든 받은 람다식을 호출시킬 수 있음.
}

val lambda:(String)->Boolean = { str ->
    println("lambda function : $str")
    true
}