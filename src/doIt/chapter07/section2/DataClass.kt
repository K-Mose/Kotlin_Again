package doIt.chapter07.section2

/*
https://kotlinlang.org/docs/data-classes.html

데이터 클래스 - Data class
데이터 객체를 전달하기 위한 클래스
데이터 클래스는 하위 항목을 내부적으로 자동으로 생성한다.
    1. 프로퍼티의 게터/세터
    2. 비교를 위한 equal(), 키 사용을 위한 hashCode()
    3. 프로퍼티를 문자열로 변환해 순서대로 보여주는 toString()
    4. 객체 복사를 위한 copy()
    5. 프로퍼티에 상응하는 componentN()

데이터 클래스는 다음 조건을 만족해야 한다.
    1. 주 생성자는 최소한 하나의 매개변수를 가져야 한다.
    2. 주 생성자의 모든 매개변수는 val, var로 지정된 프로퍼티여야 한다.
    3. 데이터 클래스는 abstract, open, sealed, inner 키워드를 사용할 수 없다.
    -> 오직 데이터를 기술하는 용도로만 사용한다. 부생성자나 init 블록으로 간단한 로직을 포함 할 수 있다.

객체 디스트럭처링하기 (파이썬과 사용이 비슷하다)
디스트럭처링(Destructuring)한다는 것은 객체가 가지고 있는 프로퍼티를 개별 변수로 분해하여 할당하는 것을 말함
언더스코어(_)를 사용해서 특정 프로퍼티는 제외할 수 있다.
개별적으로 프로퍼티를 가져오기 위해 componentN()을 사용할 수 있다.
 */

fun main(){
    val cus1 = Customer("Mose", "KMS@email.com")
    val cus2 = Customer("Mose", "KMS@email.com")
    val cus3 = cus1.copy(name="Kim Mose") // 특정 프로퍼티만 변경한 체 복사할 수 있다.
    println(cus1 == cus2)
    println(cus2.equals(cus3)) // == 사용을 권장한다.
    println("${cus1.hashCode()} ${cus2.hashCode()} ${cus3.hashCode()}")
    println('\n')
    // 디스터럭팅
    val (name1, email2) = cus1
    println("$name1 $email2")
    println("${cus3.component1()} ${cus3.component2()}")
    println('\n')
    // 람다식을 이용한 디스트럭팅
    val lambdaD = {
        (name, email):Customer ->
        println(name)
        println(email)
    }
    lambdaD(cus2)
}

data class Customer(var name: String, var email: String){
    var job: String = "Unknown"
    // 부생성자
    constructor(name: String, email: String, _job: String): this(name, email){
        job = _job
    }

    override fun toString(): String {
        println("String")
        return super.toString()
    }

    init{
        println("손님 추가")
    }
}