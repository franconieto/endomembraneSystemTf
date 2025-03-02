package immunity;

import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class EndosomeLysosomalDigestionStep {
	static double PI = Math.PI;
	static double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");
	
	
	public static void lysosomalDigestion(Endosome endosome) {
		double so = endosome.area;
		double vo = endosome.volume;
		// if high percentage of the membrane is RabD (LateEndosome) digest lysosome
		if (endosome.rabContent.containsKey("RabD")
				&& Math.random() < endosome.rabContent.get("RabD") / endosome.area//)
				&& endosome.area > 4* Cell.mincyl)
			{
			squeezeOrganelle(endosome);//New para que no crezcan los lisosomas
			digestLysosome(endosome);
			}
		// All organelles with a s/v similar to the sphere undergoes a loss of volume
		else if (so*so*so/(vo*vo) < 1.1*36*PI// small surface/volume ration
				&& endosome.volume > 2*4/3*PI*rcyl*rcyl*rcyl)// it is big enough
			{			
			squeezeOrganelle(endosome);
			//Endosome.endosomeShape(endosome);
//			System.out.println(so/vo+" INICIAL "+so*so*so/(vo*vo)/(36*Math.PI) +" FINAL"+so/endosome.volume+endosome);
			}

	}

	private static void squeezeOrganelle(Endosome endosome) {		
//The Organelle volume is decreased.  Controls that it has enough volume to allocate the mvb and a bead
		
//		boolean isTubule = (endosome.volume/(endosome.area - 2*PI*rcyl*rcyl) <=rcyl/2);	
//		if (isTubule) return;
		double r = rcyl;		
		double newVolume = endosome.volume * 0.999;	//era 0.99	//
		double minV = 2*Math.PI*r*r*r;//minimal volume cylinder = volume of an internal vesicle
//		if it contains internal vesicles, the volume need to be enough
		double IVvol = 4/3*Math.PI*r*r*r;
		if (endosome.getSolubleContent().containsKey("mvb")) {
			minV = minV + endosome.getSolubleContent().get("mvb")* IVvol;//minimal volume + volume of all internal vesicles
		}
//		if it has a bead, the volume must be enough to contain it
		if (endosome.getSolubleContent().containsKey("solubleMarker")
				&& endosome.getSolubleContent().get("solubleMarker")>0.9) {
			minV = minV + ModelProperties.getInstance().getCellK().get("beadVolume"); // 5E8 bead volume. Need to be introduced in Model Properties
		}
		if(newVolume > minV)
		{
		endosome.volume = newVolume;	//squeeze only if there is enough volume	
		}
		else {endosome.volume = minV;
		}
	}

	private static void digestLysosome(Endosome endosome) {
		
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
			if (Math.random() < 0.01 * rabDratio) {// was 0.01/
				finalMvb = Math.round(initialMvb*0.99);//was 0.99, 
			} else {
				finalMvb = initialMvb;
			}
		}

//		Soluble component are digested proportional to the RabD content, except the soluble marker
//		Observo que membrane y soluble se digieren diferente.  Concluyo que la mayor parte de los cargos de membrana se digieren
//		por la formación de los mvb, no por la digestión aqui.  Los solubles no sufren esa digestión.  Voy a meter mayor digestión para solubles
		for (String sol : endosome.solubleContent.keySet()) {
				double solDigested = endosome.solubleContent.get(sol) * 0.0005 * rabDratio;
				endosome.solubleContent.put(sol, endosome.solubleContent.get(sol) - solDigested);
			}
		if (endosome.solubleContent.containsKey("mvb"))
			endosome.solubleContent.put("mvb", finalMvb);
		if (endosome.solubleContent.containsKey("solubleMarker") && endosome.solubleContent.get("solubleMarker")>0.9)
			endosome.solubleContent.put("solubleMarker", 1d);

		for (String mem : endosome.membraneContent.keySet()) {
				double memDigested = endosome.membraneContent.get(mem) * 0.0000001 * rabDratio;
				endosome.membraneContent.put(mem, endosome.membraneContent.get(mem) - memDigested);
			}
		if (endosome.membraneContent.containsKey("membraneMarker") && endosome.membraneContent.get("membraneMarker")>0.9){
			endosome.membraneContent.put("membraneMarker", 1d);}
//		endosome.membraneContent.put("vATPase", finalvATPase);

		
	}
}
