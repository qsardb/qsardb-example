/*
 * Copyright (c) 2012 University of Tartu
 */
package org.qsardb.example;

import java.io.*;

import org.qsardb.model.*;

public class CopyExample {

	static
	public void main(String... args) throws Exception {

		if(args.length != 2){
			System.err.println("Usage: java " + CopyExample.class.getName() + " <QDB input file or directory> <QDB output file or directory>");

			System.exit(-1);
		}

		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);

		if(outputFile.exists()){
			throw new IOException("Path " + outputFile.getCanonicalPath() + " already exists");
		}

		String name = outputFile.getName();

		if(name.indexOf('.') > -1){
			outputFile.createNewFile();
		} else

		{
			outputFile.mkdirs();
		}

		copyQdb(inputFile, outputFile);
	}

	static
	private void copyQdb(File inputFile, File outputFile) throws Exception {
		Storage input = StorageUtil.openInput(inputFile);

		try {
			Qdb qdb = new Qdb(input);

			try {
				Storage output = StorageUtil.openOutput(outputFile);

				try {
					qdb.copyTo(output);
				} finally {
					output.close();
				}
			} finally {
				qdb.close();
			}
		} finally {
			input.close();
		}
	}
}