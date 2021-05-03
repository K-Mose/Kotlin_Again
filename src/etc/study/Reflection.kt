package etc.study

import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaGetter

// https://kotlinlang.org/docs/reflection.html#bound-constructor-references
/*
리플렉션은 런타임에서 프로그램의 내성(introspecting)을 허용하는 언어와 라이브러리 기능의 집합체이다.
크톨린은 일급 객체의 함수와 프로퍼티를 만들 수 있는 객체이다.
그리고 그들의 내성은 (예로 런타임에서 프로퍼티와 함수의 이름 또는 타입을 학습한다.)
함수적, 반응형 유형에 단순히 쓰이면서 밀접하게 뒤얽힌다.(and introspecting them (for example, learning a name or a type of a property or function at runtime) is closely intertwined with simply using a functional or reactive style.)
 */

/*
JVM 의존성
JVM 플랫폼에서 5런타임 컴포넌트는 리플렉션 기능을 사용하는 것을 필요로 한다.
(Kotlin compiler distribution 안에 분산되어 있는 아티팩트인 kotlin-reflect.jat 처럼 분리되어져있다.)
요구되는 리플렉션 기능을 사용하지 않는 애플리케이션들을 위한 런타임 라이브러리의 사이즈를 줄이기 위해 사용된다.

리플렉션 을 사용하기 위해 Gradle이나 Maven에 kotlin-reflect 의존성을 주입하면 된다.
(만약 둘을 사용하지 않는다면 kotlin-reflect.jar를 프로젝트 클래스패스에 넣으면 된다.)
 */

/*
Class references
가장 기본적인 리플렉션 기능은 코틀린 클래스에서 런타임 참고문헌을 가져오는것이다.
정적으로 알려진 코틀린 클래스의 참고문헌을 가져오기 위해서는 class literal syntax를 사용해야 한다.
val c = MyClass::class
JVM에서 코틀린 클래스 참고문헌은 자바 클래스 참고문헌과 다르다.
자바 클래스 참고문헌을 가져오기 위해서는 KClass 인스턴스의 프로퍼티인 .java을 사용한다.

Bound class reference
오브젝트를 리시버로 받는 ::class syntax를 사용해 특정한 객체의 클래스 참고문헌을 가져올 수 있다.
val widget: Widget = ...
assert(Widget is GoodWidget) {"Bad widget: ${widget::class.qualifiedName}"}
여기서 리시버 표현식의 타입과 별개로 객체(GoodWidget or BadWidget 같은)의 정확한 클래스 참고문헌을 획득할 수 있다.
 */

/*
Callable reference
프로그램 구조를 제외한 함수, 프로퍼티 그리고 생성자 참조는 함수 타입의 인스턴스로 사용, 불러진다.
호출 가능한 참조의 일반적인 슈퍼타입은 KCallable<out R>이다. R은 반환되는 값의 타입이다.

function references
fun isOdd(x: Int) = x % 2 != 0
위와 같이 함수를 선언했다면, 직접적으로 isOdd(5) 처럼 호출 가능하다.
또한 다른 함수에 인자로 들어가는 함수형 값으로 사용 가능하다.
:: 를 사용해야 한다.
::isOdd의 타입은 (Int)->Boolean 이고 isOdd()의 타입은 Boolean이다

함수 참조는 인자 수에 의존하는 KFunction<out R> 서브타입에 속해있다.
eg. KFunction<T1, T2, T3, R>

:: 는 컨텍스트에서 기대 타입이 알려질 때 오버로드된 함수로 혼용될 수 있다.
eg.
fun isOdd(x: Int) = x % 2 != 0
fun isOdd(s: String) = s == "brillig" || s == "slithy" || s == "tove"

val numbers = listOf(1, 2, 3)
println(numbers.filter(::isOdd)) // refers to isOdd(x: Int)
그렇지않으면, 명시적인 특정 타입 변수 안에 메소드 참조를 저장하는 필요한 컨텍스트를 제공할 수 있다.
val predicate: (String) -> Boolean = ::isOdd   // refers to isOdd(x: String)
( 위에서 (String) -> Boolean 같이)

만약 클래스의 멤버 또는 확장 함수 사용이 필요하다면, 이와 같이 사용한다.
String::toCharArray
심지어 확장 함수 참조로 변수를 초기화 하더라도, 언급된 함수는 인자를 갖지 못한다.
(리시버 객체가 추가적인 인자를 받을 것이다.)
타입을 명시적으로 표시한다.
val isEmptyStringList: List<String>.() -> Boolean = List<String>::isEmpty
isEmpty()는 String의 내장 함수이다.

function composition 예
아래 함수를 봐보자
fun <A, B, C> compose(f: (B)->C, g: (A)->B): (A) -> C {
    return { x -> f(g(x))}
}
이것은 compose(f,g) = f(g(*))를 받는 두 함수의 복합체를 반환한다.
아래와 같이 호출 가능한 함수로 저굥 가능하다.
fun length(s: String) = s.length
val oddLength = compose(::isOdd, ::length)
var strings = listOf("a", "ab", "abc")
println(strings.filter(oddLength))
 => [a,abc]
여기서 oddLenght는 x로 받은 값을 length()로 길이를 구하고 isOdd()에 넘겨 홀수여부를 Boolean으로 return 한다.
그래서 리스트에서 받은 값들이 홀수인 것들만 출력된다.

Property references
코틀린에서 프로퍼티를 일급 객체로써 접근하려면 ::를 써야 한다.
eg.
val x = 1
fun main(){
    println(::x.get())
    println(::x.name)
} // 함수 안에 있는 변수를 사용하면 > Unsupported [References to variables aren't supported yet
::x는 KProperty<Iny> 타입의 객체를 평가한다. get()을 이용해 읽을 수 있고, name으로 이름을 가져올 수 있다.
https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/ 참조
수정 가능한 프로퍼티는 KMutableProperty<Int> 객체를 반환한다.
프로퍼티 참조는 아래와 같이 사용 가능하다.
val strs = listOf("a","bc","def")
println(strs.map(String::length))

확장 프로퍼티로
val String.lastChar: String
    get() = this[length-1]
...
println(String::lastChar.get("abc") // c
"""
String::length 는 KProperty1<String, Int> 타입의 값이다.
KProperty1은 프로퍼티를 대표하며, 하나의 리시버를 받게 작동한다.
interface KProperty1<T, out V> : KProperty<V>, (T) -> V
T - 프로퍼티의 변수로 사용되는 리시버의 타입
V - 프로퍼티의 값의 타입
내장된 get, invoke 함수로 실행 가능하다.
String::length.get("ABCDE") > 5
String::length.invoke("ABCDE") > 5
"""

Interoperability with Java reflection
JVM 플랫폼에서 표준 라이브러리는 자바 리플렉션 객체에서 맵핑을 제공하는 리플렉션 클래스를 위해 확장을 포함한다.
(standard library contains extensions for reflection classes that provide a mapping to and from Java reflection objects)
eg. 코틀린 프로퍼티에서 getter를 제공하는 backing 필드 또는 자바 메소드를 찾기 위해 아래와 같이 작성 가능하다.
import kotlin.reflect.jvm.*

class A(val p: Int)

fun main() {
    println(A::p.javaGetter) // prints "public final int A.getP()"
    println(A::p.javaField)  // prints "private final int A.p"
}
자바 클래스와 상응하는 코틀린 클래스를 얻기 위해 확장 프로퍼티를 아래와 같이 사용하면 된다.

Constructor reference - 잘 모르겠다
생성자는 메소드와 프로퍼티와 같이 참조된다.
생성자가 받는것 처럼 함수의 객체 타입이 예측되고적절한 타입의 객체로 반환할 때 사용 가능하다.
생성자는 ::와 클래스 이름을 사용해서 참조된다.
아래를 잘 봐라. 인자가 없는 함수 타입을 인자로 받고 Foo 타입을 반환을 예상하는 함수다.
class Foo

fun function(factory: () -> Foo) {
    val x: Foo = factory()
}

::Foo를 사용해 인자 없는(zero-argument) 생성자를 를 만든다.
function(::Foo)
생성자의 호출가능한 참조는 인자 수를 의존하는 KFunction<out R> 서브타입으로 타입이 지정된다.

Bound function and property reference
개별의 객체의 인스턴스 메소드를 참조할 수 있다.
val numberRegex = "\\d+".toRegex()
println(numberRegex.matches("29")) // 직접 부름 directly

val isNumber = numberRegex::matches // 참조 사용 reference
println(isNumber("29"))
 > true true
matches를 직접 부르는 대신에, 참조를 사용했다.
matches는 직접 부를 수도 있고, 함수 타입의 표현식이 예상될 때 사용된다.

val numberRegex = "\\d+".toRegex()
val strings = listOf("abc", "124", "a70")
println(strings.filter(numberRegex::matches))
바인드된 타입과 상응하는 언바인드된 참조를 비교한다.
바운드는 attached 리시버를 갖는 호출가능한 참조이다.
리시버의 타입은 더이상 인자가 아니다.
val isNumber: (CharSequence) -> Boolean = numberRegex::matches

val matches: (Regex, CharSequence) -> Boolean = Regex::matches
프로퍼티 참조는 아래와 같이 바인드될 수 있다.
val prop = "abc"::length
println(prop.get()) > 3

리시버를 this로 구체화 시킬 필요가 없다.
this::foo와 ::foo는 같다.

Bound constructor reference
생성자의 inner class의 바인드된 호출 가능한 참초는 outer class의 인스턴스를 제공받음으로
아래와 같이 획득 가능 하다(can be obtained).
class Outer {
    inner class Inner
}

val o = Outer()
val boundInnerCtor = o::Inner
 */
val x = 1
val arrString = ArrayList<String>()
fun main(){
//    val isEmptyStringList: List<String>.()->Boolean = List<String>::isEmpty
//
//    println(arrString.isEmptyStringList())
//    println(arrString.isEmpty())
//    arrString.add("EBEB")
//    println(arrString.isEmptyStringList())
//    println(isEmptyStringList)
//    println(::arrString.name)
//    println(::arrString.get())
//    println(::x.get())
//    println(::x.name)
    fun getKClass(o: Any): KClass<Any> = o.javaClass.kotlin
    println(String::length.invoke("ABCDE"))
    println(String::length.get("ABCDE"))
    println(A::p.javaGetter)
    println(A::p.javaClass)
    println(getKClass(A::p.javaClass))
}

class A(val p: Int){

}