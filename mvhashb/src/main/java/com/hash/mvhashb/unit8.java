package com.hash.mvhashb;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

public class unit8 {

	/// #include "base64.h"

	/*
	 * Function: file_to_uint8 Description: Copies a file to an uint8_t-array. If the filename is invalid, this function will return
	 * a null-pointer. Input: filename uint8_array - destination of the copy operation. Space for this array will be allocated by
	 * this function and must be freed by the caller. n - the size of the uint8_array will be stored here. Return: int.
	 *
	 */
	/*
	 * Author: Knut Petter Åstebøl Date: 29. December 2012 Version: 2.0.
	 *
	 */

	// Performs operations at an uint8_t string.

	/*
	 * Author: Knut Petter Åstebøl Date: 29. December 2012 Version: 2.0.
	 */

	// Performs operations at an uint8_t string.

	// Uint8_t-array operations.

	// Copies a file to an uint8_t-array.
	public static FlexiFileStream file_to_uint8(String filename) {
		File file = null;

		// Input validation.
		if (StringUtils.isBlank(filename)) {
			System.exit(1);
		}

		// Copy the file into uint8-array.
		file = new File(filename);

		if (file == null || !file.exists() || file.length() <= 0) {
			System.out.print("foobar\n");
			return null;
		}

		// n = UnsignedLong.valueOf(file.length());
		// C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
		try {
			return new FlexiFileStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// return 0;
		}

		// fread(uint8_array, 1, n.argValue, file);

		// Terminating
		// fclose(file);

		return null;
	}

	/*
	 * Function: uint8_array_inspect Description: Print the value of the uint8-array. Parameters: array - the array to be inspected.
	 * size - the number of elements in the array. Returns: void.
	 */

	// Inspects an uint8_t array.
	public static void uint8_array_inspect(byte[] array, int size) {

		int i;
		for (i = 0; i < size; i++) {
			System.out.printf("Array[%d]: %u.\n", i, array[i]);
		}

		return;
	}

	/*
	 * Function: uint8_array_print_hex Description: Prints the uint8-array as hex. Parameters: array - the array to be printed. size
	 * - the number of elements in the array. Returns: void.
	 */
	public static void uint8_array_print_hex(byte[] array, int size) {
		int i;
		for (i = 0; i < size; i++) {
			System.out.printf("%02X", array[i]);

		}
		return;
	}

	/*
	 * Function: uint8_array_print_base64 Description: Prints the uint8-array as base64. Parameters: array - the array to be
	 * printed. size - the number of elements in the array. Returns: void.
	 */
	public static void uint8_array_print_base64(int[] array, int size) {
		String output_base64;
//		int output_size;

		ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(array);

		output_base64 = Base64.getEncoder().encode(byteBuffer).asCharBuffer().toString();// base64(array, size, output_size);

		System.out.printf("%s", output_base64);

		return;
	}

	// Brian Kernighan’s Algorithm : Loops as many no.of 1's are present in number
	public static int numb_bits_in_byte(int number) {
		int count = 0;
		while (number > 0) { // loop and swap the last 1 bit in number. So loop till num > 0
			count++;
			number = number & number - 1;
		}
		return count;
	}

	/*
	 * Function: numb_bits_in_byte Description: Count the number of bits within a byte, using Brian Kernighan's way. Parameters: n -
	 * the byte Returns: The number of bits.
	 */

	// Bit-operations

	// public static byte numb_bits_in_byte(byte n) {
	// byte c; // The number of bits set in n
	//
	// for (c = 0; n != 0; c++) {
	// // This operation removes the least significant bit.
	// n = (n & (n - 1));
	// }
	//
	// return c;
	// }

	/*
	 * Function: file_to_uint16 Description: Copies a file to an uint16_t-array. Input: filename uint16_array - destination of the
	 * copy operation. Space for this array will be allocated by this function and must be freed by the caller. n - the size of the
	 * uint16_array will be stored here (in terms of the number of uint16 elements). Return: void.
	 *
	 */

	// Uint16_t-array operations.

	// public static void file_to_uint16(String filename, short[] uint16_array, int n) {
	// byte[] file_uint8 = 0; // The file as uint8.
	// int size_uint8; // The number of elements in the uint8-representation of the file.
	// int i;
	// int j;
	// short temp;
	//
	// int tempRef_size_uint8 = size_uint8;
	// file_to_uint8(filename, file_uint8, tempRef_size_uint8);
	// size_uint8 = tempRef_size_uint8;
	//
	// if ((size_uint8 % 2) == 1) {
	// size_uint8--;
	// }
	//
	// // C++ TO JAVA CONVERTER TODO TASK: The memory management function 'malloc' has no equivalent in Java:
	// uint16_array = new short[size_uint8];
	//
	// j = 0;
	// for (i = 0; i < size_uint8; i += 2) {
	// temp = 0;
	// temp = (short) file_uint8[i];
	// temp = temp << 8;
	// temp = temp | ((short) file_uint8[i + 1]);
	// uint16_array[j] = temp;
	// j++;
	// }
	//
	// n = (size_uint8 / 2);
	//
	// // Terminating
	// // C++ TO JAVA CONVERTER TODO TASK: The memory management function 'free' has no equivalent in Java:
	// // free(file_uint8);
	// file_uint8 = null;
	//
	// return;
	// }

}