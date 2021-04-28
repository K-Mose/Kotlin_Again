package doIt.chapter07.section2

/*
https://kotlinlang.org/docs/annotations.html#usage - 좀 더 자세히 볼 것
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

 */

// 선언 및 사용
annotation class Fancy
@Fancy class MyClass{}



// 리플렉션 테스트
