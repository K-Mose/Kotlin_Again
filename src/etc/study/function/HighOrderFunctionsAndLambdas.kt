package etc.study.function

/*
https://kotlinlang.org/docs/lambdas.html
고차 함수와 람다

코틀린 함수들은 일급객체이다. 일급객체는 변수와 데이터 구조로서 저장되고,
고차 함수의 인자로 들어가고 반환된다. 함수와 함께 연산 가능하며 비 함수 값과 함께 사용 가능하다.

정적 타입의 프로그래밍 언어인 코틀린에서 사용하고,
함수들을 대표하고 특수화된 언어 구성의 세트를 제공하기 위해
(Int)->String와 같은 함수 타입들이 사용된다.
(함수형 타입 - https://kotlinlang.org/docs/lambdas.html#function-types )

고차 함수
고차 함수는 함수를 인자로 받고, 반환하는 함수이다.

좋은 예로 https://en.wikipedia.org/wiki/Fold_(higher-order_function)
컬력션을 위한 함수형 프로그래밍 특징이 있다.
초기 누적값을 가지고 함수와 결합하여 빌드하고,
각 컬렉션 요소가 누적해 대체되어 연속적으로 결합된 현제 누적값을 반환한다.
fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}
위 코드 안에서, combine 인자는 (R,T)->R의 함수형 값을 받는다.
combine이 받은 함수 안에서는 R과 T타입을 받고 R 타입을 리턴한다.
이것은 내부적으로 for 루프에서 호출된다. 그리고 반환 값이 accumulator에 할당된다.

fold함수를 부르기 위해 함수형 인스턴스를 인자로 필요로 한다.
그리고 람다식은 고차 함수 호출부에서 폭넓게 사용된다.
예제 callFold() 확인.

함수 타입
코틀린은 함수를 다루기 위한 선언에 함수 타입류를 사용한다.
이러한 타입들은 함수들의 특징에 응답하는 특수한 표기법을 사용한다.
    - 모든 함수 타입은 삽입어구를 갖는 값들의 리스트와 리턴 타입을 갖는다.
      (A, B)->C 는 A,B 타입의 인자를 받고 C 타입의 값을 반환하는 함수라고 볼 수 있다.
      ()->A와 같이 인자 타입의 리스트가 빈다면, Unit 리턴 타입이 빠지면 안된다.
    - 함수 타입들은 점 표기법(.)으로 명시된 추가적인 리시버 타입을 가질 수 있다.
      A.(B)->C와 같이 리시버 객체 A에서 불려지고, B는 인자로, C는 반환 값을 사용된다.
      Function literals with receiver는 이러한 타입들과 같이 자주 사용된다.
    - 유보 함수는 suspend 한정자와 함께 사용되며, 특별한 종류의 함수 타입에 속한다.
함수 타입 표기법은 선택적으로 함수 인자를 포함하기 위한 이름으로 사용될 수 있다.
(x: Int, y: Int) -> Point. 이러한 이름들은 인자들의 의미를 기록하기(documenting) 위해 사용된다.
함수 타입을 널 허용(nullable)으로 명시하기 위해 이와 같은 한정자를 쓴다. ((Int,Int)->Int)?
함수 타입은 수식어구들과 합쳐질 수 있다. (Int) -> ((Int)->Unit))
* 화살표 표기법은 오른쪽 결합이다. (Int)->(Int)->Unit은
((Int)->(Int))->Unit이 아니라 (Int) -> ((Int)->Unit))와 같다.

또한 함수 타입에 타입 명명을 할 수 있다.
typealias ClickHandler = (Button, ClickEvent)->Unit

함수 타입의 인스턴스화
함수형 타입의 인스턴스를 획득하기 위한 방법은 몇가지가 있다.
    - 아래 형식 중 function literal 내부의 코드블록을 사용한다.
        - 람다식 : { a, b -> a + b}
        - 익명 함수 : fun(s: String): Int { return s.toIntOrNull() ?: 0 }
      Function literals with receiver는 리시버로 함수 타입의 값처럼 사용된다.
    - 기존의 선언을 호출 가능한 참조로 사용
        - 최고 레벨, 지역, 멤버 또는 확장 함수 : ::IsOdd, String::toInt
        - 최고 레벨, 함수 또는 확장 프로퍼티 : List<Int>::size
        - 생성자 : ::Regex
    foo::String과 같이 특정 인스턴스의 멤버를 지칭하는 바인딩된 호출 가능한 참조를 포함한다.
    - 함수 타입을 인터페이스처럼 받는 사용자 클래스의 인스턴스에서의 사용
        class IntTransformer: (Int) -> Int {
            override operator fun invoke(x: Int): Int = TODO()
        }
        val intFunction: (Int) -> Int = IntTransformer()
충분한 정보가 없으면, 컴파일러는 함수 타입을 위한 변수를 추론한다.
val a = {i: Int -> i + 1} // The inferred type is (Int)->Int

리시버가 있고 없는 함수의 Non-literal 값들은 교체 가능하다.
그래서 첫 인자로 리시버를 대신할 수 있다.(stand in for) 그리고 반대로도(vice versa).
예로 (A,B)->C는 A.(B)->C가 오는 자리에 주어지거나 할당될 수 있다.
그리고 반대로도(and the other way around). *andTheOtherWayAround() 함수 참고
* 리시버가 없는 함수 타입은 default로 추론된다. 심지어 변변수는 확장 함수의 참조와 함께 초기화 된다.
  바꾸기 위해 변수 타입을 명시적으로 써줘야 한다.

함수 타입 인스턴스 깨우기
함수 타입의 값은 invoke() 연산자[f.invoke(x) 또는 f(X)]를 사용해서 깨울 수 있다.
만약 값이 리시버 타입을 가지고 있다면, 리시버 오브젝트는 첫 번째 인자로서 넘겨진다.
다른 방법으로는 리시버 객체와 함께 접두에 붙이는 것이다.
값이 1.foo(2) 같이 함수의 확장 처럼 호출 된다. * invokingFunctionTypeInstance()
> invoke 또는 f() 처럼 받는 것은 같다.

inline 함수
가끔씩 효율성을 위해 인라인 함수는 사용된다.
인라인 함수는 고차함수를 위해 유연한 제어 흐름을 제공한다.


람다 표현식과 익명 함수
람다 표현과 익명 함수는 함수 리터럴(function literals) 함수이다.
(function literals - https://www.informit.com/articles/article.aspx?p=3089301&seqNum=3#:~:text=The%20function%20literal%20is%20a,passed%20to%20the%20map%20method.&text=Think%20of%20anonymous%20function%20literals,then%20giving%20it%20a%20name.)
function literals는 선언되지는 않았지만 즉각적으로 표현식을 넘기는 함수이다.
max(strings, { a, b -> a.length < b.length })
예제를 보면, max 함수는 두 번째 인자로 함수 값을 받는 고차 함수이다.
이 두 번째 인자는 스스로를 함수라고 표현한다. 그리고 이것을 함수 리터럴이라고 부른다.
그리고 아래 함수와 동일하다.
fun compare(a: String, b: String): Boolean = a.length < b.length

람다 표현식 문법
람다식의 모든 문법적 형태는 아래와 같다.
val sum: (Int, Int) -> Int = {x: Int, y: Int -> x + y}
    - 람다식은 항상 중괄호{curly braces}로 둘러싸인다.
    - 모든 문법 형태안에 있는 인자 선언은 중괄호 속에 들어가고, 선택적으로 타입을 표기할 수 있다.
    - 바디는 -> 화살표시 이후로 나온다.
    - 리턴타입이 Unit이 아니라면, 마지막(또는 하나의) 표현식 안에서 람다 바디는 마지막 값을 리턴값으로 여긴다.
val sum = {x:Int, y:Int -> x + y } 여기서 x+y가 리턴값


끄는 람다 넘기기(Passing trailing lambdas)
코틀린 안에서는, 함수의 마지막 인자가 함수형이라면 그리고 람다식을 삽입어구 바깥에 놓아
인자로 반응할 수 있게 넘겨줄 수 있는 관습이 있다.
val product = item.fold(1) { acc, e -> acc * e}
위와 같은 문법들이 끄는 람다이다.
만약 람다가 하나의 인자만 호출한다면, 삽입어구 전체가 생략 가능하다.
run { println("...") }

it: 단일 인자의 암시적 이름
람다식이 하나의 인자만 갖는 것은 일반적이다.
만약 컴파일러가 스스로 특징을 알아 차린다면,
it은 선언 뿐만 아니라 화살표기법(->)을 생략 가능하게 한다.
인자는 암시적으로 it 이름으로 선언될 것이다.
ints.filter { it > 0 } // 리터럴의 타입은 (it: Int) -> Boolean 이다.

람다식에서 값 반환
람다식에서 보증된 반환 문법을 사용해서 명시적으로 값 반환을 할 수 있다.
반대로, 마지막의 값이 암시적으로 반환된다.
아래의 두 코드는 같다.
ints.filter{
    val shouldFilter = it > 0
    shouldFilter
}
ints.filter{
    val shouldFilter = it > 0
    return@filter shouldFilter
}
이러한 관례는 외부 삽입어구에 람다식 넘기기(Passing trailing lambdas)와 함께
LINQ 스타일 코드에 허용된다.

사용하지 않는 변수에 언더스코어
만약 람다의 인자가 사용되지 않는다면 언더스코어로 이름을 대체할 수 있다.
map.forEach { _, value -> println("$value!")}

람다식 분해 - 주소 참조 https://kotlinlang.org/docs/destructuring-declarations.html#destructuring-in-lambdas

익명 함수
위에서 보여준 람다식 문법에서 빠진 하나는 함수의 리턴 타입을 명시화 하는 기능이다.
수 많은 경우에서 리턴타입이 자동적으로 추론 가능하기 때문에 불필요하게 여겨졌다.
그러나, 익명함수를 쓴다면 이것을 명시적으로 구체화 시키는 것이 반드시 필요하다.
fun(x: Int, y: Int): Int = x + y
익명 함수는 이름이 빠진것만 빼면 일반 함수의 선언과 매우 비슷해보인다.
그리고 익명 함수는 블록으로 바디의 내용을 감쌀 수 있다.
fun(x: Int, y: Int): Int{
    return x + y
}
인자 값과 리턴 타입은 일반 함수와 같은 방식으로 구체화 된다.
예외적으로 인자의 타입이 문맥에서 추론 가능하다면 생략 될 수 있다.
ints.filter(fun(item) = item > 0)
익명 함수에서 반환 타입 추론은 일반 함수와 같이 발생한다.
익명 함수에서는 표현식 바디와 함께 리턴 타입이 자동적으로 추론되어지고,
익명 함수의 블록 바디로 구체적으로 명시화된다.
* 익명 함수 인자는 항상 삽입어구 안으로 넘겨진다.
  편리한 문법(the shorthand syntax)은 함수를 떠나
  외부 삽입어구에 작동하는 람다 표현시으로 사용되게 허용한다.
람다식과 익명 함수의 다른 점은 지역 반환 없이 행동하는 것이다.
라벨이 없는 return 문은 항상 fun 키워드와 함께 선언된 함수에서 반환된다.
이것은 람다식 안에 return은 동봉된 함수에서 반환될 것이다.
반면에, 익명함수는 항상 익명함수 스스로를 반환단다는 것을 뜻한다.

클로저
람다식과 익명 함수는 (로컬 함수와 object 표현식일 때) 외부 범위에 선언된 변수를 포함한 클로저에 접근할 수 있다.
클로저 안에 포착된 변수는 람다식 안에서 변환되어진다. * closures() 참고

함수 리터럴과 리시버
A.(B)->C와 같은 함수 타입과 리시버는 함수 리터럴의 특수한 형태로 객체화 된다.
위에서 말했듯, 코틀린은 함수 타입 리시버의 인스턴스를 호출하면서 리시버 객체를 제공할 수 있다.
함수 리터럴의 바디 안에서 리시버 객체는 암시적으로 this를 전달 받는다. 그러므로 추가적인 접근자 없이
리시버 객체의 맴버에 접근할 수 있다. 또 this 표현으로 리시버 객체어 접근할 수 있다.
 > 확장 함수에서 it과 this로 받는 차이를 여기서 알 수 있다.
이러한 방식은 확장 함수와 비슷하다. 리시버 객체의 함수 내부에서 위와 같이 접근 할 수 있다.
리시버 객체가 plus()에서 호출 되면서 함수 리터럴이 타입과 함께하는 리시버와 오는 예제이다.
val sum: Int.(Int)->Int = {other -> plus(other)}
 > 여기서 plus는 리시버 객체를 암시적으로 this를 사용하기 때문에 Int의 내장 함수인 plus에 바로 접근한다.
익명 함수 문법은 함수 리터럴의 리시버 타입을 직접적으로 구체화 시키는 것을 허용한다.
이것은 함수 타입의 변수를 리시버와 함께 선언하는 것이 필요할 때 유용하게 쓰인다. 그리고 나중에도.
val sum = fun Int.(other: Int): Int = this + other
 > this로 구체화 시켜서 확장 함수로 받는 other 변수와 더하는 함수를 구현한다.
람다식은 리시버 타입이 문맥 안에서 추론되어 질 때 함수 리터럴과 리시버가 사용되어 진다.
가장 중요하게 사용되는 예제중 하나인 type-safe builders이다.
class HTML { // https://kotlinlang.org/docs/type-safe-builders.html
    fun body() { ... }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()  // create the receiver object
    html.init()        // pass the receiver object to the lambda
    return html
}

html {       // lambda with receiver begins here
    body()   // calling a method on the receiver object
}




 */

fun main(){
//    callFold()
//    andTheOtherWayAround()
//    invokingFunctionTypeInstance()
    closures()
}

fun callFold(){
    val items = listOf(1,2,3,4,5)
    items.fold(0) { acc: Int, i: Int ->
        println("acc = $acc, i = $i")
        val result = acc + i
        println("result = $result")
        result
    }
    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i})
    val product = items.fold(1, Int::times)
}

fun andTheOtherWayAround(){
    val repeatFun: String.(Int) -> String = { times -> this.repeat(times)} // CharSequence의 repeat 내장 함수
    val twoParameters: (String, Int)->String = repeatFun // 가능
    fun runTransformation(f: (String, Int)->String):String {
        return f("hello ",3)
    }
    val result = runTransformation(repeatFun)

}

fun invokingFunctionTypeInstance(){
    val stringPlus: (String, String)->String = String::plus // 스트링의 내장 함수 plus
    val intPlus: Int.(Int)->Int = Int::plus

    println(stringPlus.invoke("<-", "->"))
    println(stringPlus("Hello,", "World!"))
    println(intPlus.invoke(1,1))
    println(intPlus(1,2))
    println(2.intPlus(3)) // 확장처럼 호출
}

fun closures(){
    val ints = arrayOf(-4,-3,-2,-1,0,1,2,3,4,5)
    var sum = 0
    ints.filter { it > 0 }.forEach {
        sum += it
        println(sum)
    }
    println("sum : $sum")
}