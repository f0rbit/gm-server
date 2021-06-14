var move_x = keyboard_check(ord("D"))-keyboard_check(ord("A"));
var move_jump = keyboard_check_pressed(vk_space) || keyboard_check_pressed(ord("W"));

if (move_jump && onFloor()) vspd = -15;
hspd = move_x*4;


var origin_x = x;
var origin_y = y;

move();
fall();

if (origin_x != x || origin_y != y) {
	// move
	send_move_packet();
}