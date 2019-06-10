package br.com.youse.database


import java.sql.{DriverManager, SQLException}
import br.com.youse.utils.ConfigUtils

class Database {

  def newdb (file: String): String = {

    if(file.equals("") || file == null) {
      return "Empty Path"
    }

    val path = "jdbc:sqlite:" + file

    try {
      val conn = DriverManager.getConnection(path)
      try {
        if (conn != null) {
          val metadata = conn.getMetaData
          return "db created"
        }
      }
      catch {
        case e: SQLException =>
          return e.getMessage
      }finally {
        if (conn != null) {
          conn.close()
        }
      }
    }
    catch {
      case e: Exception =>
        return e.getMessage
    }
    return "Failed create db"
  }

  def newTable(db: String): String = {

    if(db.equals("") || db == null) {
      return "Empty String"
    }

    val path = "jdbc:sqlite:" + db

    val orders           = ConfigUtils.getConfig("sqlite.orders.table_create")
    val insurance_policy = ConfigUtils.getConfig("sqlite.insurance_policy.table_create")

    try {

      val conn = DriverManager.getConnection(path)
      val statement = conn.createStatement

      try {

        println(statement.execute(orders))
        println(statement.execute(insurance_policy))

        return "tables created"
      } catch {
        case e: SQLException =>
          return e.getMessage
      } finally {

        if (conn != null)
          conn.close()

        if (statement != null)
          statement.close()
      }
    } catch {
      case e: Exception =>
        return e.getMessage
    }
  }
}