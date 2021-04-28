package doIt.chapter07.section2

import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KClass
// https://kotlinlang.org/docs/reflection.html

class User(val id: Int, val name: String, var grade: String = "Normal"){
    fun check(){
        if (grade == "Normal") println("You need to get the Silver grade")
    }
}

fun main(){
    val classInfo = User::class
    // 타입 출력
    println(classInfo) // 클래스의 레퍼런스 위해 ::class 사용
    println(classInfo.java)
    // 클래스의 프로퍼티와 메서드의 정보 출력
    classInfo.memberProperties.forEach{
        println("Property name: ${it.name}, type: ${it.returnType}")
    }
    classInfo.memberFunctions.forEach {
        println("Function name: ${it.name}, type: ${it.returnType}")
    }
    getKotlinType(classInfo)
}

fun getKotlinType(obj: KClass<*>){
    println(obj.qualifiedName)
}