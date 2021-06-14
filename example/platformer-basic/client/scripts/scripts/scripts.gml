function get_network_client(uuid) {
	var inst = noone;
	with (obj_other_player) {
		if (network_id == uuid) {
			inst = id;	
		}
	}
	if (inst == noone) {
		inst = create_network_client(uuid, 0, 0);
	}
	return inst;
}

function create_network_client(uuid, client_x, client_y) {
	var inst = instance_create_layer(client_x, client_y, "Instances", obj_other_player);
	inst.network_id = uuid;
	return inst;
}