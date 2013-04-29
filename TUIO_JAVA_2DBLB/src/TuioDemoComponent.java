/*
	TUIO Java Demo - part of the reacTIVision project
	http://reactivision.sourceforge.net/

	Copyright (c) 2005-2009 Martin Kaltenbrunner <mkalten@iua.upf.edu>

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import TUIO.*;

public class TuioDemoComponent extends JComponent implements TuioListener {

	private Hashtable<Long,TuioDemoObject> objectList = new Hashtable<Long,TuioDemoObject>();
	private Hashtable<Long,TuioCursor> cursorList = new Hashtable<Long,TuioCursor>();
	private Hashtable<Long,TuioBlob> blobList = new Hashtable<Long,TuioBlob>();
	public static final int finger_size = 15;
	public static final int object_size = 60;
	public static final int table_size = 760;
	
	public static int width, height;
	private float scale = 1.0f;
	public boolean verbose = false;
			
	public void setSize(int w, int h) {
		super.setSize(w,h);
		width = w;
		height = h;
		scale  = height/(float)TuioDemoComponent.table_size;	
	}
	
	public void addTuioObject(TuioObject tobj) {
		TuioDemoObject demo = new TuioDemoObject(tobj);
		objectList.put(tobj.getSessionID(),demo);

		if (verbose) 
			System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());	
	}

	public void updateTuioObject(TuioObject tobj) {

		TuioDemoObject demo = (TuioDemoObject)objectList.get(tobj.getSessionID());
		demo.update(tobj);
		
		if (verbose) 
			System.out.println("set obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel()); 	
	}
	
	public void removeTuioObject(TuioObject tobj) {
		objectList.remove(tobj.getSessionID());
		
		if (verbose) 
			System.out.println("del obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+")");	
	}

	public void addTuioCursor(TuioCursor tcur) {
	
		if (!cursorList.containsKey(tcur.getSessionID())) {
			cursorList.put(tcur.getSessionID(), tcur);
			repaint();
		}
		
		if (verbose) 
			System.out.println("add cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY());	
	}

	public void updateTuioCursor(TuioCursor tcur) {

		repaint();
		
		if (verbose) 
			System.out.println("set cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY()+" "+tcur.getMotionSpeed()+" "+tcur.getMotionAccel()); 
	}
	
	public void removeTuioCursor(TuioCursor tcur) {
	
		cursorList.remove(tcur.getSessionID());	
		repaint();
		
		if (verbose) 
			System.out.println("del cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+")"); 
	}

	public void refresh(TuioTime frameTime) {
		repaint();
	}
	
	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
	
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
		g2.setColor(Color.white);
		g2.fillRect(0,0,width,height);
	
		int w = (int)Math.round(width-scale*finger_size/2.0f);
		int h = (int)Math.round(height-scale*finger_size/2.0f);
		
		Enumeration<TuioCursor> cursors = cursorList.elements();
		while (cursors.hasMoreElements()) {
			TuioCursor tcur = cursors.nextElement();
			if (tcur==null) continue;
			Vector<TuioPoint> path = tcur.getPath();
			TuioPoint current_point = path.elementAt(0);
			if (current_point!=null) {
				// draw the cursor path
				g2.setPaint(Color.blue);
				for (int i=0;i<path.size();i++) {
					TuioPoint next_point = path.elementAt(i);
					g2.drawLine(current_point.getScreenX(w), current_point.getScreenY(h), next_point.getScreenX(w), next_point.getScreenY(h));
					current_point = next_point;
				}
			}
			
			// draw the finger tip
			g2.setPaint(Color.lightGray);
			int s = (int)(scale*finger_size);
			//System.out.println("tcur :"+tcur.xpos+","+tcur.ypos);
			g2.fillOval(current_point.getScreenX(w-s/2),current_point.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawString(tcur.getCursorID()+"",current_point.getScreenX(w),current_point.getScreenY(h));
		}

		// draw the objects
		Enumeration<TuioDemoObject> objects = objectList.elements();
		while (objects.hasMoreElements()) {
			TuioDemoObject tobj = objects.nextElement();
			if (tobj!=null) tobj.paint(g2, width,height);
		}
		
		// draw the blobs
		Enumeration<TuioBlob> blobs = blobList.elements();
		while (blobs.hasMoreElements()) {
			TuioBlob tblb = blobs.nextElement();
			
			
			
			if (tblb==null) continue;
			Vector<TuioPoint> pathblb = tblb.getPath();
			TuioPoint current_pointblb = pathblb.elementAt(0);
		
			if (current_pointblb!=null) {
				// draw the cursor path
				g2.setPaint(Color.blue);
				for (int i=0;i<pathblb.size();i++) {
					TuioPoint next_point = pathblb.elementAt(i);
					g2.drawLine(current_pointblb.getScreenX(w), current_pointblb.getScreenY(h), next_point.getScreenX(w), next_point.getScreenY(h));
					current_pointblb = next_point;
				}
			}
			
			// draw the finger tip
			g2.setPaint(Color.lightGray);
			int s = (int)(scale*finger_size);
			
			g2.fillOval(current_pointblb.getScreenX(w-s/2),current_pointblb.getScreenY(h-s/2),s,s);
			//System.out.println("puntox:"+current_pointblb.getScreenX(w-s/2));
			g2.setPaint(Color.black);
			g2.drawString(tblb.getBlobID()+"",current_pointblb.getScreenX(w),current_pointblb.getScreenY(h));
			
			Iterator<TuioPoint> ite=tblb.getPuntos().iterator();
			while(ite.hasNext()){
				TuioPoint pttemp=ite.next();
				g2.setPaint(Color.MAGENTA);
				g2.fillOval((int)pttemp.getScreenX(w-s/2),(int)pttemp.getScreenY(h-s/2),s,s);
				g2.setPaint(Color.black);
				
			}
/*
			g2.setPaint(Color.MAGENTA);
			g2.fillOval((int)tblb.getPunto1().getScreenX(w-s/2),(int)tblb.getPunto1().getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawLine((int)tblb.getPunto1().getScreenX(w-s/2), (int)tblb.getPunto1().getScreenY(h-s/2), (int)tblb.getPunto2().getScreenX(w-s/2), (int)tblb.getPunto2().getScreenY(h-s/2));
			//System.out.println("tcur :"+tblb.getPunto1().xpos+","+tblb.getPunto1().ypos);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval((int)tblb.getPunto2().getScreenX(w-s/2),(int)tblb.getPunto2().getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawLine((int)tblb.getPunto2().getScreenX(w-s/2), (int)tblb.getPunto2().getScreenY(h-s/2), (int)tblb.getPunto3().getScreenX(w-s/2), (int)tblb.getPunto3().getScreenY(h-s/2));
			//System.out.println("tcur :"+tblb.getPunto2().xpos+","+tblb.getPunto2().ypos);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval((int)tblb.getPunto3().getScreenX(w-s/2),(int)tblb.getPunto3().getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawLine((int)tblb.getPunto3().getScreenX(w-s/2), (int)tblb.getPunto3().getScreenY(h-s/2), (int)tblb.getPunto4().getScreenX(w-s/2), (int)tblb.getPunto4().getScreenY(h-s/2));
			//System.out.println("tcur :"+tblb.getPunto3().xpos+","+tblb.getPunto3().ypos);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval((int)tblb.getPunto4().getScreenX(w-s/2),(int)tblb.getPunto4().getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawLine((int)tblb.getPunto4().getScreenX(w-s/2), (int)tblb.getPunto4().getScreenY(h-s/2), (int)tblb.getPunto5().getScreenX(w-s/2), (int)tblb.getPunto5().getScreenY(h-s/2));
			//System.out.println("tcur :"+tblb.getPunto4().xpos+","+tblb.getPunto4().ypos);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval((int)tblb.getPunto5().getScreenX(w-s/2),(int)tblb.getPunto5().getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			g2.drawLine((int)tblb.getPunto5().getScreenX(w-s/2), (int)tblb.getPunto5().getScreenY(h-s/2), (int)tblb.getPunto1().getScreenX(w-s/2), (int)tblb.getPunto1().getScreenY(h-s/2));
			*/
			//-----System.out.println("tcur :"+tblb.getPunto5().xpos+","+tblb.getPunto5().ypos);
			/*
			g2.setPaint(Color.MAGENTA);
			g2.fillOval(tblb.getPunto2().getScreenX(w-s/2),current_pointblb.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval(tblb.getPunto3().getScreenX(w-s/2),current_pointblb.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval(tblb.getPunto4().getScreenX(w-s/2),current_pointblb.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			
			g2.setPaint(Color.MAGENTA);
			g2.fillOval(tblb.getPunto5().getScreenX(w-s/2),current_pointblb.getScreenY(h-s/2),s,s);
			g2.setPaint(Color.black);
			
			
			*/
			
			
		}
	}

	@Override
	public void addTuioBlob(TuioBlob tblb) {
		if (!blobList.containsKey(tblb.getSessionID())) {
			blobList.put(tblb.getSessionID(), tblb);
			
			repaint();
		}
		
		if (verbose) 
			System.out.println("add cur "+tblb.getBlobID()+" ("+tblb.getSessionID()+") "+tblb.getX()+" "+tblb.getY());		
		
	}

	@Override
	public void removeTuioBlob(TuioBlob tblb) {
		blobList.remove(tblb.getSessionID());	
		repaint();
		
		if (verbose) 
			System.out.println("del blb "+tblb.getBlobID()+" ("+tblb.getSessionID()+")");	
	}

	@Override
	public void updateTuioBlob(TuioBlob tblb) {
	repaint();
		
		if (verbose) 
		//	System.out.println("set obj "+tblb.getBlobID()+" ("+tblb.getSessionID()+") "+tblb.getX()+" "+tblb.getY()+" "+tblb.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel()); 	
				System.out.println("set blb "+tblb.getBlobID()+" ("+tblb.getSessionID()+") "+tblb.getX()+" "+tblb.getY()+" "+" "+tblb.getMotionSpeed()+" "+tblb.getRotationSpeed()+" "+tblb.getMotionAccel()+" "+tblb.getRotationAccel()); 	
	
	}
}
