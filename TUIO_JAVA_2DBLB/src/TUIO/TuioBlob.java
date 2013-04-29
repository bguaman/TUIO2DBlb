/*
	TUIO Java backend - part of the reacTIVision project
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
package TUIO;

import java.util.ArrayList;

/**
 * The TuioObject class encapsulates /tuio/2Dobj TUIO objects.
 *
 * @author Martin Kaltenbrunner
 * @version 1.4
 */ 
public class TuioBlob extends TuioContainer {
	
	/**
	 * The individual blob ID number that is assigned to each TuioBlob.
	 */ 
	protected int blob_id;	
	
	/**
	 * The blob contact angle value.
	 */ 
	protected float contactAngle;
	/**
	 * The blob contact width value.
	 */ 
	protected float contactWidth;
	/**
	 * The blob contact height value.
	 */ 
	protected float contactHeight;
	/**
	 * The blob contact area value.
	 */ 
	protected float contactArea;
			
	/**
	 * The rotation speed value.
	 */ 
	protected float rotation_speed;
	/**
	 * The rotation acceleration value.
	 */ 
	protected float rotation_accel;
	/**
	 * Defines the ROTATING state.
	 */ 
	ArrayList<TuioPoint> puntos;
	
	public TuioPoint punto1 , punto2 , punto3 , punto4 , punto5 ;
	
	public static final int TUIO_ROTATING = 5;
	

	/**
	 * This constructor takes a TuioTime argument and assigns it along with the provided 
	 * Session ID, Blob ID, X and Y coordinate and contactAngle to the newly created TuioBlob.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	si	the Session ID  to assign
	 * @param	bi	the Blob Symbol ID  to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param	a	the contact angle to assign
	 */
	public TuioBlob (TuioTime ttime, long si, int bi, float xp, float yp, float cAngle, float cw, float ch, float cArea,ArrayList<TuioPoint> points) {
		super(ttime, si, xp, yp);
		blob_id = bi;
		contactAngle = cAngle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = cArea;
		rotation_speed = 0.0f;
		rotation_accel = 0.0f;
		puntos=points;
		
	}

	


	public TuioBlob (long si, int bi, float xp, float yp, float cAngle, float cw, float ch, float cArea,ArrayList<TuioPoint> points) {
		super(si,xp,yp);
		blob_id = bi;
		contactAngle = cAngle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = cArea;
		rotation_speed = 0.0f;
		rotation_accel = 0.0f;
		puntos=points;
	}

	/**
	 * This constructor takes the attributes of the provided TuioBlob 
	 * and assigns these values to the newly created TuioBlob.
	 *
	 * @param	tobj	the TuioBlob to assign
	 */
	public TuioBlob (TuioBlob tblb) {
		super(tblb);
		blob_id = tblb.getBlobID();
		contactAngle = tblb.getContactAngle();
		contactWidth = tblb.getContactWidth();
		contactHeight = tblb.getContactHeight();
		contactArea = tblb.getContactArea();
		rotation_speed = tblb.getRotationSpeed();
		rotation_accel = tblb.getRotationAccel();
		this.puntos=tblb.getPuntos();
	}

	/**
	 * Takes a TuioTime argument and assigns it along with the provided 
	 * X and Y coordinate, contact angle, contact width, contact height, contact area,
	 * X and Y velocity, motion acceleration,
	 * rotation speed and rotation acceleration to the private TuioBlob attributes.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp		the X coordinate to assign
	 * @param	yp		the Y coordinate to assign
	 * @param	cangle	the contact angle to assign
	 * @param	cw		the contact width to assign
	 * @param	ch		the contact height to assign
	 * @param	carea	the contact width to assign
	 * @param	xs	the X velocity to assign
	 * @param	ys	the Y velocity to assign
	 * @param	as	the rotation velocity to assign
	 * @param	ma	the motion acceleration to assign
	 * @param	ra	the rotation acceleration to assign
	 */
	public void update (TuioTime ttime, float xp, float yp, float cangle, float cw, float ch, float carea, float xs, float ys, float as, float ma, float ra) {
		super.update(ttime,xp,yp,xs,ys,ma);
		contactAngle = cangle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = carea;
		rotation_speed = as;
		rotation_accel = ra;
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}

	/**
	 * Assigns the provided 
	 * X and Y coordinate, contact angle, contact width, contact height, contact area,
	 * X and Y velocity, motion acceleration,
	 * rotation speed and rotation acceleration to the private TuioBlob attributes.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp		the X coordinate to assign
	 * @param	yp		the Y coordinate to assign
	 * @param	cangle	the contact angle to assign
	 * @param	cw		the contact width to assign
	 * @param	ch		the contact height to assign
	 * @param	carea	the contact width to assign
	 * @param	xs	the X velocity to assign
	 * @param	ys	the Y velocity to assign
	 * @param	as	the rotation velocity to assign
	 * @param	ma	the motion acceleration to assign
	 * @param	ra	the rotation acceleration to assign	 */
	public void update (float xp, float yp, float cangle, float cw, float ch, float carea, float xs, float ys, float as, float ma, float ra ,ArrayList<TuioPoint> points) {
		super.update(xp,yp,xs,ys,ma);
		contactAngle = cangle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = carea;
		rotation_speed = as;
		rotation_accel = ra;
		this.puntos=points;
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}
	

	
	public void update (float xp, float yp, float xs, float ys, float ma,ArrayList<TuioPoint> points) {
		super.update(xp,yp,xs,ys,ma);
		
		
		puntos=points;
		
		
	}

	
	
	
	
	
	/**
	 * Takes a TuioTime argument and assigns it along with the provided 
	 * X and Y coordinate and angle to the private TuioBlob attributes.
	 * The speed and acceleration values are calculated accordingly.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param	a	the angle coordinate to assign
	 */
	public void update (TuioTime ttime, float xp, float yp, float cangle, float cw, float ch, float cArea) {
		TuioPoint lastPoint = path.lastElement();
		super.update(ttime,xp,yp);

		TuioTime diffTime = currentTime.subtract(lastPoint.getTuioTime());
		float dt = diffTime.getTotalMilliseconds()/1000.0f;
		float last_angle = contactAngle;
		float last_rotation_speed = rotation_speed;
		contactAngle = cangle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = cArea;
		
		float da = (this.contactAngle-last_angle)/(2.0f*(float)Math.PI);
		if (da>0.75f) da-=1.0f;
		else if (da<-0.75f) da+=1.0f;
		
		rotation_speed = da/dt;
		rotation_accel = (rotation_speed - last_rotation_speed)/dt;
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}
	
	public void update (TuioTime ttime, float xp, float yp, float cangle, float cw, float ch, float cArea,TuioPoint punto1,TuioPoint punto2,TuioPoint punto3,
			TuioPoint punto4,TuioPoint punto5) {
		TuioPoint lastPoint = path.lastElement();
		super.update(ttime,xp,yp);

		TuioTime diffTime = currentTime.subtract(lastPoint.getTuioTime());
		float dt = diffTime.getTotalMilliseconds()/1000.0f;
		float last_angle = contactAngle;
		float last_rotation_speed = rotation_speed;
		contactAngle = cangle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = cArea;
		this.punto1=punto1;
		this.punto2=punto2;
		this.punto3=punto3;
		this.punto4=punto4;
		this.punto5=punto5;
		
		
		float da = (this.contactAngle-last_angle)/(2.0f*(float)Math.PI);
		if (da>0.75f) da-=1.0f;
		else if (da<-0.75f) da+=1.0f;
		
		rotation_speed = da/dt;
		rotation_accel = (rotation_speed - last_rotation_speed)/dt;
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}
	
	public void update (TuioTime ttime, float xp, float yp, float cangle, float cw, float ch, float cArea,ArrayList<TuioPoint> points) {
		TuioPoint lastPoint = path.lastElement();
		super.update(ttime,xp,yp);

		TuioTime diffTime = currentTime.subtract(lastPoint.getTuioTime());
		float dt = diffTime.getTotalMilliseconds()/1000.0f;
		float last_angle = contactAngle;
		float last_rotation_speed = rotation_speed;
		contactAngle = cangle;
		contactWidth = cw;
		contactHeight = ch;
		contactArea = cArea;
		puntos=points;
		
		float da = (this.contactAngle-last_angle)/(2.0f*(float)Math.PI);
		if (da>0.75f) da-=1.0f;
		else if (da<-0.75f) da+=1.0f;
		
		rotation_speed = da/dt;
		rotation_accel = (rotation_speed - last_rotation_speed)/dt;
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}
	
	/**
	 * Takes the attributes of the provided TuioBlob 
	 * and assigns these values to this TuioBlob.
	 * The TuioTime time stamp of this TuioContainer remains unchanged.
	 *
	 * @param	tobj	the TuioBlob to assign
	 */	
	public void update (TuioBlob tblb) {
		super.update(tblb);		
		blob_id = tblb.getBlobID();
		contactAngle = tblb.getContactAngle();
		contactWidth = tblb.getContactWidth();
		contactHeight = tblb.getContactHeight();
		contactArea = tblb.getContactArea();
		rotation_speed = tblb.getRotationSpeed();
		rotation_accel = tblb.getRotationAccel();
		if ((rotation_accel!=0) && (state!=TUIO_STOPPED)) state = TUIO_ROTATING;
	}
	
	/**
	 * This method is used to calculate the speed and acceleration values of a
	 * TuioBlob with unchanged position and angle.
	 *
	 * @param	ttime	the TuioTime to assign
	 */
	public void stop (TuioTime ttime) {
		update(ttime,xpos,ypos,contactAngle,contactWidth,contactHeight);
	}

	/**
	 * Returns the blob ID of this TuioBlob.
	 * @return	the blob ID of this TuioBlob
	 */
	public int getBlobID() {
		return blob_id;
	}
	
	/**
	 * Returns the contact angle of this TuioBlob.
	 * @return	the contact angle of this TuioBlob
	 */
	public float getContactAngle() {
		return contactAngle;
	}

	/**
	 * Returns the contact width of this TuioBlob.
	 * @return	the contact width of this TuioBlob
	 */
	public float getContactWidth() {
		return contactWidth;
	}

	/**
	 * Returns the contact height of this TuioBlob.
	 * @return	the contact height of this TuioBlob
	 */
	public float getContactHeight() {
		return contactHeight;
	}
	
	/**
	 * Returns the contact area of this TuioBlob.
	 * @return	the contact area of this TuioBlob
	 */
	public float getContactArea() {
		return contactArea;
	}
	
	/**
	 * Returns the contact angle in degrees of this TuioBlob.
	 * @return	the contact angle in degrees of this TuioBlob
	 */
	public float getContactAngleDegrees() {
		return contactAngle/(float)Math.PI*180.0f;
	}
	
	/**
	 * Returns the rotation speed of this TuioObject.
	 * @return	the rotation speed of this TuioObject
	 */
	public float getRotationSpeed() {
		return rotation_speed;
	}
		
	/**
	 * Returns the rotation acceleration of this TuioObject.
	 * @return	the rotation acceleration of this TuioObject
	 */
	public float getRotationAccel() {
		return rotation_accel;
	}

	/**
	 * Returns true of this TuioObject is moving.
	 * @return	true of this TuioObject is moving
	 */
	public boolean isMoving() { 
		if ((state==TUIO_ACCELERATING) || (state==TUIO_DECELERATING) || (state==TUIO_ROTATING)) return true;
		else return false;
	}

	public ArrayList<TuioPoint> getPuntos(){
		return this.puntos;
		
	}
	
}
