if (DRAW_OUTPUT) {
	draw_set_colour(c_white);
	draw_set_font(fnt_log);
	draw_set_halign(fa_left);
	draw_set_valign(fa_top);
	for (var i = 0; i < ds_list_size(output); i++) {
		draw_text(4,4+(i*16), output[| i]);	
	}
}