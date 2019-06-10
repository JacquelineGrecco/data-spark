package br.com.youse.insurance_policy

import br.com.youse.utils.SparkUtils

class InsurancePolicyActivated {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {
    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("activated_base")

    val policy = sparkSession.sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM activated_base")
    policy.createOrReplaceTempView("activated_base")

    val activatedPolicy = sparkSession.sql("SELECT "+
      "message_id, "+
      "from_unixtime(raw_timestamp) as activated_at, "+
      "routing_key, "+
      "order_uuid, "+
      "policy_number, "+
      "insurance_type as product "+
      "FROM activated_base")
    activatedPolicy.createOrReplaceTempView("policy_activated")

    sparkSession.catalog.clearCache()
  }
}