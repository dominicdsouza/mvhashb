package com.hash.mvhashb;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class mvhashb {
	/*
	 * Author: Knut Petter Åstebøl Date: 29th of July 2013 Version: 3.1.
	 *
	 * This prototype generates hashes of mvHash-B.
	 *
	 */

	public static void main(String[] args) {
//		int c;

		int width = 80; // The size of the neighbourhood. Default value.
		int elements_pr_bf = 2048; // Elements pr Bloom filter. Default value.
		int threshold = 8; // Majority vote threshold. Default value.

		int c_flag = 1;
		int d_flag = 0;
		int e_flag = 0;
		int g_flag = 0;
		int t_flag = 0;
		int w_flag = 0;
		int v_flag = 0;
		String dirPath = "D:/DocDeduplication/samples";
		String filePaths = "";

		// Parse the command line options.
//		int opterr = 1; // Print default error messages when parsing the command line options.
		// -c generate hashes and compare all files in specified directory, the results of the comparison will be printed to screen.
		// -g generate hashes and compare all specified files, the results of the comparison will be printed to screen.
		// -e - Elements pr Bloom filter.
		// -h - HELP
		// -t - THRESHOLD
		// -v
		// -w - WIDTH
		// for (int i = 0; i < args.length; i++)
		// // while ((c = getopt(argc, args, "cde:ghw:t:v")) != -1)
		// {
		// switch (args[i]) {
		// case "-c":
		// c_flag = 1;
		// dirPath = args[i + 1];
		// break;
		// case "-d":
		// threshold = 7;
		// width = 20;
		// d_flag = 1;
		// break;
		// case "-e":
		// elements_pr_bf = Integer.parseInt(args[i + 1]);
		// e_flag = 1;
		// break;
		// case "-g":
		// g_flag = 1;
		// filePaths = args[i + 1];
		// break;
		// case "-h":
		// System.out.println("HELP!!");
		// System.exit(1);
		// // help_and_exit();
		// break;
		// case "-t":
		// t_flag = 1;
		// if (Integer.parseInt(args[i + 1]) != 0) {
		// threshold = Integer.parseInt(args[i + 1]);
		// }
		// break;
		// case "-v":
		// v_flag = 1;
		// break;
		// case "-w":
		// w_flag = 1;
		// if (Integer.parseInt(args[i + 1]) != 0) {
		// width = Integer.parseInt(args[i + 1]);
		// }
		// break;
		// default:
		// System.exit(1);
		// break;
		// }
		// }

		// Check whether invalid combinations of flags are used.
		if (c_flag != 0 && g_flag != 0) {
			System.out.print("Error: Both -c and -g are specified. \n");
			System.exit(1);
		}

		// It's not allowed to configure the parameters using both d-flag and other flags for configuration as the configuration may
		// be ambiguous.
		if (d_flag != 0 && (e_flag != 0 || t_flag != 0 || w_flag != 0)) {
			System.out.print("Error: The -d flag may not be used in combination with -e, -t or -w. \n");
			System.exit(1);
		}

		// This program can run in three ways:
		// -c generate hashes and compare all files in specified directory, the results of the comparison will be printed to screen.
		// -g generate hashes and compare all specified files, the results of the comparison will be printed to screen.
		// If neither -g or -c are specified, print the base64 representation of the hash of the specified files to terminal.

		// If the c-flag is specified, generate hash of all files in specified directory and do all-against-all comparison of these
		// hashes.
		if (c_flag == 1) {

			File folder = new File(dirPath);
			if (StringUtils.isBlank(dirPath) || !folder.exists()) {
				// if ((argc - optind) < 1)
				// {
				System.out.print("Error: You must specify a valid directory or digest file when using the -c flag. \n");
				System.exit(1);
			}

			// Handle the situation different depending on whether it is specified a directory or a digest file.
			// stat s = new stat();
			// stat(args[optind], s);

			// If a directory is specified.
			if (folder.isDirectory()) {
//				String dot = ".";
				int count = 0;

				// Count the number of files in specified directory.
				// The way to identify the files in the directory is copied from
				// http://stackoverflow.com/questions/612097/how-can-i-get-a-list-of-files-in-a-directory-using-c-or-c
//				File dir;
				// dirent ent;
				File[] folderFiles = folder.listFiles(new FileFilter() {

					public boolean accept(File file) {
						if (file.getName().startsWith(".")) {
							return false;
						}
						if (file.isFile() && !file.isHidden()) {
							return true;
						}
						return false;
					}

				});
				count = folderFiles.length;

				if (count <= 0) {
					System.exit(1);
				}

				// An array for storing the names of the files in the directory.
				// C++ TO JAVA CONVERTER TODO TASK: Java does not have an equivalent to pointers to value types:
				// ORIGINAL LINE: sbyte **string_array = malloc(sizeof(sbyte *) * count);
				String[] string_array = new String[count];

				// String String = new String(new char[500]);
//				int i = 0;

				// Identify the files within specified directory and copy them into string_array.
				for (int j = 0; j < folderFiles.length; j++) {
					string_array[j] = folderFiles[j].getAbsolutePath();
				}

				// uint8_t-array to store the files.
				FlexiFileStream[] files;
				files = new FlexiFileStream[(count)];

				// integer-array to store the size of the files
				int[] file_size;
				file_size = new int[count];

				// uint8_t-array to store the hashes in an uint8_t-array.
				int[][] hashes;
				hashes = new int[count][];

				// integer-array to store the size of the hashes
				int[] hash_size;
				hash_size = new int[count];

				int counter;

				// Copy the files into uint8-arrays and hash them.
				for (counter = 0; counter < count; counter++) {

					// Copy the file into an uint8-array.
					files[counter] = unit8.file_to_uint8(string_array[counter]);
					try {
						file_size[counter] = Math.toIntExact(files[counter].getChannel().size());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (files[counter] == null) {
						// The file name is not valid.
						hashes[counter] = null;
					}

					else {
						// This function hashes the files.
						hashes[counter] = mvc.majority_vote_combined(files[counter], file_size[counter], width, threshold,
								elements_pr_bf);
						hash_size[counter] = hashes[counter].length;
					}

					if ((hashes[counter] == null) && (v_flag == 1)) {
						System.out.printf("%s: Error. File too short or invalid filename. \n", string_array[counter]);
					}

				}

				// Do the all-against-all comparison of the hashes and print the results to screen.
				int j;
				double score;
				for (counter = 0; counter < count; counter++) {
					if (hashes[counter] == null) {
						// Do nothing.
					}

					else {
						for (j = counter + 1; j < count; j++) {
							if (hashes[j] == null) {
							} // Do nothing.

							else {
								score = match.new_match_bloom(hashes[counter], hash_size[counter], hashes[j], hash_size[j],
										elements_pr_bf);
								System.out.printf("%s|%s|%s\n", string_array[counter], string_array[j], score);
							}
						}
					}
				}

			}

			// else // It's not a directory, it's a digest file. Process the digest file.
			// {}

		}

		else {

			String[] filesArr = filePaths.split(",");

			// Do the operations that are common for g-flag and 'no-flag'.
			// uint8_t-array to store the files.
			FlexiFileStream[] files;
			files = new FlexiFileStream[filesArr.length];

			// integer-array to store the size of the files
			int[] file_size;
			file_size = new int[filesArr.length];

			// uint8_t-array to store the hashes in an uint8_t-array.
			int[][] hashes;
			hashes = new int[filesArr.length][];

			// integer-array to store the size of the hashes
			int[] hash_size;
			hash_size = new int[filesArr.length];

			// Step through all the files and generate their corresponding hashes.
			int index;

			for (index = 0; index < filesArr.length; index++) {
				// Copy the file into an uint8_t-array.

				files[index] = unit8.file_to_uint8(filesArr[index]);
				try {
					file_size[index] = Math.toIntExact(files[index].getChannel().size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (files[index] == null) {
					// The file name is not valid.
					hashes[index] = null;
				}

				else {
					// This function performs majority vote, RLE, and generates Bloom filters.
					hashes[index] = mvc.majority_vote_combined(files[index], file_size[index], width, threshold, elements_pr_bf);
				}

				if ((hashes[index] == null) && (v_flag == 1)) {
					System.out.printf("%s: Error. File too short or invalid filename. \n", filesArr[index]);
				}

			}

			if (g_flag == 1) {
				// Do the g_flag operations (all-against-all comparison of the specified files).
				// Check whether at least two files are specified.
				if (filesArr.length < 2) {
					System.out.print("Error: You must specify at least 2 files when using the -g flag. \n");
					System.exit(1);
				}

				// Do the all-against-all comparison and print the results.
				int j;
				double score;
				for (index = 0; index < filesArr.length; index++) {
					if (hashes[index] == null) {
					} // Do nothing.

					else {
						for (j = index + 1; j < filesArr.length; j++) {
							if (hashes[j] == null) {
							} // Do nothing.
							else {
								score = match.new_match_bloom(hashes[index], hash_size[index], hashes[j], hash_size[j],
										elements_pr_bf);
								if (score >= 1) {
									System.out.printf("%s|%s|%d\n", filesArr[index], filesArr[j], score);
								}
							}

						}
					}
				}
			}

			else {
				// Do the no-flag operation; print to terminal the base64 representation of the hash of the specified files.
				// Check that at least one file is specified.
				if (filesArr.length < 1) {
					System.out.print("Error: Specify at least one file. \n");
					System.exit(1);
				}
				// Print the hashes to screen
				for (index = 0; index < filesArr.length; index++) {

					// Print the hash if a valid hash is generated.
					if (hashes[index] == null) {
						// Do nothing.
					} else {
						System.out.printf("%s:", filesArr[index]);
						// uint8_array_print_hex(hashes[index], hash_size[index]);

						// Prints the base64 representation of the hash.
						unit8.uint8_array_print_base64(hashes[index], hash_size[index]);
						System.out.print("\n");
					}

				}

			}

		}

		// return 1;
	}

}