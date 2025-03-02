package immunity;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import org.COPASI.CModel;
import org.COPASI.CTimeSeries;
import org.apache.commons.lang3.StringUtils;

import repast.simphony.engine.environment.RunEnvironment;

public class EndosomeCopasiStep {
	public static double delta = 0;
	
	public static void antPresTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getEndosomeTimeSeries().isEmpty()){			
			callLipidPresentation(endosome);
			timeSeriesLoadintoEndosome(endosome);

			return;
		} 
		if (tick >= Collections.max(endosome.getEndosomeTimeSeries().keySet())) {
			timeSeriesLoadintoEndosome(endosome);
			endosome.getEndosomeTimeSeries().clear();
			callLipidPresentation(endosome);

			return;
			}
		if (!endosome.endosomeTimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {

			timeSeriesLoadintoEndosome(endosome);
			return;

		}
	}
	public static void timeSeriesLoadintoEndosome(Endosome endosome){
//		values in endosomeTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		TreeMap< Integer, HashMap<String, Double>> orderedValues = new TreeMap<Integer ,HashMap<String, Double>>(endosome.endosomeTimeSeries);
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.endosomeTimeSeries.get(tick));
		HashMap<String, Double> pastValues = new HashMap<String, Double>();
//		The release of metabolites into cytosol from a time series must consider that it is a "delta".
//		Supose a metabolite is not modified by the copasi, if the value in the time series is added, the
//		concentration will artificially build up. At time zero of the series, the delta is zero for all the 
//		metabolites.
		int pastTick = 0;
		if (tick == endosome.endosomeTimeSeries.firstKey()){ ;// first tick in the time series
		pastValues = presentValues;
		pastTick = tick;
		} else {
			pastTick = endosome.endosomeTimeSeries.lowerKey(tick);
			pastValues = endosome.endosomeTimeSeries.get(pastTick);
		}
		
		for (String met :presentValues.keySet()){
			// if the content is cytosolic, increase the cell pull proportinal to the volume.  The content is
			//			eliminated from endosome
			String met1 = met;//.substring(0, met.length()-2); COPASI uses metabolite names  with the substring incorporated
			if (met.endsWith("En") && ModelProperties.getInstance().solubleMet.contains(met)) {
				double metValue = presentValues.get(met)* endosome.volume;
				endosome.solubleContent.put(met, metValue);
			}
			else if (met.endsWith("En") && ModelProperties.getInstance().membraneMet.contains(met)) {
				double metValue = presentValues.get(met)* endosome.area;
				endosome.membraneContent.put(met, metValue);
			}
//			metabolites in the Cell are expressed in concentration. I am using the area ratio between PM and Cell 
//			for dilution of the metabilite that is released into the cell.  I may use volume ratio?
//			Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
//			Since the endosome is releasing Cyto metabolites at each tick, what must be incorporated is the delta with respect to the previous tick.
//			At tick = 0, nothing is released (pastValues = presentValues)
			else if (met.endsWith("Cy")){
				 if (!Cell.getInstance().getSolubleCell().containsKey(met)){Cell.getInstance().getSolubleCell().put(met, 0.0);}
//				 System.out.println("TICK " + met+tick + "\n " + pastTick + "\n " + presentValues.get(met) + "\n " + pastValues.get(met) + "\n" + endosome
//				 );
				double delta =  presentValues.get(met) - pastValues.get(met);
				double metValue = Cell.getInstance().getSolubleCell().get(met)
						+ delta * endosome.volume/Cell.getInstance().getCellVolume();
				Cell.getInstance().getSolubleCell().put(met, metValue);
//			 System.out.println("SOLUBLE CELL " + Cell.getInstance().getSolubleCell()+ " MET " + met);
//				endosome.solubleContent.remove(met);
			}
//			Only a fraction of the metabolite in the plasma membrane participates
//			in copasi, hence the concentration are added to the existing values.
//			Ask if the metabolite is soluble or membrane associated.  If is not in PM set to zero the metabolite
//			Since the endosome is releasing PM metabolites at each tick, what must be incorporated is the delta with respect to the previous tick.
//			At tick = 0, nothing is released (pastValues = presentValues)
			else if (met.endsWith("Pm") && ModelProperties.getInstance().getSolubleMet().contains(met)) {
				 if (!PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met))PlasmaMembrane.getInstance().getSolubleRecycle().put(met, 0.0);
				 double delta =  presentValues.get(met) - pastValues.get(met);
				 double metValue = PlasmaMembrane.getInstance().getSolubleRecycle().get(met)
						+ delta * endosome.volume;
				PlasmaMembrane.getInstance().getSolubleRecycle().put(met, metValue);
			}
			else if (met.endsWith("Pm") && ModelProperties.getInstance().getMembraneMet().contains(met)) {
				 if (!PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met))PlasmaMembrane.getInstance().getMembraneRecycle().put(met, 0.0);
				 double delta =  presentValues.get(met) - pastValues.get(met);
				 double metValue = PlasmaMembrane.getInstance().getMembraneRecycle().get(met)
						+ delta * endosome.area;
				PlasmaMembrane.getInstance().getMembraneRecycle().put(met, metValue);
			}
		}
		
	}
	
	public static void callLipidPresentation(Endosome endosome) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		EndosomeCopasi lipidMetabolism = EndosomeCopasi.getInstance();
// to set the size of the compartments in copasi.  Soluble = volume ; membrane = area		
		int iMax = (int) lipidMetabolism.getModel().getCompartments().size();
//		lipidMetabolism.getModel().setModelType(20);
//		System.out.println("Model type  " + lipidMetabolism.getModel().getModelType());
		for (int i = 0;i < iMax;++i)
        {
		if (lipidMetabolism.getModel().getCompartment(i).getObjectName().equals("membrane"))
			lipidMetabolism.getModel().getCompartment(i).setInitialValue(endosome.area);
//      
		else if (lipidMetabolism.getModel().getCompartment(i).getObjectName().equals("soluble"))
			lipidMetabolism.getModel().getCompartment(i).setInitialValue(endosome.volume);
		else 
			lipidMetabolism.getModel().getCompartment(i).setInitialValue(1);
//		System.out.println("compartimiento volumen \t" + lipidMetabolism.getModel().getCompartment(i).getObjectName() + lipidMetabolism.getModel().getCompartment(i).getInitialValue());
        }

		Set<String> metabolites = lipidMetabolism.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		
		for (String met : metabolites) {
			String met1 = met;//met.substring(0, met.length()-2);
//			for endosomes and other organelles, all the metabolites participate in the reaction
			if (met.endsWith("En") && endosome.membraneContent.containsKey(met1)) {
				double metValue = endosome.membraneContent.get(met1)/endosome.area;
//				double a = sigFigs(metValue, 5);
//				System.out.println(metValue +" REDONDEO "+ a);
				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, sigFigs(metValue,6));
			} else if (met.endsWith("En") && endosome.solubleContent.containsKey(met1)) {
				double metValue = endosome.solubleContent.get(met1)/endosome.volume;
				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, sigFigs(metValue,6));
//				for metabolites in the plasma membrane, only a fraction participate in the reaction and it is consumed 
//				for soluble metabolites, proportional to the volume and for membrane metabolites proportional to the area
//			} else if (met.endsWith("Pm") && PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met1)) {
//				double metValue = PlasmaMembrane.getInstance().getMembraneRecycle().get(met1)/PlasmaMembrane.getInstance().getPlasmaMembraneArea();
//				double metLeft = metValue* (PlasmaMembrane.getInstance().getPlasmaMembraneArea() - endosome.area);
//				PlasmaMembrane.getInstance().getMembraneRecycle().put(met1, metLeft);
//				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
//			} else if (met.endsWith("Pm") && PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met1)) {
//				double metValue = PlasmaMembrane.getInstance().getSolubleRecycle().get(met1)/PlasmaMembrane.getInstance().getPlasmaMembraneVolume();
//				double metLeft = metValue* (PlasmaMembrane.getInstance().getPlasmaMembraneVolume() - endosome.volume);
//				PlasmaMembrane.getInstance().getSolubleRecycle().put(met1, metLeft);
//				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
//				Rabs are included only for reactions that occurs in a specific compartment. 
			} else if (met.startsWith("Rab") && endosome.rabContent.containsKey(met)) {
				double metValue = endosome.rabContent.get(met)/endosome.area;
				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, sigFigs(metValue,6));
//				ERROR
//				for metabolites in the cell, only a fraction participate in the reaction and it is consumed 
//				metabolites in the cell are in concentration units and only soluble metabolites are considered
//				I found this ERROR when observed that the ovaCy was going down even in the absence of proteosomal hydrolysis
//				CORRECTION
//				A portion of the CY, the size of the organelle is modified by COPASI, but the DELTA is
//				considered to change the CY.  Hence if no change, CY no changes; if it changes it does
//				proportional to the volume of endosome.  In brief, no need to consider a metLeft
			} else if (met.endsWith("Cy") && Cell.getInstance().getSolubleCell().containsKey(met1)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
//				System.out.println(Cell.area + "volume cell "+Cell.volume);
//				double metLeft = metValue*(Cell.getInstance().getCellVolume() - endosome.volume)/(Cell.getInstance().getCellVolume());
//				Cell.getInstance().getSolubleCell().put(met1, metLeft);
				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, sigFigs(metValue,6));
//			} else if (met.equals("area")) {
//				double metValue = endosome.area;
////				System.out.println(Cell.area + "volume cell "+Cell.volume);
//				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
//			} else if (met.equals("volume")) {
//				double metValue = endosome.volume;
////				System.out.println(Cell.area + "volume cell "+Cell.volume);
//				lipidMetabolism.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
			} else {
				lipidMetabolism.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		lipidMetabolism.setInitialConcentration("protonCy", 1e-04); // pH 7
//		lipidMetabolism.setInitialConcentration("protonEn", 1e-04); // pH 7
		localM.put("protonCy", 1e-04);
//		localM.put("protonEn", 1e-04);
//		System.out.println(endosome.membraneContent.get("pepMHCIEn")+" METABOLITES COPASI "+ localM);
//for (String met:localM.keySet()) {if (localM.get(met).isNaN()) localM.put(met,0.0);}
//		System.out.println(endosome.membraneContent.get("pepMHCIEn")+" METABOLITES IN "+ localM);

//		if (localM.get("proton")==null||localM.get("proton") < 1e-05){
//			lipidMetabolism.setInitialConcentration("proton", 1e-04);
//			localM.put("proton", 1e-04);
//		}
		

		lipidMetabolism.runTimeCourse();
		

		CTimeSeries timeSeries = lipidMetabolism.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		double initpepMHC = 0d;
		double finalpepMHC = 0d;
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		System.out.println("Serie de tiempo "+timeSeries.getRecordedSteps());

		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
//				if (timeSeries.getTitle(met).equals("protonEn")) 
//				System.out.println(met + " EndosomeEn "+timeSeries.getTitle(met));
				if (timeSeries.getTitle(met).equals("pepMHCIEn")){
						if (time == 1 ) initpepMHC = timeSeries.getConcentrationData(time, met);
						else if (time == stepNro - 1 ) finalpepMHC = timeSeries.getConcentrationData(time, met);
				}
				value.put(timeSeries.getTitle(met), sigFigs(timeSeries.getConcentrationData(time, met),6));
				endosome.getEndosomeTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		if(initpepMHC > 0 || finalpepMHC > 0) {
//		System.out.println(initpepMHC +" ENDOSOME WITH pepMHC "+ finalpepMHC + endosome.toString());
		endosome.complexMHC = finalpepMHC;
		endosome.assembleMHC = finalpepMHC - initpepMHC;
		}	
		
//		delta = delta + (localM.get("cMHCIEn")+localM.get("oMHCIEn")+localM.get("pepMHCIEn"))-
//				(timeSeries.getConcentrationData(1, 9)+timeSeries.getConcentrationData(1, 10)+timeSeries.getConcentrationData(1, 11));
//			System.out.println("DELTA "+ delta);		
		}
	public static double sigFigs(double n, int sig) {
//		if (Math.abs(n) < 1E-20) return 0d;
//		else 
//		{
		Double mult = Math.pow(10, sig - Math.floor(Math.log(n) / Math.log(10) + 1));
		if (mult.isNaN()) return 0.0;
		else return Math.round(n * mult) / mult;
//	    }
	}
	
	
}

