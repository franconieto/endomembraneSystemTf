package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class RecycleStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void recycle(Endosome endosome) {
		HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(endosome.getMembraneContent());
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(endosome.getSolubleContent());
		double cellLimit = 3 * Cell.orgScale;
		NdPoint myPoint = endosome.getSpace().getLocation(endosome);
			double x = myPoint.getX();
			double y = myPoint.getY();
//			if far from the PM no recycling
			if(!(isPointInSquare(x, y, 25, 25, 50-5*cellLimit))){ // if it is near the PM
//			if near the PM and larger domain is EE and is a tubule, recycle full fusion
//			if near the PM	and larger domain is TGN, full fusion
//			if near the PM	and larger domain is RE, full fusion 4%; 96% kiss and run
				
//			So, I am assuming a fast recycling cycle probably with Rab4 tubules 
/* I will test the possibility of recycling of the membrane and having a balance of EE
 * and PM membrane.
 */		
		
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelle = ModelProperties.getInstance().getRabOrganelle().get(maxRab);    
			if (organelle.equals("EE")) recycleEE(endosome, maxRab);
			else if (organelle.equals("RE")||organelle.equals("SE")) recycleRE(endosome, maxRab);
			else if (organelle.equals("TGN")) recycleRE(endosome, maxRab); //same rules than for RE
			else return;
		}
		else { // it is not near the PM
//		if near the ER and larger domain is ERGIC and it is a tubule, fuse back with ER 
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelle = ModelProperties.getInstance().getRabOrganelle().get(maxRab);    
			if (organelle.equals("ERGIC")) {
				recycleERGIC(endosome, maxRab);
			}
			else return;
		}
	
		}

	private static void recycleERGIC(Endosome endosome, String maxRab) {
//		System.out.println("RECYCLE ERGIC  " + endosome.area);
//		if near the PM and larger domain is ERGIC and is a tubule, fuse back with ER
		
		boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2); // should be /2
		if (!isTubule) return;// if it is not a tubule no recycling
		double recyProb = ModelProperties.getInstance().getRabRecyProb().get(maxRab) * endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb
				|| endosome.tickCount < 3000){
//			System.out.println(endosome.tickCount + "  ERGIC ER DISTANCE NNNNNOOOO " +
//				recyProb);
			return;}
		else {
// ER back transport
			
			EndoplasmicReticulum ER = EndoplasmicReticulum.getInstance();
//			Since ER in a cell is everywhere, I just make any ERGIC tubule to fuse back to e ER
//			without measuring any distance
//			double distance = distanceErgicEr(endosome, ER);
//			System.out.println("ERGIC ER DISTANCE  " + distance);
//			if (distance < 10) 
			{
//			System.out.println("ERGIC RECYCLE TO ER  ");
			HashMap<String, Double> membraneRecycle = ER.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}
			endosome.membraneContent.clear();

			HashMap<String, Double> solubleRecycle = ER.getSolubleRecycle();
//			double endopH = endosome.solubleContent.get("proton");
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}

			ER.getendoplasmicReticulumTimeSeries().clear();
			double area = endosome.area + ER.getEndoplasmicReticulumArea();
			ER.setEndoplasmicReticulumArea(area);
;
//			System.out.println("RECYCLING OF ER  " + area);
//			to delete the recycled endosome.
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);

		}
		}
	}
		


	private static void recycleRE(Endosome endosome, String maxRab) {
//		System.out.println("RECYCLE RRE  " + endosome.area);

		//NEW RULES
		/*
		 * If near the PM and it is a Recycling Endosome, kiss and run exocytosis
		 * (recycle all but the endosome is preserved 4% the endosome is eliminated
		 * (full fusion)
		 * TGN is full fusion
		 * 
		 */
		double recyProb = ModelProperties.getInstance().getRabRecyProb().get(maxRab) * endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb){
			return;}
		else {
			// Recycle membrane content
			HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
					.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}

			HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
					.getSolubleRecycle();
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}
//			to delete the 100% of the TGN and 2% of the RE fusing with PM
			if (maxRab.equals("RabE")
					|| (maxRab.equals("RabC") && Math.random()<0.04)// era 0.02
					) {
				PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
				double plasmaMembrane = endosome.area + PlasmaMembrane.getInstance().getPlasmaMembraneArea();
				PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
//				System.out.println("SECRETION TGN OR RE" + plasmaMembrane);
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);
			}
			else {// if it is not deleted, it forms an empty tubule
			endosome.membraneContent.clear();
			endosome.solubleContent.clear();		
			endosome.getEndosomeTimeSeries().clear();
			PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
			double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");// radius tubule
			double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);// length of a tubule with the area of the recycled endosome
			endosome.volume = Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a tubule.
			endosome.solubleContent.put("protonEn", 3.98e-5*endosome.volume); //pH 7.4
			endosome.heading = -90; //moving in the nucleus direction
			}
		}

		
	}

	private static void recycleEE(Endosome endosome, String maxRab) {
//		System.out.println("RECYCLE EE  " + endosome.area);

		//NEW RULES
//		if near the PM and larger domain is EE and is a tubule, recycle
//		So, I am assuming a fast recycling cycle probably with Rab4 tubules 
/* I will test the posibility of recycling of the membrane and having a balance of EE
* and PM membrane.
* firt tests if it is a tubule (return) then higher probabilities to tubules with high proportion of EE domain
*/
		//boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2); // should be /2
		//if (!isTubule) return;// if it is not a tubule no recycling
		double recyProb = ModelProperties.getInstance().getRabRecyProb().get(maxRab)*endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb
				|| endosome.tickCount<1000){
			return;}
		else {
// EE RECYCLING
// Recycle membrane content
			HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
					.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}
			endosome.membraneContent.clear();

			HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
					.getSolubleRecycle();
//			double endopH = endosome.solubleContent.get("proton");
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}


			PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
			double plasmaMembrane = endosome.area + PlasmaMembrane.getInstance().getPlasmaMembraneArea();
			PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
			System.out.println("RECYCLING OF EE  " + endosome.tickCount);

//			to delete the recycled EE endosome.
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);

		}

		
	}
    public static boolean isPointInSquare(double x, double y, double x0, double y0, double ll) {
        double halfSide = ll / 2.0;
        // Calculate the boundaries of the square
        double left = x0 - halfSide;
        double right = x0 + halfSide;
        double top = y0 + halfSide;
        double bottom = y0 - halfSide;

        // Check if the point is within the boundaries
        return (x >= left && x <= right && y >= bottom && y <= top);
    }

////	Collections.shuffle(mts); 19-7-21 No need to shuffle because the closest MT will be selected
//	double dist = 1000;
//	MT mt = null;
////NEW		RULE 19-7-2021.  The organelle will sense the MT around it and select the closest one (minimal absolute distance)
//	for (MT mmt : mts) {
//		double ndist = distance(endosome, mmt);
////		The distance is in space units from 0 to 50. At scale 1, the space is 1500 nm.  At 
////		scale 0.5 it is 3000 nm.
////		Hence to convert to nm, I must multiply by 45 (2250/50) and divide by scale. An organelle will sense MT
////		at a distance less than its size.
//		if (Math.abs(ndist) <= Math.abs(dist)) {
//			dist = ndist; 
//			mt = mmt;
//		}
//	}
    
	private static double distanceErgicEr(Endosome endosome, Object eR) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		// If the line passes through two points P1=(x1,y1) and P2=(x2,y2) then
		// the distance of (x0,y0) from the line is calculate from wiki
		System.out.println("EEEEEEEEEEEEEEEE  " + endosome.area);
		NdPoint pt = space.getLocation(endosome);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double) ((MT) eR).getYend();
		double ymin = (double) ((MT) eR).getYorigin();
		double xmax = (double) ((MT) eR).getXend();
		double xmin = (double) ((MT) eR).getXorigin();

		
        // Calculate the projection of C onto the line containing AB
        Point A = new Point(xmin, ymin);
        Point B = new Point(xmax, ymax);
        Point C = new Point(xpt, ypt);
        LineSegment segment = new LineSegment(A, B);
        double ACx = C.x - segment.A.x;
        double ACy = C.y - segment.A.y;
        double ABx = segment.B.x - segment.A.x;
        double ABy = segment.B.y - segment.A.y;
        double dotProduct = ACx * ABx + ACy * ABy;
        double t = dotProduct / (ABx * ABx + ABy * ABy);

        // Check if the projection point lies within the segment bounds
        if (!(t >= 0 && t <= 1)) return 2000;
		
		double a = (xmax - xmin) * (ymin - ypt) - (ymax - ymin)
				* (xmin - xpt);
		double b = Math.sqrt((ymax - ymin) * (ymax - ymin) + (xmax - xmin)
				* (xmax - xmin));
		double distance = a / b;
//		The distance has a sign that it is used to move the organelle to the position in the Mt
		return distance;
	}
    
	}

