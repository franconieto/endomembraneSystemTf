package immunity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.context.space.grid.ContextGrid;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.collections.IndexedIterable;
// This class contains the properties of the cell.  It is loaded with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class FreezeDryEndosomes {

//	public static final String configFilename = "config.json";
	
	private static FreezeDryEndosomes instance;
	//CAMBIO
	LocalPath mainpath=LocalPath.getInstance(); 
	String FreezeOutputPath = mainpath.getPathOutputFE(); 	
	
	public static FreezeDryEndosomes getInstance() {
		if( instance == null ) {
			instance = new  FreezeDryEndosomes();
			try {
				loadFromCsv();
//				loadOrganellePropertiesFromCsv(ModelProperties.getInstance());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return instance;
	}
	
	public static void loadFromCsv() throws IOException {

		Scanner scanner = new Scanner(new File(
//				"inputFrozenEndosomes.csv")); esto es cuando no es en Batch
//		ESTO ES PARA BATCH.  LEE LOS ENDOSOMAS DE UN FOLDER DATA RELATIVO.  El folder se llama "data" y all� est� 
//				tambi�n el file de input inputIntrTransp3.csv
				".//data//inputFrozenEndosomes.csv"));

		scanner.useDelimiter(",");

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
//			System.out.println("AQUI PARA b  "+b[0]);
			String subString = b[0].substring(0,2);
			switch (subString) {

			// INITIAL ORGANELLES kind 7 is for phagosomes

			case "ki":
			{
				break;
// if the first two letters are "en", load data for an endosome
			}
			case "en":
			{
				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				System.out.println("AQUI PARA b0  "+b[0]);
				inOr.getDiffOrganelles().add(b[0]);
//				System.out.println("AQUI PARA  "+b[1]);
				switch (b[1]) {
				case "initOrgProp": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitOrgProp().put(b[0], value);
					break;
				}
				case "initRabContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//						System.out.println("AQUI PARA  "+b[i]+" "+ b[i + 1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitRabContent().put(b[0], value);
					break;
				}
				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						if (!ModelProperties.getInstance().getSolubleMet().contains(b[i]))continue;
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitSolubleContent().put(b[0], value);
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						if (!ModelProperties.getInstance().getMembraneMet().contains(b[i]))continue;

//						System.out.println("VALOR MALO" + b[i] + "" + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitMembraneContent().put(b[0], value);
					break;
				}
				default: {
					System.out.println("no a valid entry");
				}
				}
				break;
			}
			case "pl":
			{
				switch (b[1]) {
				case "initOrgProp": {
					PlasmaMembrane.getInstance().setPlasmaMembraneArea(Double.parseDouble(b[3]));
					PlasmaMembrane.getInstance().setPlasmaMembraneVolume(Double.parseDouble(b[5]));
					break;
				}
				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//						if (!ModelProperties.getInstance().getInitPMsolubleRecycle().containsKey(b[i]))continue;
//						if (!ModelProperties.getInstance().getInitPMsolubleRecycle().get(b[i]).equals(0.0))continue;
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					PlasmaMembrane.getInstance().getSolubleRecycle().putAll(value);
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//						if (!ModelProperties.getInstance().getInitPMmembraneRecycle().containsKey(b[i]))continue;
//						if (!ModelProperties.getInstance().getInitPMmembraneRecycle().get(b[i]).equals(0.0))continue;

//						System.out.println("VALOR MALO PM " + b[i] + " " + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					PlasmaMembrane.getInstance().getMembraneRecycle().putAll(value);
					break;
				}
				default: {
					System.out.println("no a valid entry");
				}
				}
				break;
			}
			case "ER":
			{
				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				System.out.println("AQUI PARA b0  "+b[0]);
				inOr.getDiffOrganelles().add(b[0]);
//				System.out.println("AQUI PARA  "+b[1]);
				switch (b[1]) {
				case "initOrgProp": {
					EndoplasmicReticulum.getInstance().setEndoplasmicReticulumArea(Double.parseDouble(b[3]));
					EndoplasmicReticulum.getInstance().setEndoplasmicReticulumVolume(Double.parseDouble(b[5]));
					break;
				}

				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//						if (!ModelProperties.getInstance().getInitERsolubleRecycle().containsKey(b[i]))continue;
//						if (!ModelProperties.getInstance().getInitERsolubleRecycle().get(b[i]).equals(0.0))continue;
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					EndoplasmicReticulum.getInstance().getSolubleRecycle().putAll(value);
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
			//			if (!ModelProperties.getInstance().getInitERmembraneRecycle().containsKey(b[i]))continue;
			//			if (!ModelProperties.getInstance().getInitERmembraneRecycle().get(b[i]).equals(0.0))continue;

//						System.out.println("VALOR MALO PM " + b[i] + " " + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					EndoplasmicReticulum.getInstance().getMembraneRecycle().putAll(value);
					System.out.println("VALOR ERRRRRRRRRRRRR " + EndoplasmicReticulum.getInstance().getMembraneRecycle());

					break;
				}
				default: {
					System.out.println("no a valid entry");
				}
				}
				break;
			//			case "ER":
//			{
//				switch (b[1]) {
//				case "initOrgProp": {
//					EndoplasmicReticulum.getInstance().setEndoplasmicReticulumArea(Double.parseDouble(b[3]));
//					EndoplasmicReticulum.getInstance().setEndoplasmicReticulumVolume(Double.parseDouble(b[5]));
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						if (!ModelProperties.getInstance().getInitERsolubleRecycle().containsKey(b[i]))continue;
//						if (!ModelProperties.getInstance().getInitERsolubleRecycle().get(b[i]).equals(0.0))continue;
//
////						System.out.println("VALOR MALO ER " + b[i] + " " + b[i+1]);
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					EndoplasmicReticulum.getInstance().getSolubleRecycle().putAll(value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						if (!ModelProperties.getInstance().getInitERmembraneRecycle().containsKey(b[i]))continue;
//						if (!ModelProperties.getInstance().getInitERmembraneRecycle().get(b[i]).equals(0.0))continue;
//
////						System.out.println("VALOR MALO " + b[i] + " " + b[i+1]);
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					EndoplasmicReticulum.getInstance().getMembraneRecycle().putAll(value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
			}
			case "Cy":
			{
				switch (b[1]) {
				case "initOrgProp": {
					Cell.getInstance().setcellArea(Double.parseDouble(b[3]));
					Cell.getInstance().setcellVolume(Double.parseDouble(b[5]));
					break;
				}
				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						if (!ModelProperties.getInstance().getSolubleCell().containsKey(b[i]))continue;
						if (!ModelProperties.getInstance().getSolubleCell().get(b[i]).equals(0.0))continue;

//						System.out.println("VALOR MALO ER " + b[i] + " " + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					Cell.getInstance().getSolubleCell().putAll(value);
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						if (!ModelProperties.getInstance().getMembraneCell().containsKey(b[i]))continue;
						if (!ModelProperties.getInstance().getMembraneCell().get(b[i]).equals(0.0))continue;

//						System.out.println("VALOR MALO " + b[i] + " " + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					Cell.getInstance().getMembraneCell().putAll(value);
					break;
				}
				default: {
					System.out.println("no a valid entry");
				}
				}
				break;
			}
			default: {
				System.out.println("no a valid entry");
			}
			}

		}
		scanner.close();
//		System.out.println(frozenEndosomes.solubleMet);
//		System.out.println(frozenEndosomes.tubuleTropism);
	}
	
	
	
	public void writeToCsv() throws IOException {
		
		IndexedIterable<Endosome> collection = CellBuilder.getCollection();
//		System.out.println("ALL ENDOSOMES"+collection);
		int index = 0;
		Writer output;	
	    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		String line ="tick " + tick + "\n";
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
		output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
		output.append(line);
		output.close();
		for (Endosome endosome : collection) {
			line = "";
			String ch = RandomStringUtils.randomAlphabetic(1);
            line = line + "endosome"+index + ch + ",";
            line = line + "initOrgProp" + ",";
            line = line + "area" + "," + endosome.getArea()  + ",";
            line = line + "volume" + "," + endosome.getVolume() + ",";
            line = line + "xcoor" + "," + endosome.getXcoor() + ",";
            line = line + "ycoor" + "," + endosome.getYcoor() + ",";
		line = line + "\n";	
		output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
		output.append(line);
		line = "";
        line = line + "endosome"+index + ch + ",";
        String rabContent = endosome.getRabContent().toString().replace("=",",");
        rabContent = rabContent.replace("{","");
        rabContent = rabContent.replace("}","");
        rabContent = rabContent.replace(" ","");
        line = line + "initRabContent" + "," + rabContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";
		String membraneContent = endosome.getMembraneContent().toString().replaceAll("=",","); 
		membraneContent = membraneContent.replace("{","");
		membraneContent = membraneContent.replace("}","");
        membraneContent = membraneContent.replace(" ","");
        line = line + "initMembraneContent" + "," + membraneContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";        
        String solubleContent = endosome.getSolubleContent().toString().replaceAll("=",",");        
		solubleContent = solubleContent.replace("{","");
		solubleContent = solubleContent.replace("}","");
        solubleContent = solubleContent.replace(" ","");
        line = line + "initSolubleContent" + "," + solubleContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		index = index + 1;
		output.close();
	}
	}
		public void writeToCsvPM() throws IOException {
			
			PlasmaMembrane plasmaMembrane = PlasmaMembrane.getInstance();
//			System.out.println("ALL ENDOSOMES"+collection);
			Writer output;	
//		    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//			String line ="tick " + tick + "\n";
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
//			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output.append(line);
//			output.close();
				String	line = "";
//				String ch = RandomStringUtils.randomAlphabetic(1);
	            line = line + "plasmaMembrane" + ",";
	            line = line + "initOrgProp" + ",";
	            line = line + "area" + "," + plasmaMembrane.getPlasmaMembraneArea() + ",";
	            line = line + "volume" + "," + plasmaMembrane.getPlasmaMembraneVolume() + ",";
			line = line + "\n";	
			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
			output.append(line);
//			line = "";
//	        line = line + "plasmaMembrane"+index + ch + ",";
//	        String rabContent = endosome.getRabContent().toString().replace("=",",");
//	        rabContent = rabContent.replace("{","");
//	        rabContent = rabContent.replace("}","");
//	        rabContent = rabContent.replace(" ","");
//	        line = line + "initRabContent" + "," + rabContent;
//	        line = line + "\n";
////			Writer output;
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
//			output.append(line);
			
			line = "";  
	        line = line + "plasmaMembrane"+ ",";
			String membraneContent = plasmaMembrane.getMembraneRecycle().toString().replaceAll("=",","); 
			membraneContent = membraneContent.replace("{","");
			membraneContent = membraneContent.replace("}","");
	        membraneContent = membraneContent.replace(" ","");
	        line = line + "initMembraneContent" + "," + membraneContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			
			line = "";  
	        line = line + "plasmaMembrane"+ ",";        
	        String solubleContent = plasmaMembrane.getSolubleRecycle().toString().replaceAll("=",",");        
			solubleContent = solubleContent.replace("{","");
			solubleContent = solubleContent.replace("}","");
	        solubleContent = solubleContent.replace(" ","");
	        line = line + "initSolubleContent" + "," + solubleContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			output.close();
		}	
		
		
//		public void writeToCsvER2() throws IOException {
//			
//			IndexedIterable<EndoplasmicReticulum> collectionER = CellBuilder.getCollectionER();
////			System.out.println("ALL ENDOSOMES"+collection);
//			int index = 0;
//			Writer output;	
//		    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//			String line ="tick " + tick + "\n";
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
//			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output.append(line);
//			output.close();
//			for (EndoplasmicReticulum ER : collectionER) {
//				line = "";
//				String ch = RandomStringUtils.randomAlphabetic(1);
//	            line = line + "ER"+index + ch + ",";
//	            line = line + "initOrgProp" + ",";
//	            line = line + "area" + "," + ER.area  + ",";
//	            line = line + "volume" + "," + ER.volume + ",";
//	            line = line + "xcoor" + "," + ER.xcoor + ",";
//	            line = line + "ycoor" + "," + ER.ycoor + ",";
//			line = line + "\n";	
//			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
//			output.append(line);
//			line = "";
//	        line = line + "ER"+index + ch + ",";
//	//        String rabContent = endosome.getRabContent().toString().replace("=",",");
////	        rabContent = rabContent.replace("{","");
////	        rabContent = rabContent.replace("}","");
////	        rabContent = rabContent.replace(" ","");
////	        line = line + "initRabContent" + "," + rabContent;
////	        line = line + "\n";
////			Writer output;
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
//			output.append(line);
//			
//			line = "";  
//	        line = line + "endosome"+index + ch + ",";
//			String membraneContent = ER.getMembraneRecycle().toString().replaceAll("=",","); 
//			membraneContent = membraneContent.replace("{","");
//			membraneContent = membraneContent.replace("}","");
//	        membraneContent = membraneContent.replace(" ","");
//	        line = line + "initMembraneContent" + "," + membraneContent;
//	        line = line + "\n";
////			Writer output;
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
//			output.append(line);
//			
//			line = "";  
//	        line = line + "ER"+index + ch + ",";        
//	        String solubleContent = ER.getSolubleRecycle().toString().replaceAll("=",",");        
//			solubleContent = solubleContent.replace("{","");
//			solubleContent = solubleContent.replace("}","");
//	        solubleContent = solubleContent.replace(" ","");
//	        line = line + "initSolubleContent" + "," + solubleContent;
//	        line = line + "\n";
////			Writer output;
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
//			output.append(line);
//			index = index + 1;
//			output.close();
//		}
//		}
		public void writeToCsvER() throws IOException {
			
			EndoplasmicReticulum endoplasmicReticulum = EndoplasmicReticulum.getInstance();
//			System.out.println("ALL ENDOSOMES"+collection);
			Writer output;	
//		    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//			String line ="tick " + tick + "\n";
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
//			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output.append(line);
//			output.close();
				String	line = "";
//				String ch = RandomStringUtils.randomAlphabetic(1);
	            line = line + "EReticulum" + ",";
	            line = line + "initOrgProp" + ",";
	            line = line + "area" + "," + endoplasmicReticulum.getEndoplasmicReticulumArea() + ",";
	            line = line + "volume" + "," + endoplasmicReticulum.getEndoplasmicReticulumVolume() + ",";
			line = line + "\n";	
			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
			output.append(line);
//			line = "";
//	        line = line + "EndoplasmicReticulum"+index + ch + ",";
//	        String rabContent = endosome.getRabContent().toString().replace("=",",");
//	        rabContent = rabContent.replace("{","");
//	        rabContent = rabContent.replace("}","");
//	        rabContent = rabContent.replace(" ","");
//	        line = line + "initRabContent" + "," + rabContent;
//	        line = line + "\n";
////			Writer output;
////			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
//			output.append(line);
			
			line = "";  
	        line = line + "EReticulum"+ ",";
			String membraneContent = endoplasmicReticulum.getMembraneRecycle().toString().replaceAll("=",","); 
			membraneContent = membraneContent.replace("{","");
			membraneContent = membraneContent.replace("}","");
	        membraneContent = membraneContent.replace(" ","");
	        line = line + "initMembraneContent" + "," + membraneContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			
			line = "";  
	        line = line + "EReticulum"+ ",";        
	        String solubleContent = endoplasmicReticulum.getSolubleRecycle().toString().replaceAll("=",",");        
			solubleContent = solubleContent.replace("{","");
			solubleContent = solubleContent.replace("}","");
	        solubleContent = solubleContent.replace(" ","");
	        line = line + "initSolubleContent" + "," + solubleContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			output.close();
		}
		public void writeToCsvCy() throws IOException {
			
			Cell cell = Cell.getInstance();

			Writer output;	
				String	line = "";
//				String ch = RandomStringUtils.randomAlphabetic(1);
	            line = line + "CyCell" + ",";
	            line = line + "initOrgProp" + ",";
	            line = line + "area" + "," + cell.getCellArea() + ",";
	            line = line + "volume" + "," + cell.getCellVolume() + ",";
			line = line + "\n";	
			output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
			output.append(line);
			
			line = "";  
	        line = line + "CyCell"+ ",";
			String membraneContent = cell.getMembraneCell().toString().replaceAll("=",","); 
			membraneContent = membraneContent.replace("{","");
			membraneContent = membraneContent.replace("}","");
	        membraneContent = membraneContent.replace(" ","");
	        line = line + "initMembraneContent" + "," + membraneContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			
			line = "";  
	        line = line + "CyCell"+ ",";        
	        String solubleContent = cell.getSolubleCell().toString().replaceAll("=",",");        
			solubleContent = solubleContent.replace("{","");
			solubleContent = solubleContent.replace("}","");
	        solubleContent = solubleContent.replace(" ","");
	        line = line + "initSolubleContent" + "," + solubleContent;
	        line = line + "\n";
//			Writer output;
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			output.close();
		}
	}
	
	
	
	
	
	
	
	

