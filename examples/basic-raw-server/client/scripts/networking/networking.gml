function connect_tcp() {
	// connect to tcp server first
	global.tcp_socket = network_create_socket(network_socket_tcp);
	global.udp_socket = network_create_socket(network_socket_udp);
	global.uuid = -1;
	global.udp_status = -1;
	global.ping = -2;
	global.tcp_status = network_connect_raw(global.tcp_socket, ADDRESS, TCP_PORT);
	show_debug_message("connected to tcp. status: "+string(global.tcp_status));
	global.ping = -1;
}

function connect_udp() {
	global.udp_status = network_connect_raw(global.udp_socket, ADDRESS, UDP_PORT);
	show_debug_message("connected to udp. status: "+string(global.udp_status));
	var connection_buffer = buffer_create(BUFFER_SIZE, buffer_fixed, 1);
	buffer_write(connection_buffer, buffer_string, REGISTRATION_PACKET_ID);
	buffer_write(connection_buffer, buffer_string, global.uuid);
	network_send_raw(global.udp_socket, connection_buffer, buffer_tell(connection_buffer));
	if (udp_status >= 0) {
		global.connected = true;
		connected();
	}
}

function connected() {
		
	// create player
	instance_create_layer(room_width/2, room_height/2, "Instances", obj_player);
	// begin pinging
	alarm[0] = 1;
}
