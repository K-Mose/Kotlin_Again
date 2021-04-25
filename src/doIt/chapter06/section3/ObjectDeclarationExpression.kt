package doIt.chapter06.section3

/*
object 선언 vs object 표현식

object 선언 - companion object {}
object로 선언된 객체는 멤버 프로퍼티와 메서드를 객체 인스턴스 생성없이 바로 사용가능함
단일 인스턴스를 생성해 처리하기 때문에 싱글톤 패턴에 이용됨
object 선언시 접근 시점에서 객체가 생성됨.
생성자를 호출하지 않으므로 주/부 생성자 사용 불가, 하지만 초기화 블록 init 사용 가능
클래스나 인터페이스를 상속할 수 있음

object 표현식 -
object 선언과 다르게 이름이 없으며 싱글톤이 아님
object 표현식이 사용될 때마다 새로운 인스턴스가 생성됨
사용
    1. 딱 한 번만 구현되는 인터페이스를 정의하기 부담스러울 경우
        interface Shape{
            fun onDraw(){...}
        }
        val triangle = object: Shape{
            override fun onDraw(){...} // 여기서 딱 한 번 구현
        }
    2. 객체는 필요하지만 상위 인터페이스나 클래스가 없는 경우
        fun foo(){
            val adHoc = obejct {
                var x: Int = 0
                var y: Int = 0
            }
            println(adHoc.x + adHoc.y)
        }

object 표현식 안의 코드는 둘러싸여있는 범위 내부의 변수에 접근 가능 ( inner class 같이 )

 */
fun publicFoo() = object {val x: String = "x"}
private fun privateFoo() = object {val y: String = "y"}
fun main(){
    val pretendedMan = object: SuperMan(){
        override fun fly() = println("못날아 ..")
        val name: String = "PrettyMan"
        var age: Int = 999
    }
    pretendedMan.work()
    pretendedMan.talk()
    pretendedMan.fly()
    // 내부 변수 접근
    println("${pretendedMan.name} : ${pretendedMan.age}")

    fun innerPublicFoo() = object {val x: String = "x"}
    val x = innerPublicFoo().x // 가능
    val y = privateFoo().y // 가능
//    val x = publicFoo().x // Unresolved reference
    println("$x \t $y")
    println('\n')

    // 외부 객체 접근 / inner class 와 비슷해보임
    var count = 0
    val click = object {
        fun click(){
            count++
        }
    }
    click.click()
    println(count)
    Writer()
}
class Writer{
    companion object{
        val writtenBy: String = "Kim Mose"
    }
    init {
        println("\n\nwritten by $writtenBy")
    }
}
open class SuperMan(){
    fun work() = println("일함")
    fun talk() = println("말함")
    open fun fly() = println("날라다님")
}