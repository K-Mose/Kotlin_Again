package doIt.chapter09.section4

/*
요소의 처리와 집계
요소의 순환
forEach, forEachIndexed, onEach
forEach - 순회 후 종료, 자료형 객체를 순회
forEach - 순회할 떄 객체와 인덱스가 포함됨
onEach - 순회 후 컬렉션을 반환

요소의 개수 반환 - count
최댓값과 최솟값 반환 - max, min, maxByOrNull, minByOrNull

각 요소의 정해진 식 적용 (scala와 같다)
fold, reduce
초깃값과 정해진 식에 따라 요소에서 치리하기 위해 fold와 reduce를 사용함
fold - 초깃값과 정해진 식에 따라 처음 요소부터 끝 요소에 적용해 값을 반환
reduce - fold와 동일하지만 초기값을 사용하지 않음(초기값은 0번 인덱스값이라 볼 수 있음)

요소의 검사
all, any - 일치 여부
contains, containsAll - 포함 되어있는지
none, isEmpty - 비어있는지

요소의 필터와 추출
특정 요소 골라내기
filter - 컬렉션을 it으로 받아 조건에 맞는 값만 추출
map - key, value로 추출

특정 범위를 잘래나거나 반환하기
slice - 특정 범위의 인덱스를 가진 List를 인자로 사용해서 기존 List에서 요소들을 잘라냄
take - n개의 요소를 가진 List 반환
drop - take와 반대로 처음부터 n개의 요소를 제외하고 반환

각 요소의 반환
componentN()을 사용, data class와 동일

합집합과 교집합
distinct - 중복 요소 제거 후 반환
intersect - 두 컬렉션 중 겹치는 요소만 반환

요소의 매핑
.map - 주어진 요소에 일괄적으로 값을 적용해 컬렉션을 리턴함. forEach와 달리 주 컬렉션을 건드리지 않음
flatMap - 각 요소에 식을 적용한 후 다시 하나로 합쳐 새로운 컬렉션을 반환
groupBy - 주어진 식에 따라 요소를 그룹화하고 map으로 반환

요소 처리와 검색
elementAt - 위치 값 반환, 인덱스가 넘어가면 오류
elementAtOrElse - 인덱스 벗어나도 식에 따라 결과 반환
elementAtOrNull - 인덱스 벗어나면 null 반환
first - 식에 일치하는 첫 번째 값 반환
firstOrNull - 일치 값 없으면 null 반환
last - 식에 일치하는 마지막 값 반환
lastOrNull - 없으면 null 반환
indexOfFirst - 람다식에서 일치하는 해당 요소중 첫 번째 인덱스의 값을 반환
lastIndexOf - 인자에 지정된 요소에 대한 마지막 인덱스 반환
indexOfLast - 람다식에 일치하는 요소 중 마지막 인덱스 값 반환
single - 해당 조건식에 일치하는 요소를 하나 반환, 일치하는 요소가 하나 보다 많으면 오류 발생
singleOrNull - 없거나 하나 보다 많으면 null 반환

컬렉션의 분리와 병합
union - 중복 요소를 하나만 허용하여 반환, 컬렉션은 Set
plus - 중복 요소를 포함하여 반환, 컬렉션은 List
zip - 두 개의 컬렉션을 동일한 인덱스끼리 Pair를 만들어 반환

순서와 정렬
reversed - 요소의 순서를 거꾸로 해서 반환
나머지는 배열과 같음
 */
val list = listOf(1,2,3,4,5,6)
val listRepeated = listOf(2,2,5,5,7,7)
val listPair = listOf(Pair("A", 300),Pair("B", 200),Pair("C", 100))
val listMixed = listOf(1, "이", null, "사", 5)
val map = mapOf(11 to "Java", 22 to "Kotlin", 33 to "C++")

fun main(){
//    each()
//    foldReduce()
//    extensionCheck()
//    extensionFilter()
//    extensionSlicing()
//    compoNDistInter()
//     mapping()
    search()

}

fun search() {
    println(list.elementAt(4))
    println(list.elementAtOrElse(1) {it + 1})
    println(list.elementAtOrNull(10))
    println(list.firstOrNull{ it > 10})
    println(list.lastOrNull{ it < 4})

}

fun mapping() {
    println(list.map { it * it })
    println(list.mapIndexed{index, i -> index * i })
    println(listMixed.mapNotNull { it })
    println(list.flatMap { listOf(it,'+') })
    println(listOf("abc","2456").flatMap { it.toList() })
    println(list.groupBy { if(it%2==0) "even" else "odd" })
}

fun each(){
    list.forEach { print("$it ") }
    println()
    list.forEachIndexed{ i, l -> print("[index : $i / value : $l]") }
    println()
    val list2 = list.onEach { print("${list.indexOf(it)} / $it  ") }
    println("\n$list2")
}

fun foldReduce(){
    // 첫 인자는 계속 누적된 값이고, 두 번째 인자는 list의 값이다.
    println("fold")
    list.fold(4) {acc: Int, i: Int ->  println("$acc + $i = ${acc+i}")
        acc + i}
    println("reduce")
    list.reduce  {acc: Int, i: Int ->  println("$acc + $i = ${acc+i}")
        acc + i}
}

fun extensionCheck(){
    // 요소의 일치 여부 검사하기
    // all - 모든 요소가 일치해야 true
    println(list.all{it < 10})
    println(list.all{it % 2 == 0})
    // any - 요소가 하나라도 일치하면 true
    println(list.any{it < 1})
    println(list.any{it % 2 == 0})
    println()
    // 특정 요소의 포함 및 존재 여부
    println(list.contains(2))
    println(10 in list)
    println(list.containsAll(listOf(1,2,3)))
    println()
    // 비어있는지 검사
    println(list.none()) // 요소가 없으면
    println(emptyList<Int>().none())
    println(list.isEmpty()) // 컬렉션이 비어있으면
    println(emptyList<Int>().isEmpty())
}

fun extensionFilter(){
    // 특정 요소 골라내기
    println(list.filter{it%2==0})
    println(list.filterNot{it%2==0})
    println(listMixed.filterNotNull())
    println()
    // 인덱스 추출
    println(list.filterIndexed{ idx, value ->
        idx != 1 && value % 2 == 0
    })
    println(list.filterIndexedTo(mutableListOf()){ idx, value ->
        idx != 1 && value % 2 == 0
    })
}

fun extensionSlicing(){
    // slice
    println(list)
    println(list.slice(listOf(2,3,4))) // 인덱스가 2,3,4인 것들
    println()
    // take
    println(list.take(3))
    println(list.takeLast(3))
    println(list.takeWhile { it < 3 || it > 5 })
    println()
    // drop : take와 반대
    println(list.drop(3))
    println(list.dropLast(3))
    println(list.dropWhile { it < 3 || it > 5 })
}

fun compoNDistInter(){
    println(list.component5())
    println(listPair.component1())
    println(listRepeated.distinct())
    println(listRepeated.intersect(list))
}

