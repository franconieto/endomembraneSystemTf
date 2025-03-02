package immunity;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

public class MTStyle implements StyleOGL2D<MT> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}
	// World is a 50 X 50 space.  Each unit of space has a size of 15 
	// hence the world is 750 X 750 size in repast units
	@Override
	public VSpatial getVSpatial(MT object, VSpatial spatial) {
		double h =  object.getMtheading();
		double hh = h * Math.PI /180;
		int length = (int) object.getLength()*750/50;

		int diameterMT = (int) (12*Cell.orgScale);
//		System.out.println(diameterMT + " di le " + length);
		VSpatial createRectangle = this.factory.createRectangle(diameterMT, length);
		return createRectangle;

	}

	@Override
	public Color getColor(MT object) {
		//int f = Math.abs( (int)object.getArea() % 256 );
		return new Color(0, 0, 200, 80);
	}

	@Override
	public int getBorderSize(MT object) {
		return 0;
	}

	@Override
	public Color getBorderColor(MT object) {
		return new Color(100);
	}

	@Override
	public float getRotation(MT object) {
		float h = (float) (object.getMtheading());
		return h;
	}

	@Override
	public float getScale(MT object) {
		
		return (float) 1;
	}

	@Override
	public String getLabel(MT object) {
		return "";//object.getXend()+" "+object.getYend()+" "+object.getMtheading(); 
	}
	@Override
	public Font getLabelFont(MT object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(MT object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(MT object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(MT object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(MT object) {
		return new Color(100);
	}

}