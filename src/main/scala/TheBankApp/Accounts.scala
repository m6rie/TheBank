package TheBankApp

import Database.methods.{addingMoneyDB, balanceDB, createUserAccountDB, deleteDBAccount, moneyToAddDB, withdrawnBalanceDB}

import scala.io.StdIn.{readChar, readDouble, readInt, readLine}
import io.AnsiColor._
import scala.util.control.Breaks.break
import scala.util.matching.Regex
import scala.util.Random

import java.sql.{Connection, DriverManager, PreparedStatement}


abstract class Account {
  val firstName: String
  val lastName: String
  val DOB: String
  val client_username: String
  val client_password: String
  var balance: Double
  var interestRate: Double
  val creditCard: String
  val the_bank_id: Int = 1
}

object AccountMethods {
  def createNewAccount(): Unit = {
    var newAccount = new Account {
      println("Please enter your first name: ")
      override val firstName: String = readLine()
      println("Please enter your last name: ")
      override val lastName: String = readLine()
      println("Please enter your date of birth with the format YYYY/MM/DD: ")
      override val DOB: String = readLine()
      println("Please enter your username: ")
      override val client_username: String = readLine()
      println("Please enter your password: ")
      override val client_password: String = readLine()
      println("How much money are you transferring to your account? ")
      var balance: Double = readDouble()
      override val creditCard: String = Random.between(1000000000000000L,9999999999999999L).toString
      override var interestRate: Double = 0.00

      createUserAccountDB(firstName: String,lastName: String,DOB: String, client_username: String,client_password: String, balance: Double, creditCard: String, interestRate: Double)

      println(s"We are very happy to count you among us $firstName!\n")

      accountMenuDB(firstName)
    }
  }

  def interestRateCal() = {
    //      if()
  }

  def userLoginDB(): Boolean = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/theSbank"
    val username = "root"
    val password = "FtE73.mP11"

    println(s"Enter your ${BOLD}username${RESET}: ")
    var usernameInput: String = readLine()
    println(s"Enter your ${BOLD}password${RESET}: ")
    var passwordInput: String = readLine()


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

          val currentUser: String = first_name
          accountMenuDB(first_name)
          return true
        }
      }
    }
    false
  }

  def accountMenuDB(user: String): Unit = {
    println(s"Hi ${user}. What would you like to do today? Select your choice and press enter: \n" +
      "\bTo check your balance, press 1\n " +
      "\bTo add money to your account, press 2\n " +
      "\bTo withdraw money from your account, press 3\n " +
      "\bTo delete your account, press 4\n " +
      "\bTo log out, press 5")
    var userMenuChoice = readInt()

    userMenuChoice match {
      case 1 => checkBalanceDB(user: String)
      case 2 => addMoneyDB(user: String, amountToAddToAccountDB(user: String))
      case 3 => withdrawMoneyDB(user: String)
      case 4 => deleteAccountDB(user: String)
      case 5 => loggingOutDB(user: String)
      case _ => println("Invalid choice. Try again: ")
    }
  }

  // METHOD TO CHECK BALANCE
  def checkBalanceDB(user: String): Unit = {
    balanceDB(user:String)
    accountMenuDB(user: String)
  }

  // WE CAN USE A HIGHER ORDER FUNCTION TO ADD MONEY TO AN ACCOUNT
  def amountToAddToAccountDB(user: String): Double = {
    moneyToAddDB(user: String)
    println("How much money would you like to add?\n")
    var amountAdded: Double = readDouble()
    amountAdded
  }

  def addMoneyDB(user: String, amount: Double): Unit = {
    addingMoneyDB(user:String, amount: Double)
    accountMenuDB(user: String)
  }

  // THE METHOD TO WITHDRAW MONEY
  def withdrawMoneyDB(user: String): Unit = {
    withdrawnBalanceDB(user:String)
  }

  def deleteAccountDB(user: String): Unit = {
    var deletingAccount: Boolean = false
    println(s"${RED}Are you sure you want to delete your account?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!deletingAccount) {
      var accountDeletion: Char = readChar().toLower
      if (accountDeletion != 'n' && accountDeletion != 'y') {
        println("Invalid choice. Try again: ")
      } else if (accountDeletion == 'n') {
        accountMenuDB(user: String)
      } else {
        println(s"Sorry to see you go ${user}!\n" +
        "-----------------------------------------")
        deleteDBAccount(user:String)
        deletingAccount = true
        break()
      }
    }
  }

  def loggingOutDB(user: String): Unit = {
    var logOut: Boolean = false
    println(s"${RED}Are you sure you want to log out?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!logOut) {
      var logOutVerifying: Char = readChar().toLower
      if (logOutVerifying != 'n' && logOutVerifying != 'y') {
        println("Invalid choice. Try again: ")
      } else if (logOutVerifying == 'n') {
        accountMenuDB(user: String)
      } else {
        println(s"Thank you for your visit ${user}!\n")
        logOut = true
        break()
      }
    }
  }

}

/*
    class ExceptionPrepTest(input: String) extends Exception {
      override def toString: String = super.toString
    }

    class NumberFormatException {
      @throws(classOf[ExceptionPrepTest])
      def nonValid(numInput:Regex) = {
        if(numInput != "\\d".r){
          throw new ExceptionPrepTest("not a num")
        }
      }
    }
    var m = new NumberFormatException
    try {
      m.nonValid("\\d".r)
    } catch {
      case m:ExceptionPrepTest => println("Try again"+m)
    }

 */