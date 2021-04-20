package doIt.chapter03.section3

import java.math.BigInteger

/*
코틀린의 다양한 함수2

확장 함수 - Extension function
클래스와 같은 확장 대상에 함수를 더 추가하는 기능
fun 확장 대상.함수 이름(매개변수, ):반환값{}
Any 클래스에 확장을 추가하면 모든 클래스에 확장이 적용된다.

중위 함수 - Infix Notation
클래스 멤버를 호출할 때 사용하는 점(.)을 생략하고 함수 이름 뒤에 소괄호를 붙이지 않고 실행
직관적이며, 일종의 연산자를 구현할 수 있는 함수. 특히 비트연산자에서 사용
조건
    1. 멤버 메서드 또는 확장 함수여야 한다.
    2. 하나의 매개변수를 가져야 한다.
    3. infix 키워드를 사용하여 정의한다.

꼬리 재귀 함수 - Tail Recursion
재귀 함수는 자기 자신을 계속 호출, 다시 돌아옴
재귀 함수 조건
    1. 무한 호출에 빠지지 않도록 탈출 조건을 만든다.
    2. 스택 영역을 이용하므로 호출 횟수를 무리하게 많이 지정해 연산하지 않는다.
    3. 코드를 복잡하게 만들지 않는다.
꼬리 재귀는 함수 호출을 스택에 계속 쌓이는 방식이 아닌 꼬리를 무는 형태로 반복함, 다시 돌아오지 않음
계산을 먼저 하고 재귀를 호출함. 재귀 함수를 계산을 먼저 실행하게 수정함
 */

fun main(){
    val source = "Hello World"
    val target = "Kotlin!"
    val doubleTarget = "Wow"
    println(source.getLongString(target))
    println(doubleTarget.getLongString(target))

    // 중위 함수 사용, 직관적임.?
    println("그럼 ${100 multiply 100 multiply 2 }")
    println(2 plus 3 multiply 4 plus 5 multiply 6) // 앞에서 뒤로 순서대로 계산됨
    println()
    //
    println(factorial(5))
    println()
    println(fibonacci(1000, BigInteger("0"), BigInteger("1")))

}

fun String.getLongString(target:String):String = if(this.length > target.length) this else target

// Int의 확장함수로 multiply 정의
infix fun Int.multiply(x:Int):Int{
    return this*x
}
infix fun Int.plus(x: Int):Int{
    return this + x
}
// 기존  fun factorial(n:Int):Long{return if(n==1)n.toLong() else n * factorial(n-1)}
tailrec fun factorial(n:Int, run:Int = 1):Long{
    println(n)
    return if (n == 1) run.toLong() else factorial(n-1, run*n)
}

// 피보나치를 인자에서 계산하게 한 후 호출하는 방법으로 구현
tailrec fun fibonacci(n:Int, a:BigInteger, b:BigInteger):BigInteger{
    return if(n==0) a else fibonacci(n-1, b, a+b)
}