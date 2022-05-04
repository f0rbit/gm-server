function handle_packet(header, buffer){
	show_debug_message("received packet: "+string(header));
	switch(header) {
		// handle each case
		// each case should be the package & class name
		// like so:
		case PING_PACKET_ID: {
			// simple ping implementation
			var sent_time = buffer_read(buffer, buffer_s32);
			//var data = json_parse(buffer_read(buffer, buffer_string));
			//var sent_time = data.receivedTime;
			//show_debug_message("sent_time: "+string(sent_time)+" global.sent_time: "+string(global.sent_time));
			if (sent_time > 0) {
				global.ping = current_time-sent_time;
			}
			alarm[0] = room_speed; // wait a second before sending another packet, or else it would spam the server
			break;	
		}
		case LOCATION_PACKET_ID: {
			// location packet
			var client_x = buffer_read(buffer, buffer_s32);
			var client_y = buffer_read(buffer, buffer_s32);
			var client_id = buffer_read(buffer, buffer_string);
			var inst = get_client_character(client_id);
			inst.x = client_x;
			inst.y = client_y;	
			break;
		}
		case DISCONNECT_PACKET_ID: {
			var client_id = buffer_read(buffer,buffer_string);
			var inst = get_client_character(client_id);
			if (inst > noone) {
				instance_destroy(inst);
			}
			break;
		}
		default: break;
	}
}