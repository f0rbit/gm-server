hspd = 0;
vspd = 0;

function onFloor() {
	return place_meeting(x,y+1,obj_floor);
}

function move() {
	if (place_meeting(x+hspd,y,obj_floor)) {
		while (!place_meeting(x+sign(hspd),y,obj_floor)) x += sign(hspd);
		hspd = 0;
	}
	x += hspd;
	
	if (place_meeting(x,y+vspd,obj_floor)) {
		while (!place_meeting(x,y+sign(vspd),obj_floor)) y += sign(vspd);	
		vspd = 0;
	}
	
	y += vspd;
}

function fall() {
	if (!onFloor()) {
		vspd += 1;
	} else {
		vspd = 0;	
	}
}