/// @desc Receive Data
switch (async_load[? "type"]) {
	case network_type_data: { // data receieved
		var buffer = async_load[? "buffer"]; // get the buffer
		buffer_seek(buffer, buffer_seek_start, 0); // go to the start of the buffer
		var header = buffer_read(buffer, buffer_string); // get the header (package & class of packet)
		if (ENABLE_LOG) show_debug_message("recieved packet. header:"+string(header)); // log
		if (header == "dev.forbit.server.packets.gson.GSONConnectionPacket") {
			// get the UUID from connection packet
			log_output.log("received connection packet from tcp server"); // log
			var data = buffer_read(buffer, buffer_string);
			log_output.log("data: "+data);
			var map = json_parse(data);
			var client = variable_struct_get(map, "client");
			var uuid = variable_struct_get(client, "id");
			global.uuid = uuid;
			connect_udp(); // connect to udp server after recieving uuid, because we must send it to udp server
			// this is so the UDP server knows which client we are based off of our address
		} else {
			if (room == rm_init) room_goto(rm_main); // leave init room
			var data = buffer_read(buffer, buffer_string);
			handle_packet(header, json_parse(data)); // handle the packet
		}
		break;
	}
	
}