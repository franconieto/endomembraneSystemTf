package immunity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

//import javax.media.opengl.GL2;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.render.RenderState;
import saf.v3d.scene.Position;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

public class PlasmaMembraneStyle implements StyleOGL2D<PlasmaMembrane> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(PlasmaMembrane object, VSpatial spatial) {
		// 15 measure the size of the grid.  The world is 50*15 X 50*15 or 750 X 750
		double initialAreaPM = object.getInitialPlasmaMembraneArea();
		double areaPM = object.getPlasmaMembraneArea();		
//		System.out.println(areaPM + "areas PM  " + initialAreaPM);
		double angle1 = 40;//Math.random()* 40 + 40;
		double angle2 = 40;//Math.random()* 40 + 40;		
        Shape rec = new RoundRectangle2D.Double(-750/2, -750/2, areaPM/initialAreaPM*750, areaPM/initialAreaPM*750,  angle1, angle2);
//      arguments x, y, ancho, largo, corner angle (small sharp), side curvature (small, straight)
        VSpatial shape = this.factory.createShape(rec);
        return shape;
//		VSpatial createRectangle = this.factory.createRectangle((int) (areaPM/initialAreaPM*750), (int) (areaPM/initialAreaPM*750));
//		return createRectangle;
	}

	@Override
	public Color getColor(PlasmaMembrane object) {
		// eventually the color will reflect some local PlasmaMembrane characteristics
		int red = (int)object.getPmcolor();
		return new Color(255-red, 255, 255-red);
	}

	@Override
	public int getBorderSize(PlasmaMembrane object) {
		//if larger than 0, form a nice grid
		return 10;
	}

	@Override
	public Color getBorderColor(PlasmaMembrane object) {
		return new Color(50,50,50);
	}

	@Override
	public float getRotation(PlasmaMembrane object) {
		return 0;
	}

	@Override
	public float getScale(PlasmaMembrane object) {	
		return (float) 1;
	}

	@Override
	public String getLabel(PlasmaMembrane object) {
		return ""; 
	}
	@Override
	public Font getLabelFont(PlasmaMembrane object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLabelYOffset(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getLabelPosition(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelColor(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return null;
	}

}
