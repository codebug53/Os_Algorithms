import java.util.*;

public class Banker{
	public static int[] Available;
	public static int[][] Max;
	public static int[][] Allocation;
	public static int[][] Need;
	public static int P;
	public static int R;
	
	// Private helper method to calculate Need matrix
	private static void calculateNeed() {
		for (int i = 0; i < P; i++) {
			for (int j = 0; j < R; j++) {
				// Need = Max - Allocation
				Need[i][j] = Max[i][j] - Allocation[i][j];
			}
		}
	}
	
	public static void getSystemStateInput() {
		Scanner sc = new Scanner(System.in);
		
		// Get the dimensions for the data
		System.out.print("Enter the number of process(P): ");
		P = sc.nextInt();	
		
		// Corrected: Read R before initializing arrays
		System.out.print("Enter the number of resource types(R): ");
		R = sc.nextInt();
		
		// Initialize the data structures after reading R
		Available = new int[R];
		Allocation = new int[P][R];
		Max = new int[P][R];
		Need = new int[P][R];
		
		// 2. Get Available Vector
		System.out.println("\nEnter the AVAILABLE resource vector (e.g., 3 3 2):");
		for (int j = 0; j < R; j++) {
			System.out.printf("  Available[%d]: ", j);
			Available[j] = sc.nextInt(); // Corrected scanner variable to 'sc'
		}

		// 3. Get Max Matrix
		System.out.println("\nEnter the MAX resource matrix:");
		for (int i = 0; i < P; i++) {
			System.out.printf("  Max demand for Process P%d: ", i);
			for (int j = 0; j < R; j++) {
				Max[i][j] = sc.nextInt();
			}
		}

		// 4. Get Allocation Matrix
		System.out.println("\nEnter the ALLOCATION matrix:");
		for (int i = 0; i < P; i++) {
			System.out.printf("  Current allocation for Process P%d: ", i);
			for (int j = 0; j < R; j++) {
				Allocation[i][j] = sc.nextInt();
			}
		}
	}
	
	private static boolean isSafe() {
		calculateNeed();
		
		// Work vector is a copy of Available
		int[] work = Arrays.copyOf(Available, R);
		boolean[] finish = new boolean[P];
		ArrayList<Integer> safeSequence = new ArrayList<>();
		int count = 0; // Tracks the number of finished processes
		
		// Outer loop: Continues until all processes finish or no safe process is found
		while (count < P) {
			boolean found = false;
			
			// Inner loop: Check every process for safety
			for (int i = 0; i < P; i++) {
				
				// Only consider processes that haven't finished
				if (finish[i] == false) { 
					
					boolean canExecute = true;
					// Check if Need[i] <= Work (Need for all resources must be met)
					for (int j = 0; j < R; j++) {
						if (Need[i][j] > work[j]) {
							canExecute = false;
							break;
						}
					}
					
					// If all resource needs were met
					if (canExecute) {
						// Process executes and releases resources (Work = Work + Allocation[i])
						for (int k = 0; k < R; k++) {
							work[k] += Allocation[i][k];
						}
						safeSequence.add(i);
						finish[i] = true;
						found = true;
						count++;
					}
				}
			}
			
			// If we iterated through all processes and found no process that could finish
			if (found == false && count < P) {
				System.out.println("\nSystem is NOT in a safe state.");
				System.out.println("No safe sequence exists.");
				return false;
			}
		}
		
		// If the while loop completes, all processes finished
		System.out.println("\nSystem is in a SAFE state.");
		System.out.print("Safe sequence is: ");
		for (int i = 0; i < P; i++) {
			System.out.print("P" + safeSequence.get(i) + (i == P - 1 ? "" : " -> "));
		}
		System.out.println();
		
		// Return true at the end of the method
		return true;
	}
	
	public static void main(String args[]) {
		System.out.println("--- Banker's Algorithm: Safety Check ---");
		getSystemStateInput();
		isSafe();
	}
}
