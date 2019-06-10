package br.com.youse.insurance_policy

import br.com.youse.utils.SparkUtils

class InsurancePolicyRefused {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {
    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("refused_base")

    val policy = sparkSession.sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM refused_base")
    policy.createOrReplaceTempView("refused_base")

    val refusedPolicy = sparkSession.sql("SELECT "+
      "message_id, "+
      "from_unixtime(raw_timestamp) as refused_at, "+
      "routing_key, "+
      "order_uuid, "+
      "policy_number, "+
      "reason as refused_reason, "+
      "insurance_type as product "+
      "FROM refused_base")
    refusedPolicy.createOrReplaceTempView("policy_refused")

    sparkSession.catalog.clearCache()
  }
}
