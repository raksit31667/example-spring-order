package com.raksit.example.order

import com.raksit.example.order.PerformanceTestConfiguration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FindOrderByIdSimulation extends Simulation {

  private val testDataFeed = csv("orderIds.csv").circular

  private val requestPerSeconds = Integer.getInteger("requests.per.seconds", 1)
  private val duration = Integer.getInteger("requests.duration", 100)

  private val findOrderByIdScenarios = scenario("Find Order by ID")
    .feed(testDataFeed)
    .exec(http("find order ${orderId}")
      .get("/orders/${orderId}")
    )
  setUp(
    findOrderByIdScenarios.inject(
      constantUsersPerSec(requestPerSeconds.toDouble) during (duration seconds)
    )
  ).protocols(httpConfiguration)
    .assertions(
      global.successfulRequests.percent.gte(99),
      global.responseTime.mean.lt(500),
      global.responseTime.percentile1.lt(500),
      global.responseTime.percentile2.lt(800),
      global.responseTime.percentile3.lt(1000),
    )
}
