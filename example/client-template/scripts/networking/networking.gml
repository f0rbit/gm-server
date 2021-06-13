#macro TCP_PORT 70 // port that the TCP server is ran on
#macro UDP_PORT 71 // port that the UDP server is ran on
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
		default: break;
	}
}