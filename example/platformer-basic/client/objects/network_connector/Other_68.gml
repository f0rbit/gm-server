/// @desc Receive Data
switch (async_load[? "type"]) {
	case network_type_data: { // data receieved
		var buffer = async_load[? "buffer"]; // get the buffer
		buffer_seek(buffer, buffer_seek_start, 0); // go to the start of the buffer
		var header = buffer_read(buffer, buffer_string); // get the header (package & class of packet)
		if (ENABLE_LOG) show_debug_message("recieved packet. header:"+string(header)); // log
		if (header == "dev.forbit.server.packets.ConnectionPacket") {
			// get the UUID from connection packet
			log_output.log("received connection packet from tcp server"); // log
			var uuid = buffer_read(buffer, buffer_string); // uuid here
			log_output.log("uuid: "+uuid); // log
			global.uuid = string(uuid); // set global.uuid to recieved uuid
			connect_udp(); // connect to udp server after recieving uuid, because we must send it to udp server
			// this is so the UDP server knows which client we are based off of our address
		} else {
			if (room == rm_init) room_goto(rm_main); // leave init room
			handle_packet(header, buffer); // handle the packet
		}
		break;
	}
	
}