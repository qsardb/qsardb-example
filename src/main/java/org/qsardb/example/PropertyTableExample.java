/*
 * Copyright (c) 2013 University of Tartu
 */
package org.qsardb.example;

import java.io.*;
import java.util.*;

import org.qsardb.cargo.map.*;
import org.qsardb.model.*;

import org.apache.commons.io.*;

public class PropertyTableExample {

	static
	public void main(String... args) throws Exception {

		if(args.length < 2 || args.length > 3){
			System.err.println("Usage: java " + PropertyTableExample.class.getName() + " <QDB file or directory> <Property identifier> <Compound identifiers file>?");

			System.exit(-1);
		}

		File qdbFile = new File(args[0]);

		String propertyId = args[1];

		List<String> compoundIds = new ArrayList<String>();

		// Read from File
		if(args.length > 2){
			File compoundIdsFile = new File(args[2]);

			InputStream is = new FileInputStream(compoundIdsFile);

			try {
				compoundIds.addAll(IOUtils.readLines(is, "UTF-8"));
			} finally {
				is.close();
			}
		} else

		// Read from System in
		{
			compoundIds.addAll(IOUtils.readLines(System.in, "UTF-8"));
		}

		Storage input = StorageUtil.openInput(qdbFile);

		try {
			Qdb qdb = new Qdb(input);

			try {
				Property property = qdb.getProperty(propertyId);
				if(property == null){
					throw new IOException("Property " + propertyId + " not found");
				}

				ValuesCargo valuesCargo = property.getCargo(ValuesCargo.class);

				Map<String, String> values = valuesCargo.loadStringMap();

				for(String compoundId : compoundIds){
					Compound compound = qdb.getCompound(compoundId);

					if(compound == null){
						throw new IOException("Compound " + compoundId + " not found");
					}

					String smiles = null;

					if(compound.hasCargo("smiles")){
						Cargo<Compound> smilesCargo = compound.getCargo("smiles");

						smiles = smilesCargo.loadString();
					}

					System.out.println(compound.getId() + "\t" + compound.getName() + "\t" + formatCell(compound.getInChI()) + "\t" + formatCell(smiles) + "\t" + values.get(compound.getId()));
				}
			} finally {
				qdb.close();
			}
		} finally {
			input.close();
		}
	}

	static
	private String formatCell(String value){
		return (value != null ? value : "");
	}
}