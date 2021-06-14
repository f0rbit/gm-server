global.tcp_socket = network_create_socket(network_socket_tcp);
global.udp_socket = network_create_socket(network_socket_udp);

global.uuid = -1;
global.udp_status = -1;
global.tcp_status = network_connect_raw(global.tcp_socket, ADDRESS, TCP_PORT);
log_output.log("connected to tcp. status: "+string(global.tcp_status));

global.ping = -1;
global.sent_time = -1;



function connect_udp() {
	// connect to the udp server
	// called after successfully connecting to the TCP server
	// at this point, tcp_status should be > 0, and uuid should not be -1.
	global.udp_status = network_connect_raw(global.udp_socket, ADDRESS, UDP_PORT); // connect to tcp server
	var connection_buffer = buffer_create(128, buffer_fixed, 1); // create new buffer
	buffer_write(connection_buffer, buffer_string, "dev.forbit.server.packets.RegisterPacket"); // RegisterPacket
	buffer_write(connection_buffer, buffer_string, global.uuid); // put in our uuid receieved from TCP server.
	network_send_raw(global.udp_socket, connection_buffer, buffer_tell(connection_buffer)); // send it to the udp server.
	log_output.log("connected to udp. status: "+string(global.udp_status)); // log
}