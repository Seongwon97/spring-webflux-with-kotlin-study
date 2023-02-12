package com.example.reactorstudy

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux

class FluxTest {

    @Test
    fun anyTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)

        flux.any { i -> i % 2 == 0 }
            .subscribe { println(it) }
        /**
         * any는 Flux안에 해당 조건에 만족하는 데이터가 존재하는지 확인하고
         * 만족하는 데이터가 있다면 True, 없다면 False를 반환한다.
         */
    }

    @Test
    fun reduceTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)

        flux.reduce { total, num -> total + num }
            .subscribe { println(it) }
        /**
         * reduce는 "줄이다"라는 뜻 이외에 "(어떤 물질에) 전자를 첨가하다"라는 뜻도 존재한다.
         * 해당 메서드에서는 두 번째 뜻을 의미한다.
         * 요소의 첫 번째 데이터를 초기 데이터로 지정하고 그 후의 첨가하여 연산을 진행한다.
         * 위의 예시는 1~5까지 더하는 연산이 되어 결과가 15가 나온다.
         */
    }

    @Test
    fun scanTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)

        flux.scan { total, num -> total + num }
            .subscribe { println(it) }
        /**
         * reduce와 같이 첫 번째 값을 기준으로 연산을 진횅한다.
         * 다만 reduce는 최종 결과만을 다음 Pipeline으로 넘겼다면
         * scan은 연산이 진행되며 발생한 모든 중간 결과들을 넘긴다.
         */
    }

    @Test
    fun concatTest() {
        val flux1 = Flux.range(1, 5)
        val flux2 = Flux.range(6, 5)
        val flux3 = Flux.range(11, 5)

        Flux.concat(flux1, flux2, flux3)
            .subscribe { println(it) }
        /**
         * concat은 이름에서 알 수 있듯이 flux들을 합쳐주는 역할을 한다.
         * 위의 예시에서는 3개의 flux를 합쳐서 1~15까지의 수가 출력된다.
         */
    }

    @Test
    fun bufferTest() {
        val flux = Flux.range(1, 20)

        flux.buffer(5)
            .subscribe { println(it) }
        /**
         * buffer는 Flux의 데이터들을 매개변수로 넣은 n개씩 묶어서 반환한다.
         */
    }

    @Test
    fun groupTest() {
        val flux = Flux.range(1, 20)

        flux.groupBy { it % 2 }
            .flatMap { group ->
                group.collectList()
                    .map { list -> Pair(group.key(), list) }
            }
            .subscribe { println("Key: ${it.first}, Values: ${it.second}") }
        /**
         * groupBy는 flux내의 값들을 주어진 조건에 따라 나누어 GroupedFlux<K, T>에 저장하게 된다.
         * 위의 예시에서는 데이터를 짝수, 홀수로 나누어 각각 GroupedFlux<K, T>에 나누어 저장한 것을 확인할 수 있다.
         */
    }

    @Test
    fun mapTest() {
        val flux = Flux.fromIterable(listOf(1, 2, 3, 4, 5))

        flux.map { i -> i + 10 }
            .subscribe { println(it) }
        /**
         * map은 flux 안에 있는 데이터들을 동기적인 작업을 통해 값을 변경해주는 동작을 한다.
         */
    }

    @Test
    fun flatMapTest() {
        val flux = Flux.range(1, 10)

        flux.flatMap { i ->
            Flux.just(i + 10)
        }.subscribe { println(it) }
        /**
         * flatMap은 flux안에 있는 element들을 비동기로 동작해 새로운 reactive stream을 만들어 반환한다.
         * 비동기로 동작하여 다른 API 또는 Repository의 메서드를 호출할 때 적합하다
         */
    }

    @Test
    fun flatMapSequentialTest() {
        val flux = Flux.range(1, 10)

        flux.flatMapSequential { i ->
            Flux.just("Element: $i")
//                .delayElements(Duration.ofMillis(100))
        }.subscribe { println(it) }
        /**
         * flatMap과 다르게 flatMapSequential는 동시에 작업을 처리하는 것이 아니라 순차적으로 작업한다.
         *
         */
    }

    @Test
    fun doOnEachTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)
            .concatWith(Flux.error(RuntimeException("에러 발생")))

        flux.doOnEach { println(it) }
            .subscribe()
        /**
         * sequence를 처리하는동안 doOnNext, doOnError, doOnComplete, doOnSubscribe, doOnRequest, doOnCancel가 실행된다.
         * doOnEach에 동작을 지정하면 이러한 동작들이 동작할 때 항상 동작하게 된다.
         */
    }

    @Test
    fun doOnNextTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)

        flux.doOnNext { println("Received: $it") }
            .subscribe { println("Processed: $it") }
        /**
         * doOnNext는 sequence에서 OnNext메서드가 호출될 때, 부가적으로 동작할 내용들을 지정할 때 사용한다.
         * 스트림을 수정하지 않고 내보낸 값에 대해 작업을 수행할 수 있기에 주로 로깅이나 디버깅을 목적으로 사용된다.
         */
    }

    @Test
    fun doOnCompleteTest() {
        val flux = Flux.just(1, 2, 3, 4, 5)

        flux.doOnComplete { println("Complete!") }
            .subscribe { println("Processed: $it") }
        /**
         * doOnComplete는 complete메서드가 호출되었을 때 동작하게 된다.
         */
    }

    @Test
    fun onErrorReturnTest() {
        val flux = Flux.just(1, 2, 0, 4, 5)
            .map { 10 / it }
            .onErrorReturn(-1)

        flux.subscribe({ println(it) },
            { println("Error: $it") })
        /**
         * webClient와 같이 외부 서비스에 연동할 때 문제가 발생하였을 때 동작할 핸들러를 지정할 수 있다.
         * onErrorReturn, onErrorResume, onErrorMap를 통해 새로운 반환값을 지정할 수도 있고, 새로운 동작, 또는 다른 flux로 변환하여 보낼 수 있다.
         *
         * 위의 예시에서는 각각의 flux의 element를 10으로 나누는데 0으로 나누어 에러가 발생할 경우 -1을 반환하는 것을 확인할 수 있다.(이후의 연산은 진행되지 않는다.)
         */
    }

    @Test
    fun onErrorResumeTest() {
        val flux = Flux.just(1, 2, 0, 4, 5)
            .map {
                if (it == 0) {
                    throw IllegalArgumentException("Cannot divide by zero")
                }
                10 / it
            }
            .onErrorResume { error ->
                if (error is IllegalArgumentException) {
                    Flux.just(-1)
                } else {
                    Flux.error(error)
                }
            }

        flux.subscribe({ println(it) }, { println("Error: $it") })
    }

    @Test
    fun onErrorMapTest() {
        val flux = Flux.just(1, 2, 0, 4, 5)
            .map {
                if (it == 0) {
                    throw IllegalArgumentException("Cannot divide by zero")
                }
                10 / it
            }
            .onErrorMap { error ->
                if (error is IllegalArgumentException) {
                    CustomException("Cannot divide by zero")
                } else {
                    error
                }
            }

        flux.subscribe({ println(it) }, { println("Error: $it") })
    }
}

class CustomException(s: String) : Throwable()
