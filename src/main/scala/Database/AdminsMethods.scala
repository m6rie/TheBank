package Database

import java.sql._

object AdminsMethods {
  // METHODS FOR THE ADMIN
  def allCustomersDB() = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM theSbank.accounts")
      while (resultSet.next()) {
        val username = resultSet.getString("client_username")
        val first_name = resultSet.getString("client_first_name")
        val last_name = resultSet.getString("client_last_name")
        val DOB = resultSet.getString("client_date_of_birth")
        val balance = resultSet.getDouble("balance")
        val interestRate = resultSet.getDouble("interest_rate")
        val creditCard = resultSet.getString("credit_card")
        val id = resultSet.getInt("id")

        println(s"Account number: ${id}\n" +
          s"Username: ${username}\n" +
          s"First name: ${first_name}\n" +
          s"Last name: ${last_name}\n" +
          s"Client date of birth: ${DOB}\n" +
          s"Account balance: ${balance}\n" +
          s"Interest rate: ${interestRate}\n" +
          s"Account credit card number: ${creditCard}\n"
          //          s"\n------------------------------------\n"
        )
      }
    }
  }

  def selectCustomerByIdDB(id: Int): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE id = $id ")
      while (resultSet.next()) {
        val usernameDB = resultSet.getString("client_username")
        val first_name = resultSet.getString("client_first_name")
        val last_name = resultSet.getString("client_last_name")
        val DOB = resultSet.getString("client_date_of_birth")
        val balance = resultSet.getDouble("balance")
        val interestRate = resultSet.getDouble("interest_rate")
        val creditCard = resultSet.getString("credit_card")
        val id = resultSet.getInt("id")

        println(s"Account number: ${id}\n" +
          s"Username: ${usernameDB}\n" +
          s"First name: ${first_name}\n" +
          s"Last name: ${last_name}\n" +
          s"Client date of birth: ${DOB}\n" +
          s"Account balance: ${balance}\n" +
          s"Account interest rate: ${interestRate}\n" +
          s"Account credit card number: ${creditCard}\n"
          //          s"\n------------------------------------\n"
        )
      }
    }
  }

  def selectCustomerByLastnameDB(lastname: String): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts WHERE client_last_name LIKE '%$lastname%' ")
      while (resultSet.next()) {
        val username = resultSet.getString("client_username")
        val first_name = resultSet.getString("client_first_name")
        val last_name = resultSet.getString("client_last_name")
        val DOB = resultSet.getString("client_date_of_birth")
        val balance = resultSet.getDouble("balance")
        val interestRate = resultSet.getDouble("interest_rate")
        val creditCard = resultSet.getString("credit_card")
        val id = resultSet.getInt("id")

        println(s"Account number: ${id}\n" +
          s"Username: ${username}\n" +
          s"First name: ${first_name}\n" +
          s"Last name: ${last_name}\n" +
          s"Client date of birth: ${DOB}\n" +
          s"Account balance: ${balance}\n" +
          s"Account interest rate: ${interestRate}\n" +
          s"Account credit card number: ${creditCard}\n"
          //          s"\n------------------------------------\n"
        )
      }
    }
  }

  def adminDeleteDBAccount(userAccountDeletion: Int): Unit = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeUpdate(s"DELETE FROM accounts WHERE id = ${userAccountDeletion}")
    }
  }

}
