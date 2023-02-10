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

        flux.groupBy { num ->
            when {
                num % 2 == 0 -> "Even"
                else -> "Odd"
            }
        }.subscribe { groupFlux ->
            groupFlux.subscribe { print("$it ") }
            println()
        }
    }

    @Test
    fun mapTest() {
        val flux = Flux.fromIterable(listOf(1, 2, 3, 4, 5))

        flux.map { i -> i + 10 }
            .subscribe { data -> println(data) }
    }
}
