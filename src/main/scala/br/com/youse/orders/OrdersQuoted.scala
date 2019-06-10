package br.com.youse.orders

import br.com.youse.utils.{SparkUtils}

class OrdersQuoted {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {

    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("quoted_base")

    val orders = sparkSession
      .sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM quoted_base")
    orders.createOrReplaceTempView("quoted_base")


    val ordersQuoted = sparkSession
      .sql("SELECT message_id, insurance_type, lead_person.*, order_uuid, pricing.*, sales_channel, " +
        "from_unixtime(raw_timestamp) as quoted_at, routing_key" +
        " FROM quoted_base")
    ordersQuoted.createOrReplaceTempView("orders_quoted")

    sparkSession.catalog.clearCache()
  }
}



