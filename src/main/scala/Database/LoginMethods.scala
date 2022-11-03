package Database

import java.sql._
import TheBankApp.AccountMethods.accountMenuDB

object LoginMethods {
  // LOGIN METHODS
  def adminLoginDB(usernameInput: String, passwordInput: String): Boolean = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM admins")
      while (resultSet.next()) {
        val adminDB = resultSet.getString("username")
        val passwordDB = resultSet.getString("password")

        if (usernameInput == adminDB && passwordInput == passwordDB) {
          return true
        }
        val currentAdmin: String = adminDB
      }
    }
    false
  }

  def userLoginDB(usernameInput: String, passwordInput: String): Boolean = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts")
      while (resultSet.next()) {
        val usernameDB = resultSet.getString("client_username")
        val passwordDB = resultSet.getString("client_password")
        val first_name = resultSet.getString("client_first_name")

        if (usernameInput == usernameDB && passwordInput == passwordDB) {
          accountMenuDB(first_name)
          return true
        }
        val currentUser: String = usernameDB
      }
    }
    false
  }

}
