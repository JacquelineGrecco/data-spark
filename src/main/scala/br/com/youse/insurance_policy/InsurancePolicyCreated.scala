package br.com.youse.insurance_policy

import br.com.youse.utils.SparkUtils

class InsurancePolicyCreated {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {
    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("created_base")

    val policy = sparkSession.sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM created_base")
    policy.createOrReplaceTempView("created_base")

    val createdPolicy = sparkSession.sql("SELECT "+
      "message_id, "+
      "from_unixtime(raw_timestamp) as created_at, "+
      "routing_key, "+
      "order_uuid, "+
      "policy_number, "+
      "insurance_type as product "+
      "FROM created_base")
    createdPolicy.createOrReplaceTempView("policy_created")

    sparkSession.catalog.clearCache()
  }
}
