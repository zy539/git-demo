import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class UserRegistration {
    // Constants for VIP discounts and fees
    private static final double VIP_DISCOUNT_UNDER_18_BIRTHDAY = 25.0;
    private static final double VIP_DISCOUNT_UNDER_18 = 20.0;
    private static final double VIP_BASE_FEE = 100.0;
    
    // User personal information
    private String fullName;
    private String emailAddress;
    private String dateOfBirth;
    private long cardNumber;
    private String cardProvider;
    private String cardExpiryDate;
    private double feeToCharge;
    private int cvv;
    
    // User type and validation flags
    private String userType;
    private boolean emailValid;
    private boolean minorAndBirthday;
    private boolean minor;
    private boolean ageValid;
    private boolean cardNumberValid;
    private boolean cardStillValid;
    private boolean validCVV;
    
    // Scanner for user input
    private Scanner scanner;
    
    // Default constructor
    public UserRegistration() {
        this.scanner = new Scanner(System.in);
        this.minorAndBirthday = false;
        this.minor = false;
    }
    
    // Main registration method
    public void registration() {
        // Step 1: Get user type choice
        System.out.println("Welcome to the ERyder Registration.\r\n" + //
                        "Here are your two options:");
        System.out.println("1. Register as a Regular User");
        System.out.println("2. Register as a VIP User");
        System.out.print("Please enter your choice (1 or 2): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (choice == 1) {
            userType = "Regular User";
        } else if (choice == 2) {
            userType = "VIP User";
        } else {
            System.out.println("Invalid choice. Please try again.\n");
            registration();
            return;
        }
        
        // Step 2: Get fullname
        System.out.print("\nEnter your full name: ");
        fullName = scanner.nextLine();
        
        // Step 3: Get email address and validate
        System.out.print("Enter your email address: ");
        emailAddress = scanner.nextLine();
        emailValid = analyseEmail(emailAddress);
        
        // Step 4: Get date of birth and validate
        System.out.print("Enter your date of birth (YYYY-MM-DD): ");
        dateOfBirth = scanner.nextLine();
        LocalDate dob = LocalDate.parse(dateOfBirth);
        ageValid = analyseAge(dob);
        
        // Step 5: Get card number and validate
        System.out.print("Enter your card number (Visa, MasterCard, or American Express only): ");
        cardNumber = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        cardNumberValid = analyseCardNumber(cardNumber);
        
        // Step 6: Get card expiry date and validate
        System.out.print("Enter card expiry date (MM/YY): ");
        cardExpiryDate = scanner.nextLine();
        cardStillValid = analyseCardExpiryDate(cardExpiryDate);
        
        // Step 7: Get CVV and validate
        System.out.print("Enter card CVV: ");
        cvv = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        validCVV = analyseCVV(cvv);
        
        // Step 8: Final checkpoint
        finalCheckpoint();
    }
    
    // Email validation method
    private boolean analyseEmail(String email) {
        if (email.contains("@") && email.contains(".")) {
            System.out.println("Email is valid");
            return true;
        } else {
            System.out.println("Invalid email address. Going back to the start of the registration");
            registration();
            return false;
        }
    }
    
    // Age validation method
    private boolean analyseAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dob, currentDate);
        int years = age.getYears();
        
        // Check if today is user's birthday
        boolean isBirthday = (dob.getMonth() == currentDate.getMonth() && 
                             dob.getDayOfMonth() == currentDate.getDayOfMonth());
        
        // VIP User specific checks
        if (userType.equals("VIP User")) {
            // Check for birthday + under 18 (but over 12)
            if (isBirthday && years <= 18 && years > 12) {
                System.out.println("\nHappy Birthday!");
                System.out.println("You get 25% discount on the VIP subscription fee for being born today and being under 18!");
                minorAndBirthday = true;
            }
            // Check for under 18 (but over 12) - not birthday
            else if (!isBirthday && years <= 18 && years > 12) {
                System.out.println("\nYou get 20% discount on the VIP subscription fee for being under 18!");
                minor = true;
            }
        }
        
        // Check for invalid age (too young or too old)
        if (years <= 12 || years > 120) {
            System.out.println("\nLooks like you are either too young or already dead. Sorry, you can't be our user. Have a nice day");
            System.exit(0);
        }
        
        return true;
    }
    
    // Card number validation method
    private boolean analyseCardNumber(long cardNum) {
        String cardNumStr = Long.toString(cardNum);
        int length = cardNumStr.length();
        
        // Extract first two digits
        String firstTwoStr = cardNumStr.substring(0, 2);
        int firstTwoDigits = Integer.parseInt(firstTwoStr);
        
        // Extract first four digits
        String firstFourStr = cardNumStr.substring(0, 4);
        int firstFourDigits = Integer.parseInt(firstFourStr);
        
        // Check for VISA
        if ((length == 13 || length == 16) && cardNumStr.startsWith("4")) {
            cardProvider = "VISA";
            System.out.println("Card provider identified: VISA");
            return true;
        }
        // Check for MasterCard
        else if (length == 16 && 
                ((firstTwoDigits >= 51 && firstTwoDigits <= 55) ||
                 (firstFourDigits >= 2221 && firstFourDigits <= 2720))) {
            cardProvider = "MasterCard";
            System.out.println("Card provider identified: MasterCard");
            return true;
        }
        // Check for American Express
        else if (length == 15 && 
                (cardNumStr.startsWith("34") || cardNumStr.startsWith("37"))) {
            cardProvider = "American Express";
            System.out.println("Card provider identified: American Express");
            return true;
        }
        // Invalid card
        else {
            System.out.println("\nSorry, but we accept only VISA, MasterCard, or American Express cards. Please try again with a valid card.");
            System.out.println("Going back to the start of the registration.\n");
            registration();
            return false;
        }
    }
    
    // Card expiry date validation method
    private boolean analyseCardExpiryDate(String expiryDate) {
        // Extract month and year
        String monthStr = expiryDate.substring(0, 2);
        String yearStr = expiryDate.substring(3, 5);
        
        int month = Integer.parseInt(monthStr);
        int year = 2000 + Integer.parseInt(yearStr); // Convert YY to YYYY
        
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        
        // Check if card is still valid
        if (year > currentYear || (year == currentYear && month >= currentMonth)) {
            System.out.println("The card is still valid");
            return true;
        } else {
            System.out.println("\nSorry, your card has expired. Please use a different card.");
            System.out.println("Going back to the start of the registration process...\n");
            registration();
            return false;
        }
    }
    
    // CVV validation method
    private boolean analyseCVV(int cvvNum) {
        String cvvStr = Integer.toString(cvvNum);
        int length = cvvStr.length();
        
        // Check CVV based on card provider
        if ((cardProvider.equals("American Express") && length == 4) ||
            (cardProvider.equals("VISA") && length == 3) ||
            (cardProvider.equals("MasterCard") && length == 3)) {
            System.out.println("Card CVV is valid.");
            return true;
        } else {
            System.out.println("\nInvalid CVV for the given card.");
            System.out.println("Going back to the start of the registration process.\n");
            registration();
            return false;
        }
    }
    
    // Final checkpoint method
    private void finalCheckpoint() {
        // Check if all validations passed
        if (emailValid && ageValid && cardNumberValid && cardStillValid && validCVV) {
            chargeFees();
        } else {
            System.out.println("\nValidation failed. Please try again.");
            registration();
        }
    }
    
    // Charge fees method
    private void chargeFees() {
        // Apply discounts for VIP users
        if (userType.equals("VIP User")) {
            if (minorAndBirthday) {
                feeToCharge = VIP_BASE_FEE * (1 - VIP_DISCOUNT_UNDER_18_BIRTHDAY / 100);
            } else if (minor) {
                feeToCharge = VIP_BASE_FEE * (1 - VIP_DISCOUNT_UNDER_18 / 100);
            } else {
                feeToCharge = VIP_BASE_FEE;
            }
        } else {
            feeToCharge = 0.0; // Regular users pay no fee
        }
        
        // Get last 4 digits of card
        String cardNumStr = Long.toString(cardNumber);
        String lastFourDigits = cardNumStr.substring(cardNumStr.length() - 4);
        
        System.out.println("\nThank you for your payment.");
        System.out.printf("A fee of $%.2f has been charged to your card ending with ****%s\n", 
                         feeToCharge, lastFourDigits);
    }
    
    // toString method - called automatically when object is printed
    @Override
    public String toString() {
        String cardNumStr = Long.toString(cardNumber);
        int length = cardNumStr.length();
        
        // Mask the card number for privacy
        String censoredPart;
        if (length > 4) {
            censoredPart = cardNumStr.substring(0, length - 4).replaceAll(".", "*");
        } else {
            censoredPart = "";
        }
        
        String lastFourDigits = cardNumStr.substring(length - 4);
        String censoredNumber = censoredPart + lastFourDigits;
        
        // Build the output string
        StringBuilder result = new StringBuilder();
        result.append("\nRegistration successful! Here are your details:\n");
        result.append("User Type: ").append(userType).append("\n");
        result.append("Full Name: ").append(fullName).append("\n");
        result.append("Email Address: ").append(emailAddress).append("\n");
        result.append("Date of Birth: ").append(dateOfBirth).append("\n");
        result.append("Card Number: ").append(censoredNumber).append("\n");
        result.append("Card Provider: ").append(cardProvider).append("\n");
        result.append("Card Expiry Date: ").append(cardExpiryDate).append("\n");
        
        return result.toString();
    }
    
    // Close scanner method
    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}