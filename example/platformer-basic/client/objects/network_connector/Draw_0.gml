if (DRAW_PING) {
	draw_set_colour(c_white);
	draw_set_halign(fa_right);
	draw_set_valign(fa_bottom);
	draw_text(room_width-4,room_height-4,"ping: "+string(global.ping));
	draw_set_halign(fa_left);
	draw_set_valign(fa_top);
}