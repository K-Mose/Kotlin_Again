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
    val result = callByValue(lambda())
    println(result)
}

fun callByValue(b: Boolean):Boolean{
    println("callByValue function")
    return b
}

val lambda:()->Boolean = {
    println("lambda function")
    true
}