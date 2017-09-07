package com.hash.mvhashb;

public class misc {

	/*
	 * Function: help_and_exit Description: This function prints the help function. Input - no input. Return: Void.
	 *
	 */
	/*
	 * Author: Knut Petter Åstebøl Date: 29th of July 2013 Version: 3.1.
	 */

	// Miscellaneous functions.

	/*
	 * Author: Knut Petter Åstebøl Date: 29. December 2012 Version: 2.0.
	 */

	// Miscellaneous functions.

	public static void help_and_exit() {
		System.out.print("mvHash 3.0, July 2013, Knut Petter Åstebøl, knutpettercom@gmail.com \n");
		System.out.print(
				"mvHash uses the configuration most suitable for compressed/encoded files if not configured using flags. \n");
		System.out.printf("%1s %-21s %s", "", "mvHash <files>", ": Generate hex-encoded hashes for all files. \n");
		System.out.printf("%2s %-20s %s", "", "-g <files>",
				": For all specified files, the hashes will be generated and compared. \n");
		System.out.printf("%2s %-20s %s", "", "-c <directory> <digest file>",
				": Specify either a digest file or a directory. It will be performed all-against-all comparison. \n");
		System.out.printf("%2s %-20s %s", "", "-h", ": Print this help instructions.\n");
		System.out.print("Use the flags below to alter the configuration of how mvHash hashes the files. \n");
		System.out.printf("%2s %-20s %s", "", "-d",
				": Uses the configuration (neighbourhood size, influencing bits and BFa) for text-files.\n");
		System.out.printf("%2s %-20s %s", "", "-v", ": Verbose.\n");
		System.out.printf("%2s %-20s %s", "", "-w <number>",
				": Alter the size of the neighbourhood to the specified value. Default: 50. \n");
		System.out.printf("%2s %-20s %s", "", "-t <number>",
				": Alter the value of the influencing bits to the specified value. Default: 8. \n");
		System.out.printf("%2s %-20s %s", "", "-e <number>",
				": Alter the value of the number of entries per Bloom filter (BFa) to the specified value. Default: 2048. \n");

		System.exit(1);
	}

	/*
	 * Function: exit_if_NULL Description: This function checks whether a pointer is null. If it is null print error message and
	 * exit the program. Otherwise, do nothing. Input: pointer - the pointer to be checked. Return: Void.
	 *
	 */

	public static void exit_if_NULL(Object pointer) {
		if (pointer == null) {
			System.err.println("Error: NULL-pointer used. Exiting. \n");
			System.exit(1);
		}
	}

	/*
	 * Function: max_value_in_bit Description: Calculates the maximum value (unsigned) it's possible to store within a specified
	 * number of bits. Input: numb - the number of bits. Return: The maximum value.
	 *
	 */

	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	// ORIGINAL LINE: uint max_value_in_bit(uint numb);
	public static int max_value_in_bit(int numb) {
		// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
		// ORIGINAL LINE: uint output = 1;
		int output = 1;
		int i;

		for (i = 0; i < (numb); i++) {
			output = output << 1;
		}

		output -= 1;

		return output;
	}

	// Useful macro to compute maximum value of a block of a specified size t in bytes.
	// C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
	// ORIGINAL LINE: #define umaxof(t) (((0x1ULL << ((t * 8ULL) - 1ULL)) - 1ULL) | (0xFULL << ((t * 8ULL) - 4ULL)))

}