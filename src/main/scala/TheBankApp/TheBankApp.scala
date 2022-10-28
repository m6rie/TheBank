package TheBankApp

import AccountMethods._

import scala.io.StdIn.{readChar, readDouble, readInt, readLine}
import io.AnsiColor._
import scala.util.Random
import scala.util.control.Breaks.break
import java.sql.{Connection, DriverManager, PreparedStatement}
import Database.methods.{adminDeleteDBAccount, adminLoginDB, allCustomersDB, selectCustomerByIdDB, selectCustomerByLastnameDB}


class TheBankApp(name:String) {
  // This function is the homepage, what everybody will see at the beginning when entering the "website"
  def homePage(): Unit = {
    println(s"Hello, Welcome to ${YELLOW}The Bank${RESET}. What would you like to do today?\n")
    println("Select your choice and press enter: \n" +
      "To log in, press 1\n" +
      "To create an account, press 2\n" +
      "For administrator access, press 3")
    var mainMenuChoice = readInt() // to take user's input
    while (mainMenuChoice != 1 || mainMenuChoice != 2 || mainMenuChoice != 3) {
      if (mainMenuChoice == 1) { // redirect to customer login page
        userLoginDB()
      } else if (mainMenuChoice == 2) { // redirect to the account creation function
        createNewAccount()
      } else if (mainMenuChoice == 3) { // redirect to the administrator login page
        adminLoggingInDB()
      } else {
        println("Invalid choice. Try again: ")
        mainMenuChoice = readInt()
      }
    }
  }
  homePage()

  // The administrator logs-in thanks to this function
  def adminLoggingInDB() {
    var existingAdminUsername = false
    var correctAdminPassword = false

    while (!existingAdminUsername && !correctAdminPassword) {
      println(s"${BOLD}Admin log in${RESET}\nEnter your ${BOLD}username${RESET}: ")
      var registeredAdmin: String = readLine()
      println(s"Enter your ${BOLD}password${RESET}: ")
      var registeredAdminPassword: String = readLine()
      if (adminLoginDB(registeredAdmin: String, registeredAdminPassword: String) == true) {
        var existingAdminUsername = true
        var correctAdminPassword = true
        adminMenu()
      }
    }
  }

  def adminMenu(): Unit = {
    println(s"Hi. What would you like to do today? Select your choice and press enter: \n" +
      "\bTo see all clients details, press 1\n " +
      "\bTo search clients using their account number, press 2\n " +
      "\bTo search clients using their last name, press 3\n " +
      "\bTo create a user account, press 4\n " +
      "\bTo delete a user account, press 5\n " +
      "\bTo log out, press 6")
    var adminMenuChoice = readInt()

    adminMenuChoice match {
      case 1 => listAllCustomers()
      case 2 => searchClientById()
      case 3 => searchClientByLastName()
      case 4 => adminCreateNewAccount()
      case 5 => deleteUser()
      case 6 => adminLoggingOut()
      case _ => println("Invalid choice. Try again: ")
    }
  }

  // ALL ACCOUNT DISPLAY LIST
  def listAllCustomers(): Unit = {
    allCustomersDB()
    adminMenu()
  }

  def searchClientById(): Unit = {
    println("Search a client using their account ID: ")
    var searchedID: Int = readInt()
    selectCustomerByIdDB(searchedID)

    // ADD AN IF STATEMENT TO CHECK IF ACCOUNT ID EXISTS
//    if (checkingExistingID(searchedID: Int) == true) {
//      selectCustomerByIdDB(searchedID)
//    } else {
//      println("This account number does not exists.\n")
//    }
    adminMenu()
  }

  def searchClientByLastName(): Unit = {
    println("Search a client using their last name: ")
    val searchedByLastName: String = readLine().toLowerCase.capitalize

    // ADD AN IF STATEMENT TO CHECK IF ACCOUNT name EXISTS
    selectCustomerByLastnameDB(searchedByLastName)
    adminMenu()
  }

  def adminCreateNewAccount(): Unit = {
    var newAccount = new Account {
      println("Please enter the client first name: ")
      override val firstName: String = readLine()
      println("Please enter the client last name: ")
      override val lastName: String = readLine()
      println("Please enter the client date of birth with the format YYYY/MM/DD: ")
      override val DOB: String = readLine()
      println("Please enter the client username: ")
      override val client_username: String = readLine()
      println("Please enter the client password: ")
      override val client_password: String = readLine()
      println("How much money are they transferring to their account? ")
      var balance: Double = readDouble()
      override val creditCard: String = Random.between(1000000000000000L,9999999999999999L).toString
      override var interestRate: Double = 0.00

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

      println(s"We are very happy to count $firstName among us!\n")
    }
    adminMenu()
  }

  def deleteUser(): Unit = {
    println("What is the account number you wish to delete?")
    var userAccountDeletion = readInt()
    adminDeleteDBAccount(userAccountDeletion:Int)
    adminMenu()
  }

  def adminLoggingOut(): Unit = {
    var logOut: Boolean = false
    println(s"${RED}Are you sure you want to log out?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!logOut) {
      var logOutVerifying: Char = readChar().toLower
      if (logOutVerifying != 'n' && logOutVerifying != 'y') {
        println("Invalid choice. Try again: ")
      } else if (logOutVerifying == 'n') {
        adminMenu()
      } else {
        println(s"See you later!\n")
        logOut = true
        break()
      }
    }
  }
}
object TheBankApp extends App {
  var bankingApp = new TheBankApp("The Bank")

  bankingApp.homePage()
}

