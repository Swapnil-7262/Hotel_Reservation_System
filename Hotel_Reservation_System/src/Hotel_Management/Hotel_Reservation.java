package Hotel_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Hotel_Reservation {

	private static final String url = "jdbc:mysql://localhost:3306/hotel_db";

	private static final String username = "root";

	private static final String password = "Swap7262@";

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			while (true) {
				System.out.println();
				System.out.println("Hotel Managment System");
				Scanner scanner = new Scanner(System.in);
				System.out.println("1.Reserve Room");
				System.out.println("2.Views Reservations");
				System.out.println("3.Get Room Number");
				System.out.println("4.Update reservations");
				System.out.println("5.Delete Reservatons");
				System.out.println("6.Exits");
				System.out.println("Choose any One");
				int choice = scanner.nextInt();

				switch (choice) {
				case 1:
					reserveRoom(connection, scanner);
					break;
				case 2:
					viewReservations(connection);
					break;
				case 3:
					getRoomNumber(connection, scanner);
					break;
				case 4:
					updateReservaton(connection, scanner);
					break;
				case 5:
					deleteReservation(connection, scanner);
					break;
				case 6:
					exit();
					scanner.close();
					return;

				default:
					System.out.println("Invalid Choice. Try again.");
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void reserveRoom(Connection connection, Scanner scanner) {
		try {
			System.out.println("Enter guest name: ");
			String guestName = scanner.next();
			scanner.nextLine();
			System.out.println("Enter room number: ");
			int roomNumber = scanner.nextInt();
			System.out.println("Enter Contact number: ");
			double contactNo = scanner.nextDouble();

			String sql = "insert into reservations(r_Name,r_RoomNumber,r_ContactNumber)" + "values ('" + guestName
					+ "'," + roomNumber + "," + contactNo + ")";
			try (Statement statement = connection.createStatement()) {
				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) {
					System.out.println("Resevation SuccessFul.");
				} else {
					System.out.println("Reservation Fail.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void viewReservations(Connection connection) throws SQLException {
		String sql = "select r_id ,r_Name ,r_RoomNumber,r_ContactNumber,r_Date from reservations;";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

			System.out.println("Current Reservations:");
			while (resultSet.next()) {
				int r_id = resultSet.getInt("r_Id");
				String r_name = resultSet.getString("r_Name");
				int r_roomNo = resultSet.getInt("r_RoomNumber");
				double r_contactNo = resultSet.getDouble("r_ContactNumber");
				String r_date = resultSet.getString("r_Date");

				System.out.println("-----------------------------------------------------------");
				System.out.println();
				System.out.println("ID:            " + r_id);
				System.out.println("NAME:          " + r_name);
				System.out.println("ROOM NO:       " + r_roomNo);
				System.out.println("CONTACT NO:    " + r_contactNo);
				System.out.println("DATE AND TIME: " + r_date);
				System.out.println();
				System.out.println("-----------------------------------------------------------");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void getRoomNumber(Connection connection, Scanner scanner) {
		try {
			System.out.println("Enter Id: ");
			int r_id = scanner.nextInt();
			System.out.println("Enter guest Name: ");
			String r_name = scanner.next();
			// scanner.nextLine();

			String sql = "select r_RoomNumber from reservations where r_Id =" + r_id + " and" + " r_Name='" + r_name
					+ "';";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(sql)) {

				if (resultSet.next()) {
					int r_roomNo = resultSet.getInt("r_RoomNumber");
					System.out.println("Reservation ID=" + r_id + "\n Name=" + r_name + "\n Room Number=" + r_roomNo);
				} else {
					System.out.println("Reservation not found.");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateReservaton(Connection connection, Scanner scanner) {
		try {
			System.out.println("Enter reservation id:");
			int r_id = scanner.nextInt();
			scanner.nextLine();

			if (!reservationExits(connection, r_id)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}

			System.out.println("Enter guest name: ");
			String newGuestName = scanner.nextLine();
			System.out.println("Enter room no: ");
			int newRoomNo = scanner.nextInt();
			System.out.println("Enter cantact no: ");
			double newContactNo = scanner.nextDouble();

			String sql = "update reservations set r_name='" + newGuestName + "',r_RoomNumber=" + newRoomNo + ","
					+ "r_ContactNumber=" + newContactNo + "where r_Id=" + r_id;

			try (Statement statement = connection.createStatement()) {
				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) {
					System.out.println("Reservation Update Success");

				} else {
					System.out.println("Reservation updated Fails.");

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void deleteReservation(Connection connection, Scanner scanner) {
		try {
			System.out.println("Enter reservaattion id: ");
			int r_id = scanner.nextInt();

			if (!reservationExits(connection, r_id)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}
			String sql = "delete from reservations where r_Id=" + r_id;

			try (Statement statement = connection.createStatement();) {

				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) {
					System.out.println("Reservation Delete Success");

				} else {
					System.out.println("Reservation Delete Fails.");

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static boolean reservationExits(Connection connection, int r_id) {
		try {
			String sql = " select r_Id from reservations where r_Id=" + r_id;
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(sql)) {
				return resultSet.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	private static void exit() throws InterruptedException {
		System.out.print("Exiting System");
		int i = 5;
		while (i != 0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("Thank You!");
	}

}
