package etc.study.delegation
import kotlin.math.E
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
// Delegate 대리자를 사용하면 직접 접근 하지 않고 값을 할당하거나 가져올 수 있다.
/*
※ 의역 많음 !!
https://kotlinlang.org/docs/delegated-properties.html
Delegated Properties
데이터를 필요할 때 수동으로 적용시키는 방법 3가지
    - Lazy properties : 첫 접근 때에만 값이 정해진다.
    - Observable properties : listener가 값 변경을 감지해 적용한다.
    - Storing properties in map, instead of a separate field for each property.

코틀린은 Delegated properties를 제공한다.
package doIt.chapter06.section2 - DelegationByObservableVetoable
에서 본것 같이 by을 이용한 대리자 위임이 가능하다.
val/  var <property name>: <Type> by <expression>
after by is a delegate,
because get() (and set()) corresponding to the property will be delegated to its getValue() and setValue() methods.
속성 델리게이트는 인터페이스를 필요로 하지 않는다.
getValue() (var 에서 사용시 setValue())를 지원한다.
 */
// Example
class Example {
/*
p를 읽으려 할 때 Delegate의 인스턴스가 대리자로 위임된다.
getValue()가 Delegate()에 의하여 실행되면 첫 번쨰 인자에 p를 읽는 객체가 들어가고
두 번쨰 인자에는 p 자체가 들어간다.

p에 값을 할당할 때도 위와 비슷하다.
 setValue()가 호출되면, 앞에 두 인자에 위와 같고 세 번쨰 인자에는 할당된 변수가 들어간다.
 */
    var p: String by Delegate()
}
class Delegate{
    // operator fun ?? https://stackoverflow.com/a/48193569
    // operator는 일반적으로 함수가 연산자처럼 작동되길 원할때 사용한다.
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String{
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String ) {
        println("$value has been assigned to '${property.name}' in $thisRef")
    }
}

/*
Lazy properties
lazy()는 람다를 인자로 받고 Lazy<T> 객체를 반환한다. (lazy property를 수행하기 위한 대리자로 여겨지는 객체)
public interface Lazy<out T> {
    public val value: T
    public fun isInitialized(): Boolean
}
첫 호출을 하게 되면 람다식이 lazy()에 전달되고 결과를 기억한다.
두 번째 호출부터는 기억된 결과를 반환하기만 한다.
*/
// lazy의 2가지 방법, by를 이용한게 thread에 더 안정적이라 한다.
val lazyValue:String by lazy{
    println("Computed") // 첫 초기화 때만 출력 됨
    "I'm lazyOne" // 반환 값
}
val lazyValue2 = lazy{
    println("Computed") // 첫 초기화 때만 출력 됨
    "LazyTwo homie " // 반환 값
}

/*
Observable properties
observable은 2개의 인자를 받는다. 초깃값과 수정을 위한 핸들러
핸들러는 값을 할당할 때 마다 호출 된다.
핸들러는 3개의 인자를 받는다.
 */

class User{
    var name: String by Delegates.observable("No Name" ){
        property, oldValue, newValue ->
        println("$property.name : $oldValue -> $newValue")
    }
}

/*
다른 프로퍼티 위임하기
프로퍼티는 자기의 getter와 setter를 다른 프로퍼티에 위임할 수 있다.
대리자는 최상위와 클래스 프로퍼티에 사용될 수 있다.
The delegate property
  - a top-level property
  - a member or an extension property of the same class
  - a member or an extension property of another class
최상위 프로퍼티, 같은(다른) 클래스 내 멤버 혹 확장 프로퍼티
위임하기 위해 델리게이터 이름에 :: 한정자를 사용한다.
eg. property::delegate (delegate에 값을 넣으면 property값이 바뀐다.)
Deprecated 할 떄 보기 편하게 할 수 있다 한다.
 */
var topLevelInt: Int = 0
class ClassWithDelegate(val anotherClassInt: Int)
class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate){
    var delegatedToMember: Int by this::memberInt
    var delegatedToTopLevel: Int by ::topLevelInt
    // 아직 잘 모르겠다?
//    var delegatedToAnotherClass: Int by anotherClassInstance::anotherClassInt
}
var MyClass.extDelegated: Int by ::topLevelInt

/*
Storing properties in a map
map으로 들어온 k,v가 프로퍼티의 이름과 같은 곳에 값이 할당된다.
 */
// MutableMap에서도 가능하다.
class UserMap(val map: Map<String, Any?>){
    val name: String by map
    val age: Int by map
    init {
        println("${map.keys} -> ${map.values}")
    }
}


fun main(){
    val e = Example()
    println(e)
    println(e.p)
    e.p = "NEW"
    println(e.p)
    println('\n')

    println(lazyValue)
    println(lazyValue2.value)
    println(lazyValue2.isInitialized())
    println(lazyValue)
    println('\n')

    val user = User()
    user.name = "First"
    user.name = "Second"
    println('\n')

    val classWithDelegate = ClassWithDelegate(20)
    val myClass = MyClass(10, classWithDelegate)
    println(myClass.extDelegated)
    println(myClass.delegatedToMember)
    println(myClass.delegatedToTopLevel)
    println(myClass.anotherClassInstance)
    myClass.extDelegated = 100
    println(myClass.extDelegated)
    println(myClass.delegatedToMember)
    println(myClass.delegatedToTopLevel)
    println(myClass.anotherClassInstance)
    println('\n')

    // map을 대리자로 사용하려면 Map의 key와 프로퍼티의 이름을 같게 해야한다.
    val userM = UserMap(mapOf(
        "name" to "Kim Mose",
        "age" to 28
    ))
    println(userM.name)
    println(userM.age)
    println(userM.map)
}

/*
Local delegated properties
지역 변수를 대리자처럼 사용 가능하다.
 */

/* // super/sub type - https://learndatamodeling.com/blog/supertype-and-subtype/
Property delegate requirements - https://kotlinlang.org/docs/delegated-properties.html#property-delegate-requirements
대리자가 요구하는 것들
읽기 전용 객체에서는 대리자가 getValue()함수 내에서 다음과 같은 인자를 제공해야 한다
  - thisRef : 소유 객체보다 같거나 supertype이어야 한다. (확장인 경우에는 타입도 확장 되어야 한다. ??)
  - property : KProperty<*>의 타입이거나 supertype이어야 한다.
getValue()는 객체와 같은 타입의 값을 리턴해야 한다.

수정 하능한 객체는 setValue()함수를 추가적으로 제공하며, 아래와 같은 인자를 같는다
  - thisRef : 소유 객체보다 같거나 supertype이어야 한다. (확장인 경우에는 타입도 확장 되어야 한다.)
  - property : KProperty<*>의 타입이거나 supertype이어야 한다.
  - value : 객체와 같은 타입이어야 한다. (혹은 supertype과)

getValue()와 setValue()는 대리자 클래스의 멤버 함수 또는 확장 함수를 제되어진다.
두 함수 모두 operator 키워드로 제한되어야 한다.

대리자를 익명 객체에 만들 수 있다.
[ReadOnlyProperty, ReadWriteProperty] 읽기 전용, 읽기 쓰기 객체로 만들 수 있다.
읽기 전용 객체에는 getValue()가 선언되고,
읽기 쓰기 객체는 읽기 전용 객체를 상속하고 setValue()가 추가로 선언되었다.
 > 읽기 쓰기 객체로 읽기 전용 객체를 처리할 수 있다. ( 업케스팅?

Translation Rules
모든 대리자 객체를 위해 코틀린 컴파일러는 보조 객체와 대맂를 생성한다.
eg. prop 객체에 숨겨진 객체인 prop$delegate가 생성되었다.
// this code is generated by the compiler instead:
class C {
    private val prop$delegate = MyDelegate()
    var prop: Type
        get() = prop$delegate.getValue(this, this::prop)
        set(value: Type) = prop$delegate.setValue(this, this::prop, value)
}
코틀린 컴파일러는 prop에 필수 정보들을 인자로 넘겨준다.
첫 번쨰 인자에는 this는 outer class C가 들어가고, this::prop에는 KProperty타입에 스스로를 나타내는 객체가 들어간다.

Providing a delegate
ProvideDelegate 함수를 정의하기 위해
대리자를 상속하는 객체 생성 로직을 확장할 수 있다. (you can extend the logic of creating the object to which the property implementation is delegated._
만약 객체가 by의 오른쪽에 정의되었으면, provideDelegate를 멤버 또는 확장 함수로 정의 합니다.
위 함수는 대리자 인스턴스를 생성하기 위해 호출되어집니다.
provideDelegate는 객체가 초기화 되었을 때에 일관성 검사로 사용할 수 있습니다.
eg. 객체가 바인딩 되기 전에 객체를 확인하고 싶다면 아래와 같이 하면 됩니다.
class ResourceDelegate<T> : ReadOnlyProperty<MyUI, T> {
    override fun getValue(thisRef: MyUI, property: KProperty<*>): T { ... }
}

class ResourceLoader<T>(id: ResourceID<T>) {
    operator fun provideDelegate(
            thisRef: MyUI,
            prop: KProperty<*>
    ): ReadOnlyProperty<MyUI, T> {
        checkProperty(thisRef, prop.name)
        // create delegate
        return ResourceDelegate()
    }

    private fun checkProperty(thisRef: MyUI, name: String) { ... }
}

class MyUI {
    fun <T> bindResource(id: ResourceID<T>): ResourceLoader<T> { ... }

    val image by bindResource(ResourceID.image_id)
    val text by bindResource(ResourceID.text_id)
}
"""
위에는 provideDelegate 함수가 MyUI와 리소스가 바인드하기 위해 대리자로 사용되지만
아래에는 provideDelegate를 사용하지 않아 명시적으로 객체의 이름을 쓰고 있습니다.
즉, 대리자를 사용하면 암시적으로 바인딩이 된다는 소리??
"""
provideDelegate의 인자들은 getValue()와 같게 여겨집니다.
  - thisRef : 소유 객체보다 같거나 supertype이어야 한다. (확장인 경우에는 타입도 확장 되어야 한다.)
  - property : KProperty<*>의 타입이거나 supertype이어야 한다.
provideDelegate 함수는 MyUI 객체가 생성되는 동안에 각각 객체로 호출되고,
수행되는 동안에 필수 유효성 검사(necessary validation)를 합니다.
provideDelegate 기능이 없이 바인딩을 하려면, 프로퍼티의 이름을 명시적으로 제공해야 합니다.
// Checking the property name without "provideDelegate" functionality
class MyUI {
    val image by bindResource(ResourceID.image_id, "image")
    val text by bindResource(ResourceID.text_id, "text")
}

fun <T> MyUI.bindResource(
        id: ResourceID<T>,
        propertyName: String
): ReadOnlyProperty<MyUI, T> {
    checkProperty(this, propertyName)
    // create delegate
}

아래 코드에서는 provideDelegate 함수가 보조적인 prop$delegate 객체를 초기화 하기 위해 호출됩니다.
class C {
    var prop: Type by MyDelegate()
}

// this code is generated by the compiler
// when the 'provideDelegate' function is available:
class C {
    // calling "provideDelegate" to create the additional "delegate" property
    private val prop$delegate = MyDelegate().provideDelegate(this, this::prop)
    var prop: Type
        get() = prop$delegate.getValue(this, this::prop)
        set(value: Type) = prop$delegate.setValue(this, this::prop, value)
}
Translation rule과 비교하여 보면, MyDelegate()를 호출할 때 provideDelegate를 같이 호출해줍니다.
※Note: provideDelegate 함수는 생성시 보조 객체로서 영향을 미치고, getter와 setter에는 영향을 미치지 않습니다.

코틀린 라이브러리에서 제공되는 PropertyDelegateProvider를 사용하면,
새로운 함수 없이 delegate provider를 생성 할 수 있습니다.
val provider = PropertyDelegateProvider { thisRef: Any?, property ->
    ReadOnlyProperty<Any?, Int> {_, property -> 42 }
}
val delegate: Int by provider

 */