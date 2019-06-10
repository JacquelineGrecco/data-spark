package br.com.youse_test.database

import br.com.youse.database.Database
import br.com.youse.utils.ConfigUtils
import org.scalatest.{FlatSpec, Matchers}

class DatabaseTest extends FlatSpec with Matchers {

  val database = new Database()

  it should "create a database" in {
    database.newdb(ConfigUtils.getConfig("sqlite.database_name")) should be ("db created")
  }

  it should "don't create a database" in {
    database.newdb("") should be ("Empty Path")
  }

  it should "create a table" in {
    database.newTable(ConfigUtils.getConfig("sqlite.database_name")) should be ("tables created")
  }

  it should "don't create a table" in {
    database.newTable("")  should be ("Empty String")
  }
}