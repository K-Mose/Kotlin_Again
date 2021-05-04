package doIt.chapter10.section2

/*
람다식과 DSL
DSL - Domain-specific Language
범용 언어와는 반대로, 특정 어플리케이션의 도메인을 위해 특화된 언어로 사용 목적에 따라 달라진다.
SQL이 대표적인 DSL.

코틀린의 람다식같은 특징을 잘 사용하면 개발에 집중할 수 있는 간략하고 좋은 코드를 만들 수 있다.

코틀린에서 DSL 사용하기
코틀린은 범용 언어의 목적을 가지고 있지만 앞에서 배운 람다식과 확장 함수 등의 개념을 적절히 활용하면 코틀린 안에서 DSL 형태의 언어를 만들 수 있다.

DSL 경험해 보기
고객의 정보를 구체화하는 DSL 예제
val person = person{
    name = "kim"
    age = 28
    job {
        category = "IT"
        position = "Android Developer"
        extension = 1234
    }
}
클래스간 객체선언 처럼 보이지만 범용적인 문법과는 다르다.
선언부의 키워드가 보이지 않고 변수와 값 형태만 남아 있어 읽기 쉽게 구성된 모습을 보여준다.
이것을 구성하기 위해 먼저 데이터 모들을 만들어야 한다. *아래 데이터 클래스 참조
data class로 Person과 Job의 프로퍼티가 정의되었다. 이 모데을 사용해 person() 함수를 만들어보자. *아래 함수 참조
person() 함수는 람다식을 매개변수로 가지고 Person 객체를 받는다. person(block: (Person)->Unit)
함수는 Person의 객체를 생성하고 람다식 블록에 p를 넘긴다.
그리고 p를 반환한다. 물론 반환 전에는 초기화 되어야 한다.
인자를 하나만 받기 때문에 it을 사용해서 객체를 가르킬 수 있다.
it을 제거하려면 람다식을 Person에서 받는 확장 함수형태로 바꿔야 한다.
person(block: Person.()->Unit)

Job에 대한 항목을 작성하기 위해 확장 함수를 추가한다.
fun Person.job(block: Job.()->Unit)
외부에서 받은 Job을 내부에서 생성하는 Job() 객체에 apply(block)하여
Person 객체 안에 있는 var job을 생성한다.

DSL을 구현하는 요소
DSL 만들기 위한 요소 * 세부내용 책 p.479 표 참고
기법
연산자 오버로딩
자료형 별칭
게터/세터
분해(디스트처링)
괄호 바깥의 람다식
확장 함수
중위(infix) 함수
수신자와 람다식
문맥 제어 @DslMarker
 */
fun main(){
    val person1 = person{
        it.name = "kim"
        it.age = 28
        it.job {
            category = "IT"
            position = "Android Developer"
            extension = 1234
        }
    }
    val person2 = pExtension{
        name = "kim"
        age = 28
        job {
            category = "IT"
            position = "Android Developer"
            extension = 1234
        }
    }
}

// data 클래스 만들기
data class Person(
    var name: String? = null,
    var age: Int? = null,
    var job: Job? = null
)
data class Job(
    var category: String? = null,
    var position: String? = null,
    var extension: Int? = null
)
// person 함수
fun person(block: (Person)->Unit): Person{
    val p = Person()
    block(p)
    return p
}
// 확장 함수로 변경 -> apply, run이 이 형태
fun pExtension(block: Person.()->Unit): Person{
    val p = Person()
    p.block()
    return p
}
// Job에 대한 확장 추가
fun Person.job(block: Job.()->Unit){ // Job1
    job = Job().apply(block) // Job2
    // Job1의 객체를 Job2에 apply 하고 Person 객체의 var job에 할당한다.
}