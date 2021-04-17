package doIt.chapter03.section3

// 순수 함수를 지향하는 함수형 프로그래밍
/*
순수 함수의 조건
1. 같은 인자에 대하여 항상 같은 값을 반환한다
2. 함수 외부의 어떤 상태도 바꾸지 않는다.

람다식 - 람다 대수(Lambda Calculation)에서 유래한 것
eg. {x,y -> x+y} // x,y를 대입해 합을 구하는 함수

일급 객체 - First Class Citizen
특징  1. 일급 객체는 함수의 인자로 전달할 수 있다.
     2. 일급 객체는 함수의 반환값에 사용할 수 있다.
     3. 일급 객체는 변수에 담을 수 있다.
     -> 일반적으로 적용 가능한 연산을 모두 지원하는 객체를 가리킴
     함수가 일급 객체면 일급 함수라 부르고, 일급 함수에 이름이 없는 경우 '람다식 함수' 혹은 '람다함수' 라고 부름

고차 함수 - High-order Function
다른 함수를 인자로 사용하거나 결과값으로 반환하는 함수.
즉, 일급 객체 혹은 일급 함수를 서로 주고받을 수 있는 함수가 고차 함수가 된다.
eg. highFunc({x, y-> x+y}, 10, 20) // 람다식 함수를 인자로 넘김
    fun highFunc(sum:(Int, Int)->Int, a:Int, b:Int):Int = sum(a,b) // sum 매개변수 함수

마무리. 함수형 프로그래밍의 정의와 특징
  * 순수 함수를 사용해야 한다
  * 람다식을 사용할 수 있다
  * 고차 함수를 사용할 수 있다.
 */

fun highOrder(getFunction:(Int, Int)->Int, a:Int, b:Int):Int{
    return getFunction(a,b)
}

fun main(){
    val greet:(Int)->Unit = {x -> print("Hello $x World\n")}
    greet(4)

    val func:(Int,Int)->Int = { a, b -> a - b }

    val result = highOrder({x,y -> x*y}, 10, 20)
    println(result)
    val result2 = highOrder(func, 10, 20)
    println(result2)
}