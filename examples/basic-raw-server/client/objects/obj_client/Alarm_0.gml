/// @desc Send PingPacket to Server
var packet = buffer_create(BUFFER_SIZE, buffer_fixed, 1); // creates a new buffer
buffer_seek(packet, buffer_seek_start,0); // go to start of buffer
buffer_write(packet, buffer_string, PING_PACKET_ID); // class name
buffer_write(packet, buffer_s32, current_time); // time since game begun
buffer_write(packet, buffer_s32, global.ping); // received ping from last time (so server knows our connection)
network_send_raw(global.udp_socket, packet, buffer_tell(packet)); // send to udp server