package immunity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
// This class contains the properties of the cell.  It is loaded with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class ModelProperties {
	
//	public ModelProperties() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
	public static final String configFilename = "config.json";
	
	private static ModelProperties instance;
	
	public static ModelProperties getInstance() {
		//Para no parameter, usar 
//		Scanner scanner = new Scanner(new File(
//				"inputIntrTransp3.csv"));
//		scanner.useDelimiter(",");
		
//		PARA BATCH USAR ESTO.  DE ESTE MODO SE PUEDEN BARRER VARIOS INPUTFILE CON DIFERENTES PROPIEDADES
		Parameters parm = RunEnvironment.getInstance().getParameters();
		String inputFile =(String) parm.getValue("inputFile");
		File file = new File (".//data//"+inputFile);

		//		"inputIntrTransp3.csv"));
		// PARA BATCH MODE.  LEE DE UN FOLDER DATA RELATIVO QUE SE GENERA AL CORRER EN BATCH 
//				el folder se llama data y all� hay que meter todo lo que se lea, como el inputIntrTransp3.csv y los copasi que
//				se necesiten
		if( instance == null ) {
			instance = new  ModelProperties();
			try {
				ModelProperties modelProperties = new ModelProperties();
				modelProperties.loadFromCsv(instance, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return instance;
	}
//	Cell proterties that are loaded from a csv file by the CellBuilder class
	public HashMap<String, Double> cellK = new HashMap<String, Double>();
	public HashMap<String, Double> actionProbabilities = new HashMap<String, Double>();
	public HashMap<String, Double> cellAgentProperties = new HashMap<String, Double>();
	public HashMap<String, Double> plasmaMembraneProperties = new HashMap<String, Double>();
	public HashMap<String, Double> endoplasmicReticulumProperties = new HashMap<String, Double>();
	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();
	public HashMap<String, Double> membraneCell = new HashMap<String, Double>();
	public HashMap<String, Double> initPMmembraneRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> initPMsolubleRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> initERProperties = new HashMap<String, Double>();
	public HashMap<String, Double> initERmembraneRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> initERsolubleRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
	public HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>();
	public HashMap<String, Double> mtTropismTubule = new HashMap<String, Double>();
	public HashMap<String, Double> mtTropismRest = new HashMap<String, Double>();
	public HashMap<String, Double> rabRecyProb = new HashMap<String, Double>();
	public HashMap<String, String> colorRab = new HashMap<String, String>();
	public HashMap<String, String> colorContent = new HashMap<String, String>();
	public HashMap<String, String> copasiFiles = new HashMap<String, String>();
	public HashMap<String, Double> uptakeRate = new HashMap<String, Double>();
	public HashMap<String, Double> secretionRate = new HashMap<String, Double>();
	public HashMap<String, String> rabOrganelle = new HashMap<String, String>();
	public HashMap<Double, String> events = new HashMap<Double, String>();
	public Set<String> solubleMet = new HashSet<String>();
	public Set<String> membraneMet = new HashSet<String>();
	public Set<String> rabSet = new HashSet<String>();
	

//	GETTERS
	
	public HashMap<String, Double> getCellAgentProperties() {
		return cellAgentProperties;
	}
	public HashMap<String, Double> getPlasmaMembraneProperties() {
		return plasmaMembraneProperties;
	}
	public HashMap<String, Double> getEndoplasmicReticulumProperties() {
		return endoplasmicReticulumProperties;
	}
	public HashMap<String, String> getCopasiFiles() {
		return copasiFiles;
	}
	public HashMap<String, Double> getCellK() {
		return cellK;
	}
	public HashMap<String, Double> getInitRabCell() {
		return initRabCell;
	}
	public HashMap<String, Double> getRabCompatibility() {
		return rabCompatibility;
	}
	public HashMap<String, Double> getTubuleTropism() {
		return tubuleTropism;
	}
	public HashMap<String, Set<String>> getRabTropism() {
		return rabTropism;
	}
	public HashMap<String, Double> getMtTropismTubule() {
		return mtTropismTubule;
	}
	public HashMap<String, Double> getMtTropismRest() {
		return mtTropismRest;
	}
	public HashMap<String, Double> getRabRecyProb() {
		return rabRecyProb;
	}
	public HashMap<String, String> getRabOrganelle() {
		return rabOrganelle;
	}
	public HashMap<String, String> getColorRab() {
		return colorRab;
	}
	public HashMap<String, String> getColorContent() {
		return colorContent;
	}

	public Set<String> getMembraneMet() {
		return membraneMet;
	}
	public HashMap<String, Double> getUptakeRate() {
		return uptakeRate;
	}
	public HashMap<String, Double> getSecretionRate() {
		return secretionRate;
	}
	public Set<String> getSolubleMet() {
		return solubleMet;
	}
	public Set<String> getRabSet() {
		return rabSet;
	}
	public HashMap<String, Double> getSolubleCell() {
//		System.out.println("DEVUELVE SOLUBLE CELL " + solubleCell);
		return solubleCell;
	}
	public HashMap<String, Double> getMembraneCell() {
//		System.out.println("DEVUELVE SOLUBLE CELL " + solubleCell);
		return solubleCell;
	}
	public HashMap<String, Double> getInitPMmembraneRecycle() {
		return initPMmembraneRecycle;
	}
	public HashMap<String, Double> getInitPMsolubleRecycle() {
		return initPMsolubleRecycle;
	}
	public HashMap<String, Double> getInitERmembraneRecycle() {
		return initERmembraneRecycle;
	}
	public HashMap<String, Double> getInitERsolubleRecycle() {
		return initERsolubleRecycle;
	}
	public HashMap<String, Double> getActionProbabilities() {
		return actionProbabilities;
	}
	public HashMap<Double, String> getEvents() {
		return events;
	}
	public HashMap<String, Double> getInitERProperties() {
		return initERProperties;
	}
	
	public void loadFromCsv(ModelProperties modelProperties, File file) throws IOException {

		Scanner scanner = new Scanner(file);				
		scanner.useDelimiter(",");
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			ModelProperties config = objectMapper.readValue(new File(ModelProperties.configFilename), ModelProperties.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
//		freezeDryOption: // this names the WHILE loop, so I can break from the loop when I want.  
			//Something I did not know that it could be done
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
			switch (b[0]) {
			case "cellK": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getCellK().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getCellK());
				}
				
				break;
			}
			case "actionProbabilities": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getActionProbabilities().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println("actionProbabilities" + modelProperties.getActionProbabilities());
				}
				
				break;
			}
			
			case "cellAgentProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getCellAgentProperties().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getCellK());
				}
				
				break;
			}
			case "plasmaMembraneProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getPlasmaMembraneProperties().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println("plasma membrane propertiesSSSSSSSSSS "+modelProperties.getCellK());
				}			
				break;
			}
			case "initERProperties": {				
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getInitERProperties().put(b[i], Double.parseDouble(b[i+1]));
				System.out.println("QUE ES LO QUE NO ANDA!!!!!!!!!!! " + modelProperties.getInitERProperties());
				}	
				break;
			}
			case "cellCopasi": case "plasmaMembraneCopasi" : case "endosomeCopasi": case "rabCopasi":{
					modelProperties.getCopasiFiles().put(b[0], b[1]);

				break;
			}
			case "initRabCell": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getInitRabCell().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getInitRabCell());
				}
				break;
			}
			case "initPMmembraneRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getInitPMmembraneRecycle().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getMembraneRecycle());
				}
				break;
			}
			case "initPMsolubleRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				System.out.println("initPMsolubleRecycle   "+b[i] + b[i+1]);
				modelProperties.getInitPMsolubleRecycle().put(b[i], Double.parseDouble(b[i+1]));

				}
				break;
			}
			case "initERmembraneRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getInitERmembraneRecycle().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getMembraneRecycle());getInitERmembraneRecycle
				}
				break;
			}
			case "initERsolubleRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getInitERsolubleRecycle().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getMembraneRecycle());
				}
				break;
			}
			case "solubleCell": {
				for (int i = 1; i < b.length; i = i + 2) {
				modelProperties.getSolubleCell().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(modelProperties.getSolubleCell());
				}
				break;
			}
			case "rabCompatibility": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getRabCompatibility().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(modelProperties.getRabCompatibility());
					}
				break;
			}
			case "tubuleTropism": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getTubuleTropism().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(modelProperties.getTubuleTropism()); 
					}
				break;
			}
			case "rabTropism": {
				Set<String> rabT = new HashSet<String>();
				for (int i = 2; i < b.length; i++) {
					//System.out.println(b[i]);
					if (b[i].length()>0) {
						rabT.add(b[i]);
					}
				}
				modelProperties.getRabTropism().put(b[1], rabT);
				break;
			}
			case "mtTropismTubule": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getMtTropismTubule().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(modelProperties.getMtTropism());
					}
				break;
			}
			case "mtTropismRest": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getMtTropismRest().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(modelProperties.getMtTropism());
					}
				break;
			}
			case "rabRecyProb": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getRabRecyProb().put(b[i], Double.parseDouble(b[i+1]));
					}
				break;
			}
			
			case "organelle": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getRabOrganelle().put(b[i], b[i+1]);
					}
				break;
			}
			case "uptakeRate": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getUptakeRate().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "secretionRate": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getSecretionRate().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "solubleMet": {
				for (int i = 1; i < b.length; i++) {
					modelProperties.getSolubleMet().add(b[i]);
				}
				break;
			}
			case "membraneMet": {
				for (int i = 1; i < b.length; i++) {
					modelProperties.getMembraneMet().add(b[i]);
				}
				break;
			}
			case "rabSet": {
				for (int i = 1; i < b.length; i++) {
					modelProperties.getRabSet().add(b[i]);
				}
				break;
			}
			
			case "colorRab": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getColorRab().put(b[i], b[i + 1]);
				}
				break;
			}
			case "colorContent": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getColorContent().put(b[i], b[i + 1]);
				}
				break;
			}
			case "events": {
				for (int i = 1; i < b.length; i = i + 2) {
					modelProperties.getEvents().put(Double.parseDouble(b[i]), b[i + 1]);
				}
				break;
			}
			
//			case "freezeDry":
//				{
//					FreezeDryEndosomes.getInstance();
//					break freezeDryOption; // if freezeDry then exit because the initial organelles will be loaded in a 
//					// different way.  HOWEVER, THE KIND1-KIND6 PROPERTIES NEED TO BE LOADED BECAUSE THEY ARE
////					USED FOR NEW ORGANELLES (UPTAKE). JUST BY CHANCE THIS IS DONE FOR THE UPDATE CLASS
////					NEED TO IMPROVE THIS
//			}
			// INITIAL ORGANELLES kind Large is for phagosomes
			case "kind1": case "kind2": case "kind3": case "kind4": case "kind5": case "kind6":case "kind7": case "kind8": case "kind9": case "kindLarge":
			{
				InitialOrganelles inOr = InitialOrganelles.getInstance();
				inOr.getDiffOrganelles().add(b[0]);
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
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitRabContent().put(b[0], value);
					break;
				}
				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitSolubleContent().put(b[0], value);
//					System.out.println("Proton is there?" + inOr.getInitSolubleContent());
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//					System.out.println("VALOR MALO" + b[i] + "" + b[i+1]);
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
			case "freezeDry":
			{
				FreezeDryEndosomes.loadFromCsv();
				break;// freezeDryOption; // if freezeDry then exit because the initial organelles will be loaded in a 
				// different way.  HOWEVER, THE KIND1-KIND6 PROPERTIES NEED TO BE LOADED BECAUSE THEY ARE
//				USED FOR NEW ORGANELLES (UPTAKE). JUST BY CHANCE THIS IS DONE FOR THE UPDATE CLASS
//				NEED TO IMPROVE THIS
		}

			default: {
				System.out.println("no a valid entry");
			}
			}

		}
		scanner.close();

	}


	public static void loadOrganellePropertiesFromCsv(ModelProperties modelProperties) throws IOException {
		Parameters parm = RunEnvironment.getInstance().getParameters();
		String inputFile =(String) parm.getValue("inputFile");
		File scannerfile = new File (".//data//"+inputFile);
		Scanner scanner = new Scanner(scannerfile);
		scanner.useDelimiter(",");

//		freezeDryOption: // this names the WHILE loop, so I can break from the loop when I want.  
			//Something I did not know that it could be done
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] b = line.split(",");
				switch (b[0]) {
				//				case "freezeDry":
				//					{
				//						FreezeDryEndosomes.getInstance();
				//						break freezeDryOption; // if freezeDry then exit because the initial organelles will be loaded in a 
				//						// different way.  HOWEVER, THE KIND1-KIND6 PROPERTIES NEED TO BE LOADED BECAUSE THEY ARE
				////						USED FOR NEW ORGANELLES (UPTAKE). JUST BY CHANCE THIS IS DONE FOR THE UPDATE CLASS
				////						NEED TO IMPROVE THIS
				//				}
				//				// INITIAL ORGANELLES kind Large is for phagosomes
				case "kind1": case "kind2": case "kind3": case "kind4": case "kind5": case "kind6":case "kind7": case "kind8": case "kind9": case "kindLarge":
				{
					InitialOrganelles inOr = InitialOrganelles.getInstance();
					inOr.getDiffOrganelles().add(b[0]);
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
							value.put(b[i], Double.parseDouble(b[i + 1]));
						}
						inOr.getInitRabContent().put(b[0], value);
						break;
					}
					case "initSolubleContent": {
						HashMap<String, Double> value = new HashMap<String, Double>();
						for (int i = 2; i < b.length; i = i + 2) {
							value.put(b[i], Double.parseDouble(b[i + 1]));
						}
						inOr.getInitSolubleContent().put(b[0], value);
						//						System.out.println("Proton is there?" + inOr.getInitialSolubleContent());
						break;
					}
					case "initMembraneContent": {
						HashMap<String, Double> value = new HashMap<String, Double>();
						for (int i = 2; i < b.length; i = i + 2) {
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


				default: {
					System.out.println("no a valid entry");
				}

				}
			}
//			System.out.println("  FREEZE DRY INITIAL ORGANELLES FOR UPTAKE " + InitialOrganelles.getInstance().initOrgProp);
	}

	

}