package doIt.chapter07.section2

/*
내부 클래스
중첩(nested)/이너(inner) 두 종류가 있다.
독립저인 클래스로 정의하기 모호한 경우나 다른 클래스에서 잘 사용하지 않는 내부에서만 사용하고
외부에서는 접근할 필요가 없을 때가 있기 떄문. 남용하면 클래스의 의존성이 커지고 코드가 읽기 어려워진다.

Nested class
말 그대로 클래스 안에 또다른 클래스가 정의된 것
객체 생성없이 바로 사용 가능하다.
내부 클래스에서 외부 클래스의 프로퍼티에 접근 불가능하다.
하지만 companion object는 고정적인 메모리를 가지기 때문에 접근 가능하다.

Inner class
중첩 클래스와 달리, 내부 클래스에서 외부 클래스 프로퍼티에 직접 접근 가능하다. (private 까지)

Local class
지역 클래스는 특정 블록 범위 내에서만 작동되는 클래스이다.

Anonymous class
익명 객체, 6장의 object expression과 동일하다.
 */
fun main(){
    // Outer의 생성자를 거치지 않고 Nested의 생성자를 부름
    println(Outer.Nested().greeting())
    Outer().outside()
    println()
    println(Outer().Inner().getInner())
}
class Outer{
    companion object{
        fun whereAreYou(code: Int): String {
            return when (code) {
                1 -> "USA"
                81 -> "JAPAN"
                82 -> "KOREA"
                else -> "I don't know"
            }
        }
    }
    val ov = 82
    private var po: String = "[Outer]"
    class Nested{
        var po: String = "[Nested]"
        val nv: Int = 1
        fun greeting() = "$po Hello ! ${whereAreYou(nv)}" // companion object는 접근 가능함
    }

    fun outside(){
        val msg = Nested().greeting()
        println("$po: $msg, ${whereAreYou(ov)}")
    }

    inner class Inner{
        fun getInner(): String {
            po = "[Inner]"
            val iv = 81
            // local class - 함수 내부에서만 사용 가능
            class Local{
                val po: String = "[Local]"
            }
            return "$po $ov ${whereAreYou(iv)} and ${Local().po}"
        }
    }


}