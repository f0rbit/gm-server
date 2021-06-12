/// @desc Ping Packet

// sends ping packet to udp server
var packet = buffer_create(128, buffer_fixed, 1); // create new buffer
buffer_seek(packet, buffer_seek_start,0); // go to start of buffer
buffer_write(packet, buffer_string, "dev.forbit.server.packets.PingPacket"); // class name
buffer_write(packet, buffer_s32, current_time); // time since game begun
global.sent_time = current_time; // keep track of when packet was sent
network_send_raw(global.udp_socket, packet, buffer_tell(packet)); // send to udp server