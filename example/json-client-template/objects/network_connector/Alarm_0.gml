/// @desc Ping Packet

// sends ping packet to udp server
var packet = buffer_create(1024, buffer_fixed, 1); // create new buffer
buffer_seek(packet, buffer_seek_start,0); // go to start of buffer
buffer_write(packet, buffer_string, "dev.forbit.server.packets.gson.GSONPingPacket"); // class name
var data = {
	time : current_time,
	lastPing : global.ping
};
buffer_write(packet, buffer_string, json_stringify(data));
global.sent_time = current_time; // keep track of when packet was sent
network_send_raw(global.udp_socket, packet, buffer_tell(packet)); // send to udp server