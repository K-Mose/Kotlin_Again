package doIt.chapter07.section2

/*
연산자 오버로딩 - operator overloading
코틀린은 특정 연산자의 역할을 함수로 정의하고 있고 협약이라 부른다.

연산자의 작동 방식
연산자를 사용하면 관련된 멤버 메서드를 호출하는 것과 같다.
eg. a+b는 a.plus(b)와 같음
기본적으로 다양한 자료형들을 처리하기 위해 오버로딩 되었고, 사용자가 추가할 수 있다.

 연산자의 종류
 산술 연산자
    1. +: .plus
    2. -: .minus
    3. *: .times
    4. /: .div
    5. %: rem
    6. ..: .rangeTo

호출 연산자 - Invoke Operator
함수 호출을 도움.
생략 가능함.

인덱스 접근 연산자 - Indexed Access Operator
게터 세터를 다루기 위한 대괄호([]) 연산자를 제공

단일 연산자 - unary~
단일 연산자는 변수의 자료형을 정의하고 매개변수 없이 각 연선자에 대한 함수를 호출한다.
그리고 연산된 결과를 반환한다.
-> 단순히 부호 변경 연산자

범위 연선자 ..
in 연산자로 특정 객체 반복이나 범위 연산자와 함께 포함 여부를 판단한다.
!in 연산자로 반대의 경우를 나타낼 수 있다.

대입 연산자
대입 연산자는 연산의 결과를 다시 자신에게 할당한다.
+에 대응하는 plus()를 오버로딩 하면 +=에 대응하는 plusAssign()은 자동으로 구현된다.
따로 오버로딩하게 된다면 오류가 발생한다.

동등 연산자
일치나 불일치를 판별하는 ==,!=

비교 연산자
compareTo()를 호출해 반환되는 정수를 보고 비교한다.
 */

fun main(){
    val p1 = Point(3, -8)
    val p2 = Point(2,9)

    var point = Point()
    point = p1 + p2 // 객체에서 + 연산이 가능하게 되었다.
    println("${point.x}, ${point.y}")
    --point
    println("${point.x}, ${point.y}")
    point--
    println("${point.x}, ${point.y}")

    // invoke
    val sum: (Int,Int)->Int = {x,y -> x+y}
    println(sum.invoke(4,5))
    // 위 아래 같음
    println(sum(4,5)) // invoke 생략 가능

    var a = 100
    a = a.unaryMinus()
    println(a)
    a = a.unaryMinus()
    println(a)
    a = a.unaryPlus()
    println(a)
}

class Point(var x: Int = 0, var y: Int = 10){
    // + 연산자 오버로딩
    operator fun plus(p: Point): Point{
        return Point(x + p.x, y + p.y)
    }

    operator fun dec() = Point(--x, --y)
}