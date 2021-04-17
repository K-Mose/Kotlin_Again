package doIt.chapter03.section3

/*
람다식의 매개변수 - 람다식의 매개변수는 마지막 람다식을 표현식처럼 빼낼 수 있다.

매개변수가 없는 경우 - {표현식} 형태의 인자로 할당 할 수 있다.

매개변수가 1개인 경우 - 매개 변수 이름을 정하고 넘길 수 있고 it으로 생략 가능하다.

매개변수가 2개인 경우 - 매개 변수를 it으로 생략할 순 없고 _로 생략 가능하다.

일반 함수에 람다식 매개 변수가 2개 이상인 경우 - 소괄호를 생략할 수 없고 마지막 람다식은 표현식으로 빼낼 수 있다.
 */

fun main(){

    // 매개 변수 없음
    val lamP:()->String = {"\"Basic\""} // 기본 형
    noParam (lamP)
    noParam ({"소괄호"}) // Lambda argument should be moved out of parentheses 라고 소괄호를 제거하길 추천해준다.
    noParam {"표현식으로만"}
    // 매개 변수 1개
    oneParam { a -> "Hello $a World" }
    oneParam { "Hello $it World" } // 1개인 경우 매개 변수 이름을 생략하고 it으로 전달 가능하다.
    // 매개 변수 2개 이상
    manyParam{a, b-> "Hello $a $b World!"} // 2개 이상인 경우에는 it으로 생략 불가
    manyParam { _, _ -> "Hello World" }
    println()
    // 매개 변수로 람다를 2개 이상 받는 함수
    twoLambda({"$it"}){a,b->"$a $b"}
}

fun noParam(out:()->String)=println(out()) // 매개 변수 없음
fun oneParam(out:(String)->String)=println(out("OneParam"))
fun manyParam(out:(String, String)->String)=println(out("One","two"))
fun twoLambda(first:(String)->String, second:(String,String)->String){
    println(first("FirstLambda"))
    println(second("Second","Lambda"))
}