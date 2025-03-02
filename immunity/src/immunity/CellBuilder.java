package immunity;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import java.util.Set;



//import immunity.Element;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.parameter.Parameters;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.collections.IndexedIterable;

public class CellBuilder implements ContextBuilder<Object> { // contextbuilder es una interfaz, debe tener una clase context que se sobrescribe mas abajo

	public static IndexedIterable collection = null;
	private static Collection context;
	public static final IndexedIterable getCollection() {
		return collection;
	}
	public static IndexedIterable collectionER = null;
//	private static Collection context;
	public static final IndexedIterable getCollectionER() {
		return collectionER;
	}
//CHANGES HERE
// Create two spaces, one for PM where Agents are molecules and other for the Intracellular transport.  
	@Override
	public Context build(Context<Object> context) {
		context.setId("immunity");

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
											"infection network", context, true);
		netBuilder.buildNetwork();
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
											.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
										"space", context, new RandomCartesianAdder<Object>(),
										new repast.simphony.space.continuous.BouncyBorders(), 
										50,50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
							new GridBuilderParameters<Object>(new WrapAroundBorders(),
							new SimpleGridAdder<Object>(), true,
							50, 50));
		
// PM space
//		ContinuousSpace<Object> spacePM = spaceFactory.createContinuousSpace(
//										"spacePM", context, new RandomCartesianAdder<Object>(),
//										new repast.simphony.space.continuous.WrapAroundBorders(), 
//										50,100);
//
//		Grid<Object> gridPM = gridFactory.createGrid("gridPM", context,
//							  new GridBuilderParameters<Object>(new WrapAroundBorders(),
//						   	  new SimpleGridAdder<Object>(), true,
//						   	  50, 100));
//		
//		Parameters p = RunEnvironment.getInstance().getParameters();
		
//  introduce the agents in the space
		
//		System.out.println(" builder CellProperties cargado");
//			context.add(ModelProperties);	
		//Cell cell = Cell.getInstance();
		context.add(new Cell(space, grid));
		context.add(new Results(space, grid, null, null));// 
		context.add(new UpdateParameters());
		context.add(new PlasmaMembrane(space, grid));	
		context.add(new EndoplasmicReticulum(space, grid));
		context.add(new Scale(space, grid));
		InitialOrganelles initialOrganelles  = InitialOrganelles.getInstance();
		context.add(initialOrganelles);	
		
		
		// Microtubules
		for (int i = 0; i < (int)6* 1/Cell.orgScale; i++) {// change the number of MT 3 for 6 MT
//			for (int i = 0; i < 10; i++) {// change the number of MT 3 for 6 MT
			context.add(new MT(space, grid));
		}

////		MOLECULES IN THE PLASMA MEMBRANE SPACE
//		for (int i = 0; i < (int) 10/Cell.orgScale; i++) {// change the number of MT 3 for 6 MT
//			MoleculePM molecule = new MoleculePM(spacePM, gridPM, "receptor");
//			context.add(molecule);
//			double y = 100* Math.random();
//			double x =  50* Math.random();
//			spacePM.moveTo(molecule, x, y);
//			gridPM.moveTo(molecule,(int) x, (int)y);
//		}
//		for (int i = 0; i < (int) 1000/Cell.orgScale; i++) {// change the number of MT 3 for 6 MT
//			MoleculePM molecule = new MoleculePM(spacePM, gridPM, "lipid");
//			context.add(molecule);
//			double y = 100* Math.random();
//			double x =  50* Math.random();
//			spacePM.moveTo(molecule, x, y);
//			gridPM.moveTo(molecule,(int) x, (int)y);
//		}
//	
//// 		ENDOPLASMIC RETICULUM
//		for (int i = 0; i < 4; i=i+1)
//		{
//			EndoplasmicReticulum endoplasmicReticulum = new EndoplasmicReticulum(space, grid, null, null, null);
//			endoplasmicReticulum.heading = 45 + 90 * i;
//			System.out.println("heading   " + endoplasmicReticulum.heading);
//			endoplasmicReticulum.ycoor = 28 + 18*Math.cos(-endoplasmicReticulum.heading*Math.PI/180);
//			endoplasmicReticulum.xcoor = 25 + 18*Math.sin(-endoplasmicReticulum.heading*Math.PI/180);
//			ModelProperties modelProperties = ModelProperties.getInstance();
////			System.out.println("Properties  " + modelProperties.getInitERProperties());
////			endoplasmicReticulum.area = modelProperties.getInitERProperties().get("endoplasmicReticulumAreaInit");// 
//	//		initialendoplasmicReticulumArea = 1500*400/orgScale/orgScale;// modelProperties.getEndoplasmicReticulumProperties().get("endoplasmicReticulumArea");// 	
////			endoplasmicReticulum.volume = modelProperties.getInitERProperties().get("endoplasmicReticulumVolumeInit");;//
//	//		initialendoplasmicReticulumVolume = 1500*400*1000/orgScale/orgScale/orgScale;// modelProperties.getEndoplasmicReticulumProperties().get("endoplasmicReticulumVolume");//
//
//			context.add(endoplasmicReticulum);
//		}
		// ENDOSOMES
		ModelProperties modelProperties = ModelProperties.getInstance();
		Set<String> diffOrganelles = initialOrganelles.getDiffOrganelles();
		String name;
		int ite=0;
		if (modelProperties.getCellK().get("freezeDry").equals(0d)) {
		// RabA is Rab5.  Organelles are constructed with a given radius that depend on the type (EE, LE, Lys) and with a 
		// total surface.  These values were obtained of simulations that progressed by 40000 steps
			for (String kind : diffOrganelles){
				
					HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(initialOrganelles.getInitOrgProp().get(kind));
					double totalArea = initOrgProp.get("area")/modelProperties.getCellK().get("orgScale");
					double maxRadius = initOrgProp.get("maxRadius");
					double maxAsym = initOrgProp.get("maxAsym");
					double minRadius = Cell.rcyl*1.1;
					while (totalArea > 32d*Math.PI*minRadius*minRadius){
						
						double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);
						double c,area,volume;
						HashMap<String, Double> rabKindContent = new HashMap<String, Double>(initialOrganelles.getInitRabContent().get(kind));
						
						if(isGolgi(rabKindContent)) {
							double aq = 2d*Math.PI;
							double bq = 4*Math.PI*Cell.rcyl;
							double cq = -totalArea;
							double dq =  bq * bq - 4 * aq * cq;
							double radius = ( - bq + Math.sqrt(dq))/(2*aq);
							area = totalArea;
							volume =Math.PI*Math.pow(radius, 2)* 2 * Cell.rcyl;
						}
						else { // if (!kind.equals("kindLarge")){		
							c = a + a  * Math.random()* maxAsym;
							double f = 1.6075;
							double af= Math.pow(a, f);
							double cf= Math.pow(c, f);
							area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
							volume = 4d/3d*Math.PI*a*a*c;
						}
						
						initOrgProp.put("area", area);
						initOrgProp.put("volume", volume);
						totalArea = totalArea-area;

						HashMap<String, Double> rabContent = new HashMap<String, Double>(initialOrganelles.getInitRabContent().get(kind));
						for (String rab : rabContent.keySet()){
							double rr = rabContent.get(rab);
							rabContent.put(rab, rr*area);
						}
						HashMap<String, Double> membraneContent = new HashMap<String, Double>(initialOrganelles.getInitMembraneContent().get(kind));
						for (String mem : membraneContent.keySet()){
							if (mem.equals("membraneMarker")){
								membraneContent.put(mem, 1d);
								initialOrganelles.getInitMembraneContent().get(kind).remove("membraneMarker");
							}
							else {double mm = membraneContent.get(mem);
							membraneContent.put(mem, mm*area);
							}
						}

						HashMap<String, Double> solubleContent = new HashMap<String, Double>(initialOrganelles.getInitSolubleContent().get(kind));
						for (String sol : solubleContent.keySet()){
							if (sol.equals("solubleMarker")){
								solubleContent.put(sol, 1d);
								initialOrganelles.getInitSolubleContent().get(kind).remove("solubleMarker");
							}
							else {double ss = solubleContent.get(sol);
							solubleContent.put(sol, ss*volume);
							}
						}

						Endosome end = new Endosome(space, grid, rabContent, membraneContent,
													solubleContent, initOrgProp);
//						ite+=1;
//						end.setName(kind+" "+Integer.toString(ite)) ;
						context.add(end);
						//Endosome.endosomeShape(end);
						//System.out.println(end.getName()+ " " +membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);				
				}
			}
		}
		else {
//			if endosomes are loadaed from a freezeDry csv file
//			CellProperties.getInstance().getCellK().get("freezeDry").equals(1d)
//			System.out.println("FREEZE DRY METHOD   "+diffOrganelles);
			for (String kind : diffOrganelles){
				if (kind.substring(0,2).equals("ki")) continue;
				else if (kind.substring(0,2).equals("en")) {
				HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
				HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get(kind));
				HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get(kind));
				HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
//				System.out.println(kind + membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);
						Endosome end = new Endosome(space, grid, rabContent, membraneContent,
													solubleContent, initOrgProp);
						context.add(end);
						double x = initOrgProp.get("xcoor");
						double y = initOrgProp.get("ycoor");
						space.moveTo(end, x, y);
						grid.moveTo(end, (int) x, (int) y);	
						//Endosome.endosomeShape(end);

			}
//				else if (kind.substring(0,2).equals("ER")) {
//				HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
//				HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get(kind));
//				HashMap<String, Double> solubleRecycle = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
//				System.out.println(kind + membraneRecycle + " " + solubleRecycle + " " + initOrgProp);
//						EndoplasmicReticulum ER = new EndoplasmicReticulum(space, grid, membraneRecycle,
//													solubleRecycle, initOrgProp);
//						context.add(ER);
//						ER.area = initOrgProp.get("area");
//						ER.volume = initOrgProp.get("volume");
//						double x = ER.xcoor;
//						double y = ER.ycoor;
//						space.moveTo(ER, x, y);
//						grid.moveTo(ER, (int) x, (int) y);	
//
//			}
				
			}
//			After loading the frozen organelles, the properties of KIND... organelles need to be loaded to generate
//			new organelles during UPTAKE  or SECRETION .....

			try {
				ModelProperties.getInstance().loadOrganellePropertiesFromCsv(ModelProperties.getInstance());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
			
		
		// Cytosol (NOT USED IN PRESENT BRANCH)
//		for (int i = 0; i < 50; i++) {
//			for (int j = 0; j < 50; j++) {
//				HashMap<String, Double> cytoContent = new HashMap<String, Double>();
//				context.add(new Cytosol(space, grid, cytoContent, i, j));
//			}
//		}
		// Cell
		//context.add(Cell.getInstance());
		//context.add(CellProperties.getInstance());

		// Locate the object in the space and grid
		for (Object obj : context) {
			if (obj instanceof Cytosol) {
				double xcoor = ((Cytosol) obj).getXcoor();
				double ycoor = ((Cytosol) obj).getYcoor();
				space.moveTo(obj, xcoor, ycoor);
				grid.moveTo(obj, (int) xcoor, (int) ycoor);
			}
			else if (obj instanceof PlasmaMembrane) {
				space.moveTo(obj, 25, 25);
				grid.moveTo(obj, (int) 25, (int) 25);
			}
//			else if (obj instanceof EndoplasmicReticulum) {
//				space.moveTo(obj, 24.5, .5);
//				grid.moveTo(obj, (int) 24, (int) 0);
//			}
			else if (obj instanceof Scale) {
				space.moveTo(obj, Scale.getScale500nm()/2d-(0.4)+7.5, 49.65);
//				System.out.println ("SCALE SCALE "+Scale.getScale500nm()/2d);
				grid.moveTo(obj, (int) (Scale.getScale500nm()/2d), (int) 49);
			}
			else if (obj instanceof MT) {
				((MT) obj).changePosition((MT)obj);
			} 
			
			else if (obj instanceof EndoplasmicReticulum) {
				double x = 25;//((EndoplasmicReticulum) obj).getXcoor();
				double y = 25;// ((EndoplasmicReticulum) obj).getYcoor();
				space.moveTo(obj, x, y);
				grid.moveTo(obj, (int) x, (int) y);					
//				System.out.println(((EndoplasmicReticulum) obj).area + "  " +
//						((EndoplasmicReticulum) obj).getMembraneRecycle());
			} 
			
// Find new position for endosomes unless they are coming from a freezeDry file
			if (obj instanceof Endosome && ModelProperties.getInstance().getCellK().get("freezeDry").equals(0d) ) {
			double position = ((Endosome) obj).getInitOrgProp().get("position");
//				NdPoint pt = space.getLocation(obj);
				double y = 5d + 40d * position + RandomHelper.nextDoubleFromTo(-4d, 4d);
				double x = RandomHelper.nextDoubleFromTo(5d, 45d);
				space.moveTo(obj, x, y);
				grid.moveTo(obj, (int) x, (int) y);					
//			to position endosomes in a specific way
//				if(((Endosome) obj).getRabContent().containsKey("RabB")){
////					NdPoint pt = space.getLocation(obj);
//					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
//					double y = RandomHelper.nextDoubleFromTo(25d, 45d);
//					space.moveTo(obj, x, y);
//					grid.moveTo(obj, (int) x, (int) y);					
//					}
			}
		}

		
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(60100);
		}

		collection = context.getObjects(Endosome.class);// se guardan los objetos de la clase endosoma, supongo que seran endosomas
		collectionER = context.getObjects(EndoplasmicReticulum.class);
		return context;	
		
	}
	
	
	
	private static boolean isGolgi(HashMap<String, Double> rabContent) {
		double areaGolgi = 0d;
		for (String rab : rabContent.keySet()){
			String name = ModelProperties.getInstance().rabOrganelle.get(rab);
			if (name.contains("Golgi")) {areaGolgi = areaGolgi + rabContent.get(rab);} 
		}
		boolean isGolgi = false;
		if (areaGolgi >= 0.5) {
			isGolgi = true;
		}
		return isGolgi;
	}	
	

/*	private void loadFromExcel(Context<Endosome> context, ContinuousSpace<Object> space, Grid<Object> grid)

			throws IOException {
		// open the excel file
		Workbook book = new XSSFWorkbook(new FileInputStream("./data/agent_input.xlsx"));
		// get the first worksheet
		Sheet sheet = book.getSheetAt(0);

		// iterate over the rows, skipping the first one

		for (Row row : sheet) {

			if (row.getRowNum() > 0) {

				String id = row.getCell(1).getStringCellValue();
				int age = (int) row.getCell(2).getNumericCellValue();
				double energy = row.getCell(3).getNumericCellValue();
				Endosome endosome = new Endosome(space, grid, null, null, null, null);
				context.add(endosome);

				double x = row.getCell(5).getNumericCellValue();
				double y = row.getCell(6).getNumericCellValue();
				space.moveTo(endosome, x, y);
			}

		}



	}*/


	
}
