package doIt.chapter07.section2

/*
봉인된 Sealed class
실드 클래스는 미리 만들어 놓은 자료형들을 묶어서 제공하기 때문에 어떤 의미에서는 Enum 클래스의 확장으로 불 수 있다.
실드 클래스 자체는 추상 클래스와 같기 떄문에 객체를 만들 수 없다.
생성자도 private로만 작성 가능며, 같은 파일 내에서만 상속이 가능하다.
블록 안에 생성되는 클래스는 상속이 필요한 경우 open으로 선언될 수 있다.

실드 클래스는 특정 객체에 자료형에 따라 when문과 is에 의해 선택적으로 실행 가능하다.
필요한 경우의 수를 내부 클래스 수 만큼 사용할 수 있다.
모든 경우가 열거되었으면 else를 사용할 필요가 없다.

열거형 Enum class
열거 클래스는 여러 개의 상수를 선언하고 열거된 값을 조건에 따라 선택하는 특수한 클래스.
열거형 클래스는 자료형이 동일한 상수들만 나열 가능하다.
클래스 내 각 상수들은 클래스의 객체로 취급되고 쉼표로 구분된다.

필요한 경우 메서드를 포함할 수 있고 세미콜론을 사용해서 열거형 끝을 알린다.
열거형의 기본 멤버는 name, toString, ordinal 등이 있다.

 */

fun main(){
    val result = Result.Success("Good!")
    val message = eval(result)
    println(message)
    println(eval(Result.Error(1, "Error Message")))
    println(eval(Inside())) // Inside도 Success를 상속하기 떄문에 when에서 Success이 선택된다.
    println('\n')

    val direction = Direction.EAST
    println(direction)
    println(direction.ordinal) // 위치를 가지는 숫자 ordinal
    println(Direction.NORTH.ordinal)
    println(direction is Direction) // 클래스의 객체로 취급됨
    println()
    val day = DayOfWeek.SATURDAY
    when(day.num){
        in 1 .. 5 -> println("Weekday")
        else -> println("Weekend!")
    }

    println(Color.RED.rgb())
    println(Color.GREEN.rgb())
    println(Color.BLUE.rgb())
    println(getColor(Color.YELLOW))
    println(getColor(Color.BLUE))
    println(getColor(Color.RED))
    println(getColor(Color.GREEN))
    println(getColor(Color.VIOLET))

    var score: Int = 100
    when(score){
        in MemberType.BRONZE.getScore() -> println("${MemberType.BRONZE.prio} Class")
        in MemberType.GOLD.getScore() -> println("${MemberType.SILVER.prio} Class")
        in MemberType.SILVER.getScore() -> println("${MemberType.GOLD.prio} Class")
        else -> println("UnRanked")
    }
    for (value in MemberType.values()){
        println(value is MemberType) // 객체를 가져옴
    }
}
fun eval(result: Result): String = when(result){
    is Status -> "in progress"
    is Result.Success -> result.message
    is Result.Error -> result.message
    // Result를 상속하는 모든 클래스가 들어있어서 else가 필요 없다.
}
// 실드 클래스를 선언하느 두 가지 방법
sealed class Result{
    open class Success(val message: String) : Result()
    class Error(val code: Int, val message: String) : Result()
}
class Status : Result() // 상속은 같은 파일 내에서만 가능
class Inside : Result.Success("Status") // 내부 클래스 상속
//class Inside2 : Result.Error() // open이 아니라 상속이 안된다.

sealed class Result2
open class Success2(val message: String) : Result2()
class Error2(val code: Int, val message: String) : Result2()
class Status2 : Result2()
class Inside2 : Success2("Status2") // 내부 클래스를 상속할 떄 점(.) 표기법을 사용하지 않고 그냥 한다.

// 열거형 클래스
enum class Direction{
    NORTH, SOUTH, WEST, EAST
}

enum class DayOfWeek(val num: Int){
    SUNDAY(0), MONDAY(1), TUESDAY(2), WENDESDAY(3), THURSDAY(4),
    FRIDAY(5), SATURDAY(6)
}

enum class Color(val r: Int, val g: Int, val b: Int){
    RED(255,0,0), ORANGE(255, 165, 0), YELLOW(255,255,0),
    GREEN(0,255,0), BLUE(0,0,255),
    INDIGO(75,0,130), VIOLET(238,130,238);
    fun rgb() = (r*256+g)*256+b
}

fun getColor(color: Color) = when(color){
    Color.RED -> color.name
    Color.GREEN -> color.ordinal
    Color.BLUE -> color.toString()
    Color.YELLOW -> color
    else -> color.rgb()
}

interface Score{
    fun getScore(): IntRange
}

enum class MemberType(var prio: String) : Score{
    BRONZE("Third"){
        override fun getScore(): IntRange {
            return 0 .. 100
        }
    },
    SILVER("Second"){
        override fun getScore(): IntRange {
            return 101 .. 200
        }
    },
    GOLD("First"){
        override fun getScore(): IntRange {
            return 201 .. 300
        }
    }
}