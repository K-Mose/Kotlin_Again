package etc.study

/*
https://typealias.com/concepts/declaration-site-variance/#:~:text=Declaration-Site%20Variance%20is%20variance,%2C%20function%2C%20or%20extension%20property.&text=Declaration-site%20variance%20is%20kind,generic%20that%20takes%20a%20vow.

가변성

Vow
  1. It can vow to never accept an object of that type parameter.
     In other words, it's a vow that the type parameter will never appear as a parameter of non-private function.
     그 타입의 매개변수 객체를 받지 않겠다 라고 맹세하는 것과 같다.
     다른 말로, 매개변수 타입이 private가 아닌 함수의 매개변수로써 나타나지 않겠다.
  2. It can vow to never return an object of that type paramter.
     In other words, It's vow that the type parameter will never appear as the return type of a non-private function.
     그 타입의 매개변수 객체를 절대 반환하지 않겠다 라고 맹세하는 것과 같다.
     다른 말로, 매개변수 타입이 private가 아닌 함수의 반환 값 타입으로 절대 나타나지 않겠다.

공변성 Covariance
out 키워드를 붙이면 T'가 T의 하위 자료형일 때 C<T'>는 C<T>의 하위 자료형으로 여긴다.
set(i: T) 종류의 함수를 사용할 수 없다.
생성자에서는 사용 가능하다.

반공변성 Contravariance
in 키워드를 붙이면 T'가 T의 하위 자료형일 때 C<T>는 C<T'>의 하위 자료형으로 여긴다.
get(): T 종류의 함수를 사용할 수 없다.

가변성의 프로퍼티
프로퍼티 또한 가변성에 영향을 받는다.
예를 들어 클래스 생성자 매개변수에는 읽기 전용인 val은 들어가지만 var는 들어갈 수 없다.
var는 암시적으로 getter/setter를 가지기 때문이다.

가변성의 상속
가변성이 상속될 때 형식 매개변수는 상속되지 않고, 서브클래스에서 재정의 된다.
subtype 형식 매개변수는 supertype과 같은 가변성으로 선언될 수 있고,
supertype의 가변성을 제거한 뒤 선언할 수 있다.
supertype이 어떻게 사용될지 약속이 없다면 subtype에서 형식 매개변수는 일관성이 없어진다.
그래서 subtype은 supertype이 만들어진 대로 만들 수 없다.

약속 깨버리기
형식 매개변수에 @UnsafeVariance 애노테이션을 사용한다면 가변성을 무시할 수 있다.
 */

// eg.
// 무변성 클래스
// get, set 둘 다 가능하다.
class Box<T>(private var item: T){
    fun getItem(): T = item
    fun setItem(newItem: T){
        item = newItem
    }
}
// 공변성 클래스 - out
// 생성자에서는 T 타입의 값을 받을 수 있지만
// 내부에 있는 non-private 함수에서는 불가능하다.
// private 함수에서는 가능 -> 내부에서 오는 변수로 넣을 떄는 T 타입의 변수가 올 것을 알기 때문에 가능하다.
class ReadOnlyBox<out T>(private var item: T){
    fun getItem(): T = item
    // private 함수에는 할당 가능하다.
    private fun setItem(newItem: T){ item = newItem }
    // Type parameter T is declared as 'out' but occurs in 'in' position in type T
//    fun setItem(newItem: T){ item = newItem }

}

// 반공변성 클래스 - in
// 공병성과 달리 non-private 함수에서 T 타입의 변수를 받을 수 있지만
// T 타입의 변수를 반환하는 것은 불가능하다.
open class WriteOnlyBox<in T>(private var item: T){
    fun setItem(newItem: T){
        item = newItem
    }
    // private 함수로는 get()이 가능하다.
    private fun getItem(): T = item
    // Type parameter T is declared as 'in' but occurs in 'out' position in type T
//    fun getItem(): T = item
}

interface Boxes<out T>{

}

interface SubBoxes<T> : Boxes<T>{

}

/// 공변성에 따른 자료형 제한하기
open class Animal(val size: Int){
    fun feed() = println("Feeding...")
}
class Cat(val jump: Int) : Animal(50)
class Spider(val poison: Boolean) : Animal(1)

// 형식 매개변수 제한
class AnimalBox<out T: Animal>(val element: T){
    fun getAnimal(): T = element
}

fun main(){
    val c1: Cat = Cat(10)
    val s1: Spider = Spider(true)

    var a1: Animal = c1 // 클래스의 상위 자료형으로 변환하는 것은 아무 문제 없음
    a1 = s1 // 객체 변환
    println("a1.size = ${a1.size}")

    val c2: AnimalBox<Animal> = AnimalBox<Cat>(Cat(10)) // 공변성 Cat은 Animal의 자료형
    println("c2.element.size = ${c2.element.size}")


}