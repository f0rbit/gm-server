draw_self();

if (keyboard_check(vk_f1)) {
	draw_set_valign(fa_bottom);
	draw_set_halign(fa_center);
	draw_text(x+12,y-2,string(uuid));	
	draw_set_halign(fa_left);
	draw_set_valign(fa_top);
}