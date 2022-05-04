function send_location_packet(x, y) {
	// sends location packet to udp server
	var packet = buffer_create(BUFFER_SIZE, buffer_fixed, 1); // create new buffer
	buffer_seek(packet, buffer_seek_start,0); // go to start of buffer
	buffer_write(packet, buffer_string, LOCATION_PACKET_ID); // class name
	buffer_write(packet, buffer_s32, x); // time since game begun
	buffer_write(packet, buffer_s32, y);
	buffer_write(packet, buffer_string, packet);
	network_send_raw(global.udp_socket, packet, buffer_tell(packet)); // send to udp server
	
}

function get_client_character(client_id) {
	var inst = noone;
	with (obj_network_character) {
		if (network_id == client_id) {
			inst = id;
		}
	}
	if (inst == noone) {
		inst = instance_create_layer(room_width/2, room_height/2, "Instances", obj_network_character);
		inst.network_id = client_id;
	}
	return inst;
}
