package doIt.chapter10.section2

/*
DSL을 이용한 사례
Spring 프레임워크
Spring은 오픈소스로 개발되었으며 의존성 주입과 엔터프라이즈 개발을 편하게 해 주는 경량급 에플리케이션 프레임워크이다.
SpringFrameWork 5.0 버전에서는 코틀린 확장을 지원하며 DSL을 이용해 웹 프레임워크를 쉽게 다룰 수 있다.
자세한 내용 - https://docs.spring.io/spring-framework/docs/current/reference/html/

Spark 프레임워크
SparkFrameWork는 원래 자바의 웹 프레임워크로 시작했지만 코틀린의 DSL을 추가했으며,
REST API를 사용해 웹 애플리케이션을 손쉽게 만들 수 있다.
eg.
val userDao = UserDao()
path("/users"){
    get("") { req, res ->
        jacksonObjectMapper().writeValueAsString(userDao.users)
    }
    get("/:id"){ req, res ->
        userDao.findById(req.params("id").toInt())
    }
    ....
}
DSL 이용해서 REST API의 get/post를 처리한다.
자세한 내용 - https://github.com/perwendel/spark-kotlin

Ktor 프레임워크
KtorFrameWork는 젯브레인즈에서 개발된 프레임워크로 HTTP 요청 처리를 위한 파이프라인 기능과 비동기 프로그래밍 모델을 제공한다.
코틀린의 코루틴과 DSL을 사용하고 대부분의 API가 람다식으로 구성되어 있다.
eg.
fun main(){
    val server = embeddeServer(Netty, port=8800)
    routing{
        get("/"){
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        get("/demo"){
            call.respondText("HELLO WORLD!")
        }
    }
    server.start(wait=true)
}
이 코드에서 서버를 구동하기 위해 DSL이 사용되고 있다.

DSL을 통해 기존의 복잡한 표현을 단순화하고 읽기 좋은 코드로 작성할 수 있기 떄문에 다양한 프레임워크에서 이용되고 있다.
 */