#macro ADDRESS "localhost"
#macro TCP_PORT 21238
#macro UDP_PORT 22238
#macro BUFFER_SIZE 1024
#macro REGISTRATION_PACKET_ID "dev.forbit.identifier.RegisterPacket"
#macro CONNECTION_PACKET_ID "dev.forbit.server.networks.raw.packets.RawConnectionPacket"
#macro PING_PACKET_ID "dev.forbit.server.networks.raw.packets.RawPingPacket"
#macro LOCATION_PACKET_ID "dev.forbit.networking.packets.LocationPacket"
#macro DISCONNECT_PACKET_ID "dev.forbit.networking.packets.DisconnectPacket"

function init() {
	global.ping = -3;
	global.tcp_status = undefined;
	global.udp_status = undefined;
	global.udp_socket = noone;
	global.tcp_socket = noone;
	global.uuid = -1;
	
	connect_tcp();
}