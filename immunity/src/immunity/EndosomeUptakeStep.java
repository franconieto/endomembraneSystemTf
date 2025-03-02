package immunity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndosomeUptakeStep {
	/*
	 * this class is not used.  Now uptake is managed from Cell class using UptakeStep2 class
	 */
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static Object membreneMet;
	public static void uptake(Endosome endosome) {
		//		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		//		if (tick < 100) return;
		space = endosome.getSpace();
		grid = endosome.getGrid();
		/* ?
		 * Old logic.  The amount of domains of each Rab is compared with the initial value
		 * The domain with more difference is selected to generate a new organelle
		 * 	If the Rab selected is RabA, the new organelle is generated from 
		 *  plasma membrane. If not, the new organelle is generated according to the "kind" 
		 *  specified in the csv file for the initial organelles
		 *  
		 *  New logic.  The EE are created according to the PM membrane disponibility
		 * that is maintained by recycling of tubular EE. The maturation of EE is compensated by the
		 * membrane of the internal vesicles that are added to the PM (thinking in synthesis of membrane)
		 * The maturation also generate LE membrane that should be compensated by the reduction of the limiting
		 * membrane also by the generation of internal vesicles
		 * The old logic will be maintained until the system is stable
		 *  */
		Cell cell = Cell.getInstance();
		HashMap<String, Double> totalRabs = new HashMap<String, Double>(Results.getInstance().getTotalRabs());
		HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>(Results.getInstance().getInitialTotalRabs());
		//		System.out.println("totalRabs  "+totalRabs);

		HashMap<String, Double> deltaRabs = new HashMap<String, Double>();  
		HashMap<String, String> rabCode = new HashMap<String, String>();
		rabCode.put("RabA", "kind1");
		rabCode.put("RabB", "kind2");
		rabCode.put("RabC", "kind3");
		rabCode.put("RabD", "kind4");
		rabCode.put("RabE", "kind5");

		for (String rab : totalRabs.keySet()){
			//			System.out.println("ErrorRabs  "+ rab + "   " +initialTotalRabs.get(rab) +"     "+ totalRabs.get(rab));
			double value = initialTotalRabs.get(rab) - totalRabs.get(rab);
			deltaRabs.put(rab, value);
		}

		//	System.out.println("Initial Rabs  "+ initialTotalRabs + " \n delta Rabs"+ deltaRabs);
		double largeDelta = 0d;
		String selectedRab = "";
		for (String rab : deltaRabs.keySet()){
			if (deltaRabs.get(rab)>largeDelta) {
				selectedRab=rab;
				largeDelta=deltaRabs.get(rab);
			}	
		}
		//		System.out.println("selected Rab for uptake "+ selectedRab);
		//		If no rab was selected or the surface required is small (less than a sphere of 60 nm radius, 
		//		no uptake is required
		if (selectedRab.equals("")|| deltaRabs.get(selectedRab)<45000) return;
		if (selectedRab.equals("RabA")){ 
			newUptake(endosome, selectedRab);}
		else {newOrganelle(endosome, selectedRab, rabCode);}

	}
		
	private static void newUptake(Endosome endosome, String selectedRab) {
		double cellLimit = 3d * Cell.orgScale;
		System.out.println("UPTAKE INITIAL ORGANELLES " +	InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		
//		System.out.println("PROPIEDADES RAB A  "+initOrgProp);
//		System.out.println("A VER?" + InitialOrganelles.getInstance().getInitOrgProp().get("kind2"));
		HashMap<String, Double> rabCell = Cell.getInstance().getRabCell();

		if (!rabCell.containsKey("RabA") || Math.random()>rabCell.get("RabA")){
			return;}
	//			double rabCellA = rabCell.get("RabA");
	// cytosolic RabA is always provided by the -> RabAc reaction.  Only in a KD will go down
	//		Then no uptake if no RabA in cyto.  The uptake is proportional to the amount of RabA	
	//		Uptake generate a new RabA organelle.  The size is the radius in the csv file for RabA
	//		initial organelles.  The content is specified in concentration. One (1) is approx 1 mM.
	//		This units are converted to membrane or volume units by multiplying by the area (rabs and
	//		membrane content) or volume (soluble content).  For uptake it is controlled that there is enough
	//		membrane and there is RabA in the cell

		double maxRadius = initOrgProp.get("maxRadius");
		double minRadius = Cell.rcyl*1.1;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random() * initOrgProp.get("maxAsym");

		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double plasmaMembrane = endosome.area + PlasmaMembrane.getInstance().getPlasmaMembraneArea();
		PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
		double volume = 4d/3d*Math.PI*a*a*c;
		initOrgProp.put("area", area);
		double value = Results.instance.getTotalRabs().get("RabA");
		value = value + area;
		Results.instance.getTotalRabs().put("RabA", value);
		initOrgProp.put("volume", volume);
//		System.out.println("PROPIEDADES RAB A  "+initOrgProp);
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


		HashMap<String, Double> rabContent = new HashMap<String, Double>();
	//			UPTAKE ENDOSOME JUST RABA
		rabContent.put("RabA", area);
		HashMap<String, Double> membraneContent = new HashMap<String,Double>();
		Set<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
		for (String mem : membraneMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			double valueInTotal = 0d;
			if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(mem))
			{
				double valuePM = PlasmaMembrane.getInstance().getMembraneRecycle().get(mem);
				valueInPM = valuePM * ModelProperties.getInstance().getUptakeRate().get(mem) * area/ PlasmaMembrane.getInstance().getPlasmaMembraneArea();	
				if (valueInPM>area) valueInPM = area;
				PlasmaMembrane.getInstance().getMembraneRecycle().put(mem, valuePM-valueInPM);
			}
			if (InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").containsKey(mem))
			{
				valueInEn = InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").get(mem)*area;
			}
			valueInTotal = valueInEn + valueInPM;
			if (valueInTotal >= area) 	valueInTotal= area;
			membraneContent.put(mem, valueInTotal);	
		}
	//System.out.println("RRRRRRRRRRRRRRRRRRRRRREEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSS "+ membraneContent);
		HashMap<String, Double> solubleContent = new HashMap<String,Double>();
		Set<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
		for (String sol : solubleMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			double valueInTotal = 0d;
			if (PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(sol))
			{
				double valuePM = PlasmaMembrane.getInstance().getSolubleRecycle().get(sol);	
				valueInPM = valuePM * volume/ PlasmaMembrane.getInstance().getPlasmaMembraneVolume();	
				if (valueInPM >= volume) valueInPM = volume;
				// decrease PM content
				PlasmaMembrane.getInstance().getSolubleRecycle().put(sol, valuePM-valueInPM);
			}
			if (InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").containsKey(sol))
			{
				valueInEn = InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").get(sol)*volume;
			}
			valueInTotal = valueInEn + valueInPM;
			if (valueInTotal >= volume) valueInEn= volume;
			solubleContent.put(sol, valueInEn);	
		}
//		HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get("kind1"));
//		for (String sol : solubleContent.keySet()){
//			double ss = solubleContent.get(sol);
//			solubleContent.put(sol, ss*volume);
//		}
		solubleContent.put("protonEn", 3.98E-5*volume); //pH 7.4
	// new endosome incorporate PM components in a proportion area new/area PM
	//					"Fully conformed MHC-I proteins internalize with the
	//					rate 0.002–0.004 min−1 (0.2–0.4% loss of initially surface expressed
	//					molecules per minute, i.e. 12–24% per hour) which is 5–8-fold slower
	//					than IR of their open forms (0.011–0.022 min−1), 
	//					Molecular Immunology 55 (2013) 149– 152 (Pero Lucin)"
	//One 60 radius endosomes has an area of about 45000 nm.  This is about 7.5% of the 1500 x 400 nm of
	//the PM considered at the 1 organelle scale.  So to internalize 0.3%. a factor of 0.04 (0.3%/7.5%) is applied.
	//Hence, factor = endosome area/ PM area * 0.04 for cMHCIa and pept-mHCI, and endosome area/ PM area * 0.2 for mHCI
	/*				
My problem is that I do not know the rate of uptake that depends on how much Kind1(Rab5) organelles are 
switched to Kind4(Rab7).  I guess is that the rate will have to be relative.  1 for open MHCI and 0.15 for closed.


	 */					
	/*			double cMHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("cMHCI");
		double cMHCIvalue = cMHCIvalueIn * 0.4 * area/ (double) PlasmaMembrane.getInstance().area;
		membraneContent.put("cMHCI", cMHCIvalue);
		PlasmaMembrane.getInstance().getMembraneRecycle().put("cMHCI", cMHCIvalueIn - cMHCIvalue);

		double mHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("mHCI");
		double mHCIvalue = mHCIvalueIn * 1 * area/ (double) PlasmaMembrane.getInstance().area;
		membraneContent.put("mHCI", mHCIvalue);
		PlasmaMembrane.getInstance().getMembraneRecycle().put("mHCI", mHCIvalueIn - mHCIvalue);
	 *///			
	//			for (String met : PlasmaMembrane.getInstance().getMembraneRecycle().keySet()){
	//				double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get(met);
	//				value = 0.001 * valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
	//				if (value > area) {value = area;}


//		System.out.println("PLASMA MEMBRANE "+PlasmaMembrane.getInstance().getMembraneRecycle());

		//			Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);
		Context<Object> context = ContextUtils.getContext(endosome);

		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		//			tMembrane = tMembrane - bud.initOrgProp.get("area");
//		bud.area = initOrgProp.get("area");
//		bud.volume = initOrgProp.get("volume");
//		bud.size = initOrgProp.get("maxRadius");// radius of a sphere with the volume of the
		// cylinder
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		double xend=0;
		double yend=0;
//		To place the endosome near the plasma membrane and heading to the center of the cell
		int randomSide = (int) Math.floor(Math.random()*4);
		switch (randomSide) {
		case 0 : {
			xend = 2*cellLimit;
			yend = Math.random()*50-2*cellLimit;
			break;
		}
		case 1 : {
			xend = 50-2*cellLimit;
			yend = Math.random()*50-2*cellLimit;
			break;
		}
		case 2 : {
			xend = Math.random()*50-2*cellLimit;
			yend = 2*cellLimit;
			break;
		}
		case 3 : {
			xend = Math.random()*50-2*cellLimit;
			yend = 50-2*cellLimit;
			break;
		}
		}		
		bud.heading = Math.atan2(xend-25, yend-25)*180/Math.PI; //;

		endosome.getSpace().moveTo(bud, xend, yend);
		endosome.getGrid().moveTo(bud, (int) xend, (int) yend);
		
		System.out.println(xend + " EEEEEEEEEE NEW UPTAKE " + yend);
//					try {
//					TimeUnit.SECONDS.sleep(5);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
		
	}

	private static void newOrganelle(Endosome endosome, String selectedRab, HashMap<String, String> rabCode) {
		String kind = rabCode.get(selectedRab);
		System.out.println(kind + " UPTAKE INITIAL ORGANELLES " +	InitialOrganelles.getInstance().getInitOrgProp().get(kind));

		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get(kind));
		//		double tMembrane = Cell.getInstance().gettMembrane();
		double maxRadius = initOrgProp.get("maxRadius");
		double maxAsym = initOrgProp.get("maxAsym");
		double minRadius = Cell.rcyl*1.1;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random()* maxAsym;
		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double volume = 4d/3d*Math.PI*a*a*c;
		double value = Results.instance.getTotalRabs().get(selectedRab);
		value = value + area;
		Results.instance.getTotalRabs().put(selectedRab, value);
		initOrgProp.put("area", area);
		initOrgProp.put("volume", volume);	
		HashMap<String, Double> rabContent = new HashMap<String, Double>();
		rabContent.put(selectedRab, area);
		
		HashMap<String, Double> membraneContent = new HashMap<String, Double>();
		HashMap<String, Double> solubleContent = new HashMap<String, Double>();
		HashSet<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
		HashSet<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
//	This is getting the keyset of the membrane metabolisms
// MEMBRANE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the membrane content is taken from the total
// membrane content associated to this rab/total area of the rab.  This is an average of the membrane content associated to the specific
// Rab.  Marker is set to zero.
		for (String mem : membraneMet){
			//				System.out.println(mem + "  MMEEMM " + selectedRab + "\n " + Results.getInstance().getContentDist());
			value = Results.getInstance().getContentDist().get(mem+selectedRab)
					/Results.getInstance().getTotalRabs().get(selectedRab);
			membraneContent.put(mem, value * area);
		}
		membraneContent.put("membraneMarker", 0d);
// SOLUBLE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the soluble content is taken from the total
// soluble content associated to this rab/total volume surrounded by the rab.  This is an average of the soluble content associated to the specific
// Rab.  Marker and mvb is set to zero
		for (String sol : solubleMet){
			value = Results.getInstance().getContentDist().get(sol+selectedRab)
					/Results.getInstance().getTotalVolumeRabs().get(selectedRab);
			solubleContent.put(sol, value * volume);
		}
		solubleContent.put("mvb", 0d);
		solubleContent.put("solubleMarker", 0d);
		Context<Object> context = ContextUtils.getContext(endosome);

		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		bud.area = area; initOrgProp.get("area");
		bud.volume = volume; initOrgProp.get("volume");
//		bud.size = initOrgProp.get("maxRadius");// radius of a sphere with the volume of the
		// cylinder
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		endosome.getSpace().moveTo(bud, rnd * 50, 10 + rnd* 30);
		endosome.getGrid().moveTo(bud, (int) rnd * 50, (int) (10 + rnd* 30));
	}
		
}
