package doIt.chapter06.section2

/*
지연초기화 - lateinit
lateinit 키워드로 값을 아예 할당하지 않은 상태로 프로퍼티를 생성함
코드가 실행되기 전까지는 값이 할당되어야함
제한
    1. var로 선언된 프로퍼티만 가능하다 (val은 수정불가기 떄문에 안됨)
    2. 프로퍼티에 대한 개터와 세터를 사용할 수 없다.

지연초기화 - lazy
val에서 사용가능한 지연초기화 방식
특징
    1. 호출 시점에 by lazy{} 정의에 의해 블록 부분이 초기화를 진행한다. (setter 처럼 사용)
    2. 불변의 변수 선언인 val에서만 사용 가능하다
    3. val이므로 값을 다시 변경 불가
초기화 된 이후에는 불변값으로 다시 변경 불가

Lazy로 객체 초기화
코드가 생성되는 시점이 아니라 코드의 접근 시점에 값이 초기화됨

lazy의 모드 3가지
    1. SYNCHRONIZED : lock을 사용해 단일 스레드만 사용하는 것을 보장
    2. PUBLICATION : 여러 군데에서 호출될 수 있으나 처음 초기화된 후 반환값을 사용
    3. NONE : lock을 사용하지 않기 때문에 빠르지만 다중 스레드가 접근할 수 있다.
    다른 모드에서 사용하고 싶다면 by lazy(모드 이름){ ... }을 사용
    항상 단일스레드가 보장된다면 LazyThreadSafetyMode.None 사용 권장,
    따로 동기화 기법을 사용하지 않는다면 다른 모드는 사용 권장하지 않음

    """
    단일 스레드 사용의 보장이란?
    하나의 코드 흐름에 해당 데이터를 접근하기 떄문에 다른 코드에 의해 변경되지 않음을 보장하는 것.
    보통 프로그램은 단일 루틴에서 수행되고 여러 개의 스레디가 동시에 수행되는 경우가 많기 때문에
    특정 자원 사용할 때 다른 스레드에 의해 값이 변경될 수 있음.
    따라서 이것을 보호하기 위해 lock을 사용하는 synchronized(){} 블록을 사용함
    """
 */

fun main() {

    // Lazy 테스트
    val lazyTest = LazyTest()
    lazyTest.flow()
    println("\n")

    // Lazy 객체 초기화
    var isPersonInstantiated = false // 객체 초기화 확인 값
    val person: Person by lazy { // 객체 지연 초기화
        isPersonInstantiated = true
        Person("Kim", 23) // Lazy 객체로 반환됨
    }
    val personDelegate = lazy{Person("Hong", 40)}
    println("person Init : $isPersonInstantiated")
    println("personDelegate Init : ${personDelegate.isInitialized()}") // Lazy 인터페이스에서 제공하는 함수 사용
    // 초기화 되는 시점
    println("person.name : ${person.name}")
    println("personDelegate.value.name : ${personDelegate.value.name}")
    println("person Init : $isPersonInstantiated")
    println("personDelegate Init : ${personDelegate.isInitialized()}")



}

class LazyTest{
    init {
        println("init block")
    }

    private val subject by lazy{
        println("lazy initialized")
        "Kotlin Programming"
    }

    fun flow(){
        println("not initialized") // 아직 초기화 안됨.
        // 호출시점에서 초기화 된다. 초기화 된 후에 값을 전달하기 떄문에 subject 안에 println문이 먼저 실행
        println("subject one $subject")
        println("subject two $subject")
    }
}

class Person(val name:String, val age:Int)