/// @desc Receive Data
switch (async_load[? "type"]) {
	case network_type_data: { // data receieved
		var buffer = async_load[? "buffer"]; // get the buffer
		buffer_seek(buffer, buffer_seek_start, 0); // go to the start of the buffer
		var header = buffer_read(buffer, buffer_string); // get the header (package & class of packet)
		if (header == CONNECTION_PACKET_ID) {
			// get the UUID from connection packet
			#region gson
			//var data = buffer_read(buffer,buffer_string);
			//var json = json_parse(data);
			//var uuid = json.UUID;
			#endregion
			#region raw
			var uuid = buffer_read(buffer, buffer_string);
			#endregion
			show_debug_message("uuid: "+string(uuid));
			global.uuid = string(uuid); // set global.uuid to recieved uuid
			connect_udp(); // connect to udp server after recieving uuid, because we must send it to udp server
			// this is so the UDP server knows which client we are based off of our address
		} else {
			handle_packet()(header, buffer); // handle the packet
		}
		break;
	}
	
}