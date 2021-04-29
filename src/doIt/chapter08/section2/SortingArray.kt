package doIt.chapter08.section2

import java.util.*

/*
배열 정렬하기
배열에는 2가지 방법과 2가지 종류가 있다.
방법
    1. 정열된 새로운 배열을 반환
    2. 원본 배열에 대한 정렬
종류
    1. 오름차순 Ascending
    2. 내림차순 Descending
1-1 sortedArray() : 오름차순으로 정렬된 새로운 배열을 반환
1-2 sortedArrayDescending() : 내림차순으로 정렬된 새로운 배열을 반환
2-1 sort() : 원본 배열을 오름차순으로 정렬
2-2 sortDescending() : 원본 배열을 내림차순으로 정렬

sortBy()로 데이터 클래스 정렬하기
sortBy()를 사용하면 data class의 멤버 변수에 따라 정렬할 수 있다.

sortWith() 비교자로 정렬하기
주어진 비교자(comparator)에 의해 정렬하는 방법
public fun <T> Array<out T>.sortWith(comparator: Comparator<in T>): Unit
sortWith()은 Comparator를 매개변수로 가지고 있다.
Comparator는 자바의 인터페이스로서 2개의 객체를 비교하는 compare()를 구현한다.
예제 arraySortWith() 참고
compareBy()를 사용하면 두 개의 프로퍼티를 기준으로 정렬할 수 있다.

배열 필터링하기
filter() 메서드로 원하는 데이터만 고른다.
minBy 또는 maxBy를 사용해 최소 혹은 최대값만 뽑을 수 있다.
(mxxByOrNull 사용 권장)
 */

fun main() {
    // basic sorts
//    basicArraySorting()

    // sortBy()
    arraySortedClass()

    // sortWith()
//    arraySortWith()
//    arraySortWithCompareBy()

    // filter
//    filterArray()
//    sortByMap()

}


fun basicArraySorting(){
    val arr = arrayOf(8,4,3,2,6,9,1)
    println("ARR : " + arr.contentToString())
    val sortedNum = arr.sortedArray()
    println("ASC : " + sortedNum.contentToString())

    val sortedNumDesc = arr.sortedArrayDescending()
    println("DEC : " + sortedNumDesc.contentToString())

    arr.sort(1,3) // sort(fromIndex, toIndex)
    println("ORI : " + arr.contentToString())
    arr.sortDescending()
    println("ORI : " + arr.contentToString())

    // LIST로 반환
    val listSorted: List<Int> = arr.sorted()
    val listSortedDesc: List<Int> = arr.sortedDescending()
    println("LST : $listSorted")
    println("LST : $listSortedDesc")

}

data class Product(val name:String, val price: Double)
fun arraySortedClass(){

    val products = arrayOf(
        Product("Pencil", 2.0),
        Product("Drone", 1020.99),
        Product("Smart Phone", 899.99),
        Product("Mouse", 30.12),
        Product("Key Board", 22.10),
        Product("Monitor", 159.90),
        Product("Tablet", 300.00),
        Product("Snow Ball", 80.12),
        Product("Cola", 1.2)
    )

    products.let {
        it.sortBy { item -> item.price }
        it.forEach { item -> println(item)}
        println()
        it.sortBy { item -> item.name }
        it.forEach { item -> println(item)}
        println()
        println(it.maxByOrNull { p -> p.price })
        println(it.minByOrNull { p -> p.price })
    }
}

fun arraySortWith(){
    val products = arrayOf(
        Product("Pencil", 2.0),
        Product("Drone", 1020.99),
        Product("Smart Phone", 899.99),
        Product("Mouse", 30.12),
        Product("Key Board", 22.10),
        Product("Monitor", 159.90),
        Product("Tablet", 300.00),
        Product("Snow Ball", 80.12),
        Product("Cola", 1.2)
    )

    products.sortWith(
        Comparator<Product>{ p1, p2 ->
            when {
                p1.price > p2.price -> 1
                p1.price < p2.price -> -1
                else -> 0
            }
        }
    )
    products.forEach { println(it) }
}

fun arraySortWithCompareBy(){
    val products = arrayOf(
        Product("Mouse", 22.10),
        Product("Key Board", 22.10),
        Product("Monitor", 159.90),
        Product("Monitor", 200.00)
    )
    products.let{ p ->
        // 이름순으로 정렬 후 가격순으로 정렬
        p.sortWith(compareBy({it.name}, {it.price}))
        p.forEach { println(it) }
        println()
        // 가격순으로 정렬 후 이름순으로 정렬
        p.sortWith(compareBy({it.price}, {it.name}))
        p.forEach { println(it) }
    }
}

fun filterArray(){
    val arr = arrayOf(1, -2, -3, 4, 5, 0)
    arr.filter { e -> e > 0 }.forEach{ println("$it ")}
}

fun sortByMap(){
    val fruits = arrayOf("banana", "avocado", "apple", "kiwi")
    fruits.filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.toUpperCase()}
        .forEach{println(it)}
}