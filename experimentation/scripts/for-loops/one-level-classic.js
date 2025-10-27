function main(items) {
	items[items.length-1].id = -100;
	if (!classic(items)) {
		console.log("Warning. Did not find element!");
	}
}
 
function classic(items) {
    let ll = items.length;
    for (let i=0; i<ll; i++) {
        let entry = items[i];
		if (entry.id == -100) {
			return true;
		}
    }
    return false;
}
 