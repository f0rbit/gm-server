// movement code
var move_x = keyboard_check(ord("D")) - keyboard_check(ord("A"));
var move_y = keyboard_check(ord("S")) - keyboard_check(ord("W"));
var move_speed = 4;
x += move_x * move_speed;
y += move_y * move_speed;

x = clamp(x,0,room_width);
y = clamp(y,0,room_height);


if (move_x != 0 || move_y != 0) {
	// send move packet
	send_location_packet(x,y);
}
