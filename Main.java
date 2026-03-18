public class Main {
    public static void main(String[] args) {
        // Create an object of UserRegistration
        UserRegistration registration = new UserRegistration();
        
        // Call the registration method
        registration.registration();
        
        // Print the object - this will automatically call toString()
        System.out.println(registration);
        
        // Close the scanner
        registration.closeScanner();
    }
}