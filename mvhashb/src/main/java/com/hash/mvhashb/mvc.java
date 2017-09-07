package com.hash.mvhashb;

import java.util.Arrays;

public class mvc {

	/*
	 * Function: majority_vote_combined Description: Calculates the majority vote, the RLE, and optionally stores the output in a
	 * Bloom filter. Parameters: input - the input array. input_size - size of the input array in bytes. width - size of the
	 * neighbourhood. threshold - majority vote threshold. output_size - pointer to where the output size should be stored.
	 * elements_pr_bf - number of entries pr Bloom filter.
	 *
	 * Returns: The hash of mvHash, stored as mvHash-L or mvHash-B.
	 */
	/*
	 * Author: Knut Petter Åstebøl Date: 29. December 2012 Version: 2.0.
	 */

	/*
	 * Author: Knut Petter Åstebøl Date: 29. December 2012 Version: 2.0.
	 */

	// Performs the majority vote, RLE, and eventually adds the output of RLE to a Bloom filter.

	// C++ TO JAVA CONVERTER WARNING: Java has no equivalent to methods returning pointers to value types:
	// ORIGINAL LINE: byte *majority_vote_combined(byte *input, int input_size, int width, int threshold, int *output_size, int
	// elements_pr_bf)
	public static int[] majority_vote_combined(FlexiFileStream input, int input_size, int width, int threshold,
			int elements_pr_bf) {// , int output_size

		// Input validation, mvHash is not able to process very short files. Note, files that are longer than 50 bytes may still be
		// rejected below if they are too short for producing a hash.
		if (input_size < 50) {
			return null;
		}

		// clock_t start = clock();

		// C++ TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to pointers to value types:
		// ORIGINAL LINE: byte *sum_array = (byte *) malloc(input_size);
		// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
		int[] sum_array = new int[input_size]; // The sum-array is an array where the number of bits for each byte is counted.
		// C++ TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to pointers to value types:
		// ORIGINAL LINE: byte *output = (byte *) malloc(input_size);
		// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
		int[] output = new int[input_size];
		int n_size; // Real neighbourhood size.
		int ones_in_neighbourhood = 0; // The number of ones within the neighbourhood of the byte in focus.
		int i; // int n;
		int j;
		int count;
		int last_value;
		int current_value;
		int single_width = width / 2;

		int[] bits = new int[256];

		// Compute the bits table array.
		for (i = 0; i < 256; i++) {
			bits[i] = unit8.numb_bits_in_byte(i);
		}

		// Debug: Prints output of sum_array
		// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #if DEBUG
		// System.out.print("\nDebug: Prints the output of sum_array. \n");
		// byte[] tempRef_sum_array = new byte[sum_array.length];
		// uint8_array_inspect(tempRef_sum_array, input_size);
		// sum_array = tempRef_sum_array.argValue;
		/// #endif

		// Process first byte

		i = 0;
		j = 0;

		int n;
		ones_in_neighbourhood = 0;
		for (n = 0; n < (single_width + 1); n++) {
			// ones_in_neighbourhood += sum_array[n];
			ones_in_neighbourhood += bits[input.readLocationByte(n)];
		}

		n_size = 1 + single_width;

		if (ones_in_neighbourhood >= ((threshold * n_size) / 2)) {
			current_value = 1;
		} else {
			current_value = 0;
		}

		// RLE for first byte.
		if (current_value != 0) {
			output[0] = 0;
			j = 1;
			count = 1;
		}

		else // current_value = 0;
		{
			count = 1;
		}

		// Process even more bytes

		for (i = 1; i <= single_width; i++) {

			ones_in_neighbourhood += bits[input.readLocationByte(i + (single_width))];

			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("\n1s within neighbourhood of [%d]: %d. \n", i, ones_in_neighbourhood);
			/// #endif

			// Compute the size of the neighbourhood. The real size of the neighbourhood depends on the position of the byte within
			// the input string.
			// Bytes close to the beginning or end of the string has a shorter neighbourhood.
			n_size = mv_size_of_neighbourhood(input_size, width, i);

			// Print neighbourhood size.
			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("Neighbourhood_size: %d. \n", mv_size_of_neighbourhood(input_size, width, i));
			/// #endif

			// Based on the number of 1s within the neighbourhood, the output byte is set to
			// either 0 or 1.

			count = count % 256;
			last_value = current_value;
			if (ones_in_neighbourhood >= ((threshold * n_size) / 2)) {
				current_value = 1;
			} else {
				current_value = 0;
			}

			// Do the RLE.

			if (i == 0) {
				if (current_value != 0) {
					output[0] = 0;
					j = 1;
					count = 1;
				}

				else // current_value = 0;
				{
					count = 1;
				}
			}

			else if (current_value == last_value) {
				count++;
			}

			else if (current_value != last_value) {
				output[j] = count;
				count = 1;
				j++;
			}

			// This should not happen.
			else {
				System.out.print("MV-RLE: An error has occured. \n");
			}

		}

		// Process even more bytes

		for (; i < (input_size - single_width); i++) {
			ones_in_neighbourhood -= bits[input.readLocationByte(i - (single_width + 1))];

			ones_in_neighbourhood += bits[input.readLocationByte(i + (single_width))];

			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("\n1s within neighbourhood of [%d]: %d. \n", i, ones_in_neighbourhood);
			/// #endif

			n_size = (2 * single_width) + 1;

			// Print neighbourhood size.
			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("Neighbourhood_size: %d. \n", mv_size_of_neighbourhood(input_size, width, i));
			/// #endif

			// Based on the number of 1s within the neighbourhood and the neighbourhood size, the output byte is set to
			// either 0 or 1.

			count = count % 256;
			last_value = current_value;
			if (ones_in_neighbourhood >= ((threshold * n_size) / 2)) {
				current_value = 1;
			} else {
				current_value = 0;
			}

			// Do the RLE.

			if (current_value == last_value) {
				count++;
			}

			else if (current_value != last_value) {
				output[j] = count;
				count = 1;
				j++;
			}

			// This should not happen.
			else {
				System.out.print("MV-RLE: An error has occured. \n");
			}

		}

		// The last bytes

		for (; i < input_size; i++) {
			ones_in_neighbourhood -= bits[input.readLocationByte(i - (single_width + 1))];

			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("\n1s within neighbourhood of [%d]: %d. \n", i, ones_in_neighbourhood);
			/// #endif

			// Compute the size of the neighbourhood. The real size of the neighbourhood depends on the position of the byte within
			// the input string.
			// Bytes close to the beginning or end of the string has a shorter neighbourhood.
			n_size = mv_size_of_neighbourhood(input_size, width, i);

			// Print neighbourhood size.
			// C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #if DEBUG
			System.out.printf("Neighbourhood_size: %d. \n", mv_size_of_neighbourhood(input_size, width, i));
			/// #endif

			// Based on the number of 1s within the neighbourhood, the neighbourhood size and the byte size, the output byte is set
			// to
			// either 0 or 1.

			count = count % 256;
			last_value = current_value;
			if (ones_in_neighbourhood >= ((threshold * n_size) / 2)) {
				current_value = 1;
			} else {
				current_value = 0;
			}

			// Do the RLE.

			// Last run.
			if (i == (input_size - 1)) {
				if (current_value == last_value) {
					count++;
					output[j] = count;
				}

				else {
					output[j] = count;
					j++;
					output[j] = 1;
				}
			}

			else if (current_value == last_value) {
				count++;
			}

			else if (current_value != last_value) {
				output[j] = count;
				count = 1;
				j++;
			}

			// This should not happen.
			else {
				System.out.print("MV-RLE: An error has occured. \n");
			}

		}

		j++;
		int output_size = j;

		// If elements_pr_bf is not 0, store the output using Bloom filters.
		if (elements_pr_bf != 0) {

			if (j < 11) {
				// printf("Error: Too short RLE (file) for making a Bloom filter. Quitting: %i. \n", j);
				return null;

			}

			int number_of_entries;
			int signature_size; // Size of the signature in bytes.

			number_of_entries = ((j - 11) / 2) + 1;

			signature_size = number_of_entries / elements_pr_bf;
			if ((number_of_entries % elements_pr_bf) != 0) {
				signature_size++;
			}
			signature_size = signature_size * 256;

			// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
			int[] bloom_filter = new int[signature_size];
			int bloom_filter_number;
			int byte_pos;
			int bit_pos;

			int k;

			// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'memset' has no equivalent in Java:
			// memset(bloom_filter, 0, signature_size);
			Arrays.fill(bloom_filter, 0);

			bloom_filter_number = 0;
			for (n = 0; n <= (j - 11); n = n + 2) {

				bloom_filter_number = (n / 2) / elements_pr_bf;

				byte_pos = 0;
				for (k = 0; k < 8; k++) {
					byte_pos += (output[n + k] % 2) * ipow(2, 7 - k);
				}

				bit_pos = 0;
				for (; k < 11; k++) {
					bit_pos += (output[n + k] % 2) * ipow(2, 10 - k);
				}

				bit_pos = ipow(2, bit_pos);

				bloom_filter[bloom_filter_number * 256 + byte_pos] |= bit_pos;

			}

			output_size = signature_size;

			return bloom_filter;
		}

		// Terminating.
		// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'free' has no equivalent in Java:
		// free(sum_array);
		sum_array = null;

		return output;

	}

	/*
	 * Function: mv_most1s Description: Determine if there are most 0s or 1s within a specified number of bytes. If the number is
	 * identical, it will be treated as most 1s. Parameters: input - the number of 1s. size - the number of bytes. Returns: 0 if
	 * most 0s, 1 if most 1s.
	 */

	public static int mv_most1s(int input, int size, int threshold) {

		int max; // maximum value within the specified number of bytes.
		max = threshold * size;

		if (input >= (max / 2)) { // printf("Most 1s. \n");
			return 1;
		} else {
			// printf("Most 0s. \n");
			return 0;
		}
	}

	/*
	 * Function: mv_update_value Description: Computes the number of 1s within the neighbourhood of a specified byte. Parameters:
	 * sum_array - an array where each byte specify the number of 1s within the corresponding byte of the input. The first byte
	 * represents the first byte, etc. size - the size of the sum_array in bytes. width - the width of the neighbourhood in number
	 * of bytes. nr - the byte nr within the sum_array for which the output value is calculated. previous_value - the output value
	 * of previous byte. If nr = 0, this value doesn't matter. Returns: The number of 1s within the neighbourhood of byte nr from
	 * the input.
	 */

	public static int mv_update_value(byte[] sum_array, int size, int width, int nr, int previous_value) {
		int single_width = width / 2;
		int output = previous_value;
		int n = 0;

		if (nr != 0) {
			if ((nr - (single_width + 1)) >= 0) {
				output -= sum_array[nr - (single_width + 1)];
			}

			if ((nr + single_width) < size) {
				output += sum_array[nr + (single_width)];
			}
		}

		else {
			output = 0;
			for (n = 0; n < (single_width + 1); n++) {
				output += sum_array[n];
			}
		}

		return output;

	}

	/*
	 * Function: build_sum_array Description: This function generates an array named sum_array based on the input. The value of
	 * first byte in sum_array is the number of 1s within the first byte of the input, the second byte is the number of 1s within
	 * the second byte of the input, etc. Parameters: input - the input array. input_size - size of the input in bytes output_size -
	 * the output has smaller or equal size of the input, at this pointer the size of the output will be stored. Returns: An uint8_t
	 * output array. The array must be freed by the caller.
	 */

	// C++ TO JAVA CONVERTER WARNING: Java has no equivalent to methods returning pointers to value types:
	// ORIGINAL LINE: byte *build_sum_array(byte *input, int input_size)
	public static int[] build_sum_array(byte[] input, int input_size) {
		// clock_t start = clock();
		// C++ TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to pointers to value types:
		// ORIGINAL LINE: byte *output;
		int[] output;
		int i;
		int[] bits = new int[256];

		// Compute the bits table array.

		for (i = 0; i < 256; i++) {
			bits[i] = unit8.numb_bits_in_byte(i);
		}

		// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
		output = new int[input_size];

		// Step through all the bytes in the input.
		for (i = (input_size - 1); i >= 0; i--) {
			output[i] = bits[input[i]];
		}

		return output;
	}

	/*
	 * Function: mv_size_of_neighbourhood. Description: Calculates the size of the neighbourhood in bytes, for a specified byte in a
	 * specified input. The size of the neighbourhood is not obvious. For a byte in the start or the end of the input, the
	 * neighbourhood may go outside the input. This function calculates therefore the real number of bytes in the neighbourhood,
	 * including the byte in focus itself. Parameters: size - the number of bytes in the input. width - the width of the
	 * neighbourhood. nr - the byte nr within for which the neighbourhood size should be calculated. Returns: The number of bytes
	 * within the neighbourhood of the byte specified in the input.
	 */

	public static int mv_size_of_neighbourhood(int size, int width, int nr) {
		int single_width = width / 2;
		int output = single_width * 2 + 1;
		int i;

		i = single_width;

		// The size of the neighbourhood is reduced if the byte is close to the start of the input.
		while ((nr - i) < 0) {
			output--;
			i--;
		}

		// The size of the neighbourhood is reduced if the byte is close to the end of the input.
		i = single_width;
		while ((nr + i) >= size) {
			output--;
			i--;
		}

		return output;
	}

	// This function is picked from:
	// http://stackoverflow.com/questions/101439/the-most-efficient-way-to-implement-an-integer-based-power-function-powint-int

	public static int ipow(int base, int exp) {
		int result = 1;
		while (exp != 0) {
			if ((exp & 1) != 0) {
				result *= base;
			}
			// C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since
			// the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift
			// operator (>>>) is more appropriate:
			exp >>= 1;
			base *= base;
		}

		return result;
	}

}