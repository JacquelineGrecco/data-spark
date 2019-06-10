package br.com.youse.orders

import br.com.youse.utils.{SparkUtils}

class OrdersCreated {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {
    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("created_base")

    val orders = sparkSession
      .sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM created_base")
    orders.createOrReplaceTempView("created_base")

    val ordersCreated = sparkSession
      .sql("SELECT message_id, insurance_type, lead_person.*, order_uuid, sales_channel, " +
        "from_unixtime(raw_timestamp) as created_at, routing_key" +
        " FROM created_base")
    ordersCreated.createOrReplaceTempView("orders_created")

    sparkSession.catalog.clearCache()
  }
}