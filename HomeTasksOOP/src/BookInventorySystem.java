import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

public class BookInventorySystem {
    private static HashMap<String, Book> inventory = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String filePath = "C:\\Users\\Ghost Hunter\\eclipse-workspace1\\HomeTasksOOP\\details.txt";

    public static void main(String[] args) {
        loadInventoryFromFile();

        int choice;
        do {
            displayMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();
            try {
                handleChoice(choice);
            } catch (BookNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (choice != 7);

        saveInventoryToFile();
    }


    private static void displayMenu() {
        System.out.println("\nBook Inventory System");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Update Book");
        System.out.println("4. Search Book by Title");
        System.out.println("5. Search Book by Author");
        System.out.println("6. Show Book List");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleChoice(int choice) throws BookNotFoundException {
        switch (choice) {
            case 1:
                addBook();
                break;
            case 2:
                removeBook();
                break;
            case 3:
                updateBook();
                break;
            case 4:
                searchBookByTitle();
                break;
            case 5:
                searchBookByAuthor();
                break;
            case 6:
                showBookList();
                break;
            case 7:
                System.out.println("Exiting Book Inventory System...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter book quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Book book = new Book(title, author, price, quantity);
        inventory.put(title, book);
        System.out.println("Book added successfully.");
    }

    private static void removeBook() throws BookNotFoundException {
        System.out.print("Enter book title to remove: ");
        String title = scanner.nextLine();
        Book book = inventory.get(title);
        if (book == null) {
            throw new BookNotFoundException("Book with title '" + title + "' not found.");
        } else {
            inventory.remove(title);
            System.out.println("Book removed successfully.");
        }
    }

    private static void updateBook() throws BookNotFoundException {
        System.out.print("Enter book title to update: ");
        String title = scanner.nextLine();
        Book book = inventory.get(title);
        if (book == null) {
            throw new BookNotFoundException("Book with title '" + title + "' not found.");
        } else {
            System.out.print("Enter new book title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new book author: ");
            String newAuthor = scanner.nextLine();
            System.out.print("Enter new book price: ");
            double newPrice = scanner.nextDouble();
            System.out.print("Enter new book quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setPrice(newPrice);
            book.setQuantity(newQuantity);
            inventory.remove(title);
            inventory.put(newTitle, book);
            System.out.println("Book updated successfully.");
        }
    }

    private static void searchBookByTitle() {
        System.out.print("Enter book title to search: ");
        String searchTitle = scanner.nextLine();
        Book foundBook = inventory.get(searchTitle);
        if (foundBook != null) {
            System.out.println("Book found:");
            System.out.println("Title: " + foundBook.getTitle() +
                               ", Author: " + foundBook.getAuthor() +
                               ", Price: $" + foundBook.getPrice() +
                               ", Quantity: " + foundBook.getQuantity());
        } else {
            System.out.println("Book with title '" + searchTitle + "' not found.");
        }
    }

    private static void searchBookByAuthor() {
        System.out.print("Enter book author to search: ");
        String searchAuthor = scanner.nextLine();
        ArrayList<Book> searchResults = new ArrayList<>();
        for (Book book : inventory.values()) {
            if (book.getAuthor().equalsIgnoreCase(searchAuthor)) {
                searchResults.add(book);
            }
        }
        if (searchResults.isEmpty()) {
            System.out.println("No books found by the author '" + searchAuthor + "'.");
        } else {
            System.out.println("Search results for author '" + searchAuthor + "':");
            for (Book book : searchResults) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    private static void showBookList() {
        if (inventory.isEmpty()) {
            System.out.println("The book inventory is empty.");
        } else {
            System.out.println("Book List:");
            for (Book book : inventory.values()) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Price: $" + book.getPrice() + ", Quantity: " + book.getQuantity());
            }
        }
    }

    private static void loadInventoryFromFile() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] bookDetails = fileScanner.nextLine().split(",");
                if (bookDetails.length == 4) {
                    String title = bookDetails[0].trim();
                    String author = bookDetails[1].trim();
                    double price = Double.parseDouble(bookDetails[2].trim());
                    int quantity = Integer.parseInt(bookDetails[3].trim());
                    Book book = new Book(title, author, price, quantity);
                    inventory.put(title, book);
                }
            }
            fileScanner.close();
            System.out.println("Book inventory loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private static void saveInventoryToFile() {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Book book : inventory.values()) {
                writer.println(book.getTitle() + "," + book.getAuthor() + "," + book.getPrice() + "," + book.getQuantity());
            }
            System.out.println("Book inventory saved to file.");
        } catch (FileNotFoundException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }

}
