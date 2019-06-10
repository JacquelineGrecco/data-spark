package br.com.youse.insurance_policy

import br.com.youse.utils.SparkUtils

class InsurancePolicyCancelled {

  val sparkSession = SparkUtils.getSession()

  def transform(path: String): Unit = {
    val base = sparkSession
      .read
      .json(path)

    base.cache()
    base.createOrReplaceTempView("cancelled_base")

    val policy = sparkSession.sql("SELECT message_id, payload.*, raw_timestamp, routing_key FROM cancelled_base")
    policy.createOrReplaceTempView("cancelled_base")

    val cancelledPolicy = sparkSession.sql("SELECT "+
      "message_id, "+
      "from_unixtime(raw_timestamp) as cancelled_at, "+
      "routing_key, "+
      "order_uuid, "+
      "policy_number, "+
      "reason as cancelled_reason, "+
      "insurance_type as product "+
      "FROM cancelled_base")
    cancelledPolicy.createOrReplaceTempView("policy_cancelled")

    sparkSession.catalog.clearCache()
  }
}
