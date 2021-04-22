package doIt.chapter06.section2

import kotlin.properties.Delegates

/*
by를 이용한 위임
특정 클래스에서 확장하거나 이용할 수 있도록 by를 통한 위임이 가능
by를 사용하면 하나의 클래스가 다른 클래스에 위임하도록 선언하여
위임된 클래스가 가지는 멤버를 참조 없이 호출할 수 있게 됨
프로퍼티를 위임하려면 위임 객체에 by 키워드를 사용
    <val | var | class> 프로퍼티 혹은 클래스 이름: 자료형 by 위임자
사용하는 이유
    코틀린의 표준라이브러리는 open으로 정의하지 않은 클래스를 사용하여 상속이나 직접 확장이 어려움
    필요한 경우에만 위임을 통해 상속과 비슷하게 클래스의 모든 기능을 사용하면서 추가 확장 구현 가능

observable() 함수와 vetoable() 함수의 위임
observable()과 vetoable()을 사용하기 위해해서는 kotlin.properties.Delegates 을 import 함

observable
프로퍼티를 감시하고 있다가 특정 코드의 로직에서 변경이 일어날 때 호출되어 처리됨
특정 변경 이벤트에 따라 호출되므로 콜백이라고도 부름


vetoable
observable과 비슷하지만 반환값에 따라 프로퍼티 변경을 허용하거나 취소할 수 있음

 */

fun main(){
    val myDamas = CarModel("Damas 2010", VanImpl("100마력"))
    val my350z = CarModel("350z 2008", SportImpl("350마력"))
    // carInfo()에 대해 다형성을 나타냄
    myDamas.go()
    my350z.go()

    println("\n")

    val user = User()
    user.name = "Mose" // observable이 감시를 하다가 값이 변경되는 시점에서 이벤트가 발생한다.
    user.name = "MoSe"

    println("\n")

    var max: Int by Delegates.vetoable(0){ // 초깃값 0
        prop, old, new -> new > old // 조건에 맞지 않으면 실행 거부 ( return 값은 Boolean )
    }
    // 조건에 맞지 않으면 실행을 하지 않기 떄문에 -1은 실행되지 않는다 ( 조건 :  new > old )
    println(max)
    max = 10
    println(max)
    max = -1
    println(max)
    max = 100
    println(max)
}

// 클래스 위임
interface Car{
    fun go(): String
}

class VanImpl (val power:String) : Car {
    override fun go(): String = "은 짐을 적재하며 $power 을 가집니다."
}
class SportImpl (val power:String) : Car {
    override fun go(): String = "은 경주용에 사용되며 $power 을 가집니다."
}

class CarModel(val model: String, val impl: Car): Car by impl {
    fun carInfo(){
        println("$model ${go()}") // 참조 없이 각 인터페이스 구현 클래스 go()에 접근
    }
}

// Observable | vetoable
class User{
    var name: String by Delegates.observable("NONAME"){
        prop, old, new -> println("$old -> $new")
    }
}