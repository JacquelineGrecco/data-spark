package br.com.youse.insurance_policy

import java.util.Properties

import br.com.youse.utils.{ConfigUtils, SparkUtils}
import org.apache.spark.sql.SaveMode

class InsurancePolicyBase {

  val sparkSession = SparkUtils.getSession()

  def transform(created_path: String, activated_path: String, cancelled_path: String, refused_path: String): Unit = {

    val insurancePolicyCreated   = new InsurancePolicyCreated()
    val insurancePolicyActivated = new InsurancePolicyActivated()
    val insurancePolicyCancelled = new InsurancePolicyCancelled()
    val insurancePolicyRefused   = new InsurancePolicyRefused()

    insurancePolicyCreated.transform(created_path)
    insurancePolicyActivated.transform(activated_path)
    insurancePolicyCancelled.transform(cancelled_path)
    insurancePolicyRefused.transform(refused_path)


    val insurancePolicyBase = sparkSession.sql("SELECT " +
      "COALESCE(c.policy_number, '') AS policy_number, "+
      "COALESCE(c.order_uuid, '') AS order_uuid, "+
      "COALESCE(c.product, '') AS product, "+
      "COALESCE(c.created_at, '') AS created_at, "+
      "COALESCE(a.activated_at, '') AS activated_at, "+
      "COALESCE(pc.cancelled_at, '') AS cancelled_at, "+
      "COALESCE(pc.cancelled_reason, '') AS cancelled_reason, "+
      "COALESCE(r.refused_at, '') AS refused_at, "+
      "COALESCE(r.refused_reason, '') AS refused_reason "+
      "FROM policy_created AS c "+
      "LEFT JOIN policy_activated AS a "+
      "ON c.policy_number = a.policy_number "+
      "LEFT JOIN policy_cancelled AS pc "+
      "ON c.policy_number = pc.policy_number "+
      "LEFT JOIN policy_refused AS r "+
      "ON c.policy_number = r.policy_number")

    insurancePolicyBase
      .dropDuplicates
      .write
      .mode(SaveMode.Append)
      .jdbc("jdbc:sqlite:" + ConfigUtils.getConfig("sqlite.database_name"), "policy_base", new Properties())

    sparkSession.catalog.clearCache
  }
}

object InsurancePolicyBase {
  def main(args: Array[String]): Unit = {
    new InsurancePolicyBase().transform(args(0), args(1), args(3), args(4))
  }
}