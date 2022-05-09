import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

	private static ArrayList<Product> stockList = new ArrayList<Product>();
	private static ArrayList<Product> shoppingCart = new ArrayList<Product>();
	private static Scanner input = new Scanner(System.in);
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	private static DecimalFormat currency = new DecimalFormat("€0.00");
	
	
	private static double totalTakings;
	private static int totalTransactions;
	private static int centralProductID = 105;
	
	public static void main(String[] args) {

		checkForData();
		mainMenu();
		
	}
	/**
	 * Method to check directory for file
	 */
	private static void checkForData() {
		File f = new File("Products.ser");
		if(f.exists()) {
			try {
				loadData();
			} catch (Exception e) {
				System.out.println("Error loading data");
			}
		}
		else {
			prePopulate();
		}
	}
	/**
	 * The main menu method
	 */
	private static void mainMenu() {
		System.out.println("Welcome to our online store");
		System.out.println("Please select an option -");
		System.out.println("\t 1 for the stock menu");
		System.out.println("\t 2 for the shop menu");
		System.out.println("\t S to save your data");
		System.out.println("\t X to close program");
		
		
		switch(input.next().toUpperCase()) {
			case "1": {
				stockMenu();
				break;
			}
			case "2": {
				shopMenu();
				break;
			}
			case "S": {
				try {
					saveData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error saving data.");
				}
				break;
			}
			case "X": {
				System.out.println("Would you like to save your data before leaving?");
				System.out.println("Type Y for yes or N for no");
				
				switch(input.next().toUpperCase()) {
					case "Y": {
						try {
							saveData();
							System.out.println("Program shutting down");
							System.exit(0);
						} catch (Exception e) {
							System.out.println("Error saving data");
						}
						break;
					}
					case "N": {
						System.out.println("Program shutting down");
						System.exit(0);
					}
					default: {
						System.out.println("Invalid choice. Please select a valid option");
						break;
					}
				}
			}
			
			default: {
				System.out.println("Error. Please select a valid choice");
				break;
			}
		}
		mainMenu();
	}
	/**
	 * Menu method for adding and removing items from shooping cart.
	 */
	private static void shopMenu() {
		System.out.println("**********Shopping Menu**********");
		System.out.println("Please select an option");
		System.out.println("\t 1 to add item to shopping cart");
		System.out.println("\t 2 to view shopping cart");
		System.out.println("\t 3 to remove item from shopping cart");
		System.out.println("\t 4 to check out");
		System.out.println("\t S to save your data");
		System.out.println("\t M to return to main menu");
		
		switch(input.next().toUpperCase()) {
			case "1": {
				addToCart();
				break;
			}
			case "2": {
				viewCart();
				break;
			}
			case "3": {
				if(shoppingCart.isEmpty()) {
					System.out.println("Your cart is currently empty!");
				}
				else {
					removeFromCart();
				}
				break;
			}
			case "4": {
				if(shoppingCart.isEmpty()) {
					System.out.println("Your cart is currently empty! Please select an item to purchase before proceeding");
				}
				else {
					checkOut();
				}
				break;
			}
			case "S": {
				try {
					saveData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error Logged");
				}
				break;
			}
			case "M": {
				mainMenu();
				break;
			}
			default: {
				System.out.println("Error. Please select a valid choice");
				break;
			}
		}
		shopMenu();
	}
	/**
	 * Method for removing items from shopping cart
	 */
	private static void removeFromCart() {
		viewCart();
		System.out.println("Please select the ID number of the item you wish to remove from cart");
		int chosenID = input.nextInt();
		boolean isFound = false;
		
		for(Product cartItem: shoppingCart) {
			if(cartItem.getProductID() == chosenID) {
				isFound = true;
				for(Product stockItem: stockList) {
					if(cartItem.getProductID() == stockItem.getProductID()) {
						stockItem.setProductQty(stockItem.getProductQty() + cartItem.getProductQty());
					}
				}
				System.out.println(cartItem.getProductName() +" removed from shopping cart");
				shoppingCart.remove(cartItem);
				break;
			}
		}
		if(isFound == false) {
			System.out.println("Error. No item with ID number "+ chosenID + " was found. Returning to shopping menu");
		}
	}
	/**
	 * Method for confirming cart items before proceeding
	 */
	private static void checkOut() {
		viewCart();
		System.out.println("Press 1 to continue to payment");
		System.out.println("Press 2 to return to shopping menu");
		
		switch(input.next()) {
		
			case "1": {
				double cartTotal = 0;
				for(Product cartItem: shoppingCart) {
					cartTotal+= cartItem.getProductPrice() * cartItem.getProductQty();
				}
				System.out.println("Your cart total is "+ currency.format(cartTotal));
				payment(cartTotal);
				break;
				}
			case "2": {
				return;
			}
			default: {
				System.out.println("Error. Please select a valid option");
				break;
			}
		}
		
	}
	/**
	 * Method for taking payment
	 * @param transactionPrice
	 */
	private static void payment(double transactionPrice) {
		System.out.println("Please enter payment");
		double enteredMoney = input.nextDouble();
		
		while(enteredMoney < transactionPrice) {
			System.out.print("Balance Remaining: ");
			System.out.println(currency.format(transactionPrice - enteredMoney));
			enteredMoney = enteredMoney + input.nextDouble();
		}
		if(enteredMoney > transactionPrice) {
			System.out.println("Please take your change: " + currency.format((enteredMoney - transactionPrice)));
			System.out.println("Thank you for your purchase");
			System.out.println("Have a nice day");
		}
		else {
			System.out.println("Thank you for your purchase");
			System.out.println("Have a nice day");
		}
		shoppingCart.clear();
		totalTakings = totalTakings + transactionPrice;
		totalTransactions = totalTransactions + 1;
	}
	/**
	 * Method for viewing shopping cart ArrayList
	 */
	private static void viewCart() {
		System.out.println("**********Your Shopping Cart**********");
		double cartTotal = 0;
		for(Product cartItem: shoppingCart) {
			System.out.print(cartItem.getProductID());
			System.out.println("\t"+ cartItem.getProductName());
			System.out.println("Quantity: "+ cartItem.getProductQty());
			System.out.println("Individual Price: "+ currency.format(cartItem.getProductPrice()));
			System.out.println("-------------------");
			cartTotal+= cartItem.getProductPrice() * cartItem.getProductQty();
		}
		System.out.println("Total cart price: "+ currency.format(cartTotal));
		
	}
	/**
	 * Method for adding items to shopping cart
	 */
	private static void addToCart() {
		System.out.println("Please select an option");
		System.out.println("\t 1 for Jackets");
		System.out.println("\t 2 for Videogames");
		
		switch(input.next()) {
			case "1": {
				printJackets();
				System.out.println("Please select the ID number of the jacket you wish to add to your cart");
				
				int chosenJacket = input.nextInt();
				boolean isFound = false;
				
				for(Product cartItem: shoppingCart) {
					if(cartItem.getProductID() == chosenJacket) {
						System.out.println(cartItem.getProductQty() + " " + cartItem.getProductName()+" in cart already");
						System.out.println("How many would you like to add?");
						int chosenQty = input.nextInt();
						for(Product stockItem: stockList) {
							if(cartItem.getProductID() == stockItem.getProductID()) {
								if(chosenQty > stockItem.getProductQty() || chosenQty < 1) {
									System.out.println("Invalid quantity. There are only "+ stockItem.getProductQty() +" left in stock");
									System.out.println("Returning to shop menu");
									return;
								}
								else {
									cartItem.setProductQty(cartItem.getProductQty()+ chosenQty);
									stockItem.setProductQty(stockItem.getProductQty() - chosenQty);					
									System.out.println(chosenQty + " more " + cartItem.getProductName() +" added to your cart");
									return;
								}
							}
						}
					}
				}
				
				
				
				
				for(Product stockItem: stockList) {
					if(chosenJacket == stockItem.getProductID() && stockItem.getProductType().equals("jacket")) {
						isFound = true;
						Jacket cartItem = new Jacket();
						cartItem.setProductID(stockItem.getProductID());
						cartItem.setProductName(stockItem.getProductName());
						cartItem.setProductPrice(stockItem.getProductPrice());
						
						System.out.println("Please select how many you would like to purchase");
						int chosenQty = input.nextInt();
						if(chosenQty > stockItem.getProductQty() || chosenQty < 1) {
							System.out.println("Invalid quantity. There are only "+ stockItem.getProductQty() +" left in stock");
							System.out.println("Returning to shop menu");
							return;
						}
						else {
							cartItem.setProductQty(chosenQty);
							stockItem.setProductQty(stockItem.getProductQty() - chosenQty);
							shoppingCart.add(cartItem);
							System.out.println(cartItem.getProductQty() + " "+ cartItem.getProductName() + " has been added to your cart");
						}
						
					}
					
				}
				if(isFound == false) {
					System.out.println("No item with ID number "+ chosenJacket + " was found");
					System.out.println("Returning to shop menu");
					}
				
				break;
			}
			case "2": {
				printVideoGames();
				System.out.println("Please select the ID number of the Videogame you wish to add to your cart");
				
				int chosenVideoGame = input.nextInt();
				boolean isFound = false;
				
				for(Product cartItem: shoppingCart) {
					if(cartItem.getProductID() == chosenVideoGame) {
						System.out.println(cartItem.getProductQty() + " " + cartItem.getProductName()+" in cart already");
						System.out.println("How many would you like to add?");
						int chosenQty = input.nextInt();
						for(Product stockItem: stockList) {
							if(chosenQty > stockItem.getProductQty() || chosenQty < 1) {
								System.out.println("Invalid quantity. There are only "+ stockItem.getProductQty() +" left in stock");
								System.out.println("Returning to shop menu");
								return;
							}
							else {
								cartItem.setProductQty(cartItem.getProductQty() + chosenQty);
								stockItem.setProductQty(stockItem.getProductQty() - chosenQty);
								shoppingCart.add(cartItem);
								System.out.println(chosenQty + " more " + cartItem.getProductName() +" added to your cart");
							}
						}
					}
				}
				
				
				
				
				for(Product stockItem: stockList) {
					if(chosenVideoGame == stockItem.getProductID() && stockItem.getProductType().equals("videogame")) {
						isFound = true;
						VideoGame cartItem = new VideoGame();
						cartItem.setProductID(stockItem.getProductID());
						cartItem.setProductName(stockItem.getProductName());
						cartItem.setProductPrice(stockItem.getProductPrice());
						
						System.out.println("Please select how many you would like to purchase");
						int chosenQty = input.nextInt();
						if(chosenQty > stockItem.getProductQty()) {
							System.out.println("Invalid quantity. There are only "+ stockItem.getProductQty() +" left in stock");
							System.out.println("Returning to shop menu");
							return;
						}
						else {
							cartItem.setProductQty(chosenQty);
							stockItem.setProductQty(stockItem.getProductQty() - chosenQty);
							shoppingCart.add(cartItem);
							System.out.println(cartItem.getProductQty() + " "+ cartItem.getProductName() + " has been added to your cart");
						}
						
					}
					
				}
				if(isFound == false) {
					System.out.println("No item with ID number "+ chosenVideoGame + " was found");
					System.out.println("Returning to shop menu");
					}
				
				break;
			}
			default: {
				System.out.println("Error. Please select a valid option");
				break;
			}
		}
		
	}
	/**
	 * Menu method for adding new stock items, replenishing stock and viewing admin stats
	 */
	private static void stockMenu() {
		System.out.println("**********Stock Menu**********");
		System.out.println("Please select an option");
		System.out.println("\t 1 to add a new stock item");
		System.out.println("\t 2 to view all stock items");
		System.out.println("\t 3 to replenish stock");
		System.out.println("\t 4 to view admin stats");
		System.out.println("\t S to save your data");
		System.out.println("\t M to return to main menu");
		
		switch(input.next().toUpperCase()) {
			case "1": {
				try {
					addStockItem();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error logged");
				}
				break;
			}
			case "2": {
				printStockList();
				break;
			}
			case "3": {
				replenishStock();
				break;
			}
			case "4": {
				viewAdminStats();
				break;
			}
			case "S": {
				try {
					saveData();
				} catch (Exception e) {
					System.out.println("Error logged.");
				}
				break;
			}
			case "M": {
				mainMenu();
				break;
			}
		}
		stockMenu();
		
	}
	/**
	 * For viewing admin stats
	 */
	private static void viewAdminStats() {
		System.out.println("**********Admin Stats**********");
		System.out.println("Total Takings: "+ currency.format(totalTakings));
		System.out.println("Total Transactions: "+ totalTransactions);
		System.out.println("Average sale per transaction: " + currency.format(totalTakings / totalTransactions));
	}
	/**
	 * For refilling stock levels
	 */
	private static void replenishStock() {
		System.out.println("Press R to reset all stock to a quantity of 50");
		
		if(input.next().toUpperCase() == "R") {
			for(Product p: stockList) {
				p.setProductQty(50);
			}
			System.out.println("Thank you. All stock has been replenished");
		}
		else {
			System.out.println("Invalid choice. Returning to stock menu");
		}
	}
	/**
	 * For selecting which type of item to add to stock
	 * @throws Exception
	 */
	private static void addStockItem() throws Exception {
		System.out.println("Please select which type of product you wish to add to the stock list");
		System.out.println("\t 1 for a jacket");
		System.out.println("\t 2 for a videogame");
		
		switch(input.next()) {
			case "1": {
				addNewJacket();
				break;
			}
			case "2": {
				addNewVideoGame();
				break;
			}
			default: {
				System.out.println("Invalid choice. Returning to stock menu");
				break;
			}
		}
		
	}

	/**
	 * For adding a new VideoGame object to stock list
	 * @throws Exception
	 */
	private static void addNewVideoGame() throws Exception {
		VideoGame vg = new VideoGame();
		
		vg.setProductID(centralProductID);
		centralProductID++;
		vg.setProductType("videogame");
		System.out.println("Please enter the videogame's product name");
		vg.setProductName(reader.readLine());
		System.out.println("Please enter the videogame's price");
		vg.setProductPrice(input.nextDouble());
		System.out.println("Please enter the quantity of videogames");
		vg.setProductQty(input.nextInt());
		System.out.println("Please enter the videogame's platform");
		vg.setVideoGamePlatform(reader.readLine());
		System.out.println("Please enter the videogame's age rating");
		vg.setVideoGameRating(reader.readLine());
		
		stockList.add(vg);
		System.out.println("Thank you. New product " + vg.getProductName() + " has been added");
		
	}
	/**
	 * For adding a new Jacket to stock list
	 * @throws Exception
	 */
	private static void addNewJacket() throws Exception {
		Jacket j = new Jacket();
		
		j.setProductID(centralProductID);
		centralProductID++;
		j.setProductType("jacket");
		System.out.println("Please enter the jacket's product name");
		j.setProductName(reader.readLine());
		System.out.println("Please enter the jacket's price");
		j.setProductPrice(input.nextDouble());
		System.out.println("Please enter the quantity of jackets");
		j.setProductQty(input.nextInt());
		System.out.println("Please enter the jacket's colour");
		j.setJacketColour(reader.readLine());
		System.out.println("Please enter the jacket's size");
		j.setJacketSize(reader.readLine());
		
		stockList.add(j);
		System.out.println("Thank you. New product " + j.getProductName() + " has been added");
		
	}
	/**
	 * For saving the current stock list to an external file
	 * @throws Exception
	 */
	private static void saveData() throws Exception {
		FileOutputStream exportFile = new FileOutputStream("Products.ser");
		ObjectOutputStream write = new ObjectOutputStream(exportFile);
		write.writeObject(stockList);
		write.close();
		System.out.println("Your data has been saved!");
	}
	/**
	 * Loads external file to fill stock list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static void loadData() throws Exception {
		FileInputStream importFile = new FileInputStream("Products.ser");
		ObjectInputStream read = new ObjectInputStream(importFile);
		stockList = (ArrayList<Product>)read.readObject();
		read.close();
	}

	/**
	 * Displays all stock and their details
	 */
	private static void printStockList() {
		
		for(Product p: stockList) {
			if(p.getProductType().equals("jacket")) {
				Jacket j = (Jacket) p;
				System.out.print(j.getProductID()+ "\t");
				System.out.print(j.getProductName()+ "\t");
				System.out.print(currency.format(j.getProductPrice())+ "\t");
				System.out.print(j.getJacketColour()+ "\t");
				System.out.print(j.getJacketSize()+ "\t");
				System.out.println(j.getProductQty()+ "\t");
				
			}
			
			else {
				VideoGame vg = (VideoGame) p ;
				System.out.print(vg.getProductID()+ "\t");
				System.out.print(vg.getProductName()+ "\t");
				System.out.print(currency.format(vg.getProductPrice())+ "\t");
				System.out.print(vg.getVideoGamePlatform()+ "\t");
				System.out.print(vg.getVideoGameRating()+ "\t");
				System.out.println(vg.getProductQty()+ "\t");
			}
		}
	}
	/**
	 * For displaying all VideoGame products in stock
	 */
	private static void printVideoGames() {
		for(Product p: stockList) {
			if(p.getProductType().equals("videogame")) {
				VideoGame vg = (VideoGame) p;
				
				System.out.print(vg.getProductID()+ "\t");
				System.out.print(vg.getProductName()+ "\t");
				System.out.print(currency.format(vg.getProductPrice())+ "\t");
				System.out.print(vg.getVideoGamePlatform()+ "\t");
				System.out.println(vg.getVideoGameRating()+ "\t");
			}
		}
	}
	/**
	 * For displaying all Jacket objects in stock
	 */
	private static void printJackets() {
		for(Product p: stockList) {
			if(p.getProductType().equals("jacket")) {
				Jacket j = (Jacket) p;
				
				System.out.print(j.getProductID()+ "\t");
				System.out.print(j.getProductName()+ "\t");
				System.out.print(currency.format(j.getProductPrice())+ "\t");
				System.out.print(j.getJacketColour()+ "\t");
				System.out.println(j.getJacketSize()+ "\t");
			}
		}
	}
	/**
	 * For populating our ArrayList with pre-made objects
	 */
	private static void prePopulate() {
	
		VideoGame vg1 = new VideoGame(101,"Elder Scrolls VI", 60.00, 30, "videogame" , "Xbox", "R18+");
		VideoGame vg2 = new VideoGame(102, "Grand Theft Auto VI", 70.00, 50, "videogame", "PS5", "R18+");
		Jacket j1 = new Jacket(103, "Men's Leather Jacket", 35.00, 20, "jacket", "Black", "L");
		Jacket j2 = new Jacket(104, "Women's Denim Jacket", 45.00, 25, "jacket", "Blue", "S");
		
		
		stockList.add(vg1);
		stockList.add(vg2);
		stockList.add(j1);
		stockList.add(j2);
	}

}
