package etc.study.function

// https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%9D%98-apply-with-let-also-run-%EC%9D%80-%EC%96%B8%EC%A0%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B0%80-4a517292df29
// Do It - 10장 표준 함수와 파일 입출력 p.454

data class Person(
    var name:String?,
    var age: Int?
)

fun getPerson(): Person {
    return Person("Kim", 20)
}

fun main(){
    val person:Person = getPerson()

    /*
    let -> 수신 객체(제네릭)의 확장 함수 형태,
           매개변수로 람다형식의 block있고 T를 받아 R을 반환
           객체 T를 it으로 가리킴
           반환 값은 block의 실행 결과를 반환한다.
           eg. 변수 지정 후 할당을 겍체.let{set(it)} 으로 사용 가능하다.
     */
    person.let { println("${it.name}: ${it.age}") }
    var nameIs:String = person.let{it.name+" Mose"}
    println("My name is $nameIs")
    person.name = null
//    person.age = null
    person.name?.let{println("${person.name}")} ?: person.age?.let { println("${person.age}") } ?: println(" 디 널이얌 /...")

    /*

    also -> 수신 객체에 확장 함수로 사용
            수신 객체 람다가 전달된 수신 객체를 전혀 사용하지 않거나,
            수신 객체의 속성을 변경하지 않고 사용하는 경우 also를 사용함
            also는 코드 블럭 내 수행 결과와 상관 없이 수신 객체를 반환함
            eg. let과 also를 혼용해 mkdir()하기
                fun makeDir(path:String) = path.let{File(it)}.also{it.mkdir}
                let 에서 File 객체를 also 에 넘기고 반환하여 파일을 만든다.
     */
    var p2 = person.also {
        println("${it.name}: ${it.age}")
        it?.name + "changing"
        it?.age?.plus(100)
        // it은 생략할 수 없다.
    }
    println("${person.name}: ${person.age}")
    println(person)
    println(p2)

    /*
    apply -> 수신 객체를 받아 수신객체를 리턴한다.
             사용하는 내부 블록은 수신 객체의 값을 수정하는 용도로만 쓰인다.
            eg. 레이아웃을 초기화 할 떄 사용
                layout.apply{
                    gravity = Gravity.CENTER
                    weight = 1f
                    topMargin = 100
                }
            eg.2 파일 만들기
                 File(path).apply{mkdir()}
     */
    person.apply {
        this.name = "Bartholomew"
        age = 10 // this는 생략할 수 있다.
    }
    println("${person.name}: ${person.age}")

    /*
    run -> 값을 계산하거나, 어러개의 지역 변수의 범위를 제한하려면 run을 사용
           매개 변수로 전달된 명시적 수긴 객체를 암시적 수신 객체로 변환할 때 사용
           인자가 없는 익명 함수처럼 동작하는 형태와 객체에서 호출하는 형태, 2가지로 사용 가능
    public inline fun <R> run(block: ()-> R): R = return block()
    public inline fun <T,R> T.run(block: T.( )-> R): R = return blkco()

     */
    val person2 = run{
        getPerson() // 마지막 표현식이 반환됨
    }
    println(person2)
    person2.run{println("${person2.name}: ${person2.age}")}
    /*
    with -> 수신 객체를 받고, 블럭 내부에서 수신 객체를 해부???하여 사용
            반환 값이 없다.
    inline fun <T, R> with(receiver: T, block: T.() -> R): R {
        return receiver.block()
    }
    */
    person.name = "Albert"
    person.age = 23
    with(person){
        println("$name: $age")
    }

    // 함수 생성 후 실행해 보기
    fun <T, R> withMe(receiver: T, block:T.()->R):R = receiver.block()
    withMe(person2){
        print(name)
    }

}