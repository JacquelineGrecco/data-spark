package br.com.youse

import java.util.Properties

import br.com.youse.orders.{OrdersCreated, OrdersQuoted}
import br.com.youse.utils.{ConfigUtils, SparkUtils}
import org.apache.spark.sql.SaveMode

class OrdersBase {

  val sparkSession = SparkUtils.getSession()

  def transform(created_path: String, quoted_path: String): Unit = {
    val ordersCreated = new OrdersCreated()
    ordersCreated.transform(created_path)

    val ordersQuoted = new OrdersQuoted()
    ordersQuoted.transform(quoted_path)

    val ordersBase = sparkSession.sql("SELECT " +
      "COALESCE(c.order_uuid,'')     AS order_uuid," +
      "COALESCE(c.created_at,'')   AS created_at," +
      "COALESCE(q.quoted_at,'')    AS quoted_at," +
      "COALESCE(c.sales_channel,'')  AS sales_channel," +
      "COALESCE(c.insurance_type,'') AS insurance_type," +
      "COALESCE(q.monthly_cost,'')   AS monthly_cost," +
      "COALESCE(c.name,'')  AS name," +
      "COALESCE(c.phone,'') AS phone," +
      "COALESCE(c.email,'') AS email" +
      " FROM orders_created AS c" +
      " LEFT JOIN orders_quoted AS q" +
      " ON c.order_uuid = q.order_uuid")

    ordersBase.createOrReplaceTempView("orders_base")

    ordersBase
      .dropDuplicates
      .write
      .mode(SaveMode.Append)
      .jdbc("jdbc:sqlite:" + ConfigUtils.getConfig("sqlite.database_name"), "orders_base", new Properties())


    sparkSession.catalog.clearCache()
  }
}


object OrdersBase {
  def main(args: Array[String]): Unit = {
    new OrdersBase().transform(args(0), args(1))
  }
}