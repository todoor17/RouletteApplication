# RouletteApp
A JavaFX Roulette application where users can create an account, log in, and play the well-known casino game. When users run out of money, they can deposit more through the application's transaction methods. This project was made for 2023 OOP assignment.

## Author:
**Todor Ioan Iulian** | Technical University of Cluj-Napoca | Second Year of Study.

## Visuals:
When user runs the code, `Start Window ` will appear. On this windows our fictive casino logo is displayed.

![image](https://github.com/todoor17/RouletteApp/assets/152257565/b8137a1d-0d69-439a-9e67-b71483c24dee)

Three seconds after the code is runned, `Login Window` appears. The following actions can be taken: login as user, login as admin, sign up.

![image](https://github.com/todoor17/RouletteApp/assets/152257565/a9163963-6bcc-4ef5-aca5-019144028b67)

If user chooses to sign up, `Sign Up Window` is displayed. Here user must fill the signing up form, with respect to the following rules (username != passoword, password != repeatPassword, cnpLenght = 13). After signing up user can press the **Back to login** button and login with the newly created credentials.

![image](https://github.com/todoor17/RouletteApp/assets/152257565/770c4c9e-2bf3-4a4c-a9a2-83323d2b5beb)

If username **admin** and password **specialAdminPassword** are provided, user is redirected to the `Admin Window`. In this page admin can see all users data, see specific user betting summarry / transaction and ban / unban specific user. *Banned* user won't login until the *Unban* action is taken. 

![image](https://github.com/todoor17/RouletteApp/assets/152257565/84ea74d6-4796-4728-b67d-67f2cee3a8c6)

If normal user is logged in, `Roulette Window` opens. Here user must select a stake sum (5, 10, 25, 50 or MAX BET), place a bet (number, color, dozen, parity, 1-18/19-36) and see the result by pressing the "Spin the wheel" button. Top right placed balance label is updating with result and every bet placed. Top left placed time label shows the current time and updates every second. 

![image](https://github.com/todoor17/RouletteApplication/assets/152257565/56395b45-00b9-4a4e-9235-67cbbb46dc1f)

If user runs out of money, he must make a deposit Transaction in order to play more (press Transaction button).

![image](https://github.com/todoor17/RouletteApplication/assets/152257565/a2357c45-340c-44bf-8ea3-05e9de86ec31)

By pressing Transactions button, `Transaction Window` opens. User's available cards are displayed in the top down list view. An operation (Add new card, Deposit, Withdraw) must be selected. If user wants to add a new card, he must add a 16 digit card number that don't exist in the database. After introducing a new card, it will have a random generated available sum (<600). The funds can be deposited after choosing deposit action. If user wants to withdraw money, he must introduce credentials of a specific card and make sure requested sum <= balance. 

![image](https://github.com/todoor17/RouletteApp/assets/152257565/34f13e6d-ea9f-4540-86c7-e97ae307e014)

## Dependencies:
This project was built on a Maven Project. The used dependencies are **jdbc** and **javafx**.

## MySql Class Diagram

![image](https://github.com/todoor17/RouletteApp/assets/152257565/bc68b7f4-560e-4a53-a481-0d39aa5403ef)

## Establish Database Connection:
To add database in your downloaded project, follow the steps described below.
First, ensure you have MySQL and the JDBC driver installed to initialize the connection between the project and the database. If you haven't installed these yet, this tutorial could be very helpful: [MySQL & JDBC Installation Tutorial](https://www.youtube.com/watch?v=e8g9eNnFpHQ&t=194s).

After confirming that MySQL and JDBC are installed on your computer, open **MySQL WORKBENCH**. On the homepage

![MySQL Workbench Homepage Image](https://github.com/todoor17/RouletteApp/assets/152257565/10e7f186-921f-410e-abb9-bca707fac0e9)

You will see your username (**root** in this example) and the port number your local MySQL server is running on (**3306**) — note that your details may differ. Click on 'Local instance MySQL80', enter your password, and log in. On the left side of the page, you will see your schemas. Navigate to **toolbar -> server -> Data import**.

![MySQL Data Import Image](https://github.com/todoor17/RouletteApp/assets/152257565/d44b7e5b-3cf6-4513-86c0-3000e8d1ed9a)

Select 'Import from Dump Project Folder' and choose the folder where you have downloaded the project files. The 'bettingdatabase' object should appear in the selection list.

![MySQL Import Selection Image](https://github.com/todoor17/RouletteApp/assets/152257565/eee9124e-2837-46de-a23c-4b4e5b7de665)

If everything works fine, create a new 'bettingdatabase' schema, select all tables, and click on **START IMPORT**. Now, open the project folder in IntelliJ, search for the `DBConnector` class, and complete the code with **YOUR_PORT_NUMBER**, **YOUR_USERNAME**, and **YOUR_PASSWORD**.

![DBConnector Setup Image](https://github.com/todoor17/RouletteApp/assets/152257565/d283ac26-db3c-4430-9982-e85478ea6b1c)

## Running the Project:

**Open Project Structure -> Modules -> + Add -> Jar file -> lib -> mysql-connector-j-8.2.0.jar**, apply changes. Now, all you have to do is open the Main class and run the code.

## Classes:
1. `Main` - this is the class that is being runned.
2. `DBConnector` - contains the MySql connection and all Sql queries needed
3.  `SceneSwitcher` - contain a function used in another classes to switch scenes
4.  `StartController` - loads the "startController.fxml" file
5.  `LoginController` - controls the login and loads "login.fxml" file
6.  `SignUpController` - controls the sign up  and loads "signUp.fxml" file
7.  `AdminController` - controls the admin and loads "admin.fxml" file
8.  `RouletteController` - controls the roulette and loads "rouletteController.fxml" file
9.  `TransactionController` - controls the transactions and loads "transaction.fxml" file
10. `UserData` - auxiliar class used for displaying UserData in dminListView
11. `Card` - auxiliar class used for displaying user's cards details in transactionsListView
12. `Transactions`- auxiliar class used for displaying Transactions in adminListView

## Class diagram:

![image](https://github.com/todoor17/RouletteApp/assets/152257565/503e50e3-28f1-4115-8c7d-c2390b37e956)


