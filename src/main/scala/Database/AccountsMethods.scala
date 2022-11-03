package Database

import java.sql._
import TheBankApp.AccountMethods.accountMenuDB
import scala.io.StdIn.{readDouble, readInt}

object AccountsMethods {

  def createUserAccountDB(firstName: String, lastName: String, DOB: String, client_username: String, client_password: String, balance: Double, creditCard: String, interestRate: Double) = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val insertNewAccount = s"INSERT INTO accounts (client_username,client_password,client_first_name,client_last_name,client_date_of_birth,balance,credit_card,interest_rate) VALUES (?,?,?,?,?,?,?,?)"
      val preparedStatement: PreparedStatement = connection.prepareStatement(insertNewAccount)

      preparedStatement.setString(1, s"${client_username}")
      preparedStatement.setString(2, s"${client_password}")
      preparedStatement.setString(3, s"${firstName}")
      preparedStatement.setString(4, s"${lastName}")
      preparedStatement.setString(5, s"${DOB}")
      preparedStatement.setString(6, s"${balance}")
      preparedStatement.setString(7, s"${creditCard}")
      preparedStatement.setString(8, s"${interestRate}")
      preparedStatement.execute()
    } catch {
      case e: Exception => e.printStackTrace()
    }
    finally {
      connection.close()
    }
  }

  def userCheckingPersonalInfo(user: String): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_username LIKE '%$user%'")
      while (resultSet.next()) {
        val usernameDB = resultSet.getString("client_username")
        val first_name = resultSet.getString("client_first_name")
        val last_name = resultSet.getString("client_last_name")
        val DOB = resultSet.getString("client_date_of_birth")
        val balance = resultSet.getDouble("balance")
        val interestRate = resultSet.getDouble("interest_rate")
        val creditCard = resultSet.getString("credit_card")
        val id = resultSet.getInt("id")

        println(s"Here are your personal information:\n" +
          s"Account number: ${id}\n" +
          s"Username: ${usernameDB}\n" +
          s"First name: ${first_name}\n" +
          s"Last name: ${last_name}\n" +
          s"Date of birth: ${DOB}\n" +
          s"Balance: ${balance}\n" +
          s"Interest rate: ${interestRate}\n" +
          s"Credit card number: ${creditCard}\n"
          //          s"\n------------------------------------\n"
        )
      }
    }
  }

  def balanceDB(user: String): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_username LIKE '%$user%' ")
      while (resultSet.next()) {
        val balance = resultSet.getDouble("balance")


        println(f"Your current balance is £${balance}%2.2f\n")
        //          s"\n------------------------------------\n")
        balance
      }
    }
  }

  def addingMoneyDB(user: String, newBalanceMoney: Double): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_username LIKE '%$user%' ")
      while (resultSet.next()) {
        val first_name = resultSet.getString("client_username")
        val DOB = resultSet.getString("client_date_of_birth")
        var balance = resultSet.getDouble("balance")

        var newBalance = balance + newBalanceMoney

        val statement2 = connection.createStatement()
        val resultSet2 = statement2.executeUpdate(s"UPDATE accounts SET balance = ${newBalance} WHERE client_username LIKE '%$user%'")
        println(f"Your new balance is £${newBalance}%2.2f")
      }
    }
  }

  def withdrawnBalanceDB(user: String): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_username LIKE '%$user%' ")
      while (resultSet.next()) {
        val first_name = resultSet.getString("client_username")
        val DOB = resultSet.getString("client_date_of_birth")
        var balance = resultSet.getDouble("balance")

        println(f"Your current balance is £${balance}%2.2f. How much would you like to withdraw from your account?")
        var withdrownMoney: Double = readDouble()
        var withdrowingTransactions: Boolean = false

        while (!withdrowingTransactions) {
          if (withdrownMoney < balance) {

            var newBalance = balance - withdrownMoney

            val statement2 = connection.createStatement()
            val resultSet2 = statement2.executeUpdate(s"UPDATE accounts SET balance = ${newBalance} WHERE client_username LIKE '%$user%'")
            println(f"Your new balance is £${newBalance}%2.2f.\n")
            accountMenuDB(user: String)
          } else {
            println(f"It seems like you have insufficient fund. Your balance is £${balance}%2.2f.\nTo try again, press 1.\nTo go back to the main menu, press 2.")
            var withdrawRefusal: Int = readInt()
            if (withdrawRefusal == 1) {
              println("How much would you like to withdraw from your account? ")
              withdrownMoney = readDouble()
            } else if (withdrawRefusal == 2) {
              withdrowingTransactions = true
              accountMenuDB(user: String)
            } else {
              println("Invalid choice. Try again.")
            }
          }
        }
      }
    }
  }

  def deleteDBAccount(user: String): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_username LIKE '%$user%' ")
      while (resultSet.next()) {
        val first_name = resultSet.getString("client_first_name")
        val DOB = resultSet.getString("client_date_of_birth")

        val statement2 = connection.createStatement()
        val resultSet2 = statement2.executeUpdate(s"DELETE FROM accounts WHERE client_username LIKE '%$user%'")
      }
    }
  }

  def checkingExistingID(idInput: Int): Boolean = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT id FROM accounts")
      while (resultSet.next()) {
        val idDB = resultSet.getString("id")

        if (idDB.contains(idInput)) {
          return true
        }
      }
    }
    false
  }

  /*
  def CCDB(): String = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT credit_card FROM accounts")
      while (resultSet.next()) {
        val CC = resultSet.getString("credit_card")
        var newCC = Random.between(1000000000000000L,9999999999999999L).toString
        while(!CC.contains(newCC)) {

          println(s"new ${newCC}")
        }
      }
    }
  }
  CCDB()
   */
}
