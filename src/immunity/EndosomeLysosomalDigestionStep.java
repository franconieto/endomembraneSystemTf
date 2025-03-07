package immunity;

import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class EndosomeLysosomalDigestionStep {

	
	public static void lysosomalDigestion(Endosome endosome) {
		// if low percentage of the membrane is RabD return
		if (!endosome.rabContent.containsKey("RabD")
				|| Math.random() > endosome.rabContent.get("RabD") / endosome.area)
			return;
		// soluble and membrane content is digested in a low percentage
		// (0.0001* proportion of RabD in the membrane)
		// volume is decreased proportional to the initial volume and also
		// considering the mvb that are digested
		double rabDratio = endosome.rabContent.get("RabD") / endosome.area;
		double volIV = 4 / 3 * Math.PI * Math.pow(Cell.rIV, 3d);
		double areaIV = 4 * Math.PI * Math.pow(Cell.rIV, 2d);
		double deltaV = 0d;
		double initialMvb = 0d;
		double finalMvb = 0d;
		double finalSolMark = 0d;
		double finalMemMark = 0d;
//		RandomEngine engine = new DRand();
//		Poisson poisson = new Poisson(2000, engine);
//		int poissonObs = poisson.nextInt();
//		System.out.println("                   POISSON DE 2000 "+poissonObs);
//		double finalvATPase = 0d;
//		Internal vesicles are digested proportional to the RabD content and to the number of internal vesicles
		if (endosome.solubleContent.containsKey("mvb")) {
			initialMvb = endosome.solubleContent.get("mvb");
			if (Math.random() < 0.01 * rabDratio * initialMvb) {
				finalMvb = initialMvb*0.99;
//				Area of the internal area digested is added to the plasma membrane (synthesis is assumed)
				double plasmaMembrane = PlasmaMembrane.getInstance().getPlasmaMembraneArea() + areaIV;
				PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
			} else {
				finalMvb = initialMvb;
			}
		}
//		if (endosome.solubleContent.containsKey("solubleMarker")) {
//				finalSolMark = 1d;
//			}
//		if (endosome.membraneContent.containsKey("membraneMarker")) {
//			finalMemMark = 1d;
//		}
//		finalvATPase = endosome.membraneContent.get("vATPase");
//		Soluble component are digested proportional to the RabD content
		for (String sol : endosome.solubleContent.keySet()) {
				double solDigested = endosome.solubleContent.get(sol) * 0.001
						* rabDratio;
				endosome.solubleContent.put(sol, endosome.solubleContent.get(sol) - solDigested);
			}
		if (endosome.solubleContent.containsKey("mvb"))
			endosome.solubleContent.put("mvb", finalMvb);
		if (endosome.solubleContent.containsKey("solubleMarker") && endosome.solubleContent.get("solubleMarker")>0.9)
			endosome.solubleContent.put("solubleMarker", 1d);

		for (String mem : endosome.membraneContent.keySet()) {
				double memDigested = endosome.membraneContent.get(mem) * 0.001 * rabDratio;
				endosome.membraneContent.put(mem, endosome.membraneContent.get(mem) - memDigested);
			}
		if (endosome.membraneContent.containsKey("membraneMarker") && endosome.membraneContent.get("membraneMarker")>0.9){
			endosome.membraneContent.put("membraneMarker", 1d);}
//		endosome.membraneContent.put("vATPase", finalvATPase);
		
			// volume is decreased
		if (endosome.solubleContent.containsKey("mvb")) {
				deltaV = (initialMvb - finalMvb) * volIV + endosome.volume * 0.001
						* rabDratio;
			} else {
				deltaV = endosome.volume * 0.001 * rabDratio;
			}
		endosome.volume = endosome.volume - deltaV;
		if (deltaV > 40000) EndosomeInternalVesicleStep.internalVesicle(endosome);
		Endosome.endosomeShape(endosome);
	}
}
