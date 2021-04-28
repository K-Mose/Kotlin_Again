package doIt.chapter07.section2

/*
https://kotlinlang.org/docs/annotations.html#usage - 예제 참고
애노테이션 클래스 - Annotation Class
애노테이션은 코드에 부가적인 정보를 추가하는 기법이다.
@ 기호와 함께 나타내며, @Test는 유닛 테스트, @JvmStatic은 자바 코드에서 컴패니언 객체 접근 등 다양하다.

애노테이션 선언하기
annotation class [애노테이션 이름]
애노테이션 클래스를 선언하기만 하면 @기호를 붙여서 다음과 같이 사용 가능하다.

애노테이션은 다음과 같은 속서으로 사용해 정의된다.
    1. @Target: 애노테이션이 지정되어 사용할 종류(클래스, 함수, 프로퍼티 등)를 정의
        eg. @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION)
    2. @Retention: 애노테이션을 컴파일된 클래스 파일에 저장할 것인지 실행할 것인지 실행 시간에 반영할 것인지 결정
        eg. Retention(AnnotationRetention.SOURCE)
        Enum Constant Detail 참고 (자바와 조금 다름) - https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-annotation-retention/
        SOURCE - 컴파일 시 애노테이션 제거
        BINARY - 클래스 파일에 포함되지만 리플렉션에 의해 나타나지 않음
        RUNTIME - 클래스 파일에 저장되고 리플렉션에 의해 나타남
        """
        리플렉션이란?
        프로그램을 실행할 때 프로그램의 특정 구저를 분석해 내는 기법으로 사용된다.
        예) 함수를 정의하는데 매개변수로 클래스 타입을 선언하고 실행할 때 매개변수로 전달된 클래스의 이름, 메서드나 프로퍼티를 알아내는 작업
        """
    3. @Repeatable: 애노테이션을 같은 요소에 여려 번 사용 가능하게 할지를 결정
    4. @MustBeDocumented: 애노테이션이 API의 일부분으로 문서화하기 위해 사용

실행 시간에 클래스를 분석하려면 클래스에 대한 정보를 표현하는 클래스 레퍼런스로부터 알아낸다.
따라서 특정 클래스의 정보를 분석하기 위해 클래스 타입인 KClass<*>로 정의하고,
클래스 레퍼런스는 [클래스 이름::class]와 같은 형태로 표현할 수 있다.
https://kotlinlang.org/docs/reflection.html#class-references
-> ReflectionTEST로 이동

애노테이션의 위치
애노테이션은 클래스, 메서드, 프로퍼티나 반환 값 앞에 표시할 수 있다.
반환 값에 표시할 때는 같이 소괄호로 묶어준다.
생성자 앞에 애노테이션을 사용한다면 constructor 키워드를 생략할 수 없다.
프로퍼티안에 게터와 세터에도 사용 가능하다

애노테이션의 매개변수와 생성자
애노테이션에 매개변수를 지정하고자 하려면 다음과 같이 파라메터를 받는다.
annotation class Special(val why: String)
@Special("example") class Foo {}
매개변수로 허용되는 타입
    1. 자바 기본형과 연동하는 자료형
    2. 문자형
    3. 클래스
    4. 열거형
    5. 기타 애노테이션
    6. 위의 목록을 가지는 배열
JVM에서 null을 애노테이션의 속성으로 지원하지 않아 애노테이션 매개변수는 nullable을 할 수 없다.
만약 애노테이션이 다른 애노테이션의 매개변수로 들어갔다면 @기호를 사용하지 않아도 된다.
특정한 클래스를 애노테이션의 매개변수로 필요로 한다면, Kotlin class(KClass)를 사용하면 된다.
코틀린 컴파일러가 자동적으로 자바 클래스로 변환시켜준다.
그래서 자바 코드에서 애노테이션과 매개변수를 정상적으로 접근 가능하다.

람다 *
애노테이션은 람다로 사용할 수 있다.
람다의 바디가 생서되었을 때, invoke() 메소드가 그 안에 적용된다.
동시 실행 컨트롤(concurrency control) 애노테이션을 사용하는 Quasar 프레임워크에 유용하게 쓰인다.
eg. annotation class Suspendable
    val f = @Suspendable { Fiber.sleep(10) }

표준 애노테이션
    1. @JvmName("filter"): filter()라는 이름을 자바에서 각각
       filterString()과 filterInts()로 바꿔준다.
    2. @JvmStatic: 자바의 정적 메서드로 생성하게 해줌
    3. @Throw: 코틀린의 throw 구문이 자바에서도 포함되게 해줌
    4. @JvmOverloads: 코틀린에서 기본값을 적용한 인자에 함수를 모두 오버로딩해 준다.
    -> 표준 애노테이션은 자바와 원할하게 연동하는데 목적을 둔다.
 */

// 선언 및 사용
annotation class Fancy
@Fancy class MyClass{}



// 리플렉션 테스트
