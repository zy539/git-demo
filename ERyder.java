public class ERyder {
    // Public constants 
    public static final String COMPANY_TITLE = "ERyder";
    public static final double MINIMUM_FARE = 1.0;
    public static final double RATE_PER_MINUTE = 0.5;

    // Final user credentials - initialized in constructors
    private final String USER_ACCOUNT;
    private final String CONTACT_NUMBER;

    // Usage statistics
    private int rideDuration;
    private double chargesAccumulated;

    // Bike specifications
    private String cycleNumber;
    private int powerRemaining;
    private boolean readyToRent;
    private double mileageRecorded;

    /**
     * Constructor Type A - With user account details
     * @param id Bike identifier
     * @param power Current battery percentage
     * @param status Availability flag
     * @param distance Total kilometers traveled
     * @param account User's account name
     * @param phone User's contact number
     */
    public ERyder(String id, int power, boolean status, double distance,
                  String account, String phone) {
        this.cycleNumber = id;
        this.powerRemaining = power;
        this.readyToRent = status;
        this.mileageRecorded = distance;
        this.USER_ACCOUNT = account;
        this.CONTACT_NUMBER = phone;
        this.rideDuration = 0;
        this.chargesAccumulated = 0.0;
    }

    /**
     * Constructor Type B - With default user (uses "guest" account)
     * @param id Bike identifier
     * @param power Current battery percentage
     * @param status Availability flag
     * @param distance Total kilometers traveled
     */
    public ERyder(String id, int power, boolean status, double distance) {
        this(id, power, status, distance, "guest_user", "000-000-0000");
    }

    /**
     * Internal fare computation
     * @param minutes Duration of ride
     * @return Total charges including base fare
     */
    private double computeCharges(int minutes) {
        return MINIMUM_FARE + (RATE_PER_MINUTE * minutes);
    }

    /**
     * Display complete trip information
     * @param minutes Duration of completed ride
     */
    public void showTripSummary(int minutes) {
        this.rideDuration = minutes;
        this.chargesAccumulated = computeCharges(minutes);

        System.out.println("╔════════════════════════════════╗");
        System.out.println("║    " + COMPANY_TITLE + " RIDE SUMMARY         ║");
        System.out.println("╠════════════════════════════════╣");
        System.out.println("║ Account: " + padRight(USER_ACCOUNT, 18) + "    ║");
        System.out.println("║ Phone:   " + padRight(CONTACT_NUMBER, 18) + "    ║");
        System.out.println("║ Bike ID: " + padRight(cycleNumber, 18) + "    ║");
        System.out.println("║ Duration: " + padRight(minutes + " minutes", 18) + "   ║");
        System.out.println("║ Fare:    $" + padLeft(String.format("%.2f", chargesAccumulated), 17) + "    ║");
        System.out.println("╚════════════════════════════════╝");
    }

    /**
     * Helper method for formatting output
     */
    private String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }

    /**
     * Helper method for formatting output
     */
    private String padLeft(String text, int length) {
        return String.format("%" + length + "s", text);
    }

    /**
     * Display basic bike information
     */
    public void displayBikeInfo() {
        System.out.println("── Bike " + cycleNumber + " ──");
        System.out.println("Power: " + powerRemaining + "% | " + 
                          (readyToRent ? "Available" : "In Use") + 
                          " | Mileage: " + mileageRecorded + "km");
    }

    // Accessor methods
    public double getCurrentCharges() {
        return chargesAccumulated;
    }

    public int getRideDuration() {
        return rideDuration;
    }

    public static void main(String[] args) {
        System.out.println("\n" + COMPANY_TITLE + " - Electric Bike Sharing Demo\n");

        // Test Case 1: User with complete information
        System.out.println(" Scenario 1: Registered User");
        ERyder member = new ERyder("EB-101", 95, true, 125.8, 
                                    "sarah_wilson", "555-123-4567");
        member.displayBikeInfo();
        member.showTripSummary(25);  // 25-minute ride

        // Test Case 2: Guest user (default account)
        System.out.println(" Scenario 2: Guest User");
        ERyder guest = new ERyder("EB-202", 80, true, 67.3);
        guest.displayBikeInfo();
        guest.showTripSummary(15);  // 15-minute ride

        // Demonstrate private method access restriction
        System.out.println("\n══════════════════════════════════");
        System.out.println(" KEY OBSERVATIONS:");
        System.out.println("──────────────────────────────────");
        System.out.println("• The computeCharges() method is PRIVATE");
        System.out.println("• Direct call: member.computeCharges(25) → COMPILE ERROR!");
        System.out.println("• Correct usage: Called internally by showTripSummary()");
        System.out.println("• Private methods act as internal helpers");
        System.out.println("══════════════════════════════════\n");
    }
}