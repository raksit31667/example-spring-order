package com.raksit.example.order

import com.raksit.example.order.PerformanceTestConfiguration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FindOrdersBySourceSimulation extends Simulation {

  private val testDataFeed = csv("orderSources.csv").circular

  private val rampUpFrom = Integer.getInteger("requests.rampup.from", 10)
  private val rampUpTo = Integer.getInteger("requests.rampup.to", 20)
  private val duration = Integer.getInteger("requests.duration", 100)

  private val findOrdersBySourceScenario = scenario("Find Orders by Source")
    .feed(testDataFeed)
    .exec(http("find orders by source ${orderSource}")
      .get("/orders?source=${orderSource}")
    )
  setUp(
    findOrdersBySourceScenario.inject(
      rampUsersPerSec(rampUpFrom.toDouble) to rampUpTo.toDouble during(duration seconds)
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
