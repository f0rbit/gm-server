#macro TCP_PORT 40938 // port that the TCP server is ran on
#macro UDP_PORT 40939 // port that the UDP server is ran on
#macro ADDRESS "localhost" // address of both servers.
#macro DRAW_OUTPUT true // whether log_output should draw or not
#macro ENABLE_LOG false // whether to print headers of packets to the console.
#macro DRAW_PING true

function handle_packet(header, buffer) {
	switch(header) {
		// handle each case
		// each case should be the package & class name
		// like so:
		case "dev.forbit.server.packets.PingPacket": {
			// simple ping implementation
			var sent_time = buffer_read(buffer, buffer_s32);
			show_debug_message("sent_time: "+string(sent_time));
			if (sent_time > 0) {
				global.ping = global.sent_time-sent_time;
			}
			alarm[0] = room_speed; // wait a second before sending another packet, or else it would spam the server
			break;	
		}
		case "dev.forbit.platform.packets.LocationPacket": {
			var client_id = buffer_read(buffer, buffer_string);
			var client_x = buffer_read(buffer, buffer_f64);
			var client_y = buffer_read(buffer, buffer_f64);
			
			var instance = get_network_client(client_id);
			if (instance == noone) {
				show_debug_message("ERROR");
				return;
			}
			instance.x = client_x;
			instance.y = client_y;			
		}
		default: break;
	}
}

function send_move_packet() {
	var buffer = buffer_create(256, buffer_fixed, 1);
	buffer_write(buffer, buffer_string, "dev.forbit.platform.packets.LocationPacket");
	buffer_write(buffer, buffer_f64, x);
	buffer_write(buffer, buffer_f64, y);
	network_send_raw(global.udp_socket, buffer, buffer_tell(buffer));
}